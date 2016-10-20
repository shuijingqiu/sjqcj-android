package com.example.sjqcjstock.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sjqcjstock.Activity.forumnotedetailActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.gwzbmatchreportAdapter;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.CalendarUtil;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.view.CustomToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 股王争霸赛实况报道页面
 * Created by Administrator on 2016/6/13.
 */
public class FragmentReport extends Fragment {
    // 定义List集合容器
    private gwzbmatchreportAdapter gwzbAdapter;
    // 定义于数据库同步的字段集合
    private ArrayList<HashMap<String, Object>> listgwzbmatchreportData;
    // 名人集合
    private ListView gwzbmatchreportlistview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gwzbmatchreport_list, container, false);
        findView(view);
        geneItems();
        return view;
    }

    private void findView(View view) {
        /** 股王争霸赛程报道集合 */
        gwzbmatchreportlistview = (ListView) view.findViewById(R.id.gwzbmatchreportlist2);
        // 存储数据的数组列表
        listgwzbmatchreportData = new ArrayList<HashMap<String, Object>>(200);
        // 为ListView 添加适配器
        gwzbAdapter = new gwzbmatchreportAdapter(getActivity());
        gwzbmatchreportlistview.setAdapter(gwzbAdapter);
        gwzbmatchreportlistview
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        try {
                            Intent intent = new Intent(getActivity(), forumnotedetailActivity.class);
                            intent.putExtra("weibo_id",
                                    (String) listgwzbmatchreportData.get(arg2)
                                            .get("starcraft_url"));
                            startActivity(intent);
                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        }
                    }

                });
    }

    private class SendInfoTasktodayuprankingloadmore extends
            AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                CustomToast.makeText(getActivity(), "", Toast.LENGTH_LONG)
                        .show();
            } else {
                super.onPostExecute(result);
                List<Map<String, Object>> lists2 = null;
                String datastr2 = null;
                List<Map<String, Object>> datastrlists2 = null;
                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                // 解析json字符串获得List<Map<String,Object>>
                if (lists2 == null) {
                    lists2 = JsonTools.listKeyMaps(result);
                }
                for (Map<String, Object> map : lists2) {
                    if (datastr2 == null) {
                        datastr2 = map.get("data").toString();
                        datastrlists2 = JsonTools.listKeyMaps("[" + datastr2
                                + "]");
                    }
                    for (Map<String, Object> datastrmap : datastrlists2) {

                        String starcraftstr = datastrmap.get("saicheng")
                                .toString();
                        List<Map<String, Object>> starcraftstrlists = JsonTools
                                .listKeyMaps(starcraftstr);
                        for (Map<String, Object> starcraftstrmap : starcraftstrlists) {
                            String starcraft_title = starcraftstrmap.get(
                                    "starcraft_title").toString();
                            String create_time = starcraftstrmap.get(
                                    "create_time").toString();
                            String starcraft_url = starcraftstrmap.get(
                                    "starcraft_url").toString();

                            starcraft_url = starcraft_url.substring(
                                    starcraft_url.lastIndexOf("/") + 1,
                                    starcraft_url.length());
                            create_time = CalendarUtil.formatDateTime(Utils
                                    .getStringtoDate(create_time));
                            HashMap<String, Object> map2 = new HashMap<String, Object>();
                            map2.put("starcraft_title", starcraft_title);
                            map2.put("create_time", create_time);
                            map2.put("starcraft_url", starcraft_url);

                            listgwzbmatchreportData.add(map2);
                        }
                    }
                }
                gwzbAdapter.setlistData(listgwzbmatchreportData);
            }
        }

    }

    private void geneItems() {
        new SendInfoTasktodayuprankingloadmore().execute(new TaskParams(
                        Constants.Url + "?app=public&mod=AppFeedList&act=AppStarcraft"
                )
        );
    }
}
