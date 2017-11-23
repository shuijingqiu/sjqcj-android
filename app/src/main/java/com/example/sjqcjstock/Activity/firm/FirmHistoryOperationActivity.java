package com.example.sjqcjstock.Activity.firm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.Activity.Article.ArticleDetailsActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.firm.FirmOperationAdapter;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.firm.FirmTransEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ToastUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 更多历史操作
 * Created by Administrator on 2017/9/29.
 */
public class FirmHistoryOperationActivity extends Activity {

    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private ListView listView;
    // 显示最新操作的Adapter
    private FirmOperationAdapter operationAdapter;
    // 网络请求提示
    private CustomProgress dialog;
    // 调用接口返回的数据
    private String resstr = "";
    // 比赛id
    private String matchId;
    // 操作的List
    private ArrayList<FirmTransEntity> firmTransEntityList;
    // 页数
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firm_history_operation);
        findView();
        getData();
    }

    private void findView() {
        matchId = getIntent().getStringExtra("id");
        dialog = new CustomProgress(this);
        dialog.showDialog();
        /**
         * 返回按钮的事件绑定
         */
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        operationAdapter = new FirmOperationAdapter(this);
        listView = (ListView) findViewById(
                R.id.list_view);
        listView.setAdapter(operationAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FirmHistoryOperationActivity.this, ArticleDetailsActivity.class);
                intent.putExtra("weibo_id", firmTransEntityList.get(position).getFeed_id());
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
                page = 1;
                getData();
            }

            // 上拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                page += 1;
                getData();
            }
        });
    }

    private void getData() {
        // 从网络获取用户操作记录
        new Thread(new Runnable() {
            @Override
            public void run() {
                resstr = HttpUtil.restHttpGet(Constants.newUrl + "/api/match/trans?id=" + matchId + "&p="+page);
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
                    JSONObject jsonObject = new JSONObject(resstr);
                    if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                        ToastUtil.showToast(getApplicationContext(), jsonObject.getString("data"));
                        // 千万别忘了告诉控件刷新完毕了哦！
                        ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                        return;
                    }
                    ArrayList<FirmTransEntity> list = (ArrayList<FirmTransEntity>) JSON.parseArray(jsonObject.getString("data"), FirmTransEntity.class);
                    if(page == 1){
                        firmTransEntityList = list;
                    }else{
                        if (firmTransEntityList !=null) {
                            firmTransEntityList.addAll(list);
                        }
                    }
                    operationAdapter.setlistData(firmTransEntityList);
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
