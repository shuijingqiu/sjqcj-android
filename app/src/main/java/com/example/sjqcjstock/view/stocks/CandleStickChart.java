/*
 * CandleStickChart.java
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
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.example.sjqcjstock.entity.stocks.OHLCEntity;
import com.example.sjqcjstock.netutil.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * CandleStickChart是在GridChart上绘制K线（蜡烛线）的图表、如果需要支持显示均线，请参照 MACandleStickChart。
 * </p>
 *
 * @author menghuan
 * @version v1.0 2013/12/25 16:29:41
 * @see CandleStickChart
 * @see MACandleStickChart
 */
public class CandleStickChart extends GridChart {
    /**
     * <p>
     * 默认阳线的边框颜色
     * </p>
     */
    public static final int DEFAULT_POSITIVE_STICK_BORDER_COLOR = Color.rgb(235, 35, 65);

    /**
     * <p>
     * 默认阳线的填充颜色
     * </p>
     */
    public static final int DEFAULT_POSITIVE_STICK_FILL_COLOR = Color.rgb(235, 35, 65);// 红色

    /**
     * <p>
     * 默认阴线的边框颜色
     * </p>
     */
    public static final int DEFAULT_NEGATIVE_STICK_BORDER_COLOR = Color.rgb(50, 174, 110);// 绿色

    /**
     * <p>
     * 默认阴线的填充颜色
     * </p>
     */
    public static final int DEFAULT_NEGATIVE_STICK_FILL_COLOR = Color.rgb(50, 174, 110);

    /**
     * <p>
     * 默认十字线显示颜色
     * </p>
     */
    public static final int DEFAULT_CROSS_STAR_COLOR = Color.LTGRAY;

    /**
     * <p>
     * 阳线的边框颜色
     * </p>
     */
    private int positiveStickBorderColor = DEFAULT_POSITIVE_STICK_BORDER_COLOR;

    /**
     * <p>
     * 阳线的填充颜色
     * </p>
     */
    private int positiveStickFillColor = DEFAULT_POSITIVE_STICK_FILL_COLOR;

    /**
     * <p>
     * 阴线的边框颜色
     * </p>
     */

    private int negativeStickBorderColor = DEFAULT_NEGATIVE_STICK_BORDER_COLOR;

    /**
     * <p>
     * 阴线的填充颜色
     * </p>
     */
    private int negativeStickFillColor = DEFAULT_NEGATIVE_STICK_FILL_COLOR;

    /**
     * <p>
     * 十字线显示颜色（十字星,一字平线,T形线的情况）
     * </p>
     */
    private int crossStarColor = DEFAULT_CROSS_STAR_COLOR;

    /**
     * <p>
     * 绘制柱条用的数据
     * </p>
     */
    private List<OHLCEntity> OHLCData;

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
    private float maxValue = 0;

    /**
     * <p>
     * Y的最小表示值
     * </p>
     */
    private float minValue = 0;

    /**
     * (non-Javadoc)
     *
     * @param context
     */
    public CandleStickChart(Context context) {
        super(context);
    }

