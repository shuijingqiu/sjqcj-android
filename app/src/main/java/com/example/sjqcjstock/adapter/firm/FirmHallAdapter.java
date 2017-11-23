package com.example.sjqcjstock.adapter.firm;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.firm.FirmHallEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 实盘比赛列表
 * Created by Administrator on 2017/8/28.
 */
public class FirmHallAdapter extends BaseAdapter {

    // 加载用的数据
    private List<FirmHallEntity> listData;
    private FirmHallEntity firmHallEntity;
    private Context context;

    public FirmHallAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<FirmHallEntity> listData) {
        if (listData != null) {
            this.listData = (List<FirmHallEntity>) listData.clone();
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
            convertView = mInflater.inflate(R.layout.list_item_firm_hall, null);
            holder = new ViewHolder();
            holder.stateTv = (TextView) convertView.findViewById(R.id.state_tv);
            holder.stateLl = (LinearLayout) convertView.findViewById(R.id.state_ll);
            holder.firmNameTv = (TextView) convertView.findViewById(R.id.firm_name_tv);
//            holder.countTv = (TextView) convertView.findViewById(R.id.count_tv);
            holder.unameTv = (TextView) convertView.findViewById(R.id.uname_tv);
            holder.amountTv = (TextView) convertView.findViewById(R.id.amount_tv);
            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.residueTv = (TextView) convertView.findViewById(R.id.residue_tv);
            holder.matchTv = (TextView) convertView.findViewById(R.id.match_tv);
            holder.residueValueTv = (TextView) convertView.findViewById(R.id.residue_value_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        firmHallEntity = listData.get(position);
        // 设置追加标题
        Spannable titleStr = new SpannableString(firmHallEntity.getTitle());

//        int conntStart = firmHallEntity.getTitle().length();
//        int conntEnd = titleStr.toString().length();
//                // 背景色
//        titleStr.setSpan(new ForegroundColorSpan(Color.rgb(150,150,150)), conntStart, conntEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        // 字体大小
//        titleStr.setSpan(new AbsoluteSizeSpan(13,true), conntStart, conntEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        holder.firmNameTv.setText(titleStr);
        holder.unameTv.setText(firmHallEntity.getSponsor());
        holder.residueTv.setText(firmHallEntity.getResidue()+"天");
        holder.amountTv.setText(firmHallEntity.getAmount());
        holder.timeTv.setText(firmHallEntity.getStart()+"—"+firmHallEntity.getEnd());
        if ("2".equals(firmHallEntity.getState())){
            // 进行中
            holder.stateLl.setBackgroundResource(R.drawable.buttonstyle9);
            holder.stateTv.setText("比赛中");
        }else if ("3".equals(firmHallEntity.getState())){
            // 已结束
            holder.stateLl.setBackgroundResource(R.drawable.buttonstyle13);
            holder.stateTv.setText("已结束");
        }else{
            holder.stateTv.setText("报名中");
            holder.stateLl.setBackgroundResource(R.drawable.buttonstyle12);
//            // 报名中
//            holder.timeTv.setText(firmHallEntity.getJoin_start()+"—"+firmHallEntity.getJoin_end());
//            holder.matchTv.setText("报名：");
//            holder.residueValueTv.setText("平均总收益：");
//            String avgRatio = firmHallEntity.getAvg_ratio();
//            if (avgRatio.indexOf("-") == -1){
//                holder.residueTv.setTextColor(holder.residueTv.getResources().getColor(R.color.color_ef3e3e));
//                holder.residueTv.setText(avgRatio+"%");
//            }else{
//                holder.residueTv.setTextColor(holder.residueTv.getResources().getColor(R.color.color_1bc07d));
//                holder.residueTv.setText(avgRatio+"%");
//            }
        }

        return convertView;
    }

    static class ViewHolder {
        // 比赛状态
        TextView stateTv;
        LinearLayout stateLl;
        // 比赛名称
        TextView firmNameTv;
//        // 参赛人数
//        TextView countTv;
        // 举办人
        TextView unameTv;
        // 总金额
        TextView amountTv;
        // 时间
        TextView timeTv;
        // 剩余天数
        TextView residueTv;
        // 比赛字体
        TextView matchTv;
        // 剩余字体
        TextView residueValueTv;
    }
}
