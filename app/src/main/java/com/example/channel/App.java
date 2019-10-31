package com.example.channel;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;
import android.widget.ImageView;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.example.channel.model.impl.UserModeImpl;
import com.example.channel.utils.GsonUtils;
import com.example.channel.utils.Location;
import com.example.channel.utils.SharedPreferencesHelper;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;

public class App extends Application {

    public static App application;

    public static int sendSmsTime = 60000;

    public static boolean isHaveUD = false;//是否有有效的风险报告

    public static int SITE_LIST = 1;//跳转列表
    public static int SITE_EDIT = 2;//跳转输入框
    public static int SITE_MATERIAL = 3;//跳转材料
    public static int SITE_PHONE = 4;//跳转拍照定位

    public Location location;
    public Vibrator mVibrator;
    private static SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        AutoSizeConfig.getInstance().setCustomFragment(true);
        AutoSize.initCompatMultiProcess(this);

        sharedPreferencesHelper = new SharedPreferencesHelper(this, "USER");

        /***
         * 初始化定位sdk，建议在Application中创建
         */
        location = new Location(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }

    public void setUser(UserModeImpl user){
        if (user == null)
            user = new UserModeImpl();
        sharedPreferencesHelper.put("user", GsonUtils.GsonString(user));
    }

    public UserModeImpl getUser(){
        if (sharedPreferencesHelper.getSharedPreference("user", null) != null){
            UserModeImpl user =  GsonUtils.GsonToBean((String) sharedPreferencesHelper.getSharedPreference("user", null), UserModeImpl.class);
            return user;
        }
        return new UserModeImpl();
    }

    public void clear(){
        sharedPreferencesHelper.clear();
    }
}
