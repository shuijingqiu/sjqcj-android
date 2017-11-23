package com.example.sjqcjstock.entity.Tomlive;

import java.util.List;

/**
 * 房间信息
 * Created by Administrator on 2017/1/13.
 */
public class TomliveRoomEntity{

    // 房间id
    private String id;
    // 用户id
    private String uid;
    // 用户名
    private String username;
    // 简介
    private String remark;
    // 1收费  2免费
    private String type;
    // 1正常  6待审核
    private String status;
    // 到期时间
    private String exp_time;
    // 状态描述
    private String status_label;
    // 1推荐  0未推荐
    private String recommand;
    // 头像
    private String avatar;
    // 1直播中 0未直播
    private String live_status;
    // 未读消息数量
    private String live_count;
    // 个人简介
    private String intro;
    // 参与人数
    private String count;
    // 定价表
    private List<priceList> prices;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_label() {
        return status_label;
    }

    public void setStatus_label(String status_label) {
        this.status_label = status_label;
    }

    public String getRecommand() {
        return recommand;
    }

    public void setRecommand(String recommand) {
        this.recommand = recommand;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLive_status() {
        return live_status;
    }

    public void setLive_status(String live_status) {
        this.live_status = live_status;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getExp_time() {
        return exp_time;
    }

    public String getLive_count() {
        return live_count;
    }

    public void setLive_count(String live_count) {
        this.live_count = live_count;
    }

    public void setExp_time(String exp_time) {
        this.exp_time = exp_time;
    }

    public List<priceList> getPrices() {
        return prices;
    }

    public void setPrices(List<priceList> prices) {
        this.prices = prices;
    }

    public class priceList {

        private String exp_time;
        private String price;

        public String getExp_time() {
            return exp_time;
        }

        public void setExp_time(String exp_time) {
            this.exp_time = exp_time;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}
