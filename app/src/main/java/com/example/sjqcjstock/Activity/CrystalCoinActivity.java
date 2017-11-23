package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 水晶币页面
 * Created by Administrator on 2016/6/24.
 */
public class CrystalCoinActivity extends Activity {

    // 水晶币个数
    private TextView shuijinbicount1;
    // 操作接口返回数据
    private String jsonStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_crystal_coin);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        findView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        shuijinbicount1 = (TextView) findViewById(R.id.shuijinbicount1);
        // 设置水晶币个数
        shuijinbicount1.setText(Constants.shuijinbiCount);
    }

    private void findView() {
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 每次进入都获取水晶币数量
//        new SendInfoTaskmywealth().execute(new TaskParams(Constants.appUserMoneyUrl,
//                new String[]{"mid", Constants.staticmyuidstr}
//        ));

        // 开线程获取水晶币数量
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/credit/info?mid=" + Constants.staticmyuidstr + "&token=" + Constants.apptoken);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    /**
     * 水晶币的单击事件
     */
    public void OnClickCrystalCoin(View view) {
        startActivity(new Intent(CrystalCoinActivity.this, CrystalBwaterActivity.class));
    }

    /**
     * 充值的单击事件
     */
    public void OnClickRecharge(View view) {
        //充值的单机事件
        startActivity(new Intent(CrystalCoinActivity.this, RechargeActivity.class));
    }

//    // 获取用户财富设置水晶币个数
//    private class SendInfoTaskmywealth extends AsyncTask<TaskParams, Void, String> {
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            if (result == null) {
//                CustomToast.makeText(CrystalCoinActivity.this, "", Toast.LENGTH_SHORT).show();
//            } else {
//                String count = "0";
//                try {
//                    JSONObject jsonObj = new JSONObject(result);
//                    // 获取水晶币
//                    count = jsonObj.getJSONObject("data").getJSONObject("credit").getJSONObject("shuijingbi").getString("value");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                Constants.shuijinbiCount = count;
//                shuijinbicount1.setText(count);
//            }
//        }
//    }

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
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        if (Constants.successCode.equals(jsonObject.getString("code"))) {
                            JSONObject jsonData = new JSONObject(jsonObject.getString("data"));
                            String shuijingbi = jsonData.getString("shuijingbi");
                            Constants.shuijinbiCount = shuijingbi;
                            shuijinbicount1.setText(shuijingbi);
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

}
