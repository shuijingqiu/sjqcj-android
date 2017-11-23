package com.example.sjqcjstock.adapter.tomlive;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.Tomlive.TomliveRenewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.Tomlive.TomliveRoomEntity;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * 直播牛人的列表
 * Created by Administrator on 2017/1/11.
 */
public class TomliveListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<TomliveRoomEntity> listData;
    private TomliveRoomEntity tomliveRoomEntity;
    private String type;
    public TomliveListAdapter(Context context,String type) {
        super();
        this.context = context;
        this.type = type;
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
            convertView = mInflater.inflate(R.layout.list_item_tomlive_list, null);
            holder = new ViewHolder();
            holder.headIm = (ImageView) convertView.findViewById(R.id.head_iv);
            holder.nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.explainTv = (TextView) convertView.findViewById(R.id.explain_tv);
            holder.tomliveImg = (ImageView) convertView.findViewById(R.id.tomlive_img);
            holder.messageCountTv = (TextView) convertView.findViewById(R.id.message_count_tv);
            holder.renewTv = (TextView) convertView.findViewById(R.id.renew_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        tomliveRoomEntity = listData.get(position);
        holder.nameTv.setText(tomliveRoomEntity.getUsername());
        holder.explainTv.setText(tomliveRoomEntity.getIntro());
        // 这个要改变（应该是是否直播中）
        String liveStatus = listData.get(position).getLive_status();
        if ("1".equals(liveStatus)){
            holder.tomliveImg.setVisibility(View.VISIBLE);
        }else{
            holder.tomliveImg.setVisibility(View.GONE);
        }
        ImageLoader.getInstance().displayImage(tomliveRoomEntity.getAvatar(),
                holder.headIm, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());

        if (type != null && "my".equals(type)){
            holder.explainTv.setText("到期时间："+tomliveRoomEntity.getExp_time());
            holder.renewTv.setVisibility(View.VISIBLE);
            holder.renewTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tomliveRoomEntity = listData.get(position);
                    Intent intent = new Intent(context,TomliveRenewActivity.class);
                    intent.putExtra("uid",tomliveRoomEntity.getUid());
                    intent.putExtra("name",tomliveRoomEntity.getUsername());
                    intent.putExtra("intro",tomliveRoomEntity.getIntro());
                    intent.putExtra("roomId",tomliveRoomEntity.getId());
                    context.startActivity(intent);
                }
            });
            String liveCount = tomliveRoomEntity.getLive_count();
            if (liveCount !=null && !"".equals(liveCount) && Integer.valueOf(liveCount)>0){
                if (Integer.valueOf(liveCount) > 99) {
                    liveCount = "99+";
                }
                holder.messageCountTv.setText(liveCount);
                holder.messageCountTv.setVisibility(View.VISIBLE);
            }else{
                holder.messageCountTv.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    static class ViewHolder {
        TextView nameTv;
        TextView explainTv;
        ImageView tomliveImg;
        ImageView headIm;
        // 消息条数
        TextView messageCountTv;
        // 续订
        TextView renewTv;
    }
}