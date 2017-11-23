package com.example.sjqcjstock.adapter.stocks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.stocks.MatchEntity;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Md5Util;
import com.example.sjqcjstock.netutil.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 模拟比赛详情的Adapter
 * Created by Administrator on 2016/8/17.
 */
public class SimulationGameDetailsAdapter extends BaseAdapter {

    // 加载用的数据
    private List<MatchEntity> listData;
    private MatchEntity matchEntity;
    private Context context;

    public SimulationGameDetailsAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<MatchEntity> listData) {
        if (listData != null) {
            this.listData = (List<MatchEntity>) listData.clone();
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
            holder.ranking = (TextView) convertView.findViewById(R.id.ranking_tv);
            holder.headImg = (ImageView) convertView.findViewById(R.id.head_img_iv);
            holder.name = (TextView) convertView.findViewById(R.id.name_tv);
            holder.profit = (TextView) convertView.findViewById(R.id.profit_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        matchEntity = listData.get(position);

        String ranking = matchEntity.getRanking();
        holder.ranking.setText("");
        if ("1".equals(ranking)){
            holder.ranking.setBackgroundResource(R.mipmap.rank1);
        }else if ("2".equals(ranking)){
            holder.ranking.setBackgroundResource(R.mipmap.rank2);
        }else if ("3".equals(ranking)){
            holder.ranking.setBackgroundResource(R.mipmap.rank3);
        }else{
            holder.ranking.setText(ranking);
            holder.ranking.setBackgroundResource(R.mipmap.nullimg);
        }

        holder.name.setText(matchEntity.getUsername());
        // 周收益率
        String totalRate = matchEntity.getTotal_rate();
        if (Double.valueOf(totalRate)==0d){
            holder.profit.setTextColor(holder.profit.getResources().getColor(R.color.color_article));
        }else if (totalRate.indexOf("-") == -1) {
            holder.profit.setTextColor(holder.profit.getResources().getColor(R.color.color_ef3e3e));
        }else{
            holder.profit.setTextColor(holder.profit.getResources().getColor(R.color.color_1bc07d));
        }
        holder.profit.setText(Utils.getNumberFormat2(totalRate)+"%");

        ImageLoader.getInstance().displayImage(Md5Util.getuidstrMd5(Md5Util
                        .getMd5(matchEntity.getUid())),
                holder.headImg, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());

        return convertView;
    }

    static class ViewHolder {
        // 排名
        TextView ranking;
        // 头像图像
        ImageView headImg;
        // 用户名
        TextView name;
        // 比赛收益
        TextView profit;
    }
}
