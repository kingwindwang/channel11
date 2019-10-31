package com.example.channel.view;

import com.example.channel.model.impl.MaterialModelImpl;
import com.example.channel.model.impl.SiteContentModelImpl;

import java.util.List;

public interface MaterialAddView extends BaseView{

    void findMaterialList(List<MaterialModelImpl> materialModels);
    void submit(String materialStr);
    void fail(String msg);
}
