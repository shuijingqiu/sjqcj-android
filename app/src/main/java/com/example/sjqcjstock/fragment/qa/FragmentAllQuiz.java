package com.example.sjqcjstock.fragment.qa;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.qa.QuizAdapter;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.qa.QuestionAnswerEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 全部提问页面
 * Created by Administrator on 2016/6/14.
 */
public class FragmentAllQuiz extends Fragment {
    // 定义List集合容器
    private QuizAdapter adapter;
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
    //  2全部问答 3我问 5我答
    private String type = "2";
    // 当前访问人的uid
    private String uid = "";
    // 访问接口的路径
    private String url = "";
    // 网络请求提示
    private CustomProgress dialog;

    public FragmentAllQuiz() {}
    public FragmentAllQuiz(String type, String uid) {
        this.type = type;
        this.uid = uid;
    }

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
        adapter = new QuizAdapter(getActivity(), this);
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
        url = Constants.moUrl + "/ask&uid=" + Constants.staticmyuidstr + "&token=" + Constants.apptoken + "&type=" + type;
        if (!Constants.staticmyuidstr.equals(uid)) {
            url = url + "&auid=" + uid;
        }
    }

    private void getData() {
        url = url + "&p=" + current;
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
     * 回掉付费的接口刷新页面
     *
     * @param id
     */
    public void Refresh(final String id) {
        // 调用接口去购买
        new Thread(new Runnable() {
            @Override
            public void run() {
                List dataList = new ArrayList();
                dataList.add(new BasicNameValuePair("uid", Constants.staticmyuidstr));
                dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                dataList.add(new BasicNameValuePair("id", id));
                resstr = HttpUtil.restHttpPost(Constants.moUrl + "/ask/index/watch", dataList);
                handler.sendEmptyMessage(1);
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
                        ArrayList<QuestionAnswerEntity> tomliveList = (ArrayList<QuestionAnswerEntity>) JSON.parseArray(jsonObject.getString("data"), QuestionAnswerEntity.class);
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
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(resstr);
                        Toast.makeText(getActivity(), jsonObject.getString("data"), Toast.LENGTH_SHORT);
                        if ("failed".equals(jsonObject.getString("status"))) {
                            return;
                        }
                        current = 1;
                        getData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
}