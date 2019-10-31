package com.example.channel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.example.channel.App;
import com.example.channel.R;

import butterknife.BindView;
import butterknife.OnClick;

public class EditActivity extends BaseActivity{

    private String content;
    private String title;

    @BindView(R.id.et_content)
    public EditText et_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addView(R.layout.activity_edit, true);
        content = getIntent().getExtras().getString("content");
        title = getIntent().getExtras().getString("title");
        tv_title.setText(title);

        if (TextUtils.isEmpty(content))
            et_content.setHint("请输入" + title);
        else
            et_content.setText(content);
    }

    @OnClick(R.id.tv_submit)
    void onSubmit(){
        Intent in = new Intent();
        in.putExtra("content", et_content.getText().toString().trim());
        setResult(App.SITE_EDIT, in);
        back();
    }
}
