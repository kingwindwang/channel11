package com.example.channel.view;

import com.example.channel.model.impl.SiteDetailModelImpl;

import java.util.List;

public interface SiteDetailView extends BaseView{

    void findSiteDetail(List<SiteDetailModelImpl> siteDetailModels);
    void fail(String msg);
    void delSiteSuccess();
}
