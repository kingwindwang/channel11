package com.example.channel.view;

import com.example.channel.model.impl.SiteContentModelImpl;

import java.util.List;

public interface AddSiteView extends BaseView{

    void findSiteContent(List<SiteContentModelImpl> siteContentModels);
    void submit(String taskId);
    void uploadImg(String images);
    void fial(String msg);
}
