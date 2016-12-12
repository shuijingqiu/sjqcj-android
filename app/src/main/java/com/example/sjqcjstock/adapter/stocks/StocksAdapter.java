package com.example.sjqcjstock.adapter.stocks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.stocks.Order;
import com.example.sjqcjstock.netutil.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 加载股票的Adapter
 * Created by Administrator on 2016/11/30.
 */
public class StocksAdapter extends BaseAdapter {

    // 加载用的数据
    private ArrayList<HashMap<String, Object>> listStocks;
    private Context context;

    public StocksAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<HashMap<String, Object>> listStocks) {
        if (listStocks != null) {
            this.listStocks = (ArrayList<HashMap<String, Object>>) listStocks.clone();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return listStocks == null ? 0 : listStocks.size();
    }

    @Override
    public Object getItem(int position) {
        return listStocks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_stocks, null);
            holder = new ViewHolder();
            holder.stocksTv = (TextView) convertView.findViewById(R.id.stocks_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.stocksTv.setText(listStocks.get(position).get("name") + "（" + listStocks.get(position).get("code") + ")");
        return convertView;
    }

    static class ViewHolder {
        TextView stocksTv;
    }
}