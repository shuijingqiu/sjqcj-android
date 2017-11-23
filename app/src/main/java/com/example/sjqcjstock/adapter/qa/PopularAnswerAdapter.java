package com.example.sjqcjstock.adapter.qa;

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
 * 所有热门答主的item
 * Created by Administrator on 2017/3/21.
 */
public class PopularAnswerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<TomliveRoomEntity> listData;
    private TomliveRoomEntity tomliveRoomEntity;
    public PopularAnswerAdapter(Context context) {
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
            convertView = mInflater.inflate(R.layout.list_item_popular_answer, null);
            holder = new ViewHolder();
            holder.headIm = (ImageView) convertView.findViewById(R.id.head_iv);
            holder.nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.explainTv = (TextView) convertView.findViewById(R.id.explain_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        tomliveRoomEntity = listData.get(position);
        holder.nameTv.setText(tomliveRoomEntity.getUsername());
        holder.explainTv.setText(tomliveRoomEntity.getIntro());
        ImageLoader.getInstance().displayImage(tomliveRoomEntity.getAvatar(),
                holder.headIm, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        return convertView;
    }

    static class ViewHolder {
        TextView nameTv;
        TextView explainTv;
        ImageView headIm;
    }
}