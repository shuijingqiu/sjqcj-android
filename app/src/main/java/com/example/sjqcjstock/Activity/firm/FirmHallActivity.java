package com.example.sjqcjstock.Activity.firm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.firm.FirmHallAdapter;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.firm.FirmHallEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 实盘比赛大厅列表
 * Created by Administrator on 2017/8/28.
 */
public class FirmHallActivity extends Activity{

    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private ListView listView;
    // 显示加载的Adapter
    private FirmHallAdapter adapter;
    // 需要加载的数据
    private ArrayList<FirmHallEntity> firmHallList;
    // 网络请求提示
    private CustomProgress dialog;
    // 调用接口返回的数据
    private String resstr = "";
    // 分页
    private int page = 1;
    // 类型 my : 我的比赛
    private String type;
    // 请求接口路径
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firm_hall_list);
        findView();
        getData();
    }

    private void findView() {
        type = getIntent().getStringExtra("type");
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
        adapter = new FirmHallAdapter(this);
        listView = (ListView) findViewById(
                R.id.firm_hall_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FirmHallEntity firmHallEntity = firmHallList.get(position);
                Intent intent = new Intent(FirmHallActivity.this, FirmDetailsActivity.class);
                intent.putExtra("uid",firmHallEntity.getUid());
                intent.putExtra("uname",firmHallEntity.getSponsor());
                intent.putExtra("title",firmHallEntity.getTitle());
                intent.putExtra("id",firmHallEntity.getId());
                intent.putExtra("feedId",firmHallEntity.getFeed_id());
                // 比赛状态
                intent.putExtra("state",firmHallEntity.getState());
                // 是否参加比赛
                intent.putExtra("isJoin",firmHallEntity.getIs_join());
                // 剩余天数
                intent.putExtra("residue",firmHallEntity.getResidue());
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
        url = Constants.newUrl;
        if ("my".equals(type)){
            url += "/api/match/mylist?mid=" + Constants.staticmyuidstr + "&token="+Constants.apptoken+"&p="+page;
        }else{
            url += "/api/match/list?mid=" + Constants.staticmyuidstr + "&p="+page;
        }
        // 开线程获比赛信息
        new Thread(new Runnable() {
            @Override
            public void run() {
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
                        dialog.dismissDlog();
                        JSONObject jsonObject = new JSONObject(resstr);
                        if (!Constants.successCode.equals(jsonObject.getString("code"))){
                            Toast.makeText(getApplicationContext(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                            dialog.dismissDlog();
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                            return;
                        }
                        ArrayList<FirmHallEntity> list = (ArrayList<FirmHallEntity>) JSON.parseArray(jsonObject.getString("data"),FirmHallEntity.class);
                        if(page == 1){
                            firmHallList = list;
                        }else{
                            firmHallList.addAll(list);
                        }
                        adapter.setlistData(firmHallList);
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
