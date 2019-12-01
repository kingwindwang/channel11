package com.example.channel.model.impl;

import android.text.TextUtils;

import com.example.channel.App;
import com.example.channel.https.HttpCallBack;
import com.example.channel.https.HttpManager;
import com.example.channel.https.HttpServer;
import com.example.channel.https.ResponseBase;
import com.example.channel.model.MaterialModel;
import com.example.channel.utils.GsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

public class MaterialModelImpl implements MaterialModel {

    private String materials_id;
    private String name;
    private List<Material1> list = new ArrayList<>();
    private List<Material1> selectMaterials = new ArrayList<>();

    public MaterialModelImpl(String materials){
        if (!TextUtils.isEmpty(materials))
            selectMaterials = GsonUtils.jsonToList(materials, Material1.class);
    }

    public MaterialModelImpl(){

    }

    List<MaterialModelImpl> materialModels;
    @Override
    public void findMaterialList(OnMaterialListener materialListener, String rod_number) {
        materialModels = new ArrayList<>();
        HttpManager httpManager = HttpManager.getInstance();
        Map<String, Object> params = new HashMap<>();
        params.put("method", "materials_list");
        params.put("rod_number", rod_number);
        httpManager.callData(params, false, new HttpCallBack() {
            @Override
            public void onSuccess(Object success) {
                String s = GsonUtils.GsonString(success);
                materialModels = GsonUtils.jsonToList(s, MaterialModelImpl.class);
                if (selectMaterials.size() > 0){
                    for (int k = 0; k < selectMaterials.size(); k++){
                        for (int i = 0; i < materialModels.size(); i++){
                            for (int j = 0; j < materialModels.get(i).getList().size(); j++){
                                if (selectMaterials.get(k).getMaterials_id().equals(materialModels.get(i).getList().get(j).getMaterials_id())){
                                    materialModels.get(i).getList().get(j).setMaterialNum(selectMaterials.get(k).getMaterialNum());
                                }
                            }
                        }
                    }
                }
                materialListener.materialList(materialModels);
            }

            @Override
            public void onFail(String fail) {
                materialListener.fail(fail);
            }
        });
    }

    public static class Material1{
        private String materials_id;
        private String name;
        private int materialNum = 0;

        public String getMaterials_id() {
            return materials_id;
        }

        public void setMaterials_id(String materials_id) {
            this.materials_id = materials_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getMaterialNum() {
            return materialNum;
        }

        public void setMaterialNum(int materialNum) {
            this.materialNum = materialNum;
        }
    }

    public String getMaterials_id() {
        return materials_id;
    }

    public void setMaterials_id(String materials_id) {
        this.materials_id = materials_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Material1> getList() {
        return list;
    }

    public void setList(List<Material1> list) {
        this.list = list;
    }
}
