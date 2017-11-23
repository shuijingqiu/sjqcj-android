package com.example.sjqcjstock.Activity.firm;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.firm.FirmRecollectionsAdapter;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.firm.FirmRemarkEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ToastUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 更多历史感言
 * Created by Administrator on 2017/10/10.
 */
public class FirmRecollectionsActivity extends Activity {

    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private ListView listView;
    // 显示更多操作感言的Adapter
    private FirmRecollectionsAdapter recollectionsAdapter;
    private ArrayList<FirmRemarkEntity> firmRemarkEntitieList;
    // 网络请求提示
    private CustomProgress dialog;
    // 调用接口返回的数据
    private String resstr = "";
    // 比赛id
    private String matchId = "";
    // 页数
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firm_recollections);
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

        recollectionsAdapter = new FirmRecollectionsAdapter(this);
        listView = (ListView) findViewById(
                R.id.list_view);
        listView.setAdapter(recollectionsAdapter);

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
        // 从网络获取用户操作总结
        new Thread(new Runnable() {
            @Override
            public void run() {
                resstr = HttpUtil.restHttpGet(Constants.newUrl + "/api/match/remark?id=" + matchId + "&p="+page);
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
                        ArrayList<FirmRemarkEntity> list = (ArrayList<FirmRemarkEntity>) JSON.parseArray(jsonObject.getString("data"), FirmRemarkEntity.class);
                        if(page == 1){
                            firmRemarkEntitieList = list;
                        }else{
                            if (firmRemarkEntitieList !=null) {
                                firmRemarkEntitieList.addAll(list);
                            }
                        }

                        recollectionsAdapter.setlistData(firmRemarkEntitieList);
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
