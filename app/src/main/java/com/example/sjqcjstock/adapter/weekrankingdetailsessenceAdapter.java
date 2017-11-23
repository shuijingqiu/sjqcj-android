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

import com.example.sjqcjstock.Activity.stocks.SharesDetailedActivity;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.MatchNewsEntity;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class weekrankingdetailsessenceAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MatchNewsEntity> listData;


    public weekrankingdetailsessenceAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<MatchNewsEntity> listData) {
        if (listData != null) {
            this.listData = (ArrayList<MatchNewsEntity>) listData.clone();
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
        // TODO Auto-generated method stub
        //动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_totalrankingdetailsessence, null);

            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.user_image1);
            holder.famousmanname1 = (TextView) convertView.findViewById(R.id.famousmanname1);
            holder.weekscore1 = (TextView) convertView.findViewById(R.id.weekscore1);
            holder.stockname1 = (TextView) convertView.findViewById(R.id.stockname1);
            holder.shares2_name = (TextView) convertView.findViewById(R.id.stockname2);
            holder.stockprice1 = (TextView) convertView.findViewById(R.id.stockprice1);
            holder.stockprice2 = (TextView) convertView.findViewById(R.id.stockprice2);
            holder.uprange1 = (TextView) convertView.findViewById(R.id.uprange1);
            holder.uprange2 = (TextView) convertView.findViewById(R.id.uprange2);
            //holder.weekly = (TextView)convertView.findViewById(R.id.weekly);
            //holder.totalscore1 = (TextView)convertView.findViewById(R.id.totalscore1);
            holder.maxuprange1 = (TextView) convertView.findViewById(R.id.maxuprange1);
            holder.maxuprange2 = (TextView) convertView.findViewById(R.id.maxuprange2);
            holder.pickfamousmanname1 = (LinearLayout) convertView.findViewById(R.id.pickfamousmanname1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final MatchNewsEntity matchNewsEntity = listData.get(position);

        ImageLoader.getInstance().displayImage(matchNewsEntity.getAvatar_middle(),
                holder.image, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());

        holder.image.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid", matchNewsEntity.getUid());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                }

            }
        });

        holder.pickfamousmanname1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid", matchNewsEntity.getUid());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

            }
        });

        ViewUtil.setViewColor(holder.uprange1,matchNewsEntity.getIntegration1());
        ViewUtil.setViewColor(holder.uprange2,matchNewsEntity.getIntegration2());
        holder.famousmanname1.setText(matchNewsEntity.getUname());
        ViewUtil.setViewColor(holder.weekscore1,matchNewsEntity.getIntegration());
        holder.stockname1.setText(matchNewsEntity.getShares_name());
        holder.shares2_name.setText(matchNewsEntity.getShares2_name());
        holder.stockprice1.setText(matchNewsEntity.getPrice());
        holder.stockprice2.setText(matchNewsEntity.getPrice2());
        ViewUtil.setViewColor(holder.maxuprange1, matchNewsEntity.getIntegration3());
        ViewUtil.setViewColor(holder.maxuprange2, matchNewsEntity.getIntegration4());


        holder.stockname1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,SharesDetailedActivity.class);
                intent.putExtra("code",matchNewsEntity.getShares());
                intent.putExtra("name",matchNewsEntity.getShares_name());
                context.startActivity(intent);
            }
        });
        holder.shares2_name.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,SharesDetailedActivity.class);
                intent.putExtra("code",matchNewsEntity.getShares2());
                intent.putExtra("name",matchNewsEntity.getShares2_name());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        ImageView image;
        TextView famousmanname1;
        TextView weekscore1;
        TextView stockname1;
        TextView shares2_name;
        TextView stockprice1;
        TextView stockprice2;
        TextView uprange1;
        TextView uprange2;
        TextView weekly;
        //TextView totalscore1;
        TextView maxuprange1;
        TextView maxuprange2;
        LinearLayout pickfamousmanname1;
    }
}
