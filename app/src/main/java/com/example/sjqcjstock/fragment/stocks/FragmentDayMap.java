package com.example.sjqcjstock.fragment.stocks;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.stocks.LineEntity;
import com.example.sjqcjstock.entity.stocks.ListStickEntity;
import com.example.sjqcjstock.entity.stocks.OHLCEntity;
import com.example.sjqcjstock.entity.stocks.SpotEntity;
import com.example.sjqcjstock.entity.stocks.StickEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.netutil.stocks.CountUtil;
import com.example.sjqcjstock.view.stocks.MACandleStickChart;
import com.example.sjqcjstock.view.stocks.StickChart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 日K图的Fragment
 * Created by Administrator on 2016/8/8.
 */
public class FragmentDayMap extends Fragment {

    // 股票编号
    private String code = "";
    //蜡线图的view对象
    private MACandleStickChart macandlestickchart;
    //柱状图数据对象
    private StickChart stickchart;
    //蜡线图数据对象
    private List<OHLCEntity> ohlcAll;
    // 截取后的数据
    private List<OHLCEntity> ohlc;
    //柱状图数据
    private List<StickEntity> volAll;
    // 截取后的数据
    private List<StickEntity> vol;
    // 五日均线全部和截取后的
    private List<SpotEntity> spot5All, spot5;
    // 十日均线全部和截取后的
    private List<SpotEntity> spot10All, spot10;
    // 二十日均线全部和截取后的
    private List<SpotEntity> spot20All, spot20;
    // Y轴的最大值
    private float maxY = 0;
    // Y轴的最小值
    private float miniY = 0;
    // 柱状图的最大值
    private float maxYS = 0;
    // 默认最大显示足数
    private int maxSticksNum = 60;
    // 滑动偏移量
    private int offset = 0;
    // 最后的结束点
    private int end = 0;
    // 滑动后的结束点
    private int offsetEnd = 0;
    // 当前年份
    private String year = "";
    // 网络请求提示
    private ProgressDialog dialog;
    // 判断是否可以获取网络数据
    private boolean isGetDate = true;
    // 当前年份的数据条数
    private int yearSize = 0;
    // 获取数据的线程
    private Thread thread;
    // 当前的最新数据
    private String strKmap = "";

