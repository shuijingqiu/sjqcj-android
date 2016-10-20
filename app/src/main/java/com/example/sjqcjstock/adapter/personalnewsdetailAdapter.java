package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class personalnewsdetailAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, Object>> listData;

    public personalnewsdetailAdapter(Context context, ArrayList<HashMap<String, Object>> listData) {
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

        //String meoryoustr=(String)listData.get(position).get("meoryou");

        String uidstr = (String) listData.get(position).get("uidstr");
        String myuidstr = Constants.getStaticmyuidstr();

        //String ctimestr=(String)listData.get(position).get("ctimestr");

        if (Constants.getStaticmyuidstr().equals(uidstr)) {

            convertView = mInflater.inflate(R.layout.chatting_item_msg_text_right, null);
            TextView tv_sendtime = (TextView) convertView.findViewById(R.id.tv_sendtime2);
            tv_sendtime.setText((String) listData.get(position).get("mtimestr"));

            TextView tv_chatcontent = (TextView) convertView.findViewById(R.id.tv_chatcontent);
            tv_chatcontent.setText((String) listData.get(position).get("contentstr"));

            //TextView tv_username=(TextView)convertView.findViewById(R.id.tv_username);
            //tv_username.setText((String)listData.get(position).get("unamestr"));

            ImageView image = (ImageView) convertView.findViewById(R.id.iv_userhead);
            // image.setBackgroundResource((Integer)listData.get(position).get("friend_image"));
            ImageLoader.getInstance().displayImage((String) listData.get(position).
                    get("avatar_middlestr"), image, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        } else {
            convertView = mInflater.inflate(R.layout.chatting_item_msg_text_left, null);

            TextView tv_sendtime = (TextView) convertView.findViewById(R.id.tv_sendtime3);
            tv_sendtime.setText((String) listData.get(position).get("mtimestr"));

            TextView tv_chatcontent = (TextView) convertView.findViewById(R.id.tv_chatcontent);
            tv_chatcontent.setText((String) listData.get(position).get("contentstr"));

            //TextView tv_username=(TextView)convertView.findViewById(R.id.tv_username);
            //tv_username.setText((String)listData.get(position).get("unamestr"));

            ImageView image = (ImageView) convertView.findViewById(R.id.iv_userhead);
            // image.setBackgroundResource((Integer)listData.get(position).get("friend_image"));
            ImageLoader.getInstance().displayImage((String) listData.get(position).
                    get("avatar_middlestr"), image, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        }

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
