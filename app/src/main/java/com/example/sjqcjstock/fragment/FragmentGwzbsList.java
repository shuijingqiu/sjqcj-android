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
import com.example.sjqcjstock.adapter.gwzbmatchAdapter;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.Md5Util;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.view.CustomToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 股王争霸赛每日榜单Fragemtn
 * Created by Administrator on 2016/6/13.
 */
public class FragmentGwzbsList extends Fragment {
    // 定义List集合容器
    private com.example.sjqcjstock.adapter.gwzbmatchAdapter gwzbmatchAdapter;
    // 定义于数据库同步的字段集合
    private ArrayList<HashMap<String, Object>> listessenceData;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gwzb_bd, container, false);
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
        listessenceData = new ArrayList<HashMap<String, Object>>();
        gwzbmatchAdapter = new gwzbmatchAdapter(getActivity());
        listView = (ListView) view.findViewById(
                R.id.gwzb_bd_list);
        listView.setAdapter(gwzbmatchAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                try {
                    Intent intent = new Intent(getActivity(), forumnotedetailActivity.class);
                    intent.putExtra("weibo_id", (String) listessenceData.get(arg2).get("weibo_idstr"));
                    intent.putExtra("uid", (String) listessenceData.get(arg2).get("uidstr"));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取网络数据
     */
    private void getData() {
        new SendInfoTask()
                .execute(new TaskParams(
                                Constants.Url + "?app=public&mod=AppFeedList&act=AppStarcraft"
                        )
                );
    }

    private class SendInfoTask extends AsyncTask<TaskParams, Void, String> {

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
                List<Map<String, Object>> lists = null;
                String datastr = null;
                List<Map<String, Object>> datastrlists = null;
                String starcraftstr = null;
                List<Map<String, Object>> starcraftstrlists = null;

                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                // 解析json字符串获得List<Map<String,Object>>
                if (lists == null) {
                    lists = JsonTools.listKeyMaps(result);
                }
                for (Map<String, Object> map : lists) {
                    if (datastr == null) {
                        datastr = map.get("data").toString();
                        datastrlists = JsonTools.listKeyMaps("[" + datastr
                                + "]");
                    }
                    for (Map<String, Object> datastrmap : datastrlists) {
                        if (starcraftstr == null) {
                            starcraftstr = datastrmap.get("starcraft")
                                    .toString();
                            starcraftstrlists = JsonTools
                                    .listKeyMaps(starcraftstr);
                        }
                        int i = 0;
                        for (Map<String, Object> starcraftstrmap : starcraftstrlists) {
                            i++;
                            String uidstr = starcraftstrmap.get("uname")
                                    .toString();
                            String namestr = starcraftstrmap.get("name")
                                    .toString();
                            String assets_moneystr;

                            if (starcraftstrmap.get("assets_money") == null) {
                                assets_moneystr = "";
                            } else {
                                assets_moneystr = starcraftstrmap.get(
                                        "assets_money") + "";
                            }
                            String max_assetsstr = starcraftstrmap.get(
                                    "max_assets") + "";
                            // 购买条数
                            String countstr = starcraftstrmap.get("count") + "";

                            // 头像url
                            String uidmd5str = Md5Util.getMd5(uidstr);

                            String uidstrimg = Md5Util.getuidstrMd5(uidmd5str);

                            // 收益率
                            Double max_ass = Double.valueOf(max_assetsstr);
                            Double incomedou = max_ass
                                    - Double.parseDouble(String.valueOf(500000));
                            Double income = (incomedou)
                                    / Double.parseDouble(String.valueOf(500000))
                                    * 100;

                            HashMap<String, Object> map2 = new HashMap<String, Object>();
                            map2.put("uidstrimg", uidstrimg);
                            map2.put("uid", uidstr);
                            map2.put("namestr", namestr);
                            map2.put("assets_moneystr", assets_moneystr);
                            map2.put("max_assetsstr", max_assetsstr);
                            map2.put("countstr", countstr);
                            map2.put("ranking", String.valueOf(i));
                            // 取值截取小数点后两位
                            map2.put("income", String.format("%.2f", income)
                                    + "%");
                            String suggeststr;
                            if (starcraftstrmap.get("suggest") == null) {
                                suggeststr = "";
                            } else {

                                suggeststr = starcraftstrmap.get("suggest")
                                        .toString();
                            }
                            map2.put("suggeststr", suggeststr);
                            listessenceData.add(map2);
                        }
                    }
                }
                gwzbmatchAdapter.setlistData(listessenceData);
            }
        }

    }
}
