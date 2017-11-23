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
import com.example.sjqcjstock.adapter.FeedListAdapter;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.Article.FeedListEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 浮生的Fragment
 * Created by Administrator on 2016/5/31.
 */
public class FragmentEssenceFs extends Fragment {
    // 控制器
    private FeedListAdapter adapter;
    //定义于数据库同步的字段集合
    private ArrayList<FeedListEntity> listessenceData;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private ListView listView;
    //访问页数控制
    private int current = 1;
    // 接口返回数据
    private String jsonStr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_essence_fs, container, false);
        findView(view);
        // 自动下拉刷新
        ptrl.autoRefresh();
        return view;
    }

    /**
     * 页面控件的加载
     */
    private void findView(View view) {
        listessenceData = new ArrayList<FeedListEntity>();
        adapter = new FeedListAdapter(getActivity());
        listView = (ListView) view.findViewById(
                R.id.fs_list_view);
        listView.setAdapter(adapter);
        ptrl = ((PullToRefreshLayout) view.findViewById(
                R.id.refresh_view));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                try {
                    Intent intent = new Intent(getActivity(), ArticleDetailsActivity.class);
                    // 传递发布微博的信息
                    intent.putExtra("weibo_id", listessenceData.get(arg2).getFeed_id());
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
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
     * 数据的获取
     */
    private void getData() {
//        SendInfoTaskmyweibolistloadmore task = new SendInfoTaskmyweibolistloadmore();
//        task.execute(new TaskParams(
//                Constants.Url + "?app=public&mod=FeedListMini&act=channel",
//                new String[]{"mid", Constants.staticmyuidstr},
//                new String[]{"cid", "8"},
//                new String[]{"p", String.valueOf(current)}
//        ));

        // 获取浮生的数据列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 15选股讨论区 8浮生
                jsonStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/channel/list?cid=8&mid=" + Constants.staticmyuidstr + "&p=" + current);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

//    //刷新和加载更多都是这个异步任务
//    private class SendInfoTaskmyweibolistloadmore extends AsyncTask<TaskParams, Void, String> {
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
//                // 千万别忘了告诉控件刷新完毕了哦！失败
//                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
//                return;
//            }
//            String digg_countstr = "";
//            String comment_countstr = "";
//            String repost_countstr = "";
//            String source_user_infostr = "";
//            String source_contentstr = "";
//            String source_feed_idstr = "";
//            String sourceuidstr = "";
//            String sourceunamestr = "";
//            String avatar_middlestr = "";
//            String user_infostr = "";
//            // 获取概要
//            String introduction = "";
//            // 判断是否是微博
//            Object state = null;
//            // 获取水晶币个数
//            String reward = "";
//            String datastr2 = null;
//            List<Map<String, Object>> datastrlists2 = null;
//            super.onPostExecute(result);
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//                // 获取点赞标识的数组
//                String diggArrstr;
//                if (jsonObject.getString("diggArr") == null) {
//                    diggArrstr = "";
//                } else {
//                    diggArrstr = jsonObject.getString("diggArr");
//                }
//                if (datastr2 == null) {
//                    datastr2 = jsonObject.getString("data");
//                    datastrlists2 = JsonTools.listKeyMaps(datastr2);
//                }
//                if (datastrlists2 == null) {
//                    datastrlists2 = new ArrayList<Map<String, Object>>();
//                }
//                for (Map<String, Object> datastrmap : datastrlists2) {
//                    String isdigg = "0";// isdigg为0为未点赞为1为已点赞
//                    String feed_idstr = datastrmap.get("feed_id").toString();
//                    if (diggArrstr.contains(feed_idstr)) {
//                        isdigg = "1";
//                    }
//
//                    // 微博类型
//                    String typestr = datastrmap.get("type").toString();
//                    // 获取为本类容
//                    String contentstr = "";
//                    introduction = "";
//                    state = datastrmap.get("state");
//                    // 先判断是否是付费文章如果是就获取概要
//                    if (state != null && "1".equals(state.toString())) {
//                        reward = datastrmap.get("reward").toString();
//                        Object introd = datastrmap.get("introduction");
//                        if (introd != null && !"null".equals(introd.toString())) {
//                            introduction = introd.toString();
//                        }
//                    }
//                    if (datastrmap.get("body") == null) {
//                        contentstr = "内容为空";
//                    } else {
//                        contentstr = datastrmap.get("body")+"";
//                    }
//
//                    String title = datastrmap.get("title")+"";
//                    if (!"".equals(title) && !"null".equals(title)){
//                        contentstr = "<font color=\"#4471BC\" >" + title + "</font><br/>"+contentstr;
//                    }
//                    // 正则表达式处理 去Html代码
//                    // 替换表情符的 肯定有问题 要改
//                    contentstr = contentstr.replace("[", "<img src=\"");
//                    contentstr = contentstr.replace("]", "\"/>");
//                    HashMap<String, String> map2 = new HashMap<String, String>();
//                    map2.put("type", typestr);
//                    if ("repost".equals(typestr)) {
//                        String api_sourcestr = datastrmap.get("api_source").toString();
//                        List<Map<String, Object>> api_sourcestrlists = JsonTools.listKeyMaps("[" + api_sourcestr + "]");
//                        for (Map<String, Object> api_sourcestrmap : api_sourcestrlists) {
//                            source_user_infostr = api_sourcestrmap.get("source_user_info") + "";
//                            source_contentstr = api_sourcestrmap.get("source_content") + "";
//                            source_feed_idstr = api_sourcestrmap.get("feed_id") + "";
//                            title = api_sourcestrmap.get("title")+"";
//                            if (!"".equals(title) && !"null".equals(title)){
//                                source_contentstr = "<font color=\"#4471BC\" >" + title + "</font><br/>"+source_contentstr;
//                            }
//
//                            state = api_sourcestrmap.get("state");
//                            // 先判断是否是付费文章如果是就获取概要
//                            if (state != null && "1".equals(state.toString())) {
//                                if (api_sourcestrmap.get("reward") != null) {
//                                    reward = api_sourcestrmap.get("reward").toString();
//                                }
//                                String gaiyao = api_sourcestrmap.get("zy").toString();
//                                source_contentstr = "<font color=\"#4471BC\" >" + title + "</font><Br/>" + gaiyao;
//                            }
//                            map2.put("source_contentstr", source_contentstr);
//                            map2.put("source_feed_idstr", source_feed_idstr);
//
//                            List<Map<String, Object>> source_user_infostrlists = JsonTools.listKeyMaps("[" + source_user_infostr + "]");
//                            for (Map<String, Object> source_user_infostrmap : source_user_infostrlists) {
//                                sourceuidstr = source_user_infostrmap.get("uid").toString();
//                                sourceunamestr = source_user_infostrmap.get("uname").toString();
//                                avatar_middlestr = source_user_infostrmap.get("avatar_middle").toString();
//                                String userGroup = source_user_infostrmap.get("user_group").toString();
//                                map2.put("isVipSource", userGroup);
//                                map2.put("sourceuidstr", sourceuidstr);
//                                map2.put("sourceuname", sourceunamestr);
//                                map2.put("sourceavatar_middlestr", avatar_middlestr);
//                            }
//                        }
//                    }
//
//                    String publish_timestr = datastrmap.get("publish_time").toString();
//                    if (datastrmap.get("digg_count") == null) {
//                        digg_countstr = "0";
//                    } else {
//                        digg_countstr = datastrmap.get("digg_count").toString();
//                    }
//                    if (datastrmap.get("comment_count") == null) {
//                        comment_countstr = "0";
//                    } else {
//                        comment_countstr = datastrmap.get("comment_count").toString();
//                    }
//                    if (datastrmap.get("repost_count") == null) {
//                        repost_countstr = "0";
//                    } else {
//                        repost_countstr = datastrmap.get("repost_count").toString();
//                    }
//                    publish_timestr = CalendarUtil.formatDateTime(Utils
//                            .getStringtoDate(publish_timestr));
//                    if (contentstr.contains("feed_img_lists")) {
//                        contentstr = contentstr.substring(0,
//                                contentstr.indexOf("feed_img_lists"));
//                    }
//                    String attach_urlstr;
//                    if (datastrmap.get("attach_url") == null) {
//                        attach_urlstr = "";
//                    } else {
//                        attach_urlstr = datastrmap.get("attach_url")
//                                .toString();
//                        // 解析短微博图片地址
//                        attach_urlstr = attach_urlstr.substring(1, attach_urlstr.length() - 1);
//                    }
//
//                    // 不带图的富文本
//                    if ("".equals(contentstr)) {
//                        contentstr = "//";
//                    }
//                    // 如果是付费文章就有概要 显示内容为标题+概要
//                    if (!"".equals(introduction)) {
//                        contentstr = "<font color=\"#4471BC\" >" + title + "</font><Br/>" + introduction;
//                    }
//                    if (state != null) {
//                        map2.put("state", state.toString());
//                    }
//                    map2.put("reward", reward);
//                    map2.put("feed_id", feed_idstr);
//                    map2.put("content", contentstr);
//                    map2.put("create", publish_timestr);
//                    map2.put("digg_count", digg_countstr);
//                    map2.put("comment_count", comment_countstr);
//                    map2.put("repost_count", repost_countstr);
//                    map2.put("isdigg", isdigg);
//                    map2.put("attach_url", attach_urlstr);
//                    if (datastrmap.get("user_info") == null) {
//                        user_infostr = "";
//                    } else {
//                        user_infostr = datastrmap.get("user_info")
//                                .toString();
//                    }
//                    List<Map<String, Object>> user_infostrlists = JsonTools
//                            .listKeyMaps("[" + user_infostr + "]");
//                    for (Map<String, Object> user_infostrmap : user_infostrlists) {
//                        String uidstr = user_infostrmap.get("uid").toString();
//                        String unamestr = user_infostrmap.get("uname").toString();
//                        String avatar_middlestr2 = user_infostrmap.get("avatar_middle").toString();
//                        String userGroup = user_infostrmap.get("user_group").toString();
//                        map2.put("isVip", userGroup);
//                        map2.put("uid", uidstr);
//                        map2.put("uname", unamestr);
//                        map2.put("avatar_middle", avatar_middlestr2);
//                    }
//                    listessenceData.add(map2);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            adapter.setlistData(listessenceData);
//            // 千万别忘了告诉控件刷新完毕了哦！
//            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
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
                        ArrayList<FeedListEntity> feedListEntitys = (ArrayList<FeedListEntity>) JSON.parseArray(jsonObject.getString("data"), FeedListEntity.class);
                        if (current == 1) {
                            listessenceData = feedListEntitys;
                        } else {
                            listessenceData.addAll(feedListEntitys);
                        }
                        adapter.setlistData(listessenceData);
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