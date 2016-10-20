package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.netutil.ViewUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * (大盘，个股，名家，内参，论道)的控制器
 */
public class personalstocklistAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, Object>> listData;


    public personalstocklistAdapter(Context context) {
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
        //动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_personalstock, null);
        }
        TextView informtitle1 = (TextView) convertView.findViewById(R.id.title1);
        informtitle1.setText((String) listData.get(position).get("weibo_titlestr"));

        TextView uname1 = (TextView) convertView.findViewById(R.id.uname1);
        uname1.setText((String) listData.get(position).get("username"));

        TextView create_time1 = (TextView) convertView.findViewById(R.id.create_time1);
        create_time1.setText((String) listData.get(position).get("ctimestr"));

        TextView comment_count1 = (TextView) convertView.findViewById(R.id.comment_count1);
        comment_count1.setText((String) listData.get(position).get("comment_countstr"));

        ImageView vipImg = (ImageView) convertView.findViewById(R.id.vip_img);
        String isVip = listData.get(position).get(
                "isVip") + "";
        ViewUtil.setUpVip(isVip, vipImg);

        return convertView;
    }

}
