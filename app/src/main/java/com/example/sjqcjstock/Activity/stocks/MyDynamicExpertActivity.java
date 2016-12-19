package com.example.sjqcjstock.Activity.stocks;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.stocks.DynamicExpertAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.entity.stocks.GeniusEntity;

import java.util.ArrayList;

/**
 * 我的牛人动态页面
 * Created by Administrator on 2016/12/16.
 */
public class MyDynamicExpertActivity extends Activity{

    private ListView listView;
    // 牛人动态的Adapter
    private DynamicExpertAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_dynamic_expert);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        findView();
        getData();
    }

    /**
     * 页面控件的绑定
     */
    private void findView() {
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView = (ListView) findViewById(R.id.list_view);
        listAdapter = new DynamicExpertAdapter(this);
        listView.setAdapter(listAdapter);
    }

    /**
     * 加载绑定数据
     * @return
     */
    public void getData() {
        String jsonStr = "[{\"id\":37,\"uid\":25110,\"stock\":\"000519\",\"stock_name\":\"江南红箭\",\"username\":\"强哥一号拖拉机\",\"price\":15.39,\"time\":\"2016-12-16 13:01:43\",\"type\":1,\"total_rate\":2.798,\"ratio\":0.552},{\"id\":36,\"uid\":25512,\"stock\":\"000519\",\"stock_name\":\"江南红箭\",\"username\":\"强哥3号挖掘机1\",\"price\":15.18,\"time\":\"2016-12-16 13:00:00\",\"type\":1,\"total_rate\":0.026,\"ratio\":1.941},{\"id\":34,\"uid\":25512,\"stock\":\"600858\",\"stock_name\":\"银座股份\",\"username\":\"强哥3号挖掘机1\",\"price\":9,\"time\":\"2016-12-16 13:17:01\",\"type\":1,\"total_rate\":0.026,\"ratio\":0.011},{\"id\":32,\"uid\":10014,\"stock\":\"000519\",\"stock_name\":\"江南红箭\",\"username\":\"水晶球\",\"price\":15.15,\"time\":\"2016-12-16 11:24:42\",\"type\":1,\"total_rate\":1.066,\"ratio\":3.095},{\"id\":26,\"uid\":49125,\"stock\":\"600838\",\"stock_name\":\"上海九百\",\"username\":\"公冶\",\"price\":18.47,\"time\":\"2016-12-16 10:00:43\",\"type\":1,\"total_rate\":-1.467,\"ratio\":-2.694},{\"id\":23,\"uid\":10014,\"stock\":\"000519\",\"stock_name\":\"江南红箭\",\"username\":\"水晶球\",\"price\":14.87,\"time\":\"2016-12-16 09:36:02\",\"type\":1,\"total_rate\":1.066,\"ratio\":3.095},{\"id\":22,\"uid\":42816,\"stock\":\"000065\",\"stock_name\":\"北方国际\",\"username\":\"梦幻西游\",\"price\":24.6,\"time\":\"2016-12-16 09:32:01\",\"type\":1,\"total_rate\":0.026,\"ratio\":1.23},{\"id\":21,\"uid\":10014,\"stock\":\"600822\",\"stock_name\":\"上海物贸\",\"username\":\"水晶球\",\"price\":20.01,\"time\":\"2016-12-16 09:30:02\",\"type\":1,\"total_rate\":1.066,\"ratio\":7.189},{\"id\":20,\"uid\":25110,\"stock\":\"600838\",\"stock_name\":\"上海九百\",\"username\":\"强哥一号拖拉机\",\"price\":17.7,\"time\":\"2016-12-16 09:30:02\",\"type\":1,\"total_rate\":2.798,\"ratio\":1.352},{\"id\":18,\"uid\":10014,\"stock\":\"600838\",\"stock_name\":\"上海九百\",\"username\":\"水晶球\",\"price\":17.98,\"time\":\"2016-12-15 14:56:57\",\"type\":1,\"total_rate\":1.066,\"ratio\":-0.044},{\"id\":17,\"uid\":26265,\"stock\":\"600838\",\"stock_name\":\"上海九百\",\"username\":\"文汇\",\"price\":17.65,\"time\":\"2016-12-15 13:44:07\",\"type\":1,\"total_rate\":1.612,\"ratio\":1.823},{\"id\":16,\"uid\":10032,\"stock\":\"300357\",\"stock_name\":\"我武生物\",\"username\":\"黄色水晶球\",\"price\":36.37,\"time\":\"2016-12-15 13:38:56\",\"type\":1,\"total_rate\":-0.101,\"ratio\":-0.099},{\"id\":13,\"uid\":27493,\"stock\":\"002389\",\"stock_name\":\"南洋科技\",\"username\":\"被冰封的xin\",\"price\":26.6,\"time\":\"2016-12-15 13:25:01\",\"type\":1,\"total_rate\":0.693,\"ratio\":2.603},{\"id\":11,\"uid\":25110,\"stock\":\"600275\",\"stock_name\":\"武昌鱼\",\"username\":\"强哥一号拖拉机\",\"price\":18.97,\"time\":\"2016-12-15 13:20:02\",\"type\":1,\"total_rate\":2.798,\"ratio\":8.01},{\"id\":10,\"uid\":49125,\"stock\":\"600150\",\"stock_name\":\"中国船舶\",\"username\":\"公冶\",\"price\":28.2,\"time\":\"2016-12-15 13:46:02\",\"type\":1,\"total_rate\":-1.467,\"ratio\":-4.315},{\"id\":8,\"uid\":25110,\"stock\":\"600822\",\"stock_name\":\"上海物贸\",\"username\":\"强哥一号拖拉机\",\"price\":20.26,\"time\":\"2016-12-15 13:12:05\",\"type\":1,\"total_rate\":2.798,\"ratio\":5.868},{\"id\":7,\"uid\":11643,\"stock\":\"600005\",\"stock_name\":\"武钢股份\",\"username\":\"gooder普通\",\"price\":3.56,\"time\":\"2016-12-15 13:09:06\",\"type\":1,\"total_rate\":-0.001,\"ratio\":-1.255},{\"id\":6,\"uid\":42816,\"stock\":\"600650\",\"stock_name\":\"锦江投资\",\"username\":\"梦幻西游\",\"price\":23.11,\"time\":\"2016-12-15 13:00:02\",\"type\":1,\"total_rate\":0.026,\"ratio\":2.245},{\"id\":5,\"uid\":42816,\"stock\":\"000065\",\"stock_name\":\"北方国际\",\"username\":\"梦幻西游\",\"price\":24.71,\"time\":\"2016-12-15 13:00:02\",\"type\":1,\"total_rate\":0.026,\"ratio\":1.23},{\"id\":4,\"uid\":42816,\"stock\":\"600519\",\"stock_name\":\"贵州茅台\",\"username\":\"梦幻西游\",\"price\":327,\"time\":\"2016-12-15 13:20:02\",\"type\":1,\"total_rate\":0.026,\"ratio\":-0.369},{\"id\":3,\"uid\":10032,\"stock\":\"600468\",\"stock_name\":\"百利电气\",\"username\":\"黄色水晶球\",\"price\":13.63,\"time\":\"2016-12-15 13:00:02\",\"type\":1,\"total_rate\":-0.101,\"ratio\":-2.266}]";
        ArrayList<GeniusEntity> geniusList = (ArrayList<GeniusEntity>) JSON.parseArray(jsonStr,GeniusEntity.class);
        if (geniusList != null && geniusList.size()>0) {
            listAdapter.setlistData(geniusList);
        }
    }
}
