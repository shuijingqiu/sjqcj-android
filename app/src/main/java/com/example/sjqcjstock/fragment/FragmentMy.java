package com.example.sjqcjstock.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.Activity.ActivityMessage;
import com.example.sjqcjstock.Activity.AgreementActivity;
import com.example.sjqcjstock.Activity.Article.ReferenceActivity;
import com.example.sjqcjstock.Activity.Article.commentshortweiboActivity;
import com.example.sjqcjstock.Activity.CrystalCoinActivity;
import com.example.sjqcjstock.Activity.MyMatchActivity;
import com.example.sjqcjstock.Activity.OnlineServiceActivity;
import com.example.sjqcjstock.Activity.Tomlive.TomlivePersonnelListActivity;
import com.example.sjqcjstock.Activity.plan.MyPlanActivity;
import com.example.sjqcjstock.Activity.qa.MyQuestionAnswerActivity;
import com.example.sjqcjstock.Activity.stocks.ExpertSubscriptionActivity;
import com.example.sjqcjstock.Activity.stocks.MyDealAccountActivity;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.Activity.user.RegisterActivity;
import com.example.sjqcjstock.Activity.user.loginActivity;
import com.example.sjqcjstock.Activity.user.myfansActivity;
import com.example.sjqcjstock.Activity.user.usersettingActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.ACache;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.Article.UserEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 关于我的页面
 */
public class FragmentMy extends Fragment {

    // 获取控件
    private RelativeLayout pickuserinfoedit2;
    private LinearLayout pickcollect2;

    private LinearLayout picksetting;
    private LinearLayout pickaboutmy1;

    private TextView following_count1;
    private TextView follower_count1;
    private TextView weibo_count1;

    private ImageView usersex1;
    private TextView username2;
    private TextView userintro1;
    private ImageView headimg2;
    private LinearLayout myattention1;
    private LinearLayout mybyattention1;
    private ImageView pickaddweibo1;
    // 关注微博
    private LinearLayout ll_myAttention;
    // 我的订阅
    private LinearLayout myweibo1;
    private LinearLayout pickshuijinqiu1;
//    private String unamestr;
//    private String sexstr;
//    private String introstr;
//    private String avatar_middlestr;
    // 缓存用的类
    private ACache mCache;
    // 判断是否是第一次加载数据
    private boolean isFirst = true;
    // 缓存用户信息
    private String appUserStr = "";
    private ImageView vipImg;
    // 未读消息条数
    private TextView messageCountTv;
    // 下面红点点的显示
    private TextView messageTv;
    // 未读牛人动态消息条数
    private TextView expertCountTv;
    // 未读直播消息条数
    private TextView tomliveCountTv;

    public FragmentMy(){}
    public FragmentMy(TextView messageTv){
        this.messageTv = messageTv;
    }

