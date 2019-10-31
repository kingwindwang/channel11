package com.example.channel.model.impl;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import com.example.channel.App;
import com.example.channel.R;
import com.example.channel.https.HttpCallBack;
import com.example.channel.https.HttpManager;
import com.example.channel.https.HttpServer;
import com.example.channel.model.OnListener;
import com.example.channel.model.SiteContentModel;
import com.example.channel.utils.GsonUtils;
import com.example.channel.utils.ImageUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SiteContentModelImpl implements SiteContentModel {

    private String name;
    private String[] contents;
    private int selectPosition;
    private Context context;
    private int rod_number;
    private SiteDetailModelImpl site;

    public SiteContentModelImpl(Context context, int rod_number, SiteDetailModelImpl site){
        this.context = context;
        this.rod_number = rod_number;
        this.site = site;
    }

    public SiteContentModelImpl(){

    }

    private String getNames1(){
        if (rod_number != -1){
            return rod_number == 0 ? "路线名称" : "点类型";
        }else {
            return site.getRod_number().equals("0") ? "路线名称" : "点类型";
        }
    }

    @Override
    public void findSiteContent(OnSiteContentListener siteContentListener) {
        List<SiteContentModelImpl> siteContentModels = new ArrayList<>();

        String[] names = {"施工班组", getNames1(), "电压等级", "杆号", "是否有同杆", "杆型", "跨越/穿越/带电"
                , "添加材料", "附件及定位"};
        Resources res =context.getResources();
        List<String[]> strings = new ArrayList<>();
        UserModeImpl user = App.application.getUser();
        String[] listnull = {""};
        String[] dep = {user.getDep_name()};
        strings.add(dep);
        String[] rod_num = {rod_number == -1 ? site.getRod_number() : rod_number +""};
        if (rod_num[0] .equals("0")){
            if (site != null && !TextUtils.isEmpty(site.getLine_name())){
                String[] lineName = {site.getLine_name()};
                strings.add(lineName);
            }else {
                strings.add(listnull);
            }

        }else {
            strings.add(res.getStringArray(R.array.list1));
        }
        strings.add(res.getStringArray(R.array.list3));
        String[] rod_num1 = {rod_num[0], "利旧", "变电站"};
        strings.add(rod_num1);
        strings.add(res.getStringArray(R.array.list5));
        strings.add(res.getStringArray(R.array.list6));
        strings.add(res.getStringArray(R.array.list7));
        strings.add(listnull);
        strings.add(listnull);
        for (int i = 0; i < names.length; i++){
            SiteContentModelImpl siteContentModel = new SiteContentModelImpl();
            siteContentModel.name = names[i];
            siteContentModel.contents = strings.get(i);
            if (rod_number == -1 && siteContentModel.contents.length > 1){
                for (int k = 0; k < siteContentModel.contents.length; k++){
                    if (siteContentModel.contents[k].equals(getSite(i))){
                        siteContentModel.selectPosition = k;
                        break;
                    }
                }
            }else {
                siteContentModel.selectPosition = 0;
            }
            siteContentModels.add(siteContentModel);
        }
        siteContentListener.siteContents(siteContentModels);
    }

    private String getSite(int k){
        String siteStr = "";
        switch (k){
            case 1://点类型（可选：普通点、终止点）
                if (!site.getRod_number().equals("0"))
                    siteStr = site.getPoint_type();
                break;
            case 2://电压等级-（选择10Kv、220V、400V）
                siteStr = site.getVoltage_level();
                break;
            case 3://杆号
                siteStr = site.getRod_number();
                break;
            case 4://是否有同杆-（选择是、否-400V、否-220V）
                siteStr = site.getSame_rod_flag();
                break;
            case 5://杆型：转角耐张、直线耐张、直线、转角、分支（400V分400V、220V分220V）、转换（400V转220V）、终端、门杆
                siteStr = site.getRod_type();
                break;
            case 6://跨越/穿越/带电（选择公路、通讯、河流、电力）
                siteStr = site.getCross();
                break;
        }
        return siteStr;
    }

    @Override
    public void submit(Map<String, Object> params, OnSiteContentListener listener) {
        HttpManager httpManager = HttpManager.getInstance();
        httpManager.callData(params, false, new HttpCallBack() {
            @Override
            public void onSuccess(Object success) {
                listener.submitSuccess();
            }

            @Override
            public void onFail(String fail) {
                listener.submitFail(fail);
            }
        });
    }

    @Override
    public void uploadImage(String url, OnListener listener) {
        HttpManager httpManager = HttpManager.getInstance();
        HttpServer httpServer = httpManager.retrofit.create(HttpServer.class);
        if (TextUtils.isEmpty(url)){
            listener.onFail("文件不能为空");
            return;
        }
        File file = new File(url);
        if (!file.exists()){
            listener.onFail("文件不存在");
            return;
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse("text/plain"), file);
        MultipartBody.Part part =MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        RequestBody description = RequestBody.create( MediaType.parse("multipart/form-data"), "file_upload");
        // 执行请求
        Call<ResponseBody> call = httpServer.saveImage(part, description);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                try {
                    Map<String, Object> map = GsonUtils.GsonToMaps(response.body().string());
                    if ((Double) map.get("state") == 1){
                        listener.onSuccess(map.get("content"));
                    }else {
                        listener.onFail((String) map.get("message"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onFail(t.getMessage());
            }
        });
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getContents() {
        return contents;
    }

    public void setContents(String[] contents) {
        this.contents = contents;
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }
}
