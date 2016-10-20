package com.example.sjqcjstock.Activity.stocks;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.entity.stocks.StocksInfo;
import com.example.sjqcjstock.fragment.stocks.FragmentDayMap;
import com.example.sjqcjstock.fragment.stocks.FragmentMonthMap;
import com.example.sjqcjstock.fragment.stocks.FragmentTimeMap;
import com.example.sjqcjstock.fragment.stocks.FragmentWeekMap;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.view.stocks.NoScrollViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 股票详细页面
 * Created by Administrator on 2016/8/5.
 */
public class SharesDetailedActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    // 分时线图
    private FragmentTimeMap timeMap;
    // 股票标题
    private String titleName = "";
    // 股票编号
    private String code = "";
    // 自定义ViewPager(用来禁止滑动事件)
    private NoScrollViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mDatas;
    // 控件
    private TextView textMinute, textDay, textWeek, textMonth;
    private LinearLayout llMinute, llDay, llWeek, llMonth;
    private ImageView img_line;

    // 当前价格
    private TextView price;
    // 当前涨幅价格
    private TextView priceRise;
    // 当前涨幅百分比
    private TextView riseAndFall;
    // 今开
    private TextView openPrice;
    // 昨收
    private TextView zuoShou;
    // 成交量
    private TextView volume;
    // 换手率
    private TextView turnover;
    // 最高
    private TextView highest;
    // 内盘
    private TextView invol;
    // 市盈率
    private TextView PERatio;
    // 最低
    private TextView minimum;
    // 外盘
    private TextView outerDisc;
    // 振幅
    private TextView amplitude;
    // 成交额
    private TextView turnoverVolume;
    // 总市值
    private TextView totalMarketValue;
    // 流通市值
    private TextView circulationarketValue;
    // 买卖的数量价格
    private Map<String, String> buySellMap;
    // 增长类型
    private String increaseType;
    // 当前股票的数据（日K,周K,月k用）
    private String strK = "";
    // 滑动条颜色
    private int select_color;
    private int unselect_color;
    private int mScreen1_4;
    /**
     * 当前视图宽度
     **/
    private Integer viewPagerW = 0;

    // 需要加载的行情数据
    private StocksInfo stocksInfo;
    // 定时器
    private Timer timer;
    // 线图Fragment的定义
    private FragmentDayMap dayMap = null;
    private FragmentWeekMap weekMap = null;
    private FragmentMonthMap monthMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shares_detailed);
        ExitApplication.getInstance().addActivity(this);
        titleName = getIntent().getStringExtra("name");
        code = getIntent().getStringExtra("code");
        initLine();
        findView();
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 关闭掉定时器
        timer.cancel();
    }


    /**
     * 控件绑定
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
        // 股票标题
        ((TextView) findViewById(R.id.title_name)).setText(titleName + "(" + code + ")");
        // 当前价格
        price = (TextView) findViewById(R.id.price_tv);
        // 当前涨幅价格
        priceRise = (TextView) findViewById(R.id.price_rise_tv);
        // 当前涨幅百分比
        riseAndFall = (TextView) findViewById(R.id.rise_and_fall_tv);
        // 今开
        openPrice = (TextView) findViewById(R.id.open_price_tv);
        // 昨收
        zuoShou = (TextView) findViewById(R.id.zuo_shuo_tv);
        // 成交量
        volume = (TextView) findViewById(R.id.volume_tv);
        // 换手率
        turnover = (TextView) findViewById(R.id.turnover_tv);
        // 最高
        highest = (TextView) findViewById(R.id.highest_tv);
        // 内盘
        invol = (TextView) findViewById(R.id.invol_tv);
        // 市盈率
        PERatio = (TextView) findViewById(R.id.p_e_ratio_tv);
        // 最低
        minimum = (TextView) findViewById(R.id.minimum_tv);
        // 外盘
        outerDisc = (TextView) findViewById(R.id.outerDisc_tv);
        // 振幅
        amplitude = (TextView) findViewById(R.id.amplitude_tv);
        // 成交额
        turnoverVolume = (TextView) findViewById(R.id.turnover_volume_tv);
        // 总市值
        totalMarketValue = (TextView) findViewById(R.id.total_market_value_tv);
        // 流通市值
        circulationarketValue = (TextView) findViewById(R.id.circulationalket_value_tv);
        // 获取颜色
        select_color = getResources().getColor(R.color.color_toptitle);
        unselect_color = getResources().getColor(R.color.color_000000);

        textMinute = (TextView) findViewById(R.id.text_minute);
        textDay = (TextView) findViewById(R.id.text_day);
        llMinute = (LinearLayout) findViewById(R.id.linear_minute);
        llDay = (LinearLayout) findViewById(R.id.linear_day);
        textWeek = (TextView) findViewById(R.id.text_week);
        llWeek = (LinearLayout) findViewById(R.id.linear_week);
        llMonth = (LinearLayout) findViewById(R.id.linear_month);
        textMonth = (TextView) findViewById(R.id.text_month);

        llMinute.setOnClickListener(new MyOnClickListenser(0));
        llDay.setOnClickListener(new MyOnClickListenser(1));
        llWeek.setOnClickListener(new MyOnClickListenser(2));
        llMonth.setOnClickListener(new MyOnClickListenser(3));

        mViewPager = (NoScrollViewPager) findViewById(R.id.mViewpager);
        // 预加载的页面个数
        mViewPager.setOffscreenPageLimit(4);
        mDatas = new ArrayList<Fragment>();

    }

    /**
     * 获取整理数据
     */
    private void initData() {
        stocksInfo = new StocksInfo();

        // 开线程获取网络数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取该股票当天的分时数据
                String strData = HttpUtil.getIntentData("http://qt.gtimg.cn/q=sz" + code);
                processData(strData);
                handler.sendEmptyMessage(0);
            }
        }).start();

        timer = new Timer();
        // 开定时器获取数据
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // 获取该股票当天的分时数据
                String strData = HttpUtil.getIntentData("http://qt.gtimg.cn/q=sz" + code);
                processData(strData);
                handler.sendEmptyMessage(1);
            }
        };
        timer.schedule(task, 30000, 30000); // 30000s后执行task,经过30s再次执行

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
                    setInformation();
                    // 加载K线图
                    initFragment(stocksInfo.getZuoShou());
                    break;
                case 1:
                    setInformation();
                    timeMap.updateBuySell(buySellMap);
