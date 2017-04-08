package com.logn.yunupan.module.upload;

import android.content.Context;
import android.util.Log;

import com.logn.yunupan.entity.greendao.FileInfoBean;
import com.logn.yunupan.entity.greendao.FileUploadBean;
import com.logn.yunupan.utils.MD5;
import com.logn.yunupan.utils.Retrofit.ProgressListener;
import com.logn.yunupan.utils.ToastShort;
import com.logn.yunupan.utils.WIFIWithDLUTUtil;
import com.logn.yunupan.utils.eventbus.EventBusInstance;
import com.logn.yunupan.utils.eventbus.EventFailedToUpload;
import com.logn.yunupan.utils.eventbus.EventProgressU;
import com.logn.yunupan.utils.eventbus.EventToastInfo;
import com.logn.yunupan.utils.greendao.DBManagerR;
import com.logn.yunupan.utils.logger.Logger;
import com.logn.yunupan.utils.rxjava.SchedulersCompat;
import com.logn.yunupan.utils.rxjava.YunUPanService;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Stack;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by OurEDA on 2017/4/2.
 */

public class UploadModule {
    private static final String TAG = "UploadModule";

    private Context context;
    DBManagerR dbr;

    private FileUploadBean bean;

    public UploadModule(Context context) {
        this.context = context;
        dbr = DBManagerR.getInstance(context);
    }


    public void checkForUpload(String path) {
        if (path == "") {
            return;
        }
        Logger.e("上传前，路径：" + path);
        if (findRecordWithPath(path)) {
            Logger.e("文件已上传：");
            return;
        }
        //文件开始上传
        //加入数据库，实时更新
        //如果下载失败则删除记录
        File file = new File(path);
        String md5 = MD5.getFileMD5(file);

        bean = new FileUploadBean();
        bean.setFile_id(System.currentTimeMillis());
        bean.setFile_path(path);
        bean.setFile_MD5(md5);
        bean.setFile_exist(true);
        bean.setUpload_isUploading(true);
        bean.setFile_name(file.getName());
        bean.setUpload_progress(0);
        bean.setFile_size(file.length());
        bean.setUpload_hasDown(false);
        bean.setTime(System.currentTimeMillis());
        ////////////////////////////////
        EventProgressU eventU = new EventProgressU();
        eventU.setBean(bean);
        eventU.setPosition(-1);
        eventU.setDone(false);
        EventBusInstance.getBusInstance().post(eventU);
        //开始上传
        getFileForUpload(path);
    }


    private void getFileForUpload(String path) {

        Observable.just(path)
                .map(new Func1<String, File>() {
                    @Override
                    public File call(String s) {
                        File file = new File(s);
                        if (!WIFIWithDLUTUtil.connected()) {
                            EventBusInstance.getBusInstance().post(new EventToastInfo("请检查网络，请连接校园网"));
                            return null;
                        }
                        if (!file.exists()) {
                            EventBusInstance.getBusInstance().post(new EventToastInfo("文件获取失败"));
                            return null;
                        }
                        Logger.e(TAG + "woc_" + file.getName());
                        return file;
                    }
                })
                .compose(SchedulersCompat.<File>applyIoSchedulers())
                .subscribe(new Subscriber<File>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Logger.e("fuck_" + e.toString());
                    }

                    @Override
                    public void onNext(File file) {
                        if (file != null) {
                            //updateForXML(file);
                            //在record页面显示上传进度。
                            uploadFile(file);
                        } else {
                            EventFailedToUpload eventFailedToUpload = new EventFailedToUpload(bean.getFile_MD5(), bean.getFile_path());
                            EventBusInstance.getBusInstance().post(eventFailedToUpload);
                        }
                    }

                });
    }


    private void uploadFile(File file) {

        Observable.just(file)
                .flatMap(new Func1<File, Observable<ResponseBody>>() {
                    @Override
                    public Observable<ResponseBody> call(File file) {
                        Logger.e(file.getName() + "\tstart_time: " + System.currentTimeMillis());
                        return YunUPanService.uploadFileService(file, uploadListener);
                    }
                })
                .compose(SchedulersCompat.<ResponseBody>applyIoSchedulers())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Logger.e(TAG + ": error~:" + e.toString());
                        //ToastShort.show(mView.getContext(), "请使用校园网\n错误信息：" + e.toString());
                        EventBusInstance.getBusInstance().post(new EventToastInfo("请使用校园网\n错误信息：" + e.toString()));
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String result = null;   //存储返回的提取码
                        try {
                            result = responseBody.string();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Logger.e(TAG + ":XML:error");
                        }
                        Logger.e(TAG + ":XML:code__" + result);
                        Logger.e(result + "end_time: " + System.currentTimeMillis());

                        saveRecord(result);
                    }
                });
    }


    private void saveRecord(String code) {
        //ToastShort.show(mView.getContext(), "保存文件:" + code);
        ToastShort.showL("保存文件:" + code);
        long time = System.currentTimeMillis();
        bean.setFile_code(code);//获得提取码
        bean.setTime(time);
        bean.setUpload_time(time);
        bean.setUpload_hasDown(true);
        bean.setUpload_isUploading(false);

        bean.setUpload_progress(100);
        bean.setFile_exist(true);

        EventProgressU eventProgressU = new EventProgressU();
        eventProgressU.setBean(bean);
        eventProgressU.setBytesWritten(100);
        eventProgressU.setContentLength(100);
        eventProgressU.setDone(true);
        eventProgressU.setPosition(-1);
        //发送进度信息
        EventBusInstance.getBusInstance().post(eventProgressU);
    }

    /**
     * 上传进度监听器，
     * 通过event bus 通知界面变化？
     * 根据md5获取位置
     * 直接控制界面变化
     */
    private ProgressListener.RequestListener uploadListener = new ProgressListener.RequestListener() {

        @Override
        public void onRequest(long bytesWritten, long contentLength, boolean done) {
            int progress = (int) (bytesWritten * 100 / (float) contentLength);

            bean.setUpload_progress(progress);
            EventProgressU eventProgressU = new EventProgressU();
            eventProgressU.setBean(bean);
            eventProgressU.setBytesWritten(bytesWritten);
            eventProgressU.setContentLength(contentLength);
            eventProgressU.setDone(done);
            eventProgressU.setPosition(-1);
            //发送进度信息
            EventBusInstance.getBusInstance().post(eventProgressU);

        }
    };

    private boolean findRecordWithPath(String path) {
        if (path != null && !path.isEmpty()) {
            File file = new File(path);
            if (file.isFile()) {
                String md5 = MD5.getFileMD5(file);
                List<FileInfoBean> beans = dbr.queryWithMD5(md5);
                if (beans.size() > 0) {
                    //在record中找到上传记录。检查提取码是否为空，不是空则返回提取码；不用管文件路径是否存在。
                    //按理说只会有一个记录。。。
                    String code = beans.get(0).getFile_code();
                    if (code != null && !code.isEmpty()) {
                        EventBusInstance.getBusInstance().post(new EventToastInfo("找到文件" + file.getName() + "的提取码：" + code));
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
