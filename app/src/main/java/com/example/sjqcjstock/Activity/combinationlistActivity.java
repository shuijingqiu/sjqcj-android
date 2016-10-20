package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.combinationAdapter;
import com.example.sjqcjstock.app.ExitApplication;

import java.util.ArrayList;
import java.util.HashMap;

public class combinationlistActivity extends Activity {
    //定义List集合容器
    private combinationAdapter combinationAdapter;
    //定义于数据库同步的字段集合
    //private String[] name;
    private ArrayList<HashMap<String, Object>> listcombinationData;
    /**
     * 普通帖集合
     */
    private ListView combinationlistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.combination_list);

        //将Activity反复链表
        ExitApplication.getInstance().addActivity(this);

        initView();

    }

    private void initView() {
        // TODO Auto-generated method stub
        /**投资组合集合*/
        combinationlistview = (ListView) findViewById(R.id.combinationlist2);
        /**组合集合*/
        //selfselectstocklistview=(ListView)findViewById(R.id.notelist1);


        //存储数据的数组列表
        listcombinationData = new ArrayList<HashMap<String, Object>>();


        for (int i = 0; i < 20; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();

            //添加数据
            listcombinationData.add(map);
        }

        //为ListView 添加适配器

        combinationAdapter = new combinationAdapter(combinationlistActivity.this, listcombinationData);

        combinationlistview.setAdapter(combinationAdapter);
    }

}
