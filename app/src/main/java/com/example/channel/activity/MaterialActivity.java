package com.example.channel.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.channel.App;
import com.example.channel.R;
import com.example.channel.adapter.MaterialAdapter;
import com.example.channel.model.impl.MaterialModelImpl;
import com.example.channel.utils.AddMaterialDialog;
import com.example.channel.utils.CommonUtil;
import com.example.channel.utils.GsonUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import butterknife.BindView;
import butterknife.OnClick;

public class MaterialActivity extends BaseActivity{

    private List<MaterialModelImpl.Material1> material1s = new ArrayList<>();

    @BindView(R.id.lv_material)
    public ListView lvMaterial;
    @BindView(R.id.tv_submit_material)
    public TextView tv_submit_material;

    private String materials;
    private MaterialAdapter materialAdapter;
    private int type = 1;//1:材料；2：附加材料
    private AddMaterialDialog dialog;
    private String rod_num;//选择的杆号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addView(R.layout.activity_material, true);
        Bundle bundle = getIntent().getExtras();
        int rod_number = bundle.getInt("rod_number", 0);
        rod_num = bundle.getString("rod_num");
        if (rod_number == -2){
            tv_submit.setVisibility(View.GONE);
            tv_submit_material.setVisibility(View.GONE);
        }
        tv_submit.setText("添加");
        type = getIntent().getIntExtra("type", 1);
        materials = getIntent().getStringExtra("materials");
        if (type == 1)
            tv_title.setText("材料列表");
        else{
            dialog = new AddMaterialDialog(this);
            tv_title.setText("附加材料列表");
            material1s = getMaterial1s();
        }



        setAdapter();
    }

    private void setAdapter(){
        if (!TextUtils.isEmpty(materials) && type == 1){
                material1s = GsonUtils.jsonToList(materials, MaterialModelImpl.Material1.class);
        }

        materialAdapter = new MaterialAdapter(this, material1s);
        lvMaterial.setAdapter(materialAdapter);
        if (type == 2){
            lvMaterial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    addfujia(material1s.get(position).getName(), material1s.get(position).getMaterialNum()+"", position);
                }
            });

            lvMaterial.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    goDel(position);
                    return true;
                }
            });
        }
    }

    @OnClick({R.id.tv_submit})
    void OnAdd(){
        if (type == 1){
            goMaterial();
        }else {
            addfujia("", "", -1);
        }

    }

    //选择材料
    private void goMaterial(){
        Bundle bundle = new Bundle();
        bundle.putString("materials", materials);
        bundle.putString("rod_num", rod_num);
        Intent in = new Intent(MaterialActivity.this, MaterialAddActivity.class);
        in.putExtras(bundle);
        startActivityForResult(in, App.SITE_MATERIAL);
    }

    private void addfujia(String name, String num, final int i){
        AddMaterialDialog dialog = new AddMaterialDialog(this);
        dialog.builder(name, num);
        dialog.setOnLintener(new AddMaterialDialog.OnLintener() {
            @Override
            public void onOk(String name, int num) {
                if (i == -1){
                    MaterialModelImpl.Material1 a = new MaterialModelImpl.Material1();
                    a.setName(name);
                    a.setMaterialNum(num);
                    material1s.add(a);
                }else {
                    material1s.get(i).setName(name);
                    material1s.get(i).setMaterialNum(num);
                }

                setAdapter();
            }
        });
    }

    private void goDel(int j){
        AlertDialog alertDialog2 = new AlertDialog.Builder(this)
                .setTitle("是否删除")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        material1s.remove(j);
                        setAdapter();
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

    @OnClick({R.id.tv_submit_material})
    void OnSubmitMaterial(){
        Intent in = new Intent();
        if (type == 1)
            in.putExtra("materials", materials);
        else
            in.putExtra("materials", getMaterStr());
        setResult(App.SITE_MATERIAL, in);
        back();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        if (requestCode == App.SITE_MATERIAL && resultCode == App.SITE_MATERIAL){
            material1s = new ArrayList<>();
            materials = data.getStringExtra("materials");
            setAdapter();
        }
    }

    private List<MaterialModelImpl.Material1> getMaterial1s(){
        List<MaterialModelImpl.Material1> list = new ArrayList<>();
        if (TextUtils.isEmpty(materials))
            return list;
        String[] a = materials.split(";");
        for (String b : a){
            MaterialModelImpl.Material1 map = new MaterialModelImpl.Material1();
            map.setName(b.split(":")[0]);
            map.setMaterialNum(CommonUtil.getInt(b.split(":")[1]));
            list.add(map);
        }
        return list;
    }

    private String getMaterStr(){
        if (material1s == null || material1s.size() == 0)
            return "";
        StringBuffer buffer = new StringBuffer();
        for (MaterialModelImpl.Material1 material1 : material1s){
            buffer.append(material1.getName());
            buffer.append(":");
            buffer.append(material1.getMaterialNum());
            buffer.append(";");
        }
        return buffer.toString().substring(0, buffer.length()-1);
    }
}
