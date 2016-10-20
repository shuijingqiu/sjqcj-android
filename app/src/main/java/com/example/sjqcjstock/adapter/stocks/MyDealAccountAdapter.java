package com.example.sjqcjstock.adapter.stocks;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.stocks.BusinessActivity;
import com.example.sjqcjstock.Activity.stocks.TransactionDetailActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.stocks.StocksInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 交易账户页面的持仓股票信息ListAdaper
 * Created by Administrator on 2016/8/15.
 */
public class MyDealAccountAdapter extends BaseAdapter {

    private List<StocksInfo> listData;
    private Context context;

    public MyDealAccountAdapter(Context context) {
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
            convertView = mInflater.inflate(R.layout.list_item_position, null);
            holder = new ViewHolder();
            holder.name_code = (TextView) convertView.findViewById(R.id.name_code_tv);
            holder.presentPrice = (TextView) convertView.findViewById(R.id.present_price_value_tv);
            holder.profit = (TextView) convertView.findViewById(R.id.profit_value_tv);
            holder.latestMarketPrice = (TextView) convertView.findViewById(R.id.latest_market_price_value_tv);
            holder.averageCost = (TextView) convertView.findViewById(R.id.average_cost_value_tv);
            holder.positionNumber = (TextView) convertView.findViewById(R.id.position_number_value_tv);
            holder.canBuyQuantity = (TextView) convertView.findViewById(R.id.can_buy_quantity_value_tv);
            holder.transactionDetail = (TextView) convertView.findViewById(R.id.transaction_detail_tv);
            holder.purchase = (TextView) convertView.findViewById(R.id.purchase_tv);
            holder.sellOut = (TextView) convertView.findViewById(R.id.sell_out_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name_code.setText(listData.get(position).getName() + "  " + listData.get(position).getCode());

        holder.transactionDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TransactionDetailActivity.class);
                context.startActivity(intent);
            }
        });
        holder.purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BusinessActivity.class);
                intent.putExtra("type", "0");
                intent.putExtra("code", "000001");
                context.startActivity(intent);
            }
        });
        holder.sellOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BusinessActivity.class);
                intent.putExtra("type", "1");
                intent.putExtra("code", "000001");
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView name_code;
        // 现价
        TextView presentPrice;
        // 收益
        TextView profit;
        // 最新市价
        TextView latestMarketPrice;
        // 平均成本
        TextView averageCost;
        // 持仓数量
        TextView positionNumber;
        // 可买数量
        TextView canBuyQuantity;
        // 成交明细
        TextView transactionDetail;
        // 可买数量
        TextView purchase;
        // 可买数量
        TextView sellOut;
    }
}
