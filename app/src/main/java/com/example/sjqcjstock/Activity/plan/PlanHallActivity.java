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

import com.example.sjqcjstock.Activity.user.loginActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.fragment.plan.FragmentAverageEarnings;
import com.example.sjqcjstock.fragment.plan.FragmentNewPlan;
import com.example.sjqcjstock.fragment.plan.FragmentStandard;
import com.example.sjqcjstock.fragment.plan.FragmentTotalEarnings;
import com.example.sjqcjstock.netutil.ImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 投资计划大厅页面
 * Created by Administrator on 2017/4/14.
 */
public class PlanHallActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    // 两个个滑动页面
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mDatas;
    // 最新计划
    private FragmentNewPlan fragment1;
    // 控件
    private TextView text_new = null;
    private TextView text_selected = null;
    private TextView text_profit = null;
    private TextView text_standard = null;
    private TextView text_avg = null;
    private LinearLayout ll_new = null;
    private LinearLayout ll_selected = null;
    private LinearLayout ll_profit = null;
    private LinearLayout ll_standard = null;
    private LinearLayout ll_avg = null;
    // 滑动条颜色
    private int select_color;
    private int unselect_color;
    /**
     * 当前视图宽度
     **/
    private Integer viewPagerW = 0;
    private int mScreen;
    private ImageView img_line;
    // 1 显示最新 2 显示精选
    private int type = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_hall);
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
            ll_new.performClick();
            // 调用刷新页面的方法
            fragment1.RefreshPage();
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
        text_new = (TextView) findViewById(R.id.text_new);
        text_selected = (TextView) findViewById(R.id.text_selected);
        text_profit = (TextView) findViewById(R.id.text_profit);
        text_avg = (TextView) findViewById(R.id.text_avg);
        text_standard = (TextView) findViewById(R.id.text_standard);
        ll_new = (LinearLayout) findViewById(R.id.linear_new);
        ll_selected = (LinearLayout) findViewById(R.id.linear_selected);
        ll_profit = (LinearLayout) findViewById(R.id.linear_profit);
        ll_standard = (LinearLayout) findViewById(R.id.linear_standard);
        ll_avg = (LinearLayout) findViewById(R.id.linear_avg);
        ll_new.setOnClickListener(new MyOnClickListenser(0));
        ll_selected.setOnClickListener(new MyOnClickListenser(1));
        ll_profit.setOnClickListener(new MyOnClickListenser(2));
        ll_standard.setOnClickListener(new MyOnClickListenser(3));
        ll_avg.setOnClickListener(new MyOnClickListenser(4));
        mViewPager = (ViewPager) findViewById(R.id.mViewpager);
        mViewPager.setOffscreenPageLimit(5);
        mDatas = new ArrayList<Fragment>();
        type = getIntent().getIntExtra("type",1);
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        fragment1 = new FragmentNewPlan("1");
        // 计划精选
        FragmentNewPlan fragment2 = new FragmentNewPlan("3");
        FragmentTotalEarnings fragment3 = new FragmentTotalEarnings();
        FragmentStandard fragment4 = new FragmentStandard();
        FragmentAverageEarnings fragment5= new FragmentAverageEarnings();
        mDatas.add(fragment1);
        mDatas.add(fragment2);
        mDatas.add(fragment3);
        mDatas.add(fragment4);
        mDatas.add(fragment5);
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
        if(type==2){
            text_new.setTextColor(unselect_color);
            text_selected.setTextColor(select_color);
            text_standard.setTextColor(unselect_color);
            text_avg.setTextColor(unselect_color);
            text_profit.setTextColor(unselect_color);
            mViewPager.setCurrentItem(1);
        }else{
            mViewPager.setCurrentItem(0);
        }
    }

    /**
     * 初始化line
     */
    private void initLine() {
        img_line = (ImageView) findViewById(R.id.img_line);
        mScreen = ImageUtil.getScreenWidth(this) / 5;
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
                text_profit.setTextColor(unselect_color);
                text_standard.setTextColor(unselect_color);
                text_avg.setTextColor(unselect_color);
                text_new.setTextColor(select_color);
                text_selected.setTextColor(unselect_color);
                break;
            case 1:
                text_new.setTextColor(unselect_color);
                text_selected.setTextColor(select_color);
                text_profit.setTextColor(unselect_color);
                text_standard.setTextColor(unselect_color);
                text_avg.setTextColor(unselect_color);
                break;
            case 2:
                text_new.setTextColor(unselect_color);
                text_selected.setTextColor(unselect_color);
                text_profit.setTextColor(select_color);
                text_standard.setTextColor(unselect_color);
                text_avg.setTextColor(unselect_color);
                break;
            case 3:
                text_new.setTextColor(unselect_color);
                text_selected.setTextColor(unselect_color);
                text_profit.setTextColor(unselect_color);
                text_standard.setTextColor(select_color);
                text_avg.setTextColor(unselect_color);
                break;
            case 4:
                text_new.setTextColor(unselect_color);
                text_selected.setTextColor(unselect_color);
                text_profit.setTextColor(unselect_color);
                text_standard.setTextColor(unselect_color);
                text_avg.setTextColor(select_color);
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
                case R.id.linear_new:
                    text_profit.setTextColor(unselect_color);
                    text_standard.setTextColor(unselect_color);
                    text_selected.setTextColor(unselect_color);
                    text_avg.setTextColor(unselect_color);
                    text_new.setTextColor(select_color);
                    break;
                case R.id.linear_selected:
                    text_new.setTextColor(unselect_color);
                    text_selected.setTextColor(select_color);
                    text_standard.setTextColor(unselect_color);
                    text_avg.setTextColor(unselect_color);
                    text_profit.setTextColor(unselect_color);
                    break;
                case R.id.linear_popular:
                    text_new.setTextColor(unselect_color);
                    text_selected.setTextColor(unselect_color);
                    text_standard.setTextColor(unselect_color);
                    text_avg.setTextColor(unselect_color);
                    text_profit.setTextColor(select_color);
                    break;
                case R.id.linear_standard:
                    text_new.setTextColor(unselect_color);
                    text_selected.setTextColor(unselect_color);
                    text_standard.setTextColor(select_color);
                    text_avg.setTextColor(unselect_color);
                    text_profit.setTextColor(unselect_color);
                    break;
                case R.id.linear_avg:
                    text_new.setTextColor(unselect_color);
                    text_selected.setTextColor(unselect_color);
                    text_standard.setTextColor(unselect_color);
                    text_avg.setTextColor(select_color);
                    text_profit.setTextColor(unselect_color);
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
        // menghuan 不用登陆也可以用
        // 只用用户登陆才能发布计划
        if (!Constants.isLogin){
            Intent intent = new Intent(this, loginActivity.class);
            startActivity(intent);
            return;
        }
        Intent intent = new Intent(this,ReleasePlanActivity.class);
        startActivityForResult(intent,1);
    }
}
