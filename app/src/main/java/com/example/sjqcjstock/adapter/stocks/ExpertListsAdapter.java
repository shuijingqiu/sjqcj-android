package com.example.sjqcjstock.adapter.stocks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.stocks.TotalProfitEntity;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Md5Util;
import com.example.sjqcjstock.netutil.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 牛人排行榜的Adapter
 * Created by Administrator on 2016/8/11.
 */
public class ExpertListsAdapter extends BaseAdapter {

    // 加载用的数据
    private List<TotalProfitEntity> listData;
    private Context context;
    private TotalProfitEntity totalProfitEntity;
    // 加载类型
    private int type;

    public ExpertListsAdapter(Context context,int type) {
        super();
        this.context = context;
        this.type = type;
    }

    public void setlistData(ArrayList<TotalProfitEntity> listData) {
        if (listData != null) {
            this.listData = (List<TotalProfitEntity>) listData.clone();
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
            convertView = mInflater.inflate(R.layout.list_item_expert_lists, null);
            holder = new ViewHolder();
            holder.explainStrTv = (TextView) convertView.findViewById(R.id.gross_profit_rate_tv);
            holder.ranking = (TextView) convertView.findViewById(R.id.ranking_tv);
            holder.head = (ImageView) convertView.findViewById(R.id.head_iv);
            holder.name = (TextView) convertView.findViewById(R.id.name_tv);
            holder.averageIncome = (TextView) convertView.findViewById(R.id.average_income_tv);
            holder.grossProfitRate = (TextView) convertView.findViewById(R.id.gross_profit_rate_value_tv);
            holder.winningProbability = (TextView) convertView.findViewById(R.id.winning_probability_value_tv);
            holder.winningProbabilityTv = (TextView) convertView.findViewById(R.id.winning_probability_tv);
            holder.holdingPeriod = (TextView) convertView.findViewById(R.id.holding_period_value_tv);
            holder.position = (TextView) convertView.findViewById(R.id.position_value_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        totalProfitEntity = listData.get(position);
        holder.name.setText(totalProfitEntity.getUsername());
        String rownum = totalProfitEntity.getRownum();
        holder.ranking.setText("");
        if ("1".equals(rownum)){
//            holder.ranking.setBackground(holder.ranking.getResources().getDrawable(R.mipmap.rank1));
            holder.ranking.setBackgroundResource(R.mipmap.rank1);
        }else if ("2".equals(rownum)){
            holder.ranking.setBackgroundResource(R.mipmap.rank2);
        }else if ("3".equals(rownum)){
            holder.ranking.setBackgroundResource(R.mipmap.rank3);
        }else{
            holder.ranking.setText(rownum);
            holder.ranking.setBackgroundResource(R.mipmap.nullimg);
        }
        holder.winningProbability.setText(totalProfitEntity.getSuccess_rate()+"%");
        if(type == 0){
            String weekAvgProfitRate = Utils.getNumberFormat2(totalProfitEntity.getWeek_avg_profit_rate());
            // 常胜牛人
            holder.averageIncome.setText("周平均收益率 "+ weekAvgProfitRate +"%");
            if (Double.valueOf(weekAvgProfitRate)>0){
                holder.averageIncome.setTextColor(holder.averageIncome.getResources().getColor(R.color.color_ef3e3e));
            }else if(Double.valueOf(weekAvgProfitRate)<0){
                holder.averageIncome.setTextColor(holder.averageIncome.getResources().getColor(R.color.color_1bc07d));
            }else{
                holder.averageIncome.setTextColor(holder.averageIncome.getResources().getColor(R.color.color_000000));
            }
            holder.grossProfitRate.setText(Utils.getNumberFormat2(totalProfitEntity.getTotal_rate())+"%");
        }else if(type == 1){
            // 人气牛人
            holder.averageIncome.setText("粉丝数 "+totalProfitEntity.getFans());
            holder.grossProfitRate.setText(Utils.getNumberFormat2(totalProfitEntity.getTotal_rate())+"%");
        }else if(type == 2){
            String totalRate = Utils.getNumberFormat2(totalProfitEntity.getTotal_rate());
            // 总收益榜
            holder.averageIncome.setText("总收益率 "+totalRate+"%");
            if (Double.valueOf(totalRate)>0){
                holder.averageIncome.setTextColor(holder.averageIncome.getResources().getColor(R.color.color_ef3e3e));
            }else if(Double.valueOf(totalRate)<0){
                holder.averageIncome.setTextColor(holder.averageIncome.getResources().getColor(R.color.color_1bc07d));
            }else{
                holder.averageIncome.setTextColor(holder.averageIncome.getResources().getColor(R.color.color_000000));
            }
            holder.grossProfitRate.setText(Utils.getNumberFormat2(totalProfitEntity.getWeek_avg_profit_rate())+"%");
            holder.explainStrTv.setText("周平均收益率 ");
        }else{
            String successRate = Utils.getNumberFormat2(totalProfitEntity.getSuccess_rate());
            // 选股牛人
            holder.averageIncome.setText("选股胜率 "+successRate+"%");
            if (Double.valueOf(successRate)>0){
                holder.averageIncome.setTextColor(holder.averageIncome.getResources().getColor(R.color.color_ef3e3e));
            }else if(Double.valueOf(successRate)<0){
                holder.averageIncome.setTextColor(holder.averageIncome.getResources().getColor(R.color.color_1bc07d));
            }else{
                holder.averageIncome.setTextColor(holder.averageIncome.getResources().getColor(R.color.color_000000));
            }
            holder.grossProfitRate.setText(Utils.getNumberFormat2(totalProfitEntity.getTotal_rate())+"%");
            holder.winningProbabilityTv.setText("周平均收益率 ");
            holder.winningProbability.setText(Utils.getNumberFormat2(totalProfitEntity.getWeek_avg_profit_rate())+"%");
        }
        holder.holdingPeriod.setText(Utils.getNumberFormat2(totalProfitEntity.getAvg_position_day()));
        holder.position.setText(Utils.getNumberFormat2(totalProfitEntity.getPosition())+"%");

        ImageLoader.getInstance().displayImage(Md5Util.getuidstrMd5(Md5Util
                        .getMd5(totalProfitEntity.getUid())),
                holder.head, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());

        return convertView;
    }

    static class ViewHolder {
        // 排名
        TextView ranking;
        // 头像
        ImageView head;
        // 名称
        TextView name;
        // 收益数值
        TextView averageIncome;
        // 总收益利率
        TextView grossProfitRate;
        // 胜率
        TextView winningProbability;
        // 胜率文字
        TextView winningProbabilityTv;
        // 平均持股周期
        TextView holdingPeriod;
        // 仓位
        TextView position;
        // 第一个说明文字
        TextView explainStrTv;
    }
}
