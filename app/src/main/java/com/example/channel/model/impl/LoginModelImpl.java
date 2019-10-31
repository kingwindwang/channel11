package com.example.channel.model.impl;

import com.example.channel.App;
import com.example.channel.https.HttpCallBack;
import com.example.channel.https.HttpManager;
import com.example.channel.https.HttpServer;
import com.example.channel.https.ResponseBase;
import com.example.channel.model.LoginModel;
import com.example.channel.model.OnListener;
import com.example.channel.utils.GsonUtils;
import com.example.channel.utils.ImageUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;

public class LoginModelImpl implements LoginModel {

    @Override
    public void login(String userName, String passWord, OnListener listener) {
        Map<String, Object> params = new HashMap<>();
        params.put("method", "login");
        params.put("tel", userName);
        params.put("password", passWord);

        HttpManager httpManager = HttpManager.getInstance();
        httpManager.callData(params, false, new HttpCallBack() {
            @Override
            public void onSuccess(Object success) {
                String s = GsonUtils.GsonString(success);
                MainModelImpl mainModel = GsonUtils.GsonToBean(s, MainModelImpl.class);
                UserModeImpl user = mainModel.getUser_info();
                App.application.setUser(user);
                listener.onSuccess(s);
            }

            @Override
            public void onFail(String fail) {
                listener.onFail(fail);
            }
        });

    }
}
