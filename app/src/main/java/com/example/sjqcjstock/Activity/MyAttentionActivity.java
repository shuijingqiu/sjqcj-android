package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.commonnoteAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.app.MyApplication;
import com.example.sjqcjstock.constant.ACache;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.CalendarUtil;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我关注微博
 */
public class MyAttentionActivity extends Activity {

    private ListView lv_myAttention;
    private commonnoteAdapter MyAttentionActivityAdapter;
    private ArrayList<HashMap<String, String>> myattentioncommonnoteData;
    private int page = 1;
    private String isreferlist = "1";
    // 缓存用的类
    private ACache mCache;
    // 缓存消息用
    private ArrayList<HashMap<String, String>> loadMoreFollowingList;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_attiontion);
        ExitApplication.getInstance().addActivity(this);
        initView();
        // 自动下拉刷新
        ptrl.autoRefresh();
    }

    private void initView() {
        // 返回按钮
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.gc();
            }
        });
        // 缓存类
        mCache = ACache.get(this);
        lv_myAttention = (ListView) findViewById(R.id.lv_myAttention);
        myattentioncommonnoteData = new ArrayList<HashMap<String, String>>(200);
        MyAttentionActivityAdapter = new commonnoteAdapter(MyAttentionActivity.this);
        MyApplication myapplication = (MyApplication) getApplication();
        ImageLoader imageLoader = myapplication.getImageLoader();
        lv_myAttention.setAdapter(MyAttentionActivityAdapter);
        lv_myAttention.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                try {
                    String content = myattentioncommonnoteData.get(arg2).get("content");
                    if (content.length() > 3 && Constants.microBlogShare.equals(content.substring(0, 4))) {
                        return;
                    }
                    Intent intent = new Intent(MyAttentionActivity.this, forumnotedetailActivity.class);
                    intent.putExtra("weibo_id", (String) myattentioncommonnoteData.get(arg2).get("feed_id"));
                    intent.putExtra("uid", (String) myattentioncommonnoteData.get(arg2).get("uid"));

                    // 传递转发微博的信息
                    if ("repost".equals(myattentioncommonnoteData.get(arg2).get("type").toString())) {
                        intent.putExtra("sourceweibo_id", myattentioncommonnoteData.get(arg2).get("source_feed_idstr").toString());
                        intent.putExtra("sourceuid", myattentioncommonnoteData.get(arg2).get("sourceuidstr").toString());
                        // 转发类型
                        intent.putExtra("type", myattentioncommonnoteData.get(arg2).get("type").toString());
                    }
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        String str = mCache.getAsString("LoadMoreFollowingx");
        myattentioncommonnoteData = Utils.getListMap(str);
        MyAttentionActivityAdapter.setlistData(myattentioncommonnoteData);

        lv_myAttention.setOnScrollListener(new PauseOnScrollListener(imageLoader, true, false));
        ptrl = ((PullToRefreshLayout) findViewById(
                R.id.refresh_view));
        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下来刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                //清空列表重载数据
                myattentioncommonnoteData.clear();
                page = 1;
                isreferlist = "1";
                // 加载请求数据
                geneItemattention();
            }

            // 下拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                isreferlist = "0";
                // 加载请求数据
                geneItemattention();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (loadMoreFollowingList != null && loadMoreFollowingList.size() > 0) {
            // 做缓存
            mCache.put("LoadMoreFollowingx", Utils.getListMapStr(loadMoreFollowingList));
        }
        super.onDestroy();
    }

    private void geneItemattention() {
        new SendInfoTaskmyweiboattentionlistloadmore().execute(new TaskParams(
                Constants.Url + "?app=public&mod=FeedListMini&act=loadMore",
                new String[]{"mid", Constants.staticmyuidstr},
                new String[]{"id", Constants.staticmyuidstr},
                new String[]{"type", "following"},
                new String[]{"p", String.valueOf(page)}
        ));
        page++;
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                MyAttentionActivityAdapter.setlistData(myattentioncommonnoteData);
                // 千万别忘了告诉控件刷新完毕了哦！
                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                if ("1".equals(isreferlist)) {
                    loadMoreFollowingList = (ArrayList<HashMap<String, String>>) myattentioncommonnoteData.clone();
                }
            } else {
                // 千万别忘了告诉控件刷新完毕了哦！加载失败
                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
            }
            super.handleMessage(msg);
        }
    };

    private class SendInfoTaskmyweiboattentionlistloadmore extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            if (result == null) {
                CustomToast.makeText(MyAttentionActivity.this, "", Toast.LENGTH_LONG).show();
                mHandler.sendEmptyMessage(1);
            } else {
                super.onPostExecute(result);
                if ("1".equals(isreferlist)) {
                    myattentioncommonnoteData.clear();
                }
                List<Map<String, Object>> attentiongbqblists2 = null;
                String attentiondatastr2 = null;
                List<Map<String, Object>> attentiondatastrlists2 = null;

                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";

                if (attentiongbqblists2 == null) {
                    attentiongbqblists2 = JsonTools.listKeyMaps(result);
                }
                for (Map<String, Object> map : attentiongbqblists2) {
                    // List<Map<String,Object>>
                    // datastrlists=JsonTools.listKeyMaps(diggArrstr);
                    // 获取点赞标识的数组
                    String diggArrstr;
                    if (map.get("diggArr") == null) {
                        diggArrstr = "";
                    } else {
                        diggArrstr = map.get("diggArr") + "";

                    }
                    if (attentiondatastr2 == null) {
                        attentiondatastr2 = map.get("data") + "";
                        attentiondatastrlists2 = JsonTools.listKeyMaps(attentiondatastr2);
                    }
                    if (attentiondatastrlists2 == null) {
                        attentiondatastrlists2 = new ArrayList<Map<String, Object>>();
                    }
                    for (Map<String, Object> datastrmap : attentiondatastrlists2) {
                        String digg_countstr = "";
                        String comment_countstr = "";
                        String repost_countstr = "";
                        // mh 判断水晶币和打赏文的字段
                        // 概要
                        String introduction = "";
                        // 水晶币个数
                        String reward = "";
                        // 是否是付费文章 0 ：不是
                        String state = "0";

                        String isdigg = "0";// isdigg为0为未点赞为1为已点赞
                        // String diggArrstr= map.get("diggArr")+"";

                        String feed_idstr = datastrmap.get("feed_id") + "";

                        if (diggArrstr.contains(feed_idstr)) {
                            isdigg = "1";
                        }
                        String typestr = datastrmap.get("type") + "";

                        // String bodystr= datastrmap.get("body")+"";
                        String contentstr = datastrmap.get("body") + "";

                        contentstr = contentstr.replace("【",
                                "<font color=\"#4471BC\" >【");
                        contentstr = contentstr.replace("】", "】</font>");

//                        contentstr = contentstr.replace("ahref", "a  href ");
                        contentstr = contentstr.replace("atarget", "a ");
                        contentstr = contentstr.replace("target", "");

                        contentstr = contentstr.replace("</a>", "&nbsp</a>");

                        // 正则表达式处理 去Html代码
                        String regex = "\\<[^\\>]+\\>";
                        // contentstr = contentstr.replaceAll (regex, "");
                        // contentstr=contentstr.replace("\\", "\\\\");
                        // 打印字符串
                        // contentstr=contentstr.replace("\t", "\\t");
                        // contentstr=contentstr.replace("\n", "\\n");

                        // contentstr=contentstr.replace("\n\n\n", "");
                        contentstr = contentstr.replace("\t", "");
                        contentstr = contentstr.replace("\n", "");
                        contentstr = contentstr.replace("\r", "");

                        contentstr = contentstr.replace("&nbsp;", "");
                        // contentstr=contentstr.replace("	","");
                        contentstr = contentstr.replace("	", "");

                        // contentstr=contentstr.replace("　　","");
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

                        if ("repost".equals(typestr)) {

                            contentstr = contentstr.substring(0,
                                    contentstr.indexOf("◆"));

                            String api_sourcestr = datastrmap.get("api_source")
                                    + "";
                            List<Map<String, Object>> api_sourcestrlists = JsonTools
                                    .listKeyMaps("[" + api_sourcestr + "]");

                            for (Map<String, Object> api_sourcestrmap : api_sourcestrlists) {
                                if (api_sourcestrmap.get("reward") != null)
                                    reward = api_sourcestrmap.get("reward") + "";
                                if (api_sourcestrmap.get("state") != null)
                                    state = api_sourcestrmap.get("state") + "";

                                String source_feed_idstr = api_sourcestrmap
                                        .get("feed_id") + "";
                                String source_user_infostr = api_sourcestrmap
                                        .get("source_user_info") + "";
                                String source_contentstr = api_sourcestrmap
                                        .get("source_content") + "";
                                // String source_titlestr=
                                // api_sourcestrmap.get("source_title")+"";

                                source_contentstr = source_contentstr.replace("<feed-titlestyle='display:none'>", "<font color=\"#4471BC\" >【");
                                source_contentstr = source_contentstr.replace("</feed-title>", "】</font><Br/>");
                                source_contentstr = source_contentstr
                                        .replaceAll(regex, "");
                                // contentstr=contentstr.replace("\\", "\\\\");
                                // 打印字符串
                                // contentstr=contentstr.replace("\t", "\\t");
                                // contentstr=contentstr.replace("\n", "\\n");

                                // source_contentstr=source_contentstr.replace("\n\n\n",
                                // "");
                                source_contentstr = source_contentstr.replace(
                                        "\t", "");
                                source_contentstr = source_contentstr.replace(
                                        "\n", "");

                                // map2.put("source_titlestr", source_titlestr);


                                // 先判断是否是付费文章如果是就获取概要
                                if ("1".equals(state.toString())) {
                                    String gaiyao = api_sourcestrmap.get("zy") + "";
                                    source_contentstr = "<font color=\"#4471BC\" >" + source_contentstr.substring(source_contentstr.indexOf("【"), source_contentstr.indexOf("】") + 1) + "</font><Br/>" + gaiyao;
                                }
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
                                    // if(source_user_infostrmap.get("ctime")==null){
                                    // ctimestr="";
                                    // }else{
                                    // ctimestr=
                                    // source_user_infostrmap.get("ctime")+"";
                                    // }
                                    String avatar_middlestr = source_user_infostrmap
                                            .get("avatar_middle") + "";
                                    String userGroup = source_user_infostrmap.get("user_group") + "";
                                    map2.put("isVipSource", userGroup);
                                    map2.put("sourceuidstr", sourceuidstr);
                                    map2.put("sourceuname", sourceunamestr);
                                    // map2.put("sourceuctime", ctimestr);
                                    map2.put("sourceavatar_middlestr",
                                            avatar_middlestr);
                                }
                            }
                        } else {
                            // 获取打赏相关的数据
                            if (datastrmap.get("introduction") != null) {
                                introduction = datastrmap.get("introduction") + "";
                            }
                            if (null != datastrmap.get("reward"))
                                reward = datastrmap.get("reward") + "";
                            if (null != datastrmap.get("state"))
                                state = datastrmap.get("state") + "";

                            // 如果是付费文章就有概要 显示内容为标题+概要
                            if (!"0".equals(state)) {
                                contentstr = "<font color=\"#4471BC\" >" + contentstr.substring(contentstr.indexOf("【"), contentstr.indexOf("】") + 1) + "</font><Br/>" + introduction;
                            }
                        }

                        map2.put("reward", reward);
                        map2.put("state", state);

                        // 是否转发
                        String is_repoststr = datastrmap.get("is_repost")
                                + "";

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
                            attach_urlstr = datastrmap.get("attach_url") + "";
                            // 解析短微博图片地址
                            attach_urlstr = attach_urlstr.substring(1, attach_urlstr.length() - 1);

                        }

                        // String[] attach_urlstrs = attach_urlstr.split(",");

                        // 将时间戳转换成date
                        // SimpleDateFormat formatter=new
                        // SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        // Date curDate =new
                        // Date(Long.parseLong(publish_timestr)*1000); //获取当前时间
                        // //Date curDate =new Date(System.currentTimeMillis());
                        // publish_timestr=formatter.format(curDate);
                        //
                        //
                        // publish_timestr
                        // =CalendarUtil.formatDateTime(publish_timestr);

                        publish_timestr = CalendarUtil.formatDateTime(Utils.getStringtoDate(publish_timestr));

                        // 富文本处理

                        // contentstr=contentstr.replace("/data/upload",
                        // "http://www.sjqcj.com/data/upload");
                        // contentstr=contentstr.replace("imgsrc", "img src");

                        // /** 加载图片
                        /**
                         * CharSequence
                         * charSequence=Html.fromHtml(contentstr,new
                         * ImageGetter() {
                         *
                         * @Override public Drawable getDrawable(String source)
                         *           { // TODO Auto-generated method stub URL
                         *           url; Drawable drawable=null; try { url =
                         *           new URL(source);
                         *           drawable=Drawable.createFromStream
                         *           (url.openStream(),"");
                         *           //这句话必写，不然图片是有了不过显示的面积为0.
                         *           drawable.setBounds
                         *           (0,0,drawable.getIntrinsicWidth
                         *           ()+100,drawable.getIntrinsicHeight()+100);
                         *
                         *           } catch (Exception e) { // TODO
                         *           Auto-generated catch block
                         *           e.printStackTrace(); } return drawable; }
                         *           },null);
                         */
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
                            user_infostr = datastrmap.get("user_info") + "";
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
                        myattentioncommonnoteData.add(map2);
                    }
                }
                mHandler.sendEmptyMessage(0);
            }
        }
    }
}
