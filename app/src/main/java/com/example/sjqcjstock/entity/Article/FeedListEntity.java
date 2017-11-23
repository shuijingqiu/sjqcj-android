package com.example.sjqcjstock.entity.Article;

/**
 * 二级页面列表实体类
 * Created by Administrator on 2017/7/6.
 */
public class FeedListEntity {

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
    // 附图
    private String attach_url;
    //摘要
    private String introduction;
    // 标题
    private String title;
    // 需打赏水晶币个数
    private String reward;
    // 是否点赞  0否  1是
    private String is_digg;
    // 文章发布用户
    private UserEntity user_info;
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

    public String getAttach_url() {
        return attach_url;
    }

    public void setAttach_url(String attach_url) {
        this.attach_url = attach_url;
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

    public String getIs_digg() {
        return is_digg;
    }

    public void setIs_digg(String is_digg) {
        this.is_digg = is_digg;
    }

    public UserEntity getUser_info() {
        return user_info;
    }

    public void setUser_info(UserEntity user_info) {
        this.user_info = user_info;
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
        // 文章发布用户
        private UserEntity user_info = new UserEntity();

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

        public UserEntity getUser_info() {
            return user_info;
        }

        public void setUser_info(UserEntity user_info) {
            this.user_info = user_info;
        }
    }

}
