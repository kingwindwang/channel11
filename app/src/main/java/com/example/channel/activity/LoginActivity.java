package com.example.channel.activity;

import android.Manifest;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.channel.App;
import com.example.channel.R;
import com.example.channel.model.impl.LoginModelImpl;
import com.example.channel.model.impl.UserModeImpl;
import com.example.channel.present.impl.LoginPresentImpl;
import com.example.channel.utils.CommonUtil;
import com.example.channel.utils.LoadDialog;
import com.example.channel.view.LoginView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginView {

    private LoginPresentImpl loginPresent;

    @BindView(R.id.et_username)
    public EditText et_username;

    @BindView(R.id.et_password)
    public EditText et_password;

    private LoadDialog loadDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!hasPermission(Manifest.permission.INTERNET)){

            requestPermission(2, Manifest.permission.INTERNET);
        }
        UserModeImpl userMode = App.application.getUser();
        if (!TextUtils.isEmpty(userMode.getUser_id())){
            gotoActivity(MainActivity.class, true);
            return;
        }
        addView(R.layout.activity_login);
        loginPresent = new LoginPresentImpl(this, new LoginModelImpl());

        loadDialog = new LoadDialog(this);
    }

    @OnClick(R.id.btn_login)
    void login(){
        if (!isGo()) return;
        loginPresent.doLogin(CommonUtil.getEdit(et_username), CommonUtil.getEdit(et_password));
    }

    private boolean isGo(){
        String msg;
        if (TextUtils.isEmpty(CommonUtil.getEdit(et_username)))
            msg = "手机号不能为空";
//        else if (!CommonUtil.isMobileNO(CommonUtil.getEdit(et_username)))
//            msg = "请输入正确的手机号码格式";
        else if (TextUtils.isEmpty(CommonUtil.getEdit(et_password)))
            msg = "密码不能为空";
        else
            return true;
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void loginSuccess() {
        gotoActivity(MainActivity.class, true);
    }

    @Override
    public void loginFail(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        if (loadDialog != null)
            loadDialog.builder();
    }

    @Override
    public void hideProgress() {
        if (loadDialog != null)
            loadDialog.dismiss();
    }
}
