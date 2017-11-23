package com.example.sjqcjstock.fragment.plan;

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
import com.example.sjqcjstock.Activity.plan.PlanExhibitionActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.plan.PlanAdapter;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.plan.PlanEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 最新计划的Fragment
 * Created by Administrator on 2017/4/14.
 */
public class FragmentNewPlan extends Fragment {
    // 定义List集合容器
    private PlanAdapter adapter;
    //定义于数据库同步的字段集合
    private ArrayList<PlanEntity> listData;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private ListView listView;
    //访问页数控制
    private int current = 1;
    // 调用接口返回的数据
    private String resstr = "";
    // 1 最新发布的 2我购买的 3计划精选
    private String type = "1";
    // 网络请求提示
    private CustomProgress dialog;

    public FragmentNewPlan(){}
    public FragmentNewPlan(String type){
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_plan, container, false);
        findView(view);
        getData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 控件的加载和初始化
     *
     * @param view
     */
    private void findView(View view) {
        dialog = new CustomProgress(getActivity());
        dialog.showDialog();

        listData = new ArrayList<PlanEntity>();
        adapter = new PlanAdapter(getActivity());
        listView = (ListView) view.findViewById(
                R.id.plan_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PlanExhibitionActivity.class);
                intent.putExtra("id",listData.get(position).getId());
                intent.putExtra("uid",listData.get(position).getUid());
                getActivity().startActivity(intent);
            }
        });
        ptrl = ((PullToRefreshLayout) view.findViewById(
                R.id.refresh_view));
        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下来刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                //清空列表重载数据
                listData.clear();
                current = 1;
                getData();
            }

            // 下拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                current++;
                getData();
            }
        });
    }

    private void getData() {
        // 获取最新的计划投资信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                if ("3".equals(type)) {
                    // 计划精选
                    resstr = HttpUtil.restHttpGet(Constants.moUrl+"/recommend/dealplan?mid="+Constants.staticmyuidstr+"&p="+current);
                }
                else if ("2".equals(type)){
                    // 我购买的计划
                    resstr = HttpUtil.restHttpGet(Constants.moUrl+"/dealplan/index/myDesert?uid="+Constants.staticmyuidstr+"&token="+Constants.apptoken+"&p="+current);
                }else{
                    // 最新发布
                    resstr = HttpUtil.restHttpGet(Constants.moUrl + "/dealplan/index/list?order=time&mid="+Constants.staticmyuidstr+"&p=" + current);
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
                        dialog.dismissDlog();
                        JSONObject jsonObject = new JSONObject(resstr);
                        if ("failed".equals(jsonObject.getString("status"))) {
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            return;
                        }
                        ArrayList<PlanEntity> tomliveList = (ArrayList<PlanEntity>) JSON.parseArray(jsonObject.getString("data"), PlanEntity.class);
                        if (current == 1) {
                            listData = tomliveList;
                        } else {
                            listData.addAll(tomliveList);
                        }
                        adapter.setlistData(listData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // 千万别忘了告诉控件刷新完毕了哦！
                    ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    break;
            }
        }
    };

    /**
     *  刷新页面
     */
    public void  RefreshPage(){
            current = 1;
            getData();
    }

}
