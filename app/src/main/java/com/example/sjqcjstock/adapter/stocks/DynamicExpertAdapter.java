package com.example.sjqcjstock.adapter.stocks;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.stocks.BusinessActivity;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.stocks.StocksInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 牛人动态的Adapter
 * Created by Administrator on 2016/8/16.
 */
public class DynamicExpertAdapter extends BaseAdapter {

    // 加载用的数据
    private List<StocksInfo> listData;
    private Context context;

    public DynamicExpertAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<StocksInfo> listData) {
        if (listData != null) {
            this.listData = (List<StocksInfo>) listData.clone();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_dynamic_expert, null);
            holder = new ViewHolder();
            holder.headNameRl = (RelativeLayout) convertView.findViewById(R.id.head_name_rl);
            holder.head = (ImageView) convertView.findViewById(R.id.head_iv);
            holder.time = (TextView) convertView.findViewById(R.id.time_tv);
            holder.name = (TextView) convertView.findViewById(R.id.name_tv);
            holder.winningProbability = (TextView) convertView.findViewById(R.id.winning_probability_tv);
            holder.totalYield = (TextView) convertView.findViewById(R.id.total_yield_tv);
            holder.nameCode = (TextView) convertView.findViewById(R.id.name_code_tv);
            holder.toBuy = (TextView) convertView.findViewById(R.id.to_buy_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 跳转到跟人中心
        holder.headNameRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(),
                        UserDetailNewActivity.class);
                // 要修改的
                intent.putExtra("uid", "26364");
                intent.putExtra("type", "1");
                context.startActivity(intent);
            }
        });

        // 跳转到买卖页面
        holder.toBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(),
                        BusinessActivity.class);
                // 要修改的
                intent.putExtra("type", "0");
                context.startActivity(intent);
            }
        });
        holder.name.setText(listData.get(position).getName());
        // 没写完

        return convertView;
    }

    static class ViewHolder {
        // 头像的单机控件
        RelativeLayout headNameRl;
        // 头像
        ImageView head;
        // 时间
        TextView time;
        // 名称
        TextView name;
        // 胜率
        TextView winningProbability;
        // 总收益率
        TextView totalYield;
        // 股票名称和代码
        TextView nameCode;
        // 跟买
        TextView toBuy;
    }
}
