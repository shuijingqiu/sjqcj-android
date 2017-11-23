package com.example.sjqcjstock.entity.firm;

/**
 * 操作感言的列表
 * Created by Administrator on 2017/10/26.
 */
public class FirmRemarkEntity {
    // 文章id
    private String feed_id;
    // 总结
    private String content;
    // 时间
    private String time;

    public String getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(String feed_id) {
        this.feed_id = feed_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
