package com.example.sjqcjstock.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class supermanActivityAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, String>> listData;

    public supermanActivityAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<HashMap<String, String>> listData) {
        if (listData != null) {
            this.listData = (ArrayList<HashMap<String, String>>) listData.clone();
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        // 获取精华网络数据

        // 动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);
        // if(convertView==null){
        convertView = mInflater.inflate(R.layout.list_item_superman, null);
        // }

        ImageView image = (ImageView) convertView.findViewById(R.id.user_image);
        // image.setBackgroundResource((Integer)listData.get(position).get("user_image"));
        ImageLoader.getInstance().displayImage(
                listData.get(position).get("image_url"), image,
                ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());

        //
        TextView username = (TextView) convertView.findViewById(R.id.username);
        username.setText(listData.get(position).get("username"));
        //
        TextView detailcomment = (TextView) convertView
                .findViewById(R.id.detailcomment);
        detailcomment.setText(listData.get(position).get("intro"));

        final Button friend_image = (Button) convertView
                .findViewById(R.id.attentionuser1);
        final Button attentionuser1 = (Button) convertView
                .findViewById(R.id.attentionuser1);

        String following = listData.get(position).get("following").toString();
        String follower = listData.get(position).get("follower").toString();
        if ("1".equals(following)) {
            attentionuser1.setText("已关注");
            attentionuser1.setTextColor(attentionuser1.getResources().getColor(R.color.color_toptitle));
            friend_image.setBackground(friend_image.getResources().getDrawable(R.drawable.buttonstyle5));
        }
        if ("1".equals(following)
                && "1".equals(follower)) {
            attentionuser1.setText("相互关注");
            attentionuser1.setTextColor(attentionuser1.getResources().getColor(R.color.color_toptitle));
            friend_image.setBackground(friend_image.getResources().getDrawable(R.drawable.buttonstyle5));

        }

        //关注按钮的点击事件
        friend_image.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                //点击关注
                if ("0".equals(listData.get(position).get("following"))) {
                    if (Constants.staticLoginType.equals("qq") || Constants.staticLoginType.equals("weixin")) {
                        //三方用户
                        new SendInfoTaskfollowsb().execute(new TaskParams(
                                Constants.Url + "?app=public&mod=AppFeedList&act=AddFollow",
                                new String[]{"mid", Constants.staticmyuidstr},
                                //new String[] { "login_password",Constants.staticpasswordstr },
                                new String[]{"tokey", Constants.statictokeystr},
                                new String[]{"fid", listData.get(position).get("uid")},
                                new String[]{"type", Constants.staticLoginType}
                        ));
                    } else {
                        //普通用户
                        new SendInfoTaskfollowsb().execute(new TaskParams(
                                Constants.Url + "?app=public&mod=AppFeedList&act=AddFollow",
                                new String[]{"mid", Constants.staticmyuidstr},
                                new String[]{"login_password", Constants.staticpasswordstr},
                                new String[]{"tokey", Constants.statictokeystr},
                                new String[]{"fid", (String) listData.get(position).get("uid")}

                        ));
                    }
                    attentionuser1.setText("已关注");

                    listData.get(position).put("following", "1");
                    notifyDataSetChanged();
                } else {
                    //取消关注
                    if (Constants.staticLoginType.equals("qq") || Constants.staticLoginType.equals("weixin")) {
                        //三方用户
                        new SendInfoTaskfollowcancelsb().execute(new TaskParams(
                                Constants.Url + "?app=public&mod=AppFeedList&act=DelFollow",
                                new String[]{"mid", Constants.staticmyuidstr},
                                //new String[] { "login_password",Constants.staticpasswordstr },
                                new String[]{"tokey", Constants.statictokeystr},
                                new String[]{"fid", listData.get(position).get("uid")},
                                new String[]{"type", Constants.staticLoginType}
                        ));
                    } else {
                        //普通用户
                        new SendInfoTaskfollowcancelsb().execute(new TaskParams(
                                Constants.Url + "?app=public&mod=AppFeedList&act=DelFollow",
                                new String[]{"mid", Constants.staticmyuidstr},
                                new String[]{"login_password", Constants.staticpasswordstr},
                                new String[]{"tokey", Constants.statictokeystr},
                                new String[]{"fid", listData.get(position).get("uid")}
                        ));
                    }
                    attentionuser1.setText("+ 关注");
                    listData.get(position).put("following", "0");
                    notifyDataSetChanged();
                }
            }
        });

        ImageView vipImg = (ImageView) convertView.findViewById(R.id.vip_img);
        String isVip = listData.get(position).get(
                "isVip") + "";
        ViewUtil.setUpVip(isVip, vipImg);
        return convertView;
    }

    //关注
    private class SendInfoTaskfollowsb extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            // CustomToast.makeText(supermanlistActivity.this, result, 1).show();

//			result = result.replace("\n ", "");
//			result = result.replace("\n", "");
//			result = result.replace(" ", "");
//			result = "[" + result + "]";
//			// 解析json字符串获得List<Map<String,Object>> mh逻辑有问题
//			List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
//			for (Map<String, Object> map : lists) {
//				String statusstr = map.get("status").toString();
//				String infostr = map.get("info").toString();
//				String datastr = map.get("data").toString();
//			}
        }
    }

    //取消关注
    private class SendInfoTaskfollowcancelsb extends
            AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
//			result = result.replace("\n ", "");
//			result = result.replace("\n", "");
//			result = result.replace(" ", "");
//			result = "[" + result + "]";
            // 解析json字符串获得List<Map<String,Object>> mh逻辑有问题
//			List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
//			for (Map<String, Object> map : lists) {
//				String statusstr = map.get("status").toString();
//				String infostr = map.get("info").toString();
//				String datastr = map.get("data").toString();
//			}

        }

    }

}
