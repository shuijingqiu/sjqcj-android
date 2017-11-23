package com.example.sjqcjstock.Activity.stocks;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Md5Util;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 用户资料显示页面
 * Created by Administrator on 2017/2/21.
 */
public class UserDataActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        findView();
    }

    private void findView() {
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView headIm = (ImageView) findViewById(R.id.head_iv);
        TextView nameTv = (TextView) findViewById(R.id.name_tv);
        TextView introTv = (TextView) findViewById(R.id.intro_tv);
        nameTv.setText(getIntent().getStringExtra("name"));
        introTv.setText(getIntent().getStringExtra("intro"));
        String uid = getIntent().getStringExtra("uid");
        if (uid!=null && !"".equals(uid)){
            ImageLoader.getInstance().displayImage(Md5Util.getuidstrMd5(Md5Util
                            .getMd5(uid)),
                    headIm, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        }
    }
}
