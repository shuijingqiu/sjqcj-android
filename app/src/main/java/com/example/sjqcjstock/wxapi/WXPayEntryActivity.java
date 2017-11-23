package com.example.sjqcjstock.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjqcjstock.Activity.RechargeActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;
    private TextView resultTv;
    private TextView resultCount;
    // 弹出加载窗体
    private CustomProgress dialog;
    // 订单返回数据
    private String orderStr;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        resultTv = (TextView) findViewById(R.id.result_tv);
        resultCount = (TextView) findViewById(R.id.result_count);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        dialog = new CustomProgress(this);
        // dialog.setTitle("提示信息");
        dialog.setMessageDialog("正在查询支付结果");
        dialog.showDialog();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0 || resp.errCode == -2) {
                if (resp.errCode == -2) {
                    // 保存订单状态订单号(用于控制)
                    SharedPreferences.Editor editorIsLogin = getSharedPreferences("Recharge", MODE_PRIVATE).edit();
                    // 订单状态1为未处理0为处理
                    editorIsLogin.putString("order_type", "0");
                }
                selectOrder();
            } else {
                resultTv.setText("对不起你购买水晶币失败！");
                // 保存订单状态订单号(用于控制)
                SharedPreferences.Editor editorIsLogin = getSharedPreferences("Recharge", MODE_PRIVATE).edit();
                // 订单状态1为未处理0为处理
                editorIsLogin.putString("order_type", "0");
                editorIsLogin.commit();
            }
        }
    }

    /**
     * 查询订单是否成功
     */
    private void selectOrder() {
//        new SelectOrder()
//                .execute(new TaskParams(Constants.queryOrder + "&uid=" + Constants.staticmyuidstr + "&out_trade_no=" + Constants.orderNumber
//                ));
        new Thread(new Runnable() {
            @Override
            public void run() {
                List dataList = new ArrayList();
                // 微博id
                dataList.add(new BasicNameValuePair("mid", Constants.staticmyuidstr));
                dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                dataList.add(new BasicNameValuePair("out_trade_no", Constants.orderNumber));
                orderStr = HttpUtil.restHttpPost(Constants.newUrl + "/api/pay/wxQuery", dataList);
                handler.sendEmptyMessage(0);
            }
        }).start();

    }

//    private class SelectOrder extends AsyncTask<TaskParams, Void, String> {
//
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            if (result == null) {
//                CustomToast.makeText(getApplicationContext(), "支付失败！", Toast.LENGTH_SHORT)
//                        .show();
//                resultTv.setText("对不起你购买水晶币失败！");
//            } else {
//                try {
//                    JSONObject json = new JSONObject(result);
//                    String status = json.getString("status");
//                    String info = json.getString("info");
//                    String data = json.getString("data");
//                    if ("1".equals(status)) {
//                        resultTv.setText("恭喜你购买水晶币成功！");
//                        resultCount.setText("当前水晶币个数为" + data + "个");
//                        resultCount.setVisibility(View.VISIBLE);
//                        Constants.shuijinbiCount = data;
//                        // 保存订单状态订单号(用于控制)
//                        SharedPreferences.Editor editorIsLogin = getSharedPreferences("Recharge", MODE_PRIVATE).edit();
//                        // 订单状态1为未处理0为处理
//                        editorIsLogin.putString("order_type", "0");
//                        editorIsLogin.commit();
//                        if (RechargeActivity.instance != null) {
//                            // 打开微信支付成功后关闭支付页面
//                            RechargeActivity.instance.finish();
//                        }
//                    } else {
//                        CustomToast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT)
//                                .show();
//                        resultTv.setText("对不起你购买水晶币失败！");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    resultTv.setText("对不起你购买水晶币失败！");
//                    CustomToast.makeText(getApplicationContext(), "支付失败！", Toast.LENGTH_SHORT)
//                            .show();
//                }
//            }
//            dialog.dismissDlog();
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
                    // 微信支付订单查询
                    try {
                        JSONObject jsonObject = new JSONObject(orderStr);
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            CustomToast.makeText(getApplicationContext(), "支付失败！", Toast.LENGTH_SHORT)
                                    .show();
                            resultTv.setText("对不起你购买水晶币失败！");
                            dialog.dismissDlog();
                            return;
                        }
                        String shuijingbi = jsonObject.getString("data");
                        resultTv.setText("恭喜你购买水晶币成功！");
                        resultCount.setText("当前水晶币个数为" + shuijingbi + "个");
                        resultCount.setVisibility(View.VISIBLE);
                        Constants.shuijinbiCount = shuijingbi;
                        // 保存订单状态订单号(用于控制)
                        SharedPreferences.Editor editorIsLogin = getSharedPreferences("Recharge", MODE_PRIVATE).edit();
                        // 订单状态1为未处理0为处理
                        editorIsLogin.putString("order_type", "0");
                        editorIsLogin.commit();
                        if (RechargeActivity.instance != null) {
                            // 打开微信支付成功后关闭支付页面
                            RechargeActivity.instance.finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        resultTv.setText("对不起你购买水晶币失败！");
                        CustomToast.makeText(getApplicationContext(), "支付失败！", Toast.LENGTH_SHORT)
                                .show();
                    }
                    dialog.dismissDlog();
                    break;
            }
        }
    };

}