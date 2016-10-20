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
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.RewardMessageAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.RewardMessage;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.view.CustomToast;

/**
 * 文章打赏水晶币列表页面
 * Created by Administrator on 2016/5/9.
 */
public class RewardListAcitivity extends Activity {

    // 加载系统消息
    private ListView rewardListView;
    // 控制器
    private RewardMessageAdapter adapter;
    // 获取信息的实体类
    private RewardMessage rewarMessage;
    // 返回按钮
    private LinearLayout goback1;
    // 访问页数控制
    private int current = 1;
    // 微博Id
    private String feedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reward_list);
        //将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        feedId = getIntent().getStringExtra("feed_id");
        findView();
        geneItems();
    }

    /**
     * 控件的绑定
     */
    private void findView() {
        // 返回按钮
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        goback1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rewardListView = (ListView) findViewById(R.id.reward_list);
        rewardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String uId = ((TextView) view.findViewById(R.id.user_id_tv)).getText() + "";
                // 跳转到个人中心页面
                Intent intent = new Intent(RewardListAcitivity.this, UserDetailNewActivity.class);
                intent.putExtra("uid", uId);
                startActivity(intent);
            }
        });
        adapter = new RewardMessageAdapter(RewardListAcitivity.this);
        rewardListView.setAdapter(adapter);
//        ptrl = ((PullToRefreshLayout) findViewById(
//                R.id.refresh_view));
//        // 添加上下拉刷新事件
//        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
//            // 下来刷新
//            @Override
//            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
//                current = 1;
//            }
//
//            // 下拉加载
//            @Override
//            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
//                current++;
//                geneItems();
//            }
//        });
    }

    private void geneItems() {
        new SendSystemMessageTask().execute(new TaskParams(Constants.playAMan,
                new String[]{"feed_id", feedId}, new String[]{
                "p", current + ""}));
    }

    private class SendSystemMessageTask extends AsyncTask<TaskParams, Void, String> {
        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG)
                        .show();
                // 千万别忘了告诉控件刷新完毕了哦！
//                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
            } else {
                rewarMessage = JSON.parseObject(result, RewardMessage.class);
                if ("1".equals(rewarMessage.getStatus())) {
                    if (current == 1) {
                        adapter.setRewardMessage(rewarMessage);
                    } else {
                        adapter.setAddList(rewarMessage.getData());
                    }
                    // 千万别忘了告诉控件刷新完毕了哦！
//                    ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
            }
        }
    }
}
