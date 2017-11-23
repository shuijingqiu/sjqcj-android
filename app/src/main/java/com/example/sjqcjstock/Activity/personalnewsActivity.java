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
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.peronalnewslistAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.ACache;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.PrivateMessageEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 私信列表activity
 *
 * @author Administrator
 */
public class personalnewsActivity extends Activity {

    // 定义List集合容器
    private peronalnewslistAdapter personalnewslistAdapter;
    // 定义于数据库同步的字段集合
    private ArrayList<PrivateMessageEntity> privateMessageEntityArrayList;
    private ListView personalnewslistview;
    // 访问页数控制
    private int current = 1;
    //    // 刷新列表标识
//    private String isreferlist = "1";
    // 缓存用的类
    private ACache mCache;
    //    // 缓存信息用的值
//    private ArrayList<HashMap<String, String>> messageList;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 接口返回数据
    private String jsonStr;
    private String mCacheJsonStr;
    // 网络请求提示
    private CustomProgress dialog;

    @Override
    protected void onResume() {
        super.onResume();
//        isreferlist = "1";
        current = 1;
        geneItems();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.personalnews_list);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
        dialog = new CustomProgress(this);
        dialog.showDialog();
    }

    @Override
    protected void onDestroy() {
        if (null != mCacheJsonStr && !"".equals(mCacheJsonStr)) {
            // 做缓存
            mCache.put("Messagex", mCacheJsonStr);
        }
        super.onDestroy();
    }

    private void initView() {
        // 缓存类
        mCache = ACache.get(this);
        findViewById(R.id.goback1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        //私信列表集合
        personalnewslistview = (ListView) findViewById(R.id.sesencelist2);
        // 存储数据的数组列表
        privateMessageEntityArrayList = new ArrayList<PrivateMessageEntity>();
        personalnewslistAdapter = new peronalnewslistAdapter(personalnewsActivity.this);
        personalnewslistview.setAdapter(personalnewslistAdapter);
        personalnewslistview.setOnItemClickListener(new OnItemClickListener() {

            //第三个参数为position
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
            if (privateMessageEntityArrayList.size() < 1) {
                return;
            }
            try {
                PrivateMessageEntity privateMessageEntity = privateMessageEntityArrayList.get(arg2);
                Intent intent = new Intent(personalnewsActivity.this, OnlineServiceActivity.class);
                //列表编号list_id是从哪里获取的
                intent.putExtra("list_id", privateMessageEntity.getList_id());
                intent.putExtra("unamestr", privateMessageEntity.getUname());
                intent.putExtra("uidstr", privateMessageEntity.getUid());
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            }
        });
        jsonStr = mCache.getAsString("Messagex");
        if (null != jsonStr && !"".equals(jsonStr)) {
            handler.sendEmptyMessage(0);
        }
        personalnewslistAdapter.setlistData(privateMessageEntityArrayList);
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
        // 获取私信列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/message?mid=" + Constants.staticmyuidstr + "&token=" + Constants.apptoken + "&p=" + current);
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
                            CustomToast.makeText(personalnewsActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            // 千万别忘了告诉控件刷新完毕了哦！加载失败
                            ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                            return;
                        }
                        ArrayList<PrivateMessageEntity> privateMessageEntities = (ArrayList<PrivateMessageEntity>) JSON.parseArray(jsonObject.getString("data"), PrivateMessageEntity.class);
                        if (1 == current) {
                            privateMessageEntityArrayList = privateMessageEntities;
                            mCacheJsonStr = jsonStr;
                        } else {
                            privateMessageEntityArrayList.addAll(privateMessageEntities);
                        }
                        personalnewslistAdapter.setlistData(privateMessageEntityArrayList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // 千万别忘了告诉控件刷新完毕了哦！
                    ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    break;
            }
        }
    };
}
