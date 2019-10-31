package com.example.channel.model;

import com.example.channel.model.impl.MaterialModelImpl;

import java.util.List;

public interface MaterialModel {

    void findMaterialList(OnMaterialListener materialListener);

    interface OnMaterialListener{
        void materialList(List<MaterialModelImpl> materialModels);
        void fail(String msg);
    }
}