//                    dayMap.udpateKmap(strK);
                    break;
            }
        }
    };

    /**
     * 设定刷新页面信息
     */
    private void setInformation() {
        price.setText(stocksInfo.getSpotPrice());
        riseAndFall.setText(stocksInfo.getHighsLowsThan() + "%");
        Double highsLows = Double.valueOf(stocksInfo.getHighsLows());
        priceRise.setText(highsLows + "");
        if (highsLows > 0) {
            price.setTextColor(Color.RED);
            riseAndFall.setTextColor(Color.RED);
            priceRise.setTextColor(Color.RED);
        } else {
            price.setTextColor(Color.rgb(139, 195, 74));
            riseAndFall.setTextColor(Color.rgb(139, 195, 74));
            priceRise.setTextColor(Color.rgb(139, 195, 74));
        }

        openPrice.setText(stocksInfo.getOpenPrice());
        // 昨收
        zuoShou.setText(stocksInfo.getZuoShou());
        // 成交量
        volume.setText(Utils.getNumberFormatWY(stocksInfo.getVolume()));
        // 换手率
        turnover.setText(stocksInfo.getTurnover() + "%");
        // 最高
        highest.setText(stocksInfo.getHighest());
        // 内盘
        invol.setText(Utils.getNumberFormatWY(stocksInfo.getInvol()));
        // 市盈率
        PERatio.setText(stocksInfo.getPERatio());
        // 最低
        minimum.setText(stocksInfo.getMinimum());
        // 外盘
        outerDisc.setText(Utils.getNumberFormatWY(stocksInfo.getOuterDisc()));
        // 振幅
        amplitude.setText(stocksInfo.getAmplitude() + "%");
        // 成交额
        turnoverVolume.setText(stocksInfo.getTurnoverVolume() + "万");
        // 总市值
        totalMarketValue.setText(stocksInfo.getTotalMarketValue() + "亿");
        // 流通市值
        circulationarketValue.setText(stocksInfo.getCirculationarketValue() + "亿");
    }


    /**
     * 根据返回的值进行处理得到想要的数据
     *
     * @param strData
     */
    private void processData(String strData) {
        stocksInfo = new StocksInfo();
        buySellMap = new HashMap<String, String>();
        // 每只股票的数据
        String[] shares = strData.split(";");
        for (String str : shares) {
            if ("".equals(str.trim())) {
                continue;
            }
            // 每只股票的详细数据
            String[] sharesMinute = str.split("~");
            // 股票名称
            stocksInfo.setName(sharesMinute[1]);
            // 股票代码
            stocksInfo.setCode(sharesMinute[2]);
            // 当前价格
            stocksInfo.setSpotPrice(sharesMinute[3]);
            // 今开价格
            stocksInfo.setOpenPrice(sharesMinute[5]);
            // 涨跌
            stocksInfo.setHighsLows(sharesMinute[31]);
            // 涨跌百分比
            stocksInfo.setHighsLowsThan(sharesMinute[32]);
            // 昨收
            stocksInfo.setZuoShou(sharesMinute[4]);
            // 成交量
            stocksInfo.setVolume(sharesMinute[6]);
            // 换手率
            stocksInfo.setTurnover(sharesMinute[38]);
            // 最高
            stocksInfo.setHighest(sharesMinute[33]);
            // 内盘
            stocksInfo.setInvol(sharesMinute[8]);
            // 市盈率
            stocksInfo.setPERatio(sharesMinute[39]);
            // 最低
            stocksInfo.setMinimum(sharesMinute[34]);
            // 外盘
            stocksInfo.setOuterDisc(sharesMinute[7]);
            // 振幅
            stocksInfo.setAmplitude(sharesMinute[43]);
            // 成交额
            stocksInfo.setTurnoverVolume(sharesMinute[37]);
            // 总市值
            stocksInfo.setTotalMarketValue(sharesMinute[45]);
            // 流通市值
            stocksInfo.setCirculationarketValue(sharesMinute[44]);
            // 拼接要用的价格信息
            strK = Utils.getNowDate1() + "|" + sharesMinute[5] + "|" + sharesMinute[3] + "|" + sharesMinute[33] + "|" + sharesMinute[34] + "|" + sharesMinute[6] + "|" + sharesMinute[30];
            buySellMap.put("buy1P", sharesMinute[9]);
            buySellMap.put("buy1N", sharesMinute[10]);
            buySellMap.put("buy2P", sharesMinute[11]);
            buySellMap.put("buy2N", sharesMinute[12]);
            buySellMap.put("buy3P", sharesMinute[13]);
            buySellMap.put("buy3N", sharesMinute[14]);
            buySellMap.put("buy4P", sharesMinute[15]);
            buySellMap.put("buy4N", sharesMinute[16]);
            buySellMap.put("buy5P", sharesMinute[17]);
            buySellMap.put("buy5N", sharesMinute[18]);

            buySellMap.put("sell1P", sharesMinute[19]);
            buySellMap.put("sell1N", sharesMinute[20]);
            buySellMap.put("sell2P", sharesMinute[21]);
            buySellMap.put("sell2N", sharesMinute[22]);
            buySellMap.put("sell3P", sharesMinute[23]);
            buySellMap.put("sell3N", sharesMinute[24]);
            buySellMap.put("sell4P", sharesMinute[25]);
            buySellMap.put("sell4N", sharesMinute[26]);
            buySellMap.put("sell5P", sharesMinute[27]);
            buySellMap.put("sell5N", sharesMinute[28]);

            Double highsLows = Double.valueOf(sharesMinute[31]);
            if (highsLows > 0) {
                increaseType = "1";
            } else {
                increaseType = "0";
            }
            buySellMap.put("increaseType", increaseType);
        }
    }

    /**
     * 初始化fragment
     */
    private void initFragment(String openF) {
        timeMap = new FragmentTimeMap(code, openF, buySellMap);
        dayMap = new FragmentDayMap(code, strK);
        weekMap = new FragmentWeekMap(code, strK);
        monthMap = new FragmentMonthMap(code, strK);
        mDatas.add(timeMap);
        mDatas.add(dayMap);
        mDatas.add(weekMap);
        mDatas.add(monthMap);
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mDatas == null ? 0 : mDatas.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mDatas.get(position);
            }
        };
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setCurrentItem(0);


    }

    /**
     * 初始化line
     */
    private void initLine() {
        img_line = (ImageView) findViewById(R.id.img_line);
        mScreen1_4 = ImageUtil.getScreenWidth(this) / 4;
        ViewGroup.LayoutParams lp = img_line.getLayoutParams();
        lp.width = mScreen1_4;
        img_line.setLayoutParams(lp);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        viewPagerW = mViewPager.getWidth() + mViewPager.getPageMargin();
        LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) img_line.getLayoutParams();
        // 关键算法
        lp.leftMargin = (int) ((int) (mScreen1_4 * position) + (((double) positionOffsetPixels / viewPagerW) * mScreen1_4));
        img_line.setLayoutParams(lp);
    }

    @Override
    public void onPageSelected(int position) {
        resetTextColor();
        switch (mViewPager.getCurrentItem()) {
            case 0:
                textMinute.setTextColor(select_color);
                break;
            case 1:
                textDay.setTextColor(select_color);
                break;
            case 2:
                textWeek.setTextColor(select_color);
                break;
            case 3:
                textMonth.setTextColor(select_color);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 点击文字进行切换
     *
     * @author wuxl
     */
    public class MyOnClickListenser implements View.OnClickListener {

        private int index = 0;

        public MyOnClickListenser(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            resetTextColor();
            switch (v.getId()) {
                case R.id.linear_minute:
                    textMinute.setTextColor(select_color);
                    break;
                case R.id.linear_day:
                    textDay.setTextColor(select_color);
                    break;
                case R.id.linear_week:
                    textWeek.setTextColor(select_color);
                    break;
                case R.id.linear_month:
                    textMonth.setTextColor(select_color);
                    break;
            }
            mViewPager.setCurrentItem(index);
        }
    }

    /**
     * 将文字设置为未选中时的颜色
     */
    private void resetTextColor() {
        textMinute.setTextColor(unselect_color);
        textDay.setTextColor(unselect_color);
        textWeek.setTextColor(unselect_color);
        textMonth.setTextColor(unselect_color);
    }

    /**
     * 跳转到买入股票
     */
    public void purchaseClick(View view) {
        Intent intent = new Intent(this, BusinessActivity.class);
        intent.putExtra("type", "0");
        startActivity(intent);
    }

    /**
     * 加入自选股
     */
    public void optionalClick(View view) {

    }
}
