package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.Article.FeedEntity;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 正文页面下面加载头像的控制器
 * Created by Administrator on 2016/5/5.
 */
public class HeadImageAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private List<FeedEntity.asslist> asslist;

    public HeadImageAdapter(List<FeedEntity.asslist> asslist, Context mContext) {
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

        //动态加载布局
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gridview_item_head, null);
            holder = new ViewHolder();
            holder.head = (ImageView) convertView.findViewById(R.id.head_img);
            holder.uidTv = (TextView) convertView.findViewById(R.id.head_uid_tv);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        FeedEntity.asslist asslistEntity = asslist.get(position);
        ImageLoader.getInstance().displayImage(asslistEntity.getAvatar_tiny(),
                holder.head, ImageUtil.getOption());
        holder.uidTv.setText(asslistEntity.getUid());
        return convertView;

    }

    static class ViewHolder {
        ImageView head;
        TextView uidTv;
    }
}
