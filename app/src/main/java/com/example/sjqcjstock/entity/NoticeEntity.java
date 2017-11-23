package com.example.sjqcjstock.entity;

/**
 * 公告和实体类的
 * Created by Administrator on 2017/5/4.
 */
public class NoticeEntity {
    private String id;
    // 标题
    private String title;
    // 链接
    private String url;
    // 0 链接 1微博 2 个人主页 3 交易 4 直播 5 投资计划
    private String type;
    // 相关ID
    private paramEntity param;

    public class paramEntity{
        private String uid;
        // 房间ID
        private String room_id;
        // 微博id
        private String feed_id;
        // 计划id
        private String dp_id;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public String getFeed_id() {
            return feed_id;
        }

        public void setFeed_id(String feed_id) {
            this.feed_id = feed_id;
        }

        public String getDp_id() {
            return dp_id;
        }

        public void setDp_id(String dp_id) {
            this.dp_id = dp_id;
        }
    }
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public paramEntity getParam() {
        return param;
    }

    public void setParam(paramEntity param) {
        this.param = param;
    }
}
