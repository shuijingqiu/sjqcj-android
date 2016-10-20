package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.sjqcjstock.R;

import java.util.ArrayList;
import java.util.HashMap;

public class facelibaryAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<HashMap<String, Object>> listData;

    // 构造函数
    public facelibaryAdapter(Context context, ArrayList<HashMap<String, Object>> listData) {
        this.context = context;
        this.listData = listData;
    }

    public void setlistData(ArrayList<HashMap<String, Object>> listData) {
        this.listData = listData;
    }

    @Override
    public int getCount() {
        // return imagesUrl.length;
        return listData.size();
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
        // 动态加载布局
        convertView = LayoutInflater.from(context).inflate(R.layout.facelibary_item, null);
        ImageView image = (ImageView) convertView.findViewById(R.id.faceimg);
        image.setBackgroundResource((Integer) listData.get(position).get("friend_image"));
        return convertView;
    }

}
