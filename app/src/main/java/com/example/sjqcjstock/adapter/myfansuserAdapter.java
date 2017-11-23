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
import com.example.sjqcjstock.entity.Article.UserEntity;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * 粉丝列表的控制器
 */
public class myfansuserAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<UserEntity> listData;

    public myfansuserAdapter(Context context) {
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
        //动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_fansuser, null);
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
        UserEntity userEntity = listData.get(position);
        ImageLoader.getInstance().displayImage(userEntity.getAvatar_middle(),
                holder.image, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        holder.nameTv.setText(userEntity.getUname());
        holder.detailcomment.setText(userEntity.getIntro());
        holder.relativeLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid", listData.get(position).getUid());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        ViewUtil.setUpVipNew(userEntity.getUser_group_icon_url(), holder.vipImg);
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
