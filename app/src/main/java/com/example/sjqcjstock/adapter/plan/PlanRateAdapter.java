package com.example.sjqcjstock.adapter.plan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.plan.PlanRateEntity;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * 计划率列表的adapter
 * Created by Administrator on 2017/4/26.
 */
public class PlanRateAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<PlanRateEntity> listData;
    // 1 总收益 2 达标率 3 均收益
    private String type = "1";

    public PlanRateAdapter(Context context,String type) {
        super();
        this.context = context;
        this.type = type;
    }

    public void setlistData(ArrayList<PlanRateEntity> listData) {
        if (listData != null) {
            this.listData = (ArrayList<PlanRateEntity>) listData.clone();
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
            convertView = mInflater.inflate(R.layout.list_item_plan_rate, null);
            holder = new ViewHolder();
            // 头像图片
            holder.headIm = (ImageView) convertView.findViewById(R.id.head_iv);
            // 用户名称
            holder.nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            // 计划个数
            holder.planCountTv = (TextView) convertView.findViewById(R.id.plan_count_tv);
            // 计划率
            holder.planRateTv = (TextView) convertView.findViewById(R.id.plan_rate_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PlanRateEntity planRateEntity = listData.get(position);
        ImageLoader.getInstance().displayImage(planRateEntity.getAvatar(),
                holder.headIm, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        holder.nameTv.setText(planRateEntity.getUsername());
        holder.planCountTv.setText(planRateEntity.getCount());
        if ("1".equals(type)){
            String totalRatio = planRateEntity.getTotal_ratio();
            // 总收益
            holder.planRateTv.setText(totalRatio);
            if (totalRatio.indexOf("-")!=-1){
                holder.planRateTv.setTextColor(holder.planRateTv.getResources().getColor(R.color.color_1bc07d));
            }else{
                holder.planRateTv.setTextColor(holder.planRateTv.getResources().getColor(R.color.color_ef3e3e));
            }
        }else if("2".equals(type)){
            String successRatio = planRateEntity.getSuccess_ratio();
            // 达标率
            holder.planRateTv.setText(successRatio);
            if (successRatio.indexOf("-")!=-1){
                holder.planRateTv.setTextColor(holder.planRateTv.getResources().getColor(R.color.color_1bc07d));
            }else{
                holder.planRateTv.setTextColor(holder.planRateTv.getResources().getColor(R.color.color_ef3e3e));
            }
        }else{
            String avgRatio = planRateEntity.getAvg_ratio();
            // 均收益
            holder.planRateTv.setText(avgRatio);
            if (avgRatio.indexOf("-")!=-1){
                holder.planRateTv.setTextColor(holder.planRateTv.getResources().getColor(R.color.color_1bc07d));
            }else{
                holder.planRateTv.setTextColor(holder.planRateTv.getResources().getColor(R.color.color_ef3e3e));
            }
        }
        return convertView;
    }

    private static class ViewHolder {
        // 头像图片
        ImageView headIm;
        // 用户名称
        TextView nameTv;
        // 计划个数
        TextView planCountTv;
        // 计划率
        TextView planRateTv;

    }
}
