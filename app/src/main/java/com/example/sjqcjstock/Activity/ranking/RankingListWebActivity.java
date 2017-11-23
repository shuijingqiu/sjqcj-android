package com.example.sjqcjstock.Activity.ranking;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.javaScriptfaces.RankingListJavascriptInterface;

/**
 * 各种排行榜的WebView
 * Created by Administrator on 2017/11/1.
 */
public class RankingListWebActivity extends Activity{

    // 显示页面的WebView
    private WebView inforWv;
    // 网络请求提示
    private RelativeLayout rl_refresh_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_list_web);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        findView();
    }

    private void findView() {
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rl_refresh_layout = (RelativeLayout) findViewById(R.id.rl_refresh_layout);
        inforWv = (WebView) findViewById(R.id.infor_wv);
        //设置支持js脚本，必须设置
        inforWv.getSettings().setJavaScriptEnabled(true);
        inforWv.addJavascriptInterface(RankingListJavascriptInterface.with(RankingListWebActivity.this), RankingListJavascriptInterface.NAME);
        inforWv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {//加载进度回调
                if (newProgress >= 90) {
                    rl_refresh_layout.setVisibility(View.GONE);
                    inforWv.setVisibility(View.VISIBLE);
                }
            }
        });

        inforWv.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                findViewById(R.id.error_tv).setVisibility(View.VISIBLE);
            }
        });
        inforWv.loadUrl(Constants.h5Url+"cattle_list.html");
    }
}
