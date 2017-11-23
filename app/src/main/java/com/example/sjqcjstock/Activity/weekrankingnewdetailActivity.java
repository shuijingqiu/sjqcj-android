package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.content.Intent;
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
import com.example.sjqcjstock.adapter.weekrankingdetailsessenceAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.MatchNewsEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 周收益详细列表(名人组 精英组)
 */
public class weekrankingnewdetailActivity extends Activity {

    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    private LinearLayout goback1;
    // 定义List集合容器
    private weekrankingdetailsessenceAdapter weekrankingdetailAdapter;
    private ArrayList<MatchNewsEntity> weekrankingdetaildata;
    private ListView weekrankinglist2;
    // 获取intent数据 比赛周数和分组类型
    private String matchstr;
    private String typestr;
    // 网络请求提示
    private CustomProgress dialog;
    // 分页数
    private int page = 1;
    //接口返回数据
    private String jsonStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weekrankingnewdetail_list);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
        getData();
    }

    private void initView() {
        dialog = new CustomProgress(weekrankingnewdetailActivity.this);
        dialog.showDialog();

        // 获取intent数据
        Intent intent = getIntent();
        matchstr = intent.getStringExtra("match");
        // 名人组还是精英组
        typestr = intent.getStringExtra("type");
        TextView titleName = (TextView) findViewById(R.id.title_name);
        if (typestr.equals("1")) {
            titleName.setText("名人组");
        } else {
            titleName.setText("精英组");
        }

        goback1 = (LinearLayout) findViewById(R.id.goback1);
        goback1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /** 牛人集合 */
        weekrankinglist2 = (ListView) findViewById(R.id.weekrankinglist2);
        // 为ListView 添加适配器

        weekrankingdetailAdapter = new weekrankingdetailsessenceAdapter(weekrankingnewdetailActivity.this);
        weekrankinglist2.setAdapter(weekrankingdetailAdapter);
        ptrl = ((PullToRefreshLayout) findViewById(
                R.id.refresh_view));
        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下来刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                weekrankingdetaildata.clear();
                page = 1;
                getData();
            }

            // 上拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                page += 1;
                getData();
            }
        });
    }

    /**
     * 调用接口获取数据
     */
    private void getData() {
//        new SendInfoTask()
//                .execute(new TaskParams(
//                                Constants.Url + "?app=public&mod=AppFeedList&act=AppListWeekBallot&macth="
//                                        + matchstr
//                                        + "&type="
//                                        + typestr
//                                        + "&p="
//                                        + page
//                        )
//                );

        // 获取周排行
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/ballot/week?macth="+matchstr+"&type="+typestr+"&p="+page);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

//    private class SendInfoTask extends AsyncTask<TaskParams, Void, String> {
//
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            if (result == null) {
//                CustomToast.makeText(getApplicationContext(), "", 1)
//                        .show();
//                dialog.dismissDlog();
//                // 千万别忘了告诉控件刷新完毕了哦！
//                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//            } else {
//                super.onPostExecute(result);
//                List<Map<String, Object>> lists = null;
//                String datastr = null;
//                List<Map<String, Object>> datastrlists = null;
//
//                result = result.replace("\n ", "");
//                result = result.replace("\n", "");
//                result = result.replace(" ", "");
//                result = "[" + result + "]";
//                // 解析json字符串获得List<Map<String,Object>>
//                if (lists == null) {
//                    lists = JsonTools.listKeyMaps(result);
//                }
//                for (Map<String, Object> map : lists) {
//                    if (datastr == null) {
//                        datastr = map.get("data") + "";
//                        datastrlists = JsonTools.listKeyMaps(datastr);
//                    }
//                    for (Map<String, Object> datastrmap : datastrlists) {
//                        String pricestr;
//                        String price2str;
//                        // 用户id
//                        String uidstr = datastrmap.get("uid") + "";
//                        // 股票名字
//                        String shares_namestr = datastrmap.get("shares_name")
//                                + "";
//                        String shares2_namestr = datastrmap.get("shares2_name") + "";
//                        //第一只股票编码
//                        String shares = datastrmap.get("shares") + "";
//                        //第二只股票编码
//                        String shares2 = datastrmap.get("shares2") + "";
//
//                        // 当前价1
//                        if (datastrmap.get("list_ballot_price") == null) {
//                            pricestr = "0.00";
//                        } else {
//                            pricestr = datastrmap.get("list_ballot_price")
//                                    + "";
//                            pricestr = Utils.getNumberFormat(pricestr);
//                        }
//                        // 当前价2
//                        if (datastrmap.get("list_ballot_price2") == null) {
//                            price2str = "0.00";
//                        } else {
//                            price2str = datastrmap.get("list_ballot_price2")
//                                    + "";
//                            price2str = Utils.getNumberFormat(price2str);
//                        }
//                        // 第1只最高涨幅
//                        String integration3str = datastrmap.get("integration1")
//                                + "";
//                        integration3str = Utils.getNumberFormat(integration3str);
//
//                        // 第2只最高涨幅
//                        String integration4str = datastrmap.get("integration2")
//                                + "";
//                        integration4str = Utils.getNumberFormat(integration4str);
//
//                        // 第1只最高涨幅
//                        String integration1str = datastrmap.get("integration3")
//                                + "";
//                        integration1str = Utils.getNumberFormat(integration1str);
//                        // 最2只最高涨幅
//                        String integration2str = datastrmap.get("integration4")
//                                + "";
//                        integration2str = Utils.getNumberFormat(integration2str);
//
//                        // 周收益
//                        String list_pricestr = datastrmap.get("integration")
//                                + "";
//                        String unamestr;
//                        if (datastrmap.get("uname") == null) {
//                            unamestr = "暂无";
//                        } else {
//                            unamestr = datastrmap.get("uname") + "";
//                        }
//                        HashMap<String, Object> map2 = new HashMap<String, Object>();
//                        map2.put("uname", unamestr);
//                        map2.put("shares_name", shares_namestr);
//                        map2.put("shares2_name", shares2_namestr);
//                        map2.put("shares", Utils.jieQuSharesCode(shares));
//                        map2.put("shares2", Utils.jieQuSharesCode(shares2));
//                        map2.put("list_price", list_pricestr);
//                        map2.put("price", pricestr);
//                        map2.put("price2", price2str);
//                        map2.put("integration3", integration3str);
//                        map2.put("integration4", integration4str);
//
//                        map2.put("integration1", integration1str);
//                        map2.put("integration2", integration2str);
//                        map2.put("uidimg",
//                                Md5Util.getuidstrMd5(Md5Util.getMd5(uidstr)));
//                        map2.put("uid", uidstr);
//                        weekrankingdetaildata.add(map2);
//                    }
//                }
//                dialog.dismissDlog();
//                weekrankingdetailAdapter.setlistData(weekrankingdetaildata);
//                // 千万别忘了告诉控件刷新完毕了哦！
//                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
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
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            // 请求失败的情况
                            CustomToast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            dialog.dismissDlog();
                            return;
                        }
                        ArrayList<MatchNewsEntity> matchNewsEntities = (ArrayList<MatchNewsEntity>) JSON.parseArray(jsonObject.getString("data"),MatchNewsEntity.class);
                        if (1 == page){
                            weekrankingdetaildata = matchNewsEntities;
                        }else{
                            weekrankingdetaildata.addAll(matchNewsEntities);
                        }
                        weekrankingdetailAdapter.setlistData(weekrankingdetaildata);

                        // 千万别忘了告诉控件刷新完毕了哦！
                        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.dismissDlog();
                    break;
            }
        }
    };

}
