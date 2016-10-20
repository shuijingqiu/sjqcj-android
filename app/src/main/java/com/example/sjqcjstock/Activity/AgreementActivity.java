package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.javaScriptfaces.JavascriptInterface;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.ImageWebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 协议页面
 * Created by Administrator on 2016/5/6.
 */
public class AgreementActivity extends Activity {

    // 主体内容
    private WebView bodyWv;
    // 标题
    private TextView titleTv;
    // 网络请求提示
    private RelativeLayout rl_refresh_layout;
    // 传入Id
    private String serviceId = "7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_agreement);
        findView();
        initData();
    }

    /**
     * 控件的绑定
     */
    private void findView() {
        bodyWv = (WebView) findViewById(R.id.agreement_body_wv);
        titleTv = (TextView) findViewById(R.id.agreement_title_tv);
        rl_refresh_layout = (RelativeLayout) findViewById(R.id.rl_refresh_layout);
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 数据的获取
     */
    private void initData() {
        String type = getIntent().getStringExtra("type");
        if (type != null && !"".equals(type)) {
            serviceId = "8";
        }
        rl_refresh_layout.setVisibility(View.VISIBLE);
        new SendAgreementTask().execute(new TaskParams(Constants.agreementUrl,
                new String[]{"mid", Constants.staticmyuidstr}, new String[]{
                "id", serviceId}));
    }

    private class SendAgreementTask extends AsyncTask<TaskParams, Void, String> {
        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG)
                        .show();
                rl_refresh_layout.setVisibility(View.GONE);

            } else {
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    if ("1".equals(jsonObj.getString("status"))) {
                        JSONObject data = jsonObj.getJSONObject("data");
                        String content = data.getString("content");
                        String title = data.getString("title");
                        titleTv.setText(title);
                        bodyWv.getSettings().setJavaScriptEnabled(true);
                        bodyWv.addJavascriptInterface(new JavascriptInterface(AgreementActivity.this), "imagelistner");
                        bodyWv.setWebViewClient(new ImageWebViewClient(bodyWv));
                        bodyWv.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);

                    } else {
                        CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG)
                                .show();
                    }

                } catch (JSONException e) {
                    rl_refresh_layout.setVisibility(View.GONE);
                    e.printStackTrace();
                }
                rl_refresh_layout.setVisibility(View.GONE);
            }
        }
    }
}
