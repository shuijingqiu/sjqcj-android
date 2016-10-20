package com.example.sjqcjstock.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sjqcjstock.Activity.informdetailActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.InformAdapter;
import com.example.sjqcjstock.constant.ACache;
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
 * 财经资讯页面
 */
public class FragmentInform extends Fragment {

    // 定义List集合容器
    private InformAdapter informAdapter;
    private ArrayList<HashMap<String, String>> listinformData;
    private ListView informlistview;
    // 获取全局变量财经资讯id
    private String news_idstr;
    // 访问页数控制
    private int current = 1;
    private String isreferlist = "1";
    // 缓存用的类
    private ACache mCache;
    // 缓存质询信息用
    private ArrayList<HashMap<String, String>> appNewsList;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_informnews, null);
        // 缓存类
        mCache = ACache.get(getActivity());
        initView2(view);
        geneItems();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            geneItems();
        } else {
            if (appNewsList != null && appNewsList.size() > 0) {
                // 做缓存
                mCache.put("AppNewsx", Utils.getListMapStr(appNewsList));
            }
        }
    }

    private void initView2(View view) {
        informlistview = (ListView) view.findViewById(R.id.informnewslist2);
        // 存储数据的数组列表
        listinformData = new ArrayList<HashMap<String, String>>(200);
        ptrl = ((PullToRefreshLayout) view.findViewById(
                R.id.refresh_view));
        // 为ListView 添加适配器
        informAdapter = new InformAdapter(getActivity().getApplicationContext());
        informlistview.setAdapter(informAdapter);
        informlistview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                try {
                    Intent intent = new Intent(getActivity(),
                            informdetailActivity.class);
                    intent.putExtra("news_id", listinformData.get(arg2)
                            .get("news_idstrstr").toString());
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        String str = mCache.getAsString("AppNewsx");
        listinformData = Utils.getListMap(str);
        informAdapter.setlistData(listinformData);
        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下来刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                //清空列表重载数据
                listinformData.clear();
                current = 1;
                isreferlist = "1";
                // 加载请求数据
                geneItems();
            }

            // 下拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                isreferlist = "0";
                current++;
                // 加载请求数据
                geneItems();
            }
        });
    }

    private class SendInfoTaskloadmore extends
            AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                CustomToast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
                mHandler.sendEmptyMessage(1);
            } else {
                super.onPostExecute(result);
                if ("1".equals(isreferlist)) {
                    listinformData.clear();
                    // 如果是刷新就回到顶部
                    informlistview.smoothScrollToPosition(0);
                }
                List<Map<String, Object>> lists2 = null;
                String datastr2 = null;
                List<Map<String, Object>> datastrlists2 = null;
                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                // 解析json字符串获得List<Map<String,Object>>
                lists2 = JsonTools.listKeyMaps(result);
                for (Map<String, Object> map : lists2) {
                    if (datastr2 == null) {
                        datastr2 = map.get("data").toString();

                        datastrlists2 = JsonTools.listKeyMaps(datastr2);
                    }
                    for (Map<String, Object> data2strmap : datastrlists2) {
                        news_idstr = data2strmap.get("news_id").toString();
                        String created = "";
                        String news_titlestr = data2strmap
                                .get("news_title").toString();
                        if (data2strmap.get("created") == null) {
                            created = "1";
                        } else {
                            created = data2strmap.get("created").toString();
                        }
                        created = CalendarUtil.formatDateTime(Utils.getStringtoDate(created));
                        HashMap<String, String> map2 = new HashMap<String, String>();
                        map2.put("news_idstrstr", news_idstr);
                        map2.put("news_titlestr", news_titlestr);
                        map2.put("created", created);
                        listinformData.add(map2);
                    }
                }
                mHandler.sendEmptyMessage(0);
            }
        }
    }

    private void geneItems() {
        new SendInfoTaskloadmore().execute(new TaskParams(
                Constants.Url + "?app=public&mod=Profile&act=AppNewsZx&p="
                        + String.valueOf(current)
        ));
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                informAdapter.setlistData(listinformData);
                // 千万别忘了告诉控件刷新完毕了哦！
                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                if ("1".equals(isreferlist)) {
                    appNewsList = (ArrayList<HashMap<String, String>>) listinformData.clone();
                }
            } else {
                // 千万别忘了告诉控件刷新完毕了哦！加载失败
                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
            }
            super.handleMessage(msg);
        }
    };
}
