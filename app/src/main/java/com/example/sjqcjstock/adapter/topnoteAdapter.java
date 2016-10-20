package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.forumnotedetailActivity;
import com.example.sjqcjstock.R;

import java.util.ArrayList;
import java.util.HashMap;

public class topnoteAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, Object>> listData;

    //构造函数
    public topnoteAdapter(Context context, ArrayList<HashMap<String, Object>> listData) {
        this.context = context;
        this.listData = listData;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        //动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_topnote, null);
        }
        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setText((String) listData.get(position).get("title"));
        TextView create_time1 = (TextView) convertView.findViewById(R.id.create_time1);
        create_time1.setText((String) listData.get(position).get("ctimestr"));
        RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.RelativeLayout);
        relativeLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    Intent intent = new Intent(context.getApplicationContext(), forumnotedetailActivity.class);
                    intent.putExtra("weibo_id", (String) listData.get(position).get("feed_id"));
                    intent.putExtra("uid", (String) listData.get(position).get("uid"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return convertView;
    }
}
