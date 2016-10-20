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
import com.example.sjqcjstock.netutil.Md5Util;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.view.CustomToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 当周牛股榜列表页
 */
public class weekbullsrankingActivity extends Activity {

    private LinearLayout goback1;
    // 当日涨幅榜列表集合
    private com.example.sjqcjstock.adapter.thisweekuprankingAdapter thisweekuprankingAdapter;
    private ArrayList<HashMap<String, String>> listthisweekuprankingData;
    // 当日牛股榜集合
    private ListView thisweekuprankinglist2;
    // 网络请求提示
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weekbullsranking_list);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
    }

    private void initView() {
        dialog = new ProgressDialog(weekbullsrankingActivity.this);
        dialog.setMessage(Constants.loadMessage);
        dialog.setCancelable(true);
        dialog.show();
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        goback1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /** 当周牛股榜集合 */
        thisweekuprankinglist2 = (ListView) findViewById(R.id.thisweekuprankinglist2);
        // 存储数据的数组列表
        listthisweekuprankingData = new ArrayList<HashMap<String, String>>(200);
        // 为ListView 添加适配器
        thisweekuprankingAdapter = new com.example.sjqcjstock.adapter.thisweekuprankingAdapter(
                getApplicationContext(), listthisweekuprankingData);
        thisweekuprankinglist2.setAdapter(thisweekuprankingAdapter);
        thisweekuprankinglist2
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {

                    }
                });
        geneItems();
    }

    private class SendInfoTaskthisweekuprankingloadmore extends
            AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "", 1)
                        .show();
            } else {
                listthisweekuprankingData.clear();
                super.onPostExecute(result);
                List<Map<String, Object>> lists2 = null;
                String datastr2 = null;
                List<Map<String, Object>> datastrlists2 = null;

                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                // 解析json字符串获得List<Map<String,Object>>
                if (lists2 == null) {
                    lists2 = JsonTools.listKeyMaps(result);
                }

                for (Map<String, Object> map : lists2) {
                    if (datastr2 == null) {
                        datastr2 = map.get("data") + "";

                        datastrlists2 = JsonTools.listKeyMaps(datastr2);
                    }

                    if (datastrlists2 == null) {
                        datastrlists2 = new ArrayList<Map<String, Object>>();
                    }
                    for (Map<String, Object> datastrmap : datastrlists2) {

                        // 涨幅
                        String increasestr = datastrmap.get("increase") + "";
                        // 最新价
                        String currentPricestr = datastrmap.get("currentPrice") + "";

                        // 股票名
                        String ballot_namestr;
                        if (datastrmap.get("ballot_name") == null) {
                            ballot_namestr = "暂无";
                        } else {
                            ballot_namestr = datastrmap.get("ballot_name")
                                    + "";
                        }
                        // 用户名
                        String unamestr = datastrmap.get("uname") + "";

                        String uidstr = datastrmap.get("uid") + "";
                        HashMap<String, String> map2 = new HashMap<String, String>();

                        map2.put("increase", increasestr);
                        map2.put("currentPrice", currentPricestr);

                        map2.put("ballot_name", ballot_namestr);
                        map2.put("uname", unamestr);
                        map2.put("uidimg",
                                Md5Util.getuidstrMd5(Md5Util.getMd5(uidstr)));
                        map2.put("uid", uidstr);
                        listthisweekuprankingData.add(map2);
                    }
                }
                thisweekuprankingAdapter.notifyDataSetChanged();
            }
            dialog.dismiss();
        }
    }

    private void geneItems() {
        // 不用分页吗？请奇怪 mh
        new SendInfoTaskthisweekuprankingloadmore().execute(new TaskParams(
                        Constants.Url + "?app=public&mod=AppFeedList&act=AppTodayBallot&group=week"
                )
        );
    }

}
