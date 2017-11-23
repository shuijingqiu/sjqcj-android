package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.stocks.SharesDetailedActivity;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.NiuguListEntity;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * 当周 当日牛股控制器
 */
public class thisweekuprankingAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NiuguListEntity> listData;

    public thisweekuprankingAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<NiuguListEntity> listData) {
        if (listData != null) {
            this.listData = (ArrayList<NiuguListEntity>) listData.clone();
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
            convertView = mInflater.inflate(R.layout.list_item_todayupranking, null);
            holder = new ViewHolder();
            holder.unamestr = (TextView) convertView.findViewById(R.id.unamestr);
            holder.increasestr = (TextView) convertView.findViewById(R.id.increasestr);
            holder.ballot_namestr = (TextView) convertView.findViewById(R.id.ballot_namestr);
            holder.user_image = (ImageView) convertView.findViewById(R.id.user_image);
            holder.currentPrice = (TextView) convertView.findViewById(R.id.currentPrice);
            holder.pickuserinfo1 = (LinearLayout) convertView.findViewById(R.id.pickuserinfo1);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final NiuguListEntity niuguListEntity = listData.get(position);
        ImageLoader.getInstance().displayImage(niuguListEntity.getAvatar_middle(),
                holder.user_image, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());

        holder.user_image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid",niuguListEntity.getUid());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

            }
        });
        holder.unamestr.setText(niuguListEntity.getUname());
        String currentPrice = niuguListEntity.getCurrentPrice();
        if (currentPrice == null || "".equals(currentPrice) || Double.valueOf(currentPrice) == 0) {
            holder.increasestr.setText("—");
            holder.currentPrice.setText("—");
            holder.increasestr.setTextColor(Color.rgb(51, 51, 51));
        } else {
            ViewUtil.setViewColor(holder.increasestr, niuguListEntity.getIncrease());
            holder.currentPrice.setText(Utils.getNumberFormat(currentPrice));
        }


        if ("2".equals(niuguListEntity.getIs_free())){
            holder.ballot_namestr.setText("****");
            holder.currentPrice.setText("**");
        }else {
            holder.ballot_namestr.setText(niuguListEntity.getBallot_name());
            holder.ballot_namestr.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SharesDetailedActivity.class);
                    intent.putExtra("code", niuguListEntity.getShares());
                    intent.putExtra("name", niuguListEntity.getBallot_name());
                    context.startActivity(intent);
                }
            });
        }

        holder.pickuserinfo1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid", niuguListEntity.getUid());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView unamestr;
        TextView increasestr;
        TextView ballot_namestr;
        ImageView user_image;
        TextView currentPrice;
        LinearLayout pickuserinfo1;
    }

}
