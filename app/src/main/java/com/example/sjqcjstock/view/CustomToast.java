package com.example.sjqcjstock.view;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;

/**
 * 重写Toast
 * Created by Administrator on 2016/5/17.
 */
public class CustomToast extends Toast {
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public CustomToast(Context context) {

        super(context);
    }

    public static Toast makeText(Context context, String text, int duration) {
        Toast result = new Toast(context);
        //获取LayoutInflater对象
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //由layout文件创建一个View对象
        View layout = inflater.inflate(R.layout.custom_toast, null);
        TextView textView = (TextView) layout.findViewById(R.id.text0);
        if ("".equals(text)) {
            if (HttpUtil.isNetworkAvailable(context)) {
                text = Constants.noData;
            } else {
                text = Constants.noNetwork;
            }
        }
        textView.setText(text);
        result.setView(layout);
        result.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        // 设置持续时间
        result.setDuration(Toast.LENGTH_SHORT);
        return result;
    }

}
