/*
 * StickChart.java
 * Android-Charts
 *
 * Created by limc on 2011/05/29.
 *
 * Copyright 2011 limc.cn All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.sjqcjstock.view.stocks;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.example.sjqcjstock.entity.stocks.ListStickEntity;
import com.example.sjqcjstock.entity.stocks.StickEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * StickChart是在GridChart上绘制柱状数据的图表、如果需要支持显示均线，请参照 MAStickChart。
 * </p>
 *
 * @author menghuan
 * @version v1.0 2013/12/25 14:58:59
 */
public class StickChart extends LineChart {


    /**
     * <p>
     * 柱条的最大表示数
     * </p>
     */
    private int maxSticksNum;

    /**
     * <p>
     * Y的最大表示值
     * </p>
     */
    protected float maxValue;

    /**
     * <p>
     * Y的最小表示值
     * </p>
     */
    protected float minValue;

    /**
     * <p>
     * 绘制柱条的属性值数据
     * </p>
     */
    private ListStickEntity StickData;

    /**
     * <p>
     * 柱条之间的间距
     * </p>
     */
    private float stickSpacing = 2f;

    /**
     * <p>
     * 柱条之间是否有边框
     * </p>
     */
    private boolean isStickrame = false;

    /**
     * (non-Javadoc)
     *
     * @param context
     * @see cn.limc.androidcharts.view.BaseChart#BaseChart(Context)
     */
    public StickChart(Context context) {
        super(context);
    }

    /**
     * (non-Javadoc)
     *
     * @param context
     * @param attrs
     * @param defStyle
     * @see cn.limc.androidcharts.view.BaseChart#BaseChart(Context,
     * AttributeSet, int)
     */
    public StickChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * (non-Javadoc)
     *
     * @param context
     * @param attrs
     * @see cn.limc.androidcharts.view.BaseChart#BaseChart(Context,
     * AttributeSet)
     */
    public StickChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * (non-Javadoc)
     * <p/>
     * <p>绘制图表时调用<p>
     *
     * @param canvas
     * @see android.view.View#onDraw(Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        initAxisY();
        initAxisX();
        super.onDraw(canvas);
        drawSticks(canvas);
        if (super.isDisplayCrossXOnTouch() || super.isDisplayCrossYOnTouch()) {
            drawWithFingerClick(canvas);
        }

    }
