package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.atfriendActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.Article.UserEntity;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class atfrientAdapter extends BaseAdapter {
    private Context context;
    private atfriendActivity activity;
    private ArrayList<UserEntity> listData;

    public atfrientAdapter(Context context, atfriendActivity activity) {
        super();
        this.context = context;
        this.activity = activity;
    }

    public void setlistData(ArrayList<UserEntity> listData) {
        if (listData != null) {
            this.listData = (ArrayList<UserEntity>) listData.clone();
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
            holder.detailcomment = (TextView) convertView.findViewById(R.id.detailcomment);
            holder.image = (ImageView) convertView.findViewById(R.id.user_image);
            holder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.RelativeLayout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final UserEntity userEntity = listData.get(position);
        ImageLoader.getInstance().displayImage(
                userEntity.getAvatar_middle(), holder.image,
                ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        holder.nameTv.setText(userEntity.getUname());
        // 考虑这个不要
//        holder.detailcomment.setText(userEntity.getIntro());
        holder.relativeLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                activity.atcallback(userEntity.getUname());
            }
        });
        return convertView;
    }

    public static class ViewHolder {
        TextView nameTv;
        TextView detailcomment;
        ImageView image;
        RelativeLayout relativeLayout;
    }
}
