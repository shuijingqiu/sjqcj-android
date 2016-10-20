package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sjqcjstock.R;

import java.util.ArrayList;
import java.util.HashMap;

public class InformAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, String>> listData;

    public InformAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<HashMap<String, String>> listData) {
        if (this.listData != null)
            this.listData.clear();
        this.listData = (ArrayList<HashMap<String, String>>) listData.clone();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        //return imagesUrl.length;
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
            convertView = mInflater.inflate(R.layout.list_item_inform, null);
            holder = new ViewHolder();
            holder.informtitle1 = (TextView) convertView.findViewById(R.id.informtitle1);
            holder.create_time1 = (TextView) convertView.findViewById(R.id.create_time1);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //TextView informtitle1=(TextView)convertView.findViewById(R.id.informtitle1);
        holder.informtitle1.setText((String) listData.get(position).get("news_titlestr"));
//		
        //TextView create_time1=(TextView)convertView.findViewById(R.id.create_time1);
        holder.create_time1.setText((String) listData.get(position).get("created"));
//		
        //TextView id=(TextView)convertView.findViewById(R.id.create_time1);
        //id.setText((String)listData.get(position).get("friend_id"));
//		
//		CheckBox check=(CheckBox)convertView.findViewById(R.id.selected);

//		//判断用户是否已被选择，如被选择，则复选框为勾选，如未选择则复选框为可选
//		if((Boolean) listData.get(position).get("selected")){
//			state.put(position,true);
//			
//		}

//		check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				// TODO Auto-generated method stub
//				if(isChecked){
//					state.put(position,isChecked);
//				}else {
//					state.remove(position);
//				}
//			}
//		});

//		check.setChecked((state.get(position)==null?false:true));

        return convertView;
    }

    static class ViewHolder {
        TextView informtitle1;
        TextView create_time1;


    }
}
