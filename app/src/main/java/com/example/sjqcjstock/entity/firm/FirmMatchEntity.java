package com.example.sjqcjstock.entity.firm;

/**
 * 比赛实盘信息
 * Created by Administrator on 2017/10/26.
 */
public class FirmMatchEntity {

    private String uid;
    // 比赛名称
    private String title;
    // 天数
    private String days;
    // 初始资产
    private String initial;
    // 昨日资产
    private String yesterday;
    // 现有资产
    private String assets;
    // 存取
    private String access;
    // 总收益率
    private String total_ratio;
    // 排名
    private String rank;
    // 5日周一率
    private String week_ratio;
    // 今日收益率
    private String days_ratio;
    // 昨日收益率
    private String yesterday_ratio;
    // 更新时间
    private String u_time;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getYesterday() {
        return yesterday;
    }

    public void setYesterday(String yesterday) {
        this.yesterday = yesterday;
    }

    public String getAssets() {
        return assets;
    }

    public void setAssets(String assets) {
        this.assets = assets;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getTotal_ratio() {
        return total_ratio;
    }

    public void setTotal_ratio(String total_ratio) {
        this.total_ratio = total_ratio;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getWeek_ratio() {
        return week_ratio;
    }

    public void setWeek_ratio(String week_ratio) {
        this.week_ratio = week_ratio;
    }

    public String getDays_ratio() {
        return days_ratio;
    }

    public void setDays_ratio(String days_ratio) {
        this.days_ratio = days_ratio;
    }

    public String getYesterday_ratio() {
        return yesterday_ratio;
    }

    public void setYesterday_ratio(String yesterday_ratio) {
        this.yesterday_ratio = yesterday_ratio;
    }

    public String getU_time() {
        return u_time;
    }

    public void setU_time(String u_time) {
        this.u_time = u_time;
    }
}
