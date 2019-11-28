package com.example.channel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.channel.App;
import com.example.channel.R;
import com.example.channel.adapter.Material1Adapter;
import com.example.channel.adapter.Material2Adapter;
import com.example.channel.model.impl.MaterialModelImpl;
import com.example.channel.present.impl.MaterialPresentImpl;
import com.example.channel.utils.LoadDialog;
import com.example.channel.utils.MaterialNumDialog;
import com.example.channel.view.MaterialAddView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MaterialAddActivity extends BaseActivity implements MaterialAddView {

    @BindView(R.id.lv_material1)
    public ListView lv_material1;

    @BindView(R.id.lv_material2)
    public ListView lv_material2;

    private int parentPosition = 0;
    private Material1Adapter material1Adapter;
    private Material2Adapter material2Adapter;
    private List<MaterialModelImpl> materialModelList = new ArrayList<>();
    private List<MaterialModelImpl.Material1> material1s = new ArrayList<>();
    private String materials;
    private MaterialPresentImpl materialPresent;
    private LoadDialog loadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addView(R.layout.activity_material_add, true);
        materials = getIntent().getStringExtra("materials");
        loadDialog = new LoadDialog(this);
        materialPresent = new MaterialPresentImpl(this, new MaterialModelImpl(materials));
        materialPresent.showMaterial(true);

        tv_add.setVisibility(View.VISIBLE);
        tv_add.setText("帮助");
    }

    @Override
    public void findMaterialList(List<MaterialModelImpl> materialModels) {
        materialModelList = materialModels;
        material1Adapter = new Material1Adapter(this, materialModelList);
        lv_material1.setAdapter(material1Adapter);

        update2(true);

        lv_material1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                parentPosition = i;
                material1Adapter.selectPosition(parentPosition);
                material1Adapter.notifyDataSetChanged();
                update2(true);
            }
        });
    }

    public void jiajian(int position, int num){
        updateNum(position, num);
    }

    public void editNum(int position, int num){
        MaterialNumDialog dialog = new MaterialNumDialog(this);
        dialog.builder(num);
        dialog.setOnLintener(new MaterialNumDialog.OnLintener() {
            @Override
            public void onOk(int newNum) {
                updateNum(position, newNum);
            }
        });
    }

    private void updateNum(int position, int num){
        materialModelList.get(parentPosition).getList().get(position).setMaterialNum(num);
        update2(false);
    }

    private void update2(boolean isNew){
        material1s = materialModelList.get(parentPosition).getList();
        if (!isNew && material2Adapter != null){
            material2Adapter.notifyDataSetChanged();
        }else {
            material2Adapter = new Material2Adapter(MaterialAddActivity.this, material1s);
            lv_material2.setAdapter(material2Adapter);
        }

    }

    @OnClick(R.id.tv_submit)
    void OnSubmit(){
        materialPresent.submit(materialModelList);
    }

    @OnClick(R.id.tv_add)
    void OnHelp(){
        gotoActivity(HelpActivity.class);
    }

    @Override
    public void submit(String materialStr) {
        Intent in = new Intent();
        in.putExtra("materials", materialStr);
        setResult(App.SITE_MATERIAL, in);
        back();
    }

    @Override
    public void fail(String msg) {
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
