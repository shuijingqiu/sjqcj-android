package com.example.sjqcjstock.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.sjqcjstock.netutil.ViewUtil;

/**
 * Created by Administrator on 2016/7/29.
 */
public class ImageWebViewClient extends WebViewClient {

    private WebView webView;

    private Context context;

    public ImageWebViewClient(WebView webView) {
        this.webView = webView;
    }

    public ImageWebViewClient(WebView webView, Context context) {
        this.webView = webView;
        this.context = context;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        ViewUtil.LinkJump(context,url);
//        Intent intent = new Intent();
//        if (url.indexOf("/weibo") != -1) {
//            intent = new Intent(context, ArticleDetailsActivity.class);
//            String feedId = url.substring(url.indexOf("weibo/"));
//            feedId = feedId.replace("weibo/", "");
//            feedId = feedId.replace("/", "");
//            // 文章的ID
//            intent.putExtra("weibo_id", feedId);
//        } else if (url.indexOf("/space") != -1) {
//            // 个人主页内部微博
//            intent = new Intent(context, UserDetailNewActivity.class);
//            if (url.indexOf("mypay") != -1) {
//                // 内参
//                intent.putExtra("type", "2");
//            }
//            String uid = url.substring(url.indexOf("space/"), url.indexOf("space/") + 11);
//            uid = uid.replace("space/", "");
//            intent.putExtra("uid", uid);
//        } else if (url.indexOf("/ask") != -1) {
//            // 个人主页内部问答
//            intent = new Intent(context, UserDetailNewActivity.class);
//            String uid = url.substring(url.indexOf("ask/"));
//            uid = uid.replace("ask/", "");
//            uid = uid.replace("/", "");
//            intent.putExtra("uid", uid);
//            intent.putExtra("type", "3");
//        } else if (url.indexOf("personal") != -1) {
//            // 个人主页内部交易
//            intent = new Intent(context, UserDetailNewActivity.class);
//            String uid = url.substring(url.indexOf("uid="), url.indexOf("uid=") + 9);
//            uid = uid.replace("uid=", "");
//            intent.putExtra("uid", uid);
//            intent.putExtra("type", "1");
//        } else if (url.indexOf("/blogger") != -1) {
//            // 个人主页内部计划
//            intent = new Intent(context, UserDetailNewActivity.class);
//            String uid = url.substring(url.indexOf("blogger/"));
//            uid = uid.replace("blogger/", "");
//            uid = uid.replace("/", "");
//            intent.putExtra("uid", uid);
//            intent.putExtra("type", "4");
//        } else if (url.indexOf("/dealplan") != -1) {
//            if (url.indexOf("id=") == -1) {
//                // 打开系统浏览器加载网页
//                intent.setAction("android.intent.action.VIEW");
//                Uri content_url = Uri.parse(url);
//                intent.setData(content_url);
//            } else {
//                // 计划ID
//                String planId = url.substring(url.indexOf("id="));
//                // 用户id
//                String uid = "";
//                planId = planId.replace("uid=", "");
//                planId = planId.replace("id=", "");
//                String[] strs = planId.split("&");
//                if (strs.length>1) {
//                    planId = strs[0];
//                    uid = strs[1];
//                    // 跳转到计划详情
//                    intent = new Intent(context, PlanExhibitionActivity.class);
//                    // 计划id
//                    intent.putExtra("id", planId);
//                    // 用户ID
//                    intent.putExtra("uid", uid);
//                }
//            }
//        }else if(url.indexOf("/live")!=-1){
//            // 内部跳转到问答和直播里面
//            String uid = url.substring(url.indexOf("live/"));
//            uid=uid.replace("live/","");
//            uid=uid.replace("rid=","");
//            uid=uid.replace("/","");
//            String[] strs = uid.split("\\?");
//            if (strs.length>1){
//                intent = new Intent(context, DirectBroadcastingRoomActivity.class);
//                intent.putExtra("uid",strs[0]);
//                // 房间的ID
//                intent.putExtra("roomId",strs[1]);
//            }
//        }else if(url.indexOf("guba/")!=-1){
//            // 跳转到股票详细页面
//            intent = new Intent(context, SharesDetailedActivity.class);
//            String code = url.substring(url.indexOf("guba/"));
//            code=code.replace("guba/","");
//            code = code.substring(2);
//            intent.putExtra("code", code);
//        }
//        else {
//            // 打开系统浏览器加载网页
//            intent.setAction("android.intent.action.VIEW");
//            Uri content_url = Uri.parse(url);
//            intent.setData(content_url);
//        }
//        if (intent != null){
//            context.startActivity(intent);
//        }else{
//            // 打开系统浏览器加载网页
//            intent.setAction("android.intent.action.VIEW");
//            Uri content_url = Uri.parse(url);
//            intent.setData(content_url);
//        }

        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        view.getSettings().setJavaScriptEnabled(true);
        super.onPageFinished(view, url);
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistner.openImage(this.src);  " +
                "    }  " +
                "}" +
                "})()");
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        view.getSettings().setJavaScriptEnabled(true);
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);

    }
}

