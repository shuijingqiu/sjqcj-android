package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.NoticeEntity;

import java.util.ArrayList;

/**
 * 实时战绩的adapter
 * Created by Administrator on 2017/05/02.
 */
public class realTimeRecordAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NoticeEntity> listData;

    public realTimeRecordAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<NoticeEntity> listData) {
        if (listData != null) {
            this.listData = (ArrayList<NoticeEntity>) listData.clone();
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
        // TODO Auto-generated method stub
        //动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_real_time_record, null);
            holder = new ViewHolder();
            holder.subjectTv = (TextView) convertView.findViewById(R.id.subject_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.subjectTv.setText(listData.get(position).getTitle());
        return convertView;
    }

    static class ViewHolder {
        TextView subjectTv;
    }
}
