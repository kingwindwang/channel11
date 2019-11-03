package com.example.channel.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.channel.R;
import com.example.channel.utils.CacheActivity;

import org.greenrobot.eventbus.EventBus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BaseActivity extends AppCompatActivity {

    public LinearLayout ll_base;

    @BindView(R.id.tv_title)
    public TextView tv_title;

    @BindView(R.id.img_back)
    public ImageView img_back;

    @BindView(R.id.tv_submit)
    public TextView tv_submit;

    @BindView(R.id.tv_add)
    public TextView tv_add;

    @BindView(R.id.rl_title)
    public RelativeLayout rl_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        ll_base = findViewById(R.id.ll_base);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        hideSoftInput();

    }

    public void addView(View view){
        addView(view, false);
    }

    public void addView(int layoutResID){
        View view = View.inflate(getApplicationContext(), layoutResID, null);
        addView(view);
    }

    public void addView(int layoutResID, boolean isTitle){
        View view = View.inflate(getApplicationContext(), layoutResID, null);
        addView(view, isTitle);
    }

    public void addView(View view, boolean isTitle){
        ll_base.addView(view);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.height = LinearLayout.LayoutParams.MATCH_PARENT;
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        view.setLayoutParams(params);
        ButterKnife.bind(this);
        if (!isTitle){
            rl_title.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.img_back)
    void onBack(){
        back();
    }

    /**
     * 打开一个Activity 默认 不关闭当前activity
     */
    public void gotoActivity(Class<?> clz) {
        gotoActivity(clz, false, null);
    }

    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity) {
        gotoActivity(clz, isCloseCurrentActivity, null);
    }

    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity, Bundle ex) {
        Intent intent = new Intent(this, clz);
        if (ex != null) intent.putExtras(ex);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_right_in,R.anim.anim_left_out);
        if (isCloseCurrentActivity) {
            finish();
        }
    }

    /**
     * 隐藏软件盘
     */
    public void hideSoftInput() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void back(){
        finish();
        overridePendingTransition(R.anim.anim_left_in,R.anim.anim_right_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.anim_right_in,R.anim.anim_left_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.anim_right_in,R.anim.anim_left_out);
    }

    /**
     * 权限检查方法，false代表没有该权限，ture代表有该权限
     */
    public boolean hasPermission(String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    /**
     * 权限请求方法
     */
    public void requestPermission(int code, String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, code);
    }

    /**
     * 处理请求权限结果事件
     *
     * @param requestCode  请求码
     * @param permissions  权限组
     * @param grantResults 结果集
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doRequestPermissionsResult(requestCode, grantResults);
    }

    /**
     * 处理请求权限结果事件
     *
     * @param requestCode  请求码
     * @param grantResults 结果集
     */
    public void doRequestPermissionsResult(int requestCode, int[] grantResults) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CacheActivity.finishSingleActivity(this);
        EventBus.getDefault().unregister(this);
    }

    // 设置返回按钮的监听事件
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // 监听返回键，点击两次退出程序
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (getClass() == MainActivity.class){
                if ((System.currentTimeMillis() - exitTime) > 5000) {
                    Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_LONG).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    System.exit(0);
                }
                return true;
            }else
                back();
        }


        return super.onKeyDown(keyCode, event);
    }
}
