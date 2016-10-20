/*
 * OHLCEntity.java
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
 * 保存蜡烛线表示用的OHLC四个值的实体对象
 * </p>
 *
 * @author menghuan
 * @version v1.0 2013/12/25 12:19:06
 */
public class OHLCEntity {

    /**
     * <p>
     * 开盘价
     * </p>
     */
    private double open;

    /**
     * <p>
     * 最高价
     * </p>
     */
    private double high;

    /**
     * <p>
     * 最低价
     * </p>
     */
    private double low;

    /**
     * <p>
     * 收盘价
     * </p>
     */
    private double close;

    /**
     * <p>
     * 日期
     * </p>
     */
    private String date;

    /**
     * <p>
     * OHLCEntityのコンストラクター
     * </p>
     *
     * @param open  <p>
     *              开盘价
     *              </p>
     * @param high  <p>
     *              最高价
     *              </p>
     * @param low   <p>
     *              最低价
     *              </p>
     * @param close <p>
     *              收盘价
     *              </p>
     * @param date  <p>
     *              日期
     *              </p>
     */
    public OHLCEntity(double open, double high, double low, double close,
                      String date) {
        super();
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.date = date;
    }

    /**
     * <p>
     * OHLCEntityのコンストラクター
     * </p>
     */
    public OHLCEntity() {
        super();
    }

    /**
     * @return the open
     */
    public double getOpen() {
        return open;
    }

    /**
     * @param open the open to set
     */
    public void setOpen(double open) {
        this.open = open;
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
     * @return the close
     */
    public double getClose() {
        return close;
    }

    /**
     * @param close the close to set
     */
    public void setClose(double close) {
        this.close = close;
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
}
