package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.Activity.Article.ArticleDetailsActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.systemMessageAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.ACache;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.SystemMessage;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

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
    // 接口返回数据
    private String jsonStr;
    // 网络请求提示
    private CustomProgress dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_system_message);
        //将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        findView();
        geneItems();
    }

    /**
     * 控件的绑定
     */
    private void findView() {
        dialog = new CustomProgress(this);
        dialog.showDialog();
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
                String relateId = systemMessage.getData().get(position).getRelate_id();
                if (!"0".equals(relateId)) {
                    // 跳转到对于的微博页面
                    Intent intent = new Intent(rewardMessageActivity.this, ArticleDetailsActivity.class);
                    intent.putExtra("weibo_id", relateId);
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
        // 获取系统消息列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/message/system?mid=" + Constants.staticmyuidstr + "&token=" + Constants.apptoken + "&type=reward&p=" + current);
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
                    dialog.dismissDlog();
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            CustomToast.makeText(rewardMessageActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            // 千万别忘了告诉控件刷新完毕了哦！加载失败
                            ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                            return;
                        }
                        systemMessage = JSON.parseObject(jsonStr, SystemMessage.class);
                        if (current == 1) {
                            adapter.setSystemMessage(systemMessage);
                            rewardMessageStr = jsonStr;
                        } else {
                            adapter.setAddList(systemMessage.getData());
                        }

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
