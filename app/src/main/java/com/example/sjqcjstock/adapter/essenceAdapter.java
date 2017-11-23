package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.Article.addcommentweiboActivity;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.Activity.user.loginActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.Article.RaceReportEntity;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * 精华里面焦点的控制器
 */
public class essenceAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<RaceReportEntity> listData;

    public essenceAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<RaceReportEntity> listData) {
        if (listData != null) {
            this.listData = (ArrayList<RaceReportEntity>) listData.clone();
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
            convertView = mInflater.inflate(R.layout.list_item_sesence, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.user_image);
            holder.view = convertView.findViewById(R.id.thisline1);
            holder.titleStr = (TextView) convertView.findViewById(R.id.weibo_titlestr);
            holder.userName = (TextView) convertView.findViewById(R.id.username);
            holder.commentcount1 = (TextView) convertView.findViewById(R.id.commentcount1);
            holder.commentweibo1 = (ImageView) convertView.findViewById(R.id.commentweibo1);
            holder.vipImg = (ImageView) convertView.findViewById(R.id.vip_img);
            holder.pickuserinfo1 = (LinearLayout) convertView.findViewById(R.id.pickuserinfo1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final RaceReportEntity raceReportEntity = listData.get(position);
        ImageLoader.getInstance().displayImage(raceReportEntity.getImg(),
                holder.image, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());

        holder.image.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid", raceReportEntity.getUid());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

//        if (listData.get(position).get("i") != null) {
//            if (Integer.valueOf(listData.get(position).get("i")) == 2) {
//                holder.view.setVisibility(View.GONE);
//            }
//        }

        holder.titleStr.setText(raceReportEntity.getTitle());
        holder.userName.setText(raceReportEntity.getUname());
        holder.commentcount1.setText(raceReportEntity.getComment_count());
        // 评论微博的按钮
        holder.commentweibo1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // menghuan 不用登陆也可以用
                // 如果未登陆跳转到登陆页面
                if (!Constants.isLogin){
                    Intent intent = new Intent(context, loginActivity.class);
                    context.startActivity(intent);
                    return;
                }
                try {
                    Intent intent = new Intent(context.getApplicationContext(), addcommentweiboActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("feed_id", raceReportEntity.getFeed_id());
                    intent.putExtra("feeduid", raceReportEntity.getUid());
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        holder.pickuserinfo1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("uid", raceReportEntity.getUid());
                    context.startActivity(intent);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });

        ViewUtil.setUpVipNew(raceReportEntity.getUser_group_icon_url(), holder.vipImg);

        return convertView;
    }

    public static class ViewHolder {
        ImageView image;
        View view;
        TextView titleStr;
        TextView userName;
        TextView commentcount1;
        LinearLayout pickuserinfo1;
        ImageView commentweibo1;
        ImageView vipImg;
    }

}
