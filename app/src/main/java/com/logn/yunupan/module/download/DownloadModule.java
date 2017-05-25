package com.logn.yunupan.module.download;

import android.content.Context;

import com.logn.yunupan.entity.greendao.FileDownloadBean;
import com.logn.yunupan.entity.greendao.FileInfoBean;
import com.logn.yunupan.utils.ToastShort;
import com.logn.yunupan.utils.eventbus.EventBusInstance;
import com.logn.yunupan.utils.eventbus.EventDialogForFile;
import com.logn.yunupan.utils.eventbus.EventToastInfo;
import com.logn.yunupan.utils.greendao.DBManagerD;
import com.logn.yunupan.utils.greendao.DBManagerR;
import com.logn.yunupan.utils.greendao.DBManagerU;
import com.logn.yunupan.utils.logger.Logger;
import com.logn.yunupan.utils.rxjava.SchedulersCompat;

import java.io.File;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by OurEDA on 2017/3/31.
 */

public class DownloadModule {
    private Context context;
    private DBManagerD dbd;

    private DBManagerR dbr;

    public DownloadModule(Context context) {
        this.context = context;
        dbd = DBManagerD.getInstance(context);
        dbr = DBManagerR.getInstance(context);
    }

    /**
     * 检查是否需要下载
     */
    public void checkForDownload(String code) {
        if (code.length() != 4) {
            Logger.e("提取码长度错误：->> " + code.length());
            return;
        }
        if (findFileWithCode(code)) {
            Logger.e("文件已存在：" + code);
        } else {
            // 未找到记录，开始下载。
            // 加入数据库，实时更新数据库
            // 如果下载失败则删除数据库
            FileDownloadBean bean = new FileDownloadBean();
            bean.setFile_id(System.currentTimeMillis());
            bean.setFile_code(code);
            dbd.insertFileInfo(bean);
            downloadFile(code);
        }

    }

    private void downloadFile(String code) {
        Observable.just(code)
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return new FileDownload().downWithCode(s);
                    }
                }).compose(SchedulersCompat.<String>applyIoSchedulers())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Logger.e("完成：download.onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String statusOrName) {
                        //  -1:  提取码不存在
                        //  0：  下载成功

                        if (statusOrName.equals("-1")) {
                            ToastShort.show(context, "提取码不存在");
                            Logger.e("提取码不存在");
                        } else if (statusOrName.equals("0")) {
                            ToastShort.show(context, "下载成功");
                            Logger.e("下载成功");
                        }
                        Logger.e("结束后的状态：" + statusOrName);
                    }
                });
    }

    public boolean findFileWithCode(String code) {
        //只检查record数据库
        if(code==null||code==""){
            return false;
        }
        List<FileInfoBean> beans = dbr.queryWithCode(code);
        if (beans.size() > 0) {
            for (FileInfoBean bean : beans) {
                String path = bean.getFile_path();
                if (path != null && !path.isEmpty()) {
                    File file = new File(path);
                    if (file.isFile()) {
                        //通知生成对话框。
                        EventBusInstance.getBusInstance().post(new EventDialogForFile(bean));
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
