/*
 * LineChart.java
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
import android.util.AttributeSet;

import com.example.sjqcjstock.entity.stocks.LineEntity;
import com.example.sjqcjstock.entity.stocks.SpotEntity;

import java.util.List;

/**
 * <p>
 * LineChart是在GridChart上绘制一条或多条线条的图
 * </p>
 *
 * @author menghuan
 * @version v1.0 2011/05/30 14:23:53
 * @see GridChart
 */
public class LineChart extends GridChart {
    /**
     * <p>
     * 绘制线条用的数据
     * </p>
     */
    private List<LineEntity> lineData;

    /**
     * <p>
     * 线条的最大表示点数
     * </p>
     */
    private int maxPointNum;

    /**
     * <p>
     * Y的最小表示值
     * </p>
     */
    private float minValue;

    /**
     * <p>
     * Y的最大表示值
     * </p>
     */
    private float maxValue;

    public LineChart(Context context) {
        super(context);
    }

    public LineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * <p>绘制图表时调用<p>
     *
     * @param canvas
     * @see android.view.View#onDraw(Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // draw lines
        if (null != this.lineData) {
            drawLines(canvas);
        }
        // 先画线再画里面的标题
        super.drawAxisGridYInner(canvas);
    }

    /**
     * <p>
     * 绘制线条（注意数据不为""才画，并且开始画后直到结束中间不能出现""）
     * </p>
     *
     * @param canvas
     */
    protected void drawLines(Canvas canvas) {
        //  两点间的距离
        float lineLength = ((super.getWidth() - super.getAxisMarginLeft() - super.getAxisMarginRight()) / (this.getMaxPointNum() + 2));
        // 起始点的X
        float startX;
        //起始点的Y
        float valueY = 0f;
        // draw lines
        for (int i = 0; i < lineData.size(); i++) {
            LineEntity line = (LineEntity) lineData.get(i);
            if (line.isDisplay()) {
                Paint mPaint = new Paint();
                //线的路径
                Path path = null;
                mPaint.setColor(line.getLineColor());
                mPaint.setAntiAlias(true);
                mPaint.setStyle(Paint.Style.STROKE);
                //线条的粗细
                mPaint.setStrokeWidth(line.getLineSize());
                //是否为虚线
                if (!line.isRealLine()) {
                    PathEffect effects = new DashPathEffect(
                            new float[]{10, 3}, 1);
                    mPaint.setPathEffect(effects);
                }
                List<SpotEntity> linedatas = line.getLineData();
                // 为起始点X附值
                startX = super.getAxisMarginLeft() + 1;
                SpotEntity spotEntity = null;
                if (lineData != null) {
                    for (int j = 0; j < linedatas.size(); j++) {
                        spotEntity = linedatas.get(j);
                        //Y轴的实际值
                        if (null != spotEntity) {
                            float value = spotEntity.getSpotData();
                            // 计算 Y
                            valueY = (float) ((1f - (value - this
                                    .getMinValue())
                                    / (this.getMaxValue() - this.getMinValue())) * (super
                                    .getHeight() - super.getAxisMarginBottom() - super.getAxisMarginTop())) + super.getAxisMarginTop();
                            //画特殊点
                            if (spotEntity.getSpotType() != 0) {
                                drawSpot(canvas, spotEntity, startX, valueY);
                            }
                            //如果不是第一点连接到以前的点
                            if (j > 0) {
                                path.lineTo(startX, valueY);
                            } else {
                                //初始化线的对象并添加第一点
                                path = new Path();
                                path.moveTo(startX, valueY);
                            }
                        }
                        startX = startX + lineLength;
                        //最后一段也连线
                        if (j == linedatas.size() - 1) {
                            path.lineTo(startX, valueY);

                            // 先画背景颜色
                            if (!line.isBackger()) {
                                Path paths = new Path();
                                paths.addPath(path);
                                paths.lineTo(startX, super.getHeight() - super.getAxisMarginBottom());
                                paths.lineTo(super.getAxisMarginLeft(), super.getHeight() - super.getAxisMarginBottom());
                                paths.lineTo(super.getAxisMarginLeft(), super.getHeight() - super.getAxisMarginBottom());
                                paths.close();
                                Paint mPaints = new Paint();
                                mPaints.setColor(Color.rgb(242, 244, 246));
                                mPaints.setStyle(Paint.Style.FILL);
                                mPaints.setAlpha(127);
                                canvas.drawPath(paths, mPaints);
                            }
                            // 再画路径
                            canvas.drawPath(path, mPaint);
                        }
                    }
                }
            }
        }
    }

    /**
     * @return the lineData
     */
    public List<LineEntity> getLineData() {
        return lineData;
    }

    /**
     * @param lineData the lineData to set
     */
    public void setLineData(List<LineEntity> lineData) {
        this.lineData = lineData;
    }

    /**
     * @return the maxPointNum
     */
    public int getMaxPointNum() {
        return maxPointNum;
    }

    /**
     * @param maxPointNum the maxPointNum to set
     */
    public void setMaxPointNum(int maxPointNum) {
        this.maxPointNum = maxPointNum;
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

}
