package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 当周牛股控制器
 */
public class thisweekuprankingAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, String>> listData;

    public thisweekuprankingAdapter(Context context, ArrayList<HashMap<String, String>> listData) {
        super();
        this.context = context;
        this.listData = listData;
    }

    public void setlistData(ArrayList<HashMap<String, String>> listData) {
        this.listData = listData;
        notifyDataSetChanged();
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
        // TODO Auto-generated method stub
        //动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_todayupranking, null);
            holder = new ViewHolder();
            holder.unamestr = (TextView) convertView.findViewById(R.id.unamestr);
            holder.increasestr = (TextView) convertView.findViewById(R.id.increasestr);
            holder.ballot_namestr = (TextView) convertView.findViewById(R.id.ballot_namestr);
            holder.user_image = (ImageView) convertView.findViewById(R.id.user_image);
            holder.currentPrice = (TextView) convertView.findViewById(R.id.currentPrice);
            holder.pickuserinfo1 = (LinearLayout) convertView.findViewById(R.id.pickuserinfo1);
//	        holder.thisline1=(ImageView)convertView.findViewById(R.id.thisline1);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


//		ImageView image=(ImageView)convertView.findViewById(R.id.user_image1);
//		 // image.setBackgroundResource((Integer)listData.get(position).get("friend_image"));
//		  ImageLoader.getInstance().displayImage((String)listData.get(position).
//	    		get("uidimg"),
//				image,options,new AnimateFirstDisplayListener());
//		  
//		  image.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent intent=new Intent(context.getApplicationContext(),UserDetailNewActivity.class);
//				if("".equals((String)listData.get(position).get("uidstr"))){
//					
//					//intent.putExtra("uid",(String)listData.get(position).get("sourceuid"));
//					// context.startActivity(intent);
//		  		}else{
//				  intent.putExtra("uid",(String)listData.get(position).get("uidstr"));
//				  context.startActivity(intent);
//		  		}
//			}
//		  }); 
//		  
//		  
//		  TextView rankingcount1=(TextView)convertView.findViewById(R.id.rankingcount1);
//		  rankingcount1.setText((String)listData.get(position).get("rankcountstr"));
//			
        //TextView unamestr=(TextView)convertView.findViewById(R.id.unamestr);\

        ImageLoader.getInstance().displayImage((String) listData.get(position).
                        get("uidimg"),
                holder.user_image, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());


        holder.user_image.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid", (String) listData.get(position).get("uid"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } finally {

                }

            }
        });

//		if(listData.get(position).get("i")!=null){
//			if((Integer)listData.get(position).get("i")==3){
//				holder.thisline1.setVisibility(View.GONE);
//			 }
//		}


        holder.unamestr.setText(listData.get(position).get("uname"));
        holder.ballot_namestr.setText(listData.get(position).get("ballot_name"));
        String currentPrice = listData.get(position).get("currentPrice");
        if (currentPrice == null || "".equals(currentPrice) || Double.valueOf(currentPrice) == 0) {
            holder.increasestr.setText("—");
            holder.currentPrice.setText("—");
            holder.increasestr.setTextColor(Color.rgb(51, 51, 51));
        } else {
            ViewUtil.setViewColor(holder.increasestr, listData.get(position).get("increase").toString());
            holder.currentPrice.setText(Utils.getNumberFormat(currentPrice));
        }

        holder.pickuserinfo1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid", listData.get(position).get("uid"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });
        /**
         ImageView sourceuser_image2=(ImageView)convertView.findViewById(R.id.sourceuser_image2);
         // image.setBackgroundResource((Integer)listData.get(position).get("friend_image"));
         ImageLoader.getInstance().displayImage((String)listData.get(position).
         get("sourceavatar_middlestr"),
         sourceuser_image2,options,new AnimateFirstDisplayListener());

         sourceuser_image2.setOnClickListener(new OnClickListener() {

        @Override public void onClick(View arg0) {
        // TODO Auto-generated method stub
        Intent intent=new Intent(context.getApplicationContext(),UserDetailNewActivity.class);
        intent.putExtra("uid",(String)listData.get(position).get("sourceuid"));
        context.startActivity(intent);

        }
        });
         */

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
        TextView unamestr;
        TextView increasestr;
        TextView ballot_namestr;
        ImageView user_image;
        TextView currentPrice;
        LinearLayout pickuserinfo1;
//        ImageView thisline1;


    }

}
