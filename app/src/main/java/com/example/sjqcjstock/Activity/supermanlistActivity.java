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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.supermanActivityAdapter;
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
 * 认证牛人列表页面
 */
public class supermanlistActivity extends Activity {
    private LinearLayout goback1;
    // 定义List集合容器
    private supermanActivityAdapter supermanAdapter;
    // 定义于数据库同步的字段集合
    private ArrayList<UserEntity> listsupermanData;
    private ListView supermanlistview;
    // 访问页数控制
    private int current = 1;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 接口返回数据
    private String jsonStr;
    // 网络请求提示
    private CustomProgress dialog;

//    public void test(ArrayList<UserEntity> listData) {
//        listsupermanData = listData;
//        supermanAdapter.notifyDataSetChanged();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.superman_list);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
        geneItems();
    }

    private void initView() {
        dialog = new CustomProgress(this);
        dialog.showDialog();
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        goback1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /** 牛人集合 */
        supermanlistview = (ListView) findViewById(R.id.supermanlist2);
        // 为ListView 添加适配器
        supermanAdapter = new supermanActivityAdapter(supermanlistActivity.this);
        supermanlistview.setAdapter(supermanAdapter);
        supermanlistview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                try {
                    Intent intent = new Intent(supermanlistActivity.this,
                            UserDetailNewActivity.class);
                    intent.putExtra("uid",
                            listsupermanData.get(arg2).getUid());
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
//                if (result == null) {
//                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT)
//                        .show();
//                // 千万别忘了告诉控件刷新完毕了哦！
//                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
//            } else {
//                super.onPostExecute(result);
//                result = result.replace("\n ", "");
//                result = result.replace("\n", "");
//                result = result.replace(" ", "");
//                result = "[" + result + "]";
//                // 解析json字符串获得List<Map<String,Object>>
//                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
//
//                for (Map<String, Object> map : lists) {
//
//                    if (map.get("data") == null) {
//                        CustomToast.makeText(getApplicationContext(), "",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                    }
//                    String statusstr = map.get("data") + "";
//                    List<Map<String, Object>> supermanlists = JsonTools
//                            .listKeyMaps(statusstr);
//                    for (Map<String, Object> supermanmap : supermanlists) {
//                        String introstr = "";
//                        String uidstr = "";
//                        String unamestr = "";
//                        String save_pathstr = "";
//
//                        if (supermanmap.get("uid") == null) {
//                            uidstr = "";
//                        } else {
//                            uidstr = supermanmap.get("uid") + "";
//                        }
//                        if (supermanmap.get("uname") == null) {
//                            unamestr = "该用户已不存在";
//                        } else {
//                            unamestr = supermanmap.get("uname") + "";
//                        }
//                        if (supermanmap.get("save_path") == null) {
//                            save_pathstr = "";
//                        } else {
//                            save_pathstr = supermanmap.get("save_path")
//                                    + "";
//                        }
//
//                        String followstr;
//                        if (supermanmap.get("follow") == null) {
//                            followstr = "";
//                        } else {
//                            followstr = supermanmap.get("follow") + "";
//                        }
//
//                        List<Map<String, Object>> followingstrlists = JsonTools
//                                .listKeyMaps("[" + followstr + "]");
//
//                        String followingstr = "";
//                        String followerstr = "";
//                        for (Map<String, Object> followingstrmap : followingstrlists) {
//                            followingstr = followingstrmap.get("following")
//                                    + "";
//                            followerstr = followingstrmap.get("follower")
//                                    + "";
//                        }
//
//                        if (supermanmap.get("intro") != null) {
//                            introstr = supermanmap.get("intro") + "";
//                        }
//                        HashMap<String, String> map2 = new HashMap<String, String>();
//                        String authentication = supermanmap.get("authentication") + "";
//                        map2.put("isVip", authentication);
//                        map2.put("username", unamestr);
//                        map2.put("uid", uidstr);
//                        map2.put("intro", introstr);
//                        map2.put("following", followingstr);
//                        map2.put("follower", followerstr);
//                        map2.put("image_url",
//                                "http://www.sjqcj.com/data/upload/avatar"
//                                        + save_pathstr + "original_200_200.jpg");
//                        listsupermanData.add(map2);
//                    }
//                }
//                supermanAdapter.setlistData(listsupermanData);
//                // 千万别忘了告诉控件刷新完毕了哦！
//                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//            }
//        }
//    }

    private void geneItems() {
//        new SendInfoTaskloadmore().execute(new TaskParams(
//                        Constants.Url + "?app=index&mod=Index&act=APPUserSort",
//                        // new String[] { "position", "2"},
//                        new String[]{"mid", Constants.staticmyuidstr},
//                        new String[]{"p", String.valueOf(current)}
//                )
//        );

        // 获取认证牛人的数据列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/user/auth?mid="+Constants.staticmyuidstr+"&p="+current);
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
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            // 请求失败的情况
                            CustomToast.makeText(supermanlistActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            // 千万别忘了告诉控件刷新完毕了哦！加载失败
                            ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                            return;
                        }

                        ArrayList<UserEntity> userEntityList = (ArrayList<UserEntity>) JSON.parseArray(jsonObject.getString("data"),UserEntity.class);
                        if (1== current){
                            listsupermanData = userEntityList;
                        }else{
                            listsupermanData.addAll(userEntityList);
                        }
                        supermanAdapter.setlistData(listsupermanData);

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
