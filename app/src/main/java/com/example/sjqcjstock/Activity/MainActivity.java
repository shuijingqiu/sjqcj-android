package com.example.sjqcjstock.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.Activity.user.loginActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.ACache;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.UnreadCount;
import com.example.sjqcjstock.fragment.FragmentForum;
import com.example.sjqcjstock.fragment.FragmentHome;
import com.example.sjqcjstock.fragment.FragmentInform;
import com.example.sjqcjstock.fragment.FragmentMy;
import com.example.sjqcjstock.fragment.stocks.FragmentAnalogHome;
import com.example.sjqcjstock.helper.ChangeFragmentHelper;
import com.example.sjqcjstock.netutil.HttpUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 框架主体显示页面
 */
public class MainActivity extends FragmentActivity {

    public static MainActivity mainActivity;
    private Fragment currentShowFragment;
    private FragmentHome fragmentHome;
    private FragmentForum fragmentForum;
    private FragmentAnalogHome fragmentAnalogHome;
    private FragmentInform fragmentInform;
    private FragmentMy fragmentMy;
    private FragmentManager manager;
    private FragmentTransaction fragmentTransaction;
    private RadioGroup main_tabBar;
    // 未读消息条数
    private TextView messageCountTv;
    // 接口返回的消息条数
    private String resstr;
    // 是否注册极光推送别名(每次进来只注册一次)
    private boolean isRn = true;
    // 登陆接口返回数据
    private String loginStr;
    // 消息条数接口返回数据
    private String messageStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ExitApplication.getInstance().addActivity(this);
        initView();
        // menghuan先不获取token直接进入首页
        // 用户登陆了再去获取数据
        if (Constants.isLogin) {
            // 开定时器获取Token
            obtainToken();
        }
        mainActivity = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 以后要要的
        if (Constants.unreadCountInfo != null) {
            //返回主框架页面重新设置未读消息条数
            int total = Constants.unreadCountInfo.getData().getUnread_total();
            String totalStr = total + "";
            if (totalStr.equals("0") && Constants.unreadCountInfo.getNr_count() == 0 && Constants.unreadCountInfo.getZb_count() == 0) {
                messageCountTv.setVisibility(View.GONE);
            } else {
                messageCountTv.setVisibility(View.VISIBLE);
            }
            if (fragmentMy != null) {
                // 重新设置我的页面的消息条数
                fragmentMy.setMessageCount();
            }
        }
    }

    //初始时默认显示的界面
    private void defaultShowFragment() {
        fragmentHome = new FragmentHome(this);
        currentShowFragment = fragmentHome;
        ChangeFragmentHelper helper = new ChangeFragmentHelper();
        helper.setTargetFragment(fragmentHome);
        helper.setIsClearStackBack(true);
        changeFragment(helper);
    }

    private void initView() {
        main_tabBar = ((RadioGroup) findViewById(R.id.main_tabBar));
        manager = getSupportFragmentManager();
        fragmentTransaction = manager.beginTransaction();
        messageCountTv = (TextView) findViewById(R.id.message_count_tv);
        main_tabBar.check(R.id.main_home);
        //默认显示首页界面
        defaultShowFragment();
        main_tabBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioGruopClick(group, checkedId);
            }
        });
    }

    // 获取全局所用数据
    private void loadData() {
        // menghuan 先不要登陆 不登录也可以看
        // 用户登陆了再去获取数据
        if (Constants.isLogin) {
            // 加载未读消息体的一些信息
            new Thread(new Runnable() {
                @Override
                public void run() {
                    messageStr = HttpUtil.restHttpGet(Constants.newUrl + "/api/message/unread?token=" + Constants.apptoken + "&mid=" + Constants.staticmyuidstr);
                    handler.sendEmptyMessage(2);
                }
            }).start();

            // 获取牛人动态未读消息条数
            new Thread(new Runnable() {
                @Override
                public void run() {
                    resstr = HttpUtil.restHttpGet(Constants.moUrl + "/message/unread&token=" + Constants.apptoken + "&uid=" + Constants.staticmyuidstr);
                    handler.sendEmptyMessage(0);
                }
            }).start();
        }
    }

    protected void radioGruopClick(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.main_home:
                if (currentShowFragment != fragmentHome) {
                    if (fragmentHome == null) {
                        fragmentHome = new FragmentHome(this);
                        getSupportFragmentManager().beginTransaction().hide(currentShowFragment).add(R.id.main_container, fragmentHome).commit();
                        currentShowFragment = fragmentHome;
                    } else {
                        getSupportFragmentManager().beginTransaction().hide(currentShowFragment).show(fragmentHome).commit();
                        currentShowFragment = fragmentHome;
                    }
                }
                break;
            case R.id.main_forum:
                if (currentShowFragment != fragmentForum) {
                    if (fragmentForum == null) {
                        fragmentForum = new FragmentForum();
                        getSupportFragmentManager().beginTransaction().hide(currentShowFragment).add(R.id.main_container, fragmentForum).commit();
                        currentShowFragment = fragmentForum;
                        // 设置为不刷新 其他地方控制刷新了
                        Constants.isreferforumlist = "1";
                    } else {
                        getSupportFragmentManager().beginTransaction().hide(currentShowFragment).show(fragmentForum).commit();
                        currentShowFragment = fragmentForum;
                    }
                }
                break;
            case R.id.main_match:
                if (currentShowFragment != fragmentAnalogHome) {
                    if (fragmentAnalogHome == null) {
                        fragmentAnalogHome = new FragmentAnalogHome();
                        getSupportFragmentManager().beginTransaction().hide(currentShowFragment).add(R.id.main_container, fragmentAnalogHome).commit();
                        currentShowFragment = fragmentAnalogHome;
                    } else {
                        getSupportFragmentManager().beginTransaction().hide(currentShowFragment).show(fragmentAnalogHome).commit();
                        currentShowFragment = fragmentAnalogHome;
                    }
                }
                break;
            case R.id.main_inform:
                if (currentShowFragment != fragmentInform) {
                    if (fragmentInform == null) {
                        fragmentInform = new FragmentInform();
                        getSupportFragmentManager().beginTransaction().hide(currentShowFragment).add(R.id.main_container, fragmentInform).commit();
                        currentShowFragment = fragmentInform;
                    } else {
                        getSupportFragmentManager().beginTransaction().hide(currentShowFragment).show(fragmentInform).commit();
                        currentShowFragment = fragmentInform;
                    }
                }
                break;

            case R.id.main_my:
                if (currentShowFragment != fragmentMy) {
                    if (fragmentMy == null) {
                        fragmentMy = new FragmentMy(messageCountTv);
                        getSupportFragmentManager().beginTransaction().hide(currentShowFragment).add(R.id.main_container, fragmentMy).commit();
                        currentShowFragment = fragmentMy;
                    } else {
                        getSupportFragmentManager().beginTransaction().hide(currentShowFragment).show(fragmentMy).commit();
                        currentShowFragment = fragmentMy;
                        // 以后要要的
                        // 重新获取消息数进行显示
                        loadData();
                    }
                }
                break;
        }
    }

    public void changeFragment(ChangeFragmentHelper helper) {
        //获取需要跳转的Fragment
        Fragment targetFragment = helper.getTargetFragment();
        //获取是否清空回退栈
        boolean clearStackBack = helper.isClearStackBack();
        //获取是否加入回退栈
        String targetFragmentTag = helper.getTargetFragmentTag();
        //获取Bundle
        Bundle bundle = helper.getBundle();
        //是否给Fragment传值
        if (bundle != null) {
            targetFragment.setArguments(bundle);
        }
        if (targetFragment != null) {
            //将目标Fragment添加到容器中
            fragmentTransaction.replace(R.id.main_container, targetFragment);
        }
        //是否添加到回退栈
        if (targetFragmentTag != null) {
            fragmentTransaction.addToBackStack(targetFragmentTag);
        }
        //是否清空回退栈
        if (clearStackBack) {
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        fragmentTransaction.commit();
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            exitBy2Click(); //调用双击退出函数
            // 把返回键当HOME键来处理
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.addCategory(Intent.CATEGORY_HOME);
            startActivity(i);
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            finish();
            ExitApplication.getInstance().exit();
        }
    }

    /**
     * 调用股票的按钮事件
     */
    public void clickStocks() {
        main_tabBar.check(R.id.main_match);
        radioGruopClick(null, R.id.main_match);
    }

    /**
     * 定时获取token(根据账号密码or第三方的token)
     */
    private void obtainToken() {
        // 当前用户类型
        final String type = Constants.getStaticLoginType();
        final String login_email = getSharedPreferences("userinfo", MODE_PRIVATE).getString("login_email", "");
        Constants.timer = new Timer();
        // 开定时器获取数据
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // 没有网络就不进行请求
                if (!HttpUtil.isNetworkAvailable(MainActivity.this)) {
                    return;
                }
                String url = "";
                if ("qq".equals(type)) {
                    // 第三方登陆
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            List dataList = new ArrayList();
                            // 第三分token
                            dataList.add(new BasicNameValuePair("tokey", Constants.statictokeystr));
                            // 类型
                            dataList.add(new BasicNameValuePair("type", "qq"));
                            loginStr = HttpUtil.restHttpPost(Constants.newUrl + "/api/login/thirdParty", dataList);
                            handler.sendEmptyMessage(1);
                        }
                    }).start();

                } else if ("weixin".equals(type)) {
                    // 第三方登陆
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            List dataList = new ArrayList();
                            // 第三分token
                            dataList.add(new BasicNameValuePair("tokey", Constants.statictokeystr));
                            // 类型
                            dataList.add(new BasicNameValuePair("type", "weixin"));
                            loginStr = HttpUtil.restHttpPost(Constants.newUrl + "/api/login/thirdParty", dataList);
                            handler.sendEmptyMessage(1);
                        }
                    }).start();
                } else {
                    //用户登陆
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            List dataList = new ArrayList();
                            // 用户名
                            dataList.add(new BasicNameValuePair("uname", login_email));
                            // 密码
                            dataList.add(new BasicNameValuePair("password", Constants.staticpasswordstr));
                            loginStr = HttpUtil.restHttpPost(Constants.newUrl + "/api/login", dataList);
                            handler.sendEmptyMessage(1);
                        }
                    }).start();
                }
            }
        };
        Constants.timer.schedule(task, 1, 1000 * 60 * 30); // 10ms后执行task,经过0.5h再次执行
