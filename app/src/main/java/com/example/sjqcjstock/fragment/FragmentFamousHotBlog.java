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
 * 名家，名博的Fragment
 * Created by Administrator on 2017/05/02.
 */
public class FragmentFamousHotBlog extends Fragment implements ViewPager.OnPageChangeListener {

    // 两个个滑动页面
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mDatas;
    // 控件
    private TextView text_mj = null;
    private TextView text_mb = null;
    private LinearLayout ll_mj = null;
    private LinearLayout ll_mb = null;
    // 滑动条颜色
    private int select_color;
    private int unselect_color;
    /**
     * 当前视图宽度
     **/
    private Integer viewPagerW = 0;
    private int mScreen1_2;
    private ImageView img_line;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_famous_hot_blog, container, false);
        findView(view);
        initFragment();
        initLine(view);
        return view;
    }

    private void findView(View view) {
        // 获取颜色
        select_color = getResources().getColor(R.color.color_toptitle);
        unselect_color = getResources().getColor(R.color.color_000000);
        text_mj = (TextView) view.findViewById(R.id.text_mj);
        text_mb = (TextView) view.findViewById(R.id.text_mb);
        ll_mj = (LinearLayout) view.findViewById(R.id.linear_mj);
        ll_mb = (LinearLayout) view.findViewById(R.id.linear_mb);
        ll_mj.setOnClickListener(new MyOnClickListenser(0));
        ll_mb.setOnClickListener(new MyOnClickListenser(1));
        mViewPager = (ViewPager) view.findViewById(R.id.myViewpager);
        mViewPager.setOffscreenPageLimit(1);
        mDatas = new ArrayList<Fragment>();
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        FragmentFamous mjList = new FragmentFamous("1");
        FragmentFamous mbList = new FragmentFamous("0");
        mDatas.add(mjList);
        mDatas.add(mbList);
        mAdapter = new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
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
    private void initLine(View view) {
        img_line = (ImageView) view.findViewById(R.id.img_line);
        mScreen1_2 = ImageUtil.getScreenWidth(getActivity()) / 2;
        ViewGroup.LayoutParams lp = img_line.getLayoutParams();
        lp.width = mScreen1_2;
        img_line.setLayoutParams(lp);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        viewPagerW = mViewPager.getWidth() + mViewPager.getPageMargin();
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) img_line.getLayoutParams();
        // 关键算法
        lp.leftMargin = (int) ((int) (mScreen1_2 * position) + (((double) positionOffsetPixels / viewPagerW) * mScreen1_2));
        img_line.setLayoutParams(lp);
    }

    @Override
    public void onPageSelected(int position) {
        switch (mViewPager.getCurrentItem()) {
            case 0:
                text_mb.setTextColor(unselect_color);
                text_mj.setTextColor(select_color);
                break;
            case 1:
                text_mj.setTextColor(unselect_color);
                text_mb.setTextColor(select_color);
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
                    text_mb.setTextColor(unselect_color);
                    text_mj.setTextColor(select_color);
                    break;
                case R.id.linear_dp:
                    text_mj.setTextColor(unselect_color);
                    text_mb.setTextColor(select_color);
                    break;
            }
            mViewPager.setCurrentItem(index);
        }
    }
}