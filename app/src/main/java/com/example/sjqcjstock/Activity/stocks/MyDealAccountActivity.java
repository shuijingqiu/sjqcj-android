package com.example.sjqcjstock.Activity.stocks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.stocks.MyDealAccountAdapter;
import com.example.sjqcjstock.adapter.stocks.StockAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.stocks.StocksInfo;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.example.sjqcjstock.view.PullToRefreshLayout;
import com.example.sjqcjstock.view.PullableScrollView;
import com.example.sjqcjstock.view.SoListView;

import java.util.ArrayList;

/**
 * 我的交易账户
 * Created by Administrator on 2016/8/15.
 */
public class MyDealAccountActivity extends Activity {

    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // ScrollView
    private PullableScrollView myScrollView;
    // 股票交易的List
    private SoListView listView;
    // 自选股的List
    private SoListView stockListView;
    // 交易的行情List的Adapter
    private MyDealAccountAdapter listAdapter;
    // 自选股的List Adapter
    private StockAdapter stockAdapter;
    // 需要加载的行情数据
    private ArrayList<StocksInfo> listInfo;
    // 网络请求提示
    private ProgressDialog dialog;
    // 交易列表
    private LinearLayout transactionLl;
    // 自选股列表
    private LinearLayout stockLl;

    // 控件
    private TextView textTransaction = null;
    private TextView textStock = null;
    private ImageView img_line;
    // 滑动条颜色
    private int select_color;
    private int unselect_color;
    private int mScreen1_4;
    /**
     * 当前视图宽度
     **/
    private Integer viewPagerW = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_deal_account);
        ExitApplication.getInstance().addActivity(this);
        findView();
        initData();
        initLine();
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
        transactionLl = (LinearLayout) findViewById(R.id.transaction_ll);
        stockLl = (LinearLayout) findViewById(R.id.stock_ll);
        listAdapter = new MyDealAccountAdapter(this);
        listView = (SoListView) findViewById(
                R.id.position_list);
        listView.setAdapter(listAdapter);

        stockAdapter = new StockAdapter(this);
        stockListView = (SoListView) findViewById(
                R.id.stock_list);
        stockListView.setAdapter(stockAdapter);
        // 自选股的Item单机事件
        stockListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent inten = new Intent();
                inten.putExtra("name", ((TextView) view.findViewById(R.id.name_tv)).getText() + "");
                inten.putExtra("code", ((TextView) view.findViewById(R.id.code_tv)).getText() + "");
                inten.setClass(MyDealAccountActivity.this, SharesDetailedActivity.class);
                startActivity(inten);
            }
        });

        myScrollView = (PullableScrollView) findViewById(R.id.myScrollView);
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

        textTransaction = (TextView) findViewById(R.id.text_transaction);
        textStock = (TextView) findViewById(R.id.text_my_stock);
        // 获取颜色
        select_color = getResources().getColor(R.color.color_toptitle);
        unselect_color = getResources().getColor(R.color.color_000000);

        // 获取屏幕宽度
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        viewPagerW = size.x;
    }

    /**
     * 数据的获取
     */
    private void initData() {

        ArrayList<StocksInfo> listStocks = new ArrayList<StocksInfo>();
        StocksInfo stocks = new StocksInfo();
        stocks.setName("五粮液");
        stocks.setCode("000858");
        listStocks.add(stocks);
        stocks = new StocksInfo();
        stocks.setName("中潜股份");
        stocks.setCode("300526");
        listStocks.add(stocks);
        stocks = new StocksInfo();
        stocks.setName("深物业A");
        stocks.setCode("000011");
        listStocks.add(stocks);
        stocks = new StocksInfo();
        stocks.setName("富祥股份");
        stocks.setCode("300497");
        listStocks.add(stocks);
        stocks = new StocksInfo();
        stocks.setName("*ST钒钛");
        stocks.setCode("000629");
        listStocks.add(stocks);
        stocks = new StocksInfo();
        stocks.setName("京东方Ａ");
        stocks.setCode("000725");
        listStocks.add(stocks);
        stocks = new StocksInfo();
        stocks.setName("巨星科技");
        stocks.setCode("002444");
        listStocks.add(stocks);
        listAdapter.setlistData(listStocks);
        ViewUtil.setListViewHeightBasedOnChildren(listView);

        stocks = new StocksInfo();
        stocks.setName("辰安科技");
        stocks.setCode("300523");
        listStocks.add(stocks);
        stocks = new StocksInfo();
        stocks.setName("深圳机场");
        stocks.setCode("000089");
        listStocks.add(stocks);
        stocks = new StocksInfo();
        stocks.setName("春兴精工");
        stocks.setCode("002547");
        listStocks.add(stocks);
        stocks = new StocksInfo();
        stockAdapter.setlistData(listStocks);

        // 滚动到顶部
        myScrollView.smoothScrollTo(0, 0);
        dialog.dismiss();

    }

    /**
     * 初始化line
     */
    private void initLine() {
        img_line = (ImageView) findViewById(R.id.img_line);
        mScreen1_4 = ImageUtil.getScreenWidth(this) / 2;
        ViewGroup.LayoutParams lp = img_line.getLayoutParams();
        lp.width = mScreen1_4;
        img_line.setLayoutParams(lp);
    }


    /**
     * 查询的单机事件
     */
    public void qyeryClick(View view) {
        Intent intent = new Intent(this, QueryActivity.class);
        startActivity(intent);
    }

    /**
     * 买入的单机事件
     */
    public void buyClick(View view) {
        Intent intent = new Intent(this, BusinessActivity.class);
        intent.putExtra("type", "0");
        startActivity(intent);
    }

    /**
     * 交易切换页面
     */
    public void transactionClick(View view) {
        textStock.setTextColor(unselect_color);
        textTransaction.setTextColor(select_color);
        transactionLl.setVisibility(View.VISIBLE);
        stockLl.setVisibility(View.GONE);
        // 滚动到顶部
        myScrollView.smoothScrollTo(0, 0);

        // 设置下横条的位置
        LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) img_line.getLayoutParams();
        // 关键算法
        lp.leftMargin = (int) ((int) (mScreen1_4 * 0) + (((double) 1 / viewPagerW) * mScreen1_4));
        img_line.setLayoutParams(lp);
    }

    /**
     * 自选股页面
     */
    public void stockClick(View view) {
        textStock.setTextColor(select_color);
        textTransaction.setTextColor(unselect_color);
        transactionLl.setVisibility(View.GONE);
        stockLl.setVisibility(View.VISIBLE);
        // 滚动到顶部
        myScrollView.smoothScrollTo(0, 0);

        // 设置下横条的位置
        LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) img_line.getLayoutParams();
        // 关键算法
        lp.leftMargin = (int) ((int) (mScreen1_4 * 1) + (((double) 2 / viewPagerW) * mScreen1_4));
        img_line.setLayoutParams(lp);
    }

}
