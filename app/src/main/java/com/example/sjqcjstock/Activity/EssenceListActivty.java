package com.example.sjqcjstock.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.fragment.FragmentEssenceFs;
import com.example.sjqcjstock.fragment.FragmentEssenceJd;
import com.example.sjqcjstock.fragment.FragmentEssenceJh;
import com.example.sjqcjstock.netutil.ImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 精华集合d主体页面
 * Created by Administrator on 2016/5/31.
 */
public class EssenceListActivty extends FragmentActivity implements ViewPager.OnPageChangeListener {

    // 七个滑动页面
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mDatas;
    // 控件
    private TextView text_jd = null;
    private TextView text_dp = null;
    private TextView text_gg = null;
    private TextView text_mj = null;
    private TextView text_nc = null;
    private TextView text_ld = null;
    private TextView text_fs = null;
    private LinearLayout ll_jd = null;
    private LinearLayout ll_dp = null;
    private LinearLayout ll_gg = null;
    private LinearLayout ll_mj = null;
    private LinearLayout ll_nc = null;
    private LinearLayout ll_ld = null;
    private LinearLayout ll_fs = null;
    private ImageView img_line;

    // 滑动条颜色
    private int select_color;
    private int unselect_color;
    private int mScreen1_4;
    /**
     * 当前视图宽度
     **/
    private Integer viewPagerW = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_essence);
        //将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initLine();
        initView();
        initFragment();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        // 获取颜色
        select_color = getResources().getColor(R.color.color_toptitle);
        unselect_color = getResources().getColor(R.color.color_000000);

        text_jd = (TextView) findViewById(R.id.text_jd);
        text_dp = (TextView) findViewById(R.id.text_dp);
        text_gg = (TextView) findViewById(R.id.text_gg);
        text_mj = (TextView) findViewById(R.id.text_mj);
        text_nc = (TextView) findViewById(R.id.text_nc);
        text_ld = (TextView) findViewById(R.id.text_ld);
        text_fs = (TextView) findViewById(R.id.text_fs);
        ll_jd = (LinearLayout) findViewById(R.id.linear_jd);
        ll_dp = (LinearLayout) findViewById(R.id.linear_dp);
        ll_gg = (LinearLayout) findViewById(R.id.linear_gg);
        ll_mj = (LinearLayout) findViewById(R.id.linear_mj);
        ll_nc = (LinearLayout) findViewById(R.id.linear_nc);
        ll_ld = (LinearLayout) findViewById(R.id.linear_ld);
        ll_fs = (LinearLayout) findViewById(R.id.linear_fs);

        ll_jd.setOnClickListener(new MyOnClickListenser(0));
        ll_dp.setOnClickListener(new MyOnClickListenser(1));
        ll_gg.setOnClickListener(new MyOnClickListenser(2));
        ll_mj.setOnClickListener(new MyOnClickListenser(3));
        ll_nc.setOnClickListener(new MyOnClickListenser(4));
        ll_ld.setOnClickListener(new MyOnClickListenser(5));
        ll_fs.setOnClickListener(new MyOnClickListenser(6));
        mViewPager = (ViewPager) findViewById(R.id.mViewpager);
        mViewPager.setOffscreenPageLimit(7);
        mDatas = new ArrayList<Fragment>();

        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // 返回
                finish();
            }
        });
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        FragmentEssenceJd mJD = new FragmentEssenceJd();
        FragmentEssenceJh mDp = new FragmentEssenceJh("3");
        FragmentEssenceJh mGg = new FragmentEssenceJh("4");
        FragmentEssenceJh mMj = new FragmentEssenceJh("6");
        FragmentEssenceJh mNC = new FragmentEssenceJh("8");
        FragmentEssenceJh mLd = new FragmentEssenceJh("5");
        FragmentEssenceFs mFS = new FragmentEssenceFs();
        mDatas.add(mJD);
        mDatas.add(mDp);
        mDatas.add(mGg);
        mDatas.add(mMj);
        mDatas.add(mNC);
        mDatas.add(mLd);
        mDatas.add(mFS);
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
        mScreen1_4 = ImageUtil.getScreenWidth(this) / 7;
        LayoutParams lp = img_line.getLayoutParams();
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
        resetTextColor();
        switch (mViewPager.getCurrentItem()) {
            case 0:
                text_jd.setTextColor(select_color);
                break;
            case 1:
                text_dp.setTextColor(select_color);
                break;
            case 2:
                text_gg.setTextColor(select_color);
                break;
            case 3:
                text_mj.setTextColor(select_color);
                break;
            case 4:
                text_nc.setTextColor(select_color);
                break;
            case 5:
                text_ld.setTextColor(select_color);
                break;
            case 6:
                text_fs.setTextColor(select_color);
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
            resetTextColor();
            switch (v.getId()) {
                case R.id.linear_jd:
                    text_jd.setTextColor(select_color);
                    break;
                case R.id.linear_dp:
                    text_dp.setTextColor(select_color);
                    break;
                case R.id.linear_gg:
                    text_gg.setTextColor(select_color);
                    break;
                case R.id.linear_mj:
                    text_mj.setTextColor(select_color);
                    break;
                case R.id.linear_nc:
                    text_nc.setTextColor(select_color);
                    break;
                case R.id.linear_ld:
                    text_ld.setTextColor(select_color);
                    break;
                case R.id.linear_fs:
                    text_fs.setTextColor(select_color);
                    break;
            }
            mViewPager.setCurrentItem(index);
        }
    }

    /**
     * 将文字设置为未选中时的颜色
     */
    private void resetTextColor() {
        text_jd.setTextColor(unselect_color);
        text_dp.setTextColor(unselect_color);
        text_gg.setTextColor(unselect_color);
        text_mj.setTextColor(unselect_color);
        text_nc.setTextColor(unselect_color);
        text_ld.setTextColor(unselect_color);
        text_fs.setTextColor(unselect_color);
    }

}
