/*
 * MACandleStickChart.java
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
import android.graphics.PointF;
import android.util.AttributeSet;

import com.example.sjqcjstock.entity.stocks.LineEntity;
import com.example.sjqcjstock.entity.stocks.SpotEntity;

import java.util.List;

/**
 * <p>
 * MACandleStickChart继承于CandleStickChart的，可以在CandleStickChart基础上
 * 显示移动平均等各种分析指标数据。
 * </p>
 *
 * @author menghuan
 * @version v1.0 2013/12/25 14:49:02
 * @see CandleStickChart
 * @see StickChart
 */
public class MACandleStickChart extends CandleStickChart {

    /**
     * <p>
     * 绘制线条用的数据
     * </p>
     */
    private List<LineEntity> lineData;

    /**
     * (non-Javadoc)
     *
     * @param context
     */
    public MACandleStickChart(Context context) {
        super(context);
    }

    /**
     * (non-Javadoc)
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public MACandleStickChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * (non-Javadoc)
     *
     * @param context
     * @param attrs
     */
    public MACandleStickChart(Context context, AttributeSet attrs) {
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
        if (null != this.lineData) {
            if (0 != this.lineData.size()) {
                drawLines(canvas);
            }
        }
        // 先画线再画里面的标题
        super.drawAxisGridYInnerL(canvas);
    }

    /**
     * <p>
     * 绘制线条(注意数据不为""才画，并且开始画后直到结束中间不能出现"")
     * </p>
     *
     * @param canvas
     */
    protected void drawLines(Canvas canvas) {
        //  两点间的距离
        float lineLength = ((super.getWidth() - super.getAxisMarginLeft() - super.getAxisMarginRight()) / (super.getMaxSticksNum()));
        // 起始点的X
        float startX;
        //起始点的Y
        float valueY = 0f;
        // draw lines
        for (int i = 0; i < lineData.size(); i++) {
            LineEntity line = (LineEntity) lineData.get(i);
            if (line.isDisplay()) {
                Paint mPaint = new Paint();
                mPaint.setColor(line.getLineColor());
                mPaint.setAntiAlias(true);
                List<SpotEntity> lineData = line.getLineData();
                // 为起始点X附值
                startX = super.getAxisMarginLeft() + lineLength / 2;
                // start point
                PointF ptFirst = null;
                if (lineData != null) {
                    for (int j = 0; j < lineData.size(); j++) {
                        float value = lineData.get(j).getSpotData();
                        // 计算 Y
                        valueY = (float) ((1f - (value - this
                                .getMinValue())
                                / (this.getMaxValue() - this.getMinValue())) * (super
                                .getHeight() - super.getAxisMarginBottom() - super.getAxisMarginTop())) + super.getAxisMarginTop();
                        //Y轴的实际值
                        if (value > 0) {
                            //华特殊点
                            if (lineData.get(j).getSpotType() != 0) {
                                drawSpot(canvas, lineData.get(j), startX, valueY);
                            }
                            //如果不是第一点连接到以前的点
                            if (j > 0 && ptFirst != null) {
                                canvas.drawLine(ptFirst.x, ptFirst.y, startX,
                                        valueY, mPaint);
                            }
                            // 重置
                            ptFirst = new PointF(startX, valueY);
                        }
                        startX = startX + lineLength;
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
}
