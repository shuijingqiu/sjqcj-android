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
 * 我的收藏页面
 */
public class mycollectActivity extends Activity {

    private LinearLayout goback1;
    // 定义List集合容器
    private commonnoteAdapter usercommonnoteAdapter;
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
    private ArrayList<HashMap<String, String>> appCollectionList;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;


    public void test(ArrayList<HashMap<String, String>> listData) {
        usercommonnoteAdapter.setlistData(listData);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mycellect_list);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
        // 自动下拉刷新
        ptrl.autoRefresh();
    }

    @Override
    protected void onDestroy() {
        if (appCollectionList != null && appCollectionList.size() > 0) {
            // 做缓存
            mCache.put("AppCollectionx", Utils.getListMapStr(appCollectionList));
        }
        super.onDestroy();
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
        usercommonnoteAdapter = new commonnoteAdapter(mycollectActivity.this);
        mycellectlistview.setAdapter(usercommonnoteAdapter);
        mycellectlistview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                try {
                    Intent intent = new Intent(mycollectActivity.this, forumnotedetailActivity.class);
                    // 传递发布微博的信息
                    intent.putExtra("weibo_id", listmycellectData.get(arg2).get("feed_id").toString());
                    intent.putExtra("uid", listmycellectData.get(arg2).get("uid").toString());
                    // 传递转发微博的信息
                    if ("repost".equals(listmycellectData.get(arg2).get("type").toString())) {
                        intent.putExtra("sourceweibo_id", listmycellectData.get(arg2).get("source_feed_idstr").toString());
                        intent.putExtra("sourceuid", listmycellectData.get(arg2).get("sourceuidstr").toString());

                        // 转发类型
                        intent.putExtra("type", listmycellectData.get(arg2).get("type").toString());
                    }
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        String str = mCache.getAsString("AppCollectionx");
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

    class goback1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            finish();
            System.gc();
        }
    }

    private void geneItems() {
        new SendInfoTaskmycolectloadmore().execute(new TaskParams(
                Constants.Url + "?app=public&mod=AppFeedList&act=AppCollection",
                // new String[] { "position", "2"},
                new String[]{"mid", Constants.staticmyuidstr},
                new String[]{"login_password", Constants.staticpasswordstr},
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
                    appCollectionList = (ArrayList<HashMap<String, String>>) listmycellectData.clone();
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
                CustomToast.makeText(getApplicationContext(), "",
                        Toast.LENGTH_SHORT).show();
                mHandler.sendEmptyMessage(1);
            } else {
                String datastr2 = null;
                List<Map<String, Object>> data2strlists2 = null;
                List<Map<String, Object>> datastrlists2 = null;
                String data2str2 = null;
                List<Map<String, Object>> lists2 = null;
                if ("1".equals(isreferlist)) {
                    listmycellectData.clear();
                }
                super.onPostExecute(result);
                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                // 解析json字符串获得List<Map<String,Object>>
                if (lists2 == null) {
                    lists2 = JsonTools.listKeyMaps(result);
                }
                for (Map<String, Object> map : lists2) {
                    if (datastr2 == null) {
                        datastr2 = map.get("data") + "";
                        datastrlists2 = JsonTools.listKeyMaps("[" + datastr2 + "]");
                    }
                    String source_user_infostr = "";
                    String source_contentstr = "";
                    String source_feed_idstr = "";
                    String sourceuidstr = "";
                    String sourceunamestr = "";
                    String avatar_middlestr = "";
                    // 获取概要
                    String introduction = "";
                    // 判断是否是微博
                    Object state = null;
                    // 获取水晶币个数
                    String reward = "";

                    for (Map<String, Object> datastrmap : datastrlists2) {
                        // 获取点赞标识的数组
                        String diggArrstr;
                        if (datastrmap.get("diggArr") == null) {
                            diggArrstr = "";
                        } else {
                            diggArrstr = datastrmap.get("diggArr") + "";

                        }
                        if (data2str2 == null) {
                            data2str2 = datastrmap.get("data") + "";
                            data2strlists2 = JsonTools.listKeyMaps(data2str2);
                        }
                        for (Map<String, Object> data2strmap : data2strlists2) {

                            String unamestr2;
                            if (data2strmap.get("uname") == null) {
                                unamestr2 = "";
                            } else {
                                unamestr2 = data2strmap.get("uname") + "";
                            }

                            String avatar_middle2;
                            if (data2strmap.get("avatar_middle") == null) {
                                avatar_middle2 = "";
                            } else {
                                avatar_middle2 = data2strmap.get("avatar_middle")
                                        + "";
                            }
                            HashMap<String, String> map2 = new HashMap<String, String>();
                            String userGroup = data2strmap.get("user_group") + "";
                            map2.put("isVip", userGroup);
                            map2.put("uname", unamestr2);
                            map2.put("avatar_middle", avatar_middle2);
                            String source_datastr;
                            if (data2strmap.get("source_data") == null) {
                                source_datastr = "";
                            } else {
                                source_datastr = data2strmap.get("source_data")
                                        + "";
                            }
                            List<Map<String, Object>> source_datastrlists = JsonTools
                                    .listKeyMaps("[" + source_datastr + "]");
                            introduction = "";
                            state = data2strmap.get("state");
                            // 先判断是否是付费文章如果是就获取概要
                            if (state != null && "1".equals(state.toString())) {
                                reward = data2strmap.get("reward") + "";
                                Object introd = data2strmap.get("abstract");
                                if (introd != null && !"null".equals(introd.toString())) {
                                    introduction = introd + "";
                                }
                            }

                            for (Map<String, Object> source_datastrmap : source_datastrlists) {
                                String isdigg = "0";// isdigg为0为未点赞为1为已点赞
                                String feed_idstr = source_datastrmap
                                        .get("feed_id") + "";

                                if (diggArrstr.contains(feed_idstr)) {
                                    isdigg = "1";
                                }

                                String uidstr = source_datastrmap.get("uid")
                                        + "";
                                String typestr = source_datastrmap.get("type")
                                        + "";
                                String publish_timestr = source_datastrmap.get(
                                        "publish_time") + "";
                                String comment_countstr;
                                String repost_countstr;
                                String digg_countstr;
                                if (source_datastrmap.get("comment_count") == null) {
                                    comment_countstr = "0";
                                } else {
                                    comment_countstr = source_datastrmap.get(
                                            "comment_count") + "";
                                }
                                if (source_datastrmap.get("repost_count") == null) {
                                    repost_countstr = "0";
                                } else {
                                    repost_countstr = source_datastrmap.get(
                                            "repost_count") + "";
                                }

                                if (source_datastrmap.get("digg_count") == null) {
                                    digg_countstr = "0";
                                } else {
                                    digg_countstr = source_datastrmap.get(
                                            "digg_count") + "";
                                }
                                String contentstr = source_datastrmap.get("content") + "";
                                // 获得有图片的文本
                                String attach_urlstr;
                                if (datastrmap.get("attach_url") == null) {
                                    attach_urlstr = "";
                                } else {
                                    attach_urlstr = datastrmap.get("attach_url") + "";
                                    // 解析短微博图片地址
                                    attach_urlstr = attach_urlstr.substring(1, attach_urlstr.length() - 1);
                                }
                                contentstr = contentstr.replace("<feed-titlestyle='display:none'>", "fontsing1");
                                contentstr = contentstr.replace("</feed-title>", "fontsing2");
                                // 正则表达式处理 去Html代码
                                String regex = "\\<[^\\>]+\\>";
                                contentstr = contentstr.replaceAll(regex, "");
                                contentstr = contentstr.replace("\t", "");
                                contentstr = contentstr.replace("\n", "");
                                contentstr = contentstr.replace("\r", "");
                                contentstr = contentstr.replace("&nbsp;", "");
                                contentstr = contentstr.replace("	", "");
                                contentstr = contentstr.replace("　　", "");
                                contentstr = contentstr.replace("fontsing1", "<font color=\"#4471BC\" >【");
                                contentstr = contentstr.replace("fontsing2", "】</font><Br/>");
                                contentstr = contentstr.replace("[", "<img src=\"");
                                contentstr = contentstr.replace("]", "\"   >");
                                if ("repost".equals(typestr)) {
                                    String api_sourcestr = data2strmap.get("api_source") + "";
                                    List<Map<String, Object>> api_sourcestrlists = JsonTools.listKeyMaps("[" + api_sourcestr + "]");
                                    for (Map<String, Object> api_sourcestrmap : api_sourcestrlists) {

                                        source_user_infostr = api_sourcestrmap.get("source_user_info") + "";
                                        source_contentstr = api_sourcestrmap.get("source_content") + "";
                                        source_feed_idstr = api_sourcestrmap.get("feed_id") + "";
                                        source_contentstr = source_contentstr.replace("<feed-titlestyle='display:none'>", "fontsing1");
                                        source_contentstr = source_contentstr.replace("<feed-title style='display:none'>", "fontsing1");
                                        source_contentstr = source_contentstr.replace("</feed-title>", "fontsing2");
                                        // 正则表达式处理 去Html代码
                                        String regex2 = "\\<[^\\>]+\\>";
                                        source_contentstr = source_contentstr.replaceAll(regex, "");
                                        source_contentstr = source_contentstr.replace("fontsing1", "<font color=\"#4471BC\" >【");
                                        source_contentstr = source_contentstr.replace("fontsing2", "】</font><Br/>");
                                        source_contentstr = source_contentstr.replace("\t", "");
                                        source_contentstr = source_contentstr.replace("\n", "");
                                        state = api_sourcestrmap.get("state");
                                        // 先判断是否是付费文章如果是就获取概要
                                        if (state != null && "1".equals(state.toString())) {
                                            if (api_sourcestrmap.get("reward") != null) {
                                                reward = api_sourcestrmap.get("reward") + "";
                                            }
                                            String gaiyao = api_sourcestrmap.get("zy") + "";
                                            source_contentstr = "<font color=\"#4471BC\" >" + source_contentstr.substring(source_contentstr.indexOf("【"), source_contentstr.indexOf("】") + 1) + "</font><Br/>" + gaiyao;
                                        }

                                        map2.put("source_contentstr", source_contentstr);
                                        map2.put("source_feed_idstr", source_feed_idstr);

                                        List<Map<String, Object>> source_user_infostrlists = JsonTools.listKeyMaps("[" + source_user_infostr + "]");
                                        for (Map<String, Object> source_user_infostrmap : source_user_infostrlists) {

                                            sourceuidstr = source_user_infostrmap.get("uid") + "";
                                            sourceunamestr = source_user_infostrmap.get("uname") + "";
                                            avatar_middlestr = source_user_infostrmap.get("avatar_middle") + "";
                                            userGroup = source_user_infostrmap.get("user_group") + "";
                                            map2.put("isVipSource", userGroup);
                                            map2.put("sourceuidstr", sourceuidstr);
                                            map2.put("sourceuname", sourceunamestr);
                                            map2.put("sourceavatar_middlestr", avatar_middlestr);

                                        }
                                    }
                                }
                                if ("".equals(contentstr)) {
                                    contentstr = "//";
                                }
//                                CharSequence charSequence = "";
                                //  如果是付费文章就有概要 显示内容为标题+概要
                                if (!"".equals(introduction)) {
                                    contentstr = "<font color=\"#4471BC\" >" + contentstr.substring(contentstr.indexOf("【"), contentstr.indexOf("】") + 1) + "</font><Br/>" + introduction;
                                }
                                publish_timestr = CalendarUtil.formatDateTime(Utils.getStringtoDate(publish_timestr));

                                if (state != null) {
                                    map2.put("state", state.toString());
                                }
                                map2.put("reward", reward);
                                map2.put("feed_id", feed_idstr);
                                map2.put("uid", uidstr);
                                map2.put("type", typestr);
                                map2.put("publish_time", publish_timestr);
                                map2.put("comment_count", comment_countstr);
                                map2.put("repost_count", repost_countstr);
                                map2.put("attach_url", attach_urlstr);
                                map2.put("digg_count", digg_countstr);
                                map2.put("content", contentstr);
                                map2.put("create", publish_timestr);
                                map2.put("isdigg", isdigg);
                                listmycellectData.add(map2);
                            }
                        }
                    }
                }
                mHandler.sendEmptyMessage(0);
            }
        }
    }
}
