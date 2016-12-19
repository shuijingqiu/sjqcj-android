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
import com.example.sjqcjstock.adapter.stocks.PositionAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.stocks.PositionEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 历史持仓页面
 * Created by Administrator on 2016/12/19.
 */
public class HistoryPositionActivity extends Activity{

    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private ListView listView;
    // 显示加载的Adapter
    private PositionAdapter positionAdapter;
    // 需要加载的数据
    private ArrayList<PositionEntity> positionArrayList;
    // 网络请求提示
    private ProgressDialog dialog;
    // 调用接口返回的数据
    private String resstr = "";
    // 查询用户的uid
    private String uid;
    // 分页
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_history_position);
        ExitApplication.getInstance().addActivity(this);
        uid = getIntent().getStringExtra("uid");
        if (uid == null){
            uid = Constants.staticmyuidstr;
        }
        findView();
        getData();
    }

    // 页面控件的绑定
    private void findView() {
        dialog = new ProgressDialog(this);
        dialog.setMessage(Constants.loadMessage);
        dialog.setCancelable(true);
        dialog.show();
        /**
         * 返回按钮的事件绑定
         */
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        positionAdapter = new PositionAdapter(this);
        listView = (ListView) findViewById(
                R.id.position_list);
        listView.setAdapter(positionAdapter);
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
     * 获取历史交易信息
     */
    private void getData(){
        // 开线程获历史交易信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用接口获取股票当前行情数据
                resstr = HttpUtil.restHttpGet(Constants.moUrl+"/share/historicalPosition&uid="+uid+"&token="+Constants.apptoken+"&p="+page);
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
                            if (page == 1){
                                Toast.makeText(getApplicationContext(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            return;
                        }
                        ArrayList<PositionEntity> positionLists = (ArrayList<PositionEntity>) JSON.parseArray(jsonObject.getString("data"),PositionEntity.class);
                        if(page == 1){
                            positionArrayList = positionLists;
                        }else{
                            positionArrayList.addAll(positionLists);
                        }
                        positionAdapter.setlistData(positionArrayList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                    // 千万别忘了告诉控件刷新完毕了哦！
                    ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    break;
            }
        }
    };
}
