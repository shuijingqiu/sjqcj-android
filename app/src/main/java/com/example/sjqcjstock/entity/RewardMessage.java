package com.example.sjqcjstock.entity;

import java.util.List;

/**
 * 打赏消息的实体类
 * Created by Administrator on 2016/5/4.
 */
public class RewardMessage {
    private String status;
    private String msg;
    private List<datas> data;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<datas> getData() {
        return data;
    }

    public void setData(List<datas> data) {
        this.data = data;
    }

    public class datas {
        private String uid;
        private String uname;
        private String avatar_tiny;
        private String amount;

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

        public String getAvatar_tiny() {
            return avatar_tiny;
        }

        public void setAvatar_tiny(String avatar_tiny) {
            this.avatar_tiny = avatar_tiny;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }
}
