package com.example.sjqcjstock.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sjqcjstock.Activity.forumnotedetailActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.commonnoteAdapter;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.CalendarUtil;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
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
 * 浮生的Fragment
 * Created by Administrator on 2016/5/31.
 */
public class FragmentEssenceFs extends Fragment {

    // 控制器
    private commonnoteAdapter commonnoteAdapter;
    //定义于数据库同步的字段集合
    private ArrayList<HashMap<String, String>> listessenceData;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private ListView listView;
    //访问页数控制
    private int current = 1;

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
        listessenceData = new ArrayList<HashMap<String, String>>();
        commonnoteAdapter = new commonnoteAdapter(getActivity());
        listView = (ListView) view.findViewById(
                R.id.fs_list_view);
        listView.setAdapter(commonnoteAdapter);
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
        SendInfoTaskmyweibolistloadmore task = new SendInfoTaskmyweibolistloadmore();
        task.execute(new TaskParams(
                Constants.Url + "?app=public&mod=AppContent&act=render",
                new String[]{"mid", Constants.staticmyuidstr},
                new String[]{"cid", "8"},
                new String[]{"p", String.valueOf(current)}
        ));
    }


    //刷新和加载更多都是这个异步任务
    private class SendInfoTaskmyweibolistloadmore extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            String digg_countstr = "";
            String comment_countstr = "";
            String repost_countstr = "";

            String source_user_infostr = "";
            String source_contentstr = "";
            String source_feed_idstr = "";

            String sourceuidstr = "";
            String sourceunamestr = "";
            // String ctimestr= source_user_infostrmap.get("ctime").toString();
            String avatar_middlestr = "";

            String user_infostr = "";
            // 获取概要
            String introduction = "";
            // 判断是否是微博
            Object state = null;
            // 获取水晶币个数
            String reward = "";
            List<Map<String, Object>> gbqblists2 = null;
            String datastr2 = null;
            List<Map<String, Object>> datastrlists2 = null;
            if (result == null) {
                CustomToast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
                // 千万别忘了告诉控件刷新完毕了哦！
                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
            } else {
                super.onPostExecute(result);
                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                // 解析json字符串获得List<Map<String,Object>>
                if (gbqblists2 == null) {
                    gbqblists2 = JsonTools.listKeyMaps(result);
                }
                int j = 0;
                for (Map<String, Object> map : gbqblists2) {
                    j++;
                    // 获取点赞标识的数组
                    String diggArrstr;
                    if (map.get("diggArr") == null) {
                        diggArrstr = "";
                    } else {
                        diggArrstr = map.get("diggArr").toString();
                    }

                    if (datastr2 == null) {
                        datastr2 = map.get("data").toString();
                        datastrlists2 = JsonTools.listKeyMaps(datastr2);
                    }
                    if (datastrlists2 == null) {
                        datastrlists2 = new ArrayList<Map<String, Object>>();
                    }
                    for (Map<String, Object> datastrmap : datastrlists2) {
                        String isdigg = "0";// isdigg为0为未点赞为1为已点赞
                        String feed_idstr = datastrmap.get("feed_id").toString();
                        if (diggArrstr.contains(feed_idstr)) {
                            isdigg = "1";
                        }

                        // 微博类型
                        String typestr = datastrmap.get("type").toString();
                        // 获取为本类容
                        String contentstr = "";
                        introduction = "";
                        state = datastrmap.get("state");
                        // 先判断是否是付费文章如果是就获取概要
                        if (state != null && "1".equals(state.toString())) {
                            reward = datastrmap.get("reward").toString();
                            Object introd = datastrmap.get("introduction");
                            if (introd != null && !"null".equals(introd.toString())) {
                                introduction = introd.toString();
                            }
                        }
                        if (datastrmap.get("body") == null) {
                            contentstr = "内容无空";
                        } else {
                            contentstr = datastrmap.get("body").toString();
                        }

                        contentstr = contentstr.replace("【", "<font color=\"#4471BC\" >【");
                        contentstr = contentstr.replace("】", "】</font>");

                        // 正则表达式处理 去Html代码
                        String regex = "\\<[^\\>]+\\>";
                        /////////////////////////////////////////////////////////////////////
//                        contentstr = contentstr.replace("ahref", "a  href ");
                        contentstr = contentstr.replace("atarget", "a ");
                        contentstr = contentstr.replace("target", "");

                        contentstr = contentstr.replace("</a>", "&nbsp</a>");

                        contentstr = contentstr.replace("&nbsp;", "");
                        contentstr = contentstr.replace("	", "");
                        contentstr = contentstr.replace("　　", "");

                        contentstr = contentstr.replace("fontsing1", "<font color=\"#89B1E0\" >【");
                        contentstr = contentstr.replace("fontsing2", "】</font><Br/>");
                        /////////////////////////////////////////////////////////////////////////////////
                        contentstr = contentstr.replace("'__THEME__/image/expression/miniblog/", "\"");
                        contentstr = contentstr.replace(".gif'", "\"");
                        contentstr = contentstr.replace("imgsrc", "img src");
                        contentstr = contentstr.replace("'http://www.sjqcj.com/addons/plugin/LongText/editor/kindeditor-4.1.4/plugins/emoticons/images/", "\"");


                        HashMap<String, String> map2 = new HashMap<String, String>();
                        map2.put("type", typestr);
                        if ("repost".equals(typestr)) {
                            contentstr = contentstr.substring(0, contentstr.indexOf("◆"));

                            String api_sourcestr = datastrmap.get("api_source").toString();
                            List<Map<String, Object>> api_sourcestrlists = JsonTools.listKeyMaps("[" + api_sourcestr + "]");
                            for (Map<String, Object> api_sourcestrmap : api_sourcestrlists) {

                                source_user_infostr = api_sourcestrmap.get("source_user_info").toString();
                                source_contentstr = api_sourcestrmap.get("source_content").toString();
                                source_feed_idstr = api_sourcestrmap.get("feed_id").toString();
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
                                        reward = api_sourcestrmap.get("reward").toString();
                                    }
                                    String gaiyao = api_sourcestrmap.get("zy").toString();
                                    source_contentstr = "<font color=\"#4471BC\" >" + source_contentstr.substring(source_contentstr.indexOf("【"), source_contentstr.indexOf("】") + 1) + "</font><Br/>" + gaiyao;
                                }
                                CharSequence source_contentstrcharSequence = Html.fromHtml(source_contentstr);

                                map2.put("source_contentstr", source_contentstrcharSequence.toString());
                                map2.put("source_feed_idstr", source_feed_idstr);

                                List<Map<String, Object>> source_user_infostrlists = JsonTools.listKeyMaps("[" + source_user_infostr + "]");
                                for (Map<String, Object> source_user_infostrmap : source_user_infostrlists) {

                                    sourceuidstr = source_user_infostrmap.get("uid").toString();
                                    sourceunamestr = source_user_infostrmap.get("uname").toString();
                                    avatar_middlestr = source_user_infostrmap.get("avatar_middle").toString();
                                    String userGroup = source_user_infostrmap.get("user_group").toString();
                                    map2.put("isVipSource", userGroup);
                                    map2.put("sourceuidstr", sourceuidstr);
                                    map2.put("sourceuname", sourceunamestr);
                                    map2.put("sourceavatar_middlestr", avatar_middlestr);
                                }
                            }
                        }

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
                        publish_timestr = CalendarUtil.formatDateTime(Utils
                                .getStringtoDate(publish_timestr));
                        if (contentstr.contains("feed_img_lists")) {
                            contentstr = contentstr.substring(0,
                                    contentstr.indexOf("feed_img_lists"));
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

                        // 不带图的富文本
                        if ("".equals(contentstr)) {
                            contentstr = "//";
                        }
                        CharSequence charSequence = "";
                        // 如果是付费文章就有概要 显示内容为标题+概要
                        if (!"".equals(introduction)) {
                            contentstr = "<font color=\"#4471BC\" >" + contentstr.substring(contentstr.indexOf("【"), contentstr.indexOf("】") + 1) + "</font><Br/>" + introduction;
                        }
                        try {
                            charSequence = Html.fromHtml(contentstr, ImageUtil.getImageGetter(getResources()), null);
                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        } finally {

                        }
                        if (state != null) {
                            map2.put("state", state.toString());
                        }
                        map2.put("reward", reward);
                        map2.put("feed_id", feed_idstr);
                        map2.put("content", charSequence.toString());
                        map2.put("create", publish_timestr);
                        map2.put("digg_count", digg_countstr);
                        map2.put("comment_count", comment_countstr);
                        map2.put("repost_count", repost_countstr);
                        map2.put("isdigg", isdigg);

                        map2.put("attach_url", attach_urlstr);
                        // String user_infostr;
                        if (datastrmap.get("user_info") == null) {
                            user_infostr = "";
                        } else {

                            user_infostr = datastrmap.get("user_info")
                                    .toString();
                        }
                        List<Map<String, Object>> user_infostrlists = JsonTools
                                .listKeyMaps("[" + user_infostr + "]");

                        for (Map<String, Object> user_infostrmap : user_infostrlists) {
                            String uidstr = user_infostrmap.get("uid").toString();
                            String unamestr = user_infostrmap.get("uname").toString();
                            String avatar_middlestr2 = user_infostrmap.get("avatar_middle").toString();
                            String userGroup = user_infostrmap.get("user_group").toString();
                            map2.put("isVip", userGroup);
                            map2.put("uid", uidstr);
                            map2.put("uname", unamestr);
                            map2.put("avatar_middle", avatar_middlestr2);
                        }
                        listessenceData.add(map2);
                    }
                    commonnoteAdapter.setlistData(listessenceData);
                    // 千万别忘了告诉控件刷新完毕了哦！
                    ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
            }
        }
    }
}