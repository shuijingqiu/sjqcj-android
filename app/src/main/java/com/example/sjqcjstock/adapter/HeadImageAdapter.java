package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
import java.util.Map;

/**
 * 正文页面下面加载头像的控制器
 * Created by Administrator on 2016/5/5.
 */
public class HeadImageAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private List<Map<String, Object>> asslist;

    public HeadImageAdapter(List<Map<String, Object>> asslist, Context mContext) {
        this.asslist = asslist;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return asslist.size() > 6 ? 6 : asslist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.gridview_item_head, null);
        ImageView head = (ImageView) convertView.findViewById(R.id.head_img);
        TextView uidTv = (TextView) convertView.findViewById(R.id.head_uid_tv);
        ImageLoader.getInstance().displayImage((String) asslist.get(position).
                        get("avatar_tiny"),
                head, ImageUtil.getOption());
        uidTv.setText(asslist.get(position).get("uid").toString());
        return convertView;
    }
}
