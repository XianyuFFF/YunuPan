package com.logn.yunupan.utils.Retrofit;

/**
 * Created by OurEDA on 2016/12/20.
 */

public class ProgressListener {
    /**
     * 请求体进度回调接口，用于文件上传进度回调
     */
    public interface RequestListener {
        void onRequest(long bytesWritten, long contentLength, boolean done);
    }

    /**
     * 响应体回调接口，用于文件下载进度回调
     */
    public interface ResponseListener {
        void onResponse(long bytesRead, long contentLength, boolean done);
    }
}
