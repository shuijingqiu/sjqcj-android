package com.example.sjqcjstock.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.example.sjqcjstock.Activity.Article.ArticleDetailsActivity;
import com.example.sjqcjstock.Activity.OfficialNewsActivity;
import com.example.sjqcjstock.Activity.Tomlive.DirectBroadcastingRoomActivity;
import com.example.sjqcjstock.Activity.advertUrlActivity;
import com.example.sjqcjstock.Activity.qa.MyQuestionAnswerActivity;
import com.example.sjqcjstock.Activity.stocks.ExpertSubscriptionActivity;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 *
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JpushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        Log.e(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.e(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.e(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            Log.e(TAG, "值1"+bundle.getString(JPushInterface.EXTRA_EXTRA));

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            Log.e(TAG, "值"+bundle.getString(JPushInterface.EXTRA_EXTRA));
            String jsonStr = bundle.getString(JPushInterface.EXTRA_EXTRA);
            if (Utils.isEmpty(jsonStr)){
                return;
            }
            try {
                JSONObject extraJson = new JSONObject(jsonStr);
                Intent i = null;
                String uid = "";
                String url = "";
                if (extraJson.length() > 0) {
                    int type = extraJson.getInt("type");
                   switch (type){
                        case 1:
                            // 1 版本更新
                            // 打开网页下载最新版本
                            // 打开的网址路径
                            url = extraJson.getString("url");
                            if (Utils.isWebsite(url)){
                                Uri uri = Uri.parse(url);
                                i = new Intent(Intent.ACTION_VIEW, uri);
                            }
                            break;
                       case 2:
                           uid = extraJson.getString("uid");
                           String roomId = extraJson.getString("room_id");
                           // 2.直播消息
                           i = new Intent(context, DirectBroadcastingRoomActivity.class);
                           // 房间ID
                           i.putExtra("roomId",roomId);
                           // 用户ID
                           i.putExtra("uid",uid);
                           break;
                       case 3:
                           // 3.牛人订阅
                           i = new Intent(context, ExpertSubscriptionActivity.class);
                           break;
                       case 4:
                           // 4.文章打开
                           i = new Intent(context, ArticleDetailsActivity.class);
                           String feed_id = extraJson.getString("feed_id");
                           // 文章的ID
                           i.putExtra("weibo_id",feed_id);
                           break;
                       case 5:
                           // 5.官方消息（就是一条话）
                           String message = bundle.getString(JPushInterface.EXTRA_ALERT);
                           i = new Intent(context, OfficialNewsActivity.class);
                           i.putExtra("message", message);
                           break;
                       case 6:
                           // 6.官方文章（跳转到文章打开页面）
                            String title = extraJson.getString("title");
                            url = extraJson.getString("url");
                           if (Utils.isWebsite(url)) {
                               i = new Intent(context, advertUrlActivity.class);
                               i.putExtra("url", url);
                               i.putExtra("title", title);
                           }
                           break;
                       case 7:
                           // 7.我的问答消息
                           i = new Intent(context, MyQuestionAnswerActivity.class);
                           i.putExtra("name", Constants.userEntity.getUname());
                           i.putExtra("intro", Constants.userEntity.getIntro());
                           break;
                       case 8:
                           uid = extraJson.getString("uid");
                           // 8.个人主页内部问答
                           i = new Intent(context, UserDetailNewActivity.class);
                           i.putExtra("uid", uid);
                           i.putExtra("type", "3");
                           i.putExtra("type3", "2");
                           break;

                    }
                    if (i != null){
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                        context.startActivity(i);
                    }
                }
            } catch (JSONException e) {
                Log.i(TAG, e.getMessage());
            }

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            }else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it =  json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " +json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
//        if (MainActivity.isForeground) {
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Log.d(TAG, "消息体message"+message);
            Log.d(TAG, "消息体extras"+extras);

//            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//            if (!ExampleUtil.isEmpty(extras)) {
//                try {
//                    JSONObject extraJson = new JSONObject(extras);
//                    if (extraJson.length() > 0) {
//                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//                    }
//                } catch (JSONException e) {
//
//                }
//
//            }
//            context.sendBroadcast(msgIntent);
//        }
    }
}
