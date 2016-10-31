package com.example.sjqcjstock.Activity.stocks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.stocks.StocksInfo;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.netutil.sharesUtil;

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
    // 买卖类型
    private String type = "0";
    // 股票输入代码
    private String code = "";
    // 股票获取处理后的信息
    private StocksInfo  stocksInfo = null;
    // 当前价格
    private String spotPrice = "0";
    // 当前可买的股票数量
    private String businessNumber = "";
    // 网络请求提示
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_business);
        ExitApplication.getInstance().addActivity(this);
        type = getIntent().getStringExtra("type");
        findView();
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
        if ("1".equals(type)) {
            titleName.setText("模拟盘-卖出");
            ((Button) findViewById(R.id.confirm_business)).setText("卖出");
            ((Button) findViewById(R.id.confirm_business1)).setText("市场卖出");
            businessNumberTv1.setText("可卖");
        } else {
            titleName.setText("模拟盘-买入");
            ((Button) findViewById(R.id.confirm_business)).setText("买入");
            ((Button) findViewById(R.id.confirm_business1)).setText("市场买入");
            businessNumberTv1.setText("可买");
        }
        // 监听股票代码输入状态
        codeEt.addTextChangedListener(watcher);
    }

    /**
     * 增加价格的方法
     *
     * @param view
     */
    public void priceRaise(View view) {
        if (Double.valueOf(spotPrice)>0){
            spotPrice = Utils.getNumberFormat1((Float.valueOf(spotPrice)+0.01) + "");
            priceEt.setText(spotPrice);
        }
    }

    /**
     * 减少价格的方法
     *
     * @param view
     */
    public void priceReduce(View view) {
        if (Double.valueOf(spotPrice)>0){
            spotPrice = Utils.getNumberFormat1((Float.valueOf(spotPrice)-0.01) + "");
            priceEt.setText(spotPrice);
        }
    }

    /**
     * 四分之一的数量
     *
     * @param view
     */
    public void numberQuarter(View view) {
        numberEt.setText(Integer.valueOf(businessNumber)/4+"");
    }

    /**
     * 二分之一的数量
     *
     * @param view
     */
    public void numberAhalf(View view) {
        numberEt.setText(Integer.valueOf(businessNumber)/2+"");
    }

    /**
     * 全部的数量
     *
     * @param view
     */
    public void numberAll(View view) {
        numberEt.setText(businessNumber);
    }

    /**
     * 确认买卖的方法
     *
     * @param view
     */
    public void confirmBusiness(View view) {
        String number = numberEt.getText().toString();
        if (Double.valueOf(spotPrice)>0 && !"".equals(number) && Double.valueOf(number)>0){
            showDialog("0");
        }
    }

    /**
     * 市场价的买卖方法
     *
     * @param view
     */
    public void confirmBusiness1(View view) {
        String number = numberEt.getText().toString();
        if (Double.valueOf(spotPrice)>0 && !"".equals(number) && Double.valueOf(number)>0){
            showDialog("1");
        }
    }

    /**
     * 弹出显示确认的dialog
     */
    private void showDialog(String str) {
        View viewDialog = LayoutInflater.from(this).inflate(R.layout.dialog_business, null);
        TextView dialogTitle = (TextView) viewDialog.findViewById(R.id.dialog_title);
        Button dialogConfirm = (Button) viewDialog.findViewById(R.id.dialog_confirm);
        TextView nameTv = (TextView) viewDialog.findViewById(R.id.dialog_name);
        TextView codeTv = (TextView) viewDialog.findViewById(R.id.dialog_code);
        TextView priceTv = (TextView) viewDialog.findViewById(R.id.dialog_price);
        TextView numberTv = (TextView) viewDialog.findViewById(R.id.dialog_number);
        nameTv.setText(stocksInfo.getName());
        codeTv.setText(stocksInfo.getCode());
        numberTv.setText(numberEt.getText().toString());
        if ("1".equals(str)){
            // 市场价格买入
            priceTv.setText(stocksInfo.getSpotPrice());
        }else{
            priceTv.setText(spotPrice);
        }
        if ("1".equals(type)) {
            dialogTitle.setText("委托卖出确认");
            dialogConfirm.setText("确认卖出");
        } else {
            dialogTitle.setText("委托买入确认");
            dialogConfirm.setText("确认买入");
        }

        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(viewDialog).show();
        alertDialog.setCancelable(false);
        // 确认买卖
        dialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    /**
     * 监听股票代码的输入状态
     */
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            code = s.toString();
            if (s.toString().length() == 6){
                code = Utils.judgeSharesCode(code);
                dialog.show();
                // 开线程获取股票数据信息
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 调用接口获取股票当前行情数据
                        stocksInfo = new sharesUtil().processOrderData(code);
                        handler.sendEmptyMessage(0);
                    }
                }).start();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

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
                        businessNumber = Constants.totalAmount/Double.valueOf(spotPrice)+"";
                        businessNumber = (int)(Double.valueOf(businessNumber)/100*100)+"";
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
                    }
                    dialog.dismiss();
                    break;
                case 1:
                    break;
            }
        }
    };
}
