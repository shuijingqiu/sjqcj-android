package com.example.sjqcjstock.Activity;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.LinearLayout;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.fragment.FragmentForum;
import com.example.sjqcjstock.fragment.FragmentHome;
import com.example.sjqcjstock.fragment.FragmentInform;
import com.example.sjqcjstock.fragment.FragmentMy;
import com.example.sjqcjstock.fragment.stocks.FragmentAnalogHome;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class MainTab extends FragmentActivity implements OnTouchListener, OnGestureListener {

    protected ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 加载菜单
        getMenuInflater().inflate(R.menu.main_menu, menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_clear_memory_cache:
                imageLoader.clearMemoryCache(); // 清除内存缓存
                return true;
            case R.id.item_clear_disc_cache:
                imageLoader.clearDiscCache(); // 清除SD卡中的缓存
                return true;
            default:
                return false;
        }
    }

    private String TAG = MainTab.class.getName();

    public TabFragmentHost mTabHost;
    // 标签
    private String[] TabTag = {"tab1", "tab2", "tab3", "tab4", "tab5"};

    // 自定义tab布局显示文本和顶部的图片
    private Integer[] ImgTab = {R.layout.tab_main_home,
            R.layout.tab_main_forum, R.layout.tab_main_message,
            R.layout.tab_main_inform, R.layout.tab_main_my};

    private Class[] ClassTab = {FragmentHome.class, FragmentForum.class,
            FragmentAnalogHome.class, FragmentInform.class, FragmentMy.class};

    private Integer[] StyleTab = {R.color.color_F0F0F0, R.color.color_F0F0F0,
            R.color.color_F0F0F0, R.color.color_F0F0F0, R.color.color_F0F0F0,
            R.color.color_F0F0F0};

    /**
     * 定义手势检测实例
     */
    public static GestureDetector detector;
    /**
     * 做标签，记录当前是哪个fragment
     */
    public int MARK = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置导航栏不显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.maintabs);

        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);

        // 可以在主线程中使用http网络访问
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        setupView();
        initValue();
        setLinstener();
        fillData();
        // 创建手势检测器
        detector = new GestureDetector(this);
    }

    private void setupView() {
        // tabhost
        // 实例化framentTabHost
        mTabHost = (TabFragmentHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

    }

    private void initValue() {

        // 初始化tab选项卡
        InitTabView();
    }

    private void setLinstener() {
        // imv_back.setOnClickListener(this);

    }

    private void fillData() {
        // TODO Auto-generated method stub

    }

    // 初始化 tab 自定义的选项卡 ///////////////
    private void InitTabView() {

        // 可以传递参数 b;传递公共的userid,version,sid
        Bundle b = new Bundle();
        // 循环加入自定义的tab
        for (int i = 0; i < TabTag.length; i++) {
            // 封装的自定义的tab的样式
            View indicator = getIndicatorView(i);
            mTabHost.addTab(
                    mTabHost.newTabSpec(TabTag[i]).setIndicator(indicator),
                    ClassTab[i], b);// 传递公共参数

        }
        mTabHost.getTabWidget().setDividerDrawable(R.color.white);
    }

    // 设置tab自定义样式:注意 每一个tab xml子布局的linearlayout 的id必须一样
    private View getIndicatorView(int i) {
        // 找到tabhost的子tab的布局视图
        View v = getLayoutInflater().inflate(ImgTab[i], null);
        LinearLayout tv_lay = (LinearLayout) v.findViewById(R.id.layout_back);
        tv_lay.setBackgroundResource(StyleTab[i]);
        return v;
    }

    // Fragment实现触摸屏

    private ArrayList<MyOnTouchListener> onTouchListenners = new ArrayList<MainTab.MyOnTouchListener>(
            10);

    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListenners.add(myOnTouchListener);

    }

    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListenners.remove(myOnTouchListener);

    }

    public interface MyOnTouchListener {
        public boolean onTouch(MotionEvent ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        // 使得触屏效果可用在布局上生效
        if (MARK == 1) {// 使得fragment的平滑触屏制作350dp屏幕以下有效
            if (ev.getY() > 350) {
                detector.onTouchEvent(ev);
            }
            for (MyOnTouchListener listener : onTouchListenners) {
                listener.onTouch(ev);
            }

            return super.dispatchTouchEvent(ev);
        } else {

            detector.onTouchEvent(ev);
            for (MyOnTouchListener listener : onTouchListenners) {
                listener.onTouch(ev);
            }

            return super.dispatchTouchEvent(ev);
        }
    }

    @Override
    public boolean onTouch(View arg0, MotionEvent arg1) {
        // TODO Auto-generated method stub
        // 使得触屏效果可用在布局上生效

        if (MARK == 1) {// 使得fragment的平滑触屏制作350dp屏幕以下有效
            if (arg1.getY() > 350) {
                return detector.onTouchEvent(arg1);

            }
        } else {
            return detector.onTouchEvent(arg1);

        }
        return false;
    }

    @Override
    public boolean onDown(MotionEvent arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
                           float arg3) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onLongPress(MotionEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
                            float arg3) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onShowPress(MotionEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent arg0) {
        // TODO Auto-generated method stub
        return false;
    }
}
