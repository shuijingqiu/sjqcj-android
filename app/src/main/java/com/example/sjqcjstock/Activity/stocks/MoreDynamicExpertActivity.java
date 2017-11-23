package com.example.sjqcjstock.Activity.stocks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.stocks.DynamicExpertAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.stocks.GeniusEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 更多牛人动态
 * Created by Administrator on 2017/1/4.
 */
public class MoreDynamicExpertActivity extends Activity{


    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private ListView listView;
    // 行情List的Adapter
    private DynamicExpertAdapter listAdapter;
    // 需要加载的牛人动态数据
    private ArrayList<GeniusEntity> geniusList;
    // 网络请求提示
    private CustomProgress dialog;
    // 调用牛人动态接口返回的数据
    private String resstr = "";
    // 分页
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_more_dynamic_expert);
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
        listAdapter = new DynamicExpertAdapter(this);
        listView = (ListView) findViewById(
                R.id.list_view);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent inten = new Intent();
                inten.putExtra("code", geniusList.get(arg2).getStock());
                inten.putExtra("name", geniusList.get(arg2).getStock_name());
                inten.setClass(MoreDynamicExpertActivity.this, SharesDetailedActivity.class);
                startActivity(inten);
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
     * 更多牛人动态信息
     */
    private void getData(){
        // 开线程获牛人买卖动态信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                resstr = HttpUtil.restHttpGet(Constants.moUrl+"/orders&p="+page);
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
//                            CustomToast.makeText(getActivity(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                            geniusList = new  ArrayList<GeniusEntity>();
                            listAdapter.setlistData(geniusList);
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            dialog.dismissDlog();
                            return;
                        }
                        ArrayList<GeniusEntity> list = (ArrayList<GeniusEntity>) JSON.parseArray(jsonObject.getString("data"),GeniusEntity.class);
                        if(page == 1){
                            geniusList = list;
                        }else{
                            geniusList.addAll(list);
                        }
                        listAdapter.setlistData(geniusList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // 千万别忘了告诉控件刷新完毕了哦！
                    ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    dialog.dismissDlog();
            }
        }
    };

}
