/*
 * LineEntity.java
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

package com.example.sjqcjstock.entity.stocks;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>保存线图表示用单个线的对象、多条线的时候请使用相应的数据结构保存数据</p>
 *
 * @author menghuan
 * @version v1.0 2013/12/25 12:24:49
 */
public class LineEntity {

    /**
     * <p>线表示数据的点</p>
     */
    private List<SpotEntity> lineData;

    /**
     * <p>线的标题，用于标识别这条线</p>
     */
    private String title;

    /**
     * <p>线的颜色</p>
     */
    private int lineColor;

    /**
     * <p>是否在图表上显示该线</p>
     */
    private boolean display = true;

    /**
     * <p>该线是否是实线</p>
     */
    private boolean realLine = true;

    /**
     * <p>该线是否包围成有背景色</p>
     */
    private boolean isBackger = false;

    /**
     * <p>该线的粗细</p>
     */
    private float lineSize = 2f;

    /**
     * <p>LineEntity类对象的构造函数</p>
     */
    public LineEntity() {
        super();
    }

    /**
     * <p>LineEntity类对象的构造函数</p>
     *
     * @param lineData  <p>线表示数据</p>
     * @param title     <p>线的标题，用于标识别这条线</p>
     * @param lineColor <p>线的颜色</p>
     */
    public LineEntity(List<SpotEntity> lineData, String title, int lineColor) {
        super();
        this.lineData = lineData;
        this.title = title;
        this.lineColor = lineColor;
    }

    /**
     * @param value
     */
    public void put(SpotEntity value) {
        if (null == lineData) {
            lineData = new ArrayList<SpotEntity>();
        }
        lineData.add(value);
    }

    /**
     * @return the lineData
     */
    public List<SpotEntity> getLineData() {
        return lineData;
    }

    /**
     * @param lineData the lineData to set
     */
    public void setLineData(List<SpotEntity> lineData) {
        this.lineData = lineData;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the lineColor
     */
    public int getLineColor() {
        return lineColor;
    }

    /**
     * @param lineColor the lineColor to set
     */
    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    /**
     * @return the display
     */
    public boolean isDisplay() {
        return display;
    }

    /**
     * @param display the display to set
     */
    public void setDisplay(boolean display) {
        this.display = display;
    }

    public boolean isRealLine() {
        return realLine;
    }

    public void setRealLine(boolean realLine) {
        this.realLine = realLine;
    }

    public float getLineSize() {
        return lineSize;
    }

    public void setLineSize(float lineSize) {
        this.lineSize = lineSize;
    }

    public boolean isBackger() {
        return isBackger;
    }

    public void setBackger(boolean backger) {
        isBackger = backger;
    }
}
