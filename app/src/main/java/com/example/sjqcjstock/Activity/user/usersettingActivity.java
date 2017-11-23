package com.example.sjqcjstock.Activity.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.PushSettingsActivity;
import com.example.sjqcjstock.Activity.Tomlive.CreateLiveActivity;
import com.example.sjqcjstock.Activity.aboutmyActivityNew;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.ACache;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用户设置页面
 */
public class usersettingActivity extends Activity {
    //获得控件
    private LinearLayout pickaboutmy;
    private LinearLayout pickmodifypassword2;
    private LinearLayout goback1;
    private LinearLayout pickexitapp1;
    private TextView passwordTv;
    // 是否是直播返回的数据
    private String tomliveStr;
    //  房间Id
    private String roomId;
    // 版本更新名字
    private TextView versionTv;
    // 更新图片
    private ImageView versionImg;
    // 版本号
    private TextView versionCodeTv;
    // 最新版本下载地址
    private String versionUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.myusersetting);
        //将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
        getVersion();
    }

    /**
     * 获取当前版本
     */
    private void getVersion() {
        // 缓存类
        ACache mCache = ACache.get(this);
        String versionStr = mCache.getAsString("versionStr");
        // 当前版本名称
        String  versionNameOld = Utils.getVersionName(this);
        versionCodeTv.setText(versionNameOld);
        if (versionStr!=null && versionStr.length()>10){
            // 当前版本号
            int  versionCodeOld = Utils.getVersionCode(this);
            String[] versionS = versionStr.split("\\|");
            String versionCode = versionS[0];
            String versionName = versionS[1];
            versionUrl =  versionS[2];
            if (versionCodeOld < Integer.valueOf(versionCode)){
                versionTv.setText("版本更新");
                versionImg.setVisibility(View.VISIBLE);
                versionCodeTv.setText(versionNameOld+" -> "+versionName);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getisTomlive();
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
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        pickexitapp1 = (LinearLayout) findViewById(R.id.pickexitapp1);
        pickaboutmy.setOnClickListener(new pickaboutmy_listener());
        pickmodifypassword2.setOnClickListener(new pickmodifypassword2_listener());
        goback1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pickexitapp1.setOnClickListener(new pickexitapp1_listener());
        passwordTv = (TextView) findViewById(R.id.password_tv);
        versionTv = (TextView) findViewById(R.id.version_tv);
        versionImg = (ImageView) findViewById(R.id.version_img);
        versionCodeTv = (TextView) findViewById(R.id.version_code_tv);

    }


    /**
     * 判断当前用户是否开通直播间
     */
    private void getisTomlive() {
        // 开线程获取用户是否开通直播间
        new Thread(new Runnable() {
            @Override
            public void run() {
                tomliveStr = HttpUtil.restHttpGet(Constants.moUrl + "/live/room/hasRoom&uid=" + Constants.staticmyuidstr + "&token=" + Constants.apptoken);
                handler.sendEmptyMessage(0);
            }
        }).start();
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
                            if (Constants.timer != null){
                                // 关闭掉定时器
                                Constants.timer.cancel();
                            }
                            ExitApplication.getInstance().exit();
//                            finish();
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

    /**
     * 用户基本设置
     */
    public void userSetUp(View view){
            Intent intent = new Intent(usersettingActivity.this, userinfoeditActivitynew.class);
            startActivity(intent);
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

    /**
     * 创建直播
     * @param view
     */
    public void FoundTomlive(View view){
        Intent intent = new Intent(this, CreateLiveActivity.class);
        intent.putExtra("roomId",roomId);
        startActivity(intent);
    }

    /**
     * 推送设置
     * @param view
     */
    public void pushSettings(View view){
        Intent intent = new Intent(this, PushSettingsActivity.class);
        startActivity(intent);
    }

    /**
     * 线程更新Ui
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    try {
                        JSONObject jsonObject = new JSONObject(tomliveStr);
                        if ("failed".equals(jsonObject.getString("status"))) {
                            return;
                        }
                        String data = jsonObject.getString("data");
                        if ("1".equals(data)) {
                            roomId = jsonObject.getString("room_id");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    /**
     * 打开网页跳转到更新页面
     * @param view
     */
    public void NewVersion(View view){
        if (versionImg.getVisibility() == View.GONE){
            return;
        }
        // 打开网页下载最新版本
        // 先判断是否是网址
        if (Utils.isWebsite(versionUrl)){
            Uri uri = Uri.parse(versionUrl);
            // 调用手机浏览器打开网址进行下载
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }
}
