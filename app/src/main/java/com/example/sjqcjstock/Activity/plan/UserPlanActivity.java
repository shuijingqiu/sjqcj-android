package com.example.sjqcjstock.Activity.plan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.plan.PlanAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.plan.PlanEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.PullToRefreshLayout;
import com.example.sjqcjstock.view.SoListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 单个用户的投资计划列表
 * Created by Administrator on 2017/4/25.
 */
public class UserPlanActivity extends Activity{

    // 头像
    private ImageView headImg;
    // 用户名
    private TextView userNameTv;
    // 简介
    private TextView userintro1;
    // 投资计划
    private TextView investmentPlanTv;
    // 总达标率
    private TextView totalComplianceRateTv;
    // 计划总收益
    private TextView totalPlannedIncomeTv;

    // 定义List集合容器
    private PlanAdapter adapter;
    //定义于数据库同步的字段集合
    private ArrayList<PlanEntity> listData;
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private SoListView listView;
    //访问页数控制
    private int current = 1;

    // 网络请求提示
    private CustomProgress dialog;
    // 接口返回数据
    private String resstr;
    private String resstr1;
    // 用户Uid
    private String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_plan);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        findView();
        getData();
        getDataPlan();
    }

    /**
     * 页面控件的绑定
     */
    private void findView() {
        uid = getIntent().getStringExtra("uid");
        String name = getIntent().getStringExtra("name");
        ((TextView)findViewById(R.id.title_tv)).setText(name+"的投资计划");
        dialog = new CustomProgress(this);
        dialog.showDialog();

        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 头像
        headImg = (ImageView) findViewById(R.id.head_img);
        // 用户名
        userNameTv = (TextView) findViewById(R.id.user_name_tv);
        // 简介
        userintro1 = (TextView) findViewById(R.id.userintro1);
        // 投资计划
        investmentPlanTv = (TextView) findViewById(R.id.investment_plan_tv);
        // 总达标率
        totalComplianceRateTv = (TextView) findViewById(R.id.total_compliance_rate_tv);
        // 计划总收益
        totalPlannedIncomeTv = (TextView) findViewById(R.id.total_planned_income_tv);
        listData = new ArrayList<PlanEntity>();
        adapter = new PlanAdapter(this);
        listView = (SoListView) findViewById(
                R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UserPlanActivity.this, PlanExhibitionActivity.class);
                intent.putExtra("id",listData.get(position).getId());
                intent.putExtra("uid",listData.get(position).getUid());
                startActivity(intent);
            }
        });
        ptrl = ((PullToRefreshLayout) findViewById(
                R.id.refresh_view));
        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下来刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                //清空列表重载数据
                listData.clear();
                current = 1;
                getDataPlan();
            }

            // 下拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                current++;
                getDataPlan();
            }
        });
    }

    /**
     * 获取用户数据
     */
    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取投资计划博主信息
                resstr = HttpUtil.restHttpGet(Constants.moUrl + "/dealplan/blogger/info?uid=" + uid);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    /**
     * 获取投资计划
     */
    private void getDataPlan(){
        // 获取单个用户的投资计划
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用接口获取直播列表
                resstr1 = HttpUtil.restHttpGet(Constants.moUrl+"/dealplan/index/list?uid="+uid+"&mid="+Constants.staticmyuidstr+"&p="+current);
                handler.sendEmptyMessage(1);
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
                        if ("failed".equals(jsonObject.getString("status"))) {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                            dialog.dismissDlog();
                            return;
                        }
                        JSONObject jsonData = new JSONObject(jsonObject.getString("data"));
                        // 头像
                        ImageLoader.getInstance().displayImage(jsonData.getString("avatar"),
                                headImg, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
                        // 用户名
                        userNameTv.setText(jsonData.getString("username"));
                        // 简介
                        userintro1.setText(jsonData.getString("intro"));
                        // 投资计划数
                        investmentPlanTv.setText(jsonData.getString("count"));
                        // 总达标率
                        totalComplianceRateTv.setText(jsonData.getString("success_ratio"));
                        // 计划总收益
                        totalPlannedIncomeTv.setText(jsonData.getString("total_ratio"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(resstr1);
                        if ("failed".equals(jsonObject.getString("status"))) {
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            return;
                        }
                        ArrayList<PlanEntity> tomliveList = (ArrayList<PlanEntity>) JSON.parseArray(jsonObject.getString("data"), PlanEntity.class);
                        if (current == 1) {
                            listData = tomliveList;
                        } else {
                            listData.addAll(tomliveList);
                        }
                        adapter.setlistData(listData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }finally {
                        dialog.dismissDlog();
                    }
                    // 千万别忘了告诉控件刷新完毕了哦！
                    ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    break;
            }
        }
    };

    /**
     * 跳转到用户主页
     * @param view
     */
    public void UserDataClick(View view){
        // 跳转到个人主页
        Intent intent = new Intent(this,
                UserDetailNewActivity.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }

}
