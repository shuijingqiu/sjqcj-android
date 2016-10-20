package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;

public class forumsearchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //将Activity反复链表
        ExitApplication.getInstance().addActivity(this);

        setContentView(R.layout.forumsearch);

    }

}
