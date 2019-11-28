package com.example.channel.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ListView;

import com.example.channel.R;

import butterknife.BindView;

/**
 * 帮助
 * Created by Administrator on 2019/11/28 0028.
 */

public class HelpActivity extends BaseActivity{

    @BindView(R.id.webView)
    public WebView webView;

    private String url = "http://www.avatek.cn/sg/help.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addView(R.layout.activity_help, true);
        webView.loadUrl(url);
        tv_title.setText("帮助");
        tv_submit.setVisibility(View.GONE);
    }
}
