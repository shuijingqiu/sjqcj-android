package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.Activity.Article.ArticleDetailsActivity;
import com.example.sjqcjstock.Activity.Article.commentshortweiboActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.FeedListAdapter;
import com.example.sjqcjstock.app.MyApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.Article.FeedListEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 讨论区的页面
 */
public class discussareaActivity extends Activity {

    // 获取控件
    private ImageView pickaddweibo1;
    private FeedListAdapter adapter;
    private ArrayList<FeedListEntity> feedListEntityList;
    private ListView commonlistview;
    // 访问页数控制
    private int current = 1;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 返回微博列表数据
    private String feedStr;
    // 网络请求提示
    private CustomProgress dialog;

    @Override
    protected void onStart() {
        super.onStart();
        current = 1;
        geneItems();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.discussarea_list);
        initView();
    }

    private void initView() {
        dialog = new CustomProgress(this);
        dialog.showDialog();

        pickaddweibo1 = (ImageView) findViewById(R.id.pickaddweibo1);
        findViewById(R.id.goback1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pickaddweibo1.setOnClickListener(new pickaddweibo1_listener());
        /** 普通帖集合 */
        commonlistview = (ListView) findViewById(R.id.dissarealist2);
        adapter = new FeedListAdapter(getApplicationContext());
        commonlistview.setAdapter(adapter);
        MyApplication myapplication = (MyApplication) getApplication();
        ImageLoader imageLoader = myapplication.getImageLoader();
        commonlistview.setOnScrollListener(new PauseOnScrollListener(
                imageLoader, true, false));
        commonlistview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                try {
                    Intent intent = new Intent(discussareaActivity.this,
                            ArticleDetailsActivity.class);
                    // 传递发布微博的信息
                    intent.putExtra("weibo_id", feedListEntityList
                            .get(arg2).getFeed_id());
//                    intent.putExtra("uid", listcommonnoteData.get(arg2)
//                            .get("uid").toString());
//
//                    // 传递转发微博的信息
//                    if ("repost".equals(listcommonnoteData.get(arg2)
//                            .get("type").toString())) {
//                        intent.putExtra("sourceweibo_id", listcommonnoteData
//                                .get(arg2).get("source_feed_idstr")
//                                .toString());
//                        intent.putExtra(
//                                "sourceuid",
//                                listcommonnoteData.get(arg2)
//                                        .get("sourceuidstr").toString());
//
//                        // 转发类型
//                        intent.putExtra("type", listcommonnoteData
//                                .get(arg2).get("type").toString());
//                    }
                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        ptrl = ((PullToRefreshLayout) findViewById(
                R.id.refresh_view));
        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下来刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                current = 1;
                geneItems();
            }

            // 下拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                current++;
                geneItems();
            }
        });
    }

    class pickaddweibo1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent(discussareaActivity.this,
                    commentshortweiboActivity.class);
            intent.putExtra("channelId","15");
            startActivity(intent);
        }
    }

