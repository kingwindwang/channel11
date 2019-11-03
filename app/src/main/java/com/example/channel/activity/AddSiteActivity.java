package com.example.channel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.channel.App;
import com.example.channel.R;
import com.example.channel.adapter.AddSiteAdapter;
import com.example.channel.https.HttpManager;
import com.example.channel.model.impl.MainModelImpl;
import com.example.channel.model.impl.SiteContentModelImpl;
import com.example.channel.model.impl.SiteDetailModelImpl;
import com.example.channel.model.impl.UserModeImpl;
import com.example.channel.present.SiteContentPresent;
import com.example.channel.present.impl.SiteContentPresentImpl;
import com.example.channel.utils.CommonUtil;
import com.example.channel.utils.GsonUtils;
import com.example.channel.utils.ImageUtil;
import com.example.channel.utils.LoadDialog;
import com.example.channel.view.AddSiteView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class AddSiteActivity extends BaseActivity implements AddSiteView {

    @BindView(R.id.lv_add_site)
    public ListView lv_add_site;

    private int position = -1;

    private List<SiteContentModelImpl> siteContentModelList = new ArrayList<>();
    private AddSiteAdapter addSiteAdapter;
    private String materials = "";
    private int rod_number = 0;//-2表示不可操作；=-1表示编辑；其他 判断当前工单里这是第几个点，如果是第二个点就传1，第三个点就传2，以此类推
    private String rod_number_parent = "";//空表示不是子节点，有表示为子节点
    private String rod_numStr = "";
    private SiteContentPresent siteContentPresent;
    private String latitude,longtitude,addr,url;//维度，经度，地址，图片路径
    private SiteDetailModelImpl site;
    private String task_id ="";
    private LoadDialog loadDialog;
    private String point_id;
    private String point_id_parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addView(R.layout.activity_add_site, true);
        rod_number = getIntent().getExtras().getInt("rod_number");
        rod_number_parent = getIntent().getExtras().getString("rod_number_parent");
        if (!TextUtils.isEmpty(rod_number_parent)){
            point_id_parent = getIntent().getExtras().getString("point_id_parent");
        }
        if (rod_number > 0)
            task_id = getIntent().getExtras().getString("task_id");
        if (rod_number < 0 ){
            if (rod_number == -2)
                tv_submit.setVisibility(View.GONE);
            if (TextUtils.isEmpty(rod_number_parent)){
                tv_add.setVisibility(View.VISIBLE);
                tv_add.setText("子选点");
            }
            site = GsonUtils.GsonToBean(getIntent().getExtras().getString("site"), SiteDetailModelImpl.class);
            materials = CommonUtil.getMaterials(site.getMaterials());
            latitude = site.getLat();
            longtitude = site.getLont();
            addr = site.getAddress();
            url = HttpManager.IMG_URL + site.getImages();
            task_id = site.getTask_id();
        }
        loadDialog = new LoadDialog(this);
        siteContentPresent = new SiteContentPresentImpl(this, new SiteContentModelImpl(AddSiteActivity.this, rod_number == -2 ? -1 : rod_number, rod_number_parent, site));
        siteContentPresent.showSiteContent();
    }


    @Override
    public void findSiteContent(List<SiteContentModelImpl> siteContentModels) {
        siteContentModelList = siteContentModels;
        addSiteAdapter = new AddSiteAdapter(this, siteContentModelList, rod_number == -2 ? -1 : rod_number);
        lv_add_site.setAdapter(addSiteAdapter);
        lv_add_site.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent in;
                Bundle bundle;
                position = i;
                switch (i){
                    case 0:
                        break;
                    case 4:
                    case 5:
                    case 6:
                    case 2:
                    case 3:
                        if (rod_number == -2)
                            break;
                        if (i == 3){
                            if (!(rod_number == 0 || siteContentModels.get(1).getContents()[siteContentModels.get(1).getSelectPosition()].equals("终止点") || (site != null && site.getPoint_type().equals("起始点")))){
                                return;
                            }
                        }
                        bundle = new Bundle();
                        bundle.putInt("position", siteContentModels.get(i).getSelectPosition());
                        bundle.putStringArray("content", siteContentModels.get(i).getContents());
                        bundle.putString("title", siteContentModels.get(i).getName());
                        in = new Intent(AddSiteActivity.this, SelectActivity.class);
                        in.putExtras(bundle);
                        startActivityForResult(in, App.SITE_LIST);
                        break;
                    case 1://输入框
                        if (rod_number < 0){
                            break;
                        }
                        bundle = new Bundle();
                        bundle.putString("title", siteContentModels.get(i).getName());
                        if (rod_number == 0 && TextUtils.isEmpty(rod_number_parent)){
                            bundle.putString("content", siteContentModels.get(i).getContents()[0]);
                            in = new Intent(AddSiteActivity.this, EditActivity.class);
                            in.putExtras(bundle);
                            startActivityForResult(in, App.SITE_EDIT);
                        }else {
                            bundle.putInt("position", siteContentModels.get(i).getSelectPosition());
                            bundle.putStringArray("content", siteContentModels.get(i).getContents());
                            in = new Intent(AddSiteActivity.this, SelectActivity.class);
                            in.putExtras(bundle);
                            startActivityForResult(in, App.SITE_LIST);
                        }
                        break;
                    case 7://选择材料
                        bundle = new Bundle();
                        bundle.putString("materials", materials);
                        bundle.putInt("rod_number", rod_number);
                        in = new Intent(AddSiteActivity.this, MaterialActivity.class);
                        in.putExtras(bundle);
                        startActivityForResult(in, App.SITE_MATERIAL);
                        break;
                    case 8://定位拍照
                        bundle = new Bundle();
                        bundle.putString("address", CommonUtil.getStr(addr));
                        bundle.putString("lont", CommonUtil.getStr(longtitude));
                        bundle.putString("lat", CommonUtil.getStr(latitude));
                        bundle.putString("url", CommonUtil.getStr(url));
                        bundle.putInt("rod_number", rod_number);
                        in = new Intent(AddSiteActivity.this, CameraActivity.class);
                        in.putExtras(bundle);
                        startActivityForResult(in, App.SITE_PHONE);
                        break;

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        if (requestCode == App.SITE_LIST && resultCode == App.SITE_LIST){//列表
            siteContentModelList.get(position).setSelectPosition(data.getIntExtra("position", 0));
            addSiteAdapter.notifyDataSetChanged();
            if (position == 3){
                rod_numStr = siteContentModelList.get(position).getContents()[data.getIntExtra("position", 0)];
            }
        }else if (requestCode == App.SITE_EDIT && resultCode == App.SITE_EDIT){//输入框
            siteContentModelList.get(position).setSelectPosition(0);
            String[] c = {data.getStringExtra("content")};
            siteContentModelList.get(position).setContents(c);
            addSiteAdapter.notifyDataSetChanged();
        }else if (requestCode == App.SITE_MATERIAL && resultCode == App.SITE_MATERIAL){//材料
            materials = data.getStringExtra("materials");
        }else if (requestCode == App.SITE_PHONE && resultCode == App.SITE_PHONE){//拍照定位
            addr = data.getStringExtra("address");
            longtitude = data.getStringExtra("lont");
            latitude = data.getStringExtra("lat");
            url = data.getStringExtra("url");
        }
    }

    @OnClick(R.id.tv_submit)
    void OnSubmit(){
        if (rod_number == -1 && url.contains("http"))
            siteContentPresent.submit(rodNum(), point_id_parent, longtitude, latitude, ImageUtil.getImgName(url), addr,
                    materials, siteContentModelList, site.getPoint_id(), site.getTask_id(), true);
        else
            siteContentPresent.uploadImage(url, true);
    }

    @OnClick(R.id.tv_add)
    void OnSonPointList(){
        Bundle bundle = new Bundle();
        bundle.putString("task_id", task_id);
        bundle.putInt("state", rod_number < 0 ? -1 : 0);
        bundle.putString("rod_number_parent", site.getRod_number());
        bundle.putString("point_id_parent", site.getPoint_id());
        gotoActivity(SiteDetailActivity.class, false, bundle);
    }

    private int rodNum(){
        if (site.getPoint_type().equals("起始点")){
            return 0;
        } else if (site.getPoint_type().equals("终止点")){
            return 1;
        } else{
            return Integer.valueOf(site.getRod_number());
        }
    }

    @Override
    public void submit() {
        back();
        Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
        EventBus.getDefault().post(new MainModelImpl());
    }

    @Override
    public void uploadImg(String images) {
        siteContentPresent.submit(rod_number == -1 ? rodNum() : rod_number, rod_number_parent, longtitude, latitude,
                images, addr, materials, siteContentModelList, site == null ? "" : site.getPoint_id(), task_id, false);
    }

    @Override
    public void fial(String msg) {
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
