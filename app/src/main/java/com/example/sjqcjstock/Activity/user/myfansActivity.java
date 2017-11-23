package com.example.sjqcjstock.Activity.user;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.myfansuserAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.Article.UserEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 关注我的用户页面
 */
public class myfansActivity extends Activity {

    //定义List集合容器
    private myfansuserAdapter myfansuserAdapter;
    //定义于数据库同步的字段集合
    private ArrayList<UserEntity> myfansuserData;
    private ListView myfansuserlistview;
    // 标题
    private TextView titleTv;
    //加载更多
    //访问页数控制
    private int current = 1;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 获取intent里的uid
    private String uidstr;
    // 接口返回数据
    private String jsonStr;
    // 是关注还是粉丝 following 关注 follower粉丝
    private String type;
    // 网络请求提示
    private CustomProgress dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_myfanslist);
        //将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
        geneItems();
    }

    private void initView() {
        dialog = new CustomProgress(this);
        dialog.showDialog();
        titleTv = (TextView) findViewById(R.id.title_tv);
        // 获取intent里的uidstr
        Intent intent = getIntent();
        uidstr = intent.getStringExtra("uidstr");
        type = intent.getStringExtra("type");
        if (uidstr == null || "".equals(uidstr.trim())) {
            uidstr = Constants.staticmyuidstr;
        }
        if ("following".equals(type)){
            titleTv.setText("关注");
        }else{
            titleTv.setText("粉丝");
        }

        findViewById(R.id.goback1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /**我的粉丝集合*/
        myfansuserlistview = (ListView) findViewById(R.id.myfanslist2);
        //存储数据的数组列表
        myfansuserData = new ArrayList<UserEntity>();
        //为ListView 添加适配器
        myfansuserAdapter = new myfansuserAdapter(myfansActivity.this);
        myfansuserlistview.setAdapter(myfansuserAdapter);
        myfansuserlistview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(myfansActivity.this, UserDetailNewActivity.class);
                intent.putExtra("uid", myfansuserData.get(arg2 - 1).getUid());
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
                // 加载请求数据
                geneItems();
            }

            // 下拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                current++;
                // 加载请求数据
                geneItems();
            }
        });
    }

    private void geneItems() {
//        new SendInfoTaskmyfansloadmore().execute(new TaskParams(Constants.Url + "?app=public&mod=AppFeedList&act=AppFollower",
//                        new String[]{"mid", uidstr},
//                        new String[]{"login_password", Constants.staticpasswordstr},
//                        new String[]{"p", String.valueOf(current)}
//                )
//        );
        // 获取关注用户列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/user/following?uid=" + uidstr+"&p="+current+"&type="+type);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

//    private class SendInfoTaskmyfansloadmore extends AsyncTask<TaskParams, Void, String> {
//
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            if (result == null) {
//                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
//                // 千万别忘了告诉控件刷新完毕了哦！加载失败
//                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
//            } else {
//                if ("1".equals(isreferlist)) {
//                    myfansuserData.clear();
//                }
//                super.onPostExecute(result);
//                result = result.replace("\n ", "");
//                result = result.replace("\n", "");
//                result = result.replace(" ", "");
//                result = "[" + result + "]";
//                //解析json字符串获得List<Map<String,Object>>
//                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
//                for (Map<String, Object> map : lists) {
//                    String datastr = map.get("data") + "";
//                    List<Map<String, Object>> datastrlists = JsonTools.listKeyMaps("[" + datastr + "]");
//                    for (Map<String, Object> datastrmap : datastrlists) {
//                        String followGroupListstr = datastrmap.get("followerList") + "";
//                        List<Map<String, Object>> followGroupListstrlists = JsonTools.listKeyMaps("[" + followGroupListstr + "]");
//
//                        for (Map<String, Object> followGroupListstrmap : followGroupListstrlists) {
//                            String data2str = followGroupListstrmap.get("data") + "";
//                            List<Map<String, Object>> data2strlists = JsonTools.listKeyMaps(data2str);
//                            for (Map<String, Object> data2strmap : data2strlists) {
//                                // 360 改
//                                if (data2strmap==null){
//                                    continue;
//                                }
//                                String introstr;
//                                String uidstr = data2strmap.get("uid") + "";
//                                String unamestr = data2strmap.get("uname") + "";
//                                if (data2strmap.get("intro") == null) {
//                                    introstr = "暂无简介";
//                                } else {
//                                    introstr = data2strmap.get("intro") + "";
//                                }
//                                String avatar_middlestr = data2strmap.get("avatar_middle") + "";
//                                HashMap<String, Object> map2 = new HashMap<String, Object>();
//                                String userGroup = data2strmap.get("user_group") + "";
//                                map2.put("isVip", userGroup);
//                                map2.put("uid", uidstr);
//                                map2.put("uname", unamestr);
//                                map2.put("intro", introstr);
//                                map2.put("avatar_middle", avatar_middlestr);
//                                myfansuserData.add(map2);
//                            }
//                        }
//                    }
//                }
//                myfansuserAdapter.setlistData(myfansuserData);
//                // 千万别忘了告诉控件刷新完毕了哦！加载失败
//                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//            }
//        }
//    }

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
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            CustomToast.makeText(myfansActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            // 千万别忘了告诉控件刷新完毕了哦！加载失败
                            ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                            return;
                        }
                        ArrayList<UserEntity> userEntityList = (ArrayList<UserEntity>) JSON.parseArray(jsonObject.getString("data"),UserEntity.class);
                        if (1== current){
                            myfansuserData = userEntityList;
                        }else{
                            myfansuserData.addAll(userEntityList);
                        }
                        myfansuserAdapter.setlistData(myfansuserData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // 千万别忘了告诉控件刷新完毕了哦！加载失败
                    ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    break;
            }
        }
    };

}
