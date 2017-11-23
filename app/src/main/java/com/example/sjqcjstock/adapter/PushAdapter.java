package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.PushSettingsActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.PushEntity;

import java.util.ArrayList;

/**
 * 推送加载列表
 */
public class PushAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<PushEntity> listData;
    private PushSettingsActivity pushSettingsActivity;

    public PushAdapter(Context context, PushSettingsActivity pushSettingsActivity) {
        super();
        this.context = context;
        this.pushSettingsActivity = pushSettingsActivity;
    }

    public void setlistData(ArrayList<PushEntity> listData) {
        if (this.listData != null) {
            this.listData.clear();
        }
        this.listData = (ArrayList<PushEntity>) listData.clone();
        notifyDataSetChanged();
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
        //动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_push, null);
            holder = new ViewHolder();
            holder.nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.stateCb = (CheckBox) convertView.findViewById(R.id.state_cb);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PushEntity pushEntity= listData.get(position);
        holder.nameTv.setText(pushEntity.getName());
        holder.stateCb.setTag(pushEntity.getId());
        if ("2".equals(pushEntity.getState())){
            holder.stateCb.setChecked(false);
        }else{
            holder.stateCb.setChecked(true);
        }
        holder.stateCb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String state;
                String id;
                CheckBox checkBox = (CheckBox)v;
                id = checkBox.getTag().toString();
                if (checkBox.isChecked()){
                    state = "1";
                }else{
                    state = "2";
                }
                pushSettingsActivity.updatePush(state,id);
            }
        });
        return convertView;
    }

    public static class ViewHolder {
        TextView nameTv;
        CheckBox stateCb;
    }

}
