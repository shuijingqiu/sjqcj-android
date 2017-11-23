package com.example.sjqcjstock.Activity.Article;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.javaScriptfaces.NewsJavascriptInterface;
import com.example.sjqcjstock.netutil.ShareUtil;

/**
 * 资讯详情页面
 * Created by Administrator on 2017/10/20.
 */
public class NewsDetailActivity extends Activity{

    // 主体内容
    private WebView newsWv;
    // 标题
    private TextView titleTv1;
    // 网络请求提示
    private RelativeLayout rl_refresh_layout;
    // 摘要
    private String summary;
    // 标题
    private String title;
    // 路径
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_detail);
        findView();
        initData();
    }

    /**
     * 控件的绑定
     */
    private void findView() {
        newsWv = (WebView) findViewById(R.id.news_detail_wv);
        titleTv1 = (TextView) findViewById(R.id.title_tv);
        rl_refresh_layout = (RelativeLayout) findViewById(R.id.rl_refresh_layout);
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        newsWv.setWebViewClient(new WebViewClient() {
            /**
             * 若没有设置 WebViewClient 则在点击链接之后由系统处理该 url，通常是使用浏览器打开或弹出浏览器选择对话框。
             * 若设置 WebViewClient 且该方法返回 true ，则说明由应用的代码处理该 url，WebView 不处理。
             * 若设置 WebViewClient 且该方法返回 false，则说明由 WebView 处理该 url，即用 WebView 加载该 url。
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {//页面加载完成
                super.onPageFinished(view, url);
            }
        });
        newsWv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {//加载进度回调
                if (newProgress >= 90) {
                    rl_refresh_layout.setVisibility(View.GONE);
                    newsWv.setVisibility(View.VISIBLE);
                    findViewById(R.id.iv_share).setVisibility(View.VISIBLE);
                }
            }
        });

        findViewById(R.id.iv_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strUrl = url+"&isShare=true";
                ShareUtil.showShare(NewsDetailActivity.this, strUrl, title, summary);
            }
        });
    }
    /**
     * 数据的获取
     */
    private void initData() {
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        summary = getIntent().getStringExtra("summary");
        if (summary== null || "".equals(summary)){
            summary =title;
        }
        if (!"".equals(url) || url !=null ){
            titleTv1.setText(title);
            //设置支持js脚本，必须设置
            newsWv.getSettings().setJavaScriptEnabled(true);
            newsWv.addJavascriptInterface(NewsJavascriptInterface.with(this), NewsJavascriptInterface.NAME);
            newsWv.loadUrl(url);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        newsWv.onResume();//必须设置
    }

    @Override
    protected void onPause() {
        super.onPause();
        newsWv.onPause();//必须设置
    }

}
