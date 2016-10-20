package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.SearchActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchAdapter extends BaseAdapter {
    private Context context;
    private SearchActivity searchActivity;
    private ArrayList<HashMap<String, Object>> listData;


    public SearchAdapter(Context context, SearchActivity searchActivity) {
        super();
        this.context = context;
        this.searchActivity = searchActivity;
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
        // TODO Auto-generated method stub
        // 动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_myattentionuser, null);
        }

        ImageView image = (ImageView) convertView.findViewById(R.id.user_image);
        //ImageLoader根据图片的url加载数据
        ImageLoader.getInstance().displayImage((String) listData.get(position).get("avatar_middlestr"), image,
                ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());

        TextView username = (TextView) convertView.findViewById(R.id.username);
        username.setText((String) listData.get(position).get("unamestr"));

        RelativeLayout relat1 = (RelativeLayout) convertView.findViewById(R.id.RelativeLayout);

        relat1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                searchActivity.searchcallback((String) listData.get(position).get("uid"));
            }
        });

        return convertView;
    }
}
