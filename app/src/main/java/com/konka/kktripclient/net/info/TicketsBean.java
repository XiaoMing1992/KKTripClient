package com.konka.kktripclient.net.info;

/**
 * Created by smith on 2017/06/05.
 */
public class TicketsBean implements IEvent  {
    /**
     * id : 22
     * name : 香港迪士尼
     * description : 香港迪士尼
     * originalPrice : 10001.0
     * discountType : 大屏专享价
     * discountPrice : 8001.0
     * customService : 114
     * goodsType : 1
     * businessHours : 每天上午8点到晚上10点
     * score : 0.0
     * weight : 0
     * qrcode : http://happyvalleywh.smartoct.com/weixin/?r=ticketwindow/index&id=2&ocid=10001&tcid=2&sn=fee8f0dfe2fb0c30da8ad162bf689f1a&param=access_token&code=061y9nG32bO6VK0MyrH32U4CG32y9nGX
     * thumbnail : /kkupload/45/455000/F455000/170601110511QqwI.png
     * poster1 : /kkupload/45/455000/F455000/1706011104164qEd.png
     * poster2 : /kkupload/45/455000/F455000/170601110418UZs9.png
     * poster3 : /kkupload/45/455000/F455000/170601110421ORhx.png
     * poster4 : /kkupload/45/455000/F455000/170601110423Wlfm.png
     * poster5 : /kkupload/45/455000/F455000/170601110425EXkQ.png
     * createDate : 2017-05-31 20:51:45
     * updateDate : 2017-06-01 17:07:55
     */

    private int id;
    private String name;
    private String description;
    private double originalPrice;
    private String discountType;
    private double discountPrice;
    private String customService;
    private int goodsType;
    private String businessHours;
    private double score;
    private int weight;
    private int state;
    private String qrcode;
    private String thumbnail;
    private String poster1;
    private String poster2;
    private String poster3;
    private String poster4;
    private String poster5;
    private String createDate;
    private String updateDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getCustomService() {
        return customService;
    }

    public void setCustomService(String customService) {
        this.customService = customService;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public String getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getPoster1() {
        return poster1;
    }

    public void setPoster1(String poster1) {
        this.poster1 = poster1;
    }

    public String getPoster2() {
        return poster2;
    }

    public void setPoster2(String poster2) {
        this.poster2 = poster2;
    }

    public String getPoster3() {
        return poster3;
    }

    public void setPoster3(String poster3) {
        this.poster3 = poster3;
    }

    public String getPoster4() {
        return poster4;
    }

    public void setPoster4(String poster4) {
        this.poster4 = poster4;
    }

    public String getPoster5() {
        return poster5;
    }

    public void setPoster5(String poster5) {
        this.poster5 = poster5;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public int getState() { return state;}

    public void setState(int state) { this.state = state;}

    @Override
    public String toString() {
        return "TicketsBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", originalPrice=" + originalPrice +
                ", discountType='" + discountType + '\'' +
                ", discountPrice=" + discountPrice +
                ", customService='" + customService + '\'' +
                ", goodsType=" + goodsType +
                ", businessHours='" + businessHours + '\'' +
                ", score=" + score +
                ", weight=" + weight +
                ", qrcode='" + qrcode + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", poster1='" + poster1 + '\'' +
                ", poster2='" + poster2 + '\'' +
                ", poster3='" + poster3 + '\'' +
                ", poster4='" + poster4 + '\'' +
                ", poster5='" + poster5 + '\'' +
                ", createDate='" + createDate + '\'' +
                ", updateDate='" + updateDate + '\'' +
                '}';
    }
}
