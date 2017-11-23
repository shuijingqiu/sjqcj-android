package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.atmeAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.ACache;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.Article.AtFeedListEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 提到我的评论
 */
public class atmeActivity extends Activity {
    private LinearLayout goback1;
    // 定义List集合容器
    private atmeAdapter adapter;
    private ListView xlistView2;
    // at我列表的数据集合
    private ArrayList<AtFeedListEntity> listatmecommentData;
    // 加载更多
    // 访问页数控制
    private int current = 1;
    // 缓存用的类
    private ACache mCache;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 接口返回数据
    private String feedStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.atmecomment_list);
        ExitApplication.getInstance().addActivity(this);
        initView();
        geneItems();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        // 缓存类
        mCache = ACache.get(this);
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        goback1.setOnClickListener(new goback1_listener());
        xlistView2 = (ListView) findViewById(R.id.listView2);
        adapter = new atmeAdapter(atmeActivity.this);
        xlistView2.setAdapter(adapter);
        ptrl = ((PullToRefreshLayout) findViewById(
                R.id.refresh_view));

        // 先加载缓存
        String feedACache = mCache.getAsString("AppMentionx");
        if (feedACache != null && !"".equals(feedACache)) {
            setData(feedACache);
        }

        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下来刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
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
    }

    class goback1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            finish();
            System.gc();
        }
    }

    private void geneItems() {
//        new SendInfoTaskatmecomment().execute(new TaskParams(
//                Constants.Url + "?app=public&mod=FeedListMini&act=mention",
//                new String[]{"mid",
//                        Constants.staticmyuidstr}, new String[]{
//                "login_password", Constants.staticpasswordstr},
//                new String[]{"p", String.valueOf(current)}));

        // 获取@提到我的列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                feedStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/feed/mention?mid=" + Constants.staticmyuidstr + "&token=" + Constants.apptoken + "&p=" + current);
                mHandler.sendEmptyMessage(0);
            }
        }).start();

    }


    public Handler mHandler = new Handler() {
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
    private void setData(String data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                // 请求失败的情况
                CustomToast.makeText(atmeActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                // 千万别忘了告诉控件刷新完毕了哦！失败
                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                return;
            }
            // 千万别忘了告诉控件刷新完毕了哦！
            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
            ArrayList<AtFeedListEntity> atFeedListEntitys = (ArrayList<AtFeedListEntity>) JSON.parseArray(jsonObject.getString("data"), AtFeedListEntity.class);
            if (current == 1){
                listatmecommentData = atFeedListEntitys;
                // 做缓存
                mCache.put("AppMentionx", data);
            }else{
                listatmecommentData.addAll(atFeedListEntitys);
            }
            adapter.setlistData(listatmecommentData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
