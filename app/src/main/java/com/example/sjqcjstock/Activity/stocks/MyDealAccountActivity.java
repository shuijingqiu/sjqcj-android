package com.example.sjqcjstock.Activity.stocks;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.stocks.EntrustAdapter;
import com.example.sjqcjstock.adapter.stocks.MyDealAccountAdapter;
import com.example.sjqcjstock.adapter.stocks.StockAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.stocks.OptionalEntity;
import com.example.sjqcjstock.entity.stocks.PositionEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.netutil.sharesUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.SoListView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 我的交易账户
 * Created by Administrator on 2016/8/15.
 */
public class MyDealAccountActivity extends Activity {

//    // 上下拉刷新控件
//    private PullToRefreshLayout ptrl;
    // ScrollView
    private ScrollView myScrollView;
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
    private CustomProgress dialog;
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
    private TextView positionsTv;
    // 仓位
    private TextView positionTv;
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
    // 最新市值
    private TextView marketTv;

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
    // 调用自选股接口返回的数据
    private String zxgstr = "";
    // 持仓的数据
    private ArrayList<PositionEntity> positionArrayList = null;
    // 委托的数据
    private ArrayList<PositionEntity> entrustArrayList = null;
    // 自选股的的数据
    private ArrayList<OptionalEntity> optionalArrayList = null;
    // 股票号码集合
    private String codeStr = "";
    // 持仓股票最新信息的Map
    private Map<String,Map> mapStr;
    // 自选股股票最新信息的Map
    private Map<String,Map> mapZxgStr;
    // 最新市值
    private double market = 0;
//    // 计算的总资产
//    private double totalAssets = 0;
//    // 可用资金
//    private double totalAmount = 0;
    // 定时器
    private Timer timer;
    // 是否是第一次加载
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_deal_account);
        ExitApplication.getInstance().addActivity(this);
        findView();
        initLine();
        Constants.isBuy = true;
        timer = new Timer(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        market = 0;
        if (Constants.isBuy){
            getData();
            Constants.isBuy = false;
        }
        // 回到页面看刷新不
        if (Utils.isTransactionDate()){
            timer = new Timer(true);
            // 开定时器获取数据
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    market = 0;
                    getData();
                }
            };
            timer.schedule(task, 30000, 30000); // 30000s后执行task,经过30s再次执行
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer!=null) {
            // 页面关闭时
            // 关闭掉定时器
            timer.cancel();
        }
    }

    /**
     * 控件的绑定
     */
    private void findView() {
        dialog = new CustomProgress(this);
        dialog.showDialog();
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
        listAdapter = new MyDealAccountAdapter(this,Constants.staticmyuidstr);
        entrustAdapter = new EntrustAdapter(this,this);
        listView = (SoListView) findViewById(
                R.id.position_list);
        listView.setAdapter(listAdapter);
        // 仓位
        positionTv = (TextView) findViewById(R.id.position_tv);
        // 总收益
        totalRevenueTv = (TextView) findViewById(R.id.total_revenue_value_tv);
        // 排名
        rankingTv = (TextView) findViewById(R.id.ranking_value_tv);
        // 总资产
        totalAssetsTv = (TextView) findViewById(R.id.total_assets_value_tv);
        // 可用资金
        availableFundsTv = (TextView) findViewById(R.id.available_funds_tv);
        // 当日盈亏
        dayBreakTv = (TextView) findViewById(R.id.day_break_tv);
        // 最新市值
        marketTv = (TextView) findViewById(R.id.market_value_tv);

        entrustLl = (LinearLayout) findViewById(R.id.entrust_ll);
        entrustTv = (TextView) findViewById(R.id.entrust_tv);
        positionsTv = (TextView) findViewById(R.id.positions_tv);
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

        myScrollView = (ScrollView) findViewById(R.id.myScrollView);
//        ptrl = ((PullToRefreshLayout) findViewById(
//                R.id.refresh_view));
//        // 添加上下拉刷新事件
//        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
//            // 下来刷新
//            @Override
//            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
//                if (transactionLl.getVisibility()==View.VISIBLE){
//                    market = 0;
//                    getData();
//                }
//                else{
//                    getData1();
//                }
//            }
//            // 下拉加载
//            @Override
//            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
//                // 千万别忘了告诉控件刷新完毕了哦！
//                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//            }
//        });

        textTransaction = (TextView) findViewById(R.id.text_transaction);
        textStock = (TextView) findViewById(R.id.text_my_stock);
        // 获取颜色
        select_color = getResources().getColor(R.color.color_toptitle);
        unselect_color = getResources().getColor(R.color.color_000000);

        // 获取屏幕宽度
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        viewPagerW = size.x;

        findViewById(R.id.iv_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MyDealAccountActivity.this, SearchActivity.class);
//                startActivity(intent);
                Intent intent = new Intent(MyDealAccountActivity.this, SearchSharesActivity.class);
                intent.putExtra("jumpType","1");
                startActivity(intent);
            }
        });
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
                resstr = HttpUtil.restHttpGet(Constants.moUrl+"/users&uid="+Constants.staticmyuidstr+"&token="+Constants.apptoken);
                handler.sendEmptyMessage(0);
            }
        }).start();
        // 开线程获取用户账户信息和总盈利排名
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用接口获取用户账户信息和总盈利排名
                xxstr = HttpUtil.restHttpGet(Constants.moUrl+"/users/"+Constants.staticmyuidstr+"&token="+Constants.apptoken+"&uid="+Constants.staticmyuidstr);
                handler.sendEmptyMessage(3);
            }
        }).start();
        // 开线程获取用户获取自选股
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用接口获取用户获取自选股
                zxgstr = HttpUtil.restHttpGet(Constants.moUrl+"/user/getUserOptional&uid="+Constants.staticmyuidstr+"&token="+Constants.apptoken);
                handler.sendEmptyMessage(4);
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
                        dialog.dismissDlog();
                        JSONObject jsonObject = new JSONObject(resstr);
                        if ("failed".equals(jsonObject.getString("status"))){
                            Toast.makeText(getApplicationContext(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // 持仓的数据
                        ArrayList<PositionEntity> positionList = (ArrayList<PositionEntity>) JSON.parseArray(jsonObject.getString("data"),PositionEntity.class);
                        // 委托的数据
                        ArrayList<PositionEntity> entrustList = (ArrayList<PositionEntity>) JSON.parseArray(jsonObject.getString("nData"),PositionEntity.class);
                            positionArrayList = positionList;
                            entrustArrayList = entrustList;
//                        String number = "";
//                        String price = "";
//                        // 计算总资金
//                        for(PositionEntity entrust:entrustList){
//                            number = entrust.getNumber();
//                            price = entrust.getPrice();
//                            totalAssets += Double.valueOf(price) * Double.valueOf(number)+Double.valueOf(entrust.getFee());
//                        }
                        // 有数据就展示
                        if (1 > entrustArrayList.size()){
                            entrustLl.setVisibility(View.GONE);
                        }else{
                            entrustTv.setText("当前委托("+entrustArrayList.size()+")");
                            entrustLl.setVisibility(View.VISIBLE);
                        }
                        if (1 > positionArrayList.size()){
                            positionsTv.setText("当前持仓(0)");
                        }
                        else{
                            positionsTv.setText("当前持仓("+positionArrayList.size()+")");
                        }
                            entrustAdapter.setlistData(entrustArrayList);
                            // 获取最新的股票信息
                            getRealTimeData(positionList);
                            listAdapter.setlistData(positionArrayList);

//                        if (transactionLl.getVisibility() == View.VISIBLE) {
//                            // 千万别忘了告诉控件刷新完毕了哦！
//                            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (isFirst){
                        // 滚动到顶部
                        myScrollView.smoothScrollTo(0, 0);
                        isFirst = false;
                    }
                    break;
                case 1:
                    if (mapStr==null || mapStr.size()<1){
                        break;
                    }
                    Double price = 0.0;
                    // 收益率
                    String profitStr = "0";
                    // 成本价
                    String costPrice = "0";
                    for(int i = 0;i < positionArrayList.size();i++){
                        Map<String,String> mStr = mapStr.get(positionArrayList.get(i).getStock());
                        String prices = mStr.get("price");
                        if (prices != null && !"".equals(prices) && Double.valueOf(prices) != 0) {
                            price = Double.valueOf(prices);
                            positionArrayList.get(i).setLatest_price(price+"");
                        }else{
                            price = Double.valueOf(positionArrayList.get(i).getLatest_price());
                        }
                        positionArrayList.get(i).setIsType(mStr.get("type"));
                        // 成本价
                        costPrice = positionArrayList.get(i).getCost_price();
                        // 收益率
                        profitStr = Utils.getNumberFormat2((Double.valueOf(price) -  Double.valueOf(costPrice))/Math.abs(Double.valueOf(costPrice))*100 + "");
                        positionArrayList.get(i).setRatio(profitStr);
                        // 持仓数量
                        int positionValue = Integer.valueOf(positionArrayList.get(i).getPosition_number());
                        market += price * positionValue;
                    }
//                    totalAssets = market + totalAmount;
//                    // 重新更新总资金
//                    totalAssetsTv.setText(Utils.getNumberFormat2(totalAssets+""));
//                    // 重新更新仓位
//                    positionTv.setText(Utils.getNumberFormat2(market/totalAssets*100+"")+"%");
                    // 设置最新市值
                    marketTv.setText(Utils.getNumberFormat2(market+""));
                    // 刷新持仓列表
                    listAdapter.setlistData(positionArrayList);
                    break;
                case 2:
                    try {
                        JSONObject jsonObject = new JSONObject(cdstr);
                        Toast.makeText(getApplicationContext(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                        if ("success".equals(jsonObject.getString("status"))){
                            // 撤单成功后刷新数据
                            getData();
                        }else{
                            dialog.dismissDlog();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        JSONObject jsonObject = new JSONObject(xxstr);
                        if ("failed".equals(jsonObject.getString("status"))){
                            Toast.makeText(getApplicationContext(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        JSONObject jsonStr = new JSONObject(jsonObject.getString("data"));
                        // 仓位
                        positionTv.setText(Utils.getNumberFormat2(jsonStr.getString("position"))+"%");
                        // 总收益
                        totalRevenueTv.setText(Utils.getNumberFormat2(jsonStr.getString("total_rate")+"")+"%");
                        // 排名
                        rankingTv.setText(jsonStr.getString("total_profit_rank"));
                        // 总资产
                        totalAssetsTv.setText(Utils.getNumberFormat2(jsonStr.getString("funds")));
                        // 可用资金
                        double totalAmount = jsonStr.getDouble("available_funds");
                        availableFundsTv.setText(Utils.getNumberFormat2(totalAmount+""));
                        // 当日盈亏
                        dayBreakTv.setText(Utils.getNumberFormat2(jsonStr.getString("shares")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        JSONObject jsonObject = new JSONObject(zxgstr);
                        stockAdapter.setlistData(new  ArrayList<OptionalEntity>());
                        if ("failed".equals(jsonObject.getString("status"))){
                            if (stockLl.getVisibility() == View.VISIBLE){
//                                Toast.makeText(getApplicationContext(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }
                        // 自选股的数据
                        ArrayList<OptionalEntity> optionalList = (ArrayList<OptionalEntity>) JSON.parseArray(jsonObject.getString("data"),OptionalEntity.class);
                        // 加载自选股股票信息
                        stockAdapter.setlistData(optionalList);
                        optionalArrayList = optionalList;
                        // 获取股票实时数据
                        getoptionalData(optionalList);
//                        if (stockLl.getVisibility() == View.VISIBLE){
//                            // 千万别忘了告诉控件刷新完毕了哦！
//                            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    // 重新加载自选股列表
                    for(int i = 0;i < optionalArrayList.size();i++){
                        Map<String,String> mStr = mapZxgStr.get(optionalArrayList.get(i).getStock());
                        if (mStr!=null && mStr.size()>0) {
                            optionalArrayList.get(i).setPrice(mStr.get("price"));
                            optionalArrayList.get(i).setRising(mStr.get("rising"));
                            optionalArrayList.get(i).setIstype(mStr.get("type"));
                        }
                    }
                    // 刷新自选股列表
                    stockAdapter.setlistData(optionalArrayList);
                    break;
            }
        }
    };

    /**
     * 获取自选股股票实时数据进行处理
     * @param listOptional
     */
    private void getoptionalData(ArrayList<OptionalEntity>  listOptional){
        String str = "";
        for (OptionalEntity optionalEntity:listOptional){
            if (!"".equals(str)){
                str += ",";
            }
            str += Utils.judgeSharesCode(optionalEntity.getStock());
        }
        if ("".equals(str)){
            return;
        }
        // 开线程获股票当前信息
        final String finalStr = str;
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用接口获取股票当前行情数据（返回股票代码和最新价格的map）
                mapZxgStr = sharesUtil.getsharess(finalStr);
                // 重新加载自选股数据
                handler.sendEmptyMessage(5);
            }
        }).start();
    }

    /**
     * 获取持仓股票实时数据进行处理
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
        if ("".equals(codeStr)){
            return;
        }
        // 开线程获股票当前信息
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
        intent.putExtra("type","1");
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
        // 设置下横条的位置
        LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) img_line.getLayoutParams();
        // 关键算法
        lp.leftMargin = (int) ((int) (mScreen1_4 * 0) + (((double) 1 / viewPagerW) * mScreen1_4));
        img_line.setLayoutParams(lp);
        // 滚动到顶部
        myScrollView.smoothScrollTo(0, 0);
    }

    /**
     * 自选股页面
     */
    public void stockClick(View view) {
        textStock.setTextColor(select_color);
        textTransaction.setTextColor(unselect_color);
        transactionLl.setVisibility(View.GONE);
        stockLl.setVisibility(View.VISIBLE);
        // 设置下横条的位置
        LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) img_line.getLayoutParams();
        // 关键算法
        lp.leftMargin = (int) ((int) (mScreen1_4 * 1) + (((double) 2 / viewPagerW) * mScreen1_4));
        img_line.setLayoutParams(lp);
        // 滚动到顶部
        myScrollView.smoothScrollTo(0, 0);
    }

    /**
     *
     * @param id 撤单ID
     */
    public void killAnOrderClick(final String id){
        dialog.showDialog();
        // 开线程撤单
        new Thread(new Runnable() {
            @Override
            public void run() {
                List dataList = new ArrayList();
                //组装数据放到HttpEntity中发送到服务器
//                dataList.add(new BasicNameValuePair("id", id));
                dataList.add(new BasicNameValuePair("status", "2"));
                dataList.add(new BasicNameValuePair("uid", Constants.staticmyuidstr));
                dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                // 调用接口撤单
                cdstr = HttpUtil.restHttpPut(Constants.moUrl+"/orders/"+id,dataList);
                handler.sendEmptyMessage(2);
            }
        }).start();
    }

}
