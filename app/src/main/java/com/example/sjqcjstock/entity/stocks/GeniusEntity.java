package com.example.sjqcjstock.entity.stocks;

/**
 * 牛人动态的实体类
 * Created by Administrator on 2016/11/28.
 */
public class GeniusEntity {

    private String id;
    private String uid;
    // 股票代码
    private String stock;
    // 价格
    private String price;
    // 时间
    private String time;
    // 1代表买入  2代表卖出
    private String type;
    // 股票名称
    private String stock_name;
    //
    private String sorts;
    // 用户名
    private String username;
    // 总盈利率
    private String total_rate;
    // 股票当前盈利率
    private String ratio;

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

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStock_name() {
        return stock_name;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name;
    }

    public String getSorts() {
        return sorts;
    }

    public void setSorts(String sorts) {
        this.sorts = sorts;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTotal_rate() {
        return total_rate;
    }

    public void setTotal_rate(String total_rate) {
        this.total_rate = total_rate;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }
}
