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
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.plan.PlanEntity;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * 计划列表的adapter
 * Created by Administrator on 2017/4/14.
 */
public class PlanAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<PlanEntity> listData;
    public PlanAdapter(Context context) {
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
            convertView = mInflater.inflate(R.layout.list_item_plan, null);
            holder = new ViewHolder();
            // 目标收益
            holder.profitTv = (TextView) convertView.findViewById(R.id.profit_tv);
            // 计划标题
            holder.planTitleTv = (TextView) convertView.findViewById(R.id.plan_title_tv);
            // 状态
            holder.planTypeImg = (ImageView) convertView.findViewById(R.id.plan_type_img);
            holder.planTypeTv = (TextView) convertView.findViewById(R.id.plan_type_tv);
            // 交易时间
            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            // 股票代码
            holder.codeTv = (TextView) convertView.findViewById(R.id.code_tv);
            // 头像图片
            holder.headIm = (ImageView) convertView.findViewById(R.id.head_iv);
            // 用户名称
            holder.nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            // 保证金
            holder.moneyTv = (TextView) convertView.findViewById(R.id.money_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final PlanEntity planEntity = listData.get(position);
        ImageLoader.getInstance().displayImage(planEntity.getAvatar(),
                holder.headIm, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        holder.profitTv.setText(Utils.getNumberFormat2(planEntity.getPlan_ratio())+"%");
        holder.planTitleTv.setText(planEntity.getTitle());
        holder.timeTv.setText(planEntity.getStart().substring(5,10)+"-"+planEntity.getEnd().substring(5,10)+" ("+planEntity.getTrade_days()+"个交易日)");
        String status = planEntity.getStatus();
        // 当未订阅了 进行中 未开始  可以看到代码
        if ("2".equals(planEntity.getDesert()) && ("1".equals(status)||"2".equals(status)) && !Constants.staticmyuidstr.equals(planEntity.getUid())){
            holder.codeTv.setText(planEntity.getStock().substring(0,3)+"***");
            holder.codeTv.setTextColor(holder.codeTv.getResources().getColor(R.color.color_text1));
            holder.codeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }else{
            holder.codeTv.setText(planEntity.getStock());
            holder.codeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 跳转到股票行情
                    Intent intent = new Intent(context,
                            SharesDetailedActivity.class);
                    intent.putExtra("code", planEntity.getStock());
                    intent.putExtra("name", planEntity.getStock_name());
                    context.startActivity(intent);
                }
            });
            holder.codeTv.setTextColor(holder.codeTv.getResources().getColor(R.color.color_toptitle));
        }

        holder.nameTv.setText(planEntity.getUsername());
        holder.nameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到个人主页
                Intent intent = new Intent(context,
                        UserDetailNewActivity.class);
                intent.putExtra("uid", planEntity.getUid());
                context.startActivity(intent);
            }
        });
        holder.moneyTv.setText(planEntity.getMargin());
        holder.planTypeImg.setVisibility(View.GONE);
        holder.planTypeTv.setVisibility(View.GONE);
        if ("1".equals(status)){
            // 未开始
            holder.planTypeTv.setVisibility(View.VISIBLE);
            holder.planTypeTv.setText("未开始");
        }else if ("2".equals(status)){
            // 进行中
            holder.planTypeTv.setVisibility(View.VISIBLE);
            holder.planTypeTv.setText("进行中");
        }else if ("6".equals(planEntity.getStatus())){
            // 核算中
            holder.planTypeTv.setVisibility(View.VISIBLE);
            holder.planTypeTv.setText("核算中");
        }else if ("3".equals(status)){
            // 成功
            holder.planTypeImg.setVisibility(View.VISIBLE);
            holder.planTypeImg.setImageResource(R.mipmap.plan_cg);
        }else if ("4".equals(status)){
            // 失败
            holder.planTypeImg.setVisibility(View.VISIBLE);
            holder.planTypeImg.setImageResource(R.mipmap.plan_sp);
        }else if ("5".equals(status)){
            // 失效
            holder.planTypeImg.setVisibility(View.VISIBLE);
            holder.planTypeImg.setImageResource(R.mipmap.plan_sx);
        }
        return convertView;
    }

    private static class ViewHolder {
        // 目标收益
        TextView profitTv;
        // 计划标题
        TextView planTitleTv;
        // 状态
        ImageView planTypeImg;
        TextView planTypeTv;
        // 交易时间
        TextView timeTv;
        // 股票代码
        TextView codeTv;
        // 头像图片
        ImageView headIm;
        // 用户名称
        TextView nameTv;
        // 保证金
        TextView moneyTv;
    }
}
