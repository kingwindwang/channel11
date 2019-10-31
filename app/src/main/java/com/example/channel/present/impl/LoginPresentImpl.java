package com.example.channel.present.impl;

import com.example.channel.model.OnListener;
import com.example.channel.model.impl.LoginModelImpl;
import com.example.channel.present.LoginPresent;
import com.example.channel.view.LoginView;

public class LoginPresentImpl implements LoginPresent, OnListener {

    private LoginView loginView;
    private LoginModelImpl loginModel;

    public LoginPresentImpl(LoginView loginView, LoginModelImpl loginModel){
        this.loginModel = loginModel;
        this.loginView = loginView;
    }

    @Override
    public void doLogin(String username, String password) {
        loginModel.login(username, password, this);
        loginView.showProgress();
    }

    @Override
    public void onSuccess(Object success) {

        loginView.loginSuccess();
        loginView.hideProgress();
    }

    @Override
    public void onFail(String msg) {
        loginView.loginFail(msg);
        loginView.hideProgress();
    }
}
