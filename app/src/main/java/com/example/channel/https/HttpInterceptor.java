package com.example.channel.https;

import com.example.channel.App;
import com.example.channel.utils.NetworkUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Admin on 2019/8/19.
 * 编写人：li
 * 功能描述：
 */
public class HttpInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request request = original.newBuilder()
                .header("User-Agent", "Your-App-Name")
                .header("Accept", "application/vnd.yourapi.v1.full+json")
                .method(original.method(), original.body())
                .build();

        return chain.proceed(request);
    }
}
