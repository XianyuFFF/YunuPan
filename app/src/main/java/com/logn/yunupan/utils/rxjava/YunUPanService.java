package com.logn.yunupan.utils.rxjava;

import android.support.annotation.NonNull;

import com.logn.yunupan.utils.Retrofit.HttpClientHelper;
import com.logn.yunupan.utils.Retrofit.ProgressListener;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * 创建  上传下载 service
 */

public class YunUPanService {

    private static final String API_BASE_URL = "http://upan.oureda.cn/Home/";

    /**
     * 上传
     *
     * @param file
     * @return
     */
    public static Observable<ResponseBody> uploadFileService(File file, @NonNull ProgressListener.RequestListener requestListener) {
        Retrofit retrofit = new Retrofit.Builder()
                //.addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(HttpClientHelper.addProgRequestListener(requestListener))
                .baseUrl(API_BASE_URL)
                .build();
        ServiceGroup service = retrofit.create(ServiceGroup.class);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        return service.uploadFile(body);
    }


    /**
     * 下载
     *
     * @param fileCode
     * @return
     */
    public static Observable<ResponseBody> downloadFile(String fileCode, @NonNull ProgressListener.ResponseListener responseListener) {
        Retrofit retrofit = new Retrofit.Builder()
                //设置okhttpclient ，实现下载进度监听
                .client(HttpClientHelper.addProResponseListener(responseListener))
                .addConverterFactory(GsonConverterFactory.create())
                //Rxjava 和 retrofit  适配
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(API_BASE_URL)
                .build();
        //动态代理
        ServiceGroup serviceDownload = retrofit.create(ServiceGroup.class);

        return serviceDownload.downloadFile(fileCode);
    }

}
