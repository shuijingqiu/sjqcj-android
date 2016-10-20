package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;

public class accountboundActivity extends Activity {

    //获取控件
    private ImageView goback4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accountbound);

        //将Activity反复链表
        ExitApplication.getInstance().addActivity(this);

        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub
        goback4 = (ImageView) findViewById(R.id.goback4);
        goback4.setOnClickListener(new goback4_listener());

    }

    //回退按钮
    class goback4_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            finish();

        }

    }

}
