package com.example.sjqcjstock.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.matchadapter.totalrankingfamousAdapter;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.Md5Util;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 总积分榜名人组
 * Created by Administrator on 2016/6/14.
 */
public class FragmentTotalCelebrity extends Fragment {
    // 定义List集合容器
    private totalrankingfamousAdapter adapter;
    //定义于数据库同步的字段集合
    private ArrayList<HashMap<String, Object>> listData;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private ListView listView;
    //访问页数控制
    private int current = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_total_mr, container, false);
        findView(view);
        // 自动下拉刷新
        ptrl.autoRefresh();
        return view;
    }

    /**
     * 控件的加载和初始化
     *
     * @param view
     */
    private void findView(View view) {
        listData = new ArrayList<HashMap<String, Object>>();
        adapter = new totalrankingfamousAdapter(getActivity());
        listView = (ListView) view.findViewById(
                R.id.mr_list_view);
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
    }

    private void getData() {
        new SendInfoTaskfamousranking()
                .execute(new TaskParams(
                                Constants.Url + "?app=public&mod=AppFeedList&act=AppTopBallot&type=1&p=" + current
                        )
                );
    }

    private class SendInfoTaskfamousranking extends
            AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                CustomToast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
                // 千万别忘了告诉控件刷新完毕了哦！
                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
            } else {
                super.onPostExecute(result);
                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                List<Map<String, Object>> lists = null;
                String statusstr = null;
                List<Map<String, Object>> statusstrlists = null;
                String status2str = null;
                List<Map<String, Object>> statusstr2lists = null;
                // 解析json字符串获得List<Map<String,Object>>
                if (lists == null) {
                    lists = JsonTools.listKeyMaps(result);
                }
                for (Map<String, Object> map : lists) {
                    if (statusstr == null) {
                        statusstr = map.get("data").toString();
                        statusstrlists = JsonTools.listKeyMaps("[" + statusstr
                                + "]");
                    }
                    for (Map<String, Object> statusstrmap : statusstrlists) {
                        if (status2str == null) {
                            status2str = statusstrmap.get("data").toString();
                            statusstr2lists = JsonTools.listKeyMaps(status2str);
                        }
                        for (Map<String, Object> statusstrlists2map : statusstr2lists) {
                            String uidstr = statusstrlists2map.get("uid")
                                    .toString();
                            String ballot_jifenstr = statusstrlists2map.get(
                                    "ballot_jifen").toString();
                            String weeklystr = statusstrlists2map.get("weekly")
                                    .toString();
                            String unamestr = statusstrlists2map.get("uname")
                                    .toString();
                            // 取小数点后两位
                            java.text.DecimalFormat df = new java.text.DecimalFormat(
                                    "#.##");
                            HashMap<String, Object> map2 = new HashMap<String, Object>();
                            map2.put("uname", unamestr);
                            map2.put("uid", uidstr);
                            map2.put(
                                    "ballot_jifen",
                                    df.format(Double
                                            .parseDouble(ballot_jifenstr))
                                            + "%");
                            map2.put("weekly", weeklystr);
                            map2.put("uidimg", Md5Util.getuidstrMd5(Md5Util
                                    .getMd5(uidstr)));
                            listData.add(map2);
                        }
                    }
                }
                adapter.setlistData(listData);
                // 千万别忘了告诉控件刷新完毕了哦！
                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
            }
        }
    }
}