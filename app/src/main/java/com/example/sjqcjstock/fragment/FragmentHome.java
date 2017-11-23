package com.example.sjqcjstock.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.Activity.Article.ArticleDetailsActivity;
import com.example.sjqcjstock.Activity.Article.ReferenceActivity;
import com.example.sjqcjstock.Activity.CattleListActivity;
import com.example.sjqcjstock.Activity.EssenceListActivty;
import com.example.sjqcjstock.Activity.MainActivity;
import com.example.sjqcjstock.Activity.RealTimeRecordActivity;
import com.example.sjqcjstock.Activity.Tomlive.DirectBroadcastingRoomActivity;
import com.example.sjqcjstock.Activity.Tomlive.TomlivePersonnelListActivity;
import com.example.sjqcjstock.Activity.advertUrlActivity;
import com.example.sjqcjstock.Activity.firm.FirmHallActivity;
import com.example.sjqcjstock.Activity.gwzhmatchActivity;
import com.example.sjqcjstock.Activity.plan.PlanExhibitionActivity;
import com.example.sjqcjstock.Activity.plan.PlanHallActivity;
import com.example.sjqcjstock.Activity.ranking.RankingListWebActivity;
import com.example.sjqcjstock.Activity.selectstockmatchActivity;
import com.example.sjqcjstock.Activity.stocks.MoreDynamicExpertRecommendActivity;
import com.example.sjqcjstock.Activity.stocks.SharesDetailedActivity;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.Activity.supermanlistActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.essenceAdapter;
import com.example.sjqcjstock.adapter.plan.PlanAdapter;
import com.example.sjqcjstock.adapter.stocks.DynamicExpertAdapter;
import com.example.sjqcjstock.adapter.supermanActivityAdapter;
import com.example.sjqcjstock.adapter.tomlive.TomliveListAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.ACache;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.ADInfo;
import com.example.sjqcjstock.entity.Article.RaceReportEntity;
import com.example.sjqcjstock.entity.Article.UserEntity;
import com.example.sjqcjstock.entity.NoticeEntity;
import com.example.sjqcjstock.entity.Tomlive.TomliveRoomEntity;
import com.example.sjqcjstock.entity.plan.PlanEntity;
import com.example.sjqcjstock.entity.stocks.GeniusEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.userdefined.MyScrollView;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CycleViewPager;
import com.example.sjqcjstock.view.MarqueeView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 2017-5-2 新追加投资计划列
 * 2017-5-4 新追加跑马灯 MarqueeView
 */
public class FragmentHome extends Fragment {

    // 获取控件
    private LinearLayout pickessence;
    //    private LinearLayout pickhotstock;
    private LinearLayout picksuperman;
    private LinearLayout pickgwzbmatch1;
    //    private RelativeLayout rl_refresh_layout;
    private LinearLayout pickselectstockmatch1;
    private LinearLayout picktosesencelist1;
    private LinearLayout picktosupermanlist1;
    private LinearLayout tomliveLl;
    private LinearLayout planLl;
    private MyScrollView myScrollView;
    // 定义List集合容器
    private essenceAdapter essencelistAdapter;
    private supermanActivityAdapter supermanAdapter;
    private DynamicExpertAdapter dynamicExpertAdapter;
    private ArrayList<RaceReportEntity> listessenceData;
    // 需要加载的牛人动态数据
    private ArrayList<GeniusEntity> geniusList;
    // 精华集合
    private ListView essencelistview;
    // 牛人集合
    private ListView supermanlistview;
    // 牛人操作精选列表
    private ListView expertOperationlist;
    // 直播房间集合
    private ListView tomliveList;
    // 投资计划3个集合
    private ListView planList;
    // 广告滚动的加载
    private CycleViewPager cycleViewPager;
    // 跑马灯
    private MarqueeView marqueeView;
    // 获取广告的接口的类
    private List<ADInfo> infos = new ArrayList<ADInfo>();
    // 图片的加载
    private List<ImageView> views = new ArrayList<ImageView>();
    // 网络请求提示
    private CustomProgress dialog;
    // 缓存用的类
    private ACache mCache;
    // 缓存滚动的图片用
    private String globalSwfACache = "";
    private String globalSwfStr = "";
    private MainActivity mainActivity;
    // 牛人动态返回的数据
    private String resstr;
    private String resAcache;
    // 直播房间返回的数据
    private String liveResstr;
    private String liveResACache;
    // 直播显示加载的Adapter
    private TomliveListAdapter tomliveAdapter;
    // 直播需要加载的数据集合
    private ArrayList<TomliveRoomEntity> tomliveListData;
    // 显示投资计划的Adapter
    private PlanAdapter planAdapter;
    // 投资计划集合
    private ArrayList<PlanEntity> planListData;
    // 投资计划返回的数据
    private String planResstr;
    private String planResACache;
    // 版本更新返回数据
    private String versionStr;
    // 跑马灯实时战绩广告
    private List<NoticeEntity> noticeEntitys;
    // 认证牛人
    private ArrayList<UserEntity> userEntityList;

