package com.example.sjqcjstock.javaScriptfaces;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.Activity.Article.NewsDetailActivity;
import com.example.sjqcjstock.Activity.ShowImageActivity;
import com.example.sjqcjstock.Activity.Tomlive.DirectBroadcastingRoomActivity;
import com.example.sjqcjstock.Activity.firm.FirmDetailsActivity;
import com.example.sjqcjstock.Activity.firm.FirmHallActivity;
import com.example.sjqcjstock.Activity.firm.FirmUserHomePageActivity;
import com.example.sjqcjstock.Activity.plan.UserPlanActivity;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.firm.FirmDetailEntity;
import com.example.sjqcjstock.entity.inform.NewsEntity;
import com.example.sjqcjstock.entity.inform.PictureBean;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 注入到WebView中的java对象
 */
public final class RankingListJavascriptInterface {
    private static final String TAG = "JavascriptInterface";
    public static final String NAME = "JSInterface";
    private Context mContext;
    private String jsonStr = "";

    public static RankingListJavascriptInterface with(@NonNull Context context){
        return new RankingListJavascriptInterface(context);
    }

    private RankingListJavascriptInterface(@NonNull Context context) {
        mContext = context;
    }

    /**
     * 跳转到个人主页 (默认页)
     */
    @android.webkit.JavascriptInterface
    public void openNewsDetailPageWb(String uid) {
        if (TextUtils.isEmpty(uid)){
            Log.e(TAG,"---h5端js调用所传参数为null----");
            return;
        }
        Intent intent = new Intent();
        intent.setClass(mContext, UserDetailNewActivity.class);
        intent.putExtra("uid", uid);
        mContext.startActivity(intent);
    }

    /**
     * 跳转到直播页面
     */
    @android.webkit.JavascriptInterface
    public void openTomlivePersonnePage(String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)){
            Log.e(TAG,"---h5端js调用所传参数为null----");
            return;
        }
        Log.e("mh1238",jsonStr);
        try {
            JSONObject json = new JSONObject(jsonStr);
            Intent intent = new Intent(mContext,DirectBroadcastingRoomActivity.class);
            intent.putExtra("roomId",json.getString("id"));
            intent.putExtra("uid",json.getString("uid"));
            intent.putExtra("name",json.getString("name"));
            intent.putExtra("remark",json.getString("remark"));
            intent.putExtra("type",json.getString("type"));
            mContext.startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到实盘比赛页面
     */
    @android.webkit.JavascriptInterface
    public void openBullDealerPage(final String id) {
        // 获取实盘比赛信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/match/minfo?mid=" + Constants.staticmyuidstr + "&match_id=" +id);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    /**
     * 跳转到实盘比赛页面 (交易页)
     */
    @android.webkit.JavascriptInterface
    public void openNewsDetailPageJy(String uid) {
        if (TextUtils.isEmpty(uid)){
            Log.e(TAG,"---h5端js调用所传参数为null----");
            return;
        }
        Intent intent = new Intent();
        intent.setClass(mContext, UserDetailNewActivity.class);
        intent.putExtra("uid", uid);
        intent.putExtra("type", "1");
        mContext.startActivity(intent);
    }

    /**
     * 跳转到计划主页页面
     */
    @android.webkit.JavascriptInterface
    public void openNewsPlanPage(String uid,String name) {
        if (TextUtils.isEmpty(uid)){
            Log.e(TAG,"---h5端js调用所传参数为null----");
            return;
        }
        Intent intent = new Intent();
        intent.setClass(mContext, UserPlanActivity.class);
        intent.putExtra("uid", uid);
        intent.putExtra("name", name);
        mContext.startActivity(intent);
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
                        JSONObject json = new JSONObject(jsonStr);
                        if (!Constants.successCode.equals(json.getString("code"))){
                            return;
                        }
                        JSONObject jsonObject = json.getJSONObject("data");
                        Intent intent = new Intent(mContext,FirmDetailsActivity.class);
                        intent.putExtra("id",jsonObject.getString("id"));
                        intent.putExtra("feedId",jsonObject.getString("feed_id"));
                        intent.putExtra("uname",jsonObject.getString("sponsor"));
                        intent.putExtra("title",jsonObject.getString("title"));
                        intent.putExtra("state",jsonObject.getString("state"));
                        intent.putExtra("isJoin",jsonObject.getString("is_join"));
                        intent.putExtra("residue",jsonObject.getString("residue"));
                        mContext.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };


}

