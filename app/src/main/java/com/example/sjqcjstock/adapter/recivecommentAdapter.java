package com.example.sjqcjstock.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.addreplycommentweiboActivity;
import com.example.sjqcjstock.Activity.forumnotedetailActivity;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class recivecommentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, String>> listData;

    public recivecommentAdapter(Context context) {
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
        //动态加载布局
        LayoutInflater mInflater = LayoutInflater.from(context);
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_recivecomment, null);

            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.user_image1);
            //holder.originalname1 = (TextView)convertView.findViewById(R.id.originalname1);
            holder.contentname1 = (TextView) convertView.findViewById(R.id.contentname1);
            holder.contentbody1 = (TextView) convertView.findViewById(R.id.contentbody1);
            holder.reward = (TextView) convertView.findViewById(R.id.reward);
            holder.originalnotecontent1 = (TextView) convertView.findViewById(R.id.originalnotecontent1);
            holder.contenttimes1 = (TextView) convertView.findViewById(R.id.contenttimes1);
            holder.sourceusername1 = (TextView) convertView.findViewById(R.id.sourceusername1);
//	        holder.repost_count3 = (TextView)convertView.findViewById(R.id.repost_count3);
//	        holder.digg_count3 = (TextView)convertView.findViewById(R.id.digg_count3);
//	        holder.comment_count3 = (TextView)convertView.findViewById(R.id.comment_count3);
            holder.commentreply1 = (TextView) convertView.findViewById(R.id.commentreply1);
            holder.repostlin1 = (LinearLayout) convertView.findViewById(R.id.repostlin1);

            holder.repostuser1 = (LinearLayout) convertView.findViewById(R.id.repostuser1);
            //holder.sourceusername2 = (LinearLayout)convertView.findViewById(R.id.sourceusername2);
//	        holder.pickcomment1 = (LinearLayout)convertView.findViewById(R.id.pickcomment1);
//	        holder.transpond2 = (LinearLayout)convertView.findViewById(R.id.transpond2);
//            holder.pickdigg3 = (LinearLayout) convertView.findViewById(R.id.pickdigg3);
            holder.sourceusername1 = (TextView) convertView.findViewById(R.id.sourceusername1);

//	        holder.repost_count3 = (TextView)convertView.findViewById(R.id.repost_count3);
//	        holder.digg_count3 = (TextView)convertView.findViewById(R.id.digg_count3);
//	        holder.comment_count3 = (TextView)convertView.findViewById(R.id.comment_count3);
//	        holder.replyInfostr1=(TextView)convertView.findViewById(R.id.replyInfostr1);

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
                Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                if ("".equals((String) listData.get(position).get("uidstr"))) {

                    intent.putExtra("uid", (String) listData.get(position).get("sourceuid"));
                    context.startActivity(intent);
                } else {
                    intent.putExtra("uid", (String) listData.get(position).get("uidstr"));
                    context.startActivity(intent);
                }
            }
        });

//		
        //TextView originalname1=(TextView)convertView.findViewById(R.id.originalname1);
        //holder.originalname1.setText((String)listData.get(position).get("sourceunamestr"));

        //TextView contentname1=(TextView)convertView.findViewById(R.id.contentname1);
        holder.contentname1.setText((String) listData.get(position).get("contentunamestr"));
        holder.contentbody1.setText(Html.fromHtml(listData.get(position).get("contentstr"), ImageUtil.getImageGetter(context.getResources()), null));
