package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.Article.UserEntity;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * 名家，名博的adapter
 * Created by Administrator on 2017/5/02.
 */
public class famousAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<UserEntity> listData;

    public famousAdapter(Context context) {
        super();
        this.context = context;
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
        // TODO Auto-generated method stub
        //动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_famous, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.user_image1);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.intro = (TextView) convertView.findViewById(R.id.intro);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        UserEntity userEntity = listData.get(position);
        ImageLoader.getInstance().displayImage(userEntity.getAvatar_middle(),
                holder.image, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        holder.name.setText(userEntity.getUname());
        holder.intro.setText(userEntity.getIntro());
        return convertView;
    }

    public static class ViewHolder {
        ImageView image;
        TextView name;
        TextView intro;
    }
}
