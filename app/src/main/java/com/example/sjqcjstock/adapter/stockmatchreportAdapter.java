package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.Article.RaceReportEntity;
import com.example.sjqcjstock.netutil.CalendarUtil;
import com.example.sjqcjstock.netutil.Utils;

import java.util.ArrayList;

/**
 * 赛程报道的adapter
 */
public class stockmatchreportAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<RaceReportEntity> listData;

    public stockmatchreportAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<RaceReportEntity> listData) {
        if (listData != null) {
            this.listData = (ArrayList<RaceReportEntity>) listData.clone();
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
        RaceReportEntity raceReportEntity = listData.get(position);
        holder.course_titlestr.setText(raceReportEntity.getTitle());
        holder.create_timestr.setText(CalendarUtil.formatDateTime(Utils.getStringtoDate(raceReportEntity.getTime())));
        return convertView;
    }

    static class ViewHolder {
        TextView course_titlestr;
        TextView create_timestr;
    }
}
