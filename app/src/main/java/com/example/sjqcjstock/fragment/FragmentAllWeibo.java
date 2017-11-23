package com.example.sjqcjstock.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.Activity.Article.ArticleDetailsActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.FeedListAdapter;
import com.example.sjqcjstock.adapter.topnoteAdapter;
import com.example.sjqcjstock.constant.ACache;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.Article.FeedListEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 显示全部微博的Fragment
 * Created by Administrator on 2016/6/13.
 * 2017-07-06 修改对接新接口
 */
public class FragmentAllWeibo extends Fragment {
    // 控制器
    private FeedListAdapter adapter;
    //定义于数据库同步的字段集合
//    private ArrayList<HashMap<String, String>> listessenceData;
    private ArrayList<FeedListEntity> feedListEntityList;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private ListView listView;
    //访问页数控制
    private int current = 1;
    // 缓存用的类
    private ACache mCache;
    // 缓存全部微博信息用
//    private ArrayList<HashMap<String, String>> loadMoreList;
//    // 刷新列表标识
//    private String isreferlist = "1";
    //置顶list
    private ListView topnotelistview;
    private topnoteAdapter topnoteAdapter;
    //置顶贴数据集
    private ArrayList<HashMap<String, Object>> listtopnoteData;
    // 文章类型(all 付费文章 pay 打赏文章)
    private String type = "all";
    // 返回微博列表数据
    private String feedStr;
    // 返回置顶微博列表数据
    private String feedTopStr;
    // 需要缓存的数据
    private String feedCache = "";

    public FragmentAllWeibo() {
    }

    public FragmentAllWeibo(String type) {
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 缓存类
        mCache = ACache.get(getActivity());
        View view = inflater.inflate(R.layout.fragment_all_weibo, container, false);
        findView(view);
        getData();
        return view;
    }

    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            if (listtopnoteData != null){
                listtopnoteData.clear();
            }
            current = 1;
//            isreferlist = "1";
            getData();
        } else {
            if (!"".equals(feedCache)) {
                // 做缓存
                mCache.put("loadMorex"+type, feedCache);
            }
        }
    }

    /**
     * 页面控件的加载
     */
    private void findView(View view) {
        adapter = new FeedListAdapter(getActivity());
        listView = (ListView) view.findViewById(
                R.id.all_weibo_list_view);
        listView.setAdapter(adapter);
        ptrl = ((PullToRefreshLayout) view.findViewById(
                R.id.refresh_view));
        /**
         * 置顶帖列表，相关处理
         */
        topnotelistview = (ListView) view.findViewById(R.id.topnotelist1);
        listtopnoteData = new ArrayList<HashMap<String, Object>>();
        topnoteAdapter = new topnoteAdapter(getActivity().getApplicationContext(), listtopnoteData);
        topnotelistview.setAdapter(topnoteAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Log.e("mhcs5","-"+arg2);
                Log.e("mhcs6","-"+feedListEntityList.size());
                if (feedListEntityList.size() < 1) {
                    return;
                }
                try {
                    String content = feedListEntityList.get(arg2).getFeed_content();
                    if (content.length() > 3 && Constants.microBlogShare.equals(content.substring(0, 4))) {
                        return;
                    }
                    Intent intent = new Intent(getActivity(), ArticleDetailsActivity.class);
                    // 传递发布微博的信息
                    intent.putExtra("weibo_id", feedListEntityList.get(arg2).getFeed_id().toString());
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        String str = mCache.getAsString("loadMorex"+type);
        if (str != null && !"".equals(str)) {
            feedListEntityList = (ArrayList<FeedListEntity>) JSON.parseArray(str, FeedListEntity.class);
            adapter.setlistData(feedListEntityList);
        }

        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下来刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (feedListEntityList != null) {
                    //清空列表重载数据
                    feedListEntityList.clear();
                }
                current = 1;
                getData();
            }

            // 上拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                current++;
//                isreferlist = "0";
                getData();
            }
        });
    }

    /**
     * 数据的获取
     */
    //获取全部微博和置顶微博的列表项
    private void getData() {
        if ("all".equals(type) && (listtopnoteData == null || listtopnoteData.size()<1)) {
//            //在onCreateView()中直接直接请求数据，请求置顶列表的数据
//            new SendInfoTasktopweibolist().execute(new TaskParams(
//                    Constants.Url + "?app=public&mod=AppFeedList&act=Left_feed_top"
//            ));
            // 获取置顶微博
            new Thread(new Runnable() {
                @Override
                public void run() {
                    feedTopStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/feed/stick?1=1");
                    handler.sendEmptyMessage(1);
                }
            }).start();
        }
        // 获取微博列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                feedStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/feed/getList?type=" + type + "&mid=" + Constants.staticmyuidstr + "&uid=" + Constants.staticmyuidstr + "&p=" + current);
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
                        JSONObject jsonObject = new JSONObject(feedStr);
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            // 请求失败的情况
                            CustomToast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            // 千万别忘了告诉控件刷新完毕了哦！失败
                            ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                            feedCache = "";
                            return;
                        }
                        // 千万别忘了告诉控件刷新完毕了哦！
                        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                        feedCache = jsonObject.getString("data");
                        ArrayList<FeedListEntity> feedListEntitys = (ArrayList<FeedListEntity>) JSON.parseArray(feedCache, FeedListEntity.class);
                        if (current == 1) {
                            feedListEntityList = feedListEntitys;
                        } else {
                            feedListEntityList.addAll(feedListEntitys);
                        }
                        adapter.setlistData(feedListEntityList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // 千万别忘了告诉控件刷新完毕了哦！失败
                        ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                    }
                    break;
                case 1:
                    try {
                        JSONObject jsonTopObject = new JSONObject(feedTopStr);
                        if (!Constants.successCode.equals(jsonTopObject.getString("code"))) {
                            return;
                        }
                        HashMap<String, Object> map2 = null;
                        List<Map<String, Object>> lists = JsonTools.listKeyMaps(jsonTopObject.getString("data"));
                        String titlestr,feedId;
                        for (Map<String, Object> map : lists) {
                            map2 = new HashMap<String, Object>();
                            titlestr = map.get("title")+"";
                            feedId = map.get("feed_id")+"";
                            map2.put("title", titlestr);
                            map2.put("feed_id", feedId);
                            listtopnoteData.add(map2);
                        }
                        topnoteAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
}