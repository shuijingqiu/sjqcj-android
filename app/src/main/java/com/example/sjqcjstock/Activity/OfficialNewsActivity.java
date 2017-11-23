package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;

/**
 * 官方推送消息的显示
 * Created by Administrator on 2017/2/20.
 */
public class OfficialNewsActivity extends Activity{
    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_official_news);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        findView();
    }

    private void findView(){
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String messageStr = getIntent().getStringExtra("message");
        ((TextView)findViewById(R.id.message_tv)).setText(messageStr);
    }
}
