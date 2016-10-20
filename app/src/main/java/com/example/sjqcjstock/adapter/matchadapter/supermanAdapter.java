package com.example.sjqcjstock.adapter.matchadapter;

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

/**
 * 牛人推荐的适配器
 */
public class supermanAdapter extends BaseAdapter {

    private String followingstr;
    private String followerstr;

    private Context context;
    private com.example.sjqcjstock.fragment.FragmentHome FragmentHome;
    private ArrayList<HashMap<String, String>> listData;

    public supermanAdapter(Context context,
                           com.example.sjqcjstock.fragment.FragmentHome FragmentHome,
                           ArrayList<HashMap<String, String>> listData) {
        super();
        this.context = context;
        this.listData = listData;
        this.FragmentHome = FragmentHome;
    }

    public void setlistData(ArrayList<HashMap<String, String>> listData) {
        this.listData = listData;
    }

    @Override
    public int getCount() {
        // return imagesUrl.length;
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        // 获取精华网络数据

        // 动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_superman, null);
        }

        ImageView image = (ImageView) convertView.findViewById(R.id.user_image);
        ImageLoader.getInstance().displayImage(
                (String) listData.get(position).get("image_url"), image,
                ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());

        if (listData.get(position).get("i") != null) {
            if (Integer.valueOf(listData.get(position).get("i")) == 2) {
                convertView.findViewById(R.id.thisline1).setVisibility(
                        View.GONE);
            }
        }

        TextView username = (TextView) convertView.findViewById(R.id.username);
        username.setText(listData.get(position).get("username"));
        //
        TextView detailcomment = (TextView) convertView
                .findViewById(R.id.detailcomment);
        detailcomment.setText(listData.get(position).get("intro"));

        final Button friend_image = (Button) convertView
                .findViewById(R.id.attentionuser1);
        final TextView attentionuser1 = (TextView) convertView
                .findViewById(R.id.attentionuser1);

        followingstr = listData.get(position).get("following");
        followerstr = listData.get(position).get("follower");
        if ("1".equals(followingstr) && "1".equals(followerstr)) {
            attentionuser1.setText("相互关注");
            attentionuser1.setTextColor(attentionuser1.getResources().getColor(R.color.color_toptitle));
            friend_image.setBackground(friend_image.getResources().getDrawable(R.drawable.buttonstyle5));
        }
        if ("1".equals(followingstr)) {
            attentionuser1.setText("已关注");
            attentionuser1.setTextColor(attentionuser1.getResources().getColor(R.color.color_toptitle));
            friend_image.setBackground(friend_image.getResources().getDrawable(R.drawable.buttonstyle5));
        }

        friend_image.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if ("0".equals((String) listData.get(position).get("following"))) {

                    if (Constants.staticLoginType.equals("weixin")
                            || Constants.staticLoginType.equals("qq")) {
                        new SendInfoTaskfollowsb()
                                .execute(new TaskParams(
                                        Constants.Url + "?app=public&mod=AppFeedList&act=AddFollow",
                                        new String[]{"mid", Constants.staticmyuidstr},
                                        new String[]{"tokey", Constants.statictokeystr},
                                        new String[]{"type", Constants.staticLoginType},
                                        new String[]{"fid", (String) listData.get(position).get("uid")}));
                    } else {
                        // 普通用户登录请求数据
                        new SendInfoTaskfollowsb().execute(new TaskParams(
                                Constants.Url + "?app=public&mod=AppFeedList&act=AddFollow", new String[]{
                                "mid", Constants.staticmyuidstr},
                                new String[]{"login_password", Constants.staticpasswordstr},
                                new String[]{"tokey", Constants.statictokeystr},
                                new String[]{"fid", (String) listData.get(position).get("uid")}));
                    }

                    if ("1".equals(followerstr)) {
                        attentionuser1.setText("相互关注");
                        attentionuser1.setTextColor(attentionuser1.getResources().getColor(R.color.color_toptitle));
                    } else {
                        attentionuser1.setText("已关注");
                        attentionuser1.setTextColor(attentionuser1.getResources().getColor(R.color.color_toptitle));
                    }
                    friend_image.setBackground(friend_image.getResources().getDrawable(R.drawable.buttonstyle5));
                    followingstr = "1";
                    listData.get(position).put("following", followingstr);
                    if (FragmentHome != null) {
                        FragmentHome.test(listData);
                    }

                } else {
                    new SendInfoTaskfollowcancelsb()
                            .execute(new TaskParams(
                                            Constants.Url + "?app=public&mod=AppFeedList&act=DelFollow",
                                            new String[]{"mid",
                                                    Constants.staticmyuidstr},
                                            new String[]{"login_password",
                                                    Constants.staticpasswordstr},
                                            new String[]{"tokey",
                                                    Constants.statictokeystr},
                                            new String[]{
                                                    "fid",
                                                    (String) listData.get(position)
                                                            .get("uid")}
                                    )
                            );
                    attentionuser1.setText("+ 关注");
                    attentionuser1.setTextColor(attentionuser1.getResources().getColor(R.color.color_text2));
                    friend_image.setBackground(friend_image.getResources().getDrawable(R.drawable.buttonstyle4));

                    followingstr = "0";
                    listData.get(position).put("following", followingstr);
                    if (FragmentHome != null) {
                        FragmentHome.test(listData);
                    }
                }

            }
        });
        ImageView vipImg = (ImageView) convertView.findViewById(R.id.vip_img);
        String isVip = listData.get(position).get(
                "isVip") + "";
        ViewUtil.setUpVip(isVip, vipImg);
        return convertView;
    }

    private class SendInfoTaskfollowsb extends
            AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            result = result.replace("\n ", "");
            result = result.replace("\n", "");
            result = result.replace(" ", "");
            result = "[" + result + "]";
//
//			// 解析json字符串获得List<Map<String,Object>>
//			List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
//			for (Map<String, Object> map : lists) {
//				String statusstr = map.get("status").toString();
//				String infostr = map.get("info").toString();
//				String datastr = map.get("data").toString();
//
//
//			}
        }
    }

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
            // CustomToast.makeText(supermanlistActivity.this, result, 1).show();

//			result = result.replace("\n ", "");
//			result = result.replace("\n", "");
//			result = result.replace(" ", "");
//			result = "[" + result + "]";
//			// 解析json字符串获得List<Map<String,Object>>
//			List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
//			for (Map<String, Object> map : lists) {
//				String statusstr = map.get("status").toString();
//				String infostr = map.get("info").toString();
//				String datastr = map.get("data").toString();
//
//
//
//			}

        }

    }

}
