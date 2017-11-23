package com.example.sjqcjstock.entity;

/**
 * 推送的实体类
 * Created by Administrator on 2017/6/5.
 */
public class PushEntity {
    private String id;
    private String name;
    // 1为开启 2为关闭
    private String state;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
