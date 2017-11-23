package com.example.sjqcjstock.entity.Article;

import java.util.List;

/**
 * 文章相关信息内容
 * Created by Administrator on 2017/7/5.
 */
public class FeedEntity {
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
    // 简介
    private String introduction;
    // 附图
    private String attach_url;
    // 标题
    private String title;
    // 需打赏水晶币个数
    private String reward;
    // 是否订阅  0否  1是
    private String PayState;
    // 是否收藏  0否  1是
    private String Collection;
    // 是否点赞  0否  1是
    private String is_digg;
    // 打赏总次数
    private String reward_count;
    // 打赏总金额
    private String amount_count;
    public List<asslist> asslist;

    public String getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(String feed_id) {
        this.feed_id = feed_id;
    }

    public String getAttach_url() {
        return attach_url;
    }

    public void setAttach_url(String attach_url) {
        this.attach_url = attach_url;
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
        this.feed_content = feed_content;
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

    public String getPayState() {
        return PayState;
    }

    public void setPayState(String payState) {
        PayState = payState;
    }

    public String getCollection() {
        return Collection;
    }

    public void setCollection(String collection) {
        Collection = collection;
    }

    public String getIs_digg() {
        return is_digg;
    }

    public void setIs_digg(String is_digg) {
        this.is_digg = is_digg;
    }

    public String getReward_count() {
        return reward_count;
    }

    public void setReward_count(String reward_count) {
        this.reward_count = reward_count;
    }

    public String getAmount_count() {
        return amount_count;
    }

    public void setAmount_count(String amount_count) {
        this.amount_count = amount_count;
    }

    public List<FeedEntity.asslist> getAsslist() {
        return asslist;
    }

    public void setAsslist(List<FeedEntity.asslist> asslist) {
        this.asslist = asslist;
    }

    /**
     * 打赏人信息
     */
    public class asslist{
        private String uid;
        private String uname;
        // 头像
        private String avatar_tiny;
        // 打赏金额
        private String amount;

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

        public String getAvatar_tiny() {
            return avatar_tiny;
        }

        public void setAvatar_tiny(String avatar_tiny) {
            this.avatar_tiny = avatar_tiny;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }
}
