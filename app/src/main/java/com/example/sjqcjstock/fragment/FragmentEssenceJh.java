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
import com.example.sjqcjstock.Activity.Article.ArticleDetailsActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.personalstocklistAdapter;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.Article.RaceReportEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * (大盘，个股，名家，内参，论道)的Fragment
 * Created by Administrator on 2016/5/31.
 */
public class FragmentEssenceJh extends Fragment {

    //定义List集合容器
    private personalstocklistAdapter listAdapter;
    //定义于数据库同步的字段集合
    private ArrayList<RaceReportEntity> listessenceData;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private ListView listView;
    //访问页数控制
    private int current = 1;
    // 模块的标识
    private String category = "3";
    // 接口返回数据
    private String jsonStr;

    public FragmentEssenceJh(){}

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
                    Intent intent = new Intent(getActivity(), ArticleDetailsActivity.class);
                    intent.putExtra("weibo_id", listessenceData.get(arg2).getFeed_id());
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
//        SendInfoTaskloadmore task = new SendInfoTaskloadmore();
//        task.execute(new TaskParams(Constants.Url + "?app=index&mod=Index&act=Appindex&position=2&category=" + category + "&p=" + String.valueOf(current))
//        );

        // 获取精华的一些数据列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/feed/recommend?position=2&category="+category+"&p="+current);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

//    private class SendInfoTaskloadmore extends AsyncTask<TaskParams, Void, String> {
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            if (result == null) {
//                CustomToast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
//                // 千万别忘了告诉控件刷新完毕了哦！
//                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
//            } else {
//                super.onPostExecute(result);
//                result = result.replace("\n ", "");
//                result = result.replace("\n", "");
//                result = result.replace(" ", "");
//                result = "[" + result + "]";
//                //解析json字符串获得List<Map<String,Object>>
//                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
//                for (Map<String, Object> map : lists) {
//                    String statusstr = map.get("data").toString();
//                    List<Map<String, Object>> supermanlists = JsonTools.listKeyMaps(statusstr);
//                    for (Map<String, Object> supermanmap : supermanlists) {
//                        HashMap<String, Object> map2 = new HashMap<String, Object>();
//                        String namestr = supermanmap.get("uname")+"";
//                        String weibo_titlestr = supermanmap.get("weibo_title")+"";
//                        String ctimestr = supermanmap.get("ctime")+"";
//                        String comment_countstr = supermanmap.get("comment_count")+"";
//                        String weibo_idstr = supermanmap.get("weibo_id")+"";
//                        String uidstr = supermanmap.get("uid")+"";
//                        ctimestr = CalendarUtil.formatDateTime(Utils.getStringtoDate(ctimestr));
//                        String authentication = supermanmap.get("authentication") + "";
//                        map2.put("isVip", authentication);
//                        map2.put("weibo_titlestr", weibo_titlestr);
//                        map2.put("username", namestr);
//                        map2.put("ctimestr", ctimestr);
//                        map2.put("comment_countstr", comment_countstr);
//                        map2.put("weibo_idstr", weibo_idstr);
//                        map2.put("uidstr", uidstr);
//                        listessenceData.add(map2);
//                    }
//                }
//                listAdapter.setlistData(listessenceData);
//                // 千万别忘了告诉控件刷新完毕了哦！
//                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
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

                        ArrayList<RaceReportEntity> raceReportEntities = (ArrayList<RaceReportEntity>) JSON.parseArray(jsonObject.getString("data"),RaceReportEntity.class);
                        if (1 == current){
                            listessenceData = raceReportEntities;
                        }else{
                            listessenceData.addAll(raceReportEntities);
                        }
                        listAdapter.setlistData(listessenceData);

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