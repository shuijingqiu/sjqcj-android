package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.weekrankingnewdetailActivity;
import com.example.sjqcjstock.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 */
public class weekrankingdirAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, Object>> listData;


    public weekrankingdirAdapter(Context context, ArrayList<HashMap<String, Object>> listData) {
        super();
        this.context = context;
        this.listData = listData;
    }

    public void setlistData(ArrayList<HashMap<String, Object>> listData) {
        this.listData = listData;
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
            convertView = mInflater.inflate(R.layout.list_item_weekrankingdir, null);
            holder = new ViewHolder();
            holder.weekly = (TextView) convertView.findViewById(R.id.weekly);
            holder.pickfamousweekscore1 = (LinearLayout) convertView.findViewById(R.id.pickfamousweekscore1);
            holder.picksesenceweekscore1 = (LinearLayout) convertView.findViewById(R.id.picksesenceweekscore1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.weekly.setText((String) listData.get(position).get("weekly"));
        holder.pickfamousweekscore1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    Intent intent = new Intent(context.getApplicationContext(), weekrankingnewdetailActivity.class);
                    intent.putExtra("match", (String) listData.get(position).get("weekly"));
                    intent.putExtra("type", "1");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        holder.picksesenceweekscore1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    Intent intent = new Intent(context.getApplicationContext(), weekrankingnewdetailActivity.class);
                    intent.putExtra("match", (String) listData.get(position).get("weekly"));
                    intent.putExtra("type", "2");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return convertView;
    }

    public static class ViewHolder {
        TextView weekly;
        LinearLayout picksesenceweekscore1;
        LinearLayout pickfamousweekscore1;
    }
}
