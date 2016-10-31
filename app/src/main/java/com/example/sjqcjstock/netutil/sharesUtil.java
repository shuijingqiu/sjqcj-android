package com.example.sjqcjstock.netutil;

import com.example.sjqcjstock.entity.stocks.StocksInfo;

import java.util.HashMap;

/**
 * 查询调用处理股票数据信息
 * Created by Administrator on 2016/10/24.
 */
public class sharesUtil {

    /**
     * 根据返回的值进行处理得到想要的数据（数据全面  K线图的时候调用  调用腾讯的）
     * @param code
     * @return 股票信息的实体类
     */
    public StocksInfo processData(String code) {

        // 获取该股票当天的分时数据(腾讯接口)
        String str = HttpUtil.getIntentData("http://qt.gtimg.cn/q=" + code);

        // 增长类型
        String increaseType = "1";
        StocksInfo stocksInfo = null;
        // 买入和卖出的价格
        HashMap<String,String> buySellMap = new HashMap<String, String>();
//        // 每只股票的数据
//        String[] shares = strData.split(";");
//        for (String str : shares) {
            if ("".equals(str.trim())) {
                return null;
            }
            stocksInfo = new StocksInfo();
            // 每只股票的详细数据
            String[] sharesMinute = str.split("~");
            if (sharesMinute.length < 2) {
                return null;
            }
            // 股票名称
            stocksInfo.setName(sharesMinute[1]);
            // 股票代码
            stocksInfo.setCode(sharesMinute[2]);
            // 当前价格
            stocksInfo.setSpotPrice(sharesMinute[3]);
            // 今开价格
            stocksInfo.setOpenPrice(sharesMinute[5]);
            // 涨跌
            stocksInfo.setHighsLows(sharesMinute[31]);
            // 涨跌百分比
            stocksInfo.setHighsLowsThan(sharesMinute[32]);
            // 昨收
            stocksInfo.setZuoShou(sharesMinute[4]);
            // 成交量
            stocksInfo.setVolume(sharesMinute[6]);
            // 换手率
            stocksInfo.setTurnover(sharesMinute[38]);
            // 最高
            stocksInfo.setHighest(sharesMinute[33]);
            // 内盘
            stocksInfo.setInvol(sharesMinute[8]);
            // 市盈率
            stocksInfo.setPERatio(sharesMinute[39]);
            // 最低
            stocksInfo.setMinimum(sharesMinute[34]);
            // 外盘
            stocksInfo.setOuterDisc(sharesMinute[7]);
            // 振幅
            stocksInfo.setAmplitude(sharesMinute[43]);
            // 成交额
            stocksInfo.setTurnoverVolume(sharesMinute[37]);
            // 总市值
            stocksInfo.setTotalMarketValue(sharesMinute[45]);
            // 流通市值
            stocksInfo.setCirculationarketValue(sharesMinute[44]);
            // 拼接要用的价格信息
//            strK = Utils.getNowDate1() + "|" + sharesMinute[5] + "|" + sharesMinute[3] + "|" + sharesMinute[33] + "|" + sharesMinute[34] + "|" + sharesMinute[6] + "|" + sharesMinute[30];
            buySellMap.put("buy1P", sharesMinute[9]);
            buySellMap.put("buy1N", sharesMinute[10]);
            buySellMap.put("buy2P", sharesMinute[11]);
            buySellMap.put("buy2N", sharesMinute[12]);
            buySellMap.put("buy3P", sharesMinute[13]);
            buySellMap.put("buy3N", sharesMinute[14]);
            buySellMap.put("buy4P", sharesMinute[15]);
            buySellMap.put("buy4N", sharesMinute[16]);
            buySellMap.put("buy5P", sharesMinute[17]);
            buySellMap.put("buy5N", sharesMinute[18]);

            buySellMap.put("sell1P", sharesMinute[19]);
            buySellMap.put("sell1N", sharesMinute[20]);
            buySellMap.put("sell2P", sharesMinute[21]);
            buySellMap.put("sell2N", sharesMinute[22]);
            buySellMap.put("sell3P", sharesMinute[23]);
            buySellMap.put("sell3N", sharesMinute[24]);
            buySellMap.put("sell4P", sharesMinute[25]);
            buySellMap.put("sell4N", sharesMinute[26]);
            buySellMap.put("sell5P", sharesMinute[27]);
            buySellMap.put("sell5N", sharesMinute[28]);

            Double highsLows = Double.valueOf(sharesMinute[31]);

            if (highsLows > 0) {
                increaseType = "1";
            } else {
                increaseType = "0";
            }
            buySellMap.put("increaseType", increaseType);
            stocksInfo.setBuySellMap(buySellMap);
//        }
        return stocksInfo;
    }