    // 用户信息数据
    private String userStr;
    // 实时战绩数据
    private String noticeStr;
    // 订单返回数据
    private String orderStr;
    // 水晶币返回数据
    private String jsonStr;
    // 焦点返回数据
    private String focusStr="";
    // 焦点缓存用
    private String focusACache;
    // 认证牛人返回数据
    private String authStr;
    // 认证牛人缓存用
    private String authACache;


    public FragmentHome() {
    }

    public FragmentHome(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        // 缓存类
        mCache = ACache.get(getActivity());
        initView(view);
        selectRecharge();
        // 滚动到顶部
        myScrollView.smoothScrollTo(0, 0);
        // 获取判断版本信息
        VersionUpdate();
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (dialog != null) {
            dialog.dismissDlog();
        }
    }

    private void initView(View view) {
        // 图片滚动广告
        if (cycleViewPager == null) {
            cycleViewPager = new CycleViewPager();
            this.getActivity().getFragmentManager().beginTransaction().replace(R.id.fragment_picture, cycleViewPager).commit();
        }
        // 跑马灯
        marqueeView = (MarqueeView) view.findViewById(R.id.marqueeView);
        noticeEntitys = new ArrayList<NoticeEntity>();
        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                NoticeEntity noticeEntity = noticeEntitys.get(position);
                String type = noticeEntity.getType();
                // 0 链接 1微博 2 个人主页 3 交易 4 直播 5 投资计划
                String uid = noticeEntity.getParam().getUid();
                Intent intent = null;
                if ("1".equals(type)) {
                    // 文章打开
                    intent = new Intent(getActivity(), ArticleDetailsActivity.class);
                    // 文章的ID
                    intent.putExtra("weibo_id", noticeEntity.getParam().getFeed_id());
                } else if ("2".equals(type)) {
                    // 个人主页 微博
                    intent = new Intent(getActivity(), UserDetailNewActivity.class);
                    intent.putExtra("uid", uid);
                } else if ("3".equals(type)) {
                    // 个人主页 交易
                    intent = new Intent(getActivity(), UserDetailNewActivity.class);
                    intent.putExtra("uid", uid);
                    intent.putExtra("type", "1");
                } else if ("4".equals(type)) {
                    //  直播
                    intent = new Intent(getActivity(), DirectBroadcastingRoomActivity.class);
                    // 房间ID
                    intent.putExtra("roomId", noticeEntity.getParam().getRoom_id());
                    // 用户ID
                    intent.putExtra("uid", uid);
                } else if ("5".equals(type)) {
                    //  投资计划
                    intent = new Intent(getActivity(), PlanExhibitionActivity.class);
                    // 计划id
                    intent.putExtra("id", noticeEntity.getParam().getDp_id());
                    // 用户ID
                    intent.putExtra("uid", uid);
                } else {
                    String url = noticeEntity.getUrl();
                    if (Utils.isWebsite(url)) {
                        Uri uri = Uri.parse(url);
                        intent = new Intent(Intent.ACTION_VIEW, uri);
                    }
                }
                startActivity(intent);
            }
        });

        dialog = new CustomProgress(getActivity());
        // dialog.setTitle("提示信息");
        dialog.showDialog();
        // 加载控件
//        rl_refresh_layout = (RelativeLayout) view.findViewById(R.id.rl_refresh_layout);
        pickessence = (LinearLayout) view.findViewById(R.id.pickessence);
