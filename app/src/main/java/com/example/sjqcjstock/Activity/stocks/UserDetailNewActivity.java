package com.example.sjqcjstock.Activity.stocks;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.Activity.Article.ArticleDetailsActivity;
import com.example.sjqcjstock.Activity.OnlineServiceActivity;
import com.example.sjqcjstock.Activity.Tomlive.DirectBroadcastingRoomActivity;
import com.example.sjqcjstock.Activity.plan.PlanExhibitionActivity;
import com.example.sjqcjstock.Activity.qa.QuestionAnswerActivity;
import com.example.sjqcjstock.Activity.user.loginActivity;
import com.example.sjqcjstock.Activity.user.myfansActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.FeedListAdapter;
import com.example.sjqcjstock.adapter.plan.PlanAdapter;
import com.example.sjqcjstock.adapter.qa.QuizAdapter;
import com.example.sjqcjstock.adapter.qa.QuizMyAdapter;
import com.example.sjqcjstock.adapter.stocks.MyDealAccountAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.Article.FeedListEntity;
import com.example.sjqcjstock.entity.Article.UserEntity;
import com.example.sjqcjstock.entity.plan.PlanEntity;
import com.example.sjqcjstock.entity.qa.QuestionAnswerEntity;
import com.example.sjqcjstock.entity.stocks.LineEntity;
import com.example.sjqcjstock.entity.stocks.PositionEntity;
import com.example.sjqcjstock.entity.stocks.SpotEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.example.sjqcjstock.netutil.sharesUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;
import com.example.sjqcjstock.view.PullableScrollView;
import com.example.sjqcjstock.view.SoListView;
import com.example.sjqcjstock.view.stocks.LineChart;
import com.example.sjqcjstock.view.stocks.SubscribeChoicePopup;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人中心(模拟炒股和以前的个人中心集合)
 * @author Administrator
 * 20170706 修改微博列表接口处理
 */
public class UserDetailNewActivity extends FragmentActivity {

    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 上面显示的标题
    private TextView titleName;
    // 订阅按钮
    private TextView subscribeTv;
    // 获取uid
    private String uidstr;
    // 获取控件
    private TextView username2;
    private ImageView headimg2;
    private ImageView usersex1;
    private TextView userintro1;
    private TextView following_count2;
    //    private TextView weibo_count1;
    // 自选股条数
    private TextView optional_stock_tv;
    private TextView follower_count2;
    private Button personalletter3;
    private LinearLayout myattentionuserlist1;
    private LinearLayout myfansuserlist1;
    private LinearLayout goback1;
    private Button followersign1;
    // 是否是粉丝  1是  0否
    private String followerstr;
    // 是否关注  1是  0否
    private String followingstr;
    private ImageView vipImg;
    private String unamestr;
    private String list_idstr;

    // 三个滑动页面
//    private CustomPager mViewPager;
//    private FragmentPagerAdapter mAdapter;
//    private List<Fragment> mDatas;
    // 微博的LinearLayout
    private LinearLayout fragmentMicroBlog;
    // 交易的LinearLayout
    private LinearLayout linearTransaction;
    //    // 自选股的LinearLayout
//    private LinearLayout linearStock;
    // 投资计划的LinearLayout
    private LinearLayout linearPlan;
    // 问答的LinearLayout
    private LinearLayout fragmentQa;
    // 控件
    private TextView textMicroBlog = null;
    private TextView textTransaction = null;
    private TextView textPlan = null;
    private TextView textQa = null;
    private TextView textPlayReward = null;
    private LinearLayout llMicroBlog = null;
    private LinearLayout llTransaction = null;
    private LinearLayout llPlan = null;
    private LinearLayout llQa = null;
    private LinearLayout llPlayReward = null;
    private ImageView img_line;
    private PullableScrollView myScrollView;
    // 到直播间的按钮
    private ImageView tomliveLv;
    // 是否是直播返回的数据
    private String tomliveStr;
    // 房间ID
    private String roomId;
    // 滑动条颜色
    private int select_color;
    private int unselect_color;
    private int mScreen1_4;
    /**
     * 当前视图宽度
     **/
    private Integer viewPagerW = 0;
    // 微博的ListView
    private SoListView commonlistview;
    // 定义List集合容器
//    private commonnoteAdapter usercommonnoteAdapter;
//    private ArrayList<HashMap<String, String>> listusercommonnoteData;
    private FeedListAdapter userFeedListAdapter;
    private ArrayList<FeedListEntity> userFeedListEntityList;

    // 访问微博页数控制
    private int weiboPage = 1;
    // 访问打赏文章的页数
    private int rewardPage = 1;
//    // 自选股的List
//    private ListView stockListView;
//    // 自选股的List Adapter
//    private StockAdapter stockAdapter;
//    // 调用自选股的数据
//    private String resstr = "";
//    // 自选股股票最新信息的Map
//    private Map<String, Map> mapZxgStr;
//    // 自选股的的数据
//    private ArrayList<OptionalEntity> optionalArrayList = null;

    // 调用投资计划的数据
    private String resstrPlan = "";
    // 自选股条数的数据
    private String resstr = "";
    // 计划的页数
    private int planPage = 1;
    // 定义List集合容器
    private PlanAdapter planAdapter;
    //定义于数据库同步的字段集合
    private ArrayList<PlanEntity> planEntityArrayList;
    // 投资计划的List
    private SoListView planListView;

    // 交易用的一些属性
    // 股票交易的List
    private SoListView listView;
    // 交易的行情List的Adapter
    private MyDealAccountAdapter listAdapter;
    // 调用买卖接口返回的数据
    private String mMresstr = "";
    // 调用接口获取用户的交易排名信息
    private String xxstr = "";
    // 调用是否订阅的接口获取的数据
    private String desertStr;
    // 分时接口返回的数据
    private String chartstr = "";
    // 持仓的数据
    private ArrayList<PositionEntity> positionArrayList = null;
    // 持仓股票最新信息的Map
    private Map<String, Map> mapStr;
    // 总收益
    private TextView totalRate;
    private TextView totalRate1;
    // 排名
    private TextView totalProfitRank;
    // 胜率
    private TextView winRate;
    private TextView winRate1;
    // 周均收益率
    private TextView weekAvgProfitRate;
    // 可用资金
    private TextView availableFunds;
    // 总资产
    private TextView funds;
    // 股票市值
    private TextView marketValue;
    // 仓位
    private TextView position;
    // 建户时间
    private TextView time;

    // 最新市值
    private double market = 0;
    // K线图的
    private LineChart assetsChart;
    // 账户分时图的数据
    private List<SpotEntity> lineAveData;
    // Y轴最小值
    private float miniY;
    // Y轴最大值
    private float maxY;
    // Y轴标题
    private List<String> ytitle;
    // X轴标题
    private List<String> xtitle;
    // 设置下横条的位置
    private LinearLayout.LayoutParams lp;
    // 可用资金
    private double availableFundsValue = 0;
    // 网络请求提示
    private CustomProgress dialog;
    // 是否订阅的订阅ID
    private String desertId = "";
    // 是否可以延长订阅
    private String desertExtend = "";
    // 订阅时间
    private String desertTime = "";
    // 2017.1.12 追加的打赏微博
    // 打赏微博的列表
    private ListView playRewardList;
//    // 打赏微博的数据集合
//    private ArrayList<HashMap<String, String>> listessenceData;
//    // 控制器
//    private myattentioncommonnoteAdapter commonnoteAdapter;

    // 控制器
    private FeedListAdapter payFeedListAdapter;
        // 打赏微博的数据集合
    private ArrayList<FeedListEntity> payFeedListEntityList;

    // 问答相关的空间
    // 控件
    private TextView text_wd = null;
    private TextView text_tw = null;
    private LinearLayout ll_wd = null;
    private LinearLayout ll_tw = null;
    // 提问的按钮
    private Button qaBt = null;
    private int mScreen1_2;
    private ImageView img_line2;
    // 调用接口返回的数据
    private String resstrAsk = "";
    // 获取全部问答
    private String resstrAll = "";
    // 获取我的问答
    private String resstrMy = "";
    // 是否可以提问
    private boolean isRn = false;
    // 全部问答的List
    private ListView allQaList;
    // 我的问答的List
    private ListView myQaList;

    // 定义List集合容器
    private QuizMyAdapter adapterMy;
    private QuizAdapter adapterAll;
    //定义于数据库同步的字段集合
    private ArrayList<QuestionAnswerEntity> listDataMy;
    private ArrayList<QuestionAnswerEntity> listDataAll;

    // 我的问答列表分页
    private int myPage = 1;
    // 全部问答列表分页
    private int allPage = 1;
    // 提问水晶币个数
    private String sjbCount = "0";

    // 个人微博列表数据
    private String feedSpaceStr;
    // 个人微博内参列表数据
    private String feedSpacePayStr;
    // 博主的个人信息数据
    private String userStr;
    // 操作接口数据
    private String jsonStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_detail);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        // 可以在主线程中使用http网络访问
