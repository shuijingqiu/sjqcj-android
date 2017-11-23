package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.CrystalBwater;
import com.example.sjqcjstock.netutil.Utils;

import java.util.List;

/**
 * 加载水晶币流动的控制器
 * Created by Administrator on 2016/5/6.
 */
public class CrystalBwaterAdapter extends BaseAdapter {
    // 要加载的数据集合
    private List<CrystalBwater> crystalBwaterList;
    private Context context;

    public CrystalBwaterAdapter(Context context) {
        this.context = context;
    }

    public void setCrystalBwater(List<CrystalBwater> crystalBwaterList) {
        if (crystalBwaterList!=null){
            this.crystalBwaterList = crystalBwaterList;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return crystalBwaterList == null ? 0 : crystalBwaterList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_crystal_bwater, null);
            holder = new ViewHolder();
            holder.time = (TextView) convertView.findViewById(R.id.bwater_time_tv);
            holder.name = (TextView) convertView.findViewById(R.id.bwater_name_tv);
            holder.typeTv = (TextView) convertView.findViewById(R.id.bwater_type_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CrystalBwater msg = crystalBwaterList.get(position);
        holder.name.setText(msg.getUname());
        holder.time.setText(Utils.getStringtoDate1(msg.getTime()));
        String type = msg.getType();
        if ("0".equals(type)) {
            holder.typeTv.setText(msg.getRetype()+"：+" + msg.getAmount());
            holder.typeTv.setTextColor(holder.typeTv.getResources().getColor(R.color.color_ef3e3e));
        } else if ("1".equals(type)) {
            holder.typeTv.setText(msg.getRetype()+"：-" + msg.getAmount());
            holder.typeTv.setTextColor(holder.typeTv.getResources().getColor(R.color.color_1bc07d));
        } else if ("2".equals(type)) {
            holder.typeTv.setText(msg.getRetype()+"：" + msg.getAmount());
            holder.typeTv.setTextColor(holder.typeTv.getResources().getColor(R.color.color_ef3e3e));
        }
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到个人中心
                Intent intent = new Intent(context, UserDetailNewActivity.class);
                intent.putExtra("uid", crystalBwaterList.get(position).getAssist_uid());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public static class ViewHolder {
        TextView time;
        TextView name;
        TextView typeTv;
    }

}
