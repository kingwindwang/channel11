package com.example.channel.present;

import com.example.channel.model.impl.SiteContentModelImpl;

import java.util.ArrayList;
import java.util.List;

public interface SiteContentPresent {

    void showSiteContent();
    void submit(int rod_number, String pid, String lont, String lat, String images, String address, String materials, String add_materials, List<SiteContentModelImpl> siteContentModelList, String point_id, String task_id, boolean isShowLoad);
    void uploadImage(ArrayList<String> urls, boolean isShowLoad);
}
