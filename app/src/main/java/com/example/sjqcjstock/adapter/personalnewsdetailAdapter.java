package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.ShowImageActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.ChatMessageEntity;
import com.example.sjqcjstock.netutil.CalendarUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class personalnewsdetailAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ChatMessageEntity> listData;

    public personalnewsdetailAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<ChatMessageEntity> listData) {
        if (listData != null) {
            this.listData = (ArrayList<ChatMessageEntity>) listData.clone();
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
            convertView = mInflater.inflate(R.layout.chatting_item_msg_text, null);
            holder = new ViewHolder();
            holder.sendtimeTv = (TextView) convertView.findViewById(R.id.tv_sendtime);
            holder.chatcontentLeftTv = (TextView) convertView.findViewById(R.id.tv_chatcontent_left);
            holder.contentLeftImg = (ImageView) convertView.findViewById(R.id.content_left_img);
            holder.headLeftIv = (ImageView) convertView.findViewById(R.id.head_img_left);
            holder.chatcontentRightTv = (TextView) convertView.findViewById(R.id.tv_chatcontent_right);
            holder.contentRightImg = (ImageView) convertView.findViewById(R.id.content_right_img);
            holder.headRightIv = (ImageView) convertView.findViewById(R.id.head_img_right);
            holder.righRl = (RelativeLayout) convertView.findViewById(R.id.righ_rl);
            holder.leftRl = (RelativeLayout) convertView.findViewById(R.id.left_rl);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        int count = listData.size()-1-position;
        ChatMessageEntity chatMessageEntity = listData.get(count);
        String uidstr = chatMessageEntity.getFrom_uid();
        final String contentstr = chatMessageEntity.getContent();
        holder.sendtimeTv.setText(CalendarUtil.formatDateTime(Utils.getStringtoDate(chatMessageEntity.getMtime())));
        if (Constants.getStaticmyuidstr().equals(uidstr)) {
            holder.righRl.setVisibility(View.VISIBLE);
            holder.leftRl.setVisibility(View.GONE);
            if (contentstr.indexOf("sjqcj.com/data/upload/")!=-1){
                ImageLoader.getInstance().displayImage(contentstr, holder.contentRightImg, ImageUtil.getOptionLocal(), ImageUtil.getAnimateFirstDisplayListener());
                holder.chatcontentRightTv.setVisibility(View.GONE);
                holder.contentRightImg.setVisibility(View.VISIBLE);
                holder.contentRightImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ShowImageActivity.class);
                        intent.putExtra("image",contentstr);
                        context.startActivity(intent);
                    }
                });
                holder.contentRightImg.setMaxWidth(220);
            }else{
                holder.chatcontentRightTv.setText(contentstr);
                holder.chatcontentRightTv.setVisibility(View.VISIBLE);
                holder.contentRightImg.setVisibility(View.GONE);
            }
            ImageLoader.getInstance().displayImage(chatMessageEntity.getAvatar_middle(), holder.headRightIv, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        }
        else{
            holder.righRl.setVisibility(View.GONE);
            holder.leftRl.setVisibility(View.VISIBLE);
//            if (contentstr.indexOf("https://www.sjqcj.com/data/upload/")!=-1){
            if (contentstr.indexOf("sjqcj.com/data/upload/")!=-1){
                ImageLoader.getInstance().displayImage(contentstr, holder.contentLeftImg, ImageUtil.getOptionLocal(), ImageUtil.getAnimateFirstDisplayListener());
                holder.chatcontentLeftTv.setVisibility(View.GONE);
                holder.contentLeftImg.setVisibility(View.VISIBLE);
                holder.contentLeftImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ShowImageActivity.class);
                        intent.putExtra("image",contentstr);
                        context.startActivity(intent);
                    }
                });
            }else{
                holder.chatcontentLeftTv.setText(contentstr);
                holder.chatcontentLeftTv.setVisibility(View.VISIBLE);
                holder.contentLeftImg.setVisibility(View.GONE);
            }
            ImageLoader.getInstance().displayImage(chatMessageEntity.getAvatar_middle(), holder.headLeftIv, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView sendtimeTv;
        TextView chatcontentLeftTv;
        ImageView contentLeftImg;
        ImageView headLeftIv;
        TextView chatcontentRightTv;
        ImageView contentRightImg;
        ImageView headRightIv;
        RelativeLayout righRl;
        RelativeLayout leftRl;
    }

}
