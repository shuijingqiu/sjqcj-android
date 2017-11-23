package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.sjqcjstock.Activity.firm.FirmHallActivity;
import com.example.sjqcjstock.Activity.stocks.SimulationGameActivity;
import com.example.sjqcjstock.R;

/**
 * 我的比赛选择页面
 * Created by Administrator on 2017/9/1.
 */
public class MyMatchActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_match);
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 模拟比赛
     * @param view
     */
    public void SimulationClick(View view){
        Intent intent = new Intent(MyMatchActivity.this, SimulationGameActivity.class);
        intent.putExtra("type","0");
        startActivity(intent);
    }

    /**
     * 实盘比赛17196072453
     * @param view
     */
    public void FirmClick(View view){
        Intent intent = new Intent(MyMatchActivity.this, FirmHallActivity.class);
        intent.putExtra("type","my");
        startActivity(intent);
    }

}
