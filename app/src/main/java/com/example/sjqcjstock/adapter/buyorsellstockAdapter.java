package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.stocks.SharesDetailedActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.StarcraftEntity;
import com.example.sjqcjstock.netutil.Utils;

import java.util.ArrayList;

public class buyorsellstockAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<StarcraftEntity.Suggest> listData;

    public buyorsellstockAdapter(Context context, ArrayList<StarcraftEntity.Suggest> listData) {
        super();
        this.context = context;
        this.listData = listData;
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
        //动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_buyorsellstock, null);
            holder = new ViewHolder();
            holder.shoptype1 = (TextView) convertView.findViewById(R.id.shoptype1);
            holder.shopname1 = (TextView) convertView.findViewById(R.id.shopname1);
            holder.tradeprice1 = (TextView) convertView.findViewById(R.id.tradeprice1);
            holder.tradecount1 = (TextView) convertView.findViewById(R.id.tradecount1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        final StarcraftEntity.Suggest suggest = listData.get(position);

        String shopstr = suggest.getShop();
        if ("0".equals(shopstr)) {
            shopstr = "买入";
        } else if ("1".equals(shopstr)) {
            shopstr = "卖出";
        } else if ("2".equals(shopstr)) {
            shopstr = "持有";
        }
        holder.shoptype1.setText(shopstr);
        holder.shopname1.setText(suggest.getShopname());
        holder.tradeprice1.setText(suggest.getShopmoney());
        holder.tradecount1.setText(suggest.getShopnum()+ "股");
        holder.shopname1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object name = suggest.getShopname();
                if (name != null && !"".equals(name.toString()+"")){
                    Intent intent = new Intent(context,SharesDetailedActivity.class);
                    intent.putExtra("name",name.toString());
                    intent.putExtra("code", Utils.jieQuSharesCode(suggest.getShares()));
                    context.startActivity(intent);
                }
            }
        });



        return convertView;
    }

    static class ViewHolder {
        TextView shoptype1;
        TextView shopname1;
        TextView tradeprice1;
        TextView tradecount1;


    }
}
