package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.essencematchAdapter;
import com.example.sjqcjstock.adapter.famousmanmatchAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.Md5Util;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.userdefined.MyScrollView;
import com.example.sjqcjstock.userdefined.RoundImageView;
import com.example.sjqcjstock.view.CustomToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 选股比赛页面
 */
public class selectstockmatchActivity extends Activity {

    private LinearLayout goback1;
    //定义List集合容器
    private famousmanmatchAdapter famousmanmatchAdapter;
    private essencematchAdapter essencematchAdapter;
    //定义于数据库同步的字段集合
    private ArrayList<HashMap<String, Object>> listfamousmanmatchData;
    private ArrayList<HashMap<String, Object>> listessencematchData;
    //名人集合
    private ListView famousmanlistview;
    //精英集合
    private ListView essencelistview;
    //获取控件
    private MyScrollView myScrollView;
    private RelativeLayout totop;
    private RoundImageView picktodayupranking1;
    private LinearLayout pinkselectstock1;
    private RoundImageView pickdiscussarea1;
    private RoundImageView pickthisweekranking1;
    private RoundImageView pickstockmatchreport1;
    private RoundImageView totalranking1;
    private RoundImageView weekranking1;
    private TextView famousmore1;
    private TextView essencemore1;
    private LinearLayout toplay02;
    //排行榜标题栏
    private LinearLayout pickessence;
    private LinearLayout pickfamous;
    //网络请求提示
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.selectstockmatch_list);
        //将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
    }

    private void initView() {
        dialog = new ProgressDialog(selectstockmatchActivity.this);
        //dialog.setTitle("提示信息");
        dialog.setMessage(Constants.loadMessage);
        dialog.setCancelable(true);
        dialog.show();

        // TODO Auto-generated method stub\

        toplay02 = (LinearLayout) findViewById(R.id.toplay02);
        toplay02.setFocusable(true);
        toplay02.setFocusableInTouchMode(true);

        goback1 = (LinearLayout) findViewById(R.id.goback1);
        myScrollView = (MyScrollView) findViewById(R.id.myScrollView);
        totop = (RelativeLayout) findViewById(R.id.totop);
        picktodayupranking1 = (RoundImageView) findViewById(R.id.picktodayupranking1);
        pickstockmatchreport1 = (RoundImageView) findViewById(R.id.pickstockmatchreport1);
        pinkselectstock1 = (LinearLayout) findViewById(R.id.pinkselectstock1);
        totalranking1 = (RoundImageView) findViewById(R.id.totalranking1);
        weekranking1 = (RoundImageView) findViewById(R.id.weekranking1);
        pickessence = (LinearLayout) findViewById(R.id.pickessence);
        pickfamous = (LinearLayout) findViewById(R.id.pickfamous);
        pickdiscussarea1 = (RoundImageView) findViewById(R.id.pickdiscussarea1);
        pickthisweekranking1 = (RoundImageView) findViewById(R.id.pickthisweekranking1);
        famousmore1 = (TextView) findViewById(R.id.famousmore1);
        essencemore1 = (TextView) findViewById(R.id.essencemore1);
        pickessence.setOnClickListener(new pickessence_listener());
        pickfamous.setOnClickListener(new pickfamous_listener());
        pinkselectstock1.setOnClickListener(new pinkselectstock1_listener());
        totalranking1.setOnClickListener(new totalranking1_listener());
        pickdiscussarea1.setOnClickListener(new pickdiscussarea1_listener());
        weekranking1.setOnClickListener(new weekranking1_listener());
        pickthisweekranking1.setOnClickListener(new pickthisweekranking1_listener());

        totop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                myScrollView.fullScroll(ScrollView.FOCUS_UP);

            }
        });
        //模拟按钮点击
        totop.performClick();

        goback1.setOnClickListener(new goback1_listener());
        picktodayupranking1.setOnClickListener(new picktodayupranking1_listener());
        pickstockmatchreport1.setOnClickListener(new pickstockmatchreport1_listener());


        /**名人集合*/
        famousmanlistview = (ListView) findViewById(R.id.famousmanlist);


        //存储数据的数组列表
        listfamousmanmatchData = new ArrayList<HashMap<String, Object>>();


        /**
         for(int i=0;i<3;i++){
         HashMap<String,Object> map=new HashMap<String, Object>();

         //添加数据
         listfamousmanmatchData.add(map);
         }

         */


        //为ListView 添加适配器

        famousmanmatchAdapter = new famousmanmatchAdapter(selectstockmatchActivity.this, listfamousmanmatchData);

        famousmanlistview.setAdapter(famousmanmatchAdapter);

        //解决ScrollView和ListView的冲突问题
        setListViewHeightBasedOnChildren(famousmanlistview);

        famousmanlistview.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub

            }

            //判断是否滑到到list顶部或底部
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                //滑到顶部
                if (firstVisibleItem == 0) {
                    //CustomToast.makeText(getActivity(), "滑到顶部", 1).show();
                    // Log.e("log", "滑到顶部");
                    //mListView.setAdapter(mAdapter);
                    //listcommonnoteData.clear();
                    //current=1;
                    //geneItems();
                    //onLoad();
                    //topnotelistview.setVisibility(View.VISIBLE);
                    //rankentra1.setVisibility(View.VISIBLE);
                    //rankentra2.setVisibility(View.VISIBLE);

                } else {
                    //rankentra1.setVisibility(View.GONE);
                    //rankentra2.setVisibility(View.GONE);

                }
