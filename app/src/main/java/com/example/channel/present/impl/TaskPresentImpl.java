package com.example.channel.present.impl;

import com.example.channel.model.OnListener;
import com.example.channel.model.impl.SiteModelImpl;
import com.example.channel.present.TaskPresent;
import com.example.channel.utils.GsonUtils;
import com.example.channel.view.TaskView;

import java.util.List;

/**
 * Created by Administrator on 2019/10/10 0010.
 */

public class TaskPresentImpl implements TaskPresent, OnListener{

    private TaskView taskView;
    private SiteModelImpl siteModel;
    private boolean isShowLoad;

    public TaskPresentImpl(TaskView taskView){
        this.taskView = taskView;
        siteModel = new SiteModelImpl();
    }

    @Override
    public void onSuccess(Object success) {
        String s = GsonUtils.GsonString(success);
        List<SiteModelImpl> siteModels = GsonUtils.jsonToList(s, SiteModelImpl.class);
        taskView.findTask(siteModels);
        if (isShowLoad)
            taskView.hideProgress();
    }

    @Override
    public void onFail(String msg) {
        taskView.fail(msg);
        if (isShowLoad)
            taskView.hideProgress();
    }

    @Override
    public void showTask(int state, int page, boolean isShowLoad) {
        siteModel.findTask(state, page, this);
        this.isShowLoad = isShowLoad;
        if (isShowLoad)
            taskView.showProgress();
    }
}
