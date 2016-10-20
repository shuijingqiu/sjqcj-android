package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.TaskParams;

import java.util.ArrayList;
import java.util.HashMap;

public class moodsrankingAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<HashMap<String, Object>> listData;


    public moodsrankingAdapter(Context context, ArrayList<HashMap<String, Object>> listData) {
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

//		if(convertView==null){
//			convertView=mInflater.inflate(R.layout.list_item_moodsranking, null);
//		}
//
//
//		ImageView image=(ImageView)convertView.findViewById(R.id.user_image);
//		//image.setBackgroundResource((Integer)listData.get(position).get("user_image"));
//		ImageLoader.getInstance().displayImage((String)listData.get(position).
//				get("uidimg"),
//				image, ImageUtil.getOption(),ImageUtil.getAnimateFirstDisplayListener());
//
////
//		TextView username=(TextView)convertView.findViewById(R.id.username);
//		username.setText((String)listData.get(position).get("unamestr"));
////
//		TextView fanscount1=(TextView)convertView.findViewById(R.id.fanscount1);
//		fanscount1.setText((String)listData.get(position).get("fansstr"));
//
//
//		TextView rankingcount=(TextView)convertView.findViewById(R.id.rankingcount);
//		rankingcount.setText((String)listData.get(position).get("rankingcount"));


        return convertView;
    }


    private class SendInfoTask extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //CustomToast.makeText(supermanlistActivity.this, result, 1).show();


        }

    }

}
