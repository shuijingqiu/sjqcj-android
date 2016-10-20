package com.example.sjqcjstock.Widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.sjqcjstock.R;

public class CustomPopupWindow extends PopupWindow {

    private View view;
    private final ImageView iv_dialogDismiss;
    private final EditText et_inputCount;
    private final TextView tv_restCount;
    private final Button bt_ok;

    public CustomPopupWindow(final Context context) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.dialog_sang, null);

        iv_dialogDismiss = (ImageView) view.findViewById(R.id.iv_dialogDismiss);
        et_inputCount = (EditText) view.findViewById(R.id.et_inputCount);
        tv_restCount = (TextView) view.findViewById(R.id.tv_restCount);
        bt_ok = (Button) view.findViewById(R.id.bt_ok);

        iv_dialogDismiss.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });

        bt_ok.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dismiss();
                //提交数据到服务器
            }
        });

        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle();
        //this.setAnimationStyle(R.style.popwin_anim_style);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        this.setOutsideTouchable(true);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框       
    }
}