//        Constants.timer.schedule(task, 10, 1000*60); // 10ms后执行task,经过0.5h再次执行

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
                        if ("failed".equals(jsonObject.getString("status"))) {
                            return;
                        }
                        Constants.unreadCountInfo.setNr_count(jsonObject.getInt("data"));
                        Constants.unreadCountInfo.setZb_count(jsonObject.getInt("live_count"));
                        int total = Constants.unreadCountInfo.getData().getUnread_total();
                        String totalStr = total + "";
                        if (totalStr.equals("0") && Constants.unreadCountInfo.getNr_count() == 0 && Constants.unreadCountInfo.getZb_count() == 0) {
                            messageCountTv.setVisibility(View.GONE);
                        } else {
                            messageCountTv.setVisibility(View.VISIBLE);
                        }
                        if (fragmentMy != null) {
                            // 重新设置我的页面的消息条数
                            fragmentMy.setMessageCount();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(loginStr);
                        String code = jsonObject.getString("code");
                        // 没有登录成功
                        if (!Constants.successCode.equals(code)) {
                            // 登录不成功退出程序到登陆页面
                            Intent intent = new Intent(MainActivity.this, loginActivity.class);
                            startActivity(intent);
                            ExitApplication.getInstance().exit();
                        } else {
                            JSONObject jsonData = new JSONObject(jsonObject.getString("data"));
                            String token = jsonData.getString("token");
                            // 改这个是为了登陆不要token 也可以用
                            // *--------------------------*
                            Constants.apptoken = token;
                            // 登陆成功了然后再去获取消息条数
                            loadData();
                            // 获取缓存是否接收推送
                            String isPush = ACache.get(MainActivity.this).getAsString("isPush");
                            if (isRn && !"false".equals(isPush)) {
                                // 登陆后开启推送服务）
                                JPushInterface.resumePush(MainActivity.this);
                                // 极光推送为用户添加别名（重复设置为覆盖）Uid作为别名
                                JPushInterface.setAliasAndTags(MainActivity.this, Constants.staticmyuidstr, null, new TagAliasCallback() {
                                    @Override
                                    public void gotResult(int i, String s, Set<String> set) {
                                        // i=0 为成功
                                    }
                                });
                                isRn = false;
                            }
                            // *--------------------------*
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        JSONObject jsonObject = new JSONObject(messageStr);
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            return;
                        }
                        UnreadCount unreadCount = JSON.parseObject(messageStr, UnreadCount.class);
                        Constants.unreadCountInfo.setData(unreadCount.getData());
                        //以后要要的
                        int total = Constants.unreadCountInfo.getData().getUnread_total();
                        String totalStr = total + "";
                        if (totalStr.equals("0") && Constants.unreadCountInfo.getNr_count() == 0 && Constants.unreadCountInfo.getZb_count() == 0) {
                            messageCountTv.setVisibility(View.GONE);
                        } else {
                            messageCountTv.setVisibility(View.VISIBLE);
                        }
                        if (fragmentMy != null) {
                            // 重新设置我的页面的消息条数
                            fragmentMy.setMessageCount();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };
}

