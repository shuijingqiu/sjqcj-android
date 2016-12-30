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

import com.example.sjqcjstock.Activity.stocks.SubscribeConfirmActivity;
import com.example.sjqcjstock.Activity.stocks.SubscribeOrderListActivity;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.stocks.DesertEntity;
import com.example.sjqcjstock.fragment.stocks.FragmentDynamicOrder;
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
 * 我的订阅牛人的列表
 * Created by Administrator on 2016/12/26.
 */
public class DynamicOredrAdapter extends BaseAdapter {

    // 加载用的数据
    private List<DesertEntity> listData;
    private Context context;
    // 接口返回的数据
    private String rest;
    // 回掉的类
    private FragmentDynamicOrder fragmentDynamicOrder;


    public DynamicOredrAdapter(Context context,FragmentDynamicOrder fragmentDynamicOrder) {
        super();
        this.context = context;
        this.fragmentDynamicOrder = fragmentDynamicOrder;
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
            convertView = mInflater.inflate(R.layout.list_item_dynamic_order, null);
            holder = new ViewHolder();
            holder.headNameRl = (RelativeLayout) convertView.findViewById(R.id.head_name_rl);
            holder.headIm = (ImageView) convertView.findViewById(R.id.head_iv);
            holder.nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.typeTv = (TextView) convertView.findViewById(R.id.type_tv);
//            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.extendedTv = (TextView) convertView.findViewById(R.id.extended_tv);
            holder.cancelTv = (TextView) convertView.findViewById(R.id.cancel_tv);
            holder.seeTv = (TextView) convertView.findViewById(R.id.see_tv);
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
//        holder.timeTv.setText("有效期至："+desertEntity.getExp_time());

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

        if ("1".equals(desertEntity.getIs_extend())){
            holder.extendedTv.setTextColor(holder.extendedTv.getResources().getColor(R.color.color_text1));
        }else{
            holder.extendedTv.setTextColor(holder.extendedTv.getResources().getColor(R.color.color_text2));
        }
        // 延长订阅
        holder.extendedTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(desertEntity.getIs_extend())) {
                    Intent intent = new Intent();
                    intent.setClass(context, SubscribeConfirmActivity.class);
                    intent.putExtra("uid", desertEntity.getPrice_uid());
                    intent.putExtra("name", desertEntity.getPrice_username());
                    intent.putExtra("time", desertEntity.getExp_time());
                    intent.putExtra("type", "1");
                    context.startActivity(intent);
                }
            }
        });
        // 取消订阅
        holder.cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 开线程取消订阅
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List dataList = new ArrayList();
                        dataList.add(new BasicNameValuePair("uid", Constants.staticmyuidstr));
                        dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                        dataList.add(new BasicNameValuePair("id", desertEntity.getId()));
                        rest = HttpUtil.restHttpPost(Constants.moUrl + "/desert/cancel", dataList);
                        handler.sendEmptyMessage(0);
                    }
                }).start();
            }
        });
        // 查看订单列表
        holder.seeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context,SubscribeOrderListActivity.class);
                intent.putExtra("uid",desertEntity.getPrice_uid());
                context.startActivity(intent);
            }
        });
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
//        // 有效日期
//        TextView timeTv;
        // 延长订阅
        TextView extendedTv;
        // 取消订阅
        TextView cancelTv;
        // 再次订阅
        TextView seeTv;
    }
    /**
     * 线程更新Ui
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    try {
                        JSONObject jsonObject = new JSONObject(rest);
                        if ("failed".equals(jsonObject.getString("status"))){
                            Toast.makeText(context, jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Constants.isDynamic = true;
                        if (fragmentDynamicOrder !=null) {
                            fragmentDynamicOrder.getData();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
}