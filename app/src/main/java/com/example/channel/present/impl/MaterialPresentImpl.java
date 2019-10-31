package com.example.channel.present.impl;

import com.example.channel.model.MaterialModel;
import com.example.channel.model.impl.MaterialModelImpl;
import com.example.channel.present.MaterialPresent;
import com.example.channel.utils.GsonUtils;
import com.example.channel.view.MaterialAddView;

import java.util.ArrayList;
import java.util.List;

public class MaterialPresentImpl implements MaterialPresent, MaterialModel.OnMaterialListener {

    private MaterialAddView materialAddView;
    private MaterialModel materialModel;
    private boolean isShowLoad;

    public MaterialPresentImpl(MaterialAddView materialAddView, MaterialModel materialModel){
        this.materialAddView = materialAddView;
        this.materialModel = materialModel;
    }

    @Override
    public void materialList(List<MaterialModelImpl> materialModels) {
        materialAddView.findMaterialList(materialModels);
        if (isShowLoad)
            materialAddView.hideProgress();
    }

    @Override
    public void fail(String msg) {
        materialAddView.fail(msg);
        if (isShowLoad)
            materialAddView.hideProgress();
    }

    @Override
    public void showMaterial(boolean isShowLoad) {
        this.isShowLoad = isShowLoad;
        if (isShowLoad)
            materialAddView.showProgress();
        materialModel.findMaterialList(this);
    }

    @Override
    public void submit(List<MaterialModelImpl> materialModels) {
        List<MaterialModelImpl.Material1> selectMaterials = new ArrayList<>();
        for (int i = 0; i < materialModels.size(); i++){
            for (int j = 0; j < materialModels.get(i).getList().size(); j++){
                if (materialModels.get(i).getList().get(j).getMaterialNum() > 0)
                    selectMaterials.add(materialModels.get(i).getList().get(j));
            }
        }
        String materials = GsonUtils.GsonString(selectMaterials);
        materialAddView.submit(materials);
    }
}
