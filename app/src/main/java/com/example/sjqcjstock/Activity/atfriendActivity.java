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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检索联系人的页面
 */
public class atfriendActivity extends Activity {

    private LinearLayout goback1;
    // 获取控件
    private EditText searchfriends;
    // 列表数据存储
    private ListView atfrientlist2;
    private com.example.sjqcjstock.adapter.atfrientAdapter atfrientAdapter;
    private ArrayList<HashMap<String, Object>> listatfrientData;
    // 访问页数控制
    private int current = 1;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 要检索的字段
    private String inputstr = "";
    // 初始化加载的数据列表
    private String appFriendGroup = "";
    // 检索按钮
    private Button btSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.atfriendlist);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
    }

    private void initView() {
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        goback1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchfriends = (EditText) findViewById(R.id.searchfriends);
        searchfriends.setOnClickListener(new searchfriends_listener());
        btSearch = (Button) findViewById(R.id.bt_search);
        // 设置EditText按键输入时的事件
        btSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                inputstr = searchfriends.getText() + "";
                listatfrientData.clear();
                new SendInfoTaskalluser().execute(new TaskParams(
                        Constants.Url + "?app=public&mod=AppFeedList&act=search",
                        new String[]{"mid", Constants.staticmyuidstr},
                        new String[]{"type", "at"},
                        new String[]{"key", inputstr}
                ));
                if ("".equals(inputstr.trim())) {
                    if (!"".equals(appFriendGroup.trim())) {
                        new SendInfoTaskloadmore().onPostExecute(appFriendGroup);
                    }
                }
            }
        });

        /** 精华集合 */
        atfrientlist2 = (ListView) findViewById(R.id.atfrientlist2);
        // 存储数据的数组列表
        listatfrientData = new ArrayList<HashMap<String, Object>>(200);
        atfrientAdapter = new com.example.sjqcjstock.adapter.atfrientAdapter(atfriendActivity.this, atfriendActivity.this);
        atfrientlist2.setAdapter(atfrientAdapter);
        atfrientlist2.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                // 将参数传回请求的Activity
                // CustomToast.makeText(getApplicationContext(), "at回传",1).show();
                try {
                    Intent intent = new Intent();
                    Bundle unameBundle = new Bundle();
                    unameBundle.putString("unamestr", (String) listatfrientData.get(arg2 - 1).get("unamestr"));
                    // zhuceIntent.putExtra("username",
                    // zhuceEdit.getText().toString());
                    intent.putExtras(unameBundle);
                    setResult(4, intent);
                    finish();
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
                if ("".equals(inputstr.trim())) {
                    //清空列表重载数据
                    listatfrientData.clear();
                    current = 1;
                    geneItems();
                } else {
                    // 千万别忘了告诉控件刷新完毕了哦！
                    ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
            }

            // 下拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if ("".equals(inputstr.trim())) {
                    current++;
                    geneItems();
                } else {
                    // 千万别忘了告诉控件刷新完毕了哦！
                    ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
            }
        });
        geneItems();
    }

    // 回调
    public void atcallback(String usernamestr) {
        Intent intent = new Intent();
        Bundle unameBundle = new Bundle();
        unameBundle.putString("unamestr", usernamestr);
        intent.putExtras(unameBundle);
        setResult(4, intent);
        finish();
    }

    class searchfriends_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            searchfriends.setFocusableInTouchMode(true);
        }
    }

    private class SendInfoTaskalluser extends
            AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG)
                        .show();
            } else {
                super.onPostExecute(result);
                // CustomToast.makeText(supermanlistActivity.this, result, 1).show();
                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                // 解析json字符串获得List<Map<String,Object>>
                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
                for (Map<String, Object> map : lists) {
                    if (map.get("data") == null) {

                    } else {
                        String datastr = map.get("data") + "";
                        List<Map<String, Object>> datastrlists = JsonTools
                                .listKeyMaps(datastr);
                        for (Map<String, Object> datastrmap : datastrlists) {
//                            String search_keystr;
//                            String uidstr = datastrmap.get("uid")+"";
                            String unamestr = datastrmap.get("uname")
                                    + "";
                            String avatar_smallstr = datastrmap.get(
                                    "avatar_small") + "";
//                            if (datastrmap.get("search_key") == null) {
//                                search_keystr = "";
//                            } else {
//                                search_keystr = datastrmap.get("search_key")
//                                        +"";
//                            }
                            HashMap<String, Object> map2 = new HashMap<String, Object>();
                            String userGroup = datastrmap.get("user_group") + "";
                            map2.put("isVip", userGroup);
                            map2.put("avatar_middlestr", avatar_smallstr);
                            map2.put("unamestr", unamestr);
                            listatfrientData.add(map2);
                        }
                    }
                }
                atfrientAdapter.setlistData(listatfrientData);
            }

        }

    }

    private class SendInfoTaskloadmore extends
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
                // 千万别忘了告诉控件刷新完毕了哦！
                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
            } else {
                appFriendGroup = result;
                super.onPostExecute(result);
                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                // 解析json字符串获得List<Map<String,Object>>
                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
                for (Map<String, Object> map : lists) {
                    String datastr = map.get("data") + "";
                    List<Map<String, Object>> datastrlists = JsonTools
                            .listKeyMaps("[" + datastr + "]");
                    for (Map<String, Object> datastrmap : datastrlists) {
                        String groupusersstr = datastrmap.get("groupusers")
                                + "";
                        List<Map<String, Object>> groupusersstrlists = JsonTools
                                .listKeyMaps("[" + groupusersstr + "]");

                        for (Map<String, Object> groupusersstrmap : groupusersstrlists) {
                            String group2str = groupusersstrmap.get("group-2")
                                    + "";
                            List<Map<String, Object>> group2strlists = JsonTools
                                    .listKeyMaps(group2str);
                            for (Map<String, Object> group2strmap : group2strlists) {
                                String avatar_middlestr = group2strmap.get(
                                        "avatar_middle") + "";
                                String unamestr = group2strmap.get("uname")
                                        + "";
                                HashMap<String, Object> map2 = new HashMap<String, Object>();
                                String userGroup = datastrmap.get("user_group") + "";
                                map2.put("isVip", userGroup);
                                map2.put("avatar_middlestr", avatar_middlestr);
                                map2.put("unamestr", unamestr);
                                listatfrientData.add(map2);
                            }
                        }
                    }
                }
                atfrientAdapter.setlistData(listatfrientData);
                // 千万别忘了告诉控件刷新完毕了哦！
                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
            }
        }
    }

    private void geneItems() {
        new SendInfoTaskloadmore().execute(new TaskParams(
                Constants.Url + "?app=public&mod=AppFeedList&act=AppFriendGroup&p="
                        + String.valueOf(current),
                new String[]{"mid", Constants.staticmyuidstr},
                new String[]{"login_password", Constants.staticpasswordstr}
        ));
    }
}
