package com.example.sjqcjstock.adapter.tomlive;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.Tomlive.TomliveRoomEntity;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * 订阅直播人的列表
 * Created by Administrator on 2017/1/24.
 */
public class SubscriptionListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<TomliveRoomEntity> listData;
    private TomliveRoomEntity tomliveRoomEntity;
    public SubscriptionListAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<TomliveRoomEntity> listData) {
        if (listData != null) {
            this.listData = (ArrayList<TomliveRoomEntity>) listData.clone();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        if (listData == null){
            return 0;
        }
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
        //动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_subscription_list, null);
            holder = new ViewHolder();
            holder.headIm = (ImageView) convertView.findViewById(R.id.head_iv);
            holder.nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.timlTv = (TextView) convertView.findViewById(R.id.time_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        tomliveRoomEntity = listData.get(position);
        holder.nameTv.setText(tomliveRoomEntity.getUsername());
        holder.timlTv.setText("到期时间："+tomliveRoomEntity.getExp_time().substring(0,10));
        ImageLoader.getInstance().displayImage(tomliveRoomEntity.getAvatar(),
                holder.headIm, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        return convertView;
    }

    static class ViewHolder {
        TextView nameTv;
        TextView timlTv;
        ImageView headIm;
    }
}