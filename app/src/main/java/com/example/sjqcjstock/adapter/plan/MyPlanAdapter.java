package com.example.sjqcjstock.adapter.plan;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.stocks.SharesDetailedActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.plan.PlanEntity;
import com.example.sjqcjstock.netutil.Utils;

import java.util.ArrayList;

/**
 * 我的计划列表的adapter
 * Created by Administrator on 2017/4/18
 */
public class MyPlanAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<PlanEntity> listData;
    public MyPlanAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<PlanEntity> listData) {
        if (listData != null) {
            this.listData = (ArrayList<PlanEntity>) listData.clone();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        if (listData == null){
            return 0;
        }
        return listData.size();
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
        //动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_my_plan, null);
            holder = new ViewHolder();
            // 当前收益
            holder.currentProfitTv = (TextView) convertView.findViewById(R.id.current_profit_tv);
            // 目标收益
            holder.targetProfitTv = (TextView) convertView.findViewById(R.id.target_profit_tv);
            // 计划标题
            holder.planTitleTv = (TextView) convertView.findViewById(R.id.plan_title_tv);
            // 状态
            holder.planTypeImg = (ImageView) convertView.findViewById(R.id.plan_type_img);
            holder.planTypeTv = (TextView) convertView.findViewById(R.id.plan_type_tv);
            // 交易时间
            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            // 股票名称
            holder.codeNameTv = (TextView) convertView.findViewById(R.id.code_name_tv);
            // 购买人数
            holder.countTv = (TextView) convertView.findViewById(R.id.count_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final PlanEntity planEntity = listData.get(position);

        double income = Double.valueOf(Utils.getNumberFormat2(planEntity.getIncome()));
        //  当前收益
        holder.currentProfitTv.setText( income + "%");
        if (income >= 0) {
            holder.currentProfitTv.setTextColor(holder.currentProfitTv.getResources().getColor(R.color.color_ef3e3e));
        } else {
            holder.currentProfitTv.setTextColor(holder.currentProfitTv.getResources().getColor(R.color.color_1bc07d));
        }

        holder.targetProfitTv.setText(Utils.getNumberFormat2(planEntity.getPlan_ratio())+"%");
        holder.planTitleTv.setText(planEntity.getTitle());
        holder.timeTv.setText(planEntity.getStart().substring(5,10)+"-"+planEntity.getEnd().substring(5,10)+" ("+planEntity.getTrade_days()+"个交易日)");
        holder.codeNameTv.setText(planEntity.getStock_name()+"("+planEntity.getStock()+")");
        holder.codeNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SharesDetailedActivity.class);
                intent.putExtra("name",planEntity.getStock_name());
                intent.putExtra("code",planEntity.getStock());
                context.startActivity(intent);
            }
        });
        holder.countTv.setText(planEntity.getBuy_count()+"人");
        holder.planTypeImg.setVisibility(View.GONE);
        holder.planTypeTv.setVisibility(View.GONE);
        if ("1".equals(planEntity.getStatus())){
            // 未开始
            holder.planTypeTv.setVisibility(View.VISIBLE);
            holder.planTypeTv.setText("未开始");
        }else if ("2".equals(planEntity.getStatus())){
            // 进行中
            holder.planTypeTv.setVisibility(View.VISIBLE);
            holder.planTypeTv.setText("进行中");
        }else if ("6".equals(planEntity.getStatus())){
            // 核算中
            holder.planTypeTv.setVisibility(View.VISIBLE);
            holder.planTypeTv.setText("核算中");
        }else if ("3".equals(planEntity.getStatus())){
            // 成功
            holder.planTypeImg.setVisibility(View.VISIBLE);
            holder.planTypeImg.setImageResource(R.mipmap.plan_cg);
        }else if ("4".equals(planEntity.getStatus())){
            // 失败
            holder.planTypeImg.setVisibility(View.VISIBLE);
            holder.planTypeImg.setImageResource(R.mipmap.plan_sp);
        }else if ("5".equals(planEntity.getStatus())){
            // 失效
            holder.planTypeImg.setVisibility(View.VISIBLE);
            holder.planTypeImg.setImageResource(R.mipmap.plan_sx);
        }
        return convertView;
    }

    private static class ViewHolder {
        // 当前收益
        TextView currentProfitTv;
        // 目标收益
        TextView targetProfitTv;
        // 计划标题
        TextView planTitleTv;
        // 状态
        ImageView planTypeImg;
        TextView planTypeTv;
        // 交易时间
        TextView timeTv;
        // 股票代码名称
        TextView codeNameTv;
        // 购买人数
        TextView countTv;

    }
}
