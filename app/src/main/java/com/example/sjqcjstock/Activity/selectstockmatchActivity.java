package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.Activity.user.loginActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.essencematchAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.MatchNewsEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;
import com.example.sjqcjstock.view.PullableScrollView;
import com.example.sjqcjstock.view.SoListView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 选股比赛页面
 */
public class selectstockmatchActivity extends Activity {

    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    //定义List集合容器
    private essencematchAdapter adapter;
    private essencematchAdapter essencematchAdapter;
    //定义于数据库同步的字段集合
    // 名人组数据
    private ArrayList<MatchNewsEntity> listfamousmanmatchData;
    // 精英组数据
    private ArrayList<MatchNewsEntity> listessencematchData;
    //名人集合
    private ListView famousmanlistview;
    //精英集合
    private ListView essencelistview;
    //获取控件
    private PullableScrollView myScrollView;
    private RelativeLayout totop;
    private ImageView picktodayupranking1;
    private ImageView pickdiscussarea1;
    private ImageView pickthisweekranking1;
    private ImageView pickstockmatchreport1;
    private ImageView weekranking1;
    private TextView famousmore1Tv;
    private TextView essencemore1Tv;
    private LinearLayout toplay02;
    //排行榜标题栏
    private LinearLayout pickessence;
    private LinearLayout pickfamous;
    //网络请求提示
    private CustomProgress dialog;
    private int mScreen1_2;
    private ImageView img_line2;
    // 文字颜色
    private int select_color;
    private int unselect_color;
    /**
     * 当前视图宽度
     **/
    private Integer viewPagerW = 0;
    // 分页
    private int pageMr = 1;
    private int pageJy = 1;
    // 每页条数
    private int limit = 0;

