package com.example.sjqcjstock.Activity.stocks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjqcjstock.Activity.AgreementActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 订阅确认页面
 * Created by Administrator on 2016/12/16.
 */
public class SubscribeConfirmActivity extends Activity{

    // 网络请求提示
    private CustomProgress dialog;
    private TextView nameTv;
    private TextView timeTv;
    // 接口返回的数据
    private String rest;
    // 需订阅人的uid
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_subscribe_confirm);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        findView();
        getData();
    }

    /**
     * 获取数据
     */
    private void getData() {
        String type = getIntent().getStringExtra("type");
        // 设置有效期
        String time = getIntent().getStringExtra("time");
        // 如果type =1 为延长订阅 反之为 第一次订阅
        if (type != null  && "1".equals(type)){
            time = Utils.GetSysDate("yyyy-MM-dd",time,0,1,0)+" 23:59";
        }else{
            time = Utils.GetSysDate("yyyy-MM-dd","",0,1,-1)+" 23:59";
        }
        timeTv.setText(time);
    }

    /**
     * 控件的绑定
     */
    private void findView() {
        dialog = new CustomProgress(this);
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        nameTv = (TextView) findViewById(R.id.name_tv);
        timeTv = (TextView) findViewById(R.id.time_tv);
        nameTv.setText(getIntent().getStringExtra("name"));
        uid = getIntent().getStringExtra("uid");
        
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null){
            dialog.dismissDlog();
        }
    }

    /**
     *  阅读协议的单机事件
     */
    public void serviceClick(View view){
        Intent intent = new Intent(SubscribeConfirmActivity.this, AgreementActivity.class);
        intent.putExtra("type", "12");
        startActivity(intent);
    }

    /**
     * 确认订阅的单击事件
     */
    public void subscribeOkClick(View view){
        if (Utils.isFastDoubleClick3()){
            return;
        }
        if (!((CheckBox) findViewById(R.id.check_box_protocol)).isChecked()) {
            CustomToast.makeText(getApplicationContext(), "请阅读《水晶球牛人动态订阅服务协议》", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.showDialog();
        // 确认订阅的接口
        new Thread(new Runnable() {
            @Override
            public void run() {
                List dataList = new ArrayList();
                // 用户ID
                dataList.add(new BasicNameValuePair("uid", Constants.staticmyuidstr));
                dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                dataList.add(new BasicNameValuePair("price_uid", uid));
                dataList.add(new BasicNameValuePair("desert_time", "1"));
                rest = HttpUtil.restHttpPost(Constants.moUrl+"/desert/order",dataList);
                handler.sendEmptyMessage(0);
            }
        }).start();
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
                        JSONObject jsonObject = new JSONObject(rest);
                        if ("failed".equals(jsonObject.getString("status"))){
                            Toast.makeText(getApplicationContext(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                            dialog.dismissDlog();
                            return;
                        }
                        Constants.isDynamic = true;
                        Toast.makeText(getApplicationContext(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                        dialog.dismissDlog();
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
}
