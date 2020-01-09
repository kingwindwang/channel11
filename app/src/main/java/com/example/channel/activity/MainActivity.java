package com.example.channel.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.channel.App;
import com.example.channel.R;
import com.example.channel.adapter.SiteAdapter;
import com.example.channel.model.impl.MainModelImpl;
import com.example.channel.present.impl.MainPresentImpl;
import com.example.channel.utils.LoadDialog;
import com.example.channel.view.MainView;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import androidx.appcompat.app.AlertDialog;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements MainView {

    @BindViews({R.id.tv_tasking, R.id.tv_tasked, R.id.tv_task_del})
    public List<TextView> tvTasks;

    @BindView(R.id.tv_add_site)
    public Button tv_add_site;

    @BindView(R.id.img_head)
    public ImageView img_head;

    @BindView(R.id.tv_login_out)
    public TextView tv_login_out;

    @BindView(R.id.lv_site)
    public PullLoadMoreRecyclerView lv_site;

    @BindViews({R.id.tv_name, R.id.tv_company})
    public List<TextView> tvMys;

    private SiteAdapter siteAdapter;
    private MainPresentImpl mainPresent;

    private LoadDialog loadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addView(R.layout.activity_main);
        loadDialog = new LoadDialog(this);
        mainPresent = new MainPresentImpl(this, new MainModelImpl());
        mainPresent.showMain(true);
        EventBus.getDefault().register(this);
    }

    @OnClick(R.id.tv_add_site)
    void OnAddSite(){
        Bundle bundle = new Bundle();
        bundle.putInt("rod_number", 0);
        bundle.putInt("state", -1);
        bundle.putString("rod_number_parent", "");
        gotoActivity(AddSiteActivity.class, false, bundle);
    }

    @OnClick(R.id.tv_login_out)
    void LoginOut(){
        AlertDialog alertDialog2 = new AlertDialog.Builder(this)
                .setTitle("是否退出登录")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        App.application.clear();
                        gotoActivity(LoginActivity.class, true);
                    }
                })

                .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
        alertDialog2.show();
    }

    public void gotoDetail(String task_id, int state){
        Bundle bundle = new Bundle();
        bundle.putString("task_id", task_id);
        bundle.putInt("state", state);
        bundle.putString("rod_number_parent", "");
        gotoActivity(SiteDetailActivity.class, false, bundle);
    }

    @Override
    public void findTaskSuccess(MainModelImpl mainModel) {
        tvMys.get(0).setText(mainModel.getUser_info().getName());
        tvMys.get(1).setText(mainModel.getUser_info().getDep_name());

        tvTasks.get(0).setText(mainModel.getSata_info().getUnfinish()+"");
        tvTasks.get(1).setText(mainModel.getSata_info().getFinish()+"");
        tvTasks.get(2).setText(mainModel.getSata_info().getDel()+"");

        lv_site.setLinearLayout();
        siteAdapter = new SiteAdapter(this, mainModel.getTask_list());
        lv_site.setAdapter(siteAdapter);
        lv_site.setPullRefreshEnable(false);
        lv_site.setPushRefreshEnable(false);
        lv_site.setIsLoadMore(false);
    }

    @OnClick({R.id.tv_tasking, R.id.tv_tasked, R.id.tv_task_del})
    void OnTask(View v){
        Bundle bundle = new Bundle();
        if (v.getId() == R.id.tv_tasking)
            bundle.putInt("position", 0);
        else if (v.getId() == R.id.tv_tasked)
            bundle.putInt("position", 1);
        else if (v.getId() == R.id.tv_task_del)
            bundle.putInt("position", 2);
        gotoActivity(TaskActivity.class, false, bundle);
    }

    @Override
    public void findFail(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    @Subscribe
    public void onEventMainThread(MainModelImpl a){
        mainPresent.showMain(false);
    }

    @Subscribe
    public void onEventMainThread(String a){
        mainPresent.showMain(false);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
