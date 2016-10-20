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

import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.myfansuserAdapter;
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
 * 关注我的用户页面
 */
public class myfansActivity extends Activity {

    private LinearLayout goback1;
    //定义List集合容器
    private myfansuserAdapter myfansuserAdapter;
    //定义于数据库同步的字段集合
    private ArrayList<HashMap<String, Object>> myfansuserData;
    private ListView myfansuserlistview;
    //加载更多
    //访问页数控制
    private int current = 2;
    //刷新列表
    private String isreferlist = "1";
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 获取intent里的uid
    private String uidstr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_myfanslist);
        //将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
        // 自动下拉刷新
        ptrl.autoRefresh();
    }

    private void initView() {
        // 获取intent里的uidstr
        Intent intent = getIntent();
        uidstr = intent.getStringExtra("uidstr");
        if (uidstr == null || "".equals(uidstr.trim())) {
            uidstr = Constants.staticmyuidstr;
        }
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        goback1.setOnClickListener(new goback1_listener());
        /**我的粉丝集合*/
        myfansuserlistview = (ListView) findViewById(R.id.myfanslist2);
        //存储数据的数组列表
        myfansuserData = new ArrayList<HashMap<String, Object>>(200);
        //为ListView 添加适配器
        myfansuserAdapter = new myfansuserAdapter(myfansActivity.this);
        myfansuserlistview.setAdapter(myfansuserAdapter);
        myfansuserlistview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(myfansActivity.this, UserDetailNewActivity.class);
                intent.putExtra("uid", myfansuserData.get(arg2 - 1).get("uid").toString());
                startActivity(intent);
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
            finish();
        }
    }

    private void geneItems() {
        new SendInfoTaskmyfansloadmore().execute(new TaskParams(Constants.Url + "?app=public&mod=AppFeedList&act=AppFollower",
                        new String[]{"mid", uidstr},
                        new String[]{"login_password", Constants.staticpasswordstr},
                        new String[]{"p", String.valueOf(current)}
                )
        );
    }

    private class SendInfoTaskmyfansloadmore extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                // 千万别忘了告诉控件刷新完毕了哦！加载失败
                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
            } else {
                if ("1".equals(isreferlist)) {
                    myfansuserData.clear();
                }
                super.onPostExecute(result);
                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                //解析json字符串获得List<Map<String,Object>>
                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
                for (Map<String, Object> map : lists) {
                    String datastr = map.get("data") + "";
                    List<Map<String, Object>> datastrlists = JsonTools.listKeyMaps("[" + datastr + "]");
                    for (Map<String, Object> datastrmap : datastrlists) {
                        String followGroupListstr = datastrmap.get("followerList") + "";
                        List<Map<String, Object>> followGroupListstrlists = JsonTools.listKeyMaps("[" + followGroupListstr + "]");

                        for (Map<String, Object> followGroupListstrmap : followGroupListstrlists) {
                            String data2str = followGroupListstrmap.get("data") + "";
                            List<Map<String, Object>> data2strlists = JsonTools.listKeyMaps(data2str);
                            for (Map<String, Object> data2strmap : data2strlists) {
                                String introstr;

                                String uidstr = data2strmap.get("uid") + "";
                                String unamestr = data2strmap.get("uname") + "";
                                if (data2strmap.get("intro") == null) {
                                    introstr = "暂无简介";
                                } else {
                                    introstr = data2strmap.get("intro") + "";
                                }
                                String avatar_middlestr = data2strmap.get("avatar_middle") + "";
                                HashMap<String, Object> map2 = new HashMap<String, Object>();
                                String userGroup = data2strmap.get("user_group") + "";
                                map2.put("isVip", userGroup);
                                map2.put("uid", uidstr);
                                map2.put("uname", unamestr);
                                map2.put("intro", introstr);
                                map2.put("avatar_middle", avatar_middlestr);
                                myfansuserData.add(map2);
                            }
                        }
                    }
                }
                myfansuserAdapter.setlistData(myfansuserData);
                // 千万别忘了告诉控件刷新完毕了哦！加载失败
                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
            }
        }

    }

}
