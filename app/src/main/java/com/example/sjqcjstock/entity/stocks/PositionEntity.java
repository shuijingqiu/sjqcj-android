package com.example.sjqcjstock.entity.stocks;

/**
 * 用户持仓和未成交列表的Entity
 * Created by Administrator on 2016/11/16.
 */
public class PositionEntity {
    private String id;
    // 用户id
    private String uid;
    // 时间
    private String time;
    // 股票代码
    private String stock;
    // 股票代码
    private String stock_name;
    // 是否持有 1代表持有  2代表不持有
    private String is_position;
    // 成本价
    private String cost;
    // 最新资产
    private String assets;
    // 是否盈利
    private String is_profit;
    // 盈利比例额
    private String ratio;
    // 可卖数量
    private String available_number;
    // 持仓数量
    private String position_number;
    // 冻结数量
    private String freeze_number;
    // 这只股票总的手续费
    private String fee;
    // 最近操作时间
    private String update_time;
    // 平均成本价
    private String cost_price;
    // 是否假删除
    private String delete_time;
    // 账户类型
    private String sorts;

    // 指定的购买价格
    private String price;
    // 购买数量
    private String number;
    //  购买类型 1为买入 2为卖出
    private String type;
    // 未成交  0代表带成交  1代表成交  2代表撤单
    private String status;
    // 剩余可用资金
    private String available_funds;

    // 最新价格
    private String latest_price = "0";
    // 判断当前价格是涨是跌
    private String isType = "0";

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getStock_name() {
        return stock_name;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name;
    }

    public String getIs_position() {
        return is_position;
    }

    public void setIs_position(String is_position) {
        this.is_position = is_position;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getAssets() {
        return assets;
    }

    public void setAssets(String assets) {
        this.assets = assets;
    }

    public String getIs_profit() {
        return is_profit;
    }

    public void setIs_profit(String is_profit) {
        this.is_profit = is_profit;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getAvailable_number() {
        return available_number;
    }

    public void setAvailable_number(String available_number) {
        this.available_number = available_number;
    }

    public String getFreeze_number() {
        return freeze_number;
    }

    public void setFreeze_number(String freeze_number) {
        this.freeze_number = freeze_number;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getCost_price() {
        return cost_price;
    }

    public void setCost_price(String cost_price) {
        this.cost_price = cost_price;
    }

    public String getDelete_time() {
        return delete_time;
    }

    public void setDelete_time(String delete_time) {
        this.delete_time = delete_time;
    }

    public String getSorts() {
        return sorts;
    }

    public void setSorts(String sorts) {
        this.sorts = sorts;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAvailable_funds() {
        return available_funds;
    }

    public void setAvailable_funds(String available_funds) {
        this.available_funds = available_funds;
    }

    public String getLatest_price() {
        return latest_price;
    }

    public void setLatest_price(String latest_price) {
        this.latest_price = latest_price;
    }

    public String getIsType() {
        return isType;
    }

    public void setIsType(String isType) {
        this.isType = isType;
    }

    public String getPosition_number() {
        return position_number;
    }

    public void setPosition_number(String position_number) {
        this.position_number = position_number;
    }
}
