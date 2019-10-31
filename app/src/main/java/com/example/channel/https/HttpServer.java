package com.example.channel.https;


import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Admin on 2019/8/19.
 * 编写人：li
 * 功能描述：
 */
public interface HttpServer {

    @POST("sg/api.php")
    Observable<ResponseBase> http(@Body Map<String, String> params);

    @Multipart
    @POST("sg/api.php")
    Call<ResponseBody> saveImage(@Part MultipartBody.Part file, @Part("method") RequestBody method);

    @POST("sg/api.php")
    Call<ResponseBody> http1(@QueryMap Map<String, Object> params);

}
