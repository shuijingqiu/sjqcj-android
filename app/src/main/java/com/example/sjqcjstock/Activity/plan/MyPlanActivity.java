package com.example.sjqcjstock.Activity.plan;

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

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.fragment.plan.FragmentMyPlan;
import com.example.sjqcjstock.fragment.plan.FragmentNewPlan;
import com.example.sjqcjstock.netutil.ImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的投资计划页面
 * Created by Administrator on 2017/4/14.
 */
public class MyPlanActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    // 两个个滑动页面
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mDatas;
    // 我发布的计划
    private FragmentMyPlan fragment2;
    // 控件
    private TextView text_purchase = null;
    private TextView text_release = null;
    private LinearLayout ll_purchase = null;
    private LinearLayout ll_release = null;
    // 滑动条颜色
    private int select_color;
    private int unselect_color;
    /**
     * 当前视图宽度
     **/
    private Integer viewPagerW = 0;
    private int mScreen;
    private ImageView img_line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plan);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        findView();
        initFragment();
        initLine();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2){
            // 发布计划成功跳转到最新发布并且刷新页面
            ll_release.performClick();
            // 调用刷新页面的方法
            fragment2.RefreshPage();
        }
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
        text_purchase = (TextView) findViewById(R.id.text_purchase);
        text_release = (TextView) findViewById(R.id.text_release);
        ll_purchase = (LinearLayout) findViewById(R.id.linear_purchase);
        ll_release = (LinearLayout) findViewById(R.id.linear_release);
        ll_purchase.setOnClickListener(new MyOnClickListenser(0));
        ll_release.setOnClickListener(new MyOnClickListenser(1));
        mViewPager = (ViewPager) findViewById(R.id.mViewpager);
        mViewPager.setOffscreenPageLimit(1);
        mDatas = new ArrayList<Fragment>();
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        FragmentNewPlan fragment1 = new FragmentNewPlan("2");
        fragment2 = new FragmentMyPlan();
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
        mScreen = ImageUtil.getScreenWidth(this) / 2;
        ViewGroup.LayoutParams lp = img_line.getLayoutParams();
        lp.width = mScreen;
        img_line.setLayoutParams(lp);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        viewPagerW = mViewPager.getWidth() + mViewPager.getPageMargin();
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) img_line.getLayoutParams();
        // 关键算法
        lp.leftMargin = (int) ((mScreen * position) + (((double) positionOffsetPixels / viewPagerW) * mScreen));
        img_line.setLayoutParams(lp);
    }

    @Override
    public void onPageSelected(int position) {
        switch (mViewPager.getCurrentItem()) {
            case 0:
                text_release.setTextColor(unselect_color);
                text_purchase.setTextColor(select_color);
                break;
            case 1:
                text_purchase.setTextColor(unselect_color);
                text_release.setTextColor(select_color);
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
                case R.id.linear_purchase:
                    text_release.setTextColor(unselect_color);
                    text_purchase.setTextColor(select_color);
                    break;
                case R.id.linear_popular:
                    text_purchase.setTextColor(unselect_color);
                    text_release.setTextColor(select_color);
                    break;
            }
            mViewPager.setCurrentItem(index);
        }
    }

    /**
     * 跳转到发布也面
     * @param view
     */
    public void ReleasePlan(View view){
        Intent intent = new Intent(this,ReleasePlanActivity.class);
        startActivityForResult(intent,1);
    }

}
