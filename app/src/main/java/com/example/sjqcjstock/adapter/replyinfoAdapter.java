package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
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
import com.example.sjqcjstock.entity.Article.CommentListEntity;
import com.example.sjqcjstock.netutil.CalendarUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class replyinfoAdapter extends BaseAdapter {


    private Context context;
    private ArrayList<CommentListEntity> listData;

    public replyinfoAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<CommentListEntity> listData) {
        if (this.listData != null)
            this.listData.clear();
        this.listData = (ArrayList<CommentListEntity>) listData.clone();
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
            convertView = mInflater.inflate(R.layout.list_item_replyinfo, null);
            holder = new ViewHolder();
            holder.createtime1 = (TextView) convertView.findViewById(R.id.createtime1);
            holder.wv_ad = (TextView) convertView.findViewById(R.id.wv_ad);
            holder.storey1 = (TextView) convertView.findViewById(R.id.storey1);
            holder.username1 = (TextView) convertView.findViewById(R.id.username1);
            holder.image = (ImageView) convertView.findViewById(R.id.userimg2);
            holder.pickcommentreply2 = (LinearLayout) convertView.findViewById(R.id.pickcommentreply2);
            holder.vipImg = (ImageView) convertView
                    .findViewById(R.id.vip_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CommentListEntity commentListEntity = listData.get(position);
        holder.createtime1.setText(CalendarUtil.formatDateTime(Utils.getStringtoDate(commentListEntity.getCtime())));
        String contentstr = commentListEntity.getContent();
        CharSequence charSequence = Html.fromHtml(contentstr, ImageUtil.getImageGetter(context.getResources()), null);

        holder.wv_ad.setText(charSequence);
        holder.storey1.setText(commentListEntity.getStorey()+"");
        holder.username1.setText(commentListEntity.getUname());
        ImageLoader.getInstance().displayImage(commentListEntity.getAvatar_middle(),
                holder.image, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());

        holder.image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid", commentListEntity.getUid());
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        });

        ViewUtil.setUpVipNew(commentListEntity.getUser_group_icon_url(), holder.vipImg);
        return convertView;

    }


    static class ViewHolder {
        ImageView image;
        ImageView vipImg;
        TextView createtime1;
        TextView wv_ad;
        TextView storey1;
        TextView username1;
        LinearLayout pickcommentreply2;
    }
}
