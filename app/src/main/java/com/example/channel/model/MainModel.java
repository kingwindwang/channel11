package com.example.channel.model;

import com.example.channel.model.impl.MainModelImpl;
import com.example.channel.model.impl.SiteModelImpl;
import com.example.channel.model.impl.TaskModelImpl;

import java.util.List;

public interface MainModel {

    void findMain(OnMainListener listener);

    interface OnMainListener{
        void taskSuccess(MainModelImpl mainModel);
        void fial(String msg);
    }
}
