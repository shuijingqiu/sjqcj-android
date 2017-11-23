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

import com.example.sjqcjstock.Activity.Article.ArticleDetailsActivity;
import com.example.sjqcjstock.Activity.Article.addcommentweiboActivity;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.Article.AtFeedListEntity;
import com.example.sjqcjstock.netutil.CalendarUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.example.sjqcjstock.view.ClickOnTouch;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class atmeAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<AtFeedListEntity> listData;

    public atmeAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<AtFeedListEntity> listData) {
        if (this.listData != null)
            this.listData.clear();
        this.listData = (ArrayList<AtFeedListEntity>) listData.clone();
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
            convertView = mInflater.inflate(R.layout.list_item_atmecomment, null);
            holder = new ViewHolder();
            holder.user_image1 = (ImageView) convertView.findViewById(R.id.user_image1);

            holder.contentname1 = (TextView) convertView.findViewById(R.id.contentname1);
            holder.contentbody1 = (TextView) convertView.findViewById(R.id.contentbody1);
            holder.sourceusername1 = (TextView) convertView.findViewById(R.id.sourceusername1);
            holder.rewardTv = (TextView) convertView.findViewById(R.id.reward_tv);
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

        final AtFeedListEntity atFeedListEntity = listData.get(position);
        holder.repostlin1.setVisibility(View.GONE);
        ImageLoader.getInstance().displayImage(atFeedListEntity.getAvatar_middle(),
                holder.user_image1, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        holder.contentname1.setText(atFeedListEntity.getUname());
        String content = atFeedListEntity.getFeed_content();
        content = content==""?"微博分享":content;
        holder.contentbody1.setText(Html.fromHtml(content, ImageUtil.getImageGetter(context.getResources()), null));
        holder.contentbody1.setOnTouchListener(new ClickOnTouch(context));
        //如果转发，显示原微博
        if ("repost".equals(atFeedListEntity.getType())) {
            holder.sourceusername1.setText(atFeedListEntity.getApi_source().getUname());
            holder.sourceusername1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid", atFeedListEntity.getApi_source().getUid());
                    context.startActivity(intent);
                }
            });
            holder.repostlin1.setVisibility(View.VISIBLE);
            holder.repostlin1.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(context.getApplicationContext(), ArticleDetailsActivity.class);
                    intent.putExtra("weibo_id", atFeedListEntity.getApi_source().getFeed_id());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            // 打赏文章
            if ("repost".equals(atFeedListEntity.getApi_source().getType())) {
                holder.rewardTv.setText(atFeedListEntity.getApi_source().getReward());
                holder.originalnotecontent1.setText(Html.fromHtml(atFeedListEntity.getApi_source().getIntroduction(), ImageUtil.getImageGetter(context.getResources()), null));
            }else{
                holder.rewardTv.setText("");
                holder.originalnotecontent1.setText(Html.fromHtml(atFeedListEntity.getApi_source().getFeed_content(), ImageUtil.getImageGetter(context.getResources()), null));
            }
            holder.originalnotecontent1.setOnTouchListener(new ClickOnTouch(context));

        }else{
            holder.repostlin1.setVisibility(View.GONE);
        }

        // 发布时间
        String feedTime = CalendarUtil.formatDateTime(Utils
                .getStringtoDate(atFeedListEntity.getPublish_time()));
        holder.contenttimes1.setText(feedTime);

        holder.user_image1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid", atFeedListEntity.getUid());
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
                Intent intent = new Intent(context.getApplicationContext(), addcommentweiboActivity.class);
                intent.putExtra("feed_id", atFeedListEntity.getFeed_id());
                intent.putExtra("feeduid", atFeedListEntity.getUid());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        if (!Constants.microBlogShare.equals(content)) {
            holder.pickcontentbody1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(context.getApplicationContext(), ArticleDetailsActivity.class);
                    intent.putExtra("weibo_id", atFeedListEntity.getFeed_id());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }

        ViewUtil.setUpVipNew(atFeedListEntity.getUser_group_icon_url(), holder.vipImg);
        ViewUtil.setUpVipNew(atFeedListEntity.getApi_source().getUser_group_icon_url(), holder.vipImgSource);
        return convertView;
    }

    static class ViewHolder {
        ImageView user_image1;
        TextView contentname1;
        TextView contentbody1;
        TextView sourceusername1;
        TextView rewardTv;
        TextView originalnotecontent1;
        LinearLayout repostlin1;
        TextView contenttimes1;
        LinearLayout pickcontentbody1;
        TextView commentreply1;
        ImageView vipImg;
        ImageView vipImgSource;
    }

}
