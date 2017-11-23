package com.example.sjqcjstock.adapter.firm;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.Article.ArticleDetailsActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.firm.FirmRemarkEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 操作感言的列表
 * Created by Administrator on 2017/9/28.
 */
public class FirmRecollectionsAdapter extends BaseAdapter {

    // 加载用的数据
    private List<FirmRemarkEntity> listData;
    private FirmRemarkEntity firmRemarkEntity;
    private Context context;

    public FirmRecollectionsAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<FirmRemarkEntity> listData) {
        if (listData != null) {
            this.listData = (List<FirmRemarkEntity>) listData.clone();
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
        LayoutInflater mInflater = LayoutInflater.from(context);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_firm_recollections, null);
            holder = new ViewHolder();
            holder.recollectionsTv = (TextView) convertView.findViewById(R.id.recollections_tv);
            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.accountBt = (Button) convertView.findViewById(R.id.account_bt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        firmRemarkEntity = listData.get(position);
        holder.recollectionsTv.setText("        "+firmRemarkEntity.getContent());
        holder.timeTv.setText(firmRemarkEntity.getTime());
        holder.accountBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ArticleDetailsActivity.class);
                intent.putExtra("weibo_id",listData.get(position).getFeed_id());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        // 操作感言
        TextView recollectionsTv;
        TextView timeTv;
        Button accountBt;
    }
}