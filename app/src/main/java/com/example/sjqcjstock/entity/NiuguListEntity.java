package com.example.sjqcjstock.entity;

/**
 * 牛股榜的实体信息
 * Created by Administrator on 2017/7/18.
 */
public class NiuguListEntity {

    private String uid;
    private String uname;
    private String avatar_middle;
    // 股票代码
    private String shares;
    // 股票名称
    private String ballot_name;
    // 是否显示  1是  2否
    private String is_free;
    // 最新价
    private String currentPrice;
    // 涨幅
    private String increase;
    // 1名人组  0精英组
    private String types;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getAvatar_middle() {
        return avatar_middle;
    }

    public void setAvatar_middle(String avatar_middle) {
        this.avatar_middle = avatar_middle;
    }

    public String getShares() {
        return shares;
    }

    public void setShares(String shares) {
        this.shares = shares;
    }

    public String getBallot_name() {
        return ballot_name;
    }

    public void setBallot_name(String ballot_name) {
        this.ballot_name = ballot_name;
    }

    public String getIs_free() {
        return is_free;
    }

    public void setIs_free(String is_free) {
        this.is_free = is_free;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getIncrease() {
        return increase;
    }

    public void setIncrease(String increase) {
        this.increase = increase;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }
}