//        if (android.os.Build.VERSION.SDK_INT > 9) {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//        }
        initView();
        initLine();
        // 获取屏幕宽度
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        viewPagerW = size.x;
        lp = (android.widget.LinearLayout.LayoutParams) img_line.getLayoutParams();
        // 判断显示那个页
        String type = getIntent().getStringExtra("type");
        if ("1".equals(type)) {
            // 交易
            linearTransaction.setVisibility(View.VISIBLE);
            subscribeTv.setVisibility(View.VISIBLE);
            // 关键算法
            lp.leftMargin = (int) ((mScreen1_4 * 3) + (((double) 4 / viewPagerW) * mScreen1_4));
            getTransactionData();
            textMicroBlog.setTextColor(unselect_color);
            textTransaction.setTextColor(select_color);
        } else if ("2".equals(type)) {
            // 内参
            playRewardList.setVisibility(View.VISIBLE);
            // 关键算法
            lp.leftMargin = (int) ((mScreen1_4 * 1) + (((double) 2 / viewPagerW) * mScreen1_4));
            getRewardData();
            textMicroBlog.setTextColor(unselect_color);
            textPlayReward.setTextColor(select_color);
        } else if ("3".equals(type)) {
            // 问答
            fragmentQa.setVisibility(View.VISIBLE);
            // 关键算法
            lp.leftMargin = (int) ((mScreen1_4 * 2) + (((double) 3 / viewPagerW) * mScreen1_4));
            getData();
            getDataAll();
            getDataMy();
            textMicroBlog.setTextColor(unselect_color);
            textQa.setTextColor(select_color);
            String type3 = getIntent().getStringExtra("type3");
            if (type3 != null && "2".equals(type3)) {
                text_wd.setTextColor(unselect_color);
                text_tw.setTextColor(select_color);
                allQaList.setVisibility(View.GONE);
                myQaList.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) img_line2.getLayoutParams();
                // 关键算法
                lp.leftMargin = (int) ((mScreen1_2 * 1) + (((double) 2 / viewPagerW) * mScreen1_2));
                img_line2.setLayoutParams(lp);
            }
        } else if ("4".equals(type)) {
            // 个人计划
            playRewardList.setVisibility(View.VISIBLE);
            // 关键算法
            lp.leftMargin = (int) ((mScreen1_4 * 4) + (((double) 5 / viewPagerW) * mScreen1_4));
            getPlanData();
            textMicroBlog.setTextColor(unselect_color);
            textPlan.setTextColor(select_color);
        } else {
            // 微博
            fragmentMicroBlog.setVisibility(View.VISIBLE);
            // 关键算法
            lp.leftMargin = (int) ((mScreen1_4 * 0) + (((double) 1 / viewPagerW) * mScreen1_4));
            textMicroBlog.setTextColor(select_color);
            textTransaction.setTextColor(unselect_color);
            getWeiBoData();
        }
        img_line.setLayoutParams(lp);
        // 滚动到顶部
        myScrollView.smoothScrollTo(0, 0);
        // 判断是否有直播间
        getisTomlive();
        // 获取自选股条数
        getStockData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        desertId = "";
        // 当交易页面显示的时候再去判断是否查询被订阅
        if (linearTransaction.getVisibility() == View.VISIBLE) {
            // 查询当前用户是否被订阅
            getisDesert();
        }
        if (Constants.qaIsrn) {
            getDataAll();
            getDataMy();

            // 这个以后来改  很丑 回答后回来刷新的问题
            text_tw.setTextColor(unselect_color);
            text_wd.setTextColor(select_color);
            allQaList.setVisibility(View.VISIBLE);
            myQaList.setVisibility(View.GONE);
            LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) img_line2.getLayoutParams();
            // 关键算法
            lp.leftMargin = (int) ((mScreen1_2 * 0) + (((double) (0 + 1) / viewPagerW) * mScreen1_2));
            img_line2.setLayoutParams(lp);
            // 滚动到顶部
            myScrollView.smoothScrollTo(0, 0);

            Constants.qaIsrn = false;
        }
    }

    private void initView() {
        // 获取intent的数据
        uidstr = getIntent().getStringExtra("uid");
        dialog = new CustomProgress(this);
        dialog.showDialog();
        myScrollView = (PullableScrollView) findViewById(R.id.myScrollView);
        titleName = (TextView) findViewById(R.id.title_name);
        subscribeTv = (TextView) findViewById(R.id.subscribe_tv);
        subscribeTv.setVisibility(View.GONE);
        tomliveLv = (ImageView) findViewById(R.id.tomlive_lv);
        tomliveLv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (roomId == null || roomId.equals("")) {
                    return;
                }
                Intent intent = new Intent(UserDetailNewActivity.this, DirectBroadcastingRoomActivity.class);
                intent.putExtra("roomId", roomId);
                intent.putExtra("uid", uidstr);
                intent.putExtra("name", unamestr);
                intent.putExtra("remark", userintro1.getText().toString().trim());
                startActivity(intent);
            }
        });
        if (Constants.staticmyuidstr.equals(uidstr)) {
            findViewById(R.id.private_letter_follow_ll).setVisibility(View.GONE);
            findViewById(R.id.view_line_v).setVisibility(View.GONE);
            titleName.setText("我的主页");
            subscribeTv.setVisibility(View.GONE);
        } else {
            titleName.setText("Ta的主页");
        }
        username2 = (TextView) findViewById(R.id.username2);
        headimg2 = (ImageView) findViewById(R.id.headimg2);
        usersex1 = (ImageView) findViewById(R.id.usersex1);
        userintro1 = (TextView) findViewById(R.id.userintro1);
        following_count2 = (TextView) findViewById(R.id.following_count2);
