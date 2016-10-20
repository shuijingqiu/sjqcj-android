package com.example.sjqcjstock.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
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
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.UnreadCount;
import com.example.sjqcjstock.fragment.FragmentForum;
import com.example.sjqcjstock.fragment.FragmentHome;
import com.example.sjqcjstock.fragment.FragmentInform;
import com.example.sjqcjstock.fragment.FragmentMessage;
import com.example.sjqcjstock.fragment.FragmentMy;
import com.example.sjqcjstock.fragment.stocks.FragmentAnalogHome;
import com.example.sjqcjstock.helper.ChangeFragmentHelper;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.view.CustomToast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 框架主体显示页面
 */
public class MainActivity extends FragmentActivity {

    private Fragment currentShowFragment;
    private FragmentHome fragmentHome;
    private FragmentForum fragmentForum;
    private FragmentAnalogHome fragmentAnalogHome;
    private FragmentMessage fragmentMessage;// 以后不要的
    private FragmentInform fragmentInform;
    private FragmentMy fragmentMy;
    private FragmentManager manager;
    private FragmentTransaction fragmentTransaction;
    // 未读消息条数
    private TextView messageCountTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ExitApplication.getInstance().addActivity(this);
        initView();
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 以后要要的
        if (Constants.unreadCountInfo != null) {
            // 返回主框架页面重新设置未读消息条数
            int total = Constants.unreadCountInfo.getData().getUnread_total();
            String totalStr = total + "";
            if (total > 99) {
                totalStr = "99+";
            }
//            messageCountTv.setText(totalStr);
            if (totalStr.equals("0")) {
                messageCountTv.setVisibility(View.GONE);
            } else {
                messageCountTv.setVisibility(View.VISIBLE);
            }
//            if(fragmentMy != null){
//                // 重新设置我的页面的消息条数
//                fragmentMy.setMessageCount();
//            }
        }
    }

    //初始时默认显示的界面
    private void defaultShowFragment() {
        fragmentHome = new FragmentHome();
        currentShowFragment = fragmentHome;
        ChangeFragmentHelper helper = new ChangeFragmentHelper();
        helper.setTargetFragment(fragmentHome);
        helper.setIsClearStackBack(true);
        changeFragment(helper);
    }

    private void initView() {
        messageCountTv = (TextView) findViewById(R.id.message_count_tv);
        RadioGroup main_tabBar = ((RadioGroup) findViewById(R.id.main_tabBar));
        manager = getSupportFragmentManager();
        fragmentTransaction = manager.beginTransaction();

//        if(Constants.intentFlag.equals("1")){
//        	Constants.intentFlag = "";
//
//        	main_tabBar.check(R.id.main_forum);
//
//        	defaultShowFragment(new FragmentForum());
//
//            main_tabBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(RadioGroup group, int checkedId) {
//               	 radioGruopClick(group, checkedId);
//                }
//            });
//
//        } else {

        main_tabBar.check(R.id.main_home);

        //默认显示首页界面
        defaultShowFragment();
        main_tabBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioGruopClick(group, checkedId);
            }
        });
//        }
    }

    // 获取全局所用数据
    private void loadData() {
        // 加载未读消息体的一些信息
        new UnreadCountData().execute(new TaskParams(Constants.unreadCount + "&uid=" + Constants.staticmyuidstr
        ));

    }

    // 获取未读消息条数信息
    private class UnreadCountData extends AsyncTask<TaskParams, Void, String> {
        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackgroundGet(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (result == null) {
                CustomToast.makeText(MainActivity.this, "", Toast.LENGTH_LONG).show();
            } else {
                Constants.unreadCountInfo = JSON.parseObject(result, UnreadCount.class);
                //以后要要的
                int total = Constants.unreadCountInfo.getData().getUnread_total();
                String totalStr = total + "";
                if (total > 99) {
                    totalStr = "99+";
                }
//                messageCountTv.setText(totalStr);
                if (totalStr.equals("0")) {
                    messageCountTv.setVisibility(View.GONE);
                } else {
                    messageCountTv.setVisibility(View.VISIBLE);
                }

                // 以后要替换成下面那个的
                if (fragmentMessage != null) {
                    // 重新设置我的页面的消息条数
                    fragmentMessage.showMessage();
                }

//                if (fragmentMy != null) {
//                    // 重新设置我的页面的消息条数
//                    fragmentMy.setMessageCount();
//                }
            }
        }
    }

    protected void radioGruopClick(RadioGroup group, int checkedId) {
        // TODO Auto-generated method stub
        switch (checkedId) {
            case R.id.main_home:
                if (currentShowFragment != fragmentHome) {
                    if (fragmentHome == null) {
                        fragmentHome = new FragmentHome();
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
                if (currentShowFragment != fragmentMessage) {
                    if (fragmentMessage == null) {
                        fragmentMessage = new FragmentMessage();
                        getSupportFragmentManager().beginTransaction().hide(currentShowFragment).add(R.id.main_container, fragmentMessage).commit();
                        currentShowFragment = fragmentMessage;
                    } else {
                        getSupportFragmentManager().beginTransaction().hide(currentShowFragment).show(fragmentMessage).commit();
                        currentShowFragment = fragmentMessage;
                    }
                    // 重新获取消息数进行显示
                    loadData();
                }
                break;

//                if (currentShowFragment != fragmentAnalogHome) {
//                    if (fragmentAnalogHome == null) {
//                        fragmentAnalogHome = new FragmentAnalogHome();
//                        getSupportFragmentManager().beginTransaction().hide(currentShowFragment).add(R.id.main_container, fragmentAnalogHome).commit();
//                        currentShowFragment = fragmentAnalogHome;
//                    } else {
//                        getSupportFragmentManager().beginTransaction().hide(currentShowFragment).show(fragmentAnalogHome).commit();
//                        currentShowFragment = fragmentAnalogHome;
//                    }
//                }
//                break;

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
                        fragmentMy = new FragmentMy();
                        getSupportFragmentManager().beginTransaction().hide(currentShowFragment).add(R.id.main_container, fragmentMy).commit();
                        currentShowFragment = fragmentMy;
                    } else {
                        getSupportFragmentManager().beginTransaction().hide(currentShowFragment).show(fragmentMy).commit();
                        currentShowFragment = fragmentMy;
                        // 以后要要的
//                        // 重新获取消息数进行显示
//                        loadData();
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
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click(); //调用双击退出函数
        }
        return false;
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
}

