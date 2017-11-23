package com.example.sjqcjstock.Activity.stocks;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.stocks.DealAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.stocks.Order;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 当日成交信息查询页面
 * Created by Administrator on 2016/8/11.
 */
public class DayDealActivity extends Activity {

    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private ListView listView;
    // 显示加载的Adapter
    private DealAdapter dealAdapter;
    // 需要加载的数据
    private ArrayList<Order> orderArrayList;
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
        setContentView(R.layout.activity_day_deal);
        ExitApplication.getInstance().addActivity(this);
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
     * 获取当日成交信息
     */
    private void getData(){
        // 开线程获历史交易信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用接口获取股票当前行情数据
                resstr = HttpUtil.restHttpGet(Constants.moUrl+"/orders/"+Constants.staticmyuidstr+"&token="+Constants.apptoken+"&type=deal&p="+page+"&uid="+Constants.staticmyuidstr);
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
//                            Toast.makeText(getApplicationContext(), "暂无数据", Toast.LENGTH_SHORT).show();
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
}
