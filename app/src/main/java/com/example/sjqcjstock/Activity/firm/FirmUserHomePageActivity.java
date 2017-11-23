package com.example.sjqcjstock.Activity.firm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.Activity.Article.ArticleDetailsActivity;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.firm.FirmOperationAdapter;
import com.example.sjqcjstock.adapter.firm.FirmPositionAdapter;
import com.example.sjqcjstock.adapter.firm.FirmRecollectionsAdapter;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.Article.UserEntity;
import com.example.sjqcjstock.entity.firm.FirmCodeEntity;
import com.example.sjqcjstock.entity.firm.FirmMatchEntity;
import com.example.sjqcjstock.entity.firm.FirmRemarkEntity;
import com.example.sjqcjstock.entity.firm.FirmTransEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.ToastUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.view.CustomProgress;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 实盘用户个人主页
 * Created by Administrator on 2017/9/28.
 */
public class FirmUserHomePageActivity extends Activity {
    // 加载持仓ListView
    private ListView positionLv;
    // 最新操作的ListView
    private ListView operationLv;
    // 加载操作感言ListView
    private ListView recollectionsLv;
    private FirmPositionAdapter positionAdapter;
    private FirmRecollectionsAdapter recollectionsAdapter;
    // 显示最新操作的Adapter
    private FirmOperationAdapter operationAdapter;
    // 最新操作的List
    private ArrayList<FirmTransEntity> firmTransEntityList;
    // 网络请求提示
    private CustomProgress dialog;
    // 调用接口返回的数据
    private String resstr = "";
    private String appUserStr = "";
    private String transStr = "";
    private String positionStr = "";
    private String remarkStr = "";
    private ScrollView myScrollView;
    // 比赛名称
    private TextView matchNameTv;
    // 更新日期
    private TextView timeTv;
    // 初始资产
    private TextView initialTv;
    // 昨日资产
    private TextView yesterdayTv;
    // 现有资产
    private TextView assetsTv;
    // 存取
    private TextView accessTv;
    // 总收益率
    private TextView totalRatioTv;
    // 排名
    private TextView rankTv;
    // 5日收益率
    private TextView weekRatioTv;
    // 今日收益率
    private TextView daysRatioTv;
    // 昨日收益率
    private TextView yesterdayRatioTv;
    // 博主名称
    private TextView userNameTv;
    // 博主简介
    private TextView userIntroTv;
    // 博主头像
    private ImageView headIv;
    // 参赛id
    private String matchId;
    // 比赛人id
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firm_user_home_page);
        findView();
        getData();
        // 滚动到顶部
        myScrollView.smoothScrollTo(0, 0);
    }

    private void findView() {
        matchId = getIntent().getStringExtra("id");
        uid = getIntent().getStringExtra("uid");
        dialog = new CustomProgress(this);
        dialog.showDialog();
        /**
         * 返回按钮的事件绑定
         */
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        myScrollView = (ScrollView) findViewById(R.id.myScrollView);
        positionAdapter = new FirmPositionAdapter(this);
        positionLv = (ListView) findViewById(
                R.id.position_lv);
        positionLv.setAdapter(positionAdapter);

        operationAdapter = new FirmOperationAdapter(this);
        operationLv = (ListView) findViewById(
                R.id.operation_lv);
        operationLv.setAdapter(operationAdapter);
        operationLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FirmUserHomePageActivity.this, ArticleDetailsActivity.class);
                intent.putExtra("weibo_id", firmTransEntityList.get(position).getFeed_id());
                startActivity(intent);
            }
        });

        recollectionsAdapter = new FirmRecollectionsAdapter(this);
        recollectionsLv = (ListView) findViewById(
                R.id.recollections_lv);
        recollectionsLv.setAdapter(recollectionsAdapter);
        userNameTv = (TextView) findViewById(R.id.user_name_tv);
        userIntroTv = (TextView) findViewById(R.id.user_intro_tv);
        headIv = (ImageView) findViewById(R.id.head_iv);
        matchNameTv = (TextView) findViewById(R.id.match_name_tv);
        timeTv = (TextView) findViewById(R.id.time_tv);
        initialTv = (TextView) findViewById(R.id.initial_tv);
        yesterdayTv = (TextView) findViewById(R.id.yesterday_tv);
        assetsTv = (TextView) findViewById(R.id.assets_tv);
        accessTv = (TextView) findViewById(R.id.access_tv);
        totalRatioTv = (TextView) findViewById(R.id.total_ratio_tv);
        rankTv = (TextView) findViewById(R.id.rank_tv);
        weekRatioTv = (TextView) findViewById(R.id.week_ratio_tv);
        daysRatioTv = (TextView) findViewById(R.id.days_ratio_tv);
        yesterdayRatioTv = (TextView) findViewById(R.id.yesterday_ratio_tv);

    }

    private void getData() {
        // 开线程获比赛信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                resstr = HttpUtil.restHttpGet(Constants.newUrl + "/api/match/space?id=" + matchId);
                handler.sendEmptyMessage(0);
            }
        }).start();

        // 从网络获取用户详细信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                appUserStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/user/info?mid=" + uid);
                handler.sendEmptyMessage(1);
            }
        }).start();

        // 从网络获取用户操作记录
        new Thread(new Runnable() {
            @Override
            public void run() {
                transStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/match/trans?id=" + matchId + "&p=1&limit=3");
                handler.sendEmptyMessage(2);
            }
        }).start();

        // 从网络获取用户持仓
        new Thread(new Runnable() {
            @Override
            public void run() {
                positionStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/match/position?id=" + matchId);
                handler.sendEmptyMessage(3);
            }
        }).start();

        // 从网络获取用户操作总结
        new Thread(new Runnable() {
            @Override
            public void run() {
                remarkStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/match/remark?id=" + matchId + "&p=1&limit=3");
                handler.sendEmptyMessage(4);
            }
        }).start();

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
                        JSONObject jsonObject = new JSONObject(resstr);
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            ToastUtil.showToast(getApplicationContext(), jsonObject.getString("data"));
                            dialog.dismissDlog();
                            return;
                        }
                        FirmMatchEntity firmMatchEntity = JSON.parseObject(jsonObject.getString("data"), FirmMatchEntity.class);
                        matchNameTv.setText(firmMatchEntity.getTitle() + "(第" + firmMatchEntity.getDays() + "天)");
                        timeTv.setText("更新日期（" + firmMatchEntity.getU_time() + "）");
                        rankTv.setText(firmMatchEntity.getRank());

                        String totalRatio = firmMatchEntity.getTotal_ratio() + "";
                        if (Double.valueOf(totalRatio) == 0d) {
                            totalRatioTv.setTextColor(totalRatioTv.getResources().getColor(R.color.color_article));
                        } else if (totalRatio.indexOf("-") == -1) {
                            totalRatioTv.setTextColor(totalRatioTv.getResources().getColor(R.color.color_ef3e3e));
                        } else {
                            totalRatioTv.setTextColor(totalRatioTv.getResources().getColor(R.color.color_1bc07d));
                        }
                        totalRatioTv.setText(totalRatio + "%");

                        String initial = firmMatchEntity.getInitial() + "";
                        initialTv.setText(initial);
