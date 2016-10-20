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
import com.example.sjqcjstock.adapter.essenceAdapter;
import com.example.sjqcjstock.constant.ACache;
import com.example.sjqcjstock.constant.Constants;
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
 * 焦点的Fragment
 * Created by Administrator on 2016/5/31.
 */
public class FragmentEssenceJd extends Fragment {

    //定义List集合容器
    private essenceAdapter essencelistAdapter;
    //定义于数据库同步的字段集合
    private ArrayList<HashMap<String, String>> listessenceData;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private ListView listView;
    //访问页数控制
    private int current = 1;
    // 缓存用的类
    private ACache mCache;
    // 缓存全部微博信息用
    private ArrayList<HashMap<String, String>> appindexListJd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_essence_jd, container, false);
        findView(view);
        // 自动下拉刷新
        ptrl.autoRefresh();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (appindexListJd != null && appindexListJd.size() > 0) {
            // 做缓存
            mCache.put("appindexjd", Utils.getListMapStr(appindexListJd));
        }
    }

    /**
     * 控件的加载和初始化
     *
     * @param view
     */
    private void findView(View view) {
        // 缓存类
        mCache = ACache.get(getActivity());
        listessenceData = new ArrayList<HashMap<String, String>>();
        essencelistAdapter = new essenceAdapter(getActivity());
        listView = (ListView) view.findViewById(
                R.id.jd_list_view);
        listView.setAdapter(essencelistAdapter);
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
        String str = mCache.getAsString("appindexjd");
        listessenceData = Utils.getListMap(str);
        essencelistAdapter.setlistData(listessenceData);

        ptrl = ((PullToRefreshLayout) view.findViewById(
                R.id.refresh_view));
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
        task.execute(new TaskParams(Constants.Url + "?app=index&mod=Index&act=Appindex",
                        new String[]{"position", "2"},
                        new String[]{"p", String.valueOf(current)}
                )
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
                        String namestr = supermanmap.get("uname").toString();
                        String weibo_titlestr = supermanmap.get("weibo_title").toString();
                        String save_pathstr = supermanmap.get("save_path").toString();
                        String save_namestr = supermanmap.get("save_name").toString();
                        String weibo_idstr = supermanmap.get("weibo_id").toString();
                        String uidstr = supermanmap.get("uid").toString();
                        String comment_countstr = supermanmap.get("comment_count").toString();
                        String imageurl = save_pathstr + save_namestr;
                        HashMap<String, String> map2 = new HashMap<String, String>();
                        String authentication = supermanmap.get("authentication") + "";
                        map2.put("isVip", authentication);
                        map2.put("weibo_titlestr", weibo_titlestr);
                        map2.put("username", namestr);
                        map2.put("image_url", "http://www.sjqcj.com/data/upload/" + imageurl);
                        map2.put("weibo_idstr", weibo_idstr);
                        map2.put("uidstr", uidstr);
                        map2.put("comment_countstr", comment_countstr);
                        listessenceData.add(map2);
                    }
                }
                essencelistAdapter.setlistData(listessenceData);
                // 千万别忘了告诉控件刷新完毕了哦！
                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                if (current == 1) {
                    appindexListJd = (ArrayList<HashMap<String, String>>) listessenceData.clone();
                }
            }
        }
    }

}