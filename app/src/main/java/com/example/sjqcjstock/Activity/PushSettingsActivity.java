package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.PushAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.ACache;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.PushEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * 推送设置
 * Created by Administrator on 2017/6/2.
 */
public class PushSettingsActivity extends Activity implements View.OnClickListener{

    private PushAdapter pushAdapter;
    private ListView pushList;
    private ArrayList<PushEntity> pushEntityList;
    // 缓存用的类
    private ACache mCache;
    private CheckBox kgCheckBox;
    // 网络请求提示
    private CustomProgress dialog;
    // 接口返回数据
    private String resstr;
    private String resstr1;
    // 总开关的id
    private String pushId = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_setting);
        //将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        findView();
        getData();
    }

    /**
     * 页面控件的绑定
     */
    private void findView() {
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 缓存类
        mCache = ACache.get(this);
        dialog = new CustomProgress(this);
        dialog.showDialog();
        kgCheckBox = (CheckBox) findViewById(R.id.push_kg_rb);
        kgCheckBox.setOnClickListener(this);
        pushList = (ListView) findViewById(R.id.push_list);
        pushAdapter = new PushAdapter(this,this);
        pushList.setAdapter(pushAdapter);
    }

    /**
     * 获取推送设置的
     */
    private void getData(){
        // 是否接收推送
        String isPush = mCache.getAsString("isPush");
        // 获取缓存的设置
        if ("false".equals(isPush)){
            kgCheckBox.setChecked(false);
        }else{
            kgCheckBox.setChecked(true);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取推送设置信息
                resstr = HttpUtil.restHttpGet(Constants.moUrl + "/jpush/getSetting?uid=" + Constants.staticmyuidstr+"&token="+Constants.apptoken);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.push_kg_rb:
                if (!kgCheckBox.isChecked()){
                    mCache.put("isPush", "false");
                    // 循环关闭
                    if (pushEntityList != null && pushEntityList.size()>0){
                        for (PushEntity pushEntity:pushEntityList){
                            pushEntity.setState("2");
                        }
                        pushAdapter.setlistData(pushEntityList);
                    }
                    dialog.showDialog();
                    // 关闭全部推送
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // 关闭推送
                            List dataList = new ArrayList();
                            // 用户ID
                            dataList.add(new BasicNameValuePair("uid", Constants.staticmyuidstr));
                            dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                            dataList.add(new BasicNameValuePair("id", pushId));
                            dataList.add(new BasicNameValuePair("state", "2"));
                            resstr1 = HttpUtil.restHttpPost(Constants.moUrl + "/jpush/setting",dataList);
                            handler.sendEmptyMessage(1);
                        }
                    }).start();
                    // 停止推送服务
                    JPushInterface.stopPush(this);
                }else{
                    mCache.put("isPush", "true");
                    // 开启推送服务
                    JPushInterface.resumePush(this);
                }
            break;
        }
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
                        dialog.dismissDlog();
                        JSONObject jsonObject = new JSONObject(resstr);
                        if ("failed".equals(jsonObject.getString("status"))) {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        pushEntityList = (ArrayList<PushEntity>) JSON.parseArray(jsonObject.getString("data"), PushEntity.class);
                        pushAdapter.setlistData(pushEntityList);
                        PushEntity pushEntitie = (PushEntity) JSON.parseObject(jsonObject.getString("total"),PushEntity.class);
                        pushId = pushEntitie.getId();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(resstr1);
                        dialog.dismissDlog();
                        if ("failed".equals(jsonObject.getString("status"))) {
                            return;
                        }
                        Toast.makeText(getApplicationContext(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    /**
     * 修改推送消息
     * @param state 1为开启 2关闭
     * @param id id
     */
    public void updatePush(final String state, final String id){
        if ("1".equals(state)){
            kgCheckBox.setChecked(true);
            mCache.put("isPush", "true");
            // 开启推送服务
            JPushInterface.resumePush(this);
        }
        dialog.showDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 关闭推送
                List dataList = new ArrayList();
                // 用户ID
                dataList.add(new BasicNameValuePair("uid", Constants.staticmyuidstr));
                dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                dataList.add(new BasicNameValuePair("id", id));
                dataList.add(new BasicNameValuePair("state", state));
                resstr1 = HttpUtil.restHttpPost(Constants.moUrl + "/jpush/setting",dataList);
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

}
