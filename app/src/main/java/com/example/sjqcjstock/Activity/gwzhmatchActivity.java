package com.example.sjqcjstock.Activity;

import android.content.Intent;
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
import com.example.sjqcjstock.fragment.FragmentGwzbsList;
import com.example.sjqcjstock.fragment.FragmentReport;
import com.example.sjqcjstock.netutil.ImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 大师联赛（股王争霸）
 */
public class gwzhmatchActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    // 两个个滑动页面
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mDatas;
    // 控件
    private TextView text_ls = null;
    private TextView text_bd = null;
    private TextView text_sk = null;
    private LinearLayout ll_ls = null;
    private LinearLayout ll_bd = null;
    private LinearLayout ll_sk = null;
    // 滑动条颜色
    private int select_color;
    private int unselect_color;
    /**
     * 当前视图宽度
     **/
    private Integer viewPagerW = 0;
    private int mScreen1_3;
    private ImageView img_line;
    private FragmentGwzbsList bdList;
    private FragmentGwzbsList bdList1;
    private FragmentReport skLIst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gwzbmatch_list);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
        initFragment();
        initLine();
//        getData();
    }

    private void initView() {
        findViewById(R.id.goback1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 获取颜色
        select_color = getResources().getColor(R.color.color_toptitle);
        unselect_color = getResources().getColor(R.color.color_000000);
        text_ls = (TextView) findViewById(R.id.text_ls);
        text_bd = (TextView) findViewById(R.id.text_bd);
        text_sk = (TextView) findViewById(R.id.text_sk);
        ll_bd = (LinearLayout) findViewById(R.id.linear_bd);
        ll_ls = (LinearLayout) findViewById(R.id.linear_ls);
        ll_sk = (LinearLayout) findViewById(R.id.linear_sk);
        ll_ls.setOnClickListener(new MyOnClickListenser(0));
        ll_bd.setOnClickListener(new MyOnClickListenser(1));
        ll_sk.setOnClickListener(new MyOnClickListenser(2));
        mViewPager = (ViewPager) findViewById(R.id.mViewpager);
        mViewPager.setOffscreenPageLimit(3);
        mDatas = new ArrayList<Fragment>();
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        bdList = new FragmentGwzbsList("0");
        bdList1 = new FragmentGwzbsList("1");
        skLIst = new FragmentReport();
        mDatas.add(bdList);
        mDatas.add(bdList1);
        mDatas.add(skLIst);
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
        mScreen1_3 = ImageUtil.getScreenWidth(this) / 3;
        ViewGroup.LayoutParams lp = img_line.getLayoutParams();
        lp.width = mScreen1_3;
        img_line.setLayoutParams(lp);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        viewPagerW = mViewPager.getWidth() + mViewPager.getPageMargin();
        LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) img_line.getLayoutParams();
        // 关键算法
        lp.leftMargin = (int) ((mScreen1_3 * position) + (((double) positionOffsetPixels / viewPagerW) * mScreen1_3));
        img_line.setLayoutParams(lp);
    }

    @Override
    public void onPageSelected(int position) {
        switch (mViewPager.getCurrentItem()) {
            case 0:
                text_sk.setTextColor(unselect_color);
                text_ls.setTextColor(select_color);
                text_bd.setTextColor(unselect_color);
                break;
            case 1:
                text_bd.setTextColor(select_color);
                text_ls.setTextColor(unselect_color);
                text_sk.setTextColor(unselect_color);
                break;
            case 2:
                text_bd.setTextColor(unselect_color);
                text_ls.setTextColor(unselect_color);
                text_sk.setTextColor(select_color);
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
                case R.id.linear_ls:
                    text_sk.setTextColor(unselect_color);
                    text_bd.setTextColor(unselect_color);
                    text_ls.setTextColor(select_color);
                    break;
                case R.id.linear_bd:
                    text_sk.setTextColor(unselect_color);
                    text_ls.setTextColor(unselect_color);
                    text_bd.setTextColor(select_color);
                    break;
                case R.id.linear_sk:
                    text_bd.setTextColor(unselect_color);
                    text_ls.setTextColor(unselect_color);
                    text_sk.setTextColor(select_color);
                    break;
            }
            mViewPager.setCurrentItem(index);
        }
    }

    /**
     * 大师联赛及订阅规则
     *
     * @param view
     */
    public void RuleClick(View view) {
        // 大师联赛及订阅规则
        Intent intent = new Intent(this, AgreementActivity.class);
        intent.putExtra("type", "14");
        startActivity(intent);
    }
}
