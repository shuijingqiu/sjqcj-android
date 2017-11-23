package com.example.sjqcjstock.Activity.qa;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.fragment.qa.FragmentAllQuiz;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Md5Util;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的问答页面
 * Created by Administrator on 2017/3/3.
 */
public class MyQuestionAnswerActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    private LinearLayout goback1;
    // 两个个滑动页面
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mDatas;
    // 控件
    private TextView text_ww = null;
    private TextView text_wd = null;
    private LinearLayout ll_ww = null;
    private LinearLayout ll_wd = null;
    // 滑动条颜色
    private int select_color;
    private int unselect_color;
    /**
     * 当前视图宽度
     **/
    private Integer viewPagerW = 0;
    private int mScreen1_4;
    private ImageView img_line;
    private EditText sjbCountEt;
    // 调用接口返回的数据
    private String resstr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_question_answer);
        // 初始默认不开启软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        findView();
        initFragment();
        initLine();
        getData();
    }

    /**
     * 数据的绑定
     */
    private void findView() {
        ((TextView)findViewById(R.id.username2)).setText(Constants.userEntity.getUname());
        ((TextView)findViewById(R.id.userintro1)).setText(Constants.userEntity.getIntro());

        ImageView headimg2 = (ImageView) findViewById(R.id.headimg2);
        ImageLoader.getInstance().displayImage(Md5Util.getuidstrMd5(Md5Util
                        .getMd5(Constants.getStaticmyuidstr())),
                headimg2, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        goback1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sjbCountEt = (EditText) findViewById(R.id.sjb_count_et);
        // 获取颜色
        select_color = getResources().getColor(R.color.color_toptitle);
        unselect_color = getResources().getColor(R.color.color_000000);
        text_ww = (TextView) findViewById(R.id.text_ww);
        text_wd = (TextView) findViewById(R.id.text_wd);
        ll_ww = (LinearLayout) findViewById(R.id.linear_ww);
        ll_wd = (LinearLayout) findViewById(R.id.linear_wd);
        ll_ww.setOnClickListener(new MyOnClickListenser(0));
        ll_wd.setOnClickListener(new MyOnClickListenser(1));
        mViewPager = (ViewPager) findViewById(R.id.mViewpager);
        mViewPager.setOffscreenPageLimit(1);
        mDatas = new ArrayList<Fragment>();
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        FragmentAllQuiz wdList = new FragmentAllQuiz("3", Constants.staticmyuidstr);
        FragmentAllQuiz twList = new FragmentAllQuiz("5", Constants.staticmyuidstr);
        mDatas.add(wdList);
        mDatas.add(twList);
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mDatas == null ? 0 : mDatas.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mDatas.get(position);
            }
        };
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setCurrentItem(0);
    }

    /**
     * 调用接口获取数据
     */
    private void getData(){
        // 获得设置的打赏水晶币个数
        new Thread(new Runnable() {
            @Override
            public void run() {
                resstr = HttpUtil.restHttpGet(Constants.moUrl + "/ask/index/getPrice&uid="+ Constants.staticmyuidstr);
                handler.sendEmptyMessage(0);
            }
        }).start();
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
                        JSONObject jsonObject = new JSONObject(resstr);
                        if ("failed".equals(jsonObject.getString("status"))){
                            return;
                        }
                        sjbCountEt.setText(jsonObject.getString("data"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(resstr);
                        Toast.makeText(MyQuestionAnswerActivity.this,jsonObject.getString("data"),Toast.LENGTH_SHORT).show();
                        if ("failed".equals(jsonObject.getString("status"))){
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // 强制关闭软键盘
                    InputMethodManager imm = (InputMethodManager) sjbCountEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    break;
            }
        }
    };

    /**
     * 初始化line
     */
    private void initLine() {
        img_line = (ImageView) findViewById(R.id.img_line);
        mScreen1_4 = ImageUtil.getScreenWidth(this) / 2;
        ViewGroup.LayoutParams lp = img_line.getLayoutParams();
        lp.width = mScreen1_4;
        img_line.setLayoutParams(lp);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        viewPagerW = mViewPager.getWidth() + mViewPager.getPageMargin();
        LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) img_line.getLayoutParams();
        // 关键算法
        lp.leftMargin = (int) ((int) (mScreen1_4 * position) + (((double) positionOffsetPixels / viewPagerW) * mScreen1_4));
        img_line.setLayoutParams(lp);
    }

    @Override
    public void onPageSelected(int position) {
        switch (mViewPager.getCurrentItem()) {
            case 0: 
                text_wd.setTextColor(unselect_color);
                text_ww.setTextColor(select_color);
                break;
            case 1:
                text_ww.setTextColor(unselect_color);
                text_wd.setTextColor(select_color);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 点击文字进行切换
     *
     * @author wuxl
     */
    public class MyOnClickListenser implements View.OnClickListener {

        private int index = 0;
        public MyOnClickListenser(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.linear_jd:
                    text_wd.setTextColor(unselect_color);
                    text_ww.setTextColor(select_color);
                    break;
                case R.id.linear_dp:
                    text_ww.setTextColor(unselect_color);
                    text_wd.setTextColor(select_color);
                    break;
            }
            mViewPager.setCurrentItem(index);
        }
    }

    /**
     * 修改水晶币数量的事件
     */
    public void UpdateClick(View view){
            String sjbCount = sjbCountEt.getText().toString().trim();
            if (!"".equals(sjbCount) && !"0".equals(sjbCount)){
                if (Integer.valueOf(sjbCount) < 100 || Integer.valueOf(sjbCount) > 1000){
                    Toast.makeText(this,"请输入正确的水晶币个数",Toast.LENGTH_SHORT).show();
                    sjbCountEt.setFocusable(true);
                    return;
                }
            }else{
                sjbCount = "0";
            }
        final String count = sjbCount;
            // 修改设置打赏的水晶币个数
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List dataList = new ArrayList();
                    // 用户ID
                    dataList.add(new BasicNameValuePair("uid", Constants.staticmyuidstr));
                    dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                    dataList.add(new BasicNameValuePair("price", count));
                    // 调用接口获取直播列表
                    resstr = HttpUtil.restHttpPost(Constants.moUrl + "/ask/index/setPrice",dataList);
                    handler.sendEmptyMessage(1);
                }
            }).start();
    }
}
