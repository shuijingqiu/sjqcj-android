package com.example.sjqcjstock.fragment.stocks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.Activity.SearchActivity;
import com.example.sjqcjstock.Activity.advertUrlActivity;
import com.example.sjqcjstock.Activity.stocks.ExpertListsActivity;
import com.example.sjqcjstock.Activity.stocks.MyDealAccountActivity;
import com.example.sjqcjstock.Activity.stocks.SharesDetailedActivity;
import com.example.sjqcjstock.Activity.stocks.SimulationGameActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.stocks.DynamicExpertAdapter;
import com.example.sjqcjstock.constant.ACache;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.ADInfo;
import com.example.sjqcjstock.entity.stocks.GeniusEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.example.sjqcjstock.userdefined.MyScrollView;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.CycleViewPager;
import com.example.sjqcjstock.view.PullToRefreshLayout;
import com.example.sjqcjstock.view.PullableScrollView;
import com.example.sjqcjstock.view.SoListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 模拟炒股首页
 * Created by Administrator on 2016/8/16.
 */
public class FragmentAnalogHome extends Fragment {

//    // 上下拉刷新控件
//    private PullToRefreshLayout ptrl;
    // ScrollView
    private ScrollView myScrollView;
    // 广告滚动的加载
    private CycleViewPager cycleViewPager;
    // 获取广告的接口的类
    private List<ADInfo> infos = new ArrayList<ADInfo>();
    // 图片的加载
    private List<ImageView> views = new ArrayList<ImageView>();
    // 股票的List
    private SoListView listView;
    // 行情List的Adapter
    private DynamicExpertAdapter listAdapter;
    // 需要加载的牛人动态数据
    private ArrayList<GeniusEntity> geniusList;
    // 网络请求提示
    private ProgressDialog dialog;
    // 牛人动态返回的数据
    private String resstr;
    // 返回图片数据
    private String resimg;
    // 缓存用的类
    private ACache mCache;
    // 定时器
    private Timer timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_analog_home, null);
        // 缓存类
        mCache = ACache.get(getActivity());
        findView(view);
        initData();
        initData1();

        if (Utils.isTransactionDate()) {
            timer = new Timer();
            // 开定时器获取数据
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    initData1();
                }
            };
            timer.schedule(task, 30000, 30000); // 60s后执行task,经过60s再次执行
        }
            return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer!=null) {
            // 关闭掉定时器
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    /**
     * 控件的绑定
     */
    private void findView(View view) {
        if (cycleViewPager == null) {
            cycleViewPager = new CycleViewPager();
            this.getActivity().getFragmentManager().beginTransaction().replace(R.id.fragment_picture_imgs, cycleViewPager).commit();
        }
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(Constants.loadMessage);
        dialog.setCancelable(true);
        dialog.show();

        myScrollView = (ScrollView) view.findViewById(R.id.myScrollView);
//        ptrl = ((PullToRefreshLayout) view.findViewById(
//                R.id.refresh_view));
//        // 添加上下拉刷新事件
//        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
//            // 下来刷新
//            @Override
//            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
//                initData1();
//            }
//            // 下拉加载
//            @Override
//            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
//                // 千万别忘了告诉控件刷新完毕了哦！
//                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//            }
//        });

        listAdapter = new DynamicExpertAdapter(getActivity());
        listView = (SoListView) view.findViewById(
                R.id.list_view);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent inten = new Intent();
                inten.putExtra("code", geniusList.get(position).getStock());
                inten.putExtra("name", geniusList.get(position).getStock_name());
                inten.setClass(getActivity(), SharesDetailedActivity.class);
                startActivity(inten);
            }
        });

        // 跳转到检索页面
        view.findViewById(R.id.iv_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 常胜牛人
         */
        view.findViewById(R.id.csnr_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent();
                inten.putExtra("type", 0);
                inten.setClass(getActivity(), ExpertListsActivity.class);
                startActivity(inten);
            }
        });

        /**
         * 人气牛人
         */
        view.findViewById(R.id.rqnr_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent inten = new Intent();
//                inten.putExtra("type", 1);
//                inten.setClass(getActivity(), ExpertListsActivity.class);
//                startActivity(inten);
                Toast.makeText(getActivity(), "功能完善中，敬请期待！", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * 总收益榜
         */
        view.findViewById(R.id.zsyb_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent();
                inten.putExtra("type", 2);
                inten.setClass(getActivity(), ExpertListsActivity.class);
                startActivity(inten);
            }
        });

        /**
         * 选股牛人
         */
        view.findViewById(R.id.gpnr_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent();
                inten.putExtra("type", 3);
                inten.setClass(getActivity(), ExpertListsActivity.class);
                startActivity(inten);
            }
        });

        /**
         * 模拟比赛
         */
        view.findViewById(R.id.mnbs_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SimulationGameActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 模拟交易
         */
        view.findViewById(R.id.mnjy_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyDealAccountActivity.class);
                startActivity(intent);
            }
        });
        // 开线程获取广告图片
        new Thread(new Runnable() {
            @Override
            public void run() {
                resimg = HttpUtil.restHttpGet(Constants.moUrl+"/ad&type=1&token="+Constants.apptoken+"&uid="+Constants.staticmyuidstr);
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

    /**
     * 加载缓存的数据
     */
    private void initData() {
        String data = mCache.getAsString("orders");
        if (data != null){
            geniusList = (ArrayList<GeniusEntity>) JSON.parseArray(data,GeniusEntity.class);
            if (geniusList != null && geniusList.size()>0) {
                listAdapter.setlistData(geniusList);
                dialog.dismiss();
            }
        }
    }

    /**
     * 数据的加载
     */
    private void initData1() {
        // 开线程获牛人动态数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                resstr = HttpUtil.restHttpGet(Constants.moUrl+"/orders&token="+Constants.apptoken+"&uid="+Constants.staticmyuidstr);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }


    /**
     * 广告图片的一些加载适配
     */
    private void setImageLoader() {
        // 将最后一个ImageView添加进来
        views.add(ImageUtil.getImageView(this.getActivity(), infos.get(infos.size() - 1).getUrl()));
        for (int i = 0; i < infos.size(); i++) {
            views.add(ImageUtil.getImageView(this.getActivity(), infos.get(i).getUrl()));
        }
        // 将第一个ImageView添加进来
        views.add(ImageUtil.getImageView(this.getActivity(), infos.get(0).getUrl()));
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
    }

    /**
     * 点击广告图片的单机事件
     */
    private CycleViewPager.ImageCycleViewListener mAdCycleViewListener = new CycleViewPager.ImageCycleViewListener() {
        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {
            if (cycleViewPager.isCycle()) {
                position = position - 1;
                Intent intent = new Intent(getActivity(), advertUrlActivity.class);
                intent.putExtra("url", info.getContent());
                startActivity(intent);
            }
        }
    };

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
                        JSONObject jsonObject = new JSONObject(resstr);
                        if ("failed".equals(jsonObject.getString("status"))){
                            CustomToast.makeText(getActivity(), jsonObject.getString("data"), Toast.LENGTH_LONG).show();
//                            // 千万别忘了告诉控件刷新完毕了哦！
//                            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            dialog.dismiss();
                            return;
                        }
                        String data = jsonObject.getString("data");
                        // 牛人动态做缓存
                        mCache.put("orders", data);
                        geniusList = (ArrayList<GeniusEntity>) JSON.parseArray(jsonObject.getString("data"),GeniusEntity.class);
                        listAdapter.setlistData(geniusList);
                        ViewUtil.setListViewHeightBasedOnChildren(listView);
                        // 滚动到顶部
                        myScrollView.scrollTo(0, 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    // 千万别忘了告诉控件刷新完毕了哦！
//                    ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    dialog.dismiss();
                    break;
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(resimg);
                        String  status = jsonObject.getString("status");
                        if ("success".equals(status)) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if (jsonArray != null && jsonArray.length() > 0) {
                                ADInfo info = null;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonObject = (JSONObject) jsonArray.get(i);
                                    info = new ADInfo();
                                    info.setUrl(jsonObject.getString("image"));
                                    info.setContent(jsonObject.getString("url"));
                                    infos.add(info);
                                }
                            }
                            if (infos.size() > 0) {
                                setImageLoader();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
}
