package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.ACache;
import com.example.sjqcjstock.constant.Constants;

public class usersettingActivity extends Activity {
    //获得控件
    private LinearLayout pickaboutmy;
    private LinearLayout pickmodifypassword2;
    private LinearLayout pickaccountbound1;
    private LinearLayout pickfeedback1;
    private LinearLayout goback1;
    private LinearLayout pickexitapp1;
    private LinearLayout picktemporaryexitapp1;
    private TextView passwordTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.myusersetting);
        //将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 如果当前是第三方登陆就设置密码（反之就是修改密码）
        if (Constants.isDefault) {
            passwordTv.setText("修改密码");
        } else {
            passwordTv.setText("设置密码");
        }
    }

    private void initView() {
        // TODO Auto-generated method stub
        pickaboutmy = (LinearLayout) findViewById(R.id.pickaboutmy);
        pickmodifypassword2 = (LinearLayout) findViewById(R.id.pickmodifypassword2);
        pickaccountbound1 = (LinearLayout) findViewById(R.id.pickaccountbound1);
        pickfeedback1 = (LinearLayout) findViewById(R.id.pickfeedback1);
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        pickexitapp1 = (LinearLayout) findViewById(R.id.pickexitapp1);
        picktemporaryexitapp1 = (LinearLayout) findViewById(R.id.picktemporaryexitapp1);

        pickaboutmy.setOnClickListener(new pickaboutmy_listener());
        pickmodifypassword2.setOnClickListener(new pickmodifypassword2_listener());
        goback1.setOnClickListener(new goback1_listener());
        pickaccountbound1.setOnClickListener(new pickaccountbound1_listener());
        pickfeedback1.setOnClickListener(new pickfeedback1_listener());
        pickexitapp1.setOnClickListener(new pickexitapp1_listener());
        picktemporaryexitapp1.setOnClickListener(new picktemporaryexitapp1_listener());

        passwordTv = (TextView) findViewById(R.id.password_tv);
    }

    //暂时退出app
    class picktemporaryexitapp1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            System.exit(0);
        }

    }

    //退出app
    class pickexitapp1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            new AlertDialog.Builder(usersettingActivity.this)
                    .setMessage("确认退出吗？")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //ExitApplication.getInstance().exit(usersettingActivity.this);
                            //获得编辑器
                            SharedPreferences.Editor editor = getSharedPreferences("loginInfo", MODE_PRIVATE).edit();//获得编辑这个文件的编辑器
                            //退出置空用户信息
                            //存储数据
                            // 保存登陆状体（1为登陆 0为退出）
                            editor.putString("isLogin", "0");
                            editor.commit();
                            // 清除与该用户相关的缓存信息
                            // 缓存类
                            ACache mCache = ACache.get(usersettingActivity.this);
                            // 提交我的评论
                            mCache.put("AppMentionx", "");
                            // 私信
                            mCache.put("Messagex", "");
                            // 发出的评论
                            mCache.put("MyCommentSendx", "");
                            // 打赏列表信息
                            mCache.put("RewardMessagex", "");
                            // 系统信息
                            mCache.put("SystemMessagex", "");
                            // 收到的评论
                            mCache.put("MyCommentx", "");
                            // 用户基本信息
                            mCache.put("AppUserx", "");
                            // 水晶币信息
                            mCache.put("AppUserMoneyx", "");
                            // 我关注的微博
                            mCache.put("LoadMoreFollowingx", "");
                            // 我的订阅微博
                            mCache.put("MySubscribex", "");
                            // 我的收藏微博
                            mCache.put("AppCollectionx", "");
                            // 我的微博
                            mCache.put("loadMoreMyfeedx", "");
                            Intent intent = new Intent(usersettingActivity.this, loginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();   //关闭alertDialog
                        }
                    }).show();
        }
    }


    //关于我们按钮
    class pickaboutmy_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(usersettingActivity.this, aboutmyActivityNew.class);
            startActivity(intent);
        }
    }

    //修改密码
    class pickmodifypassword2_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(usersettingActivity.this, modifypasswordActivity.class);
            startActivity(intent);
        }
    }

    //修改密码
    class goback1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            finish();
        }
    }

    //修改密码
    class pickaccountbound1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(usersettingActivity.this, accountboundActivity.class);
            startActivity(intent);
        }
    }

    //修改密码
    class pickfeedback1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(usersettingActivity.this, feedbackActivity.class);
            startActivity(intent);
        }
    }
}
