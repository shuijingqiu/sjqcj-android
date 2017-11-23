package com.example.sjqcjstock.entity.Article;

/**
 * Created by Administrator on 2017/7/7.
 */
public class CommentEntity {

    // 评论id
    private String comment_id;
    // 评论内容
    private String content;
    // 评论时间
    private String ctime;
    // 发评论的用户信息
    private UserEntity user_info;
    // 评论的文章信息
    private SourceInfo sourceInfo;

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

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public UserEntity getUser_info() {
        return user_info;
    }

    public void setUser_info(UserEntity user_info) {
        this.user_info = user_info;
    }

    public SourceInfo getSourceInfo() {
        return sourceInfo;
    }

    public void setSourceInfo(SourceInfo sourceInfo) {
        this.sourceInfo = sourceInfo;
    }

    public class SourceInfo{
        // 文章id
        private String feed_id;
        // 文章类型
        private String type;
        // 文章标题
        private String title;
        // 用户简介
        private String introduction;
        // 打赏水晶币个数
        private String reward;
        // 文章内容
        private String feed_content;
        // 用户id
        private String uid;
        // 用户名称
        private String uname;
        // 用户组图标
        private String user_group_icon_url;

        public String getFeed_id() {
            return feed_id;
        }

        public void setFeed_id(String feed_id) {
            this.feed_id = feed_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getReward() {
            return reward;
        }

        public void setReward(String reward) {
            this.reward = reward;
        }

        public String getFeed_content() {
            return feed_content;
        }

        public void setFeed_content(String feed_content) {
            this.feed_content = feed_content;
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

        public String getUser_group_icon_url() {
            return user_group_icon_url;
        }

        public void setUser_group_icon_url(String user_group_icon_url) {
            this.user_group_icon_url = user_group_icon_url;
        }
    }
}
