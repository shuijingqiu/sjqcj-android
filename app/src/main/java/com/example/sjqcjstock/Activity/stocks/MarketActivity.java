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
import android.widget.ListView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.stocks.MarketAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.stocks.StocksInfo;
import com.example.sjqcjstock.netutil.HttpUtil;

import java.util.ArrayList;

/**
 * 行情主页面(不要的。。。。。。。。)
 * Created by Administrator on 2016/8/5.
 */
public class MarketActivity extends Activity {

    // 股票的List
    private ListView sharesList;
    // 行情List的Adapter
    private MarketAdapter listAdapter;
    // 需要加载的行情数据
    private ArrayList<StocksInfo> listInfo;
    // 网络请求提示
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_market);
        ExitApplication.getInstance().addActivity(this);
        findView();
        initData();
    }

    /**
     * 绑定页面控件
     */
    public void findView() {
        dialog = new ProgressDialog(this);
        // dialog.setTitle("提示信息");
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

        // 显示行情的List
        sharesList = (ListView) findViewById(R.id.shares_market_lv);
        // 点击股票信息的单击事件
        sharesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String code = ((TextView)view.findViewById(R.id.shares_code)).getText()+"";
//                String name = ((TextView)view.findViewById(R.id.shares_name)).getText()+"";
                Intent inten = new Intent();
                inten.putExtra("code", listInfo.get(position).getCode());
                inten.putExtra("name", listInfo.get(position).getName());
                inten.putExtra("openF", listInfo.get(position).getOpenPrice());
                inten.setClass(MarketActivity.this, SharesDetailedActivity.class);
                startActivity(inten);
            }
        });
        listAdapter = new MarketAdapter(this);
        sharesList.setAdapter(listAdapter);
    }

    /**
     * 买入按钮
     *
     * @param view
     */
    public void mairuClick(View view) {
        Intent intent = new Intent(this, BusinessActivity.class);
        intent.putExtra("type", "0");
        startActivity(intent);
    }

    /**
     * 卖出按钮
     *
     * @param view
     */
    public void maichuClick(View view) {
        Intent intent = new Intent(this, BusinessActivity.class);
        intent.putExtra("type", "1");
        startActivity(intent);
    }

    /**
     * 查询按钮
     *
     * @param view
     */
    public void chaxunClick(View view) {
        Intent intent = new Intent(this, QueryActivity.class);
        startActivity(intent);
    }

    /**
     * 交易账户按钮
     *
     * @param view
     */
    public void zhanghuClick(View view) {
        Intent intent = new Intent(this, UserDetailNewActivity.class);
        intent.putExtra("uid", "11643");
        startActivity(intent);
    }

    /**
     * 模拟炒股首页
     *
     * @param view
     */
    public void analogHomeClick(View view) {

    }

    /**
     * 获取整理数据
     */
    private void initData() {
        listInfo = new ArrayList<StocksInfo>();
        // 开线程获取网络数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取16年该股票的数据
                String strData = HttpUtil.getIntentData("http://qt.gtimg.cn/q=sz000858,sz300526,sz000011,sz300497,sz000629,sz000725");
                processData(strData);
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
                    listAdapter.setlistData(listInfo);
                    dialog.dismiss();
                    break;
            }
        }
    };

    /**
     * 根据返回的值进行处理得到想要的数据
     *
     * @param strData
     */
    private void processData(String strData) {
        // 行情数据
        StocksInfo stocks = null;
        // 每只股票的数据
        String[] shares = strData.split(";");
        for (String str : shares) {
            if ("".equals(str.trim())) {
                continue;
            }
            // 每只股票的详细数据
            String[] sharesMinute = str.split("~");
            stocks = new StocksInfo();
            stocks.setName(sharesMinute[1]);
            stocks.setCode(sharesMinute[2]);
            stocks.setSpotPrice(sharesMinute[3]);
            stocks.setOpenPrice(sharesMinute[4]);
            if (sharesMinute.length < 31) {
                stocks.setHighsLows("0");
            } else {
                stocks.setHighsLows(sharesMinute[31]);
            }
            listInfo.add(stocks);
        }
    }

}
