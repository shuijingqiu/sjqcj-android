package com.example.sjqcjstock.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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

import com.example.sjqcjstock.Activity.Article.ArticleDetailsActivity;
import com.example.sjqcjstock.Activity.Article.CommentActivity;
import com.example.sjqcjstock.Activity.Article.addreplycommentweiboActivity;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.Article.CommentEntity;
import com.example.sjqcjstock.netutil.CalendarUtil;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.example.sjqcjstock.view.ClickOnTouch;
import com.example.sjqcjstock.view.CustomToast;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 收到和发出评论的Adapter
 */
public class CommentAdapter extends BaseAdapter {

    private CommentActivity commentActivity;
    private Context context;
    private ArrayList<CommentEntity> listData;
    // 评论还是被评论
    private String type;
    // 操作接口返回数据
    private String jsonStr;

    public CommentAdapter(Context context, CommentActivity commentActivity,String type) {
        super();
        this.context = context;
        this.commentActivity = commentActivity;
        this.type = type;
    }

    public void setlistData(ArrayList<CommentEntity> listData) {
        if (this.listData != null)
            this.listData.clear();
        this.listData = (ArrayList<CommentEntity>) listData.clone();
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
            convertView = mInflater.inflate(R.layout.list_item_comment, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.user_image1);
            holder.reward = (TextView) convertView.findViewById(R.id.reward);
            holder.contentname1 = (TextView) convertView.findViewById(R.id.contentname1);
            holder.contentbody1 = (TextView) convertView.findViewById(R.id.contentbody1);
            holder.originalnotecontent1 = (TextView) convertView.findViewById(R.id.originalnotecontent1);
            holder.contenttimes1 = (TextView) convertView.findViewById(R.id.contenttimes1);
            holder.sourceusername1 = (TextView) convertView.findViewById(R.id.sourceusername1);
            holder.repostlin1 = (LinearLayout) convertView.findViewById(R.id.repostlin1);
            holder.repostuser1 = (LinearLayout) convertView.findViewById(R.id.repostuser1);
            holder.sourceusername1 = (TextView) convertView.findViewById(R.id.sourceusername1);
            holder.pickuserinfo1 = (LinearLayout) convertView.findViewById(R.id.pickuserinfo1);
            holder.pickdeletecomment1 = (TextView) convertView.findViewById(R.id.pickdeletecomment1);
            holder.vipImg = (ImageView) convertView.findViewById(R.id.vip_img);
            holder.vipImgSource = (ImageView) convertView.findViewById(R.id.vip_img_source);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if ("receive".equals(type)){
            // 收到的评论
            holder.pickdeletecomment1.setText("回复");
        }else{
            // 发出的评论
            holder.pickdeletecomment1.setText("删除");
        }
        final CommentEntity commentEntity = listData.get(position);
        final CommentEntity.SourceInfo sourceInfo = commentEntity.getSourceInfo();
        final String uid = commentEntity.getUser_info().getUid();
        ImageLoader.getInstance().displayImage(commentEntity.getUser_info().getAvatar_middle(),
                holder.image, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());

        holder.image.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid", uid);
                    context.startActivity(intent);
            }
        });
        holder.pickuserinfo1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid", uid);
                    context.startActivity(intent);
            }
        });
        holder.repostuser1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid", sourceInfo.getUid());
                    context.startActivity(intent);
            }
        });
        holder.sourceusername1.setText(sourceInfo.getUname());
        holder.contentname1.setText(commentEntity.getUser_info().getUname());
        holder.contentbody1.setText(Html.fromHtml(commentEntity.getContent(), ImageUtil.getImageGetter(context.getResources()), null));
        holder.contentbody1.setOnTouchListener(new ClickOnTouch(context));

        // 发布时间
        String cime = CalendarUtil.formatDateTime(Utils
                .getStringtoDate(commentEntity.getCtime()));
        holder.contenttimes1.setText(cime);

        // 评论的文章
        String feedContent = sourceInfo.getFeed_content();

        String feedType = sourceInfo.getType();
        // 显示水晶币
        if (feedType != null && "paid_post".equals(feedType)) {
            holder.reward.setText(sourceInfo.getReward());
            holder.reward.setVisibility(View.VISIBLE);
            feedContent = "<font color=\"#4471BC\" >" + sourceInfo.getTitle() + "</font><Br/>摘要：" + sourceInfo.getIntroduction();
        } else {
            holder.reward.setVisibility(View.GONE);
        }
        holder.repostlin1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                    Intent intent = new Intent(context.getApplicationContext(), ArticleDetailsActivity.class);
                    intent.putExtra("weibo_id", sourceInfo.getFeed_id());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
            }
        });


        holder.pickdeletecomment1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                if ("receive".equals(type)){
                    // 收到的评论
                    // 跳转到回复页面
                    Intent intent = new Intent(context.getApplicationContext(), addreplycommentweiboActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("feed_id", sourceInfo.getFeed_id());
                    intent.putExtra("feeduid", sourceInfo.getUid());
                    intent.putExtra("to_uid", uid);
                    intent.putExtra("comment_id", commentEntity.getComment_id());
                    intent.putExtra("oldname", commentEntity.getUser_info().getUname());
                    context.startActivity(intent);
                }else{
                    // 发出的评论
                    new AlertDialog.Builder(context)
                            .setMessage("确认删除吗？")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 删除评论
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            jsonStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/comment/del?mid=" + Constants.staticmyuidstr + "&token=" + Constants.apptoken + "&comment_id=" + commentEntity.getComment_id());
                                            handler.sendEmptyMessage(0);
                                        }
                                    }).start();
                                }
                            })
                            .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss(); //关闭alertDialog
                                }
                            }).show();
                }

            }
        });
        // 评论文章的内容
        holder.originalnotecontent1.setText(Html.fromHtml(feedContent+"", ImageUtil.getImageGetter(context.getResources()), null));
        holder.originalnotecontent1.setOnTouchListener(new ClickOnTouch(context));
        ViewUtil.setUpVipNew(commentEntity.getUser_info().getUser_group_icon_url(), holder.vipImg);
        ViewUtil.setUpVipNew(sourceInfo.getUser_group_icon_url(), holder.vipImgSource);
        return convertView;
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
        LinearLayout repostlin1;
        LinearLayout repostuser1;
        TextView pickdeletecomment1;
        ImageView vipImg;
        ImageView vipImgSource;
    }

    /**
     * 线程更新Ui
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    // 删除评论
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        CustomToast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        if (Constants.successCode.equals(jsonObject.getString("code"))) {
                            commentActivity.referActivity();
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };


}
