package com.example.sjqcjstock.Activity.stocks;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.example.sjqcjstock.adapter.stocks.SimulationGameAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.stocks.MatchEntity;
import com.example.sjqcjstock.entity.stocks.StocksInfo;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 模拟比赛列表
 * Created by Administrator on 2016/8/17.
 */
public class SimulationGameActivity extends Activity {

    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private ListView listView;
    // 加载比赛列表的数据
    private ArrayList<MatchEntity> matchLists;
    // 显示加载的Adapter
    private SimulationGameAdapter sgAdapter;
    // 获取比赛返回的数据
    private String matchStr = "";
    // 分页的页码
    private int page = 1;
    // 网络请求提示
    private ProgressDialog dialog;
    // 请求数据的路径
    private String url;
    // 请求标题
    private TextView titleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_simulation_game);
        ExitApplication.getInstance().addActivity(this);
        findView();
        dialog.show();
        initData();
    }

    /**
     * 控件的绑定
     */
    private void findView() {
        dialog = new ProgressDialog(this);
        dialog.setMessage(Constants.loadMessage);
        dialog.setCancelable(true);
        dialog.show();
        titleName = (TextView) findViewById(R.id.title_name);
        if (getIntent().getStringExtra("type") != null){
            url = Constants.moUrl+"/match/index&uid="+Constants.staticmyuidstr+"&joined=1"+"&token="+Constants.apptoken;
        }else{
            url = Constants.moUrl+"/match/index&uid="+Constants.staticmyuidstr+"&token="+Constants.apptoken;
            titleName.setText("模拟比赛");
        }
        /**
         * 返回按钮的事件绑定
         */
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sgAdapter = new SimulationGameAdapter(this,this);
        listView = (ListView) findViewById(
                R.id.list_view);
        listView.setAdapter(sgAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent();
                intent.putExtra("id",matchLists.get(arg2).getId());
                intent.putExtra("name",matchLists.get(arg2).getName());
                intent.setClass(SimulationGameActivity.this, SimulationGameDetailsActivity.class);
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
     * 数据的获取
     */
    private void initData() {
        // 开线程获取比赛列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                matchStr = HttpUtil.restHttpGet(url + "&np="+page);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    /**
     * 参加比赛后刷新页面
     */
    public void refreshPage(){
        page = 1;
        initData();
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
                        if("".equals(matchStr)){
                            CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                            dialog.dismiss();
                            return;
                        }
                        JSONObject jsonObject = new JSONObject(matchStr);
                        if ("failed".equals(jsonObject.getString("status"))){
                            Toast.makeText(getApplicationContext(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                        }else{
                            ArrayList<MatchEntity> matchList = (ArrayList<MatchEntity>) JSON.parseArray(jsonObject.getString("data"),MatchEntity.class);
                            if(page == 1){
                                matchLists = matchList;
                            }else{
                                matchLists.addAll(matchList);
                            }
                            sgAdapter.setlistData(matchLists);
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                        }
                    } catch (JSONException e) {
                        // 千万别忘了告诉控件刷新完毕了哦！
                        ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                    break;
            }
        }
    };
}
