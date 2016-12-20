package com.example.sjqcjstock.Activity.stocks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.stocks.SimulationGameDetailsAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.stocks.MatchEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Md5Util;
import com.example.sjqcjstock.view.PullToRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 模拟比赛详情列表
 * Created by Administrator on 2016/8/17.
 */
public class SimulationGameDetailsActivity extends Activity {

    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 加载的ListView
    private ListView listView;
    // 我的收益
    private TextView myProfitTv;
    // 我的排名
    private TextView rankingTv;
    // 头像
    private ImageView headImgIv;
    // 我比赛的
    private LinearLayout myMatchLl;
    // 显示加载的Adapter
    private SimulationGameDetailsAdapter sgdAdapter;
    // 比赛数据
    private ArrayList<MatchEntity> matchLists;
    // 获取比赛详细的排名数据
    private String matchStr = "";
    // 比赛的id
    private String matchId;
    // 比赛的标题
    private String title;
    // 分页
    private int page = 1;
    // 网络请求提示
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_simulation_game_details);
        ExitApplication.getInstance().addActivity(this);
        title = getIntent().getStringExtra("name");
        matchId = getIntent().getStringExtra("id");
        findView();
        initData();
    }

    /**
     * 控件的绑定
     */
    private void findView() {
        dialog = new ProgressDialog(this);
        dialog.setMessage(Constants.loadMessage);
        dialog.setCancelable(true);
        dialog.show();
        /**
         * 返回按钮的事件绑定
         */
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 设置比赛标题
        ((TextView) findViewById(R.id.top_title_tv)).setText(title);
        sgdAdapter = new SimulationGameDetailsAdapter(this);
        listView = (ListView) findViewById(
                R.id.list_view);
        listView.setAdapter(sgdAdapter);
        headImgIv = (ImageView) findViewById(R.id.head_img_iv);
        myProfitTv = (TextView) findViewById(R.id.my_profit_tv);
        rankingTv = (TextView) findViewById(R.id.ranking_tv);
        myMatchLl = (LinearLayout) findViewById(R.id.my_match_ll);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SimulationGameDetailsActivity.this,
                        UserDetailNewActivity.class);
                intent.putExtra("uid", matchLists.get(position).getUid());
                intent.putExtra("type","1");
            }
        });
        ptrl = ((PullToRefreshLayout) findViewById(
                R.id.refresh_view));
        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下来刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                page = 1;
                initData();
                // 千万别忘了告诉控件刷新完毕了哦！
                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            // 下拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                page += 1;
                initData();
                // 千万别忘了告诉控件刷新完毕了哦！
                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
            }
        });
        ImageLoader.getInstance().displayImage(Md5Util.getuidstrMd5(Md5Util
                        .getMd5(Constants.staticmyuidstr)),
                headImgIv, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
    }

    /**
     * 数据的获取
     */
    private void initData() {
        // 开线程获取比赛详细信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                matchStr = HttpUtil.restHttpGet(Constants.moUrl+"/match/detail&id="+matchId+"&uid="+Constants.staticmyuidstr+"&token="+Constants.apptoken+"&np="+page+"&limit="+50);
                handler.sendEmptyMessage(0);
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
                        JSONObject jsonObject = new JSONObject(matchStr);
                        if ("failed".equals(jsonObject.getString("status"))){
                            Toast.makeText(getApplicationContext(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                        }else{
                            JSONObject jsonData = jsonObject.getJSONObject("data");
                            MatchEntity matchEntity = JSON.parseObject(jsonData.getString("match"),MatchEntity.class);
                            // 0未参加 1已参加
                            if ("0".equals(matchEntity.getJoined())){
                                myMatchLl.setVisibility(View.GONE);
                            }else{
                                myMatchLl.setVisibility(View.VISIBLE);
                                rankingTv.setText(matchEntity.getRanking());
                                String totalRate = matchEntity.getTotal_rate();
                                if (totalRate ==null || Double.valueOf(totalRate)>=0){
                                    myProfitTv.setTextColor(myProfitTv.getResources().getColor(R.color.color_ef3e3e));
                                }else{
                                    myProfitTv.setTextColor(myProfitTv.getResources().getColor(R.color.color_1bc07d));
                                }
                                myProfitTv.setText(totalRate+"%");
                            }
                            ArrayList<MatchEntity> matchList = (ArrayList<MatchEntity>) JSON.parseArray(jsonData.getString("rankList"),MatchEntity.class);
                            if(page == 1){
                                matchLists = matchList;
                            }else{
                                matchLists.addAll(matchList);
                            }
                            sgdAdapter.setlistData(matchLists);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                    break;
            }
        }
    };
}
