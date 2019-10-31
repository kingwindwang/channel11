package com.example.channel.https;

import android.app.Activity;
import android.util.Log;

import com.example.channel.utils.GsonUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Admin on 2019/8/19.
 * 编写人：li
 * 功能描述：
 */
public class HttpManager {

    private volatile static HttpManager INSTANCE;
    public static String BASE_URL = "https://www.avatek.cn/";
    public static String IMG_URL = "https://www.avatek.cn/sg/upload/";
    public Retrofit retrofit;

    //构造方法私有
    private HttpManager() {
        //手动创建一个OkHttpClient并设置超时时间
        okhttp3.OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.SECONDS);
//        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
//        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(new HttpInterceptor());
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        this.retrofit = retrofit;
    }

    //获取单例
    public static HttpManager getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpManager();
                }
            }
        }
        return INSTANCE;
    }

    public <T> void setData(Observable<T> observable, final HttpCallBack callBack){
        setData(observable, true, callBack);
    }

    public <T> void setData(Observable<T> observable, final boolean isShowDialog, final HttpCallBack callBack){

        observable.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<T>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                callBack.onFail(e.toString());
                            }

                            @Override
                            public void onNext(T retrofitEntity) {
                                ResponseBase responseBase = (ResponseBase) retrofitEntity;
                                if (responseBase.getState() == 1){
                                    callBack.onSuccess(responseBase.getContent());
                                }else {
                                    callBack.onFail(responseBase.getReason());
                                }

                            }

                            @Override
                            public void onStart() {
                                super.onStart();
                            }
                        }

                );
    }

    public <T> void callData(Map<String, Object> params, final boolean isShowDialog, final HttpCallBack callBack){
        HttpServer httpServer = retrofit.create(HttpServer.class);
        Call<ResponseBody> call = httpServer.http1(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                try {
                    Map<String, Object> map = GsonUtils.GsonToMaps(response.body().string());
                    if (map == null)
                        callBack.onFail("数据异常");
                    else if (Integer.valueOf(map.get("state").toString()) == 1){
                        callBack.onSuccess(map.get("content"));
                    }else {
                        callBack.onFail((String) map.get("reason"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callBack.onFail(t.getMessage());
            }
        });
    }

}
