package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.IncomeStatementEntity;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class totalrankingfamousAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<IncomeStatementEntity> listData;
    // 展示类容 1 为总收益榜 2为长胜牛人
    private String type = "1";

    public totalrankingfamousAdapter(Context context,String type) {
        super();
        this.context = context;
        this.type = type;
    }

    public void setlistData(ArrayList<IncomeStatementEntity> listData) {
        if (listData != null) {
            this.listData = (ArrayList<IncomeStatementEntity>) listData.clone();
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
        // TODO Auto-generated method stub
        //动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_totalrankingfamous, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.user_image1);
            holder.famousmanname1 = (TextView) convertView.findViewById(R.id.famousmanname1);
            holder.totalscore1 = (TextView) convertView.findViewById(R.id.totalscore1);
            holder.weekly = (TextView) convertView.findViewById(R.id.weekly);
            holder.userLay = (LinearLayout) convertView.findViewById(R.id.user_lay);
            holder.profitTv = (TextView) convertView.findViewById(R.id.profit_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final IncomeStatementEntity incomeStatementEntity = listData.get(position);
        ImageLoader.getInstance().displayImage(incomeStatementEntity.getAvatar_middle(),
                holder.image, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        holder.userLay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid", incomeStatementEntity.getUid());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        holder.famousmanname1.setText(incomeStatementEntity.getUname());
        holder.weekly.setText(incomeStatementEntity.getWeekly());
        if ("2".equals(type)){
            holder.profitTv.setText("周均收益");
            ViewUtil.setViewColor(holder.totalscore1, incomeStatementEntity.getAvg_jefen()+"%");
        }else{
            ViewUtil.setViewColor(holder.totalscore1, incomeStatementEntity.getBallot_jifen()+"%");
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView image;
        TextView famousmanname1;
        TextView totalscore1;
        TextView weekly;
        TextView profitTv;
        LinearLayout userLay;
    }
}
