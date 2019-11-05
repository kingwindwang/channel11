package com.example.channel.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.channel.App;
import com.example.channel.R;
import com.example.channel.adapter.SiteDetailAdapter;
import com.example.channel.model.impl.MainModelImpl;
import com.example.channel.model.impl.SiteDetailModelImpl;
import com.example.channel.present.SiteDetailPresent;
import com.example.channel.present.impl.SiteDetailPresentImpl;
import com.example.channel.utils.CacheActivity;
import com.example.channel.utils.CommonUtil;
import com.example.channel.utils.GsonUtils;
import com.example.channel.utils.LoadDialog;
import com.example.channel.view.SiteDetailView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import butterknife.BindView;
import butterknife.OnClick;

public class SiteDetailActivity extends BaseActivity implements SiteDetailView {

    @BindView(R.id.lv_select)
    public ListView lvSite;

    private SiteDetailPresent siteDetailPresent;
    private String task_id;
    private int state;
    private SiteDetailAdapter siteDetailAdapter;
    private List<SiteDetailModelImpl> siteDetailModelList = new ArrayList<>();
    private boolean isAdd = true;
    private LoadDialog loadDialog;
    private String rod_number_parent = "";
    private String point_id_parent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addView(R.layout.list, true);
        rod_number_parent = getIntent().getExtras().getString("rod_number_parent");
        task_id = getIntent().getExtras().getString("task_id");
        state = getIntent().getExtras().getInt("state");
        if (state == -1){
            tv_submit.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(rod_number_parent)){
            tv_add.setVisibility(View.VISIBLE);
            tv_add.setText("添加子选点");
            tv_title.setText("子选点列表");
            point_id_parent = getIntent().getExtras().getString("point_id_parent");
        }else
            tv_title.setText("选点列表");
        loadDialog = new LoadDialog(this);
        siteDetailPresent = new SiteDetailPresentImpl(this, new SiteDetailModelImpl());
        siteDetailPresent.showSiteDetail(task_id, point_id_parent, true);
        EventBus.getDefault().register(this);

    }

    @Override
    public void findSiteDetail(List<SiteDetailModelImpl> siteDetailModels) {
        siteDetailModelList = siteDetailModels;
        if (siteDetailModelList.get(siteDetailModelList.size()-1).getPoint_type().equals("终止点")){
            tv_submit.setText("删除");
            isAdd = false;
        }else {
            tv_submit.setText("添加");
            isAdd = true;
        }
        siteDetailAdapter = new SiteDetailAdapter(this, siteDetailModelList, isAdd);
        lvSite.setAdapter(siteDetailAdapter);
        if (state != 0){
            lvSite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    goAddSite(position, -2);
                }
            });
        }

    }

    @OnClick(R.id.tv_submit)
    void onSubmit(){
        if (!isAdd){
            goDel();
        }else {//添加新选点
            Bundle bundle = new Bundle();
            bundle.putInt("rod_number", siteDetailModelList.size());
            bundle.putString("task_id", task_id);
            bundle.putString("rod_number_parent", "");
            gotoActivity(AddSiteActivity.class, false, bundle);
        }
    }

    //添加子选点
    @OnClick(R.id.tv_add)
    void onAddSonPoint(){
        Bundle bundle = new Bundle();
        bundle.putInt("rod_number", siteDetailModelList.size());
        bundle.putString("rod_number_parent", rod_number_parent);
        bundle.putString("point_id_parent", point_id_parent);
        bundle.putString("task_id", task_id);
        gotoActivity(AddSiteActivity.class, false, bundle);
    }

    private void goDel(){
        AlertDialog alertDialog2 = new AlertDialog.Builder(this)
                .setTitle("是否删除")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        siteDetailPresent.delSite(task_id, true);
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

    public void edit(int position){
        goAddSite(position, -1);
    }

    //修改查询选点
    private void goAddSite(int position, int rod_number){
        SiteDetailModelImpl site = siteDetailModelList.get(position);
        String s = GsonUtils.GsonString(site);
        Bundle bundle = new Bundle();
        bundle.putString("site", s);
        bundle.putInt("rod_number", rod_number);
        bundle.putString("rod_number_parent", rod_number_parent);
        gotoActivity(AddSiteActivity.class, false, bundle);
    }

    @Override
    public void fail(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void delSiteSuccess() {
        Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
        EventBus.getDefault().post("");
        back();
    }

    @Subscribe
    public void onEventMainThread(MainModelImpl a){
        siteDetailPresent.showSiteDetail(task_id, point_id_parent, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
