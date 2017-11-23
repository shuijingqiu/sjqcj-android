package com.example.sjqcjstock.entity.inform;

/**图片的相关信息
 * Created by Administrator on 2017/2/24.
 */

public class PictureEntity{
    private String simage;//小图
    private String limage;//大图
    private String content;//每一张图片对应的描述，预留字段，暂时还没用到，js端目前还不会回传这个字段

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSimage() {
        return simage;
    }

    public void setSimage(String simage) {
        this.simage = simage;
    }

    public String getLimage() {
        return limage;
    }

    public void setLimage(String limage) {
        this.limage = limage;
    }
}
