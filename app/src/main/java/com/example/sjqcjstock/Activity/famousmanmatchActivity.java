package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.sjqcjstock.adapter.essencematchAdapter;
import com.example.sjqcjstock.adapter.famousmanmatchAdapter;
import com.example.sjqcjstock.app.ExitApplication;

import java.util.ArrayList;
import java.util.HashMap;

public class famousmanmatchActivity extends Activity {

    private ImageView goback1;

    // 名人组列表页面

    // famousmanmatchAdapter famousmanmatchAdapter;

    // ListView listView;

    // 定义List集合容器

    famousmanmatchAdapter famousmanmatchAdapter;

    // hotstockAdapter hotstockAdapter;

    essencematchAdapter essencematchAdapter;

    // 定义于数据库同步的字段集合
    // private String[] name;
    ArrayList<HashMap<String, Object>> listfamousmanmatchData;
    // ArrayList<HashMap<String,Object>> listhotstockData;
    ArrayList<HashMap<String, Object>> listessencematchData;

    // 获取我是否已关注用户的标识

    // 名人集合
    ListView famousmanlistview;
    // 精英集合
    ListView essencelistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);

    }

}
