package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.addcommentweiboActivity;
import com.example.sjqcjstock.R;

import java.util.ArrayList;
import java.util.HashMap;

public class floatstocklistAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, Object>> listData;


    public floatstocklistAdapter(Context context, ArrayList<HashMap<String, Object>> listData) {
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

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_floatstock, null);
        }


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

        ImageView commentweibo1 = (ImageView) convertView.findViewById(R.id.commentweibo1);
        commentweibo1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                    Intent intent = new Intent(context.getApplicationContext(), addcommentweiboActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("feed_id", (String) listData.get(position).get("weibo_idstr"));
                    intent.putExtra("feeduid", (String) listData.get(position).get("uidstr"));

                    context.startActivity(intent);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } finally {

                }

            }
        });

        TextView informtitle1 = (TextView) convertView.findViewById(R.id.title1);
        informtitle1.setText((String) listData.get(position).get("weibo_titlestr"));

        TextView uname1 = (TextView) convertView.findViewById(R.id.uname1);
        uname1.setText((String) listData.get(position).get("unamestr"));

        TextView create_time1 = (TextView) convertView.findViewById(R.id.create_time1);
        create_time1.setText((String) listData.get(position).get("ctimestr"));

        TextView comment_count1 = (TextView) convertView.findViewById(R.id.comment_count1);
        comment_count1.setText((String) listData.get(position).get("comment_countstr"));
        /**
         //
         TextView commentcount1=(TextView)convertView.findViewById(R.id.commentcount1);
         commentcount1.setText((String)listData.get(position).get("comment_countstr"));
         //
         //		CheckBox check=(CheckBox)convertView.findViewById(R.id.selected);

         //		//判断用户是否已被选择，如被选择，则复选框为勾选，如未选择则复选框为可选
         //		if((Boolean) listData.get(position).get("selected")){
         //			state.put(position,true);
         //
         //		}
         */
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
