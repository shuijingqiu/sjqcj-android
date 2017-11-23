package com.example.sjqcjstock.view;

import android.content.Context;
import android.text.Layout;
import android.text.Spannable;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.sjqcjstock.netutil.ViewUtil;

/**
 * TextView 的OnTouchListener
 * Created by Administrator on 2017/6/21.
 */
public class ClickOnTouch implements View.OnTouchListener {

    private Context context;

    public ClickOnTouch(Context context){
        this.context = context;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean ret = false;
        CharSequence text = ((TextView) v).getText();
        Spannable stext = Spannable.Factory.getInstance().newSpannable(text);
        TextView widget = (TextView) v;
        int action = event.getAction();
                if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
//        if (action == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();
            x += widget.getScrollX();
            y += widget.getScrollY();
            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);
            //获取textview里的超链接对象，并用ClickableSpan数组盛装
            ClickableSpan[] link = stext.getSpans(off, off, ClickableSpan.class);
            URLSpan[] linkurl = stext.getSpans(off, off, URLSpan.class);
            if (link.length != 0) { //此处是对超链接点击事件进行处理……
                if (action == MotionEvent.ACTION_UP) {
//                    link[0].onClick(widget);
                    ViewUtil.LinkJump(context,linkurl[0].getURL());

                }
                return true; //处理结束后返回true,这里一定要注意：当返回值为true时则表示点击事件已经被，就再也不会继续传递了
            }
//
//            URLSpan[] link = stext.getSpans(off, off, URLSpan.class);
//                    Log.e("mhcs1：",link.length+"--");
//            if (link.length != 0) {
//                for(final URLSpan span : link) {
//                    Log.e("mhcs3",span.getURL());
//                    ViewUtil.LinkJump(context,span.getURL());
//                }
//            }
        }
        return ret;
    }
}
