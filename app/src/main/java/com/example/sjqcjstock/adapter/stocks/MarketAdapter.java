package com.example.sjqcjstock.adapter.stocks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.stocks.StocksInfo;
import com.example.sjqcjstock.netutil.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 行情页面的炒股信息ListAdaper
 * Created by Administrator on 2016/8/5.
 */
public class MarketAdapter extends BaseAdapter {

    private List<StocksInfo> listData;
    private Context context;

    public MarketAdapter(Context context) {
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
            convertView = mInflater.inflate(R.layout.list_item_shares_market, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.shares_name);
            holder.code = (TextView) convertView.findViewById(R.id.shares_code);
            holder.spotPrice = (TextView) convertView.findViewById(R.id.shares_price);
            holder.highsLows = (TextView) convertView.findViewById(R.id.shares_rose);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(listData.get(position).getName());
        holder.code.setText(listData.get(position).getCode());
        holder.spotPrice.setText(listData.get(position).getSpotPrice());
        ViewUtil.setViewColor(holder.highsLows, listData.get(position).getHighsLows());

        return convertView;
    }

    static class ViewHolder {
        TextView name;
        TextView code;
        TextView spotPrice;
        TextView highsLows;
    }
}
