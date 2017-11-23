package com.example.sjqcjstock.adapter.firm;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.firm.FirmUserHomePageActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.firm.FirmDetailEntity;
import com.example.sjqcjstock.fragment.firm.FragmentMatchZsy;

import java.util.ArrayList;
import java.util.List;

/**
 * 实盘比赛总收益列表
 * Created by Administrator on 2017/8/28.
 */
public class FirmHallZsyAdapter  extends BaseAdapter {

    private FragmentMatchZsy fragmentMatchZsy;
    // 加载用的数据
    private List<FirmDetailEntity> listData;
    private FirmDetailEntity firmDetailEntity;
    private Context context;
    // 比赛状态 1报名中  2进行中  3已结束  4待开始
    private String state;

    public FirmHallZsyAdapter(Context context,FragmentMatchZsy fragmentMatchZsy,String state) {
        super();
        this.context = context;
        this.fragmentMatchZsy = fragmentMatchZsy;
        this.state = state;
    }

    public void setlistData(ArrayList<FirmDetailEntity> listData) {
        if (listData != null) {
            this.listData = (List<FirmDetailEntity>) listData.clone();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_firm_hall_zsy, null);
            holder = new ViewHolder();
            holder.nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.rankingTv = (TextView) convertView.findViewById(R.id.ranking_tv);
            holder.totalRatioTv = (TextView) convertView.findViewById(R.id.total_ratio_tv);
            holder.subscribeBt = (Button) convertView.findViewById(R.id.subscribe_bt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        firmDetailEntity = listData.get(position);
        holder.nameTv.setText(firmDetailEntity.getUname());
        holder.rankingTv.setText(firmDetailEntity.getRank());
        String rationStr = firmDetailEntity.getTotal_ratio()+"";
        if (Double.valueOf(rationStr) == 0d){
            holder.totalRatioTv.setTextColor(holder.totalRatioTv.getResources().getColor(R.color.color_article));
        }else if (rationStr.indexOf("-") == -1){
            holder.totalRatioTv.setTextColor(holder.totalRatioTv.getResources().getColor(R.color.color_ef3e3e));
        }else{
            holder.totalRatioTv.setTextColor(holder.totalRatioTv.getResources().getColor(R.color.color_1bc07d));
        }
        holder.totalRatioTv.setText( rationStr+"%");
//        holder.nameTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, UserDetailNewActivity.class);
//                intent.putExtra("uid",listData.get(position).getUid());
//                context.startActivity(intent);
//            }
//        });
        String type = firmDetailEntity.getIs_desert();
        if ("1".equals(state)){
            holder.subscribeBt.setText("查看");
            holder.subscribeBt.setBackgroundResource(R.drawable.buttonstyle13);
        }else{
            // 1 为订阅
            if ("1".equals(type) || firmDetailEntity.getUid().equals(Constants.staticmyuidstr)){
                holder.subscribeBt.setText("已订阅");
                holder.subscribeBt.setBackgroundResource(R.drawable.buttonstyle13);
                holder.subscribeBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirmDetailEntity firmDetailEntity = listData.get(position);
                        Intent intent = new Intent(context, FirmUserHomePageActivity.class);
                        intent.putExtra("id",firmDetailEntity.getId());
                        intent.putExtra("uid",firmDetailEntity.getUid());
                        context.startActivity(intent);
                    }
                });
            }else if ("2".equals(state) ){
                holder.subscribeBt.setText("查看");
                holder.subscribeBt.setBackgroundResource(R.drawable.buttonstyle7);
                holder.subscribeBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirmDetailEntity firmDetailEntity = listData.get(position);
                        fragmentMatchZsy.PayCrystalCoin(firmDetailEntity.getId(),firmDetailEntity.getDesert_reward(),firmDetailEntity.getUid());
                    }
                });
            }else if("3".equals(state)){
                // 比赛以结束
                holder.subscribeBt.setText("查看");
                holder.subscribeBt.setBackgroundResource(R.drawable.buttonstyle7);
                FirmDetailEntity firmDetailEntity = listData.get(position);
                Intent intent = new Intent(context, FirmUserHomePageActivity.class);
                intent.putExtra("id",firmDetailEntity.getId());
                intent.putExtra("uid",firmDetailEntity.getUid());
                context.startActivity(intent);
            }
        }
        return convertView;
    }

    static class ViewHolder {
        // 用户名称
        TextView nameTv;
        // 排名
        TextView rankingTv;
        // 总收益率
        TextView totalRatioTv;
        // 订阅按钮
        Button subscribeBt;
    }
}