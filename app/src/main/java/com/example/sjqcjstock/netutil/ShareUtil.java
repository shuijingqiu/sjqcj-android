package com.example.sjqcjstock.netutil;

import android.content.Context;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 分享的共通
 * Created by Administrator on 2016/5/23.
 */
public class ShareUtil {

    /**
     * 分享文章的
     *
     * @param context
     * @param id      微博id
     * @param title   最多30个字符
     * @param str     最多40个字符
     */
    public static void showShare(Context context, String id, String title, String str) {
        if (str.length() > 40) {
            str = str.substring(0, 39);
        }
        String url = Constants.shareArticle + id;
        ShareSDK.initSDK(context);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(str);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        oks.setImageUrl("http://www.sjqcj.com/addons/theme/stv1/_static/image/applogo.png");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(str);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(context.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);
        // 启动分享GUI
        oks.show(context);
    }

}