    /**
     * (non-Javadoc)
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CandleStickChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * (non-Javadoc)
     *
     * @param context
     * @param attrs
     */
    public CandleStickChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * (non-Javadoc)
     * <p/>
     * <p>绘制图表时调用</p>
     *
     * @param canvas
     * @see android.view.View#onDraw(Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        initAxisY();
        initAxisX();
        super.onDraw(canvas);
        drawCandleSticks(canvas);
    }

    /**
     * (non-Javadoc)
     *
     * @param value
     */
    @Override
    public String getAxisXGraduate(Object value) {
        float graduate = Float.valueOf(super.getAxisXGraduate(value));
        int index = (int) Math.floor(graduate * maxSticksNum);
        if (index >= maxSticksNum) {
            index = maxSticksNum - 1;
        } else if (index < 0) {
            index = 0;
        }
        return String.valueOf(OHLCData.get(index).getDate());
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
     * (non-Javadoc)
     *
     * @param value
     */
    @Override
    public String getAxisYGraduate(Object value) {
        float graduate = Float.valueOf(super.getAxisYGraduate(value));
        return String.valueOf((int) Math.floor(graduate * (maxValue - minValue)
                + minValue));
    }

    /**
     * <p>
     * 初始化X轴的坐标值
     * </p>
     */
    protected void initAxisX() {
        List<String> TitleX = new ArrayList<String>();
        if (null != OHLCData) {
            int sticksNum = OHLCData.size();
            if (sticksNum > maxSticksNum) {
                sticksNum = maxSticksNum;
            }
            float average = sticksNum / this.getLongitudeNum();
            String date = "";
            String str = "";
            String strOld = "";
            for (int i = 0; i < this.getLongitudeNum(); i++) {
                int index = (int) Math.floor(i * average);
                if (index > sticksNum - 1) {
                    index = sticksNum - 1;
                }
                date = OHLCData.get(index).getDate();
                str = date.substring(0, 2);
                if (Integer.valueOf(str) > 0 && Integer.valueOf(str) < 50) {
                    date = "20" + date;
                    str = "20" + str;
                } else {
                    date = "19" + date;
                    str = "19" + str;
                }
                date = Utils.getFormatDate(date);
                if (strOld.equals(str)) {
                    TitleX.add(date.substring(5));
                } else {
                    TitleX.add(date);
                }
                strOld = str;
            }
            date = OHLCData.get(sticksNum - 1).getDate();
            str = date.substring(0, 2);
            if (Integer.valueOf(str) > 0 && Integer.valueOf(str) < 50) {
                date = "20" + date;
            } else {
                date = "19" + date;
            }
            date = Utils.getFormatDate(date);
            TitleX.add(date);
        }
        super.setAxisXTitles(TitleX);
    }

    /**
     * <p>
     * 初始化Y轴的坐标值
     * </p>
     */
    protected void initAxisY() {
        List<String> TitleY = new ArrayList<String>();
        float average = ((maxValue - minValue) / this.getLatitudeNum());
        String value = "";
        // calculate degrees on Y axis
        for (int i = 0; i < this.getLatitudeNum() + 1; i++) {
            if (super.getAxisYTitlesR() == null) {
                // 如果内图有标题那么外标题就不显示(或是显示为空)
                value = String.valueOf(minValue + i
                        * average);
                if (value.length() < super.getAxisYMaxTitleLength()) {
                    while (value.length() < super.getAxisYMaxTitleLength()) {
                        value = new String(" ") + value;
                    }
                }
            }
            TitleY.add(value);
        }
        super.setAxisYTitles(TitleY);
    }

    /**
     * <p>
     * 绘制柱条
     * </p>
     *
     * @param canvas
     */
    protected void drawCandleSticks(Canvas canvas) {
        float stickWidth = ((super.getWidth() - super.getAxisMarginLeft() - super
                .getAxisMarginRight()) / maxSticksNum) - 1;
        float stickX = super.getAxisMarginLeft() + 1;
        Paint mPaintPositive = new Paint();
        mPaintPositive.setColor(positiveStickFillColor);
        Paint mPaintNegative = new Paint();
        mPaintNegative.setColor(negativeStickFillColor);
        Paint mPaintCross = new Paint();
        mPaintCross.setColor(crossStarColor);

        if (null != OHLCData) {
            for (int i = 0; i < OHLCData.size(); i++) {
                OHLCEntity ohlc = OHLCData.get(i);
                float openY = (float) ((1f - (ohlc.getOpen() - minValue)
                        / (maxValue - minValue))
                        * (super.getHeight() - super.getAxisMarginBottom() - super.getAxisMarginTop()) + super
                        .getAxisMarginTop());
                float highY = (float) ((1f - (ohlc.getHigh() - minValue)
                        / (maxValue - minValue))
                        * (super.getHeight() - super.getAxisMarginBottom() - super.getAxisMarginTop()) + super
                        .getAxisMarginTop());
                float lowY = (float) ((1f - (ohlc.getLow() - minValue)
                        / (maxValue - minValue))
                        * (super.getHeight() - super.getAxisMarginBottom() - super.getAxisMarginTop()) + super
                        .getAxisMarginTop());
                float closeY = (float) ((1f - (ohlc.getClose() - minValue)
                        / (maxValue - minValue))
                        * (super.getHeight() - super.getAxisMarginBottom() - super.getAxisMarginTop()) + super
                        .getAxisMarginTop());

                if (ohlc.getOpen() < ohlc.getClose()) {
                    // stick or line
                    if (stickWidth >= 2f) {
                        canvas.drawRect(stickX, closeY, stickX + stickWidth,
                                openY, mPaintPositive);
                    }
                    canvas.drawRect(stickX + stickWidth / 2f - 1, highY, stickX
                            + stickWidth / 2f + 1, lowY, mPaintPositive);

                } else if (ohlc.getOpen() > ohlc.getClose()) {
                    // stick or line
                    if (stickWidth >= 2f) {
                        canvas.drawRect(stickX, openY, stickX + stickWidth,
                                closeY, mPaintNegative);
                    }
                    canvas.drawRect(stickX + stickWidth / 2f - 1, highY, stickX
                            + stickWidth / 2f + 1, lowY, mPaintNegative);
                } else {
                    // line or point
                    if (stickWidth >= 2f) {
                        canvas.drawLine(stickX, closeY, stickX + stickWidth,
                                openY, mPaintCross);
                    }
                    canvas.drawLine(stickX + stickWidth / 2f, highY, stickX
                            + stickWidth / 2f, lowY, mPaintCross);
                }
                stickX = stickX + 1 + stickWidth;
            }
        }
    }

    /**
     * <p>
     * 追加一条新数据并刷新当前图表
     * </p>
     *
     * @param entity <p>
     *               新数据
     *               </p>
     */
    public void pushData(OHLCEntity entity) {
        if (null != entity) {
            // 强制重刷新
            super.postInvalidate();
        }
    }

    private final int NONE = 0;
    private final int ZOOM = 1;
    private final int DOWN = 2;

    private float olddistance = 0f;
    private float newdistance = 0f;

    private int TOUCH_MODE;

    /**
     * (non-Javadoc)
     * <p/>
     * <p>图表点击时调用<p>
     *
     * @param event
     * @see android.view.View#onTouchEvent(MotionEvent)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
//		final float MIN_LENGTH = (super.getWidth() / 40) < 5 ? 5 : (super
//				.getWidth() / 50);
//
//		switch (event.getAction() & MotionEvent.ACTION_MASK) {
//		case MotionEvent.ACTION_DOWN:
//			TOUCH_MODE = DOWN;
//			break;
//		case MotionEvent.ACTION_UP:
//		case MotionEvent.ACTION_POINTER_UP:
//			TOUCH_MODE = NONE;
//			return super.onTouchEvent(event);
//		case MotionEvent.ACTION_POINTER_DOWN:
//			olddistance = calcDistance(event);
//			if (olddistance > MIN_LENGTH) {
//				TOUCH_MODE = ZOOM;
//			}
//			break;
//		case MotionEvent.ACTION_MOVE:
//			if (TOUCH_MODE == ZOOM) {
//				newdistance = calcDistance(event);
//				if (newdistance > MIN_LENGTH
//						&& Math.abs(newdistance - olddistance) > MIN_LENGTH) {
//
//					if (newdistance > olddistance) {
//						zoomIn();
//					} else {
//						zoomOut();
//					}
//					olddistance = newdistance;
//					super.postInvalidate();
//					super.notifyEventAll(this);
//				}
//			}
//			break;
//		}
        return true;
    }

    /**
     * <p>
     * 计算两点触控时两点之间的距离
     * </p>
     *
     * @param event
     * @return float
     * <p>
     * 距离
     * </p>
     */
    private float calcDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * <p>
     * 放大表示
     * </p>
     */
    protected void zoomIn() {
        if (maxSticksNum > 10) {
            maxSticksNum = maxSticksNum - 3;
        }
    }

    /**
     * <p>
     * 缩小
     * </p>
     */
    protected void zoomOut() {
        if (maxSticksNum < OHLCData.size() - 1) {
            maxSticksNum = maxSticksNum + 3;
        }
    }

    /**
     * @return the positiveStickBorderColor
     */
    public int getPositiveStickBorderColor() {
        return positiveStickBorderColor;
    }

    /**
     * @param positiveStickBorderColor the positiveStickBorderColor to set
     */
    public void setPositiveStickBorderColor(int positiveStickBorderColor) {
        this.positiveStickBorderColor = positiveStickBorderColor;
    }

    /**
     * @return the positiveStickFillColor
     */
    public int getPositiveStickFillColor() {
        return positiveStickFillColor;
    }

    /**
     * @param positiveStickFillColor the positiveStickFillColor to set
     */
    public void setPositiveStickFillColor(int positiveStickFillColor) {
        this.positiveStickFillColor = positiveStickFillColor;
    }

    /**
     * @return the negativeStickBorderColor
     */
    public int getNegativeStickBorderColor() {
        return negativeStickBorderColor;
    }

    /**
     * @param negativeStickBorderColor the negativeStickBorderColor to set
     */
    public void setNegativeStickBorderColor(int negativeStickBorderColor) {
        this.negativeStickBorderColor = negativeStickBorderColor;
    }

    /**
     * @return the negativeStickFillColor
     */
    public int getNegativeStickFillColor() {
        return negativeStickFillColor;
    }

    /**
     * @param negativeStickFillColor the negativeStickFillColor to set
     */
    public void setNegativeStickFillColor(int negativeStickFillColor) {
        this.negativeStickFillColor = negativeStickFillColor;
    }

    /**
     * @return the crossStarColor
     */
    public int getCrossStarColor() {
        return crossStarColor;
    }

    /**
     * @param crossStarColor the crossStarColor to set
     */
    public void setCrossStarColor(int crossStarColor) {
        this.crossStarColor = crossStarColor;
    }

    /**
     * @return the oHLCData
     */
    public List<OHLCEntity> getOHLCData() {
        return OHLCData;
    }

    /**
     * @param oHLCData the oHLCData to set
     */
    public void setOHLCData(List<OHLCEntity> oHLCData) {
//		if (null != OHLCData) {
//			OHLCData.clear();
//		}
        OHLCData = oHLCData;
    }

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
}
