package com.example.sjqcjstock.adapter.matchadapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class totalrankingfamousAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, Object>> listData;


    public totalrankingfamousAdapter(Context context) {
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
            convertView = mInflater.inflate(R.layout.list_item_totalrankingfamous, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.user_image1);
            holder.famousmanname1 = (TextView) convertView.findViewById(R.id.famousmanname1);
            holder.totalscore1 = (TextView) convertView.findViewById(R.id.totalscore1);
            holder.weekly = (TextView) convertView.findViewById(R.id.weekly);
            holder.userLay = (LinearLayout) convertView.findViewById(R.id.user_lay);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage((String) listData.get(position).
                        get("uidimg"),
                holder.image, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        holder.userLay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid", (String) listData.get(position).get("uid"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        holder.famousmanname1.setText((String) listData.get(position).get("uname"));
        holder.totalscore1.setText((CharSequence) listData.get(position).get("ballot_jifen"));
        holder.weekly.setText((String) listData.get(position).get("weekly"));
        return convertView;
    }

    static class ViewHolder {
        ImageView image;
        TextView famousmanname1;
        TextView totalscore1;
        TextView weekly;
        LinearLayout userLay;
    }
}