    // 名人组接口数据
    private String jsonMr;
    // 精英组接口数据
    private String jsonJy;
    // 打赏接口返回数据
    private String jsonStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.selectstockmatch_list);
        //将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
        getDataMr();
        getDataJy();
    }

    private void initView() {
        // 获取颜色
        select_color = getResources().getColor(R.color.color_toptitle);
        unselect_color = getResources().getColor(R.color.color_000000);
        // 获取屏幕宽度
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        viewPagerW = size.x;

        dialog = new CustomProgress(selectstockmatchActivity.this);
        dialog.showDialog();

        toplay02 = (LinearLayout) findViewById(R.id.toplay02);
        toplay02.setFocusable(true);
        toplay02.setFocusableInTouchMode(true);

        myScrollView = (PullableScrollView) findViewById(R.id.myScrollView);
        totop = (RelativeLayout) findViewById(R.id.totop);
        picktodayupranking1 = (ImageView) findViewById(R.id.picktodayupranking1);
        pickstockmatchreport1 = (ImageView) findViewById(R.id.pickstockmatchreport1);
        weekranking1 = (ImageView) findViewById(R.id.weekranking1);
        pickessence = (LinearLayout) findViewById(R.id.pickessence);
        pickfamous = (LinearLayout) findViewById(R.id.pickfamous);
        pickdiscussarea1 = (ImageView) findViewById(R.id.pickdiscussarea1);
        pickthisweekranking1 = (ImageView) findViewById(R.id.pickthisweekranking1);
        famousmore1Tv = (TextView) findViewById(R.id.famousmore1);
        essencemore1Tv = (TextView) findViewById(R.id.essencemore1);
        pickessence.setOnClickListener(new MyOnClickListenser(1));
        pickfamous.setOnClickListener(new MyOnClickListenser(0));
        pickdiscussarea1.setOnClickListener(new pickdiscussarea1_listener());
        weekranking1.setOnClickListener(new weekranking1_listener());
        pickthisweekranking1.setOnClickListener(new pickthisweekranking1_listener());

        totop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                myScrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
        //模拟按钮点击
        totop.performClick();

        findViewById(R.id.goback1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        picktodayupranking1.setOnClickListener(new picktodayupranking1_listener());
        pickstockmatchreport1.setOnClickListener(new pickstockmatchreport1_listener());

        /**名人集合*/
        famousmanlistview = (SoListView) findViewById(R.id.famousmanlist);

        //为ListView 添加适配器
        adapter = new essencematchAdapter(selectstockmatchActivity.this,this);
        famousmanlistview.setAdapter(adapter);

        /**精英集合*/
        essencelistview = (SoListView) findViewById(R.id.essencelist);
        essencematchAdapter = new essencematchAdapter(selectstockmatchActivity.this,this);
        essencelistview.setAdapter(essencematchAdapter);

        img_line2 = (ImageView) findViewById(R.id.img_line2);
        mScreen1_2 = ImageUtil.getScreenWidth(this) / 2;
        ViewGroup.LayoutParams lp = img_line2.getLayoutParams();
        lp.width = mScreen1_2;
        img_line2.setLayoutParams(lp);

        ptrl = ((PullToRefreshLayout) findViewById(
                R.id.refresh_view));
        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下来刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (famousmanlistview.getVisibility() == View.VISIBLE){
                    if(listfamousmanmatchData != null){
                        listfamousmanmatchData.clear();
                    }
                    pageMr = 1;
                    getDataMr();
                }else{
                    if(listessencematchData != null){
                        listessencematchData.clear();
                    }
                    pageJy = 1;
                    getDataJy();
                }
            }

            // 下拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (famousmanlistview.getVisibility() == View.VISIBLE){
                    pageMr += 1;
                    getDataMr();
                }else{
                    pageJy += 1;
                    getDataJy();
                }
            }
        });

    }

    /**
     * 调用接口获取数据
     */
    private void getDataMr(){
        // 名人组
//        new SendInfoTaskMr().execute(new TaskParams(Constants.Url + "?app=public&mod=AppFeedList&act=AppBallot2&type=1&p="+pageMr+"&uid="+Constants.staticmyuidstr));

        // 名人组信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonMr = HttpUtil.restHttpGet(Constants.newUrl+"/api/ballot/list?mid="+Constants.staticmyuidstr+"&type=1&p="+pageMr);
                handler.sendEmptyMessage(0);
            }
        }).start();

    } /**
     * 调用接口获取数据
     */
    private void getDataJy(){
//        if (limit > 0){
//            listessencematchData.clear();
//            // 精英组
//            new SendInfoTaskJy().execute(new TaskParams(Constants.Url + "?app=public&mod=AppFeedList&act=AppBallot2&type=2&p=1&limit="+limit+"&uid="+Constants.staticmyuidstr));
//            limit = 0;
//
//        }else{
//            // 精英组
//            new SendInfoTaskJy().execute(new TaskParams(Constants.Url + "?app=public&mod=AppFeedList&act=AppBallot2&type=2&p="+pageJy+"&uid="+Constants.staticmyuidstr));
//        }

        // 精英组信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonJy = HttpUtil.restHttpGet(Constants.newUrl+"/api/ballot/list?mid="+Constants.staticmyuidstr+"&type=2&p="+pageJy);
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    class weekranking1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent(selectstockmatchActivity.this, weekrankingnewActivity.class);
            startActivity(intent);
        }
    }

    class pickdiscussarea1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // menghuan 不用登陆也可以用
            // 如果未登陆跳转到登陆页面
            if (!Constants.isLogin){
                Intent intent = new Intent(selectstockmatchActivity.this, loginActivity.class);
                startActivity(intent);
                return;
            }
            // 讨论区
            Intent intent = new Intent(selectstockmatchActivity.this, discussareaActivity.class);
            startActivity(intent);
        }
    }

    class pickthisweekranking1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent(selectstockmatchActivity.this, weekbullsrankingActivity.class);
            intent.putExtra("type","week");
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //模拟按钮点击
        totop.performClick();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //模拟按钮点击
        totop.performClick();
        myScrollView.fullScroll(ScrollView.FOCUS_UP);
    }

    class pickstockmatchreport1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent(selectstockmatchActivity.this, stockmatchreportActivity.class);
            startActivity(intent);
        }
    }

    class picktodayupranking1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent(selectstockmatchActivity.this, weekbullsrankingActivity.class);
            intent.putExtra("type","today");
            startActivity(intent);
        }
    }

