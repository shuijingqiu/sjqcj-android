package com.example.sjqcjstock.fragment.stocks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.stocks.MyDealAccountAdapter;
import com.example.sjqcjstock.entity.stocks.StocksInfo;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.example.sjqcjstock.userdefined.MyScrollView;
import com.example.sjqcjstock.view.SoListView;

import java.util.ArrayList;

/**
 * 股票交易列表
 * Created by Administrator on 2016/8/19.
 */
public class FragmentTransactionList extends Fragment {


    // 股票交易的List
    private SoListView listView;
    // 交易的行情List的Adapter
    private MyDealAccountAdapter listAdapter;
    // 滚动控件
    private MyScrollView myScrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        findView(view);
        initData();
        return view;
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        // 滚动到顶部
//        myScrollView.scrollTo(0, 0);
//        myScrollView.smoothScrollTo(0, 0);
//    }

    private void findView(View view) {

        myScrollView = (MyScrollView) view.findViewById(R.id.myScrollView);
        listAdapter = new MyDealAccountAdapter(getActivity());
        listView = (SoListView) view.findViewById(
                R.id.list_view);
        listView.setAdapter(listAdapter);

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
        listAdapter.setlistData(listStocks);

        ViewUtil.setListViewHeightBasedOnChildren(listView);
        // 滚动到顶部
        myScrollView.scrollTo(0, 0);
//        myScrollView.smoothScrollTo(0, 0);
    }
}
