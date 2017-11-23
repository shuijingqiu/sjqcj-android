package com.example.sjqcjstock.Activity.stocks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.stocks.StockAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.stocks.OptionalEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ToastUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.netutil.sharesUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * 我的自选股页面
 * Created by Administrator on 2017/5/3.
 */
public class MyStocksActivity extends Activity {

    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    private ListView listView;
    // 牛人动态的Adapter
    private StockAdapter listAdapter;
    // 返回接口的数据
    private String resstr;
    // 网络请求提示
    private CustomProgress dialog;
    // 页数 分页暂时不需要
    private int page = 0;
    // 需要查询人的uid
    private String uidstr;
    // 自选股的的数据
    private ArrayList<OptionalEntity> optionalArrayList = null;
    // 自选股股票最新信息的Map
    private Map<String, Map> mapZxgStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_stocks);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        findView();
        getData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismissDlog();
        }
    }

    /**
     * 页面控件的绑定
     */
    private void findView() {
        uidstr = getIntent().getStringExtra("uid");
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialog = new CustomProgress(this);
        dialog.showDialog();
        listView = (ListView) findViewById(R.id.list_view);
        listAdapter = new StockAdapter(this);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent inten = new Intent();
                inten.putExtra("name", optionalArrayList.get(position).getStock_name());
                inten.putExtra("code", optionalArrayList.get(position).getStock());
                inten.setClass(MyStocksActivity.this, SharesDetailedActivity.class);
                startActivity(inten);
            }
        });

        ptrl = ((PullToRefreshLayout) findViewById(
                R.id.refresh_view));
        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下来刷新
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                optionalArrayList.clear();
                page = 1;
                getData();
            }

            // 上拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                optionalArrayList.clear();
                page += 1;
                getData();
            }
        });
    }

    /**
     * 加载绑定数据
     *
     * @return
     */
    public void getData() {
        // 开线程获牛人动态数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用接口获取用户获取自选股
                resstr = HttpUtil.restHttpGet(Constants.moUrl + "/user/getUserOptional&uid=" + uidstr + "&token=" + Constants.apptoken);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }


    /**
     * 获取自选股股票实时数据进行处理
     *
     * @param listOptional
     */
    private void getoptionalData(ArrayList<OptionalEntity> listOptional) {
        String str = "";
        for (OptionalEntity optionalEntity : listOptional) {
            if (!"".equals(str)) {
                str += ",";
            }
            str += Utils.judgeSharesCode(optionalEntity.getStock());
        }
        // 开线程获股票当前信息
        final String finalStr = str;
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用接口获取股票当前行情数据（返回股票代码和最新价格的map）
                mapZxgStr = sharesUtil.getsharess(finalStr);
                // 重新加载自选股数据
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
                            // 千万别忘了告诉控件刷新完毕了哦！
                            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            ToastUtil.showToast(MyStocksActivity.this,jsonObject.getString("data"));
                            dialog.dismissDlog();
                            return;
                        }
                        String data = jsonObject.getString("data");
                        if (data.length()<10){
                            ToastUtil.showToast(MyStocksActivity.this,jsonObject.getString("data"));
                            dialog.dismissDlog();
                            return;
                        }
                        // 自选股的数据
                        ArrayList<OptionalEntity> optionalList = (ArrayList<OptionalEntity>) JSON.parseArray(jsonObject.getString("data"), OptionalEntity.class);
                        getoptionalData(optionalList);
                        optionalArrayList = optionalList;
                        // 加载自选股股票信息
                        listAdapter.setlistData(optionalList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    dialog.dismissDlog();
                    break;
                case 1:
                    // 重新加载自选股列表
                    for (int i = 0; i < optionalArrayList.size(); i++) {
                        Map<String, String> mStr = mapZxgStr.get(optionalArrayList.get(i).getStock());
                        if (mStr != null && mStr.size() > 0) {
                            optionalArrayList.get(i).setPrice(mStr.get("price"));
                            optionalArrayList.get(i).setRising(mStr.get("rising"));
                            optionalArrayList.get(i).setIstype(mStr.get("type"));
                        }
                    }
                    // 刷新自选股列表
                    listAdapter.setlistData(optionalArrayList);
                    break;
            }
        }
    };
}