//                        if (initial.indexOf("-") == -1) {
//                            initialTv.setTextColor(initialTv.getResources().getColor(R.color.color_ef3e3e));
//                        } else {
//                            initialTv.setTextColor(initialTv.getResources().getColor(R.color.color_1bc07d));
//                        }

                        String yesterday = firmMatchEntity.getYesterday() + "";
//                        if (yesterday.indexOf("-") == -1) {
//                            yesterdayTv.setTextColor(yesterdayTv.getResources().getColor(R.color.color_ef3e3e));
//                        } else {
//                            yesterdayTv.setTextColor(yesterdayTv.getResources().getColor(R.color.color_1bc07d));
//                        }
                        yesterdayTv.setText(yesterday);

                        String assets = firmMatchEntity.getAssets() + "";
                        assetsTv.setText(assets);
//                        if (assets.indexOf("-") == -1) {
//                            assetsTv.setTextColor(assetsTv.getResources().getColor(R.color.color_ef3e3e));
//                        } else {
//                            assetsTv.setTextColor(assetsTv.getResources().getColor(R.color.color_1bc07d));
//                        }

                        String access = firmMatchEntity.getAccess() + "";
                        accessTv.setText(access);
                        access = Utils.getNumber(access);
                        if (Double.valueOf(access) == 0d) {
                            accessTv.setTextColor(accessTv.getResources().getColor(R.color.color_article));
                        } else if (access.indexOf("-") == -1) {
                            accessTv.setTextColor(accessTv.getResources().getColor(R.color.color_ef3e3e));
                        } else {
                            accessTv.setTextColor(accessTv.getResources().getColor(R.color.color_1bc07d));
                        }

                        String weekRatio = firmMatchEntity.getWeek_ratio() + "";
                        if (Double.valueOf(weekRatio) == 0d) {
                            weekRatioTv.setTextColor(weekRatioTv.getResources().getColor(R.color.color_article));
                        } else if (weekRatio.indexOf("-") == -1) {
                            weekRatioTv.setTextColor(weekRatioTv.getResources().getColor(R.color.color_ef3e3e));
                        } else {
                            weekRatioTv.setTextColor(weekRatioTv.getResources().getColor(R.color.color_1bc07d));
                        }
                        weekRatioTv.setText(weekRatio + "%");

                        String daysRatio = firmMatchEntity.getDays_ratio() + "";
                        if (Double.valueOf(daysRatio) == 0d) {
                            daysRatioTv.setTextColor(daysRatioTv.getResources().getColor(R.color.color_article));
                        } else if (daysRatio.indexOf("-") == -1) {
                            daysRatioTv.setTextColor(daysRatioTv.getResources().getColor(R.color.color_ef3e3e));
                        } else {
                            daysRatioTv.setTextColor(daysRatioTv.getResources().getColor(R.color.color_1bc07d));
                        }
                        daysRatioTv.setText(daysRatio + "%");

                        String yesterdayRatio = firmMatchEntity.getYesterday_ratio() + "";
                        if (Double.valueOf(yesterdayRatio) == 0d) {
                            yesterdayRatioTv.setTextColor(yesterdayRatioTv.getResources().getColor(R.color.color_article));
                        } else if (yesterdayRatio.indexOf("-") == -1) {
                            yesterdayRatioTv.setTextColor(yesterdayRatioTv.getResources().getColor(R.color.color_ef3e3e));
                        } else {
                            yesterdayRatioTv.setTextColor(yesterdayRatioTv.getResources().getColor(R.color.color_1bc07d));
                        }
                        yesterdayRatioTv.setText(yesterdayRatio + "%");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.dismissDlog();
                    break;
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(appUserStr);
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            return;
                        }
                        UserEntity userEntity = JSON.parseObject(jsonObject.getString("data"), UserEntity.class);
                        String unamestr = userEntity.getUname();
                        String introstr = userEntity.getIntro();
                        if (introstr == null || "".equals(introstr)) {
                            introstr = "暂无简介";
                        }
                        userNameTv.setText(unamestr);
                        userIntroTv.setText(introstr);
                        ImageLoader.getInstance().displayImage(userEntity.getAvatar_middle(),
                                headIv, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        JSONObject jsonObject = new JSONObject(transStr);
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            return;
                        }
                        firmTransEntityList = (ArrayList<FirmTransEntity>) JSON.parseArray(jsonObject.getString("data"), FirmTransEntity.class);
                        operationAdapter.setlistData(firmTransEntityList);
                        Utils.setListViewHeightBasedOnChildren(operationLv);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        JSONObject jsonObject = new JSONObject(positionStr);
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            return;
                        }
                        ArrayList<FirmCodeEntity> list = (ArrayList<FirmCodeEntity>) JSON.parseArray(jsonObject.getString("data"), FirmCodeEntity.class);
                        ArrayList<FirmCodeEntity> firmCodeEntityArrayList = new ArrayList<FirmCodeEntity>();
                        if (list.size() > 3) {
                            for (int i = 0; i < 3; i++) {

                                firmCodeEntityArrayList.add(list.get(i));
                            }
                        }else{
                            firmCodeEntityArrayList = list;
                        }
                        positionAdapter.setlistData(firmCodeEntityArrayList);
                        Utils.setListViewHeightBasedOnChildren(positionLv);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        JSONObject jsonObject = new JSONObject(remarkStr);
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            return;
                        }
                        ArrayList<FirmRemarkEntity> list = (ArrayList<FirmRemarkEntity>) JSON.parseArray(jsonObject.getString("data"), FirmRemarkEntity.class);
                        recollectionsAdapter.setlistData(list);
                        Utils.setListViewHeightBasedOnChildren(recollectionsLv);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    /**
     * 历史操作的点击事件
     *
     * @param view
     */
    public void HistoryOperationClick(View view) {
        Intent intent = new Intent(this, FirmHistoryOperationActivity.class);
        intent.putExtra("id", matchId);
        startActivity(intent);
    }

    /**
     * 历史持仓的点击事件
     *
     * @param view
     */
    public void HistoryPositionClick(View view) {
        Intent intent = new Intent(this, FirmHistoryPositionActivity.class);
        intent.putExtra("positionStr", positionStr);
        startActivity(intent);
    }

    /**
     * 更多炒作感言的点击事件
     *
     * @param view
     */
    public void MoreRecollectionsClick(View view) {
        Intent intent = new Intent(this, FirmRecollectionsActivity.class);
        intent.putExtra("id", matchId);
        startActivity(intent);
    }

    /**
     * 跳转到个人主页的点击事件
     *
     * @param view
     */
    public void UserHomeClick(View view) {
        Intent intent = new Intent(this, UserDetailNewActivity.class);
        intent.putExtra("uid", uid);
        intent.putExtra("type", "2");
        startActivity(intent);
    }

}