//    private class SendInfoTaskloadmore extends
//            AsyncTask<TaskParams, Void, String> {
//
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            if (result == null) {
//                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT)
//                        .show();
//                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
//            } else {
//                super.onPostExecute(result);
//                List<Map<String, Object>> gbqblists2 = null;
//                String datastr2 = null;
//                List<Map<String, Object>> datastrlists2 = null;
//
//                result = result.replace("\n ", "");
//                result = result.replace("\n", "");
//                result = result.replace(" ", "");
//                result = "[" + result + "]";
//                // 解析json字符串获得List<Map<String,Object>>
//                if (gbqblists2 == null) {
//                    gbqblists2 = JsonTools.listKeyMaps(result);
//                }
//
//                for (Map<String, Object> map : gbqblists2) {
//                    // List<Map<String,Object>>
//                    // datastrlists=JsonTools.listKeyMaps(diggArrstr);
//                    // 获取点赞标识的数组
//
//                    String diggArrstr;
//                    if (map.get("diggArr") == null) {
//                        diggArrstr = "";
//                    } else {
//                        diggArrstr = map.get("diggArr") + "";
//
//                    }
//                    if (datastr2 == null) {
//                        datastr2 = map.get("data") + "";
//                        datastrlists2 = JsonTools.listKeyMaps(datastr2);
//                    }
//                    if (datastrlists2 == null) {
//                        datastrlists2 = new ArrayList<Map<String, Object>>();
//                    }
//                    for (int i = 0; i < datastrlists2.size(); i++) {
//                        String digg_countstr = "";
//                        String comment_countstr = "";
//                        String repost_countstr = "";
//
//                        String isdigg = "0";// isdigg为0为未点赞为1为已点赞
//                        String feed_idstr = datastrlists2.get(i).get("feed_id")
//                                + "";
//                        if (diggArrstr.contains(feed_idstr)) {
//                            isdigg = "1";
//                        }
//                        String typestr = datastrlists2.get(i).get("type")
//                                + "";
//                        String contentstr = datastrlists2.get(i).get("content")
//                                + "";
//
//                        contentstr = contentstr
//                                .replace("<feed-titlestyle='display:none'>",
//                                        "fontsing1");
//                        contentstr = contentstr.replace("</feed-title>",
//                                "fontsing2");
//
//                        // 正则表达式处理 去Html代码
//                        String regex = "\\<[^\\>]+\\>";
//                        contentstr = contentstr.replaceAll(regex, "");
//                        contentstr = contentstr.replace("\t", "");
//                        contentstr = contentstr.replace("\n", "");
//                        contentstr = contentstr.replace("\r", "");
//
//                        contentstr = contentstr.replace("&nbsp;", "");
//                        // contentstr=contentstr.replace("	","");
//                        contentstr = contentstr.replace("	", "");
//
//                        contentstr = contentstr.replace("　　", "");
//
//                        contentstr = contentstr.replace("fontsing1",
//                                "<font color=\"#89B1E0\" >【");
//                        contentstr = contentstr.replace("fontsing2",
//                                "】</font><Br/>");
//
//                        contentstr = contentstr.replace("[", "<img src=\"");
//                        contentstr = contentstr.replace("]", "\"   >");
//
//                        HashMap<String, Object> map2 = new HashMap<String, Object>();
//                        map2.put("type", typestr);
//                        if ("repost".equals(typestr)) {
//                            String api_sourcestr = datastrlists2.get(i)
//                                    .get("api_source") + "";
//                            List<Map<String, Object>> api_sourcestrlists = JsonTools
//                                    .listKeyMaps("[" + api_sourcestr + "]");
//
//                            for (Map<String, Object> api_sourcestrmap : api_sourcestrlists) {
//
//                                String source_feed_idstr = api_sourcestrmap
//                                        .get("feed_id") + "";
//                                String source_user_infostr = api_sourcestrmap
//                                        .get("source_user_info") + "";
//                                String source_contentstr = api_sourcestrmap
//                                        .get("source_content") + "";
//                                // 正则表达式处理 去Html代码
//                                String regex2 = "\\<[^\\>]+\\>";
//                                source_contentstr = source_contentstr
//                                        .replaceAll(regex, "");
//                                source_contentstr = source_contentstr.replace(
//                                        "\n\n\n", "");
//                                source_contentstr = source_contentstr.replace(
//                                        "\t", "");
//                                source_contentstr = source_contentstr.replace(
//                                        "\n", "");
//
//                                CharSequence source_contentstrcharSequence = Html
//                                        .fromHtml(source_contentstr);
//
//                                map2.put("source_feed_idstr", source_feed_idstr);
//
//                                map2.put("source_contentstr",
//                                        source_contentstrcharSequence);
//                                // map2.put("source_titlestr", source_titlestr);
//
//                                List<Map<String, Object>> source_user_infostrlists = JsonTools
//                                        .listKeyMaps("[" + source_user_infostr
//                                                + "]");
//                                for (Map<String, Object> source_user_infostrmap : source_user_infostrlists) {
//                                    String ctimestr;
//                                    String sourceuidstr = source_user_infostrmap
//                                            .get("uid") + "";
//                                    String sourceunamestr = source_user_infostrmap
//                                            .get("uname") + "";
//                                    if (source_user_infostrmap.get("ctime") == null) {
//                                        ctimestr = "";
//                                    } else {
//                                        ctimestr = source_user_infostrmap.get(
//                                                "ctime") + "";
//                                    }
//                                    String avatar_middlestr = source_user_infostrmap
//                                            .get("avatar_middle") + "";
//                                    map2.put("sourceuidstr", sourceuidstr);
//                                    map2.put("sourceuname", sourceunamestr);
//                                    map2.put("sourceuctime", ctimestr);
//                                    map2.put("sourceavatar_middlestr",
//                                            avatar_middlestr);
//                                }
//                            }
//                        }
//
//                        // 是否转发
//                        String is_repoststr = datastrlists2.get(i)
//                                .get("is_repost") + "";
//
//                        String publish_timestr = datastrlists2.get(i)
//                                .get("publish_time") + "";
//                        if (datastrlists2.get(i).get("digg_count") == null) {
//                            digg_countstr = "0";
//                        } else {
//                            digg_countstr = datastrlists2.get(i)
//                                    .get("digg_count") + "";
//                        }
//                        if (datastrlists2.get(i).get("comment_count") == null) {
//                            comment_countstr = "0";
//                        } else {
//                            comment_countstr = datastrlists2.get(i)
//                                    .get("comment_count") + "";
//                        }
//                        if (datastrlists2.get(i).get("repost_count") == null) {
//                            repost_countstr = "0";
//                        } else {
//                            repost_countstr = datastrlists2.get(i)
//                                    .get("repost_count") + "";
//
//                        }
//                        String attach_urlstr;
//                        if (datastrlists2.get(i).get("attach_url") == null) {
//                            attach_urlstr = "";
//                        } else {
//                            attach_urlstr = datastrlists2.get(i)
//                                    .get("attach_url") + "";
//                            // 解析短微博图片地址
//                            attach_urlstr = attach_urlstr.substring(1,
//                                    attach_urlstr.length() - 1);
//
//                        }
//                        publish_timestr = CalendarUtil.formatDateTime(Utils
//                                .getStringtoDate(publish_timestr));
//                        /** 富文本方案二 */
//                        CharSequence charSequence = "";
//                        try {
//                            charSequence = Html.fromHtml(contentstr,
//                                    ImageUtil.getImageGetter(getResources()), null);
//                        } catch (Exception e) {
//                            // TODO: handle exception
//                            e.printStackTrace();
//                        }
//
//                        /** 不带图的富文本 */
//                        if ("".equals(contentstr)) {
//                            contentstr = "//";
//                        }
//
//                        map2.put("feed_id", feed_idstr);
//                        map2.put("content", charSequence);
//                        map2.put("create", publish_timestr);
//                        map2.put("digg_count", digg_countstr);
//                        map2.put("comment_count", comment_countstr);
//                        map2.put("repost_count", repost_countstr);
//                        map2.put("isdigg", isdigg);
//
//                        map2.put("attach_url", attach_urlstr);
//
//                        String user_infostr;
//                        if (datastrlists2.get(i).get("user_info") == null) {
//                            user_infostr = "";
//                        } else {
//
//                            user_infostr = datastrlists2.get(i)
//                                    .get("user_info") + "";
//                        }
//                        List<Map<String, Object>> user_infostrlists = JsonTools
//                                .listKeyMaps("[" + user_infostr + "]");
//
//                        for (Map<String, Object> user_infostrmap : user_infostrlists) {
//                            String uidstr = user_infostrmap.get("uid")
//                                    + "";
//                            String unamestr = user_infostrmap.get("uname")
//                                    + "";
//                            String avatar_middlestr = user_infostrmap.get(
//                                    "avatar_middle") + "";
//
//                            map2.put("uid", uidstr);
//                            map2.put("uname", unamestr);
//                            map2.put("avatar_middle", avatar_middlestr);
//                        }
//                        listcommonnoteData.add(map2);
//                    }
//                }
//                discussareanoteAdapter.setlistData(listcommonnoteData);
//                // 千万别忘了告诉控件刷新完毕了哦！
//                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//            }
//        }
//    }

    private void geneItems() {
//        AsyncTask<TaskParams, Void, String> task = new SendInfoTaskloadmore();
//        task.execute(new TaskParams(
//                        Constants.Url + "?app=public&mod=AppContent&act=render&p="
//                                + String.valueOf(current),
//                        // new String[] { "position", "2"},
//                        new String[]{"mid", Constants.staticmyuidstr}, new String[]{
//                        "cid", "15"}
//                )
//        );

        // 获取讨论区文章列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 15选股讨论区 8浮生
                feedStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/channel/list?cid=15&mid=" + Constants.staticmyuidstr + "&p=" + current);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

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
                        dialog.dismissDlog();
                        JSONObject jsonObject = new JSONObject(feedStr);
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            // 请求失败的情况
                            CustomToast.makeText(discussareaActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            // 千万别忘了告诉控件刷新完毕了哦！失败
                            ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                            return;
                        }
                        ArrayList<FeedListEntity> feedListEntitys = (ArrayList<FeedListEntity>) JSON.parseArray(jsonObject.getString("data"), FeedListEntity.class);
                        if (current == 1) {
                            feedListEntityList = feedListEntitys;
                        } else {
                            feedListEntityList.addAll(feedListEntitys);
                        }
                        adapter.setlistData(feedListEntityList);
                        // 千万别忘了告诉控件刷新完毕了哦！
                        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

}
