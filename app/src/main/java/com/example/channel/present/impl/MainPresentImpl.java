package com.example.channel.present.impl;

import com.example.channel.model.MainModel;
import com.example.channel.model.impl.MainModelImpl;
import com.example.channel.present.MainPresent;
import com.example.channel.view.MainView;

public class MainPresentImpl implements MainPresent, MainModel.OnMainListener {

    private MainView mainView;
    private MainModel mainModel;
    private boolean isShowLoad = false;

    public MainPresentImpl(MainView mainView, MainModel mainModel){
        this.mainView = mainView;
        this.mainModel = mainModel;
    }

    @Override
    public void showMain(boolean isShowLoad) {
        this.isShowLoad = isShowLoad;
        mainModel.findMain(this);
        if (isShowLoad)
            mainView.showProgress();
    }

    @Override
    public void taskSuccess(MainModelImpl mainModel) {
        mainView.findTaskSuccess(mainModel);
        if (isShowLoad)
            mainView.hideProgress();
    }

    @Override
    public void fial(String msg) {
        mainView.findFail(msg);
        if (isShowLoad)
            mainView.hideProgress();
    }

}
