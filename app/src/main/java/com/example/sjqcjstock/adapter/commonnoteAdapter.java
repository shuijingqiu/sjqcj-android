package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.addcommentweiboActivity;
import com.example.sjqcjstock.Activity.forumnotedetailActivity;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.Activity.transpondweiboActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class commonnoteAdapter extends BaseAdapter {
    private String isdigg;
    private Context context;
    private ArrayList<HashMap<String, String>> listData;

    public commonnoteAdapter(Context context) {
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater mInflater = LayoutInflater.from(context);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_commonnote, null);
            holder = new ViewHolder();
            holder.repostlin1 = (LinearLayout) convertView.findViewById(R.id.repostlin1);
            holder.repostusername1 = (TextView) convertView.findViewById(R.id.repostusername1);
            holder.repostweibocomment1 = (TextView) convertView.findViewById(R.id.repostweibocomment1);
            holder.image = (ImageView) convertView.findViewById(R.id.userimg1);
            holder.username = (TextView) convertView.findViewById(R.id.username1);
            holder.rewardTv = (TextView) convertView.findViewById(R.id.reward);
            holder.rewardZhuanTv = (TextView) convertView.findViewById(R.id.reward_zhuan_tv);
            holder.weibocomment1 = (TextView) convertView.findViewById(R.id.weibocomment1);
            holder.repostuser1 = (LinearLayout) convertView.findViewById(R.id.repostuser1);
            holder.createtime11 = (TextView) convertView.findViewById(R.id.createtime11);
            holder.repost_count2 = (TextView) convertView.findViewById(R.id.repost_count3);
            holder.comment_count2 = (TextView) convertView.findViewById(R.id.comment_count3);
            holder.digg_count3 = (TextView) convertView.findViewById(R.id.digg_count3);
            holder.pickdigg2 = (ImageView) convertView.findViewById(R.id.pickdigg2);
            holder.pickdigg3 = (LinearLayout) convertView
                    .findViewById(R.id.pickdigg3);
            holder.transpond2 = (LinearLayout) convertView
                    .findViewById(R.id.transpond2);
            holder.pickcomment1 = (LinearLayout) convertView
                    .findViewById(R.id.pickcomment1);
            holder.shortweibolay1 = (LinearLayout) convertView
                    .findViewById(R.id.shortweibolay1);
            holder.shortweibolay2 = (LinearLayout) convertView
                    .findViewById(R.id.shortweibolay2);
            holder.shortweibolay3 = (LinearLayout) convertView
                    .findViewById(R.id.shortweibolay3);
            holder.pickfromunametouserinfo1 = (LinearLayout) convertView
                    .findViewById(R.id.pickfromunametouserinfo1);
            holder.vipImg = (ImageView) convertView.findViewById(R.id.vip_img);
            holder.vipImgSource = (ImageView) convertView.findViewById(R.id.vip_img_source);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        holder.repostlin1.setVisibility(View.GONE);
        holder.shortweibolay1.setVisibility(View.GONE);
        holder.shortweibolay2.setVisibility(View.GONE);
        holder.shortweibolay3.setVisibility(View.GONE);
        // 是否是转发文章
        Object stateObj = listData.get(position).get("state");
        if ("repost".equals((String) listData.get(position).get("type"))) {
            // LinearLayout
            // repostlin1=(LinearLayout)convertView.findViewById(R.id.repostlin1);
            holder.repostlin1.setVisibility(View.VISIBLE);
            // TextView
            // repostusername1=(TextView)convertView.findViewById(R.id.repostusername1);
            holder.repostusername1.setText((String) listData.get(position).get(
                    "sourceuname"));

            CharSequence charSequence = Html.fromHtml(listData.get(position).get("source_contentstr"), ImageUtil.getImageGetter(context.getResources()), null);

            holder.repostweibocomment1.setText(charSequence);

            if (stateObj != null && !"0".equals(stateObj.toString())) {
                // 设置并显示水晶币个数
                holder.rewardZhuanTv.setText(listData.get(position).get("reward").toString());
                holder.rewardZhuanTv.setVisibility(View.VISIBLE);
            } else {
                holder.rewardZhuanTv.setVisibility(View.GONE);
            }
            holder.rewardTv.setVisibility(View.GONE);

            holder.repostlin1.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    // String
                    // str=(String)listData.get(position).get("source_feed_idstr");
                    // String
                    // str2=(String)listData.get(position).get("sourceuidstr");
                    Constants.isreferforumlist = "1";
                    try {
                        Intent intent = new Intent(context
                                .getApplicationContext(),
                                forumnotedetailActivity.class);
                        intent.putExtra(
                                "weibo_id",
                                (String) listData.get(position).get(
                                        "source_feed_idstr"));
                        intent.putExtra("uid", (String) listData.get(position)
                                .get("sourceuidstr"));

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        } else {
            holder.rewardZhuanTv.setVisibility(View.GONE);
            if (stateObj != null && !"0".equals(stateObj.toString())) {
                // 设置并显示水晶币个数
                holder.rewardTv.setText(listData.get(position).get("reward").toString());
                holder.rewardTv.setVisibility(View.VISIBLE);
            } else {
                holder.rewardTv.setVisibility(View.GONE);
            }
        }

        // ImageView image=(ImageView)convertView.findViewById(R.id.userimg1);
        // image.setBackgroundResource((Integer)listData.get(position).get("friend_image"));
        ImageLoader.getInstance().displayImage(
                (String) listData.get(position).get("avatar_middle"),
                holder.image, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());

        holder.image.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                    Intent intent = new Intent(context.getApplicationContext(),
                            UserDetailNewActivity.class);
                    intent.putExtra("uid",
                            (String) listData.get(position).get("uid"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        holder.pickfromunametouserinfo1
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        try {
                            Intent intent = new Intent(context
                                    .getApplicationContext(),
                                    UserDetailNewActivity.class);
                            intent.putExtra("uid",
                                    (String) listData.get(position).get("uid"));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        }

                    }
                });

        holder.username.setText((String) listData.get(position).get("uname"));
        CharSequence charSequence = Html.fromHtml(listData.get(position).get("content"), ImageUtil.getImageGetter(context.getResources()), null);
        holder.weibocomment1.setText(charSequence);

        holder.repostuser1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid", (String) listData.get(position).get("sourceuidstr"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e2) {
                    e2.printStackTrace();
                } finally {

                }

            }
        });

        holder.createtime11.setText((String) listData.get(position).get(
                "create"));
        holder.repost_count2.setText((String) listData.get(position).get(
                "repost_count"));
        holder.comment_count2.setText((String) listData.get(position).get(
                "comment_count"));
        holder.digg_count3.setText((String) listData.get(position).get("digg_count"));
        isdigg = (String) listData.get(position).get("isdigg");
        final TextView diggsing1 = (TextView) convertView
                .findViewById(R.id.diggsing1);
        diggsing1.setText(isdigg);

        if ("1".equals(isdigg)) {
            holder.pickdigg2.setImageResource(R.mipmap.praise6_l);
            holder.digg_count3.setTextColor(holder.digg_count3.getResources().getColor(R.color.color_000000));
        } else {
            holder.pickdigg2.setImageResource(R.mipmap.praise6);
            holder.digg_count3.setTextColor(holder.digg_count3.getResources().getColor(R.color.color_count));
        }

        // 点赞操作
        holder.pickdigg3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // 通过网络范围进行点赞操作
                if ("0".equals(diggsing1.getText().toString())) {
                    /***/
                    // 进行点赞操作9
                    if (Utils.isFastDoubleClick2()) {
                        return;
                    }
                    new SendInfoTaskpraise().execute(new TaskParams(
                                    Constants.Url + "?app=public&mod=AppFeedList&act=AddDigg", new String[]{
                                    "mid", Constants.staticmyuidstr},
                                    new String[]{"login_password",
                                            Constants.staticpasswordstr},
                                    new String[]{"tokey", Constants.statictokeystr},

                                    new String[]{
                                            "feed_id",
                                            (String) listData.get(position).get(
                                                    "feed_id")}
                            )

                    );

                    String digg_count = holder.digg_count3.getText().toString();

                    int diggcount = Integer.valueOf(digg_count);

                    diggcount++;

                    holder.digg_count3.setText(String.valueOf(diggcount));
                    // (String)listData.get(position).get("digg_count");
                    listData.get(position).put("digg_count",
                            String.valueOf(diggcount));

                    isdigg = "1";
                    listData.get(position).put("isdigg", isdigg);
                    notifyDataSetChanged();

                    // refer();
                    // listData.get(position).put("isdigg", isdigg);


                } else {
                    // 进行取消点赞操作
                    if (Utils.isFastDoubleClick2()) {
                        return;
                    }
                    new SendInfoTaskcancelpraise().execute(new TaskParams(
                            Constants.Url + "?app=public&mod=AppFeedList&act=DelDigg", new String[]{
                            "mid", Constants.staticmyuidstr},
                            new String[]{"login_password",
                                    Constants.staticpasswordstr},
                            new String[]{"tokey", Constants.statictokeystr},

                            new String[]{
                                    "feed_id",
                                    (String) listData.get(position).get(
                                            "feed_id")}

                    ));

                    String digg_count = holder.digg_count3.getText().toString();

                    int diggcount = Integer.valueOf(digg_count);

                    diggcount--;

                    holder.digg_count3.setText(String.valueOf(diggcount));
                    listData.get(position).put("digg_count",
                            String.valueOf(diggcount));

                    isdigg = "0";
                    // listData.get(position).put("isdigg", isdigg);
                    listData.get(position).put("isdigg", isdigg);
                    notifyDataSetChanged();

                }
            }
        });

        // 进入转发页面
        holder.transpond2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                    Intent intent = new Intent(context.getApplicationContext(), transpondweiboActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (listData.get(position).get("source_contentstr") == null) {
                        intent.putExtra("feed_id", listData.get(position).get("feed_id"));
                        intent.putExtra("feeduid", listData.get(position).get("uid"));
                    } else {
                        intent.putExtra("feed_id", listData.get(position).get("feed_id"));
                        intent.putExtra("feeduid", listData.get(position).get("uid"));
                        intent.putExtra("content", (CharSequence) listData.get(position).get("content"));
                        intent.putExtra("uname", listData.get(position).get("uname"));
                    }
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        holder.pickcomment1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                    Intent intent = new Intent(context.getApplicationContext(),
                            addcommentweiboActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("feed_id", listData.get(position)
                            .get("feed_id"));
                    intent.putExtra("feeduid", listData.get(position)
                            .get("uid"));

                    context.startActivity(intent);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } finally {

                }

            }
        });

        // LinearLayout shortweibolay1;
        // LinearLayout shortweibolay2;
        // LinearLayout shortweibolay3;

        // 短微博图片处理
        // String attach_urlstr=
        // (String)listData.get(position).get("attach_url");
        String attach_urlstr = (String) listData.get(position).get("type");
        if (!"postimage".equals(attach_urlstr)) {// 不是短微博

        } else {// 是短微博
            // shortweibolay1=(LinearLayout)convertView.findViewById(R.id.shortweibolay1);
            // shortweibolay2=(LinearLayout)convertView.findViewById(R.id.shortweibolay2);
            // shortweibolay3=(LinearLayout)convertView.findViewById(R.id.shortweibolay3);
            //
            // shortweibolay1.setVisibility(View.VISIBLE);
            // shortweibolay2.setVisibility(View.VISIBLE);
            // shortweibolay3.setVisibility(View.VISIBLE);
            String[] attach_urlstrs = null;
            if (attach_urlstr != null) {
                String attach_urlstr2 = listData.get(position).get(
                        "attach_url");

                attach_urlstrs = attach_urlstr2.split(",");

                int[] imageviews = {R.id.shortweiboimg1, R.id.shortweiboimg2,
                        R.id.shortweiboimg3, R.id.shortweiboimg4,
                        R.id.shortweiboimg5, R.id.shortweiboimg6,
                        R.id.shortweiboimg7, R.id.shortweiboimg8,
                        R.id.shortweiboimg9};

                for (int i = 0; i < attach_urlstrs.length; i++) {
                    String attachurl = attach_urlstrs[i];
                    if (!"".equals(attachurl)) {
                        attachurl = attachurl.substring(1,
                                attachurl.length() - 1);
                        attachurl = "http://www.sjqcj.com/data/upload/"
                                + attachurl;

                        // String s="shortweiboimg1";
                        ImageView attachimage = (ImageView) convertView
                                .findViewById(imageviews[i]);
                        // image.setBackgroundResource((Integer)listData.get(position).get("friend_image"));
                        ImageLoader.getInstance().displayImage(attachurl,
                                attachimage, ImageUtil.getOption(),
                                ImageUtil.getAnimateFirstDisplayListener());
                    }
                }

                if (!"".equals(attach_urlstrs[0]) && attach_urlstrs.length > 0
                        && attach_urlstrs.length <= 3) {
                    // shortweibolay1=(LinearLayout)convertView.findViewById(R.id.shortweibolay1);
                    holder.shortweibolay1.setVisibility(View.VISIBLE);
                }

                if (attach_urlstrs.length > 3 && attach_urlstrs.length <= 6) {
                    // shortweibolay1=(LinearLayout)convertView.findViewById(R.id.shortweibolay1);
                    // shortweibolay2=(LinearLayout)convertView.findViewById(R.id.shortweibolay2);
                    holder.shortweibolay1.setVisibility(View.VISIBLE);
                    holder.shortweibolay2.setVisibility(View.VISIBLE);
                }

                if (attach_urlstrs.length > 6) {
                    // shortweibolay1=(LinearLayout)convertView.findViewById(R.id.shortweibolay1);
                    // shortweibolay2=(LinearLayout)convertView.findViewById(R.id.shortweibolay2);
                    // shortweibolay3=(LinearLayout)convertView.findViewById(R.id.shortweibolay3);
                    holder.shortweibolay1.setVisibility(View.VISIBLE);
                    holder.shortweibolay2.setVisibility(View.VISIBLE);
                    holder.shortweibolay3.setVisibility(View.VISIBLE);
                }
            }

            // shortweibolay1=(LinearLayout)convertView. (R.id.shortweibolay1);

            // shortweibolay1.setVisibility(View.VISIBLE);

        }

        //
        // CheckBox check=(CheckBox)convertView.findViewById(R.id.selected);

        // //判断用户是否已被选择，如被选择，则复选框为勾选，如未选择则复选框为可选
        // if((Boolean) listData.get(position).get("selected")){
        // state.put(position,true);
        //
        // }

        // check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        //
        // @Override
        // public void onCheckedChanged(CompoundButton buttonView, boolean
        // isChecked) {
        // // TODO Auto-generated method stub
        // if(isChecked){
        // state.put(position,isChecked);
        // }else {
        // state.remove(position);
        // }
        // }
        // });

        // check.setChecked((state.get(position)==null?false:true));
        String isVip = listData.get(position).get(
                "isVip");
        String isVipSource = listData.get(position).get(
                "isVipSource");
        ViewUtil.setUpVip(isVip, holder.vipImg);
        ViewUtil.setUpVip(isVipSource, holder.vipImgSource);

        return convertView;
    }

    private class SendInfoTaskpraise extends
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

            /**
             * result=result.replace("\n ",""); result=result.replace("\n","");
             * result=result.replace(" ",""); result="["+result+"]";
             * //解析json字符串获得List<Map<String,Object>> List<Map<String,Object>>
             * lists=JsonTools.listKeyMaps(result); for(Map<String,Object>
             * map:lists){ String infostr= map.get("info").toString(); String
             * statusstr= map.get("status").toString(); String datastr=
             * map.get("data").toString();
             *
             * is_digg="1";
             *
             *
             *  }
             *
             * int
             * diggcount2=Integer.parseInt(digg_count1.getText().toString());
             *
             * digg_count1.setText(String.valueOf(diggcount2+1));
             */
        }

    }

    private class SendInfoTaskcancelpraise extends
            AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            // 点赞成功
            // CustomToast.makeText(supermanlistActivity.this, result, 1).show();
