package com.example.sjqcjstock.entity;

/**
 * 收益榜的实体类
 * Created by Administrator on 2017/7/18.
 */
public class IncomeStatementEntity {

    private String id;
    private String uid;
    private String uname;
    private String avatar_middle;
    // 总收益
    private String ballot_jifen;
    // 周均受益
    private String avg_jefen;
    // 参与周数
    private String weekly;

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

    public String getAvg_jefen() {
        return avg_jefen;
    }

    public void setAvg_jefen(String avg_jefen) {
        this.avg_jefen = avg_jefen;
    }

    public String getWeekly() {
        return weekly;
    }

    public void setWeekly(String weekly) {
        this.weekly = weekly;
    }
}
