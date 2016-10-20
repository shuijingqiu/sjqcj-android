package com.example.sjqcjstock.adapter.stocks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.stocks.StocksInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 模拟比赛列表的Adapter
 * Created by Administrator on 2016/8/17.
 */
public class SimulationGameAdapter extends BaseAdapter {

    // 加载用的数据
    private List<StocksInfo> listData;
    private Context context;

    public SimulationGameAdapter(Context context) {
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
            convertView = mInflater.inflate(R.layout.list_item_simulation_game, null);
            holder = new ViewHolder();
            holder.titleImg = (ImageView) convertView.findViewById(R.id.title_img_iv);
            holder.title = (TextView) convertView.findViewById(R.id.title_tv);
            holder.time = (TextView) convertView.findViewById(R.id.time_tv);
            holder.ranking = (TextView) convertView.findViewById(R.id.ranking_tv);
            holder.rankingValue = (TextView) convertView.findViewById(R.id.ranking_value_tv);
            holder.joinTv = (TextView) convertView.findViewById(R.id.join_tv);
            holder.joinBut = (Button) convertView.findViewById(R.id.join_but);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(listData.get(position).getName());
        // 没写完

        return convertView;
    }

    static class ViewHolder {
        ImageView titleImg;
        TextView title;
        TextView time;
        // 排名
        TextView ranking;
        // 排名字
        TextView rankingValue;
        // 参加
        TextView joinTv;
        // 参加Button
        Button joinBut;
    }
}
