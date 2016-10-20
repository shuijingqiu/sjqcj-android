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
import com.example.sjqcjstock.adapter.CrystalBwaterAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.CrystalBwater;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;

/**
 * 水晶币流动页面
 * Created by Administrator on 2016/5/6.
 */
public class CrystalBwaterActivity extends Activity {

    // 加载系统消息
    private ListView crystalBwaterListView;
    // 控制器
    private CrystalBwaterAdapter adapter;
    // 获取信息的实体类
    private CrystalBwater crystalBwater;
    // 返回按钮
    private LinearLayout goback1;
    // 访问页数控制
    private int current = 1;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_crystal_bwater);
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
        // 返回按钮
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        goback1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter = new CrystalBwaterAdapter(CrystalBwaterActivity.this);
        crystalBwaterListView = (ListView) findViewById(R.id.crystal_bwater_list);
        crystalBwaterListView.setAdapter(adapter);
        crystalBwaterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String userId = ((TextView) view.findViewById(R.id.bwater_id_tv)).getText() + "";
                // 跳转到个人中心
                Intent intent = new Intent(CrystalBwaterActivity.this, UserDetailNewActivity.class);
                intent.putExtra("uid", userId);
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

    private void geneItems() {
        new SendSystemMessageTask().execute(new TaskParams(Constants.crystalBwaterUrl,
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
                crystalBwater = JSON.parseObject(result, CrystalBwater.class);
                if ("1".equals(crystalBwater.getStatus())) {
                    if (current == 1) {
                        adapter.setCrystalBwater(crystalBwater);
                    } else {
                        adapter.setAddList(crystalBwater.getMsg());
                    }
                } else {
                    CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG)
                            .show();
                    // 千万别忘了告诉控件刷新完毕了哦！
                    ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                }
                // 千万别忘了告诉控件刷新完毕了哦！
                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
            }
        }
    }
}