//        holder.contentbody1.setText(listData.get(position).get("contentstr"));

        holder.originalnotecontent1.setText(Html.fromHtml(listData.get(position).get("source_content"), ImageUtil.getImageGetter(context.getResources()), null));

        //TextView contenttimes1=(TextView)convertView.findViewById(R.id.contenttimes1);
        holder.contenttimes1.setText((String) listData.get(position).get("ctimestr"));

        //TextView sourceusername1=(TextView)convertView.findViewById(R.id.sourceusername1);
        holder.sourceusername1.setText((String) listData.get(position).get("sourceunamestr"));

        // 显示水晶币
        if (listData.get(position).get("state") != null && "1".equals(listData.get(position).get("state"))) {
            holder.reward.setText(listData.get(position).get("reward").toString());
            holder.reward.setVisibility(View.VISIBLE);
        } else {
            holder.reward.setVisibility(View.GONE);
        }

        holder.commentreply1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    Intent intent = new Intent(context.getApplicationContext(), addreplycommentweiboActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("feed_id", (String) listData.get(position).get("sourcefeed_id"));
                    intent.putExtra("feeduid", (String) listData.get(position).get("sourceuid"));
                    intent.putExtra("to_uid", (String) listData.get(position).get("uidstr"));
                    intent.putExtra("comment_id", (String) listData.get(position).get("comment_id"));
                    intent.putExtra("oldname", (String) listData.get(position).get("contentunamestr"));
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        //LinearLayout repostlin1=(LinearLayout)convertView.findViewById(R.id.repostlin1);
        //comment_count3.setText((String)listData.get(position).get("comment_count"));

        holder.repostlin1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                    Intent intent = new Intent(context.getApplicationContext(), forumnotedetailActivity.class);
                    intent.putExtra("weibo_id", (String) listData.get(position).get("sourcefeed_id"));
                    intent.putExtra("uid", (String) listData.get(position).get("sourceuid"));

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } finally {

                }


            }
        });


        //LinearLayout repostuser1=(LinearLayout)convertView.findViewById(R.id.repostuser1);

        holder.repostuser1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                try {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid", (String) listData.get(position).get("sourceuid"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
//				intent.putExtra("uid",(String)listData.get(position).get("sourceuid"));
//				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
//				context.startActivity(intent);
//			}
//		});


        //LinearLayout pickcomment1=(LinearLayout)convertView.findViewById(R.id.pickcomment1);

//		holder.pickcomment1.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				try {
//					Intent intent=new Intent(context.getApplicationContext(),addcommentweiboActivity.class);
//					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					intent.putExtra("feed_id",(String)listData.get(position).get("sourcefeed_id"));
//					intent.putExtra("feeduid",(String)listData.get(position).get("sourceuid"));
//
//					context.startActivity(intent);
//				} catch (Exception e) {
//					// TODO: handle exception
//					e.printStackTrace();
//				}finally{
//
//				}
//
//			}
//		});


        //LinearLayout transpond2=(LinearLayout)convertView.findViewById(R.id.transpond2);
//
//		holder.transpond2.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				try {
//					Intent intent=new Intent(context.getApplicationContext(),transpondweiboActivity.class);
//					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					intent.putExtra("feed_id",(String)listData.get(position).get("sourcefeed_id"));
//					context.startActivity(intent);
//				} catch (Exception e) {
//					// TODO: handle exception
//					e.printStackTrace();
//				}finally{
//
//				}
//
//			}
//		});
//

        //LinearLayout pickdigg3 = (LinearLayout)convertView.findViewById(R.id.pickdigg3);

//        holder.pickdigg3.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//

//					if("0".equals(diggsing1.getText().toString())){
//					
//					/***/
//					//进行点赞操作9
//					
//					if (Utils.isFastDoubleClick()) {  
//				          return;  
//				      }   
//				 
//							
//							//geneItems();
//							//mAdapter.notifyDataSetChanged();
//							new SendInfoTaskpraise().execute(new TaskParams(Constants.Url+"?app=public&mod=AppFeedList&act=AddDigg",
//									new String[] { "mid", Constants.staticmyuidstr}, 
//									new String[] { "login_password", Constants.staticpasswordstr }, 
//									new String[] { "feed_id'", (String)listData.get(position).get("feed_id") }
//
//								)
//							
//							);
//							
//							String digg_count=digg_count2.getText().toString();
//							
//							int diggcount=Integer.valueOf(digg_count);
//							
//							
//							diggcount++;
//							
//							digg_count2.setText(String.valueOf(diggcount));
//							//(String)listData.get(position).get("digg_count");
//							listData.get(position).put("digg_count", String.valueOf(diggcount));
//
//							isdigg="1";
//							listData.get(position).put("isdigg", isdigg);
//							if(fragmentForum!=null){
//						         fragmentForum.test(listData);
//								}
//							notifyDataSetChanged();
//
//							//refer();
//							//listData.get(position).put("isdigg", isdigg);
//
//					
//					
//					
//					
//				}else{
//					//进行取消点赞操作
//					
//					if (Utils.isFastDoubleClick()) {  
//				          return;  
//				      }   
//					
//							//geneItems();
//							//mAdapter.notifyDataSetChanged();                     //http://www.sjqcj.com/index.php?app=public&mod=AppFeedList&act=DelDigg
//							new SendInfoTaskcancelpraise().execute(new TaskParams(Constants.Url+"?app=public&mod=AppFeedList&act=DelDigg",
//									new String[] { "mid", Constants.staticmyuidstr }, 
//									new String[] { "login_password", Constants.staticpasswordstr }, 
//									new String[] { "feed_id'", (String)listData.get(position).get("feed_id") }
//
//								)
//							
//							);
//							
//							String digg_count=digg_count2.getText().toString();
//							
//							int diggcount=Integer.valueOf(digg_count);
//							
//							
//							diggcount--;
//							
//							digg_count2.setText(String.valueOf(diggcount));
//							listData.get(position).put("digg_count", String.valueOf(diggcount));
//
//							isdigg="0";
//							//listData.get(position).put("isdigg", isdigg);
//							listData.get(position).put("isdigg", isdigg);
//							if(fragmentForum!=null){
//						         fragmentForum.test(listData);
//								}
//							//refer();
//							notifyDataSetChanged();
//
//
//					
//
//				}
//
//
//            }
//        });

        String isVip = listData.get(position).get(
                "isVip");
        String isVipSource = listData.get(position).get(
                "isVipSource");
        ViewUtil.setUpVip(isVip, holder.vipImg);
        ViewUtil.setUpVip(isVipSource, holder.vipImgSource);
        return convertView;
    }


