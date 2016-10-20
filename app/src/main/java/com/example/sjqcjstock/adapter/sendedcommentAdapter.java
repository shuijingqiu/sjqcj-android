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
import android.widget.Toast;

import com.example.sjqcjstock.Activity.forumnotedetailActivity;
import com.example.sjqcjstock.Activity.sendedcommentActivity;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.example.sjqcjstock.view.CustomToast;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class sendedcommentAdapter extends BaseAdapter {

    private sendedcommentActivity sendedcommentActivity;
    private Context context;
    private ArrayList<HashMap<String, String>> listData;


    public sendedcommentAdapter(Context context, sendedcommentActivity sendedcommentActivity) {
        super();
        this.context = context;
        this.sendedcommentActivity = sendedcommentActivity;

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

        //动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_sendedcomment, null);

            holder = new ViewHolder();

            holder.image = (ImageView) convertView.findViewById(R.id.user_image1);
            holder.reward = (TextView) convertView.findViewById(R.id.reward);
            holder.contentname1 = (TextView) convertView.findViewById(R.id.contentname1);
            holder.contentbody1 = (TextView) convertView.findViewById(R.id.contentbody1);
            holder.originalnotecontent1 = (TextView) convertView.findViewById(R.id.originalnotecontent1);
            holder.contenttimes1 = (TextView) convertView.findViewById(R.id.contenttimes1);
            holder.sourceusername1 = (TextView) convertView.findViewById(R.id.sourceusername1);
//            holder.repost_count3 = (TextView) convertView.findViewById(R.id.repost_count3);
//            holder.digg_count3 = (TextView) convertView.findViewById(R.id.digg_count3);
//            holder.comment_count3 = (TextView) convertView.findViewById(R.id.comment_count3);
//            holder.commentreply1 = (ImageView) convertView.findViewById(R.id.commentreply1);
            holder.repostlin1 = (LinearLayout) convertView.findViewById(R.id.repostlin1);

            holder.repostuser1 = (LinearLayout) convertView.findViewById(R.id.repostuser1);
            //holder.sourceusername2 = (LinearLayout)convertView.findViewById(R.id.sourceusername2);
//            holder.pickcomment1 = (LinearLayout) convertView.findViewById(R.id.pickcomment1);
//            holder.transpond2 = (LinearLayout) convertView.findViewById(R.id.transpond2);
//            holder.pickdigg3 = (LinearLayout) convertView.findViewById(R.id.pickdigg3);
            holder.sourceusername1 = (TextView) convertView.findViewById(R.id.sourceusername1);

//            holder.repost_count3 = (TextView) convertView.findViewById(R.id.repost_count3);
//            holder.digg_count3 = (TextView) convertView.findViewById(R.id.digg_count3);
//            holder.comment_count3 = (TextView) convertView.findViewById(R.id.comment_count3);
            holder.pickuserinfo1 = (LinearLayout) convertView.findViewById(R.id.pickuserinfo1);
            holder.pickdeletecomment1 = (TextView) convertView.findViewById(R.id.pickdeletecomment1);

            holder.vipImg = (ImageView) convertView.findViewById(R.id.vip_img);
            holder.vipImgSource = (ImageView) convertView.findViewById(R.id.vip_img_source);


            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage((String) listData.get(position).
                        get("avatar_middlestr"),
                holder.image, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());

        holder.image.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid", Constants.staticmyuidstr);
                    context.startActivity(intent);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } finally {

                }

            }
        });


        //LinearLayout pickuserinfo1=(LinearLayout)convertView.findViewById(R.id.pickuserinfo1);

        holder.pickuserinfo1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid", Constants.staticmyuidstr);
                    context.startActivity(intent);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } finally {

                }

            }
        });

        //LinearLayout sourceusername2=(LinearLayout)convertView.findViewById(R.id.sourceusername2);

//		holder.sourceusername2.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent intent=new Intent(context.getApplicationContext(),UserDetailNewActivity.class);
//				intent.putExtra("uid",(String)listData.get(position).get("sourceuidstr"));
//				context.startActivity(intent);
//			}
//		});


        //LinearLayout repostuser1=(LinearLayout)convertView.findViewById(R.id.repostuser1);

        holder.repostuser1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid", (String) listData.get(position).get("sourceuidstr"));
                    context.startActivity(intent);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } finally {

                }

            }
        });

//		
        //TextView originalname1=(TextView)convertView.findViewById(R.id.originalname1);
        //holder.originalname1.setText((String)listData.get(position).get("sourceunamestr"));

        //TextView sourceusername1=(TextView)convertView.findViewById(R.id.sourceusername1);
        holder.sourceusername1.setText((String) listData.get(position).get("sourceunamestr"));


        //TextView contentuname1=(TextView)convertView.findViewById(R.id.contentname1);
        holder.contentname1.setText((String) listData.get(position).get("contentunamestr"));

