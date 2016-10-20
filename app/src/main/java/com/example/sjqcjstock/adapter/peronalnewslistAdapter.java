package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class peronalnewslistAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, String>> listData;

    public peronalnewslistAdapter(Context context) {
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

        //convertView=mInflater.inflate(R.layout.list_item_sesence, null);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_personalnews, null);
        }
        //convertView=mInflater.inflate(R.layout.n, null);

        ImageView image = (ImageView) convertView.findViewById(R.id.user_image1);
        // image.setBackgroundResource((Integer)listData.get(position).get("friend_image"));
        ImageLoader.getInstance().displayImage((String) listData.get(position).
                        get("avatar_middlestr"),
                image, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
//		
        TextView username1 = (TextView) convertView.findViewById(R.id.username1);
        username1.setText((String) listData.get(position).get("unamestr"));

        TextView lastmessage1 = (TextView) convertView.findViewById(R.id.lastmessage1);
        lastmessage1.setText((String) listData.get(position).get("contentstr"));
//		
        TextView creatime1 = (TextView) convertView.findViewById(R.id.creatime1);
        creatime1.setText((String) listData.get(position).get("list_ctimestr"));
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

        ImageView vipImg = (ImageView) convertView
                .findViewById(R.id.vip_img);
        String isVip = listData.get(position).get(
                "isVip");
        ViewUtil.setUpVip(isVip, vipImg);
        return convertView;
    }


}
