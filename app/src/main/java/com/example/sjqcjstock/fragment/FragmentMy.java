package com.example.sjqcjstock.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjqcjstock.Activity.ActivityMessage;
import com.example.sjqcjstock.Activity.CrystalCoinActivity;
import com.example.sjqcjstock.Activity.MyAttentionActivity;
import com.example.sjqcjstock.Activity.aboutmyActivityNew;
import com.example.sjqcjstock.Activity.commentshortweiboActivity;
import com.example.sjqcjstock.Activity.myattentionlistActivity;
import com.example.sjqcjstock.Activity.mycollectActivity;
import com.example.sjqcjstock.Activity.myfansActivity;
import com.example.sjqcjstock.Activity.mynoteslistActivity;
import com.example.sjqcjstock.Activity.mysubscribeActivity;
import com.example.sjqcjstock.Activity.stocks.MyDealAccountActivity;
import com.example.sjqcjstock.Activity.stocks.SimulationGameActivity;
import com.example.sjqcjstock.Activity.userinfoeditActivity;
import com.example.sjqcjstock.Activity.userinfoeditActivitynew;
import com.example.sjqcjstock.Activity.usersettingActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.ACache;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.netutil.ViewUtil;
import com.example.sjqcjstock.view.CustomToast;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
import java.util.Map;

/**
 * 关于我的页面
 */
public class FragmentMy extends Fragment {

    // 获取控件
    private Button pickuserinfoedit;
    private RelativeLayout pickuserinfoedit2;
    private LinearLayout pickcollect2;

    private LinearLayout picksetting;
    private LinearLayout picksetting2;
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
    private LinearLayout pickshare1;
    private String unamestr;
    private String sexstr;
    private String introstr;
    private String avatar_middlestr;
    // 缓存用的类
    private ACache mCache;
    // 判断是否是第一次加载数据
    private boolean isFirst = true;
    // 缓存用户水晶币信息用
    private String appUserMoneyStr = "";
    // 缓存用户信息
    private String appUserStr = "";
    private ImageView vipImg;
    // 未读消息条数
    private TextView messageCountTv;

