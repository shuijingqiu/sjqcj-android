package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.sjqcjstock.R;

import java.util.ArrayList;
import java.util.HashMap;

public class feedbackAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, Object>> listData;


    public feedbackAdapter(Context context, ArrayList<HashMap<String, Object>> listData) {
        super();
        this.context = context;
        this.listData = listData;
    }

    public void setlistData(ArrayList<HashMap<String, Object>> listData) {
        this.listData = listData;
    }

    private String[] imagesUrl;

    public void setImagesUrl(String[] imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    @Override
    public int getCount() {
        //return imagesUrl.length;
        return listData.size();
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

        convertView = mInflater.inflate(R.layout.list_item_feedback, null);

//		ImageView image=(ImageView)convertView.findViewById(R.id.user_image);
//		 // image.setBackgroundResource((Integer)listData.get(position).get("friend_image"));
//		  ImageLoader.getInstance().displayImage((String)listData.get(position).
//	    		get("image_url"),
//				image,options,new AnimateFirstDisplayListener());
//		
//		TextView username=(TextView)convertView.findViewById(R.id.weibo_titlestr);
//		username.setText((String)listData.get(position).get("weibo_titlestr"));
//		
//		TextView detailAddress=(TextView)convertView.findViewById(R.id.username);
//		detailAddress.setText((String)listData.get(position).get("username"));
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
