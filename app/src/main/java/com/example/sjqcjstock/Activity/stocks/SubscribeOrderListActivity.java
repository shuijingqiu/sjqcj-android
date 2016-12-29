package com.example.sjqcjstock.Activity.stocks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.stocks.DynamicExpertAdapter;
import com.example.sjqcjstock.adapter.stocks.DynamicOredrAdapter;
import com.example.sjqcjstock.adapter.stocks.DynamicOredrListAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.stocks.DesertEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 订阅订单列表
 * Created by Administrator on 2016/12/29.
 */
public class SubscribeOrderListActivity extends Activity{

    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    private ListView listView;
    // 订阅牛人的Adapter
    private DynamicOredrListAdapter listAdapter;
    // 数据列
    private ArrayList<DesertEntity> desertList;
    private DesertEntity desertEntity;
    // 返回接口的数据
    private String resstr;
    // 网络请求提示
    private ProgressDialog dialog;
    // 分页
    private int page = 1;
    // 查询人的uid
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_subscribe_order_list);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        findView();
        getData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null){
            dialog.dismiss();
        }
    }

    /**
     * 页面控件的绑定
     */
    private void findView() {
        uid = getIntent().getStringExtra("uid");
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialog = new ProgressDialog(this);
        dialog.setMessage(Constants.loadMessage);
        dialog.setCancelable(true);
        dialog.show();
        listView = (ListView) findViewById(R.id.list_view);
        listAdapter = new DynamicOredrListAdapter(this);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SubscribeOrderListActivity.this,DynamicOrderDetailsActivity.class);
                desertEntity = desertList.get(position);
                intent.putExtra("price_uid",desertEntity.getPrice_uid());
                intent.putExtra("price_username",desertEntity.getPrice_username());
                intent.putExtra("exp_time",desertEntity.getExp_time());
                intent.putExtra("time",desertEntity.getTime());
                intent.putExtra("order_number",desertEntity.getOrder_number());
                startActivity(intent);
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
     * 数据的获取
     */
    private void getData() {
        // 开线程获牛人动态列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                resstr = HttpUtil.restHttpGet(Constants.moUrl+"/desert/orderList&token="+Constants.apptoken+"&uid="+Constants.staticmyuidstr+"&price_uid="+uid+"&p="+page);
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
                            CustomToast.makeText(SubscribeOrderListActivity.this, jsonObject.getString("data"), Toast.LENGTH_LONG).show();
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            dialog.dismiss();
                            return;
                        }
                        String data = jsonObject.getString("data");
                        desertList = (ArrayList<DesertEntity>) JSON.parseArray(data,DesertEntity.class);
                        Log.e("mh123","123"+desertList.size());
                        if (desertList != null && desertList.size()>0) {
                            listAdapter.setlistData(desertList);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // 千万别忘了告诉控件刷新完毕了哦！
                    ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    dialog.dismiss();
                    break;
            }
        }
    };
}
