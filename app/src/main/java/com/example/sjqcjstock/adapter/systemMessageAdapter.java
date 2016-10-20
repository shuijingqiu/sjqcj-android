package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.SystemMessage;
import com.example.sjqcjstock.netutil.Utils;

import java.util.List;

/**
 * 加载系统消息的控制器
 * Created by Administrator on 2016/5/4.
 */
public class systemMessageAdapter extends BaseAdapter {

    // 要加载的数据集合
    private SystemMessage systemMessage;
    private Context context;

    public systemMessageAdapter(Context context) {
        this.context = context;
    }

    public void setSystemMessage(SystemMessage systemMessage) {
        this.systemMessage = systemMessage;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return systemMessage == null ? 0 : systemMessage.getData().size();
    }

    @Override
    public Object getItem(int position) {
        return systemMessage.getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SystemMessage.datas data = systemMessage.getData().get(position);
        return makeItemView(data);
    }

    // 追加数据
    public void setAddList(List<SystemMessage.datas> data) {
        systemMessage.getData().addAll(data);
        notifyDataSetChanged();
    }

    // 绘制Item的函数
    public View makeItemView(SystemMessage.datas data) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 使用View的对象itemView与R.layout.item关联
        View itemView = inflater
                .inflate(R.layout.list_item_system_meaage, null);
        TextView message = (TextView) itemView.findViewById(R.id.message_tv);
        TextView messageTime = (TextView) itemView.findViewById(R.id.message_time_tv);
        TextView feedid = (TextView) itemView.findViewById(R.id.feed_id_tv);
        message.setText(data.getBody());
        messageTime.setText(Utils.getStringtoDate1(data.getCtime()));
        feedid.setText(data.getFeed_id());
        return itemView;
    }
}
