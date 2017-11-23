package com.example.sjqcjstock.entity.plan;

/**
 * 投资计划列表的实体类
 * Created by Administrator on 2017/4/24.
 */
public class PlanEntity {

    // 计划id
    private String id;
    // 用户ID
    private String uid;
    // 用户名称
    private String username;
    // 用户头像
    private String avatar;
    //保证金
    private String margin;
    //订阅价格
    private String desert_price;
    //股票初始价格
    private String price;
    //实际价格
    private String last_price;
    //计划收益率
    private String plan_ratio;
    //实际收益率
    private String income;
    // 计划止损率
    private String stop_loss_ratio;
    //开始时间
    private String start;
    //结束时间
    private String end;
    //多少交易日
    private String trade_days;
    // 股票代码
    private String stock;
    // 股票名称
    private String stock_name;
    // 股票标题
    private String title;
    // 理由
    private String reason;
    // 状态 1未开始   2进行中   3成功   4失败
    private String status;
    // 1已订阅 2未订阅 3 不可定义
    private String desert;
    // 购买人数
    private String buy_count;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMargin() {
        return margin;
    }

    public void setMargin(String margin) {
        this.margin = margin;
    }

    public String getDesert_price() {
        return desert_price;
    }

    public void setDesert_price(String desert_price) {
        this.desert_price = desert_price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLast_price() {
        return last_price;
    }

    public void setLast_price(String last_price) {
        this.last_price = last_price;
    }

    public String getPlan_ratio() {
        return plan_ratio;
    }

    public void setPlan_ratio(String plan_ratio) {
        this.plan_ratio = plan_ratio;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getStop_loss_ratio() {
        return stop_loss_ratio;
    }

    public void setStop_loss_ratio(String stop_loss_ratio) {
        this.stop_loss_ratio = stop_loss_ratio;
    }

    public String getStart() {
        return start.replace("-",".");
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end.replace("-",".");
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getTrade_days() {
        return trade_days;
    }

    public void setTrade_days(String trade_days) {
        this.trade_days = trade_days;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesert() {
        return desert;
    }

    public void setDesert(String desert) {
        this.desert = desert;
    }

    public String getBuy_count() {
        return buy_count;
    }

    public void setBuy_count(String buy_count) {
        this.buy_count = buy_count;
    }
}
