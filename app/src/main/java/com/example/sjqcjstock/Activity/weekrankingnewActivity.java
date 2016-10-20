package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.view.CustomToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 周积分列表新
 */
public class weekrankingnewActivity extends Activity {

    private LinearLayout goback1;
    // 定义于数据库同步的字段集合
    private ListView weekrankinglist2;
    private ArrayList<HashMap<String, Object>> listweekrankingData;
    private com.example.sjqcjstock.adapter.weekrankingdirAdapter weekrankingdirAdapter;
    // 网络请求提示
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weekrankingnew_list);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
    }

    private void initView() {
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        goback1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialog = new ProgressDialog(weekrankingnewActivity.this);
        dialog.setMessage(Constants.loadMessage);
        dialog.setCancelable(true);
        dialog.show();

        /** 牛人集合 */
        weekrankinglist2 = (ListView) findViewById(R.id.weekrankinglist2);
        // 存储数据的数组列表
        listweekrankingData = new ArrayList<HashMap<String, Object>>(200);
        // 为ListView 添加适配器
        weekrankingdirAdapter = new com.example.sjqcjstock.adapter.weekrankingdirAdapter(
                weekrankingnewActivity.this, listweekrankingData);

        weekrankinglist2.setAdapter(weekrankingdirAdapter);
        weekrankinglist2.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
            }
        });

        new SendInfoTaskweekrankinglist()
                .execute(new TaskParams(
                                Constants.Url + "?app=public&mod=AppFeedList&act=AppBallotWeek"
                        )
                );
    }

    private class SendInfoTaskweekrankinglist extends
            AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "", 1)
                        .show();
                dialog.dismiss();
            } else {
                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                // 解析json字符串获得List<Map<String,Object>>
                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);

                for (Map<String, Object> map : lists) {
                    String datastr = map.get("data") + "";

                    Integer weeklycount = Integer.parseInt(datastr);

                    for (int i = 0; i < weeklycount; i++) {
                        HashMap<String, Object> map2 = new HashMap<String, Object>();

                        // 添加数据
                        map2.put("weekly", String.valueOf(i + 1));
                        // 从头部插入
                        // listpersonalnewsData.add(0,map2);
                        listweekrankingData.add(0, map2);
                    }
                }
                dialog.dismiss();
                weekrankingdirAdapter.notifyDataSetChanged();
            }
        }
    }
}
