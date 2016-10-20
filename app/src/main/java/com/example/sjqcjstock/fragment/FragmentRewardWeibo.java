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
import com.example.sjqcjstock.adapter.myattentioncommonnoteAdapter;
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
 * 打赏微博的Fragment
 * Created by Administrator on 2016/6/13.
 */
public class FragmentRewardWeibo extends Fragment {

    // 控制器
    private myattentioncommonnoteAdapter commonnoteAdapter;
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
    private ArrayList<HashMap<String, String>> subscribeListS;
    // 刷新列表标识
    private String isreferlist = "1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 缓存类
        mCache = ACache.get(getActivity());
        View view = inflater.inflate(R.layout.fragment_reward_weibo, container, false);
        findView(view);
        // 自动下拉刷新
        ptrl.autoRefresh();
        return view;
    }

    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            // 自动下拉刷新
            ptrl.autoRefresh();
        } else {
            if (subscribeListS != null && subscribeListS.size() > 0) {
                // 做缓存
                mCache.put("SubscribeListx", Utils.getListMapStr(subscribeListS));
            }
        }
    }

    /**
     * 页面控件的加载
     */
    private void findView(View view) {
        listessenceData = new ArrayList<HashMap<String, String>>();
        commonnoteAdapter = new myattentioncommonnoteAdapter(getActivity());
        listView = (ListView) view.findViewById(
                R.id.reward_weibo_list_view);
        listView.setAdapter(commonnoteAdapter);

        String str = mCache.getAsString("SubscribeListx");
        listessenceData = Utils.getListMap(str);
        commonnoteAdapter.setlistData(listessenceData);

        ptrl = ((PullToRefreshLayout) view.findViewById(
                R.id.refresh_view));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                try {
                    Intent intent = new Intent(getActivity(), forumnotedetailActivity.class);
                    // 传递发布微博的信息
                    intent.putExtra("weibo_id", listessenceData.get(arg2).get("feed_id").toString());
                    intent.putExtra("uid", listessenceData.get(arg2).get("uid").toString());
                    // 传递转发微博的信息
                    if ("repost".equals(listessenceData.get(arg2).get("type").toString())) {
                        intent.putExtra("sourceweibo_id", listessenceData.get(arg2).get("source_feed_idstr").toString());
                        intent.putExtra("sourceuid", listessenceData.get(arg2).get("sourceuidstr").toString());
                        // 转发类型
                        intent.putExtra("type", listessenceData.get(arg2).get("type").toString());
                    }
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
                isreferlist = "1";
                current = 1;
                getData();
            }

            // 下拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                current++;
                isreferlist = "0";
                getData();
            }
        });
    }

    /**
     * 数据的获取
     */

    //获取打赏微博的列表项
    private void getData() {
        new SendInfoTaskmyattentionweibolist().execute(new TaskParams(
                Constants.subscribeListUrl,
                new String[]{"mid", Constants.staticmyuidstr},
                new String[]{"p", String.valueOf(current)}
        ));
    }

    private class SendInfoTaskmyattentionweibolist extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            //调用共通方法获取网络数据
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            String digg_countstr = "";
            String comment_countstr = "";
            String repost_countstr = "";
            String isdigg = "0";
            String feed_idstr = "";
            String contentstr = "";
            // 打赏的水晶币
            String reward = "";
            // 是否是付费文章
            Object state = null;
            // 概要
            String introduction = "";
            String avatar_middlestr = "";
            String user_infostr = "";

            if (result == null || "0".equals("")) {
                CustomToast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
                // 千万别忘了告诉控件刷新完毕了哦！
                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
            } else {
                List<Map<String, Object>> attentiongbqblists = null;
                String attentiondatastr = null;
                List<Map<String, Object>> attentiondatastrlists = null;

                super.onPostExecute(result);
                if ("1".equals(isreferlist)) {
                    listessenceData.clear();
                    // 如果是刷新就回到顶部
                    listView.smoothScrollToPosition(0);
                }
                result = "[" + result + "]";
                if (attentiongbqblists == null) {
                    attentiongbqblists = JsonTools.listKeyMaps(result);
                }

                for (Map<String, Object> map : attentiongbqblists) {
                    String diggArrstr;
                    if (map.get("diggArr") == null) {
                        diggArrstr = "";
                    } else {
                        diggArrstr = map.get("diggArr").toString();

                    }
                    if (attentiondatastr == null) {
                        attentiondatastr = map.get("data").toString();
                        attentiondatastrlists = JsonTools.listKeyMaps(attentiondatastr);
                    }
                    for (Map<String, Object> datastrmap : attentiondatastrlists) {
                        HashMap<String, String> map2 = new HashMap<String, String>();
                        // 设置水晶币有多少个
                        reward = datastrmap.get("reward").toString();
                        state = datastrmap.get("state").toString();
                        Object introd = datastrmap.get("introduction");
                        if (introd != null && !"null".equals(introd.toString())) {
                            introduction = introd.toString();
                        }

                        feed_idstr = datastrmap.get("feed_id").toString();
                        isdigg = "0";
                        if (diggArrstr.contains(feed_idstr)) {
                            isdigg = "1";
                        }
                        String typestr = datastrmap.get("type").toString();

                        // String bodystr= datastrmap.get("body").toString();
                        contentstr = datastrmap.get("body").toString();
                        // 获取标题
                        String feed_datastr =
                                datastrmap.get("feed_data").toString();


                        contentstr = contentstr.replace(">【", "><font color=\"#4471BC\" >【");
                        contentstr = contentstr.replace("】<", "】</font><Br/><");
                        contentstr = contentstr.replace("atarget", "a ");
                        contentstr = contentstr.replace("target", "");
                        contentstr = contentstr.replace("</a>", "&nbsp</a>");

                        // 正则表达式处理 去Html代码
                        String regex = "\\<[^\\>]+\\>";
                        contentstr = contentstr.replace("\t", "");
                        contentstr = contentstr.replace("\n", "");
                        contentstr = contentstr.replace("\r", "");

                        contentstr = contentstr.replace("&nbsp;", "");
                        contentstr = contentstr.replace("	", "");
                        contentstr = contentstr.replace("　　", "");
                        contentstr = contentstr.replace("[", "<img src=\"");
                        contentstr = contentstr.replace("]", "\"   >");

                        contentstr = contentstr.replace("<imgsrc='__THEME__/image/expression/miniblog/", "<img src=\"");
                        contentstr = contentstr.replace(".gif'", "\"");
                        contentstr = contentstr.replace("imgsrc", "img src");


                        map2.put("type", typestr);

                        String publish_timestr = datastrmap.get("publish_time").toString();
                        if (datastrmap.get("digg_count") == null) {
                            digg_countstr = "0";
                        } else {
                            digg_countstr = datastrmap.get("digg_count").toString();
                        }
                        if (datastrmap.get("comment_count") == null) {
                            comment_countstr = "0";
                        } else {
                            comment_countstr = datastrmap.get("comment_count").toString();
                        }
                        if (datastrmap.get("repost_count") == null) {
                            repost_countstr = "0";
                        } else {
                            repost_countstr = datastrmap.get("repost_count").toString();
                        }
                        if (contentstr.contains("feed_img_lists")) {
                            contentstr = contentstr.substring(0, contentstr.indexOf("feed_img_lists"));
                        }

                        String attach_urlstr;
                        if (datastrmap.get("attach_url") == null) {
                            attach_urlstr = "";
                        } else {
                            attach_urlstr = datastrmap.get("attach_url")
                                    .toString();
                            // 解析短微博图片地址
                            attach_urlstr = attach_urlstr.substring(1, attach_urlstr.length() - 1);
                        }

                        publish_timestr = CalendarUtil.formatDateTime(Utils
                                .getStringtoDate(publish_timestr));
                        if ("".equals(contentstr)) {
                            contentstr = "//";
                        }

                        // 如果是付费文章就有概要 显示内容为标题+概要
                        if (!"".equals(introduction)) {
                            contentstr = "<font color=\"#4471BC\" >" + contentstr.substring(contentstr.indexOf("【"), contentstr.indexOf("】") + 1) + "</font><Br/>摘要：" + introduction;
                        }
                        map2.put("feed_id", feed_idstr);
                        map2.put("content", contentstr);
                        map2.put("create", publish_timestr);
                        map2.put("digg_count", digg_countstr);
                        map2.put("comment_count", comment_countstr);
                        map2.put("repost_count", repost_countstr);
                        map2.put("isdigg", isdigg);
                        // 保存水晶币到map
                        map2.put("reward", reward);
                        if (state != null) {
                            // 保存是否是付费文章
                            map2.put("state", state.toString());
                        }
                        // 保存概要
                        map2.put("introduction", introduction);
                        // 保存标题
                        map2.put("feed_datastr", feed_datastr);

                        map2.put("attach_url", attach_urlstr);
                        if (datastrmap.get("user_info") == null) {
                            user_infostr = "";
                        } else {
                            user_infostr = datastrmap.get("user_info")
                                    .toString();
                        }
                        List<Map<String, Object>> user_infostrlists = JsonTools
                                .listKeyMaps("[" + user_infostr + "]");

                        for (Map<String, Object> user_infostrmap : user_infostrlists) {
                            String userGroup = user_infostrmap.get("user_group").toString();
                            map2.put("isVip", userGroup);
                            map2.put("uid", user_infostrmap.get("uid").toString());
                            map2.put("uname", user_infostrmap.get("uname").toString());
                            map2.put("avatar_middle", user_infostrmap.get("avatar_middle").toString());
                        }
                        listessenceData.add(map2);
                    }
                }
                commonnoteAdapter.setlistData(listessenceData);
                // 千万别忘了告诉控件刷新完毕了哦！
                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                if ("1".equals(isreferlist)) {
                    subscribeListS = (ArrayList<HashMap<String, String>>) listessenceData.clone();
                }
            }
        }
    }
}