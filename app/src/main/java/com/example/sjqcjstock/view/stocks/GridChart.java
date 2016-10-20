/*
 * GridChart.java
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
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.example.sjqcjstock.entity.stocks.SpotEntity;
import com.example.sjqcjstock.interfaces.ITouchEventNotify;
import com.example.sjqcjstock.interfaces.ITouchEventResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * GridChart是所有网格图表的基础类对象，它实现了基本的网格图表功能，这些功能将被它的继承类使用
 * </p>
 *
 * @author meng
 * @version v1.0 2013/12/20 14:19:50
 */
public class GridChart extends BaseChart implements ITouchEventNotify,
        ITouchEventResponse {

    /**
     * <p>
     * 默认背景色
     * </p>
     */
    public static final int DEFAULT_BACKGROUND_COLOR = Color.WHITE;

    /**
     * <p>
     * 默认坐标轴X的显示颜色
     * </p>
     */
    public static final int DEFAULT_AXIS_X_COLOR = Color.rgb(198, 198, 198);

    /**
     * <p>
     * 默认坐标轴Y的显示颜色
     * </p>
     */
    public static final int DEFAULT_AXIS_Y_COLOR = Color.rgb(198, 198, 198);

    /**
     * <p>
     * 默认网格经线的显示颜色
     * </p>
     */
    public static final int DEFAULT_LONGITUDE_COLOR = Color.rgb(221, 221, 221);

    /**
     * <p>
     * 默认网格纬线的显示颜色
     * </p>
     */
    public static final int DEFAULT_LAITUDE_COLOR = Color.rgb(221, 221, 221);

    /**
     * <p>
     * 默认轴线左边距
     * </p>
     */
    public static final float DEFAULT_AXIS_MARGIN_LEFT = 42f;

    /**
     * <p>
     * 默认轴线下边距
     * </p>
     */
    public static final float DEFAULT_AXIS_MARGIN_BOTTOM = 16f;

    /**
     * <p>
     * 默认轴线上边距
     * </p>
     */
    public static final float DEFAULT_AXIS_MARGIN_TOP = 5f;

    /**
     * <p>
     * 轴线右边距
     * </p>
     */
    public static final float DEFAULT_AXIS_MARGIN_RIGHT = 5f;

    /**
     * <p>
     * 网格纬线的数量
     * </p>
     */
    public static final int DEFAULT_LATITUDE_NUM = 4;

    /**
     * <p>
     * 网格经线的数量
     * </p>
     */
    public static final int DEFAULT_LONGITUDE_NUM = 3;

    /**
     * <p>
     * 默认经线是否显示
     * </p>
     */
    public static final boolean DEFAULT_DISPLAY_LONGITUDE = Boolean.TRUE;

    /**
     * <p>
     * 默认经线是否显示为虚线
     * </p>
     */
    public static final boolean DEFAULT_DASH_LONGITUDE = Boolean.TRUE;

    /**
     * <p>
     * 纬线是否显示
     * </p>
     */
    public static final boolean DEFAULT_DISPLAY_LATITUDE = Boolean.TRUE;

    /**
     * <p>
     * 纬线是否显示为虚线
     * </p>
     */
    public static final boolean DEFAULT_DASH_LATITUDE = Boolean.TRUE;

    /**
     * <p>
     * X轴上的标题是否显示
     * </p>
     */
    public static final boolean DEFAULT_DISPLAY_AXIS_X_TITLE = Boolean.TRUE;

    /**
     * <p>
     * 默认Y轴上的标题是否显示
     * </p>
     */
    public static final boolean DEFAULT_DISPLAY_AXIS_Y_TITLE = Boolean.TRUE;

    /**
     * <p>
     * 默认控件是否显示边框
     * </p>
     */
    public static final boolean DEFAULT_DISPLAY_BORDER = Boolean.FALSE;

    /**
     * <p>
     * 默认经线刻度字体颜色
     * </p>
     */
    public static final int DEFAULT_BORDER_COLOR = Color.rgb(102, 102, 102);

    /**
     * <p>
     * 经线刻度字体颜色
     * </p>
     */
    public static final int DEFAULT_LONGITUDE_FONT_COLOR = Color.rgb(154, 154, 154);

    /**
     * <p>
     * 经线刻度字体大小
     * </p>
     */
    public static final int DEFAULT_LONGITUDE_FONT_SIZE = 20;

    /**
     * <p>
     * 纬线刻度字体颜色
     * </p>
     */
    public static final int DEFAULT_LATITUDE_FONT_COLOR = Color.rgb(102, 102, 102);

    /**
     * <p>
     * 默认纬线刻度字体大小
     * </p>
     */
    public static final int DEFAULT_LATITUDE_FONT_SIZE = 20;

    /**
     * <p>
     * 默认Y轴标题最大文字长度
     * </p>
     */
    public static final int DEFAULT_AXIS_Y_MAX_TITLE_LENGTH = 5;

    /**
     * <p>
     * 默认虚线效果
     * </p>
     */
    public static final PathEffect DEFAULT_DASH_EFFECT = new DashPathEffect(
            new float[]{20, 10}, 2);

    /**
     * <p>
     * 默认在控件被点击时，显示十字竖线线
     * </p>
     */
    public static final boolean DEFAULT_DISPLAY_CROSS_X_ON_TOUCH = false;

    /**
     * <p>
     * 默认在控件被点击时，显示十字横线线
     * </p>
     */
    public static final boolean DEFAULT_DISPLAY_CROSS_Y_ON_TOUCH = false;

    /**
     * <p>
     * 背景色
     * </p>
     */
    private int backgroundColor = DEFAULT_BACKGROUND_COLOR;

    /**
     * <p>
     * 坐标轴X的显示颜色
     * </p>
     */
    private int axisXColor = DEFAULT_AXIS_X_COLOR;

    /**
     * <p>
     * 坐标轴Y的显示颜色
     * </p>
     */
    private int axisYColor = DEFAULT_AXIS_Y_COLOR;

    /**
     * <p>
     * 网格经线的显示颜色
     * </p>
     */
    private int longitudeColor = DEFAULT_LONGITUDE_COLOR;

    /**
     * <p>
     * 网格纬线的显示颜色
     * </p>
     */
    private int latitudeColor = DEFAULT_LAITUDE_COLOR;

    /**
     * <p>
     * 轴线左边距
     * </p>
     */
    private float axisMarginLeft = DEFAULT_AXIS_MARGIN_LEFT;

    /**
     * <p>
     * 轴线下边距
     * </p>
     */
    private float axisMarginBottom = DEFAULT_AXIS_MARGIN_BOTTOM;

    /**
     * <p>
     * 轴线上边距
     * </p>
     */
    private float axisMarginTop = DEFAULT_AXIS_MARGIN_TOP;

    /**
     * <p>
     * 轴线右边距
     * </p>
     */
    private float axisMarginRight = DEFAULT_AXIS_MARGIN_RIGHT;

    /**
     * <p>
     * X轴上的标题是否显示
     * </p>
     */
    private boolean displayAxisXTitle = DEFAULT_DISPLAY_AXIS_X_TITLE;

    /**
     * <p>
     * Y轴上的标题是否显示
     * </p>
     */
    private boolean displayAxisYTitle = DEFAULT_DISPLAY_AXIS_Y_TITLE;

    /**
     * <p>
     * 网格纬线的数量
     * </p>
     */
    private int latitudeNum = DEFAULT_LATITUDE_NUM;

    /**
     * <p>
     * 网格经线的数量
     * </p>
     */
    private int longitudeNum = DEFAULT_LONGITUDE_NUM;

    /**
     * <p>
     * 经线是否显示
     * </p>
     */
    private boolean displayLongitude = DEFAULT_DISPLAY_LONGITUDE;

    /**
     * <p>
     * 经线是否显示为虚线
     * </p>
     */
    private boolean dashLongitude = DEFAULT_DASH_LONGITUDE;

    /**
     * <p>
     * 纬线是否显示
     * </p>
     */
    private boolean displayLatitude = DEFAULT_DISPLAY_LATITUDE;

    /**
     * <p>
     * 纬线是否显示为虚线
     * </p>
     */
    private boolean dashLatitude = DEFAULT_DASH_LATITUDE;

    /**
     * <p>
     * 虚线效果
     * </p>
     */
    private PathEffect dashEffect = DEFAULT_DASH_EFFECT;

    /**
     * <p>
     * 控件是否显示边框
     * </p>
     */
    private boolean displayBorder = DEFAULT_DISPLAY_BORDER;

    /**
     * <p>
     * 图边框的颜色
     * </p>
     */
    private int borderColor = DEFAULT_BORDER_COLOR;

    /**
     * <p>
     * 经线刻度字体颜色
     * </p>
     */
    private int longitudeFontColor = DEFAULT_LONGITUDE_FONT_COLOR;

    /**
     * <p>
     * 经线刻度字体大小
     * </p>
     */
    private int longitudeFontSize = DEFAULT_LONGITUDE_FONT_SIZE;

    /**
     * <p>
     * 纬线刻度字体颜色
     * </p>
     */
    private int latitudeFontColor = DEFAULT_LATITUDE_FONT_COLOR;

    /**
     * <p>
     * 纬线刻度字体大小
     * </p>
     */
    private int latitudeFontSize = DEFAULT_LATITUDE_FONT_SIZE;

    /**
     * <p>
     * X轴标题数组
     * </p>
     */
    private List<String> axisXTitles;

    /**
     * <p>
     * Y轴标题数组（左）
     * </p>
     */
    private List<String> axisYTitles;

    /**
     * <p>
     * Y轴标题数组(右)
     * </p>
     */
    private List<String> axisYTitlesR;

    /**
     * <p>
     * Y轴标题最大文字长度
     * </p>
     */
    private int axisYMaxTitleLength = DEFAULT_AXIS_Y_MAX_TITLE_LENGTH;

    /**
     * <p>
     * 在控件被点击时，显示十字竖线线
     * </p>
     */
    private boolean displayCrossXOnTouch = DEFAULT_DISPLAY_CROSS_X_ON_TOUCH;

    /**
     * <p>
     * 在控件被点击时，显示十字横线线
     * </p>
     */
    private boolean displayCrossYOnTouch = DEFAULT_DISPLAY_CROSS_Y_ON_TOUCH;

    /**
     * <p>
     * 单点触控的选中点
     * </p>
     */
    private PointF touchPoint;

    /**
     * <p>
     * 单点触控的选中点的X
     * </p>
     */
    private float clickPostX = 0f;

    /**
     * <p>
     * 单点触控的选中点的Y
     * </p>
     */
    private float clickPostY = 0f;

    /**
     * <p>
     * 事件通知对象列表
     * </p>
     */
    private List<ITouchEventResponse> notifyList;

    /**
     * <p>
     * X轴标题显示的位置是否居中
     * </p>
     */
    private boolean showPlace = false;

    /**
     * (non-Javadoc)
     *
     * @param context
     */
    public GridChart(Context context) {
        super(context);
    }

    /**
     * (non-Javadoc)
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public GridChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * (non-Javadoc)
     *
     * @param context
     * @param attrs
     */
    public GridChart(Context context, AttributeSet attrs) {
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
        super.onDraw(canvas);
        super.setBackgroundColor(backgroundColor);
        if (this.displayBorder) {
            drawBorder(canvas);
        }
        if (displayLongitude || displayAxisXTitle) {
            drawAxisGridX(canvas);
        }
        if (displayLatitude || displayAxisYTitle) {
            drawAxisGridY(canvas);
        }
        if (displayCrossXOnTouch || displayCrossYOnTouch) {
            drawWithFingerClick(canvas);
        }
        drawXAxis(canvas);
        drawYAxis(canvas);
    }

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

        if (event.getY() > 0
                && event.getY() < super.getBottom() - getAxisMarginBottom()
                && event.getX() > super.getLeft() + getAxisMarginLeft()
                && event.getX() < super.getRight()) {

//			// touched points, if touch point is only one
//			if (event.getPointerCount() == 1) {
//				// 获取点击坐�?
//				clickPostX = event.getX();
//				clickPostY = event.getY();
//
//				PointF point = new PointF(clickPostX, clickPostY);
//				touchPoint = point;
//
//				// redraw
//				super.invalidate();
//
//				// do notify
//				notifyEventAll(this);
//
//			} else if (event.getPointerCount() == 2) {
//
//			}
            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                // 获取点击坐�?
                clickPostX = event.getX();
                clickPostY = event.getY();
                PointF point = new PointF(clickPostX, clickPostY);
                touchPoint = point;
                // redraw
                super.invalidate();
                // do notify
                notifyEventAll(this);
            }
        }
        return true;
    }

    /**
     * <p>
     * 绘制一段文本，并增加外框
     * </p>
     *
     * @param ptStart  <p>
     *                 开始点
     *                 </p>
     * @param ptEnd    <p>
     *                 结束点
     *                 </p>
     * @param content  <p>
     *                 文字内容
     *                 </p>
     * @param fontSize <p>
     *                 字体大小
     *                 </p>
     * @param canvas
     */
    private void drawAlphaTextBox(PointF ptStart, PointF ptEnd, String content,
                                  int fontSize, Canvas canvas) {

        Paint mPaintBox = new Paint();
        mPaintBox.setColor(Color.BLACK);
        mPaintBox.setAlpha(80);

        Paint mPaintBoxLine = new Paint();
        mPaintBoxLine.setColor(Color.CYAN);
        mPaintBoxLine.setAntiAlias(true);

        // draw a rectangle
        canvas.drawRoundRect(new RectF(ptStart.x, ptStart.y, ptEnd.x, ptEnd.y),
                20.0f, 20.0f, mPaintBox);

        // draw a rectangle' border
        canvas.drawLine(ptStart.x, ptStart.y, ptStart.x, ptEnd.y, mPaintBoxLine);
        canvas.drawLine(ptStart.x, ptEnd.y, ptEnd.x, ptEnd.y, mPaintBoxLine);
        canvas.drawLine(ptEnd.x, ptEnd.y, ptEnd.x, ptStart.y, mPaintBoxLine);
        canvas.drawLine(ptEnd.x, ptStart.y, ptStart.x, ptStart.y, mPaintBoxLine);

        // draw text
        canvas.drawText(content, ptStart.x, ptEnd.y, mPaintBoxLine);
    }

    /**
     * <p>
     * 计算X轴上显示的坐标值
     * </p>
     *
     * @param value <p>
     *              计算用数据
     *              </p>
     * @return String
     * <p>
     * 坐标值
     * </p>
     */
    public String getAxisXGraduate(Object value) {

        float length = super.getWidth() - axisMarginLeft - axisMarginRight;
        float valueLength = ((Float) value).floatValue() - axisMarginLeft;

        return String.valueOf(valueLength / length);
    }

    /**
     * <p>
     * 计算Y轴上显示的坐标值
     * </p>
     *
     * @param value <p>
     *              计算用数据
     *              </p>
     * @return String
     * <p>
     * 坐标值
     * </p>
     */
    public String getAxisYGraduate(Object value) {

        float length = super.getHeight() - axisMarginBottom - axisMarginTop;
        float valueLength = length
                - (((Float) value).floatValue() - axisMarginTop);
        return String.valueOf(valueLength / length);
    }

    /**
     * <p>
     * 在图表被点击后绘制十字线
     * </p>
     *
     * @param canvas
     */
    protected void drawWithFingerClick(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setColor(Color.CYAN);

        float lineHLength = getWidth() - 2f;
        float lineVLength = getHeight() - 2f;
        // draw text
        if (isDisplayAxisXTitle()) {
            lineVLength = lineVLength - axisMarginBottom;

            if (clickPostX > 0 && clickPostY > 0) {
                if (displayCrossXOnTouch) {
                    // TODO calculate points to draw
                    PointF BoxVS = new PointF(clickPostX - longitudeFontSize
                            * 5f / 2f, lineVLength + 2f);
                    PointF BoxVE = new PointF(clickPostX + longitudeFontSize
                            * 5f / 2f, lineVLength + axisMarginBottom - 1f);
                    // draw text
                    drawAlphaTextBox(BoxVS, BoxVE,
                            getAxisXGraduate(clickPostX), longitudeFontSize,
                            canvas);
                }
            }
        }

        if (isDisplayAxisYTitle()) {
            lineHLength = lineHLength - getAxisMarginLeft();

            if (clickPostX > 0 && clickPostY > 0) {
                if (displayCrossYOnTouch) {
                    // TODO calculate points to draw
                    PointF BoxHS = new PointF(1f, clickPostY - latitudeFontSize
                            / 2f);
                    PointF BoxHE = new PointF(axisMarginLeft, clickPostY
                            + latitudeFontSize / 2f);

                    // draw text
                    drawAlphaTextBox(BoxHS, BoxHE,
                            getAxisYGraduate(clickPostY), latitudeFontSize,
                            canvas);
                }
            }
        }

        // draw line
        if (clickPostX > 0 && clickPostY > 0) {
            if (displayCrossXOnTouch) {
                canvas.drawLine(clickPostX, 1f, clickPostX, lineVLength, mPaint);
            }

            if (displayCrossYOnTouch) {
                canvas.drawLine(axisMarginLeft, clickPostY, axisMarginLeft
                        + lineHLength, clickPostY, mPaint);
            }
        }
    }

    /**
     * <p>
     * 绘制边框
     * </p>
     *
     * @param canvas
     */
    protected void drawBorder(Canvas canvas) {
        float width = super.getWidth() - 2;
        float height = super.getHeight() - 2;

        Paint mPaint = new Paint();
        mPaint.setColor(borderColor);

        // draw a rectangle
        canvas.drawLine(1f, 1f, 1f + width, 1f, mPaint);
        canvas.drawLine(1f + width, 1f, 1f + width, 1f + height, mPaint);
        canvas.drawLine(1f + width, 1f + height, 1f, 1f + height, mPaint);
        canvas.drawLine(1f, 1f + height, 1f, 1f, mPaint);
    }

    /**
     * <p>
     * 绘制X轴
     * </p>
     *
     * @param canvas
     */
    protected void drawXAxis(Canvas canvas) {

        float length = super.getWidth() - axisMarginRight;
        float postY = super.getHeight() - axisMarginBottom;

        Paint mPaint = new Paint();
        mPaint.setColor(axisXColor);

        canvas.drawLine(axisMarginLeft, postY, length, postY, mPaint);

    }

    /**
     * <p>
     * 绘制Y轴
     * </p>
     *
     * @param canvas
     */
    protected void drawYAxis(Canvas canvas) {

        float length = super.getHeight() - axisMarginBottom;
        float postX = axisMarginLeft;

        Paint mPaint = new Paint();
        mPaint.setColor(axisXColor);

        canvas.drawLine(postX, axisMarginTop, postX, length, mPaint);
    }

    /**
     * <p>
     * 绘制经线
     * </p>
     *
     * @param canvas
     */
    protected void drawAxisGridX(Canvas canvas) {

        if (null != axisXTitles) {
            int counts = axisXTitles.size();
            float length = super.getHeight() - axisMarginBottom;

            Paint mPaintLine = new Paint();
            mPaintLine.setColor(longitudeColor);
            //设置画出的线的 粗细程度
            mPaintLine.setStrokeWidth(1);
            if (dashLongitude) {
                dashEffect = new DashPathEffect(new float[]{1, 2, 4, 8}, 1);
                mPaintLine.setPathEffect(dashEffect);
            }
            Paint mPaintFont = new Paint();
            mPaintFont.setColor(longitudeFontColor);
            mPaintFont.setTextSize(longitudeFontSize);
            mPaintFont.setAntiAlias(true);
            if (counts > 1) {
                //X每份所占有的长度
                float postOffset = (super.getWidth() - axisMarginLeft - axisMarginRight)
                        / (counts - 1);
                for (int i = 0; i < counts; i++) {
                    // draw line 并且第一条经线不画
                    if (displayLongitude && i > 0) {
                        // 中间的线加粗
                        if (counts / 2 == i) {
                            mPaintLine.setStrokeWidth(2);
                        } else {
                            mPaintLine.setStrokeWidth(1);
                        }
//                        Path paths = new Path();
//                        paths.moveTo(axisMarginLeft + i * postOffset, axisMarginTop);
//                        paths.lineTo(axisMarginLeft + i
//                                * postOffset,length);
//                        paths.close();
                        canvas.drawLine(axisMarginLeft + i * postOffset, axisMarginTop, axisMarginLeft + i
                                * postOffset, length, mPaintLine);
//                        canvas.drawPath(paths,mPaintFont);
                    }
                    // draw title
                    if (displayAxisXTitle) {
                        //标题是否居中显示
                        if (showPlace) {
                            if (i != counts) {
                                canvas.drawText(axisXTitles.get(i), axisMarginLeft + postOffset / 2f +
                                                i * postOffset
                                                - (axisXTitles.get(i).length())
                                                * longitudeFontSize / 4f, super.getHeight()
                                                - axisMarginBottom + longitudeFontSize,
                                        mPaintFont);
                            }
                        } else {
                            if (i == (counts - 1)) {
                                // 最后一个标题
                                canvas.drawText(axisXTitles.get(i), super.getWidth() - axisMarginRight - axisXTitles.get(i).length() * latitudeFontSize / 2 - 5, super.getHeight()
                                                - axisMarginBottom + longitudeFontSize,
                                        mPaintFont);

                            } else if (0 == i) {
                                canvas.drawText(axisXTitles.get(i),
                                        this.axisMarginLeft + 2f, super.getHeight()
                                                - axisMarginBottom
                                                + longitudeFontSize, mPaintFont);
                            } else {
                                canvas.drawText(axisXTitles.get(i), axisMarginLeft + i
                                                * postOffset
                                                - (axisXTitles.get(i).length())
                                                * longitudeFontSize / 4f, super.getHeight()
                                                - axisMarginBottom + longitudeFontSize,
                                        mPaintFont);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * <p>
     * 绘制纬线
     * </p>
     *
     * @param canvas
     */
    protected void drawAxisGridY(Canvas canvas) {
        if (null != axisYTitles) {
            int counts = axisYTitles.size();
            float length = super.getWidth() - axisMarginLeft - axisMarginRight;

            Paint mPaintLine = new Paint();
            mPaintLine.setColor(latitudeColor);
            if (dashLatitude) {
                mPaintLine.setPathEffect(dashEffect);
            }

            Paint mPaintFont = new Paint();
            mPaintFont.setColor(latitudeFontColor);
            mPaintFont.setTextSize(latitudeFontSize);
            mPaintFont.setAntiAlias(true);

            if (counts > 1) {
                //纬线每份所占的高度
                float postOffset = (super.getHeight() - axisMarginBottom - axisMarginTop)
                        / (counts - 1);
                //y轴的起始高度
                float offset = super.getHeight() - axisMarginBottom;
                for (int i = 0; i <= counts; i++) {
                    // draw line 并且第一条纬线不画
                    if (displayLatitude && i > 0) {
                        // 中间那条线粗点
                        if (counts / 2 == i) {
                            mPaintLine.setStrokeWidth(2);
                        } else {
                            mPaintLine.setStrokeWidth(1);
                        }
                        canvas.drawLine(axisMarginLeft,
                                offset - i * postOffset, axisMarginLeft
                                        + length, offset - i * postOffset,
                                mPaintLine);
                    }
                    // draw title
                    if (displayAxisYTitle) {
                        if (i < counts && i > 0) {
                            canvas.drawText(axisYTitles.get(i), 0f, offset - i
                                            * postOffset + latitudeFontSize / 2f,
                                    mPaintFont);
                        } else if (0 == i) {
                            canvas.drawText(axisYTitles.get(i), 0f,
                                    super.getHeight() - this.axisMarginBottom
                                            - 2f, mPaintFont);
                        }
                    } else if (i == (counts - 1)) {
                        canvas.drawText(axisXTitles.get(i), this.axisMarginLeft + i
                                        * postOffset
                                        - (axisXTitles.get(i).length())
                                        * longitudeFontSize / 4f, super.getHeight()
                                        - axisMarginBottom + longitudeFontSize,
                                mPaintFont);

                    }
                }
            }
        }
    }

    /**
     * 在图像内画标题（最大最小值和涨幅）
     */
    public void drawAxisGridYInner(Canvas canvas) {
        if (axisYTitlesR != null && axisYTitlesR.size() > 1) {
            Paint mPaintFont = new Paint();
            mPaintFont.setColor(latitudeFontColor);
            mPaintFont.setTextSize(latitudeFontSize);
            mPaintFont.setAntiAlias(true);
            mPaintFont.setColor(Color.rgb(235, 35, 65));
            // 绘制最高价格
            canvas.drawText(axisYTitlesR.get(0), this.axisMarginLeft + 2f, this.axisMarginTop + latitudeFontSize,
                    mPaintFont);
            // 绘制最高涨跌幅
            canvas.drawText(axisYTitlesR.get(1), super.getWidth() - axisMarginRight - axisYTitlesR.get(1).length() * latitudeFontSize / 2 - 5, this.axisMarginTop + latitudeFontSize, mPaintFont);

            mPaintFont.setColor(Color.rgb(50, 174, 110));
            // 绘制最低价格
            canvas.drawText(axisYTitlesR.get(2), this.axisMarginLeft + 2f,
                    super.getHeight() - this.axisMarginBottom - 2f
                            - 2f, mPaintFont);

            // 绘制低涨跌幅
            canvas.drawText(axisYTitlesR.get(3), super.getWidth() - axisMarginRight - axisYTitlesR.get(3).length() * latitudeFontSize / 2 - 5,
                    super.getHeight() - this.axisMarginBottom - 2f
                            - 2f, mPaintFont);
        }
    }

    /**
     * 在图像内画标题（最大最小值 蜡线图用）
     */
    public void drawAxisGridYInnerL(Canvas canvas) {
        if (axisYTitlesR != null && axisYTitlesR.size() > 1) {
            Paint mPaintFont = new Paint();
            mPaintFont.setColor(latitudeFontColor);
            mPaintFont.setTextSize(latitudeFontSize);
            mPaintFont.setAntiAlias(true);
            // 绘制最高价格
            canvas.drawText(axisYTitlesR.get(0), this.axisMarginLeft + 2f, this.axisMarginTop + latitudeFontSize,
                    mPaintFont);
            // 绘制最低价格
            canvas.drawText(axisYTitlesR.get(1), this.axisMarginLeft + 2f,
                    super.getHeight() - this.axisMarginBottom - 2f
                            - 2f, mPaintFont);
        }
    }

    /**
     * <p>
     * 绘制几何图形
     * </p>
     *
     * @param canvas
     */
    public void drawSpot(Canvas canvas, SpotEntity spot, float cx, float cy) {
        Paint paint = new Paint();
        Path path = null;
        //设置透明度数
        int alpha = spot.getAlpha();
        // 消除锯齿
        paint.setAntiAlias(true);
        float radius = spot.getSpotR();
        switch (spot.getSpotType()) {
            case 1:
                //空实心圆
                paint.setColor(spot.getPadColor());
                paint.setStyle(Paint.Style.FILL);
                //0~255 越小越透明
                paint.setAlpha(alpha);
                canvas.drawCircle(cx, cy, radius, paint);
                //空心圆
                paint.setColor(spot.getSpotColor());
                paint.setStrokeWidth(spot.getSpotSize());
                paint.setAlpha(255);
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(cx, cy, spot.getSpotR(), paint);
                break;
            case 2:
                //画实心三角形
                paint.setStyle(Paint.Style.FILL);
                paint.setAlpha(alpha);
                paint.setColor(spot.getPadColor());
                path = new Path();
                path.moveTo(cx, cy - radius);
                path.lineTo(cx - (float) Math.sqrt(3.0) * (radius / 2), cy + radius / 2);
                path.lineTo(cx + (float) Math.sqrt(3.0) * (radius / 2), cy + radius / 2);
                path.lineTo(cx, cy - radius);
                canvas.drawPath(path, paint);
                //画空心三角形
                paint.setColor(spot.getSpotColor());
                paint.setStrokeWidth(spot.getSpotSize());
                paint.setStyle(Paint.Style.STROKE);
                paint.setAlpha(255);
                path = new Path();
                path.moveTo(cx, cy - radius);
                path.lineTo(cx - (float) Math.sqrt(3.0) * (radius / 2), cy + radius / 2);
                path.lineTo(cx + (float) Math.sqrt(3.0) * (radius / 2), cy + radius / 2);
                path.lineTo(cx, cy - radius);
                canvas.drawPath(path, paint);
                break;
            case 3:
                //画实心菱形
                paint.setStyle(Paint.Style.FILL);
                paint.setAlpha(alpha);
                paint.setColor(spot.getPadColor());
                path = new Path();
                path.moveTo(cx, cy - radius);
                path.lineTo(cx - radius, cy);
                path.lineTo(cx, cy + radius);
                path.lineTo(cx + radius, cy);
                path.lineTo(cx, cy - radius);
                canvas.drawPath(path, paint);
                //画空心三菱形
                paint.setStyle(Paint.Style.STROKE);
                paint.setAlpha(255);
                paint.setColor(spot.getSpotColor());
                path = new Path();
                path.moveTo(cx, cy - radius);
                path.lineTo(cx - radius, cy);
                path.lineTo(cx, cy + radius);
                path.lineTo(cx + radius, cy);
                path.lineTo(cx, cy - radius);
                canvas.drawPath(path, paint);
                break;
            case 4:
                //空实心正方形
                paint.setColor(spot.getPadColor());
                paint.setStyle(Paint.Style.FILL);
                paint.setAlpha(alpha);
                paint.setColor(spot.getPadColor());
                path = new Path();
                path.moveTo(cx - radius, cy - radius);
                path.lineTo(cx - radius, cy + radius);
                path.lineTo(cx + radius, cy + radius);
                path.lineTo(cx + radius, cy - radius);
                path.lineTo(cx - radius, cy - radius);
                canvas.drawPath(path, paint);
                //空心圆正方形
                paint.setColor(spot.getSpotColor());
                paint.setStrokeWidth(spot.getSpotSize());
                paint.setStyle(Paint.Style.STROKE);
                paint.setAlpha(255);
                path.moveTo(cx - radius, cy - radius);
                path.lineTo(cx - radius, cy + radius);
                path.lineTo(cx + radius, cy + radius);
                path.lineTo(cx + radius, cy - radius);
                path.lineTo(cx - radius, cy - radius);
                canvas.drawPath(path, paint);
                break;
            case 5:
                //实心点
                paint.setColor(spot.getSpotColor());
                paint.setStrokeWidth(spot.getSpotSize());
                paint.setStyle(Paint.Style.FILL);
                paint.setAlpha(alpha);
                canvas.drawCircle(cx, cy, spot.getSpotR(), paint);
                break;
        }

    }


    /**
     * (non-Javadoc)
     */
    public void notifyEvent(GridChart chart) {
        PointF point = chart.getTouchPoint();
        if (null != point) {
            clickPostX = point.x;
            clickPostY = point.y;
        }
        touchPoint = new PointF(clickPostX, clickPostY);
        super.invalidate();
    }

    /**
     * (non-Javadoc)
     */
    public void addNotify(ITouchEventResponse notify) {
        if (null == notifyList) {
            notifyList = new ArrayList<ITouchEventResponse>();
        }
        notifyList.add(notify);
    }

    /**
     * (non-Javadoc)
     */
    public void removeNotify(int i) {
        if (null != notifyList && notifyList.size() > i) {
            notifyList.remove(i);
        }
    }

    /**
     * (non-Javadoc)
     */
    public void removeAllNotify() {
        if (null != notifyList) {
            notifyList.clear();
        }
    }

    public void notifyEventAll(GridChart chart) {
        if (null != notifyList) {
            for (int i = 0; i < notifyList.size(); i++) {
                ITouchEventResponse ichart = notifyList.get(i);
                ichart.notifyEvent(chart);
            }
        }
    }

    /**
     * @return the axisXColor
     */
    public int getAxisXColor() {
        return axisXColor;
    }

    /**
     * @param axisXColor the axisXColor to set
     */
    public void setAxisXColor(int axisXColor) {
        this.axisXColor = axisXColor;
    }

    /**
     * @return the axisYColor
     */
    public int getAxisYColor() {
        return axisYColor;
    }

    /**
     * @param axisYColor the axisYColor to set
     */
    public void setAxisYColor(int axisYColor) {
        this.axisYColor = axisYColor;
    }

    /**
     * @return the longitudeColor
     */
    public int getLongitudeColor() {
        return longitudeColor;
    }

    /**
     * @param longitudeColor the longitudeColor to set
     */
    public void setLongitudeColor(int longitudeColor) {
        this.longitudeColor = longitudeColor;
    }

    /**
     * @return the latitudeColor
     */
    public int getLatitudeColor() {
        return latitudeColor;
    }

    /**
     * @param latitudeColor the latitudeColor to set
     */
    public void setLatitudeColor(int latitudeColor) {
        this.latitudeColor = latitudeColor;
    }

    /**
     * @return the axisMarginLeft
     */
    public float getAxisMarginLeft() {
        return axisMarginLeft;
    }

    /**
     * @param axisMarginLeft the axisMarginLeft to set
     */
    public void setAxisMarginLeft(float axisMarginLeft) {
        this.axisMarginLeft = axisMarginLeft;
    }

    /**
     * @return the axisMarginBottom
     */
    public float getAxisMarginBottom() {
        return axisMarginBottom;
    }

    /**
     * @param axisMarginBottom the axisMarginBottom to set
     */
    public void setAxisMarginBottom(float axisMarginBottom) {
        this.axisMarginBottom = axisMarginBottom;
    }

    /**
     * @return the axisMarginTop
     */
    public float getAxisMarginTop() {
        return axisMarginTop;
    }

    /**
     * @param axisMarginTop the axisMarginTop to set
     */
    public void setAxisMarginTop(float axisMarginTop) {
        this.axisMarginTop = axisMarginTop;
    }

    /**
     * @return the axisMarginRight
     */
    public float getAxisMarginRight() {
        return axisMarginRight;
    }

    /**
     * @param axisMarginRight the axisMarginRight to set
     */
    public void setAxisMarginRight(float axisMarginRight) {
        this.axisMarginRight = axisMarginRight;
    }

    /**
     * @return the displayAxisXTitle
     */
    public boolean isDisplayAxisXTitle() {
        return displayAxisXTitle;
    }

    /**
     * @param displayAxisXTitle the displayAxisXTitle to set
     */
    public void setDisplayAxisXTitle(boolean displayAxisXTitle) {
        this.displayAxisXTitle = displayAxisXTitle;
    }

    /**
     * @return the displayAxisYTitle
     */
    public boolean isDisplayAxisYTitle() {
        return displayAxisYTitle;
    }

    /**
     * @param displayAxisYTitle the displayAxisYTitle to set
     */
    public void setDisplayAxisYTitle(boolean displayAxisYTitle) {
        this.displayAxisYTitle = displayAxisYTitle;
    }

    /**
     * @return the latitudeNum
     */
    public int getLatitudeNum() {
        return latitudeNum;
    }

    /**
     * @param latitudeNum the latitudeNum to set
     */
    public void setLatitudeNum(int latitudeNum) {
        this.latitudeNum = latitudeNum;
    }

    /**
     * @return the longitudeNum
     */
    public int getLongitudeNum() {
        return longitudeNum;
    }

    /**
     * @param longitudeNum the longitudeNum to set
     */
    public void setLongitudeNum(int longitudeNum) {
        this.longitudeNum = longitudeNum;
    }

    /**
     * @return the displayLongitude
     */
    public boolean isDisplayLongitude() {
        return displayLongitude;
    }

    /**
     * @param displayLongitude the displayLongitude to set
     */
    public void setDisplayLongitude(boolean displayLongitude) {
        this.displayLongitude = displayLongitude;
    }

    /**
     * @return the dashLongitude
     */
    public boolean isDashLongitude() {
        return dashLongitude;
    }

    /**
     * @param dashLongitude the dashLongitude to set
     */
    public void setDashLongitude(boolean dashLongitude) {
        this.dashLongitude = dashLongitude;
    }

    /**
     * @return the displayLatitude
     */
    public boolean isDisplayLatitude() {
        return displayLatitude;
    }

    /**
     * @param displayLatitude the displayLatitude to set
     */
    public void setDisplayLatitude(boolean displayLatitude) {
        this.displayLatitude = displayLatitude;
    }

    /**
     * @return the dashLatitude
     */
    public boolean isDashLatitude() {
        return dashLatitude;
    }

    /**
     * @param dashLatitude the dashLatitude to set
     */
    public void setDashLatitude(boolean dashLatitude) {
        this.dashLatitude = dashLatitude;
    }

    /**
     * @return the dashEffect
     */
    public PathEffect getDashEffect() {
        return dashEffect;
    }

    /**
     * @param dashEffect the dashEffect to set
     */
    public void setDashEffect(PathEffect dashEffect) {
        this.dashEffect = dashEffect;
    }

    /**
     * @return the displayBorder
     */
    public boolean isDisplayBorder() {
        return displayBorder;
    }

    /**
     * @param displayBorder the displayBorder to set
     */
    public void setDisplayBorder(boolean displayBorder) {
        this.displayBorder = displayBorder;
    }

    /**
     * @return the borderColor
     */
    public int getBorderColor() {
        return borderColor;
    }

    /**
     * @param borderColor the borderColor to set
     */
    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    /**
     * @return the longitudeFontColor
     */
    public int getLongitudeFontColor() {
        return longitudeFontColor;
    }

    /**
     * @param longitudeFontColor the longitudeFontColor to set
     */
    public void setLongitudeFontColor(int longitudeFontColor) {
        this.longitudeFontColor = longitudeFontColor;
    }

    /**
     * @return the longitudeFontSize
     */
    public int getLongitudeFontSize() {
        return longitudeFontSize;
    }

    /**
     * @param longitudeFontSize the longitudeFontSize to set
     */
    public void setLongitudeFontSize(int longitudeFontSize) {
        this.longitudeFontSize = longitudeFontSize;
    }

    /**
     * @return the latitudeFontColor
     */
    public int getLatitudeFontColor() {
        return latitudeFontColor;
    }

    /**
     * @param latitudeFontColor the latitudeFontColor to set
     */
    public void setLatitudeFontColor(int latitudeFontColor) {
        this.latitudeFontColor = latitudeFontColor;
    }

    /**
     * @return the latitudeFontSize
     */
    public int getLatitudeFontSize() {
        return latitudeFontSize;
    }

    /**
     * @param latitudeFontSize the latitudeFontSize to set
     */
    public void setLatitudeFontSize(int latitudeFontSize) {
        this.latitudeFontSize = latitudeFontSize;
    }

    /**
     * @return the axisXTitles
     */
    public List<String> getAxisXTitles() {
        return axisXTitles;
    }

    /**
     * @param axisXTitles the axisXTitles to set
     */
    public void setAxisXTitles(List<String> axisXTitles) {
        this.axisXTitles = axisXTitles;
    }

    /**
     * @return the axisYTitles
     */
    public List<String> getAxisYTitles() {
        return axisYTitles;
    }

    /**
     * @param axisYTitles the axisYTitles to set
     */
    public void setAxisYTitles(List<String> axisYTitles) {
        this.axisYTitles = axisYTitles;
    }

    /**
     * @return the axisYMaxTitleLength
     */
    public int getAxisYMaxTitleLength() {
        return axisYMaxTitleLength;
    }

    /**
     * @param axisYMaxTitleLength the axisYMaxTitleLength to set
     */
    public void setAxisYMaxTitleLength(int axisYMaxTitleLength) {
        this.axisYMaxTitleLength = axisYMaxTitleLength;
    }

    /**
     * @return the displayCrossXOnTouch
     */
    public boolean isDisplayCrossXOnTouch() {
        return displayCrossXOnTouch;
    }

    /**
     * @param displayCrossXOnTouch the displayCrossXOnTouch to set
     */
    public void setDisplayCrossXOnTouch(boolean displayCrossXOnTouch) {
        this.displayCrossXOnTouch = displayCrossXOnTouch;
    }

    /**
     * @return the displayCrossYOnTouch
     */
    public boolean isDisplayCrossYOnTouch() {
        return displayCrossYOnTouch;
    }

    /**
     * @param displayCrossYOnTouch the displayCrossYOnTouch to set
     */
    public void setDisplayCrossYOnTouch(boolean displayCrossYOnTouch) {
        this.displayCrossYOnTouch = displayCrossYOnTouch;
    }

    /**
     * @return the clickPostX
     */
    public float getClickPostX() {
        return clickPostX;
    }

    /**
     * @param clickPostX the clickPostX to set
     */
    public void setClickPostX(float clickPostX) {
        this.clickPostX = clickPostX;
    }

    /**
     * @return the clickPostY
     */
    public float getClickPostY() {
        return clickPostY;
    }

    /**
     * @param clickPostY the clickPostY to set
     */
    public void setClickPostY(float clickPostY) {
        this.clickPostY = clickPostY;
    }

    /**
     * @return the notifyList
     */
    public List<ITouchEventResponse> getNotifyList() {
        return notifyList;
    }

    /**
     * @param notifyList the notifyList to set
     */
    public void setNotifyList(List<ITouchEventResponse> notifyList) {
        this.notifyList = notifyList;
    }

    /**
     * @return the touchPoint
     */
    public PointF getTouchPoint() {
        return touchPoint;
    }

    /**
     * @param touchPoint the touchPoint to set
     */
    public void setTouchPoint(PointF touchPoint) {
        this.touchPoint = touchPoint;
    }

    /**
     * @return the backgroundColor
     */
    public int getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * @param backgroundColor the backgroundColor to set
     */
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isShowPlace() {
        return showPlace;
    }

    public void setShowPlace(boolean showPlace) {
        this.showPlace = showPlace;
    }

    public List<String> getAxisYTitlesR() {
        return axisYTitlesR;
    }

    public void setAxisYTitlesR(List<String> axisYTitlesR) {
        this.axisYTitlesR = axisYTitlesR;
    }
}
