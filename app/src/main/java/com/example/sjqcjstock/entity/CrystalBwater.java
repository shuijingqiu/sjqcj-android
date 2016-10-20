package com.example.sjqcjstock.entity;

import java.util.List;

/**
 * 流水信息的实体
 * Created by Administrator on 2016/5/6.
 */
public class CrystalBwater {

    private String status;
    private List<msgs> msg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<msgs> getMsg() {
        return msg;
    }

    public void setMsg(List<msgs> msg) {
        this.msg = msg;
    }


    public class msgs {
        private String id;
        private String uid;
        private String assist_uid;
        private String type;
        private String amount;
        private String time;
        private String oper_id;
        private String uname;

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

        public String getAssist_uid() {
            return assist_uid;
        }

        public void setAssist_uid(String assist_uid) {
            this.assist_uid = assist_uid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getOper_id() {
            return oper_id;
        }

        public void setOper_id(String oper_id) {
            this.oper_id = oper_id;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }
    }
}
