package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.thisweekuprankingAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.NiuguListEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 当周牛股榜列表页
 */
public class weekbullsrankingActivity extends Activity {

    private LinearLayout goback1;
    private TextView titleTv;
    // 当日涨幅榜列表集合
    private thisweekuprankingAdapter adapter;
    private ArrayList<NiuguListEntity> listthisweekuprankingData;
    // 当日牛股榜集合
    private ListView thisweekuprankinglist2;
    // 网络请求提示
    private CustomProgress dialog;
    // today日涨幅榜 week周涨幅榜
    private String type;
    // 榜单返回数据
    private String jsonStr;

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
        dialog = new CustomProgress(weekbullsrankingActivity.this);
        dialog.showDialog();
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        goback1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTv = (TextView) findViewById(R.id.title_tv);
        /** 当周牛股榜集合 */
        thisweekuprankinglist2 = (ListView) findViewById(R.id.thisweekuprankinglist2);
        // 为ListView 添加适配器
        adapter = new thisweekuprankingAdapter(this);
        thisweekuprankinglist2.setAdapter(adapter);
        type = getIntent().getStringExtra("type");
        if ("today".equals(type)){
            titleTv.setText("当日牛股榜");
        }
        geneItems();
    }

//    private class SendInfoTaskthisweekuprankingloadmore extends
//            AsyncTask<TaskParams, Void, String> {
//
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            if (result == null) {
//                CustomToast.makeText(getApplicationContext(), "", 1)
//                        .show();
//            } else {
//                listthisweekuprankingData.clear();
//                super.onPostExecute(result);
//                List<Map<String, Object>> lists2 = null;
//                String datastr2 = null;
//                List<Map<String, Object>> datastrlists2 = null;
//
//                result = result.replace("\n ", "");
//                result = result.replace("\n", "");
//                result = result.replace(" ", "");
//                result = "[" + result + "]";
//                // 解析json字符串获得List<Map<String,Object>>
//                if (lists2 == null) {
//                    lists2 = JsonTools.listKeyMaps(result);
//                }
//
//                for (Map<String, Object> map : lists2) {
//                    if (datastr2 == null) {
//                        datastr2 = map.get("data") + "";
//
//                        datastrlists2 = JsonTools.listKeyMaps(datastr2);
//                    }
//
//                    if (datastrlists2 == null) {
//                        datastrlists2 = new ArrayList<Map<String, Object>>();
//                    }
//                    for (Map<String, Object> datastrmap : datastrlists2) {
//
//                        // 涨幅
//                        String increasestr = datastrmap.get("increase") + "";
//                        // 最新价
//                        String currentPricestr = datastrmap.get("currentPrice") + "";
//
//                        // 股票名
//                        String ballot_namestr;
//                        if (datastrmap.get("ballot_name") == null) {
//                            ballot_namestr = "暂无";
//                        } else {
//                            ballot_namestr = datastrmap.get("ballot_name")
//                                    + "";
//                        }
//                        // 用户名
//                        String unamestr = datastrmap.get("uname") + "";
//                        // 股票代码
//                        String shares = datastrmap.get("shares")+"";
//                        String uidstr = datastrmap.get("uid") + "";
//                        HashMap<String, String> map2 = new HashMap<String, String>();
//                        // 是否显示股票
//                        String isFree = datastrmap.get("is_free")+"";
//                        map2.put("is_free", isFree);
//                        map2.put("increase", increasestr);
//                        map2.put("currentPrice", currentPricestr);
//                        map2.put("ballot_name", ballot_namestr);
//                        map2.put("shares", Utils.jieQuSharesCode(shares));
//                        map2.put("uname", unamestr);
//                        map2.put("uidimg",
//                                Md5Util.getuidstrMd5(Md5Util.getMd5(uidstr)));
//                        map2.put("uid", uidstr);
//                        listthisweekuprankingData.add(map2);
//                    }
//                }
//                thisweekuprankingAdapter.notifyDataSetChanged();
//            }
//            dialog.dismissDlog();
//        }
//    }

    private void geneItems() {

//        new SendInfoTaskthisweekuprankingloadmore().execute(new TaskParams(
//                        Constants.Url + "?app=public&mod=AppFeedList&act=AppTodayBallot&group=week&mid="+Constants.staticmyuidstr
//                )
//        );

        // 牛股榜
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonStr = HttpUtil.restHttpGet(Constants.newUrl+"/api/ballot/today?group="+type);
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
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        dialog.dismissDlog();
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            CustomToast.makeText(weekbullsrankingActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        listthisweekuprankingData = (ArrayList<NiuguListEntity>) JSON.parseArray(jsonObject.getString("data"),NiuguListEntity.class);
                        adapter.setlistData(listthisweekuprankingData);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

}
