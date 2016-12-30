package com.example.sjqcjstock.fragment.stocks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.Activity.stocks.SharesDetailedActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.stocks.DynamicExpertAdapter;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.stocks.GeniusEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 牛人动态列表
 * Created by Administrator on 2016/12/30.
 */
public class FragmentDynamicExpert extends Fragment {

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
    // 需要加载的数据
    private ArrayList<GeniusEntity> geniusList;

    public FragmentDynamicExpert (){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_dynamic_expert, container, false);
        findView(view);
        getData();
        return view;
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
    private void findView(View view) {
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(Constants.loadMessage);
        dialog.setCancelable(true);
        dialog.show();
        listView = (ListView) view.findViewById(R.id.list_view);
        listAdapter = new DynamicExpertAdapter(getActivity());
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent inten = new Intent();
                inten.putExtra("code", geniusList.get(position).getStock());
                inten.putExtra("name", geniusList.get(position).getStock_name());
                inten.setClass(getActivity(), SharesDetailedActivity.class);
                startActivity(inten);
            }
        });

        ptrl = ((PullToRefreshLayout) view.findViewById(
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
        // 开线程获牛人动态数据（全部的牛人动态信息）
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = Constants.moUrl+"/desert/getCattleTrack&token="+Constants.apptoken+"&uid="+Constants.staticmyuidstr+"&p="+page;
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
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            dialog.dismiss();
                            return;
                        }
                        geniusList = (ArrayList<GeniusEntity>) JSON.parseArray(jsonObject.getString("data"),GeniusEntity.class);
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