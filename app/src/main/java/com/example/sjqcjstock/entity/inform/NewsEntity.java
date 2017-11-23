package com.example.sjqcjstock.entity.inform;

/**js调用java方法传递的json字符串实体类
 * Created by Administrator on 2016/12/24.
 */

public class NewsEntity {
    //1.0版本字段
    private String title;//新闻标题
    private String detailUrl;//新闻详情的url
//    private List<PictureEntity> imageUrls;//新闻图片的集合
    private String source;//新闻来源
    private String time;//新闻时间

    //注意：新增以下两个字段只是为了便于解析推送消息，js端回传参数还是和1.0版本一致
    private String pushType;////推送类型  现有类型：news（普通新闻）,可扩展
    private String summary;//新闻摘要


    private String collected;//该字段为demo工程模拟收藏功能所加，忽略即可

    public String getCollected() {
        return collected;
    }

    public void setCollected(String collected) {
        this.collected = collected;
    }

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }


    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

//    public List<PictureEntity> getImageUrls() {
//        return imageUrls;
//    }
//
//    public void setImageUrls(List<PictureEntity> imageUrls) {
//        this.imageUrls = imageUrls;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl){
        this.detailUrl = detailUrl;
    }

    public String getSource(){
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
