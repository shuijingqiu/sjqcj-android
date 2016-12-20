package com.example.sjqcjstock.fragment.stocks;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.stocks.MyDealAccountAdapter;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.stocks.LineEntity;
import com.example.sjqcjstock.entity.stocks.PositionEntity;
import com.example.sjqcjstock.entity.stocks.SpotEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.netutil.sharesUtil;
import com.example.sjqcjstock.view.PullToRefreshLayout;
import com.example.sjqcjstock.view.SoListView;
import com.example.sjqcjstock.view.stocks.LineChart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 股票交易列表 不要的
 * Created by Administrator on 2016/8/19.
 */
public class FragmentTransactionList extends Fragment {

    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 股票交易的List
    private SoListView listView;
    // 交易的行情List的Adapter
    private MyDealAccountAdapter listAdapter;
    // 调用买卖接口返回的数据
    private String mMresstr = "";
    // 调用接口获取用户的交易排名信息
    private String xxstr = "";
    // 分时接口返回的数据
    private String chartstr = "";
    // 分页
    private int page = 1;
    // 持仓的数据
    private ArrayList<PositionEntity> positionArrayList = null;
    // 持仓股票最新信息的Map
    private Map<String,Map> mapStr;
    // 总收益
    private TextView totalRate;
    private TextView totalRate1;
    // 排名
    private TextView totalProfitRank;
    // 胜率
    private TextView winRate;
    private TextView winRate1;
    // 周均收益率
    private TextView weekAvgProfitRate;
    // 可用资金
    private TextView availableFunds;
    // 总资产
    private TextView funds;
    // 股票市值
    private TextView marketValue;
    // 仓位
    private TextView position;
    // 建户时间
    private TextView time;
    // 最新市值
    private double market = 0;
    // K线图的
    private LineChart assetsChart;
    // 账户分时图的数据
    private List<SpotEntity> lineAveData;
    // Y轴最小值
    private float miniY;
    // Y轴最大值
    private float maxY;
    // Y轴标题
    private List<String> ytitle;
    // X轴标题
    private List<String> xtitle;
    // 查看的用户ID
    private String uidstr = "";

    public FragmentTransactionList(String uidstr){
        this.uidstr = uidstr;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        findView(view);
        initData();
        return view;
    }

    private void findView(View view) {
        assetsChart = (LineChart) view.findViewById(R.id.assets_chart);
        listAdapter = new MyDealAccountAdapter(getActivity(),true);
        listView = (SoListView) view.findViewById(
                R.id.list_view);
        listView.setAdapter(listAdapter);
        totalRate = (TextView) view.findViewById(R.id.total_rate);
        totalRate1 = (TextView) view.findViewById(R.id.total_rate1);
        totalProfitRank = (TextView) view.findViewById(R.id.total_profit_rank);
        winRate = (TextView) view.findViewById(R.id.win_rate);
        winRate1 = (TextView) view.findViewById(R.id.win_rate1);
        weekAvgProfitRate = (TextView) view.findViewById(R.id.week_avg_profit_rate);
        availableFunds = (TextView) view.findViewById(R.id.available_funds);
        funds = (TextView) view.findViewById(R.id.funds);
        marketValue = (TextView) view.findViewById(R.id.market_value);
        position = (TextView) view.findViewById(R.id.position);
        time = (TextView) view.findViewById(R.id.time);

//        ptrl = ((PullToRefreshLayout) view.findViewById(
//                R.id.refresh_view));
//        // 添加上下拉刷新事件
//        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
//            // 下来刷新
//            @Override
//            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
//                page = 1;
//                initData();
//                // 千万别忘了告诉控件刷新完毕了哦！
//                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//            }
//            // 下拉加载
//            @Override
//            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
//                page += 1;
//                initData();
//                // 千万别忘了告诉控件刷新完毕了哦！
//                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//            }
//        });
    }


