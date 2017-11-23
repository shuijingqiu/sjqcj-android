package com.example.sjqcjstock.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.javaScriptfaces.NewsJavascriptInterface;
import com.giiso.sdk.GiisoServiceListener;
import com.giiso.sdk.openapi.GiisoApi;
import com.giiso.sdk.openapi.GiisoServiceConfig;

/**
 * 财经资讯页面
 */
public class FragmentInform extends Fragment implements GiisoServiceListener {
    // 显示页面的WebView
    private WebView inforWv;
    // 网络请求提示
    private RelativeLayout rl_refresh_layout;


    public FragmentInform() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_informnews, null);
        rl_refresh_layout = (RelativeLayout) view.findViewById(R.id.rl_refresh_layout);
        inforWv = (WebView) view.findViewById(R.id.infor_wv);
        //设置支持js脚本，必须设置
        inforWv.getSettings().setJavaScriptEnabled(true);
        //注入js调用对象，注入的java对象必须定义了openNewsDetailPage(String //newsInfo) 方法，并加上@JavascriptInterface注解，第二个参数也必须传//入”NewsBridge”
        //必须设置，h5的新闻列表中当点击单篇新闻跳转到新闻详情页时，需要js端调用java代码进行跳转、传参
        inforWv.addJavascriptInterface(NewsJavascriptInterface.with(getActivity()), NewsJavascriptInterface.NAME);
        inforWv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {//加载进度回调
                if (newProgress >= 90) {
                    rl_refresh_layout.setVisibility(View.GONE);
                    inforWv.setVisibility(View.VISIBLE);
                }
            }
        });
        onStataInfor();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
//            geneItems();
        }
    }

    /**
     * 初始化咨询sdk
     */
    private void onStataInfor() {
        GiisoServiceConfig config = GiisoServiceConfig.newBuilder()
                .setAppUid("C0006110001")//app端根据业务需要进行设置，可以为空
                .setGiisoServiceListener(this)
                .setLocation("")
                .build();
        GiisoApi.init(getActivity(), config);
    }

    /**
     * 咨询初始化成功调用
     *
     * @param s  H5新闻列表页的url
     * @param s1 我方一个识别用户的识别码
     * @param s2 我方初始化成功返回的token值
     */
    @Override
    public void onSuccess(String s, String s1, String s2) {
        inforWv.loadUrl(s);
        Log.e("mh1238", s + "  code:" + s1);
    }

    /**
     * 咨询初始化失败调用
     *
     * @param s 失败原因
     * @param i 失败码 0:未知错误 1:app端初始化传参异常 2:服务器错误 3:网络异常 4:服务器校验不通过
     */
    @Override
    public void onError(String s, int i) {
        Log.e("mh1238", s + "  code:" + i);
    }

}
