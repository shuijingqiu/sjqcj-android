package com.example.sjqcjstock.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.fragment.FragmentAnExpert;
import com.example.sjqcjstock.fragment.FragmentFamousHotBlog;
import com.example.sjqcjstock.fragment.FragmentMustEarnCattle;
import com.example.sjqcjstock.fragment.plan.FragmentStandard;
import com.example.sjqcjstock.view.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * 各种牛人榜单
 * Created by Administrator on 2017/4/28.
 */
public class CattleListActivity extends FragmentActivity {

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;
    private List<Fragment> mDatas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cattle_lise);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        findView();
    }

    /**
     * 页面控件绑定
     */
    private void findView() {
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        mDatas = new ArrayList<Fragment>();

        // 名家，名博
        FragmentFamousHotBlog fragment1 = new FragmentFamousHotBlog();
//        FragmentMustEarnCattle fragment1 = new FragmentMustEarnCattle(3);
        // 必赚牛人(炒股比赛的选股牛人)
        FragmentMustEarnCattle fragment2 = new FragmentMustEarnCattle(3);
        // 总收益榜（炒股比赛的总收益榜）
        FragmentMustEarnCattle fragment3 = new FragmentMustEarnCattle(2);
        // 选股牛人（选股比赛的常胜牛人）
        FragmentAnExpert fragment4 = new FragmentAnExpert();
        // 达标率
        FragmentStandard fragment5 = new FragmentStandard();
        mDatas.add(fragment1);
        mDatas.add(fragment2);
        mDatas.add(fragment3);
        mDatas.add(fragment4);
        mDatas.add(fragment5);

        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        pager.setOffscreenPageLimit(5);
        tabs.setViewPager(pager);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { "名家名博", "常胜牛人", "总收益榜", "选股牛人", "计划牛人" };

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            return mDatas.get(position);
        }

    }

}
