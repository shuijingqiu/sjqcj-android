/*
 * SpotEntity.java
 * Android-Charts
 *
 * Created by limc on 2014.
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

/**
 * <p>保存每一个点的对象</p>
 *
 * @author menghuan
 * @version v1.0 2014-1-2 下午12:10:33
 */
public class SpotEntity {

    /**
     * <p>点的数据大小</p>
     */
    private float spotData;

    /**
     * <p>点对应的时间</p>
     */
    private String spotTime;

    /**
     * <p>点的显示类型(0:普通；1：圆形；2：三角形；3：菱形；4：正方形 ；5：细点)</p>
     */
    private int spotType;

    /**
     * <p>点的半径大小</p>
     */
    private float spotR;

    /**
     * <p>点的颜色</p>
     */
    private int spotColor;

    /**
     * <p>点的粗细</p>
     */
    private int spotSize;

    /**
     * <p>填充色</p>
     */
    private int padColor;

    /**
     * <p>透明度</p>
     */
    private int alpha = 255;

    public SpotEntity() {

    }

    public SpotEntity(float spotData, String spotTime) {
        this.spotData = spotData;
        this.spotTime = spotTime;
        this.spotType = 0;
    }

    public SpotEntity(float spotData, int spotType, float spotR, int spotColor, int spotSize, int padColor) {
        this.spotData = spotData;
        this.spotType = spotType;
        this.spotR = spotR;
        this.spotColor = spotColor;
        this.spotSize = spotSize;
        this.padColor = padColor;
    }

    public SpotEntity(float spotData, float spotR, int spotColor, int spotSize, int padColor) {
        this.spotData = spotData;
        this.spotR = spotR;
        this.spotColor = spotColor;
        this.spotSize = spotSize;
        this.padColor = padColor;
    }

    public float getSpotData() {
        return spotData;
    }

    public void setSpotData(float spotData) {
        this.spotData = spotData;
    }

    public int getSpotType() {
        return spotType;
    }

    public void setSpotType(int spotType) {
        this.spotType = spotType;
    }

    public float getSpotR() {
        return spotR;
    }

    public void setSpotR(float spotR) {
        this.spotR = spotR;
    }

    public int getSpotColor() {
        return spotColor;
    }

    public void setSpotColor(int spotColor) {
        this.spotColor = spotColor;
    }

    public int getSpotSize() {
        return spotSize;
    }

    public void setSpotSize(int spotSize) {
        this.spotSize = spotSize;
    }

    public int getPadColor() {
        return padColor;
    }

    public void setPadColor(int padColor) {
        this.padColor = padColor;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public String getSpotTime() {
        return spotTime;
    }

    public void setSpotTime(String spotTime) {
        this.spotTime = spotTime;
    }
}
