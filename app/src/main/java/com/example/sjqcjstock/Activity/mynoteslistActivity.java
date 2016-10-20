package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.usermycommonnoteAdapter;
import com.example.sjqcjstock.app.ExitApplication;
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
 * 我的微博页面
 */
public class mynoteslistActivity extends Activity {

    private LinearLayout goback1;
    // 定义List集合容器
    private usermycommonnoteAdapter commonnoteAdapter;
    private ArrayList<HashMap<String, String>> listusercommonnoteData;
    /**
     * 普通帖集合
     */
    private ListView commonlistview;
    // 访问页数控制
    private int current = 1;
    // 是否熟悉列表
    private String isreferlist = "1";
    // 缓存用的类
    private ACache mCache;
    // 缓存消息用
    private ArrayList<HashMap<String, String>> loadMoreMyfeedList;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;

    public void test(ArrayList<HashMap<String, String>> listData) {
        commonnoteAdapter.setlistData(listData);
    }

    public void referActivity() {
        current = 1;
        isreferlist = "1";
        // 自动下拉刷新
        ptrl.autoRefresh();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mynotes_list);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
        // 自动下拉刷新
        ptrl.autoRefresh();
    }

    private void initView() {
        // 缓存类
        mCache = ACache.get(this);
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        goback1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /** 普通帖集合 */
        commonlistview = (ListView) findViewById(R.id.mynoteslist2);
        // 存储数据的数组列表
        listusercommonnoteData = new ArrayList<HashMap<String, String>>(200);
        // 为ListView 添加适配器
        commonnoteAdapter = new usermycommonnoteAdapter(
                mynoteslistActivity.this, this);
        commonlistview.setAdapter(commonnoteAdapter);
        commonlistview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                try {
                    String content = listusercommonnoteData.get(position).get("content");
                    if (content.length() > 3 && Constants.microBlogShare.equals(content.substring(0, 4))) {
                        return;
                    }
                    Intent intent = new Intent(mynoteslistActivity.this,
                            forumnotedetailActivity.class);
                    // 传递发布微博的信息
                    intent.putExtra(
                            "weibo_id",
                            listusercommonnoteData.get(position)
                                    .get("feed_id").toString());
                    intent.putExtra("uid",
                            listusercommonnoteData.get(position).get("uid")
                                    .toString());

                    // 传递转发微博的信息
                    if ("repost".equals(listusercommonnoteData
                            .get(position).get("type").toString())) {
                        intent.putExtra(
                                "sourceweibo_id",
                                listusercommonnoteData.get(position)
                                        .get("source_feed_idstr").toString());
                        intent.putExtra("sourceuid", listusercommonnoteData
                                .get(position).get("sourceuidstr")
                                .toString());

                        // 转发类型
                        intent.putExtra(
                                "type",
                                listusercommonnoteData.get(position)
                                        .get("type").toString());
                    }
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });

        String str = mCache.getAsString("loadMoreMyfeedx");
        listusercommonnoteData = Utils.getListMap(str);
        commonnoteAdapter.setlistData(listusercommonnoteData);

        ptrl = ((PullToRefreshLayout) findViewById(
                R.id.refresh_view));
        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下来刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                //清空列表重载数据
                listusercommonnoteData.clear();
                current = 1;
                isreferlist = "1";
                // 加载请求数据
                geneItems();
            }

            // 下拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                isreferlist = "0";
                // 加载请求数据
                geneItems();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (loadMoreMyfeedList != null && loadMoreMyfeedList.size() > 0) {
            // 做缓存
            mCache.put("loadMoreMyfeedx", Utils.getListMapStr(loadMoreMyfeedList));
        }
        super.onDestroy();
    }

    private void geneItems() {
        new SendInfoTaskloadmore().execute(new TaskParams(
                Constants.Url + "?app=public&mod=FeedListMini&act=loadMore",
                new String[]{"type", "myfeed"},
                new String[]{"mid", Constants.staticmyuidstr},
                new String[]{"id", Constants.staticmyuidstr},
                new String[]{"p", String.valueOf(current)}
        ));
        current++;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                commonnoteAdapter.setlistData(listusercommonnoteData);
                // 千万别忘了告诉控件刷新完毕了哦！
                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                if ("1".equals(isreferlist)) {
                    loadMoreMyfeedList = (ArrayList<HashMap<String, String>>) listusercommonnoteData.clone();
                }
            } else {
                // 千万别忘了告诉控件刷新完毕了哦！加载失败
                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
            }
            super.handleMessage(msg);
        }
    };

    private class SendInfoTaskloadmore extends
            AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {

            if (result == null) {
                CustomToast.makeText(mynoteslistActivity.this, "", Toast.LENGTH_LONG)
                        .show();
                mHandler.sendEmptyMessage(1);
            } else {
                List<Map<String, Object>> gbqblists2 = null;
                String datastr2 = null;
                List<Map<String, Object>> datastrlists2 = null;
                if ("1".equals(isreferlist)) {
                    listusercommonnoteData.clear();
                    commonlistview.smoothScrollToPosition(0);
                }
                String regex = "\\<[^\\>]+\\>";
                super.onPostExecute(result);

                if (gbqblists2 == null) {
                    gbqblists2 = JsonTools.listKeyMaps("[" + result + "]");
                }
                for (Map<String, Object> map : gbqblists2) {
                    // 获取点赞标识的数组
                    String diggArrstr;
                    if (map.get("diggArr") == null) {
                        diggArrstr = "";
                    } else {
                        diggArrstr = map.get("diggArr") + "";
                    }

                    if (datastr2 == null) {
                        datastr2 = map.get("data") + "";
                        datastrlists2 = JsonTools.listKeyMaps(datastr2);
                    }
                    if (datastrlists2 == null) {
                        datastrlists2 = new ArrayList<Map<String, Object>>();
                    }

                    for (Map<String, Object> datastrmap : datastrlists2) {
                        String digg_countstr = "";
                        String comment_countstr = "";
                        String repost_countstr = "";

                        String isdigg = "0";// isdigg为0为未点赞为1为已点赞
                        if (datastrmap.get("feed_id") == null) {
                            mHandler.sendEmptyMessage(0);
                            return;
                        }
                        String feed_idstr = datastrmap.get("feed_id")
                                + "";
                        if (diggArrstr.contains(feed_idstr)) {
                            isdigg = "1";
                        }
                        String typestr = datastrmap.get("type") + "";
                        String contentstr = datastrmap.get("body") + "";
                        contentstr = contentstr.replace("【",
                                "<font color=\"#4471BC\" >【");
                        contentstr = contentstr.replace("】", "】</font>");
                        contentstr = contentstr.replace("atarget", "a ");
                        contentstr = contentstr.replace("target", "");
                        contentstr = contentstr.replace("</a>", "&nbsp</a>");
                        contentstr = contentstr.replace("&nbsp;", "");
                        contentstr = contentstr.replace("	", "");
                        contentstr = contentstr.replace("　　", "");
                        contentstr = contentstr.replace("fontsing1",
                                "<font color=\"#4471BC\" >【");
                        contentstr = contentstr.replace("fontsing2",
                                "】</font><Br/>");
                        contentstr = contentstr
                                .replace(
                                        "<imgsrc='__THEME__/image/expression/miniblog/",
                                        "<img src=\"");
                        contentstr = contentstr.replace(
                                "'__THEME__/image/expression/miniblog/", "\"");

                        contentstr = contentstr.replace(".gif'", "\"");
                        HashMap<String, String> map2 = new HashMap<String, String>();
                        map2.put("type", typestr);
                        if ("repost".equals(typestr)) {

                            contentstr = contentstr.substring(0, contentstr.indexOf("◆"));

                            String api_sourcestr = datastrmap.get("api_source")
                                    + "";
                            List<Map<String, Object>> api_sourcestrlists = JsonTools
                                    .listKeyMaps("[" + api_sourcestr + "]");

                            for (Map<String, Object> api_sourcestrmap : api_sourcestrlists) {
                                String source_contentstr;
                                String source_feed_idstr = api_sourcestrmap
                                        .get("feed_id") + "";
                                String source_user_infostr = api_sourcestrmap
                                        .get("source_user_info") + "";
                                if (api_sourcestrmap.get("source_content") == null) {
                                    source_contentstr = "该内容已不存在";
                                } else {
                                    source_contentstr = api_sourcestrmap.get(
                                            "source_content") + "";
                                }
                                source_contentstr = source_contentstr.replace(
                                        "<feed-titlestyle='display:none'>",
                                        "fontsing1");
                                source_contentstr = source_contentstr.replace(
                                        "<feed-title style='display:none'>",
                                        "fontsing1");

                                source_contentstr = source_contentstr.replace(
                                        "</feed-title>", "fontsing2");
                                source_contentstr = source_contentstr
                                        .replaceAll(regex, "");
                                source_contentstr = source_contentstr.replace(
                                        "fontsing1",
                                        "<font color=\"#4471BC\" >【");
                                source_contentstr = source_contentstr.replace(
                                        "fontsing2", "】</font><Br/>");
                                source_contentstr = source_contentstr.replace(
                                        "\t", "");
                                source_contentstr = source_contentstr.replace(
                                        "\n", "");

                                map2.put("source_feed_idstr", source_feed_idstr);
                                map2.put("source_contentstr", source_contentstr);

                                List<Map<String, Object>> source_user_infostrlists = JsonTools
                                        .listKeyMaps("[" + source_user_infostr
                                                + "]");
                                for (Map<String, Object> source_user_infostrmap : source_user_infostrlists) {
                                    String ctimestr;
                                    String sourceuidstr = source_user_infostrmap
                                            .get("uid") + "";
                                    String sourceunamestr = source_user_infostrmap
                                            .get("uname") + "";
                                    if (source_user_infostrmap.get("ctime") == null) {
                                        ctimestr = "";
                                    } else {
                                        ctimestr = source_user_infostrmap.get(
                                                "ctime") + "";
                                    }
                                    String avatar_middlestr = source_user_infostrmap
                                            .get("avatar_middle") + "";
                                    String userGroup = source_user_infostrmap.get("user_group") + "";
                                    map2.put("isVipSource", userGroup);
                                    map2.put("sourceuidstr", sourceuidstr);
                                    map2.put("sourceuname", sourceunamestr);
                                    map2.put("sourceuctime", ctimestr);
                                    map2.put("sourceavatar_middlestr",
                                            avatar_middlestr);
                                }
                            }
                        }
                        String publish_timestr = datastrmap.get("publish_time")
                                + "";
                        if (datastrmap.get("digg_count") == null) {
                            digg_countstr = "0";
                        } else {
                            digg_countstr = datastrmap.get("digg_count")
                                    + "";
                        }
                        if (datastrmap.get("comment_count") == null) {
                            comment_countstr = "0";
                        } else {
                            comment_countstr = datastrmap.get("comment_count")
                                    + "";
                        }
                        if (datastrmap.get("repost_count") == null) {
                            repost_countstr = "0";
                        } else {
                            repost_countstr = datastrmap.get("repost_count")
                                    + "";
                        }
                        if (contentstr.contains("feed_img_lists")) {
                            contentstr = contentstr.substring(0, contentstr.indexOf("feed_img_lists"));
                        }
                        String attach_urlstr;
                        if (datastrmap.get("attach_url") == null) {
                            attach_urlstr = "";
                        } else {
                            attach_urlstr = datastrmap.get("attach_url")
                                    + "";
                            // 解析短微博图片地址
                            attach_urlstr = attach_urlstr.substring(1,
                                    attach_urlstr.length() - 1);
                        }
                        publish_timestr = CalendarUtil.formatDateTime(Utils
                                .getStringtoDate(publish_timestr));
                        /** 不带图的富文本 */
                        if ("".equals(contentstr)) {
                            contentstr = "//";
                        }

                        map2.put("feed_id", feed_idstr);
                        map2.put("content", contentstr);
                        map2.put("create", publish_timestr);
                        map2.put("digg_count", digg_countstr);
                        map2.put("comment_count", comment_countstr);
                        map2.put("repost_count", repost_countstr);
                        map2.put("isdigg", isdigg);
                        map2.put("attach_url", attach_urlstr);
                        String user_infostr;
                        if (datastrmap.get("user_info") == null) {
                            user_infostr = "";
                        } else {
                            user_infostr = datastrmap.get("user_info")
                                    + "";
                        }
                        List<Map<String, Object>> user_infostrlists = JsonTools.listKeyMaps("[" + user_infostr + "]");
                        for (Map<String, Object> user_infostrmap : user_infostrlists) {
                            String uidstr = user_infostrmap.get("uid") + "";
                            String unamestr = user_infostrmap.get("uname") + "";
                            String avatar_middlestr = user_infostrmap.get("avatar_middle") + "";
                            String userGroup = user_infostrmap.get("user_group") + "";
                            map2.put("isVip", userGroup);
                            map2.put("uid", uidstr);
                            map2.put("uname", unamestr);
                            map2.put("avatar_middle", avatar_middlestr);
                        }
                        listusercommonnoteData.add(map2);
                    }
                }
                mHandler.sendEmptyMessage(0);
            }
        }
    }
}
