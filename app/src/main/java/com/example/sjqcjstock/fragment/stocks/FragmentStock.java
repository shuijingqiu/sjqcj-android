package com.example.sjqcjstock.fragment.stocks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.stocks.StockAdapter;
import com.example.sjqcjstock.entity.stocks.StocksInfo;

import java.util.ArrayList;

/**
 * 自选股列表页面
 * Created by Administrator on 2016/8/19.
 */
public class FragmentStock extends Fragment {

    // 自选股的List
    private ListView stockListView;
    // 自选股的List Adapter
    private StockAdapter stockAdapter;
    // 需要加载自选股的行情数据
    private ArrayList<StocksInfo> listInfo;

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

    }

    /**
     * 数据的获取
     */
    private void initData() {

        ArrayList<StocksInfo> listStocks = new ArrayList<StocksInfo>();
        StocksInfo stocks = new StocksInfo();
        stocks.setName("测试用的1");
        listStocks.add(stocks);
        stocks = new StocksInfo();
        stocks.setName("测试用的2");
        listStocks.add(stocks);
        stocks = new StocksInfo();
        stocks.setName("测试用的3");
        listStocks.add(stocks);
        stocks = new StocksInfo();
        stocks.setName("测试用的4");
        listStocks.add(stocks);
        stocks = new StocksInfo();
        stocks.setName("测试用的5");
        listStocks.add(stocks);
        stocks = new StocksInfo();
        stocks.setName("测试用的6");
        listStocks.add(stocks);
        stocks = new StocksInfo();
        stocks.setName("测试用的7");
        listStocks.add(stocks);
        stocks = new StocksInfo();
        stocks.setName("测试用的8");
        listStocks.add(stocks);
        stocks = new StocksInfo();
        stocks.setName("测试用的9");
        listStocks.add(stocks);
        stocks = new StocksInfo();
        stocks.setName("测试用的10");
        listStocks.add(stocks);
        stocks = new StocksInfo();
        stocks.setName("测试用的11");
        listStocks.add(stocks);
        stockAdapter.setlistData(listStocks);

    }
}
