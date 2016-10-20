package com.example.sjqcjstock.netutil.stocks;

import android.graphics.Color;

import com.example.sjqcjstock.entity.stocks.OHLCEntity;
import com.example.sjqcjstock.entity.stocks.SpotEntity;
import com.example.sjqcjstock.entity.stocks.StickEntity;
import com.example.sjqcjstock.netutil.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>en</p>
 * <p>jp</p>
 * <p>计算公式</p>
 *
 * @author limc
 * @version v1.0 2014-1-2 上午10:27:11
 */
public class CountUtil {

    //计算蜡线days日均线
    public static List<SpotEntity> initMA(int days, List<OHLCEntity> ohlc) {
        if (days < 2) {
            return null;
        }
        SpotEntity spot = null;
        List<SpotEntity> MA5Values = new ArrayList<SpotEntity>();
        float sum = 0;
        float avg = 0;
        for (int i = 0; i < ohlc.size(); i++) {
            float close = (float) ohlc.get(i).getClose();
            if (i < days) {
                sum = sum + close;
                avg = sum / (i + 1f);
            } else {
                sum = sum + close - (float) ohlc.get(i - days).getClose();
                avg = sum / days;
            }
            if (i < days) {
                avg = 0;
            }
            spot = new SpotEntity();
            spot.setSpotData(avg);
            MA5Values.add(spot);
        }
        return MA5Values;
    }

    //计算线days日均线
    public static List<SpotEntity> initVMA(int days, List<StickEntity> vol) {
        if (days < 2) {
            return null;
        }
        SpotEntity spot = null;
        List<SpotEntity> MA5Values = new ArrayList<SpotEntity>();

        float sum = 0;
        float avg = 0;
        for (int i = 0; i < vol.size(); i++) {
            float close = (float) vol.get(i).getHigh();
            if (i < days) {
                sum = sum + close;
                avg = sum / (i + 1f);
            } else {
                sum = sum + close - (float) vol.get(i - days).getHigh();
                avg = sum / days;
            }
            spot = new SpotEntity(avg, 8, Color.rgb(135, 135, 135), 2, Color.rgb(70, 81, 167));
            if (i % 4 != 0) {
                spot.setSpotType(0);
            } else {
                switch (days) {
                    case 5:
                        spot.setSpotType(5);
                        spot.setSpotR(4f);
                        break;
                    case 10:
                        spot.setSpotType(2);
                        break;
                    case 20:
                        spot.setSpotType(3);
                        break;
                    default:
                        spot.setSpotType(4);
                        break;
                }
            }
            MA5Values.add(spot);
        }
        return MA5Values;
    }


    //计算线days日均线
    public static List<SpotEntity> initVMA(List<StickEntity> vol) {
        SpotEntity spot = null;
        List<SpotEntity> listSpot = new ArrayList<SpotEntity>();
        float sum = 0;
        float avg = 0;
        for (int i = 0; i < vol.size(); i++) {
            float close = (float) vol.get(i).getHigh();
            spot = new SpotEntity(close, 8, Color.rgb(135, 135, 135), 2, Color.rgb(70, 81, 167));
            spot.setSpotType(0);
            listSpot.add(spot);
        }
        return listSpot;
    }


    /**
     * 根据最大，最小，开盘价来计算Y轴的标题
     */
    public static List<String> setYTitle(float maxY, float openF) {
        List<String> listStr = new ArrayList<String>();
        float val = 0f;
        val = (maxY - openF) / 3;
        listStr.add(Utils.getNumberFormat((openF - val * 3) + ""));
        listStr.add(Utils.getNumberFormat((openF - val * 2) + ""));
        listStr.add(Utils.getNumberFormat((openF - val) + ""));
        listStr.add(Utils.getNumberFormat((openF) + ""));
        listStr.add(Utils.getNumberFormat((openF + val) + ""));
        listStr.add(Utils.getNumberFormat((openF + val * 2) + ""));
        listStr.add(Utils.getNumberFormat((openF + val * 3) + ""));
        return listStr;
    }

    /**
     * 根据最大，最小，开盘价来计算Y轴的标题（对内）
     */
    public static List<String> setYTitleInner(float maxY, float openF) {
        List<String> listStr = new ArrayList<String>();
        if (openF == 0) {
            return listStr;
        }
        float val = 0f;
        val = (maxY - openF) / 2;
        listStr.add(Utils.getNumberFormat((openF + val * 2) + ""));
        listStr.add(Utils.getNumberFormat((maxY - openF) / openF * 100 + "") + "%");
        listStr.add(Utils.getNumberFormat((openF - val * 2) + ""));
        listStr.add("-" + Utils.getNumberFormat((maxY - openF) / openF * 100 + "") + "%");
        return listStr;
    }

}
