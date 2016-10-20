package com.example.sjqcjstock.Activity.match;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

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

public class totalessencerankinglistActivity extends Activity {


    //获取控件
    private LinearLayout goback1;

    //定义List集合容器


    //hotstockAdapter hotstockAdapter;

    com.example.sjqcjstock.adapter.matchadapter.totalrankingsessenceAdapter totalrankingsessenceAdapter;


    //定义于数据库同步的字段集合
    //private String[] name;
    //ArrayList<HashMap<String,Object>> listhotstockData;
    ArrayList<HashMap<String, Object>> totalrankingsessenceData;

    //获取我是否已关注用户的标识


    //名人集合
    //精英集合
    ListView totalrankingsessencelistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.totalessencerankingl_list);

        //将Activity反复链表
        ExitApplication.getInstance().addActivity(this);

        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub

        goback1 = (LinearLayout) findViewById(R.id.goback1);

        goback1.setOnClickListener(new goback1_listener());


        /**精英集合*/
        totalrankingsessencelistview = (ListView) findViewById(R.id.totalrankingsessencelist);


        //存储数据的数组列表
        totalrankingsessenceData = new ArrayList<HashMap<String, Object>>();


        /**
         for(int i=0;i<3;i++){
         HashMap<String,Object> map=new HashMap<String, Object>();

         //添加数据
         listessencematchData.add(map);
         }

         */

        //为ListView 添加适配器

        totalrankingsessenceAdapter = new com.example.sjqcjstock.adapter.matchadapter.totalrankingsessenceAdapter(totalessencerankinglistActivity.this, totalrankingsessenceData);

        totalrankingsessencelistview.setAdapter(totalrankingsessenceAdapter);

        //解决ScrollView和ListView的冲突问题
        // setListViewHeightBasedOnChildren(essencelistview);


        /**
         famousmanlistview.setOnItemClickListener(new OnItemClickListener() {

        @Override public void onItemClick(AdapterView<?> parent, View v, int position,
        long id) {
        // TODO Auto-generated method stub
        CustomToast.makeText(selectstockmatchActivity.this, "weibo_titlestr:"+(String)listfamousmanmatchData.get(position).get("weibo_titlestr")+","+
        "uidstr:"+(String)listfamousmanmatchData.get(position).get("uidstr")+","+
        "weibo_idstr:"+(String)listfamousmanmatchData.get(position).get("weibo_idstr"), Toast.LENGTH_SHORT).show();

        Intent intent=new Intent(selectstockmatchActivity.this,notedetailActivity.class);
        intent.putExtra("weibo_id", (String)listfamousmanmatchData.get(position).get("weibo_idstr"));
        intent.putExtra("uid", (String)listfamousmanmatchData.get(position).get("uidstr"));

        startActivity(intent);
        }


        });
         */
        new SendInfoTask().execute(new TaskParams(Constants.Url + "?app=public&mod=AppFeedList&act=AppBallot"));

    }


    class goback1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            finish();
        }

    }


    private class SendInfoTask extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }


        List<Map<String, Object>> lists;

        String statusstr;

        List<Map<String, Object>> statusstrlists;

        String jy_max_ballotstr;

        List<Map<String, Object>> jy_max_ballotstrlists;

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
            } else {
                super.onPostExecute(result);
                //CustomToast.makeText(supermanlistActivity.this, result, 1).show();

                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                //解析json字符串获得List<Map<String,Object>>

                if (lists == null) {
                    lists = JsonTools.listKeyMaps(result);
                }
                for (Map<String, Object> map : lists) {
                    if (statusstr == null) {
                        statusstr = map.get("data") + "";
                        statusstrlists = JsonTools.listKeyMaps("[" + statusstr + "]");
                    }
                    for (Map<String, Object> statusstrmap : statusstrlists) {
                        //String weekmapstr= statusstrmap.get("week")+"";


                        //名人总积分榜
                        if (jy_max_ballotstr == null) {

                            jy_max_ballotstr = statusstrmap.get("jy_max_ballot") + "";
                            jy_max_ballotstrlists = JsonTools.listKeyMaps(jy_max_ballotstr);
                        }
                        int g = 0;


                        for (Map<String, Object> jy_max_ballotstrmap : jy_max_ballotstrlists) {

                            g++;

                            //荐股id

                            String ballot_idstr = jy_max_ballotstrmap.get("ballot_id") + "";
                            //用户id
                            String uidstr = jy_max_ballotstrmap.get("uid") + "";
                            //股票编码
                            String sharesstr = jy_max_ballotstrmap.get("shares") + "";
                            //股票名字
                            String shares_namestr = jy_max_ballotstrmap.get("shares_name") + "";

                            //第二只股票编码
                            String shares2str = jy_max_ballotstrmap.get("shares2") + "";
                            //第二只股票的名称
                            String shares2_namestr = jy_max_ballotstrmap.get("shares2_name") + "";

                            //当前价1
                            String pricestr = jy_max_ballotstrmap.get("price") + "";
                            //当前价2
                            String price2str = jy_max_ballotstrmap.get("price2") + "";

                            //第1只最高涨幅
                            String integration3str = jy_max_ballotstrmap.get("integration1") + "";

                            //第2只最高涨幅
                            String integration4str = jy_max_ballotstrmap.get("integration2") + "";

                            //周积分
                            String list_pricestr = jy_max_ballotstrmap.get("integration") + "";

                            //总积分
                            String ballot_jifenstr = jy_max_ballotstrmap.get("ballot_jifen") + "";

                            String unamestr = jy_max_ballotstrmap.get("uname") + "";

                            String weeklystr = jy_max_ballotstrmap.get("weekly") + "";


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
                            map2.put("rankingcount", String.valueOf(g));


                            totalrankingsessenceData.add(map2);
                            //						if(g==3){
                            //							break;
                            //						}
                        }


                    }


                }
                totalrankingsessenceAdapter.notifyDataSetChanged();

            }

        }

    }

}
