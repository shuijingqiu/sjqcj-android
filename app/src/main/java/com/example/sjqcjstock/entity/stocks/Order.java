package com.example.sjqcjstock.entity.stocks;

/**
 * 订单相关信息的实体类
 * Created by Administrator on 2016/11/11.
 */
public class Order {
    //
    private String id;
    //
    private String uid;
    //
    private String stock;
    //
    private String price;
    //
    private String number;
    //
    private String time;
    //
    private String type;
    //
    private String status;
    //
    private String fee;
    //
    private String stock_name;
    //
    private String available_funds;
    //
    private String status_name;
    //
    private String username;

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
}
