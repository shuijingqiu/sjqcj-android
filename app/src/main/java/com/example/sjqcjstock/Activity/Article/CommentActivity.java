package com.example.sjqcjstock.Activity.Article;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.CommentAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.ACache;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.Article.CommentEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 收到和发出的评论
 * Created by Administrator on 2017/7/7.
 */
public class CommentActivity extends Activity {

    // 网络请求提示
    private CustomProgress dialog;
    private ListView xlistView2;
    private CommentAdapter commentAdapter;
    private ArrayList<CommentEntity> commentEntitieList;
    // 加载更多
    // 访问页数控制
    private int current = 1;
    // 缓存用的类
    private ACache mCache;
    // 缓存消息用
//        private ArrayList<HashMap<String, String>> myCommentList;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 标题
    private TextView titleTv;
    // 类型 1 为发送的评论 2 为收到的评论
    private String type;
    // 接口返回数据
    private String commentStr;

    public void referActivity() {
        current = 1;
        // 自动下拉刷新
        ptrl.autoRefresh();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_comment_list);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
        geneItems();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        dialog = new CustomProgress(this);
        dialog.showDialog();
        // 缓存类
        mCache = ACache.get(this);
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        type = getIntent().getStringExtra("type");
        titleTv = (TextView) findViewById(R.id.title_tv);
        xlistView2 = (ListView) findViewById(R.id.listView2);
        commentEntitieList = new ArrayList<CommentEntity>();
        commentAdapter = new CommentAdapter(CommentActivity.this, this, type);
        xlistView2.setAdapter(commentAdapter);

        ptrl = ((PullToRefreshLayout) findViewById(
                R.id.refresh_view));
        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下来刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                //清空列表重载数据
                commentEntitieList.clear();
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
        if ("receive".equals(type)) {
            // 收到的评论
            titleTv.setText("收到的评论");
        } else {
            // 发出的评论
            titleTv.setText("发出的评论");
        }
        // 先加载缓存
        String feedACache = mCache.getAsString(type + "mh");
        if (feedACache != null && !"".equals(feedACache)) {
            setData(feedACache);
        }
    }

    private void geneItems() {
        // 获取评论相关的信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                commentStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/comment/myList?mid=" + Constants.staticmyuidstr + "&token=" + Constants.apptoken + "&type=" + type + "&p=" + current);
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
                    setData(commentStr);
                    break;
            }
        }
    };

    /**
     * 解析绑定数据
     */
    private void setData(String data){
        try {
            dialog.dismissDlog();
            JSONObject jsonObject = new JSONObject(data);
            if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                // 请求失败的情况
                CustomToast.makeText(CommentActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                // 千万别忘了告诉控件刷新完毕了哦！失败
                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                return;
            }
            // 千万别忘了告诉控件刷新完毕了哦！
            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
            ArrayList<CommentEntity> commentEntities = (ArrayList<CommentEntity>) JSON.parseArray(jsonObject.getString("data"), CommentEntity.class);
            if (current == 1) {
                // 做缓存
                mCache.put(type+"mh", data);
                commentEntitieList = commentEntities;
            } else {
                commentEntitieList.addAll(commentEntities);
            }
            commentAdapter.setlistData(commentEntitieList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
