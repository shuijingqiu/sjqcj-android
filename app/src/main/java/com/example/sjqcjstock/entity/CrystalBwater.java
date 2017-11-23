package com.example.sjqcjstock.entity;

/**
 * 流水信息的实体
 * Created by Administrator on 2016/5/6.
 */
public class CrystalBwater {

    private String id;
    private String uid;
    private String assist_uid;
    private String uname;
    // 类型 1 支出 2 收入
    private String type;
    // 来源类型
    private String source;
    // 来源描述
    private String retype;
    // 水晶币数量
    private String amount;
    private String time;
    // 相关id
    private String oper_id;

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

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRetype() {
        return retype;
    }

    public void setRetype(String retype) {
        this.retype = retype;
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
}
