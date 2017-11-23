package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.InformationEntity;
import com.example.sjqcjstock.netutil.CalendarUtil;
import com.example.sjqcjstock.netutil.Utils;

import java.util.ArrayList;

public class InformAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<InformationEntity> listData;

    public InformAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<InformationEntity> listData) {
        if (this.listData != null)
            this.listData.clear();
        this.listData = (ArrayList<InformationEntity>) listData.clone();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        //return imagesUrl.length;
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
        //动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_inform, null);
            holder = new ViewHolder();
            holder.informtitle1 = (TextView) convertView.findViewById(R.id.informtitle1);
            holder.create_time1 = (TextView) convertView.findViewById(R.id.create_time1);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        InformationEntity informationEntity = listData.get(position);
        holder.informtitle1.setText(informationEntity.getNews_title());
        holder.create_time1.setText(CalendarUtil.formatDateTime(Utils.getStringtoDate(informationEntity.getCreated())));

        return convertView;
    }

    static class ViewHolder {
        TextView informtitle1;
        TextView create_time1;
    }
}
