package com.example.sjqcjstock.entity.Tomlive;

/**
 * 问答消息的实体类
 * Created by Administrator on 2017/1/16.
 */
public class QuestionEntity {

    // 消息id
    private String id;
    // 名称
    private String username;
    // 发送者id
    private String uid;
    // 时间
    private String time;
    // 内容
    private String content;
    // 提问id
    private String parent_id;
    // 回答的JSON
    private String answer;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
