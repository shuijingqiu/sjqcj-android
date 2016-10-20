package com.example.sjqcjstock.adapter.matchadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class totalrankingdetailfamousAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, Object>> listData;

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
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_totalrankingdetailfamous, null);

            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.user_image1);
            holder.famousmanname1 = (TextView) convertView.findViewById(R.id.famousmanname1);
            holder.totalscore1 = (TextView) convertView.findViewById(R.id.totalscore1);
            holder.weekly = (TextView) convertView.findViewById(R.id.weekly);


            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //ImageView image=(ImageView)convertView.findViewById(R.id.user_image1);
        // image.setBackgroundResource((Integer)listData.get(position).get("friend_image"));
        ImageLoader.getInstance().displayImage((String) listData.get(position).
                        get("uidimg"),
                holder.image, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        /**

         image.setOnClickListener(new OnClickListener() {

        @Override public void onClick(View arg0) {
        // TODO Auto-generated method stub
        Intent intent=new Intent(context.getApplicationContext(),UserDetailNewActivity.class);
        if("".equals((String)listData.get(position).get("uidstr"))){

        intent.putExtra("uid",(String)listData.get(position).get("sourceuid"));
        context.startActivity(intent);
        }else{
        intent.putExtra("uid",(String)listData.get(position).get("uidstr"));
        context.startActivity(intent);
        }
        }
        });
         */

//		
        //TextView famousmanname1=(TextView)convertView.findViewById(R.id.famousmanname1);
        holder.famousmanname1.setText((String) listData.get(position).get("uname"));

        //TextView weekscore1=(TextView)convertView.findViewById(R.id.weekscore1);
        //weekscore1.setText((CharSequence)listData.get(position).get("list_price"));
//		
        //TextView totalscore1=(TextView)convertView.findViewById(R.id.totalscore1);
        holder.totalscore1.setText((CharSequence) listData.get(position).get("ballot_jifen"));

        //TextView weekly=(TextView)convertView.findViewById(R.id.weekly);
        holder.weekly.setText((String) listData.get(position).get("weekly"));
//		


//		TextView stockname1=(TextView)convertView.findViewById(R.id.stockname1);
//		stockname1.setText((String)listData.get(position).get("shares_name"));
//		
//		TextView shares2_name=(TextView)convertView.findViewById(R.id.stockname2);
//		shares2_name.setText((String)listData.get(position).get("shares2_name"));
//		
//		
//		TextView stockprice1=(TextView)convertView.findViewById(R.id.stockprice1);
//		stockprice1.setText((String)listData.get(position).get("price"));
//		
//		TextView stockprice2=(TextView)convertView.findViewById(R.id.stockprice2);
//		stockprice2.setText((String)listData.get(position).get("price2"));
//		
//		TextView uprange1=(TextView)convertView.findViewById(R.id.uprange1);
//		uprange1.setText((String)listData.get(position).get("integration3"));
//		
//		TextView uprange2=(TextView)convertView.findViewById(R.id.uprange2);
//		uprange2.setText((String)listData.get(position).get("integration4"));

        //TextView rankingcount=(TextView)convertView.findViewById(R.id.rankingcount);
        //rankingcount.setText((String)listData.get(position).get("rankingcount"));

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
        ImageView image;
        TextView famousmanname1;
        TextView totalscore1;
        TextView weekly;
    }
}
