package com.example.sjqcjstock.Activity.stocks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.stocks.StocksInfo;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.netutil.sharesUtil;
import com.example.sjqcjstock.view.CustomToast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 买卖页面的Activity
 * Created by Administrator on 2016/8/10.
 */
public class BusinessActivity extends Activity {

    // 页面标题
    private TextView titleName;
    // 股票代码
    private EditText codeEt;
    // 股票价格
    private EditText priceEt;
    // 交易数量
    private EditText numberEt;
    // 股票名称
    private TextView nameTv;
    // 跌停价格
    private TextView priceLimitTv;
    // 涨停价格
    private TextView priceAmexTv;
    // 交易数量
    private TextView businessNumberTv;
    // 交易数量(文字)
    private TextView businessNumberTv1;
    // 买卖价格
    private TextView priceSell1;
    private TextView priceSell2;
    private TextView priceSell3;
    private TextView priceSell4;
    private TextView priceSell5;
    private TextView priceBuy1;
    private TextView priceBuy2;
    private TextView priceBuy3;
    private TextView priceBuy4;
    private TextView priceBuy5;
    // 买卖数量
    private TextView numberSell1;
    private TextView numberSell2;
    private TextView numberSell3;
    private TextView numberSell4;
    private TextView numberSell5;
    private TextView numberBuy1;
    private TextView numberBuy2;
    private TextView numberBuy3;
    private TextView numberBuy4;
    private TextView numberBuy5;
    // 买卖类型(1代表买入 2代表卖出)
    private String type = "2";
    // 股票输入代码
    private String code = "";
    // 股票获取处理后的信息
    private StocksInfo  stocksInfo = null;
    // 当前价格
    private String spotPrice = "0";
    // 当前可卖买的股票数量
    private String businessNumber = "0";
    // 网络请求提示
    private ProgressDialog dialog;
    // 调用买卖接口返回的数据
    private String resstr = "";
    // 确认的弹框
    private AlertDialog alertDialog;
    // 调用接口获取用户的交易排名信息
    private String xxstr = "";
    // 可用资金
    private String totalAmount = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_business);
        ExitApplication.getInstance().addActivity(this);
        type = getIntent().getStringExtra("type");
        code = getIntent().getStringExtra("code");
        businessNumber = getIntent().getStringExtra("number");
        if (businessNumber == null){
            businessNumber = "0";
        }
        findView();
        getData();
        if (code !=null && !"".equals(code)){
            codeEt.setText(code);
            getData1();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!"".equals(Constants.choiceCode)){
            code = Constants.choiceCode;
            codeEt.setText(code);
            getData1();
            Constants.choiceCode = "";
        }
    }

    /**
     * 页面的绑定
     */
    private void findView() {
        dialog = new ProgressDialog(this);
        dialog.setMessage(Constants.loadMessage);
        dialog.setCancelable(true);
        /**
         * 返回按钮的事件绑定
         */
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleName = (TextView) findViewById(R.id.title_name);
        codeEt = (EditText) findViewById(R.id.code_et);
        priceEt = (EditText) findViewById(R.id.price_et);
        numberEt = (EditText) findViewById(R.id.number_et);
        nameTv = (TextView) findViewById(R.id.name_tv);
        priceLimitTv = (TextView) findViewById(R.id.price_limit_tv);
        priceAmexTv = (TextView) findViewById(R.id.price_amex_tv);
        businessNumberTv = (TextView) findViewById(R.id.business_number_tv);
        businessNumberTv1 = (TextView) findViewById(R.id.business_number);
        priceSell1 = (TextView) findViewById(R.id.price_sell_1);
        priceSell2 = (TextView) findViewById(R.id.price_sell_2);
        priceSell3 = (TextView) findViewById(R.id.price_sell_3);
        priceSell4 = (TextView) findViewById(R.id.price_sell_4);
        priceSell5 = (TextView) findViewById(R.id.price_sell_5);
        priceBuy1 = (TextView) findViewById(R.id.price_buy_1);
        priceBuy2 = (TextView) findViewById(R.id.price_buy_2);
        priceBuy3 = (TextView) findViewById(R.id.price_buy_3);
        priceBuy4 = (TextView) findViewById(R.id.price_buy_4);
        priceBuy5 = (TextView) findViewById(R.id.price_buy_5);
        // 买卖数量
        numberSell1 = (TextView) findViewById(R.id.number_sell_1);
        numberSell2 = (TextView) findViewById(R.id.number_sell_2);
        numberSell3 = (TextView) findViewById(R.id.number_sell_3);
        numberSell4 = (TextView) findViewById(R.id.number_sell_4);
        numberSell5 = (TextView) findViewById(R.id.number_sell_5);
        numberBuy1 = (TextView) findViewById(R.id.number_buy_1);
        numberBuy2 = (TextView) findViewById(R.id.number_buy_2);
        numberBuy3 = (TextView) findViewById(R.id.number_buy_3);
        numberBuy4 = (TextView) findViewById(R.id.number_buy_4);
        numberBuy5 = (TextView) findViewById(R.id.number_buy_5);
        if ("2".equals(type)) {
            titleName.setText("模拟盘-卖出");
            ((Button) findViewById(R.id.confirm_business)).setText("卖出");
            ((Button) findViewById(R.id.confirm_business1)).setText("市价卖出");
            businessNumberTv1.setText("可卖");
        } else {
            titleName.setText("模拟盘-买入");
            ((Button) findViewById(R.id.confirm_business)).setText("买入");
            ((Button) findViewById(R.id.confirm_business1)).setText("市价买入");
            businessNumberTv1.setText("可买");
        }

//        // 监听股票代码输入状态
//        codeEt.addTextChangedListener(watcher);
        codeEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"2".equals(type)) {
                    Intent intent = new Intent(BusinessActivity.this, SearchSharesActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    /**
     * 开线程获取当前用户账户信息
     */
    private void getData(){
        // 开线程获取用户账户信息和总盈利排名
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用接口获取用户账户信息和总盈利排名
                xxstr = HttpUtil.restHttpGet(Constants.moUrl+"/users/"+Constants.staticmyuidstr+"&token="+Constants.apptoken+"&uid="+Constants.staticmyuidstr);
                handler.sendEmptyMessage(2);
            }
        }).start();
    }
    /**
     * 开线程获取当前股票信息
     */
    private void getData1(){
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用接口获取股票当前行情数据
                stocksInfo = new sharesUtil().processOrderData(Utils.judgeSharesCode(code));
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    /**
     * 增加价格的方法
     *
     * @param view
     */
    public void priceRaise(View view) {
        if(stocksInfo!=null){
            if (Double.valueOf(spotPrice)>0){
                spotPrice = Utils.getNumberFormat2((Float.valueOf(spotPrice)+0.01) + "");
                if(Double.valueOf(spotPrice) > Double.valueOf(stocksInfo.getHighLimit())){
                    spotPrice = stocksInfo.getHighLimit();
                }
                priceEt.setText(spotPrice);
                priceEt.setSelection(priceEt.length());
            }
        }
    }

    /**
     * 减少价格的方法
     *
     * @param view
     */
    public void priceReduce(View view) {
        if(stocksInfo!=null){
            if (Double.valueOf(spotPrice)>0){
                spotPrice = Utils.getNumberFormat2((Float.valueOf(spotPrice)-0.01) + "");
                if(Double.valueOf(stocksInfo.getPriceLimit())>Double.valueOf(spotPrice)){
                    spotPrice = stocksInfo.getPriceLimit();
                }
                priceEt.setText(spotPrice);
                priceEt.setSelection(priceEt.length());
            }
        }
    }

    /**
     * 四分之一的数量
     *
     * @param view
     */
    public void numberQuarter(View view) {
        if (businessNumber == null) return;
        if ("2".equals(type)) {
            numberEt.setText(Integer.valueOf(businessNumber) / 4 + "");
        }else{
            numberEt.setText(Integer.valueOf(businessNumber) / 4 / 100 * 100 + "");
        }
    }

    /**
     * 二分之一的数量
     *
     * @param view
     */
    public void numberAhalf(View view) {
        if (businessNumber == null) return;
        if ("2".equals(type)) {
            numberEt.setText(Integer.valueOf(businessNumber) / 2 + "");
        }else{
            numberEt.setText(Integer.valueOf(businessNumber) / 2 / 100 * 100 + "");
        }
    }

    /**
     * 全部的数量
     *
     * @param view
     */
    public void numberAll(View view) {
        if (businessNumber == null) return;
        numberEt.setText(businessNumber);
    }

    /**
     * 确认买卖的方法
     *
     * @param view
     */
    public void confirmBusiness(View view) {
        if (Utils.isFastDoubleClick3()){
            return;
        }
        if (stocksInfo.getZuoShou() == null ||Double.valueOf(stocksInfo.getZuoShou()) <= 0){
            CustomToast.makeText(getApplicationContext(), "该股票已停牌", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (Double.valueOf(spotPrice) <= 0){
            CustomToast.makeText(getApplicationContext(), "集合竞价中不进行交易", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        String number = numberEt.getText().toString();
        if (businessNumber==null || Double.valueOf(businessNumber) < 1){
            number = "0";
        }
        // 当前输入的价格
        spotPrice = priceEt.getText().toString();
        if ("".equals(code)){
            CustomToast.makeText(getApplicationContext(), "请输股票代码", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if ("".equals(spotPrice)){
            CustomToast.makeText(getApplicationContext(), "请输入正确价格", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if ("".equals(number)){
            CustomToast.makeText(getApplicationContext(), "请输交易数量", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        if ("2".equals(type)) {
            if(Integer.valueOf(number) == 0){
                CustomToast.makeText(getApplicationContext(), "对不起你没有持仓数量", Toast.LENGTH_SHORT)
                        .show();
                numberEt.setText("0");
                return;
            }
        }else {
            if(Integer.valueOf(number) < 100){
                numberEt.setText("100");
                number = "100";
            }
            if (Integer.valueOf(number) % 100 != 0) {
                CustomToast.makeText(getApplicationContext(), "请输入100倍数的数量", Toast.LENGTH_SHORT)
                        .show();
                number = Integer.valueOf(number) / 100 + "00";
                numberEt.setText(number);
                return;
            }
        }
        if(Integer.valueOf(number) > Double.valueOf(businessNumber)){
            numberEt.setText(businessNumber);
        }
        // 判断输入价格是否小于跌停价格
        if(Double.valueOf(stocksInfo.getPriceLimit())>Double.valueOf(spotPrice)){
            spotPrice = stocksInfo.getPriceLimit();
            priceEt.setText(spotPrice);
        }
        // 判断输入价格是否大于涨停价格
        if(Double.valueOf(spotPrice) > Double.valueOf(stocksInfo.getHighLimit())){
            spotPrice = stocksInfo.getHighLimit();
            priceEt.setText(spotPrice);
        }
        priceEt.setSelection(priceEt.length());
        showDialog("2");
    }

    /**
     * 市价的买卖方法
     *
     * @param view
     */
    public void confirmBusiness1(View view) {
        if (Utils.isFastDoubleClick3()){
            return;
        }
        if (stocksInfo.getZuoShou() == null ||Double.valueOf(stocksInfo.getZuoShou()) <= 0){
            CustomToast.makeText(getApplicationContext(), "该股票已停牌", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (Double.valueOf(spotPrice) <= 0){
            CustomToast.makeText(getApplicationContext(), "停牌或集合竞价中不进行交易", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        String number = numberEt.getText().toString();
        if (businessNumber==null || Double.valueOf(businessNumber) < 1){
            number = "0";
        }
        // 当前输入的价格
        String spotPriceStr = priceEt.getText().toString();
        if ("".equals(code)){
            CustomToast.makeText(getApplicationContext(), "请输股票代码", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if ("".equals(spotPriceStr)){
            CustomToast.makeText(getApplicationContext(), "请输入正确价格", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if ("".equals(number)){
            CustomToast.makeText(getApplicationContext(), "请输入交易数量", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if ("2".equals(type)) {
            if(Integer.valueOf(number) == 0){
                CustomToast.makeText(getApplicationContext(), "对不起你没有持仓数量", Toast.LENGTH_SHORT)
                        .show();
                numberEt.setText("0");
                return;
            }
        }else{
            if(Integer.valueOf(number) < 100){
                numberEt.setText("100");
                number = "100";
            }
            if(Integer.valueOf(number)%100 != 0){
                CustomToast.makeText(getApplicationContext(), "请输入100倍数的数量", Toast.LENGTH_SHORT)
                        .show();
                number = Integer.valueOf(number)/100+"00";
                numberEt.setText(number);
                return;
            }
        }
        showDialog("1");
    }

    /**
     * 弹出显示确认的dialog
     */
    private void showDialog(final String str) {
        View viewDialog = LayoutInflater.from(this).inflate(R.layout.dialog_business, null);
        TextView dialogTitle = (TextView) viewDialog.findViewById(R.id.dialog_title);
        Button dialogConfirm = (Button) viewDialog.findViewById(R.id.dialog_confirm);
        TextView nameTv = (TextView) viewDialog.findViewById(R.id.dialog_name);
        TextView codeTv = (TextView) viewDialog.findViewById(R.id.dialog_code);
        TextView priceTv = (TextView) viewDialog.findViewById(R.id.dialog_price);
        TextView numberTv = (TextView) viewDialog.findViewById(R.id.dialog_number);
        // 购买数量
        final String number = numberEt.getText().toString();
        // 买卖价格
        String priceStr = "";
        nameTv.setText(stocksInfo.getName());
        codeTv.setText(stocksInfo.getCode());
        numberTv.setText(number);
        if ("1".equals(str)){
            // 市场价格买入
            priceStr = "0";
            priceTv.setText("--");
        }else{
            priceStr = spotPrice;
            priceTv.setText(priceStr);
        }
        final String price = priceStr;
        if ("2".equals(type)) {
            dialogTitle.setText("委托卖出确认");
            dialogConfirm.setText("确认卖出");
        } else {
            dialogTitle.setText("委托买入确认");
            dialogConfirm.setText("确认买入");
        }

        alertDialog = new AlertDialog.Builder(this).setView(viewDialog).show();
        alertDialog.setCancelable(false);
        // 确认买卖
        dialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                // 开线程获取股票数据信息
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List dataList = new ArrayList();
                        // 用户ID
                        dataList.add(new BasicNameValuePair("uid", Constants.staticmyuidstr));
                        // 股票代码
                        dataList.add(new BasicNameValuePair("stock", stocksInfo.getCode()));
                        // 购买金额
                        dataList.add(new BasicNameValuePair("price", price));
                        // 购买数量（必选大于100）
                        dataList.add(new BasicNameValuePair("number", number));
                        // 购买的方式（1代表买入 2代表卖出）
                        dataList.add(new BasicNameValuePair("type", type));
                        // 账户的类型 （账户扩展字段，目前只有1个）
                        dataList.add(new BasicNameValuePair("sorts", "1"));
                        // 是否市价买入 （1代表市价买入 2代表下单买入）
                        dataList.add(new BasicNameValuePair("isMarket", str));
                        // 调用接口发送买卖数据到后台
                        resstr = HttpUtil.restHttpPost(Constants.moUrl+"/orders&token="+Constants.apptoken,dataList);
                        handler.sendEmptyMessage(1);
                    }
                }).start();
            }
        });
        // 取消买卖
        viewDialog.findViewById(R.id.dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
//
//    /**
//     * 监听股票代码的输入状态
//     */
//    private TextWatcher watcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            code = s.toString();
//            if (s.toString().length() == 6){
//                code = Utils.judgeSharesCode(code);
//                dialog.show();
//                // 开线程获取股票数据信息
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // 调用接口获取股票当前行情数据
//                        stocksInfo = new sharesUtil().processOrderData(code);
//                        handler.sendEmptyMessage(0);
//                    }
//                }).start();
//            }
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//
//        }
//    };

    /**
     * 线程更新Ui
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (stocksInfo != null){
                        // 当前价格
                        spotPrice = stocksInfo.getSpotPrice();
                        priceEt.setText(spotPrice);
                        priceEt.setSelection(priceEt.length());
                        if ("1".equals(type)&& Double.valueOf(spotPrice)>0) {
                            businessNumber = Double.valueOf(totalAmount)*0.9999 / Double.valueOf(spotPrice) + "";
                            businessNumber = (int) (Double.valueOf(businessNumber) / 100) * 100 + "";
                        }
                        businessNumberTv.setText(businessNumber);
                        nameTv.setText(stocksInfo.getName());
                        priceLimitTv.setText("跌停 " + stocksInfo.getPriceLimit());
                        priceAmexTv.setText("涨停 " + stocksInfo.getHighLimit());
                        priceSell1.setText(stocksInfo.getBuySellMap().get("sell1P"));
                        numberSell1.setText(stocksInfo.getBuySellMap().get("sell1N"));
                        priceSell2.setText(stocksInfo.getBuySellMap().get("sell2P"));
                        numberSell2.setText(stocksInfo.getBuySellMap().get("sell2N"));
                        priceSell3.setText(stocksInfo.getBuySellMap().get("sell3P"));
                        numberSell3.setText(stocksInfo.getBuySellMap().get("sell3N"));
                        priceSell4.setText(stocksInfo.getBuySellMap().get("sell4P"));
                        numberSell4.setText(stocksInfo.getBuySellMap().get("sell4N"));
                        priceSell5.setText(stocksInfo.getBuySellMap().get("sell5P"));
                        numberSell5.setText(stocksInfo.getBuySellMap().get("sell5N"));

                        priceBuy1.setText(stocksInfo.getBuySellMap().get("buy1P"));
                        numberBuy1.setText(stocksInfo.getBuySellMap().get("buy1N"));
                        priceBuy2.setText(stocksInfo.getBuySellMap().get("buy2P"));
                        numberBuy2.setText(stocksInfo.getBuySellMap().get("buy2N"));
                        priceBuy3.setText(stocksInfo.getBuySellMap().get("buy3P"));
                        numberBuy3.setText(stocksInfo.getBuySellMap().get("buy3N"));
                        priceBuy4.setText(stocksInfo.getBuySellMap().get("buy4P"));
                        numberBuy4.setText(stocksInfo.getBuySellMap().get("buy4N"));
                        priceBuy5.setText(stocksInfo.getBuySellMap().get("buy5P"));
                        numberBuy5.setText(stocksInfo.getBuySellMap().get("buy5N"));

                        int color = priceBuy1.getResources().getColor(R.color.color_1bc07d);
                        if("1".equals(stocksInfo.getBuySellMap().get("increaseType"))){
                            color = priceBuy1.getResources().getColor(R.color.color_ef3e3e);
                        }
                        priceBuy1.setTextColor(color);
                        priceBuy2.setTextColor(color);
                        priceBuy3.setTextColor(color);
                        priceBuy4.setTextColor(color);
                        priceBuy5.setTextColor(color);
                        priceSell1.setTextColor(color);
                        priceSell2.setTextColor(color);
                        priceSell3.setTextColor(color);
                        priceSell4.setTextColor(color);
                        priceSell5.setTextColor(color);
                    }else{
                        clerData();
                    }
                    dialog.dismiss();
                    break;
                case 1:
                    if(!"".equals(resstr.trim())){
                        try {
                            JSONObject jsonObject = new JSONObject(resstr);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                            if("success".equals(jsonObject.getString("status"))){
                                dialog.dismiss();
                                if(alertDialog !=null )
                                {
                                    alertDialog.dismiss();
                                }
                                Constants.isBuy = true;
                                if ("2".equals(type)) {
                                    // （如果购买成功清空数据）卖的情况
                                    finish();
                                }else{
                                    // 设置买卖成功 用于个人账户刷新
                                    clerData();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if(alertDialog !=null )
                    {
                        alertDialog.dismiss();
                    }
                    dialog.dismiss();
                    break;
                case 2:
                    try {
                        if("".equals(xxstr)){
                            CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        JSONObject jsonObject = new JSONObject(xxstr);
                        if ("failed".equals(jsonObject.getString("status"))){
                            Toast.makeText(BusinessActivity.this, jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            JSONObject jsonData = new JSONObject(jsonObject.getString("data"));
                            totalAmount = jsonData.getString("available_funds");
                            if ("1".equals(type) && Double.valueOf(spotPrice)>0) {
                                businessNumber = Double.valueOf(totalAmount) / Double.valueOf(spotPrice) + "";
                                businessNumber = (int) (Double.valueOf(businessNumber) / 100) * 100 + "";
                                businessNumberTv.setText(businessNumber);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    /**
     * 清空还原数据
     */
    private void clerData(){
        // 当前价格
        spotPrice = "0";
        priceEt.setText("");
        businessNumber = "0";
        businessNumberTv.setText("");
        nameTv.setText("");
        priceLimitTv.setText("跌停 0");
        priceAmexTv.setText("涨停 0");
        priceSell1.setText("0");
        numberSell1.setText("0");
        priceSell2.setText("0");
        numberSell2.setText("0");
        priceSell3.setText("0");
        numberSell3.setText("0");
        priceSell4.setText("0");
        numberSell4.setText("0");
        priceSell5.setText("0");
        numberSell5.setText("0");
        priceBuy1.setText("0");
        numberBuy1.setText("0");
        priceBuy2.setText("0");
        numberBuy2.setText("0");
        priceBuy3.setText("0");
        numberBuy3.setText("0");
        priceBuy4.setText("0");
        numberBuy4.setText("0");
        priceBuy5.setText("0");
        numberBuy5.setText("0");
        codeEt.setText("");
        numberEt.setText("");
    }
}
