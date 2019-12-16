package com.example.channel.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.channel.R;

import butterknife.BindView;

/**
 * Created by Administrator on 2019/12/16 0016.
 */

public class DianganActivity extends BaseActivity{

    @BindView(R.id.webView)
    public WebView webView;

    private String url = "http://www.avatek.cn/sg/image/img.php?task_id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addView(R.layout.activity_help, true);
        tv_title.setText("完善电杆走向");
        tv_submit.setVisibility(View.GONE);
        String taskId = getIntent().getExtras().getString("taskId");
        WebSettings webSettings = webView.getSettings();

        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webView.loadUrl(url+taskId);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        webView.setWebViewClient(new WebViewClient());

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return true;
            }
        });

    }
}
