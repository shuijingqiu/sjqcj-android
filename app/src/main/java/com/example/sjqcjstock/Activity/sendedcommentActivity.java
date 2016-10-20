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
import com.example.sjqcjstock.adapter.sendedcommentAdapter;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发出的评论列表页面
 */
public class sendedcommentActivity extends Activity {
    private LinearLayout goback1;
    private ListView xlistView2;
    // 定义List集合容器
    private sendedcommentAdapter sendedcommentAdapter;
    private ArrayList<HashMap<String, String>> listsendedcommentData;
    // 加载更多
    // 访问页数控制
    private int current = 1;
    // 刷新列表标识
    private String isreferlist = "1";
    // 缓存用的类
    private ACache mCache;
    // 缓存消息用
    private ArrayList<HashMap<String, String>> myCommentList;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;

    public void referActivity() {
        current = 1;
        isreferlist = "1";
        // 自动下拉刷新
        ptrl.autoRefresh();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sendedcomment_list);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
        // 自动下拉刷新
        ptrl.autoRefresh();
    }

    @Override
    protected void onDestroy() {
        if (myCommentList != null && myCommentList.size() > 0) {
            // 做缓存
            mCache.put("MyCommentSendx", Utils.getListMapStr(myCommentList));
        }
        super.onDestroy();
    }

    private void initView() {
        // 缓存类
        mCache = ACache.get(this);
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        goback1.setOnClickListener(new goback1_listener());
        xlistView2 = (ListView) findViewById(R.id.listView2);
        listsendedcommentData = new ArrayList<HashMap<String, String>>(200);
        sendedcommentAdapter = new sendedcommentAdapter(
                sendedcommentActivity.this, this);
        xlistView2.setAdapter(sendedcommentAdapter);
        String str = mCache.getAsString("MyCommentSendx");
        listsendedcommentData = Utils.getListMap(str);
        sendedcommentAdapter.setlistData(listsendedcommentData);
        ptrl = ((PullToRefreshLayout) findViewById(
                R.id.refresh_view));
        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下来刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                //清空列表重载数据
                listsendedcommentData.clear();
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
        new SendInfoTask().execute(new TaskParams(
                Constants.Url + "?app=public&mod=AppFeedList&act=MyComment", new String[]{"type", "send"},
                new String[]{"mid", Constants.staticmyuidstr}, new String[]{
                "login_password", Constants.staticpasswordstr},
                new String[]{"p", String.valueOf(current)}));
        current++;
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                sendedcommentAdapter.setlistData(listsendedcommentData);
                // 千万别忘了告诉控件刷新完毕了哦！
                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                if ("1".equals(isreferlist)) {
                    myCommentList = (ArrayList<HashMap<String, String>>) listsendedcommentData.clone();
                }
            } else {
                // 千万别忘了告诉控件刷新完毕了哦！加载失败
                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
            }
            super.handleMessage(msg);
        }
    };

    private class SendInfoTask extends AsyncTask<TaskParams, Void, String> {
        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG)
                        .show();
                mHandler.sendEmptyMessage(1);
            } else {
                List<Map<String, Object>> lists = null;
                String datastr = null;
                List<Map<String, Object>> datastrlists = null;
                String data2str = null;
                List<Map<String, Object>> data2strlists = null;
                if ("1".equals(isreferlist)) {
                    xlistView2.smoothScrollToPosition(0);
                    listsendedcommentData.clear();
                }
                super.onPostExecute(result);
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
                    for (Map<String, Object> datastrmap : datastrlists) {
                        if (data2str == null) {
                            data2str = datastrmap.get("data") + "";
                            data2strlists = JsonTools.listKeyMaps(data2str);
                        }
                        if (data2strlists == null) {
                            data2strlists = new ArrayList<Map<String, Object>>();
                        }
                        for (Map<String, Object> data2strmap : data2strlists) {
                            String app_detail_summarystr;
                            String comment_idstr = data2strmap
                                    .get("comment_id") + "";
                            String ctimestr = data2strmap.get("ctime")
                                    + "";
                            String contentstr = data2strmap.get("content")
                                    + "";

                            Object sourceInfo = data2strmap.get("sourceInfo");
                            if (sourceInfo == null) {
                                sourceInfo = "[]";
                            }
                            String sourceContent = null;
                            try {
                                sourceContent = new JSONObject(sourceInfo.toString()).getString("source_content");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (sourceContent == null) {
                                app_detail_summarystr = "";
                            } else {
                                app_detail_summarystr = sourceContent + "";


                                app_detail_summarystr = app_detail_summarystr.replace("<feed-titlestyle='display:none'>", "feedtitleqian");
                                app_detail_summarystr = app_detail_summarystr.replace("</feed-title>", "feedtitlehou");

                                String regex2 = "\\<[^\\>]+\\>";
                                app_detail_summarystr = app_detail_summarystr.replaceAll(regex2, "");

                                app_detail_summarystr = app_detail_summarystr.replace("feedtitleqian", "<font color=\"#4471BC\" >【");
                                app_detail_summarystr = app_detail_summarystr.replace("feedtitlehou", "】</font><Br/>");

                                app_detail_summarystr = app_detail_summarystr.replace("\t", "");
                                app_detail_summarystr = app_detail_summarystr.replace("\n", "");


                            }
                            ctimestr = CalendarUtil.formatDateTime(Utils
                                    .getStringtoDate(ctimestr));
                            HashMap<String, String> map2 = new HashMap<String, String>();
                            map2.put("comment_id", comment_idstr);
                            // user_info 回复用户信息
                            String user_infostr = data2strmap.get("user_info")
                                    + "";
                            List<Map<String, Object>> user_infostrlists = JsonTools
                                    .listKeyMaps("[" + user_infostr + "]");
                            String unamestr2 = "";
                            for (Map<String, Object> user_infostrmap : user_infostrlists) {
                                String uidstr = user_infostrmap.get("uid")
                                        + "";
                                unamestr2 = user_infostrmap.get("uname")
                                        + "";
                                String avatar_middlestr = user_infostrmap.get(
                                        "avatar_middle") + "";
                                String replyctimestr = user_infostrmap.get(
                                        "ctime") + "";

                                replyctimestr = CalendarUtil
                                        .formatDateTime(Utils
                                                .getStringtoDate(replyctimestr));
                                String userGroup = user_infostrmap.get("user_group") + "";
                                map2.put("isVip", userGroup);
                                map2.put("replyctimestr", replyctimestr);
                                map2.put("contentunamestr", unamestr2);
                                map2.put("avatar_middlestr", avatar_middlestr);
                                map2.put("uidstr", uidstr);
                            }
                            contentstr = contentstr
                                    .replace("imgsrc", "img src");

                            contentstr = contentstr.replace(
                                    "'__THEME__/image/expression/miniblog/",
                                    "\"");
                            contentstr = contentstr.replace(".gif'", "\"");

                            map2.put("ctimestr", ctimestr);
                            map2.put("contentstr", contentstr);
                            // sourceuser_info 回复用户信息

                            String sourceInfostr = data2strmap
                                    .get("sourceInfo") + "";
                            List<Map<String, Object>> sourceInfolists = JsonTools
                                    .listKeyMaps("[" + sourceInfostr + "]");
                            for (Map<String, Object> sourceInfostrmap : sourceInfolists) {

                                String feed_idstr = sourceInfostrmap.get(
                                        "feed_id") + "";

                                String uidstr = sourceInfostrmap.get("uid")
                                        + "";
                                String unamestr = sourceInfostrmap.get("uname")
                                        + "";
                                // 获取概要
                                String introduction = "";
                                // 判断是否是打赏微博
                                Object state = sourceInfostrmap.get("state");
                                // 获取水晶币个数
                                String reward = "";
                                if (state != null && !"null".equals(state.toString())) {
                                    if (sourceInfostrmap.get("reward") != null)
                                        reward = sourceInfostrmap.get("reward") + "";
                                    if (sourceInfostrmap.get("abstract") != null)
                                        introduction = sourceInfostrmap.get("abstract") + "";
                                }
                                if (state != null && "1".equals(state.toString())) {
                                    if ("".equals(app_detail_summarystr)) {
                                        app_detail_summarystr = "摘要：" + introduction;
                                    } else {
                                        int conunt = app_detail_summarystr.indexOf("【");
                                        if (conunt < 1) {
                                            app_detail_summarystr = "摘要：" + introduction;
                                        } else {
                                            app_detail_summarystr = "<font color=\"#4471BC\" >" + app_detail_summarystr.substring(app_detail_summarystr.indexOf("【"), app_detail_summarystr.indexOf("】") + 1) + "</font><Br/>摘要：" + introduction;
                                        }
                                    }
                                }
                                map2.put("reward", reward);
                                if (state != null) {
                                    map2.put("state", state.toString());
                                }

                                String userGroup = sourceInfostrmap.get("user_group") + "";
                                map2.put("isVipSource", userGroup);
                                map2.put("sourceunamestr", unamestr);
                                map2.put("sourceuidstr", uidstr);
                                map2.put("sourcefeed_id", feed_idstr);
                            }
                            map2.put("app_detail_summarystr", app_detail_summarystr);
                            listsendedcommentData.add(map2);
                        }
                    }
                }
                mHandler.sendEmptyMessage(0);
            }
        }
    }
}
