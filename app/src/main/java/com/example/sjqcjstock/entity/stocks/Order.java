package com.example.sjqcjstock.entity.stocks;

/**
 * 订单相关信息的实体类
 * Created by Administrator on 2016/11/11.
 */
public class Order {
    //
    private String id;
    // 用户id
    private String uid;
    // 股票代码
    private String stock;
    // 指定的购买价格
    private String price;
    // 购买数量
    private String number;
    // 委托时间
    private String time;
    // 成交时间
    private String deal_time;
    // 购买类型 1为买入 2为卖出
    private String type;
    // 未成交  0代表带成交  1代表成交  2代表撤单
    private String status;
    // 手续费
    private String fee;
    // 股票名字
    private String stock_name;
    // 剩余可用资金
    private String available_funds;
    // 股票名称
    private String status_name;
    //
    private String username;
    // 委托价格
    private String entrustment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getStock_name() {
        return stock_name;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name;
    }

    public String getAvailable_funds() {
        return available_funds;
    }

    public void setAvailable_funds(String available_funds) {
        this.available_funds = available_funds;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeal_time() {
        return deal_time;
    }

    public void setDeal_time(String deal_time) {
        this.deal_time = deal_time;
    }

    public String getEntrustment() {
        return entrustment;
    }

    public void setEntrustment(String entrustment) {
        this.entrustment = entrustment;
    }
}
