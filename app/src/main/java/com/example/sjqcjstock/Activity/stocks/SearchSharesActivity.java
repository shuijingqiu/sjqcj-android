package com.example.sjqcjstock.Activity.stocks;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.stocks.StocksAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.example.sjqcjstock.view.CustomToast;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * 股票代码的检索
 */
public class SearchSharesActivity extends Activity {

    // 获取控件
    private EditText et_searchFriend;
    private ListView stosksList;
    // 保存搜索的股票
    private ArrayList<HashMap<String, Object>> listStocks;
    // 加载股票的adapter
    private StocksAdapter stocksAdapter;
    // 股票代码
    private String code;
    // 跳转地方
    private String jumpType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search_shares);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
        code = getIntent().getStringExtra("code");
        jumpType = getIntent().getStringExtra("jumpType");
        if (code !=null){
            et_searchFriend.setText(code);
        }
    }

    private void initView() {
        findViewById(R.id.goback1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        stosksList = (ListView) findViewById(R.id.stocks_list);
        et_searchFriend = (EditText) findViewById(R.id.et_searchFriend);
        // 监听股票代码输入状态
        et_searchFriend.addTextChangedListener(watcher);

        stosksList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ("1".equals(jumpType)){
                    Intent intent = new Intent(SearchSharesActivity.this, SharesDetailedActivity.class);
                    intent.putExtra("name", listStocks.get(position).get("name")+"");
                    intent.putExtra("code", listStocks.get(position).get("code")+"");
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(SearchSharesActivity.this, BusinessActivity.class);
                    intent.putExtra("type", "1");
                    intent.putExtra("code", listStocks.get(position).get("code")+"");
                    startActivity(intent);
                    finish();
                }

            }
        });
        // 存储数据的数组列表
        listStocks = new ArrayList<HashMap<String, Object>>(200);
        stocksAdapter = new StocksAdapter(SearchSharesActivity.this);
        stosksList.setAdapter(stocksAdapter);
    }

    private class SearchStocks extends AsyncTask<TaskParams, Void, String>{

        @Override
        protected String doInBackground(TaskParams... params) {
            TaskParams tp = params[0];
            return HttpUtil.getIntentData(tp.getUrl());
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if ("".equals(result)){
                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG)
                        .show();
                return;
            }
            if (result.length() < 30){
                return;
            }
            result = result.substring(result.indexOf("\"")+1);
            String[] strs = result.split(";");
            HashMap<String, Object> map3;
            listStocks.clear();
            for (String str:strs){
                map3 = new HashMap<String, Object>();
                String[] strlist = str.split(",");
                map3.put("code",strlist[2]);
                map3.put("name",strlist[4]);
                listStocks.add(map3);
            }
            stocksAdapter.setlistData(listStocks);
            ViewUtil.setListViewHeightBasedOnChildren(stosksList);
        }
    }


    /**
     * 监听股票代码的输入状态
     */
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            code = s.toString();
            // 开线程调用股票查询接口
            new SearchStocks().execute(new TaskParams("http://suggest3.sinajs.cn/suggest/type=111&key="+code));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
