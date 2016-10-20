package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.hotstockAdapter;
import com.example.sjqcjstock.app.ExitApplication;

import java.util.ArrayList;
import java.util.HashMap;

public class hotstocklistActivity extends Activity {

    // 定义List集合容器

    hotstockAdapter hotstockAdapter;

    // 定义于数据库同步的字段集合
    // private String[] name;
    ArrayList<HashMap<String, Object>> listhotstockData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.hotstock_list);

        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);

        initView();
    }

    private void initView() {
        /** 热门股集合 */
        ListView hotstocklistview = (ListView) findViewById(R.id.hotstocklist2);

        // 存储数据的数组列表
        listhotstockData = new ArrayList<HashMap<String, Object>>();

        for (int i = 0; i < 20; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();

            // 添加数据
            listhotstockData.add(map);
        }

        // 为ListView 添加适配器

        hotstockAdapter = new hotstockAdapter(hotstocklistActivity.this,
                listhotstockData);

        hotstocklistview.setAdapter(hotstockAdapter);
    }

}
