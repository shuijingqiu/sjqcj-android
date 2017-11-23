package com.example.sjqcjstock.Activity.Tomlive;

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

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.Activity.AgreementActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.tomlive.TomliveListAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.Tomlive.TomliveRoomEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 直播人员的列表
 * Created by Administrator on 2017/1/11.
 */
public class TomlivePersonnelListActivity extends Activity {

    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private ListView listView;
    // 显示加载的Adapter
    private TomliveListAdapter adapter;
    // 需要加载的数据集合
    private ArrayList<TomliveRoomEntity> listData;
    // 网络请求提示
    private CustomProgress dialog;
    // 调用买卖接口返回的数据
    private String resstr = "";
    // 分页
    private int page = 1;
    // 查询类型(my 为我的订阅列表)
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tomlive_chat);
        ExitApplication.getInstance().addActivity(this);
        findView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    /**
     * 控件的绑定
     */
    private void findView() {
        type = getIntent().getStringExtra("type");
        if (type != null && "my".equals(type)){
            ((TextView)findViewById(R.id.title_tv)).setText("直播订阅");
        }else{

        }
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
        listData = new ArrayList<TomliveRoomEntity>();
        adapter = new TomliveListAdapter(this,type);
        listView = (ListView) findViewById(
                R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(TomlivePersonnelListActivity.this,DirectBroadcastingRoomActivity.class);
                intent.putExtra("roomId",listData.get(arg2).getId());
                intent.putExtra("uid",listData.get(arg2).getUid());
                intent.putExtra("name",listData.get(arg2).getUsername());
                intent.putExtra("remark",listData.get(arg2).getIntro());
                intent.putExtra("type",listData.get(arg2).getType());
                startActivity(intent);
                arg1.findViewById(R.id.message_count_tv).setVisibility(View.GONE);
                String liveCount = listData.get(arg2).getLive_count();
                if (liveCount == null || "".equals(liveCount)){
                    return;
                }
                int count = Constants.unreadCountInfo.getZb_count();
                count =  count - Integer.valueOf(liveCount);
                if (count<0){
                    count = 0;
                }
                Constants.unreadCountInfo.setZb_count(count);
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

    /**
     * 获取直播列表
     */
    private void getData(){
        // 获取直播列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (type != null && "my".equals(type)){
                    // 调用接口获取直播列表
                    resstr = HttpUtil.restHttpGet(Constants.moUrl + "/live/room/myDesert&uid="+ Constants.staticmyuidstr + "&token=" + Constants.apptoken + "&p=" + page);
                }else {
                    // 调用接口获取直播列表
                    resstr = HttpUtil.restHttpGet(Constants.moUrl + "/live/room" + "&p=" + page);
                }
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
                            dialog.dismissDlog();
                            if(page == 1){
                                listData = new ArrayList<TomliveRoomEntity>();
                                adapter.setlistData(listData);
                            }
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            return;
                        }
                        ArrayList<TomliveRoomEntity> tomliveList = (ArrayList<TomliveRoomEntity>) JSON.parseArray(jsonObject.getString("data"),TomliveRoomEntity.class);
                        if(page == 1){
                            listData = tomliveList;
                        }else{
                            listData.addAll(tomliveList);
                        }
                        adapter.setlistData(listData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.dismissDlog();
                    // 千万别忘了告诉控件刷新完毕了哦！
                    ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    break;
            }
        }
    };

    /**
     * 直播服务规则
     *
     * @param view
     */
    public void RuleClick(View view) {
        // 直播规则
        Intent intent = new Intent(this, AgreementActivity.class);
        intent.putExtra("type", "23");
        startActivity(intent);
    }
}