//        weibo_count1 = (TextView) findViewById(R.id.weibo_count1);
        optional_stock_tv = (TextView) findViewById(R.id.optional_stock_tv);
        follower_count2 = (TextView) findViewById(R.id.follower_count2);
        personalletter3 = (Button) findViewById(R.id.personalletter3);
        myattentionuserlist1 = (LinearLayout) findViewById(R.id.myattentionuserlist1);
        myfansuserlist1 = (LinearLayout) findViewById(R.id.myfansuserlist1);
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        followersign1 = (Button) findViewById(R.id.followersign1);
        vipImg = (ImageView) findViewById(R.id.vip_img);
        personalletter3.setOnClickListener(new personalletter3_listener());
        myattentionuserlist1
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UserDetailNewActivity.this,
                                myfansActivity.class);
                        intent.putExtra("uidstr", uidstr);
                        intent.putExtra("type", "following");
                        startActivity(intent);
                    }
                });
        myfansuserlist1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDetailNewActivity.this,
                        myfansActivity.class);
                intent.putExtra("uidstr", uidstr);
                intent.putExtra("type", "follower");
                startActivity(intent);
            }
        });
        goback1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        followersign1.setOnClickListener(new followersign1_listener());

        // 从网络获取用户详细信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                userStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/user/info?mid=" + uidstr + "&uid=" + Constants.staticmyuidstr);
                handler.sendEmptyMessage(14);
            }
        }).start();

        // 下面新加的东西
        textMicroBlog = (TextView) findViewById(R.id.text_micro_blog);
        textTransaction = (TextView) findViewById(R.id.text_transaction);
        textPlan = (TextView) findViewById(R.id.text_my_plan);
        textQa = (TextView) findViewById(R.id.text_qa);
        textPlayReward = (TextView) findViewById(R.id.text_play_reward);

        llMicroBlog = (LinearLayout) findViewById(R.id.linear_micro_blog);
        llTransaction = (LinearLayout) findViewById(R.id.linear_transaction);
        llPlan = (LinearLayout) findViewById(R.id.linear_my_plan);
        llQa = (LinearLayout) findViewById(R.id.linear_qa);
        llPlayReward = (LinearLayout) findViewById(R.id.linear_play_reward);

        llMicroBlog.setOnClickListener(new MyOnClickListenser(0));
        llPlayReward.setOnClickListener(new MyOnClickListenser(1));
        llQa.setOnClickListener(new MyOnClickListenser(2));
        llTransaction.setOnClickListener(new MyOnClickListenser(3));
        llPlan.setOnClickListener(new MyOnClickListenser(4));
        // 获取颜色
        select_color = getResources().getColor(R.color.color_toptitle);
        unselect_color = getResources().getColor(R.color.color_000000);


        ptrl = ((PullToRefreshLayout) findViewById(
                R.id.refresh_view));
        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下来刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if (fragmentMicroBlog.getVisibility() == View.VISIBLE) {
                    weiboPage = 1;
                    userFeedListEntityList.clear();
                    getWeiBoData();
                } else if (linearTransaction.getVisibility() == View.VISIBLE) {
                    market = 0;
                    getTransactionData();
                } else if (playRewardList.getVisibility() == View.VISIBLE) {
                    rewardPage = 1;
                    payFeedListEntityList.clear();
                    getRewardData();
                } else if (fragmentQa.getVisibility() == View.VISIBLE) {
                    if (allQaList.getVisibility() == View.VISIBLE) {
                        allPage = 1;
                        getDataAll();
                    } else {
                        myPage = 1;
                        getDataMy();
                    }
                } else {
                    planPage = 1;
                    planEntityArrayList.clear();
                    // 自选股
                    getPlanData();
                }
            }

            // 下拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (fragmentMicroBlog.getVisibility() == View.VISIBLE) {
                    weiboPage += 1;
                    getWeiBoData();
                } else if (linearTransaction.getVisibility() == View.VISIBLE) {
                    market = 0;
                    getTransactionData();
                } else if (playRewardList.getVisibility() == View.VISIBLE) {
                    rewardPage += 1;
                    getRewardData();
                } else if (fragmentQa.getVisibility() == View.VISIBLE) {
                    if (allQaList.getVisibility() == View.VISIBLE) {
                        allPage += 1;
                        getDataAll();
                    } else {
                        myPage += 1;
                        getDataMy();
                    }
                } else {
                    planPage += 1;
                    // 自选股
                    getPlanData();
                    // 千万别忘了告诉控件刷新完毕了哦！
                    ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
            }
        });
        findViewWeibo();
        findViewPlan();
        findViewTransaction();
        findViewReward();
        findViewQa();
    }

    /**
     * 打赏微博的数据绑定
     */
    private void findViewReward() {

        payFeedListEntityList = new ArrayList<FeedListEntity>();
        payFeedListAdapter = new FeedListAdapter(UserDetailNewActivity.this);
        // 打赏微博的列表
        playRewardList = (ListView) findViewById(R.id.play_reward);
        playRewardList.setAdapter(payFeedListAdapter);

        playRewardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                try {
                    if (payFeedListEntityList.size() < 1) {
                        return;
                    }
                    Intent intent = new Intent(UserDetailNewActivity.this, ArticleDetailsActivity.class);
                    // 传递发布微博的信息
                    intent.putExtra("weibo_id", payFeedListEntityList.get(arg2).getFeed_id());
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 判断当前用户是否被订阅
     */
    private void getisDesert() {
        // 开线程获取用户是否订阅
        new Thread(new Runnable() {
            @Override
            public void run() {
                desertStr = HttpUtil.restHttpGet(Constants.moUrl + "/desert/isDesert?uid=" + Constants.staticmyuidstr + "&price_uid=" + uidstr);
                handler.sendEmptyMessage(6);
            }
        }).start();
    }

    /**
     * 判断当前用户是否开通直播间
     */
    private void getisTomlive() {
        // 开线程获取用户是否开通直播间
        new Thread(new Runnable() {
            @Override
            public void run() {
                tomliveStr = HttpUtil.restHttpGet(Constants.moUrl + "/live/room/hasRoom&uid=" + uidstr + "&token=" + Constants.apptoken);
                handler.sendEmptyMessage(7);
            }
        }).start();
    }

    /**
     * 绑定交易的控件
     */
    private void findViewTransaction() {
        linearTransaction = (LinearLayout) findViewById(R.id.fragment_transaction);
        assetsChart = (LineChart) findViewById(R.id.assets_chart);
        listAdapter = new MyDealAccountAdapter(this, uidstr);
        listView = (SoListView) findViewById(
                R.id.list_view);
        listView.setAdapter(listAdapter);
        totalRate = (TextView) findViewById(R.id.total_rate);
        totalRate1 = (TextView) findViewById(R.id.total_rate1);
        totalProfitRank = (TextView) findViewById(R.id.total_profit_rank);
        winRate = (TextView) findViewById(R.id.win_rate);
        winRate1 = (TextView) findViewById(R.id.win_rate1);
        weekAvgProfitRate = (TextView) findViewById(R.id.week_avg_profit_rate);
        availableFunds = (TextView) findViewById(R.id.available_funds);
        funds = (TextView) findViewById(R.id.funds);
        marketValue = (TextView) findViewById(R.id.market_value);
        position = (TextView) findViewById(R.id.position);
        time = (TextView) findViewById(R.id.time);
    }

//    /**
//     * 绑定自选股的控件
//     */
//    private void findViewStock() {
//        linearStock = (LinearLayout) findViewById(R.id.fragment_stock);
//        stockAdapter = new StockAdapter(this);
//        stockListView = (ListView) findViewById(
//                R.id.stock_list);
//        stockListView.setAdapter(stockAdapter);
//        stockListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent inten = new Intent();
//                inten.putExtra("name", optionalArrayList.get(position).getStock_name());
//                inten.putExtra("code", optionalArrayList.get(position).getStock());
//                inten.setClass(UserDetailNewActivity.this, SharesDetailedActivity.class);
//                startActivity(inten);
//            }
//        });
//    }

    /**
     * 绑定投资计划的控件
     */
    private void findViewPlan() {
        linearPlan = (LinearLayout) findViewById(R.id.fragment_plan);
        planEntityArrayList = new ArrayList<PlanEntity>();
        planAdapter = new PlanAdapter(this);
        planListView = (SoListView) findViewById(
                R.id.plan_list_view);
        planListView.setAdapter(planAdapter);
        planListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UserDetailNewActivity.this, PlanExhibitionActivity.class);
                intent.putExtra("id", planEntityArrayList.get(position).getId());
                intent.putExtra("uid", planEntityArrayList.get(position).getUid());
                startActivity(intent);
            }
        });
    }

    /**
     * 绑定微博的控件
     */
    private void findViewWeibo() {
        // 微博的LinearLayout
        fragmentMicroBlog = (LinearLayout) findViewById(R.id.fragment_micro_blog);
        /** 普通帖集合 */
        commonlistview = (SoListView) findViewById(R.id.notelist1);
        // 存储数据的数组列表
        userFeedListEntityList = new ArrayList<FeedListEntity>();
        userFeedListAdapter = new FeedListAdapter(this);
        commonlistview.setAdapter(userFeedListAdapter);
        commonlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                String content = userFeedListEntityList.get(position).getFeed_content();
                if (content.length() > 3 && Constants.microBlogShare.equals(content.substring(0, 4))) {
                    return;
                }
                Intent intent = new Intent(UserDetailNewActivity.this,
                        ArticleDetailsActivity.class);
                intent.putExtra(
                        "weibo_id",
                        userFeedListEntityList.get(position).getFeed_id());
                startActivity(intent);
            }
        });
    }

    /**
     * 关注的单机事件
     */
    class followersign1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            if (Utils.isFastDoubleClick()) {
                return;
            }
            // menghuan 不用登陆也可以用
            // 如果未登陆跳转到登陆页面
            if (!Constants.isLogin) {
                Intent intent = new Intent(UserDetailNewActivity.this, loginActivity.class);
                startActivity(intent);
                return;
            }

            final String type;
            if ("0".equals(followingstr)) {
                type = "add";
            }else{
                type = "cancel";
            }
            // 关注 取消关注
            new Thread(new Runnable() {
                @Override
                public void run() {
                    jsonStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/user/follow?mid=" + Constants.staticmyuidstr + "&token=" + Constants.apptoken + "&uid=" + uidstr+"&type="+type);
                    handler.sendEmptyMessage(15);
                }
            }).start();

        }
    }

    /**
     * 私信的单击事件
     */
    class personalletter3_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // menghuan 不用登陆也可以用
            // 如果未登陆跳转到登陆页面
            if (!Constants.isLogin) {
                Intent intent = new Intent(UserDetailNewActivity.this, loginActivity.class);
                startActivity(intent);
                return;
            }

            Intent intent = new Intent(UserDetailNewActivity.this, OnlineServiceActivity.class);
            intent.putExtra("uidstr", uidstr);
            intent.putExtra("unamestr", unamestr);
            startActivity(intent);

            // 测试可以看看不用判断直接跳转
//            new SendInfoTaskForChatIsExist().execute(new TaskParams(
//                    Constants.Url + "?app=public&mod=AppFeedList&act=Messages",
//                    new String[]{"mid", Constants.staticmyuidstr},
//                    new String[]{"cid", uidstr}));
        }
    }
//
//    private class SendInfoTasksuserinfodetail extends
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
//                CustomToast.makeText(UserDetailNewActivity.this, "", Toast.LENGTH_SHORT).show();
//            } else {
//                super.onPostExecute(result);
//                result = "[" + result + "]";
//                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
//
//                for (Map<String, Object> map : lists) {
//                    String statusstr = map.get("data") + "";
//                    statusstr = "[" + statusstr + "]";
//                    List<Map<String, Object>> weibolists = JsonTools.listKeyMaps(statusstr);
//
//                    for (Map<String, Object> weibomap : weibolists) {
//                        String introstr;
//                        unamestr = weibomap.get("uname") + "";
//                        String avatar_middlestr = weibomap.get("avatar_middle") + "";
//                        String Userdatastr = weibomap.get("Userdata") + "";
//                        List<Map<String, Object>> Userdatastrlists = JsonTools.listKeyMaps("[" + Userdatastr + "]");
//
//                        for (Map<String, Object> Userdatamap : Userdatastrlists) {
//                            String following_countstr;
//                            String follower_countstr;
//                            String weibo_countstr;
//                            if (Userdatamap.get("weibo_count") == null) {
//                                weibo_countstr = "0";
//                            } else {
//                                weibo_countstr = Userdatamap.get("weibo_count") + "";
//                            }
//                            if (Userdatamap.get("following_count") == null) {
//                                following_countstr = "0";
//                            } else {
//                                following_countstr = Userdatamap.get("following_count") + "";
//                            }
//
//                            if (Userdatamap.get("follower_count") == null) {
//                                follower_countstr = "0";
//                            } else {
//                                follower_countstr = Userdatamap.get("follower_count") + "";
//                            }
//                            following_count2.setText(following_countstr);
//                            follower_count2.setText(follower_countstr);
//                        }
//                        String followstr = weibomap.get("follow") + "";
//                        if (!"".equals(followstr) && !"null".equals(followstr)){
//                            List<Map<String, Object>> followstrlists = JsonTools.listKeyMaps("[" + followstr + "]");
//                            for (Map<String, Object> followstrmap : followstrlists) {
//                                followingstr = followstrmap.get("following") + "";
//                                followerstr = followstrmap.get("follower") + "";// 0是未关注,1是已关注
//                                if ("0".equals(followingstr)) {
//                                    followersign1.setText(" + 关注");
//                                } else {
//                                    if ("1".equals(followerstr)) {
//                                        followersign1.setText("相互关注");
//                                    } else {
//                                        followersign1.setText("取消关注");
//                                    }
//                                }
//                            }
//                        }
//                        //空指针异常
//                        String sexstr = weibomap.get("sex") + "";
//                        if (weibomap.get("intro") == null) {
//                            introstr = "暂无简介";
//                        } else {
//                            introstr = weibomap.get("intro") + "";
//                        }
//                        if ("1".equals(sexstr)) {
//                            usersex1.setImageResource(R.mipmap.nan);
//                        } else {
//                            usersex1.setImageResource(R.mipmap.nv);
//                        }
//                        username2.setText(unamestr);
//                        userintro1.setText(introstr);
//
//                        ImageLoader.getInstance().displayImage(avatar_middlestr,
//                                headimg2, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
//
//                        ViewUtil.setUpVip(weibomap.get("user_group") + "", vipImg);
//                    }
//                }
//            }
//        }
//    }

