package com.example.sjqcjstock.app;

import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;
import java.util.List;

//设置退出app的全局变量

/**
 * 用一个链表来存放所有创建了的activity.而在退出时将链表中的activity全部finish(),
 * 这样就实现了完全退出功能，而且不影响返回键功能
 */
public class ExitApplication extends Application {

    private List<Activity> list = new ArrayList<Activity>();

    private static ExitApplication ea;

    private ExitApplication() {
        // TODO Auto-generated constructor stub
    }

    //单例创建对象
    public static ExitApplication getInstance() {

        if (null == ea) {
            ea = new ExitApplication();
        }
        return ea;

    }

    //往链表里添加activity

    public void addActivity(Activity activity) {
        list.add(activity);
    }

    //退出时finish 所有的activity

//	public void exit(Context context) {
//		for(int i=0;i<list.size()-1;i++){
//			list.get(i).finish();
//		}
//	}

    public void exit() {
        for (Activity activity : list) {
            activity.finish();
        }
        System.exit(0);
    }
}
