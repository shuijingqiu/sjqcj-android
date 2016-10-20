package com.example.sjqcjstock.Activity.stocks;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;

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
        } else {
            titleName.setText("模拟盘-买入");
            ((Button) findViewById(R.id.confirm_business)).setText("买入");
            ((Button) findViewById(R.id.confirm_business1)).setText("市场买入");
        }
    }

    /**
     * 增加价格的方法
     *
     * @param view
     */
    public void priceRaise(View view) {
    }

    /**
     * 减少价格的方法
     *
     * @param view
     */
    public void priceReduce(View view) {
    }

    /**
     * 四分之一的价格
     *
     * @param view
     */
    public void numberQuarter(View view) {
    }

    /**
     * 二分之一的价格
     *
     * @param view
     */
    public void numberAhalf(View view) {
    }

    /**
     * 全部的价格
     *
     * @param view
     */
    public void numberAll(View view) {
    }

    /**
     * 确认买卖的方法
     *
     * @param view
     */
    public void confirmBusiness(View view) {
        showDialog("0");
    }

    /**
     * 市场价的买卖方法
     *
     * @param view
     */
    public void confirmBusiness1(View view) {
        showDialog("1");
    }

    /**
     * 弹出显示确认的dialog
     */
    private void showDialog(String str) {

        View viewDialog = LayoutInflater.from(this).inflate(R.layout.dialog_business, null);
        TextView dialogTitle = (TextView) viewDialog.findViewById(R.id.dialog_title);
        Button dialogConfirm = (Button) viewDialog.findViewById(R.id.dialog_confirm);

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
}
