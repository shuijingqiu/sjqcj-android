package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 当日牛股的adapte控制器
 */
public class todayuprankingAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, String>> listData;

    public todayuprankingAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<HashMap<String, String>> listData) {
        if (listData != null) {
            this.listData = (ArrayList<HashMap<String, String>>) listData.clone();
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
            convertView = mInflater.inflate(R.layout.list_item_todayupranking, null);
            holder = new ViewHolder();
            holder.unamestr = (TextView) convertView.findViewById(R.id.unamestr);
            holder.increasestr = (TextView) convertView.findViewById(R.id.increasestr);
            holder.ballot_namestr = (TextView) convertView.findViewById(R.id.ballot_namestr);
            holder.user_image = (ImageView) convertView.findViewById(R.id.user_image);
            holder.currentPrice = (TextView) convertView.findViewById(R.id.currentPrice);
            holder.pickuserinfo1 = (LinearLayout) convertView.findViewById(R.id.pickuserinfo1);


            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        ImageLoader.getInstance().displayImage((String) listData.get(position).
                        get("uidimg"),
                holder.user_image, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());

        holder.user_image.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                intent.putExtra("uid", (String) listData.get(position).get("uid"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });


        String currentPrice = listData.get(position).get("currentPrice");
        if (currentPrice == null || "".equals(currentPrice) || Double.valueOf(currentPrice) == 0) {
            holder.increasestr.setText("—");
            holder.currentPrice.setText("—");
            holder.increasestr.setTextColor(Color.rgb(51, 51, 51));
        } else {
            ViewUtil.setViewColor(holder.increasestr, listData.get(position).get("increase") + "");
            holder.currentPrice.setText(Utils.getNumberFormat(currentPrice));
        }

        holder.unamestr.setText((String) listData.get(position).get("uname"));
        holder.ballot_namestr.setText((String) listData.get(position).get("ballot_name"));
        holder.pickuserinfo1.setOnClickListener(new OnClickListener() {

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

        return convertView;
    }

    static class ViewHolder {
        TextView unamestr;
        TextView increasestr;
        TextView ballot_namestr;
        ImageView user_image;
        TextView currentPrice;
        LinearLayout pickuserinfo1;
//        ImageView thisline1;


    }

}
