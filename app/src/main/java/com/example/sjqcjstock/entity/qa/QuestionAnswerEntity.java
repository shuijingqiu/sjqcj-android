package com.example.sjqcjstock.entity.qa;

/**
 * 问答返回的
 * Created by Administrator on 2017/3/7.
 */
public class QuestionAnswerEntity {

    // 问答的id
    private String id;
    // 提问人的id
    private String uid;
    // 提问人名称
    private String quname;
    // 提问人简介
    private String q_intro;
    // 问题
    private String question;
    // 回答人Id
    private String answerer;
    // 回答
    private String answer;
    // 1已提问 2已回答 3已退款
    private String status;
    // 回答人名称
    private String auname;
    // 回答人简介
    private String a_intro;
    // 回答人头像
    private String a_avatar;
    // 1可视  2隐藏
    private String valid;
    // 查看的价格
    private String watch_price;
    // 回答时间
    private String update_time;
    // 提问时间
    private String time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getQuname() {
        return quname;
    }

    public void setQuname(String quname) {
        this.quname = quname;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswerer() {
        return answerer;
    }

    public void setAnswerer(String answerer) {
        this.answerer = answerer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuname() {
        return auname;
    }

    public void setAuname(String auname) {
        this.auname = auname;
    }

    public String getA_avatar() {
        return a_avatar;
    }

    public void setA_avatar(String a_avatar) {
        this.a_avatar = a_avatar;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time =update_time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getQ_intro() {
        return q_intro;
    }

    public void setQ_intro(String q_intro) {
        this.q_intro = q_intro;
    }

    public String getA_intro() {
        return a_intro;
    }

    public void setA_intro(String a_intro) {
        this.a_intro = a_intro;
    }

    public String getWatch_price() {
        return watch_price;
    }

    public void setWatch_price(String watch_price) {
        this.watch_price = watch_price;
    }
}
