package com.example.sjqcjstock.entity;

/**
 * 资讯的实体类
 * Created by Administrator on 2017/7/17.
 */
public class InformationEntity {

    // id
    private String news_id;
    // 标题
    private String news_title;
    // 链接
    private String news_link;
    // 内容
    private String news_content;
    // 发布时间
    private String created;

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getNews_link() {
        return news_link;
    }

    public void setNews_link(String news_link) {
        this.news_link = news_link;
    }

    public String getNews_content() {
        return news_content;
    }

    public void setNews_content(String news_content) {
        this.news_content = news_content;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
