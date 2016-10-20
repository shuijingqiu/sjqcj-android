package com.example.sjqcjstock.entity;

/**
 * 消息提示条数的实体类
 * Created by Administrator on 2016/9/18.
 */
public class UnreadCount {

    // 返回成功与否
    private String status;
    // 消息主体
    private UnreadInfo data = new UnreadInfo();

    public class UnreadInfo {
        // 打赏的消息条数
        private String unread_notify = "0";
        // @我的条数
        private int unread_atme = 0;
        // 未读评论数目
        private int unread_comment = 0;
        // 粉丝数
        private int new_folower_count = 0;
        // 私信的数量
        private int unread_message = 0;
        // 总的未读条数
        private int unread_total = 0;
        // 系统消息消息条数
        private int sys_message = 0;

        public String getUnread_notify() {
            return unread_notify;
        }

        public void setUnread_notify(String unread_notify) {
            this.unread_notify = unread_notify;
        }

        public int getUnread_atme() {
            return unread_atme;
        }

        public void setUnread_atme(int unread_atme) {
            this.unread_atme = unread_atme;
        }

        public int getUnread_comment() {
            return unread_comment;
        }

        public void setUnread_comment(int unread_comment) {
            this.unread_comment = unread_comment;
        }

        public int getNew_folower_count() {
            return new_folower_count;
        }

        public void setNew_folower_count(int new_folower_count) {
            this.new_folower_count = new_folower_count;
        }

        public int getUnread_message() {
            return unread_message;
        }

        public void setUnread_message(int unread_message) {
            this.unread_message = unread_message;
        }

        public int getUnread_total() {
            return unread_total;
        }

        public void setUnread_total(int unread_total) {
            this.unread_total = unread_total;
        }

        public int getSys_message() {
            return sys_message;
        }

        public void setSys_message(int sys_message) {
            this.sys_message = sys_message;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UnreadInfo getData() {
        return data;
    }

    public void setData(UnreadInfo data) {
        this.data = data;
    }
}
