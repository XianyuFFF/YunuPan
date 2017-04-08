package com.logn.yunupan.utils.rxjava;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by OurEDA on 2016/12/18.
 */

public interface ServiceGroup {

    /**
     * 上传
     *
     * @param part
     * @return
     */
    @Multipart
    @POST("Upload")
    Observable<ResponseBody> uploadFile(@Part MultipartBody.Part part);

    /**
     * 下载
     *
     * @return
     */
//    @Headers({"Referer: http://upan.oureda.cn/Home/Public",
//            "UserAgent: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; flashget)"})
    @GET("DownFileByrandName")
    Observable<ResponseBody> downloadFile(@Header("randName") String fileCode);


    @GET
    Observable<String> check_Connent();

}
