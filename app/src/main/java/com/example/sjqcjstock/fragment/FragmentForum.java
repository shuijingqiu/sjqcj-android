package com.example.sjqcjstock.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.SearchActivity;
import com.example.sjqcjstock.Activity.commentshortweiboActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.ImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 股吧的显示页面
 */
public class FragmentForum extends Fragment implements ViewPager.OnPageChangeListener {
    // Bunld获得发长短微博标识
    private static final int REQUEST_CODE_shortweibofinish = 1;
    // 写微博
    private ImageView addforum1;
    // 检索
    private ImageView iv_search;
    // 两个个滑动页面
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mDatas;
    // 控件
    private TextView text_qb = null;
    private TextView text_ds = null;
    private LinearLayout ll_qb = null;
    private LinearLayout ll_ds = null;
    // 滑动条颜色
    private int select_color;
    private int unselect_color;
    /**
     * 当前视图宽度
     **/
    private Integer viewPagerW = 0;
    private int mScreen1_4;
    private ImageView img_line;
    private FragmentAllWeibo allWeibo;
    private FragmentRewardWeibo rewardWeibo;
    // 判断哪个显示true为全部文章显示
    private boolean isShow = true;
    // 是否是第一次打开这个页面
    private boolean isFisrt = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, null);
        initView2(view);
        initFragment();
        initLine(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //是否刷新股吧列表。0为刷新，1为不刷新
        if (!isFisrt) {
            if ("0".equals(Constants.isreferforumlist)) {
                // 打赏和全部都刷新
                if (rewardWeibo != null) {
                    rewardWeibo.onHiddenChanged(false);
                }
                if (allWeibo != null) {
                    allWeibo.onHiddenChanged(false);
                }
                Constants.isreferforumlist = "1";
            }
        }
        isFisrt = false;
    }

    // 一般控件处理
    private void initView2(View view) {
        iv_search = (ImageView) view.findViewById(R.id.iv_search);
        addforum1 = (ImageView) view.findViewById(R.id.addforum1);
        iv_search.setOnClickListener(new iv_search_listener());
        addforum1.setOnClickListener(new addforum1_listener());
        // 获取颜色
        select_color = getResources().getColor(R.color.color_toptitle);
        unselect_color = getResources().getColor(R.color.color_000000);
        text_qb = (TextView) view.findViewById(R.id.text_qb);
        text_ds = (TextView) view.findViewById(R.id.text_ds);
        ll_qb = (LinearLayout) view.findViewById(R.id.linear_qb);
        ll_ds = (LinearLayout) view.findViewById(R.id.linear_ds);
        ll_qb.setOnClickListener(new MyOnClickListenser(0));
        ll_ds.setOnClickListener(new MyOnClickListenser(1));
        mViewPager = (ViewPager) view.findViewById(R.id.mViewpager);
        mViewPager.setOffscreenPageLimit(1);
        mDatas = new ArrayList<Fragment>();
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        allWeibo = new FragmentAllWeibo();
        rewardWeibo = new FragmentRewardWeibo();
        mDatas.add(allWeibo);
        mDatas.add(rewardWeibo);
        mAdapter = new FragmentPagerAdapter(getFragmentManager()) {
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
        mScreen1_4 = ImageUtil.getScreenWidth(getActivity()) / 2;
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
                text_ds.setTextColor(unselect_color);
                text_qb.setTextColor(select_color);
                isShow = true;
                break;
            case 1:
                text_qb.setTextColor(unselect_color);
                text_ds.setTextColor(select_color);
                isShow = false;
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
                    text_ds.setTextColor(unselect_color);
                    text_qb.setTextColor(select_color);
                    isShow = true;
                    break;
                case R.id.linear_dp:
                    text_qb.setTextColor(unselect_color);
                    text_ds.setTextColor(select_color);
                    isShow = false;
                    break;
            }
            mViewPager.setCurrentItem(index);
        }
    }

    class iv_search_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivityForResult(intent, REQUEST_CODE_shortweibofinish);
        }
    }

    class addforum1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // 4表示禁言用户不能发帖
            if (Constants.userType.equals("4")) {
                return;
            }
            // 跳转到写博文的页面
            Intent intent = new Intent(getActivity(), commentshortweiboActivity.class);
            startActivityForResult(intent, REQUEST_CODE_shortweibofinish);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (isShow) {
                if (allWeibo != null) {
                    allWeibo.onHiddenChanged(hidden);
                }
            } else {
                if (rewardWeibo != null) {
                    rewardWeibo.onHiddenChanged(hidden);
                }
            }
        } else {
            if (rewardWeibo != null) {
                rewardWeibo.onHiddenChanged(hidden);
            }
            if (allWeibo != null) {
                allWeibo.onHiddenChanged(hidden);
            }
        }
    }
}
