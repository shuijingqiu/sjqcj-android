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
import com.example.sjqcjstock.adapter.recivecommentAdapter;
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
 * 收到的评论页面
 */
public class recivecommentActivity extends Activity {

    private LinearLayout goback1;
    private ListView xlistView2;
    // 定义List集合容器
    private recivecommentAdapter recivecommentAdapter;
    // 定义于数据库同步的字段集合
    private ArrayList<HashMap<String, String>> listrecivecommentData;
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

    @Override
    protected void onDestroy() {
        if (myCommentList != null && myCommentList.size() > 0) {
            // 做缓存
            mCache.put("MyCommentx", Utils.getListMapStr(myCommentList));
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.recivecomment_list);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
        // 自动下拉刷新
        ptrl.autoRefresh();
    }

    private void initView() {
        // 缓存类
        mCache = ACache.get(this);
        listrecivecommentData = new ArrayList<HashMap<String, String>>(200);
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        goback1.setOnClickListener(new goback1_listener());
        xlistView2 = (ListView) findViewById(R.id.listView2);
        recivecommentAdapter = new recivecommentAdapter(
                recivecommentActivity.this);
        xlistView2.setAdapter(recivecommentAdapter);

        String str = mCache.getAsString("MyCommentx");
        listrecivecommentData = Utils.getListMap(str);
        recivecommentAdapter.setlistData(listrecivecommentData);

        ptrl = ((PullToRefreshLayout) findViewById(
                R.id.refresh_view));
        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下来刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                //清空列表重载数据
                listrecivecommentData.clear();
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
                Constants.Url + "?app=public&mod=AppFeedList&act=MyComment",
                new String[]{"type", "receive"},
                new String[]{"mid", Constants.staticmyuidstr}, new String[]{
                "login_password", Constants.staticpasswordstr},
                new String[]{"p", String.valueOf(current)})
        );
        current++;
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                recivecommentAdapter.setlistData(listrecivecommentData);
                // 千万别忘了告诉控件刷新完毕了哦！
                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                if ("1".equals(isreferlist)) {
                    myCommentList = (ArrayList<HashMap<String, String>>) listrecivecommentData.clone();
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
                CustomToast.makeText(getApplicationContext(), "",
                        Toast.LENGTH_LONG).show();
                mHandler.sendEmptyMessage(1);
            } else {
                if ("1".equals(isreferlist)) {
                    xlistView2.smoothScrollToPosition(0);
                    listrecivecommentData.clear();
                }
                super.onPostExecute(result);
                List<Map<String, Object>> lists = null;
                String datastr = null;
                List<Map<String, Object>> datastrlists = null;
                String data2str = null;
                List<Map<String, Object>> data2strlists = null;
                if (lists == null) {
                    lists = JsonTools.listKeyMaps("[" + result + "]");
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
                        for (Map<String, Object> data2strmap : data2strlists) {
                            String comment_idstr = data2strmap
                                    .get("comment_id") + "";
                            String ctimestr = data2strmap.get("ctime")
                                    + "";
                            String contentstr = data2strmap.get("content")
                                    + "";
                            ctimestr = CalendarUtil.formatDateTime(Utils
                                    .getStringtoDate(ctimestr));
                            contentstr = contentstr.replace("\t", "");
                            contentstr = contentstr.replace("\n", "");
                            contentstr = contentstr.replace("\r", "");
                            contentstr = contentstr.replace(
                                    "'__THEME__/image/expression/miniblog/",
                                    "\"");
                            contentstr = contentstr.replace(".gif'", "\"");
                            HashMap<String, String> map2 = new HashMap<String, String>();
                            map2.put("ctimestr", ctimestr);
                            map2.put("contentstr", contentstr);
                            map2.put("comment_id", comment_idstr);
                            // user_info 回复用户信息
                            String user_infostr = data2strmap.get("user_info")
                                    + "";
                            List<Map<String, Object>> user_infostrlists = JsonTools
                                    .listKeyMaps("[" + user_infostr + "]");

                            for (Map<String, Object> user_infostrmap : user_infostrlists) {
                                String unamestr;
                                String avatar_middlestr;
                                String uidstr;
                                if (user_infostrmap.get("uname") == null) {
                                    unamestr = "该用户已不存在";
                                } else {
                                    unamestr = user_infostrmap.get("uname")
                                            + "";
                                }
                                if (user_infostrmap.get("avatar_middle") == null) {
                                    avatar_middlestr = "";
                                } else {
                                    avatar_middlestr = user_infostrmap.get(
                                            "avatar_middle") + "";
                                }
                                if (user_infostrmap.get("uid") == null) {
                                    uidstr = "";
                                } else {
                                    uidstr = user_infostrmap.get("uid")
                                            + "";
                                }
                                String userGroup = user_infostrmap.get("user_group") + "";
                                map2.put("isVip", userGroup);
                                map2.put("contentunamestr", unamestr);
                                map2.put("avatar_middlestr", avatar_middlestr);
                                map2.put("uidstr", uidstr);
                            }

                            String sourceInfostr = data2strmap
                                    .get("sourceInfo") + "";
                            List<Map<String, Object>> sourceInfolists = JsonTools
                                    .listKeyMaps("[" + sourceInfostr + "]");
                            for (Map<String, Object> sourceInfostrmap : sourceInfolists) {

                                // 获取概要
                                String introduction = "";
                                // 判断是否是微博
                                Object state = sourceInfostrmap.get("state");
                                // 获取水晶币个数
                                String reward = "";
                                if (state != null && "1".equals(state.toString())) {
                                    if (sourceInfostrmap.get("reward") != null)
                                        reward = sourceInfostrmap.get("reward") + "";
                                    if (sourceInfostrmap.get("abstract") != null)
                                        introduction = sourceInfostrmap.get("abstract") + "";
                                }

                                map2.put("reward", reward);
                                if (state != null) {
                                    map2.put("state", state.toString());
                                }


                                String publish_timestr = sourceInfostrmap.get(
                                        "publish_time") + "";

                                publish_timestr = CalendarUtil
                                        .formatDateTime(Utils
                                                .getStringtoDate(publish_timestr));
                                String source_content = sourceInfostrmap.get(
                                        "source_content") + "";

                                source_content = source_content.replace("<feed-title style='display:none'>", "feedtitleqian");
                                source_content = source_content.replace("</feed-title>", "feedtitlehou");

                                // 正则表达式处理 去Html代码
                                String regex2 = "\\<[^\\>]+\\>";
                                source_content = source_content.replaceAll(
                                        regex2, "");
                                source_content = source_content.replace("feedtitleqian", "<font color=\"#4471BC\" >【");
                                source_content = source_content.replace("feedtitlehou", "】</font><Br/>");

                                source_content = source_content.replace("\t",
                                        "");
                                source_content = source_content.replace("\n",
                                        "");
                                source_content = source_content.replace("\r",
                                        "");
                                source_content = source_content.replace(
                                        "&nbsp;", "");
                                source_content = source_content
                                        .replace("	", "");
                                source_content = source_content.replace("　　",
                                        "");

                                if (state != null && "1".equals(state.toString())) {
                                    source_content = "<font color=\"#4471BC\" >" + source_content.substring(source_content.indexOf("【"), source_content.indexOf("】") + 1) + "</font><Br/>摘要：" + introduction;
                                }
                                map2.put("source_content", source_content);
                                map2.put("publish_timestr", publish_timestr);
                                map2.put("sourceunamestr", sourceInfostrmap
                                        .get("uname").toString());
                                map2.put("sourceavatar_middlestr",
                                        sourceInfostrmap.get("avatar_middle")
                                                .toString());
                                map2.put("sourceuid",
                                        sourceInfostrmap.get("uid").toString());
                                map2.put("sourcefeed_id",
                                        sourceInfostrmap.get("feed_id")
                                                .toString());
                                String userGroup = sourceInfostrmap.get("user_group") + "";
                                map2.put("isVipSource", userGroup);
                            }
                            listrecivecommentData.add(map2);
                        }
                    }
                }
                mHandler.sendEmptyMessage(0);
            }
        }
    }
}
