package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.addcommentweiboActivity;
import com.example.sjqcjstock.Activity.forumnotedetailActivity;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class atmeAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, String>> listData;

    public atmeAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<HashMap<String, String>> listData) {
        if (this.listData != null)
            this.listData.clear();
        this.listData = (ArrayList<HashMap<String, String>>) listData.clone();
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
        // TODO Auto-generated method stub
        //动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_atmecomment, null);
            holder = new ViewHolder();
            holder.user_image1 = (ImageView) convertView.findViewById(R.id.user_image1);

            holder.contentname1 = (TextView) convertView.findViewById(R.id.contentname1);
            holder.contentbody1 = (TextView) convertView.findViewById(R.id.contentbody1);
            holder.sourceusername1 = (TextView) convertView.findViewById(R.id.sourceusername1);
            holder.originalnotecontent1 = (TextView) convertView.findViewById(R.id.originalnotecontent1);
            holder.contenttimes1 = (TextView) convertView.findViewById(R.id.contenttimes1);
            holder.pickcontentbody1 = (LinearLayout) convertView.findViewById(R.id.pickcontentbody1);
            holder.commentreply1 = (TextView) convertView.findViewById(R.id.commentreply1);
            holder.repostlin1 = (LinearLayout) convertView.findViewById(R.id.repostlin1);

            holder.vipImg = (ImageView) convertView.findViewById(R.id.vip_img);
            holder.vipImgSource = (ImageView) convertView.findViewById(R.id.vip_img_source);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.repostlin1.setVisibility(View.GONE);

        //ImageView user_image1=(ImageView)convertView.findViewById(R.id.user_image1);
        //image.setBackgroundResource((Integer)listData.get(position).get("friend_image"));
        ImageLoader.getInstance().displayImage((String) listData.get(position).
                        get("avatar_middle"),
                holder.user_image1, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
//		
        //TextView contentname1=(TextView)convertView.findViewById(R.id.contentname1);
        holder.contentname1.setText((String) listData.get(position).get("uname"));
//		
        //TextView contentbody1=(TextView)convertView.findViewById(R.id.contentbody1);
        holder.contentbody1.setText((String) listData.get(position).get("feed_content"));
//		
        holder.sourceusername1.setText((String) listData.get(position).get("unames"));

        //TextView originalnotecontent1=(TextView)convertView.findViewById(R.id.originalnotecontent1);
        //if("repost".equals((String)listData.get(position).get("type"))){
        //如果转发，显示原微博
        if (!"".equals(listData.get(position).get("atfeed_content"))) {
            holder.repostlin1.setVisibility(View.VISIBLE);
            holder.originalnotecontent1.setText(Html.fromHtml(listData.get(position).get("atfeed_content"), ImageUtil.getImageGetter(context.getResources()), null));
        }


        holder.repostlin1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(context.getApplicationContext(), forumnotedetailActivity.class);
                intent.putExtra("weibo_id", (String) listData.get(position).get("atfeed_id"));
                intent.putExtra("uid", (String) listData.get(position).get("atfeed_uid"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


        holder.contenttimes1.setText((String) listData.get(position).get("publish_time"));
        //}
//		


        holder.user_image1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid", (String) listData.get(position).get("uid"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // 这个要不要待考虑
        holder.commentreply1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(context.getApplicationContext(), addcommentweiboActivity.class);
                intent.putExtra("feed_id", (String) listData.get(position).get("feed_id"));
                intent.putExtra("feeduid", (String) listData.get(position).get("uid"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.pickcontentbody1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String content = listData.get(position).get("feed_content");
                if (content.length() > 3 && Constants.microBlogShare.equals(content.substring(0, 4))) {
                    return;
                }
                try {
                    Intent intent = new Intent(context.getApplicationContext(), forumnotedetailActivity.class);
                    intent.putExtra("weibo_id", (String) listData.get(position).get("feed_id"));
                    intent.putExtra("uid", (String) listData.get(position).get("uid"));

                    Log.e("mh123",listData.get(position).get("type").toString()+"----------");

                    // 传递转发微博的信息
                    if ("repost".equals(listData.get(position).get("type").toString())) {
                        intent.putExtra(
                                "sourceweibo_id",
                                listData.get(position)
                                        .get("atfeed_id").toString());
                        intent.putExtra("sourceuid", listData
                                .get(position).get("atfeed_uid")
                                .toString());
                        // 转发类型
                        intent.putExtra(
                                "type",
                                listData.get(position)
                                        .get("type").toString());
                    }

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        String isVip = listData.get(position).get(
                "isVip");
        String isVipSource = listData.get(position).get(
                "isVipSource");
        ViewUtil.setUpVip(isVip, holder.vipImg);
        ViewUtil.setUpVip(isVipSource, holder.vipImgSource);
        return convertView;
    }

    static class ViewHolder {
        ImageView user_image1;
        TextView contentname1;
        TextView contentbody1;
        TextView sourceusername1;
        TextView originalnotecontent1;
        LinearLayout repostlin1;
        TextView contenttimes1;
        LinearLayout pickcontentbody1;
        TextView commentreply1;
        ImageView vipImg;
        ImageView vipImgSource;
    }

}
