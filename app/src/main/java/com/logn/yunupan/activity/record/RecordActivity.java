package com.logn.yunupan.activity.record;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.logn.yunupan.R;
import com.logn.yunupan.activity.Index.IndexActivity;
import com.logn.yunupan.activity.scan.ScanQRActivity;
import com.logn.yunupan.adapter.RecordListAdapter;
import com.logn.yunupan.adapter.SimpleAdapter;
import com.logn.yunupan.adapter.listener.OnRecyclerViewItemClickListener;
import com.logn.yunupan.adapter.listener.OnRecyclerViewItemLongClickListener;
import com.logn.yunupan.entity.greendao.FileDownloadBean;
import com.logn.yunupan.entity.greendao.FileInfoBean;
import com.logn.yunupan.entity.greendao.FileUploadBean;
import com.logn.yunupan.utils.FileTypeUtils;
import com.logn.yunupan.utils.ScanMsgUtil;
import com.logn.yunupan.utils.SimpleItemDecoration;
import com.logn.yunupan.utils.StatusBarUtil;
import com.logn.yunupan.utils.ToastShort;
import com.logn.yunupan.utils.clipboard.ClipboardManagerCompat;
import com.logn.yunupan.utils.eventbus.EventBusInstance;
import com.logn.yunupan.utils.eventbus.EventDelRecord;
import com.logn.yunupan.utils.eventbus.EventFailedToDownload;
import com.logn.yunupan.utils.eventbus.EventFailedToUpload;
import com.logn.yunupan.utils.eventbus.EventProgressD;
import com.logn.yunupan.utils.eventbus.EventProgressU;
import com.logn.yunupan.utils.eventbus.EventToastInfo;
import com.logn.yunupan.utils.greendao.BeanTypeUtils;
import com.logn.yunupan.utils.greendao.DBManagerD;
import com.logn.yunupan.utils.greendao.DBManagerR;
import com.logn.yunupan.utils.greendao.DBManagerU;
import com.logn.yunupan.utils.logger.Logger;
import com.logn.yunupan.views.DialogFileOperation;
import com.logn.yunupan.views.QRCodeView.QRCodeView;
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

import static com.logn.yunupan.views.DialogFileOperation.DIALOG_COPY;
import static com.logn.yunupan.views.DialogFileOperation.DIALOG_DELETE_RECORD;
import static com.logn.yunupan.views.DialogFileOperation.DIALOG_FILE_INFO;
import static com.logn.yunupan.views.DialogFileOperation.DIALOG_QR;

/**
 * Created by OurEDA on 2017/3/18.
 */

public class RecordActivity extends FragmentActivity implements RecordContract.View {

    @BindView(R.id.record_title_bar)
    TitleBar titleBar;
    @BindView(R.id.record_show_files)
    RecyclerView recyclerView;

    private RecordListAdapter adapter;

    private Context context = RecordActivity.this;
    private List<String> dialogInfo;

    private List<FileInfoBean> beanList;
    private DBManagerR dbr;
    private DBManagerD dbd;
    private DBManagerU dbu;

