package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.systemMessageAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.ACache;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.SystemMessage;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;


/**
 * 打赏消息展示页面
 * Created by Administrator on 2016/5/4.
 */
public class rewardMessageActivity extends Activity {

    // 加载系统消息
    private ListView messageListView;
    // 控制器
    private systemMessageAdapter adapter;
    // 获取信息的实体类
    private SystemMessage systemMessage;
    // 返回按钮
    private LinearLayout goback1;
    // 访问页数控制
    private int current = 1;
    // 缓存用的类
    private ACache mCache;
    // 缓存的信息
    private String rewardMessageStr = "";
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_system_message);
        //将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        findView();
        // 自动下拉刷新
        ptrl.autoRefresh();
    }

    /**
     * 控件的绑定
     */
    private void findView() {
        // 缓存类
        mCache = ACache.get(this);
        // 返回按钮
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        goback1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter = new systemMessageAdapter(rewardMessageActivity.this);
        messageListView = (ListView) findViewById(R.id.system_message_list);
        messageListView.setAdapter(adapter);
        messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String feedId = ((TextView) view.findViewById(R.id.feed_id_tv)).getText() + "";
                if (!"0".equals(feedId)) {
                    // 跳转到对于的微博页面
                    Intent intent = new Intent(rewardMessageActivity.this, forumnotedetailActivity.class);
                    intent.putExtra("weibo_id", feedId);
                    // 这个UID是指的什么当前用户uid还是获取下来数据的id
                    intent.putExtra("uid", Constants.staticmyuidstr);
                    startActivity(intent);
                }
            }
        });
        // 判断是否是初次加载
        String str = mCache.getAsString("RewardMessagex");
        if (str != null && !str.equals("")) {
            systemMessage = JSON.parseObject(str, SystemMessage.class);
            adapter.setSystemMessage(systemMessage);
        }
        ptrl = ((PullToRefreshLayout) findViewById(
                R.id.refresh_view));
        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下来刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                rewardMessageStr = "";
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

    @Override
    protected void onDestroy() {
        if (!rewardMessageStr.equals("")) {
            mCache.put("RewardMessagex", rewardMessageStr);
        }
        super.onDestroy();
    }

    private void geneItems() {
        new SendSystemMessageTask().execute(new TaskParams(Constants.rewardMessagedUrl,
                new String[]{"mid", Constants.staticmyuidstr}, new String[]{
                "p", current + ""}));
    }

    private class SendSystemMessageTask extends AsyncTask<TaskParams, Void, String> {
        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG)
                        .show();
                // 千万别忘了告诉控件刷新完毕了哦！
                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
            } else {
                if (rewardMessageStr.equals("")) {
                    rewardMessageStr = result;
                }
                systemMessage = JSON.parseObject(result, SystemMessage.class);
                if ("1".equals(systemMessage.getStatus())) {
                    if (current == 1) {
                        adapter.setSystemMessage(systemMessage);
                    } else {
                        adapter.setAddList(systemMessage.getData());
                    }
                }
                // 千万别忘了告诉控件刷新完毕了哦！
                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
            }
        }
    }

}
