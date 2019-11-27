package com.example.channel.model.impl;

import android.text.TextUtils;

import com.example.channel.App;
import com.example.channel.https.HttpCallBack;
import com.example.channel.https.HttpManager;
import com.example.channel.https.HttpServer;
import com.example.channel.https.ResponseBase;
import com.example.channel.model.SiteDetailModel;
import com.example.channel.utils.GsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

public class SiteDetailModelImpl implements SiteDetailModel {

    private String point_id;
    private String task_id;
    private String create_time;
    private String point_type;// 点类型（可选：普通点、终止点）
    private String line_name;// 线路名称(文本框填写)
    private String voltage_level;// 电压等级-（选择10Kv、220V、400V）
    private String rod_number;// 杆号，这里传0
    private String same_rod_flag;// 是否有同杆-（选择是、否-400V、否-220V）
    private String rod_type;// $_REQUEST["rod_type"];  选择 转角耐张、直线耐张、直线、转角、分支（400V分400V、220V分220V）、转换（400V转220V）、终端、门杆
    private String cross;// 跨越/穿越/带电（选择公路、通讯、河流、电力）
    private String lont;// $_REQUEST["lont"];  经度
    private String lat;// $_REQUEST["lat"];   纬度
    private String address;// $_REQUEST["address"];   定位地址
    private String images;// $_REQUEST["images"];   图片名称（随机uuid.jpg）
    private String materials;// $_REQUEST["materials"];   材料列表，格式如下：材料1:数量1;材料2:数量2;....
    private String add_materials;//附加材料
    private String line1;
    private String line2;
    private String line3;
    private String line4;
    private String remark;


    private List<SiteDetailModelImpl> siteDetailModels;
    @Override
    public void findSiteDetail(String task_id, String pid, OnSiteDetailListener onSiteDetailListener) {
        siteDetailModels = new ArrayList<>();
        HttpManager httpManager = HttpManager.getInstance();
        Map<String, Object> params = new HashMap<>();
        params.put("task_id", task_id);
        if (TextUtils.isEmpty(pid))//父选点列表
            params.put("method", "point_list");
        else {//子选点列表
            params.put("method", "add_point_list");
            params.put("pid", pid);
        }
        httpManager.callData(params, false, new HttpCallBack() {
            @Override
            public void onSuccess(Object success) {
                String s = GsonUtils.GsonString(success);
                siteDetailModels = GsonUtils.jsonToList(s, SiteDetailModelImpl.class);
                onSiteDetailListener.findSiteDetail(siteDetailModels);
            }

            @Override
            public void onFail(String fail) {
                onSiteDetailListener.fail(fail);
            }
        });
    }

    @Override
    public void delSite(String task_id, OnSiteDetailListener onSiteDetailListener) {
        HttpManager httpManager = HttpManager.getInstance();
        Map<String, Object> params = new HashMap<>();
        params.put("task_id", task_id);
        params.put("method", "del_task");
        httpManager.callData(params, false, new HttpCallBack() {
            @Override
            public void onSuccess(Object success) {
                onSiteDetailListener.delSuccess();
            }

            @Override
            public void onFail(String fail) {
                onSiteDetailListener.fail(fail);
            }
        });
    }

    public String getPoint_type() {
        return point_type;
    }

    public void setPoint_type(String point_type) {
        this.point_type = point_type;
    }

    public String getLine_name() {
        return line_name;
    }

    public void setLine_name(String line_name) {
        this.line_name = line_name;
    }

    public String getVoltage_level() {
        return voltage_level;
    }

    public void setVoltage_level(String voltage_level) {
        this.voltage_level = voltage_level;
    }

    public String getRod_number() {
        return rod_number;
    }

    public void setRod_number(String rod_number) {
        this.rod_number = rod_number;
    }

    public String getSame_rod_flag() {
        return same_rod_flag;
    }

    public void setSame_rod_flag(String same_rod_flag) {
        this.same_rod_flag = same_rod_flag;
    }

    public String getRod_type() {
        return rod_type;
    }

    public void setRod_type(String rod_type) {
        this.rod_type = rod_type;
    }

    public String getCross() {
        return cross;
    }

    public void setCross(String cross) {
        this.cross = cross;
    }

    public String getLont() {
        return lont;
    }

    public void setLont(String lont) {
        this.lont = lont;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getMaterials() {
        return materials;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getLine3() {
        return line3;
    }

    public void setLine3(String line3) {
        this.line3 = line3;
    }

    public String getLine4() {
        return line4;
    }

    public void setLine4(String line4) {
        this.line4 = line4;
    }

    public String getPoint_id() {
        return point_id;
    }

    public void setPoint_id(String point_id) {
        this.point_id = point_id;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getAdd_materials() {
        return add_materials;
    }

    public void setAdd_materials(String add_materials) {
        this.add_materials = add_materials;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
