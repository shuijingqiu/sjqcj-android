package com.example.sjqcjstock.Activity.edituser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;


public class edituserinfouserintroActivity extends Activity {

    private LinearLayout goback1;
    private EditText editintro1;
    private LinearLayout edituserintro1;

    //从intent里获取用户的简介
    private String introstr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edituserinfouserintro);

        //将Activity反复链表
        ExitApplication.getInstance().addActivity(this);

        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub
        //获取intent的数据

        Intent intent = getIntent();
        introstr = intent.getStringExtra("introstr");

        goback1 = (LinearLayout) findViewById(R.id.goback1);
        editintro1 = (EditText) findViewById(R.id.editintro1);
        edituserintro1 = (LinearLayout) findViewById(R.id.edituserintro1);


        //初始化个人介绍的值
        editintro1.setText(introstr);

        goback1.setOnClickListener(new goback1_listener());
        edituserintro1.setOnClickListener(new edituserintro1_listener());

    }

    class goback1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            finish();
        }

    }

    class edituserintro1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            //将参数传回请求的Activity
            Intent intent = new Intent();
            Bundle unameBundle = new Bundle();
            unameBundle.putString("introstr", editintro1.getText().toString());
            //zhuceIntent.putExtra("username", zhuceEdit.getText().toString());
            intent.putExtras(unameBundle);
            setResult(6, intent);
            finish();
        }

    }

}
