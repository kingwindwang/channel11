package com.example.channel.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.channel.R;

/**
 * Created by Administrator on 2019/10/11 0011.
 */

public class LoadDialog {

    private Activity context;
    private Dialog dialog;

    public LoadDialog(Activity context){
        this.context = context;
    }

    public LoadDialog builder(){
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.layout_load, null);

        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);

        // 调整dialog背景大小
        view.setLayoutParams(new FrameLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT));

        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        return this;
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
