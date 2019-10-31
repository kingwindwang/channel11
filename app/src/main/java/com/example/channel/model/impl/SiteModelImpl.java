package com.example.channel.model.impl;

import com.example.channel.App;
import com.example.channel.https.HttpCallBack;
import com.example.channel.https.HttpManager;
import com.example.channel.model.OnListener;

import java.util.HashMap;
import java.util.Map;

public class SiteModelImpl {

    private String task_id;
    private String create_time;
    private String line_name;
    private int state;
    private String state_name;

    public void findTask(int state, int page, OnListener listener){
        HttpManager httpManager = HttpManager.getInstance();
        Map<String, Object> params = new HashMap<>();
        params.put("method", "task_list");
        params.put("state", state);
        params.put("page", page);
        params.put("user_id", App.application.getUser().getUser_id());
        httpManager.callData(params, false, new HttpCallBack() {
            @Override
            public void onSuccess(Object success) {
                listener.onSuccess(success);
            }

            @Override
            public void onFail(String fail) {
                listener.onFail(fail);
            }
        });
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getLine_name() {
        return line_name;
    }

    public void setLine_name(String line_name) {
        this.line_name = line_name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }
}
