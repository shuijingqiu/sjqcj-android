package com.example.sjqcjstock.adapter.stocks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.stocks.MyDealAccountActivity;
import com.example.sjqcjstock.Activity.stocks.SharesDetailedActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.stocks.PositionEntity;
import com.example.sjqcjstock.netutil.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 当前委托的EntrustAdapter
 * Created by Administrator on 2016/8/15.
 */
public class EntrustAdapter extends BaseAdapter {

    private List<PositionEntity> listData;
    private Context context;
    private MyDealAccountActivity myDealAccountActivity;

    public EntrustAdapter(Context context, MyDealAccountActivity myDealAccountActivity) {
        super();
        this.context = context;
        this.myDealAccountActivity = myDealAccountActivity;
    }
    public void setlistData(ArrayList<PositionEntity> listData) {
        if (listData != null) {
            this.listData = (List<PositionEntity>) listData.clone();
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
            convertView = mInflater.inflate(R.layout.list_item_entrust, null);
            holder = new ViewHolder();
            holder.name_code = (TextView) convertView.findViewById(R.id.name_code_tv);
            holder.type = (TextView) convertView.findViewById(R.id.type_tv);
            holder.state = (TextView) convertView.findViewById(R.id.state_tv);
            holder.entrustPrice = (TextView) convertView.findViewById(R.id.entrust_price_value_tv);
            holder.entrustTime = (TextView) convertView.findViewById(R.id.entrust_time_value_tv);
            holder.entrustNumber = (TextView) convertView.findViewById(R.id.entrust_number_value_tv);
            holder.frozenFund = (TextView) convertView.findViewById(R.id.frozen_fund_value_tv);
            holder.killAnOrder = (TextView) convertView.findViewById(R.id.kill_an_order_tv);
            holder.frozenFundTv = (TextView) convertView.findViewById(R.id.frozen_fund_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final PositionEntity positionEntity = listData.get(position);
        holder.name_code.setText(positionEntity.getStock_name() + "(" + positionEntity.getStock()+")");
        holder.name_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent();
                inten.putExtra("name", positionEntity.getStock_name());
                inten.putExtra("code", positionEntity.getStock());
                inten.setClass(context, SharesDetailedActivity.class);
                context.startActivity(inten);
            }
        });
        // 单号的id
        final String entrustId = positionEntity.getId();
        // 撤单的单机时间
        holder.killAnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        new AlertDialog.Builder(context)
            .setMessage("确认撤单吗？")
            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    myDealAccountActivity.killAnOrderClick(entrustId);
                }
            })
            .setNegativeButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }).show();
            }
        });
        String number = positionEntity.getNumber();
        String price = positionEntity.getPrice();
        // 1代表买入2代表卖出
        if("1".equals(positionEntity.getType())){
            holder.type.setText("买入");
            holder.type.setBackgroundColor(holder.type.getResources().getColor(R.color.color_ef3e3e));

            String frozenFundPirce = Double.valueOf(price) * Double.valueOf(number)+Double.valueOf(positionEntity.getFee())+"";
            holder.frozenFundTv.setText("冻结资金");
            holder.frozenFund.setText(Utils.getNumberFormat2(frozenFundPirce));
        }else{
            holder.type.setText("卖出");
            holder.type.setBackgroundColor(holder.type.getResources().getColor(R.color.color_5471ef));

            holder.frozenFundTv.setText("冻结数量");
            holder.frozenFund.setText(number);
        }
        //0代表待成交 1代表成交  2代表撤单
        String status = positionEntity.getStatus();
        if ("0".equals(status)) {
            holder.state.setText("已报");
        }
        holder.entrustPrice.setText(price);
        holder.entrustNumber.setText(number);
        holder.entrustTime.setText(positionEntity.getTime());

        return convertView;
    }

    static class ViewHolder {
        TextView name_code;
        // 类型
        TextView type;
        // 状态
        TextView state;
        // 委托价格
        TextView entrustPrice;
        // 委托时间
        TextView entrustTime;
        // 委托数量
        TextView entrustNumber;
        // 冻结资金
        TextView frozenFund;
        // 撤单
        TextView killAnOrder;
        // 冻结的状态
        TextView frozenFundTv;
    }
}
