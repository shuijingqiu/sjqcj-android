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

import com.example.sjqcjstock.Activity.addcommentweiboActivity;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 精华里面焦点的控制器
 */
public class essenceAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, String>> listData;

    public essenceAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<HashMap<String, String>> listData) {
        if (listData != null) {
            this.listData = (ArrayList<HashMap<String, String>>) listData.clone();
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

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_sesence, null);
        }
        ImageView image = (ImageView) convertView.findViewById(R.id.user_image);
        ImageLoader.getInstance().displayImage(listData.get(position).
                        get("image_url"),
                image, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        image.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid", listData.get(position).get("uidstr"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        if (listData.get(position).get("i") != null) {
            if (Integer.valueOf(listData.get(position).get("i")) == 2) {
                convertView.findViewById(R.id.thisline1).setVisibility(View.GONE);
            }

        }


        TextView username = (TextView) convertView.findViewById(R.id.weibo_titlestr);
        username.setText(listData.get(position).get("weibo_titlestr"));

        TextView detailAddress = (TextView) convertView.findViewById(R.id.username);
        detailAddress.setText(listData.get(position).get("username"));
//		
        TextView commentcount1 = (TextView) convertView.findViewById(R.id.commentcount1);
        commentcount1.setText(listData.get(position).get("comment_countstr"));

        LinearLayout pickuserinfo1 = (LinearLayout) convertView.findViewById(R.id.pickuserinfo1);

        ImageView commentweibo1 = (ImageView) convertView.findViewById(R.id.commentweibo1);
        commentweibo1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {

                    //
                    Intent intent = new Intent(context.getApplicationContext(), addcommentweiboActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("feed_id", (String) listData.get(position).get("weibo_idstr"));
                    intent.putExtra("feeduid", (String) listData.get(position).get("uidstr"));

                    context.startActivity(intent);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } finally {

                }

            }
        });

        pickuserinfo1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("uid", listData.get(position).get("uidstr"));

                    context.startActivity(intent);
                } catch (Exception e2) {
                    // TODO: handle exception
                    e2.printStackTrace();
                }
            }
        });

        ImageView vipImg = (ImageView) convertView.findViewById(R.id.vip_img);
        String isVip = listData.get(position).get(
                "isVip") + "";
        ViewUtil.setUpVip(isVip, vipImg);

        return convertView;
    }


}
