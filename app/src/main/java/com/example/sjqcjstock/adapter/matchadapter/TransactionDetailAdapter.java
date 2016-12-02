package com.example.sjqcjstock.adapter.matchadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.stocks.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * 当前持仓成交明细的Adapter
 * Created by Administrator on 2016/8/16.
 */
public class TransactionDetailAdapter extends BaseAdapter {

    // 加载用的数据
    private List<Order> listData;
    private Context context;

    public TransactionDetailAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<Order> listData) {
        if (listData != null) {
            this.listData = (List<Order>) listData.clone();
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
            convertView = mInflater.inflate(R.layout.list_item_transaction_detail, null);
            holder = new ViewHolder();
            holder.time = (TextView) convertView.findViewById(R.id.time_tv);
            holder.businessType = (TextView) convertView.findViewById(R.id.business_type_tv);
            holder.transactionPrice = (TextView) convertView.findViewById(R.id.transaction_price_value_tv);
            holder.transactionsNumber = (TextView) convertView.findViewById(R.id.transactions_number_value_tv);
            holder.transactionAmount = (TextView) convertView.findViewById(R.id.transaction_amount_value_tv);
            holder.transactionCost = (TextView) convertView.findViewById(R.id.transaction_cost_value_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.time.setText(listData.get(position).getTime());
        holder.transactionPrice.setText(listData.get(position).getPrice());
        holder.transactionsNumber.setText(listData.get(position).getNumber());
        holder.transactionAmount.setText(listData.get(position).getNumber());


        return convertView;
    }

    static class ViewHolder {
        // 买卖时间
        TextView time;
        // 交易类型
        TextView businessType;
        // 成交价格
        TextView transactionPrice;
        // 成交数量
        TextView transactionsNumber;
        // 成交金额
        TextView transactionAmount;
        // 交易费用
        TextView transactionCost;
    }
}
