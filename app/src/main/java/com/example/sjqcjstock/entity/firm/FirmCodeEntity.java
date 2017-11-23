package com.example.sjqcjstock.entity.firm;

/**
 * 实盘股票实体类
 * Created by Administrator on 2017/9/7.
 */
public class FirmCodeEntity {
    // 股票名称
    private String name;
    // 股票代码
    private String code;
    // 涨幅
    private String ratio;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        if (code.length()>6){
            code = code.substring(2);
        }
        this.code = code;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio==null?"0":ratio;
    }
}
