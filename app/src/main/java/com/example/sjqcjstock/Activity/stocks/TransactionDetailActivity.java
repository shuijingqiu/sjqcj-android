package com.example.sjqcjstock.Activity.stocks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ScrollView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.stocks.TransactionDetailAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.stocks.StocksInfo;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.example.sjqcjstock.view.SoListView;

import java.util.ArrayList;

/**
 * 当前持仓成交明细
 * Created by Administrator on 2016/8/15.
 */
public class TransactionDetailActivity extends Activity {

    // 股票的List
    private SoListView listView;
    // 行情List的Adapter
    private TransactionDetailAdapter listAdapter;
    // 滚动控件
    private ScrollView myScrollView;
    // 需要加载的行情数据
    private ArrayList<StocksInfo> listInfo;
    // 网络请求提示
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_transaction_detail);
        ExitApplication.getInstance().addActivity(this);
        findView();
        initData();
    }

    /**
     * 控件的绑定
     */
    private void findView() {
        dialog = new ProgressDialog(this);
        dialog.setMessage(Constants.loadMessage);
        dialog.setCancelable(true);
        dialog.show();
        /**
         * 返回按钮的事件绑定
         */
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        myScrollView = (ScrollView) findViewById(R.id.myScrollView);
        listAdapter = new TransactionDetailAdapter(this);
        listView = (SoListView) findViewById(
                R.id.list_view);
        listView.setAdapter(listAdapter);

    }

    /**
     * 数据的加载
     */
    private void initData() {

        ArrayList<StocksInfo> listStocks = new ArrayList<StocksInfo>();
        StocksInfo stocks = new StocksInfo();
        stocks.setName("测试用的1");
        listStocks.add(stocks);
        stocks = new StocksInfo();
        stocks.setName("测试用的2");
        listStocks.add(stocks);
        stocks = new StocksInfo();
        stocks.setName("测试用的3");
        listStocks.add(stocks);
        stocks = new StocksInfo();
        stocks.setName("测试用的4");
        listStocks.add(stocks);
        stocks = new StocksInfo();
        stocks.setName("测试用的5");
        listStocks.add(stocks);
        stocks = new StocksInfo();
        stocks.setName("测试用的6");
        listStocks.add(stocks);
        stocks = new StocksInfo();
        stocks.setName("测试用的7");
        listStocks.add(stocks);
        listAdapter.setlistData(listStocks);
        ViewUtil.setListViewHeightBasedOnChildren(listView);
        // 滚动到顶部
        myScrollView.smoothScrollTo(0, 0);
        dialog.dismiss();

    }

    /**
     * 卖出按钮事件
     *
     * @param view
     */
    public void sellClick(View view) {
        Intent intent = new Intent(this, BusinessActivity.class);
        intent.putExtra("type", "1");
        intent.putExtra("code", "0000001");
        startActivity(intent);
    }

    /**
     * 买入按钮事件
     *
     * @param view
     */
    public void buyClick(View view) {
        Intent intent = new Intent(this, BusinessActivity.class);
        intent.putExtra("type", "0");
        intent.putExtra("code", "0000001");
        startActivity(intent);
    }
}
