package com.example.sjqcjstock.adapter.stocks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.stocks.StocksInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 模拟比赛详情的Adapter
 * Created by Administrator on 2016/8/17.
 */
public class SimulationGameDetailsAdapter extends BaseAdapter {

    // 加载用的数据
    private List<StocksInfo> listData;
    private Context context;

    public SimulationGameDetailsAdapter(Context context) {
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
            convertView = mInflater.inflate(R.layout.list_item_simulation_game_details, null);
            holder = new ViewHolder();
            holder.rankingImg = (ImageView) convertView.findViewById(R.id.ranking_iv);
            holder.ranking = (TextView) convertView.findViewById(R.id.ranking_tv);
            holder.headImg = (ImageView) convertView.findViewById(R.id.head_img_iv);
            holder.name = (TextView) convertView.findViewById(R.id.name_tv);
            holder.profit = (TextView) convertView.findViewById(R.id.profit_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ranking.setText(position + 1 + "");
        holder.name.setText(listData.get(position).getName());
        // 没写完

        return convertView;
    }

    static class ViewHolder {
        // 排名图像
        ImageView rankingImg;
        // 排名字体
        TextView ranking;
        // 头像图像
        ImageView headImg;
        // 用户名
        TextView name;
        // 比赛收益
        TextView profit;
    }
}
