package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 关注列表的控制器
 */
public class myattentionuserAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, Object>> listData;

    public myattentionuserAdapter(Context context) {
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_myattentionuser, null);
            holder = new ViewHolder();
            holder.nameTv = (TextView) convertView.findViewById(R.id.username);
            holder.image = (ImageView) convertView.findViewById(R.id.user_image);
            holder.detailcomment = (TextView) convertView.findViewById(R.id.detailcomment);
            holder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.RelativeLayout);
            holder.vipImg = (ImageView) convertView.findViewById(R.id.vip_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage((String) listData.get(position).
                        get("avatar_middle"),
                holder.image, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        holder.nameTv.setText((String) listData.get(position).get("uname"));
        holder.detailcomment.setText((String) listData.get(position).get("intro"));
        holder.relativeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid", listData.get(position).get("uid").toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);;
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        String isVip = listData.get(position).get(
                "isVip") + "";
        ViewUtil.setUpVip(isVip, holder.vipImg);
        return convertView;
    }

    public static class ViewHolder {
        TextView nameTv;
        ImageView image;
        TextView detailcomment;
        RelativeLayout relativeLayout;
        ImageView vipImg;
    }

}