//    private class SendInfoTaskfollowsb extends
//            AsyncTask<TaskParams, Void, String> {
//
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            super.onPostExecute(result);
//            int follows = Integer.parseInt((follower_count2.getText()
//                    .toString()));
//            follower_count2.setText(String.valueOf((follows + 1)));
//        }
//    }
//
//    private class SendInfoTaskfollowcancelsb extends
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
//
//            int follows = Integer.parseInt((follower_count2.getText()
//                    .toString()));
//
//            follower_count2.setText(String.valueOf((follows - 1)));
//        }
//    }
//
//    //发送请求，判断私信是否存在
//    private class SendInfoTaskForChatIsExist extends AsyncTask<TaskParams, Void, String> {
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            result = result.replace("\n ", "");
//            result = result.replace("\n", "");
//            result = result.replace(" ", "");
//            result = "[" + result + "]";
//            // 解析json字符串获得List<Map<String,Object>>
//            List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
//            for (Map<String, Object> map : lists) {
//                String statusstr = map.get("status") + "";
//                String datastr = map.get("data") + "";
//                if ("1".equals(statusstr)) {
//                    List<Map<String, Object>> datastrlists = JsonTools.listKeyMaps("[" + datastr + "]");
//                    for (Map<String, Object> datastrmap : datastrlists) {
//                        list_idstr = datastrmap.get("list_id") + "";
//                    }
//                    Intent intent = new Intent(UserDetailNewActivity.this, OnlineServiceActivity.class);
//                    intent.putExtra("uidstr", uidstr);
//                    intent.putExtra("unamestr", unamestr);
//                    intent.putExtra("list_id", list_idstr);
//                    startActivity(intent);
//                }
//                if ("0".equals(statusstr)) {
//                    //不存在，直接跳转
//                    Intent intent = new Intent(UserDetailNewActivity.this, OnlineServiceActivity.class);
//                    intent.putExtra("uidstr", uidstr);
//                    intent.putExtra("unamestr", unamestr);
//                    startActivity(intent);
//                }
//            }
//        }
//    }

    /**
     * 初始化line
     */
    private void initLine() {
        img_line = (ImageView) findViewById(R.id.img_line);
        mScreen1_4 = ImageUtil.getScreenWidth(this) / 5;
        ViewGroup.LayoutParams lp = img_line.getLayoutParams();
        lp.width = mScreen1_4;
        img_line.setLayoutParams(lp);
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
            resetTextColor();
            subscribeTv.setVisibility(View.GONE);
            switch (v.getId()) {
                case R.id.linear_micro_blog:
                    textMicroBlog.setTextColor(select_color);
                    fragmentMicroBlog.setVisibility(View.VISIBLE);
                    if (userFeedListEntityList.size() < 1) {
                        dialog.showDialog();
                        getWeiBoData();
                    }
                    break;
                case R.id.linear_transaction:
                    textTransaction.setTextColor(select_color);
                    linearTransaction.setVisibility(View.VISIBLE);
                    if ("".equals(mMresstr)) {
                        dialog.showDialog();
                        getTransactionData();
                        // 查询当前用户是否被订阅
                        getisDesert();
                    }
                    subscribeTv.setVisibility(View.VISIBLE);
                    break;
                case R.id.linear_my_plan:
                    textPlan.setTextColor(select_color);
                    linearPlan.setVisibility(View.VISIBLE);
                    if ("".equals(resstrPlan)) {
                        dialog.showDialog();
                        getPlanData();
                    }
                    break;
                case R.id.linear_play_reward:
                    textPlayReward.setTextColor(select_color);
                    playRewardList.setVisibility(View.VISIBLE);
                    if (payFeedListEntityList.size() < 1) {
                        dialog.showDialog();
                        getRewardData();
                    }
                    break;
                case R.id.linear_qa:
                    textQa.setTextColor(select_color);
                    fragmentQa.setVisibility(View.VISIBLE);
                    if (listDataAll.size() < 1) {
                        dialog.showDialog();
                        getData();
                        getDataAll();
                        getDataMy();
                    }
                    break;
            }
            // 关键算法
            lp.leftMargin = (int) ((mScreen1_4 * index) + (((double) (index + 1) / viewPagerW) * mScreen1_4));
            img_line.setLayoutParams(lp);
            // 滚动到顶部
            myScrollView.smoothScrollTo(0, 0);
        }
    }

    /**
     * 将文字设置为未选中时的颜色
     */
    private void resetTextColor() {
        textMicroBlog.setTextColor(unselect_color);
        textTransaction.setTextColor(unselect_color);
        textPlan.setTextColor(unselect_color);
        textPlayReward.setTextColor(unselect_color);
        textQa.setTextColor(unselect_color);

        fragmentMicroBlog.setVisibility(View.GONE);
        linearTransaction.setVisibility(View.GONE);
        linearPlan.setVisibility(View.GONE);
        playRewardList.setVisibility(View.GONE);
        fragmentQa.setVisibility(View.GONE);
    }

    /**
     * 获取微博页面数据
     */
    private void getWeiBoData() {
//        new SendInfoTasknotereplylistloadmore().execute(new TaskParams(
//                        Constants.subscribeListUrl,
//                        new String[]{"type", "myfeed"},
//                        new String[]{"id", Constants.staticmyuidstr},
//                        new String[]{"mid", uidstr},
//                        new String[]{"p", weiboPage + ""}
//                )
//        );
        // 获取微博列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                feedSpaceStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/feed/getList?type=space&mid=" + Constants.staticmyuidstr + "&uid=" + uidstr + "&p=" + weiboPage);
                handler.sendEmptyMessage(12);
            }
        }).start();
    }

    /**
     * 获取自选股的条数
     */
    private void getStockData() {
        // 开线程获取用户获取自选股
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用接口获取用户获取自选股
                resstr = HttpUtil.restHttpGet(Constants.moUrl + "/user/getUserOptional&count=1&uid=" + uidstr + "&token=" + Constants.apptoken);
                handler.sendEmptyMessage(1);
            }
        }).start();
    }
//     * 获取自选股的数据
//     */
//    private void getStockData() {
//        // 开线程获取用户获取自选股
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // 调用接口获取用户获取自选股
//                resstr = HttpUtil.restHttpGet(Constants.moUrl + "/user/getUserOptional&uid=" + uidstr + "&token=" + Constants.apptoken);
//                handler.sendEmptyMessage(0);
//            }
//        }).start();
//    }

    /**
     * 获取投资计划的数据
     */
    private void getPlanData() {
        // 开线程获取用户获取自选股
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用接口获取用户获取自选股
                resstrPlan = HttpUtil.restHttpGet(Constants.moUrl + "/dealplan/index/list?uid=" + uidstr + "&order=time&p=" + planPage);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    /**
     * 获取打赏文的数据
     */
    private void getRewardData() {

        // 获取微博内参列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                feedSpacePayStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/feed/getList?type=spacepay&mid=" +  Constants.staticmyuidstr + "&uid=" + uidstr + "&p=" + rewardPage);
                handler.sendEmptyMessage(13);
            }
        }).start();
    }

