package com.example.sjqcjstock.fragment.stocks;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.Activity.stocks.SharesDetailedActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.stocks.StockAdapter;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.stocks.OptionalEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.netutil.sharesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * 自选股列表页面 不要的
 * Created by Administrator on 2016/8/19.
 */
public class FragmentStock extends Fragment {

    // 自选股的List
    private ListView stockListView;
    // 自选股的List Adapter
    private StockAdapter stockAdapter;
    // 调用自选股的数据
    private String resstr = "";
    // 自选股股票最新信息的Map
    private Map<String,Map> mapZxgStr;
    // 自选股的的数据
    private ArrayList<OptionalEntity> optionalArrayList = null;
    // 查看的用户ID
    private String uidstr = "";

    public FragmentStock(String uidstr){
        this.uidstr = uidstr;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock, container, false);
        findView(view);
        initData();
        return view;
    }

    /**
     * 控件的绑定
     *
     * @param view
     */
    private void findView(View view) {
        stockAdapter = new StockAdapter(getActivity());
        stockListView = (ListView) view.findViewById(
                R.id.stock_list);
        stockListView.setAdapter(stockAdapter);
        stockListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent inten = new Intent();
                inten.putExtra("name", optionalArrayList.get(position).getStock_name());
                inten.putExtra("code", optionalArrayList.get(position).getStock());
                inten.setClass(getActivity(), SharesDetailedActivity.class);
                startActivity(inten);
            }
        });
    }

    /**
     * 数据的获取
     */
    private void initData() {
        // 开线程获取用户获取自选股
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用接口获取用户获取自选股
                resstr = HttpUtil.restHttpGet(Constants.moUrl+"/user/getUserOptional&uid="+uidstr+"&token="+Constants.apptoken);
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
                    try {
                        JSONObject jsonObject = new JSONObject(resstr);
                        if ("failed".equals(jsonObject.getString("status"))){
//                            Toast.makeText(getActivity(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // 自选股的数据
                        ArrayList<OptionalEntity> optionalList = (ArrayList<OptionalEntity>) JSON.parseArray(jsonObject.getString("data"),OptionalEntity.class);
                        getoptionalData(optionalList);
                        optionalArrayList = optionalList;
                        // 加载自选股股票信息
                        stockAdapter.setlistData(optionalList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    // 重新加载自选股列表
                    for(int i = 0;i < optionalArrayList.size();i++){
                        Map<String,String> mStr = mapZxgStr.get(optionalArrayList.get(i).getStock());
                        if (mStr!=null && mStr.size()>0) {
                            optionalArrayList.get(i).setPrice(mStr.get("price"));
                            optionalArrayList.get(i).setRising(mStr.get("rising"));
                            optionalArrayList.get(i).setIstype(mStr.get("type"));
                        }
                    }
                    // 刷新自选股列表
                    stockAdapter.setlistData(optionalArrayList);
                    break;
            }
        }
    };

    /**
     * 获取自选股股票实时数据进行处理
     * @param listOptional
     */
    private void getoptionalData(ArrayList<OptionalEntity>  listOptional){
        String str = "";
        for (OptionalEntity optionalEntity:listOptional){
            if (!"".equals(str)){
                str += ",";
            }
            str += Utils.judgeSharesCode(optionalEntity.getStock());
        }
        // 开线程获股票当前信息
        final String finalStr = str;
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用接口获取股票当前行情数据（返回股票代码和最新价格的map）
                mapZxgStr = sharesUtil.getsharess(finalStr);
                // 重新加载自选股数据
                handler.sendEmptyMessage(1);
            }
        }).start();
    }
}
