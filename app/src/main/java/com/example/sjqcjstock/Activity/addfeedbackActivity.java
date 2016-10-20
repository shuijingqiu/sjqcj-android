package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;

public class addfeedbackActivity extends Activity {

    //获取控件
    private ImageView goback6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.userfeedback);

        //将Activity反复链表
        ExitApplication.getInstance().addActivity(this);

        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub
        goback6 = (ImageView) findViewById(R.id.goback6);
        goback6.setOnClickListener(new goback6_listener());

    }

    class goback6_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            finish();
        }

    }

}
