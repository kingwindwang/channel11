package com.example.channel.present;

import com.example.channel.model.impl.MaterialModelImpl;

import java.util.List;

public interface MaterialPresent {

    void showMaterial(boolean isShowLoad);
    void submit(List<MaterialModelImpl> materialModels);
}
