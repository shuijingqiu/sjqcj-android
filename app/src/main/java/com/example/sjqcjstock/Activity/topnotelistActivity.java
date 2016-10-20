package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.topnoteAdapter;
import com.example.sjqcjstock.app.ExitApplication;

import java.util.ArrayList;
import java.util.HashMap;

public class topnotelistActivity extends Activity {

    // 定义List集合容器

    topnoteAdapter topnoteAdapter;

    // 定义于数据库同步的字段集合
    // private String[] name;

    ArrayList<HashMap<String, Object>> listtopnoteData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.topnote_list);

        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);

        initView();

    }

    private void initView() {
        /** 置顶帖集合 */
        ListView topnotelistview = (ListView) findViewById(R.id.topnotelist2);

        // 存储数据的数组列表
        listtopnoteData = new ArrayList<HashMap<String, Object>>();

        for (int i = 0; i < 20; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();

            // 添加数据
            listtopnoteData.add(map);
        }

        // 为ListView 添加适配器

        topnoteAdapter = new topnoteAdapter(topnotelistActivity.this,
                listtopnoteData);

        topnotelistview.setAdapter(topnoteAdapter);
    }

}
