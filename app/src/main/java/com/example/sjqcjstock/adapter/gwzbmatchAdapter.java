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
import android.widget.ListView;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class gwzbmatchAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, Object>> listData;

    //买入卖出列表
    private ListView buyorsellflist1;

    private buyorsellstockAdapter buyorsellstockAdapter;
    private ArrayList<HashMap<String, Object>> listgwzbmatchData2;

    public gwzbmatchAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<HashMap<String, Object>> listData) {
        if (listData != null) {
            this.listData = (ArrayList<HashMap<String, Object>>) listData.clone();
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
            convertView = mInflater.inflate(R.layout.list_item_gwzbmatch, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.user_image1);
            holder.incomerate1 = (TextView) convertView.findViewById(R.id.incomerate1);
            holder.famousmanname1 = (TextView) convertView.findViewById(R.id.famousmanname1);
            holder.max_assetsstr = (TextView) convertView.findViewById(R.id.allmax_assets1);
            holder.userLay = (LinearLayout) convertView.findViewById(R.id.user_lay);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        ImageLoader.getInstance().displayImage((String) listData.get(position).
                        get("uidstrimg"),
                holder.image, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());

        holder.userLay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid", (String) listData.get(position).get("uid"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } finally {

                }

            }
        });
        ViewUtil.setViewColor(holder.incomerate1, listData.get(position).get("income").toString());
        holder.famousmanname1.setText((String) listData.get(position).get("namestr"));
        holder.max_assetsstr.setText((CharSequence) listData.get(position).get("max_assetsstr"));
        //买入卖出列表
        buyorsellflist1 = (ListView) convertView.findViewById(R.id.buyorsellflist1);
        listgwzbmatchData2 = new ArrayList<HashMap<String, Object>>();
        buyorsellstockAdapter = new com.example.sjqcjstock.adapter.buyorsellstockAdapter
                (context.getApplicationContext(), listgwzbmatchData2);
        buyorsellflist1.setAdapter(buyorsellstockAdapter);

        String suggeststr = (String) listData.get(position).get("suggeststr");
        List<Map<String, Object>> suggeststrlists = new ArrayList<Map<String, Object>>();
        if (!"".equals(suggeststr)) {
            suggeststrlists = JsonTools.listKeyMaps(suggeststr);
        }
        if (suggeststrlists.size() == 0) {
            HashMap<String, Object> map3 = new HashMap<String, Object>();
            map3.put("shopstr", "空仓");
            map3.put("shopnamestr", "");
            map3.put("sharesstr", "");
            map3.put("shopnumstr", "");
            map3.put("shopmoneystr", "");

            listgwzbmatchData2.add(map3);
        }
        for (Map<String, Object> suggeststrmap : suggeststrlists) {

            HashMap<String, Object> map3 = new HashMap<String, Object>();
            String uid2str = suggeststrmap.get("uid").toString();
            String shopstr = suggeststrmap.get("shop").toString();
            if ("0".equals(shopstr)) {
                shopstr = "买入";
            } else if ("1".equals(shopstr)) {
                shopstr = "卖出";
            } else if ("2".equals(shopstr)) {
                shopstr = "持有";
            }
            String shopnamestr = suggeststrmap.get("shopname").toString();
            String sharesstr = suggeststrmap.get("shares").toString();
            String shopnumstr = suggeststrmap.get("shopnum").toString();
            String shopmoneystr = suggeststrmap.get("shopmoney").toString();
            map3.put("uid2str", uid2str);
            map3.put("shopstr", shopstr);
            map3.put("shopnamestr", shopnamestr);
            map3.put("sharesstr", sharesstr);
            map3.put("shopnumstr", shopnumstr + "股");
            map3.put("shopmoneystr", shopmoneystr);
            listgwzbmatchData2.add(map3);
            listgwzbmatchData2.size();
        }
        buyorsellstockAdapter.notifyDataSetChanged();
        Utils.setListViewHeightBasedOnChildren(buyorsellflist1);
        return convertView;
    }

    static class ViewHolder {
        ImageView image;
        TextView incomerate1;
        TextView famousmanname1;
        TextView max_assetsstr;
        LinearLayout userLay;
    }


}
