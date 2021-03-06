package com.example.channel.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.channel.App;
import com.example.channel.R;
import com.example.channel.adapter.AddSiteAdapter;
import com.example.channel.https.HttpManager;
import com.example.channel.model.impl.MainModelImpl;
import com.example.channel.model.impl.SiteContentModelImpl;
import com.example.channel.model.impl.SiteDetailModelImpl;
import com.example.channel.present.SiteContentPresent;
import com.example.channel.present.impl.SiteContentPresentImpl;
import com.example.channel.utils.CommonUtil;
import com.example.channel.utils.GsonUtils;
import com.example.channel.utils.ImageUtil;
import com.example.channel.utils.LoadDialog;
import com.example.channel.view.AddSiteView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import butterknife.BindView;
import butterknife.OnClick;

public class AddSiteActivity extends BaseActivity implements AddSiteView {

    @BindView(R.id.lv_add_site)
    public ListView lv_add_site;
    @BindView(R.id.tv_diangan)
    public TextView tv_diangan;

    private int position = -1;

    private List<SiteContentModelImpl> siteContentModelList = new ArrayList<>();
    private AddSiteAdapter addSiteAdapter;
    private String materials = "";//材料
    private String add_materials = "";//补充材料
    private int rod_number = 0;//-2表示不可操作；=-1表示编辑；其他 判断当前工单里这是第几个点，如果是第二个点就传1，第三个点就传2，以此类推
    private String rod_number_parent = "";//空表示不是子节点，有表示为子节点
    private SiteContentPresent siteContentPresent;
    private String latitude,longtitude,addr;//维度，经度，地址
    private ArrayList<String> urls = new ArrayList<>();//图片路径
    private SiteDetailModelImpl site;
    private String task_id ="";
    private LoadDialog loadDialog;
    private String point_id_parent;
    private int state = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addView(R.layout.activity_add_site, true);
        rod_number = getIntent().getExtras().getInt("rod_number");
        rod_number_parent = getIntent().getExtras().getString("rod_number_parent");
        state = getIntent().getExtras().getInt("state");
        if (!TextUtils.isEmpty(rod_number_parent)){
            point_id_parent = getIntent().getExtras().getString("point_id_parent");
        }
        if (rod_number >= 0){
            task_id = getIntent().getExtras().getString("task_id");
            tv_title.setText("新建选点");
        }else {
            if (rod_number == -2){
                tv_submit.setVisibility(View.GONE);
                tv_title.setText("选点详情");
            }else {
                tv_title.setText("修改选点");
            }

            site = GsonUtils.GsonToBean(getIntent().getExtras().getString("site"), SiteDetailModelImpl.class);
            materials = CommonUtil.getMaterials(site.getMaterials());
            add_materials = site.getAdd_materials();
            latitude = site.getLat();
            longtitude = site.getLont();
            addr = site.getAddress();
            getUrls();
            task_id = site.getTask_id();
            if (TextUtils.isEmpty(rod_number_parent)){
                tv_add.setVisibility(View.VISIBLE);
                tv_add.setText("子选点");
                if (!site.getPoint_type().equals("起始点"))
                    tv_diangan.setVisibility(View.VISIBLE);
            }
        }
        loadDialog = new LoadDialog(this);
        siteContentPresent = new SiteContentPresentImpl(this, new SiteContentModelImpl(AddSiteActivity.this, rod_number == -2 ? -1 : rod_number, rod_number_parent, site));
        siteContentPresent.showSiteContent();
    }

    private void getUrls(){
        String[] u = site.getImages().split(";");
        urls = new ArrayList<>();
        for (String url : u){
            urls.add(HttpManager.IMG_URL + url);
        }
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
                    case 4:
                    case 5:
                    case 6:
                    case 2:
                    case 3:
                        if (rod_number == -2 && i != 6)
                            break;
                        if (i == 3){
                            if (!(rod_number == 0 || siteContentModels.get(1).getContents().equals("终止点") || (site != null && site.getPoint_type().equals("起始点")))){
                                return;
                            }
                        }
                        bundle = new Bundle();
                        bundle.putString("content", siteContentModels.get(i).getContents());
                        bundle.putString("title", siteContentModels.get(i).getName());
                        if (i == 3){
                            bundle.putString("rod_number_parent", rod_number_parent);
                            bundle.putString("rod_number", rod_number == -1 ? site.getRod_number() : rod_number +"");
                        }
                        if (i == 6 && rod_number == -2)
                            bundle.putString("rod_number", rod_number +"");
                        bundle.putInt("position", i);
                        in = new Intent(AddSiteActivity.this, SelectActivity.class);
                        in.putExtras(bundle);
                        startActivityForResult(in, App.SITE_LIST);
                        break;
                    case 0://输入框
                    case 1://输入框
                    case 10://输入框（档距）
                        if (rod_number == -2)
                            break;
                        if ((rod_number < 0 || !TextUtils.isEmpty(rod_number_parent)) && i != 10){
                            break;
                        }
                        if (rod_number > 0 && i == 0){
                            break;
                        }
                        bundle = new Bundle();
                        bundle.putString("title", siteContentModels.get(i).getName());
                        if (rod_number == 0 || i == 10){
                            bundle.putString("content", siteContentModels.get(i).getContents());
                            bundle.putString("title", siteContentModels.get(i).getName());
                            in = new Intent(AddSiteActivity.this, EditActivity.class);
                            in.putExtras(bundle);
                            startActivityForResult(in, App.SITE_EDIT);
                        }else {
                            bundle.putInt("position", i);
                            bundle.putString("content", siteContentModels.get(i).getContents());
                            bundle.putString("title", siteContentModels.get(i).getName());
                            in = new Intent(AddSiteActivity.this, SelectActivity.class);
                            in.putExtras(bundle);
                            startActivityForResult(in, App.SITE_LIST);
                        }
                        break;
                    case 7://选择材料
                    case 8://附加材料
                        bundle = new Bundle();
                        if (i == 7){
                            bundle.putString("materials", materials);
                            bundle.putInt("type", 1);
                        }else {
                            bundle.putString("materials", add_materials);
                            bundle.putInt("type", 2);
                        }
                        bundle.putInt("rod_number", rod_number);
                        bundle.putString("rod_num", siteContentModels.get(3).getContents());
                        in = new Intent(AddSiteActivity.this, MaterialActivity.class);
                        in.putExtras(bundle);
                        startActivityForResult(in, App.SITE_MATERIAL);
                        break;
                    case 9://定位拍照
                        bundle = new Bundle();
                        bundle.putString("address", CommonUtil.getStr(addr));
                        bundle.putString("lont", CommonUtil.getStr(longtitude));
                        bundle.putString("lat", CommonUtil.getStr(latitude));
                        bundle.putStringArrayList("urls", urls);
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
            siteContentModelList.get(position).setContents(data.getStringExtra("content"));
        }else if (requestCode == App.SITE_EDIT && resultCode == App.SITE_EDIT){//输入框
            siteContentModelList.get(position).setContents(data.getStringExtra("content"));
        }else if (requestCode == App.SITE_MATERIAL && resultCode == App.SITE_MATERIAL){//材料
            if (position == 7){
                materials = data.getStringExtra("materials");
                siteContentModelList.get(position).setContents(materials);
            }
            else{
                add_materials = data.getStringExtra("materials");
                siteContentModelList.get(position).setContents(add_materials);
            }
        }else if (requestCode == App.SITE_PHONE && resultCode == App.SITE_PHONE){//拍照定位
            addr = data.getStringExtra("address");
            longtitude = data.getStringExtra("lont");
            latitude = data.getStringExtra("lat");
            urls = data.getStringArrayListExtra("urls");
            siteContentModelList.get(position).setContents(addr);
        }
        addSiteAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.tv_submit)
    void OnSubmit(){
        if (rod_number == -1 && urls.size() > 0 && !isUpdateUrl())
            siteContentPresent.submit(rodNum(), point_id_parent, longtitude, latitude, ImageUtil.getImgNames(urls), addr,
                    materials, add_materials, siteContentModelList, site.getPoint_id(), task_id, true);
        else
            siteContentPresent.uploadImage(urls, true);
    }

    @OnClick(R.id.tv_add)
    void OnSonPointList(){
        Bundle bundle = new Bundle();
        bundle.putString("task_id", task_id);
        bundle.putInt("state", state);
        bundle.putString("rod_number_parent", site.getRod_number());
        bundle.putString("point_id_parent", site.getPoint_id());
        gotoActivity(SiteDetailActivity.class, false, bundle);
    }

    @OnClick(R.id.tv_diangan)
    void OnGoDianGan(){
        if (TextUtils.isEmpty(task_id))
            return;
        Bundle bundle = new Bundle();
        bundle.putString("taskId", task_id);
        gotoActivity(DianganActivity.class, false, bundle);
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
    public void submit(String taskId) {
//        back();
        Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
        EventBus.getDefault().post(new MainModelImpl());
        if (TextUtils.isEmpty(taskId))
            back();
        else {
            goWeb(taskId);
        }
    }

    private void goWeb(String taskId){
        AlertDialog alertDialog2 = new AlertDialog.Builder(this)
                .setTitle("请在下面的界面中完善电杆走向")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Bundle bundle = new Bundle();
                        bundle.putString("taskId", taskId);
                        gotoActivity(DianganActivity.class, true, bundle);
                    }
                })

                .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        back();
                    }
                })
                .create();
        alertDialog2.show();
    }

    @Override
    public void uploadImg(String images) {
        siteContentPresent.submit(rod_number == -1 ? rodNum() : rod_number, point_id_parent, longtitude, latitude,
                images, addr, materials, add_materials, siteContentModelList, site == null ? "" : site.getPoint_id(), task_id, false);
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

    private boolean isUpdateUrl(){
        for (String url : urls){
            if (!url.contains("http"))
                return true;
        }
        return false;
    }
}
