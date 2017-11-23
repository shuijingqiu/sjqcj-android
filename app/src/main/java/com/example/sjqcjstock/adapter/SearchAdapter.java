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

import com.example.sjqcjstock.Activity.SearchActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.Article.UserEntity;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 *
 */
public class SearchAdapter extends BaseAdapter {
    private Context context;
    private SearchActivity searchActivity;
    private ArrayList<UserEntity> listData;

    public SearchAdapter(Context context, SearchActivity searchActivity) {
        super();
        this.context = context;
        this.searchActivity = searchActivity;
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
            holder.image = (ImageView) convertView.findViewById(R.id.user_image);
            holder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.RelativeLayout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final UserEntity userEntity = listData.get(position);
        ImageLoader.getInstance().displayImage(userEntity.getAvatar_middle(),
                holder.image, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        holder.nameTv.setText(userEntity.getUname());
        holder.relativeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                searchActivity.searchcallback(userEntity.getUid());
            }
        });
        return convertView;
    }

    public static class ViewHolder {
        TextView nameTv;
        ImageView image;
        RelativeLayout relativeLayout;
    }
}
