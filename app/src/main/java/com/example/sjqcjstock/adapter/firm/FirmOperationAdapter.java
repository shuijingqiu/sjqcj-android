package com.example.sjqcjstock.adapter.firm;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.stocks.SharesDetailedActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.firm.FirmTransEntity;
import com.example.sjqcjstock.netutil.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 最新操作的列表
 * Created by Administrator on 2017/9/28.
 */
public class FirmOperationAdapter extends BaseAdapter {

    // 加载用的数据
    private List<FirmTransEntity> listData;
    private FirmTransEntity firmTransEntity;
    private Context context;
    // 交易价格
    private String priceStr;
    // 交易金额
    private String numberStr;


    public FirmOperationAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<FirmTransEntity> listData) {
        if (listData != null) {
            this.listData = (List<FirmTransEntity>) listData.clone();
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
            convertView = mInflater.inflate(R.layout.list_item_firm_new_operation, null);
            holder = new ViewHolder();
            holder.nameCode = (TextView) convertView.findViewById(R.id.name_code_tv);
            holder.type = (TextView) convertView.findViewById(R.id.type_tv);
            holder.time = (TextView) convertView.findViewById(R.id.time_tv);
            holder.price = (TextView) convertView.findViewById(R.id.price_tv);
            holder.money = (TextView) convertView.findViewById(R.id.money_tv);
            holder.number = (TextView) convertView.findViewById(R.id.number_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        firmTransEntity = listData.get(position);
        holder.nameCode.setText(firmTransEntity.getStock_name()+"("+firmTransEntity.getStock()+")");
        priceStr = firmTransEntity.getPrice();
        numberStr = firmTransEntity.getNumber();
        holder.price.setText(priceStr);
        holder.number.setText(numberStr);
        holder.time.setText(firmTransEntity.getTime());
        holder.money.setText(Utils.getNumberFormat2(Double.valueOf(priceStr)*Double.valueOf(numberStr)+""));
        String type = firmTransEntity.getType();
        // 1代表买入2代表卖出
        if ("2".equals(type)){
            holder.type.setText("卖");
            holder.type.setBackgroundColor(holder.type.getResources().getColor(R.color.color_1bc07d));
        }else{
            holder.type.setText("买");
            holder.type.setBackgroundColor(holder.type.getResources().getColor(R.color.color_ef3e3e));
        }

        holder.nameCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SharesDetailedActivity.class);
                intent.putExtra("code",listData.get(position).getStock());
                intent.putExtra("name",listData.get(position).getStock_name());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView nameCode;
        TextView type;
        TextView time;
        // 交易价格
        TextView price;
        // 交易金额
        TextView money;
        // 交易数量
        TextView number;
    }
}