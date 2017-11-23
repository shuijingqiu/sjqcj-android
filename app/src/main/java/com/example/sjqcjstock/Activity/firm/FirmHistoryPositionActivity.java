package com.example.sjqcjstock.Activity.firm;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.firm.FirmPositionAdapter;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.firm.FirmCodeEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 更多历史持仓
 * Created by Administrator on 2017/9/29.
 */
public class FirmHistoryPositionActivity extends Activity {
    // 加载的ListView
    private ListView listView;
    // 显示最新操作的Adapter
    private FirmPositionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firm_history_position);
        findView();
    }

    private void findView() {
        String positionStr = getIntent().getStringExtra("positionStr");
        /**
         * 返回按钮的事件绑定
         */
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new FirmPositionAdapter(this);
        listView = (ListView) findViewById(
                R.id.list_view);
        listView.setAdapter(adapter);
        getData(positionStr);
    }

    private void getData(String positionStr) {
        try {
            JSONObject jsonObject = new JSONObject(positionStr);
            if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                return;
            }
            ArrayList<FirmCodeEntity> list = (ArrayList<FirmCodeEntity>) JSON.parseArray(jsonObject.getString("data"), FirmCodeEntity.class);
            adapter.setlistData(list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
