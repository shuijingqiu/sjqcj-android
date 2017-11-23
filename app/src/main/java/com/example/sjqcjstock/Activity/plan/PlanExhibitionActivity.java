package com.example.sjqcjstock.Activity.plan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjqcjstock.Activity.AgreementActivity;
import com.example.sjqcjstock.Activity.RechargeActivity;
import com.example.sjqcjstock.Activity.stocks.SharesDetailedActivity;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.Activity.user.loginActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 计划展示页面
 * Created by Administrator on 2017/4/14.
 */
public class PlanExhibitionActivity extends Activity {

    // 头像
    private ImageView headImg;
    // 用户名
    private TextView userNameTv;
    // 简介
    private TextView userintro1;
    // 投资计划
    private TextView investmentPlanTv;
    // 总达标率
    private TextView totalComplianceRateTv;
    // 计划总收益
    private TextView totalPlannedIncomeTv;
    // 标题
    private TextView planTitleTv;
    // 预期盈利
    private TextView expectedProfitTv;
    // 交易时间
    private TextView timeTv;
    // 股票名称
    private TextView codeNameTv;
    // 股票代码
    private TextView codeTv;
    // 实际收益
    private TextView realIncomeTv;
    // 止损比率
    private TextView stopRatioTv;
    // 订阅价格
    private TextView subscriptionPriceTv;
    // 保证金
    private TextView bondTv;
    // 理由
    private TextView reasonTv;
    // 计划类型
    private TextView typeTv;
    // 提交按钮
    private Button submitBt;
    // 订阅人数
    private TextView palnCountTv;

