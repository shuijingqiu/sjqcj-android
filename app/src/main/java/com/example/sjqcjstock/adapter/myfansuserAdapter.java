package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 粉丝列表的控制器
 */
public class myfansuserAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, Object>> listData;

    public myfansuserAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<HashMap<String, Object>> listData) {
        if (listData != null) {
            this.listData = (ArrayList<HashMap<String, Object>>) listData.clone();
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
            convertView = mInflater.inflate(R.layout.list_item_fansuser, null);
        }
        ImageView image = (ImageView) convertView.findViewById(R.id.user_image);
        //image.setBackgroundResource((Integer)listData.get(position).get("user_image"));
        ImageLoader.getInstance().displayImage((String) listData.get(position).
                        get("avatar_middle"),
                image, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        TextView username = (TextView) convertView.findViewById(R.id.username);
        username.setText((String) listData.get(position).get("uname"));
        TextView detailcomment = (TextView) convertView.findViewById(R.id.detailcomment);
        detailcomment.setText((String) listData.get(position).get("intro"));
        RelativeLayout RelativeLayout = (RelativeLayout) convertView.findViewById(R.id.RelativeLayout);
        RelativeLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid", listData.get(position).get("uid").toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        String isVip = listData.get(position).get(
                "isVip") + "";
        ImageView vipImg = (ImageView) convertView.findViewById(R.id.vip_img);
        ViewUtil.setUpVip(isVip, vipImg);
        return convertView;
    }
}
