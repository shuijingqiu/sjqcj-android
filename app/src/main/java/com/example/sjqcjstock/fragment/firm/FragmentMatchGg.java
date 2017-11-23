package com.example.sjqcjstock.fragment.firm;

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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.Activity.stocks.SharesDetailedActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.firm.FirmHallGgAdapter;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.firm.FirmCodeEntity;
import com.example.sjqcjstock.netutil.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 个股
 * Created by Administrator on 2017/8/28.
 */
public class FragmentMatchGg extends Fragment {

    //定义List集合容器
    private FirmHallGgAdapter firmHallGgAdapter;
    //定义于数据库同步的字段集合
    private ArrayList<FirmCodeEntity> firmCodeEntities;
//    // 上下拉刷新控件
//    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private ListView listView;
//    //访问页数控制
//    private int page = 1;
    // 接口返回数据
    private String jsonStr;
    // 比赛id
    private String firmId;

    public FragmentMatchGg(){}
    public FragmentMatchGg(String firmId)
    {
        this.firmId = firmId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_gg, container, false);
        findView(view);
        getData();
        return view;
    }

    /**
     * 控件的加载和初始化
     *
     * @param view
     */
    private void findView(View view) {
        firmHallGgAdapter = new FirmHallGgAdapter(getActivity());
        listView = (ListView) view.findViewById(
                R.id.list_view);
        listView.setAdapter(firmHallGgAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                    Intent intent = new Intent(getActivity(), SharesDetailedActivity.class);
                    intent.putExtra("name",firmCodeEntities.get(arg2).getName());
                    intent.putExtra("code",firmCodeEntities.get(arg2).getCode());
                    startActivity(intent);
            }
        });

//        ptrl = ((PullToRefreshLayout) view.findViewById(
//                R.id.refresh_view));
//        // 添加上下拉刷新事件
//        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
//            // 下来刷新
//            @Override
//            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
//                page = 1;
//                getData();
//            }
//
//            // 下拉加载
//            @Override
//            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
//                page++;
//                getData();
//            }
//        });
    }

    /**
     * 获取网络数据
     */
    private void getData() {
            // 获取焦点数据列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonStr = HttpUtil.restHttpGet( Constants.newUrl + "/api/match/today?match_id="+firmId);
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
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        if (!Constants.successCode.equals(jsonObject.getString("code"))){
                            Toast.makeText(getActivity(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
//                            // 千万别忘了告诉控件刷新完毕了哦！
//                            ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                            return;
                        }
                        firmCodeEntities = (ArrayList<FirmCodeEntity>) JSON.parseArray(jsonObject.getString("data"),FirmCodeEntity.class);
//                        if(page == 1){
//                            listFirmDetailEntity = list;
//                        }else{
//                            listFirmDetailEntity.addAll(list);
//                        }
                        firmHallGgAdapter.setlistData(firmCodeEntities);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    // 千万别忘了告诉控件刷新完毕了哦！
//                    ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    break;
            }
        }
    };

}
