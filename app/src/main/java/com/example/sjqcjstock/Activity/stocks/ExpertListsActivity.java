package com.example.sjqcjstock.Activity.stocks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.stocks.ExpertListsAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.entity.stocks.StocksInfo;

import java.util.ArrayList;

/**
 * 各种牛人榜单的列表
 * Created by Administrator on 2016/8/16.
 */
public class ExpertListsActivity extends Activity {

    // 标题
    private TextView topTitle;
    // 加载的ListView
    private ListView listView;
    // 显示加载的Adapter
    private ExpertListsAdapter expertListsAdapter;
    // 页面显示类型(0:常胜牛人 1:人气牛人 2:总收益榜 3:选股牛人)
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_expert_lists);
        ExitApplication.getInstance().addActivity(this);
        type = getIntent().getIntExtra("type", 0);
        findView();
        setTitle();
        initData();
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
        topTitle = (TextView) findViewById(R.id.top_title_tv);
        expertListsAdapter = new ExpertListsAdapter(this);
        listView = (ListView) findViewById(
                R.id.list_view);
        listView.setAdapter(expertListsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // 跳转到个人中心页面
                Intent intent = new Intent(ExpertListsActivity.this,
                        UserDetailNewActivity.class);
                // 要修改的
                intent.putExtra("uid", "26364");
                intent.putExtra("type", "1");
                startActivity(intent);
            }
        });
    }

    /**
     * 标题的设置
     */
    private void setTitle() {
        switch (type) {
            case 0:
                topTitle.setText("常胜牛人");
                break;
            case 1:
                topTitle.setText("人气牛人");
                break;
            case 2:
                topTitle.setText("总收益榜");
                break;
            case 3:
                topTitle.setText("选股牛人");
                break;
        }
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
        expertListsAdapter.setlistData(listStocks);

    }
}
