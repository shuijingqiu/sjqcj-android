package com.example.sjqcjstock.Activity.match;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.essencematchAdapter;
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

public class essenceweekrankingActivity extends Activity {

    // 获取控件
    private LinearLayout goback1;

    // 定义List集合容器

    // hotstockAdapter hotstockAdapter;

    essencematchAdapter essencematchAdapter;

    // 定义于数据库同步的字段集合
    // private String[] name;
    // ArrayList<HashMap<String,Object>> listhotstockData;
    ArrayList<HashMap<String, Object>> listessencematchData;

    // 获取我是否已关注用户的标识

    // 名人集合
    // 精英集合
    ListView essencelistview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.sesenceweekranking_list);

        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);

        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub

        goback1 = (LinearLayout) findViewById(R.id.goback1);
        goback1.setOnClickListener(new goback1_listener());

        /** 精英集合 */
        essencelistview = (ListView) findViewById(R.id.sesencelist2);

        // 存储数据的数组列表
        listessencematchData = new ArrayList<HashMap<String, Object>>();

        /**
         * for(int i=0;i<3;i++){ HashMap<String,Object> map=new HashMap<String,
         * Object>();
         *
         * //添加数据 listessencematchData.add(map); }
         */

        // 为ListView 添加适配器

        essencematchAdapter = new essencematchAdapter(
                essenceweekrankingActivity.this, listessencematchData);

        essencelistview.setAdapter(essencematchAdapter);

        // 解决ScrollView和ListView的冲突问题
        // setListViewHeightBasedOnChildren(essencelistview);

        /**
         * famousmanlistview.setOnItemClickListener(new OnItemClickListener() {
         *
         * @Override public void onItemClick(AdapterView<?> parent, View v, int
         *           position, long id) { // TODO Auto-generated method stub
         *           CustomToast.makeText(selectstockmatchActivity.this,
         *           "weibo_titlestr:"
         *           +(String)listfamousmanmatchData.get(position
         *           ).get("weibo_titlestr")+","+
         *           "uidstr:"+(String)listfamousmanmatchData
         *           .get(position).get("uidstr")+","+
         *           "weibo_idstr:"+(String)listfamousmanmatchData
         *           .get(position).get("weibo_idstr"),
         *           Toast.LENGTH_SHORT).show();
         *
         *           Intent intent=new
         *           Intent(selectstockmatchActivity.this,notedetailActivity
         *           .class); intent.putExtra("weibo_id",
         *           (String)listfamousmanmatchData
         *           .get(position).get("weibo_idstr")); intent.putExtra("uid",
         *           (String)listfamousmanmatchData.get(position).get("uidstr"))
         *           ;
         *
         *           startActivity(intent); }
         *
         *
         *           });
         */

        new SendInfoTask().execute(new TaskParams(
                        Constants.Url + "?app=public&mod=AppFeedList&act=AppBallot"
                )
        );

    }

    class goback1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            finish();
        }

    }

    List<Map<String, Object>> lists = null;

    String statusstr = null;

    List<Map<String, Object>> statusstrlists = null;

    String ballot1str = null;

    List<Map<String, Object>> ballot1strlists = null;

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
            } else {
                super.onPostExecute(result);
                // CustomToast.makeText(supermanlistActivity.this, result, 1).show();

                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                // 解析json字符串获得List<Map<String,Object>>
                if (lists == null) {
                    lists = JsonTools.listKeyMaps(result);
                }
                for (Map<String, Object> map : lists) {
                    if (statusstr == null) {
                        statusstr = map.get("data") + "";
                        statusstrlists = JsonTools.listKeyMaps("[" + statusstr
                                + "]");
                    }
                    for (Map<String, Object> statusstrmap : statusstrlists) {
                        // String weekmapstr=
                        // statusstrmap.get("week")+"";

                        // 精英组数据
                        if (ballot1str == null) {
                            ballot1str = statusstrmap.get("ballot1") + "";

                            ballot1strlists = JsonTools.listKeyMaps(ballot1str);
                        }
                        int j = 0;
                        for (Map<String, Object> ballot1strmap : ballot1strlists) {

                            j++;

                            String pricestr;
                            String price2str;
                            // 荐股id

                            String ballot_idstr = ballot1strmap
                                    .get("ballot_id") + "";
                            // 用户id
                            String uidstr = ballot1strmap.get("uid") + "";
                            // 股票编码
                            String sharesstr = ballot1strmap.get("shares")
                                    + "";
                            // 股票名字
                            String shares_namestr = ballot1strmap.get(
                                    "shares_name") + "";

                            // 第二只股票编码
                            String shares2str = ballot1strmap.get("shares2")
                                    + "";
                            // 第二只股票的名称
                            String shares2_namestr = ballot1strmap.get(
                                    "shares2_name") + "";

                            if (ballot1strmap.get("price") == null) {
                                pricestr = "0";
                            } else {
                                // 当前价1

                                pricestr = ballot1strmap.get("price")
                                        + "";
                            }

                            if (ballot1strmap.get("price2") == null) {
                                price2str = "0";
                            } else {
                                // 当前价2
                                price2str = ballot1strmap.get("price2")
                                        + "";
                            }

                            // 第1只最高涨幅
                            String integration3str = ballot1strmap.get(
                                    "integration1") + "";

                            // 第2只最高涨幅
                            String integration4str = ballot1strmap.get(
                                    "integration2") + "";

                            // 周积分
                            String list_pricestr = ballot1strmap.get(
                                    "integration") + "";

                            // 总积分
                            String ballot_jifenstr = ballot1strmap.get(
                                    "ballot_jifen") + "";

                            String unamestr = ballot1strmap.get("uname")
                                    + "";

                            String weeklystr = ballot1strmap.get("weekly")
                                    + "";

                            integration3str = integration3str + "%";
                            integration4str = integration4str + "%";

                            uidstr = Md5Util.getMd5(uidstr);

                            uidstr = Md5Util.getuidstrMd5(uidstr);

                            HashMap<String, Object> map2 = new HashMap<String, Object>();
                            map2.put("weekly", weeklystr);

                            map2.put("uname", unamestr);
                            map2.put("shares_name", shares_namestr);
                            map2.put("shares2_name", shares2_namestr);
                            map2.put("list_price", list_pricestr);
                            map2.put("ballot_jifen", ballot_jifenstr);
                            map2.put("price", pricestr);
                            map2.put("price2", price2str);
                            map2.put("integration3", integration3str);
                            map2.put("integration4", integration4str);
                            map2.put("uidimg", uidstr);
                            map2.put("rankingcount", String.valueOf(j));

                            listessencematchData.add(map2);

                        }

                    }

                }

                essencematchAdapter.notifyDataSetChanged();
                // setListViewHeightBasedOnChildren(essencelistview);
                // 名人组数据

            }

        }

    }

}
