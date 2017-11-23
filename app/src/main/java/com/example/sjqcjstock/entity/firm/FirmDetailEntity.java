package com.example.sjqcjstock.entity.firm;

/**
 * 实盘详情实体类
 * Created by Administrator on 2017/9/7.
 */
public class FirmDetailEntity {
    private String id;
    private String uid;
    private String uname;
    // 收益率
    private String total_ratio;
    // 排名
    private String rank;
    // 是否订阅 1 是 0否
    private String is_desert;
    // 打赏水晶币
    private String desert_reward;

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

    public String getIs_desert() {
        return is_desert;
    }

    public void setIs_desert(String is_desert) {
        this.is_desert = is_desert;
    }

    public String getDesert_reward() {
        return desert_reward;
    }

    public void setDesert_reward(String desert_reward) {
        this.desert_reward = desert_reward;
    }
}
