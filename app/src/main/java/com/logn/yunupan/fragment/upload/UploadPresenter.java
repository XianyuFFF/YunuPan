package com.logn.yunupan.fragment.upload;

import android.util.Log;

import com.logn.yunupan.entity.greendao.FileInfoBean;
import com.logn.yunupan.entity.greendao.FileUploadBean;
import com.logn.yunupan.module.upload.UploadModule;
import com.logn.yunupan.utils.MD5;
import com.logn.yunupan.utils.Retrofit.ProgressListener;
import com.logn.yunupan.utils.ToastShort;
import com.logn.yunupan.utils.eventbus.EventBusInstance;
import com.logn.yunupan.utils.eventbus.EventProgressU;
import com.logn.yunupan.utils.greendao.DBManagerD;
import com.logn.yunupan.utils.greendao.DBManagerR;
import com.logn.yunupan.utils.greendao.DBManagerU;
import com.logn.yunupan.utils.logger.Logger;
import com.logn.yunupan.utils.rxjava.SchedulersCompat;
import com.logn.yunupan.utils.rxjava.YunUPanService;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

//import static org.greenrobot.eventbus.EventBus.TAG;

/**
 * Created by OurEDA on 2017/3/20.
 */

public class UploadPresenter implements UploadContract.Presenter {
    private static final String TAG = "UploadPresenter";
    private static boolean FLAG = true;

    private UploadContract.View mView;
    private UploadModule module;

    FileUploadBean bean;
    private String md5 = "";
    DBManagerU dbu;
    DBManagerR dbr;
    DBManagerD dbd;

    public UploadPresenter(UploadContract.View view) {
        mView = view;
    }

    @Override
    public void start() {

    }


    @Override
    public void start2Prepare(String path) {
        module = new UploadModule(mView.getContext());
        module.checkForUpload(path);

//        dbu = DBManagerU.getInstance(mView.getContext().getApplicationContext());
//        dbr = DBManagerR.getInstance(mView.getContext().getApplicationContext());
//        dbd = DBManagerD.getInstance(mView.getContext().getApplicationContext());
//        // 1.根据文件的md5判断此文件是否有上传记录
//        File file = new File(path);
//        if (file.isFile()) {
//            md5 = MD5.getFileMD5(file);
//            Logger.e("是啊md5:->" + md5);
//        } else {
//            Logger.e("不是文件？？？？？？？？？？？");
//            return;
//        }
//        //在三个数据库中查找
//        if (searchRecord(md5)) {
//            return;
//        }
//        // 2.检查网络情况
//
//        // 3.获取文件并开始上传,加入数据库中并更新界面
//
//        bean = new FileUploadBean();
//        //根据上传开始的时间来设置主键的值，避免主键冲突。
//        bean.setFile_id(System.currentTimeMillis());
//        bean.setFile_path(path);
//        bean.setFile_exist(true);
//        bean.setUpload_hasDown(false);
//        bean.setUpload_progress(0);
//        bean.setUpload_isUploading(true);
//
//        // 4.根据文件路径（已包括文件名）（或者MD5 ）查找在数据库中的位置。
//        // 更改文件传输状态.
//
//        getFileForUpload(path);
    }

    private boolean searchRecord(String md5) {
        List<FileUploadBean> uBeans = dbu.queryAll();
        for (FileUploadBean bean : uBeans) {
            if (bean.getFile_MD5().equals(md5)) {
                ToastShort.show(mView.getContext(), "文件 " + bean.getFile_name() + " 已存在。\n提取码：" + bean.getFile_code());
                return true;
            }
        }
        List<FileInfoBean> rBeans = dbr.queryAll();
        for (FileInfoBean bean : rBeans) {
            if (bean.getFile_MD5() == null) {
                continue;
            }
            if (bean.getFile_MD5().equals(md5)) {
                ToastShort.show(mView.getContext(), "文件 " + bean.getFile_name() + " 已存在。\n提取码：" + bean.getFile_code());
                return true;
            }
        }
//        List<FileDownloadBean> dBeans = dbd.queryAll();
//        for (FileDownloadBean bean : dBeans) {
//            if (bean.getFile_MD5().equals(md5)) {
//                ToastShort.show(mView.getContext(), "文件 " + bean.getFile_name() + " 已存在。\n提取码：" + bean.getFile_code());
//                return true;
//            }
//        }
        return false;
    }

    private void getFileForUpload(String path) {

        Observable.just(path)
                .map(new Func1<String, File>() {
                    @Override
                    public File call(String s) {
                        File file = new File(s);
                        if (!file.exists()) {
                            //文件获取失败
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
                        }
                    }

                });
    }

    private void uploadFile(File file) {
        if (file.isFile()) {
            md5 = MD5.getFileMD5(file);
            bean.setFile_MD5(md5);
            bean.setFile_name(file.getName());
            bean.setFile_size(file.length());
            //加入到item？开始上传，根据监听器设置进度
            dbu.insertFileInfo(bean);
            mView.addItemData(bean);
        }
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
                        Log.e(TAG + "", ": error~:" + e.toString());
                        ToastShort.show(mView.getContext(), "请使用校园网\n错误信息：" + e.toString());
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
        ToastShort.show(mView.getContext(), "保存文件:" + code);
        long time = System.currentTimeMillis();
        bean.setFile_code(code);
        bean.setTime(time);
        bean.setUpload_time(time);
        bean.setUpload_hasDown(true);
        bean.setUpload_isUploading(false);
        //dbu.insertFileInfo(bean);     更新数据库
        //通知recyclerview改变item，或者用监听器监听进度。
        //mView.addItemData();

        EventBusInstance.getBusInstance().post(new EventProgressU(bean.getFile_size(), bean.getFile_size(), true, mView.getPosition(md5), bean));
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
            //Logger.e(TAG + ":  上传：" + bytesWritten / (float) contentLength + "-----" + bytesWritten + ":" + contentLength);
            int position = mView.getPosition(md5);
            int progress = (int) (bytesWritten * 100 / (float) contentLength);
            if (position >= 0) {
                //更新指定item。
                bean.setUpload_progress(progress);
                bean.setUpload_isUploading(true);
            }
            if (done) {
                //上传完成，
                bean.setUpload_progress(100);
                bean.setUpload_hasDown(true);
                bean.setUpload_isUploading(false);
            }
            //Logger.e("线程名：" + Thread.currentThread().getName());
            if (FLAG) {
                Logger.e(TAG + "\tmd5:" + md5);
                Log.e(TAG, "线程名：" + Thread.currentThread().getName() + "\nposition:" + position);
                FLAG = !FLAG;
            }
            EventBusInstance.getBusInstance().post(new EventProgressU(bytesWritten, contentLength, done, position, bean));

        }
    };
}
