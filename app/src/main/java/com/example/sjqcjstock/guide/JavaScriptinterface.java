package com.example.sjqcjstock.guide;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.example.sjqcjstock.Activity.loginActivity;

public class JavaScriptinterface {

    Activity mActivity;

    public JavaScriptinterface(Activity mActivity) {
        this.mActivity = mActivity;
    }

    /**
     * 涓巎s浜や簰鏃剁敤鍒扮殑鏂规硶锛屽湪js閲岀洿鎺ヨ皟鐢ㄧ殑
     */
    @JavascriptInterface
    public void startActivity() {
        Intent intent = new Intent();
        intent.setClass(mActivity, loginActivity.class);
        mActivity.startActivity(intent);
        mActivity.finish();
    }
}
