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
import com.example.sjqcjstock.entity.stocks.GeniusEntity;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Md5Util;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 牛人动态的Adapter
 * Created by Administrator on 2016/8/16.
 */
public class DynamicExpertAdapter extends BaseAdapter {

    // 加载用的数据
    private List<GeniusEntity> listData;
    private Context context;
    // 买卖类型
    private String type;

    public DynamicExpertAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<GeniusEntity> listData) {
        if (listData != null) {
            this.listData = (List<GeniusEntity>) listData.clone();
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
            holder.totalYield = (TextView) convertView.findViewById(R.id.total_yield_tv);
            holder.nameCode = (TextView) convertView.findViewById(R.id.name_code_tv);
            holder.toBuy = (TextView) convertView.findViewById(R.id.to_buy_tv);
            holder.type = (TextView) convertView.findViewById(R.id.business_tv);
            holder.returnRate = (TextView) convertView.findViewById(R.id.return_rate_value_tv);
            holder.returnRateTv = (TextView) convertView.findViewById(R.id.return_rate_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final GeniusEntity geniusEntity = listData.get(position);
        // 跳转到跟人中心
        holder.headNameRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(),
                        UserDetailNewActivity.class);
                // 要修改的
                intent.putExtra("uid",geniusEntity.getUid());
                intent.putExtra("type", "1");
                context.startActivity(intent);
            }
        });
        type = geniusEntity.getType();
        // 跳转到买卖页面
        holder.toBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1代表买入2代表卖出
                if("1".equals(type)){
                    Intent intent = new Intent(context.getApplicationContext(),
                            BusinessActivity.class);
                    // 要修改的
                    intent.putExtra("type", "1");
                    intent.putExtra("code", geniusEntity.getStock());
                    intent.putExtra("name", geniusEntity.getStock_name());
                    context.startActivity(intent);
                }else{
                    Intent intent1 = new Intent(context.getApplicationContext(),
                            UserDetailNewActivity.class);
                    // 要修改的
                    intent1.putExtra("uid",geniusEntity.getUid());
                    intent1.putExtra("type", "1");
                    context.startActivity(intent1);
                }
            }
        });
        holder.name.setText(geniusEntity.getUsername());
        holder.time.setText(geniusEntity.getTime().substring(0,10));
        // 总收益
        String totalRate = geniusEntity.getTotal_rate();
        if (totalRate!=null && Double.valueOf(totalRate)>=0){
            holder.totalYield.setTextColor(holder.totalYield.getResources().getColor(R.color.color_ef3e3e));
        }else{
            holder.totalYield.setTextColor(holder.totalYield.getResources().getColor(R.color.color_1bc07d));
        }
        holder.totalYield.setText(geniusEntity.getTotal_rate()+"%");
        holder.nameCode.setText(geniusEntity.getStock_name()+"("+geniusEntity.getStock()+")");
        // 1代表买入2代表卖出
        if("1".equals(type)){
            holder.type.setText("买");
            holder.type.setTextColor(holder.type.getResources().getColor(R.color.color_ef3e3e));
            holder.returnRate.setText(geniusEntity.getPrice());
            holder.returnRateTv.setText("买入价");
            holder.toBuy.setText("跟买");
        }else{
            holder.type.setText("卖");
            holder.type.setTextColor(holder.type.getResources().getColor(R.color.color_1bc07d));
            // 收益率
            String ratio = geniusEntity.getRatio();
            holder.returnRate.setText(ratio+"%");
            holder.returnRateTv.setText("收益率");
            if (ratio!=null && Double.valueOf(ratio)>=0){
                holder.returnRate.setTextColor(holder.returnRate.getResources().getColor(R.color.color_ef3e3e));
            }else{
                holder.returnRate.setTextColor(holder.returnRate.getResources().getColor(R.color.color_1bc07d));
            }
            holder.toBuy.setText("查看");
        }
        ImageLoader.getInstance().displayImage(Md5Util.getuidstrMd5(Md5Util
                        .getMd5(geniusEntity.getUid())),
                holder.head, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());

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
        // 总收益率
        TextView totalYield;
        // 股票名称和代码
        TextView nameCode;
        // 跟买
        TextView toBuy;
        // 买卖类型
        TextView type;
        // 股票当前盈利率
        TextView returnRate;
        // 收益率 买入价
        TextView returnRateTv;
    }
}