    @Override
    public void onResume() {
        super.onResume();
        getItemsal();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, null);
        // 缓存类
        mCache = ACache.get(getActivity());
        initView(view);
        // setMessageCount(); // 以后要要的暂时不要
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getItemsal();
        } else {
            mCache.put("AppUserx", appUserStr);
            mCache.put("AppUserMoneyx", appUserMoneyStr);
        }
    }

    /**
     * 设置消息体条数
     */
    public void setMessageCount() {
        if (Constants.unreadCountInfo != null && Constants.unreadCountInfo.getStatus().equals("1")) {
            int total = Constants.unreadCountInfo.getData().getUnread_total();
            String totalStr = total + "";
            if (total > 99) {
                totalStr = "99+";
            }
            messageCountTv.setText(totalStr);
            if (totalStr.equals("0")) {
                messageCountTv.setVisibility(View.GONE);
            } else {
                messageCountTv.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 获取用户信息和水晶币
     */
    private void getItemsal() {
        SendInfoTaskmyuserinform taks = new SendInfoTaskmyuserinform();
        if (isFirst) {
            isFirst = false;
            String userStr = mCache.getAsString("AppUserx");
            if (userStr != null && !userStr.equals("")) {
                taks.onPostExecute(userStr);
            }
        }
        taks.execute(new TaskParams(
                Constants.Url + "?app=public&mod=Profile&act=AppUser",
                new String[]{"mid", Constants.staticmyuidstr}
        ));
    }

    private void initView(View view) {
        pickuserinfoedit = (Button) view.findViewById(R.id.pickuserinfoedit);
        // 关注微博
        ll_myAttention = (LinearLayout) view.findViewById(R.id.ll_myAttention);
        // 我的收藏
        pickcollect2 = (LinearLayout) view.findViewById(R.id.pickcollect2);
        picksetting = (LinearLayout) view.findViewById(R.id.picksetting);
        myattention1 = (LinearLayout) view.findViewById(R.id.myattention1);
        mybyattention1 = (LinearLayout) view.findViewById(R.id.mybyattention1);
        pickuserinfoedit2 = (RelativeLayout) view.findViewById(R.id.pickuserinfoedit2);
        picksetting2 = (LinearLayout) view.findViewById(R.id.picksetting2);
        pickaboutmy1 = (LinearLayout) view.findViewById(R.id.pickaboutmy1);
        myweibo1 = (LinearLayout) view.findViewById(R.id.myweibo1);
        pickshuijinqiu1 = (LinearLayout) view.findViewById(R.id.pickshuijinqiu1);
        pickshare1 = (LinearLayout) view.findViewById(R.id.pickshare1);

        following_count1 = (TextView) view.findViewById(R.id.following_count1);
        follower_count1 = (TextView) view.findViewById(R.id.follower_count1);
        weibo_count1 = (TextView) view.findViewById(R.id.weibo_count1);

        usersex1 = (ImageView) view.findViewById(R.id.usersex1);
        username2 = (TextView) view.findViewById(R.id.username2);
        userintro1 = (TextView) view.findViewById(R.id.userintro1);
        headimg2 = (ImageView) view.findViewById(R.id.headimg2);
        pickaddweibo1 = (ImageView) view.findViewById(R.id.pickaddweibo1);
        vipImg = (ImageView) view.findViewById(R.id.vip_img);

        messageCountTv = (TextView) view.findViewById(R.id.message_count_tv);

        following_count1.setText("0");
        follower_count1.setText("0");
        weibo_count1.setText("0");

        ll_myAttention.setOnClickListener(new ll_myAttention_listener());
        pickuserinfoedit.setOnClickListener(new pickuserinfoedit_listener());
        pickuserinfoedit2.setOnClickListener(new pickuserinfoedit2_listener());
        pickcollect2.setOnClickListener(new pickcollect2_listener());
        picksetting.setOnClickListener(new picksetting_listener());
        myattention1.setOnClickListener(new myattention1_listener());
        mybyattention1.setOnClickListener(new mybyattention1_listener());
        picksetting2.setOnClickListener(new picksetting2_listener());
        pickaboutmy1.setOnClickListener(new pickaboutmy1_listener());
        pickaddweibo1.setOnClickListener(new pickaddweibo1_listener());
        myweibo1.setOnClickListener(new myweibo1_listener());
        pickshuijinqiu1.setOnClickListener(new pickshuijinqiu1_listener());
        pickshare1.setOnClickListener(new pickshare1_listener());

        // 我的订阅单机事件
        view.findViewById(R.id.ll_myOrder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到我的订阅展示页面
                Intent intent = new Intent(getActivity(), mysubscribeActivity.class);
                startActivity(intent);
            }
        });

        /**
         * 我的比赛单机事件
         */
        view.findViewById(R.id.my_match_ll).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SimulationGameActivity.class);
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

    }

    class pickshare1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            CustomToast.makeText(getActivity(), "暂无该功能", Toast.LENGTH_LONG).show();
        }
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
            // TODO Auto-generated method stub
            Intent intent = new Intent(getActivity(), mynoteslistActivity.class);
            // 将头像和用户名传输到我的微博页面
            // intent.putExtra(name, value)
            // intent.putExtra("drawable", drawable);
            intent.putExtra("avatar_middlestr", avatar_middlestr);
            intent.putExtra("unamestr", unamestr);
            startActivity(intent);
        }

    }

    class pickaddweibo1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(getActivity(), commentshortweiboActivity.class);
            startActivity(intent);
        }
    }

    class pickaboutmy1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            //老版“关于我们”界面
            //Intent intent = new Intent(getActivity(), aboutmyActivity.class);

            //新版"关于我们"界面
            Intent intent = new Intent(getActivity(), aboutmyActivityNew.class);
            startActivity(intent);
        }
    }

    class picksetting2_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(getActivity(), usersettingActivity.class);
            startActivity(intent);
        }

    }

    class pickuserinfoedit2_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(getActivity(), userinfoeditActivitynew.class);
            intent.putExtra("unamestr", unamestr);
            intent.putExtra("sexstr", sexstr);
            intent.putExtra("introstr", introstr);
            intent.putExtra("avatar_middlestr", avatar_middlestr);
            startActivity(intent);
        }
    }

    class myattention1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(getActivity(), myattentionlistActivity.class);
            startActivity(intent);
        }
    }

    class mybyattention1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent(getActivity(), myfansActivity.class);
            startActivity(intent);
        }
    }

    class ll_myAttention_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent(getActivity(), MyAttentionActivity.class);
            startActivity(intent);
        }
    }


    class pickuserinfoedit_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent(getActivity(), userinfoeditActivity.class);
            intent.putExtra("unamestr", unamestr);
            intent.putExtra("sexstr", sexstr);
            intent.putExtra("introstr", userintro1.getText().toString());
            intent.putExtra("avatar_middlestr", avatar_middlestr);
            startActivity(intent);
        }
    }

