package com.example.sjqcjstock.fragment.qa;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.qa.QuizMyAdapter;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.qa.QuestionAnswerEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 我的提问页面
 * Created by Administrator on 2016/6/14.
 */
public class FragmentMyQuiz extends Fragment {
    // 定义List集合容器
    private QuizMyAdapter adapter;
    //定义于数据库同步的字段集合
    private ArrayList<QuestionAnswerEntity> listData;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private ListView listView;
    //访问页数控制
    private int current = 1;
    // 调用接口返回的数据
    private String resstr = "";
    // 访问接口的路径
    private String url = "";
    // 当前访问人的uid
    private String uid = "";
    // 网络请求提示
    private CustomProgress dialog;

    public FragmentMyQuiz(String uid){
        this.uid = uid;
    }
    public FragmentMyQuiz(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_quiz, container, false);
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
        dialog = new CustomProgress(getActivity());
        dialog.showDialog();
        listData = new ArrayList<QuestionAnswerEntity>();
        adapter = new QuizMyAdapter(getActivity(),uid);
        listView = (ListView) view.findViewById(
                R.id.wd_list_view);
        listView.setAdapter(adapter);
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
        url = Constants.moUrl + "/ask&uid="+ Constants.staticmyuidstr + "&token=" + Constants.apptoken + "&type=4&p=" + current;
        if(!Constants.staticmyuidstr.equals(uid)){
            url = url +"&auid="+uid;
        }
    }

    private void getData() {
        // 获取全部提问信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用接口获取直播列表
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
                        if ("failed".equals(jsonObject.getString("status"))){
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            return;
                        }
                        ArrayList<QuestionAnswerEntity> tomliveList = (ArrayList<QuestionAnswerEntity>) JSON.parseArray(jsonObject.getString("data"),QuestionAnswerEntity.class);
                        if(current == 1){
                            listData = tomliveList;
                        }else{
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
}