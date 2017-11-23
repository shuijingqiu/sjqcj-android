package com.example.sjqcjstock.entity.Article;

/**
 * 用户信息
 * Created by Administrator on 2017/7/5.
 */
public class UserEntity {
    private String uid;
    private String uname  = "";
    private String sex = "1";
    private String phone;
    // 简介
    private String intro = "";
    // 用户头像
    private String avatar_middle = "";
    // 用户组图标
    private String user_group_icon_url;
    // 最新关注数
    private String new_folower_count;
    // 未读@
    private String unread_atme;
    // 微博数
    private String weibo_count;
    // #未读评论
    private String unread_comment;
    // 粉丝
    private String follower_count;
    // 关注
    private String following_count;
    // 传递uid时返回该参数
    private followEntity follow;

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
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

    public String getNew_folower_count() {
        return new_folower_count;
    }

    public void setNew_folower_count(String new_folower_count) {
        this.new_folower_count = new_folower_count;
    }

    public String getUnread_atme() {
        return unread_atme;
    }

    public void setUnread_atme(String unread_atme) {
        this.unread_atme = unread_atme;
    }

    public String getWeibo_count() {
        return weibo_count;
    }

    public void setWeibo_count(String weibo_count) {
        this.weibo_count = weibo_count;
    }

    public String getUnread_comment() {
        return unread_comment;
    }

    public void setUnread_comment(String unread_comment) {
        this.unread_comment = unread_comment;
    }

    public String getFollower_count() {
        return follower_count;
    }

    public void setFollower_count(String follower_count) {
        this.follower_count = follower_count;
    }

    public String getFollowing_count() {
        return following_count;
    }

    public void setFollowing_count(String following_count) {
        this.following_count = following_count;
    }

    public followEntity getFollow() {
        return follow;
    }

    public void setFollow(followEntity follow) {
        this.follow = follow;
    }

    public class followEntity{
        // 是否关注  1是  0否
        private String following;
        // 是否是粉丝  1是  0否
        private String follower;

        public String getFollowing() {
            return following;
        }

        public void setFollowing(String following) {
            this.following = following;
        }

        public String getFollower() {
            return follower;
        }

        public void setFollower(String follower) {
            this.follower = follower;
        }
    }
}