    // 网络请求提示
    private CustomProgress dialog;
    // 接口返回数据
    private String resstr;
    private String resstr1;
    private String resstr2;
    // 计划id
    private String planId;
    // 用户Uid
    private String uid;
    private String name;
    // 支付价格
    private String sjbCount;
    // 股票名称
    private String stockName;
    // 股票代码
    private String stock;
    // 选股理由
    private String reason;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_exhibition);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        findView();
        getData();
    }

    /**
     * 页面控件的绑定
     */
    private void findView() {
        planId = getIntent().getStringExtra("id");
        uid = getIntent().getStringExtra("uid");
        dialog = new CustomProgress(this);
        dialog.showDialog();
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 头像
        headImg = (ImageView) findViewById(R.id.head_img);
        // 用户名
        userNameTv = (TextView) findViewById(R.id.user_name_tv);
        // 简介
        userintro1 = (TextView) findViewById(R.id.userintro1);
        // 投资计划
        investmentPlanTv = (TextView) findViewById(R.id.investment_plan_tv);
        // 总达标率
        totalComplianceRateTv = (TextView) findViewById(R.id.total_compliance_rate_tv);
        // 计划总收益
        totalPlannedIncomeTv = (TextView) findViewById(R.id.total_planned_income_tv);
        // 标题
        planTitleTv = (TextView) findViewById(R.id.plan_title_tv);
        // 预期盈利
        expectedProfitTv = (TextView) findViewById(R.id.expected_profit_tv);
        // 交易时间
        timeTv = (TextView) findViewById(R.id.time_tv);
        // 股票名称
        codeNameTv = (TextView) findViewById(R.id.code_name_tv);
        // 股票代码
        codeTv = (TextView) findViewById(R.id.code_tv);
        // 实际收益
        realIncomeTv = (TextView) findViewById(R.id.real_income_tv);
        // 止损比率
        stopRatioTv = (TextView) findViewById(R.id.stop_ratio_tv);
        // 订阅价格
        subscriptionPriceTv = (TextView) findViewById(R.id.subscription_price_tv);
        // 保证金
        bondTv = (TextView) findViewById(R.id.bond_tv);
        // 理由
        reasonTv = (TextView) findViewById(R.id.reason_tv);
        // 类型
        typeTv = (TextView) findViewById(R.id.type_tv);
        submitBt = (Button) findViewById(R.id.submit_bt);
        palnCountTv = (TextView) findViewById(R.id.paln_count_tv);
        if (Constants.staticmyuidstr.equals(uid)){
            submitBt.setBackgroundResource(R.drawable.buttonstyle10);
            submitBt.setText("不可订阅");
            submitBt.setClickable(false);
            palnCountTv.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 接口获取数据
     */
    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取投资计划博主信息
                resstr = HttpUtil.restHttpGet(Constants.moUrl + "/dealplan/blogger/info?uid=" + uid);
                handler.sendEmptyMessage(0);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取投资计划信息
                resstr1 = HttpUtil.restHttpGet(Constants.moUrl + "/dealplan/index/info?mid="+Constants.staticmyuidstr+"&id=" + planId);
                handler.sendEmptyMessage(1);
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
                        // 头像
                        ImageLoader.getInstance().displayImage(jsonData.getString("avatar"),
                                headImg, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
                        // 用户名
                        name = jsonData.getString("username");
                        userNameTv.setText(name);
                        // 简介
                        userintro1.setText(jsonData.getString("intro"));
                        // 投资计划数
                        investmentPlanTv.setText(jsonData.getString("count"));
                        // 总达标率
                        String successRatio = jsonData.getString("success_ratio");
                        // 总收益率
                        String totalRatio = jsonData.getString("total_ratio");
                        // 总达标率
                        totalComplianceRateTv.setText(successRatio);
                        // 计划总收益
                        totalPlannedIncomeTv.setText(totalRatio);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(resstr1);
                        if ("failed".equals(jsonObject.getString("status"))) {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                            dialog.dismissDlog();
                            return;
                        }
                        JSONObject jsonData = new JSONObject(jsonObject.getString("data"));
                        // 标题
                        planTitleTv.setText(jsonData.getString("title"));
                        // 预期盈利
                        expectedProfitTv.setText(Utils.getNumberFormat2(jsonData.getString("plan_ratio")) + "%");
                        String startTime = jsonData.getString("start").substring(5, 10);
                        startTime = startTime.replace("-",".");
                        String endTime = jsonData.getString("end").substring(5, 10);
                        endTime = endTime.replace("-",".");
                        // 交易时间
                        timeTv.setText(startTime+ "-" + endTime + " (" + jsonData.getString("trade_days") + "个交易日)");
                        stockName = jsonData.getString("stock_name");
                        stock = jsonData.getString("stock");
                        reason = jsonData.getString("reason");
                        palnCountTv.setText(jsonData.getString("buy_count")+" 人订阅");
                        // 理由
                        reasonTv.setText(reason);
                        // 订阅后再显示
                        if ("1".equals(jsonData.getString("desert")) || "3".equals(jsonData.getString("desert")) || Constants.staticmyuidstr.equals(uid)) {
                            // 股票名称
                            codeNameTv.setText(stockName);
                            // 股票代码
                            codeTv.setText(stock);
                            codeNameTv.setTextColor(codeNameTv.getResources().getColor(R.color.color_toptitle));
                            codeTv.setTextColor(codeTv.getResources().getColor(R.color.color_toptitle));
                            codeNameTv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(PlanExhibitionActivity.this, SharesDetailedActivity.class);
                                    intent.putExtra("name",stockName);
                                    intent.putExtra("code",stock);
                                    startActivity(intent);
                                }
                            });
                            codeTv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(PlanExhibitionActivity.this, SharesDetailedActivity.class);
                                    intent.putExtra("name",stockName);
                                    intent.putExtra("code",stock);
                                    startActivity(intent);
                                }
                            });
                            submitBt.setBackgroundResource(R.drawable.buttonstyle10);
                            submitBt.setClickable(false);
                            if("1".equals(jsonData.getString("desert"))){
                                submitBt.setText("已订阅");
                            }else{
                                submitBt.setText("不可订阅");
                            }
                        }else{
                            // 股票代码
                            codeTv.setText(stock.substring(0,3)+"***");
                        }
                        // 1未开始   2进行中   3成功   4失败
                        String status = jsonData.getString("status");
                        if ("1".equals(status)){
                            typeTv.setText("未开始");
                        }else if ("2".equals(status)){
                            typeTv.setText("进行中");
                        }else if ("3".equals(status)){
                            typeTv.setText("成功");
                        }else if ("4".equals(status)){
                            typeTv.setText("失败");
                        }else if ("5".equals(status)){
                            typeTv.setText("失效");
                        }else if("6".equals(status)){
                            typeTv.setText("核算中");
                        }

                        double income = Double.valueOf(Utils.getNumberFormat2(jsonData.getString("income")));
                        //  实际收益
                        realIncomeTv.setText( income + "%");
                        if (income >= 0) {
                            realIncomeTv.setTextColor(realIncomeTv.getResources().getColor(R.color.color_ef3e3e));
                        } else {
                            realIncomeTv.setTextColor(realIncomeTv.getResources().getColor(R.color.color_1bc07d));
                        }
                        // 止损比率
                        stopRatioTv.setText("-"+Utils.getNumberFormat2(jsonData.getString("stop_loss_ratio")) + "%");
                        // 订阅价格
                        subscriptionPriceTv.setText(jsonData.getString("desert_price"));
                        sjbCount = jsonData.getString("desert_price");
                        // 保证金
                        bondTv.setText(jsonData.getString("margin"));

                        dialog.dismissDlog();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        JSONObject jsonObject = new JSONObject(resstr2);
                        Toast.makeText(getApplicationContext(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                        if ("failed".equals(jsonObject.getString("status"))) {
                            dialog.dismissDlog();
                        } else {
                            // 显示订阅
                            // 股票名称
                            codeNameTv.setText(stockName);
                            // 股票代码
                            codeTv.setText(stock);
                            // 理由
                            reasonTv.setText(reason);
                            dialog.dismissDlog();

                            submitBt.setBackgroundResource(R.drawable.buttonstyle10);
                            submitBt.setText("已订阅");
                            submitBt.setClickable(false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    /**
     * 跳转到协议页面
     *
     * @param view
     */
    public void ServiceClick(View view) {
        // 购买计划协议
        Intent intent = new Intent(this, AgreementActivity.class);
        intent.putExtra("type", "18");
        startActivity(intent);
    }

    /**
     * 订阅查看的跳转
     *
     * @param view
     */
    public void SubmitData(View view) {
        // menghuan 不用登陆也可以用
        // 如果未登陆跳转到登陆页面
        if (!Constants.isLogin){
            Intent intent = new Intent(this, loginActivity.class);
            startActivity(intent);
            return;
        }

        if (!((CheckBox) findViewById(R.id.check_box_protocol)).isChecked()) {
            CustomToast.makeText(getApplicationContext(), "请阅读《订阅投资计划协议》", Toast.LENGTH_SHORT).show();
            return;
        }
        final View viewSang = LayoutInflater.from(PlanExhibitionActivity.this).inflate(R.layout.dialog_plan, null);
        RelativeLayout rl_dialogDismiss = (RelativeLayout) viewSang.findViewById(R.id.rl_dialogDismiss);

        //显示需要支付水晶币数量的控件
        TextView inputCount = (TextView) viewSang.findViewById(R.id.tv_inputCount);
        inputCount.setText("需要支付："+sjbCount+"水晶币");

        // 显示水晶币数量的控件
        TextView restCount = (TextView) viewSang.findViewById(R.id.tv_restCount);
        restCount.setText(Constants.shuijinbiCount);
        Button bt_ok = (Button) viewSang.findViewById(R.id.bt_ok);

        final AlertDialog alertDialog = new AlertDialog.Builder(PlanExhibitionActivity.this).setView(viewSang).show();
        rl_dialogDismiss.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //销毁弹出框
                alertDialog.dismiss();
            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Integer.valueOf(Constants.shuijinbiCount) < Integer.valueOf(sjbCount)) {
                    CustomToast.makeText(getApplicationContext(), "对不起你水晶币不足。", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if (!((CheckBox) viewSang.findViewById(R.id.sang_agreement_ck)).isChecked()) {
                    CustomToast.makeText(getApplicationContext(), "请阅读《订阅投资计划协议》", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.showDialog();
                // 确认支付订阅的接口
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List dataList = new ArrayList();
                        // 用户ID
                        dataList.add(new BasicNameValuePair("uid", Constants.staticmyuidstr));
                        dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                        dataList.add(new BasicNameValuePair("id", planId));
                        // 订阅投资计划
                        resstr2 = HttpUtil.restHttpPost(Constants.moUrl + "/dealplan/index/desert", dataList);
                        handler.sendEmptyMessage(2);
                    }
                }).start();

                alertDialog.dismiss();
            }
        });

        viewSang.findViewById(R.id.sang_agreement_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到水晶币协议页面
                Intent intent = new Intent(PlanExhibitionActivity.this, AgreementActivity.class);
                intent.putExtra("type","18");
                startActivity(intent);
            }
        });

        viewSang.findViewById(R.id.bt_cz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到充值页面
                Intent intent = new Intent(PlanExhibitionActivity.this, RechargeActivity.class);
                startActivity(intent);
                finish();
            }
        });

