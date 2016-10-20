/*
 * StickEntity.java
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

/**
 * <p>
 * CCSStickChart保存柱条表示用的高低值的实体对象
 * </p>
 *
 * @author menghuan
 * @version v1.0 2013/12/26 12:24:49
 */
public class StickEntity {

    /**
     * <p>
     * 最高值
     * </p>
     */
    private double high;

    /**
     * <p>
     * 最低值
     * </p>
     */
    private double low = 0;

    /**
     * <p>
     * 日期
     * </p>
     */
    private String date;

    /**
     * 涨跌类型（1为涨 0为跌）
     */
    private int type;

    /**
     * <p>
     * StickEntityのコンストラクター
     * </p>
     *
     * @param high <p>
     *             最高价
     *             </p>
     * @param low  <p>
     *             最低值
     *             </p>
     * @param date <p>
     *             日期
     *             </p>
     */
    public StickEntity(double high, double low, String date) {
        super();
        this.high = high;
        this.low = low;
        this.date = date;
    }

    /**
     * <p>
     * StickEntityのコンストラクター
     * </p>
     *
     * @param high <p>
     *             最高价
     *             </p>
     * @param type <p>
     *             最低值
     *             </p>
     * @param date <p>
     *             日期
     *             </p>
     */
    public StickEntity(double high, int type, String date) {
        super();
        this.high = high;
        this.type = type;
        this.date = date;
    }

    /**
     * <p>
     * StickEntity类对象的构造函数
     * </p>
     */
    public StickEntity() {
        super();
    }

    /**
     * @return the high
     */
    public double getHigh() {
        return high;
    }

    /**
     * @param high the high to set
     */
    public void setHigh(double high) {
        this.high = high;
    }

    /**
     * @return the low
     */
    public double getLow() {
        return low;
    }

    /**
     * @param low the low to set
     */
    public void setLow(double low) {
        this.low = low;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}