//                if(visibleItemCount+firstVisibleItem==totalItemCount){
//                   // Log.e("log", "滑到底部");
//                	CustomToast.makeText(getActivity(), "滑到底部", 1).show();
//                	//geneItems();
//    				//onLoad();
//                }
            }
        });

        /**精英集合*/
        essencelistview = (ListView) findViewById(R.id.essencelist);
        //存储数据的数组列表
        listessencematchData = new ArrayList<HashMap<String, Object>>();
        essencematchAdapter = new essencematchAdapter(selectstockmatchActivity.this, listessencematchData);
        essencelistview.setAdapter(essencematchAdapter);
        //解决ScrollView和ListView的冲突问题
        setListViewHeightBasedOnChildren(essencelistview);
        new SendInfoTask().execute(new TaskParams(Constants.Url + "?app=public&mod=AppFeedList&act=AppBallot"));

    }

    @Override
    public void onStop() {
        super.onStop();
    }


    class weekranking1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(selectstockmatchActivity.this, weekrankingnewActivity.class);
            startActivity(intent);
        }
    }

    class pickdiscussarea1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(selectstockmatchActivity.this, discussareaActivity.class);
            startActivity(intent);

        }

    }

    class pickthisweekranking1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(selectstockmatchActivity.this, weekbullsrankingActivity.class);
            startActivity(intent);
        }
    }

    class totalranking1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(selectstockmatchActivity.this, totalrankingnewActivity.class);
            startActivity(intent);
        }
    }

    class pinkselectstock1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(selectstockmatchActivity.this, IwillselectstockActivity.class);
            startActivity(intent);
        }
    }

    class pickfamous_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(selectstockmatchActivity.this, famousmatchActivity.class);
            startActivity(intent);
        }
    }

    class pickessence_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(selectstockmatchActivity.this, essencematchActivity.class);
            startActivity(intent);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        //模拟按钮点击
        totop.performClick();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        //模拟按钮点击
        totop.performClick();
        myScrollView.fullScroll(ScrollView.FOCUS_UP);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //模拟按钮点击
        totop.performClick();
        myScrollView.fullScroll(ScrollView.FOCUS_UP);
    }

    /**
     * @param listView
     */

    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params =
                listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        params.height = totalHeight + 40;
        listView.setLayoutParams(params);
    }


    class pickstockmatchreport1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(selectstockmatchActivity.this, stockmatchreportActivity.class);
            startActivity(intent);
        }
    }

    class picktodayupranking1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(selectstockmatchActivity.this, todaybullsrankingActivity.class);
            startActivity(intent);
        }
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
            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "网络超时或无数据", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            } else {
                super.onPostExecute(result);
                List<Map<String, Object>> lists = null;
                String statusstr = null;
                List<Map<String, Object>> statusstrlists = null;
                String ballot1str = null;
                List<Map<String, Object>> ballot1strlists = null;
                String ballot2str = null;
                List<Map<String, Object>> ballot2strlists = null;
                if (lists == null) {
                    lists = JsonTools.listKeyMaps("[" + result + "]");
                }
                for (Map<String, Object> map : lists) {
                    if (statusstr == null) {
                        statusstr = map.get("data") + "";

                        statusstrlists = JsonTools.listKeyMaps("[" + statusstr + "]");
                    }
                    for (Map<String, Object> statusstrmap : statusstrlists) {
                        String weekstr = statusstrmap.get("week") + "";

                        famousmore1.setText(famousmore1.getText().toString() + "(第" + weekstr + "周)");
                        essencemore1.setText(essencemore1.getText().toString() + "(第" + weekstr + "周)");
                        //精英组数据
                        if (ballot1str == null) {
                            if (statusstrmap.get("ballot1") == null) {
                                break;
                            }
                            ballot1str = statusstrmap.get("ballot1") + "";
                            ballot1strlists = JsonTools.listKeyMaps(ballot1str);
                        }
                        int j = 0;
                        for (Map<String, Object> ballot1strmap : ballot1strlists) {

                            j++;
                            //荐股id
                            String pricestr;
                            String price2str;
                            //用户id
                            String uidstr = ballot1strmap.get("uid") + "";
                            //股票名字
                            String shares_namestr = ballot1strmap.get("shares_name") + "";
                            //第二只股票编码
                            String shares2str = ballot1strmap.get("shares2") + "";
                            //第二只股票的名称
                            String shares2_namestr = ballot1strmap.get("shares2_name") + "";

                            //当前价1
                            if (ballot1strmap.get("price") == null) {
                                pricestr = "0";
                            } else {

                                pricestr = ballot1strmap.get("price") + "";
                            }
                            //当前价2
                            if (ballot1strmap.get("price") == null) {
                                price2str = "0";
                            } else {
                                price2str = ballot1strmap.get("price2") + "";
                            }
                            //第1只周涨幅
                            String integration3str = ballot1strmap.get("integration1") + "";

                            //第2只周涨幅
                            String integration4str = ballot1strmap.get("integration2") + "";

                            //第1只最高涨幅
                            String integration1str = ballot1strmap.get("integration3") + "";

                            //最2只最高涨幅
                            String integration2str = ballot1strmap.get("integration4") + "";


                            //周积分
                            String list_pricestr = ballot1strmap.get("integration") + "";

                            //总积分
                            String ballot_jifenstr = ballot1strmap.get("ballot_jifen") + "";

                            String unamestr = ballot1strmap.get("uname") + "";

                            String weekly = ballot1strmap.get("weekly") + "";

                            integration3str = integration3str + "%";
                            integration4str = integration4str + "%";
                            HashMap<String, Object> map2 = new HashMap<String, Object>();
                            map2.put("weekly", String.valueOf(Integer.parseInt(weekly) + 1));
                            map2.put("uname", unamestr);
                            map2.put("shares_name", shares_namestr);
                            map2.put("shares2_name", shares2_namestr);
                            map2.put("list_price", list_pricestr);
                            map2.put("ballot_jifen", ballot_jifenstr);
                            map2.put("price", pricestr);
                            map2.put("price2", price2str);
                            map2.put("integration3", integration3str);
                            map2.put("integration4", integration4str);
                            map2.put("integration1", integration1str + "%");
                            map2.put("integration2", integration2str + "%");
                            map2.put("uidimg", Md5Util.getuidstrMd5(Md5Util.getMd5(uidstr)));
                            map2.put("uid", uidstr);
                            map2.put("rankingcount", String.valueOf(j));
                            listessencematchData.add(map2);
                            if (j == 3) {
                                break;
                            }
                        }
                        //名人组数据
                        if (ballot2str == null) {
                            if (statusstrmap.get("ballot2") == null) {
                                break;
                            }
                            ballot2str = statusstrmap.get("ballot2") + "";
                            ballot2strlists = JsonTools.listKeyMaps(ballot2str);
                        }

                        int g = 0;
                        for (Map<String, Object> ballot2strmap : ballot2strlists) {

                            g++;
                            String pricestr;
                            String price2str;
                            String uidstr = ballot2strmap.get("uid") + "";
                            String weekly = ballot2strmap.get("weekly") + "";


                            //当前价1
                            if (ballot2strmap.get("price") == null) {
                                pricestr = "0";
                            } else {
                                pricestr = ballot2strmap.get("price") + "";

                            }
                            //当前价2
                            if (ballot2strmap.get("price2") == null) {
                                price2str = "0";
                            } else {

                                price2str = ballot2strmap.get("price2") + "";
                            }
                            //第1只最高涨幅
                            String integration3str = ballot2strmap.get("integration1") + "";
                            //第2只最高涨幅
                            String integration4str = ballot2strmap.get("integration2") + "";
                            //第1只最高涨幅
                            String integration1str = ballot2strmap.get("integration3") + "";
                            //最2只最高涨幅
                            String integration2str = ballot2strmap.get("integration4") + "";
                            //周积分
                            String list_pricestr = ballot2strmap.get("integration") + "";
                            //总积分
                            String ballot_jifenstr = ballot2strmap.get("ballot_jifen") + "";
                            String unamestr = ballot2strmap.get("uname") + "";
                            integration3str = integration3str + "%";
                            integration4str = integration4str + "%";
                            HashMap<String, Object> map2 = new HashMap<String, Object>();
                            map2.put("weekly", String.valueOf(Integer.parseInt(weekly) + 1));
                            map2.put("uname", unamestr);
                            map2.put("shares_name", ballot2strmap.get("shares_name").toString());
                            map2.put("shares2_name", ballot2strmap.get("shares2_name").toString());
                            map2.put("list_price", list_pricestr);
                            map2.put("ballot_jifen", ballot_jifenstr);
                            map2.put("price", pricestr);
                            map2.put("price2", price2str);
                            map2.put("integration3", integration3str);
                            map2.put("integration4", integration4str);
                            map2.put("integration1", integration1str + "%");
                            map2.put("integration2", integration2str + "%");
                            map2.put("uidimg", Md5Util.getuidstrMd5(Md5Util.getMd5(uidstr)));
                            map2.put("uid", uidstr);
                            map2.put("rankingcount", String.valueOf(g));
                            listfamousmanmatchData.add(map2);
                            if (g == 3) {
                                break;
                            }
                        }
                    }
                }
                dialog.dismiss();
                famousmanmatchAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(famousmanlistview);
                essencematchAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(essencelistview);
            }
            toplay02.requestFocus();
        }
    }
}
