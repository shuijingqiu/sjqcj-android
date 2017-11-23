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
import com.example.sjqcjstock.Activity.Article.addcommentweiboActivity;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.Activity.transpondweiboActivity;
import com.example.sjqcjstock.Activity.user.loginActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.Article.FeedListEntity;
import com.example.sjqcjstock.netutil.CalendarUtil;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.example.sjqcjstock.view.ClickOnTouch;
import com.example.sjqcjstock.view.CustomToast;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 微博列表的item
 * 20170706 新规的adapter
 */
public class FeedListAdapter extends BaseAdapter {
    private String isdigg;
    private Context context;
    private List<FeedListEntity> feedListEntityList;
    // 当为我的微博时候出现删除按钮
    private String type;
    // 接口返回数据
    private String jsonStr;
    // 要删除的下标
    private int delPosition;

    public FeedListAdapter(Context context) {
        super();
        this.context = context;
    }

    public FeedListAdapter(Context context, String type) {
        super();
        this.context = context;
        this.type = type;
    }

    public void setlistData(ArrayList<FeedListEntity> feedListEntityList) {
        if (feedListEntityList != null) {
            this.feedListEntityList = (List<FeedListEntity>) feedListEntityList.clone();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return feedListEntityList == null ? 0 : feedListEntityList.size();
    }

    @Override
    public Object getItem(int position) {
        return feedListEntityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
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
            holder.img1 = (ImageView) convertView.findViewById(R.id.shortweiboimg1);
            holder.img2 = (ImageView) convertView.findViewById(R.id.shortweiboimg2);
            holder.img3 = (ImageView) convertView.findViewById(R.id.shortweiboimg3);
            holder.img4 = (ImageView) convertView.findViewById(R.id.shortweiboimg4);
            holder.img5 = (ImageView) convertView.findViewById(R.id.shortweiboimg5);
            holder.img6 = (ImageView) convertView.findViewById(R.id.shortweiboimg6);
            holder.img7 = (ImageView) convertView.findViewById(R.id.shortweiboimg7);
            holder.img8 = (ImageView) convertView.findViewById(R.id.shortweiboimg8);
            holder.img9 = (ImageView) convertView.findViewById(R.id.shortweiboimg9);
            holder.deleteArticleTv = (TextView) convertView.findViewById(R.id.delete_article_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.repostlin1.setVisibility(View.GONE);
        holder.shortweibolay1.setVisibility(View.GONE);
        holder.shortweibolay2.setVisibility(View.GONE);
        holder.shortweibolay3.setVisibility(View.GONE);

        final FeedListEntity feedListEntity = feedListEntityList.get(position);
        // 文章类型
        String feedType = feedListEntity.getType();
        // 显示的文章内容
        String feedContent  = feedListEntity.getFeed_content();
        // 显示文章的标题
        String feedTitle = feedListEntity.getTitle();
        // 是否是转发文章
        if ("repost".equals(feedType)) {
            // 转发微博的信息
            final FeedListEntity.ApiSource apiSource = feedListEntity.getApi_source();
            // 显示的文章内容
            String apifeedContent  = apiSource.getFeed_content();
            // 显示文章的标题
            String apifeedTitle = apiSource.getTitle();
            ViewUtil.setUpVipNew(apiSource.getUser_info().getUser_group_icon_url(), holder.vipImgSource);
            holder.repostlin1.setVisibility(View.VISIBLE);
            // TextView
            holder.repostusername1.setText(apiSource.getUser_info().getUname());
            // 文章类型
            String repostFeedType = apiSource.getType();
            if ("paid_post".equals(repostFeedType)) {
                // 设置并显示水晶币个数
                holder.rewardZhuanTv.setText(apiSource.getReward());
                holder.rewardZhuanTv.setVisibility(View.VISIBLE);
                // 付费微博显示内容为摘要
                apifeedContent = "摘要："+apiSource.getIntroduction();
            } else {
                holder.rewardZhuanTv.setText("");
                holder.rewardZhuanTv.setVisibility(View.GONE);
            }
            if (apifeedTitle != null && !"".equals(apifeedTitle.trim())){
                apifeedContent = "<font color=\"#4471BC\" >" + apifeedTitle + "</font><br/>" + apifeedContent;
            }
            CharSequence charSequence = Html.fromHtml(apifeedContent, ImageUtil.getImageGetter(context.getResources()), null);
            holder.repostweibocomment1.setText(charSequence);
            holder.repostweibocomment1.setOnTouchListener(new ClickOnTouch(context));

            holder.rewardTv.setVisibility(View.GONE);

//            holder.repostlin1.setOnClickListener(new OnClickListener() {
            holder.repostweibocomment1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Constants.isreferforumlist = "1";
                    try {
                        Intent intent = new Intent(context
                                .getApplicationContext(),
                                ArticleDetailsActivity.class);
                        intent.putExtra(
                                "weibo_id", apiSource.getFeed_id());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        } else {
            ViewUtil.setUpVipNew(feedListEntity.getUser_info().getUser_group_icon_url(), holder.vipImg);
            holder.rewardZhuanTv.setVisibility(View.GONE);
            // 外成微博为付费微博
            if ("paid_post".equals(feedType)) {
                // 设置并显示水晶币个数
                holder.rewardTv.setText(feedListEntity.getReward());
                // 付费微博显示内容为摘要
                feedContent = "摘要："+feedListEntity.getIntroduction();
                holder.rewardTv.setVisibility(View.VISIBLE);
            } else {
                holder.rewardTv.setText("");
                holder.rewardTv.setVisibility(View.GONE);
            }
        }
        ImageLoader.getInstance().displayImage(feedListEntity.getUser_info().getAvatar_middle(),
                holder.image, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        // 外层用户id
        final String uid = feedListEntity.getUser_info().getUid();
        // 外层文章id
        final String feedId = feedListEntity.getFeed_id();
        holder.image.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    Intent intent = new Intent(context.getApplicationContext(),
                            UserDetailNewActivity.class);
                    intent.putExtra("uid", uid);
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
                        Intent intent = new Intent(context
                                .getApplicationContext(),
                                UserDetailNewActivity.class);
                        intent.putExtra("uid", uid);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });

        holder.username.setText(feedListEntity.getUser_info().getUname());
        if (feedTitle != null && !"".equals(feedTitle.trim())){
            feedContent = "<font color=\"#4471BC\" >" + feedTitle + "</font><br/>" + feedContent;
        }
        CharSequence charSequence = Html.fromHtml(feedContent, ImageUtil.getImageGetter(context.getResources()), null);
        holder.weibocomment1.setText(charSequence);
        holder.weibocomment1.setOnTouchListener(new ClickOnTouch(context));

        holder.repostuser1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    Intent intent = new Intent(context.getApplicationContext(), UserDetailNewActivity.class);
                    intent.putExtra("uid", feedListEntity.getApi_source().getUser_info().getUid());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
        // 发布时间
        String create = CalendarUtil.formatDateTime(Utils
                .getStringtoDate(feedListEntity.getPublish_time()));
        holder.createtime11.setText(create);
        holder.repost_count2.setText(feedListEntity.getRepost_count());
        holder.comment_count2.setText(feedListEntity.getComment_count());
        holder.digg_count3.setText(feedListEntity.getDigg_count());
        isdigg = feedListEntity.getIs_digg();
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
                if (Utils.isFastDoubleClick2()) {
                    return;
                }
                // menghuan 不用登陆也可以用
                // 如果未登陆跳转到登陆页面
                if (!Constants.isLogin) {
                    Intent intent = new Intent(context, loginActivity.class);
                    context.startActivity(intent);
                    return;
                }
                // 通过网络范围进行点赞操作
                if ("0".equals(diggsing1.getText().toString())) {
                    // 添加点赞
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            jsonStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/feed/digg?feed_id=" + feedListEntity.getFeed_id() + "&token=" + Constants.apptoken + "&mid=" + Constants.staticmyuidstr + "&type=add");
                            handler.sendEmptyMessage(1);
                        }
                    }).start();
                    String digg_count = holder.digg_count3.getText().toString();
                    int diggcount = Integer.valueOf(digg_count);
                    diggcount++;
                    feedListEntityList.get(position).setDigg_count(diggcount + "");
                    isdigg = "1";
                    feedListEntityList.get(position).setIs_digg(isdigg);
                    notifyDataSetChanged();
                } else {
                    // 取消点赞
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            jsonStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/feed/digg?feed_id=" + feedListEntity.getFeed_id() + "&token=" + Constants.apptoken + "&mid=" + Constants.staticmyuidstr + "&type=cancel");
                            handler.sendEmptyMessage(1);
                        }
                    }).start();

                    String digg_count = holder.digg_count3.getText().toString();
                    int diggcount = Integer.valueOf(digg_count);
                    diggcount--;
                    feedListEntityList.get(position).setDigg_count(diggcount + "");

                    isdigg = "0";
                    feedListEntityList.get(position).setIs_digg(isdigg);
                    notifyDataSetChanged();

                }
            }
        });

        // 这个是干啥的 有问题 不知道要改
        // 进入转发页面
        holder.transpond2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // menghuan 不用登陆也可以用
                // 如果未登陆跳转到登陆页面
                if (!Constants.isLogin) {
                    Intent intent = new Intent(context, loginActivity.class);
                    context.startActivity(intent);
                    return;
                }
                try {
                    Intent intent = new Intent(context.getApplicationContext(), transpondweiboActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (feedListEntity.getApi_source() == null || feedListEntity.getApi_source().getFeed_content() == null) {
                        intent.putExtra("feed_id", feedId);
                        intent.putExtra("feeduid", uid);
                    } else {
                        intent.putExtra("feed_id", feedId);
                        intent.putExtra("feeduid", uid);
                        intent.putExtra("content", (CharSequence) feedListEntity.getFeed_content());
                        intent.putExtra("uname", feedListEntity.getUser_info().getUname());
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
                // menghuan 不用登陆也可以用
                // 如果未登陆跳转到登陆页面
                if (!Constants.isLogin) {
                    Intent intent = new Intent(context, loginActivity.class);
                    context.startActivity(intent);
                    return;
                }
                try {
                    Intent intent = new Intent(context.getApplicationContext(),
                            addcommentweiboActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("feed_id", feedId);
                    intent.putExtra("feeduid", uid);
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            // 短微博图片处理
            if ("postimage".equals(feedType)) {// 是短微博
                String attachUrl = feedListEntity.getAttach_url();
                if (!"".equals(attachUrl) && attachUrl != null) {
                    JSONArray jsonArray = new JSONArray(attachUrl);
                    int count = jsonArray.length();
                    if (count > 0) {
                        holder.img1.setVisibility(View.GONE);
                        holder.img2.setVisibility(View.GONE);
                        holder.img3.setVisibility(View.GONE);
                        holder.img4.setVisibility(View.GONE);
                        holder.img5.setVisibility(View.GONE);
                        holder.img6.setVisibility(View.GONE);
                        holder.img7.setVisibility(View.GONE);
                        holder.img8.setVisibility(View.GONE);
                        holder.img9.setVisibility(View.GONE);
                        for (int i = 0; i < count; i++) {
                            String attachurl = jsonArray.getString(i);
                            if (!"".equals(attachurl)) {
                                attachurl = Constants.newUrl + "/data/upload/"
                                        + attachurl;
                                ImageView attachimage = null;
                                switch (i) {
                                    case 0:
                                        holder.img1.setVisibility(View.VISIBLE);
                                        attachimage = holder.img1;
                                        break;
                                    case 1:
                                        holder.img2.setVisibility(View.VISIBLE);
                                        attachimage = holder.img2;
                                        break;
                                    case 2:
                                        holder.img3.setVisibility(View.VISIBLE);
                                        attachimage = holder.img3;
                                        break;
                                    case 3:
                                        holder.img4.setVisibility(View.VISIBLE);
                                        attachimage = holder.img4;
                                        break;
                                    case 4:
                                        holder.img5.setVisibility(View.VISIBLE);
                                        attachimage = holder.img5;
                                        break;
                                    case 5:
                                        holder.img6.setVisibility(View.VISIBLE);
                                        attachimage = holder.img6;
                                        break;
                                    case 6:
                                        holder.img7.setVisibility(View.VISIBLE);
                                        attachimage = holder.img7;
                                        break;
                                    case 7:
                                        holder.img8.setVisibility(View.VISIBLE);
                                        attachimage = holder.img8;
                                        break;
                                    case 8:
                                        holder.img9.setVisibility(View.VISIBLE);
                                        attachimage = holder.img9;
                                        break;
                                }
                                if (attachimage != null) {
                                    ImageLoader.getInstance().displayImage(attachurl,
                                            attachimage, ImageUtil.getOption(),
                                            ImageUtil.getAnimateFirstDisplayListener());
                                }
                            }
                        }
                        if (count > 0 && count <= 3) {
                            holder.shortweibolay1.setVisibility(View.VISIBLE);
                        }
                        if (count > 3 && count <= 6) {
                            holder.shortweibolay1.setVisibility(View.VISIBLE);
                            holder.shortweibolay2.setVisibility(View.VISIBLE);
                        }
                        if (count > 6) {
                            holder.shortweibolay1.setVisibility(View.VISIBLE);
                            holder.shortweibolay2.setVisibility(View.VISIBLE);
                            holder.shortweibolay3.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if ("myfeed".equals(type)) {
            // 如果为我的微博列表就出现删除按钮
            holder.deleteArticleTv.setVisibility(View.VISIBLE);
            // 水晶币个数
            holder.rewardTv.setVisibility(View.GONE);
            holder.deleteArticleTv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utils.isFastDoubleClick()) {
                        return;
                    }
                    new AlertDialog.Builder(context)
                            .setMessage("确认删除吗")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 删除微博
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            delPosition = position;
                                            jsonStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/feed/del?feed_id=" + feedListEntity.getFeed_id() + "&token=" + Constants.apptoken + "&mid=" + Constants.staticmyuidstr);
                                            handler.sendEmptyMessage(0);
                                        }
                                    }).start();
                                }
                            })
                            .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                }
            });
        }

        return convertView;
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
        ImageView img1;
        ImageView img2;
        ImageView img3;
        ImageView img4;
        ImageView img5;
        ImageView img6;
        ImageView img7;
        ImageView img8;
        ImageView img9;
        // 删除微博的字段
        TextView deleteArticleTv;
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
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        CustomToast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        if (Constants.successCode.equals(jsonObject.getString("code"))) {
                            // 刷新列表
                            feedListEntityList.remove(delPosition);
                            notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        CustomToast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
}
