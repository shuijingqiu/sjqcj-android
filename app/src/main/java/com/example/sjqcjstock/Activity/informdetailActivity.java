package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.InformationEntity;
import com.example.sjqcjstock.javaScriptfaces.JavascriptInterface;
import com.example.sjqcjstock.netutil.CalendarUtil;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.ImageWebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * 资讯详细页面
 */
public class informdetailActivity extends Activity {
    // 获取控件
    private LinearLayout goback1;
    private TextView informtitle1;
    private TextView informcreate1;
    private WebView informcomment1;
    // 获取intent 中的资讯id数据
    private String news_idstr;
    // 接口返回数据
    private String jsonStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.informdetail);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
    }

    private void initView() {
        // 获取网络数据
        Intent intent = getIntent();
        news_idstr = intent.getStringExtra("news_id");
        // 获取控件
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        informtitle1 = (TextView) findViewById(R.id.informtitle1);
        informcreate1 = (TextView) findViewById(R.id.informcreate1);
        informcomment1 = (WebView) findViewById(R.id.informcomment1);
        goback1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        network();
    }

    private void network() {
//        new SendInfoTask().execute(new TaskParams(
//                        Constants.Url + "?app=public&mod=Profile&act=Appnews",
//                        new String[]{"news_id", news_idstr}
//                )
//        );
        // 获取资讯详情
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonStr = HttpUtil.restHttpGet(Constants.newUrl+"/api/news/info?news_id="+news_idstr);
                handler.sendEmptyMessage(0);
            }
        }).start();
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
//
//            if (result == null) {
//                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT)
//                        .show();
//            } else {
//                super.onPostExecute(result);
//                result = "[" + result + "]";
//                // 解析json字符串获得List<Map<String,Object>>
//                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
//
//                for (Map<String, Object> map : lists) {
//                    String statusstr = map.get("data") + "";
//
//                    List<Map<String, Object>> informlists = JsonTools
//                            .listKeyMaps("[" + statusstr + "]");
//
//                    for (Map<String, Object> informmap : informlists) {
//                        String informmapstr = informmap.get("news_title")
//                                + "";
//                        String news_contentstr = informmap.get("news_content")
//                                + "";
//                        String createdstr = informmap.get("created") + "";
//
//                        // 将时间戳转换成date
//                        SimpleDateFormat formatter = new SimpleDateFormat(
//                                "yyyy-MM-dd HH:mm:ss");
//                        Date curDate = new Date(
//                                Long.parseLong(createdstr) * 1000); // 获取当前时间
//                        createdstr = formatter.format(curDate);
//                        createdstr = CalendarUtil.formatDateTime(createdstr);
//                        news_contentstr = news_contentstr.replace("/data/upload", "http://www.sjqcj.com/data/upload");
//                        news_contentstr = news_contentstr.replace("http://www.sjqcj.comhttp://www.sjqcj.com/data/upload", "http://www.sjqcj.com/data/upload");
//                        news_contentstr = news_contentstr.replace("<img", "<img onclick=\"getimg(this);\" style=\"max-width: 100%;height:auto\"  ");
//                        news_contentstr = news_contentstr.replace("[", "<img src=\"http://www.sjqcj.com/addons/theme/stv1/_static/image/expression/miniblog/");
//                        news_contentstr = news_contentstr.replace("]", ".gif\"  style=\"width:20px;height:20px;padding-top:0px;margin-top:0px\" > ");
//                        //全文
//                        informcomment1.getSettings().setJavaScriptEnabled(true);
//                        informcomment1.addJavascriptInterface(new JavascriptInterface(informdetailActivity.this), "imagelistner");
//                        informcomment1.setWebViewClient(new ImageWebViewClient(informcomment1));
//                        informcomment1.loadDataWithBaseURL(null, news_contentstr, "text/html", "utf-8", null);
//                        informtitle1.setText(informmapstr);
//                        informcreate1.setText(createdstr);
//                    }
//                }
//            }
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
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            CustomToast.makeText(informdetailActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        InformationEntity informationEntity = JSON.parseObject(jsonObject.getString("data"),InformationEntity.class);
                        String createdstr = informationEntity.getCreated();
                        // 将时间戳转换成date
                        SimpleDateFormat formatter = new SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss");
                        Date curDate = new Date(
                                Long.parseLong(createdstr) * 1000); // 获取当前时间
                        createdstr = formatter.format(curDate);
                        createdstr = CalendarUtil.formatDateTime(createdstr);
                        String content = informationEntity.getNews_content();
                        content = content.replace("<img", "<img onclick=\"getimg(this);\" style=\"max-width: 100%;height:auto\"  ");
                        //全文
                        informcomment1.getSettings().setJavaScriptEnabled(true);
                        informcomment1.addJavascriptInterface(new JavascriptInterface(informdetailActivity.this), "imagelistner");
                        informcomment1.setWebViewClient(new ImageWebViewClient(informcomment1));
                        informcomment1.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
                        informtitle1.setText(informationEntity.getNews_title());
                        informcreate1.setText(createdstr);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

}
