package com.example.sjqcjstock.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.fragment.FragmentTotalCelebrity;
import com.example.sjqcjstock.fragment.FragmentTotalELite;
import com.example.sjqcjstock.netutil.ImageUtil;

import java.util.ArrayList;
import java.util.List;

public class totalrankingnewActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    private LinearLayout goback1;
    // 两个个滑动页面
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mDatas;
    // 控件
    private TextView text_mr = null;
    private TextView text_jy = null;
    private LinearLayout ll_mr = null;
    private LinearLayout ll_jy = null;
    // 滑动条颜色
    private int select_color;
    private int unselect_color;
    /**
     * 当前视图宽度
     **/
    private Integer viewPagerW = 0;
    private int mScreen1_4;
    private ImageView img_line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.totalrankingnew_list);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
        initFragment();
        initLine();
    }

    private void initView() {
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        goback1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 获取颜色
        select_color = getResources().getColor(R.color.color_toptitle);
        unselect_color = getResources().getColor(R.color.color_000000);
        text_mr = (TextView) findViewById(R.id.text_mr);
        text_jy = (TextView) findViewById(R.id.text_jy);
        ll_mr = (LinearLayout) findViewById(R.id.linear_mr);
        ll_jy = (LinearLayout) findViewById(R.id.linear_jy);
        ll_mr.setOnClickListener(new MyOnClickListenser(0));
        ll_jy.setOnClickListener(new MyOnClickListenser(1));
        mViewPager = (ViewPager) findViewById(R.id.mViewpager);
        mViewPager.setOffscreenPageLimit(1);
        mDatas = new ArrayList<Fragment>();
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        FragmentTotalCelebrity mrList = new FragmentTotalCelebrity();
        FragmentTotalELite jyList = new FragmentTotalELite();
        mDatas.add(mrList);
        mDatas.add(jyList);
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
                text_jy.setTextColor(unselect_color);
                text_mr.setTextColor(select_color);
                break;
            case 1:
                text_mr.setTextColor(unselect_color);
                text_jy.setTextColor(select_color);
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
                    text_jy.setTextColor(unselect_color);
                    text_mr.setTextColor(select_color);
                    break;
                case R.id.linear_dp:
                    text_mr.setTextColor(unselect_color);
                    text_jy.setTextColor(select_color);
                    break;
            }
            mViewPager.setCurrentItem(index);
        }
    }
}