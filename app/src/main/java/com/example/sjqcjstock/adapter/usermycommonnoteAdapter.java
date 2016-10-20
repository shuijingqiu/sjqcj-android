package com.example.sjqcjstock.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.sjqcjstock.Activity.mynoteslistActivity;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.Activity.transpondweiboActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的微博的适配器
 */
public class usermycommonnoteAdapter extends BaseAdapter {
    static String isdigg;
    private Context context;
    private ArrayList<HashMap<String, String>> listData;
    private mynoteslistActivity mynoteslistAc;

    public usermycommonnoteAdapter(Context context, mynoteslistActivity mynoteslistAc) {
        super();
        this.context = context;
        this.mynoteslistAc = mynoteslistAc;
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
        // TODO Auto-generated method stub
        if (position > 0) {
            return listData.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_mycommonnote, null);
            holder = new ViewHolder();
            holder.repostlin1 = (LinearLayout) convertView.findViewById(R.id.repostlin1);
            holder.repostusername1 = (TextView) convertView.findViewById(R.id.repostusername1);
            holder.repostweibocomment1 = (TextView) convertView.findViewById(R.id.repostweibocomment1);
            holder.image = (ImageView) convertView.findViewById(R.id.userimg1);
            holder.username = (TextView) convertView.findViewById(R.id.username1);
            holder.rewardZhuanTv = (TextView) convertView.findViewById(R.id.reward_zhuan_tv);
            holder.weibocomment1 = (TextView) convertView.findViewById(R.id.weibocomment1);
            holder.repostuser1 = (LinearLayout) convertView.findViewById(R.id.repostuser1);
            holder.createtime11 = (TextView) convertView.findViewById(R.id.createtime11);
            holder.repost_count2 = (TextView) convertView.findViewById(R.id.repost_count3);
            holder.comment_count2 = (TextView) convertView.findViewById(R.id.comment_count3);
            holder.pickdigg2 = (ImageView) convertView.findViewById(R.id.pickdigg2);
            holder.pickdigg3 = (LinearLayout) convertView.findViewById(R.id.pickdigg3);
            holder.transpond2 = (LinearLayout) convertView.findViewById(R.id.transpond2);
            holder.pickcomment1 = (LinearLayout) convertView.findViewById(R.id.pickcomment1);
            holder.shortweibolay1 = (LinearLayout) convertView.findViewById(R.id.shortweibolay1);
            holder.shortweibolay2 = (LinearLayout) convertView.findViewById(R.id.shortweibolay2);
            holder.shortweibolay3 = (LinearLayout) convertView.findViewById(R.id.shortweibolay3);
            holder.pickfromunametouserinfo1 = (LinearLayout) convertView.findViewById(R.id.pickfromunametouserinfo1);
            holder.pickdeleteweibo1 = (LinearLayout) convertView.findViewById(R.id.pickdeleteweibo1);
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
            //LinearLayout repostlin1=(LinearLayout)convertView.findViewById(R.id.repostlin1);
            holder.repostlin1.setVisibility(View.VISIBLE);
            TextView repostusername1 = (TextView) convertView.findViewById(R.id.repostusername1);
            repostusername1.setText((String) listData.get(position).get("sourceuname"));

            holder.repostweibocomment1.setText(Html.fromHtml(listData.get(position).get("source_contentstr"), ImageUtil.getImageGetter(context.getResources()), null));

            if (stateObj != null && !"0".equals(stateObj.toString())) {
                // 设置并显示水晶币个数
                holder.rewardZhuanTv.setText(listData.get(position).get("reward").toString());
                holder.rewardZhuanTv.setVisibility(View.VISIBLE);
            } else {
                holder.rewardZhuanTv.setVisibility(View.GONE);
            }

            holder.repostlin1.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    //String str=(String)listData.get(position).get("source_feed_idstr");
                    //String str2=(String)listData.get(position).get("sourceuidstr");
                    try {
                        Intent intent = new Intent(context.getApplicationContext(), forumnotedetailActivity.class);
                        intent.putExtra("weibo_id", (String) listData.get(position).get("source_feed_idstr"));
                        intent.putExtra("uid", (String) listData.get(position).get("sourceuidstr"));

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }


        holder.pickdeleteweibo1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                new AlertDialog.Builder(context)
                        .setMessage("确认删除吗")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub

                                new SendInfoTaskdeleteweibo().execute(new TaskParams(Constants.Url + "?app=public&mod=AppFeedList&act=AppremoveFeed",
                                                //new String[] { "login_email", "1061550505@qq.com" },
                                                //new String[] { "login_password", "12345678" },
                                                //new String[] { "P", "1" },
                                                //new String[] { "feed_id", weiboidstr}
                                                new String[]{"mid", Constants.staticmyuidstr},
                                                new String[]{"login_password", Constants.staticpasswordstr},
                                                new String[]{"tokey", Constants.statictokeystr},

                                                new String[]{"feed_id", (String) listData.get(position).get("feed_id")}


                                        )

                                );


                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        }).show();
            }
        });
        ImageLoader.getInstance().displayImage((String) listData.get(position).
                        get("avatar_middle"),
                holder.image, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());

        holder.image.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid", (String) listData.get(position).get("uid"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        holder.pickfromunametouserinfo1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid", (String) listData.get(position).get("uid"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        holder.username.setText((String) listData.get(position).get("uname"));
        holder.weibocomment1.setText(Html.fromHtml(listData.get(position).get("content"), ImageUtil.getImageGetter(context.getResources()), null));
        //点击进入转发用户
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
                }
            }
        });
        holder.createtime11.setText((String) listData.get(position).get("create"));
        holder.repost_count2.setText((String) listData.get(position).get("repost_count"));
        holder.comment_count2.setText((String) listData.get(position).get("comment_count"));
        final TextView digg_count2 = (TextView) convertView.findViewById(R.id.digg_count3);
        digg_count2.setText((String) listData.get(position).get("digg_count"));
        isdigg = (String) listData.get(position).get("isdigg");
        if ("1".equals(isdigg)) ;
        {
            holder.pickdigg2.setImageResource(R.mipmap.praise6_l);
        }
        final TextView diggsing1 = (TextView) convertView.findViewById(R.id.diggsing1);
        diggsing1.setText(isdigg);
        if ("1".equals(diggsing1.getText().toString())) {
            holder.pickdigg2.setImageResource(R.mipmap.praise6_l);
            digg_count2.setTextColor(digg_count2.getResources().getColor(R.color.color_000000));
        } else {
            holder.pickdigg2.setImageResource(R.mipmap.praise6);
            digg_count2.setTextColor(digg_count2.getResources().getColor(R.color.color_count));
        }

        //点赞操作
        holder.pickdigg3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //通过网络范围进行点赞操作
                //if("0".equals(is_digg)){
                if ("0".equals(diggsing1.getText().toString())) {

                    /***/
                    //进行点赞操作9

                    if (Utils.isFastDoubleClick2()) {
                        return;
                    }


                    //geneItems();
                    //mAdapter.notifyDataSetChanged();
                    new SendInfoTaskpraise().execute(new TaskParams(Constants.Url + "?app=public&mod=AppFeedList&act=AddDigg",
                                    new String[]{"mid", Constants.staticmyuidstr},
                                    new String[]{"login_password", Constants.staticpasswordstr},
                                    new String[]{"tokey", Constants.statictokeystr},

                                    new String[]{"feed_id", (String) listData.get(position).get("feed_id")}

                            )

                    );

                    String digg_count = digg_count2.getText().toString();

                    int diggcount = Integer.valueOf(digg_count);


                    diggcount++;

                    digg_count2.setText(String.valueOf(diggcount));
                    //(String)listData.get(position).get("digg_count");
                    listData.get(position).put("digg_count", String.valueOf(diggcount));

                    isdigg = "1";
                    listData.get(position).put("isdigg", isdigg);
                    notifyDataSetChanged();
                } else {
                    //进行取消点赞操作

                    if (Utils.isFastDoubleClick2()) {
                        return;
                    }

                    //geneItems();
                    //mAdapter.notifyDataSetChanged();                     //http://www.sjqcj.com/index.php?app=public&mod=AppFeedList&act=DelDigg
                    new SendInfoTaskcancelpraise().execute(new TaskParams(Constants.Url + "?app=public&mod=AppFeedList&act=DelDigg",
                                    new String[]{"mid", Constants.staticmyuidstr},
                                    new String[]{"login_password", Constants.staticpasswordstr},
                                    new String[]{"tokey", Constants.statictokeystr},

                                    new String[]{"feed_id", (String) listData.get(position).get("feed_id")}

                            )

                    );

                    String digg_count = digg_count2.getText().toString();

                    int diggcount = Integer.valueOf(digg_count);


                    diggcount--;

                    digg_count2.setText(String.valueOf(diggcount));
                    listData.get(position).put("digg_count", String.valueOf(diggcount));

                    isdigg = "0";
                    listData.get(position).put("isdigg", isdigg);
                    notifyDataSetChanged();


                }
            }
        });


        //进入转发页面
        //LinearLayout transpond2=(LinearLayout)convertView.findViewById(R.id.transpond2);
        holder.transpond2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                    Intent intent = new Intent(context.getApplicationContext(), transpondweiboActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//					intent.putExtra("feed_id",(String)listData.get(position).get("feed_id"));
//					context.startActivity(intent);

                    if (listData.get(position).get("source_contentstr") == null) {
                        intent.putExtra("feed_id", (String) listData.get(position).get("feed_id"));
//                        intent.putExtra("feeduid", (String) listData.get(position).get("uid"));
                    } else {
                        intent.putExtra("feed_id", (String) listData.get(position).get("feed_id"));
//                        intent.putExtra("feeduid", (String) listData.get(position).get("uid"));
//                        intent.putExtra("content", listData.get(position).get("content"));
//                        intent.putExtra("uname", (String) listData.get(position).get("uname"));
                    }
                    context.startActivity(intent);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } finally {

                }


            }
        });


        //LinearLayout pickcomment1=(LinearLayout)convertView.findViewById(R.id.pickcomment1);

        holder.pickcomment1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                    Intent intent = new Intent(context.getApplicationContext(), addcommentweiboActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("feed_id", (String) listData.get(position).get("feed_id"));
                    intent.putExtra("feeduid", (String) listData.get(position).get("uid"));

                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //短微博图片处理
        String attach_urlstr = (String) listData.get(position).get("type");
        if (!"postimage".equals(attach_urlstr)) {//不是短微博


        } else {//是短微博
            String[] attach_urlstrs = null;
            if (attach_urlstr != null) {
                String attach_urlstr2 = (String) listData.get(position).get("attach_url");

                attach_urlstrs = attach_urlstr2.split(",");

                int[] imageviews = {R.id.shortweiboimg1, R.id.shortweiboimg2, R.id.shortweiboimg3,
                        R.id.shortweiboimg4, R.id.shortweiboimg5, R.id.shortweiboimg6,
                        R.id.shortweiboimg7, R.id.shortweiboimg8, R.id.shortweiboimg9};

                for (int i = 0; i < attach_urlstrs.length; i++) {
                    String attachurl = attach_urlstrs[i];
                    if (!"".equals(attachurl)) {
                        attachurl = attachurl.substring(1, attachurl.length() - 1);
                        attachurl = "http://www.sjqcj.com/data/upload/" + attachurl;

                        //String  s="shortweiboimg1";
                        ImageView attachimage = (ImageView) convertView.findViewById(imageviews[i]);
                        //			image.setBackgroundResource((Integer)listData.get(position).get("friend_image"));
                        ImageLoader.getInstance().displayImage(attachurl,
                                attachimage, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());

                    }
                }


                if (!"".equals(attach_urlstrs[0]) && attach_urlstrs.length > 0 && attach_urlstrs.length <= 3) {
                    //shortweibolay1=(LinearLayout)convertView.findViewById(R.id.shortweibolay1);
                    holder.shortweibolay1.setVisibility(View.VISIBLE);

                }

                if (attach_urlstrs.length > 3 && attach_urlstrs.length <= 6) {
                    //shortweibolay1=(LinearLayout)convertView.findViewById(R.id.shortweibolay1);
                    //shortweibolay2=(LinearLayout)convertView.findViewById(R.id.shortweibolay2);
                    holder.shortweibolay1.setVisibility(View.VISIBLE);
                    holder.shortweibolay2.setVisibility(View.VISIBLE);

                }

                if (attach_urlstrs.length > 6) {
                    holder.shortweibolay1.setVisibility(View.VISIBLE);
                    holder.shortweibolay2.setVisibility(View.VISIBLE);
                    holder.shortweibolay3.setVisibility(View.VISIBLE);
                }
            }
        }

        String isVip = listData.get(position).get(
                "isVip");
        String isVipSource = listData.get(position).get(
                "isVipSource");
        ViewUtil.setUpVip(isVip, holder.vipImg);
        ViewUtil.setUpVip(isVipSource, holder.vipImgSource);
        return convertView;
    }


    private class SendInfoTaskdeleteweibo extends AsyncTask<TaskParams, Void, String> {

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
            //解析json字符串获得List<Map<String,Object>>
            List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
            for (Map<String, Object> map : lists) {
                //String infostr= map.get("info").toString();
                String statusstr = map.get("status").toString();
                String datastr = map.get("data").toString();
                if ("1".equals(statusstr)) {
                    mynoteslistAc.referActivity();
                }
            }
        }

    }


    private class SendInfoTaskpraise extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //CustomToast.makeText(supermanlistActivity.this, result, 1).show();

            /**
             result=result.replace("\n ","");
             result=result.replace("\n","");
             result=result.replace(" ","");
             result="["+result+"]";
             //解析json字符串获得List<Map<String,Object>>
             List<Map<String,Object>> lists=JsonTools.listKeyMaps(result);
             for(Map<String,Object> map:lists){
             String infostr= map.get("info").toString();
             String statusstr= map.get("status").toString();
             String datastr= map.get("data").toString();

             is_digg="1";



             }

             int diggcount2=Integer.parseInt(digg_count1.getText().toString());

             digg_count1.setText(String.valueOf(diggcount2+1));
             */
        }

    }


    private class SendInfoTaskcancelpraise extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //CustomToast.makeText(supermanlistActivity.this, result, 1).show();


            result = result.replace("\n ", "");
            result = result.replace("\n", "");
            result = result.replace(" ", "");
            result = "[" + result + "]";
            //解析json字符串获得List<Map<String,Object>>
            List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
            for (Map<String, Object> map : lists) {
                String infostr = map.get("info").toString();
                String statusstr = map.get("status").toString();

//						if("1".equals(statusstr)){
//							is_digg="0";
//		
//						}


            }

            //int diggcount2=Integer.parseInt(digg_count1.getText().toString());
            //digg_count1.setText(String.valueOf(diggcount2-1));


        }

    }


    static class ViewHolder {
        LinearLayout repostlin1;
        TextView repostusername1;
        TextView repostweibocomment1;
        ImageView image;
        TextView username;
        TextView rewardZhuanTv;
        TextView weibocomment1;
        LinearLayout repostuser1;
        TextView createtime11;
        TextView repost_count2;
        TextView comment_count2;
        ImageView pickdigg2;
        LinearLayout pickdigg3;
        LinearLayout transpond2;
        LinearLayout pickcomment1;
        LinearLayout shortweibolay1;
        LinearLayout shortweibolay2;
        LinearLayout shortweibolay3;
        LinearLayout pickfromunametouserinfo1;
        LinearLayout pickdeleteweibo1;
        ImageView vipImg;
        ImageView vipImgSource;


    }

}
