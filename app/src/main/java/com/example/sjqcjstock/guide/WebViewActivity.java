package com.example.sjqcjstock.guide;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.sjqcjstock.R;

public class WebViewActivity extends Activity {
    private WebView myWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        myWebView = (WebView) findViewById(R.id.myWebView);
        myWebView.getSettings().setJavaScriptEnabled(true);
        // 涓巎s浜や簰锛孞avaScriptinterface 鏄釜鎺ュ彛锛屼笌js浜や簰鏃剁敤鍒扮殑锛岃繖涓帴鍙ｅ疄鐜颁簡浠庣綉椤佃烦鍒癮pp涓殑activity 鐨勬柟娉曪紝鐗瑰埆閲嶈
        myWebView.addJavascriptInterface(new JavaScriptinterface(this), "androidstock");
        myWebView.loadUrl("file:///android_asset/index.html");

    }
}