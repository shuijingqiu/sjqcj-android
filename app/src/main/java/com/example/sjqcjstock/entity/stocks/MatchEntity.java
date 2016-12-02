package com.example.sjqcjstock.entity.stocks;

/**
 * 模拟比赛的实体类
 * Created by Administrator on 2016/11/28.
 */
public class MatchEntity {
    private String id;
    // 比赛名称
    private String name;
    //类型  1周赛  2月赛
    private String type;
    // 开始日期
    private String start_date;
    // 结束日期
    private String end_date;
    // 0未参加 1已参加
    private String joined;
    // 1 进行中 3已结束
    private String status;
    private String status_name;

    // 用户id
    private String uid;
    // 用户名称
    private String user_name;
    // 比赛排名
    private String ranking;
    // 比赛收益
    private String total_rate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getJoined() {
        return joined;
    }

    public void setJoined(String joined) {
        this.joined = joined;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getTotal_rate() {
        return total_rate;
    }

    public void setTotal_rate(String total_rate) {
        this.total_rate = total_rate;
    }
}
