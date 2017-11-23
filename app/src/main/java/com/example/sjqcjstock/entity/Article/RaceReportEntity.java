package com.example.sjqcjstock.entity.Article;

/**
 * 赛程报道
 * Created by Administrator on 2017/7/18.
 */
public class RaceReportEntity {

    // id
    private String id;
    // 标题
    private String title;
    // 发布时间
    private String time;
    // 微博id
    private String feed_id;
    // 评论数
    private String comment_count;
    // 用户id
    private String uid;
    // 用户名称
    private String uname;
    // banner地址
    private String img;
    // 发布时间
    private String ctime;
    // vip
    private String user_group_icon_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(String feed_id) {
        this.feed_id = feed_id;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getUser_group_icon_url() {
        return user_group_icon_url;
    }

    public void setUser_group_icon_url(String user_group_icon_url) {
        this.user_group_icon_url = user_group_icon_url;
    }
}
