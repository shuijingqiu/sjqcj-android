package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 我来选股页面
 */
public class IwillselectstockActivity extends Activity {

    // 获取返回控件
    private LinearLayout goback1;
    // 获取控件
    private Button submitmyselectstock1;
    private EditText onecode1;
    private EditText twocode1;
    private EditText one_vote1;
    private EditText two_vote1;
    // 网络请求提示
    private CustomProgress dialog;
    // 发送微博类型
    private String type = "1";
    // 选股接口返回数据
    private String jsonStr;
    // 发微博返回的数据
    private String feedStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.iwillselectstock);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
    }

    private void initView() {
        dialog = new CustomProgress(this);

        goback1 = (LinearLayout) findViewById(R.id.goback1);
        submitmyselectstock1 = (Button) findViewById(R.id.submitmyselectstock1);
        onecode1 = (EditText) findViewById(R.id.onecode1);
        twocode1 = (EditText) findViewById(R.id.twocode1);
        one_vote1 = (EditText) findViewById(R.id.one_vote1);
        two_vote1 = (EditText) findViewById(R.id.two_vote1);
        goback1.setOnClickListener(new goback4_listener());
        submitmyselectstock1
                .setOnClickListener(new submitmyselectstock1_listener());
        // 取消事件的按钮绑定
        findViewById(R.id.cancel_bt).setOnClickListener(new goback4_listener());

    }

    class goback4_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            finish();
        }
    }

    class submitmyselectstock1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            if (Utils.isFastDoubleClick()) {
                return;
            }
            final String one = onecode1.getText() + "";
            final String two = twocode1.getText() + "";
            final String one_voteStr = one_vote1.getText() + "";
            final String two_voteStr = two_vote1.getText() + "";
            if ("".equals(one)) {
                CustomToast.makeText(getApplicationContext(), "请输入第一只股票代码",
                        Toast.LENGTH_SHORT).show();
                onecode1.requestFocus();
                return;
            }
            if (one.length() != 6) {
                CustomToast.makeText(getApplicationContext(), "输入的第一只股票代码有误",
                        Toast.LENGTH_SHORT).show();
                onecode1.requestFocus();
                return;
            }
            if ("".equals(one_voteStr)) {
                CustomToast.makeText(getApplicationContext(), "请输入第一只选股理由",
                        Toast.LENGTH_SHORT).show();
                one_vote1.requestFocus();
                return;
            }
            if ("".equals(two)) {
                CustomToast.makeText(getApplicationContext(), "请输入第二只股票代码",
                        Toast.LENGTH_SHORT).show();
                twocode1.requestFocus();
                return;
            }
            if (two.length() != 6) {
                CustomToast.makeText(getApplicationContext(), "输入的第二只股票代码有误",
                        Toast.LENGTH_SHORT).show();
                twocode1.requestFocus();
                return;
            }
            if ("".equals(two_voteStr)) {
                CustomToast.makeText(getApplicationContext(), "请输入第二只选股理由",
                        Toast.LENGTH_SHORT).show();
                two_vote1.requestFocus();
                return;
            }
            dialog.showDialog();
//            new SendInfoTask()
//                    .execute(new TaskParams(
//                            Constants.Url + "?app=public&mod=AppFeedList&act=Appaddbattol",
//                            new String[]{"mid", Constants.staticmyuidstr},
//                            new String[]{"login_password", Constants.staticpasswordstr},
//                            new String[]{"tokey", Constants.statictokeystr},
//                            new String[]{"one", one},
//                            new String[]{"two", two},
//                            new String[]{"one_vote", one_voteStr},
//                            new String[]{"two_vote", two_voteStr}
//                    ));

            // 我来选股
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List dataList = new ArrayList();
                    dataList.add(new BasicNameValuePair("mid", Constants.staticmyuidstr));
                    dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                    dataList.add(new BasicNameValuePair("one", one));
                    dataList.add(new BasicNameValuePair("two", two));
                    dataList.add(new BasicNameValuePair("one_vote", one_voteStr));
                    dataList.add(new BasicNameValuePair("two_vote", two_voteStr));
                    jsonStr = HttpUtil.restHttpPost(Constants.newUrl + "/api/ballot/put", dataList);
                    handler.sendEmptyMessage(0);
                }
            }).start();
        }

    }

