package com.example.sjqcjstock.Activity.stocks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.stocks.ExpertListsAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.stocks.TotalProfitEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 各种牛人榜单的列表
 * Created by Administrator on 2016/8/16.
 */
public class ExpertListsActivity extends Activity {

    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 标题
    private TextView topTitle;
    // 加载的ListView
    private ListView listView;
    // 显示加载排行榜的Adapter
    private ExpertListsAdapter expertListsAdapter;
    // 需要加载的数据集合
    private ArrayList<TotalProfitEntity> totalProfitList;
    // 页面显示类型(0:常胜牛人 1:人气牛人 2:总收益榜 3:选股牛人)
    private int type = 0;
    // 排行接口状态
    private String condition = "";
    // 调用接口返回的数据
    private String resstr;
    // 分页的页码
    private int page = 1;
    // 网络请求提示
    private CustomProgress dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_expert_lists);
        ExitApplication.getInstance().addActivity(this);
        type = getIntent().getIntExtra("type", 0);
        findView();
        setTitle();
        initData();
    }

    /**
     * 控件的绑定
     */
    private void findView() {
        /**
         * 返回按钮的事件绑定
         */
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialog = new CustomProgress(this);
        dialog.showDialog();

        topTitle = (TextView) findViewById(R.id.top_title_tv);
        expertListsAdapter = new ExpertListsAdapter(this,type);
        listView = (ListView) findViewById(
                R.id.list_view);
        listView.setAdapter(expertListsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(ExpertListsActivity.this,
                        UserDetailNewActivity.class);
                intent.putExtra("uid", totalProfitList.get(arg2).getUid());
                intent.putExtra("type","1");
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
                initData();
            }

            // 下拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                page += 1;
                initData();
            }
        });
    }

    /**
     * 标题的设置
     */
    private void setTitle() {
        switch (type) {
            case 0:
                topTitle.setText("稳赚牛人");
                condition = "week_avg_profit_rate";
                break;
            case 1:
                topTitle.setText("人气牛人");
                condition = "fans";
                break;
            case 2:
                topTitle.setText("总收益榜");
                condition = "total_rate";
                break;
            case 3:
                topTitle.setText("常胜牛人");
                condition = "success_rate";
                break;
        }
    }

    /**
     * 数据的加载
     */
    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用接口获取排行榜信息
                resstr = HttpUtil.restHttpGet(Constants.moUrl+"/rank/getRankList&token="+Constants.apptoken+"&condition=" + condition + "&p="+page+"&uid="+Constants.staticmyuidstr);
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
                        if("".equals(resstr)){
                            CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                            return;
                        }
                        JSONObject jsonObject = new JSONObject(resstr);
                        if ("failed".equals(jsonObject.getString("status"))) {
//                            Toast.makeText(getApplicationContext(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            dialog.dismissDlog();
                            return;
                        }
                        // 持仓的数据
                        ArrayList<TotalProfitEntity> totalProfit = (ArrayList<TotalProfitEntity>) JSON.parseArray(jsonObject.getString("data"),TotalProfitEntity.class);
                        if(page == 1){
                            totalProfitList = totalProfit;
                        }else{
                            totalProfitList.addAll(totalProfit);
                        }
                        expertListsAdapter.setlistData(totalProfitList);
                        // 千万别忘了告诉控件刷新完毕了哦！
                        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    } catch (JSONException e) {
                        // 千万别忘了告诉控件刷新完毕了哦！
                        ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                        e.printStackTrace();
                    }
                    dialog.dismissDlog();
            }
        }
    };
}