//		TextView contentcreate1=(TextView)convertView.findViewById(R.id.contentcreate1);
//		contentcreate1.setText((String)listData.get(position).get("ctimestr"));

        //TextView contentbody1=(TextView)convertView.findViewById(R.id.contentbody1);
        holder.contentbody1.setText(Html.fromHtml(listData.get(position).get("contentstr"), ImageUtil.getImageGetter(context.getResources()), null));

        //TextView sourceusername2=(TextView)convertView.findViewById(R.id.sourceusername22);
        //sourceusername2.setText((String)listData.get(position).get("sourceunamestr"));

        //TextView sourcetime1=(TextView)convertView.findViewById(R.id.contenttimes1);
        holder.contenttimes1.setText(listData.get(position).get("ctimestr"));


        //TextView repost_count3=(TextView)convertView.findViewById(R.id.repost_count3);
//        holder.repost_count3.setText((String) listData.get(position).get("repost_count"));

        //TextView digg_count3=(TextView)convertView.findViewById(R.id.digg_count3);
//        holder.digg_count3.setText((String) listData.get(position).get("digg_count"));

        //TextView comment_count3=(TextView)convertView.findViewById(R.id.comment_count3);
//        holder.comment_count3.setText((String) listData.get(position).get("comment_count"));

        // 显示水晶币
        if (listData.get(position).get("state") != null && "1".equals(listData.get(position).get("state"))) {
            holder.reward.setText(listData.get(position).get("reward").toString());
            holder.reward.setVisibility(View.VISIBLE);
        } else {
            holder.reward.setVisibility(View.GONE);
        }

        //LinearLayout repostlin1=(LinearLayout)convertView.findViewById(R.id.repostlin1);

        holder.repostlin1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                    Intent intent = new Intent(context.getApplicationContext(), forumnotedetailActivity.class);
                    intent.putExtra("weibo_id", (String) listData.get(position).get("sourcefeed_id"));
                    intent.putExtra("uid", (String) listData.get(position).get("sourceuidstr"));

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e2) {
                    // TODO: handle exception
                    e2.printStackTrace();
                } finally {

                }


            }
        });


        holder.pickdeletecomment1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                new AlertDialog.Builder(context)
                        .setMessage("确认删除吗？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new SendInfoTaskdeleteweibo().execute(new TaskParams(Constants.Url + "?app=public&mod=AppFeedList&act=Appdelcomment",
                                                new String[]{"mid", Constants.staticmyuidstr},
                                                new String[]{"login_password", Constants.staticpasswordstr},
                                                new String[]{"comment_id", (String) listData.get(position).get("comment_id")}
                                        )
                                );
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                //dialog.dismiss();   //关闭alertDialog
                            }
                        }).show();

            }
        });


        //LinearLayout pickcomment1=(LinearLayout)convertView.findViewById(R.id.pickcomment1);

//        holder.pickcomment1.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//                try {
//                    Intent intent = new Intent(context.getApplicationContext(), addcommentweiboActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtra("feed_id", (String) listData.get(position).get("sourcefeed_id"));
//                    intent.putExtra("feeduid", (String) listData.get(position).get("sourceuidstr"));
//
//                    context.startActivity(intent);
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                } finally {
//
//                }
//
//
//            }
//        });

//		

        //LinearLayout transpond2=(LinearLayout)convertView.findViewById(R.id.transpond2);

//        holder.transpond2.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//                try {
//                    Intent intent = new Intent(context.getApplicationContext(), transpondweiboActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtra("feed_id", (String) listData.get(position).get("sourcefeed_id"));
//                    context.startActivity(intent);
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace();
//                } finally {
//
//                }
//
//
//            }
//        });


        //TextView originalnotecontent1=(TextView)convertView.findViewById(R.id.originalnotecontent1);
        holder.originalnotecontent1.setText(Html.fromHtml(listData.get(position).get("app_detail_summarystr"), ImageUtil.getImageGetter(context.getResources()), null));

//		ImageView originaluserimg1=(ImageView)convertView.findViewById(R.id.originaluserimg1);
//		  //image.setBackgroundResource((Integer)listData.get(position).get("friend_image"));
//		  ImageLoader.getInstance().displayImage((String)listData.get(position).
//	    		get("sourceavatar_middlestr"),
//	    		originaluserimg1,options,new AnimateFirstDisplayListener());
//		  
//		  originaluserimg1.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent intent=new Intent(context.getApplicationContext(),UserDetailNewActivity.class);
//				intent.putExtra("uid",(String)listData.get(position).get("uidstr"));
//				context.startActivity(intent);
//			}
//		  });
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
                String statusstr = map.get("status").toString();
                if ("1".equals(statusstr)) {
                    sendedcommentActivity.referActivity();
                    CustomToast.makeText(context, map.get("data").toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    static class ViewHolder {
        LinearLayout pickuserinfo1;
        ImageView image;
        TextView reward;
        TextView contentname1;
        TextView contentbody1;
        TextView originalnotecontent1;
        TextView contenttimes1;
        TextView sourceusername1;
        //        TextView repost_count3;
//        TextView digg_count3;
//        TextView comment_count3;
//        ImageView commentreply1;
        LinearLayout repostlin1;
        LinearLayout repostuser1;
        //LinearLayout sourceusername2;
//        LinearLayout pickcomment1;
//        LinearLayout transpond2;
//        LinearLayout pickdigg3;
//        TextView sourcetime1;
        TextView pickdeletecomment1;
        ImageView vipImg;
        ImageView vipImgSource;
    }

}
