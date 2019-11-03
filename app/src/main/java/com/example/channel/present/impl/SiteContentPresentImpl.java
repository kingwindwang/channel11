package com.example.channel.present.impl;

import android.text.TextUtils;

import com.example.channel.App;
import com.example.channel.model.OnListener;
import com.example.channel.model.SiteContentModel;
import com.example.channel.model.impl.MaterialModelImpl;
import com.example.channel.model.impl.SiteContentModelImpl;
import com.example.channel.model.impl.UserModeImpl;
import com.example.channel.present.SiteContentPresent;
import com.example.channel.utils.GsonUtils;
import com.example.channel.view.AddSiteView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SiteContentPresentImpl implements SiteContentPresent, SiteContentModel.OnSiteContentListener, OnListener {

    private AddSiteView siteView;
    private SiteContentModel siteContentModel;
    private boolean isShowLoad;

    public SiteContentPresentImpl(AddSiteView siteView, SiteContentModel siteContentModel){
        this.siteView = siteView;
        this.siteContentModel = siteContentModel;
    }

    @Override
    public void siteContents(List<SiteContentModelImpl> siteContentModels) {
        siteView.findSiteContent(siteContentModels);
    }

    @Override
    public void submitSuccess() {
        siteView.submit();
        if (isShowLoad)
            siteView.hideProgress();
    }

    @Override
    public void submitFail(String msg) {
        siteView.fial(msg);
        if (isShowLoad)
            siteView.hideProgress();
    }

    @Override
    public void showSiteContent() {
        siteContentModel.findSiteContent(this);
    }

    @Override
    public void submit(int rod_number, String pid, String lont, String lat, String images, String address, String materials, List<SiteContentModelImpl> siteContentModelList, String point_id, String task_id, boolean isShowLoad) {
        if (!this.isShowLoad){
            this.isShowLoad = isShowLoad;
            if (isShowLoad)
                siteView.showProgress();
        }


        Map<String, Object> param = new HashMap<>();
        UserModeImpl user = App.application.getUser();
        if (!TextUtils.isEmpty(point_id)){//修改
            param.put("method", "update_point");
            param.put("point_id", point_id);
        }else if (rod_number == 0){//添加起始点
            param.put("method", "create_task");
            param.put("user_id", user.getUser_id());
            param.put("user_name", user.getName());
            param.put("dep_id", user.getDep_id());
            param.put("dep_name", user.getDep_name());
            param.put("line_name", siteContentModelList.get(1).getContents()[0]);
        }else{//添加站点
            param.put("method", "create_point");
            param.put("task_id", task_id);
            param.put("point_type", siteContentModelList.get(1).getContents()[siteContentModelList.get(1).getSelectPosition()]);
        }
        if (!TextUtils.isEmpty(pid))
            param.put("pid", pid);
        param.put("voltage_level", siteContentModelList.get(2).getContents()[siteContentModelList.get(2).getSelectPosition()]);
        param.put("rod_number", siteContentModelList.get(3).getContents()[siteContentModelList.get(3).getSelectPosition()]);
        param.put("same_rod_flag", siteContentModelList.get(4).getContents()[siteContentModelList.get(4).getSelectPosition()]);
        param.put("rod_type", siteContentModelList.get(5).getContents()[siteContentModelList.get(5).getSelectPosition()]);
        param.put("cross", siteContentModelList.get(6).getContents()[siteContentModelList.get(6).getSelectPosition()]);
        param.put("lont", lont);
        param.put("lat", lat);
        param.put("images", images);
        param.put("address", address);
        param.put("materials", getMaterialStr(materials));
        siteContentModel.submit(param, this);
    }

    @Override
    public void uploadImage(String url, boolean isShowLoad) {
        this.isShowLoad = isShowLoad;
        if (isShowLoad)
            siteView.showProgress();
        siteContentModel.uploadImage(url, this);
    }

    private String getMaterialStr(String materials){
        List<MaterialModelImpl.Material1> material1List = new ArrayList<>();
        if (!TextUtils.isEmpty(materials))
            material1List = GsonUtils.jsonToList(materials, MaterialModelImpl.Material1.class);
        StringBuffer sp = new StringBuffer();
        for (int i = 0; i < material1List.size(); i++){
            sp.append(material1List.get(i).getMaterials_id());
            sp.append(":");
            sp.append(material1List.get(i).getName());
            sp.append(":");
            sp.append(material1List.get(i).getMaterialNum());
            if (i < material1List.size() -1)
                sp.append(";");
        }
        return sp.toString();
    }

    @Override
    public void onSuccess(Object success) {
        siteView.uploadImg((String) success);
    }

    @Override
    public void onFail(String msg) {
        siteView.fial(msg);
        if (isShowLoad)
            siteView.hideProgress();
    }
}
