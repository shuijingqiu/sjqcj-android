package com.example.sjqcjstock.fragment.stocks;

import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.stocks.LineEntity;
import com.example.sjqcjstock.entity.stocks.ListStickEntity;
import com.example.sjqcjstock.entity.stocks.SpotEntity;
import com.example.sjqcjstock.entity.stocks.StickEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.netutil.stocks.CountUtil;
import com.example.sjqcjstock.view.stocks.LineChart;
import com.example.sjqcjstock.view.stocks.StickChart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 分时图的Fragment
 * Created by Administrator on 2016/8/8.
 */
public class FragmentTimeMap extends Fragment {

    // 买的控件
    private TextView buy1p, buy1n, buy2p, buy2n, buy3p, buy3n, buy4p, buy4n, buy5p, buy5n;
    // 卖的控件
    private TextView sell1p, sell1n, sell2p, sell2n, sell3p, sell3n, sell4p, sell4n, sell5p, sell5n;
    // 股票编号
    private String code = "";
    //柱状图数据
    private List<StickEntity> vol;
    // 分时线的数据
    private List<SpotEntity> lineMinData;
    // 分时线的数据
    private List<SpotEntity> lineAveData;
    // 每个柱状图的数据
    private ListStickEntity liststick;
    // 分时图的控件
    private LineChart machart;
    //柱状图数据对象
    private StickChart stickchart;
    // Y轴的最大值
    private float maxY = 0;
    // Y轴的最小值
    private float miniY = 0;
    // 当日开盘价格RF
    private float openF = 0;
    // 柱状图的最大值
    private float maxYS = 0;
    // 买卖的数量
    private Map<String, String> buySellMap;
    // 定时器
    private Timer timer;

