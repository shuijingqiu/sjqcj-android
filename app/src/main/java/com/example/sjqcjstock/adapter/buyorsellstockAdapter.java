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

public class buyorsellstockAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, Object>> listData;


    public buyorsellstockAdapter(Context context, ArrayList<HashMap<String, Object>> listData) {
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
        //动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);
        ViewHolder holder;


        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_buyorsellstock, null);

            holder = new ViewHolder();

            holder.shoptype1 = (TextView) convertView.findViewById(R.id.shoptype1);
            holder.shopname1 = (TextView) convertView.findViewById(R.id.shopname1);
            holder.tradeprice1 = (TextView) convertView.findViewById(R.id.tradeprice1);
            holder.tradecount1 = (TextView) convertView.findViewById(R.id.tradecount1);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        //String str=(String)listData.get(position).get("max_assetsstr");
        //

//		ImageView image=(ImageView)convertView.findViewById(R.id.user_image);
//		 // image.setBackgroundResource((Integer)listData.get(position).get("friend_image"));
//		  ImageLoader.getInstance().displayImage((String)listData.get(position).
//	    		get("image_url"),
//				image,options,new AnimateFirstDisplayListener());
//		  
//		  
//		  image.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent intent=new Intent(context.getApplicationContext(),UserDetailNewActivity.class);
//				String  uidstr= (String)listData.get(position).get("uidstr");
//				intent.putExtra("uid",(String)listData.get(position).get("uidstr"));
//				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
//				context.startActivity(intent);
//				
//			}
//		  });
//		
        //TextView username=(TextView)convertView.findViewById(R.id.weibo_titlestr);
        //username.setText((String)listData.get(position).get("shopstr"));

        //TextView shoptype1=(TextView)convertView.findViewById(R.id.shoptype1);
        holder.shoptype1.setText((String) listData.get(position).get("shopstr"));

        //TextView shopname1=(TextView)convertView.findViewById(R.id.shopname1);
        holder.shopname1.setText((String) listData.get(position).get("shopnamestr"));
////		
        //TextView tradeprice1=(TextView)convertView.findViewById(R.id.tradeprice1);
        holder.tradeprice1.setText((String) listData.get(position).get("shopmoneystr"));

        //TextView tradecount1=(TextView)convertView.findViewById(R.id.tradecount1);
        holder.tradecount1.setText((String) listData.get(position).get("shopnumstr"));

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
        TextView shoptype1;
        TextView shopname1;
        TextView tradeprice1;
        TextView tradecount1;


    }
}
