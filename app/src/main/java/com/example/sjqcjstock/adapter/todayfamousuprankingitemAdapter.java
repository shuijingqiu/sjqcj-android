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

import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class todayfamousuprankingitemAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, Object>> listData;


    public todayfamousuprankingitemAdapter(Context context, ArrayList<HashMap<String, Object>> listData) {
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

        convertView = mInflater.inflate(R.layout.list_item_todayfamousupranking, null);


        ImageView image = (ImageView) convertView.findViewById(R.id.user_image1);
        // image.setBackgroundResource((Integer)listData.get(position).get("friend_image"));
        ImageLoader.getInstance().displayImage((String) listData.get(position).
                        get("uidimg"),
                image, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());

        image.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                if ("".equals((String) listData.get(position).get("uidstr"))) {

                    //intent.putExtra("uid",(String)listData.get(position).get("sourceuid"));
                    // context.startActivity(intent);
                } else {
                    intent.putExtra("uid", (String) listData.get(position).get("uidstr"));
                    context.startActivity(intent);
                }
            }
        });


        TextView rankingcount1 = (TextView) convertView.findViewById(R.id.rankingcount1);
        rankingcount1.setText((String) listData.get(position).get("rankcountstr"));

        TextView unamestr = (TextView) convertView.findViewById(R.id.unamestr);
        unamestr.setText((String) listData.get(position).get("unamestr"));

        TextView increasestr = (TextView) convertView.findViewById(R.id.increasestr);
        increasestr.setText((CharSequence) listData.get(position).get("increasestr"));
//		
        TextView ballot_namestr = (TextView) convertView.findViewById(R.id.ballot_namestr);
        ballot_namestr.setText((CharSequence) listData.get(position).get("ballot_namestr"));
//		


        //TextView contenttimes1=(TextView)convertView.findViewById(R.id.contenttimes1);
        //contenttimes1.setText((String)listData.get(position).get("ctimestr"));

        //TextView sourceusername1=(TextView)convertView.findViewById(R.id.sourceusername1);
        //sourceusername1.setText((String)listData.get(position).get("sourceunamestr"));

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


}
