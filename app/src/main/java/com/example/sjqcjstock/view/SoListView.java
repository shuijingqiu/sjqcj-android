package com.example.sjqcjstock.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 自定义的ListView （用于求控件高度）
 * Created by Administrator on 2016/6/12.
 */
public class SoListView extends ListView {
    public SoListView(Context context) {
        super(context);
    }

    public SoListView(Context context, AttributeSet as) {
        super(context, as);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
