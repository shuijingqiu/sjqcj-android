package com.example.sjqcjstock.Activity.firm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.Article.ArticleDetailsActivity;
import com.example.sjqcjstock.Activity.user.loginActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.fragment.firm.FragmentMatchGg;
import com.example.sjqcjstock.fragment.firm.FragmentMatchZsy;

import java.util.ArrayList;
import java.util.List;

/**
 * 比赛详情页面
 * Created by Administrator on 2017/8/28.
 */
public class FirmDetailsActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    private TextView titleTv;
    private TextView unameTv;

    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mDatas;
    // 控件
    private TextView text_zsy = null;
    private TextView text_jwr = null;
    private TextView text_dr = null;
    private TextView text_gg = null;
    private LinearLayout ll_zsy = null;
    private LinearLayout ll_jwr = null;
    private LinearLayout ll_gg = null;
    private LinearLayout ll_dr = null;
    private Button sendBt;

    // 比赛标题
    private String title;
    // 比赛状态 1报名中  2进行中  3已结束  4待开始
    private String state;
    // 是否参加比赛 0未参加  1参加
    private String isJoin;
    // 跳转主办人的id
//    private String uid;
    // 跳转主办人的名称
    private String uname;
    // 详情跳转文章的id
    private String feedId;
    // 比赛id
    private String firmId;
    private int unselect_color;
    private int select_color;
    // 显示报单方式
    private RelativeLayout screenshotRl;
    // 比赛剩余天数
    private String residue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firm_details);
        findView();
        getData();
        initFragment();
    }

    private void findView() {
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        feedId = getIntent().getStringExtra("feedId");
//        uid = getIntent().getStringExtra("uid");
        uname = getIntent().getStringExtra("uname");
        title = getIntent().getStringExtra("title");
        firmId = getIntent().getStringExtra("id");
        state = getIntent().getStringExtra("state");
        isJoin = getIntent().getStringExtra("isJoin");
        residue = getIntent().getStringExtra("residue");
        titleTv = (TextView) findViewById(R.id.title_tv);
        titleTv.setText(title);
        unameTv = (TextView) findViewById(R.id.uname_tv);
        unameTv.setText(uname);

        unselect_color = getResources().getColor(R.color.color_toptitle);
        select_color = getResources().getColor(R.color.white);

        screenshotRl = (RelativeLayout) findViewById(R.id.screenshot_rl);
        screenshotRl.getBackground().setAlpha(150);
        screenshotRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenshotRl.setVisibility(View.GONE);
            }
        });
        text_zsy = (TextView) findViewById(R.id.text_zsy);
        text_jwr = (TextView) findViewById(R.id.text_jwr);
        text_gg = (TextView) findViewById(R.id.text_gg);
        text_dr = (TextView) findViewById(R.id.text_dr);
        ll_zsy = (LinearLayout) findViewById(R.id.linear_zsy);
        ll_jwr = (LinearLayout) findViewById(R.id.linear_jwr);
        ll_gg = (LinearLayout) findViewById(R.id.linear_gg);
        ll_dr = (LinearLayout) findViewById(R.id.linear_dr);

        ll_zsy.setOnClickListener(new MyOnClickListenser(0));
        ll_jwr.setOnClickListener(new MyOnClickListenser(1));
        ll_dr.setOnClickListener(new MyOnClickListenser(2));
        ll_gg.setOnClickListener(new MyOnClickListenser(3));
        mViewPager = (ViewPager) findViewById(R.id.mViewpager);
        mViewPager.setOffscreenPageLimit(4);

        sendBt = (Button) findViewById(R.id.send_bt);
        mDatas = new ArrayList<Fragment>();
        if ("1".equals(state) && "1".equals(isJoin)){
            sendBt.setText("已报名");
        }else if ("2".equals(state)){
            sendBt.setText("进行中");
            if ("1".equals(isJoin)){
                findViewById(R.id.declaration_form_tv).setVisibility(View.VISIBLE);
            }
        }else if ("3".equals(state)){
            sendBt.setText("已结束");
        }

    }

    private void getData() {
    }


    /**
     * 初始化fragment
     */
    private void initFragment() {
        FragmentMatchZsy mzsy = new FragmentMatchZsy(firmId,1,state,residue);
        FragmentMatchZsy mjwr = new FragmentMatchZsy(firmId,2,state,residue);
        FragmentMatchZsy mgg = new FragmentMatchZsy(firmId,3,state,residue);
        FragmentMatchGg mdr = new FragmentMatchGg(firmId);
        mDatas.add(mzsy);
        mDatas.add(mjwr);
        mDatas.add(mgg);
        mDatas.add(mdr);
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


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        viewPagerW = mViewPager.getWidth() + mViewPager.getPageMargin();
//        LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) img_line.getLayoutParams();
//        // 关键算法
//        lp.leftMargin = (int) ((int) (mScreen1_4 * position) + (((double) positionOffsetPixels / viewPagerW) * mScreen1_4));
//        img_line.setLayoutParams(lp);
    }

    @Override
    public void onPageSelected(int position) {
        resetTextColor();
        switch (mViewPager.getCurrentItem()) {
            case 0:
                text_zsy.setTextColor(select_color);
                ll_zsy.setBackgroundResource(R.drawable.buttonstyle2);
                break;
            case 1:
                text_jwr.setTextColor(select_color);
                ll_jwr.setBackgroundResource(R.drawable.buttonstyle3);
                break;
            case 2:
                text_dr.setTextColor(select_color);
                ll_dr.setBackgroundResource(R.drawable.buttonstyle3);
                break;
            case 3:
                text_gg.setTextColor(select_color);
                ll_gg.setBackgroundResource(R.drawable.buttonstyle14);
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
                case R.id.linear_zsy:
                    text_zsy.setTextColor(select_color);
                    ll_zsy.setBackgroundResource(R.drawable.buttonstyle2);
                    break;
                case R.id.linear_jwr:
                    text_jwr.setTextColor(select_color);
                    ll_jwr.setBackgroundResource(R.drawable.buttonstyle3);
                    break;
                case R.id.linear_dr:
                    text_dr.setTextColor(select_color);
                    ll_dr.setBackgroundResource(R.drawable.buttonstyle3);
                    break;
                case R.id.linear_gg:
                    text_gg.setTextColor(select_color);
                    ll_gg.setBackgroundResource(R.drawable.buttonstyle14);
                    break;
            }
            mViewPager.setCurrentItem(index);
        }
    }

    /**
     * 将文字设置为未选中时的颜色
     */
    private void resetTextColor() {
        text_zsy.setTextColor(unselect_color);
        text_jwr.setTextColor(unselect_color);
        text_gg.setTextColor(unselect_color);
        text_dr.setTextColor(unselect_color);
        ll_zsy.setBackgroundResource(R.drawable.buttonstyle2_2);
        ll_jwr.setBackgroundResource(R.drawable.buttonstyle3_3);
        ll_dr.setBackgroundResource(R.drawable.buttonstyle15_2);
        ll_gg.setBackgroundResource(R.drawable.buttonstyle14_2);
    }


    /**
     * 报单的单击事件
     * @param view
     */
    public void DeclarationFormClick(View view){
        // 只用用户登陆后才能来订阅
        if (!Constants.isLogin){
            Intent intent = new Intent(this, loginActivity.class);
            startActivity(intent);
            return;
        }
        screenshotRl.setVisibility(View.VISIBLE);
    }

    /**
     * 详情的单击事件
     * @param view
     */
    public void DetailsClick(View view){
        Intent intent = new Intent(FirmDetailsActivity.this, ArticleDetailsActivity.class);
        intent.putExtra("weibo_id",feedId);
        startActivity(intent);
    }

    /**
     * 参加报名的单击事件
     * @param view
     */
    public void EnrollClick(View view){
        // menghuan 不用登陆也可以用
        // 只用用户登陆后才能来订阅
        if (!Constants.isLogin){
            Intent intent = new Intent(this, loginActivity.class);
            startActivity(intent);
            return;
        }
        // 报名中且未参加比赛才能参加比赛
        if ("1".equals(state) && "0".equals(isJoin)) {
            Intent intent = new Intent(FirmDetailsActivity.this, FirmSignUpActivity.class);
            intent.putExtra("firmId", firmId);
            startActivityForResult(intent,1);
        }
    }

