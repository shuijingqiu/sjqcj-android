package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.atmeAdapter;
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
 * 提到我的评论
 */
public class atmeActivity extends Activity {
    private LinearLayout goback1;
    // 定义List集合容器
    private atmeAdapter atmeAdapter;
    private ListView xlistView2;
    // at我列表的数据集合
    private ArrayList<HashMap<String, String>> listatmecommentData;
    // 加载更多
    // 访问页数控制
    private int current = 1;
    // 刷新列表标识
    private String isreferlist = "1";
    // 缓存用的类
    private ACache mCache;
    // 缓存的值
    private ArrayList<HashMap<String, String>> appMentionList;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.atmecomment_list);
        ExitApplication.getInstance().addActivity(this);
        initView();
        // 自动下拉刷新
        ptrl.autoRefresh();
    }

    @Override
    protected void onDestroy() {
        if (appMentionList != null && appMentionList.size() > 0) {
            // 做缓存
            mCache.put("AppMentionx", Utils.getListMapStr(appMentionList));
        }
        super.onDestroy();
    }

    private void initView() {
        // 缓存类
        mCache = ACache.get(this);
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        goback1.setOnClickListener(new goback1_listener());
        xlistView2 = (ListView) findViewById(R.id.listView2);
        listatmecommentData = new ArrayList<HashMap<String, String>>();
        atmeAdapter = new atmeAdapter(atmeActivity.this);
        xlistView2.setAdapter(atmeAdapter);
        ptrl = ((PullToRefreshLayout) findViewById(
                R.id.refresh_view));

        String str = mCache.getAsString("AppMentionx");
        listatmecommentData = Utils.getListMap(str);
        atmeAdapter.setlistData(listatmecommentData);

        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下来刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                //清空列表重载数据
                listatmecommentData.clear();
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

    class goback1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            finish();
            System.gc();
        }
    }

    private void geneItems() {
        new SendInfoTaskatmecomment().execute(new TaskParams(
                Constants.Url + "?app=public&mod=AppFeedList&act=AppMention",
                new String[]{"mid",
                        Constants.staticmyuidstr}, new String[]{
                "login_password", Constants.staticpasswordstr},
                new String[]{"p", String.valueOf(current)}));
        current++;
    }


    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                atmeAdapter.setlistData(listatmecommentData);
                // 千万别忘了告诉控件刷新完毕了哦！
                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                if ("1".equals(isreferlist)) {
                    appMentionList = (ArrayList<HashMap<String, String>>) listatmecommentData.clone();
                }
            } else {
                // 千万别忘了告诉控件刷新完毕了哦！加载失败
                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
            }
            super.handleMessage(msg);
        }
    };

    private class SendInfoTaskatmecomment extends
            AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG)
                        .show();
                mHandler.sendEmptyMessage(1);
            } else {
                if ("1".equals(isreferlist)) {
                    listatmecommentData.clear();
                }
                super.onPostExecute(result);
                List<Map<String, Object>> lists = null;
                String datastr = null;
                List<Map<String, Object>> datastrlists = null;
                String at_liststr = null;
                List<Map<String, Object>> at_liststrlists = null;
                String dataat_listdatastr = null;
                List<Map<String, Object>> dataat_listdatastrlists = null;

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
                        datastr = map.get("data") + "";
                        datastrlists = JsonTools.listKeyMaps("[" + datastr
                                + "]");
                    }
                    if (datastrlists == null) {
                        datastrlists = new ArrayList<Map<String, Object>>();
                    }
                    for (Map<String, Object> datastrmap : datastrlists) {
                        if (at_liststr == null) {
                            at_liststr = datastrmap.get("at_list") + "";
                            at_liststrlists = JsonTools.listKeyMaps("["
                                    + at_liststr + "]");
                        }
                        if (at_liststrlists == null) {
                            at_liststrlists = new ArrayList<Map<String, Object>>();
                        }
                        for (Map<String, Object> at_liststrmap : at_liststrlists) {
                            if (dataat_listdatastr == null) {
                                dataat_listdatastr = at_liststrmap.get(
                                        "data") + "";
                                dataat_listdatastrlists = JsonTools
                                        .listKeyMaps(dataat_listdatastr);
                            }
                            for (Map<String, Object> dataat_listdatastrmap : dataat_listdatastrlists) {
                                HashMap<String, String> map2 = new HashMap<String, String>();
                                String feed_idstr = null;
                                String typestr = null;
                                String uidstr = null;
                                String unamestr = null;
                                String feed_contentstr = null;
                                String avatar_middlestr = null;
                                if (dataat_listdatastrmap.get("feed_id") == null) {
                                    feed_idstr = "";
                                } else {
                                    feed_idstr = dataat_listdatastrmap.get(
                                            "feed_id") + "";
                                }
                                if (dataat_listdatastrmap.get("type") == null) {
                                    typestr = "";
                                } else {
                                    typestr = dataat_listdatastrmap.get(
                                            "type") + "";
                                }
                                if (dataat_listdatastrmap.get("uid") == null) {
                                    uidstr = "";
                                } else {
                                    uidstr = dataat_listdatastrmap.get(
                                            "uid") + "";
                                }
                                if (dataat_listdatastrmap.get("uname") == null) {
                                    unamestr = "";
                                } else {
                                    unamestr = dataat_listdatastrmap.get(
                                            "uname") + "";
                                }
                                if (dataat_listdatastrmap
                                        .get("feed_content") == null) {
                                    feed_contentstr = "";
                                } else {
                                    feed_contentstr = dataat_listdatastrmap
                                            .get("feed_content") + "";
                                }
                                // String feed_contentstr=
                                // dataat_listdatastrmap.get("feed_content")+"";

                                if (dataat_listdatastrmap
                                        .get("avatar_middle") == null) {
                                    avatar_middlestr = "";
                                } else {
                                    avatar_middlestr = dataat_listdatastrmap
                                            .get("avatar_middle")
                                            + "";
                                }

                                String publish_timestr;
                                if (dataat_listdatastrmap
                                        .get("publish_time") == null) {
                                    publish_timestr = "";
                                } else {
                                    publish_timestr = dataat_listdatastrmap
                                            .get("publish_time") + "";
                                    publish_timestr = CalendarUtil
                                            .formatDateTime(Utils
                                                    .getStringtoDate(publish_timestr));

                                }

                                String regex2 = "\\<[^\\>]+\\>";
                                feed_contentstr = feed_contentstr
                                        .replaceAll(regex2, "");
                                feed_contentstr = feed_contentstr.replace(
                                        "\t", "");
                                feed_contentstr = feed_contentstr.replace(
                                        "\n", "");
                                feed_contentstr = feed_contentstr.replace(
                                        "&nbsp;", "");
                                if ("".equals(feed_contentstr)) {
                                    feed_contentstr = "微博分享";
                                }
                                String userGroup = dataat_listdatastrmap.get("user_group") + "";
                                map2.put("isVip", userGroup);

                                map2.put("feed_content", feed_contentstr);
                                map2.put("feed_id", feed_idstr);
                                map2.put("uid", uidstr);
                                map2.put("uname", unamestr);
                                map2.put("avatar_middle", avatar_middlestr);
                                map2.put("type", typestr);
                                map2.put("publish_time", publish_timestr);

                                if (dataat_listdatastrmap
                                        .get("transpond_data") == null) {
                                    map2.put("atfeed_content", "");

                                } else {
                                    String transpond_datastr = dataat_listdatastrmap
                                            .get("transpond_data")
                                            + "";

                                    List<Map<String, Object>> transpond_datastrlists = JsonTools
                                            .listKeyMaps("["
                                                    + transpond_datastr
                                                    + "]");

                                    for (Map<String, Object> transpond_datastrmap : transpond_datastrlists) {
                                        String atfeed_idstr = transpond_datastrmap
                                                .get("feed_id") + "";
                                        String atuid_idstr = transpond_datastrmap
                                                .get("uid") + "";
                                        String atfeed_contentstr = transpond_datastrmap
                                                .get("content") + "";
                                        String uname = transpond_datastrmap
                                                .get("uname") + "";
                                        // 正则表达式处理 去Html代码
                                        String regex = "\\<[^\\>]+\\>";
                                        atfeed_contentstr = atfeed_contentstr
                                                .replaceAll(regex, "");
                                        atfeed_contentstr = atfeed_contentstr
                                                .replace("&nbsp;", " ");
                                        userGroup = transpond_datastrmap.get("user_group") + "";
                                        map2.put("isVipSource", userGroup);
                                        map2.put("atfeed_content", atfeed_contentstr);
                                        map2.put("atfeed_id", atfeed_idstr);
                                        map2.put("atfeed_uid", atuid_idstr);
                                        map2.put("unames", uname);
                                    }
                                }
                                listatmecommentData.add(map2);
                            }
                        }
                    }
                }
                mHandler.sendEmptyMessage(0);
            }
        }
    }
}
