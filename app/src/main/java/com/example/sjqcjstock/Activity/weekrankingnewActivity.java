package com.example.sjqcjstock.Activity;

import android.app.Activity;
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

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.weekrankingdirAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 周收益列表新
 */
public class weekrankingnewActivity extends Activity {

    private LinearLayout goback1;
    // 定义于数据库同步的字段集合
    private ListView weekrankinglist2;
    private ArrayList<HashMap<String, Object>> listweekrankingData;
    private weekrankingdirAdapter adapter;
    // 网络请求提示
    private CustomProgress dialog;
    // 接口返回数据
    private String jsonStr;

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
        dialog = new CustomProgress(weekrankingnewActivity.this);
        dialog.showDialog();

        /** 牛人集合 */
        weekrankinglist2 = (ListView) findViewById(R.id.weekrankinglist2);
        // 存储数据的数组列表
        listweekrankingData = new ArrayList<HashMap<String, Object>>();
        // 为ListView 添加适配器
        adapter = new weekrankingdirAdapter(
                weekrankingnewActivity.this, listweekrankingData);

        weekrankinglist2.setAdapter(adapter);
        weekrankinglist2.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
            }
        });

//        new SendInfoTaskweekrankinglist()
//                .execute(new TaskParams(
//                                Constants.Url + "?app=public&mod=AppFeedList&act=AppBallotWeek"
//                        )
//                );
        // 获取当前周数
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/ballot/weekly?1=1");
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

//    private class SendInfoTaskweekrankinglist extends
//            AsyncTask<TaskParams, Void, String> {
//
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            if (result == null) {
//                CustomToast.makeText(getApplicationContext(), "", 1)
//                        .show();
//                dialog.dismissDlog();
//            } else {
//                result = result.replace("\n ", "");
//                result = result.replace("\n", "");
//                result = result.replace(" ", "");
//                result = "[" + result + "]";
//                // 解析json字符串获得List<Map<String,Object>>
//                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
//
//                for (Map<String, Object> map : lists) {
//                    String datastr = map.get("data") + "";
//
//                    Integer weeklycount = Integer.parseInt(datastr);
//
//                    for (int i = 0; i < weeklycount; i++) {
//                        HashMap<String, Object> map2 = new HashMap<String, Object>();
//
//                        // 添加数据
//                        map2.put("weekly", String.valueOf(i + 1));
//                        // 从头部插入
//                        // listpersonalnewsData.add(0,map2);
//                        listweekrankingData.add(0, map2);
//                    }
//                }
//                dialog.dismissDlog();
//                adapter.notifyDataSetChanged();
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
                            // 请求失败的情况
                            CustomToast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int week = jsonObject.getInt("data");
                        for (int i = 1; i <= week; i++) {
                            HashMap<String, Object> map2 = new HashMap<String, Object>();
                            // 添加数据
                            map2.put("weekly", String.valueOf(i));
                            // 从头部插入
                            listweekrankingData.add(0, map2);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

}
