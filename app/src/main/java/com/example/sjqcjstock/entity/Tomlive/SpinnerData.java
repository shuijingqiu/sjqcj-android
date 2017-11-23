package com.example.sjqcjstock.entity.Tomlive;

/**
 * 价格数据的加载类
 * Created by Administrator on 2017/1/17.
 */
public class SpinnerData {

    public SpinnerData(String time,String price){
        this.time = time;
        this.price = price;
    }

    // 时间
    private String time;
    // 价格
    private String price;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
