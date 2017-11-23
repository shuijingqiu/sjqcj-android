package com.example.sjqcjstock.adapter.tomlive;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.Tomlive.SpinnerData;

import java.util.ArrayList;

/**
 * 价格选择的List
 * Created by Administrator on 2017/1/18.
 */
public class PriceAdapter  extends BaseAdapter {

    private Context context;
    private ArrayList<SpinnerData> listData;
    // 被选中的Item
    private String timeOk ="";

    public PriceAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<SpinnerData> listData) {
        if (listData != null) {
            this.listData = (ArrayList<SpinnerData>) listData.clone();
            notifyDataSetChanged();
        }
    }

    // 选择Item重新加载
    public void setItem(String timeOk){
        this.timeOk = timeOk;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (listData == null){
            return 0;
        }
        return listData.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        //动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_price, null);
            holder = new ViewHolder();
            holder.payRechargeIv = (ImageView) convertView.findViewById(R.id.pay_recharge_iv);
            holder.priceTv = (TextView) convertView.findViewById(R.id.price_tv);
            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SpinnerData spinnerData = listData.get(position);
        String time = spinnerData.getTime();
        holder.timeTv.setText(time+"个月");
        holder.priceTv.setText("("+spinnerData.getPrice()+"水晶币)");

        if (timeOk.equals(time)){
            holder.payRechargeIv.setVisibility(View.VISIBLE);
        }else{
            holder.payRechargeIv.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView payRechargeIv;
        TextView timeTv;
        TextView priceTv;
    }

}
