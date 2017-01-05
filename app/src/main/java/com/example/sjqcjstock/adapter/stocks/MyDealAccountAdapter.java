package com.example.sjqcjstock.adapter.stocks;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.stocks.BusinessActivity;
import com.example.sjqcjstock.Activity.stocks.SharesDetailedActivity;
import com.example.sjqcjstock.Activity.stocks.TransactionDetailActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.stocks.PositionEntity;
import com.example.sjqcjstock.netutil.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 交易账户页面的持仓股票信息ListAdaper
 * Created by Administrator on 2016/8/15.
 */
public class MyDealAccountAdapter extends BaseAdapter {

    private List<PositionEntity> listData;
    private Context context;
    private String uid = "";
//    private boolean isRn = false;

    public MyDealAccountAdapter(Context context) {
        super();
        this.context = context;
//        this.isRn = isRn;
    }

    public MyDealAccountAdapter(Context context,String uid) {
        super();
        this.context = context;
        this.uid = uid;
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
            convertView = mInflater.inflate(R.layout.list_item_position, null);
            holder = new ViewHolder();
            holder.name_code = (TextView) convertView.findViewById(R.id.name_code_tv);
            holder.presentPrice = (TextView) convertView.findViewById(R.id.present_price_value_tv);
            holder.profit = (TextView) convertView.findViewById(R.id.profit_value_tv);
            holder.latestMarketPrice = (TextView) convertView.findViewById(R.id.latest_market_price_value_tv);
            holder.averageCost = (TextView) convertView.findViewById(R.id.average_cost_value_tv);
            holder.positionNumber = (TextView) convertView.findViewById(R.id.position_number_value_tv);
            holder.canBuyQuantity = (TextView) convertView.findViewById(R.id.can_buy_quantity_value_tv);
            holder.transactionDetail = (TextView) convertView.findViewById(R.id.transaction_detail_tv);
            holder.purchase = (TextView) convertView.findViewById(R.id.purchase_tv);
            holder.sellOut = (TextView) convertView.findViewById(R.id.sell_out_tv);
//            holder.operationColumnLl = (LinearLayout) convertView.findViewById(R.id.operation_column_ll);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final PositionEntity positionEntity = listData.get(position);
        final String stock =  positionEntity.getStock();
        final String name =  positionEntity.getStock_name();
        // 可卖数量
        final String number = positionEntity.getAvailable_number();
//        if (!isRn){
//            holder.operationColumnLl.setVisibility(View.GONE);
//        }
        holder.name_code.setText(positionEntity.getStock_name() + "(" + stock+")");
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
        //平均成本价
        holder.averageCost.setText(positionEntity.getCost_price());
        // 持仓数量
        int positionValue = Integer.valueOf(positionEntity.getPosition_number());
        holder.positionNumber.setText(positionValue+"");
        // 可卖数量
        holder.canBuyQuantity.setText(number);
        // 现价
        String price = positionEntity.getLatest_price();
        // 如果最新市价不为空那么就计算盈利和最新市价
        if(!"".equals(price.trim())){
            // 现价
            holder.presentPrice.setText(price);
            if ("0".equals(positionEntity.getIsType())){
                holder.presentPrice.setTextColor(holder.presentPrice.getResources().getColor(R.color.color_ef3e3e));
            }else{
                holder.presentPrice.setTextColor(holder.presentPrice.getResources().getColor(R.color.color_1bc07d));
            }
            // 最新市价
            String latestMarketPriceStr = Utils.getNumberFormat1(Double.valueOf(price) * positionValue + "");
            holder.latestMarketPrice.setText(latestMarketPriceStr);
            // 收益
            String profitStr = Utils.getNumberFormat2(positionEntity.getRatio() + "");
            holder.profit.setText(profitStr+"%");
            if (Double.valueOf(profitStr)>0){
                holder.profit.setTextColor(holder.profit.getResources().getColor(R.color.color_ef3e3e));
            }else if((Double.valueOf(profitStr)<0)){
                holder.profit.setTextColor(holder.profit.getResources().getColor(R.color.color_1bc07d));
            }
            else{
                holder.profit.setTextColor(holder.profit.getResources().getColor(R.color.color_000000));
            }
        }
        holder.transactionDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TransactionDetailActivity.class);
                intent.putExtra("code",stock);
                intent.putExtra("name",name);
                intent.putExtra("uid",uid);
                context.startActivity(intent);
            }
        });
        holder.purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BusinessActivity.class);
                intent.putExtra("type", "1");
                intent.putExtra("code",  stock);
                context.startActivity(intent);
            }
        });
        holder.sellOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BusinessActivity.class);
                intent.putExtra("type", "2");
                intent.putExtra("code",  stock);
                if (Constants.staticmyuidstr.equals(uid)){
                    intent.putExtra("number",  number);
                }
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView name_code;
        // 现价
        TextView presentPrice;
        // 收益
        TextView profit;
        // 最新市价
        TextView latestMarketPrice;
        // 平均成本
        TextView averageCost;
        // 持仓数量
        TextView positionNumber;
        // 可卖数量
        TextView canBuyQuantity;
        // 成交明细
        TextView transactionDetail;
        // 买入
        TextView purchase;
        // 卖出
        TextView sellOut;
//        // 操作栏
//        LinearLayout operationColumnLl;
    }
}
