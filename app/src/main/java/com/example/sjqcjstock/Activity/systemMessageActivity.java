package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;

/**
 * 系统消息展示页面
 * Created by Administrator on 2016/5/4.
 */
public class systemMessageActivity extends Activity {

    // 标题
    private TextView titleNameTv;
    // 主体内容
    private TextView bodyTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_system_message_data);
        //将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        findView();
    }

    /**
     * 控件的绑定
     */
    private void findView() {
        // 返回按钮
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleNameTv = (TextView) findViewById(R.id.title_name_tv);
        bodyTv = (TextView) findViewById(R.id.body_tv);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String body = intent.getStringExtra("body");
        bodyTv.setText(body);
        if (title == null || "".equals(title.trim())) {
            titleNameTv.setVisibility(View.GONE);
        } else {
            titleNameTv.setText(title);
        }

    }

}