    @Override
    public void onResume() {
        super.onResume();
        // menghuan 不用登陆也可以用
        // 如果未登陆跳转到登陆页面
        if (Constants.isLogin){
            // 未登录隐藏和显示某些控件
            getItemsal();
            setMessageCount();
            // 看是否去掉下方红点
            if (messageTv != null){
                int total = Constants.unreadCountInfo.getData().getUnread_total();
                String totalStr = total + "";
                if (totalStr.equals("0")&&Constants.unreadCountInfo.getNr_count()==0&&Constants.unreadCountInfo.getZb_count()==0) {
                    messageTv.setVisibility(View.GONE);
                } else {
                    messageTv.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, null);
        // 缓存类
        mCache = ACache.get(getActivity());
        initView(view);
        // menghuan 不用登陆也可以用
        // 如果未登陆跳转到登陆页面
        if (!Constants.isLogin){
            // 未登录隐藏和显示某些控件
            view.findViewById(R.id.head_ll).setVisibility(View.GONE);
            view.findViewById(R.id.jkh_bt).setVisibility(View.GONE);
            view.findViewById(R.id.register_ll).setVisibility(View.VISIBLE);
            view.findViewById(R.id.content_ll).setVisibility(View.VISIBLE);
            view.findViewById(R.id.content_ll).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // menghuan不用登陆也可以用
                    // 如果未登陆跳转到登陆页面
                    if (!Constants.isLogin){
                        Intent intent = new Intent(getActivity(), loginActivity.class);
                        startActivity(intent);
                        return;
                    }
                }
            });
        }
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getItemsal();
        }
    }

    /**
     * 设置消息体条数
     */
    public void setMessageCount() {
        if (Constants.unreadCountInfo != null) {
            int total = Constants.unreadCountInfo.getData().getUnread_total();
            String totalStr = total + "";
            if (total > 99) {
                totalStr = "99+";
            }
            if (messageCountTv!=null) {
                messageCountTv.setText(totalStr);
                if (totalStr.equals("0")) {
                    messageCountTv.setVisibility(View.GONE);
                } else {
                    messageCountTv.setVisibility(View.VISIBLE);
                }
            }
        }

        int count = Constants.unreadCountInfo.getNr_count();
        if (count > 0){
            if (count > 99) {
                expertCountTv.setText("+99");
            }else{
                expertCountTv.setText(count+"");
            }
            expertCountTv.setVisibility(View.VISIBLE);
        }else{
            expertCountTv.setVisibility(View.GONE);
        }
        int liveCount = Constants.unreadCountInfo.getZb_count();
        if (liveCount > 0){
            if (Integer.valueOf(liveCount) > 99) {
                tomliveCountTv.setText("+99");
            }else {
                tomliveCountTv.setText(liveCount+"");
            }
            tomliveCountTv.setVisibility(View.VISIBLE);
        }else{
            tomliveCountTv.setVisibility(View.GONE);
        }
    }

    /**
     * 获取用户信息
     */
    private void getItemsal() {
        if (isFirst) {
            isFirst = false;
            String userStr = mCache.getAsString("AppUserx");
            if (userStr != null && !userStr.equals("")) {
                setData(userStr);
            }
        }
        // 从网络获取用户详细信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                appUserStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/user/info?mid=" + Constants.staticmyuidstr);
                handler.sendEmptyMessage(0);
            }
        }).start();

    }

    private void initView(View view) {
        // 关注微博
        ll_myAttention = (LinearLayout) view.findViewById(R.id.ll_myAttention);
        // 我的收藏
        pickcollect2 = (LinearLayout) view.findViewById(R.id.pickcollect2);
        picksetting = (LinearLayout) view.findViewById(R.id.picksetting);
        myattention1 = (LinearLayout) view.findViewById(R.id.myattention1);
        mybyattention1 = (LinearLayout) view.findViewById(R.id.mybyattention1);
        pickuserinfoedit2 = (RelativeLayout) view.findViewById(R.id.pickuserinfoedit2);
        pickaboutmy1 = (LinearLayout) view.findViewById(R.id.pickaboutmy1);
        myweibo1 = (LinearLayout) view.findViewById(R.id.myweibo1);
        pickshuijinqiu1 = (LinearLayout) view.findViewById(R.id.pickshuijinqiu1);

        following_count1 = (TextView) view.findViewById(R.id.following_count1);
        follower_count1 = (TextView) view.findViewById(R.id.follower_count1);
        weibo_count1 = (TextView) view.findViewById(R.id.weibo_count1);

        usersex1 = (ImageView) view.findViewById(R.id.usersex1);
        username2 = (TextView) view.findViewById(R.id.username2);
        userintro1 = (TextView) view.findViewById(R.id.userintro1);
        userintro1.setOnClickListener(new OnClickListener() {
            Boolean flag = true;
            @Override
            public void onClick(View v) {
                if(flag){
                    flag = false;
                    userintro1.setEllipsize(null); // 展开
                    userintro1.setSingleLine(flag);
                }else{
                    flag = true;
                    userintro1.setLines(2);
                    userintro1.setEllipsize(TextUtils.TruncateAt.END); // 收缩
                    userintro1.setSingleLine(flag);
                }
            }
        });
        headimg2 = (ImageView) view.findViewById(R.id.headimg2);
        pickaddweibo1 = (ImageView) view.findViewById(R.id.pickaddweibo1);
        vipImg = (ImageView) view.findViewById(R.id.vip_img);

        messageCountTv = (TextView) view.findViewById(R.id.message_count_tv);
        expertCountTv = (TextView) view.findViewById(R.id.expert_count_tv);
        tomliveCountTv = (TextView) view.findViewById(R.id.tomlive_count_tv);

        following_count1.setText("0");
        follower_count1.setText("0");
        weibo_count1.setText("0");

        ll_myAttention.setOnClickListener(new ll_myAttention_listener());
        pickuserinfoedit2.setOnClickListener(new pickuserinfoedit2_listener());
        pickcollect2.setOnClickListener(new pickcollect2_listener());
        picksetting.setOnClickListener(new picksetting_listener());
        myattention1.setOnClickListener(new myattention1_listener());
        mybyattention1.setOnClickListener(new mybyattention1_listener());
        pickaboutmy1.setOnClickListener(new pickaboutmy1_listener());
        pickaddweibo1.setOnClickListener(new pickaddweibo1_listener());
        myweibo1.setOnClickListener(new myweibo1_listener());
        pickshuijinqiu1.setOnClickListener(new pickshuijinqiu1_listener());

        // 我的订阅单机事件
        view.findViewById(R.id.ll_myOrder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到我的订阅展示页面
                Intent intent = new Intent(getActivity(), ReferenceActivity.class);
                intent.putExtra("type","subscribe");
                startActivity(intent);
            }
        });

        /**
         * 我的比赛单机事件
         */
        view.findViewById(R.id.my_match_ll).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyMatchActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 消息单机事件
         */
        view.findViewById(R.id.message_ll).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityMessage.class);
                startActivity(intent);
            }
        });

        /**
         * 个人账户（模拟炒股的账户）
         */
        view.findViewById(R.id.mh_account_ll).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyDealAccountActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 跳转到我的牛人订阅列表页面
         * @param view
         */
        view.findViewById(R.id.my_dynamic_order).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExpertSubscriptionActivity.class);
                startActivity(intent);
                Constants.unreadCountInfo.setNr_count(0);
            }
        });

        /**
         * 跳转到直播列表页面
         * @param view
         */
        view.findViewById(R.id.ll_myTomlive).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到直播列表
                Intent intent = new Intent(getActivity(),
                        TomlivePersonnelListActivity.class);
                intent.putExtra("type","my");
                startActivity(intent);
            }
        });

        /**
         * 跳转到问答页面
         * @param view
         */
        view.findViewById(R.id.interlocution_ll).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        MyQuestionAnswerActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 跳转到在线客服页面
         * @param view
         */
        view.findViewById(R.id.online_service_ll).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        OnlineServiceActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 跳转常见问题
         * @param view
         */
        view.findViewById(R.id.commo_problem).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AgreementActivity.class);
                intent.putExtra("type", "17");
                startActivity(intent);
            }
        });

        /**
         * 跳转我的投资计划
         * @param view
         */
        view.findViewById(R.id.ll_myplan).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyPlanActivity.class);
                startActivity(intent);
            }
        });


        /**
         * 跳转到注册页面
         * @param view
         */
        view.findViewById(R.id.register_bt).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 跳转到登录页面
         * @param view
         */
        view.findViewById(R.id.sign_in_bt).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), loginActivity.class);
                startActivity(intent);
            }
        });

    }

    class pickshuijinqiu1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            startActivity(new Intent(getActivity(), CrystalCoinActivity.class));
        }
    }

    class myweibo1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // menghuan 不用登陆也可以用
            // 如果未登陆跳转到登陆页面
            if (!Constants.isLogin){
                Intent intent = new Intent(getActivity(), loginActivity.class);
                startActivity(intent);
                return;
            }