//    /**
//     * 获取名人数据
//     */
//    private class SendInfoTaskMr extends AsyncTask<TaskParams, Void, String> {
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            if (result == null) {
//                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
//                dialog.dismissDlog();
//                // 千万别忘了告诉控件刷新完毕了哦！
//                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//                return;
//            } else {
//                super.onPostExecute(result);
//                List<Map<String, Object>> lists = null;
//                String statusstr = null;
//                List<Map<String, Object>> statusstrlists = null;
//                String ballot2str = null;
//                List<Map<String, Object>> ballot2strlists = null;
//                if (lists == null) {
//                    lists = JsonTools.listKeyMaps("[" + result + "]");
//                }
//                for (Map<String, Object> map : lists) {
//                    if (statusstr == null) {
//                        statusstr = map.get("data") + "";
//                        statusstrlists = JsonTools.listKeyMaps("[" + statusstr + "]");
//                    }
//                    for (Map<String, Object> statusstrmap : statusstrlists) {
//                        String weekstr = statusstrmap.get("week") + "";
//                        famousmore1Tv.setText("名人组赛况(第" + weekstr + "周)");
//                        essencemore1Tv.setText("精英组赛况(第" + weekstr + "周)");
//                        //名人组数据
//                        if (ballot2str == null) {
//                            if (statusstrmap.get("ballot") == null) {
//                                break;
//                            }
//                            ballot2str = statusstrmap.get("ballot") + "";
//                            ballot2strlists = JsonTools.listKeyMaps(ballot2str);
//                        }
//                        int g = 0;
//                        for (Map<String, Object> ballot2strmap : ballot2strlists) {
//                            g++;
//                            String pricestr;
//                            String price2str;
//                            String uidstr = ballot2strmap.get("uid") + "";
//                            String weekly = ballot2strmap.get("weekly") + "";
//                            //当前价1
//                            if (ballot2strmap.get("price") == null) {
//                                pricestr = "0";
//                            } else {
//                                pricestr = ballot2strmap.get("price") + "";
//                            }
//                            //当前价2
//                            if (ballot2strmap.get("price2") == null) {
//                                price2str = "0";
//                            } else {
//                                price2str = ballot2strmap.get("price2") + "";
//                            }
//                            //第1只最高涨幅
//                            String integration3str = ballot2strmap.get("integration1") + "";
//                            //第2只最高涨幅
//                            String integration4str = ballot2strmap.get("integration2") + "";
//                            //第1只最高涨幅
//                            String integration1str = ballot2strmap.get("integration3") + "";
//                            //最2只最高涨幅
//                            String integration2str = ballot2strmap.get("integration4") + "";
//                            //周收益
//                            String list_pricestr = ballot2strmap.get("integration") + "";
//                            //周均收益
//                            String ballot_jifenstr = ballot2strmap.get("weekly_avg") + "";
//                            String unamestr = ballot2strmap.get("uname") + "";
//                            integration3str = integration3str + "%";
//                            integration4str = integration4str + "%";
//                            HashMap<String, Object> map2 = new HashMap<String, Object>();
//                            map2.put("weekly", String.valueOf(Integer.parseInt(weekly) + 1));
//                            map2.put("uname", unamestr);
//                            map2.put("shares_name", ballot2strmap.get("shares_name").toString());
//                            map2.put("shares2_name", ballot2strmap.get("shares2_name").toString());
//                            map2.put("shares", Utils.jieQuSharesCode(ballot2strmap.get("shares").toString()));
//                            map2.put("shares2", Utils.jieQuSharesCode(ballot2strmap.get("shares2").toString()));
//                            map2.put("list_price", list_pricestr);
//                            map2.put("ballot_jifen", ballot_jifenstr);
//                            map2.put("price", pricestr);
//                            map2.put("price2", price2str);
//                            map2.put("integration3", integration3str);
//                            map2.put("integration4", integration4str);
//                            map2.put("integration1", integration1str + "%");
//                            map2.put("integration2", integration2str + "%");
//                            map2.put("uidimg", Md5Util.getuidstrMd5(Md5Util.getMd5(uidstr)));
//                            map2.put("uid", uidstr);
//                            map2.put("rankingcount", String.valueOf(g));
//                            // 付费相关的东西
//                            // 是否付费 2为付费
//                            String isFree =  ballot2strmap.get("is_free") + "";
//                            // 打赏微博ID
//                            String feedId = ballot2strmap.get("feed_id")+"";
//                            // 大赏水晶币个数
//                            String reward = ballot2strmap.get("reward")+"";
//
//                            map2.put("isFree", isFree);
//                            map2.put("reward", reward);
//                            map2.put("feedId", feedId);
//                            listfamousmanmatchData.add(map2);
//                        }
//                    }
//                }
//                dialog.dismissDlog();
//                famousmanmatchAdapter.setlistData(listfamousmanmatchData);
//            }
//            // 千万别忘了告诉控件刷新完毕了哦！
//            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//            toplay02.requestFocus();
//        }
//    }
//
//    /**
//     * 获取精英数据
//     */
//    private class SendInfoTaskJy extends AsyncTask<TaskParams, Void, String> {
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            if (result == null) {
//                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
//                // 千万别忘了告诉控件刷新完毕了哦！
//                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//                return;
//            } else {
//                super.onPostExecute(result);
//                List<Map<String, Object>> lists = null;
//                String statusstr = null;
//                List<Map<String, Object>> statusstrlists = null;
//                String ballot1str = null;
//                List<Map<String, Object>> ballot1strlists = null;
//                if (lists == null) {
//                    lists = JsonTools.listKeyMaps("[" + result + "]");
//                }
//                for (Map<String, Object> map : lists) {
//                    if (statusstr == null) {
//                        statusstr = map.get("data") + "";
//                        statusstrlists = JsonTools.listKeyMaps("[" + statusstr + "]");
//                    }
//                    for (Map<String, Object> statusstrmap : statusstrlists) {
//                        //精英组数据
//                        if (ballot1str == null) {
//                            if (statusstrmap.get("ballot") == null) {
//                                break;
//                            }
//                            ballot1str = statusstrmap.get("ballot") + "";
//                            ballot1strlists = JsonTools.listKeyMaps(ballot1str);
//                        }
//                        int j = 0;
//                        for (Map<String, Object> ballot1strmap : ballot1strlists) {
//                            j++;
//                            //荐股id
//                            String pricestr;
//                            String price2str;
//                            //用户id
//                            String uidstr = ballot1strmap.get("uid") + "";
//                            //股票名字
//                            String shares_namestr = ballot1strmap.get("shares_name") + "";
//                            //第一只股票编码
//                            String shares = ballot1strmap.get("shares") + "";
//                            //第二只股票的名称
//                            String shares2_namestr = ballot1strmap.get("shares2_name") + "";
//                            //第二只股票编码
//                            String shares2 = ballot1strmap.get("shares2") + "";
//                            //当前价1
//                            if (ballot1strmap.get("price") == null) {
//                                pricestr = "0";
//                            } else {
//                                pricestr = ballot1strmap.get("price") + "";
//                            }
//                            //当前价2
//                            if (ballot1strmap.get("price") == null) {
//                                price2str = "0";
//                            } else {
//                                price2str = ballot1strmap.get("price2") + "";
//                            }
//                            //第1只周涨幅
//                            String integration3str = ballot1strmap.get("integration1") + "";
//                            //第2只周涨幅
//                            String integration4str = ballot1strmap.get("integration2") + "";
//                            //第1只最高涨幅
//                            String integration1str = ballot1strmap.get("integration3") + "";
//                            //最2只最高涨幅
//                            String integration2str = ballot1strmap.get("integration4") + "";
//                            //周收益
//                            String list_pricestr = ballot1strmap.get("integration") + "";
//                            //周均收益
//                            String ballot_jifenstr = ballot1strmap.get("weekly_avg") + "";
//                            String unamestr = ballot1strmap.get("uname") + "";
//                            String weekly = ballot1strmap.get("weekly") + "";
//                            integration3str = integration3str + "%";
//                            integration4str = integration4str + "%";
//                            HashMap<String, Object> map2 = new HashMap<String, Object>();
//                            map2.put("weekly", String.valueOf(Integer.parseInt(weekly) + 1));
//                            map2.put("uname", unamestr);
//                            map2.put("shares_name", shares_namestr);
//                            map2.put("shares2_name", shares2_namestr);
//                            map2.put("shares", Utils.jieQuSharesCode(shares));
//                            map2.put("shares2", Utils.jieQuSharesCode(shares2));
//                            map2.put("list_price", list_pricestr);
//                            map2.put("ballot_jifen", ballot_jifenstr);
//                            map2.put("price", pricestr);
//                            map2.put("price2", price2str);
//                            map2.put("integration3", integration3str);
//                            map2.put("integration4", integration4str);
//                            map2.put("integration1", integration1str + "%");
//                            map2.put("integration2", integration2str + "%");
//                            map2.put("uidimg", Md5Util.getuidstrMd5(Md5Util.getMd5(uidstr)));
//                            map2.put("uid", uidstr);
//                            map2.put("rankingcount", String.valueOf(j));
//                            // 付费相关的东西
//                            // 是否付费 2为付费
//                            String isFree =  ballot1strmap.get("is_free") + "";
//                            // 打赏微博ID
//                            String feedId = ballot1strmap.get("feed_id")+"";
//                            // 大赏水晶币个数
//                            String reward = ballot1strmap.get("reward")+"";
//                            map2.put("isFree", isFree);
//                            map2.put("reward", reward);
//                            map2.put("feedId", feedId);
//                            listessencematchData.add(map2);
//                        }
//                    }
//                }
//                essencematchAdapter.setlistData(listessencematchData);
//            }
//            // 千万别忘了告诉控件刷新完毕了哦！
//            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//            toplay02.requestFocus();
//        }
//    }

    /**
     * 订阅水晶币的单机事件
     * @param position
     */
    public void PayCrystalCoin(final int position){

        // 打赏水晶币个数
        final String sjbCount =  listessencematchData.get(position).getReward();
        final View viewSang = LayoutInflater.from(selectstockmatchActivity.this).inflate(R.layout.dialog_sang1, null);
        RelativeLayout rl_dialogDismiss = (RelativeLayout) viewSang.findViewById(R.id.rl_dialogDismiss);

        //显示需要支付水晶币数量的控件
        TextView inputCount = (TextView) viewSang.findViewById(R.id.tv_inputCount);
        inputCount.setText("需要打赏"+sjbCount+"水晶币方可阅读");

        // 显示水晶币数量的控件
        TextView restCount = (TextView) viewSang.findViewById(R.id.tv_restCount);
        restCount.setText(Constants.shuijinbiCount);
        Button bt_ok = (Button) viewSang.findViewById(R.id.bt_ok);

        final AlertDialog alertDialog = new AlertDialog.Builder(selectstockmatchActivity.this).setView(viewSang).show();
        rl_dialogDismiss.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //销毁弹出框
                alertDialog.dismiss();
            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Integer.valueOf(Constants.shuijinbiCount) < Integer.valueOf(sjbCount)) {
                    CustomToast.makeText(getApplicationContext(), "对不起你水晶币不足。", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if (!((CheckBox) viewSang.findViewById(R.id.sang_agreement_ck)).isChecked()) {
                    CustomToast.makeText(getApplicationContext(), "请阅读《付费阅读协议》", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.showDialog();

                // 微博id
                final String weiboidstr = listessencematchData.get(position).getFeed_id();
                // 打赏人的id
                final String uidstr =  listessencematchData.get(position).getUid();

//                //提交数据到服务器
//                new SendInfoTaskReward().execute(new TaskParams(Constants.apprewardUrl,
//                        new String[]{"mid", Constants.staticmyuidstr},
//                        new String[]{"reward_coin", sjbCount},// 打赏金额
//                        new String[]{"weibo_id", weiboidstr},// 打赏微博ID
//                        new String[]{"touid", uidstr},// 被打赏用户ID
//                        new String[]{"state", "1"}////微博状态0标识免费微博，1标识付费微博
//                ));

                // 打赏水晶币
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List dataList = new ArrayList();
                        dataList.add(new BasicNameValuePair("mid", Constants.staticmyuidstr));
                        dataList.add(new BasicNameValuePair("feed_id", weiboidstr));
                        dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                        dataList.add(new BasicNameValuePair("reward_coin", sjbCount));
                        dataList.add(new BasicNameValuePair("touid", uidstr));
                        jsonStr = HttpUtil.restHttpPost(Constants.newUrl + "/api/credit/feedReward", dataList);
                        handler.sendEmptyMessage(2);
                    }
                }).start();
                alertDialog.dismiss();
            }
        });

        viewSang.findViewById(R.id.sang_agreement_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到水晶币协议页面
                Intent intent = new Intent(selectstockmatchActivity.this, AgreementActivity.class);
                startActivity(intent);
            }
        });
        viewSang.findViewById(R.id.bt_cz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到充值页面
                Intent intent = new Intent(selectstockmatchActivity.this, RechargeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

//    /**
//     * 调用打赏水晶币接口的异步
//     */
//    private class SendInfoTaskReward extends AsyncTask<TaskParams, Void, String> {
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            if (result == null) {
//                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT)
//                        .show();
//            } else {
//                String msg = "对不起付费失败,请重试";
//                super.onPostExecute(result);
//                try {
//                    JSONObject jsonParser = new JSONObject(result);
//                    msg = jsonParser.getString("msg");
//                    limit = 10 * pageJy;
//                    // 重新刷新页面
//                    getDataJy();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    CustomToast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
//                            .show();
//                }
//                CustomToast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
//                        .show();
//            }
//            dialog.dismissDlog();
//        }
//    }

    /**
     * 选股牛人
     * @param view
     */
    public void WeeklyAverageClick(View view){
        Intent intent = new Intent(selectstockmatchActivity.this, totalrankingnewActivity.class);
        intent.putExtra("type","2");
        startActivity(intent);
    }

    /**
     * 总收益
     * @param view
     */
    public void TotalIncomeClick(View view){
        Intent intent = new Intent(selectstockmatchActivity.this, totalrankingnewActivity.class);
        intent.putExtra("type","1");
        startActivity(intent);
    }

    /**
     * 我来选股
     * @param view
     */
    public void PinkselectStock(View view){
        // menghuan 不用登陆也可以用
        // 如果未登陆跳转到登陆页面
        if (!Constants.isLogin){
            Intent intent = new Intent(this, loginActivity.class);
            startActivity(intent);
            return;
        }
        Intent intent = new Intent(selectstockmatchActivity.this, IwillselectstockActivity.class);
        startActivity(intent);
    }


    /**
     * 点击文字进行切换
     *
     * @author wuxl
     */
    public class MyOnClickListenser implements View.OnClickListener {

        private int index = 0;

        public MyOnClickListenser(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                // 名人组
                case R.id.pickfamous:
                    essencemore1Tv.setTextColor(unselect_color);
                    famousmore1Tv.setTextColor(select_color);
                    famousmanlistview.setVisibility(View.VISIBLE);
                    essencelistview.setVisibility(View.GONE);
                    break;
                // 精英组
                case R.id.pickessence:
                    essencemore1Tv.setTextColor(select_color);
                    famousmore1Tv.setTextColor(unselect_color);
                    famousmanlistview.setVisibility(View.GONE);
                    essencelistview.setVisibility(View.VISIBLE);
                    break;
            }
            LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) img_line2.getLayoutParams();
            // 关键算法
            lp.leftMargin = (int) ((mScreen1_2 * index) + (((double) (index + 1) / viewPagerW) * mScreen1_2));
            img_line2.setLayoutParams(lp);
            // 滚动到顶部
            myScrollView.smoothScrollTo(0, 0);
        }
    }

    /**
     * 选股比赛规则
     *
     * @param view
     */
    public void RuleClick(View view) {
        // 选股比赛规则
        Intent intent = new Intent(this, AgreementActivity.class);
        intent.putExtra("type", "9");
        startActivity(intent);
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
                        dialog.dismissDlog();
                        JSONObject jsonObject = new JSONObject(jsonMr);
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            CustomToast.makeText(selectstockmatchActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                            return;
                        }
                        famousmore1Tv.setText("名人组赛况(第" + jsonObject.getString("week") + "周)");
                        ArrayList<MatchNewsEntity> matchNewsEntities = (ArrayList<MatchNewsEntity>) JSON.parseArray(jsonObject.getString("data"),MatchNewsEntity.class);
                        if (1 == pageMr){
                            listfamousmanmatchData = matchNewsEntities;
                        }else{
                            listfamousmanmatchData.addAll(matchNewsEntities);
                        }
                        adapter.setlistData(listfamousmanmatchData);
                        // 千万别忘了告诉控件刷新完毕了哦！
                        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                        toplay02.requestFocus();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(jsonJy);
                        dialog.dismissDlog();
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            CustomToast.makeText(selectstockmatchActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                            return;
                        }
                        essencemore1Tv.setText("精英组赛况(第" + jsonObject.getString("week") + "周)");
                        ArrayList<MatchNewsEntity> matchNewsEntities = (ArrayList<MatchNewsEntity>) JSON.parseArray(jsonObject.getString("data"),MatchNewsEntity.class);
                        if (1 == pageJy){
                            listessencematchData = matchNewsEntities;
                        }else{
                            listessencematchData.addAll(matchNewsEntities);
                        }
                        essencematchAdapter.setlistData(listessencematchData);
                        // 千万别忘了告诉控件刷新完毕了哦！
                        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                        toplay02.requestFocus();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        dialog.dismissDlog();
                        CustomToast.makeText(selectstockmatchActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        if (Constants.successCode.equals(jsonObject.getString("code"))) {
                            // 重新刷新页面
                            getDataJy();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

}