    public FragmentDayMap(String code, String strKmap) {
        this.code = code;
        this.strKmap = strKmap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shares_day_map, container, false);
        findView(view);
        initData();
        return view;
    }

    /**
     * 控件绑定
     */
    private void findView(View view) {
        Calendar calendar = Calendar.getInstance();
        year = (calendar.get(Calendar.YEAR) + "").substring(2);
        // 蜡线图的控件
        this.macandlestickchart = (MACandleStickChart) view.findViewById(R.id.day_map_k);
        // 柱状图的控件
        this.stickchart = (StickChart) view.findViewById(R.id.day_stick_chart);
        // 柱状图的声明
        this.vol = new ArrayList<StickEntity>();
        // 蜡线图的声明
        this.ohlc = new ArrayList<OHLCEntity>();
        macandlestickchart.setOnTouchListener(new setOnTouchListener());
        stickchart.setOnTouchListener(new setOnTouchListener());
        // 均线的计算
        spot5 = new ArrayList<SpotEntity>();
        spot10 = new ArrayList<SpotEntity>();
        spot20 = new ArrayList<SpotEntity>();

        this.ohlcAll = new ArrayList<OHLCEntity>();
        // 柱状图的声明
        this.volAll = new ArrayList<StickEntity>();
    }

    /**
     * 获取网络数据线程
     */
    private void initData() {
        // 开线程获取网络数据
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取16年该股票的数据
                String strData = HttpUtil.getIntentData("http://data.gtimg.cn/flashdata/hushen/daily/" + year + "/sz" + code + ".js");
                resolveData(strData);
                processData();
            }
        });
        thread.start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // UI界面的更新等相关操作
            //蜡线图数据加载
            initMACandleStickChart();
            // 柱状图数据加载
            initStickChart();
            if (dialog != null) {
                dialog.dismiss();
            }
            isGetDate = true;
        }
    };

    /**
     * 蜡线图属性的控制
     */
    private void initMACandleStickChart() {

        List<String> ytitleInner = new ArrayList<String>();
        ytitleInner.add(Utils.getNumberFormat(maxY + ""));
        ytitleInner.add(Utils.getNumberFormat(miniY + ""));
        List<LineEntity> lines = new ArrayList<LineEntity>();
        //计算5日均线
        LineEntity MA5 = new LineEntity();
        MA5.setTitle("MA5");
        MA5.setLineColor(Color.rgb(229, 182, 93));
        MA5.setLineData(spot5);
        lines.add(MA5);

        //计算10日均线
        LineEntity MA10 = new LineEntity();
        MA10.setTitle("MA10");
        MA10.setLineColor(Color.rgb(147, 1, 87));
        MA10.setLineData(spot10);
        lines.add(MA10);

        //计算20日均线
        LineEntity MA25 = new LineEntity();
        MA25.setTitle("MA20");
        MA25.setLineColor(Color.rgb(0, 150, 100));
        MA25.setLineData(spot20);
        lines.add(MA25);

        //最大显示足数
        macandlestickchart.setMaxSticksNum(maxSticksNum);
        //最大纬线数
        macandlestickchart.setLatitudeNum(4);
        //最大经线数
        macandlestickchart.setLongitudeNum(4);
        //最大价格
        macandlestickchart.setMaxValue(maxY);
        //最小价格
        macandlestickchart.setMinValue(miniY);

        macandlestickchart.setAxisMarginLeft(10);
        macandlestickchart.setAxisMarginBottom(30);
        macandlestickchart.setAxisMarginTop(10);
        macandlestickchart.setDisplayAxisXTitle(true);
        macandlestickchart.setDisplayAxisYTitle(true);
        macandlestickchart.setAxisYTitlesR(ytitleInner);

        macandlestickchart.setDisplayLatitude(true);
        macandlestickchart.setDisplayLongitude(true);
        //为chart2增加均线
        macandlestickchart.setLineData(lines);
        //为chart2增加均线
        macandlestickchart.setOHLCData(ohlc);
        // redraw
        macandlestickchart.invalidate();
        // do notify
        macandlestickchart.notifyEventAll(macandlestickchart);
    }

    /**
     * 设置柱状图的一些属性
     */
    private void initStickChart() {
        stickchart.setAxisMarginLeft(10);
        //最大显示足数
        stickchart.setMaxSticksNum(maxSticksNum);
        //最大纬线数
        stickchart.setLatitudeNum(2);
        //最大经线数
        stickchart.setLongitudeNum(9);
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

        List<String> ytitle = new ArrayList<String>();
        ytitle.add("");
        ytitle.add("");
        ytitle.add("");

        //Y轴的标题列表如果未设置 会根据设置的最大纬线数来和柱状数据时间来计算
        stickchart.setAxisYTitles(ytitle);
        //X轴的标题列表,如果未设置 X的标题列表 会根据设置的最大值最小值最大纬线数来计算
        stickchart.setAxisXTitles(xtitle);
        // X轴上的标题是否显示
        stickchart.setDisplayAxisXTitle(false);
        // Y轴上的标题是否显示
        stickchart.setDisplayAxisYTitle(true);
        // 纬线是否显示
        stickchart.setDisplayLatitude(true);
        // 经线是否显示
        stickchart.setDisplayLongitude(true);
        // 柱状图所用数据属性
        ListStickEntity liststick = new ListStickEntity();
        //填充颜色
        liststick.setStickFillColor(Color.rgb(235, 35, 65));// 红色
        liststick.setStickFillColor2(Color.rgb(50, 174, 110));// 绿色
        //为chart1增加均线
        liststick.setStickData(vol);
        stickchart.setStickData(liststick);
        // redraw
        stickchart.invalidate();
        // do notify
        stickchart.notifyEventAll(stickchart);
    }

    /**
     * 把字符串遍历成数组数据
     */
    private void resolveData(String strData) {
        strData = strData.replace("\\n\\", "*");
        strData = strData.replace("\n", "");
        String[] strS = strData.split("\\*");
        // 开盘价格
        float valueK = 0f;
        // 收盘价格
        float valueS = 0f;
        // 最高价格
        float valueMax = 0f;
        // 最低价格
        float valueMin = 0f;
        // 时间变量
        String time = "";
        // 成交量
        float volume = 0f;
        int type = 1;
        List<OHLCEntity> listOhl = new ArrayList<OHLCEntity>();
        List<StickEntity> listStic = new ArrayList<StickEntity>();
        for (int i = 1; i < strS.length - 1; i++) {
            String str = strS[i];
            String[] strk = str.split(" ");
            time = strk[0];
            valueK = Float.valueOf(strk[1]);
            valueS = Float.valueOf(strk[2]);
            valueMax = Float.valueOf(strk[3]);
            valueMin = Float.valueOf(strk[4]);
            volume = Float.valueOf(Utils.getNumberFormatW(strk[5]));
            if (valueK >= valueS) {
                type = 0;
            } else {
                type = 1;
            }
            // K线图数据的添加
            listOhl.add(new OHLCEntity(valueK, valueMax, valueMin, valueS, time));
            // 柱状图数据的添加
            listStic.add(new StickEntity(volume, type, time));
        }
        // 重新排列数据
        if (ohlcAll.size() > 0) {
            listOhl.addAll(ohlcAll);
            ohlcAll = listOhl;
        } else {
            ohlcAll = listOhl;
        }
        if (volAll.size() > 0) {
            listStic.addAll(volAll);
            volAll = listStic;
        } else {
            volAll = listStic;
        }

        if (!"".equals(strKmap)) {
            strS = strKmap.split("\\|");
            if (Utils.isTimeOne(strS[6])) {
                valueK = Float.valueOf(strS[1]);
                valueS = Float.valueOf(strS[2]);
                // K线图数据的添加
                ohlcAll.add(new OHLCEntity(valueK, Float.valueOf(strS[3]), Float.valueOf(strS[4]), valueS, strS[0]));
                if (valueK >= valueS) {
                    type = 0;
                } else {
                    type = 1;
                }
                volume = Float.valueOf(Utils.getNumberFormatW(strS[5]));
                // 柱状图数据的添加
                volAll.add(new StickEntity(volume, type, strS[0]));
            }
        }

        // 跨年增加偏移量
        end = ohlcAll.size() - yearSize;
        int count = (int) (maxSticksNum * 0.9);
        if (count < yearSize) {
            end += count;
        }
        offsetEnd = end;
        yearSize = ohlcAll.size();
        spot5All = CountUtil.initMA(5, ohlcAll);
        spot10All = CountUtil.initMA(10, ohlcAll);
        spot20All = CountUtil.initMA(20, ohlcAll);
    }

    /**
     * 加工处理数据得到我们想要的数据
     */
    private void processData() {
        // 五日的数据
        float valueMax5 = 0f;
        // 十日的数据
        float valueMax10 = 0f;
        // 二十日的数据
        float valueMax20 = 0f;
        // 最高价格
        float valueMax = 0f;
        // 最低价格
        float valueMin = 0f;
        // 成交量
        float volume = 0f;
        OHLCEntity ohlcEntity = null;
        StickEntity stickEntity = null;
        int open = 0;
        end = offsetEnd + offset;
        if (end < maxSticksNum) {
            end = maxSticksNum;
        }
        if (end > ohlcAll.size()) {
            end = ohlcAll.size();
        }
        if (end >= maxSticksNum) {
            open = end - maxSticksNum;
            if (open == 0 && isGetDate) {
                // 重新加载上一年的数据
                year = Utils.getYearFormat(year);
                dialog = new ProgressDialog(getActivity());
                dialog.setMessage(Constants.loadMessage);
                dialog.setCancelable(false);
                dialog.show();
                isGetDate = false;
                initData();
                return;
            }
        }
        ohlc.clear();
        vol.clear();
        spot5.clear();
        spot10.clear();
        spot20.clear();
        maxY = 0f;
        for (int i = open; i < end; i++) {
            valueMax5 = spot5All.get(i).getSpotData();
            valueMax10 = spot10All.get(i).getSpotData();
            valueMax20 = spot20All.get(i).getSpotData();
            ohlcEntity = ohlcAll.get(i);
            stickEntity = volAll.get(i);
            valueMax = Float.valueOf(ohlcEntity.getHigh() + "");
            valueMin = Float.valueOf(ohlcEntity.getLow() + "");
            volume = Float.valueOf(stickEntity.getHigh() + "");
            if (maxY == 0f) {
                maxY = valueMax;
                miniY = valueMin;
                maxYS = volume;
            } else {
                if (maxY < valueMax) {
                    maxY = valueMax;
                }
                if (miniY > valueMin) {
                    miniY = valueMin;
                }
                if (maxYS < volume) {
                    maxYS = volume;
                }
            }

            if (maxY < valueMax5) {
                maxY = valueMax5;
            }
            if (maxY < valueMax10) {
                maxY = valueMax10;
            }
            if (maxY < valueMax20) {
                maxY = valueMax20;
            }
            if (miniY > valueMax5 && valueMax5 != 0) {
                miniY = valueMax5;
            }
            if (miniY > valueMax10 && valueMax10 != 0) {
                miniY = valueMax10;
            }
            if (miniY > valueMax20 && valueMax20 != 0) {
                miniY = valueMax20;
            }
            // K线图数据的添加
            ohlc.add(ohlcEntity);
            // 柱状图数据的添加
            vol.add(stickEntity);
            spot5.add(spot5All.get(i));
            spot10.add(spot10All.get(i));
            spot20.add(spot20All.get(i));
            // 更新线图
            handler.sendEmptyMessage(0);
        }
    }

    // 点击屏幕的触控点
    private float clickPoint = 0;
    // 滑动 后的触控点
    private float slipPoint = 0;
    private int mode = 0;
    private float oldDist;

    /**
     * 点击视图的滚动事件
     */
    public class setOnTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (event.getY() > 0
                    && event.getY() < macandlestickchart.getBottom() - macandlestickchart.getAxisMarginBottom()
                    && event.getX() > macandlestickchart.getLeft() + macandlestickchart.getAxisMarginLeft()
                    && event.getX() < macandlestickchart.getRight()) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        mode = 1;
                        // 记录点击时候的触控点
                        clickPoint = event.getX();
                        // 重置滑动后的结束点
                        offsetEnd = end;
                        break;
                    case MotionEvent.ACTION_UP:
                        mode = 0;
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        mode -= 1;
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        oldDist = calcDistance(event);
                        mode += 1;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode >= 2) {
                            // 如果不满初始值就不能滑动
                            if (ohlcAll.size() < maxSticksNum) {
                                return false;
                            }
                            float newDist = calcDistance(event);
                            if (newDist > oldDist + 3) {
                                if (maxSticksNum > 30) {
                                    maxSticksNum = maxSticksNum - 1;
                                }
                                oldDist = newDist;
                            }
                            if (newDist < oldDist - 3) {
                                if (maxSticksNum < 90) {
                                    maxSticksNum = maxSticksNum + 1;
                                }
                                oldDist = newDist;
                            }
                            processData();
                        } else {
                            // 记录滑动时候的触控点
                            slipPoint = clickPoint - event.getX();
                            offset = (int) slipPoint / 10;
                            // 重新计算数据并且刷新线图
                            processData();
                        }
                        break;
                }
            }
            return false;
        }
    }

    private float calcDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 添加当前的数据更新线图
     */
    public void udpateKmap(String strK) {
        // thread.isAlive 判断线程是否处于活动状态
        if ("".equals(strK)) {
            return;
        }
        String[] strS = strK.split("\\|");
        Double valueK = Double.valueOf(strS[1]);
        Double valueS = Double.valueOf(strS[2]);
        // K线图数据的添加
        ohlcAll.add(new OHLCEntity(valueK, Double.valueOf(strS[3]), Double.valueOf(strS[4]), valueS, strS[0]));
        int type = 0;
        if (valueK >= valueS) {
            type = 1;
        } else {
            type = 0;
        }
        // 柱状图数据的添加
        volAll.add(new StickEntity(Double.valueOf(strS[5]), type, strS[0]));
        yearSize = ohlcAll.size();
        spot5All = CountUtil.initMA(5, ohlcAll);
        spot10All = CountUtil.initMA(10, ohlcAll);
        spot20All = CountUtil.initMA(20, ohlcAll);
        processData();
    }

}
