package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.Activity.stocks.SharesDetailedActivity;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.SearchAdapter;
import com.example.sjqcjstock.adapter.stocks.StocksAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.Article.UserEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.SoListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends Activity {

    // 获取控件
    private EditText et_searchFriend;
    private Button bt_search;
    private int page = 1;
    private String inputstr;
    // 列表数据存储
//    private ListView lv_searchResult;
    // 加载用户的adapter
    private SearchAdapter searchAdapter;
    // 加载股票的adapter
    private StocksAdapter stocksAdapter;
    // 保存搜索的用户
    private ArrayList<UserEntity> listatfrientData;
    // 保存搜索的股票
    private ArrayList<HashMap<String, Object>> listStocks;
    // 上下拉刷新控件
//    private PullToRefreshLayout ptrl;
    // 股票列表
    private LinearLayout stocksLl;
    private LinearLayout userLl;
    private SoListView stosksList;
    private SoListView userList;
    // 检索接口返回数据
    private String jsonStr;

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
        findViewById(R.id.goback1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        stocksLl = (LinearLayout) findViewById(R.id.stocks_ll);
        stosksList = (SoListView) findViewById(R.id.stocks_list);
        userList = (SoListView) findViewById(R.id.user_list);
        userLl = (LinearLayout) findViewById(R.id.user_ll);
        et_searchFriend = (EditText) findViewById(R.id.et_searchFriend);
        et_searchFriend.setOnClickListener(new et_searchFriend_listener());

        stosksList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, SharesDetailedActivity.class);
                intent.putExtra("name", listStocks.get(position).get("name")+"");
                intent.putExtra("code", listStocks.get(position).get("code")+"");
                startActivity(intent);
            }
        });

        bt_search = (Button) findViewById(R.id.bt_search);
        bt_search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(bt_search.getWindowToken(), 0);
                inputstr = et_searchFriend.getText() + "";
                listatfrientData.clear();
                listStocks.clear();
//                new SendInfoTaskalluser().execute(new TaskParams(
//                        Constants.Url + "?app=public&mod=AppFeedList&act=usersearch",
//                        new String[]{"key", inputstr},
//                        new String[]{"p", String.valueOf(page)}
//                ));

                // 请求用户列表数据
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        jsonStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/search?key=" + inputstr + "&p=" + page);
                        handler.sendEmptyMessage(0);
                    }
                }).start();

                // 开线程调用股票查询接口
                new SearchStocks().execute(new TaskParams("http://suggest3.sinajs.cn/suggest/type=111&key="+inputstr));
            }
        });

        // 存储数据的数组列表
        listatfrientData = new ArrayList<UserEntity>(200);
        listStocks = new ArrayList<HashMap<String, Object>>(200);
        searchAdapter = new SearchAdapter(SearchActivity.this, SearchActivity.this);
        userList.setAdapter(searchAdapter);
        stocksAdapter = new StocksAdapter(SearchActivity.this);
        stosksList.setAdapter(stocksAdapter);
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

//    private class SendInfoTaskalluser extends AsyncTask<TaskParams, Void, String> {
//
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            if (result == null) {
//                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
////                // 千万别忘了告诉控件刷新完毕了哦！
////                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
//            } else {
//                super.onPostExecute(result);
//                result = result.replace("\n ", "");
//                result = result.replace("\n", "");
//                result = result.replace(" ", "");
//                result = "[" + result + "]";
//                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
//                for (Map<String, Object> map : lists) {
//                    if (map.get("data") == null) {
//
//                    } else {
//                        String datastr = map.get("data") + "";
//                        if (datastr.length() < 10){
//                            userLl.setVisibility(View.GONE);
//                            return;
//                        }
//                        userLl.setVisibility(View.VISIBLE);
//
//                        List<Map<String, Object>> datastrlists = JsonTools
//                                .listKeyMaps(datastr);
//
//                        for (Map<String, Object> datastrmap : datastrlists) {
//                            String uidstr = datastrmap.get("uid") + "";
//                            String unamestr = datastrmap.get("uname")
//                                    + "";
//                            String avatar_smallstr = datastrmap.get(
//                                    "avatar_small") + "";
//                            HashMap<String, Object> map2 = new HashMap<String, Object>();
//                            map2.put("uid", uidstr);
//                            map2.put("avatar_middlestr", avatar_smallstr);
//                            map2.put("unamestr", unamestr);
//                            listatfrientData.add(map2);
//                        }
//                    }
//                }
//                searchAdapter.setlistData(listatfrientData);
//                ViewUtil.setListViewHeightBasedOnChildren(userList);
////                // 千万别忘了告诉控件刷新完毕了哦！
////                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//            }
//        }
//    }

    private class SearchStocks extends AsyncTask<TaskParams, Void, String>{

        @Override
        protected String doInBackground(TaskParams... params) {
            TaskParams tp = params[0];
            return HttpUtil.getIntentData(tp.getUrl());
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            result = result.substring(result.indexOf("\"")+1);
            if (result.length() < 10){
                stocksLl.setVisibility(View.GONE);
                return;
            }
            stocksLl.setVisibility(View.VISIBLE);
            String[] strs = result.split(";");
            HashMap<String, Object> map3;
            for (String str:strs){
                map3 = new HashMap<String, Object>();
                String[] strlist = str.split(",");
                map3.put("code",strlist[2]);
                map3.put("name",strlist[4]);
                listStocks.add(map3);
            }
            stocksAdapter.setlistData(listStocks);
            ViewUtil.setListViewHeightBasedOnChildren(stosksList);
        }
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
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            // 请求失败的情况
                            CustomToast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        listatfrientData = (ArrayList<UserEntity>) JSON.parseArray(jsonObject.getString("data"),UserEntity.class);

                        if (listatfrientData.size() < 1){
                            userLl.setVisibility(View.GONE);
                            return;
                        }
                        userLl.setVisibility(View.VISIBLE);
                        searchAdapter.setlistData(listatfrientData);
                        ViewUtil.setListViewHeightBasedOnChildren(userList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

}
