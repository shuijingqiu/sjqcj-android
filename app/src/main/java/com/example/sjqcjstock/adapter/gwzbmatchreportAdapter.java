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

/**
 * 股王争霸赛赛程报道控制器
 */
public class gwzbmatchreportAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, Object>> listData;

    public gwzbmatchreportAdapter(Context context) {
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
            convertView = mInflater.inflate(R.layout.list_item_gwzbmatchreport, null);
            holder = new ViewHolder();
            holder.course_titlestr = (TextView) convertView.findViewById(R.id.course_titlestr);
            holder.create_time1 = (TextView) convertView.findViewById(R.id.create_timestr);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.course_titlestr.setText((String) listData.get(position).get("starcraft_title"));
        holder.create_time1.setText((String) listData.get(position).get("create_time"));
        return convertView;
    }

    static class ViewHolder {
        TextView course_titlestr;
        TextView create_time1;
    }
}
