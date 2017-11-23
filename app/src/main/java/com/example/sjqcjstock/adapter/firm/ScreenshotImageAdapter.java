package com.example.sjqcjstock.adapter.firm;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.sjqcjstock.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 截图后加载的图片显示
 * Created by Administrator on 2016/5/5.
 */
public class ScreenshotImageAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private List<Bitmap> asslist;

    public ScreenshotImageAdapter(Context mContext) {
        super();
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setlistData(ArrayList<Bitmap> asslist){
        if (asslist != null) {
            this.asslist = (List<Bitmap>) asslist.clone();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return asslist == null ? 0 : asslist.size();
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
            convertView = inflater.inflate(R.layout.gridview_item_screenshot, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Bitmap bitmap = asslist.get(position);
        holder.image.setImageBitmap(bitmap);

        return convertView;

    }

    static class ViewHolder {
        ImageView image;
    }
}
