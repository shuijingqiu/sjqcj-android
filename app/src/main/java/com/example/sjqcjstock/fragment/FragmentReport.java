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
import com.example.sjqcjstock.Activity.Article.ArticleDetailsActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.stockmatchreportAdapter;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.Article.RaceReportEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 股王争霸赛实况报道页面
 * Created by Administrator on 2016/6/13.
 */
public class FragmentReport extends Fragment {
    // 定义List集合容器
    private stockmatchreportAdapter adapter;
    // 定义于数据库同步的字段集合
    private ArrayList<RaceReportEntity> listData;
    // 名人集合
    private ListView gwzbmatchreportlistview;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 接口返回数据
    private String jsonStr;
    private int page = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gwzbmatchreport_list, container, false);
        findView(view);
        geneItems();
        return view;
    }

    private void findView(View view) {
        /** 股王争霸赛程报道集合 */
        gwzbmatchreportlistview = (ListView) view.findViewById(R.id.gwzbmatchreportlist2);
        // 为ListView 添加适配器
        adapter = new stockmatchreportAdapter(getActivity());
        gwzbmatchreportlistview.setAdapter(adapter);
        gwzbmatchreportlistview
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        try {
                            Intent intent = new Intent(getActivity(), ArticleDetailsActivity.class);
                            intent.putExtra("weibo_id",
                                    (String) listData.get(arg2)
                                            .getFeed_id());
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
                geneItems();
            }

            // 下拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                page++;
                geneItems();
            }
        });

    }

    private void geneItems() {
        // 获取股王比赛赛程报道
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/starcraft/news?&p="+page);
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
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            // 请求失败的情况
                            CustomToast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                            return;
                        }
                        ArrayList<RaceReportEntity> lists = (ArrayList<RaceReportEntity>) JSON.parseArray(jsonObject.getString("data"), RaceReportEntity.class);
                        if (page == 1){
                            listData  = lists;
                        }else{
                            listData.addAll(lists);
                        }
                        adapter.setlistData(listData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // 千万别忘了告诉控件刷新完毕了哦！
                    ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    break;
            }
        }
    };

}