//
//	/**
//	 * (non-Javadoc)
//	 *
//	 * @param value
//	 *
//	 * @see cn.limc.androidcharts.view.GridChart#getAxisXGraduate(Object)
//	 */
//	@Override
//	public String getAxisXGraduate(Object value) {
//		float graduate = Float.valueOf(super.getAxisXGraduate(value));
//		int index = (int) Math.floor(graduate * maxSticksNum);
//		if (index >= maxSticksNum) {
//			index = maxSticksNum - 1;
//		} else if (index < 0) {
//			index = 0;
//		}
//		return String.valueOf(StickData.getStickData().get(index).getDate());
//	}

    /**
     * (non-Javadoc)
     *
     * @param value
     * @see cn.limc.androidcharts.view.GridChart#getAxisYGraduate(Object)
     */
    @Override
    public String getAxisYGraduate(Object value) {
        float graduate = Float.valueOf(super.getAxisYGraduate(value));
        return String.valueOf((int) Math.floor(graduate * (maxValue - minValue)
                + minValue));
    }

    /**
     * (non-Javadoc)
     */
    @Override
    public void notifyEvent(GridChart chart) {
        CandleStickChart candlechart = (CandleStickChart) chart;

        this.maxSticksNum = candlechart.getMaxSticksNum();

        super.setDisplayCrossYOnTouch(false);
        // notifyEvent
        super.notifyEvent(chart);
        // notifyEventAll
        super.notifyEventAll(this);
    }

    /**
     * <p>
     * 初始化Y轴的坐标值
     * </p>
     */
    protected void initAxisX() {
        if (super.getAxisXTitles() != null)
            return;
        List<String> TitleX = new ArrayList<String>();
        if (null != StickData) {
            float average = maxSticksNum / this.getLongitudeNum();
            for (int i = 0; i < this.getLongitudeNum(); i++) {
                int index = (int) Math.floor(i * average);
                if (index > maxSticksNum - 1) {
                    index = maxSticksNum - 1;
                }
                TitleX.add(String.valueOf(StickData.getStickData().get(index).getDate())
                        .substring(4));
            }
            TitleX.add(String
                    .valueOf(StickData.getStickData().get(maxSticksNum - 1).getDate())
                    .substring(4));
        }
        super.setAxisXTitles(TitleX);
    }

    /**
     * <p>
     * 获取当前选中的柱条的index
     * </p>
     *
     * @return int
     * <p>
     * index
     * </p>
     */
    public int getSelectedIndex() {
        if (null == super.getTouchPoint()) {
            return 0;
        }
        float graduate = Float.valueOf(super.getAxisXGraduate(super
                .getTouchPoint().x));
        int index = (int) Math.floor(graduate * maxSticksNum);

        if (index >= maxSticksNum) {
            index = maxSticksNum - 1;
        } else if (index < 0) {
            index = 0;
        }

        return index;
    }

    /**
     * <p>
     * 初始化Y轴的坐标值
     * </p>
     */
    protected void initAxisY() {
        if (super.getAxisYTitles() != null)
            return;
        List<String> TitleY = new ArrayList<String>();
        float average = (int) ((maxValue - minValue) / this.getLatitudeNum()) / 100 * 100;
        // calculate degrees on Y axis
        for (int i = 0; i < this.getLatitudeNum(); i++) {
            String value = String.valueOf((int) Math.floor(minValue + i
                    * average));
            if (value.length() < super.getAxisYMaxTitleLength()) {
                while (value.length() < super.getAxisYMaxTitleLength()) {
                    value = new String(" ") + value;
                }
            }
            TitleY.add(value);
        }
        // calculate last degrees by use max value
        String value = String.valueOf((int) Math
                .floor(((int) maxValue) / 100 * 100));
        if (value.length() < super.getAxisYMaxTitleLength()) {
            while (value.length() < super.getAxisYMaxTitleLength()) {
                value = new String(" ") + value;
            }
        }
        TitleY.add(value);

        super.setAxisYTitles(TitleY);
    }

    /**
     * <p>
     * 绘制柱条
     * </p>
     *
     * @param canvas
     */
    protected void drawSticks(Canvas canvas) {
        if (StickData == null) {
            return;
        }
        float stickWidth = 0.f;
        //竖画柱条的宽度
        stickWidth = ((super.getWidth() - super.getAxisMarginLeft() - super
                .getAxisMarginRight()) / maxSticksNum);
        float stickX = super.getAxisMarginLeft() + stickSpacing;

        Paint mPaintStick = new Paint();
        // 消除锯齿
        mPaintStick.setAntiAlias(true);
        float highY = 0.f;
        float lowY = 0.f;
        List<StickEntity> stickList = StickData.getStickData();

        int count = stickList.size() - maxSticksNum;
        if (count < 0) count = 0;
        for (int i = count; i < stickList.size(); i++) {
            //柱条的最高值
            double getHigh = 0.0;
            if (stickList.get(i).getType() == 1) {
                //设置柱状图填充颜色
                mPaintStick.setColor(StickData.getStickFillColor());
            } else {
                //设置柱状图填充颜色
                mPaintStick.setColor(StickData.getStickFillColor2());
            }
            mPaintStick.setStyle(Paint.Style.FILL);
            mPaintStick.setAlpha(StickData.getStickAlpha());
            StickEntity ohlc = StickData.getStickData().get(i);
            getHigh = getHigh + ohlc.getHigh();
//			if(!uprightStick)
            highY = (float) ((1f - (getHigh - minValue)
                    / (maxValue - minValue))
                    * (super.getHeight() - super.getAxisMarginBottom() - super.getAxisMarginTop()) + super.getAxisMarginTop());

            lowY = (super.getHeight() - super.getAxisMarginBottom() - super.getAxisMarginTop()) + super.getAxisMarginTop();

//			if(uprightStick)
//			{
//				//绘制柱状
//				canvas.drawRect(stickX, highY, stickX + stickWidth, lowY,
//						mPaintStick);
//				//柱状图是否有边框
//				if(isStickrame)
//				{
//					mPaintStick.setStyle(Paint.Style.STROKE);
//					//设置边框不透明
//					mPaintStick.setAlpha(255);
//					mPaintStick.setColor(StickData.getStickBorderColor());
//					canvas.drawRect(stickX, highY, stickX + stickWidth*i, lowY,
//							mPaintStick);
//				}
//			}
//			else
//			{
            //绘制柱状
            canvas.drawRect(stickX, highY, stickX + stickWidth - 1, lowY,
                    mPaintStick);
            //柱状图是否有边框
            if (isStickrame) {
                mPaintStick.setStyle(Paint.Style.STROKE);
                //设置边框不透明
                mPaintStick.setAlpha(255);
                mPaintStick.setColor(StickData.getStickBorderColor());
                canvas.drawRect(stickX + stickWidth * i, highY, stickX + stickWidth * i, lowY,
                        mPaintStick);
            }
//			}
            stickX += stickWidth;
        }
//			}
    }

    /**
     * <p>
     * 追加一条新数据并刷新当前图表
     * </p>
     *
     * @param entity
     *            <p>
     *            新数据
     *            </p>
     */


    /**
     * @return the maxSticksNum
     */
    public int getMaxSticksNum() {
        return maxSticksNum;
    }

    /**
     * @param maxSticksNum the maxSticksNum to set
     */
    public void setMaxSticksNum(int maxSticksNum) {
        this.maxSticksNum = maxSticksNum;
    }

    /**
     * @return the maxValue
     */
    public float getMaxValue() {
        return maxValue;
    }

    /**
     * @param maxValue the maxValue to set
     */
    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * @return the minValue
     */
    public float getMinValue() {
        return minValue;
    }

    /**
     * @param minValue the minValue to set
     */
    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }

    /**
     * @return the StickData
     */
    public ListStickEntity getStickData() {
        return StickData;
    }

    /**
     * the StickData to set
     */
    public void setStickData(ListStickEntity stickData) {
        StickData = stickData;
    }

    /**
     * @return the stickSpacing
     */
    public float getStickSpacing() {
        return stickSpacing;
    }

    /**
     * @param stickSpacing the stickSpacing to set
     */
    public void setStickSpacing(float stickSpacing) {
        this.stickSpacing = stickSpacing;
    }

    /**
     * @return the isStickrame
     */
    public boolean isStickrame() {
        return isStickrame;
    }

    /**
     * @param isStickrame the isStickrame to set
     */
    public void setStickrame(boolean isStickrame) {
        this.isStickrame = isStickrame;
    }
}
