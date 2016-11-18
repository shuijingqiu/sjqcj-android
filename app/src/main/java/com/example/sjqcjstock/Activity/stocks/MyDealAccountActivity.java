package com.example.sjqcjstock.Activity.stocks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.stocks.EntrustAdapter;
import com.example.sjqcjstock.adapter.stocks.MyDealAccountAdapter;
import com.example.sjqcjstock.adapter.stocks.StockAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.stocks.PositionEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.netutil.sharesUtil;
import com.example.sjqcjstock.view.PullToRefreshLayout;
import com.example.sjqcjstock.view.PullableScrollView;
import com.example.sjqcjstock.view.SoListView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    //当前委托的Adapter
    private EntrustAdapter entrustAdapter;
    // 交易的行情List的Adapter
    private MyDealAccountAdapter listAdapter;
    // 自选股的List Adapter
    private StockAdapter stockAdapter;
    // 网络请求提示
    private ProgressDialog dialog;
    // 交易列表
    private LinearLayout transactionLl;
    // 自选股列表
    private LinearLayout stockLl;
    // 当前委托的Layout
    private LinearLayout entrustLl;
    // 当前委托的条数
    private TextView entrustTv;
    // 当前委托的List
    private SoListView entrustList;
    // 当前持仓的条数
    private TextView positionTv;
    // 净值
    private TextView netWorthTv;
    // 总收益
    private TextView totalRevenueTv;
    // 排名
    private TextView rankingTv;
    // 总资产
    private TextView totalAssetsTv;
    // 可用资金
    private TextView availableFundsTv;
    // 当日盈亏
    private TextView dayBreakTv;

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
    // 调用个人资产信息的接口
    private String xxstr = "";
    // 调用买卖接口返回的数据
    private String resstr = "";
    // 调用撤单接口返回的数据
    private String cdstr = "";
    // 分页
    private int page = 1;
    // 委托分页
    private int wpage = 0;
    // 持仓分页
    private int cpage = 0;
    // 持仓的数据
    private ArrayList<PositionEntity> positionArrayList = null;
    // 委托的数据
    private ArrayList<PositionEntity> entrustArrayList = null;
    // 股票号码集合
    private String codeStr = "";
    // 股票最新信息的Map
    private Map<String,Map> mapStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_deal_account);
        ExitApplication.getInstance().addActivity(this);
        findView();
        getData();
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
        entrustAdapter = new EntrustAdapter(this,this);
        listView = (SoListView) findViewById(
                R.id.position_list);
        listView.setAdapter(listAdapter);
        // 净值
        netWorthTv = (TextView) findViewById(R.id.net_worth_value_tv);
        // 总收益
        totalRevenueTv = (TextView) findViewById(R.id.total_revenue_value_tv);
        // 排名
        rankingTv = (TextView) findViewById(R.id.ranking_value_tv);
        // 总资产
        totalAssetsTv = (TextView) findViewById(R.id.total_assets_tv);
        // 可用资金
        availableFundsTv = (TextView) findViewById(R.id.available_funds_tv);
        // 当日盈亏
        dayBreakTv = (TextView) findViewById(R.id.day_break_tv);

        entrustLl = (LinearLayout) findViewById(R.id.entrust_ll);
        entrustTv = (TextView) findViewById(R.id.entrust_tv);
        positionTv = (TextView) findViewById(R.id.position_tv);
        entrustList = (SoListView) findViewById(R.id.entrust_list);
        entrustList.setAdapter(entrustAdapter);

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
                page = 1;
                getData();
                // 千万别忘了告诉控件刷新完毕了哦！
                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            // 下拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                page += 1;
                getData();
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
     * 获取历史交易信息
     */
    private void getData(){
        // 开线程获历史交易信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用接口获取股票当前行情数据
                resstr = HttpUtil.restHttpGet(Constants.moUrl+"/users&uid=200&p="+page+"&np="+page);
                handler.sendEmptyMessage(0);
            }
        }).start();
        // 开线程获历史交易信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用接口获取股票当前行情数据
                xxstr = HttpUtil.restHttpGet(Constants.moUrl+"/users/200");
                handler.sendEmptyMessage(3);
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
                        if ("failed".equals(jsonObject.getString("status"))){
                            Toast.makeText(getApplicationContext(), "暂无数据", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        wpage = jsonObject.getInt("totalPage");
                        cpage = jsonObject.getInt("nTotalPage");
                        // 持仓的数据
                        ArrayList<PositionEntity> positionList = (ArrayList<PositionEntity>) JSON.parseArray(jsonObject.getString("data"),PositionEntity.class);
                        // 委托的数据
                        ArrayList<PositionEntity> entrustList = (ArrayList<PositionEntity>) JSON.parseArray(jsonObject.getString("nData"),PositionEntity.class);
                        if (positionList == null || 1 > positionList.size()){
                            positionTv.setText("持仓(0)");
                        }
                        else{
                            positionTv.setText("持仓("+positionList.size()+")");
                        }
                        // 有数据就展示
                        if (entrustList == null || 1 > entrustList.size()){
                            entrustLl.setVisibility(View.GONE);
                        }else{
                            entrustTv.setText("当前委托("+entrustList.size()+")");
                            entrustLl.setVisibility(View.VISIBLE);
                        }
                        if(page == 1){
                            positionArrayList = positionList;
                            entrustArrayList = entrustList;
                        }else{
                            positionArrayList.addAll(positionList);
                            entrustArrayList.addAll(entrustList);
                        }
                        if (page >= wpage){
                            entrustAdapter.setlistData(entrustArrayList);
                        }
                        if (page >= cpage){
                            // 获取最新的股票信息
                            getRealTimeData(positionList);
                            listAdapter.setlistData(positionArrayList);
                        }
//                        ViewUtil.setListViewHeightBasedOnChildren(listView);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // 滚动到顶部
                    myScrollView.smoothScrollTo(0, 0);
                    dialog.dismiss();
                    break;
                case 1:
                    for(int i = 0;i < positionArrayList.size();i++){
                        Map<String,String> mStr = mapStr.get(positionArrayList.get(i).getStock());
                        positionArrayList.get(i).setLatest_price(mStr.get("price"));
                        positionArrayList.get(i).setIsType(mStr.get("type"));
                    }
                    // 刷新持仓列表
                    listAdapter.setlistData(positionArrayList);
                    break;
                case 2:
                    try {
                        JSONObject jsonObject = new JSONObject(cdstr);
                        Toast.makeText(getApplicationContext(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                        if ("success".equals(jsonObject.getString("status"))){
                            // 撤单成功后刷新数据
                            dialog.show();
                            getData();
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        Log.e("mh12xxstr3:",xxstr);
                        JSONObject jsonObject = new JSONObject(xxstr);
                        if ("failed".equals(jsonObject.getString("status"))){
                            Toast.makeText(getApplicationContext(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        JSONObject jsonStr = new JSONObject(jsonObject.getString("data"));
                        // 净值
                        netWorthTv.setText("");
                        // 总收益
                        totalRevenueTv.setText(jsonStr.getString("totalProfitRatio"));
                        // 排名
                        rankingTv.setText(jsonStr.getString("rank"));
                        // 总资产
                        totalAssetsTv.setText(jsonStr.getString("funds"));
                        // 可用资金
                        availableFundsTv.setText(jsonStr.getString("available_funds"));
                        // 当日盈亏
                        dayBreakTv.setText(jsonStr.getString("shares"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

//    /**
//     * 数据的获取
//     */
//    private void initData() {
//
//        ArrayList<StocksInfo> listStocks = new ArrayList<StocksInfo>();
//        StocksInfo stocks = new StocksInfo();
//        stocks.setName("五粮液");
//        stocks.setCode("000858");
//        listStocks.add(stocks);
//        stocks = new StocksInfo();
//        stocks.setName("中潜股份");
//        stocks.setCode("300526");
//        listStocks.add(stocks);
//        stocks = new StocksInfo();
//        stocks.setName("深物业A");
//        stocks.setCode("000011");
//        listStocks.add(stocks);
//        stocks = new StocksInfo();
//        stocks.setName("富祥股份");
//        stocks.setCode("300497");
//        listStocks.add(stocks);
//        stocks = new StocksInfo();
//        stocks.setName("*ST钒钛");
//        stocks.setCode("000629");
//        listStocks.add(stocks);
//        stocks = new StocksInfo();
//        stocks.setName("京东方Ａ");
//        stocks.setCode("000725");
//        listStocks.add(stocks);
//        stocks = new StocksInfo();
//        stocks.setName("巨星科技");
//        stocks.setCode("002444");
//        listStocks.add(stocks);
//        listAdapter.setlistData(listStocks);
//        entrustAdapter.setlistData(listStocks);
//        ViewUtil.setListViewHeightBasedOnChildren(listView);
//
//        stocks = new StocksInfo();
//        stocks.setName("辰安科技");
//        stocks.setCode("300523");
//        listStocks.add(stocks);
//        stocks = new StocksInfo();
//        stocks.setName("深圳机场");
//        stocks.setCode("000089");
//        listStocks.add(stocks);
//        stocks = new StocksInfo();
//        stocks.setName("春兴精工");
//        stocks.setCode("002547");
//        listStocks.add(stocks);
//        stocks = new StocksInfo();
//        stockAdapter.setlistData(listStocks);
//
//        // 滚动到顶部
//        myScrollView.smoothScrollTo(0, 0);
//        dialog.dismiss();
//
//    }

    /**
     * 获取股票实时数据进行处理
     * @param listPosition
     */
    private void getRealTimeData(ArrayList<PositionEntity>  listPosition){
        codeStr = "";
        for (PositionEntity positionEntity:listPosition){
            if (!"".equals(codeStr)){
                codeStr += ",";
            }
            codeStr += Utils.judgeSharesCode(positionEntity.getStock());
        }
        Log.e("mh123c",codeStr);
        // 开线程获历史交易信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用接口获取股票当前行情数据（返回股票代码和最新价格的map）
                mapStr = sharesUtil.getsharess(codeStr);
                handler.sendEmptyMessage(1);
            }
        }).start();
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
        intent.putExtra("type", "1");
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

    /**
     *
     * @param id 撤单ID
     */
    public void killAnOrderClick(final String id){
        dialog.show();
        // 开线程获历史交易信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                List dataList = new ArrayList();
                //组装数据放到HttpEntity中发送到服务器
                dataList.add(new BasicNameValuePair("status", "2"));
                // 调用接口获取股票当前行情数据
                cdstr = HttpUtil.restHttpPut(Constants.moUrl+"/orders/"+id,dataList);
                handler.sendEmptyMessage(2);
            }
        }).start();
    }

}
