package com.example.sjqcjstock.fragment;

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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.stocks.ExpertListsAdapter;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.stocks.TotalProfitEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 必赚牛人,总收益榜的Fragment
 * Created by Administrator on 2016/5/31.
 */
public class FragmentMustEarnCattle extends Fragment {

    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private ListView listView;
    // 显示加载排行榜的Adapter
    private ExpertListsAdapter expertListsAdapter;
    // 需要加载的数据集合
    private ArrayList<TotalProfitEntity> totalProfitList;
    // 排行接口状态
    private String condition = "";
    // 调用接口返回的数据
    private String resstr;
    // 分页的页码
    private int page = 1;
    // 页面显示类型( 2:总收益榜 3:选股牛人)
    private int type = 2;
    // 网络请求提示
    private CustomProgress dialog;

    public FragmentMustEarnCattle(){}
    public FragmentMustEarnCattle(int type){
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_must_earn_cattle, container, false);
        findView(view);
        initData();
        return view;
    }

    /**
     * 控件的绑定
     */
    private void findView(View view) {
        dialog = new CustomProgress(getActivity());
        dialog.showDialog();
        expertListsAdapter = new ExpertListsAdapter(getActivity(),type);
        listView = (ListView) view.findViewById(
                R.id.list_view);
        listView.setAdapter(expertListsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(getActivity(),
                        UserDetailNewActivity.class);
                intent.putExtra("uid", totalProfitList.get(arg2).getUid());
                intent.putExtra("type","1");
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
                initData();
            }

            // 下拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                page += 1;
                initData();
            }
        });
        if (type == 2){
            condition = "total_rate";
        }else{
            condition = "success_rate";
        }
    }

    /**
     * 数据的加载
     */
    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用接口获取排行榜信息
                resstr = HttpUtil.restHttpGet(Constants.moUrl+"/rank/getRankList&token="+Constants.apptoken+"&condition=" + condition + "&p="+page+"&uid="+Constants.staticmyuidstr);
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
                        dialog.dismissDlog();
                        if("".equals(resstr)){
                            CustomToast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                            return;
                        }
                        JSONObject jsonObject = new JSONObject(resstr);
                        if ("failed".equals(jsonObject.getString("status"))) {
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            return;
                        }
                        // 持仓的数据
                        ArrayList<TotalProfitEntity> totalProfit = (ArrayList<TotalProfitEntity>) JSON.parseArray(jsonObject.getString("data"),TotalProfitEntity.class);
                        if(page == 1){
                            totalProfitList = totalProfit;
                        }else{
                            totalProfitList.addAll(totalProfit);
                        }
                        expertListsAdapter.setlistData(totalProfitList);
                        // 千万别忘了告诉控件刷新完毕了哦！
                        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    } catch (JSONException e) {
                        // 千万别忘了告诉控件刷新完毕了哦！
                        ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                        e.printStackTrace();
                    }
            }
        }
    };
}