package com.logn.yunupan.fragment.download;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.easybar.OnImageCircleButtonClickedListener;
import com.example.easybar.RoundRectButton;
import com.logn.yunupan.R;
import com.logn.yunupan.activity.record.RecordActivity;
import com.logn.yunupan.adapter.DownloadListAdapter;
import com.logn.yunupan.adapter.SimpleAdapter;
import com.logn.yunupan.entity.greendao.FileDownloadBean;
import com.logn.yunupan.entity.greendao.FileInfoBean;
import com.logn.yunupan.utils.FileTypeUtils;
import com.logn.yunupan.utils.SimpleItemDecoration;
import com.logn.yunupan.utils.ToastShort;
import com.logn.yunupan.utils.eventbus.EventBusInstance;
import com.logn.yunupan.utils.eventbus.EventDialogForFile;
import com.logn.yunupan.utils.eventbus.EventFailedToDownload;
import com.logn.yunupan.utils.eventbus.EventProgressD;
import com.logn.yunupan.utils.eventbus.EventToastInfo;
import com.logn.yunupan.utils.greendao.DBManagerD;
import com.logn.yunupan.utils.logger.Logger;
import com.logn.yunupan.views.EditTextWithDivLine;
import com.logn.yunupan.views.TitleBarView.TitleBar;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by OurEDA on 2017/3/21.
 */

public class DownloadFragment extends Fragment implements DownloadContract.View, OnImageCircleButtonClickedListener {

    private Context context;
    private List<FileDownloadBean> items;
    private DBManagerD dbd;
    /**
     * 判断输入的提取码是否是四位
     */
    private boolean hasCode = false;

    @BindView(R.id.download_title_bar)
    TitleBar titleBar;
    @BindView(R.id.download_edit_get_code)
    EditTextWithDivLine codeEdit;
    @BindView(R.id.download_btn_ok)
    RoundRectButton btnOK;
    @BindView(R.id.download_show_files)
    RecyclerView downloadList;

    private DownloadListAdapter adapter;

    private DownloadContract.Presenter mPresemter;


    public static DownloadFragment newInstance() {
        DownloadFragment downloadFragment = new DownloadFragment();
        Bundle bundle = new Bundle();
        downloadFragment.setArguments(bundle);
        return downloadFragment;
    }

    public DownloadFragment() {
        DownloadPresenter presenter = new DownloadPresenter(this);
        setPresenter(presenter);
        items = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View wrapper = inflater.inflate(R.layout.fragment_download, container, false);
        ButterKnife.bind(this, wrapper);
        EventBusInstance.getBusInstance().register(this);

        context = wrapper.getContext();
        dbd = DBManagerD.getInstance(context.getApplicationContext());


        checkDatesOutOfTime();

        initView();

        initItemData();

        return wrapper;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusInstance.getBusInstance().unregister(this);
    }


    private void initItemData() {
        items = dbd.queryAll();
        for (FileDownloadBean bean : items) {
            adapter.add(bean);
        }
    }

    /**
     * 检查数据库，将下载时间超过24小时的记录删除
     */
    private void checkDatesOutOfTime() {
        List<FileDownloadBean> beans = dbd.queryAll();
        long timeNow = System.currentTimeMillis();
        for (FileDownloadBean bean : beans) {
            if ((timeNow - bean.getTime()) > (1000 * 60 * 60 * 24)) {    //out of one day
                dbd.deleteFileInfo(bean);
            }
        }
    }

    private void initView() {
        titleBar.setOnTitleClickListener(tListener);
        //btnOK.setOnClickListener(listener);
        btnOK.setOnImageCircleButtonClickedListener(this);
        //codeEdit.addTextChangedListener(watcher);
        codeEdit.initStyle(R.drawable.code_input_type, 4, 1, R.color.gray, R.color.blue, 24);
        codeEdit.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        codeEdit.setOnTextFinishListener(new EditTextWithDivLine.OnTextFinishListener() {
            @Override
            public void onFinish(String str) {
                hasCode = true;
            }
        });

        adapter = new DownloadListAdapter(context);
        downloadList.setLayoutManager(new LinearLayoutManager(context));
        downloadList.addItemDecoration(new SimpleItemDecoration(context, LinearLayoutManager.VERTICAL));
        downloadList.setAdapter(adapter);
    }

