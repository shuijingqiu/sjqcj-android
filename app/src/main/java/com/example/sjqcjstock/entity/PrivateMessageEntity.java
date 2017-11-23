package com.example.sjqcjstock.entity;

/**
 * 私信消息的实体类
 * Created by Administrator on 2017/7/17.
 */
public class PrivateMessageEntity {

    // 列表id
    private String list_id;
    private String uid;
    private String uname;
    // 头像
    private String avatar_middle;
    // 用户组图标
    private String user_group_icon_url;
    // 内容
    private String content;
    // 最后聊天时间
    private String list_ctime;

    public String getList_id() {
        return list_id;
    }

    public void setList_id(String list_id) {
        this.list_id = list_id;
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

    public String getAvatar_middle() {
        return avatar_middle;
    }

    public void setAvatar_middle(String avatar_middle) {
        this.avatar_middle = avatar_middle;
    }

    public String getUser_group_icon_url() {
        return user_group_icon_url;
    }

    public void setUser_group_icon_url(String user_group_icon_url) {
        this.user_group_icon_url = user_group_icon_url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getList_ctime() {
        return list_ctime;
    }

    public void setList_ctime(String list_ctime) {
        this.list_ctime = list_ctime;
    }
}
