package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.sjqcjstock.R;

import java.util.ArrayList;
import java.util.HashMap;

public class selfselectstockAdapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String, Object>> listData;

    //记录checkbox的状态
    public HashMap<Integer, Boolean> state = new HashMap<Integer, Boolean>();

    //构造函数
    public selfselectstockAdapter(Context context, ArrayList<HashMap<String, Object>> listData) {
        this.context = context;
        this.listData = listData;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        //动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);

        convertView = mInflater.inflate(R.layout.list_item_selfselectstock, null);

//		ImageView image=(ImageView)convertView.findViewById(R.id.friend_image);
//		image.setBackgroundResource((Integer)listData.get(position).get("friend_image"));
//		
//		TextView username=(TextView)convertView.findViewById(R.id.friend_username);
//		username.setText((String)listData.get(position).get("friend_username"));
//		
//		TextView detailAddress=(TextView)convertView.findViewById(R.id.detailAddress);
//		detailAddress.setText((String)listData.get(position).get("detailAddress"));
//		
        //TextView id=(TextView)convertView.findViewById(R.id.friend_id);
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


}
