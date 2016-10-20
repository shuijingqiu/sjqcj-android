package com.example.sjqcjstock.app;

import android.app.Activity;
import android.app.Application;

import com.example.sjqcjstock.constant.Constants;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class CarApplication extends Application {
    public static IWXAPI api;

    public static void getApi(Activity activity) {


//		api =WXAPIFactory.createWXAPI(activity.getApplicationContext(), "wx6608522223b1dea0", true);
//		
//		api.registerApp("wx6608522223b1dea0");

        api = WXAPIFactory.createWXAPI(activity.getApplicationContext(), Constants.APP_ID, true);

        api.registerApp(Constants.APP_ID);

    }

//	@Override
//	public void onCreate() {
//		// TODO Auto-generated method stub
//		super.onCreate();
//		
//		//注册微信
//											 
//
//		api =WXAPIFactory.createWXAPI(this, "wx6608522223b1dea0", true);
//		
//		api.registerApp("wx6608522223b1dea0");
//	}


}
