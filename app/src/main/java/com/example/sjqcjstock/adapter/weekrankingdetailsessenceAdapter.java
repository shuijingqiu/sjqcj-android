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

import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class weekrankingdetailsessenceAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, Object>> listData;


    public weekrankingdetailsessenceAdapter(Context context, ArrayList<HashMap<String, Object>> listData) {
        super();
        this.context = context;
        this.listData = listData;
    }

    public void setlistData(ArrayList<HashMap<String, Object>> listData) {
        this.listData = listData;
    }

    @Override
    public int getCount() {
        //return imagesUrl.length;
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

        ImageView image = (ImageView) convertView.findViewById(R.id.user_image1);
        // image.setBackgroundResource((Integer)listData.get(position).get("friend_image"));
        ImageLoader.getInstance().displayImage((String) listData.get(position).
                        get("uidimg"),
                holder.image, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());

        holder.image.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid", (String) listData.get(position).get("uid"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } finally {

                }

            }
        });

        holder.pickfamousmanname1.setOnClickListener(new OnClickListener() {

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
                } finally {

                }

            }
        });

        ViewUtil.setViewColor(holder.maxuprange1, listData.get(position).get("integration1") + "");
        ViewUtil.setViewColor(holder.maxuprange2, listData.get(position).get("integration2") + "");

        //TextView famousmanname1=(TextView)convertView.findViewById(R.id.famousmanname1);
        holder.famousmanname1.setText((String) listData.get(position).get("uname"));

        //TextView weekscore1=(TextView)convertView.findViewById(R.id.weekscore1);
        holder.weekscore1.setText((CharSequence) listData.get(position).get("list_price"));
//		
        //TextView totalscore1=(TextView)convertView.findViewById(R.id.totalscore1);
        //totalscore1.setText((CharSequence)listData.get(position).get("ballot_jifen"));
//		


        //TextView stockname1=(TextView)convertView.findViewById(R.id.stockname1);
        holder.stockname1.setText((String) listData.get(position).get("shares_name"));

        //TextView shares2_name=(TextView)convertView.findViewById(R.id.stockname2);
        holder.shares2_name.setText((String) listData.get(position).get("shares2_name"));


        //TextView stockprice1=(TextView)convertView.findViewById(R.id.stockprice1);
        holder.stockprice1.setText((String) listData.get(position).get("price"));

        //TextView stockprice2=(TextView)convertView.findViewById(R.id.stockprice2);
        holder.stockprice2.setText((String) listData.get(position).get("price2"));
//
//		//TextView uprange1=(TextView)convertView.findViewById(R.id.uprange1);
//		  holder.uprange1.setText((String)listData.get(position).get("integration3"));
//
//		//TextView uprange2=(TextView)convertView.findViewById(R.id.uprange2);
//		  holder.uprange2.setText((String)listData.get(position).get("integration4"));


        ViewUtil.setViewColor(holder.uprange1, listData.get(position).get("integration3") + "");
        //TextView uprange2=(TextView)convertView.findViewById(R.id.uprange2);
        ViewUtil.setViewColor(holder.uprange2, listData.get(position).get("integration4") + "");


        //TextView weekly=(TextView)convertView.findViewById(R.id.weekly);
        // holder.weekly.setText((String)listData.get(position).get("weekly"));

        //TextView rankingcount=(TextView)convertView.findViewById(R.id.rankingcount);
        //rankingcount.setText((String)listData.get(position).get("rankingcount"));


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
