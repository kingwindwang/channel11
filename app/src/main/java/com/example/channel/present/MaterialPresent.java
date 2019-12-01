package com.example.channel.present;

import com.example.channel.model.impl.MaterialModelImpl;

import java.util.List;

public interface MaterialPresent {

    void showMaterial(boolean isShowLoad, String rod_number);
    void submit(List<MaterialModelImpl> materialModels);
}
