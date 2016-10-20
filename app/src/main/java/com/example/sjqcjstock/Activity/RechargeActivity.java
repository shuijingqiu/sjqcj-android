package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.view.CustomToast;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

/**
 * 支付页面
 * Created by Administrator on 2016/6/6.
 */
public class RechargeActivity extends Activity {

    public static RechargeActivity instance = null;
    // 微信API
    private IWXAPI api;
    // 金额
    private TextView payMoneyTv;
    // 数量
    private EditText countSjbEt;
    // 确认支付按钮
    private Button payBtn;
    // 选中的显示图标
    private ImageView wxRechargeIv, zfbRechargeIv, ylRechargeIv;
    // 调用服务器返回的订单接口数据
    private byte[] buf;
    // 弹出加载窗体
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_recharge);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        instance = this;
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.registerApp(Constants.APP_ID);
//        api = WXAPIFactory.createWXAPI(this, "wxb4ba3c02aa476ea1");
        findView();
    }

    private void findView() {
        dialog = new ProgressDialog(this);
        // dialog.setTitle("提示信息");
        dialog.setMessage("正在跳转微信支付");
        dialog.setCancelable(true);

        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        payMoneyTv = (TextView) findViewById(R.id.pay_money_tv);
        countSjbEt = (EditText) findViewById(R.id.count_sjb_et);
        countSjbEt.setSelection(countSjbEt.length());
        countSjbEt.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!"".equals(temp.toString().trim())) {
                    Float aFloat = Float.valueOf(temp.toString()) / 10;
                    payMoneyTv.setText(Utils.getNumberFormat1(aFloat + "") + "元");
                } else {
                    payMoneyTv.setText("0元");
                }
            }
        });
        wxRechargeIv = (ImageView) findViewById(R.id.wx_recharge_iv);
        zfbRechargeIv = (ImageView) findViewById(R.id.zfb_recharge_iv);
        ylRechargeIv = (ImageView) findViewById(R.id.yl_recharge_iv);
        findViewById(R.id.service_agreement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RechargeActivity.this, AgreementActivity.class);
                intent.putExtra("type", "8");
                startActivity(intent);
            }
        });
    }

    /**
     * 确认支付的单机事件
     *
     * @param view
     */
    public void onClickRecharge(View view) {

        if (Utils.isFastDoubleClick4()) {
            return;
        }

        String countStr = countSjbEt.getText().toString().trim();
        if ("".equals(countStr) || Integer.valueOf(countStr) < 1) {
            CustomToast.makeText(getApplicationContext(), "请输入充值水晶币个数", Toast.LENGTH_LONG).show();
            return;
        }

        if (!((CheckBox) findViewById(R.id.check_box_protocol)).isChecked()) {
            CustomToast.makeText(getApplicationContext(), "请阅读水晶币服务协议", Toast.LENGTH_LONG).show();
            return;
        }

        payBtn = (Button) findViewById(R.id.ok_recharge);
        payBtn.setEnabled(false);
        dialog.show();
        countStr = Double.valueOf(countStr) / 10 + "";
        final String finalCountStr = countStr;
        new Thread() {
            public void run() {
                String url = Constants.unifiedorder;
                url += "&uid=" + Constants.staticmyuidstr + "&body=水晶球财经-水晶币充值&total_fee=" + finalCountStr;
//                    String url = "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";
//                String url = "http://test.sjqcj.com/wxpay";
                buf = HttpUtil.httpGet(url);
                handler.sendEmptyMessage(0);
            }
        }.start();

    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    try {
                        if (buf != null && buf.length > 0) {
                            String content = new String(buf);
                            JSONObject json = new JSONObject(content);
                            if (null != json && !json.has("retcode")) {
                                PayReq req = new PayReq();
                                req.appId = Constants.APP_ID;
                                //商户号
                                req.partnerId = json.getString("partnerid");
                                //预支付交易会话ID
                                req.prepayId = json.getString("prepayid");
                                // 随机字符串
                                req.nonceStr = json.getString("noncestr");
                                //时间戳
                                req.timeStamp = json.getString("timestamp");
                                //扩展字段
                                req.packageValue = json.getString("package");
                                // 签名
                                req.sign = json.getString("sign");
                                // app date
                                req.extData = Utils.getNowDate(); // optional
                                // 保存订单号
                                Constants.orderNumber = json.getString("out_trade_no");

                                // 保存订单状态订单号(用于控制)
                                SharedPreferences.Editor editorIsLogin = getSharedPreferences("Recharge", MODE_PRIVATE).edit();
                                editorIsLogin.putString("out_trade_no", json.getString("out_trade_no"));
                                // 订单状态1为未处理0为处理
                                editorIsLogin.putString("order_type", "1");
                                editorIsLogin.commit();
                                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                                api.sendReq(req);
                            } else {
                                Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
//                            Toast.makeText(RechargeActivity.this, "返回错误"+json.getString("retmsg"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d("PAY_GET", "服务器请求错误");
//                        Toast.makeText(RechargeActivity.this, "服务器请求错误", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("PAY_GET", "异常：" + e.getMessage());
//                    Toast.makeText(RechargeActivity.this, "异常："+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    payBtn.setEnabled(true);
                    break;
            }
            dialog.dismiss();
        }
    };

    /**
     * 微信支付的单机事件
     *
     * @param view
     */
    public void OnClickWxRecharge(View view) {
        setImageGon();
        wxRechargeIv.setVisibility(View.VISIBLE);
    }

    /**
     * 支付宝支付的单机事件
     *
     * @param view
     */
    public void OnClickZfbRecharge(View view) {
//        setImageGon();
//        zfbRechargeIv.setVisibility(View.VISIBLE);
    }

    /**
     * 银联支付的单机事件
     *
     * @param view
     */
    public void OnClickYlRecharge(View view) {
//        setImageGon();
//        ylRechargeIv.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏所有图标的方法
     */
    private void setImageGon() {
        wxRechargeIv.setVisibility(View.GONE);
        zfbRechargeIv.setVisibility(View.GONE);
        ylRechargeIv.setVisibility(View.GONE);
    }
}
