package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;

public class aboutmyActivityNew extends Activity {

    // 获取控件
    private LinearLayout goback1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aboutwenew);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();

    }

    private void initView() {
        // TODO Auto-generated method stub
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        goback1.setOnClickListener(new pickgoback2_listener());
    }

    // 回退按钮
    class pickgoback2_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            finish();
        }
    }
}
