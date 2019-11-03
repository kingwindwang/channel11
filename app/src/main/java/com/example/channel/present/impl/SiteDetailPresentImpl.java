package com.example.channel.present.impl;

import com.example.channel.model.SiteDetailModel;
import com.example.channel.model.impl.SiteDetailModelImpl;
import com.example.channel.present.SiteDetailPresent;
import com.example.channel.view.SiteDetailView;

import java.util.List;

public class SiteDetailPresentImpl implements SiteDetailPresent, SiteDetailModel.OnSiteDetailListener {

    private SiteDetailView siteDetailView;
    private SiteDetailModelImpl siteDetailModel;
    private boolean isShowLoad;

    public SiteDetailPresentImpl(SiteDetailView siteDetailView, SiteDetailModelImpl siteDetailModel){
        this.siteDetailView = siteDetailView;
        this.siteDetailModel = siteDetailModel;
    }

    @Override
    public void showSiteDetail(String task_id, String pid, boolean isShowLoad) {
        this.isShowLoad = isShowLoad;
        siteDetailModel.findSiteDetail(task_id, pid, this);
        if (isShowLoad)
            siteDetailView.showProgress();
    }

    @Override
    public void delSite(String task_id, boolean isShowLoad) {
        this.isShowLoad = isShowLoad;
        siteDetailModel.delSite(task_id, this);
        if (isShowLoad)
            siteDetailView.showProgress();
    }

    @Override
    public void findSiteDetail(List<SiteDetailModelImpl> siteDetailModels) {
        siteDetailView.findSiteDetail(siteDetailModels);
        if (isShowLoad)
            siteDetailView.hideProgress();
    }

    @Override
    public void fail(String msg) {
        siteDetailView.fail(msg);
        if (isShowLoad)
            siteDetailView.hideProgress();
    }

    @Override
    public void delSuccess() {
        siteDetailView.delSiteSuccess();
        if (isShowLoad)
            siteDetailView.hideProgress();
    }
}
