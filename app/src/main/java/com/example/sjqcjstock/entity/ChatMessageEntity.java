package com.example.sjqcjstock.entity;

/**
 * 聊天消息的实体类
 * Created by Administrator on 2017/7/17.
 */
public class ChatMessageEntity {

    // 消息id
    private String message_id;
    // 对话id
    private String list_id;
    // 聊天人id
    private String from_uid;
    // 内容
    private String content;
    // 是否删除 0否 1是
    private String is_del;
    // 发送时间
    private String mtime;
    // 头像
    private String avatar_middle;

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getList_id() {
        return list_id;
    }

    public void setList_id(String list_id) {
        this.list_id = list_id;
    }

    public String getFrom_uid() {
        return from_uid;
    }

    public void setFrom_uid(String from_uid) {
        this.from_uid = from_uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIs_del() {
        return is_del;
    }

    public void setIs_del(String is_del) {
        this.is_del = is_del;
    }

    public String getMtime() {
        return mtime;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
    }

    public String getAvatar_middle() {
        return avatar_middle;
    }

    public void setAvatar_middle(String avatar_middle) {
        this.avatar_middle = avatar_middle;
    }
}
