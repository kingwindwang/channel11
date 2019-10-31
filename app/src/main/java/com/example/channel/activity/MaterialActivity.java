package com.example.channel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.channel.App;
import com.example.channel.R;
import com.example.channel.adapter.MaterialAdapter;
import com.example.channel.model.impl.MaterialModelImpl;
import com.example.channel.utils.GsonUtils;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addView(R.layout.activity_material, true);
        int rod_number = getIntent().getIntExtra("rod_number", 0);
        if (rod_number == -2){
            tv_submit.setVisibility(View.GONE);
            tv_submit_material.setVisibility(View.GONE);
        }
        tv_submit.setText("添加");
        tv_title.setText("材料列表");

        materials = getIntent().getStringExtra("materials");
        setAdapter();
    }

    private void setAdapter(){
        if (!TextUtils.isEmpty(materials))
            material1s = GsonUtils.jsonToList(materials, MaterialModelImpl.Material1.class);
        materialAdapter = new MaterialAdapter(this, material1s);
        lvMaterial.setAdapter(materialAdapter);
    }

    @OnClick({R.id.tv_submit})
    void OnAdd(){
        Bundle bundle = new Bundle();
        bundle.putString("materials", materials);
        Intent in = new Intent(MaterialActivity.this, MaterialAddActivity.class);
        in.putExtras(bundle);
        startActivityForResult(in, App.SITE_MATERIAL);
    }

    @OnClick({R.id.tv_submit_material})
    void OnSubmitMaterial(){
        Intent in = new Intent();
        in.putExtra("materials", materials);
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
}