    /**
     * 根据返回的值进行处理得到想要的数据（数据较少  下单时候用  调用腾讯的）
     * @param code
     * @return 股票信息的实体类
     */
    public StocksInfo processOrderData(String code) {

        // 获取该股票当天的分时数据(腾讯接口)
        String str = HttpUtil.getIntentData("http://qt.gtimg.cn/q=" + code);
        // 增长类型
        String increaseType = "1";
        StocksInfo stocksInfo = null;
        // 买入和卖出的价格
        HashMap<String,String> buySellMap = new HashMap<String, String>();
        // 每只股票的数据
//        String[] shares = strData.split(";");
//        for (String str : shares) {
            stocksInfo = new StocksInfo();
            // 每只股票的详细数据
            String[] sharesMinute = str.split("~");
            if (sharesMinute.length < 2) {
                return processOrderDataXL(code);
            }
            // 股票名称
            stocksInfo.setName(sharesMinute[1]);
            // 股票代码
            stocksInfo.setCode(sharesMinute[2]);
            // 当前价格
            stocksInfo.setSpotPrice(sharesMinute[3]);
            // 涨停价格
            stocksInfo.setHighLimit(sharesMinute[47]);
            // 跌停价格
            stocksInfo.setPriceLimit(sharesMinute[48]);

            buySellMap.put("buy1P", sharesMinute[9]);
            buySellMap.put("buy1N", sharesMinute[10]);
            buySellMap.put("buy2P", sharesMinute[11]);
            buySellMap.put("buy2N", sharesMinute[12]);
            buySellMap.put("buy3P", sharesMinute[13]);
            buySellMap.put("buy3N", sharesMinute[14]);
            buySellMap.put("buy4P", sharesMinute[15]);
            buySellMap.put("buy4N", sharesMinute[16]);
            buySellMap.put("buy5P", sharesMinute[17]);
            buySellMap.put("buy5N", sharesMinute[18]);

            buySellMap.put("sell1P", sharesMinute[19]);
            buySellMap.put("sell1N", sharesMinute[20]);
            buySellMap.put("sell2P", sharesMinute[21]);
            buySellMap.put("sell2N", sharesMinute[22]);
            buySellMap.put("sell3P", sharesMinute[23]);
            buySellMap.put("sell3N", sharesMinute[24]);
            buySellMap.put("sell4P", sharesMinute[25]);
            buySellMap.put("sell4N", sharesMinute[26]);
            buySellMap.put("sell5P", sharesMinute[27]);
            buySellMap.put("sell5N", sharesMinute[28]);

            Double highsLows = Double.valueOf(sharesMinute[31]);

            if (highsLows > 0) {
                increaseType = "1";
            } else {
                increaseType = "0";
            }
            buySellMap.put("increaseType", increaseType);
            stocksInfo.setBuySellMap(buySellMap);
//        }
        return stocksInfo;
    }

    /**
     * 根据返回的值进行处理得到想要的数据（数据较少 下单时候用   调用新浪的）
     * @param code
     * @return 股票信息的实体类
     */
    private StocksInfo processOrderDataXL(String code){
// 获取该股票当天的分时数据(腾讯接口)
        String str = HttpUtil.getIntentData("http://hq.sinajs.cn/list=" + code);

        // 增长类型
        String increaseType = "1";
        StocksInfo stocksInfo = null;
        // 买入和卖出的价格
        HashMap<String,String> buySellMap = new HashMap<String, String>();
        str = str.substring(str.indexOf("\"")+1,str.lastIndexOf("\""));
        // 每只股票的数据
        if ("".equals(str.trim())) {
            return null;
        }
        stocksInfo = new StocksInfo();
        // 每只股票的详细数据
        String[] sharesMinute = str.split(",");
        if (sharesMinute.length < 1) {
            return null;
        }

        // 昨日收盘价格
        Double priceZ = Double.valueOf(sharesMinute[2]);
        // 当前价格
        Double priceD = Double.valueOf(sharesMinute[3]);

        // 股票名称
        stocksInfo.setName(sharesMinute[0]);
        // 股票代码
        stocksInfo.setCode(code);
        // 当前价格
        stocksInfo.setSpotPrice(priceD+"");
        // 涨停价格
        stocksInfo.setHighLimit(Utils.getNumberFormat1(priceZ*1.1+""));
        // 跌停价格
        stocksInfo.setPriceLimit(Utils.getNumberFormat1(priceZ*0.9+""));

        buySellMap.put("buy1P", Utils.getNumberFormat1(sharesMinute[11]));
        buySellMap.put("buy1N", Utils.getNumberSharesHq(sharesMinute[10]));
        buySellMap.put("buy2P", Utils.getNumberFormat1(sharesMinute[13]));
        buySellMap.put("buy2N", Utils.getNumberSharesHq(sharesMinute[12]));
        buySellMap.put("buy3P", Utils.getNumberFormat1(sharesMinute[15]));
        buySellMap.put("buy3N", Utils.getNumberSharesHq(sharesMinute[14]));
        buySellMap.put("buy4P", Utils.getNumberFormat1(sharesMinute[17]));
        buySellMap.put("buy4N", Utils.getNumberSharesHq(sharesMinute[16]));
        buySellMap.put("buy5P", Utils.getNumberFormat1(sharesMinute[19]));
        buySellMap.put("buy5N", Utils.getNumberSharesHq(sharesMinute[18]));

        buySellMap.put("sell1P", Utils.getNumberFormat1(sharesMinute[21]));
        buySellMap.put("sell1N", Utils.getNumberSharesHq(sharesMinute[20]));
        buySellMap.put("sell2P", Utils.getNumberFormat1(sharesMinute[23]));
        buySellMap.put("sell2N", Utils.getNumberSharesHq(sharesMinute[22]));
        buySellMap.put("sell3P", Utils.getNumberFormat1(sharesMinute[25]));
        buySellMap.put("sell3N", Utils.getNumberSharesHq(sharesMinute[24]));
        buySellMap.put("sell4P", Utils.getNumberFormat1(sharesMinute[27]));
        buySellMap.put("sell4N", Utils.getNumberSharesHq(sharesMinute[26]));
        buySellMap.put("sell5P", Utils.getNumberFormat1(sharesMinute[29]));
        buySellMap.put("sell5N", Utils.getNumberSharesHq(sharesMinute[28]));


        if (priceD > priceZ) {
            increaseType = "1";
        } else {
            increaseType = "0";
        }
        buySellMap.put("increaseType", increaseType);
        stocksInfo.setBuySellMap(buySellMap);
        return stocksInfo;
    }
}