//        dialog.showDialog();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                List dataList = new ArrayList();
//                // 用户ID
//                dataList.add(new BasicNameValuePair("uid", Constants.staticmyuidstr));
//                dataList.add(new BasicNameValuePair("token", Constants.apptoken));
//                dataList.add(new BasicNameValuePair("id", planId));
//                // 订阅投资计划
//                resstr2 = HttpUtil.restHttpPost(Constants.moUrl + "/dealplan/index/desert", dataList);
//                handler.sendEmptyMessage(2);
//            }
//        }).start();
    }

    /**
     * 跳转到计划个数页面
     *
     * @param view
     */
    public void PlanCountClick(View view) {
        Intent intent = new Intent(this, UserPlanActivity.class);
        intent.putExtra("uid", uid);
        intent.putExtra("name", name);
        startActivity(intent);
    }

    /**
     * 跳转到用户主页
     * @param view
     */
    public void UserDataClick(View view){
        // 跳转到个人主页
        Intent intent = new Intent(this,
                UserDetailNewActivity.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }

    /**
     * 投资计划规则
     *
     * @param view
     */
    public void RuleClick(View view) {
        // 发布投资计划规则
        Intent intent = new Intent(this, AgreementActivity.class);
        intent.putExtra("type", "20");
        startActivity(intent);
    }

}
