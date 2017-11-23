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
import com.example.sjqcjstock.adapter.essenceAdapter;
import com.example.sjqcjstock.constant.ACache;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.Article.RaceReportEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 焦点的Fragment
 * Created by Administrator on 2016/5/31.
 */
public class FragmentEssenceJd extends Fragment {

    //定义List集合容器
    private essenceAdapter essencelistAdapter;
    //定义于数据库同步的字段集合
    private ArrayList<RaceReportEntity> listessenceData;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private ListView listView;
    //访问页数控制
    private int current = 1;
    // 缓存用的类
    private ACache mCache;
    // 接口返回数据
    private String jsonStr;

    public FragmentEssenceJd(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_essence_jd, container, false);
        findView(view);
        getData();
        return view;
    }

    /**
     * 控件的加载和初始化
     *
     * @param view
     */
    private void findView(View view) {
        // 缓存类
        mCache = ACache.get(getActivity());
        essencelistAdapter = new essenceAdapter(getActivity());
        listView = (ListView) view.findViewById(
                R.id.jd_list_view);
        listView.setAdapter(essencelistAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (listessenceData.size()<1){
                    return;
                }
                try {
                    Intent intent = new Intent(getActivity(), ArticleDetailsActivity.class);
                    intent.putExtra("weibo_id",listessenceData.get(arg2).getFeed_id());
//                    intent.putExtra("uid", (String) listessenceData.get(arg2).get("uidstr"));
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
                current = 1;
                getData();
            }

            // 下拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                current++;
                getData();
            }
        });
        String dataStr = mCache.getAsString("appindexjd");
        if (dataStr != null && !"".equals(dataStr)){
            setData(dataStr);
        }
    }

    /**
     * 获取网络数据
     */
    private void getData() {
        // 获取焦点数据列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/feed/recommend?position=1&p="+current);
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
                    setData(jsonStr);
                    break;
            }
        }
    };

    /**
     * 解析绑定数据
     */
    private void setData(String data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                // 请求失败的情况
                CustomToast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                // 千万别忘了告诉控件刷新完毕了哦！
                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                return;
            }
            ArrayList<RaceReportEntity> raceReportEntities = (ArrayList<RaceReportEntity>) JSON.parseArray(jsonObject.getString("data"),RaceReportEntity.class);
            if (1 == current){
                listessenceData = raceReportEntities;
                // 做缓存
                mCache.put("appindexjd", data);
            }else{
                listessenceData.addAll(raceReportEntities);
            }
            essencelistAdapter.setlistData(listessenceData);

        } catch (JSONException e) {
            e.printStackTrace();
            // 千万别忘了告诉控件刷新完毕了哦！
            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
        }
        // 千万别忘了告诉控件刷新完毕了哦！
        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
    }
}