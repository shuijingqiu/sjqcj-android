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
        //动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_system_meaage, null);
            holder = new ViewHolder();
            holder.messageTv = (TextView) convertView.findViewById(R.id.message_tv);
            holder.messageTimeTv = (TextView) convertView.findViewById(R.id.message_time_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SystemMessage.datas data = systemMessage.getData().get(position);
        holder.messageTv.setText(data.getBody());
        holder.messageTimeTv.setText(Utils.getStringtoDate1(data.getCtime()));
        return convertView;
    }

    // 追加数据
    public void setAddList(List<SystemMessage.datas> data) {
        systemMessage.getData().addAll(data);
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        TextView messageTv;
        TextView messageTimeTv;
    }

}