    /**
     * 数据的获取
     */
    private void initData() {
        // 开线程获历史交易信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用接口获取股票当前行情数据
                mMresstr = HttpUtil.restHttpGet(Constants.moUrl+"/users&uid="+uidstr+"&p="+page+"&token="+Constants.apptoken);
                handler.sendEmptyMessage(0);
            }
        }).start();
        // 开线程获取用户账户信息和总盈利排名
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用接口获取用户账户信息和总盈利排名
                xxstr = HttpUtil.restHttpGet(Constants.moUrl+"/users/"+uidstr+"&token="+Constants.apptoken+"&uid="+Constants.staticmyuidstr);
                handler.sendEmptyMessage(2);
            }
        }).start();
        // 开线程获取用户分时图数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用接口获取用户账户信息和总盈利排名
                chartstr = HttpUtil.restHttpGet(Constants.moUrl+"/share/getTimeChart&uid="+uidstr+"&token="+Constants.apptoken);
                handler.sendEmptyMessage(3);
            }
        }).start();
//        ViewUtil.setListViewHeightBasedOnChildren(listView);
//        // 滚动到顶部
//        myScrollView.scrollTo(0, 0);
////        myScrollView.smoothScrollTo(0, 0);
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
                        int wpage = 0;
                        JSONObject jsonObject = new JSONObject(mMresstr);
                        if ("failed".equals(jsonObject.getString("status"))) {
//                            Toast.makeText(getActivity(),jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        wpage = jsonObject.getInt("totalPage");
                        // 持仓的数据
                        ArrayList<PositionEntity> positionList = (ArrayList<PositionEntity>) JSON.parseArray(jsonObject.getString("data"), PositionEntity.class);
                        if (page == 1) {
                            positionArrayList = positionList;
                        } else {
                            positionArrayList.addAll(positionList);
                        }
                        if (wpage >= page) {
                            // 获取最新的股票信息
                            getRealTimeData(positionList);
                            listAdapter.setlistData(positionArrayList);
                        }
//                        ViewUtil.setListViewHeightBasedOnChildren(listView);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    // 滚动到顶部
//                    myScrollView.smoothScrollTo(0, 0);
                    break;
                case 1:
                    Double price;
                    for(int i = 0;i < positionArrayList.size();i++){
                        Map<String,String> mStr = mapStr.get(positionArrayList.get(i).getStock());
                        price = Double.valueOf(mStr.get("price"));
                        positionArrayList.get(i).setLatest_price(price+"");
                        positionArrayList.get(i).setIsType(mStr.get("type"));
                        // 持仓数量
                        int positionValue = Integer.valueOf(positionArrayList.get(i).getAvailable_number())+Integer.valueOf(positionArrayList.get(i).getFreeze_number());
                        // 最新市值的累加
                        market += price * positionValue;
                    }
                    marketValue.setText(Utils.getNumberFormat2(market+""));
                    // 刷新持仓列表
                    listAdapter.setlistData(positionArrayList);
                    break;
                case 2:
                    try {
                        JSONObject jsonObject = new JSONObject(xxstr);
                        if ("failed".equals(jsonObject.getString("status"))){
                            Toast.makeText(getActivity(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        JSONObject jsonStr = new JSONObject(jsonObject.getString("data"));
                        // 仓位
                        position.setText(jsonStr.getString("position"));

                        // 总收益
                        String totalRateValue = jsonStr.getString("total_rate");
                        totalRate.setText(totalRateValue + "%");
                        totalRate1.setText(totalRateValue + "%");
                        if (Double.valueOf(totalRateValue)>=0){
                            totalRate1.setTextColor(totalRate1.getResources().getColor(R.color.color_ef3e3e));
                        }else{
                            totalRate1.setTextColor(totalRate1.getResources().getColor(R.color.color_1bc07d));
                        }
                        // 排名
                        totalProfitRank.setText(jsonStr.getString("total_profit_rank"));
                        // 总资产
                        funds.setText(jsonStr.getString("funds"));
                        // 可用资金
                        availableFunds.setText(jsonStr.getString("available_funds"));
                        // 周收益率
                        String weekRate = jsonStr.getString("week_avg_profit_rate");
                        weekAvgProfitRate.setText(weekRate+"%");
                        if (Double.valueOf(weekRate)>=0){
                            weekAvgProfitRate.setTextColor(weekAvgProfitRate.getResources().getColor(R.color.color_ef3e3e));
                        }else{
                            weekAvgProfitRate.setTextColor(weekAvgProfitRate.getResources().getColor(R.color.color_1bc07d));
                        }

                        // 比赛胜率
                        winRate.setText(jsonStr.getString("win_rate")+"%");
                        winRate1.setText(jsonStr.getString("win_rate")+"%");
                        // 建户时间
                        time.setText(jsonStr.getString("time").substring(0,10));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                break;
                case 3:
                    lineAveData = new ArrayList<SpotEntity>();
                    xtitle = new ArrayList<String>();
                    try {
                        JSONObject jsonObject = new JSONObject(chartstr);
                        if ("failed".equals(jsonObject.getString("status"))){
                            Toast.makeText(getActivity(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                        float endFunds = 0.0f;
                        String time = "";
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = (JSONObject) jsonArray.get(i);
                            endFunds = (float) jsonObject.getDouble("endFunds");
                            time = jsonObject.getString("time");
                            if (i == 0){
                                maxY = endFunds;
                                miniY = endFunds;
                            }else{
                                if (maxY < endFunds){
                                    maxY = endFunds;
                                }else if (miniY > endFunds){
                                    miniY = endFunds;
                                }
                            }
                            SpotEntity spotEntity = new SpotEntity();
                            spotEntity.setSpotData(endFunds);
                            spotEntity.setSpotTime(time);
                            lineAveData.add(spotEntity);
                            xtitle.add(time);
                        }
                        initMinuteChart();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    /**
     * 获取持仓股票实时数据进行处理
     * @param listPosition
     */
    private void getRealTimeData(ArrayList<PositionEntity>  listPosition){
       String codeStr = "";
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
        final String finalCodeStr = codeStr;
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用接口获取股票当前行情数据（返回股票代码和最新价格的map）
                mapStr = sharesUtil.getsharess(finalCodeStr);
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

    /**
     * 设置分时图的属性
     */
    private void initMinuteChart() {

        maxY = (float) (maxY + maxY * 0.1);
        miniY = (float) (miniY - miniY * 0.1);

        List<String> ytitleInner = new ArrayList<String>();
        ytitleInner.add(maxY+"");
        ytitleInner.add("");
        ytitleInner.add(miniY+"");
        ytitleInner.add("");
        ytitle = new ArrayList<String>();
        ytitle.add("");
        ytitle.add("");
        ytitle.add("");
        int size = xtitle.size();
        List<String> listTitle = new ArrayList<String>();
        listTitle.add(xtitle.get(0));
        if (size > 2){
            listTitle.add(xtitle.get(size/2));
        }
        listTitle.add(xtitle.get(size-1));

        List<LineEntity> lines = new ArrayList<LineEntity>();
        //日均线数据
        LineEntity MAve = new LineEntity();
        MAve.setTitle("MAve");
        MAve.setLineColor(Color.rgb(255, 130, 142));
        MAve.setBackger(true);
        MAve.setLineData(lineAveData);
        lines.add(MAve);

        assetsChart.setAxisMarginTop(10);
        assetsChart.setAxisMarginLeft(10);
        assetsChart.setAxisMarginRight(10);
        assetsChart.setAxisMarginBottom(30);
        // 画布外的标题
        assetsChart.setAxisYTitles(ytitle);
        assetsChart.setAxisXTitles(listTitle);
        // 画布内的标题
        assetsChart.setAxisYTitlesR(ytitleInner);
        //y轴的最大值
        assetsChart.setMaxValue(maxY);
        //y轴的最小值
        assetsChart.setMinValue(miniY);
        //一共需要画多少点
        assetsChart.setMaxPointNum(lineAveData.size()-1);
        // X轴上的标题是否显示
        assetsChart.setDisplayAxisXTitle(true);
        // Y轴上的标题是否显示
        assetsChart.setDisplayAxisYTitle(true);
        // 纬线是否显示
        assetsChart.setDisplayLatitude(true);
        // 经线是否显示
        assetsChart.setDisplayLongitude(true);
        // 经线是否为虚线
        assetsChart.setDashLongitude(true);
        assetsChart.setLineData(lines);
        // redraw
        assetsChart.invalidate();
    }
}