    private TitleBar.OnTitleClickListener tListener = new TitleBar.OnTitleClickListener() {
        @Override
        public void onLeftClick() {
            getActivity().finish();
        }

        @Override
        public void onRightClick() {

        }

        @Override
        public void onTitleClick() {

        }
    };

    /**
     * download button click listener,when {@link #hasCode } is true,start to check and download
     */
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (hasCode) {
                // start to prepare for download.
                String code = codeEdit.getPwdText();
                mPresemter.start2Prepare(code);
            } else {
                Toast.makeText(context, "提取码为 4 位数", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            //限制输入位数最大为4位
            if (s.length() > 4) {
                s.delete(4, s.length());
                hasCode = true;
            } else if (s.length() == 4) {
                hasCode = true;
            } else {
                hasCode = false;
            }
//            ToastShort.show(context, "size:\t" + s.length() + "\ncode?\t" + hasCode);
        }
    };

    @Override
    public void setPresenter(DownloadContract.Presenter presenter) {
        this.mPresemter = presenter;
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Override
    public int getPosition(String md5) {
        dbd = DBManagerD.getInstance(context.getApplicationContext());
        items = dbd.queryAll();

        int position = items.size();
        for (FileDownloadBean bean : items) {
            position--;
            if (bean.getFile_MD5().equals(md5)) {
                return position;
            }
        }
        return -1;
    }

    @Override
    public void addItemData(FileDownloadBean bean) {
        adapter.add(bean);
        downloadList.scrollToPosition(0);
    }

    @Override
    public void updateItem(int position, FileDownloadBean bean) {
        dbd.update(bean);
        items.set(position, bean);
        adapter.setItems(position, bean);
        adapter.notifyItemChanged(0);
    }


    ///////////////////////////////////////////////////////////////////////////
    // 监听下载区
    ///////////////////////////////////////////////////////////////////////////

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void downloadProgress(EventProgressD eventD) {

        List<FileDownloadBean> beans = dbd.queryWithCode(eventD.getBean().getFile_code());
        if (beans.size() > 0) {
            //通知recycler view 改变状态。
            adapter.setProgress(eventD);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void failedToDownload(EventFailedToDownload eventFailedToDownload) {
        adapter.moveItem(eventFailedToDownload.getCode());
    }


    @Override
    public void onClick(View view) {
        if (hasCode) {
            // start to prepare for download.
            String code = codeEdit.getPwdText();
            mPresemter.start2Prepare(code);
        } else {
            Toast.makeText(context, "提取码为 4 位数", Toast.LENGTH_SHORT).show();
        }
    }


    @Subscribe
    public void dialogForFileOpen(EventDialogForFile eventDialogForFile) {
        final FileInfoBean bean = eventDialogForFile.getBean();
        if (bean != null) {
            SimpleAdapter adapter = new SimpleAdapter(getActivity(), true);
            List<String> list = new ArrayList<>();
            list.add("文件已下载");
            list.add("文件名：" + bean.getFile_name());
            list.add("打开文件");
            list.add("取消");
            adapter.setList(list);
            DialogPlus dialogPlus = DialogPlus.newDialog(getActivity())
                    .setAdapter(adapter)
                    .setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                            switch (position) {
                                case 0:
                                case 1:
                                    break;
                                case 2:
                                    String path = bean.getFile_path();
                                    File file = null;
                                    if (path != "") {
                                        file = new File(path);
                                    }
                                    if (file != null && file.isFile()) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        Logger.e("获得的文件超链接：" + FileTypeUtils.getMIMEType(file));
                                        intent.setDataAndType(Uri.parse("file://" + path), FileTypeUtils.getMIMEType(file));
                                        // start activity
                                        try {
                                            startActivity(intent);
                                        } catch (ActivityNotFoundException e) {
                                            Toast.makeText(getActivity(), "" + getContext().getResources().getString(R.string.app_un_support), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        ToastShort.show(context, "文件不存在");
                                    }
                                    break;
                                default:
                                    dialog.dismiss();
                            }
                        }
                    })
                    .setGravity(Gravity.CENTER)
                    .setExpanded(false)
                    .create();
            dialogPlus.show();
        }
    }
}