//
//	private class SendInfoTaskpraise extends AsyncTask<TaskParams, Void, String> {
//
//		@Override
//		protected String doInBackground(TaskParams... params) {
//			return HttpUtil.doInBackground(params);
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//		// TODO Auto-generated method stub
//		super.onPostExecute(result);
//		//CustomToast.makeText(supermanlistActivity.this, result, 1).show();
//
//		        /**
//				result=result.replace("\n ","");
//				result=result.replace("\n","");
//				result=result.replace(" ","");
//				result="["+result+"]";
//				//解析json字符串获得List<Map<String,Object>>
//				List<Map<String,Object>> lists=JsonTools.listKeyMaps(result);
//					for(Map<String,Object> map:lists){
//						String infostr= map.get("info").toString();
//						String statusstr= map.get("status").toString();
//						String datastr= map.get("data").toString();
//
//						is_digg="1";
//
//
//
//					}
//
//					int diggcount2=Integer.parseInt(digg_count1.getText().toString());
//
//					digg_count1.setText(String.valueOf(diggcount2+1));
//					*/
//		}
//
//	}
//
//
//
//
//
//	private class SendInfoTaskcancelpraise extends AsyncTask<TaskParams, Void, String> {
//
//		@Override
//		protected String doInBackground(TaskParams... params) {
//			return HttpUtil.doInBackground(params);
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//		// TODO Auto-generated method stub
//		super.onPostExecute(result);
//		//CustomToast.makeText(supermanlistActivity.this, result, 1).show();
//
//
//				result=result.replace("\n ","");
//				result=result.replace("\n","");
//				result=result.replace(" ","");
//				result="["+result+"]";
//				//解析json字符串获得List<Map<String,Object>>
//				List<Map<String,Object>> lists=JsonTools.listKeyMaps(result);
//					for(Map<String,Object> map:lists){
//						String infostr= map.get("info").toString();
//						String statusstr= map.get("status").toString();
//
////						if("1".equals(statusstr)){
////							is_digg="0";
////
////						}
//
//
//
//
//					}
//
//					//int diggcount2=Integer.parseInt(digg_count1.getText().toString());
//					//digg_count1.setText(String.valueOf(diggcount2-1));
//
//
//		}
//
//	}
//

    static class ViewHolder {
        ImageView image;
        //TextView originalname1;
        TextView contentname1;
        TextView contentbody1;
        // 需要打赏的水晶币
        TextView reward;
        TextView originalnotecontent1;
        TextView contenttimes1;
        TextView sourceusername1;
        //		TextView repost_count3;
//		TextView digg_count3;
//		TextView comment_count3;
        TextView commentreply1;
        LinearLayout repostlin1;
        LinearLayout repostuser1;
        //LinearLayout sourceusername2;
//		LinearLayout pickcomment1;
//		LinearLayout transpond2;
//        LinearLayout pickdigg3;
//		TextView replyInfostr1;
        ImageView vipImg;
        ImageView vipImgSource;
    }


}
