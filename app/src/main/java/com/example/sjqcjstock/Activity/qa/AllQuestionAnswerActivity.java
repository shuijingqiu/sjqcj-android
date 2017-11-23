package com.example.sjqcjstock.Activity.qa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.AgreementActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.fragment.qa.FragmentAllInterlocution;
import com.example.sjqcjstock.fragment.qa.FragmentPopularAnswer;
import com.example.sjqcjstock.netutil.ImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 问答大厅
 * Created by Administrator on 2017/3/3.
 */
public class AllQuestionAnswerActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    // 两个个滑动页面
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mDatas;
    // 控件
    private TextView text_all = null;
    private TextView text_popular = null;
    private LinearLayout ll_all = null;
    private LinearLayout ll_popular = null;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_answer_list);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        findView();
        initFragment();
        initLine();
    }

    /**
     * 控件的加载和初始化
     */
    private void findView() {
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 获取颜色
        select_color = getResources().getColor(R.color.color_toptitle);
        unselect_color = getResources().getColor(R.color.color_000000);
        text_all = (TextView) findViewById(R.id.text_all);
        text_popular = (TextView) findViewById(R.id.text_popular);
        ll_all = (LinearLayout) findViewById(R.id.linear_all);
        ll_popular = (LinearLayout) findViewById(R.id.linear_popular);
        ll_all.setOnClickListener(new MyOnClickListenser(0));
        ll_popular.setOnClickListener(new MyOnClickListenser(1));
        mViewPager = (ViewPager) findViewById(R.id.mViewpager);
        mViewPager.setOffscreenPageLimit(1);
        mDatas = new ArrayList<Fragment>();
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        FragmentAllInterlocution fragment1 = new FragmentAllInterlocution();
        FragmentPopularAnswer fragment2 = new FragmentPopularAnswer();
        mDatas.add(fragment1);
        mDatas.add(fragment2);
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
        mScreen1_2 = ImageUtil.getScreenWidth(this) / 2;
        ViewGroup.LayoutParams lp = img_line.getLayoutParams();
        lp.width = mScreen1_2;
        img_line.setLayoutParams(lp);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        viewPagerW = mViewPager.getWidth() + mViewPager.getPageMargin();
        LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) img_line.getLayoutParams();
        // 关键算法
        lp.leftMargin = (int) ((int) (mScreen1_2 * position) + (((double) positionOffsetPixels / viewPagerW) * mScreen1_2));
        img_line.setLayoutParams(lp);
    }

    @Override
    public void onPageSelected(int position) {
        switch (mViewPager.getCurrentItem()) {
            case 0:
                text_popular.setTextColor(unselect_color);
                text_all.setTextColor(select_color);
                break;
            case 1:
                text_all.setTextColor(unselect_color);
                text_popular.setTextColor(select_color);
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
                case R.id.linear_all:
                    text_popular.setTextColor(unselect_color);
                    text_all.setTextColor(select_color);
                    break;
                case R.id.linear_popular:
                    text_all.setTextColor(unselect_color);
                    text_popular.setTextColor(select_color);
                    break;
            }
            mViewPager.setCurrentItem(index);
        }
    }

    /**
     * 问答规则
     *
     * @param view
     */
    public void RuleClick(View view) {
        // 问答规则
        Intent intent = new Intent(this, AgreementActivity.class);
        intent.putExtra("type", "25");
        startActivity(intent);
    }
}
