package com.example.channel.model;

import com.example.channel.model.impl.SiteContentModelImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface SiteContentModel {

    void findSiteContent(OnSiteContentListener siteContentListener);
    void submit(Map<String, Object> param, OnSiteContentListener listener);
    void uploadImage(ArrayList<String> urls, OnListener listener);

    interface OnSiteContentListener{
        void siteContents(List<SiteContentModelImpl> siteContentModels);
        void submitSuccess();
        void submitFail(String msg);
    }
}
