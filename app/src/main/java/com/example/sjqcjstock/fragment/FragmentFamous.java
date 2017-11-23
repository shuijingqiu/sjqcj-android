package com.example.sjqcjstock.fragment;

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
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.famousAdapter;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.Article.UserEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 总收益榜名人组
 * Created by Administrator on 2016/6/14.
 */
public class FragmentFamous extends Fragment {
    // 定义List集合容器
    private famousAdapter adapter;
    //定义于数据库同步的字段集合
    private ArrayList<UserEntity> listData;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private ListView listView;
    //访问页数控制
    private int current = 1;
    // 展示类容 1名家 0名博
    private String type = "0";
    // 返回接口数据
    private String jsonStr;

    public FragmentFamous() {
    }

    public FragmentFamous(String type) {
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_famous, container, false);
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
        adapter = new famousAdapter(getActivity());
        listView = (ListView) view.findViewById(
                R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),
                        UserDetailNewActivity.class);
                intent.putExtra("uid", listData.get(position).getUid());
                startActivity(intent);
            }
        });

        ptrl = ((PullToRefreshLayout) view.findViewById(
                R.id.refresh_view));
        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下来刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
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
//        new SendInfoTaskfamousranking()
//                .execute(new TaskParams(
//                                Constants.Url + "?app=public&mod=AppFeedList&act=fuser&type=" + type + "&p=" + current
//                        )
//                );

        // 获取名家名博数据列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/ad/fuser?type=" + type + "&p=" + current);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

//    private class SendInfoTaskfamousranking extends
//            AsyncTask<TaskParams, Void, String> {
//
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            if (result == null) {
//                CustomToast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
//                // 千万别忘了告诉控件刷新完毕了哦！
//                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
//            } else {
//                super.onPostExecute(result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    if ("failed".equals(jsonObject.getString("status"))) {
//                        // 千万别忘了告诉控件刷新完毕了哦！
//                        ptrl.refreshFinish(PullToRefreshLayout.FAIL);
//                        return;
//                    }
//                    List<Map<String, Object>> dataMap = JsonTools.listKeyMaps(jsonObject.getString("data"));
//                    for (Map<String, Object> datas : dataMap) {
//                        HashMap<String, Object> map2 = new HashMap<String, Object>();
//                        map2.put("uname", datas.get("uname"));
//                        map2.put("uid", datas.get("uid"));
//                        map2.put("avatar", datas.get("avatar"));
//                        map2.put("intro", datas.get("intro"));
//                        listData.add(map2);
//                    }
//                    adapter.setlistData(listData);
//                    // 千万别忘了告诉控件刷新完毕了哦！
//                    ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }


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
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            // 请求失败的情况
                            CustomToast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                            return;
                        }
                        ArrayList<UserEntity> userEntities = (ArrayList<UserEntity>) JSON.parseArray(jsonObject.getString("data"),UserEntity.class);
                        if (1 == current){
                            listData = userEntities;
                        }else{
                            listData.addAll(userEntities);
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