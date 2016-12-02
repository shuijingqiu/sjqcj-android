package com.example.sjqcjstock.entity.stocks;

/**
 * 自选股的实体类
 * Created by Administrator on 2016/11/23.
 */
public class OptionalEntity {

    // id
    private String id;
    // 股票代码
    private String stock;
    // 股票名称
    private String stock_name;
    // 选股时间
    private String time;
    // 最新价
    private String price;
    // 涨跌幅
    private String rising;
    // 是否涨跌
    private String istype;
    //
    private String follow;

    public String getIstype() {
        return istype;
    }

    public void setIstype(String istype) {
        this.istype = istype;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getStock_name() {
        return stock_name;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRising() {
        return rising;
    }

    public void setRising(String rising) {
        this.rising = rising;
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }
}
