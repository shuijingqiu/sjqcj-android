package com.example.sjqcjstock.Activity.Article;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.FeedListAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.ACache;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.Article.FeedListEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 内参精选 我的收藏 内参订阅 我的关注 我的微博
 */
public class ReferenceActivity extends Activity {

    private FeedListAdapter feedListAdapter;
    // 定义于数据库同步的字段集合
    private ArrayList<FeedListEntity> feedListEntityList;
    /**
     * 普通帖集合
     */
    private ListView mycellectlistview;
    // 表题
    private TextView titleTv;
    // 加载更多
    // 访问页数控制
    private int current = 1;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 网络请求提示
    private CustomProgress dialog;
    // 请求类型 hotpay(内参精选) collection(我的收藏) subscribe(内参订阅) following（我的关注） myfeed(我的微博)
    private String type;
    // 返回微博列表数据
    private String feedStr = "";
    // 缓存用的类
    private ACache mCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mycellect_list);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
        // 加载请求数据
        geneItems();
    }

    private void initView() {
        // 缓存类
        mCache = ACache.get(this);
        type = getIntent().getStringExtra("type");
        dialog = new CustomProgress(this);
        dialog.showDialog();
        findViewById(R.id.goback1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /** 普通帖集合 */
        mycellectlistview = (ListView) findViewById(R.id.mycellectlist2);
        // 存储数据的数组列表
        feedListEntityList = new ArrayList<FeedListEntity>();
        // 为ListView 添加适配器
        feedListAdapter = new FeedListAdapter(ReferenceActivity.this, type);
        mycellectlistview.setAdapter(feedListAdapter);
        mycellectlistview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (feedListEntityList.size() < 1) {
                    return;
                }
                Intent intent = new Intent(ReferenceActivity.this,
                        ArticleDetailsActivity.class);
                intent.putExtra("weibo_id", feedListEntityList.get(position).getFeed_id());
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
                //清空列表重载数据
                feedListEntityList.clear();
                current = 1;
                // 加载请求数据
                geneItems();
            }

            // 下拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                current++;
                // 加载请求数据
                geneItems();
            }
        });
        // 先加载缓存
        String feedACache = mCache.getAsString(type + "mh");
        Log.e("mhhc", feedACache + "-");
        if (feedACache != null && !"".equals(feedACache)) {
            setData(feedACache);
        }
        titleTv = (TextView) findViewById(R.id.cellect_title_tv);
        if ("hotpay".equals(type)) {
            titleTv.setText("内参精选");
        } else if ("subscribe".equals(type)) {
            titleTv.setText("内参订阅");
        } else if ("following".equals(type)) {
            titleTv.setText("我的关注");
        } else if ("myfeed".equals(type)) {
            titleTv.setText("我的微博");
        }
    }

    private void geneItems() {
        if ("hotpay".equals(type)) {
            // 内参精选
            new Thread(new Runnable() {
                @Override
                public void run() {
                    feedStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/feed/getList?type=" + type + "&mid=" + Constants.staticmyuidstr + "&p=" + current);
                    handler.sendEmptyMessage(0);
                }
            }).start();
        } else {
            // 获取微博列表
            new Thread(new Runnable() {
                @Override
                public void run() {
                    feedStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/feed/myList?type=" + type + "&mid=" + Constants.staticmyuidstr + "&token=" + Constants.apptoken + "&p=" + current);
                    handler.sendEmptyMessage(0);
                }
            }).start();
        }
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
                    setData(feedStr);
                    break;
            }
        }
    };

    /**
     * 解析绑定数据
     */
    private void setData(String data) {
        try {
            dialog.dismissDlog();
            JSONObject jsonObject = new JSONObject(data);
            if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                // 请求失败的情况
                CustomToast.makeText(ReferenceActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                // 千万别忘了告诉控件刷新完毕了哦！失败
                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                return;
            }
            // 千万别忘了告诉控件刷新完毕了哦！
            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
            ArrayList<FeedListEntity> feedListEntitys = (ArrayList<FeedListEntity>) JSON.parseArray(jsonObject.getString("data"), FeedListEntity.class);
            if (current == 1) {
                // 做缓存
                mCache.put(type + "mh", data);
                feedListEntityList = feedListEntitys;
            } else {
                feedListEntityList.addAll(feedListEntitys);
            }
            feedListAdapter.setlistData(feedListEntityList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
