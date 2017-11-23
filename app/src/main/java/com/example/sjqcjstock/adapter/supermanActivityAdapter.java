package com.example.sjqcjstock.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjqcjstock.Activity.user.loginActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.Article.UserEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.example.sjqcjstock.view.CustomToast;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class supermanActivityAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<UserEntity> listData;
    // 点赞类型
    private String type;
    // 操作返回数据
    private String jsonStr;
    // 操作的数组下表
    private int count;

    public supermanActivityAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setlistData(ArrayList<UserEntity> listData) {
        if (listData != null) {
            this.listData = (ArrayList<UserEntity>) listData.clone();
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

        // 动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_superman, null);
            holder = new ViewHolder();
            holder.head = (ImageView) convertView.findViewById(R.id.user_image);
            holder.name = (TextView) convertView.findViewById(R.id.username);
            holder.detailcomment = (TextView) convertView.findViewById(R.id.detailcomment);
            holder.attentionuser1 = (Button) convertView.findViewById(R.id.attentionuser1);
            holder.vipIv = (ImageView) convertView.findViewById(R.id.vip_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final UserEntity userEntity = listData.get(position);
        ImageLoader.getInstance().displayImage(
                userEntity.getAvatar_middle(), holder.head,
                ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        holder.name.setText(userEntity.getUname());
        holder.detailcomment.setText(userEntity.getIntro());

        String following = userEntity.getFollow().getFollowing();
        String follower = userEntity.getFollow().getFollower();
        if ("1".equals(following)) {
            holder.attentionuser1.setText("已关注");
            holder.attentionuser1.setTextColor(holder.attentionuser1.getResources().getColor(R.color.color_toptitle));
            holder.attentionuser1.setBackground(holder.attentionuser1.getResources().getDrawable(R.drawable.buttonstyle5));
            if ("1".equals(follower)) {
                holder.attentionuser1.setText("相互关注");
                holder.attentionuser1.setTextColor(holder.attentionuser1.getResources().getColor(R.color.color_toptitle));
                holder.attentionuser1.setBackground(holder.attentionuser1.getResources().getDrawable(R.drawable.buttonstyle5));

            }
        } else {
            holder.attentionuser1.setText("+ 关注");
            holder.attentionuser1.setTextColor(holder.attentionuser1.getResources().getColor(R.color.color_text2));
            holder.attentionuser1.setBackground(holder.attentionuser1.getResources().getDrawable(R.drawable.buttonstyle4));
        }

        //关注按钮的点击事件
        holder.attentionuser1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (Utils.isFastDoubleClick()) {
                    return;
                }

                // menghuan 不用登陆也可以用
                // 如果未登陆跳转到登陆页面
                if (!Constants.isLogin) {
                    Intent intent = new Intent(context, loginActivity.class);
                    context.startActivity(intent);
                    return;
                }
                count = position;
                //点击关注
                if ("0".equals(userEntity.getFollow().getFollowing())) {
                    type = "add";
//                    if (Constants.staticLoginType.equals("qq") || Constants.staticLoginType.equals("weixin")) {
//                        //三方用户
//                        new SendInfoTaskfollowsb().execute(new TaskParams(
//                                Constants.Url + "?app=public&mod=AppFeedList&act=AddFollow",
//                                new String[]{"mid", Constants.staticmyuidstr},
//                                //new String[] { "login_password",Constants.staticpasswordstr },
//                                new String[]{"tokey", Constants.statictokeystr},
//                                new String[]{"fid", listData.get(position).get("uid")},
//                                new String[]{"type", Constants.staticLoginType}
//                        ));
//                    } else {
//                        //普通用户
//                        new SendInfoTaskfollowsb().execute(new TaskParams(
//                                Constants.Url + "?app=public&mod=AppFeedList&act=AddFollow",
//                                new String[]{"mid", Constants.staticmyuidstr},
//                                new String[]{"login_password", Constants.staticpasswordstr},
//                                new String[]{"tokey", Constants.statictokeystr},
//                                new String[]{"fid", (String) listData.get(position).get("uid")}
//
//                        ));
//                    }
//                    holder.attentionuser1.setText("已关注");
//                    listData.get(position).put("following", "1");
//                    holder.attentionuser1.setTextColor(holder.attentionuser1.getResources().getColor(R.color.color_toptitle));
//                    holder.attentionuser1.setBackground(holder.attentionuser1.getResources().getDrawable(R.drawable.buttonstyle5));
                } else {
                    //取消关注
                    type = "cancel";
//                    if (Constants.staticLoginType.equals("qq") || Constants.staticLoginType.equals("weixin")) {
//                        //三方用户
//                        new SendInfoTaskfollowcancelsb().execute(new TaskParams(
//                                Constants.Url + "?app=public&mod=AppFeedList&act=DelFollow",
//                                new String[]{"mid", Constants.staticmyuidstr},
//                                //new String[] { "login_password",Constants.staticpasswordstr },
//                                new String[]{"tokey", Constants.statictokeystr},
//                                new String[]{"fid", listData.get(position).get("uid")},
//                                new String[]{"type", Constants.staticLoginType}
//                        ));
//                    } else {
//                        //普通用户
//                        new SendInfoTaskfollowcancelsb().execute(new TaskParams(
//                                Constants.Url + "?app=public&mod=AppFeedList&act=DelFollow",
//                                new String[]{"mid", Constants.staticmyuidstr},
//                                new String[]{"login_password", Constants.staticpasswordstr},
//                                new String[]{"tokey", Constants.statictokeystr},
//                                new String[]{"fid", listData.get(position).get("uid")}
//                        ));
//                    }
//                    holder.attentionuser1.setText("+ 关注");
//                    listData.get(position).put("following", "0");
//                    holder.attentionuser1.setTextColor(holder.attentionuser1.getResources().getColor(R.color.color_text2));
//                    holder.attentionuser1.setBackground(holder.attentionuser1.getResources().getDrawable(R.drawable.buttonstyle4));
                }
                // 关注 取消关注
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        jsonStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/user/follow?mid=" + Constants.staticmyuidstr + "&token=" + Constants.apptoken + "&uid=" + userEntity.getUid()+"&type="+type);
                        handler.sendEmptyMessage(0);
                    }
                }).start();
            }
        });
        ViewUtil.setUpVipNew(userEntity.getUser_group_icon_url(), holder.vipIv);

        return convertView;
    }

//    //关注
//    private class SendInfoTaskfollowsb extends AsyncTask<TaskParams, Void, String> {
//
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            super.onPostExecute(result);
//        }
//    }
//
//    //取消关注
//    private class SendInfoTaskfollowcancelsb extends
//            AsyncTask<TaskParams, Void, String> {
//
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            super.onPostExecute(result);
//        }
//    }

    static class ViewHolder {
        // 头像
        ImageView head;
        // 名称
        TextView name;
        // 简介
        TextView detailcomment;
        // 关注按钮
        Button attentionuser1;
        // 是否加V
        ImageView vipIv;
    }

    /**
     * 线程更新Ui
     */
    private Handler handler = new Handler() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        CustomToast.makeText(context,jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            return;
                        }
                        //点击关注
                        if ("0".equals(listData.get(count).getFollow().getFollowing())) {
                            listData.get(count).getFollow().setFollowing("1");
                        } else {
                            listData.get(count).getFollow().setFollowing("0");
                        }
                        notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

}