    /**
     * 点击或长按之后存储的那个条目的文件信息
     */
    private static FileInfoBean selectFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.status_bar_color));
        EventBusInstance.getBusInstance().register(this);
        ToastShort.init(this.context);

        //Logger.e("准备开启服务");
        //startYunUPanService();

        ButterKnife.bind(this);
        dbr = DBManagerR.getInstance(RecordActivity.this.getApplicationContext());
        dbd = DBManagerD.getInstance(RecordActivity.this.getApplicationContext());
        dbu = DBManagerU.getInstance(RecordActivity.this.getApplicationContext());

        //findAllRecord();

        initView();
        showData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusInstance.getBusInstance().unregister(this);
    }

    private void startYunUPanService() {
//        Intent intent = new Intent(getApplicationContext(), YunUPanService.class);
//        startService(intent);
    }

    /**
     * 遍历数据库，将还没收入到记录数据库的记录加载进去
     */
    private void findAllRecord() {
        List<FileInfoBean> rBeans = dbr.queryAll();
        List<FileUploadBean> uBeans = dbu.queryAll();
        boolean found;
        //查找上传记录库
        for (FileUploadBean bean : uBeans) {
            found = false;
            for (FileInfoBean b : rBeans) {
                Log.e("查找过程：", "\n---------------------------\n" + bean.getFile_name() + ":" + bean.getFile_MD5() +
                        "\n" + b.getFile_name() + ":" + b.getFile_MD5() + "\n-----------------------------\n");
                if (b.getFile_id() == bean.getFile_id()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                dbr.insertFileInfo(BeanTypeUtils.getFromFU(bean));
            }
        }
    }


    private void testUpdate() {
        FileInfoBean beans = dbr.queryWithCode("rpp8").get(0);
        beans.setFile_code("asdf");
        beans.setFile_name("123456");
        beans.setFile_size(2048);
        dbr.update(beans);
        loadDatas();
    }

    private void showData() {
        loadDatas();
    }

    public void loadDatas() {
        beanList = dbr.queryAll();
        if (beanList.size() > 0) {
            for (FileInfoBean bean : beanList) {
                adapter.add(bean);
            }
        }
    }

    private void initView() {
        dialogInfo = new ArrayList<>();
        titleBar.setOnTitleClickListener(new TitleBar.OnTitleClickListener() {
            @Override
            public void onLeftClick() {
                Logger.e("qr_test");
                Intent intent = new Intent(RecordActivity.this, ScanQRActivity.class);
                startActivity(intent);
            }

            @Override
            public void onRightClick() {
                Intent intent = new Intent(RecordActivity.this, IndexActivity.class);
                startActivity(intent);
            }

            @Override
            public void onTitleClick() {

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new SimpleItemDecoration(context, LinearLayout.VERTICAL));
        adapter = new RecordListAdapter(context);
        adapter.setOnRecyclerViewItemClickListener(listener);
        adapter.setOnRecyclerViewItemLongClickListener(longClickListener);

        recyclerView.setAdapter(adapter);
    }

    private OnRecyclerViewItemLongClickListener longClickListener = new OnRecyclerViewItemLongClickListener() {
        @Override
        public boolean onLongClick(View v, int position) {
            DialogFileOperation dialogFileOperation = DialogFileOperation.createDialog(RecordActivity.this);
            dialogFileOperation.setListener(onItemClickListener);
            dialogFileOperation.show();
            return true;
        }
    };

    private OnRecyclerViewItemClickListener listener = new OnRecyclerViewItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

            selectFile = beanList.get(position);
            String path = selectFile.getFile_path();
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
                    Toast.makeText(RecordActivity.this, "" + getContext().getResources().getString(R.string.app_un_support), Toast.LENGTH_SHORT).show();
                }

            } else {
                ToastShort.show(context, "文件不存在");
            }
        }
    };

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
            String text = ((SimpleAdapter.ViewHolder) view.getTag()).textView.getText().toString();
            Logger.e("fuck");
            switch (text) {
                case DIALOG_FILE_INFO:
                    if (selectFile != null) {
                        String info = "文件信息\n\n文件名：" + selectFile.getFile_name() +
                                "\n\n文件路径：" + selectFile.getFile_path() +
                                "\n\n提取码：" + selectFile.getFile_code();
                        EventBusInstance.getBusInstance().post(new EventToastInfo(info));
                    }
                    break;
                case DIALOG_COPY:
                    if (selectFile != null) {
                        ClipboardManagerCompat clipboardManagerCompat = ClipboardManagerCompat.create(context);
                        clipboardManagerCompat.addText(selectFile.getFile_code());
                        EventBusInstance.getBusInstance().post(new EventToastInfo("提取码已复制到剪切板"));
                    }
                    break;
                case DIALOG_QR:
                    EventBusInstance.getBusInstance().post(new EventToastInfo("生成二维码"));
                    if (selectFile != null) {
                        QRCodeView qrCodeView = new QRCodeView();
                        qrCodeView.setType(ScanMsgUtil.ScanType.CLASS);
                        qrCodeView.setData("" + selectFile.getFile_code());
                        qrCodeView.show(getSupportFragmentManager(), "");
                    }
                    break;
                case DIALOG_DELETE_RECORD:
                    //发送通知，删除记录
                    EventBusInstance.getBusInstance().post(new EventDelRecord(selectFile));
                    break;
                default:
            }
            dialog.dismiss();
        }
    };

    private void dialogJustShow(List<String> list) {
        Logger.e("有没有？" + list.size());
        SimpleAdapter adapter = new SimpleAdapter(context, true);
        adapter.setList(list);
        DialogPlus dialogPlus = DialogPlus.newDialog(context)
                .setAdapter(adapter)
                .setExpanded(false)
                .setGravity(Gravity.CENTER)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        EventBusInstance.getBusInstance().post("posi:" + position);
                    }
                })
                .create();
        dialogPlus.show();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    @Override
    public void setPresenter(RecordContract.Presenter presenter) {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void delRecord(EventDelRecord delRecord) {
        FileInfoBean bean = delRecord.getBean();
        dbr.deleteFileInfo(bean);
        beanList.remove(bean);
        adapter.del(bean);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 下载区
    ///////////////////////////////////////////////////////////////////////////

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateProgress(String totalBytes) {
        Log.e("ERdownload:->", "" + totalBytes);
    }

    /**
     * 更新下载进度
     *
     * @param eventD
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateProgress(EventProgressD eventD) {

        String flag = "position->" + eventD.getPosition() + "  hasdone->" + eventD.isDone() + "  len->" + eventD.getContentLength() + "  progress->" + eventD.getBean().getDownload_progress();
        Log.e("下载信息-》", "" + flag);
        FileDownloadBean bean = null;

        List<FileDownloadBean> beans = dbd.queryWithCode(eventD.getBean().getFile_code());
        if (beans.size() > 0) {
            bean = eventD.getBean();
            //使用数据库的id，保证id不变。
            bean.setFile_id(beans.get(0).getFile_id());
            dbd.update(eventD.getBean());
        } else {    //如果未查找到数据，插入数据库
            dbd.insertFileInfo(eventD.getBean());
        }

        //如果下载完成，存储到record数据库
        if (eventD.isDone()) {
            if (dbr.queryWithCode(eventD.getBean().getFile_code()).size() <= 0 && bean != null) {
                FileInfoBean b = BeanTypeUtils.getFromFD(bean);
                dbr.insertFileInfo(b);
                //并更新此界面。
                if (adapter != null) {
                    adapter.add(b);
                    beanList.add(b);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void failedToDownload(EventFailedToDownload eventFailedToDownload) {
        dbd.deleteFileInfoWithCode(eventFailedToDownload.getCode());
        ToastShort.show(context, "不存在提取码：" + eventFailedToDownload.getCode());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void toastInfo(EventToastInfo info) {
        ToastShort.showL((info.getInfo() == null ? "未知信息" : info.getInfo()));
    }


    ///////////////////////////////////////////////////////////////////////////
    // 上传区
    ///////////////////////////////////////////////////////////////////////////
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateProgressU(EventProgressU eventU) {
        FileUploadBean bean = null;
        List<FileUploadBean> beans = dbu.queryWithMD5(eventU.getBean().getFile_MD5());
        if (beans.size() > 0) {
            bean = eventU.getBean();

            bean.setFile_id(beans.get(0).getFile_id());
            dbu.update(bean);
        } else {
            dbu.insertFileInfo(eventU.getBean());
        }

        //如果下载完成，存储到record数据库
        if (eventU.isDone()) {
            if (dbr.queryWithCode(eventU.getBean().getFile_code()).size() <= 0 && bean != null && bean.getFile_code() != null) {
                Logger.e("显示code：" + bean.getFile_code());
                FileInfoBean b = BeanTypeUtils.getFromFU(bean);
                dbr.insertFileInfo(b);
                //并更新此界面。
                if (adapter != null) {
                    adapter.add(b);
                    beanList.add(b);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void failedToDownload(EventFailedToUpload eventFailedToUpload) {
        //根据MD5删除上传数据。
        if (eventFailedToUpload.getMD5() == null) {
            return;
        }
        dbu.deleteFileInfoWithMD5(eventFailedToUpload.getMD5());
        //ToastShort.show(context, "不存在MD5：" + eventFailedToUpload.getMD5());

    }

}
