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

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.stockmatchreportAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.CalendarUtil;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 赛程报道页面
 */
public class stockmatchreportActivity extends Activity {

    //获取控件
    private LinearLayout goback1;
    //定义List集合容器
    private stockmatchreportAdapter stockmatchreportAdapter;
    //定义于数据库同步的字段集合
    private ArrayList<HashMap<String, Object>> liststockmatchreportData;
    //名人集合
    private ListView stockmatchreportlistview;
    //访问页数控制
    private int current = 1;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.stockmatchreport_list);
        //将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
        // 自动下拉刷新
        ptrl.autoRefresh();
    }

    private void initView() {
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        goback1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /**名人集合*/
        stockmatchreportlistview = (ListView) findViewById(R.id.stockmatchreportlist2);
        //存储数据的数组列表
        liststockmatchreportData = new ArrayList<HashMap<String, Object>>(200);
        stockmatchreportAdapter = new stockmatchreportAdapter(stockmatchreportActivity.this);
        stockmatchreportlistview.setAdapter(stockmatchreportAdapter);
        stockmatchreportlistview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                try {
                    Intent intent = new Intent(stockmatchreportActivity.this, forumnotedetailActivity.class);
                    intent.putExtra("weibo_id", (String) liststockmatchreportData.get(arg2).get("weibo_id"));
                    startActivity(intent);
                } catch (Exception e) {
                    // TODO: handle exception
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
                //清空列表重载数据
                liststockmatchreportData.clear();
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

    private class SendInfoTasktodayuprankingloadmore extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                // 千万别忘了告诉控件刷新完毕了哦！
                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
            } else {
                List<Map<String, Object>> lists = null;
                String keysvaluemapstr = null;
                List<Map<String, Object>> keysvaluemaplists = null;
                String keysvaluemapstr2 = null;
                List<Map<String, Object>> keysvaluemaplists2 = null;
                super.onPostExecute(result);
                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                //解析json字符串获得List<Map<String,Object>>
                if (lists == null) {
                    lists = JsonTools.listKeyMaps(result);
                }
                for (Map<String, Object> map : lists) {
                    if (keysvaluemapstr == null) {
                        keysvaluemapstr = map.get("data") + "";
                        keysvaluemaplists = JsonTools.listKeyMaps("[" + keysvaluemapstr + "]");
                    }
                    for (Map<String, Object> keysvaluemap : keysvaluemaplists) {
                        if (keysvaluemapstr2 == null) {
                            keysvaluemapstr2 = keysvaluemap.get("data") + "";
                            keysvaluemaplists2 = JsonTools.listKeyMaps(keysvaluemapstr2);
                        }
                        for (Map<String, Object> keysvaluemap2 : keysvaluemaplists2) {
                            //标题
                            String course_titlestr = keysvaluemap2.get("course_title") + "";
//                            //链接地址
                            String course_urlstr = keysvaluemap2.get("course_url") + "";
                            String weibo_id = course_urlstr.substring(course_urlstr.lastIndexOf("/") + 1);
                            //创建时间
                            String create_timestr = keysvaluemap2.get("create_time") + "";
//                            //是否显示
//                            String statusstr = keysvaluemap2.get("status")+"";
                            create_timestr = CalendarUtil.formatDateTime(Utils.getStringtoDate(create_timestr));
                            HashMap<String, Object> map2 = new HashMap<String, Object>();
                            map2.put("course_titlestr", course_titlestr);
                            map2.put("create_timestr", create_timestr);
                            map2.put("weibo_id", weibo_id);
                            liststockmatchreportData.add(map2);
                        }
                    }
                }
                stockmatchreportAdapter.setlistData(liststockmatchreportData);
                // 千万别忘了告诉控件刷新完毕了哦！
                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
            }
        }
    }

    private void geneItems() {
        new SendInfoTasktodayuprankingloadmore().execute(new TaskParams(Constants.Url + "?app=public&mod=AppFeedList&act=AppNewsBallot&p=" + String.valueOf(current)
                )
        );
    }
}
