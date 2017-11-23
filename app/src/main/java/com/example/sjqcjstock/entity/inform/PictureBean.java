package com.example.sjqcjstock.entity.inform;

import java.io.Serializable;
import java.util.List;

/**新闻页点击大图js回传json数据格式
 * Created by sa on 2016/10/12.
 */
public class PictureBean implements Serializable{
    private int position;//点击新闻页中第几张图片
    private List<PictureEntity> imgs;//新闻页中图片的相关信息

    public int getPosition() {
        return position;
    }

    public List<PictureEntity> getImgs() {
        return imgs;
    }

    public void setImgs(List<PictureEntity> imgs) {
        this.imgs = imgs;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
