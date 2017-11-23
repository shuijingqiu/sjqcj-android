package com.example.sjqcjstock.entity;

/**
 * 比赛信息的列表
 * Created by Administrator on 2017/7/18.
 */
public class MatchNewsEntity {

    // 数据id
    private String ballot_id;
    private String uid;
    private String uname;
    private String avatar_middle;
    // 总收益
    private String ballot_jifen;
    // 周均受益
    private String weekly_avg;
    // 周收益
    private String integration;
    // 参与周数
    private String weekly;
    // 股票代码
    private String shares;
    // 股票名称
    private String shares_name;
    // 股票价格
    private String price;
    // 第一只累计涨幅
    private String integration1;
    // 第一只最高涨幅
    private String integration3;
    //  股票代码2
    private String shares2;
    // 股票名称2
    private String shares2_name;
    // 股票价格2
    private String price2;
    // 第二只累计涨幅
    private String integration2;
    // 第二只最高涨幅
    private String integration4;
    // 是否付费 2为付费
    private String is_free;
    //  打赏微博ID
    private String feed_id;
    //  打赏水晶币个数
    private String reward;
    //
    private String types;

    public String getBallot_id() {
        return ballot_id;
    }

    public void setBallot_id(String ballot_id) {
        this.ballot_id = ballot_id;
    }

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

    public String getBallot_jifen() {
        return ballot_jifen;
    }

    public void setBallot_jifen(String ballot_jifen) {
        this.ballot_jifen = ballot_jifen;
    }

    public String getWeekly_avg() {
        return weekly_avg;
    }

    public void setWeekly_avg(String weekly_avg) {
        this.weekly_avg = weekly_avg;
    }

    public String getIntegration() {
        return integration;
    }

    public void setIntegration(String integration) {
        this.integration = integration;
    }

    public String getWeekly() {
        return weekly;
    }

    public void setWeekly(String weekly) {
        this.weekly = weekly;
    }

    public String getShares() {
        return shares;
    }

    public void setShares(String shares) {
        this.shares = shares;
    }

    public String getShares_name() {
        return shares_name;
    }

    public void setShares_name(String shares_name) {
        this.shares_name = shares_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIntegration1() {
        return integration1;
    }

    public void setIntegration1(String integration1) {
        this.integration1 = integration1;
    }

    public String getIntegration3() {
        return integration3;
    }

    public void setIntegration3(String integration3) {
        this.integration3 = integration3;
    }

    public String getShares2() {
        return shares2;
    }

    public void setShares2(String shares2) {
        this.shares2 = shares2;
    }

    public String getShares2_name() {
        return shares2_name;
    }

    public void setShares2_name(String shares2_name) {
        this.shares2_name = shares2_name;
    }

    public String getPrice2() {
        return price2;
    }

    public void setPrice2(String price2) {
        this.price2 = price2;
    }

    public String getIntegration2() {
        return integration2;
    }

    public void setIntegration2(String integration2) {
        this.integration2 = integration2;
    }

    public String getIntegration4() {
        return integration4;
    }

    public void setIntegration4(String integration4) {
        this.integration4 = integration4;
    }

    public String getIs_free() {
        return is_free;
    }

    public void setIs_free(String is_free) {
        this.is_free = is_free;
    }

    public String getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(String feed_id) {
        this.feed_id = feed_id;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }
}
