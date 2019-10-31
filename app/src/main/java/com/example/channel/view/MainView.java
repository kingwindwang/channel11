package com.example.channel.view;

import com.example.channel.model.impl.MainModelImpl;

public interface MainView extends BaseView{

    void findTaskSuccess(MainModelImpl mainModel);
    void findFail(String msg);
}
