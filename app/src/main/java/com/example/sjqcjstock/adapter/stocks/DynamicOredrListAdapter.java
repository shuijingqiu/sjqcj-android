package com.example.sjqcjstock.adapter.stocks;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjqcjstock.Activity.stocks.MyDynamicOrderActivity;
import com.example.sjqcjstock.Activity.stocks.SubscribeConfirmActivity;
import com.example.sjqcjstock.Activity.stocks.SubscribeOrderListActivity;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.stocks.DesertEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Md5Util;
import com.example.sjqcjstock.netutil.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的订阅订单列表
 * Created by Administrator on 2016/12/26.
 */
public class DynamicOredrListAdapter extends BaseAdapter {

    // 加载用的数据
    private List<DesertEntity> listData;
    private Context context;
    // 接口返回的数据
    private String rest;

    public DynamicOredrListAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<DesertEntity> listData) {
        if (listData != null) {
            this.listData = (List<DesertEntity>) listData.clone();
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
            convertView = mInflater.inflate(R.layout.list_item_dynamic_order_list, null);
            holder = new ViewHolder();
            holder.headNameRl = (RelativeLayout) convertView.findViewById(R.id.head_name_rl);
            holder.headIm = (ImageView) convertView.findViewById(R.id.head_iv);
            holder.nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.typeTv = (TextView) convertView.findViewById(R.id.type_tv);
            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final DesertEntity desertEntity = listData.get(position);
        // 跳转到跟人中心
        holder.headNameRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(),
                        UserDetailNewActivity.class);
                // 要修改的
                intent.putExtra("uid", desertEntity.getPrice_uid());
                intent.putExtra("type", "1");
                context.startActivity(intent);
            }
        });
        holder.nameTv.setText(desertEntity.getPrice_username());
        holder.timeTv.setText("有效期至："+desertEntity.getExp_time());

        if ("1".equals(desertEntity.getStatus())){
            String date = "0";
            try {
                date = Utils.CalculatedDays(desertEntity.getExp_time());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.typeTv.setText(date + "天后到期");
            holder.typeTv.setTextColor(holder.typeTv.getResources().getColor(R.color.color_ef3e3e));
        }else{
            holder.typeTv.setText("已取消");
            holder.typeTv.setTextColor(holder.typeTv.getResources().getColor(R.color.color_text2));
        }
        ImageLoader.getInstance().displayImage(Md5Util.getuidstrMd5(Md5Util
                        .getMd5(desertEntity.getPrice_uid())),
                holder.headIm, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        return convertView;
    }

    static class ViewHolder {
        // 头像栏的操作
        RelativeLayout headNameRl;
        // 头像
        ImageView headIm;
        // 用户名称
        TextView nameTv;
        // 类型（多久到期）
        TextView typeTv;
        // 有效日期
        TextView timeTv;
    }
}