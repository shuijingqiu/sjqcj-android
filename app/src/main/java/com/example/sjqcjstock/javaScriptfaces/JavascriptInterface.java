package com.example.sjqcjstock.javaScriptfaces;

import android.content.Context;
import android.content.Intent;

import com.example.sjqcjstock.Activity.ShowWebImageActivity;

/**
 * Created by Administrator on 2016/7/29.
 */
public class JavascriptInterface {
    private Context context;

    public JavascriptInterface(Context context) {
        this.context = context;
    }

    @android.webkit.JavascriptInterface
    public void openImage(String img) {
        Intent intent = new Intent();
        intent.putExtra("image", img);
        // 跳转到的页面
        intent.setClass(context, ShowWebImageActivity.class);
        context.startActivity(intent);
        System.out.println(img);
    }
}