//            Intent intent = new Intent(getActivity(), mynoteslistActivity.class);
//            // 将头像和用户名传输到我的微博页面
//            intent.putExtra("avatar_middlestr", avatar_middlestr);
//            intent.putExtra("unamestr", unamestr);
//            startActivity(intent);

            Intent intent = new Intent(getActivity(), ReferenceActivity.class);
            intent.putExtra("type","myfeed");
            startActivity(intent);
        }

    }

    /**
     * 发短微博的单机事件
     */
    class pickaddweibo1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // menghuan 不用登陆也可以用
            // 如果未登陆跳转到登陆页面
            if (!Constants.isLogin){
                Intent intent = new Intent(getActivity(), loginActivity.class);
                startActivity(intent);
                return;
            }
            Intent intent = new Intent(getActivity(), commentshortweiboActivity.class);
            startActivity(intent);
        }
    }

    class pickaboutmy1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
//            //新版"关于我们"界面
//            Intent intent = new Intent(getActivity(), aboutmyActivityNew.class);
//            startActivity(intent);

            Intent intent = new Intent(getActivity(), AgreementActivity.class);
            intent.putExtra("type", "1");
            startActivity(intent);
        }
    }

    class pickuserinfoedit2_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent(getActivity().getApplicationContext(),
                    UserDetailNewActivity.class);
            // 要修改的
            intent.putExtra("uid",Constants.staticmyuidstr);
            intent.putExtra("type", "1");
            startActivity(intent);
        }
    }

    /**
     * 关注人的单机事件
     */
    class myattention1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // menghuan 不用登陆也可以用
            // 如果未登陆跳转到登陆页面
            if (!Constants.isLogin){
                Intent intent = new Intent(getActivity(), loginActivity.class);
                startActivity(intent);
                return;
            }
            Intent intent = new Intent(getActivity(), myfansActivity.class);
            intent.putExtra("type", "following");
            startActivity(intent);
        }
    }

    /**
     * 粉丝人数的单机事件
     */
    class mybyattention1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // menghuan 不用登陆也可以用
            // 如果未登陆跳转到登陆页面
            if (!Constants.isLogin){
                Intent intent = new Intent(getActivity(), loginActivity.class);
                startActivity(intent);
                return;
            }
            Intent intent = new Intent(getActivity(), myfansActivity.class);
            intent.putExtra("type", "follower");
            startActivity(intent);
        }
    }

    class ll_myAttention_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent(getActivity(), ReferenceActivity.class);
            intent.putExtra("type","following");
            startActivity(intent);
        }
    }

    class pickcollect2_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent(getActivity(), ReferenceActivity.class);
            intent.putExtra("type","collection");
            startActivity(intent);
        }
    }

    class picksetting_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent(getActivity(), usersettingActivity.class);
            startActivity(intent);
        }
    }

