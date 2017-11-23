package com.example.sjqcjstock.entity.firm;

/**
 * 实盘比赛列表信息
 * Created by Administrator on 2017/8/28.
 */
public class FirmHallEntity {

    //
    private String id;
    // 比赛名称
    private String title;
    // 发起人id
    private String uid;
    // 发起人名称
    private String uname;
    // 比赛开始时间
    private String start;
    // 比赛结束时间
    private String end;
    // 报名开始时间
    private String join_start;
    // 报名结束时间
    private String join_end;
    // 比赛状态 1报名中  2进行中  3已结束  4待开始
    private String state;
    // 剩余天数
    private String residue;
    // 参与人数
    private String count;
    // 总金额
    private String amount;
    // 链接文章ID
    private String feed_id;
    // 平均总收益
    private String avg_ratio;
    // 是否参加比赛 0未参加  1参加
    private String is_join;
    // 主办方
    private String sponsor;

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

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getJoin_start() {
        return join_start;
    }

    public void setJoin_start(String join_start) {
        this.join_start = join_start;
    }

    public String getJoin_end() {
        return join_end;
    }

    public void setJoin_end(String join_end) {
        this.join_end = join_end;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getResidue() {
        return residue;
    }

    public void setResidue(String residue) {
        this.residue = residue;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(String feed_id) {
        this.feed_id = feed_id;
    }

    public String getAvg_ratio() {
        return avg_ratio;
    }

    public void setAvg_ratio(String avg_ratio) {
        this.avg_ratio = avg_ratio == null ? "0" : avg_ratio;
    }

    public String getIs_join() {
        return is_join;
    }

    public void setIs_join(String is_join) {
        this.is_join = is_join;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }
}
