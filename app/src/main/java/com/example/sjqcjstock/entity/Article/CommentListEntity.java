package com.example.sjqcjstock.entity.Article;

/**
 * 评论列表的实体类
 * Created by Administrator on 2017/7/10.
 */
public class CommentListEntity {
    // 评论列表id
    private String comment_id;
    // 评论内容
    private String content;
    // 楼层
    private String storey;
    // 评论时间
    private String ctime;
    // 用户id
    private String uid;
    // 用户名称
    private String uname;
    // 用户头像
    private String avatar_middle;
    // 用户组
    private String user_group_icon_url;

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStorey() {
        return storey;
    }

    public void setStorey(String storey) {
        this.storey = storey;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
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
}
