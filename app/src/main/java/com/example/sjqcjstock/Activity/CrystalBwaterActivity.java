package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.content.Intent;
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
import com.example.sjqcjstock.Activity.qa.MyQuestionAnswerActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.CrystalBwaterAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.CrystalBwater;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 水晶币流动页面
 * Created by Administrator on 2016/5/6.
 */
public class CrystalBwaterActivity extends Activity {

    // 加载系统消息
    private ListView crystalBwaterListView;
    // 控制器
    private CrystalBwaterAdapter adapter;
    // 获取信息的实体类
    private List<CrystalBwater> crystalBwaterList;
    // 访问页数控制
    private int current = 1;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 接口返回数据
    private String jsonStr;
    // 网络请求提示
    private CustomProgress dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_crystal_bwater);
        //将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        findView();
        geneItems();
    }

    /**
     * 控件的绑定
     */
    private void findView() {
        dialog = new CustomProgress(this);
        dialog.showDialog();
        // 返回按钮
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter = new CrystalBwaterAdapter(CrystalBwaterActivity.this);
        crystalBwaterListView = (ListView) findViewById(R.id.crystal_bwater_list);
        crystalBwaterListView.setAdapter(adapter);
        crystalBwaterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                String source = crystalBwaterList.get(position).getSource();
                String operId = crystalBwaterList.get(position).getOper_id();
                String assistUid = crystalBwaterList.get(position).getAssist_uid();
                String uid = crystalBwaterList.get(position).getUid();
                if ("10".equals(source) || "11".equals(source)) {
                    // 10打赏微博  11微博被打赏
                    // 文章打开
                    intent = new Intent(CrystalBwaterActivity.this, ArticleDetailsActivity.class);
                    // 文章的ID
                    intent.putExtra("weibo_id", operId);
                } else if ("20".equals(source) || "21".equals(source)) {
                    // 20订阅直播  21直播被订阅
                    // 直播消息
                    intent = new Intent(CrystalBwaterActivity.this, DirectBroadcastingRoomActivity.class);
                    // 房间ID
                    intent.putExtra("roomId", operId);
                    if ("21".equals(source)) {
                        // 被订阅 傳自己id
                        // 用户ID
                        intent.putExtra("uid", uid);
                    }else{
                        // 用户ID
                        intent.putExtra("uid", assistUid);
                    }
                } else if ("30".equals(source) || "31".equals(source) || "32".equals(source) || "33".equals(source) || "34".equals(source)) {
                    //30提问  31回答提问  32提问退币 33查看问答 34问答被查看
                    // 问答消息
                    intent = new Intent(CrystalBwaterActivity.this, MyQuestionAnswerActivity.class);
                    intent.putExtra("name", Constants.userEntity.getUname());
                    intent.putExtra("intro", Constants.userEntity.getIntro());
                } else if ("40".equals(source) || "41".equals(source) || "42".equals(source) || "43".equals(source) || "44".equals(source) || "45".equals(source)) {
                    // 40计划保证金  41订阅计划  42保证金退款  43保证金分红  44计划被订阅 45投资退款
                            // 投资计划
                    intent = new Intent(CrystalBwaterActivity.this, PlanExhibitionActivity.class);
                    // 计划id
                    intent.putExtra("id", operId);
                    if("40".equals(source) || "42".equals(source) || "44".equals(source)){
                        // 用户ID
                        intent.putExtra("uid", uid);
                    }else{
                        // 用户ID
                        intent.putExtra("uid", assistUid);
                    }
                } else if ("60".equals(source) || "61".equals(source)) {
                    // 60查看选股  61选股被查看
                    // 跳转到选股微博页面
                    intent = new Intent(CrystalBwaterActivity.this, ArticleDetailsActivity.class);
                    // 文章的ID
                    intent.putExtra("weibo_id", operId);
                }
//                else {
//                    // 跳转到个人中心
//                    intent = new Intent(CrystalBwaterActivity.this, UserDetailNewActivity.class);
//                    intent.putExtra("uid", crystalBwaterList.get(position).getUid());
//                }
                if (intent != null) {
                    startActivity(intent);
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
//        new SendSystemMessageTask().execute(new TaskParams(Constants.Url+"?app=public&mod=AppFeedList&act=crystalBwater",
//                new String[]{"mid", Constants.staticmyuidstr}, new String[]{
//                "p", current + ""}));

        // 获取水晶币流水列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/credit/rewardList?mid=" + Constants.staticmyuidstr+"&token="+Constants.apptoken+"&p="+current);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

//    private class SendSystemMessageTask extends AsyncTask<TaskParams, Void, String> {
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            if (result == null) {
//                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT)
//                        .show();
//                // 千万别忘了告诉控件刷新完毕了哦！
//                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
//            } else {
//
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//
//                    if ("failed".equals(jsonObject.getString("status"))) {
//                        CustomToast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT)
//                                .show();
//                        // 千万别忘了告诉控件刷新完毕了哦！
//                        ptrl.refreshFinish(PullToRefreshLayout.FAIL);
//                        return;
//                    }
//
//                    List<CrystalBwater> list = JSON.parseArray(jsonObject.getString("msg"), CrystalBwater.class);
//                    if (current == 1) {
//                        crystalBwaterList = list;
//                    } else {
//                        crystalBwaterList.addAll(list);
//                    }
//                    adapter.setCrystalBwater(crystalBwaterList);
//                    // 千万别忘了告诉控件刷新完毕了哦！
//                    ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
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
                            CustomToast.makeText(CrystalBwaterActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            // 千万别忘了告诉控件刷新完毕了哦！加载失败
                            ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                            return;
                        }
                        ArrayList<CrystalBwater> crystalBwaters = (ArrayList<CrystalBwater>) JSON.parseArray(jsonObject.getString("data"),CrystalBwater.class);
                        if (1== current){
                            crystalBwaterList = crystalBwaters;
                        }else{
                            crystalBwaterList.addAll(crystalBwaters);
                        }
                        adapter.setCrystalBwater(crystalBwaterList);
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
