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
import com.example.sjqcjstock.Activity.Article.ArticleDetailsActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.stockmatchreportAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.Article.RaceReportEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 赛程报道页面
 */
public class stockmatchreportActivity extends Activity {

    //定义List集合容器
    private stockmatchreportAdapter stockmatchreportAdapter;
    //定义于数据库同步的字段集合
    private ArrayList<RaceReportEntity> liststockmatchreportData;
    //名人集合
    private ListView stockmatchreportlistview;
    //访问页数控制
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
        setContentView(R.layout.stockmatchreport_list);
        //将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
        geneItems();
    }

    private void initView() {
        dialog = new CustomProgress(this);
        dialog.showDialog();
        findViewById(R.id.goback1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /**名人集合*/
        stockmatchreportlistview = (ListView) findViewById(R.id.stockmatchreportlist2);
        stockmatchreportAdapter = new stockmatchreportAdapter(stockmatchreportActivity.this);
        stockmatchreportlistview.setAdapter(stockmatchreportAdapter);
        stockmatchreportlistview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                try {
                    Intent intent = new Intent(stockmatchreportActivity.this, ArticleDetailsActivity.class);
                    intent.putExtra("weibo_id", liststockmatchreportData.get(arg2).getFeed_id());
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

//    private class SendInfoTasktodayuprankingloadmore extends AsyncTask<TaskParams, Void, String> {
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
//                // 千万别忘了告诉控件刷新完毕了哦！
//                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
//            } else {
//                List<Map<String, Object>> lists = null;
//                String keysvaluemapstr = null;
//                List<Map<String, Object>> keysvaluemaplists = null;
//                String keysvaluemapstr2 = null;
//                List<Map<String, Object>> keysvaluemaplists2 = null;
//                super.onPostExecute(result);
//                result = result.replace("\n ", "");
//                result = result.replace("\n", "");
//                result = result.replace(" ", "");
//                result = "[" + result + "]";
//                //解析json字符串获得List<Map<String,Object>>
//                if (lists == null) {
//                    lists = JsonTools.listKeyMaps(result);
//                }
//                for (Map<String, Object> map : lists) {
//                    if (keysvaluemapstr == null) {
//                        keysvaluemapstr = map.get("data") + "";
//                        keysvaluemaplists = JsonTools.listKeyMaps("[" + keysvaluemapstr + "]");
//                    }
//                    for (Map<String, Object> keysvaluemap : keysvaluemaplists) {
//                        if (keysvaluemapstr2 == null) {
//                            keysvaluemapstr2 = keysvaluemap.get("data") + "";
//                            keysvaluemaplists2 = JsonTools.listKeyMaps(keysvaluemapstr2);
//                        }
//                        for (Map<String, Object> keysvaluemap2 : keysvaluemaplists2) {
//                            //标题
//                            String course_titlestr = keysvaluemap2.get("course_title") + "";
////                            //链接地址
//                            String course_urlstr = keysvaluemap2.get("course_url") + "";
//                            String weibo_id = course_urlstr.substring(course_urlstr.lastIndexOf("/") + 1);
//                            //创建时间
//                            String create_timestr = keysvaluemap2.get("create_time") + "";
////                            //是否显示
////                            String statusstr = keysvaluemap2.get("status")+"";
//                            create_timestr = CalendarUtil.formatDateTime(Utils.getStringtoDate(create_timestr));
//                            HashMap<String, Object> map2 = new HashMap<String, Object>();
//                            map2.put("course_titlestr", course_titlestr);
//                            map2.put("create_timestr", create_timestr);
//                            map2.put("weibo_id", weibo_id);
//                            liststockmatchreportData.add(map2);
//                        }
//                    }
//                }
//                stockmatchreportAdapter.setlistData(liststockmatchreportData);
//                // 千万别忘了告诉控件刷新完毕了哦！
//                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//            }
//        }
//    }

    private void geneItems() {
//        new SendInfoTasktodayuprankingloadmore().execute(new TaskParams(Constants.Url + "?app=public&mod=AppFeedList&act=AppNewsBallot&p=" + String.valueOf(current)
//                )
//        );
        // 赛程报道
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonStr = HttpUtil.restHttpGet(Constants.newUrl+"/api/ballot/news?p="+current);
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
                            CustomToast.makeText(stockmatchreportActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                            return;
                        }
                        ArrayList<RaceReportEntity> raceReportEntities = (ArrayList<RaceReportEntity>) JSON.parseArray(jsonObject.getString("data"),RaceReportEntity.class);
                        if (1 == current){
                            liststockmatchreportData = raceReportEntities;
                        }else{
                            liststockmatchreportData.addAll(raceReportEntities);
                        }

                        stockmatchreportAdapter.setlistData(liststockmatchreportData);
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