//    private class SendInfoTasknotereplylistloadmore extends
//            AsyncTask<TaskParams, Void, String> {
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            if (result == null) {
//                CustomToast.makeText(UserDetailNewActivity.this, "", Toast.LENGTH_SHORT).show();
//                // 千万别忘了告诉控件刷新完毕了哦！失败
//                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
//                dialog.dismissDlog();
//                return;
//            }
//            String digg_countstr = "";
//            String comment_countstr = "";
//            String repost_countstr = "";
//            String source_user_infostr = "";
//            String source_contentstr = "";
//            String source_feed_idstr = "";
//            String sourceuidstr = "";
//            String sourceunamestr = "";
//            String avatar_middlestr = "";
//            String user_infostr = "";
//            // 获取概要
//            String introduction = "";
//            // 判断是否是微博
//            Object state = null;
//            // 获取水晶币个数
//            String reward = "";
//            String datastr2 = null;
//            List<Map<String, Object>> datastrlists2 = null;
//            super.onPostExecute(result);
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//                // 获取点赞标识的数组
//                String diggArrstr;
//                if (jsonObject.getString("diggArr") == null) {
//                    diggArrstr = "";
//                } else {
//                    diggArrstr = jsonObject.getString("diggArr");
//                }
//                if (datastr2 == null) {
//                    datastr2 = jsonObject.getString("data");
//                    datastrlists2 = JsonTools.listKeyMaps(datastr2);
//                }
//
//                if (datastrlists2 == null) {
//                    datastrlists2 = new ArrayList<Map<String, Object>>();
//                }
//                for (Map<String, Object> datastrmap : datastrlists2) {
//                    String isdigg = "0";// isdigg为0为未点赞为1为已点赞
//                    String feed_idstr = datastrmap.get("feed_id").toString();
//                    if (diggArrstr.contains(feed_idstr)) {
//                        isdigg = "1";
//                    }
//
//                    // 微博类型
//                    String typestr = datastrmap.get("type").toString();
//                    // 获取为本类容
//                    String contentstr = "";
//                    introduction = "";
//                    state = datastrmap.get("state");
//                    // 先判断是否是付费文章如果是就获取概要
//                    if (state != null && "1".equals(state.toString())) {
//                        reward = datastrmap.get("reward").toString();
//                        Object introd = datastrmap.get("introduction");
//                        if (introd != null && !"null".equals(introd.toString())) {
//                            introduction = introd.toString();
//                        }
//                    }
//                    if (datastrmap.get("body") == null) {
//                        contentstr = "内容为空";
//                    } else {
//                        contentstr = datastrmap.get("body") + "";
//                    }
//
//                    String title = datastrmap.get("title") + "";
//                    if (!"".equals(title) && !"null".equals(title)) {
//                        contentstr = "<font color=\"#4471BC\" >" + title + "</font><br/>" + contentstr;
//                    }
//                    // 替换表情符的 肯定有问题 要改
//                    contentstr = contentstr.replace("[", "<img src=\"");
//                    contentstr = contentstr.replace("]", "\"/>");
//
//                    HashMap<String, String> map2 = new HashMap<String, String>();
//                    map2.put("type", typestr);
//                    if ("repost".equals(typestr)) {
//                        String api_sourcestr = datastrmap.get("api_source").toString();
//                        List<Map<String, Object>> api_sourcestrlists = JsonTools.listKeyMaps("[" + api_sourcestr + "]");
//                        for (Map<String, Object> api_sourcestrmap : api_sourcestrlists) {
//
//                            source_user_infostr = api_sourcestrmap.get("source_user_info") + "";
//                            source_contentstr = api_sourcestrmap.get("source_content") + "";
//
//                            source_feed_idstr = api_sourcestrmap.get("feed_id") + "";
//
//                            title = api_sourcestrmap.get("title") + "";
//                            if (!"".equals(title) && !"null".equals(title)) {
//                                source_contentstr = "<font color=\"#4471BC\" >" + title + "</font><br/>" + source_contentstr;
//                            }
//
//                            state = api_sourcestrmap.get("state");
//                            // 先判断是否是付费文章如果是就获取概要
//                            if (state != null && "1".equals(state.toString())) {
//                                if (api_sourcestrmap.get("reward") != null) {
//                                    reward = api_sourcestrmap.get("reward").toString();
//                                }
//                                String gaiyao = api_sourcestrmap.get("zy").toString();
//                                source_contentstr = "<font color=\"#4471BC\" >" + title + "</font><Br/>" + gaiyao;
//                            }
//                            map2.put("source_contentstr", source_contentstr);
//                            map2.put("source_feed_idstr", source_feed_idstr);
//
//                            List<Map<String, Object>> source_user_infostrlists = JsonTools.listKeyMaps("[" + source_user_infostr + "]");
//                            for (Map<String, Object> source_user_infostrmap : source_user_infostrlists) {
//                                sourceuidstr = source_user_infostrmap.get("uid").toString();
//                                sourceunamestr = source_user_infostrmap.get("uname").toString();
//                                avatar_middlestr = source_user_infostrmap.get("avatar_middle").toString();
//                                String userGroup = source_user_infostrmap.get("user_group").toString();
//                                map2.put("isVipSource", userGroup);
//                                map2.put("sourceuidstr", sourceuidstr);
//                                map2.put("sourceuname", sourceunamestr);
//                                map2.put("sourceavatar_middlestr", avatar_middlestr);
//                            }
//                        }
//                    }
//
//                    String publish_timestr = datastrmap.get("publish_time").toString();
//                    if (datastrmap.get("digg_count") == null) {
//                        digg_countstr = "0";
//                    } else {
//                        digg_countstr = datastrmap.get("digg_count").toString();
//                    }
//                    if (datastrmap.get("comment_count") == null) {
//                        comment_countstr = "0";
//                    } else {
//                        comment_countstr = datastrmap.get("comment_count").toString();
//                    }
//                    if (datastrmap.get("repost_count") == null) {
//                        repost_countstr = "0";
//                    } else {
//                        repost_countstr = datastrmap.get("repost_count").toString();
//                    }
//                    publish_timestr = CalendarUtil.formatDateTime(Utils
//                            .getStringtoDate(publish_timestr));
//                    if (contentstr.contains("feed_img_lists")) {
//                        contentstr = contentstr.substring(0,
//                                contentstr.indexOf("feed_img_lists"));
//                    }
//                    String attach_urlstr;
//                    if (datastrmap.get("attach_url") == null) {
//                        attach_urlstr = "";
//                    } else {
//                        attach_urlstr = datastrmap.get("attach_url")
//                                .toString();
//                        // 解析短微博图片地址
//                        attach_urlstr = attach_urlstr.substring(1, attach_urlstr.length() - 1);
//                    }
//
//                    // 不带图的富文本
//                    if ("".equals(contentstr)) {
//                        contentstr = "//";
//                    }
//                    // 如果是付费文章就有概要 显示内容为标题+概要
//                    if (!"".equals(introduction)) {
//                        contentstr = "<font color=\"#4471BC\" >" + title + "</font><Br/>" + introduction;
//                    }
//                    if (state != null) {
//                        map2.put("state", state.toString());
//                    }
//                    map2.put("reward", reward);
//                    map2.put("feed_id", feed_idstr);
//                    map2.put("content", contentstr);
//                    map2.put("create", publish_timestr);
//                    map2.put("digg_count", digg_countstr);
//                    map2.put("comment_count", comment_countstr);
//                    map2.put("repost_count", repost_countstr);
//                    map2.put("isdigg", isdigg);
//                    map2.put("attach_url", attach_urlstr);
//                    if (datastrmap.get("user_info") == null) {
//                        user_infostr = "";
//                    } else {
//                        user_infostr = datastrmap.get("user_info")+"";
//                    }
//                    List<Map<String, Object>> user_infostrlists = JsonTools
//                            .listKeyMaps("[" + user_infostr + "]");
//                    for (Map<String, Object> user_infostrmap : user_infostrlists) {
//                        String uidstr = user_infostrmap.get("uid").toString();
//                        String unamestr = user_infostrmap.get("uname").toString();
//                        String avatar_middlestr2 = user_infostrmap.get("avatar_middle").toString();
//                        String userGroup = user_infostrmap.get("user_group").toString();
//                        map2.put("isVip", userGroup);
//                        map2.put("uid", uidstr);
//                        map2.put("uname", unamestr);
//                        map2.put("avatar_middle", avatar_middlestr2);
//                    }
//                    listusercommonnoteData.add(map2);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            usercommonnoteAdapter.setlistData(listusercommonnoteData);
//            // 千万别忘了告诉控件刷新完毕了哦！
//            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//            dialog.dismissDlog();
//        }
//    }

    /**
     * 线程更新Ui
     */
    private Handler handler = new Handler() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    try {
                        JSONObject jsonObject = new JSONObject(resstrPlan);
                        if ("failed".equals(jsonObject.getString("status"))) {
                            if (linearPlan.getVisibility() == View.VISIBLE) {
                                // 千万别忘了告诉控件刷新完毕了哦！
                                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            }
                            dialog.dismissDlog();
                            return;
                        }
                        ArrayList<PlanEntity> tomliveList = (ArrayList<PlanEntity>) JSON.parseArray(jsonObject.getString("data"), PlanEntity.class);
                        planEntityArrayList.addAll(tomliveList);
                        planAdapter.setlistData(planEntityArrayList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (linearPlan.getVisibility() == View.VISIBLE) {
                        // 千万别忘了告诉控件刷新完毕了哦！
                        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }
                    dialog.dismissDlog();
                    break;
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(resstr);
                        if ("failed".equals(jsonObject.getString("status"))) {
                            return;
                        }
                        optional_stock_tv.setText(jsonObject.getString("data"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (linearPlan.getVisibility() == View.VISIBLE) {
                        // 千万别忘了告诉控件刷新完毕了哦！
                        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }
                    dialog.dismissDlog();
                    break;
//                case 0:
//                    try {
//                        JSONObject jsonObject = new JSONObject(resstr);
//                        if ("failed".equals(jsonObject.getString("status"))) {
//                            if (linearStock.getVisibility() == View.VISIBLE) {
//                                // 千万别忘了告诉控件刷新完毕了哦！
//                                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//                            }
//                            dialog.dismissDlog();
//                            return;
//                        }
//                        // 自选股的数据
//                        ArrayList<OptionalEntity> optionalList = (ArrayList<OptionalEntity>) JSON.parseArray(jsonObject.getString("data"), OptionalEntity.class);
//                        getoptionalData(optionalList);
//                        optionalArrayList = optionalList;
//                        // 加载自选股股票信息
//                        stockAdapter.setlistData(optionalList);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    if (linearStock.getVisibility() == View.VISIBLE) {
//                        // 千万别忘了告诉控件刷新完毕了哦！
//                        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//                    }
//                    dialog.dismissDlog();
//                    break;
//                case 1:
//                    // 重新加载自选股列表
//                    for (int i = 0; i < optionalArrayList.size(); i++) {
//                        Map<String, String> mStr = mapZxgStr.get(optionalArrayList.get(i).getStock());
//                        if (mStr != null && mStr.size() > 0) {
//                            optionalArrayList.get(i).setPrice(mStr.get("price"));
//                            optionalArrayList.get(i).setRising(mStr.get("rising"));
//                            optionalArrayList.get(i).setIstype(mStr.get("type"));
//                        }
//                    }
//                    // 刷新自选股列表
//                    stockAdapter.setlistData(optionalArrayList);
//                    break;
                case 2:
                    try {
                        JSONObject jsonObject = new JSONObject(mMresstr);
                        if ("failed".equals(jsonObject.getString("status"))) {
//                            Toast.makeText(getActivity(),jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                            if (linearTransaction.getVisibility() == View.VISIBLE) {
                                // 千万别忘了告诉控件刷新完毕了哦！
                                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            }
                            return;
                        }
                        // 持仓的数据
                        ArrayList<PositionEntity> positionList = (ArrayList<PositionEntity>) JSON.parseArray(jsonObject.getString("data"), PositionEntity.class);
                        positionArrayList = positionList;
                        // 获取最新的股票信息
                        getRealTimeData(positionList);
                        listAdapter.setlistData(positionArrayList);
                        ((TextView) findViewById(R.id.position_count_tv)).setText("当前持仓(" + positionList.size() + ")");
                        // 滚动到顶部
                        myScrollView.smoothScrollTo(0, 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (linearTransaction.getVisibility() == View.VISIBLE) {
                        // 千万别忘了告诉控件刷新完毕了哦！
                        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }
                    dialog.dismissDlog();
                    break;
                case 3:
                    Double price = 0.0;
                    // 收益率
                    String profitStr = "0";
                    // 成本价
                    String costPrice = "0";
                    for (int i = 0; i < positionArrayList.size(); i++) {
                        Map<String, String> mStr = mapStr.get(positionArrayList.get(i).getStock());
                        price = Double.valueOf(mStr.get("price"));
                        positionArrayList.get(i).setLatest_price(price + "");
                        positionArrayList.get(i).setIsType(mStr.get("type"));
                        // 成本价
                        costPrice = positionArrayList.get(i).getCost_price();
                        // 收益率
                        profitStr = Utils.getNumberFormat2((Double.valueOf(price) - Double.valueOf(costPrice)) / Math.abs(Double.valueOf(costPrice)) * 100 + "");
                        positionArrayList.get(i).setRatio(profitStr);
                        // 持仓数量
                        int positionValue = Integer.valueOf(positionArrayList.get(i).getPosition_number());
                        // 最新市值的累加
                        market += price * positionValue;
                    }
//                    // 更新总资产
//                    funds.setText(Utils.getNumberFormat2(market+availableFundsValue+""));
////                     更新仓位
//                    position.setText(Utils.getNumberFormat2(market/(market+availableFundsValue)*100+"")+"%");
                    // 更新最新市值
                    marketValue.setText(Utils.getNumberFormat2(market + "") + "元");
                    // 刷新持仓列表
                    listAdapter.setlistData(positionArrayList);
                    break;
                case 4:
                    try {
                        JSONObject jsonObject = new JSONObject(xxstr);
                        if ("failed".equals(jsonObject.getString("status"))) {
//                            Toast.makeText(UserDetailNewActivity.this, jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                            dialog.dismissDlog();
                            return;
                        }
                        JSONObject jsonStr = new JSONObject(jsonObject.getString("data"));
                        // 仓位
                        position.setText(jsonStr.getString("position") + "%");
                        // 总收益
                        String totalRateValue = jsonStr.getString("total_rate");
                        totalRate.setText(Utils.getNumberFormat2(totalRateValue) + "%");
                        totalRate1.setText(Utils.getNumberFormat2(totalRateValue) + "%");
                        if (Double.valueOf(totalRateValue) >= 0) {
                            totalRate1.setTextColor(totalRate1.getResources().getColor(R.color.color_ef3e3e));
                        } else {
                            totalRate1.setTextColor(totalRate1.getResources().getColor(R.color.color_1bc07d));
                        }
                        // 排名
                        totalProfitRank.setText(jsonStr.getString("total_profit_rank"));
                        Double fundsValue = jsonStr.getDouble("funds");
                        // 总资产
                        funds.setText(Utils.getNumberFormat2(fundsValue + "") + "元");
                        // 可用资金
                        availableFundsValue = jsonStr.getDouble("available_funds");
                        // 可用资金
                        availableFunds.setText(Utils.getNumberFormat2(availableFundsValue + "") + "元");
                        // 周收益率
                        String weekRate = jsonStr.getString("week_avg_profit_rate");
                        weekAvgProfitRate.setText(Utils.getNumberFormat2(weekRate) + "%");
                        // 股票市值
                        marketValue.setText(Utils.getNumberFormat2(fundsValue - availableFundsValue + "") + "元");
                        if (Double.valueOf(weekRate) >= 0) {
                            weekAvgProfitRate.setTextColor(weekAvgProfitRate.getResources().getColor(R.color.color_ef3e3e));
                        } else {
                            weekAvgProfitRate.setTextColor(weekAvgProfitRate.getResources().getColor(R.color.color_1bc07d));
                        }
                        // 比赛胜率
                        winRate.setText(Utils.getNumberFormat2(jsonStr.getString("success_rate")) + "%");
                        winRate1.setText(Utils.getNumberFormat2(jsonStr.getString("success_rate")) + "%");
                        String timeStr = jsonStr.getString("time");
                        // 建户时间
                        if (timeStr.length() > 10) {
                            time.setText(timeStr.substring(0, 10));
                        } else {
                            if("null".equals(timeStr) || "".equals(timeStr)){
                                time.setText("--");
                            }else{
                                time.setText(timeStr);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.dismissDlog();
                    break;
                case 5:
                    lineAveData = new ArrayList<SpotEntity>();
                    xtitle = new ArrayList<String>();
                    try {
                        JSONObject jsonObject = new JSONObject(chartstr);
                        if ("failed".equals(jsonObject.getString("status"))) {
                            findViewById(R.id.transaction_chat_ll).setVisibility(View.GONE);
//                            Toast.makeText(UserDetailNewActivity.this, jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("data"));
                        float endFunds = 0.0f;
                        String time = "";
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = (JSONObject) jsonArray.get(i);
                            endFunds = (float) jsonObject.getDouble("endFunds");
                            time = jsonObject.getString("time");
                            if (i == 0) {
                                maxY = endFunds;
                                miniY = endFunds;
                            } else {
                                if (maxY < endFunds) {
                                    maxY = endFunds;
                                } else if (miniY > endFunds) {
                                    miniY = endFunds;
                                }
                            }
                            SpotEntity spotEntity = new SpotEntity();
                            spotEntity.setSpotData(endFunds);
                            spotEntity.setSpotTime(time);
                            lineAveData.add(spotEntity);
                            xtitle.add(time);
                        }
                        if (lineAveData.size() > 1) {
                            initMinuteChart();
                            findViewById(R.id.transaction_chat_ll).setVisibility(View.VISIBLE);
                        } else {
                            findViewById(R.id.transaction_chat_ll).setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.dismissDlog();
                    break;
                case 6:
                    try {
                        JSONObject jsonObject = new JSONObject(desertStr);
                        if ("failed".equals(jsonObject.getString("status"))) {
                            return;
                        }
                        String data = jsonObject.getString("data");
                        if (data.length() < 3) {
                            subscribeTv.setText("订阅");
                        } else {
                            JSONObject jsonStr = new JSONObject(data);
                            desertId = jsonStr.getString("id");
                            desertTime = jsonStr.getString("exp_time");
                            desertExtend = jsonStr.getString("is_extend");
                            subscribeTv.setText("已订阅");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 7:
                    try {
                        JSONObject jsonObject = new JSONObject(tomliveStr);
                        if ("failed".equals(jsonObject.getString("status"))) {
                            tomliveLv.setVisibility(View.GONE);
                            return;
                        }
                        String data = jsonObject.getString("data");
                        if ("1".equals(data)) {
                            roomId = jsonObject.getString("room_id");
                            tomliveLv.setVisibility(View.VISIBLE);
                        } else {
                            tomliveLv.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 8:
                    try {
                        JSONObject jsonObject = new JSONObject(resstrAsk);
                        if ("failed".equals(jsonObject.getString("status"))) {
                            return;
                        }
                        sjbCount = jsonObject.getString("data");
                        if (!sjbCount.equals("0")) {
                            qaBt.setText("我要提问（" + sjbCount + "水晶币/次）");
                            qaBt.setBackground(getResources().getDrawable(R.drawable.buttonstyle6));
                            isRn = true;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.dismissDlog();
                    break;
                case 9:
                    try {
                        JSONObject jsonObject = new JSONObject(resstrAll);
                        if ("failed".equals(jsonObject.getString("status"))) {
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            return;
                        }
                        ArrayList<QuestionAnswerEntity> tomliveList = (ArrayList<QuestionAnswerEntity>) JSON.parseArray(jsonObject.getString("data"), QuestionAnswerEntity.class);
                        if (allPage == 1) {
                            listDataAll = tomliveList;
                        } else {
                            listDataAll.addAll(tomliveList);
                        }
                        adapterAll.setlistData(listDataAll);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // 千万别忘了告诉控件刷新完毕了哦！
                    ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    dialog.dismissDlog();
                    if (allPage == 1) {
                        // 滚动到顶部
                        myScrollView.smoothScrollTo(0, 0);
                    }
                    break;
                case 10:
                    try {
                        JSONObject jsonObject = new JSONObject(resstrMy);
                        if ("failed".equals(jsonObject.getString("status"))) {
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            return;
                        }
                        ArrayList<QuestionAnswerEntity> tomliveList = (ArrayList<QuestionAnswerEntity>) JSON.parseArray(jsonObject.getString("data"), QuestionAnswerEntity.class);
                        if (myPage == 1) {
                            listDataMy = tomliveList;
                        } else {
                            listDataMy.addAll(tomliveList);
                        }
                        adapterMy.setlistData(listDataMy);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // 千万别忘了告诉控件刷新完毕了哦！
                    ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    break;
                case 11:
                    try {
                        JSONObject jsonObject = new JSONObject(resstrAll);
                        Toast.makeText(UserDetailNewActivity.this, jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                        if ("failed".equals(jsonObject.getString("status"))) {
                            dialog.dismissDlog();
                            return;
                        }
                        allPage = 1;
                        getDataAll();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 12:
                    dialog.dismissDlog();
                    // 博主微博列表显示
                    try {
                        JSONObject jsonObject = new JSONObject(feedSpaceStr);
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            // 请求失败的情况
                            CustomToast.makeText(UserDetailNewActivity.this,jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            // 千万别忘了告诉控件刷新完毕了哦！失败
                            ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                            return;
                        }
                        // 千万别忘了告诉控件刷新完毕了哦！
                        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                        ArrayList<FeedListEntity> feedListEntitys = (ArrayList<FeedListEntity>) JSON.parseArray(jsonObject.getString("data"), FeedListEntity.class);
                        if (weiboPage == 1){
                            userFeedListEntityList = feedListEntitys;
                        }else{
                            userFeedListEntityList.addAll(feedListEntitys);
                        }
                        userFeedListAdapter.setlistData(userFeedListEntityList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 13:
                    dialog.dismissDlog();
                    // 博主内参列表显示
                    try {
                        JSONObject jsonObject = new JSONObject(feedSpacePayStr);
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            // 请求失败的情况
                            CustomToast.makeText(UserDetailNewActivity.this,jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            // 千万别忘了告诉控件刷新完毕了哦！失败
                            ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                            return;
                        }
                        // 千万别忘了告诉控件刷新完毕了哦！
                        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                        ArrayList<FeedListEntity> feedListEntitys = (ArrayList<FeedListEntity>) JSON.parseArray(jsonObject.getString("data"), FeedListEntity.class);
                        if (rewardPage == 1){
                            payFeedListEntityList = feedListEntitys;
                        }else{
                            payFeedListEntityList.addAll(feedListEntitys);
                        }
                        payFeedListAdapter.setlistData(payFeedListEntityList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 14:
                    // 博主的相关信息
                    try {
                        JSONObject jsonObject = new JSONObject(userStr);
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            return;
                        }
                        UserEntity userEntity = JSON.parseObject(jsonObject.getString("data"),UserEntity.class);
                        unamestr = userEntity.getUname();
                        following_count2.setText(userEntity.getFollowing_count());
                        follower_count2.setText(userEntity.getFollower_count());
                        if(userEntity.getFollow() != null)
                        {
                            followingstr = userEntity.getFollow().getFollowing();
                            followerstr = userEntity.getFollow().getFollower();// 0是未关注,1是已关注
                            if ("0".equals(followingstr)) {
                                followersign1.setText(" + 关注");
                            } else {
                                if ("1".equals(followerstr)) {
                                    followersign1.setText("相互关注");
                                } else {
                                    followersign1.setText("取消关注");
                                }
                            }
                        }
                        String introstr = userEntity.getIntro();
                        if (introstr == null || "".equals(introstr)) {
                            introstr = "暂无简介";
                        }
                        if ("1".equals(userEntity.getSex())) {
                            usersex1.setImageResource(R.mipmap.nan);
                        } else {
                            usersex1.setImageResource(R.mipmap.nv);
                        }
                        username2.setText(unamestr);
                        userintro1.setText(introstr);
                        ImageLoader.getInstance().displayImage(userEntity.getAvatar_middle(),
                                headimg2, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
                        ViewUtil.setUpVipNew(userEntity.getUser_group_icon_url(), vipImg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 15:
                    // 博主的相关信息
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        CustomToast.makeText(UserDetailNewActivity.this,jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            return;
                        }

                        if ("0".equals(followingstr)) {
                            if ("1".equals(followerstr)) {
                                followersign1.setText("相互关注");
                            } else {
                                followersign1.setText("取消关注");
                            }
                            followingstr = "1";
                            int follows = Integer.parseInt((follower_count2.getText()
                                    .toString()));
                            follower_count2.setText(String.valueOf((follows + 1)));
                        } else {
                            followersign1.setText(" + 关注");
                            followingstr = "0";
                            int follows = Integer.parseInt((follower_count2.getText()
                                    .toString()));
                            follower_count2.setText(String.valueOf((follows - 1)));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

//    /**
//     * 获取自选股股票实时数据进行处理
//     *
//     * @param listOptional
//     */
//    private void getoptionalData(ArrayList<OptionalEntity> listOptional) {
//        String str = "";
//        for (OptionalEntity optionalEntity : listOptional) {
//            if (!"".equals(str)) {
//                str += ",";
//            }
//            str += Utils.judgeSharesCode(optionalEntity.getStock());
//        }
//        // 开线程获股票当前信息
//        final String finalStr = str;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // 调用接口获取股票当前行情数据（返回股票代码和最新价格的map）
//                mapZxgStr = sharesUtil.getsharess(finalStr);
//                // 重新加载自选股数据
//                handler.sendEmptyMessage(1);
//            }
//        }).start();
//    }


    /**
     * 获取交易相关的数据
     */
    private void getTransactionData() {
        // 开线程获历史交易信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用接口获取股票当前行情数据
                mMresstr = HttpUtil.restHttpGet(Constants.moUrl + "/users&uid=" + uidstr + "&token=" + Constants.apptoken);
                handler.sendEmptyMessage(2);
            }
        }).start();
        // 开线程获取用户账户信息和总盈利排名
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用接口获取用户账户信息和总盈利排名
                xxstr = HttpUtil.restHttpGet(Constants.moUrl + "/users/" + uidstr + "&token=" + Constants.apptoken + "&uid=" + Constants.staticmyuidstr);
                handler.sendEmptyMessage(4);
            }
        }).start();
        // 开线程获取用户分时图数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用接口获取用户账户信息和总盈利排名
                chartstr = HttpUtil.restHttpGet(Constants.moUrl + "/share/getTimeChart&uid=" + uidstr + "&token=" + Constants.apptoken);
                handler.sendEmptyMessage(5);
            }
        }).start();
    }


    /**
     * 获取持仓股票实时数据进行处理
     *
     * @param listPosition
     */
    private void getRealTimeData(ArrayList<PositionEntity> listPosition) {
        String codeStr = "";
        for (PositionEntity positionEntity : listPosition) {
            if (!"".equals(codeStr)) {
                codeStr += ",";
            }
            codeStr += Utils.judgeSharesCode(positionEntity.getStock());
        }
        if ("".equals(codeStr)) {
            return;
        }
        // 开线程获股票当前信息
        final String finalCodeStr = codeStr;
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用接口获取股票当前行情数据（返回股票代码和最新价格的map）
                mapStr = sharesUtil.getsharess(finalCodeStr);
                handler.sendEmptyMessage(3);
            }
        }).start();
    }

    /**
     * 设置分时图的属性
     */
    private void initMinuteChart() {
        List<String> ytitleInner = new ArrayList<String>();
        ytitleInner.add(maxY + "%");
        ytitleInner.add("");
        ytitleInner.add(miniY + "%");
        ytitleInner.add("");
        ytitle = new ArrayList<String>();
        ytitle.add("");
        ytitle.add("");
        ytitle.add("");
        int size = xtitle.size();
        List<String> listTitle = new ArrayList<String>();
        listTitle.add(xtitle.get(0));
        if (size > 2) {
            listTitle.add(xtitle.get(size / 2));
        }
        listTitle.add(xtitle.get(size - 1));

        List<LineEntity> lines = new ArrayList<LineEntity>();
        //日均线数据
        LineEntity MAve = new LineEntity();
        MAve.setTitle("MAve");
        MAve.setLineColor(Color.rgb(255, 130, 142));
        MAve.setBackger(true);
        MAve.setLineData(lineAveData);
        lines.add(MAve);

        assetsChart.setAxisMarginTop(10);
        assetsChart.setAxisMarginLeft(10);
        assetsChart.setAxisMarginRight(10);
        assetsChart.setAxisMarginBottom(30);
        // 画布外的标题
        assetsChart.setAxisYTitles(ytitle);
        assetsChart.setAxisXTitles(listTitle);
        // 画布内的标题
        assetsChart.setAxisYTitlesR(ytitleInner);
        //y轴的最大值
        assetsChart.setMaxValue(maxY);
        //y轴的最小值
        assetsChart.setMinValue(miniY);
        //一共需要画多少点
        assetsChart.setMaxPointNum(lineAveData.size() - 1);
        // X轴上的标题是否显示
        assetsChart.setDisplayAxisXTitle(true);
        // Y轴上的标题是否显示
        assetsChart.setDisplayAxisYTitle(true);
        // 纬线是否显示
        assetsChart.setDisplayLatitude(true);
        // 经线是否显示
        assetsChart.setDisplayLongitude(true);
        // 经线是否为虚线
        assetsChart.setDashLongitude(true);
        assetsChart.setLineData(lines);
        // redraw
        assetsChart.invalidate();
    }

    /**
     * 订阅的单机事件
     *
     * @param view
     */
    public void subscribeClick(View view) {
        // menghuan 不用登陆也可以用
        // 如果未登陆跳转到登陆页面
        if (!Constants.isLogin){
            Intent intent = new Intent(this, loginActivity.class);
            startActivity(intent);
            return;
        }
        // 防止多次点击
        if (Utils.isFastDoubleClick3()) {
            return;
        }
        if ("".equals(desertId)) {
            // 跳转到订阅确认页面
            Intent intent = new Intent();
            intent.putExtra("name", unamestr);
            intent.putExtra("uid", uidstr);
            intent.setClass(UserDetailNewActivity.this, SubscribeConfirmActivity.class);
            startActivity(intent);
        } else {
            // 打开订阅选择页面popup
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", unamestr);
            map.put("time", desertTime);
            map.put("isExtend", desertExtend);
            map.put("uid", uidstr);
            map.put("id", desertId);
            //实例化SelectPicPopupWindow
            SubscribeChoicePopup menuWindow = new SubscribeChoicePopup(UserDetailNewActivity.this, map);
            //显示窗口
            menuWindow.showAtLocation(UserDetailNewActivity.this.findViewById(R.id.title_rl), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
        }
    }

    /**
     * 查看全部持仓的单机事件
     *
     * @param view
     */
    public void DetailPositionClick(View view) {
        // 防止多次点击
        if (Utils.isFastDoubleClick3()) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(UserDetailNewActivity.this, HistoryPositionActivity.class);
        intent.putExtra("uid", uidstr);
        startActivity(intent);
    }

    // 修改订阅文字
    public void udpateSubscribeTx() {
        desertId = "";
        subscribeTv.setText("订阅");
    }


//    private class SendInfoTaskmyattentionweibolist extends AsyncTask<TaskParams, Void, String> {
//
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            //调用共通方法获取网络数据
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            if (result == null) {
//                CustomToast.makeText(UserDetailNewActivity.this, "", Toast.LENGTH_SHORT).show();
//                // 千万别忘了告诉控件刷新完毕了哦！失败
//                ptrl.refreshFinish(PullToRefreshLayout.FAIL);
//                dialog.dismissDlog();
//                return;
//            }
//            String digg_countstr = "";
//            String comment_countstr = "";
//            String repost_countstr = "";
//            String user_infostr = "";
//            // 获取概要
//            String introduction = "";
//            // 判断是否是微博
//            Object state = null;
//            // 获取水晶币个数
//            String reward = "";
//            String datastr2 = null;
//            List<Map<String, Object>> datastrlists2 = null;
//            super.onPostExecute(result);
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//                // 获取点赞标识的数组
//                String diggArrstr;
//                if (jsonObject.getString("diggArr") == null) {
//                    diggArrstr = "";
//                } else {
//                    diggArrstr = jsonObject.getString("diggArr");
//                }
//                if (datastr2 == null) {
//                    datastr2 = jsonObject.getString("data");
//                    datastrlists2 = JsonTools.listKeyMaps(datastr2);
//                }
//
//                if (datastrlists2 == null) {
//                    datastrlists2 = new ArrayList<Map<String, Object>>();
//                }
//                for (Map<String, Object> datastrmap : datastrlists2) {
//                    String isdigg = "0";// isdigg为0为未点赞为1为已点赞
//                    String feed_idstr = datastrmap.get("feed_id").toString();
//                    if (diggArrstr.contains(feed_idstr)) {
//                        isdigg = "1";
//                    }
//
//                    // 微博类型
//                    String typestr = datastrmap.get("type").toString();
//                    // 获取为本类容
//                    String contentstr = "";
//                    introduction = "";
//                    state = datastrmap.get("state");
//                    // 先判断是否是付费文章如果是就获取概要
//                    if (state != null && "1".equals(state.toString())) {
//                        reward = datastrmap.get("reward").toString();
//                        Object introd = datastrmap.get("introduction");
//                        if (introd != null && !"null".equals(introd.toString())) {
//                            introduction = introd.toString();
//                        }
//                    }
//                    if (datastrmap.get("body") == null) {
//                        contentstr = "内容为空";
//                    } else {
//                        contentstr = datastrmap.get("body") + "";
//                    }
//
//                    String title = datastrmap.get("title") + "";
//                    if (!"".equals(title) && !"null".equals(title)) {
//                        contentstr = "<font color=\"#4471BC\" >" + title + "</font><br/>" + contentstr;
//                    }
//                    // 替换表情符的 肯定有问题 要改
//                    contentstr = contentstr.replace("[", "<img src=\"");
//                    contentstr = contentstr.replace("]", "\"/>");
//
//                    HashMap<String, String> map2 = new HashMap<String, String>();
//                    map2.put("type", typestr);
//                    String publish_timestr = datastrmap.get("publish_time").toString();
//                    if (datastrmap.get("digg_count") == null) {
//                        digg_countstr = "0";
//                    } else {
//                        digg_countstr = datastrmap.get("digg_count").toString();
//                    }
//                    if (datastrmap.get("comment_count") == null) {
//                        comment_countstr = "0";
//                    } else {
//                        comment_countstr = datastrmap.get("comment_count").toString();
//                    }
//                    if (datastrmap.get("repost_count") == null) {
//                        repost_countstr = "0";
//                    } else {
//                        repost_countstr = datastrmap.get("repost_count").toString();
//                    }
//                    publish_timestr = CalendarUtil.formatDateTime(Utils
//                            .getStringtoDate(publish_timestr));
//                    if (contentstr.contains("feed_img_lists")) {
//                        contentstr = contentstr.substring(0,
//                                contentstr.indexOf("feed_img_lists"));
//                    }
//                    String attach_urlstr;
//                    if (datastrmap.get("attach_url") == null) {
//                        attach_urlstr = "";
//                    } else {
//                        attach_urlstr = datastrmap.get("attach_url")
//                                .toString();
//                        // 解析短微博图片地址
//                        attach_urlstr = attach_urlstr.substring(1, attach_urlstr.length() - 1);
//                    }
//
//                    // 不带图的富文本
//                    if ("".equals(contentstr)) {
//                        contentstr = "//";
//                    }
//                    // 如果是付费文章就有概要 显示内容为标题+概要
//                    if (!"".equals(introduction)) {
//                        contentstr = "<font color=\"#4471BC\" >" + title + "</font><Br/>" + introduction;
//                    }
//                    if (state != null) {
//                        map2.put("state", state.toString());
//                    }
//                    map2.put("reward", reward);
//                    map2.put("feed_id", feed_idstr);
//                    map2.put("content", contentstr);
//                    map2.put("create", publish_timestr);
//                    map2.put("digg_count", digg_countstr);
//                    map2.put("comment_count", comment_countstr);
//                    map2.put("repost_count", repost_countstr);
//                    map2.put("isdigg", isdigg);
//                    map2.put("attach_url", attach_urlstr);
//                    if (datastrmap.get("user_info") == null) {
//                        user_infostr = "";
//                    } else {
//                        user_infostr = datastrmap.get("user_info")
//                                .toString();
//                    }
//                    List<Map<String, Object>> user_infostrlists = JsonTools
//                            .listKeyMaps("[" + user_infostr + "]");
//                    for (Map<String, Object> user_infostrmap : user_infostrlists) {
//                        String uidstr = user_infostrmap.get("uid").toString();
//                        String unamestr = user_infostrmap.get("uname").toString();
//                        String avatar_middlestr2 = user_infostrmap.get("avatar_middle").toString();
//                        String userGroup = user_infostrmap.get("user_group").toString();
//                        map2.put("isVip", userGroup);
//                        map2.put("uid", uidstr);
//                        map2.put("uname", unamestr);
//                        map2.put("avatar_middle", avatar_middlestr2);
//                    }
//                    listessenceData.add(map2);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            commonnoteAdapter.setlistData(listessenceData);
//            // 千万别忘了告诉控件刷新完毕了哦！
//            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//            dialog.dismissDlog();
//        }
//    }

    /**
     * 跳转到用户资料页面
     *
     * @param view
     */
    public void UserDataClick(View view) {
        Intent intent = new Intent(this, UserDataActivity.class);
        intent.putExtra("name", unamestr);
        intent.putExtra("uid", uidstr);
        intent.putExtra("intro", userintro1.getText().toString().trim());
        startActivity(intent);
    }

    /**
     * 问答页面的控件绑定
     */
    private void findViewQa() {
        fragmentQa = (LinearLayout) findViewById(R.id.fragment_qa);
        text_wd = (TextView) findViewById(R.id.text_wd);
        text_tw = (TextView) findViewById(R.id.text_tw);
        ll_wd = (LinearLayout) findViewById(R.id.linear_wd);
        ll_tw = (LinearLayout) findViewById(R.id.linear_tw);
        qaBt = (Button) findViewById(R.id.qa_bt);
        ll_wd.setOnClickListener(new MyOnClickListenserQa(0));
        ll_tw.setOnClickListener(new MyOnClickListenserQa(1));
        allQaList = (ListView) findViewById(R.id.all_qa);
        myQaList = (ListView) findViewById(R.id.my_qa);
        if (Constants.staticmyuidstr.equals(uidstr)) {
            text_tw.setText("用户提问");
            qaBt.setVisibility(View.GONE);
        } else {
            qaBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // menghuan 不用登陆也可以用
                    // 如果未登陆跳转到登陆页面
                    if (!Constants.isLogin){
                        Intent intent = new Intent(UserDetailNewActivity.this, loginActivity.class);
                        startActivity(intent);
                        return;
                    }
                    if (Utils.isFastDoubleClick3() || !isRn) {
                        return;
                    }
                    // 跳转到提问页面
                    Intent intent = new Intent(UserDetailNewActivity.this, QuestionAnswerActivity.class);
                    intent.putExtra("type", "1");
                    intent.putExtra("id", uidstr);
                    intent.putExtra("sjbCount", sjbCount);
                    startActivity(intent);
                }
            });
        }

        listDataMy = new ArrayList<QuestionAnswerEntity>();
        adapterMy = new QuizMyAdapter(this, uidstr);
        myQaList.setAdapter(adapterMy);

        listDataAll = new ArrayList<QuestionAnswerEntity>();
        adapterAll = new QuizAdapter(this, this);
        allQaList.setAdapter(adapterAll);
        initLineQa();
    }


    /**
     * 问答获取水晶币个数
     */
    private void getData() {
        // 获得设置的打赏水晶币个数
        new Thread(new Runnable() {
            @Override
            public void run() {
                resstrAsk = HttpUtil.restHttpGet(Constants.moUrl + "/ask/index/getPrice&uid=" + uidstr);
                handler.sendEmptyMessage(8);
            }
        }).start();
    }

    /**
     * 获取全部问答
     */
    private void getDataAll() {
        // 获取全部提问信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                resstrAll = HttpUtil.restHttpGet(Constants.moUrl + "/ask&uid=" + Constants.staticmyuidstr + "&token=" + Constants.apptoken + "&type=2&p=" + allPage + "&auid=" + uidstr);
                handler.sendEmptyMessage(9);
            }
        }).start();
    }

    /**
     * 获取我的问答
     */
    private void getDataMy() {
        // 获取全部我的问答
        new Thread(new Runnable() {
            @Override
            public void run() {
                resstrMy = HttpUtil.restHttpGet(Constants.moUrl + "/ask&uid=" + Constants.staticmyuidstr + "&token=" + Constants.apptoken + "&type=4&p=" + myPage + "&auid=" + uidstr);
                handler.sendEmptyMessage(10);
            }
        }).start();
    }

    /**
     * 初始化line
     */
    private void initLineQa() {
        img_line2 = (ImageView) findViewById(R.id.img_line2);
        mScreen1_2 = ImageUtil.getScreenWidth(this) / 2;
        ViewGroup.LayoutParams lp = img_line2.getLayoutParams();
        lp.width = mScreen1_2;
        img_line2.setLayoutParams(lp);
    }

    /**
     * 回掉付费的接口刷新页面
     *
     * @param id
     */
    public void Refresh(final String id) {
        dialog.showDialog();
        // 调用接口去购买
        new Thread(new Runnable() {
            @Override
            public void run() {
                List dataList = new ArrayList();
                dataList.add(new BasicNameValuePair("uid", Constants.staticmyuidstr));
                dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                dataList.add(new BasicNameValuePair("id", id));
                resstrAll = HttpUtil.restHttpPost(Constants.moUrl + "/ask/index/watch", dataList);
                handler.sendEmptyMessage(11);
            }
        }).start();
    }

    /**
     * 点击文字进行切换
     *
     * @author wuxl
     */
    public class MyOnClickListenserQa implements View.OnClickListener {

        private int index = 0;

        public MyOnClickListenserQa(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.linear_wd:
                    text_tw.setTextColor(unselect_color);
                    text_wd.setTextColor(select_color);
                    allQaList.setVisibility(View.VISIBLE);
                    myQaList.setVisibility(View.GONE);
                    break;
                case R.id.linear_tw:
                    text_wd.setTextColor(unselect_color);
                    text_tw.setTextColor(select_color);
                    allQaList.setVisibility(View.GONE);
                    myQaList.setVisibility(View.VISIBLE);
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
     * 跳转到自选股页面
     *
     * @param view
     */
    public void MyStockClick(View view) {
        Intent intent = new Intent(this, MyStocksActivity.class);
        intent.putExtra("uid", uidstr);
        startActivity(intent);
    }
}
