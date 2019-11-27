package com.example.channel.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.channel.R;

/**
 * Created by Administrator on 2019/10/11 0011.
 */

public class MaterialNumDialog {

    private Activity context;
    private Dialog dialog;
    private EditText etName, etNum;
    private Button btnCancel, btnOk;
    private OnLintener onLintener;

    public MaterialNumDialog(Activity context){
        this.context = context;
    }

    public MaterialNumDialog builder(int num){
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_num_material, null);

        etNum = view.findViewById(R.id.et_num);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnOk = view.findViewById(R.id.btn_ok);

        etNum.setText(num + "");

        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);

        // 调整dialog背景大小
        view.setLayoutParams(new FrameLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT));

        dialog.show();
        dialog.setCanceledOnTouchOutside(true);


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLintener.onOk(CommonUtil.getInt(CommonUtil.getEdit(etNum)));
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return this;
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public void setOnLintener(OnLintener onLintener){
        this.onLintener = onLintener;
    }

    public interface OnLintener{
        void onOk(int num);
    }
}
