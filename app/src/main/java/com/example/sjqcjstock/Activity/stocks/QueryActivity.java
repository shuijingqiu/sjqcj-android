package com.example.sjqcjstock.Activity.stocks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;

/**
 * 模拟盘交易查询
 * Created by Administrator on 2016/8/10.
 */
public class QueryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_query);
        ExitApplication.getInstance().addActivity(this);
        findView();
    }

    /**
     * 控件的绑定
     */
    private void findView() {
        /**
         * 返回按钮的事件绑定
         */
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 当日交易
     *
     * @param view
     */
    public void dayDealClick(View view) {
        startActivity(new Intent(this, DayDealActivity.class));
    }

    /**
     * 当日委托
     *
     * @param view
     */
    public void dayCommissionClick(View view) {
        startActivity(new Intent(this, DayCommissionActivity.class));
    }

    /**
     * 历史交易
     *
     * @param view
     */
    public void historyDealClick(View view) {
        startActivity(new Intent(this, HistoryDealActivity.class));
    }

    /**
     * 历史委托
     *
     * @param view
     */
    public void historyCommissionClick(View view) {
        startActivity(new Intent(this, HistoryCommissionActivity.class));
    }

}
