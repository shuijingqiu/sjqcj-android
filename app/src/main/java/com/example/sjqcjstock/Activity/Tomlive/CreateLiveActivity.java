package com.example.sjqcjstock.Activity.Tomlive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.Activity.AgreementActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.Tomlive.TomliveRoomEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建直播间
 * Created by Administrator on 2017/1/12.
 */
public class CreateLiveActivity extends Activity{

    // 网络请求提示
    private CustomProgress dialog;
    // 是否收费的悬着
    private RadioGroup typeRg;
    // 接口返回数据
    private String resstr;
    // 简介
    private EditText remarkEt;
    // 输入的价格
    private EditText priceEt1;
    private EditText priceEt3;
    private EditText priceEt6;
    private EditText priceEt12;
    // 提交按钮
    private Button submitBt;
    // 房间类型(1收费 2免费)
    private String roomType = "2";
    // 判断类型是修改还是添加
    private String update;
    // 房间id
    private String roomId;
    // 房间价格
    private String prices = "";
    //1收费  2免费
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_create_live);
        ExitApplication.getInstance().addActivity(this);
        findView();
        getData();
    }

    /**
     * 页面控件的绑定
     */
    private void findView() {
        dialog = new CustomProgress(this);
        /**
         * 返回按钮的事件绑定
         */
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submitBt = (Button) findViewById(R.id.submit_bt);
        typeRg = (RadioGroup) findViewById(R.id.type_rg);
        typeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.free_charge){
                    roomType = "2";
                }else {
                    roomType = "1";
                }
            }
        });
        remarkEt = (EditText) findViewById(R.id.remark_et);
        priceEt1 = (EditText) findViewById(R.id.price1_et);
        priceEt3 = (EditText) findViewById(R.id.price3_et);
        priceEt6 = (EditText) findViewById(R.id.price6_et);
        priceEt12 = (EditText) findViewById(R.id.price12_et);
        roomId = getIntent().getStringExtra("roomId");
        if (roomId == null || "".equals(roomId)) {
            getData();
        }
        // 跳转到直播设置及规则页面
        findViewById(R.id.service_agreement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateLiveActivity.this, AgreementActivity.class);
                intent.putExtra("type", "16");
                startActivity(intent);
            }
        });
    }

    /**
     * 获取直播间数据
     */
    private void getData() {
        dialog.showDialog();
        // 获取直播间信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                resstr = HttpUtil.restHttpGet(Constants.moUrl+"/live/room/info&id="+roomId);
                handler.sendEmptyMessage(2);
            }
        }).start();
    }

    /**
     * 开通直播间
     */
    public void SubmitData(View view){
        if (!((CheckBox) findViewById(R.id.check_box_protocol)).isChecked()) {
            CustomToast.makeText(getApplicationContext(), "请阅读水晶球直播申请规定", Toast.LENGTH_SHORT).show();
            return;
        }
        String price1="";
        String price3="";
        String price6="";
        String price12="";
        price1 = priceEt1.getText().toString().trim();
        price3 = priceEt3.getText().toString().trim();
        price6 = priceEt6.getText().toString().trim();
        price12 = priceEt12.getText().toString().trim();
        final String remark = remarkEt.getText().toString();
        if ("".equals(remark)){
            Toast.makeText(getApplicationContext(), "请输入房间简介", Toast.LENGTH_SHORT).show();
            remarkEt.setFocusable(true);
            return;
        }
        if ("".equals(price1) && "".equals(price3) && "".equals(price6) && "".equals(price12) && "1".equals(roomType)){
            Toast.makeText(getApplicationContext(), "请至少输入价格", Toast.LENGTH_SHORT).show();
            priceEt1.setFocusable(true);
            return;
        }
        if (!"".equals(price1.trim())){
            if (Integer.valueOf(price1)<1){
                Toast.makeText(getApplicationContext(), "价格不能为零", Toast.LENGTH_SHORT).show();
                return;
            }
             prices += "{\"exp_time\":1,\"price\":"+price1+"},";
        }
        if (!"".equals(price3.trim())){
            if (Integer.valueOf(price3)<1){
                Toast.makeText(getApplicationContext(), "价格不能为零", Toast.LENGTH_SHORT).show();
                return;
            }
            prices += "{\"exp_time\":3,\"price\":"+price3+"},";
        }
        if (!"".equals(price6.trim())){
            if (Integer.valueOf(price6)<1){
                Toast.makeText(getApplicationContext(), "数值不能为零", Toast.LENGTH_SHORT).show();
                return;
            }
            prices += "{\"exp_time\":6,\"price\":"+price6+"},";
        }
        if (!"".equals(price12.trim())){
            if (Integer.valueOf(price12)<1){
                Toast.makeText(getApplicationContext(), "数值不能为零", Toast.LENGTH_SHORT).show();
                return;
            }
            prices += "{\"exp_time\":12,\"price\":"+price12+"},";
        }
        if ("1".equals(roomType)){
            prices = prices.substring(0,prices.length()-1);
        }
        // 创建直播间
        new Thread(new Runnable() {
            @Override
            public void run() {
                List dataList = new ArrayList();
                // 用户ID
                dataList.add(new BasicNameValuePair("uid", Constants.staticmyuidstr));
                dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                dataList.add(new BasicNameValuePair("remark", remark));
                dataList.add(new BasicNameValuePair("type", roomType));
                dataList.add(new BasicNameValuePair("id", roomId));
                if ("1".equals(roomType)){
                    dataList.add(new BasicNameValuePair("prices", "["+prices+"]"));
                }
                if (update != null && update.equals("update")) {
                    resstr = HttpUtil.restHttpPost(Constants.moUrl + "/live/room/update", dataList);
                    handler.sendEmptyMessage(1);
                }else{
                    resstr = HttpUtil.restHttpPost(Constants.moUrl + "/live/room/create", dataList);
                    handler.sendEmptyMessage(0);
                }
                prices = "";
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
                        JSONObject jsonObject = new JSONObject(resstr);
                        if ("failed".equals(jsonObject.getString("status"))){
                            Toast.makeText(getApplicationContext(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                            dialog.dismissDlog();
                        }
//                        Intent intent = new Intent(CreateLiveActivity.this, TomlivePersonnelListActivity.class);
//                        startActivity(intent);
                        dialog.dismissDlog();
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(resstr);
                        if ("failed".equals(jsonObject.getString("status"))){
                            Toast.makeText(getApplicationContext(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                            dialog.dismissDlog();
                            return;
                        }
                        Toast.makeText(getApplicationContext(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                        dialog.dismissDlog();
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        JSONObject jsonObject = new JSONObject(resstr);
                        if ("failed".equals(jsonObject.getString("status"))){
                            dialog.dismissDlog();
                            return;
                        }
                        // 设置一些信息
                        submitBt.setText("确认修改");
                        update = "update";
                        TomliveRoomEntity tomliveRoom = JSON.parseObject(jsonObject.getString("data"), TomliveRoomEntity.class);
                        remarkEt.setText(tomliveRoom.getRemark());
                        type = tomliveRoom.getType();
                        if ("1".equals(type)) {
                            ((RadioButton) findViewById(R.id.charge)).setChecked(true);
                        }
                        for (TomliveRoomEntity.priceList entity : tomliveRoom.getPrices()) {
                            if ("1".equals(entity.getExp_time())) {
                                priceEt1.setText(entity.getPrice());
                            } else if ("3".equals(entity.getExp_time())) {
                                priceEt3.setText(entity.getPrice());
                            } else if ("12".equals(entity.getExp_time())) {
                                priceEt12.setText(entity.getPrice());
                            } else if ("6".equals(entity.getExp_time())) {
                                priceEt6.setText(entity.getPrice());
                            }
                        }
                        dialog.dismissDlog();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

}
