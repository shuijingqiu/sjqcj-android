package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.peronalnewslistAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.ACache;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.CalendarUtil;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.Md5Util;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 私信列表activity
 *
 * @author Administrator
 */
public class personalnewsActivity extends Activity {

    // 获取控件
    private LinearLayout goback1;
    // 定义List集合容器
    private peronalnewslistAdapter personalnewslistAdapter;
    // 定义于数据库同步的字段集合
    private ArrayList<HashMap<String, String>> listpersonalnewsData;
    private ListView personalnewslistview;
    // 访问页数控制
    private int current = 1;
    // 刷新列表标识
    private String isreferlist = "1";
    // 缓存用的类
    private ACache mCache;
    // 缓存信息用的值
    private ArrayList<HashMap<String, String>> messageList;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        isreferlist = "1";
        current = 1;
        // 自动下拉刷新
        ptrl.autoRefresh();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.personalnews_list);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        gobackinit();
        initView();
        // 自动下拉刷新
        ptrl.autoRefresh();
    }

    @Override
    protected void onDestroy() {
        if (messageList != null && messageList.size() > 0) {
            // 做缓存
            mCache.put("Messagex", Utils.getListMapStr(messageList));
        }
        super.onDestroy();
    }

    private void gobackinit() {
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        goback1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }

    private void initView() {
        // 缓存类
        mCache = ACache.get(this);
        //私信列表集合
        personalnewslistview = (ListView) findViewById(R.id.sesencelist2);
        // 存储数据的数组列表
        listpersonalnewsData = new ArrayList<HashMap<String, String>>(200);
        personalnewslistAdapter = new peronalnewslistAdapter(personalnewsActivity.this);
        personalnewslistview.setAdapter(personalnewslistAdapter);
        personalnewslistview.setOnItemClickListener(new OnItemClickListener() {

            //第三个参数为position
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                try {
                    Intent intent = new Intent(personalnewsActivity.this, personalnewsdetail.class);
                    //列表编号list_id是从哪里获取的
                    intent.putExtra("list_id", (String) listpersonalnewsData.get(arg2).get("list_idstr"));
                    intent.putExtra("unamestr", (String) listpersonalnewsData.get(arg2).get("unamestr"));
                    intent.putExtra("uidstr", (String) listpersonalnewsData.get(arg2).get("uidstr"));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        String str = mCache.getAsString("Messagex");
        listpersonalnewsData = Utils.getListMap(str);
        personalnewslistAdapter.setlistData(listpersonalnewsData);
        ptrl = ((PullToRefreshLayout) findViewById(
                R.id.refresh_view));
        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下来刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                //清空列表重载数据
                listpersonalnewsData.clear();
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

    //获取私信列表的数据
    private class SendInfoTask extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                // 千万别忘了告诉控件刷新完毕了哦！
                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
            } else {
                List<Map<String, Object>> lists = null;
                String datastr = null;
                List<Map<String, Object>> datastrlists = null;
                String data2str = null;
                List<Map<String, Object>> data2strlists = null;
                // 判断用户是否还存在
                boolean isRn = true;
                //是否刷新列表，清空私信列表集合
                if ("1".equals(isreferlist)) {
                    listpersonalnewsData.clear();
                }
                super.onPostExecute(result);
                // CustomToast.makeText(supermanlistActivity.this, result, 1).show();
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
                        datastrlists = JsonTools.listKeyMaps("[" + datastr + "]");
                    }
                    for (Map<String, Object> datastrmap : datastrlists) {
                        if (datastrmap.get("data") == null) {

                        } else {
                            if (data2str == null) {
                                //将data里面的数据取出来，解析一下放到data集合里面，在进行解析
                                data2str = datastrmap.get("data") + "";
                                data2strlists = JsonTools.listKeyMaps(data2str);
                            }
                            for (Map<String, Object> data2strmap : data2strlists) {
                                //获取list_id参数
                                String list_idstr = data2strmap.get("list_id") + "";
                                // 创建时间
                                String mtimestr = data2strmap.get("mtime") + "";

                                // 最后修改时间
                                String list_ctimestr = data2strmap.get("list_ctime") + "";
                                String member_uidstr = data2strmap.get("member_uid") + "";
                                String from_uidstr = data2strmap.get("from_uid") + "";

                                list_ctimestr = CalendarUtil.formatDateTime(Utils.getStringtoDate(list_ctimestr));

                                HashMap<String, String> map2 = new HashMap<String, String>();
                                map2.put("list_ctimestr", list_ctimestr);
                                map2.put("list_idstr", list_idstr);

                                //通过last_message获取用户的信息
                                String last_messagestr = data2strmap.get("last_message") + "";
                                List<Map<String, Object>> last_messagestrlists = JsonTools.listKeyMaps("[" + last_messagestr + "]");

                                for (Map<String, Object> last_messagestrmap : last_messagestrlists) {
                                    String contentstr = last_messagestrmap.get("content") + "";
                                    map2.put("contentstr", contentstr);

                                    // 私信者的信息
                                    String user_infostr = last_messagestrmap.get("user_info") + "";
                                    List<Map<String, Object>> user_infostrlists = JsonTools.listKeyMaps("[" + user_infostr + "]");

                                    //如果用户信息为false，则该用户不存在
                                    if ("false".equals(user_infostr)) {
                                        isRn = false;
                                    }
                                    for (Map<String, Object> user_infostrmap : user_infostrlists) {

                                        //获取目标用户的相关信息  id、name
                                        String uidstr = user_infostrmap.get("uid") + "";
                                        String fromunamestr = user_infostrmap.get("uname") + "";

                                        if (Constants.staticmyuidstr.equals(uidstr)) {

                                        } else {
                                            String userGroup = user_infostrmap.get("user_group") + "";
                                            map2.put("isVip", userGroup);
                                            map2.put("unamestr", fromunamestr);
                                            map2.put("uidstr", uidstr);
                                            uidstr = Md5Util.getMd5(uidstr);
                                            uidstr = Md5Util.getuidstrMd5(uidstr);
                                            map2.put("avatar_middlestr", uidstr);
                                        }
                                    }
                                }

                                if (!isRn) {
                                    // 如果用户不存在就跳过当前消息体继续处理下一条消息体
                                    isRn = true;
                                    continue;
                                }

                                // 被私信者的信息
                                String to_user_infostr;

                                if (data2strmap.get("to_user_info") == null) {
                                    to_user_infostr = "";
                                } else {
                                    to_user_infostr = data2strmap.get("to_user_info") + "";
                                }
                                List<Map<String, Object>> to_user_infostrlists = JsonTools.listKeyMaps("[" + to_user_infostr + "]");
                                for (Map<String, Object> to_user_infostrmap : to_user_infostrlists) {
                                    Set<String> touserinfokey = to_user_infostrmap.keySet();
                                    for (String key : touserinfokey) {
                                        String to_user_infostrmapstr = to_user_infostrmap.get(key) + "";
                                        if ("false".equals(to_user_infostrmapstr)) {
                                            map2.put("unamestr", "该用户已不存在");
                                        }
                                        List<Map<String, Object>> to_user_infostrmapstrlists = JsonTools.listKeyMaps("[" + to_user_infostrmapstr + "]");
                                        for (Map<String, Object> to_user_infostrmapstrmap : to_user_infostrmapstrlists) {
                                            String uidstr = to_user_infostrmapstrmap.get("uid") + "";
                                            String unamestr = to_user_infostrmapstrmap.get("uname") + "";
                                            String avatar_middlestr = to_user_infostrmapstrmap.get("avatar_middle") + "";
                                            if (Constants.staticmyuidstr.equals(uidstr)) {

                                            } else {
                                                map2.put("unamestr", unamestr);
                                                map2.put("uidstr", uidstr);
                                                uidstr = Md5Util.getMd5(uidstr);
                                                uidstr = Md5Util.getuidstrMd5(uidstr);
                                                map2.put("avatar_middlestr", uidstr);
                                            }
                                            if (Constants.staticmyuidstr.equals(member_uidstr) && Constants.staticmyuidstr.equals(from_uidstr)) {
                                                // map2.put("avatar_middlestr",avatar_middlestr);
                                            }
                                        }
                                        //私信列表的数据源
                                        listpersonalnewsData.add(map2);
                                    }
                                }
                            }
                        }
                    }
                }
                personalnewslistAdapter.setlistData(listpersonalnewsData);
                // 千万别忘了告诉控件刷新完毕了哦！
                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                if ("1".equals(isreferlist)) {
                    messageList = (ArrayList<HashMap<String, String>>) listpersonalnewsData.clone();
                }
            }
        }
    }

    private void geneItems() {
        new SendInfoTask().execute(new TaskParams(
                Constants.Url + "?app=public&mod=AppFeedList&act=Message&mid="
                        + Constants.staticmyuidstr + "&p="
                        + String.valueOf(current)
        ));
        current++;
    }
}
