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
 * 成交查询的Adapter
 * Created by Administrator on 2016/8/11.
 */
public class DealAdapter extends BaseAdapter {

    // 加载用的数据
    private List<StocksInfo> listData;
    private Context context;

    public DealAdapter(Context context) {
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
            convertView = mInflater.inflate(R.layout.list_item_deal, null);
            holder = new ViewHolder();
            holder.nameCode = (TextView) convertView.findViewById(R.id.name_code_tv);
            holder.type = (TextView) convertView.findViewById(R.id.type_tv);
            holder.time = (TextView) convertView.findViewById(R.id.time_tv);
            holder.price = (TextView) convertView.findViewById(R.id.price_tv);
            holder.money = (TextView) convertView.findViewById(R.id.money_tv);
            holder.number = (TextView) convertView.findViewById(R.id.number_tv);
            holder.cost = (TextView) convertView.findViewById(R.id.cost_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nameCode.setText(listData.get(position).getName());
        // 没写完

        return convertView;
    }

    static class ViewHolder {
        TextView nameCode;
        TextView type;
        TextView time;
        // 交易价格
        TextView price;
        // 交易金额
        TextView money;
        // 交易数量
        TextView number;
        // 交易费用
        TextView cost;
    }
}
