package com.example.sjqcjstock.netutil;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 一些视图的共通处理方法
 * Created by Administrator on 2016/5/25.
 */
public class ViewUtil {

    /**
     * 判断值设置字体颜色
     *
     * @param tv  需要设置的字体
     * @param str 判断的值
     */
    public static void setViewColor(TextView tv, String str) {
        str = Utils.getNumberFormat(str);
        float floatCount = Float.valueOf(str);
        if (floatCount > 0) {
            tv.setTextColor(Color.RED);
            tv.setText("+" + str + "%");
        } else if (floatCount < 0) {
            tv.setTextColor(Color.rgb(139, 195, 74));
            tv.setText(str + "%");
        } else {
            tv.setTextColor(Color.rgb(51, 51, 51));
            tv.setText(str + "%");
        }
    }


    /**
     * 获取listView动态高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View mView = listAdapter.getView(i, null, listView);
            mView.measure(0, 0);
            totalHeight += mView.getMeasuredHeight() + 15;
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    /**
     * 处理该用户是否是VIP 并委托设置图像
     *
     * @param str
     * @param imageView
     */
    public static void setUpVip(String str, ImageView imageView) {
        if (str == null) {
            return;
        }
        if (str.length() > 10) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
            return;
        }
        str = str.substring(str.lastIndexOf("http"));
        str = str.substring(0, str.indexOf("\""));
        if (str.length() > 10)
            ImageLoader.getInstance().displayImage(str,
                    imageView, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
    }


}
