package com.example.sjqcjstock.Activity.stocks;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjqcjstock.Activity.myattentionlistActivity;
import com.example.sjqcjstock.Activity.myfansActivity;
import com.example.sjqcjstock.Activity.personalnewsdetail;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.fragment.stocks.FragmentMicroBlog;
import com.example.sjqcjstock.fragment.stocks.FragmentStock;
import com.example.sjqcjstock.fragment.stocks.FragmentTransactionList;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.example.sjqcjstock.view.CustomToast;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 个人中心(模拟炒股和以前的个人中心集合)
 *
 * @author Administrator
 */
public class UserDetailNewActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {
    // 获取uid
    private String uidstr;
    // 获取控件
    private TextView username2;
    private ImageView headimg2;
    private ImageView usersex1;
    private TextView userintro1;
    private TextView following_count2;
    private TextView follower_count2;
    private LinearLayout attention3;
    private Button personalletter3;
    private LinearLayout myattentionuserlist1;
    private LinearLayout myfansuserlist1;
    private LinearLayout goback1;
    private Button followersign1;
    ;
    private String followerstr;
    private String followingstr;
    // 获取自己的id信息
    private String myuid;
    private ImageView vipImg;
    private String unamestr;
    private String list_idstr;


    // 七个滑动页面
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mDatas;
    // 控件
    private TextView textMicroBlog = null;
    private TextView textTransaction = null;
    private TextView textStock = null;
    private LinearLayout llMicroBlog = null;
    private LinearLayout llTransaction = null;
    private LinearLayout llStock = null;
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
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_detail);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        // 可以在主线程中使用http网络访问
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        initView();
        initFragment();
        initLine();
    }

    private void initView() {
        // 获取自己的id信息
        myuid = getSharedPreferences("userinfo", MODE_PRIVATE).getString("userid", "");
        // 获取intent的数据
        uidstr = getIntent().getStringExtra("uid");
        if (myuid.equals(uidstr)) {
            findViewById(R.id.private_letter_follow_ll).setVisibility(View.GONE);
            findViewById(R.id.line_iv).setVisibility(View.GONE);
        }

        attention3 = (LinearLayout) findViewById(R.id.attention3);
        username2 = (TextView) findViewById(R.id.username2);
        headimg2 = (ImageView) findViewById(R.id.headimg2);
        usersex1 = (ImageView) findViewById(R.id.usersex1);
        userintro1 = (TextView) findViewById(R.id.userintro1);
        following_count2 = (TextView) findViewById(R.id.following_count2);
        follower_count2 = (TextView) findViewById(R.id.follower_count2);
        personalletter3 = (Button) findViewById(R.id.personalletter3);
        myattentionuserlist1 = (LinearLayout) findViewById(R.id.myattentionuserlist1);
        myfansuserlist1 = (LinearLayout) findViewById(R.id.myfansuserlist1);
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        followersign1 = (Button) findViewById(R.id.followersign1);
        vipImg = (ImageView) findViewById(R.id.vip_img);
        personalletter3.setOnClickListener(new personalletter3_listener());
        myattentionuserlist1
                .setOnClickListener(new myattentionuserlist1_listener());
        myfansuserlist1.setOnClickListener(new myfansuserlist1_listener());
        goback1.setOnClickListener(new goback1_listener());
        followersign1.setOnClickListener(new followersign1_listener());
        // 从网络获取用户详细信息
        new SendInfoTasksuserinfodetail().execute(new TaskParams(
                Constants.Url + "?app=public&mod=Profile&act=AppUser",
                new String[]{"mid", uidstr},
                new String[]{"id", myuid}
        ));
        // 下面新加的东西
        textMicroBlog = (TextView) findViewById(R.id.text_micro_blog);
        textTransaction = (TextView) findViewById(R.id.text_transaction);
        textStock = (TextView) findViewById(R.id.text_my_stock);

        llMicroBlog = (LinearLayout) findViewById(R.id.linear_micro_blog);
        llTransaction = (LinearLayout) findViewById(R.id.linear_transaction);
        llStock = (LinearLayout) findViewById(R.id.linear_my_stock);
        llMicroBlog.setOnClickListener(new MyOnClickListenser(0));
        llTransaction.setOnClickListener(new MyOnClickListenser(1));
        llStock.setOnClickListener(new MyOnClickListenser(2));
        mViewPager = (ViewPager) findViewById(R.id.mViewpager);
        mViewPager.setOffscreenPageLimit(2);
        mDatas = new ArrayList<Fragment>();
        // 获取颜色
        select_color = getResources().getColor(R.color.color_toptitle);
        unselect_color = getResources().getColor(R.color.color_000000);

    }

    class goback1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            finish();
        }
    }

    class followersign1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            if ("0".equals(followingstr)) {
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                new SendInfoTaskfollowsb().execute(new TaskParams(
                        Constants.Url + "?app=public&mod=AppFeedList&act=AddFollow", new String[]{"mid",
                        Constants.staticmyuidstr},
                        new String[]{"login_password",
                                Constants.staticpasswordstr}, new String[]{
                        "fid", uidstr}
                ));
                if ("1".equals(followerstr)) {
                    followersign1.setText("相互关注");
                } else {
                    followersign1.setText("取消关注");
                }
                followingstr = "1";
            } else {
                if (Utils.isFastDoubleClick()) {
                    return;
                }
                new SendInfoTaskfollowcancelsb().execute(new TaskParams(
                                Constants.Url + "?app=public&mod=AppFeedList&act=DelFollow", new String[]{"mid",
                                Constants.staticmyuidstr},
                                new String[]{"login_password",
                                        Constants.staticpasswordstr}, new String[]{
                                "fid", uidstr}
                        )
                );
                followersign1.setText(" + 关注");
                followingstr = "0";
            }
        }
    }

    class myfansuserlist1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(UserDetailNewActivity.this,
                    myfansActivity.class);
            intent.putExtra("uidstr", uidstr);
            startActivity(intent);
        }

    }

    class myattentionuserlist1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(UserDetailNewActivity.this,
                    myattentionlistActivity.class);
            intent.putExtra("uidstr", uidstr);
            startActivity(intent);
        }
    }

    class personalletter3_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            new SendInfoTaskForChatIsExist().execute(new TaskParams(
                    Constants.Url + "?app=public&mod=AppFeedList&act=Messages",
                    new String[]{"mid", Constants.staticmyuidstr},
                    new String[]{"cid", uidstr}));
        }
    }

    private class SendInfoTasksuserinfodetail extends
            AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                CustomToast.makeText(UserDetailNewActivity.this, "", Toast.LENGTH_LONG).show();
            } else {
                super.onPostExecute(result);
                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);

                for (Map<String, Object> map : lists) {
                    String statusstr = map.get("data") + "";
                    statusstr = "[" + statusstr + "]";
                    List<Map<String, Object>> weibolists = JsonTools.listKeyMaps(statusstr);

                    for (Map<String, Object> weibomap : weibolists) {
                        String introstr;
                        unamestr = weibomap.get("uname") + "";
                        String avatar_middlestr = weibomap.get("avatar_middle") + "";
                        String Userdatastr = weibomap.get("Userdata") + "";
                        List<Map<String, Object>> Userdatastrlists = JsonTools.listKeyMaps("[" + Userdatastr + "]");

                        for (Map<String, Object> Userdatamap : Userdatastrlists) {
                            String following_countstr;
                            String follower_countstr;
//                            String weibo_countstr;
                            if (Userdatamap.get("following_count") == null) {
                                following_countstr = "0";
                            } else {
                                following_countstr = Userdatamap.get("following_count") + "";
                            }

                            if (Userdatamap.get("follower_count") == null) {
                                follower_countstr = "0";
                            } else {
                                follower_countstr = Userdatamap.get("follower_count") + "";
                            }
//
//                            if (Userdatamap.get("weibo_count") == null) {
//                                weibo_countstr = "0";
//                            } else {
//                                weibo_countstr = Userdatamap.get("weibo_count")+"";
//                            }
                            following_count2.setText(following_countstr);
                            follower_count2.setText(follower_countstr);
                        }
                        String followstr = weibomap.get("follow") + "";
                        List<Map<String, Object>> followstrlists = JsonTools.listKeyMaps("[" + followstr + "]");
                        for (Map<String, Object> followstrmap : followstrlists) {
                            followingstr = followstrmap.get("following") + "";
                            followerstr = followstrmap.get("follower") + "";// 0是未关注,1是已关注
                            if ("0".equals(followingstr)) {
                                followersign1.setText(" + 关注");
                            } else {
                                if ("1".equals(followerstr)) {
                                    followersign1.setText("相互关注");
                                } else {
                                    followersign1.setText("取消关注");
                                }
                            }
                        }
                        //空指针异常
                        String sexstr = weibomap.get("sex") + "";
                        if (weibomap.get("intro") == null) {
                            introstr = "暂无简介";
                        } else {
                            introstr = weibomap.get("intro") + "";
                        }
                        if ("1".equals(sexstr)) {
                            usersex1.setImageResource(R.mipmap.nan);
                        } else {
                            usersex1.setImageResource(R.mipmap.nv);
                        }
                        username2.setText(unamestr);
                        userintro1.setText(introstr);

                        ImageLoader.getInstance().displayImage(avatar_middlestr,
                                headimg2, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());

                        ViewUtil.setUpVip(weibomap.get("user_group") + "", vipImg);
                    }
                }
            }
        }
    }

    private class SendInfoTaskfollowsb extends
            AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            int follows = Integer.parseInt((follower_count2.getText()
                    .toString()));
            follower_count2.setText(String.valueOf((follows + 1)));
        }
    }

    private class SendInfoTaskfollowcancelsb extends
            AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            int follows = Integer.parseInt((follower_count2.getText()
                    .toString()));

            follower_count2.setText(String.valueOf((follows - 1)));
        }
    }

    //发送请求，判断私信是否存在
    private class SendInfoTaskForChatIsExist extends AsyncTask<TaskParams, Void, String> {
        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            result = result.replace("\n ", "");
            result = result.replace("\n", "");
            result = result.replace(" ", "");
            result = "[" + result + "]";
            // 解析json字符串获得List<Map<String,Object>>
            List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
            for (Map<String, Object> map : lists) {
                String statusstr = map.get("status") + "";
                String datastr = map.get("data") + "";
                if ("1".equals(statusstr)) {
                    List<Map<String, Object>> datastrlists = JsonTools.listKeyMaps("[" + datastr + "]");
                    for (Map<String, Object> datastrmap : datastrlists) {
                        list_idstr = datastrmap.get("list_id") + "";
                    }
                    Intent intent = new Intent(UserDetailNewActivity.this, personalnewsdetail.class);
                    intent.putExtra("uidstr", uidstr);
                    intent.putExtra("unamestr", unamestr);
                    intent.putExtra("list_id", list_idstr);
                    startActivity(intent);
                }
                if ("0".equals(statusstr)) {
                    //不存在，直接跳转
                    Intent intent = new Intent(UserDetailNewActivity.this, personalnewsdetail.class);
                    intent.putExtra("uidstr", uidstr);
                    intent.putExtra("unamestr", unamestr);
                    startActivity(intent);
                }
            }
        }
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        FragmentMicroBlog microBlog = new FragmentMicroBlog(uidstr);
        FragmentTransactionList transaction = new FragmentTransactionList();
        FragmentStock stock = new FragmentStock();
        mDatas.add(microBlog);
        // 以后要要的
//        mDatas.add(transaction);
//        mDatas.add(stock);
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
        mScreen1_4 = ImageUtil.getScreenWidth(this) / 3;
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
        resetTextColor();
        switch (mViewPager.getCurrentItem()) {
            case 0:
                textMicroBlog.setTextColor(select_color);
                break;
            case 1:
                textTransaction.setTextColor(select_color);
                break;
            case 2:
                textStock.setTextColor(select_color);
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
                case R.id.linear_micro_blog:
                    textMicroBlog.setTextColor(select_color);
                    break;
                case R.id.linear_transaction:
                    textTransaction.setTextColor(select_color);
                    break;
                case R.id.linear_my_stock:
                    textStock.setTextColor(select_color);
                    break;
            }
            mViewPager.setCurrentItem(index);
        }
    }

    /**
     * 将文字设置为未选中时的颜色
     */
    private void resetTextColor() {
        textMicroBlog.setTextColor(unselect_color);
        textTransaction.setTextColor(unselect_color);
        textStock.setTextColor(unselect_color);
    }

}
