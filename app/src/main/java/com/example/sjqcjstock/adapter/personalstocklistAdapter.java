package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.Article.RaceReportEntity;
import com.example.sjqcjstock.netutil.CalendarUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.netutil.ViewUtil;

import java.util.ArrayList;

/**
 * (大盘，个股，名家，内参，论道)的控制器
 */
public class personalstocklistAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<RaceReportEntity> listData;


    public personalstocklistAdapter(Context context) {
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
            convertView = mInflater.inflate(R.layout.list_item_personalstock, null);
            holder = new ViewHolder();
            holder.titleStr = (TextView) convertView.findViewById(R.id.title1);
            holder.userName = (TextView) convertView.findViewById(R.id.uname1);
            holder.time = (TextView) convertView.findViewById(R.id.create_time1);
            holder.count = (TextView) convertView.findViewById(R.id.comment_count1);
            holder.vipImg = (ImageView) convertView.findViewById(R.id.vip_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RaceReportEntity raceReportEntity = listData.get(position);
        holder.titleStr.setText(raceReportEntity.getTitle());
        holder.userName.setText(raceReportEntity.getUname());
        holder.time.setText(CalendarUtil.formatDateTime(Utils
                .getStringtoDate(raceReportEntity.getCtime())));
        holder.count.setText(raceReportEntity.getComment_count());

        ViewUtil.setUpVipNew(raceReportEntity.getUser_group_icon_url(), holder.vipImg);

        return convertView;
    }
    public static class ViewHolder {
        ImageView image;
        View view;
        TextView titleStr;
        TextView userName;
        TextView time;
        TextView count;
        ImageView vipImg;
    }
}
