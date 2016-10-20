package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.weekrankingdetailsessenceAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.Md5Util;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.view.CustomToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 周积分详细列表(名人组)
 */
public class weekrankingnewdetailActivity extends Activity {

    private LinearLayout goback1;
    // 定义List集合容器
    private weekrankingdetailsessenceAdapter weekrankingdetailAdapter;
    private ArrayList<HashMap<String, Object>> weekrankingdetaildata;
    private ListView weekrankinglist2;
    // 获取intent数据 比赛周数和分组类型
    private String matchstr;
    private String typestr;
    // 网络请求提示
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weekrankingnewdetail_list);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
    }

    private void initView() {
        dialog = new ProgressDialog(weekrankingnewdetailActivity.this);
        dialog.setMessage(Constants.loadMessage);
        dialog.setCancelable(true);
        dialog.show();

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
        // 存储数据的数组列表
        weekrankingdetaildata = new ArrayList<HashMap<String, Object>>(200);
        // 为ListView 添加适配器

        weekrankingdetailAdapter = new weekrankingdetailsessenceAdapter(
                weekrankingnewdetailActivity.this, weekrankingdetaildata);
        weekrankinglist2.setAdapter(weekrankingdetailAdapter);
        weekrankinglist2.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
            }
        });
        new SendInfoTask()
                .execute(new TaskParams(
                                Constants.Url + "?app=public&mod=AppFeedList&act=AppListWeekBallot&macth="
                                        + matchstr
                                        + "&type="
                                        + typestr
                        )
                );
    }

    private class SendInfoTask extends AsyncTask<TaskParams, Void, String> {

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
                dialog.dismiss();
            } else {
                super.onPostExecute(result);
                List<Map<String, Object>> lists = null;
                String datastr = null;
                List<Map<String, Object>> datastrlists = null;

                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                // 解析json字符串获得List<Map<String,Object>>
                if (lists == null) {
                    lists = JsonTools.listKeyMaps(result);
                }
                for (Map<String, Object> map : lists) {
                    if (datastr == null) {
                        datastr = map.get("data") + "";
                        if (datastr.equals("[]")) {
                            CustomToast.makeText(getApplicationContext(), "暂无数据", 1)
                                    .show();
                        }
                        datastrlists = JsonTools.listKeyMaps(datastr);
                    }
                    for (Map<String, Object> datastrmap : datastrlists) {
                        String pricestr;
                        String price2str;
                        // 荐股id
                        // String ballot_idstr=
                        // ballot2strmap.get("ballot_id")+"";
                        // 用户id
                        String uidstr = datastrmap.get("uid") + "";
                        // 股票编码
                        // String sharesstr=
                        // ballot2strmap.get("shares")+"";
                        // 股票名字
                        // String shares_namestr=
                        // ballot2strmap.get("shares_name")+"";

                        // 第二只股票编码
                        // String shares2str=
                        // ballot2strmap.get("shares2")+"";
                        // 第二只股票的名称
                        // String shares2_namestr=
                        // ballot2strmap.get("shares2_name")+"";

                        // String weekly= datastrmap.get("weekly")+"";
                        // 股票名字
                        String shares_namestr = datastrmap.get("shares_name")
                                + "";
                        String shares2_namestr = datastrmap.get("shares2_name")
                                + "";

                        // String
                        // shares_namestr=datastrmap.get("shares_reason")+"";
                        // String
                        // shares2_namestr=datastrmap.get("shares2_reason")+"";

                        // 当前价1
                        if (datastrmap.get("list_ballot_price") == null) {
                            pricestr = "0.00";
                        } else {
                            pricestr = datastrmap.get("list_ballot_price")
                                    + "";
                            pricestr = Utils.getNumberFormat(pricestr);
                        }
                        // 当前价2
                        if (datastrmap.get("list_ballot_price2") == null) {
                            price2str = "0.00";
                        } else {
                            price2str = datastrmap.get("list_ballot_price2")
                                    + "";
                            price2str = Utils.getNumberFormat(price2str);
                        }
                        // 第1只最高涨幅
                        String integration3str = datastrmap.get("integration1")
                                + "";
                        integration3str = Utils.getNumberFormat(integration3str);

                        // 第2只最高涨幅
                        String integration4str = datastrmap.get("integration2")
                                + "";
                        integration4str = Utils.getNumberFormat(integration4str);

                        // 第1只最高涨幅
                        String integration1str = datastrmap.get("integration3")
                                + "";
                        integration1str = Utils.getNumberFormat(integration1str);
                        // 最2只最高涨幅
                        String integration2str = datastrmap.get("integration4")
                                + "";
                        integration2str = Utils.getNumberFormat(integration2str);

                        // 周积分
                        String list_pricestr = datastrmap.get("integration")
                                + "";

                        // //总积分
                        // String ballot_jifenstr=
                        // datastrmap.get("ballot_jifen")+"";
                        String unamestr;
                        if (datastrmap.get("uname") == null) {
                            unamestr = "暂无";
                        } else {
                            unamestr = datastrmap.get("uname") + "";
                        }

                        HashMap<String, Object> map2 = new HashMap<String, Object>();
                        // map2.put("weekly",matchstr);

                        map2.put("uname", unamestr);
                        // map2.put("shares_name",datastrmap.get("shares_name").toString());
                        // map2.put("shares2_name",
                        // datastrmap.get("shares2_name").toString());
                        map2.put("shares_name", shares_namestr);
                        map2.put("shares2_name", shares2_namestr);
                        map2.put("list_price", list_pricestr);
                        // map2.put("ballot_jifen", ballot_jifenstr);
                        map2.put("price", pricestr);
                        map2.put("price2", price2str);
                        map2.put("integration3", integration3str);
                        map2.put("integration4", integration4str);

                        map2.put("integration1", integration1str);
                        map2.put("integration2", integration2str);
                        map2.put("uidimg",
                                Md5Util.getuidstrMd5(Md5Util.getMd5(uidstr)));
                        map2.put("uid", uidstr);
                        weekrankingdetaildata.add(map2);
                    }
                }
                dialog.dismiss();
                weekrankingdetailAdapter.notifyDataSetChanged();
            }
        }
    }


}