//            result = result.replace("\n ", "");
//            result = result.replace("\n", "");
//            result = result.replace(" ", "");
//            result = "[" + result + "]";
//            // 解析json字符串获得List<Map<String,Object>>
//            List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
//            for (Map<String, Object> map : lists) {
//                String infostr = map.get("info").toString();
//                String statusstr = map.get("status").toString();
//
//                // if("1".equals(statusstr)){
//                // is_digg="0";
//                //
//                // }
//
//
//            }
//
//            // int
//            // diggcount2=Integer.parseInt(digg_count1.getText().toString());
//            // digg_count1.setText(String.valueOf(diggcount2-1));

        }

    }

    static class ViewHolder {
        LinearLayout repostlin1;
        TextView repostusername1;
        TextView repostweibocomment1;
        ImageView image;
        // 水晶币
        TextView rewardTv;
        // 转发微博的水晶币
        TextView rewardZhuanTv;
        TextView username;
        TextView weibocomment1;
        LinearLayout repostuser1;
        TextView createtime11;
        TextView repost_count2;
        TextView comment_count2;
        TextView digg_count3;
        ImageView pickdigg2;
        LinearLayout pickdigg3;
        LinearLayout transpond2;
        LinearLayout pickcomment1;
        LinearLayout shortweibolay1;
        LinearLayout shortweibolay2;
        LinearLayout shortweibolay3;
        LinearLayout pickfromunametouserinfo1;
        ImageView vipImg;
        ImageView vipImgSource;

    }

}
