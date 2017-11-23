package com.example.sjqcjstock.Activity.plan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjqcjstock.Activity.AgreementActivity;
import com.example.sjqcjstock.Activity.stocks.SearchSharesActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 发布计划页面
 * Created by Administrator on 2017/4/14.
 */
public class ReleasePlanActivity extends Activity {

    public static ReleasePlanActivity instance = null;
    // 标题
    private EditText titleEt;
    // 预期时间
    private EditText timeEt;
    // 上涨
    private EditText riseEt;
    // 止损
    private EditText stopRatioEt;
    // 理由
    private EditText reasonEt;
    // 价格
    private EditText priceEt;
    // 保证金
    private EditText bondEt;
    // 股票名称
    private TextView codeNameTv;
    // 股票代码
    private TextView codeTv;

    // 网络请求提示
    private CustomProgress dialog;
    // 接口返回数据
    private String resstr;
    // 要跳转页面的Intent;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_plan);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        instance = this;
        findView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 判断是否选中了股票
        if (!"".equals(Constants.choiceCode)) {
            codeNameTv.setText(Constants.choiceName);
            codeTv.setText(Constants.choiceCode);
            codeNameTv.setVisibility(View.VISIBLE);
            codeTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 清除选中的股票
        Constants.choiceCode = "";
        Constants.choiceName = "";
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if ("".equals(Constants.choiceCode)&&"".equals(titleEt.getText().toString())){
                finish();
                return false;
            }
            new AlertDialog.Builder(this)
                    .setMessage("确认放弃发布吗?")
                    .setPositiveButton("确认",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    finish();
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss(); //关闭alertDialog
                                }
                            }).show();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }

    /**
     * 页面控件的绑定
     */
    private void findView() {
        dialog = new CustomProgress(this);
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(Constants.choiceCode)&&"".equals(titleEt.getText().toString())){
                    finish();
                    return;
                }
                new AlertDialog.Builder(ReleasePlanActivity.this)
                        .setMessage("确认放弃发布吗?")
                        .setPositiveButton("确认",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        finish();
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss(); //关闭alertDialog
                                    }
                                }).show();
            }
        });
        // 标题
        titleEt = (EditText) findViewById(R.id.title_et);
        // 预期时间
        timeEt = (EditText) findViewById(R.id.time_et);
        // 上涨
        riseEt = (EditText) findViewById(R.id.rise_et);
        // 止损
        stopRatioEt = (EditText) findViewById(R.id.stop_ratio_et);
        // 理由
        reasonEt = (EditText) findViewById(R.id.reason_et);
        // 价格
        priceEt = (EditText) findViewById(R.id.price_et);
        // 保证金
        bondEt = (EditText) findViewById(R.id.bond_et);
        // 股票名称
        codeNameTv = (TextView) findViewById(R.id.code_name_tv);
        codeTv = (TextView) findViewById(R.id.code_tv);

    }

    /**
     * 提交发布信息到确认页面
     */
    public void SubmitData(View view) {
        // 标题
        String titleStr = titleEt.getText().toString().trim();
        // 股票代码
        String codeStr = codeTv.getText().toString().trim();
        // 预期时间
        final String timeStr = timeEt.getText().toString().trim();
        // 上涨
        String riseStr = riseEt.getText().toString().trim();
        // 止损
        String stopRatioStr = stopRatioEt.getText().toString().trim();
        // 理由
        String reasonStr = reasonEt.getText().toString().trim();
        // 价格
        String priceStr = priceEt.getText().toString().trim();
        // 保证金
        String bondStr = bondEt.getText().toString().trim();

        if ("".equals(titleStr)) {
            Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show();
            titleEt.setFocusable(true);
            return;
        }
        if (titleStr.length() > 10 && titleStr.length() < 2) {
            Toast.makeText(this, "请输入2-15个字的标题", Toast.LENGTH_SHORT).show();
            titleEt.setFocusable(true);
            return;
        }
        if ("".equals(codeStr)) {
            Toast.makeText(this, "请输入股票代码", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(timeStr)) {
            Toast.makeText(this, "请输入交易日", Toast.LENGTH_SHORT).show();
            timeEt.setFocusable(true);
            return;
        } else {
            int timeInt = Integer.valueOf(timeStr);
            if (timeInt > 60 || timeInt < 2) {
                Toast.makeText(this, "计划时间范围：2-60个交易日", Toast.LENGTH_SHORT).show();
                timeEt.setFocusable(true);
                return;
            }
        }
        Double riseDoub = 0.0;
        if ("".equals(riseStr)) {
            Toast.makeText(this, "请输入涨幅目标比率", Toast.LENGTH_SHORT).show();
            riseEt.setFocusable(true);
            return;
        } else {
            riseDoub = Double.valueOf(riseStr);
            if (riseDoub > 200 || riseDoub < 5) {
                Toast.makeText(this, "预期涨幅范围：5-200%", Toast.LENGTH_SHORT).show();
                riseEt.setFocusable(true);
                return;
            }
        }
        Double stopRatioDoub = 0.0;
        if ("".equals(stopRatioStr)) {
            Toast.makeText(this, "请输入止损比率", Toast.LENGTH_SHORT).show();
            stopRatioEt.setFocusable(true);
            return;
        } else {
            stopRatioDoub = Double.valueOf(stopRatioStr);
            if (stopRatioDoub > 20 || stopRatioDoub < 5) {
                Toast.makeText(this, "止损比例范围：5-20%", Toast.LENGTH_SHORT).show();
                stopRatioEt.setFocusable(true);
                return;
            }
        }
        if (riseDoub<stopRatioDoub){
            Toast.makeText(this, "止损比例必须小于或等于涨幅", Toast.LENGTH_SHORT).show();
            return;
        }

        if ("".equals(reasonStr)) {
            Toast.makeText(this, "请输入看好理由及操作建议", Toast.LENGTH_SHORT).show();
            reasonEt.setFocusable(true);
            return;
        } else if (reasonStr.length() > 200 || reasonStr.length() < 10) {
            Toast.makeText(this, "请输入10-200字看好理由及操作建议", Toast.LENGTH_SHORT).show();
            reasonEt.setFocusable(true);
            return;
        }
        if ("".equals(priceStr)) {
            Toast.makeText(this, "请输入定价", Toast.LENGTH_SHORT).show();
            priceEt.setFocusable(true);
            return;
        } else {
            int priceInt = Integer.valueOf(priceStr);
            if (priceInt > 2000 || priceInt < 50) {
                Toast.makeText(this, "请输50-2000的定价", Toast.LENGTH_SHORT).show();
                priceEt.setFocusable(true);
                return;
            }
        }
        if ("".equals(bondStr)) {
            Toast.makeText(this, "请输入保证金", Toast.LENGTH_SHORT).show();
            bondEt.setFocusable(true);
            return;
        } else {
            int priceInt = Integer.valueOf(priceStr);
            int bonInt = Integer.valueOf(bondStr);
            if (bonInt < priceInt) {
                Toast.makeText(this, "保证金必须大于或者等于订阅价格", Toast.LENGTH_SHORT).show();
                bondEt.setFocusable(true);
                return;
            }
        }
        if (!((CheckBox) findViewById(R.id.check_box_protocol)).isChecked()) {
            CustomToast.makeText(getApplicationContext(), "请阅读《发布投资计划协议》", Toast.LENGTH_SHORT).show();
            return;
        }
        // 做数据验证并跳转页面
        intent = new Intent(this, ReleaseConfirmActivity.class);
        intent.putExtra("titleStr", titleStr);
        intent.putExtra("codeStr", codeStr);
        intent.putExtra("codeNameStr", codeNameTv.getText());
        intent.putExtra("timeStr", timeStr);
        intent.putExtra("riseStr", riseStr);
        intent.putExtra("stopRatioStr", stopRatioStr);
        intent.putExtra("reasonStr", reasonStr);
        intent.putExtra("priceStr", priceStr);
        intent.putExtra("bondStr", bondStr);

        dialog.showDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用创建投资计划
                resstr = HttpUtil.getIntentData(Constants.moUrl + "/dealplan/index/getCycle?trade_days=" + timeStr);
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
                        JSONObject jsonObject = new JSONObject(resstr);
                        if ("failed".equals(jsonObject.getString("status"))) {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                            dialog.dismissDlog();
                            return;
                        }
                        JSONObject jsonData = new JSONObject(jsonObject.getString("data"));
                        intent.putExtra("startData", jsonData.getString("start"));
                        intent.putExtra("endData", jsonData.getString("end"));
                        dialog.dismissDlog();
                        // 跳转到确认页
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    /**
     * 跳转到规则页面
     *
     * @param view
     */
    public void RuleClick(View view) {
        // 发布投资计划协议
        Intent intent = new Intent(this, AgreementActivity.class);
        intent.putExtra("type", "20");
        startActivity(intent);
    }

    /**
     * 股票代码的选择控件
     *
     * @param view
     */
    public void CodeClick(View view) {
        Intent intent = new Intent(this, SearchSharesActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到协议页面
     *
     * @param view
     */
    public void ServiceClick(View view) {
        // 发布投资计划协议
        Intent intent = new Intent(this, AgreementActivity.class);
        intent.putExtra("type", "19");
        startActivity(intent);
    }

}
