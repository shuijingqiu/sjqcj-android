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
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.commonnoteAdapter;
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
 * 我的订阅页面
 */
public class mysubscribeActivity extends Activity {

    private LinearLayout goback1;
    // 定义List集合容器
    private commonnoteAdapter usercommonnoteAdapter;
    // 定义于数据库同步的字段集合
    private ArrayList<HashMap<String, String>> listmycellectData;
    /**
     * 普通帖集合
     */
    private ListView mycellectlistview;
    // 加载更多
    // 访问页数控制
    private int current = 1;
    // 网络请求提示
    private String isreferlist = "1";
    // 缓存用的类
    private ACache mCache;
    // 缓存消息用
    private ArrayList<HashMap<String, String>> mySubscribeList;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mycellect_list);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
        ((TextView) findViewById(R.id.cellect_title_tv)).setText("订阅微博");
        // 自动下拉刷新
        ptrl.autoRefresh();
    }

    private void initView() {
        // 缓存类
        mCache = ACache.get(this);
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        goback1.setOnClickListener(new goback1_listener());
        /** 普通帖集合 */
        mycellectlistview = (ListView) findViewById(R.id.mycellectlist2);
        // 存储数据的数组列表
        listmycellectData = new ArrayList<HashMap<String, String>>(200);
        // 为ListView 添加适配器
        usercommonnoteAdapter = new commonnoteAdapter(mysubscribeActivity.this);
        mycellectlistview.setAdapter(usercommonnoteAdapter);
        mycellectlistview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Intent intent = new Intent(mysubscribeActivity.this,
                        forumnotedetailActivity.class);
                intent.putExtra("weibo_id", (String) listmycellectData.get(position).get("feed_id"));
                intent.putExtra("uid", (String) listmycellectData.get(position).get("uid"));
                // mh传递转微博的参数还没有写
                startActivity(intent);
            }
        });
        String str = mCache.getAsString("MySubscribex");
        listmycellectData = Utils.getListMap(str);
        usercommonnoteAdapter.setlistData(listmycellectData);

        ptrl = ((PullToRefreshLayout) findViewById(
                R.id.refresh_view));
        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下来刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                //清空列表重载数据
                listmycellectData.clear();
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
        if (mySubscribeList != null && mySubscribeList.size() > 0) {
            // 做缓存
            mCache.put("MySubscribex", Utils.getListMapStr(mySubscribeList));
        }
        super.onDestroy();
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
        new SendInfoTaskmycolectloadmore().execute(new TaskParams(Constants.mySubscribeUrl,
                new String[]{"mid", Constants.staticmyuidstr},
                new String[]{"p", String.valueOf(current)}
        ));
        current++;
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                usercommonnoteAdapter.setlistData(listmycellectData);
                // 千万别忘了告诉控件刷新完毕了哦！
                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                if ("1".equals(isreferlist)) {
                    mySubscribeList = (ArrayList<HashMap<String, String>>) listmycellectData.clone();
                }
            } else {
                // 千万别忘了告诉控件刷新完毕了哦！加载失败
                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
            }
            super.handleMessage(msg);
        }
    };

    private class SendInfoTaskmycolectloadmore extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                mHandler.sendEmptyMessage(1);
            } else {
                if ("1".equals(isreferlist)) {
                    listmycellectData.clear();
                }
                super.onPostExecute(result);
                String data2str2 = null;
                List<Map<String, Object>> data2strlists2 = null;
                List<Map<String, Object>> lists2 = null;
                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                if (lists2 == null) {
                    lists2 = JsonTools.listKeyMaps(result);
                }
                for (Map<String, Object> map : lists2) {
                    // 概要
                    String introduction = "";
                    // 水晶币个数
                    String reward = "";
                    // 是否是付费文章 0 ：不是
                    String state = "0";
                    // 获取点赞标识的数组
                    String diggArrstr;
                    if (map.get("diggArr") == null) {
                        diggArrstr = "";
                    } else {
                        diggArrstr = map.get("diggArr") + "";

                    }
                    if (data2str2 == null) {
                        data2str2 = map.get("data") + "";
                        data2strlists2 = JsonTools.listKeyMaps(data2str2);
                    }
                    for (Map<String, Object> datastrmap : data2strlists2) {
                        String digg_countstr = "";
                        String comment_countstr = "";
                        String repost_countstr = "";
                        String isdigg = "0";// isdigg为0为未点赞为1为已点赞
                        String feed_idstr = datastrmap.get("feed_id")
                                + "";

                        if (diggArrstr.contains(feed_idstr)) {
                            isdigg = "1";
                        }
                        String typestr = datastrmap.get("type") + "";
                        String contentstr = datastrmap.get("body") + "";
                        contentstr = contentstr.replace("【",
                                "<font color=\"#0054AA\" >【");
                        contentstr = contentstr.replace("】", "】</font><Br/>");
                        contentstr = contentstr.replace("atarget", "a ");
                        contentstr = contentstr.replace("target", "");
                        contentstr = contentstr.replace("</a>", "&nbsp</a>");
                        contentstr = contentstr.replace("\t", "");
                        contentstr = contentstr.replace("\n", "");
                        contentstr = contentstr.replace("\r", "");
                        contentstr = contentstr.replace("&nbsp;", "");
                        contentstr = contentstr.replace("	", "");
                        contentstr = contentstr.replace("　　", "");
                        contentstr = contentstr.replace("[", "<img src=\"");
                        contentstr = contentstr.replace("]", "\"   >");
                        contentstr = contentstr
                                .replace(
                                        "<imgsrc='__THEME__/image/expression/miniblog/",
                                        "<img src=\"");
                        contentstr = contentstr.replace(".gif'", "\"");
                        HashMap<String, String> map2 = new HashMap<String, String>();
                        map2.put("type", typestr);
                        // 获取打赏相关的数据
                        if (datastrmap.get("introduction") != null) {
                            introduction = datastrmap.get("introduction") + "";
                        }
                        reward = datastrmap.get("reward") + "";
                        state = datastrmap.get("state") + "";
                        // 如果是付费文章就有概要 显示内容为标题+概要
                        if (!"0".equals(state)) {
                            contentstr = "<font color=\"#4471BC\" >" + contentstr.substring(contentstr.indexOf("【"), contentstr.indexOf("】") + 1) + "</font><Br/>" + introduction;
                        }
                        map2.put("reward", reward);
                        map2.put("state", state);

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
                            contentstr = contentstr.substring(0,
                                    contentstr.indexOf("feed_img_lists"));
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
                            String uidstr = user_infostrmap.get("uid")
                                    + "";
                            String unamestr = user_infostrmap.get("uname") + "";
                            String avatar_middlestr = user_infostrmap.get("avatar_middle") + "";
                            String userGroup = user_infostrmap.get("user_group") + "";
                            map2.put("isVip", userGroup);
                            map2.put("uid", uidstr);
                            map2.put("uname", unamestr);
                            map2.put("avatar_middle", avatar_middlestr);
                        }
                        listmycellectData.add(map2);
                    }
                }
                mHandler.sendEmptyMessage(0);
            }
        }
    }
}
