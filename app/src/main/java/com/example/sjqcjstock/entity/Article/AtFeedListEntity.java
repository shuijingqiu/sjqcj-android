package com.example.sjqcjstock.entity.Article;

/**
 * at我的列表实体类
 * Created by Administrator on 2017/7/25.
 */
public class AtFeedListEntity {

    // 文章ID
    private String feed_id;
    // 文章类型post短微博 long_post长微博 paid_post付费微博 repost转发微博 postimage图片微博
    private String type;
    // 发布时间
    private String publish_time;
    // 评论数
    private String comment_count;
    // 转发数
    private String repost_count;
    // 点赞数
    private String digg_count;
    // 内容
    private String feed_content;
    //摘要
    private String introduction;
    // 标题
    private String title;
    // 需打赏水晶币个数
    private String reward;
    private String uid;
    private String uname;
    private String avatar_middle;
    private String user_group_icon_url;

    // 转发微博的信息
    public ApiSource api_source;

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

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getRepost_count() {
        return repost_count;
    }

    public void setRepost_count(String repost_count) {
        this.repost_count = repost_count;
    }

    public String getDigg_count() {
        return digg_count;
    }

    public void setDigg_count(String digg_count) {
        this.digg_count = digg_count;
    }

    public String getFeed_content() {
        return feed_content;
    }

    public void setFeed_content(String feed_content) {
        this.feed_content = feed_content == null ?"":feed_content;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
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

    public ApiSource getApi_source() {
        return api_source;
    }

    public void setApi_source(ApiSource api_source) {
        this.api_source = api_source;
    }

    // 转发微博的信息
    public class ApiSource{
        // 文章ID
        private String feed_id;
        // 文章类型post短微博 long_post长微博 paid_post付费微博 repost转发微博 postimage图片微博
        private String type;
        // 内容
        private String feed_content;
        //摘要
        private String introduction;
        // 标题
        private String title;
        // 需打赏水晶币个数
        private String reward;
        private String uid;
        private String uname;
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

        public String getFeed_content() {
            return feed_content;
        }

        public void setFeed_content(String feed_content) {
            this.feed_content = feed_content==null?"":feed_content;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getReward() {
            return reward;
        }

        public void setReward(String reward) {
            this.reward = reward;
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