//	class picknote2_listener implements OnClickListener {
//
//		@Override
//		public void onClick(View arg0) {
//			// TODO Auto-generated method stub
//			Intent intent = new Intent(getActivity(), mynoteslistActivity.class);
//
//			startActivity(intent);
//
//		}
//
//	}
//
//	class pickselfselect2_listener implements OnClickListener {
//
//		@Override
//		public void onClick(View arg0) {
//			// TODO Auto-generated method stub
//			Intent intent = new Intent(getActivity(),
//					selfselectstocklistActivity.class);
//			startActivity(intent);
//
//		}
//
//	}
//
//	class combination2_listener implements OnClickListener {
//
//		@Override
//		public void onClick(View arg0) {
//			// TODO Auto-generated method stub
//			Intent intent = new Intent(getActivity(),
//					combinationlistActivity.class);
//			startActivity(intent);
//
//		}
//
//	}

    class pickcollect2_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(getActivity(), mycollectActivity.class);
            startActivity(intent);
        }
    }

    class picksetting_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(getActivity(), usersettingActivity.class);
            startActivity(intent);

        }

    }

    private class SendInfoTaskmyuserinform extends
            AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                CustomToast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
            } else {
                super.onPostExecute(result);
                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                appUserStr = result;
                result = "[" + result + "]";
                // 解析json字符串获得List<Map<String,Object>>
                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
                for (Map<String, Object> map : lists) {
                    String datastr = map.get("data").toString();
                    List<Map<String, Object>> datalists = JsonTools.listKeyMaps("[" + datastr + "]");
                    for (Map<String, Object> datamap : datalists) {
                        unamestr = datamap.get("uname").toString();
                        avatar_middlestr = datamap.get("avatar_middle").toString();
                        String Userdatastr = datamap.get("Userdata").toString();
                        List<Map<String, Object>> Userdatastrlists = JsonTools.listKeyMaps("[" + Userdatastr + "]");

                        for (Map<String, Object> Userdatamap : Userdatastrlists) {
                            String following_countstr = "";
                            String follower_countstr = "";
                            String weibo_countstr = "";

                            if (Userdatamap.get("following_count") == null) {
                                following_countstr = "0";
                            } else {
                                following_countstr = Userdatamap.get("following_count").toString();
                            }

                            if (Userdatamap.get("follower_count") == null) {
                                follower_countstr = "0";
                            } else {
                                follower_countstr = Userdatamap.get("follower_count").toString();
                            }

                            if (Userdatamap.get("weibo_count") == null) {
                                weibo_countstr = "0";
                            } else {
                                weibo_countstr = Userdatamap.get("weibo_count").toString();
                            }
                            following_count1.setText(following_countstr);
                            follower_count1.setText(follower_countstr);
                            weibo_count1.setText(weibo_countstr);
                        }

                        sexstr = datamap.get("sex").toString();
                        if (datamap.get("intro") == null) {
                            introstr = "暂无简介";
                        } else {
                            introstr = datamap.get("intro").toString();
                        }
                        try {
                            if ("1".equals(sexstr)) {
                                usersex1.setImageResource(R.mipmap.nan);
                            } else {
                                usersex1.setImageResource(R.mipmap.nv);
                            }
                            //设置用户名
                            username2.setText(unamestr);
                            userintro1.setText(introstr);
                            ImageLoader.getInstance().displayImage(avatar_middlestr,
                                    headimg2);

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        ViewUtil.setUpVip(datamap.get("user_group") + "", vipImg);
                    }
                }
            }
        }
    }
}