//    /**
//     * 跳转到个人主页
//     * @param view
//     */
//    public void UnameClick(View view){
//        Intent intent = new Intent(FirmDetailsActivity.this, UserDetailNewActivity.class);
//        intent.putExtra("uid", uid);
//        startActivity(intent);
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 从相册获取图片
        if (requestCode == 1) {
            if (data != null) {
                String isSuccess  = data.getExtras().get("isSuccess").toString();
                if (isSuccess != null && "true".equals(isSuccess)){
                    sendBt.setText("已报名");
                }
            }
        }
    }
    /**
     * 盘中报单
     * @param view
     */
    public void MiddleFromClick(View view){
        Intent intent = new Intent(FirmDetailsActivity.this, FirmEndDeclarationFormActivity.class);
        intent.putExtra("firmId",firmId);
        intent.putExtra("title",title);
        startActivity(intent);
        screenshotRl.setVisibility(View.GONE);
    }

    /**
     * 盘尾报单
     * @param view
     */
    public void EndFromClick(View view){
        Intent intent = new Intent(FirmDetailsActivity.this, FirmDeclarationFormActivity.class);
        intent.putExtra("firmId",firmId);
        intent.putExtra("title",title);
        startActivity(intent);
        screenshotRl.setVisibility(View.GONE);
    }


}
