package com.logn.yunupan.fragment.upload;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.logn.yunupan.R;
import com.logn.yunupan.activity.download.DownloadActivity;
import com.logn.yunupan.adapter.UploadListAdapter;
import com.logn.yunupan.adapter.listener.OnRecyclerViewItemClickListener;
import com.logn.yunupan.entity.greendao.FileUploadBean;
import com.logn.yunupan.utils.FileGetPathUtil;
import com.logn.yunupan.utils.SimpleItemDecoration;
import com.logn.yunupan.utils.ToastShort;
import com.logn.yunupan.utils.eventbus.EventBusInstance;
import com.logn.yunupan.utils.eventbus.EventFailedToUpload;
import com.logn.yunupan.utils.eventbus.EventGetFilePath;
import com.logn.yunupan.utils.eventbus.EventProgressU;
import com.logn.yunupan.utils.file_manager.FileManager;
import com.logn.yunupan.utils.greendao.DBManagerU;
import com.logn.yunupan.utils.logger.Logger;
import com.logn.yunupan.views.TitleBarView.TitleBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by OurEDA on 2017/3/20.
 */

public class UploadFragment extends Fragment implements UploadContract.View, OnRecyclerViewItemClickListener {
    private static final String TAG = "UploadFragment";

    private Context context;
    private List<FileUploadBean> items;
    private DBManagerU dbu;

    @BindView(R.id.upload_title_bar)
    TitleBar titleBar;
    @BindView(R.id.upload_show_files)
    RecyclerView uploadList;
    @BindView(R.id.upload_btn_choose_file)
    Button btnChooseFile;

    private UploadListAdapter adapter;

    private UploadContract.Presenter mPresenter;

    public static UploadFragment newInstance() {
        UploadFragment uploadFragment = new UploadFragment();
        Bundle bundle = new Bundle();
        uploadFragment.setArguments(bundle);
        return uploadFragment;
    }

    public UploadFragment() {
        UploadPresenter presenter = new UploadPresenter(this);
        setPresenter(presenter);
        items = new ArrayList<>();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View wrapper = inflater.inflate(R.layout.fragment_upload, container, false);
        ButterKnife.bind(this, wrapper);
        EventBusInstance.getBusInstance().register(this);

        context = wrapper.getContext();
        dbu = DBManagerU.getInstance(context.getApplicationContext());

        checkDatesOutOfTime();

        initView();
        //从数据库读取数据并显示在界面上。
        initItemsData();
        Logger.e("加载数据后。。。");

        return wrapper;
    }

    private void checkDatesOutOfTime() {
        List<FileUploadBean> beans = dbu.queryAll();
        long timeNow = System.currentTimeMillis();
        for (FileUploadBean bean : beans) {
            if ((timeNow - bean.getTime()) > (1000 * 60 * 60 * 24)) {    //out of one day
                dbu.deleteFileInfo(bean);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBusInstance.getBusInstance().unregister(this);
    }

    private void initView() {
        titleBar.setOnTitleClickListener(listener);
        btnChooseFile.setOnClickListener(choosBtnListener);

        adapter = new UploadListAdapter(context);
        uploadList.setLayoutManager(new LinearLayoutManager(context));
        uploadList.addItemDecoration(new SimpleItemDecoration(context, LinearLayoutManager.VERTICAL));
        uploadList.setAdapter(adapter);

    }


    private TitleBar.OnTitleClickListener listener = new TitleBar.OnTitleClickListener() {
        @Override
        public void onLeftClick() {
            getActivity().finish();
        }

        @Override
        public void onRightClick() {
            Intent intent = new Intent();
            intent.setClass(context, DownloadActivity.class);
            startActivity(intent);
        }

        @Override
        public void onTitleClick() {

        }
    };

    private View.OnClickListener choosBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //start2GetFile("*/*");
            Intent intent = new Intent(UploadFragment.this.getActivity(), FileManager.class);
            startActivity(intent);
        }
    };

    @Override
    public void setPresenter(UploadContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK) {
            //查看返回的数据
            Logger.e("读取文件返回的数据：\nrequestCode:\t" + requestCode + "\nresultCode:\t" + resultCode + "\ndata:\t" + data.toString());
            Uri uri = data.getData();
            String path = FileGetPathUtil.getPathByUri4kitkat(context, uri);
            Logger.e("get_path:" + path);
            File file = new File(path);
            if (file != null && file.isFile()) {
                ToastShort.show(context, file.getName() + "\n" + path);
                //开始准备上传，并将文件数据
                // （文件名，文件类型-图标，大小，时间（上传开始时间），文件路径）
                // 实时加入数据库并显示在界面上，
                mPresenter.start2Prepare(path);
            }
        }
    }


    @Override
    public void addItemData(FileUploadBean bean) {
        adapter.add(bean);
        uploadList.scrollToPosition(0);
    }

    @Override
    public void updateItem(int position, FileUploadBean bean) {
        //更新数据库
        dbu.update(bean);//根据主键来更新
        // 更新items。
        items.set(position, bean);//????????????
        adapter.setItems(position, bean);
        adapter.notifyItemChanged(position);
    }

    private void initItemsData() {
        items = dbu.queryAll();
        for (FileUploadBean bean : items) {
            adapter.add(bean);
            Log.e(TAG, "加载的时候文件名：" + bean.getFile_name());
        }
    }

    /*
     * 获取上传文件
     */
    private void start2GetFile(String type) {
        //type   "*/*"  全类型
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(type);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
    }

    @Override
    public int getPosition(String md5) {
        dbu = DBManagerU.getInstance(context.getApplicationContext());
        items = dbu.queryAll();

        int position = items.size();
        for (FileUploadBean bean : items) {
            position--;
            if (bean.getFile_MD5().equals(md5)) {
                return position;
            }
        }
        return -1;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 上传区
    ///////////////////////////////////////////////////////////////////////////

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void uploadProgress(EventProgressU eventU) {
        adapter.setProgress(eventU);
        Log.e("UF","progress: " + eventU.getBean().getUpload_progress() + "\tbytes:"+eventU.getBytesWritten()
        +"\tlen:"+eventU.getContentLength());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateProgress(EventFailedToUpload eventFailedToUpload) {
        adapter.moveItem(eventFailedToUpload.getMD5(), eventFailedToUpload.getPath());
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getFilePath(EventGetFilePath path) {
        mPresenter.start2Prepare(path.getPath());
    }

}
