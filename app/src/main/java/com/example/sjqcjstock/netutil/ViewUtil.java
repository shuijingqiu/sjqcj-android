package com.example.sjqcjstock.netutil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.Article.ArticleDetailsActivity;
import com.example.sjqcjstock.Activity.Tomlive.DirectBroadcastingRoomActivity;
import com.example.sjqcjstock.Activity.plan.PlanExhibitionActivity;
import com.example.sjqcjstock.Activity.stocks.SharesDetailedActivity;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 一些视图的共通处理方法
 * Created by Administrator on 2016/5/25.
 */
public class ViewUtil {

    /**
     * 判断值设置字体颜色
     *
     * @param tv  需要设置的字体
     * @param str 判断的值
     */
    public static void setViewColor(TextView tv, String str) {
        str = Utils.getNumberFormat(str);
        float floatCount = Float.valueOf(str);
        if (floatCount > 0) {
            tv.setTextColor(Color.RED);
            tv.setText("+" + str + "%");
        } else if (floatCount < 0) {
            tv.setTextColor(Color.rgb(139, 195, 74));
            tv.setText(str + "%");
        } else {
            tv.setTextColor(Color.rgb(51, 51, 51));
            tv.setText(str + "%");
        }
    }


    /**
     * 获取listView动态高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View mView = listAdapter.getView(i, null, listView);
            mView.measure(0, 0);
            totalHeight += mView.getMeasuredHeight() + 15;
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    /**
     * 处理该用户是否是VIP 并委托设置图像
     *
     * @param str
     * @param imageView
     */
    public static void  setUpVip(String str, ImageView imageView) {
        if (str == null) {
            return;
        }
        str = str.replace("\\/","/");
        if (str.length() > 10) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
            return;
        }
        str = str.substring(str.lastIndexOf("http"));
        str = str.substring(0, str.indexOf("\""));
        if (str.length() > 10)
            ImageLoader.getInstance().displayImage(str,
                    imageView, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
    }

    /**
     * 处理该用户是否是VIP 并委托设置图像
     *
     * @param str
     * @param imageView
     */
    public static void  setUpVipNew(String str, ImageView imageView) {
        if (str == null || "".equals(str) ) {
            imageView.setVisibility(View.GONE);
            return;
        } else {
            imageView.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(str,
                    imageView, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        }
    }

    /**
     * 控制一些超链接跳转
     *
     * @param context
     * @param url
     */
    public static void LinkJump(Context context, String url){
        Intent intent = new Intent();
        if (url.indexOf("sjqcj.com/weibo/") != -1) {
            intent = new Intent(context, ArticleDetailsActivity.class);
            String feedId = url.substring(url.indexOf("weibo/"));
            feedId = feedId.replace("weibo/", "");
            feedId = feedId.replace("/", "");
            // 文章的ID
            intent.putExtra("weibo_id", feedId);
        } else if (url.indexOf("sjqcj.com/space") != -1) {
            // 个人主页内部微博
            intent = new Intent(context, UserDetailNewActivity.class);
            if (url.indexOf("mypay") != -1) {
                // 内参
                intent.putExtra("type", "2");
            }
            String uid = url.substring(url.indexOf("space/"), url.indexOf("space/") + 11);
            uid = uid.replace("space/", "");
            intent.putExtra("uid", uid);
        } else if (url.indexOf("sjqcj.com/ask") != -1) {
            // 个人主页内部问答
            intent = new Intent(context, UserDetailNewActivity.class);
            String uid = url.substring(url.indexOf("ask/"));
            uid = uid.replace("ask/", "");
            uid = uid.replace("/", "");
            intent.putExtra("uid", uid);
            intent.putExtra("type", "3");
        } else if (url.indexOf("personal") != -1) {
            // 个人主页内部交易
            intent = new Intent(context, UserDetailNewActivity.class);
            String uid = url.substring(url.indexOf("uid="), url.indexOf("uid=") + 9);
            uid = uid.replace("uid=", "");
            intent.putExtra("uid", uid);
            intent.putExtra("type", "1");
        } else if (url.indexOf("sjqcj.com/blogger") != -1) {
            // 个人主页内部计划
            intent = new Intent(context, UserDetailNewActivity.class);
            String uid = url.substring(url.indexOf("blogger/"));
            uid = uid.replace("blogger/", "");
            uid = uid.replace("/", "");
            intent.putExtra("uid", uid);
            intent.putExtra("type", "4");
        } else if (url.indexOf("sjqcj.com/dealplan") != -1) {
            if (url.indexOf("id=") == -1) {
                // 打开系统浏览器加载网页
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(url);
                intent.setData(content_url);
            } else {
                // 计划ID
                String planId = url.substring(url.indexOf("id="));
                // 用户id
                String uid = "";
                planId = planId.replace("uid=", "");
                planId = planId.replace("id=", "");
                String[] strs = planId.split("&");
                if (strs.length>1) {
                    planId = strs[0];
                    uid = strs[1];
                    // 跳转到计划详情
                    intent = new Intent(context, PlanExhibitionActivity.class);
                    // 计划id
                    intent.putExtra("id", planId);
                    // 用户ID
                    intent.putExtra("uid", uid);
                }
            }
        }else if(url.indexOf("/live")!=-1){
            // 内部跳转到问答和直播里面
            String uid = url.substring(url.indexOf("live/"));
            uid=uid.replace("live/","");
            uid=uid.replace("rid=","");
            uid=uid.replace("/","");
            String[] strs = uid.split("\\?");
            if (strs.length>1){
                intent = new Intent(context, DirectBroadcastingRoomActivity.class);
                intent.putExtra("uid",strs[0]);
                // 房间的ID
                intent.putExtra("roomId",strs[1]);
            }
        }else if(url.indexOf("guba/")!=-1){
            // 跳转到股票详细页面
            intent = new Intent(context, SharesDetailedActivity.class);
            String code = url.substring(url.indexOf("guba/"));
            code=code.replace("guba/","");
            code = code.substring(2);
            intent.putExtra("code", code);
        }else {
            // 打开系统浏览器加载网页
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
        }
        if (intent != null){
            context.startActivity(intent);
//        }else{
//            // 打开系统浏览器加载网页
//            intent.setAction("android.intent.action.VIEW");
//            Uri content_url = Uri.parse(url);
//            intent.setData(content_url);
        }

    }

}
