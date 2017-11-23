package com.example.sjqcjstock.Activity.Tomlive;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.Activity.AgreementActivity;
import com.example.sjqcjstock.Activity.RechargeActivity;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.Activity.user.loginActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.tomlive.PriceAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.Tomlive.SpinnerData;
import com.example.sjqcjstock.entity.Tomlive.TomliveRoomEntity;
import com.example.sjqcjstock.fragment.Tomlive.FragmentChat;
import com.example.sjqcjstock.fragment.Tomlive.FragmentTomlive;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Md5Util;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.userdefined.MyScrollView;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.SoListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 直播间的页面
 * Created by Administrator on 2017/1/10.
 */
public class DirectBroadcastingRoomActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    // 网络请求提示
    private CustomProgress dialog;
    private LinearLayout goback1;
    // 两个个滑动页面
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mDatas;
    // 控件
    private TextView text_zb = null;
    private TextView text_lt = null;
    private LinearLayout ll_zb = null;
    private LinearLayout ll_lt = null;
    // 简介
    private TextView presentTv;
    // 名称
    private TextView nameTv;
    // 头像
    private ImageView headIm;
    // 打赏占位页
    private MyScrollView playShowLl;
    // 内容占位
    private LinearLayout mainContentsLl;
    // Title标题
    private TextView roomNameTv;
    // 价格选择的List
    private SoListView priceLv;
    // 价格选择的Adapter
    private PriceAdapter priceAdapter;
    // 直播介绍
    private TextView introduceTv;
    // 参与人数
    private TextView numberTv;


    // 滑动条颜色
    private int select_color;
    private int unselect_color;
    /**
     * 当前视图宽度
     **/
    private Integer viewPagerW = 0;
    private int mScreen1_4;
    private ImageView img_line;
    // 聊天的页面
    private FragmentChat ltLIst;
    // 直播的页面
    private FragmentTomlive zbList;
    // 房间ID
    private String roomId;
    // 房主uid
    private String ownerUid;
    // 房间返回的数据
    private String resstr;
    // 支付的水晶币个数
    private String sjbCount = "0";
    // 订阅的月份
    private String desertTime;
    // 调用是否订阅的接口获取的数据
    private String desertStr;
    // 价格数据
    private ArrayList<SpinnerData> pricelist;
    // 订阅的接口返回
    private String desertRst;
    // 房间信息
    private String roomJson = "";
    // 是否是免费直播间
    private String type = "2";

    // 水晶币返回数据
    private String jsonStr;

    public DirectBroadcastingRoomActivity(){}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_direct_broadcasting_room);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
        initFragment();
        initLine();
        getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null){
            dialog.dismissDlog();
        }
    }

    /**
     * 获取直播间数据
     */
    public void getData() {
        // 获取直播间信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                resstr = HttpUtil.restHttpGet(Constants.moUrl+"/live/room/info?id="+roomId);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    // 获取水晶币数量
    public void getMoney(){
//        // 每次进入都获取水晶币数量
//        new SendInfoTaskmywealth().execute(new TaskParams(Constants.appUserMoneyUrl,
//                new String[]{"mid", Constants.staticmyuidstr}
//        ));
        // 开线程获取水晶币数量
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/credit/info?mid=" + Constants.staticmyuidstr + "&token=" + Constants.apptoken);
                handler.sendEmptyMessage(3);
            }
        }).start();
    }

    /**
     * 判断当前用户是否被订阅
     */
    private void getisDesert() {
        // 开线程获取用户是否订阅
        new Thread(new Runnable() {
            @Override
            public void run() {
                desertStr = HttpUtil.restHttpGet(Constants.moUrl + "/desert/isDesert?uid=" + Constants.staticmyuidstr + "&price_uid=" + ownerUid+"&type=2");
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

    private void initView() {
        dialog = new CustomProgress(this);
        dialog.showDialog();
        roomId = getIntent().getStringExtra("roomId");
        String name = getIntent().getStringExtra("name");
        roomNameTv = (TextView) findViewById(R.id.room_name_tv);
        nameTv = (TextView) findViewById(R.id.name_tv);
        if (name != null) {
            roomNameTv.setText(name + "直播间");
            nameTv.setText(name);
        }
        ownerUid = getIntent().getStringExtra("uid");
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        goback1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        presentTv = (TextView) findViewById(R.id.present_tv);
        headIm = (ImageView) findViewById(R.id.head_img);
        numberTv = (TextView) findViewById(R.id.number_tv);
        numberTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DirectBroadcastingRoomActivity.this,SubscriptionList.class);
                startActivity(intent);
            }
        });
        presentTv.setText(getIntent().getStringExtra("remark"));
        if (ownerUid != null) {
            ImageLoader.getInstance().displayImage(Md5Util.getuidstrMd5(Md5Util
                            .getMd5(ownerUid)),
                    headIm, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        }
        // 获取颜色
        select_color = getResources().getColor(R.color.color_toptitle);
        unselect_color = getResources().getColor(R.color.color_000000);
        text_zb = (TextView) findViewById(R.id.text_zb);
        text_lt = (TextView) findViewById(R.id.text_lt);
        ll_zb = (LinearLayout) findViewById(R.id.linear_zb);
        ll_lt = (LinearLayout) findViewById(R.id.linear_lt);
        ll_zb.setOnClickListener(new MyOnClickListenser(0));
        ll_lt.setOnClickListener(new MyOnClickListenser(1));
        mainContentsLl = (LinearLayout) findViewById(R.id.main_contents_ll);
        playShowLl = (MyScrollView) findViewById(R.id.play_show_ll);
        mViewPager = (ViewPager) findViewById(R.id.mViewpager);
        mViewPager.setOffscreenPageLimit(1);
        mDatas = new ArrayList<Fragment>();
        findViewById(R.id.totle_rl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // 不是就跳转到房主的个人信息里面
            Intent intent = new Intent(DirectBroadcastingRoomActivity.this,
                        UserDetailNewActivity.class);
                intent.putExtra("uid", ownerUid);
                startActivity(intent);
            }
        });
        introduceTv = (TextView) findViewById(R.id.introduce_tv);
        presentTv.setOnClickListener(new View.OnClickListener() {
            Boolean flag = true;
            @Override
            public void onClick(View v) {
                if(flag){
                    flag = false;
                    presentTv.setEllipsize(null); // 展开
                    presentTv.setSingleLine(flag);
                }else{
                    flag = true;
                    presentTv.setLines(2);
                    presentTv.setEllipsize(TextUtils.TruncateAt.END); // 收缩
                    presentTv.setSingleLine(flag);
                }
            }
        });
        // 付费
        if (Constants.staticmyuidstr.equals(ownerUid)){
            numberTv.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        zbList = new FragmentTomlive(this,roomId,ownerUid);
        ltLIst = new FragmentChat(roomId,ownerUid);
        mDatas.add(zbList);
        mDatas.add(ltLIst);
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
        mScreen1_4 = ImageUtil.getScreenWidth(this) / 2;
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
                text_lt.setTextColor(unselect_color);
                text_zb.setTextColor(select_color);
                break;
            case 1:
                text_zb.setTextColor(unselect_color);
                text_lt.setTextColor(select_color);
                if (ltLIst != null){
                    ltLIst.onHiddenChanged();
                }
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
                    text_lt.setTextColor(unselect_color);
                    text_zb.setTextColor(select_color);
                    break;
                case R.id.linear_dp:
                    text_zb.setTextColor(unselect_color);
                    text_lt.setTextColor(select_color);
                    if (ltLIst != null){
                        ltLIst.onHiddenChanged();
                    }
                    break;
            }
            mViewPager.setCurrentItem(index);
        }
    }

    /**
     * 切换选项卡用于回调
     */
    public void switchChat(){
        // 切换选项卡
        mViewPager.setCurrentItem(1);
        ltLIst.showChat();
    }

    /**
     * 线程更新Ui
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    try {
                        JSONObject jsonObject = new JSONObject(resstr);
                        if ("failed".equals(jsonObject.getString("status"))){
                            dialog.dismissDlog();
                            return;
                        }
                        roomJson = jsonObject.getString("data");
                        TomliveRoomEntity tomliveRoom = JSON.parseObject(roomJson,TomliveRoomEntity.class);
                        type = tomliveRoom.getType();
                        // 设置是否免费直播间  以后全部付费不要
                        if (zbList !=null){
                            zbList.setType(type);
                        }
                        presentTv.setText(tomliveRoom.getIntro());
                        numberTv.setText(tomliveRoom.getCount()+"人订阅");
                        roomNameTv.setText(tomliveRoom.getUsername() + "直播间");
                        nameTv.setText(tomliveRoom.getUsername());
                        if ("2".equals(type) || Constants.staticmyuidstr.equals(ownerUid)){
                            // 免费
                            playShowLl.setVisibility(View.GONE);
                            mainContentsLl.setVisibility(View.VISIBLE);
                        }else{
                            // 付费
                            if (!Constants.staticmyuidstr.equals(ownerUid)){
                                // 不是房主进入才查询是否订阅
                                getisDesert();
                            }else{
                                // 房主进来直接显示
                                playShowLl.setVisibility(View.GONE);
                                mainContentsLl.setVisibility(View.VISIBLE);
                            }
                            // 付费才去更新水晶币数量
                            getMoney();
                        }

                        // 价格数据
                        pricelist = new ArrayList<SpinnerData>();
                        SpinnerData spinnerData;
                        if (tomliveRoom.getPrices()!=null && tomliveRoom.getPrices().size()>0){
                            for (int i=0;i<tomliveRoom.getPrices().size();i++){
                                spinnerData = new SpinnerData(tomliveRoom.getPrices().get(i).getExp_time(),tomliveRoom.getPrices().get(i).getPrice());
                                pricelist.add(spinnerData);
                            }
                            // 显示加载价格数据
                            priceLv = (SoListView) findViewById(R.id.price_lv);
                            priceAdapter = new PriceAdapter(DirectBroadcastingRoomActivity.this);
                            priceLv.setAdapter(priceAdapter);
                            priceLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    sjbCount = pricelist.get(position).getPrice();
                                    desertTime = pricelist.get(position).getTime();
                                    priceAdapter.setItem(desertTime);
                                }
                            });
                            sjbCount = pricelist.get(0).getPrice();
                            desertTime = pricelist.get(0).getTime();
                            priceAdapter.setItem(desertTime);
                            // 加载数据价格
                            priceAdapter.setlistData(pricelist);
//                            Utils.setListViewHeightBasedOnChildren(priceLv);
                        }
                        introduceTv.setText(tomliveRoom.getRemark());
                        if (dialog != null){
                            dialog.dismissDlog();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(desertStr);
                        if ("failed".equals(jsonObject.getString("status"))) {
                            playShowLl.setVisibility(View.VISIBLE);
                            // 滚动到顶部
                            playShowLl.smoothScrollTo(0, 0);
                            mainContentsLl.setVisibility(View.GONE);
                        } else {
                            String data = jsonObject.getString("data");
                            if (data.length() > 3) {
                                JSONObject jsonStr = new JSONObject(data);
                                String expTime = jsonStr.getString("exp_time");
                                if (Utils.ContrastTime1(expTime)) {
                                    playShowLl.setVisibility(View.GONE);
                                    mainContentsLl.setVisibility(View.VISIBLE);
                                } else {
                                    playShowLl.setVisibility(View.VISIBLE);
                                    // 滚动到顶部
                                    playShowLl.smoothScrollTo(0, 0);
                                    mainContentsLl.setVisibility(View.GONE);
                                }
                            } else {
                                playShowLl.setVisibility(View.VISIBLE);
                                // 滚动到顶部
                                playShowLl.smoothScrollTo(0, 0);
                                mainContentsLl.setVisibility(View.GONE);
                            }
                        }
                    }catch(JSONException e){
                        e.printStackTrace();
                    }

                    break;
                case 2:
                    dialog.dismissDlog();
                    try {
                        JSONObject jsonObject = new JSONObject(desertRst);
                        if ("failed".equals(jsonObject.getString("status"))) {
                            Toast.makeText(getApplicationContext(), "订阅失败", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(getApplicationContext(), "订阅成功", Toast.LENGTH_SHORT).show();
                        Constants.shuijinbiCount = Integer.valueOf(Constants.shuijinbiCount) - Integer.valueOf(sjbCount)+"";
                        playShowLl.setVisibility(View.GONE);
                        mainContentsLl.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        if (Constants.successCode.equals(jsonObject.getString("code"))) {
                            JSONObject jsonData = new JSONObject(jsonObject.getString("data"));
                            String shuijingbi = jsonData.getString("shuijingbi");
                            Constants.shuijinbiCount = shuijingbi;
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    /**
     * 订阅水晶币的单机事件
     * @param view
     */
    public void PayCrystalCoin(final View view){
        // menghuan 不用登陆也可以用
        // 只用用户登陆后才能来订阅
        if (!Constants.isLogin){
            Intent intent = new Intent(this, loginActivity.class);
            startActivity(intent);
            return;
        }
        final View viewSang = LayoutInflater.from(DirectBroadcastingRoomActivity.this).inflate(R.layout.dialog_pay, null);
        RelativeLayout rl_dialogDismiss = (RelativeLayout) viewSang.findViewById(R.id.rl_dialogDismiss);

        //显示需要支付水晶币数量的控件
        TextView inputCount = (TextView) viewSang.findViewById(R.id.tv_inputCount);
        inputCount.setText("需要打赏："+sjbCount+"水晶币");

        // 显示水晶币数量的控件
        TextView restCount = (TextView) viewSang.findViewById(R.id.tv_restCount);
        restCount.setText(Constants.shuijinbiCount);
        Button bt_ok = (Button) viewSang.findViewById(R.id.bt_ok);

        final AlertDialog alertDialog = new AlertDialog.Builder(DirectBroadcastingRoomActivity.this).setView(viewSang).show();
        rl_dialogDismiss.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //销毁弹出框
                alertDialog.dismiss();
            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Integer.valueOf(Constants.shuijinbiCount) < Integer.valueOf(sjbCount)) {
                    CustomToast.makeText(getApplicationContext(), "对不起你水晶币不足。", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if (!((CheckBox) viewSang.findViewById(R.id.sang_agreement_ck)).isChecked()) {
                    CustomToast.makeText(getApplicationContext(), "请阅读《水晶球直播服务协议》", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.showDialog();
                // 确认支付订阅的接口
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List dataList = new ArrayList();
                        // 用户ID
                        dataList.add(new BasicNameValuePair("uid", Constants.staticmyuidstr));
                        dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                        dataList.add(new BasicNameValuePair("price_uid", ownerUid));
                        dataList.add(new BasicNameValuePair("desert_time",desertTime));
                        dataList.add(new BasicNameValuePair("type", "2"));
                        dataList.add(new BasicNameValuePair("payment", "6"));
                        desertRst = HttpUtil.restHttpPost(Constants.moUrl+"/desert/order",dataList);
                        handler.sendEmptyMessage(2);
                    }
                }).start();

                alertDialog.dismiss();
            }
        });

        viewSang.findViewById(R.id.sang_agreement_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到水晶币协议页面
                Intent intent = new Intent(DirectBroadcastingRoomActivity.this, AgreementActivity.class);
                intent.putExtra("type","15");
                startActivity(intent);
            }
        });
        viewSang.findViewById(R.id.bt_cz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到充值页面
                Intent intent = new Intent(DirectBroadcastingRoomActivity.this, RechargeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
//
//    // 获取用户财富设置水晶币个数
//    private class SendInfoTaskmywealth extends AsyncTask<TaskParams, Void, String> {
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            if (result == null) {
//                CustomToast.makeText(DirectBroadcastingRoomActivity.this, "", Toast.LENGTH_SHORT).show();
//            } else {
//                String count = "0";
//                try {
//                    JSONObject jsonObj = new JSONObject(result);
//                    // 获取水晶币
//                    count = jsonObj.getJSONObject("data").getJSONObject("credit").getJSONObject("shuijingbi").getString("value");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                Constants.shuijinbiCount = count;
//            }
//        }
//    }
}
