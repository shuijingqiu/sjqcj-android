package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.moodsrankingAdapter;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.Md5Util;
import com.example.sjqcjstock.netutil.TaskParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class moodsrankingActivity extends Activity {

    private ImageView goback1;

    // 定义List集合容器

    // hotstockAdapter hotstockAdapter;

    moodsrankingAdapter moodsrankingAdapter;

    // 定义于数据库同步的字段集合
    // private String[] name;
    // ArrayList<HashMap<String,Object>> listhotstockData;
    ArrayList<HashMap<String, Object>> moodsrankingData;

    // 获取我是否已关注用户的标识

    // 名人集合
    // 精英集合
    ListView moodsrankinglistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.moodsranking_list);

        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub
        goback1 = (ImageView) findViewById(R.id.goback1);

        goback1.setOnClickListener(new goback1_listener());

        /** 精英集合 */
        moodsrankinglistview = (ListView) findViewById(R.id.moodsrankinglist);

        // 存储数据的数组列表
        moodsrankingData = new ArrayList<HashMap<String, Object>>();

        /**
         * for(int i=0;i<3;i++){ HashMap<String,Object> map=new HashMap<String,
         * Object>();
         *
         * //添加数据 listessencematchData.add(map); }
         */

        // 为ListView 添加适配器

        moodsrankingAdapter = new moodsrankingAdapter(
                moodsrankingActivity.this, moodsrankingData);

        moodsrankinglistview.setAdapter(moodsrankingAdapter);

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
        String uuid = UUID.randomUUID() + "";
        new SendInfoTask().execute(new TaskParams(
                        Constants.Url + "?app=public&mod=AppFeedList&act=AppBallot"
                        // new String[] { "login_email", "1061550505@qq.com" },
                        // new String[] { "login_password", "12345678" },
                        // new String[] { "position", "2"}

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

    private class SendInfoTask extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            result = result.replace("\n ", "");
            result = result.replace("\n", "");
            result = result.replace(" ", "");
            result = "[" + result + "]";
            // 解析json字符串获得List<Map<String,Object>>
            List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
            for (Map<String, Object> map : lists) {
                String statusstr = map.get("data") + "";
                List<Map<String, Object>> statusstrlists = JsonTools
                        .listKeyMaps("[" + statusstr + "]");
                for (Map<String, Object> statusstrmap : statusstrlists) {
                    // String weekmapstr= statusstrmap.get("week")+"";

                    // 名人总积分榜
                    String mr_dx_ballotstr = statusstrmap.get(
                            "mr_top_fans_ballot") + "";
                    List<Map<String, Object>> mr_dx_ballotstrlists = JsonTools
                            .listKeyMaps(mr_dx_ballotstr);

                    int j = 0;

                    for (Map<String, Object> mr_dx_ballotstrmap : mr_dx_ballotstrlists) {

                        j++;

                        String fansstr = mr_dx_ballotstrmap.get("fans")
                                + "";
                        String uidstr = mr_dx_ballotstrmap.get("uid")
                                + "";
                        String unamestr = mr_dx_ballotstrmap.get("uname")
                                + "";
                        // String uidstr=
                        // mr_dx_ballotstrmap.get("uid")+"";
                        // String uidstr=
                        // mr_dx_ballotstrmap.get("uid")+"";

                        uidstr = Md5Util.getMd5(uidstr);

                        uidstr = Md5Util.getuidstrMd5(uidstr);

                        HashMap<String, Object> map2 = new HashMap<String, Object>();
                        map2.put("fansstr", fansstr);
                        map2.put("uidimg", uidstr);
                        // map2.put("shares2_name", shares2_namestr);
                        map2.put("unamestr", unamestr);
                        // map2.put("price2", price2str);
                        // map2.put("integration4", integration4str);

                        map2.put("rankingcount", String.valueOf(j));

                        moodsrankingData.add(map2);

                    }

                    moodsrankingAdapter.notifyDataSetChanged();

                    // stubrankingitemAdapter.notifyDataSetChanged();

                }

            }

        }

    }

}
