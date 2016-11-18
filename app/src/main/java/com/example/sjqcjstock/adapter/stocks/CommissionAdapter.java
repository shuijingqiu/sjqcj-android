package com.example.sjqcjstock.adapter.stocks;

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
 * 委托查询的Adapter
 * Created by Administrator on 2016/8/11.
 */
public class CommissionAdapter extends BaseAdapter {

    private List<Order> listData;
    private Context context;
    // 订单实体类
    private Order order;

    public CommissionAdapter(Context context) {
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
            convertView = mInflater.inflate(R.layout.list_item_commission, null);
            holder = new ViewHolder();
            holder.nameCode = (TextView) convertView.findViewById(R.id.name_code_tv);
            holder.type = (TextView) convertView.findViewById(R.id.type_tv);
            holder.time = (TextView) convertView.findViewById(R.id.time_tv);
            holder.price = (TextView) convertView.findViewById(R.id.price_tv);
            holder.commissionNumber = (TextView) convertView.findViewById(R.id.commission_number_tv);
            holder.number = (TextView) convertView.findViewById(R.id.number_tv);
            holder.state = (TextView) convertView.findViewById(R.id.state_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        order = listData.get(position);
        holder.nameCode.setText(order.getStock_name()+"("+order.getStock()+")");
        holder.commissionNumber.setText(order.getNumber());
        holder.number.setText(order.getNumber());
        holder.price.setText(order.getPrice());
        holder.time.setText(order.getTime());

        // 1代表买入2代表卖出
        if("1".equals(order.getType())){
            holder.type.setText("买入");
            holder.type.setBackgroundColor(holder.type.getResources().getColor(R.color.color_ef3e3e));
        }else{
            holder.type.setText("卖出");
            holder.type.setBackgroundColor(holder.type.getResources().getColor(R.color.color_5471ef));
        }

        //0代表待成交 1代表成交  2代表撤单
        String status = order.getStatus();
        if ("0".equals(status)) {
            holder.state.setText("已报");
        }else if("2".equals(status)){
            holder.state.setText("撤单");
        }


        return convertView;
    }

    static class ViewHolder {
        TextView nameCode;
        TextView type;
        TextView time;
        // 交易价格
        TextView price;
        // 成交数量
        TextView number;
        // 委托数量
        TextView commissionNumber;
        // 交易状态
        TextView state;
    }
}