    public FragmentTimeMap(String code, String openF, Map<String, String> buySellMap) {
        this.code = code;
        this.openF = Float.valueOf(openF);
        this.buySellMap = buySellMap;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shares_minute_map, container, false);
        findView(view);
        //加载测试用数据
        initData();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 关闭掉定时器
        timer.cancel();
    }

    /**
     * 加载控件
     */
    private void findView(View view) {
//        time = (TextView) view.findViewById(R.id.time);
//        price = (TextView) view.findViewById(R.id.price);
//        headNumber = (TextView) view.findViewById(R.id.head_number);
        // 线图的控件
        machart = (LineChart) view.findViewById(R.id.machart);
        // 柱状图的控件
        this.stickchart = (StickChart) view.findViewById(R.id.stickchart);
        machart.setOnTouchListener(new setOnTouchListener());
        stickchart.setOnTouchListener(new setOnTouchListener());

        buy1p = (TextView) view.findViewById(R.id.price_buy_1);
        buy1n = (TextView) view.findViewById(R.id.number_buy_1);
        buy2p = (TextView) view.findViewById(R.id.price_buy_2);
        buy2n = (TextView) view.findViewById(R.id.number_buy_2);
        buy3p = (TextView) view.findViewById(R.id.price_buy_3);
        buy3n = (TextView) view.findViewById(R.id.number_buy_3);
        buy4p = (TextView) view.findViewById(R.id.price_buy_4);
        buy4n = (TextView) view.findViewById(R.id.number_buy_4);
        buy5p = (TextView) view.findViewById(R.id.price_buy_5);
        buy5n = (TextView) view.findViewById(R.id.number_buy_5);

        sell1p = (TextView) view.findViewById(R.id.price_sell_1);
        sell1n = (TextView) view.findViewById(R.id.number_sell_1);
        sell2p = (TextView) view.findViewById(R.id.price_sell_2);
        sell2n = (TextView) view.findViewById(R.id.number_sell_2);
        sell3p = (TextView) view.findViewById(R.id.price_sell_3);
        sell3n = (TextView) view.findViewById(R.id.number_sell_3);
        sell4p = (TextView) view.findViewById(R.id.price_sell_4);
        sell4n = (TextView) view.findViewById(R.id.number_sell_4);
        sell5p = (TextView) view.findViewById(R.id.price_sell_5);
        sell5n = (TextView) view.findViewById(R.id.number_sell_5);

        // 修改买卖信息
        updateBuySell(buySellMap);

    }

    /**
     * 获取网络数据线程
     */
    private void initData() {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                String strData = HttpUtil.getIntentData("http://data.gtimg.cn/flashdata/hushen/minute/sz" + code + ".js");
                processData(strData);
                handler.sendEmptyMessage(0);
            }
        };
        timer.schedule(task, 0, 30000); // 0s后执行task,经过30s再次执行
    }

    /**
     * 设置分时图的属性
     */
    private void initMinuteChart() {
        List<LineEntity> lines = new ArrayList<LineEntity>();

        //日均线数据
        LineEntity MAve = new LineEntity();
        MAve.setTitle("MAve");
        MAve.setLineColor(Color.rgb(255, 130, 142));
        MAve.setLineData(lineAveData);
        lines.add(MAve);

        //线数据
        LineEntity MA1 = new LineEntity();
        MA1.setTitle("MA1");
        MA1.setLineColor(Color.rgb(111, 194, 246));
        MA1.setBackger(true);
        MA1.setLineData(lineMinData);
        lines.add(MA1);


        List<String> ytitleInner = CountUtil.setYTitleInner(maxY, openF);
        List<String> ytitle = new ArrayList<String>();
        ytitle.add("");
        ytitle.add("");
        ytitle.add("");
        ytitle.add("");
        ytitle.add("");
        List<String> xtitle = new ArrayList<String>();
        xtitle.add("9:30");
        xtitle.add("10:30");
        xtitle.add("13:00");
        xtitle.add("14:00");
        xtitle.add("15:00");

        machart.setAxisMarginTop(10);
        machart.setAxisMarginLeft(10);
        machart.setAxisMarginRight(20);
        machart.setAxisMarginBottom(30);
        // 画布外的标题
        machart.setAxisYTitles(ytitle);
        machart.setAxisXTitles(xtitle);
        // 画布内的标题
        machart.setAxisYTitlesR(ytitleInner);
        //y轴的最大值
        machart.setMaxValue(maxY);
        //X轴的最小值
        machart.setMinValue(miniY);
        //一共需要画多少点
        machart.setMaxPointNum(240);
        // X轴上的标题是否显示
        machart.setDisplayAxisXTitle(true);
        // Y轴上的标题是否显示
        machart.setDisplayAxisYTitle(true);
        // 纬线是否显示
        machart.setDisplayLatitude(true);
        // 经线是否显示
        machart.setDisplayLongitude(true);
        // 经线是否为虚线
        machart.setDashLongitude(true);

        machart.setLineData(lines);

    }

    /**
     * 设置柱状图的一些属性
     */
    private void initStickChart() {
        stickchart.setAxisMarginRight(20);
        stickchart.setAxisMarginLeft(10);
        //最大显示足数
        stickchart.setMaxSticksNum(240);
        //最大纬线数
        stickchart.setLatitudeNum(2);
        //最大经线数
        stickchart.setLongitudeNum(5);
        //X最大值
        stickchart.setMaxValue(maxYS);
        //X最小值
        stickchart.setMinValue(0);
        //柱条的间距
        stickchart.setStickSpacing(2.f);
        //X轴标题是否显示中间
        stickchart.setShowPlace(true);
        //柱状图是否有边框
        stickchart.setStickrame(true);


        List<String> xtitle = new ArrayList<String>();
        xtitle.add("");
        xtitle.add("");
        xtitle.add("");
        xtitle.add("");
        xtitle.add("");

        List<String> ytitle = new ArrayList<String>();
        ytitle.add("0");
        ytitle.add(maxYS + "");

        //Y轴的标题列表如果未设置 会根据设置的最大纬线数来和柱状数据时间来计算
        stickchart.setAxisYTitles(ytitle);
        //X轴的标题列表,如果未设置 X的标题列表 会根据设置的最大值最小值最大纬线数来计算
        stickchart.setAxisXTitles(xtitle);

        // 是否显示标题
        stickchart.setDisplayAxisXTitle(false);
        stickchart.setDisplayAxisYTitle(false);
        stickchart.setDisplayLatitude(true);
        stickchart.setDisplayLongitude(true);
        liststick = new ListStickEntity();
        //填充颜色
        liststick.setStickFillColor(Color.rgb(235, 35, 65));// 红色
        liststick.setStickFillColor2(Color.rgb(50, 174, 110));// 绿色
        //为chart1增加均线
        liststick.setStickData(vol);
        stickchart.setStickData(liststick);
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // UI界面的更新等相关操作
            // 设置分时图的一些属性
            initMinuteChart();
            // 设置K线图的一些属性
            initStickChart();

            // redraw
            machart.invalidate();
            // redraw
            stickchart.invalidate();
        }
    };

    /**
     * 加工处理数据得到我们想要的数据
     */
    private void processData(String strData) {
        // 分时线数据
        lineMinData = new ArrayList<SpotEntity>();
        // 均线数据
        lineAveData = new ArrayList<SpotEntity>();

        strData = strData.replace("\\n\\", "*");
        String[] strS = strData.split("\\*");
        // 分时值
        float value = 0f;
        // 均线值
        float valueAve = 0f;
        // 总价值(求均线用)
        float valueNum = 0f;
        // 总成交量(求均线用)
        float valueNumS = 0f;
        // 当前的成交量
        float valueS = 0f;
        // 上一次的数据（手数）
        float valueNowS = 0f;
        // 上一次的数据（价格）
        float valueNow = 0f;
        // 柱状图的声明
        this.vol = new ArrayList<StickEntity>();
        // 时间变量
        String time = "";
        // 涨跌类型(1 相对上一分钟涨了 0 相对上一分钟跌了)
        int type = 1;
        for (int i = 2; i < strS.length - 1; i++) {
            String str = strS[i];
            String[] strk = str.split(" ");
            time = Utils.setInsertMark(strk[0]);
            value = Float.valueOf(strk[1]);
            valueNumS = Float.valueOf(strk[2]);
            if (maxY == 0f) {
                maxY = value;
                miniY = value;
                maxYS = valueNumS;
                valueAve = value;
                valueNow = value;
                valueNum = value * valueNumS;
                if (valueNow >= openF) {
                    type = 1;
                } else {
                    type = 0;
                }

            } else {
                valueS = valueNumS - valueNowS;
                valueNum += value * valueS;
                valueAve = valueNum / valueNumS;
                if (maxY < value) {
                    maxY = value;
                }
                if (miniY > value) {
                    miniY = value;
                }
                if (maxYS < valueS) {
                    maxYS = valueS;
                }
                if (value >= valueNow) {
                    type = 1;
                } else {
                    type = 0;
                }
            }
            valueNowS = Float.valueOf(strk[2]);
            valueNow = value;
            // K线图数据的添加
            lineMinData.add(new SpotEntity(valueAve, time));
            lineAveData.add(new SpotEntity(value, time));
            // 柱状图数据的添加
            vol.add(new StickEntity(valueS, type, time));
        }

        if (Math.abs(maxY - openF) > Math.abs(miniY - openF)) {
            // 以最大值为基准
            miniY = 2 * openF - maxY;
        } else {
            // 以最小值为基准
            maxY = 2 * openF - miniY;
        }
    }

    public class setOnTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            // 这个判断不要
            if (lineMinData == null) {
                return true;
            }
            if (event.getY() > 0
                    && event.getY() < machart.getBottom() - machart.getAxisMarginBottom()
                    && event.getX() > machart.getLeft() + machart.getAxisMarginLeft()
                    && event.getX() < machart.getRight()) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                    // 获取点击坐�?
                    machart.setClickPostX(event.getX());
                    machart.setClickPostY(event.getY());
                    PointF point = new PointF(machart.getClickPostX(), machart.getClickPostY());
                    machart.setTouchPoint(point);
                    // redraw
                    machart.invalidate();
                    // do notify
                    machart.notifyEventAll(machart);

                    stickchart.setClickPostX(event.getX());
                    stickchart.setClickPostY(event.getY());
                    stickchart.invalidate();
                    stickchart.notifyEventAll(stickchart);


                    // 获取X轴所占的百分百
                    float floatX = Float.valueOf(machart.getAxisXGraduate(event.getX()));
                    // 获取百分比所在的点(一共有240个点)
                    int intX = (int) (240 * floatX);
                    if (intX < lineMinData.size()) {
//                        price.setText(lineMinData.get(intX).getSpotData() + "");
//                        time.setText(lineMinData.get(intX).getSpotTime());
//                        headNumber.setText(liststick.getStickData().get(intX).getHigh() + "");
                    }
                }
            }
            return true;
        }
    }

    /**
     * 修改买卖量
     */
    public void updateBuySell(Map<String, String> buySellMap) {

        if (buySellMap != null && buySellMap.size() > 0) {
            if ("1".equals(buySellMap.get("increaseType"))) {
                buy1p.setTextColor(Color.RED);
                buy2p.setTextColor(Color.RED);
                buy3p.setTextColor(Color.RED);
                buy4p.setTextColor(Color.RED);
                buy5p.setTextColor(Color.RED);
                sell1p.setTextColor(Color.RED);
                sell2p.setTextColor(Color.RED);
                sell3p.setTextColor(Color.RED);
                sell4p.setTextColor(Color.RED);
                sell5p.setTextColor(Color.RED);
            } else {
                buy1p.setTextColor(Color.rgb(139, 195, 74));
                buy2p.setTextColor(Color.rgb(139, 195, 74));
                buy3p.setTextColor(Color.rgb(139, 195, 74));
                buy4p.setTextColor(Color.rgb(139, 195, 74));
                buy5p.setTextColor(Color.rgb(139, 195, 74));
                sell1p.setTextColor(Color.rgb(139, 195, 74));
                sell2p.setTextColor(Color.rgb(139, 195, 74));
                sell3p.setTextColor(Color.rgb(139, 195, 74));
                sell4p.setTextColor(Color.rgb(139, 195, 74));
                sell5p.setTextColor(Color.rgb(139, 195, 74));
            }
            sell5p.setText(buySellMap.get("sell5P"));
            sell5n.setText(buySellMap.get("sell5N"));
            sell4p.setText(buySellMap.get("sell4P"));
            sell4n.setText(buySellMap.get("sell4N"));
            sell3p.setText(buySellMap.get("sell3P"));
            sell3n.setText(buySellMap.get("sell3N"));
            sell2p.setText(buySellMap.get("sell2P"));
            sell2n.setText(buySellMap.get("sell2N"));
            sell1p.setText(buySellMap.get("sell1P"));
            sell1n.setText(buySellMap.get("sell1N"));

            buy1p.setText(buySellMap.get("buy1P"));
            buy1n.setText(buySellMap.get("buy1N"));
            buy2p.setText(buySellMap.get("buy2P"));
            buy2n.setText(buySellMap.get("buy2N"));
            buy3p.setText(buySellMap.get("buy3P"));
            buy3n.setText(buySellMap.get("buy3N"));
            buy4p.setText(buySellMap.get("buy4P"));
            buy4n.setText(buySellMap.get("buy4N"));
            buy5p.setText(buySellMap.get("buy5P"));
            buy5n.setText(buySellMap.get("buy5N"));

        }

    }

}
