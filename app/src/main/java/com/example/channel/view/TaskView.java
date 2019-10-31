package com.example.channel.view;

import com.example.channel.model.impl.SiteModelImpl;

import java.util.List;

/**
 * Created by Administrator on 2019/10/10 0010.
 */

public interface TaskView extends BaseView{

    void findTask(List<SiteModelImpl> siteModels);
    void fail(String msg);
}
