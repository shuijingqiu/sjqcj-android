package com.example.sjqcjstock.view.stocks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjqcjstock.Activity.stocks.SubscribeConfirmActivity;
import com.example.sjqcjstock.Activity.stocks.UserDetailNewActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.Utils;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 订阅选择页面
 * Created by Administrator on 2016/12/28.
 */
public class SubscribeChoicePopup extends PopupWindow{

    private UserDetailNewActivity context;
    private View mMenuView;
    // 取消按钮
    private TextView cancelTv;
    // 取消订阅
    private TextView cancelSubscribeTv;
    // 延长日
    private TextView ycdyTv;
    // 到期日
    private TextView dqrTv;
    // 延长订阅
    private LinearLayout extendSubscribeTv;
    // 调用取消订阅接口返回的数据
    private String rest;
    // 需订阅人的uid
    private String uid;
    // 订阅的ID
    private String desertId;
    // 名称
    private String name;
    // 时间
    private String time;
    // 是否可延长订阅
    private String isExtend;

    public SubscribeChoicePopup(final UserDetailNewActivity context, HashMap<String,String> map) {
        super(context);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.activity_subscribe_choice, null);
        cancelTv = (TextView) mMenuView.findViewById(R.id.cancel_tv);
        cancelSubscribeTv = (TextView) mMenuView.findViewById(R.id.cancel_subscribe_tv);
        ycdyTv = (TextView) mMenuView.findViewById(R.id.ycdy_tv);
        dqrTv = (TextView) mMenuView.findViewById(R.id.dqr_tv);
        extendSubscribeTv = (LinearLayout) mMenuView.findViewById(R.id.extend_subscribe_ll);

        // 计算屏幕的高宽
        WindowManager wm= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics= new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
//        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setWidth(metrics.widthPixels);
        //设置SelectPicPopupWindow弹出窗体的高
//        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(metrics.heightPixels);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.argb(0, 0, 0, 0));
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y=(int) event.getY();
                if(event.getAction()== MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });
        //取消按钮
        cancelTv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });
        name = map.get("name");
        time = map.get("time");
        uid = map.get("uid");
        desertId = map.get("id");
        isExtend = map.get("isExtend");
        try {
            dqrTv.setText("（"+Utils.CalculatedDays(time)+"天后到期）");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if ("1".equals(isExtend)){
            ycdyTv.setTextColor(ycdyTv.getResources().getColor(R.color.color_text1));
            dqrTv.setTextColor(dqrTv.getResources().getColor(R.color.color_text1));
        }else{
            ycdyTv.setTextColor(ycdyTv.getResources().getColor(R.color.color_text2));
            dqrTv.setTextColor(dqrTv.getResources().getColor(R.color.color_text2));

        }
        cancelSubscribeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 开线程取消订阅
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List dataList = new ArrayList();
                        dataList.add(new BasicNameValuePair("uid", Constants.staticmyuidstr));
                        dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                        dataList.add(new BasicNameValuePair("id", desertId));
                        rest = HttpUtil.restHttpPost(Constants.moUrl + "/desert/cancel", dataList);
                        handler.sendEmptyMessage(0);
                    }
                }).start();
            }
        });
        extendSubscribeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("-1".equals(isExtend)){
                    // 不能进行延长订阅
                    return;
                }
                // 跳转到订阅确认页面
                Intent intent = new Intent();
                intent.putExtra("name", name);
                intent.putExtra("time", time);
                intent.putExtra("uid", uid);
                intent.putExtra("type", "1");
                intent.setClass(context, SubscribeConfirmActivity.class);
                context.startActivity(intent);
                dismiss();
            }
        });
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
                        JSONObject jsonObject = new JSONObject(rest);
                        Toast.makeText(context, jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                        if ("failed".equals(jsonObject.getString("status"))) {

                        }else{
                            context.udpateSubscribeTx();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dismiss();
                    break;
            }
        }
    };
}
