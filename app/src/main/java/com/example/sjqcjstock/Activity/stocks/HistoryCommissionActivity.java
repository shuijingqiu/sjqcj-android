package com.example.sjqcjstock.Activity.stocks;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.stocks.CommissionAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.entity.stocks.StocksInfo;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 历史委托信息查询页面
 * Created by Administrator on 2016/8/11.
 */
public class HistoryCommissionActivity extends Activity {

    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private ListView listView;
    // 显示加载的Adapter
    private CommissionAdapter commissionAdapter;
    // 起始日期，截止日期
    private TextView startDateTv, endDateTv;
    private String startDate, endDate;
    // 起始年月日
    private int startYear, startMonth, startDay;
    // 截止年月日
    private int endYear, endMonth, endDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_history_commission);
        ExitApplication.getInstance().addActivity(this);

        Calendar now = Calendar.getInstance();
        endYear = startYear = now.get(Calendar.YEAR);
        endMonth = startMonth = now.get(Calendar.MONTH) + 1;
        endDay = startDay = now.get(Calendar.DAY_OF_MONTH);
        endDate = Utils.getStringDate(endYear, endMonth, endDay);

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

        startDateTv = (TextView) findViewById(R.id.start_date_tv);
        endDateTv = (TextView) findViewById(R.id.end_date_tv);
        endDateTv.setText(endDate);

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
        commissionAdapter.setlistData(listStocks);
    }

    /**
     * 修改起始时间
     *
     * @param view
     */
    public void updateStartTime(View view) {
        new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (year + monthOfYear + dayOfMonth < endYear + endMonth + endDay) {
                            startYear = year;
                            startMonth = monthOfYear;
                            startDay = dayOfMonth;
                            startDate = Utils.getStringDate(startYear, startMonth, startDay);
                            startDateTv.setText(startDate);
                        }
                    }
                }, startYear, startMonth, startDay).show();
    }

    /**
     * 修改截止时间
     *
     * @param view
     */
    public void updateEndTime(View view) {
        new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endYear = year;
                        endMonth = monthOfYear;
                        endDay = dayOfMonth;
                        endDate = Utils.getStringDate(endYear, endMonth, endDay);
                        endDateTv.setText(endDate);
                    }
                }, endYear, endMonth, endDay).show();
    }
}
