package com.example.sjqcjstock.view;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.entity.NoticeEntity;
import com.example.sjqcjstock.netutil.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 跑马灯自定义事件
 * Created by Administrator on 2017/5/4.
 */
public class MarqueeView extends ViewFlipper {

    private Context mContext;
    private List<NoticeEntity> notices;
    private boolean isSetAnimDuration = false;

    private int interval = 2000;
    private int animDuration = 500;
    private int textSize = 14;
    private int textColor = 0xffffffff;

    /**
     * 点击
     */
    private OnItemClickListener onItemClickListener;

    public MarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        if (notices == null) {
            notices = new ArrayList<NoticeEntity>();
        }

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MarqueeViewStyle, defStyleAttr, 0);
        interval = typedArray.getInteger(R.styleable.MarqueeViewStyle_mvInterval, interval);
        isSetAnimDuration = typedArray.hasValue(R.styleable.MarqueeViewStyle_mvAnimDuration);
        animDuration = typedArray.getInteger(R.styleable.MarqueeViewStyle_mvAnimDuration, animDuration);
        if (typedArray.hasValue(R.styleable.MarqueeViewStyle_mvTextSize)) {
            textSize = (int) typedArray.getDimension(R.styleable.MarqueeViewStyle_mvTextSize, textSize);
            textSize = DisplayUtil.px2sp(mContext, textSize);
        }
        textColor = typedArray.getColor(R.styleable.MarqueeViewStyle_mvTextColor, textColor);
        typedArray.recycle();

        setFlipInterval(interval);

        Animation animIn = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_in);
        if (isSetAnimDuration) animIn.setDuration(animDuration);
        setInAnimation(animIn);

        Animation animOut = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_out);
        if (isSetAnimDuration) animOut.setDuration(animDuration);
        setOutAnimation(animOut);
    }

    // 根据公告字符串启动轮播
    public void startWithText(final NoticeEntity notice) {
        if (TextUtils.isEmpty(notice.getTitle())) return;
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                startWithFixedWidth(notice, getWidth());
            }
        });
    }

    // 根据公告字符串列表启动轮播
    public void startWithList(List<NoticeEntity> notices) {
        setNotices(notices);
        start();
    }

    // 根据宽度和公告字符串启动轮播
    private void startWithFixedWidth(NoticeEntity notice, int width) {
        int noticeLength = notice.getTitle().length();
        int dpW = DisplayUtil.px2dip(mContext, width);
        int limit = dpW/textSize;
        if (dpW == 0) {
            throw new RuntimeException("Please set MarqueeView width !");
        }

        if (noticeLength <= limit) {
            notices.add(notice);
        } else {
            int size = noticeLength/limit + (noticeLength%limit != 0? 1:0);
            for (int i=0; i<size; i++) {
                int startIndex = i*limit;
                int endIndex = ((i+1)*limit >= noticeLength? noticeLength:(i+1)*limit);
//                notices.add(notice.getTitle().substring(startIndex, endIndex));
                notice.setTitle(notice.getTitle().substring(startIndex, endIndex));
                notices.add(notice);
            }
        }
        start();
    }

    // 启动轮播
    public boolean start() {
        if (notices == null || notices.size() == 0) return false;
        removeAllViews();
        int position = 0;
        for (NoticeEntity notice:notices) {
            final TextView tv = createTextView(notice.getTitle());
            final int finalPosition = position;
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(finalPosition, tv);
                    }
                }
            });
            position++;
            addView(tv);
        }
        if (notices.size() > 1) {
            startFlipping();
        }
        return true;
    }

    // 创建ViewFlipper下的TextView
    private TextView createTextView(String text) {
        TextView tv = new TextView(mContext);
        tv.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        tv.setText(text);
        tv.setTextColor(textColor);
        tv.setTextSize(textSize);
        tv.setLines(2);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        return tv;
    }

    public List<NoticeEntity> getNotices() {
        return notices;
    }

    public void setNotices(List<NoticeEntity> notices) {
        this.notices = notices;
    }
    /**
     * 设置监听接口
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * item_view的接口
     */
    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

}
