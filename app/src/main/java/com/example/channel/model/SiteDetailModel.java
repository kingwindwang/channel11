package com.example.channel.model;

import com.example.channel.model.impl.SiteDetailModelImpl;

import java.util.List;

public interface SiteDetailModel {

    void findSiteDetail(String task_id, OnSiteDetailListener onSiteDetailListener);
    void delSite(String task_id, OnSiteDetailListener onSiteDetailListener);

    interface OnSiteDetailListener{
        void findSiteDetail(List<SiteDetailModelImpl> siteDetailModels);
        void fail(String msg);
        void delSuccess();
    }
}
