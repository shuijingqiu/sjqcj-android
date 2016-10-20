package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sjqcjstock.R;

import java.util.ArrayList;
import java.util.HashMap;

public class stockmatchreportAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, Object>> listData;

    public stockmatchreportAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<HashMap<String, Object>> listData) {
        if (listData != null) {
            this.listData = (ArrayList<HashMap<String, Object>>) listData.clone();
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
            convertView = mInflater.inflate(R.layout.list_item_stockmatchreport, null);
            holder = new ViewHolder();
            holder.course_titlestr = (TextView) convertView.findViewById(R.id.course_titlestr);
            holder.create_timestr = (TextView) convertView.findViewById(R.id.create_timestr);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.course_titlestr.setText((String) listData.get(position).get("course_titlestr"));
        holder.create_timestr.setText((String) listData.get(position).get("create_timestr"));
        return convertView;
    }

    static class ViewHolder {
        TextView course_titlestr;
        TextView create_timestr;
    }
}
