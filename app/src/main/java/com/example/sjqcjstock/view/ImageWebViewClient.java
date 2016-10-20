package com.example.sjqcjstock.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Administrator on 2016/7/29.
 */
public class ImageWebViewClient extends WebViewClient {

    private WebView webView;

    private Context context;

    public ImageWebViewClient(WebView webView) {
        this.webView = webView;
    }

    public ImageWebViewClient(WebView webView, Context context) {
        this.webView = webView;
        this.context = context;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // 打开系统浏览器加载网页
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        context.startActivity(intent);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {

        view.getSettings().setJavaScriptEnabled(true);

        super.onPageFinished(view, url);
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistner.openImage(this.src);  " +
                "    }  " +
                "}" +
                "})()");

    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        view.getSettings().setJavaScriptEnabled(true);

        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

        super.onReceivedError(view, errorCode, description, failingUrl);

    }
}

