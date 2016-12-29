package com.example.sjqcjstock.entity.stocks;

import com.example.sjqcjstock.netutil.Utils;

/**
 * 订阅牛人信息的实体类
 * Created by Administrator on 2016/12/28.
 */
public class DesertEntity {

    // 订阅的id
    private String id;
    // 被订阅的人的uid
    private String price_uid;
    // 被订阅的人的名字
    private String price_username;
    // 剩余过期时间
    private String exp_time;
    // 1代表已经订阅   -1代表取消订阅
    private String status;
    // 创建时间
    private String time;
    // 订单号
    private String order_number;
    // 是否可以订阅
    private String is_extend;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice_uid() {
        return price_uid;
    }

    public void setPrice_uid(String price_uid) {
        this.price_uid = price_uid;
    }

    public String getPrice_username() {
        return price_username;
    }

    public void setPrice_username(String price_username) {
        this.price_username = price_username;
    }

    public String getExp_time() {
        return exp_time;
    }

    public void setExp_time(String exp_time) {
        this.exp_time = Utils.FormattingTime("yyyy-MM-dd HH:mm",exp_time);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time =  Utils.FormattingTime("yyyy-MM-dd HH:mm",time);
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getIs_extend() {
        return is_extend;
    }

    public void setIs_extend(String is_extend) {
        this.is_extend = is_extend;
    }
}
