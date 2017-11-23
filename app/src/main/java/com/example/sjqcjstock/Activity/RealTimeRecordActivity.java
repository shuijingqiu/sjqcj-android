package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.Activity.Article.ArticleDetailsActivity;
import com.example.sjqcjstock.Activity.Tomlive.DirectBroadcastingRoomActivity;
import com.example.sjqcjstock.Activity.plan.PlanExhibitionActivity;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.realTimeRecordAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.NoticeEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 实时战绩页面
 * Created by Administrator on 2017/4/28.
 */
public class RealTimeRecordActivity extends Activity{

    // 网络请求提示
    private CustomProgress dialog;
    // 跑马灯实时战绩广告
    private ArrayList<NoticeEntity> noticeEntitys;
    // 定义List集合容器
    private realTimeRecordAdapter adapter;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private ListView listView;
    //访问页数控制
    private int current = 1;
    // 接口返回数据
    private String jsonStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_real_time_record);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        findView();
        getData();
    }

    /**
     * 页面控件绑定
     */
    private void findView() {
        dialog = new CustomProgress(this);
        dialog.showDialog();
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter = new realTimeRecordAdapter(this);
        listView = (ListView) findViewById(
                R.id.list_view);
        listView.setAdapter(adapter);
        noticeEntitys = new ArrayList<NoticeEntity>();
        ptrl = ((PullToRefreshLayout) findViewById(
                R.id.refresh_view));
        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下来刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                //清空列表重载数据
                noticeEntitys.clear();
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NoticeEntity noticeEntity = noticeEntitys.get(position);
                String type = noticeEntity.getType();
                // 0 链接 1微博 2 个人主页 3 交易 4 直播 5 投资计划
                String uid = noticeEntity.getParam().getUid();
                Intent intent = null;
                if ("1".equals(type)){
                    // 文章打开
                    intent = new Intent(RealTimeRecordActivity.this, ArticleDetailsActivity.class);
                    // 文章的ID
                    intent.putExtra("weibo_id",noticeEntity.getParam().getFeed_id());
                }else if ("2".equals(type)){
                    // 个人主页 微博
                    intent = new Intent(RealTimeRecordActivity.this, UserDetailNewActivity.class);
                    intent.putExtra("uid", uid);
                }else if ("3".equals(type)){
                    // 个人主页 交易
                    intent = new Intent(RealTimeRecordActivity.this, UserDetailNewActivity.class);
                    intent.putExtra("uid", uid);
                    intent.putExtra("type", "1");
                }else if ("4".equals(type)) {
                    //  直播
                    intent = new Intent(RealTimeRecordActivity.this, DirectBroadcastingRoomActivity.class);
                    // 房间ID
                    intent.putExtra("roomId",noticeEntity.getParam().getRoom_id());
                    // 用户ID
                    intent.putExtra("uid",uid);
                }else if ("5".equals(type)) {
                    //  投资计划
                    intent = new Intent(RealTimeRecordActivity.this, PlanExhibitionActivity.class);
                    // 计划id
                    intent.putExtra("id",noticeEntity.getParam().getDp_id());
                    // 用户ID
                    intent.putExtra("uid",uid);
                }else{
                    String url = noticeEntity.getUrl();
                    if (Utils.isWebsite(url)){
                        Uri uri = Uri.parse(url);
                        intent = new Intent(Intent.ACTION_VIEW, uri);
                    }
                }
                startActivity(intent);
            }
        });

    }

    private void getData() {
        // 加载实时战绩
//        new SendInfoRealTimeRecord().execute(new TaskParams(
//                Constants.Url + "?app=public&mod=AppFeedList&act=news&p="+current));

        // 实时战绩列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonStr = HttpUtil.restHttpGet(Constants.newUrl+"/api/news/gains?p="+current);
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

//    /**
//     * 加载广告和实时战绩的数据
//     */
//    private class SendInfoRealTimeRecord extends AsyncTask<TaskParams, Void, String> {
//
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            if (result == null) {
//                // 千万别忘了告诉控件刷新完毕了哦！
//                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
//                dialog.dismissDlog();
//                return;
//            } else {
//                super.onPostExecute(result);
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    if ("failed".equals(jsonObject.getString("status"))) {
//                        // 千万别忘了告诉控件刷新完毕了哦！
//                        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//                        dialog.dismissDlog();
//                        return;
//                    }
//                    if (current == 1){
//                        List<NoticeEntity> noticeEntityContends =  JSON.parseArray(jsonObject.getString("contend"), NoticeEntity.class);
//                        noticeEntitys.addAll(noticeEntityContends);
//                    }
//                    List<NoticeEntity> noticeEntityNews = JSON.parseArray(jsonObject.getString("news"), NoticeEntity.class);
//                    noticeEntitys.addAll(noticeEntityNews);
//                    adapter.setlistData(noticeEntitys);
//                    // 千万别忘了告诉控件刷新完毕了哦！
//                    ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//                    dialog.dismissDlog();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        dialog.dismissDlog();
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            CustomToast.makeText(RealTimeRecordActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            // 千万别忘了告诉控件刷新完毕了哦！加载失败
                            ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                            return;
                        }
                        ArrayList<NoticeEntity> noticeEntityContends = (ArrayList<NoticeEntity>) JSON.parseArray(jsonObject.getString("contend"),NoticeEntity.class);
                        if (current == 1){
                            noticeEntitys = noticeEntityContends;
                        }else{
                            noticeEntitys.addAll(noticeEntityContends);
                        }
                        List<NoticeEntity> noticeEntityNews = JSON.parseArray(jsonObject.getString("news"), NoticeEntity.class);
                        noticeEntitys.addAll(noticeEntityNews);

                        adapter.setlistData(noticeEntitys);
                        // 千万别忘了告诉控件刷新完毕了哦！
                        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
}
