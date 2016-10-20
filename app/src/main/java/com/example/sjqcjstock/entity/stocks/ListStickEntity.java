package com.example.sjqcjstock.entity.stocks;

import android.graphics.Color;

import java.util.List;

/**
 * <p>柱状图所用数据属性</p>
 *
 * @author menghuan
 * @version v1.0 2014-1-3 下午4:46:40
 */
public class ListStickEntity {


    /**
     * <p>
     * 默认表示柱条的边框颜色
     * </p>
     */
    public static final int DEFAULT_STICK_BORDER_COLOR = Color.rgb(235, 35, 65);

    /**
     * <p>
     * 默认表示柱条的填充颜色
     * </p>
     */
    public static final int DEFAULT_STICK_FILL_COLOR = Color.rgb(235, 35, 65);

    /**
     * <p>
     * 表示柱条的边框颜色
     * </p>
     */
    private int stickBorderColor = DEFAULT_STICK_BORDER_COLOR;

    /**
     * <p>
     * 表示柱条的填充颜色
     * </p>
     */
    private int stickFillColor = DEFAULT_STICK_FILL_COLOR;

    /**
     * <p>
     * 表示柱条的填充颜色
     * </p>
     */
    private int stickFillColor2 = DEFAULT_STICK_FILL_COLOR;

    /**
     * <p>
     * 绘制柱条用的数据
     * </p>
     */
    private List<StickEntity> StickData;

    /**
     * <p>
     * 绘制柱条用的数据
     * </p>
     */
    private int stickAlpha = 255;

    /**
     * @return the stickBorderColor
     */
    public int getStickBorderColor() {
        return stickBorderColor;
    }

    /**
     * @param stickBorderColor the stickBorderColor to set
     */
    public void setStickBorderColor(int stickBorderColor) {
        this.stickBorderColor = stickBorderColor;
    }

    /**
     * @return the stickFillColor
     */
    public int getStickFillColor() {
        return stickFillColor;
    }


    /**
     * @param stickFillColor the stickFillColor to set
     */
    public void setStickFillColor(int stickFillColor) {
        this.stickFillColor = stickFillColor;
    }

    /**
     * @return the stickFillColor
     */
    public int getStickFillColor2() {
        return stickFillColor2;
    }

    /**
     * @param stickFillColor2 the stickFillColor to set
     */
    public void setStickFillColor2(int stickFillColor2) {
        this.stickFillColor2 = stickFillColor2;
    }

    /**
     * @return the stickData
     */
    public List<StickEntity> getStickData() {
        return StickData;
    }

    public int getStickAlpha() {
        return stickAlpha;
    }

    public void setStickAlpha(int stickAlpha) {
        this.stickAlpha = stickAlpha;
    }

    public void setStickData(List<StickEntity> stickData) {
        this.StickData = stickData;
    }


}
