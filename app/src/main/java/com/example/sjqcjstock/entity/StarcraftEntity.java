package com.example.sjqcjstock.entity;

import java.util.ArrayList;

/**
 * 股王比赛实体类
 * Created by Administrator on 2017/7/20.
 */
public class StarcraftEntity {

    // 用户id
    private String uid;
    // 用户名称
    private String uname;
    // 头像
    private String avatar;
    // 现有资产
    private String max_assets;
    // 收益率
    private String ratio;
    // 选股信息
    private ArrayList<Suggest> suggest;

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMax_assets() {
        return max_assets;
    }

    public void setMax_assets(String max_assets) {
        this.max_assets = max_assets;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public ArrayList<Suggest> getSuggest() {
        return suggest;
    }

    public void setSuggest(ArrayList<Suggest> suggest) {
        this.suggest = suggest;
    }

    public static class Suggest{

        //
        private String suggest_id;
        //
        private String uid;
        //
        private String shop;
        //
        private String shopname ="";
        //
        private String shares ="";
        //
        private String shopnum ="";
        //
        private String shopmoney ="";
        //
        private String status;
        //
        private String create_time;
        //
        private String updata_time;

        public String getSuggest_id() {
            return suggest_id;
        }

        public void setSuggest_id(String suggest_id) {
            this.suggest_id = suggest_id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getShop() {
            return shop;
        }

        public void setShop(String shop) {
            this.shop = shop;
        }

        public String getShopname() {
            return shopname;
        }

        public void setShopname(String shopname) {
            this.shopname = shopname;
        }

        public String getShares() {
            return shares;
        }

        public void setShares(String shares) {
            this.shares = shares;
        }

        public String getShopnum() {
            return shopnum;
        }

        public void setShopnum(String shopnum) {
            this.shopnum = shopnum;
        }

        public String getShopmoney() {
            return shopmoney;
        }

        public void setShopmoney(String shopmoney) {
            this.shopmoney = shopmoney;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUpdata_time() {
            return updata_time;
        }

        public void setUpdata_time(String updata_time) {
            this.updata_time = updata_time;
        }
    }
}
