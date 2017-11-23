package com.example.sjqcjstock.entity.plan;

/**
 * 投资计划列表的实体类
 * Created by Administrator on 2017/4/24.
 */
public class PlanRateEntity {

    // id
    private String id;
    // 用户ID
    private String uid;
    // 用户名称
    private String username;
    // 用户头像
    private String avatar;
    // 总数
    private String count;
    // 达标率
    private String success_ratio;
    // 成功个数
    private String success_count;
    // 总收益
    private String total_ratio;
    // 平均收益
    private String avg_ratio;

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

    public String getSuccess_ratio() {
        return success_ratio;
    }

    public void setSuccess_ratio(String success_ratio) {
        this.success_ratio = success_ratio;
    }

    public String getSuccess_count() {
        return success_count;
    }

    public void setSuccess_count(String success_count) {
        this.success_count = success_count;
    }

    public String getTotal_ratio() {
        return total_ratio;
    }

    public void setTotal_ratio(String total_ratio) {
        this.total_ratio = total_ratio;
    }

    public String getAvg_ratio() {
        return avg_ratio;
    }

    public void setAvg_ratio(String avg_ratio) {
        this.avg_ratio = avg_ratio;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