//    private class SendInfoTask extends AsyncTask<TaskParams, Void, String> {
//
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            super.onPostExecute(result);
//            if (result == null){
//                CustomToast.makeText(getApplicationContext(), "",
//                        Toast.LENGTH_SHORT).show();
//                return;
//            }
//            result = "[" + result + "]";
//            // 解析json字符串获得List<Map<String,Object>>
//            List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
//            for (Map<String, Object> map : lists) {
//                String statusstr = map.get("status") + "";
//                if ("1".equals(statusstr)) {
//                    if (map.get("body") != null) {
//                        String bodystr = map.get("body") + "";
//                        type = map.get("type") + "";
//                        if ("1".equals(type)){
//                            // 发短微博
//                            new SendInfoTaskaddlongweibo()
//                                    .execute(new TaskParams(
//                                            Constants.Url + "?app=public&mod=AppFeedList&act=AppPostFeed",
//                                            new String[]{"mid", Constants.staticmyuidstr},
//                                            new String[]{"login_password", Constants.staticpasswordstr},
//                                            new String[]{"tokey", Constants.statictokeystr},
//                                            new String[]{"app_name", "public"},
//                                            new String[]{"body", bodystr},
//                                            new String[]{"channel_id", "15"},
//                                            new String[]{"type", "post"}
//                                    ));
//                        }else{
//                            // 摘要
//                            String remark = map.get("remark") + "";
//                            String reward = map.get("reward") + "";
//                            String jid = map.get("jid") + "";
//
//                            // 发送打赏微博
//                            new SendInfoTaskaddlongweibo()
//                                    .execute(new TaskParams(
//                                            Constants.Url + "?app=public&mod=AppFeedList&act=AppPostFeed",
//                                            new String[]{"mid", Constants.staticmyuidstr},
//                                            new String[]{"login_password", Constants.staticpasswordstr},
//                                            new String[]{"tokey", Constants.statictokeystr},
//                                            new String[]{"app_name", "public"},
//                                            new String[]{"body", bodystr},
//                                            new String[]{"type", "long_post"},
//                                            new String[]{"introduction", remark},
//                                            new String[]{"reward", reward},
//                                            new String[]{"state", "1"},
//                                            new String[]{"jid", jid}
//                                    ));
//                        }
//                    }
//                } else {
//                    if (map.get("info") == null) {
//                        CustomToast.makeText(getApplicationContext(), "参赛失败",
//                                Toast.LENGTH_SHORT).show();
//                    } else {
//                        String infostr = map.get("info") + "";
//                        CustomToast.makeText(getApplicationContext(), infostr,
//                                Toast.LENGTH_SHORT).show();
//                    }
//                    dialog.dismissDlog();
//                }
//            }
//        }
//    }
//
//    private class SendInfoTaskaddlongweibo extends
//            AsyncTask<TaskParams, Void, String> {
//
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            super.onPostExecute(result);
//            result = result.replace("\n ", "");
//            result = result.replace("\n", "");
//            result = result.replace(" ", "");
//            result = "[" + result + "]";
//            // 解析json字符串获得List<Map<String,Object>>
//            List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
//            for (Map<String, Object> map : lists) {
//                String statusstr = map.get("status") + "";
//                if ("1".equals(statusstr)) {
//                    CustomToast.makeText(getApplicationContext(), "参赛成功",
//                            Toast.LENGTH_SHORT).show();
//                    if ("1".equals(type)){
//                        Intent intent = new Intent(IwillselectstockActivity.this, discussareaActivity.class);
//                        startActivity(intent);
//                    }
//                    dialog.dismissDlog();
//                    finish();
//                } else {
//                    CustomToast.makeText(getApplicationContext(), "参赛失败",
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
//            dialog.dismissDlog();
//        }
//    }

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
                        CustomToast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            // 请求失败的情况
                            dialog.dismissDlog();
                            return;
                        }
                        // 选股成功发送微博
                        JSONObject jsonData = new JSONObject(jsonObject.getString("data"));
                        //1短微博 2付费微博
                        final String type = jsonData.getString("type");
                        final String bodystr = jsonData.getString("body");
                        if ("1".equals(type)){
                            // 发短微博
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    List dataList = new ArrayList();
                                    dataList.add(new BasicNameValuePair("mid", Constants.staticmyuidstr));
                                    dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                                    dataList.add(new BasicNameValuePair("app_name", "public"));
                                    // 内容
                                    dataList.add(new BasicNameValuePair("body", bodystr));
                                    // post短微博 long_post长微博 paid_post付费微博 repost转发微博 postimage图片微博
                                    dataList.add(new BasicNameValuePair("type", "post"));
                                    dataList.add(new BasicNameValuePair("channel_id", "15"));
                                    feedStr = HttpUtil.restHttpPost(Constants.newUrl + "/api/feed/put", dataList);
                                    handler.sendEmptyMessage(1);
                                }
                            }).start();

                        }else {
                            // 发付费微博
                            // 摘要
                            final String remark = jsonData.getString("remark");
                            final String reward = jsonData.getString("reward");
                            final String title = jsonData.getString("title");
                            final String jid = jsonData.getString("jid");
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    List dataList = new ArrayList();
                                    dataList.add(new BasicNameValuePair("mid", Constants.staticmyuidstr));
                                    dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                                    dataList.add(new BasicNameValuePair("app_name", "public"));
                                    // 标题
                                    dataList.add(new BasicNameValuePair("title", title));
                                    // 概要
                                    dataList.add(new BasicNameValuePair("introduction", remark));
                                    // 内容
                                    dataList.add(new BasicNameValuePair("body", bodystr));
                                    // 打赏水晶币个数
                                    dataList.add(new BasicNameValuePair("reward", reward));
                                    // post短微博 long_post长微博 paid_post付费微博 repost转发微博 postimage图片微博
                                    dataList.add(new BasicNameValuePair("type", "paid_post"));
                                    dataList.add(new BasicNameValuePair("channel_id", "15"));
                                    // 选股id
                                    dataList.add(new BasicNameValuePair("jid", jid));
                                    feedStr = HttpUtil.restHttpPost(Constants.newUrl + "/api/feed/put", dataList);
                                    handler.sendEmptyMessage(1);
                                }
                            }).start();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    dialog.dismissDlog();
                    try {
                        JSONObject jsonObject = new JSONObject(feedStr);
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            // 请求失败的情况
                            CustomToast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // 返回给股吧页面标识
                        Constants.isreferforumlist = "0";
                        Intent intent = new Intent(IwillselectstockActivity.this, discussareaActivity.class);
                        startActivity(intent);
                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
}
