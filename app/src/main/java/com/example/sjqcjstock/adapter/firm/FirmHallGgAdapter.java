package com.example.sjqcjstock.adapter.firm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.firm.FirmCodeEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 个股显示的adapter
 * Created by Administrator on 2017/8/28.
 */
public class FirmHallGgAdapter extends BaseAdapter {

    // 加载用的数据
    private List<FirmCodeEntity> listData;
    private FirmCodeEntity firmCodeEntity;
    private Context context;

    public FirmHallGgAdapter(Context context) {
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
            convertView = mInflater.inflate(R.layout.list_item_firm_hall_gg, null);
            holder = new ViewHolder();
            holder.nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.codeTv = (TextView) convertView.findViewById(R.id.code_tv);
            holder.ratioTv = (TextView) convertView.findViewById(R.id.ratio_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        firmCodeEntity = listData.get(position);
        holder.codeTv.setText(firmCodeEntity.getCode());
        holder.nameTv.setText(firmCodeEntity.getName());
        String rationStr = firmCodeEntity.getRatio()+"";

        if (Double.valueOf(rationStr) == 0d){
            holder.ratioTv.setTextColor(holder.ratioTv.getResources().getColor(R.color.color_article));
        }else if (rationStr.indexOf("-") == -1){
            holder.ratioTv.setTextColor(holder.ratioTv.getResources().getColor(R.color.color_ef3e3e));
        }else{
            holder.ratioTv.setTextColor(holder.ratioTv.getResources().getColor(R.color.color_1bc07d));
        }
        holder.ratioTv.setText(rationStr+"%");

        return convertView;
    }

    static class ViewHolder {
        // 股票名称
        TextView nameTv;
        // 股票代码
        TextView codeTv;
        // 收益率
        TextView ratioTv;
    }
}