package com.example.sjqcjstock.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.netutil.ImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 选股牛人的Fragment
 * Created by Administrator on 2017/5/02.
 */
public class FragmentAnExpert extends Fragment implements ViewPager.OnPageChangeListener {
    // 两个个滑动页面
    private ViewPager viewPagers;
    private FragmentPagerAdapter adapter;
    private List<Fragment> datas;
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
    private Integer viewPager = 0;
    private int screen1_2;
    private ImageView imgLine;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_an_expert, container, false);
        findView(view);
        initFragment();
        initLine(view);
        return view;
    }

    private void findView(View view) {
        // 获取颜色
        select_color = getResources().getColor(R.color.color_toptitle);
        unselect_color = getResources().getColor(R.color.color_000000);
        text_mr = (TextView) view.findViewById(R.id.text_mr);
        text_jy = (TextView) view.findViewById(R.id.text_jy);
        ll_mr = (LinearLayout) view.findViewById(R.id.linear_mr);
        ll_jy = (LinearLayout) view.findViewById(R.id.linear_jy);
        ll_mr.setOnClickListener(new MyOnClickListenser(0));
        ll_jy.setOnClickListener(new MyOnClickListenser(1));
        viewPagers = (ViewPager) view.findViewById(R.id.mViewpager);
        viewPagers.setOffscreenPageLimit(1);
        datas = new ArrayList<Fragment>();
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        FragmentTotalCelebrity mrList = new FragmentTotalCelebrity("2");
        FragmentTotalELite jyList = new FragmentTotalELite("2");
        datas.add(mrList);
        datas.add(jyList);
        adapter = new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return datas == null ? 0 : datas.size();
            }

            @Override
            public Fragment getItem(int position) {
                return datas.get(position);
            }
        };
        viewPagers.setAdapter(adapter);
        viewPagers.addOnPageChangeListener(this);
        viewPagers.setCurrentItem(0);
    }

    /**
     * 初始化line
     */
    private void initLine(View view) {
        imgLine = (ImageView) view.findViewById(R.id.img_line);
        screen1_2 = ImageUtil.getScreenWidth(getActivity()) / 2;
        ViewGroup.LayoutParams lp = imgLine.getLayoutParams();
        lp.width = screen1_2;
        imgLine.setLayoutParams(lp);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        viewPager = viewPagers.getWidth() + viewPagers.getPageMargin();
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imgLine.getLayoutParams();
        // 关键算法
        lp.leftMargin = (int) ((int) (screen1_2 * position) + (((double) positionOffsetPixels / viewPager) * screen1_2));
        imgLine.setLayoutParams(lp);
    }

    @Override
    public void onPageSelected(int position) {
        switch (viewPagers.getCurrentItem()) {
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
            viewPagers.setCurrentItem(index);
        }
    }
//    // 两个个滑动页面
//    private ViewPager mViewPager;
//    private FragmentPagerAdapter mAdapter;
//    private List<Fragment> mDatas;
//    // 控件
//    private TextView text_mr = null;
//    private TextView text_jy = null;
//    private LinearLayout ll_mr = null;
//    private LinearLayout ll_jy = null;
//    // 滑动条颜色
//    private int select_color;
//    private int unselect_color;
//    /**
//     * 当前视图宽度
//     **/
//    private Integer viewPagerW = 0;
//    private int mScreen1_2;
//    private ImageView img_line;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_an_expert, container, false);
//        findView(view);
//        initFragment();
//        initLine(view);
//        return view;
//    }
//
//    private void findView(View view) {
//        // 获取颜色
//        select_color = getResources().getColor(R.color.color_toptitle);
//        unselect_color = getResources().getColor(R.color.color_000000);
//        text_mr = (TextView) view.findViewById(R.id.text_mr);
//        text_jy = (TextView) view.findViewById(R.id.text_jy);
//        ll_mr = (LinearLayout) view.findViewById(R.id.linear_mr);
//        ll_jy = (LinearLayout) view.findViewById(R.id.linear_jy);
//        ll_mr.setOnClickListener(new MyOnClickListenser(0));
//        ll_jy.setOnClickListener(new MyOnClickListenser(1));
//        mViewPager = (ViewPager) view.findViewById(R.id.mViewpager);
//        mViewPager.setOffscreenPageLimit(1);
//        mDatas = new ArrayList<Fragment>();
//    }
//
//    /**
//     * 初始化fragment
//     */
//    private void initFragment() {
////        FragmentTotalCelebrity mrList = new FragmentTotalCelebrity("2");
////        FragmentTotalELite jyList = new FragmentTotalELite("2");
////        mDatas.add(mrList);
////        mDatas.add(jyList);
//
//        FragmentFamous mjList = new FragmentFamous("1");
//        FragmentFamous mbList = new FragmentFamous("2");
//        mDatas.add(mjList);
//        mDatas.add(mbList);
//        mAdapter = new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
//            @Override
//            public int getCount() {
//                return mDatas == null ? 0 : mDatas.size();
//            }
//
//            @Override
//            public Fragment getItem(int position) {
//                return mDatas.get(position);
//            }
//        };
//        mViewPager.setAdapter(mAdapter);
//        mViewPager.addOnPageChangeListener(this);
//        mViewPager.setCurrentItem(0);
//    }
//
//    /**
//     * 初始化line
//     */
//    private void initLine(View view) {
//        img_line = (ImageView) view.findViewById(R.id.img_line);
//        mScreen1_2 = ImageUtil.getScreenWidth(getActivity()) / 2;
//        ViewGroup.LayoutParams lp = img_line.getLayoutParams();
//        lp.width = mScreen1_2;
//        img_line.setLayoutParams(lp);
//    }
//
//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        viewPagerW = mViewPager.getWidth() + mViewPager.getPageMargin();
//        LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) img_line.getLayoutParams();
//        // 关键算法
//        lp.leftMargin = (int) ((int) (mScreen1_2 * position) + (((double) positionOffsetPixels / viewPagerW) * mScreen1_2));
//        img_line.setLayoutParams(lp);
//    }
//
//    @Override
//    public void onPageSelected(int position) {
//        switch (mViewPager.getCurrentItem()) {
//            case 0:
//                text_jy.setTextColor(unselect_color);
//                text_mr.setTextColor(select_color);
//                break;
//            case 1:
//                text_mr.setTextColor(unselect_color);
//                text_jy.setTextColor(select_color);
//                break;
//        }
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int state) {
//
//    }
//
//    /**
//     * 点击文字进行切换
//     *
//     * @author wuxl
//     */
//    public class MyOnClickListenser implements View.OnClickListener {
//
//        private int index = 0;
//
//        public MyOnClickListenser(int i) {
//            index = i;
//        }
//
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.linear_jd:
//                    text_jy.setTextColor(unselect_color);
//                    text_mr.setTextColor(select_color);
//                    break;
//                case R.id.linear_dp:
//                    text_mr.setTextColor(unselect_color);
//                    text_jy.setTextColor(select_color);
//                    break;
//            }
//            mViewPager.setCurrentItem(index);
//        }
//    }
}