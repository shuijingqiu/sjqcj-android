package com.example.sjqcjstock.Activity.plan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
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
 * 确认发布计划页面
 * Created by Administrator on 2017/4/18.
 */
public class ReleaseConfirmActivity extends Activity {

    // 计划标题
    private TextView planTitleTv;
    // 股票名称
    private TextView codeNameTv;
    // 股票代码
    private TextView codeTv;
    // 交易日
    private TextView timeTv;
    // 目标盈利
    private TextView targetProfitTv;
    // 止损
    private TextView stopRatioTv;
    // 开始日期
    private TextView startDateTv;
    // 结束日期
    private TextView endDateTv;
    // 价格
    private TextView priceTv;
    // 保金
    private TextView bondTv;
    // 理由
    private TextView reasonTv;

    // 网络请求提示
    private CustomProgress dialog;
    // 接口返回数据
    private String resstr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_confirm);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        findView();
    }

    /**
     * 页面控件的绑定
     */
    private void findView() {
        dialog = new CustomProgress(this);

        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 计划标题
        planTitleTv = (TextView) findViewById(R.id.plan_title_tv);
        // 股票名称
        codeNameTv = (TextView) findViewById(R.id.code_name_tv);
        // 股票代码
        codeTv = (TextView) findViewById(R.id.code_tv);
        // 交易日
        timeTv = (TextView) findViewById(R.id.time_tv);
        // 目标盈利
        targetProfitTv = (TextView) findViewById(R.id.target_profit_tv);
        // 止损
        stopRatioTv = (TextView) findViewById(R.id.stop_ratio_tv);
        // 开始日期
        startDateTv = (TextView) findViewById(R.id.start_date_tv);
        // 结束日期
        endDateTv = (TextView) findViewById(R.id.end_date_tv);
        // 价格
        priceTv = (TextView) findViewById(R.id.price_tv);
        // 保金
        bondTv = (TextView) findViewById(R.id.bond_tv);
        // 理由
        reasonTv = (TextView) findViewById(R.id.reason_tv);

        planTitleTv.setText(getIntent().getStringExtra("titleStr"));
        codeNameTv.setText(getIntent().getStringExtra("codeNameStr"));
        codeTv.setText(getIntent().getStringExtra("codeStr"));
        timeTv.setText(getIntent().getStringExtra("timeStr") + "交易日");
        startDateTv.setText(getIntent().getStringExtra("startData"));
        endDateTv.setText(getIntent().getStringExtra("endData"));
        targetProfitTv.setText(getIntent().getStringExtra("riseStr")+"%");
        stopRatioTv.setText("-" + getIntent().getStringExtra("stopRatioStr")+"%");
        reasonTv.setText(getIntent().getStringExtra("reasonStr"));
        priceTv.setText(getIntent().getStringExtra("priceStr"));
        bondTv.setText(getIntent().getStringExtra("bondStr"));


    }

    /**
     * 提交发布信息
     */
    public void SubmitData(View view) {
        if(Utils.isFastDoubleClick3()){
            return;
        }
        if (!((CheckBox) findViewById(R.id.check_box_protocol)).isChecked()) {
            CustomToast.makeText(getApplicationContext(), "请阅读《发布投资计划协议》", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.showDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List dataList = new ArrayList();
                // 用户ID
                dataList.add(new BasicNameValuePair("uid", Constants.staticmyuidstr));
                dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                // 标题
                dataList.add(new BasicNameValuePair("title", getIntent().getStringExtra("titleStr")));
                // 股票代码
                dataList.add(new BasicNameValuePair("stock", getIntent().getStringExtra("codeStr")));
                // 股票名称
                dataList.add(new BasicNameValuePair("stock_name", getIntent().getStringExtra("codeNameStr")));
                // 计划收益率
                dataList.add(new BasicNameValuePair("plan_ratio", Utils.getNumberFormat2(getIntent().getStringExtra("riseStr"))+""));
                // 止损率
                dataList.add(new BasicNameValuePair("stop_loss_ratio", Utils.getNumberFormat2(getIntent().getStringExtra("stopRatioStr"))+""));
                // 订阅价格
                dataList.add(new BasicNameValuePair("desert_price", getIntent().getStringExtra("priceStr")));
                // 保证金
                dataList.add(new BasicNameValuePair("margin", getIntent().getStringExtra("bondStr")));
                // 交易日
                dataList.add(new BasicNameValuePair("trade_days", getIntent().getStringExtra("timeStr")));
                // 推荐理由
                dataList.add(new BasicNameValuePair("reason", getIntent().getStringExtra("reasonStr")));

                resstr = HttpUtil.restHttpPost(Constants.moUrl + "/dealplan/index/create", dataList);
                handler.sendEmptyMessage(0);
            }
        }).start();

    }

    /**
     * 跳转到规则页面
     *
     * @param view
     */
    public void RuleClick(View view) {
        // 发布投资计划协议
        Intent intent = new Intent(this, AgreementActivity.class);
        intent.putExtra("type", "19");
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
                        JSONObject jsonObject = new JSONObject(resstr);
                        Toast.makeText(getApplicationContext(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                        if ("failed".equals(jsonObject.getString("status"))) {
                            dialog.dismissDlog();
                            return;
                        }
                        dialog.dismissDlog();
                        if (ReleasePlanActivity.instance != null) {
                            // 发布成功但同时关闭发布页面
                            ReleasePlanActivity.instance.setResult(2);
                            ReleasePlanActivity.instance.finish();
                        }
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
}
