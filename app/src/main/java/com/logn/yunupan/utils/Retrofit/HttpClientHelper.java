package com.logn.yunupan.utils.Retrofit;


import com.logn.yunupan.utils.logger.Logger;

import java.io.IOException;
import java.net.URLDecoder;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 创建client ， 设置监听器  实现监听进度
 */

public class HttpClientHelper {
    //创建client，设置监听 实现进度监听
    public static OkHttpClient addProgRequestListener(final ProgressListener.RequestListener requestListener) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        //增加拦截器
        client.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .method(original.method(), new ProgressRequestBody(original.body(), requestListener))
                        .build();

                return chain.proceed(request);
            }
        });
        return client.build();
    }

    public static OkHttpClient addProResponseListener(final ProgressListener.ResponseListener responseListener) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();

        client.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                //拦截
                Response response = chain.proceed(chain.request());

                //获取文件名
                String header = response.header("Content-Disposition");
                if (header == null || header.length() == 0) {
                    Logger.e("fuck");
                } else {
                    int s = header.lastIndexOf("=");
                    int t = header.lastIndexOf(".");
                    String name = header.substring(s + 1, t);
                    String suffix = header.substring(t + 1);
                    String nameUTF = URLDecoder.decode("" + name, "utf-8");
                    nameUTF = nameUTF + "." + suffix;
                }
                //包装响应体并返回
                return response.newBuilder()
                        .body(new ProgressResponseBody(response.body(), responseListener))
                        .build();
            }
        });

        client.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("referer", "http://upan.oureda.cn/Home/Public")
                        .header("UserAgent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; flashget)")
                        .method(original.method(), original.body())
                        .build();
                Logger.e("fuck_   getRequest");
                return chain.proceed(request);
            }
        });
        return client.build();
    }
}
