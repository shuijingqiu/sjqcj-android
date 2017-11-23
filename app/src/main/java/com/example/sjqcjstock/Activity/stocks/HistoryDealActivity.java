package com.example.sjqcjstock.Activity.stocks;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.stocks.DealAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.stocks.Order;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 历史交易信息查询页面
 * Created by Administrator on 2016/8/11.
 */
public class HistoryDealActivity extends Activity {

    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private ListView listView;
    // 显示加载的Adapter
    private DealAdapter dealAdapter;
    // 需要加载的数据
    private ArrayList<Order> orderArrayList;
    // 起始日期，截止日期
    private TextView startDateTv, endDateTv;
    private String startDate, endDate;
    // 起始年月日
    private int startYear, startMonth, startDay;
    // 截止年月日
    private int endYear, endMonth, endDay;
    // 网络请求提示
    private CustomProgress dialog;
    // 调用买卖接口返回的数据
    private String resstr = "";
    // 分页
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_history_deal);
        ExitApplication.getInstance().addActivity(this);

        Calendar now = Calendar.getInstance();
        endYear = startYear = now.get(Calendar.YEAR);
        endMonth  = now.get(Calendar.MONTH) + 1;
        startMonth = now.get(Calendar.MONTH);
        if (startMonth == 0){
            startMonth = 12;
            startYear = startYear -1;
        }
        endDay = startDay = now.get(Calendar.DAY_OF_MONTH);
        endDate = Utils.GetSysDate("yyyy-MM-dd","",0,0,0);
        startDate = Utils.GetSysDate("yyyy-MM-dd","",0,-1,0);
        findView();
        dialog.showDialog();
        getData();
    }

    /**
     * 控件的绑定
     */
    private void findView() {
        dialog = new CustomProgress(this);
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
        startDateTv.setText(startDate);

        dealAdapter = new DealAdapter(this);
        listView = (ListView) findViewById(
                R.id.list_view);
        listView.setAdapter(dealAdapter);
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
                page = 1;
                getData();
            }

            // 上拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                page += 1;
                getData();
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
                resstr = HttpUtil.restHttpGet(Constants.moUrl+"/orders/"+Constants.staticmyuidstr+"&token="+Constants.apptoken+"&type=trans&p="+page+"&stime="+startDate+"&etime="+endDate+"&uid="+Constants.staticmyuidstr);
                handler.sendEmptyMessage(0);
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
                            dialog.dismissDlog();
                            if(page == 1){
                                orderArrayList = new ArrayList<Order>();
                                dealAdapter.setlistData(orderArrayList);
                            }
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            return;
                        }
                        ArrayList<Order> orderList = (ArrayList<Order>) JSON.parseArray(jsonObject.getString("data"),Order.class);
                        if(page == 1){
                            orderArrayList = orderList;
                        }else{
                            orderArrayList.addAll(orderList);
                        }
                        dealAdapter.setlistData(orderArrayList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.dismissDlog();
                    // 千万别忘了告诉控件刷新完毕了哦！
                    ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    break;
            }
        }
    };

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
                        if (Utils.ContrastTime(Utils.getStringDate(year, monthOfYear+1, dayOfMonth),endDate)) {
                            startYear = year;
                            startMonth = monthOfYear+1;
                            startDay = dayOfMonth;
                            startDate = Utils.getStringDate(startYear, startMonth, startDay);
                            startDateTv.setText(startDate);
                            // 重新获取数据
                            page = 1;
                            dialog.showDialog();
                            getData();
                        }
                    }
                }, startYear, startMonth-1, startDay).show();
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
                        if (Utils.ContrastTime(startDate,Utils.getStringDate(year, monthOfYear+1, dayOfMonth))) {
                            endYear = year;
                            endMonth = monthOfYear + 1;
                            endDay = dayOfMonth;
                            endDate = Utils.getStringDate(endYear, endMonth, endDay);
                            endDateTv.setText(endDate);
                            // 重新获取数据
                            page = 1;
                            dialog.showDialog();
                            getData();
                        }
                    }
                }, endYear, endMonth-1, endDay).show();
    }

}
