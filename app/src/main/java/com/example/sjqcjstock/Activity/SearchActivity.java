package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
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

public class SearchActivity extends Activity {

    private LinearLayout goback1;
    // 获取控件
    private EditText et_searchFriend;
    private Button bt_search;
    private int page = 1;
    private String inputstr;
    // 列表数据存储
    private ListView lv_searchResult;
    private com.example.sjqcjstock.adapter.SearchAdapter searchAdapter;
    private ArrayList<HashMap<String, Object>> listatfrientData;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);
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
        et_searchFriend = (EditText) findViewById(R.id.et_searchFriend);
        et_searchFriend.setOnClickListener(new et_searchFriend_listener());
        // 用户集合
        lv_searchResult = (ListView) findViewById(R.id.lv_searchResult);
        lv_searchResult.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                try {
                    Intent intent = new Intent();
                    Bundle unameBundle = new Bundle();
                    unameBundle.putString("unamestr", (String) listatfrientData.get(arg2 - 1).get("unamestr"));
                    intent.putExtras(unameBundle);
                    setResult(4, intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        bt_search = (Button) findViewById(R.id.bt_search);
        bt_search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(bt_search.getWindowToken(), 0);

                inputstr = et_searchFriend.getText() + "";
                listatfrientData.clear();
                // 请求用户列表数据
                new SendInfoTaskalluser().execute(new TaskParams(
                        Constants.Url + "?app=public&mod=AppFeedList&act=usersearch",
                        //new String[] { "mid",Constants.staticmyuidstr },
                        //new String[] { "type", "at" },
                        new String[]{"key", inputstr},
                        new String[]{"p", String.valueOf(page)}
                ));
            }
        });
        // 存储数据的数组列表
        listatfrientData = new ArrayList<HashMap<String, Object>>(200);

        searchAdapter = new com.example.sjqcjstock.adapter.SearchAdapter(
                SearchActivity.this, SearchActivity.this);
        lv_searchResult.setAdapter(searchAdapter);
        ptrl = ((PullToRefreshLayout) findViewById(
                R.id.refresh_view));
        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下来刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                //清空列表重载数据
                listatfrientData.clear();
                page = 1;
                geneItems();
            }

            // 下拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                page++;
                geneItems();
            }
        });
    }

    // 回调
    public void searchcallback(String uid) {
        Intent intent = new Intent(SearchActivity.this,
                UserDetailNewActivity.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }

    class et_searchFriend_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            et_searchFriend.setFocusableInTouchMode(true);
        }
    }

    private class SendInfoTaskalluser extends AsyncTask<TaskParams, Void, String> {

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
                super.onPostExecute(result);
                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
                for (Map<String, Object> map : lists) {
                    if (map.get("data") == null) {

                    } else {
                        String datastr = map.get("data") + "";
                        List<Map<String, Object>> datastrlists = JsonTools
                                .listKeyMaps(datastr);

                        for (Map<String, Object> datastrmap : datastrlists) {
                            String uidstr = datastrmap.get("uid") + "";
                            String unamestr = datastrmap.get("uname")
                                    + "";
                            String avatar_smallstr = datastrmap.get(
                                    "avatar_small") + "";
                            HashMap<String, Object> map2 = new HashMap<String, Object>();
                            map2.put("uid", uidstr);
                            map2.put("avatar_middlestr", avatar_smallstr);
                            map2.put("unamestr", unamestr);
                            listatfrientData.add(map2);
                        }
                    }
                }
                searchAdapter.setlistData(listatfrientData);
                // 千万别忘了告诉控件刷新完毕了哦！
                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
            }
        }
    }

    private void geneItems() {
        new SendInfoTaskalluser().execute(new TaskParams(
                Constants.Url + "?app=public&mod=AppFeedList&act=usersearch",
                new String[]{"key", inputstr},
                new String[]{"p", String.valueOf(page)}
        ));
    }
}
