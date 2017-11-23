package com.example.sjqcjstock.entity;

import java.util.List;

/**
 * 系统消息的实体类
 * Created by Administrator on 2016/5/4.
 */
public class    SystemMessage {
    private String code;
    private String msg;
    private List<datas> data;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<datas> getData() {
        return data;
    }

    public void setData(List<datas> data) {
        this.data = data;
    }

    public class datas {

        // 消息id
        private String id;
        // 标题
        private String title;
        // 是否已读
        private String is_read;
        // 内容
        private String body;
        // 关联id
        private String relate_id;
        // 时间
        private String ctime;

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

        public String getIs_read() {
            return is_read;
        }

        public void setIs_read(String is_read) {
            this.is_read = is_read;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getRelate_id() {
            return relate_id;
        }

        public void setRelate_id(String relate_id) {
            this.relate_id = relate_id;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        //        private String id;
//        private String uid;
//        private String node;
//        private String appname;
//        private String title;
//        private String body;
//        private String ctime;
//        private String is_read;
//        private String feed_id;
//        private String type;
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public String getUid() {
//            return uid;
//        }
//
//        public void setUid(String uid) {
//            this.uid = uid;
//        }
//
//        public String getNode() {
//            return node;
//        }
//
//        public void setNode(String node) {
//            this.node = node;
//        }
//
//        public String getAppname() {
//            return appname;
//        }
//
//        public void setAppname(String appname) {
//            this.appname = appname;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public void setTitle(String title) {
//            this.title = title;
//        }
//
//        public String getBody() {
//            return body;
//        }
//
//        public void setBody(String body) {
//            this.body = body;
//        }
//
//        public String getCtime() {
//            return ctime;
//        }
//
//        public void setCtime(String ctime) {
//            this.ctime = ctime;
//        }
//
//        public String getIs_read() {
//            return is_read;
//        }
//
//        public void setIs_read(String is_read) {
//            this.is_read = is_read;
//        }
//
//        public String getFeed_id() {
//            return feed_id;
//        }
//
//        public void setFeed_id(String feed_id) {
//            this.feed_id = feed_id;
//        }
//
//        public String getType() {
//            return type;
//        }
//
//        public void setType(String type) {
//            this.type = type;
//        }
    }
}
