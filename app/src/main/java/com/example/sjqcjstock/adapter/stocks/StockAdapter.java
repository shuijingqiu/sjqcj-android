package com.example.sjqcjstock.adapter.stocks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.stocks.StocksInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 自选股的ListAdaper
 * Created by Administrator on 2016/8/15.
 */
public class StockAdapter extends BaseAdapter {

    private List<StocksInfo> listData;
    private Context context;

    public StockAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<StocksInfo> listData) {
        if (listData != null) {
            this.listData = (List<StocksInfo>) listData.clone();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return listData == null ? 0 : listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
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
            convertView = mInflater.inflate(R.layout.list_item_stock, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name_tv);
            holder.code = (TextView) convertView.findViewById(R.id.code_tv);
            holder.price = (TextView) convertView.findViewById(R.id.price_tv);
            holder.rose = (TextView) convertView.findViewById(R.id.rose_tv);
            holder.number = (TextView) convertView.findViewById(R.id.number_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(listData.get(position).getName());
        holder.code.setText(listData.get(position).getCode());

        return convertView;
    }

    static class ViewHolder {
        // 股票名称
        TextView name;
        // 股票代码
        TextView code;
        // 价格
        TextView price;
        // 涨幅
        TextView rose;
        // 推荐人数
        TextView number;
    }
}
