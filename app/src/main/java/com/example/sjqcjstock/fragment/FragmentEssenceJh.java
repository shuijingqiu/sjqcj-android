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
import com.example.sjqcjstock.adapter.personalstocklistAdapter;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.CalendarUtil;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (大盘，个股，名家，内参，论道)的Fragment
 * Created by Administrator on 2016/5/31.
 */
public class FragmentEssenceJh extends Fragment {

    //定义List集合容器
    private personalstocklistAdapter listAdapter;
    //定义于数据库同步的字段集合
    private ArrayList<HashMap<String, Object>> listessenceData;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private ListView listView;
    //访问页数控制
    private int current = 1;
    // 模块的标识
    private String category = "3";

    public FragmentEssenceJh(String category) {
        this.category = category;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_essence_jh, container, false);
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
        listessenceData = new ArrayList<HashMap<String, Object>>();
        listAdapter = new personalstocklistAdapter(getActivity());
        listView = (ListView) view.findViewById(
                R.id.jh_list_view);
        listView.setAdapter(listAdapter);
        ptrl = ((PullToRefreshLayout) view.findViewById(
                R.id.refresh_view));
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
                } finally {
                }
            }
        });
        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下来刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                //清空列表重载数据
                listessenceData.clear();
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

    /**
     * 获取网络数据
     */
    private void getData() {
        SendInfoTaskloadmore task = new SendInfoTaskloadmore();
        task.execute(new TaskParams(Constants.Url + "?app=index&mod=Index&act=Appindex&position=2&category=" + category + "&p=" + String.valueOf(current))
        );
    }

    private class SendInfoTaskloadmore extends AsyncTask<TaskParams, Void, String> {
        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
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
                //解析json字符串获得List<Map<String,Object>>
                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
                for (Map<String, Object> map : lists) {
                    String statusstr = map.get("data").toString();
                    List<Map<String, Object>> supermanlists = JsonTools.listKeyMaps(statusstr);
                    for (Map<String, Object> supermanmap : supermanlists) {
                        HashMap<String, Object> map2 = new HashMap<String, Object>();
                        String namestr = supermanmap.get("uname").toString();
                        String weibo_titlestr = supermanmap.get("weibo_title").toString();
                        String ctimestr = supermanmap.get("ctime").toString();
                        String comment_countstr = supermanmap.get("comment_count").toString();
                        String weibo_idstr = supermanmap.get("weibo_id").toString();
                        String uidstr = supermanmap.get("uid").toString();
                        ctimestr = CalendarUtil.formatDateTime(Utils.getStringtoDate(ctimestr));
                        String authentication = supermanmap.get("authentication") + "";
                        map2.put("isVip", authentication);
                        map2.put("weibo_titlestr", weibo_titlestr);
                        map2.put("username", namestr);
                        map2.put("ctimestr", ctimestr);
                        map2.put("comment_countstr", comment_countstr);
                        map2.put("weibo_idstr", weibo_idstr);
                        map2.put("uidstr", uidstr);
                        listessenceData.add(map2);
                    }
                }
                listAdapter.setlistData(listessenceData);
                // 千万别忘了告诉控件刷新完毕了哦！
                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
            }
        }
    }
}