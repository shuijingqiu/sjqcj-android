package com.example.sjqcjstock.fragment.stocks;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.Activity.stocks.MyDynamicExpertActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.stocks.DynamicOredrAdapter;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.stocks.DesertEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 订阅牛人列表页面
 * Created by Administrator on 2016/12/26.
 */
public class FragmentDynamicOrder extends Fragment {

    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 页数
    private int page = 1;
    // 网络请求提示
    private CustomProgress dialog;
    private ListView dynamicOrderList;
    private DynamicOredrAdapter adapter;
    // 数据集
    private ArrayList<DesertEntity> desertList;
    // 接口返回的数据
    private String rest;

    public FragmentDynamicOrder() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dynamic_order, container, false);
        Constants.isDynamic = false;
        findView(view);
        getData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Constants.isDynamic) {
            getData();
            page = 1;
            if (desertList != null) {
                desertList.clear();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismissDlog();
        }
    }

    /**
     * 页面控件的绑定
     */
    private void findView(View view) {
        dialog = new CustomProgress(getActivity());
        dialog.showDialog();
        /**
         * 返回按钮的事件绑定
         */
        dynamicOrderList = (ListView) view.findViewById(R.id.dynamic_order_list);
        adapter = new DynamicOredrAdapter(getActivity(), this);
        dynamicOrderList.setAdapter(adapter);
        dynamicOrderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MyDynamicExpertActivity.class);
                intent.putExtra("price_uid", desertList.get(position).getPrice_uid());
                startActivity(intent);
            }
        });

        ptrl = ((PullToRefreshLayout) view.findViewById(
                R.id.refresh_view));
        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下来刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                page = 1;
                if (desertList != null) {
                    desertList.clear();
                }
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
     * 数据的加载
     */
    public void getData() {
        // 开线程获取订阅牛人列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                rest = HttpUtil.restHttpGet(Constants.moUrl + "/desert/getDesertList&uid=" + Constants.staticmyuidstr + "&token=" + Constants.apptoken + "&p=" + page);
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
                        JSONObject jsonObject = new JSONObject(rest);
                        if ("failed".equals(jsonObject.getString("status"))) {
                            dialog.dismissDlog();
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            return;
                        }
                        ArrayList<DesertEntity> list = (ArrayList<DesertEntity>) JSON.parseArray(jsonObject.getString("data"), DesertEntity.class);
                        if (desertList != null && desertList.size() > 0) {
                            desertList.addAll(list);
                        } else {
                            desertList = list;
                        }
                        adapter.setlistData(desertList);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // 千万别忘了告诉控件刷新完毕了哦！
                    ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    dialog.dismissDlog();
                    break;
            }
        }
    };
}
