package com.example.sjqcjstock.Activity.firm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjqcjstock.Activity.stocks.SearchSharesActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 实盘盘中报单页面
 * Created by Administrator on 2017/8/29.
 */
public class  FirmEndDeclarationFormActivity extends Activity {

    // 网络请求提示
    private CustomProgress dialog;
    // 实盘标题
    private TextView firmTitleTv;
    // 股数
    private EditText numberEt;
    // 价格
    private EditText priceEt;
    // 单个水晶币个数
    private EditText sjbCountEt1;
    // 全部水晶币个数
    private EditText sjbCountEt2;
    // 股票代码
    private EditText codeEt;
    // 交易类型
    private RadioButton buyRb;
    // 比赛id
    private String firmId;
    // 比赛名称
    private String title;
    // 接口饭会数据
    private String joinStr;
    // 全部打赏水晶币个数
    private int sjbCount = 0;
    // 选择的股票代码
    private String code;
    // 选择的股票名称
    private String codeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firm_end_declaration_form);
        findView();
        getData();
    }

    private void findView() {
        dialog = new CustomProgress(this);
        firmId = getIntent().getStringExtra("firmId");
        title = getIntent().getStringExtra("title");
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        firmTitleTv = (TextView) findViewById(R.id.firm_title_tv);
        firmTitleTv.setText(title);
        buyRb = (RadioButton) findViewById(R.id.buy_rb);
        numberEt = (EditText) findViewById(R.id.number_et);
        priceEt = (EditText) findViewById(R.id.price_et);
        sjbCountEt1 = (EditText) findViewById(R.id.sjb_count_et);
        sjbCountEt2 = (EditText) findViewById(R.id.sjb_count1_et);
        codeEt = (EditText) findViewById(R.id.code_et);
        codeEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirmEndDeclarationFormActivity.this, SearchSharesActivity.class);
                startActivityForResult(intent,1);
            }
        });
        sjbCountEt2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // 设置全部水晶币时失去焦点的时候改变
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容

                } else {
                    String sjbStr = sjbCountEt2.getText()+"";
                    if ("".equals(sjbStr)){
                        CustomToast.makeText(getApplicationContext(), "请输入正确的水晶币个数", Toast.LENGTH_SHORT).show();
//                        sjbCountEt2.setFocusable(true);
//                        sjbCountEt2.requestFocus();
                        return;
                    }
                    // 现在的水晶币个数
                    final int sjb =  Integer.valueOf(sjbStr);
                    // 此处为失去焦点时的处理内容
                    if (sjbCount != sjb){
                        sjbCount = sjb;
                        // 调用接口修改水晶币
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                List dataList = new ArrayList();
                                dataList.add(new BasicNameValuePair("mid", Constants.staticmyuidstr));
                                dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                                // 参赛id
                                dataList.add(new BasicNameValuePair("id", firmId));
                                // 订阅价格
                                dataList.add(new BasicNameValuePair("reward", sjb+""));
                                joinStr = HttpUtil.restHttpPost(Constants.newUrl + "/api/match/price", dataList);
                                handler.sendEmptyMessage(1);
                            }
                        }).start();
                    }
                }
            }
        });
    }

    private void getData(){
        dialog.showDialog();
        // 获取比赛水晶币的信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                joinStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/match/info?mid="+Constants.staticmyuidstr+"&token="+Constants.apptoken+"&id="+firmId);
                handler.sendEmptyMessage(2);
            }
        }).start();
    }
    /**
     *
     * @param requestCode 请求码
     * @param resultCode 返回码
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (data==null){
                return;
            }
            code = data.getExtras().get("code")+"";
            codeName  = data.getExtras().get("name")+"";
            // 处理选择的股票的
            if (!"".equals(code)){
                if (1 == requestCode){
                    codeEt.setText(codeName+"("+code+")");
                }
        }
    }

    /**
     * 提交信息
     *
     * @param view
     */
    public void SubmitData(View view) {
        // 股票
        if (code  ==null || "".equals(code)) {
            Toast.makeText(getApplicationContext(), "请输入交易股票", Toast.LENGTH_SHORT).show();
            return;
        }
        final String number = numberEt.getText()+"";
        // 交易股数
        if ("".equals(number)) {
            Toast.makeText(getApplicationContext(), "请输入交易股数", Toast.LENGTH_SHORT).show();
            numberEt.setFocusable(true);
            numberEt.requestFocus();
            return;
        }
        final String price = priceEt.getText()+"";
        // 交易价格
        if ("".equals(price)) {
            Toast.makeText(getApplicationContext(), "请输入交易价格", Toast.LENGTH_SHORT).show();
            priceEt.setFocusable(true);
            priceEt.requestFocus();
            return;
        }
        String type = "2";
        if (buyRb.isChecked()){
            type = "1";
        }
        final String typeStr = type;
        String remarks = sjbCountEt1.getText()+"";
        if (!"".equals(remarks)){
            remarks = Integer.valueOf(remarks)+"";
        }else{
            remarks = "0";
        }
        final String remarksStr = remarks;

        dialog.showDialog();
        // 盘中报单
        new Thread(new Runnable() {
            @Override
            public void run() {
                List dataList = new ArrayList();
                dataList.add(new BasicNameValuePair("mid", Constants.staticmyuidstr));
                dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                // 比赛id
                dataList.add(new BasicNameValuePair("match_id", firmId));
                // 股票代码
                dataList.add(new BasicNameValuePair("stock", code));
                // 股票名称
                dataList.add(new BasicNameValuePair("stock_name", codeName));
                // 交易价格
                dataList.add(new BasicNameValuePair("price", price));
                // 交易股数
                dataList.add(new BasicNameValuePair("number", number));
                // 1买入 2卖出
                dataList.add(new BasicNameValuePair("type", typeStr));
                // 打赏价格
                dataList.add(new BasicNameValuePair("reward", remarksStr));

                joinStr = HttpUtil.restHttpPost(Constants.newUrl + "/api/match/handle", dataList);
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
                        JSONObject jsonObject = new JSONObject(joinStr);
                        String code = jsonObject.getString("code");
                        String msgStr = jsonObject.getString("msg");
                        dialog.dismissDlog();
                        CustomToast.makeText(getApplicationContext(), msgStr, Toast.LENGTH_SHORT).show();
                        if (Constants.successCode.equals(code)) {
                            // 参加成功 清空当前数据 停留当前页面
                            codeEt.setText("");
                            numberEt.setText("");
                            priceEt.setText("");
                            numberEt.setFocusable(true);
                            buyRb.setChecked(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(joinStr);
                        String msgStr = jsonObject.getString("msg");
                        CustomToast.makeText(getApplicationContext(), msgStr, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        JSONObject jsonObject = new JSONObject(joinStr);
                        String code = jsonObject.getString("code");
                        if (Constants.successCode.equals(code)) {
                            JSONObject jsonData = jsonObject.getJSONObject("data");
                            String reward = jsonData.getString("reward");
                            String desert_reward = jsonData.getString("desert_reward");
                            reward = reward == "null" ?"0":reward;
                            desert_reward = desert_reward == "null" ?"0":desert_reward;
                            sjbCountEt1.setText(reward);
                            sjbCountEt2.setText(desert_reward);
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
