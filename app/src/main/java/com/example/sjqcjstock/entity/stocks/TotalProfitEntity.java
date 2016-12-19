package com.example.sjqcjstock.entity.stocks;

/**
 * 各个排行榜的实体类
 * Created by Administrator on 2016/11/21.
 */
public class TotalProfitEntity {

    // 用户id
    private String uid;
    // 总盈利率
    private String total_rate;
    // 用户名
    private String username;
    // 选股成功率
    private String success_rate;
    // 平均交易天数
    private String avg_position_day;
    // 周平均率
    private String week_avg_profit_rate;
    // 仓位
    private String position;
    // 排名
    private String rownum;
    // 粉丝数
    private String fans;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTotal_rate() {
        return total_rate;
    }

    public void setTotal_rate(String total_rate) {
        this.total_rate = total_rate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSuccess_rate() {
        return success_rate;
    }

    public void setSuccess_rate(String success_rate) {
        this.success_rate = success_rate;
    }

    public String getAvg_position_day() {
        return avg_position_day;
    }

    public void setAvg_position_day(String avg_position_day) {
        this.avg_position_day = avg_position_day;
    }

    public String getWeek_avg_profit_rate() {
        return week_avg_profit_rate;
    }

    public void setWeek_avg_profit_rate(String week_avg_profit_rate) {
        this.week_avg_profit_rate = week_avg_profit_rate;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getRownum() {
        return rownum;
    }

    public void setRownum(String rownum) {
        this.rownum = rownum;
    }

    public String getFans() {
        return fans;
    }

    public void setFans(String fans) {
        this.fans = fans;
    }
}