//        pickhotstock = (LinearLayout) view.findViewById(R.id.pickhotstock);
        picksuperman = (LinearLayout) view.findViewById(R.id.picksuperman);
        pickgwzbmatch1 = (LinearLayout) view.findViewById(R.id.pickgwzbmatch1);
        pickselectstockmatch1 = (LinearLayout) view.findViewById(R.id.pickselectstockmatch1);
        picktosesencelist1 = (LinearLayout) view.findViewById(R.id.picktosesencelist1);
        picktosupermanlist1 = (LinearLayout) view.findViewById(R.id.picktosupermanlist1);
        tomliveLl = (LinearLayout) view.findViewById(R.id.tomlive_ll);
        planLl = (LinearLayout) view.findViewById(R.id.plan_ll);
        myScrollView = (MyScrollView) view.findViewById(R.id.myScrollView);

        pickessence.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 精华
                Intent intent = new Intent(getActivity(), EssenceListActivty.class);
                startActivity(intent);
            }
        });
        picksuperman.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 认证牛人列表
                Intent intent = new Intent(getActivity(),
                        supermanlistActivity.class);
                startActivity(intent);
            }
        });
        pickgwzbmatch1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 大师联赛
                Intent intent = new Intent(getActivity(), gwzhmatchActivity.class);
                startActivity(intent);
            }
        });
        pickselectstockmatch1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 定位到选股比赛页面
                Intent intent = new Intent(getActivity(),
                        selectstockmatchActivity.class);
                startActivity(intent);
            }
        });
        picktosesencelist1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 定位到交易页面
                mainActivity.clickStocks();
            }
        });
        picktosupermanlist1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        ReferenceActivity.class);
                intent.putExtra("type", "hotpay");
                startActivity(intent);
            }
        });
        tomliveLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到直播列表
                Intent intent = new Intent(getActivity(),
                        TomlivePersonnelListActivity.class);
                startActivity(intent);
            }
        });
        planLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到投资计划显示列表
                Intent intent = new Intent(getActivity(),
                        PlanHallActivity.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.plan_list_ll).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到投资计划显示列表
                Intent intent = new Intent(getActivity(),
                        PlanHallActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.tomlive_gc_ll).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 直播广场
                Intent intent = new Intent(getActivity(),
                        TomlivePersonnelListActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.question_answer_ll).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FirmHallActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.cattle_list_ll).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到各个牛人榜单
