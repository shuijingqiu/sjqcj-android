package com.example.sjqcjstock.Activity.stocks;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.stocks.CommissionAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.entity.stocks.StocksInfo;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import java.util.ArrayList;

/**
 * 当日委托信息查询页面
 * Created by Administrator on 2016/8/11.
 */
public class DayCommissionActivity extends Activity {

    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private ListView listView;
    // 显示加载的Adapter
    private CommissionAdapter commissionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_day_commission);
        ExitApplication.getInstance().addActivity(this);
        findView();
    }

    /**
     * 控件的绑定
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

        commissionAdapter = new CommissionAdapter(this);
        listView = (ListView) findViewById(
                R.id.list_view);
        listView.setAdapter(commissionAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

            }
        });

        ptrl = ((PullToRefreshLayout) findViewById(
                R.id.refresh_view));
        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下来刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                // 千万别忘了告诉控件刷新完毕了哦！
                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            // 下拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                // 千万别忘了告诉控件刷新完毕了哦！
                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
            }
        });

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
        stocks = new StocksInfo();
        stocks.setName("测试用的8");
        listStocks.add(stocks);
        stocks = new StocksInfo();
        stocks.setName("测试用的9");
        listStocks.add(stocks);
        stocks = new StocksInfo();
        stocks.setName("测试用的10");
        listStocks.add(stocks);
        stocks = new StocksInfo();
        stocks.setName("测试用的11");
        listStocks.add(stocks);
        commissionAdapter.setlistData(listStocks);
    }
}
