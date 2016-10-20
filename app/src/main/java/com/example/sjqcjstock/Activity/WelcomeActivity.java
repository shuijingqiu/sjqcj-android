package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;

public class WelcomeActivity extends Activity {

    private Runnable runnable = null;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                //跳转到主界面
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else if (msg.what == 1) {
                Intent intent = new Intent(WelcomeActivity.this, loginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        runnable = new Runnable() {
            @Override
            public void run() {
                // 1已经登陆过，直接跳转主界面
                if (getSharedPreferences("loginInfo", MODE_PRIVATE).getString("isLogin", "").equals("1")) {
                    // 已经登陆的情况下需要判断登陆类型qq、微信、普通用户登录
                    if (getSharedPreferences("loginInfo", MODE_PRIVATE).getString("loginType", "").equals("qq") || getSharedPreferences("loginInfo", MODE_PRIVATE).getString("loginType", "").equals("weixin")) {
                        Constants.isDefault = false;
                        //用户mid token password全局变量
                        Constants.setStaticmyuidstr(getSharedPreferences("loginInfo", MODE_PRIVATE).getString("uidstr", ""));
                        Constants.setStatictokeystr(getSharedPreferences("loginInfo", MODE_PRIVATE).getString("openidstr", ""));
                        Constants.setStaticpasswordstr(getSharedPreferences("loginInfo", MODE_PRIVATE).getString("loginPwd", ""));
                        mHandler.sendEmptyMessage(0);
                        //普通用户登录，需要保存 mid 用户名 密码
                    } else if (getSharedPreferences("loginInfo", MODE_PRIVATE).getString("loginType", "").equals("common")) {
                        Constants.setStaticmyuidstr(getSharedPreferences("loginInfo", MODE_PRIVATE).getString("uidstr", ""));
                        Constants.setStaticuname(getSharedPreferences("loginInfo", MODE_PRIVATE).getString("unamestr", ""));
                        Constants.setStaticpasswordstr(getSharedPreferences("loginInfo", MODE_PRIVATE).getString("loginPwd", ""));
                        //Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                        mHandler.sendEmptyMessage(0);
                    }
                    //没有登陆过，跳转到登陆界面
                } else {
                    mHandler.sendEmptyMessage(1);
                }
            }
        };
        mHandler.postDelayed(runnable, 2000);
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 暂停线程
            mHandler.removeCallbacks(runnable);
            finish();
            System.exit(0);
        }
        return false;
    }
}