//                Intent intent = new Intent(getActivity(), CattleListActivity.class);
//                startActivity(intent);
                Intent intent = new Intent(getActivity(), RankingListWebActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.real_time_record_ll).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到实时战绩页面
                Intent intent = new Intent(getActivity(), RealTimeRecordActivity.class);
                startActivity(intent);
            }
        });

        // 牛人精选的更多
        view.findViewById(R.id.expert_selection).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 牛人精选动态
                Intent intent = new Intent(getActivity(),
                        MoreDynamicExpertRecommendActivity.class);
                startActivity(intent);
            }
        });

        /** 精华集合 */
        essencelistview = (ListView) view.findViewById(R.id.essencelist);
        // 为ListView 添加适配器
        essencelistAdapter = new essenceAdapter(getActivity());
        essencelistview.setAdapter(essencelistAdapter);
        essencelistview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                try {
                    Intent intent = new Intent(getActivity(), ArticleDetailsActivity.class);
                    intent.putExtra(
                            "weibo_id", listessenceData.get(position).getFeed_id());
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /** 牛人集合 */
        supermanlistview = (ListView) view.findViewById(R.id.supermanlist);
        // 为ListView 添加适配器
        supermanAdapter = new supermanActivityAdapter(getActivity());

        supermanlistview.setAdapter(supermanAdapter);

        supermanlistview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                try {
                    Intent intent = new Intent(getActivity(), UserDetailNewActivity.class);
                    intent.putExtra("uid", userEntityList.get(position).getUid());
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        // 牛人操作精选
        expertOperationlist = (ListView) view
                .findViewById(R.id.expertOperationlist);
        // 为ListView 添加适配器
        dynamicExpertAdapter = new DynamicExpertAdapter(getActivity());
        expertOperationlist.setAdapter(dynamicExpertAdapter);
        expertOperationlist.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent inten = new Intent();
                inten.putExtra("code", geniusList.get(position).getStock());
                inten.putExtra("name", geniusList.get(position).getStock_name());
                inten.setClass(getActivity(), SharesDetailedActivity.class);
                startActivity(inten);
            }
        });

        /** 直播房间的集合 */
        tomliveList = (ListView) view
                .findViewById(R.id.tomlive_list);
        // 为ListView 添加适配器
        tomliveAdapter = new TomliveListAdapter(getActivity(), "");
        tomliveList.setAdapter(tomliveAdapter);
        tomliveList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), DirectBroadcastingRoomActivity.class);
                intent.putExtra("roomId", tomliveListData.get(position).getId());
                intent.putExtra("uid", tomliveListData.get(position).getUid());
                intent.putExtra("name", tomliveListData.get(position).getUsername());
                intent.putExtra("remark", tomliveListData.get(position).getIntro());
                intent.putExtra("type", tomliveListData.get(position).getType());
                startActivity(intent);
            }
        });

        /** 投资计划的集合 */
        planList = (ListView) view
                .findViewById(R.id.plan_list);
        // 为ListView 添加适配器
        planAdapter = new PlanAdapter(getActivity());
        planList.setAdapter(planAdapter);
        planList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PlanExhibitionActivity.class);
                intent.putExtra("id", planListData.get(position).getId());
                intent.putExtra("uid", planListData.get(position).getUid());
                getActivity().startActivity(intent);
            }
        });
        // 加载缓存
        loadCacheData();
        loadData();

        // menghuan 不登陆也可以用
        // 当用户登陆了才能点击
        if (Constants.isLogin) {
            // 开线程获取水晶币数量
            new Thread(new Runnable() {
                @Override
                public void run() {
                    jsonStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/credit/info?mid=" + Constants.staticmyuidstr + "&token=" + Constants.apptoken);
                    handler.sendEmptyMessage(7);
                }
            }).start();

            // 从网络获取用户详细信息
            new Thread(new Runnable() {
                @Override
                public void run() {
                    userStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/user/info?mid=" + Constants.staticmyuidstr);
                    handler.sendEmptyMessage(4);
                }
            }).start();
        }

        // 获取轮播图
        new Thread(new Runnable() {
            @Override
            public void run() {
                globalSwfStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/ad/swf?1=1");
                handler.sendEmptyMessage(10);
            }
        }).start();

        // 实时战绩列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                noticeStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/news/gains?p=1");
                handler.sendEmptyMessage(5);
            }
        }).start();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            loadData();
        } else {
            if (focusACache != null && !"".equals(focusACache)) {
                // 做缓存
                mCache.put("Appindexx", focusACache);
            }
            if (authACache != null && !"".equals(authACache)) {
                // 做缓存
                mCache.put("APPUserSortx", authACache);
            }
            if (globalSwfACache != null && !"".equals(globalSwfACache)) {
                // 缓存滚动图片消息
                mCache.put("globalSwf", globalSwfACache);
            }
            // 缓存牛人动态
            if (resAcache != null && !resAcache.equals("")) {
                mCache.put("resstr", resAcache);
            }
            // 缓存直播
            if (liveResACache != null && !liveResACache.equals("")) {
                mCache.put("liveResstr", liveResACache);
            }
            // 缓存投资计划
            if (planResACache != null && !planResACache.equals("")) {
                mCache.put("planResstr", planResACache);
            }
        }
    }

    /**
     * 加载缓存数据
     */
    private void loadCacheData() {
        // 滚动图片
        globalSwfACache = mCache.getAsString("globalSwf");
        if (globalSwfACache != null && !"".equals(globalSwfACache)){
            getData10(globalSwfACache);
        }
        // 牛人动态数据
        resAcache = mCache.getAsString("resstr");
        if (resAcache != null && !"".equals(resAcache)){
            getData0(resAcache);
        }
        // 直播数据
        liveResACache = mCache.getAsString("liveResstr");
        if (liveResACache != null && !"".equals(liveResACache)){
            getData1(liveResACache);
        }
        // 投资计划精选
        planResACache = mCache.getAsString("planResstr");
        if (planResACache != null && !"".equals(planResACache)){
            getData3(planResACache);
        }
        // 精华推荐
        focusACache = mCache.getAsString("Appindexx");
        if (focusACache != null && !"".equals(focusACache)){
            getData8(focusACache);
        }
        // 认证牛人
        authACache = mCache.getAsString("APPUserSortx");
        if (authACache != null && !"".equals(authACache)){
            getData9(authACache);
        }
        // 实时战绩列表
        String noticeACache = mCache.getAsString("notice");
        if (noticeACache != null && !"".equals(noticeACache)){
            getData5(noticeACache);
        }

    }

    /**
     * 加载焦点，牛人动态，直播列表的数据
     */
    private void loadData() {
        // 获取焦点数据列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                focusStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/feed/recommend?position=1&p=1&limit=3");
                handler.sendEmptyMessage(8);
            }
        }).start();

        // 开线程获牛人动态数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                resstr = HttpUtil.restHttpGet(Constants.moUrl + "/orders&token=" + Constants.apptoken + "&uid=" + Constants.staticmyuidstr + "&recommend=1&p=1&limit=3&mod=index");
                handler.sendEmptyMessage(0);
            }
        }).start();

        // 直播间列表
        // 获取房间信息列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用接口获取股票当前行情数据
                liveResstr = HttpUtil.restHttpGet(Constants.moUrl + "/live/room" + "&p=1&limit=3");
                handler.sendEmptyMessage(1);
            }
        }).start();

        // 获取投资计划精选
        new Thread(new Runnable() {
            @Override
            public void run() {
                planResstr = HttpUtil.restHttpGet(Constants.moUrl + "/recommend/dealplan?p=1&limit=3");
                handler.sendEmptyMessage(3);
            }
        }).start();

        // 获取认证牛人的数据列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                authStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/user/auth?mid="+Constants.staticmyuidstr+"&p=1&limit=3");
                handler.sendEmptyMessage(9);
            }
        }).start();
    }

    /**
     * 检查版更新
     */
    private void VersionUpdate() {
        // 版本更新
        new Thread(new Runnable() {
            @Override
            public void run() {
                versionStr = HttpUtil.restHttpGet("https://www.sjqcj.com/version.json?ver=1");
                handler.sendEmptyMessage(2);
            }
        }).start();
    }

    /**
     * 广告图片的一些加载适配
     */
    private void setImageLoader() {
        try {
            views.clear();
            // 将最后一个ImageView添加进来
            views.add(ImageUtil.getImageView(this.getActivity(), infos.get(infos.size() - 1).getPic()));
            for (int i = 0; i < infos.size(); i++) {
                views.add(ImageUtil.getImageView(this.getActivity(), infos.get(i).getPic()));
            }
            // 将第一个ImageView添加进来
            views.add(ImageUtil.getImageView(this.getActivity(), infos.get(0).getPic()));
            // 设置循环，在调用setData方法前调用
            cycleViewPager.setCycle(true);
            // 在加载数据前设置是否循环
            cycleViewPager.setData(views, infos, mAdCycleViewListener);
            // 设置轮播
            cycleViewPager.setWheel(true);
            // 设置轮播时间，默认5000ms
            cycleViewPager.setTime(2000);
            // 设置圆点指示图标组居中显示，默认靠右
            cycleViewPager.setIndicatorCenter();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * 点击广告图片的单机事件
     */
    private CycleViewPager.ImageCycleViewListener mAdCycleViewListener = new CycleViewPager.ImageCycleViewListener() {
        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {
//            1.精华推荐 2.牛人交易推荐 3.牛人直播列表 4.个人直播房间 5.社区（文章） 6.社区(内参) 7.个人主页(微博) 8.个人主页(内参) 9.个人主页(交易)  10.详细文章
            if (cycleViewPager.isCycle()) {
                Intent intent = null;
                switch (info.getType()) {
                    case 0:
                        // 跳转到广告页面
                        intent = new Intent(getActivity(), advertUrlActivity.class);
                        // 图片路径
                        intent.putExtra("url", info.getUrl());
                        intent.putExtra("title", info.getTitle());
                        break;
                    case 1:
                        // 1.精华推荐
                        intent = new Intent(getActivity(), EssenceListActivty.class);
                        break;
                    case 2:
                        // 2.牛人交易推荐
                        intent = new Intent(getActivity(),
                                MoreDynamicExpertRecommendActivity.class);
                        break;
                    case 3:
                        // 3.牛人直播列表
                        intent = new Intent(getActivity(),
                                TomlivePersonnelListActivity.class);
                        break;
                    case 4:
                        // 4.个人直播房间
                        intent = new Intent(getActivity(), DirectBroadcastingRoomActivity.class);
                        // 房间ID
                        intent.putExtra("roomId", info.getRoom_id());
                        // 用户ID
                        intent.putExtra("uid", info.getUid());
                        break;
                    case 5:
                        // 5.个人主页(微博)
                        intent = new Intent(getActivity(), UserDetailNewActivity.class);
                        // 用户类型
                        intent.putExtra("uid", info.getUid());
                        // 显示的page
                        intent.putExtra("type", "0");
                        break;
                    case 6:
                        // 6.个人主页(内参)
                        intent = new Intent(getActivity(), UserDetailNewActivity.class);
                        // 用户id
                        intent.putExtra("uid", info.getUid());
                        // 显示的page
                        intent.putExtra("type", "2");
                        break;
                    case 7:
                        // 7.个人主页(交易)
                        intent = new Intent(getActivity(), UserDetailNewActivity.class);
                        // 用户id
                        intent.putExtra("uid", info.getUid());
                        // 显示的page
                        intent.putExtra("type", "1");
                        break;
                    case 8:
                        // 8.详细文章
                        intent = new Intent(getActivity(), ArticleDetailsActivity.class);
                        // 文章的ID
                        intent.putExtra("weibo_id", info.getFeed_id());
                        break;
                }
                if (intent != null) {
                    startActivity(intent);
                }
            }
        }
    };

    /**
     * 查询充值订单（看订单是否进行过处理）
     */
    private void selectRecharge() {
        String orderType = getActivity().getSharedPreferences("Recharge", getActivity().MODE_PRIVATE).getString("order_type", "");
        if (orderType != null && orderType.equals("1")) {
            // 重新调用后台查询订单增加水晶币
            // 订单号
            final String outTradeNo = getActivity().getSharedPreferences("Recharge", getActivity().MODE_PRIVATE).getString("out_trade_no", "");
//            new SelectOrder()
//                    .execute(new TaskParams(Constants.queryOrder + "&uid=" + Constants.staticmyuidstr + "&out_trade_no=" + outTradeNo));

            new Thread(new Runnable() {
                @Override
                public void run() {
                    List dataList = new ArrayList();
                    // 微博id
                    dataList.add(new BasicNameValuePair("mid", Constants.staticmyuidstr));
                    dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                    dataList.add(new BasicNameValuePair("out_trade_no", outTradeNo));
                    orderStr = HttpUtil.restHttpPost(Constants.newUrl + "/api/pay/wxQuery", dataList);
                    handler.sendEmptyMessage(6);
                }
            }).start();

        }
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
                    getData0(resstr);
//                    try {
//                        JSONObject jsonObject = new JSONObject(resstr);
//                        if ("failed".equals(jsonObject.getString("status"))) {
//                            dialog.dismissDlog();
//                            resAcache = mCache.getAsString("resstr");
//                            if (resAcache != null && !resAcache.equals("")) {
//                                jsonObject = new JSONObject(resAcache);
//                            }else{
//                                return;
//                            }
//                        }else{
//                            resAcache = resstr;
//                        }
//                        geniusList = new ArrayList<GeniusEntity>();
//                        ArrayList<GeniusEntity> geniusLists = (ArrayList<GeniusEntity>) JSON.parseArray(jsonObject.getString("data"), GeniusEntity.class);
//                        geniusList.addAll(geniusLists);
//                        dynamicExpertAdapter.setlistData(geniusList);
//                        Utils.setListViewHeightBasedOnChildren(expertOperationlist);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    dialog.dismissDlog();
                    break;
                case 1:
                    getData1(liveResstr);
//                    try {
//                        JSONObject jsonObject = new JSONObject(liveResstr);
//                        if ("failed".equals(jsonObject.getString("status"))) {
//                            liveResACache = mCache.getAsString("liveResstr");
//                            Log.e("mhhc",liveResACache+"*-----");
//                            if (liveResACache != null && !liveResACache.equals("")) {
//                                jsonObject = new JSONObject(liveResACache);
//                            }else{
//                                return;
//                            }
//                        }else{
//                            liveResACache = liveResstr;
//                        }
//                        tomliveListData = (ArrayList<TomliveRoomEntity>) JSON.parseArray(jsonObject.getString("data"), TomliveRoomEntity.class);
//                        tomliveAdapter.setlistData(tomliveListData);
//                        Utils.setListViewHeightBasedOnChildren(tomliveList);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    break;
                case 2:
                    try {
                        if (versionStr == null || versionStr.equals("")) {
                            return;
                        }
                        JSONObject jsonObject = new JSONObject(versionStr);
                        String textStr = jsonObject.getString("android");
                        VersionUpdate(textStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    getData3(planResstr);
//                    try {
//                        JSONObject jsonObject = new JSONObject(planResstr);
//                        if ("failed".equals(jsonObject.getString("status"))) {
//                            planResACache = mCache.getAsString("planResstr");
//                            if (planResACache != null && !planResACache.equals("")) {
//                                jsonObject = new JSONObject(planResACache);
//                            }else{
//                                return;
//                            }
//                        }else{
//                            planResACache = planResstr;
//                        }
//                        planListData = (ArrayList<PlanEntity>) JSON.parseArray(jsonObject.getString("data"), PlanEntity.class);
//                        planAdapter.setlistData(planListData);
//                        Utils.setListViewHeightBasedOnChildren(planList);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    break;
                case 4:
                    // 博主的相关信息
                    try {
                        JSONObject jsonObject = new JSONObject(userStr);
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            Constants.userEntity = new UserEntity();
                            return;
                        }
                        UserEntity userEntity = JSON.parseObject(jsonObject.getString("data"), UserEntity.class);
                        Constants.userEntity = userEntity;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    getData5(noticeStr);
//                    try {
//                        JSONObject jsonObject = new JSONObject(noticeStr);
//                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
//                            // 先获取缓存
//                            noticeStr = mCache.getAsString("notice");
//                            if(noticeStr !=null &&!"".equals(noticeStr)){
//                                jsonObject = new JSONObject(noticeStr);
//                            }else{
//                                return;
//                            }
//                        }else{
//                            // 做缓存
//                            mCache.put("notice", noticeStr);
//                        }
//                        List<NoticeEntity> noticeEntityContends = JSON.parseArray(jsonObject.getString("contend"), NoticeEntity.class);
//                        List<NoticeEntity> noticeEntityNews = JSON.parseArray(jsonObject.getString("news"), NoticeEntity.class);
//                        noticeEntitys.addAll(noticeEntityContends);
//                        noticeEntitys.addAll(noticeEntityNews);
//                        marqueeView.startWithList(noticeEntitys);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    break;
                case 6:
                    // 微信支付订单查询
                    try {
                        JSONObject jsonObject = new JSONObject(orderStr);
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            return;
                        }
                        Constants.shuijinbiCount = jsonObject.getString("shuijingbi");
                        // 保存订单状态订单号(用于控制订单的二次处理)
                        SharedPreferences.Editor editorIsLogin = getActivity().getSharedPreferences("Recharge", getActivity().MODE_PRIVATE).edit();
                        // 订单状态1为未处理0为处理
                        editorIsLogin.putString("order_type", "0");
                        editorIsLogin.commit();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 7:
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        if (Constants.successCode.equals(jsonObject.getString("code"))) {
                            JSONObject jsonData = new JSONObject(jsonObject.getString("data"));
                            String shuijingbi = jsonData.getString("shuijingbi");
                            Constants.shuijinbiCount = shuijingbi;
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 8:
                    getData8(focusStr);
//                    try {
//                        Log.e("mhhc",focusStr+"-");
//                        JSONObject jsonObject = new JSONObject(focusStr);
//                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
//                            // 先获取缓存
//                            focusACache = mCache.getAsString("Appindexx");
//                            if (focusACache != null && !"".equals(focusACache)){
//                                jsonObject = new JSONObject(focusACache);
//                                focusACache = "";
//                            }else{
//                                return;
//                            }
//                        }else{
//                            focusACache = focusStr;
//                        }
//                        listessenceData = (ArrayList<RaceReportEntity>) JSON.parseArray(jsonObject.getString("data"), RaceReportEntity.class);
//                        essencelistAdapter.setlistData(listessenceData);
//                        Utils.setListViewHeightBasedOnChildren(essencelistview);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    break;
                case 9:
                    getData9(authStr);
//                    try {
//                        JSONObject jsonObject = new JSONObject(authStr);
//                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
//                            // 先获取缓存
//                            authACache = mCache.getAsString("APPUserSortx");
//                            if (authACache != null && !"".equals(authACache)){
//                                jsonObject = new JSONObject(authACache);
//                                authACache = "";
//                            }else{
//                                return;
//                            }
//                        }else{
//                            authACache = authStr;
//                        }
//                        userEntityList = (ArrayList<UserEntity>) JSON.parseArray(jsonObject.getString("data"),UserEntity.class);
//                        supermanAdapter.setlistData(userEntityList);
//                        Utils.setListViewHeightBasedOnChildren(supermanlistview);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    break;
                case 10:
                    getData10(globalSwfStr);
//                    try {
//                        Log.e("mhhc",globalSwfStr+"1-");
//                        JSONObject jsonObject = new JSONObject(globalSwfStr);
//                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
//                            // 先获取缓存
//                            globalSwfACache = mCache.getAsString("globalSwf");
//                            if (globalSwfACache != null && !"".equals(globalSwfACache)){
//                                jsonObject = new JSONObject(globalSwfACache);
//                                globalSwfACache = "";
//                            }else{
//                                return;
//                            }
//                        }else{
//                            globalSwfACache = globalSwfStr;
//                        }
//                        infos = JSON.parseArray(jsonObject.getString("data"),ADInfo.class);
//                        if (infos.size() > 0) {
//                            // 加载滚动图片
//                            setImageLoader();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    break;
            }
        }
    };

    /**
     * 加载线程为0的数据
     * @param data
     */
    private void getData0(String data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            if ("failed".equals(jsonObject.getString("status"))) {
                return;
            }
            resAcache = resstr;
            geniusList = new ArrayList<GeniusEntity>();
            ArrayList<GeniusEntity> geniusLists = (ArrayList<GeniusEntity>) JSON.parseArray(jsonObject.getString("data"), GeniusEntity.class);
            geniusList.addAll(geniusLists);
            dynamicExpertAdapter.setlistData(geniusList);
            Utils.setListViewHeightBasedOnChildren(expertOperationlist);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载线程为1的数据
     * @param data
     */
    private void getData1(String data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            if ("failed".equals(jsonObject.getString("status"))) {
                return;
            }
            liveResACache = liveResstr;
            tomliveListData = (ArrayList<TomliveRoomEntity>) JSON.parseArray(jsonObject.getString("data"), TomliveRoomEntity.class);
            tomliveAdapter.setlistData(tomliveListData);
            Utils.setListViewHeightBasedOnChildren(tomliveList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载线程为3的数据
     * @param data
     */
    private void getData3(String data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            if ("failed".equals(jsonObject.getString("status"))) {
                return;
            }
            planResACache = planResstr;
            planListData = (ArrayList<PlanEntity>) JSON.parseArray(jsonObject.getString("data"), PlanEntity.class);
            planAdapter.setlistData(planListData);
            Utils.setListViewHeightBasedOnChildren(planList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载线程为5的数据
     * @param data
     */
    private void getData5(String data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                return;
            }
            // 做缓存
            mCache.put("notice", data);
            List<NoticeEntity> noticeEntityContends = JSON.parseArray(jsonObject.getString("contend"), NoticeEntity.class);
            List<NoticeEntity> noticeEntityNews = JSON.parseArray(jsonObject.getString("news"), NoticeEntity.class);
            noticeEntitys.addAll(noticeEntityContends);
            noticeEntitys.addAll(noticeEntityNews);
            marqueeView.startWithList(noticeEntitys);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载线程为8的数据
     * @param data
     */
    private void getData8(String data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (!Constants.successCode.equals(jsonObject.getString("code"))) {
             return;
            }
            focusACache = data;
            listessenceData = (ArrayList<RaceReportEntity>) JSON.parseArray(jsonObject.getString("data"), RaceReportEntity.class);
            essencelistAdapter.setlistData(listessenceData);
            Utils.setListViewHeightBasedOnChildren(essencelistview);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载线程为9的数据
     * @param data
     */
    private void getData9(String data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (!Constants.successCode.equals(jsonObject.getString("code"))) {
             return;
            }
            authACache = data;
            userEntityList = (ArrayList<UserEntity>) JSON.parseArray(jsonObject.getString("data"),UserEntity.class);
            supermanAdapter.setlistData(userEntityList);
            Utils.setListViewHeightBasedOnChildren(supermanlistview);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载线程为10的数据
     * @param data
     */
    private void getData10(String data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                return;
            }
            globalSwfACache = data;
            infos = JSON.parseArray(jsonObject.getString("data"),ADInfo.class);
            if (infos.size() > 0) {
                // 加载滚动图片
                setImageLoader();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断版本是否更新
     *
     * @param strJson （调用接口获取的版本信息）
     */
    private void VersionUpdate(String strJson) {
        // 当前版本号
        int versionCodeOld = Utils.getVersionCode(getActivity());
        // 最新的版本号
        final int versionCodeNew;
        // 版本名称
        String versionName;
        // 版本类型 1 强制更新 2.可以忽略
        String versionType;
        // 版本下载地址
        final String versionUrl;
        // 版本说明
        String versionExplain;
        try {
            JSONObject jsonObject = new JSONObject(strJson);
            versionCodeNew = jsonObject.getInt("version");
            versionType = jsonObject.getString("type");
//            versionCodeNew = 16;
//            versionType = "1";
            versionName = jsonObject.getString("name");
            versionUrl = jsonObject.getString("url");
            versionExplain = jsonObject.getString("notice");
            // 保存最新版本信息
            String versionStr = versionCodeNew + "|" + versionName + "|" + versionUrl;
            // 缓存版本信息
            mCache.put("versionStr", versionStr);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        // 最新版本小于等于当前版本不进行更新
        if (versionCodeNew <= versionCodeOld) {
            return;
        }
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_update, null);
        ((TextView) view.findViewById(R.id.version_explain)).setText(versionExplain);
//        ((TextView)view.findViewById(R.id.version_name)).setText(versionName);
        Button btNo = (Button) view.findViewById(R.id.bt_no);

        if ("2".equals(versionType)) {
            // 获取缓存里的版本号
            String versionCode = mCache.getAsString("versionCode");
            // 如果版本好不相等就代表是没有忽略的
            if (versionCode == null || versionCodeNew != Integer.valueOf(versionCode)) {
                btNo.setVisibility(View.VISIBLE);
            } else {

                return;
            }
        }else{
            // versionType = 1 强制更新
            btNo.setText("取消");
        }
        final String type = versionType;
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setView(view).show();
        // 获取屏幕宽度
        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        int width = (int) (size.x * 0.7);
        int height = (int) (size.y * 0.3);
        //设置窗口的大小
        alertDialog.getWindow().setLayout(width, height);
        alertDialog.setCancelable(false);
        ImageView dialogDismiss = (ImageView) view.findViewById(R.id.iv_dialogDismiss);
        dialogDismiss.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if ("1".equals(type)) {
                    ExitApplication.getInstance().exit();
                }
            }
        });
        view.findViewById(R.id.bt_ok).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开网页下载最新版本
                // 先判断是否是网址
                if (Utils.isWebsite(versionUrl)) {
                    Uri uri = Uri.parse(versionUrl);
                    // 调用手机浏览器打开网址进行下载
                    getActivity().startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    alertDialog.dismiss();
                    if ("1".equals(type)) {
                        ExitApplication.getInstance().exit();
                    }
                }
            }
        });
        btNo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("2".equals(type)) {
                    // 版本号做缓存
                    mCache.put("versionCode", versionCodeNew + "");
                    // 忽略这个版本（把版本信息存在手机里判断的时候看看是否忽略此版本  这里没写 需要再实现  现在是每次进入app 都进行提示）
                    alertDialog.dismiss();
                }else{
                    ExitApplication.getInstance().exit();
                }
            }
        });
    }
}
