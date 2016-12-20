package com.example.sjqcjstock.adapter.stocks;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.stocks.SharesDetailedActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.stocks.PositionEntity;
import com.example.sjqcjstock.netutil.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 历史持仓的Adapter
 * Created by Administrator on 2016/8/11.
 */
public class PositionAdapter extends BaseAdapter {

    // 加载用的数据
    private List<PositionEntity> listData;
    private PositionEntity positionEntity;
    private Context context;

    public PositionAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<PositionEntity> listData) {
        if (listData != null) {
            this.listData = (List<PositionEntity>) listData.clone();
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
            convertView = mInflater.inflate(R.layout.list_item_history_position, null);
            holder = new ViewHolder();
            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.stockNameTv = (TextView) convertView.findViewById(R.id.stock_name_tv);
            holder.ratioTv = (TextView) convertView.findViewById(R.id.ratio_tv);
            holder.sharesTv = (TextView) convertView.findViewById(R.id.shares_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        positionEntity = listData.get(position);
        String time = positionEntity.getTime().replace("-",".").substring(5,10);
        String updateTime = positionEntity.getUpdate_time().replace("-",".").substring(5,10);
        holder.timeTv.setText(time+"-"+updateTime);
        holder.stockNameTv.setText(positionEntity.getStock_name());
        double ration = Double.valueOf(positionEntity.getRatio());
        double assets = Double.valueOf(positionEntity.getAssets());
        double profit = ration * assets/100;
        if (ration >= 0){
            holder.ratioTv.setTextColor(holder.ratioTv.getResources().getColor(R.color.color_ef3e3e));
            holder.ratioTv.setText("+"+Utils.getNumberFormat2(ration+"")+"%");
        }else{
            holder.ratioTv.setTextColor(holder.ratioTv.getResources().getColor(R.color.color_1bc07d));
            holder.ratioTv.setText(Utils.getNumberFormat2(ration+"")+"%");
        }
        if (profit >= 0){
            holder.sharesTv.setTextColor(holder.ratioTv.getResources().getColor(R.color.color_ef3e3e));
            holder.sharesTv.setText("+"+Utils.getNumberFormat2(profit+"")+"元");
        }else{
            holder.sharesTv.setText(Utils.getNumberFormat2(profit+"")+"元");
            holder.sharesTv.setTextColor(holder.ratioTv.getResources().getColor(R.color.color_1bc07d));
        }
        holder.stockNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SharesDetailedActivity.class);
                intent.putExtra("code",positionEntity.getStock());
                intent.putExtra("name",positionEntity.getStock_name());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        // 交易时间
        TextView timeTv;
        // 股票名称
        TextView stockNameTv;
        // 收益比例
        TextView ratioTv;
        // 盈亏
        TextView sharesTv;
    }
}
