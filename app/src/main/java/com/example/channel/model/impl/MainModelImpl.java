package com.example.channel.model.impl;

import com.example.channel.App;
import com.example.channel.https.HttpCallBack;
import com.example.channel.https.HttpManager;
import com.example.channel.https.HttpServer;
import com.example.channel.https.ResponseBase;
import com.example.channel.model.MainModel;
import com.example.channel.model.OnListener;
import com.example.channel.utils.GsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

public class MainModelImpl implements MainModel {

    private UserModeImpl user_info;
    private TaskModelImpl sata_info;
    private List<SiteModelImpl> task_list;

    @Override
    public void findMain(OnMainListener listener) {
        Map<String, Object> params = new HashMap<>();
        UserModeImpl user = App.application.getUser();
        params.put("method", "update_page");
        params.put("user_id", user.getUser_id());
        HttpManager httpManager = HttpManager.getInstance();
        httpManager.callData(params, false, new HttpCallBack() {
            @Override
            public void onSuccess(Object success) {
                String s = GsonUtils.GsonString(success);
                MainModelImpl mainModel = GsonUtils.GsonToBean(s, MainModelImpl.class);
                listener.taskSuccess(mainModel);
            }

            @Override
            public void onFail(String fail) {
                listener.fial(fail);
            }
        });
    }

    public UserModeImpl getUser_info() {
        return user_info;
    }

    public void setUser_info(UserModeImpl user_info) {
        this.user_info = user_info;
    }

    public TaskModelImpl getSata_info() {
        return sata_info;
    }

    public void setSata_info(TaskModelImpl sata_info) {
        this.sata_info = sata_info;
    }

    public List<SiteModelImpl> getTask_list() {
        return task_list;
    }

    public void setTask_list(List<SiteModelImpl> task_list) {
        this.task_list = task_list;
    }

}
