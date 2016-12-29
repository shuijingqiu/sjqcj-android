package com.example.sjqcjstock.Activity.stocks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.stocks.DynamicExpertAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.stocks.GeniusEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 我的牛人动态页面
 * Created by Administrator on 2016/12/16.
 */
public class MyDynamicExpertActivity extends Activity{

    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    private ListView listView;
    // 牛人动态的Adapter
    private DynamicExpertAdapter listAdapter;
    // 返回接口的数据
    private String resstr;
    // 网络请求提示
    private ProgressDialog dialog;
    // 页数
    private int page = 0;
    // 需要查询人动态
    private String price_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_dynamic_expert);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        findView();
        getData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null){
            dialog.dismiss();
        }
    }

    /**
     * 页面控件的绑定
     */
    private void findView() {
        price_uid = getIntent().getStringExtra("price_uid");
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialog = new ProgressDialog(this);
        dialog.setMessage(Constants.loadMessage);
        dialog.setCancelable(true);
        dialog.show();
        listView = (ListView) findViewById(R.id.list_view);
        listAdapter = new DynamicExpertAdapter(this);
        listView.setAdapter(listAdapter);

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

    /**
     * 加载绑定数据
     * @return
     */
    public void getData() {
        // 开线程获牛人动态数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = Constants.moUrl+"/desert/getCattleTrack&token="+Constants.apptoken+"&uid="+Constants.staticmyuidstr+"&p="+page;
                if (price_uid != null && !"".equals(price_uid)){
                    url += "&price_uid="+price_uid;
                }
                resstr = HttpUtil.restHttpGet(url);
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
                        JSONObject jsonObject = new JSONObject(resstr);
                        if ("failed".equals(jsonObject.getString("status"))){
                            CustomToast.makeText(MyDynamicExpertActivity.this, jsonObject.getString("data"), Toast.LENGTH_LONG).show();
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            dialog.dismiss();
                            return;
                        }
                        ArrayList<GeniusEntity> geniusList = (ArrayList<GeniusEntity>) JSON.parseArray(jsonObject.getString("data"),GeniusEntity.class);
                        if (geniusList != null && geniusList.size()>0) {
                            listAdapter.setlistData(geniusList);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // 千万别忘了告诉控件刷新完毕了哦！
                    ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    dialog.dismiss();
                    break;
            }
        }
    };
}
