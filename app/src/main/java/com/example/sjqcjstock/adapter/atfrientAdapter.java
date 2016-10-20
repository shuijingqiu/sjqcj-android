package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.atfriendActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class atfrientAdapter extends BaseAdapter {
    private Context context;
    private atfriendActivity atfriendActivity;
    private ArrayList<HashMap<String, Object>> listData;

    public atfrientAdapter(Context context, atfriendActivity atfriendActivity) {
        super();
        this.context = context;
        this.atfriendActivity = atfriendActivity;
    }

    public void setlistData(ArrayList<HashMap<String, Object>> listData) {
        if (listData != null) {
            this.listData = (ArrayList<HashMap<String, Object>>) listData.clone();
            notifyDataSetChanged();
        }
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
        // 动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_myattentionuser, null);
        }
        ImageView image = (ImageView) convertView.findViewById(R.id.user_image);
        ImageLoader.getInstance().displayImage(
                (String) listData.get(position).get("avatar_middlestr"), image,
                ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        TextView username = (TextView) convertView.findViewById(R.id.username);
        username.setText((String) listData.get(position).get("unamestr"));
        TextView detailcomment = (TextView) convertView.findViewById(R.id.detailcomment);
        detailcomment.setText((String) listData.get(position).get("intro"));
        RelativeLayout relat1 = (RelativeLayout) convertView.findViewById(R.id.RelativeLayout);
        relat1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                atfriendActivity.atcallback((String) listData.get(position)
                        .get("unamestr"));
            }
        });
        return convertView;
    }
//
//	private class SendInfoTask extends AsyncTask<TaskParams, Void, String> {
//
//		@Override
//		protected String doInBackground(TaskParams... params) {
//			return HttpUtil.doInBackground(params);
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			super.onPostExecute(result);
//		}
//	}
}
