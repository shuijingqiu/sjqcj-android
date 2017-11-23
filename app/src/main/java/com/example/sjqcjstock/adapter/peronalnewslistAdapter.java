package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.PrivateMessageEntity;
import com.example.sjqcjstock.netutil.CalendarUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 *
 */
public class peronalnewslistAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<PrivateMessageEntity> listData;

    public peronalnewslistAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<PrivateMessageEntity> listData) {
        if (this.listData != null)
            this.listData.clear();
        this.listData = (ArrayList<PrivateMessageEntity>) listData.clone();
        notifyDataSetChanged();
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
            convertView = mInflater.inflate(R.layout.list_item_personalnews, null);
            holder = new ViewHolder();
            holder.nameTv = (TextView) convertView.findViewById(R.id.username1);
            holder.image = (ImageView) convertView.findViewById(R.id.user_image1);
            holder.lastmessage1 = (TextView) convertView.findViewById(R.id.lastmessage1);
            holder.creatime1 = (TextView) convertView.findViewById(R.id.creatime1);
            holder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.RelativeLayout);
            holder.vipImg = (ImageView) convertView.findViewById(R.id.vip_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PrivateMessageEntity privateMessageEntity = listData.get(position);
        ImageLoader.getInstance().displayImage(privateMessageEntity.getAvatar_middle(),
                holder.image, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        holder.nameTv.setText(privateMessageEntity.getUname());
        holder.lastmessage1.setText(privateMessageEntity.getContent());
        holder.creatime1.setText(CalendarUtil.formatDateTime(Utils.getStringtoDate(privateMessageEntity.getList_ctime())));
        ViewUtil.setUpVipNew(privateMessageEntity.getUser_group_icon_url(), holder.vipImg);
        return convertView;
    }

    public static class ViewHolder {
        TextView nameTv;
        ImageView image;
        TextView lastmessage1;
        TextView creatime1;
        RelativeLayout relativeLayout;
        ImageView vipImg;
    }

}