//    private class SendInfoTaskmyuserinform extends
//            AsyncTask<TaskParams, Void, String> {
//
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            if (result == null) {
//                CustomToast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
//            } else {
//                super.onPostExecute(result);
//                result = result.replace("\n ", "");
//                result = result.replace("\n", "");
//                result = result.replace(" ", "");
//                appUserStr = result;
//                result = "[" + result + "]";
//                // 解析json字符串获得List<Map<String,Object>>
//                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
//                for (Map<String, Object> map : lists) {
//                    String datastr = map.get("data")+"";
//                    List<Map<String, Object>> datalists = JsonTools.listKeyMaps("[" + datastr + "]");
//                    for (Map<String, Object> datamap : datalists) {
//
//                        // 这个可以不要的 但是在一个不知道的情况下可能会报错
//                        if(datamap == null || datamap.get("uname") == null){
//                            return;
//                        }
//                        unamestr = datamap.get("uname")+"";
//                        avatar_middlestr = datamap.get("avatar_middle")+"";
//                        String Userdatastr = datamap.get("Userdata")+"";
//                        List<Map<String, Object>> Userdatastrlists = JsonTools.listKeyMaps("[" + Userdatastr + "]");
//
//                        for (Map<String, Object> Userdatamap : Userdatastrlists) {
//                            String following_countstr = "";
//                            String follower_countstr = "";
//                            String weibo_countstr = "";
//
//                            if (Userdatamap.get("following_count") == null) {
//                                following_countstr = "0";
//                            } else {
//                                following_countstr = Userdatamap.get("following_count")+"";
//                            }
//
//                            if (Userdatamap.get("follower_count") == null) {
//                                follower_countstr = "0";
//                            } else {
//                                follower_countstr = Userdatamap.get("follower_count")+"";
//                            }
//
//                            if (Userdatamap.get("weibo_count") == null) {
//                                weibo_countstr = "0";
//                            } else {
//                                weibo_countstr = Userdatamap.get("weibo_count")+"";
//                            }
//                            following_count1.setText(following_countstr);
//                            follower_count1.setText(follower_countstr);
//                            weibo_count1.setText(weibo_countstr);
//                        }
//
//                        sexstr = datamap.get("sex")+"";
//                        if (datamap.get("intro") == null) {
//                            introstr = "暂无简介";
//                        } else {
//                            introstr = datamap.get("intro")+"";
//                        }
//                        try {
//                            if ("1".equals(sexstr)) {
//                                usersex1.setImageResource(R.mipmap.nan);
//                            } else {
//                                usersex1.setImageResource(R.mipmap.nv);
//                            }
//                            //设置用户名
//                            username2.setText(unamestr);
//                            userintro1.setText(introstr);
//                            ImageLoader.getInstance().displayImage(avatar_middlestr,
//                                    headimg2);
//
//                        } catch (Exception e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                        ViewUtil.setUpVip(datamap.get("user_group") + "", vipImg);
//                    }
//                }
//            }
//        }
//    }

    /**
     * 线程更新Ui
     */
    private Handler handler = new Handler() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                  setData(appUserStr);
                    break;
            }
        }
    };

    /**
     * 设置显示数据
     */
    private void setData(String data){
        // 博主的相关信息
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                return;
            }
            mCache.put("AppUserx", data);
            UserEntity userEntity = JSON.parseObject(jsonObject.getString("data"),UserEntity.class);
            String unamestr = userEntity.getUname();
            following_count1.setText(userEntity.getFollowing_count());
            follower_count1.setText(userEntity.getFollower_count());
            weibo_count1.setText(userEntity.getWeibo_count());
            String introstr = userEntity.getIntro();
            if (introstr == null || "".equals(introstr)) {
                introstr = "暂无简介";
            }
            if ("1".equals(userEntity.getSex())) {
                usersex1.setImageResource(R.mipmap.nan);
            } else {
                usersex1.setImageResource(R.mipmap.nv);
            }
            username2.setText(unamestr);
            userintro1.setText(introstr);
            ImageLoader.getInstance().displayImage(userEntity.getAvatar_middle(),
                    headimg2, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
            ViewUtil.setUpVipNew(userEntity.getUser_group_icon_url(), vipImg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
