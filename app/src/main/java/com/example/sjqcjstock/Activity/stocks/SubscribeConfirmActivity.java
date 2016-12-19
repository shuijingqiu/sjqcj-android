package com.example.sjqcjstock.Activity.stocks;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.view.CustomToast;

/**
 * 订阅确认页面
 * Created by Administrator on 2016/12/16.
 */
public class SubscribeConfirmActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_subscribe_confirm);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        findView();
    }

    /**
     * 控件的绑定
     */
    private void findView() {
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /**
     *  阅读协议的单机事件
     */
    public void serviceClick(View view){
        if (!((CheckBox) findViewById(R.id.check_box_protocol)).isChecked()) {
            CustomToast.makeText(getApplicationContext(), "请阅读订阅服务协议", Toast.LENGTH_LONG).show();
            return;
        }
    }

    /**
     * 确认订阅的单击事件
     */
    public void subscribeOkClick(View view){
        finish();
    }
}
