package com.example.sjqcjstock.adapter.firm;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.stocks.SharesDetailedActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.firm.FirmCodeEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 最新持仓的列表
 * Created by Administrator on 2017/9/28.
 */
public class FirmPositionAdapter extends BaseAdapter {

    // 加载用的数据
    private List<FirmCodeEntity> listData;
    private FirmCodeEntity firmCodeEntity;
    private Context context;
    private int count;

    public FirmPositionAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<FirmCodeEntity> listData) {
        if (listData != null) {
            this.listData = (List<FirmCodeEntity>) listData.clone();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_firm_hall_position, null);
            holder = new ViewHolder();
            holder.nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.ratioTv = (TextView) convertView.findViewById(R.id.ratio_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        firmCodeEntity = listData.get(position);
        holder.nameTv.setText(firmCodeEntity.getName()+"("+firmCodeEntity.getCode()+")");
        String rationStr = firmCodeEntity.getRatio()+"";
        if (Double.valueOf(rationStr) == 0d) {
            holder.ratioTv.setTextColor(holder.ratioTv.getResources().getColor(R.color.color_article));
        } else if (rationStr.indexOf("-") == -1){
            holder.ratioTv.setTextColor(holder.ratioTv.getResources().getColor(R.color.color_ef3e3e));
        }else{
            holder.ratioTv.setTextColor(holder.ratioTv.getResources().getColor(R.color.color_1bc07d));
        }
        holder.ratioTv.setText(rationStr+"%");

        holder.nameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SharesDetailedActivity.class);
                intent.putExtra("code",listData.get(position).getCode());
                intent.putExtra("name",listData.get(position).getName());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        // 股票名称代码
        TextView nameTv;
        // 收益率
        TextView ratioTv;
    }
}