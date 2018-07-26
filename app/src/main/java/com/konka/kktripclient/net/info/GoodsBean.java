package com.konka.kktripclient.net.info;

/**
 * Created by smith on 2017/07/04.
 */
public class GoodsBean implements IEvent {
    /**
     * id : 44
     * name : 这个也是测试的
     * goodsType : 0
     * description : 这个也是测试的
     * originalPrice : 100.0
     * discountType : 大屏惊爆价
     * discountPrice : 50.0
     * video : null
     * score : 0.0
     * weight : 19
     * state : 1
     * thumbnail : /kkupload/45/454000/F454000/170619153415Taqw.jpg
     * customService : 12345678
     * departurePlace : 海南省-三亚市,辽宁省-鞍山市,河北省-秦皇岛市,河北省-石家庄市
     * departureDate : 周末
     * poster1 : /kkupload/45/454000/F454000/1706191534172T0S.jpg
     * poster2 : /kkupload/45/454000/F454000/170619153422onkW.jpg
     * poster3 : /kkupload/45/454000/F454000/170619153428BYK3.jpg
     * poster4 : /kkupload/45/454000/F454000/170619153432QQJG.jpg
     * poster5 : null
     * origin : 测试
     * createDate : 1497857669000
     * updateDate : 1498556490000
     */

    private int id;
    private String name;
    private int goodsType;
    private String description;
    private double originalPrice;
    private String discountType;
    private double discountPrice;
    private Object video;
    private double score;
    private int weight;
    private int state;
    private String thumbnail;
    private String customService;
    private String departurePlace;
    private String departureDate;
    private String poster1;
    private String poster2;
    private String poster3;
    private String poster4;
    private Object poster5;
    private String origin;
    private long createDate;
    private long updateDate;

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

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
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

    public Object getVideo() {
        return video;
    }

    public void setVideo(Object video) {
        this.video = video;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCustomService() {
        return customService;
    }

    public void setCustomService(String customService) {
        this.customService = customService;
    }

    public String getDeparturePlace() {
        return departurePlace;
    }

    public void setDeparturePlace(String departurePlace) {
        this.departurePlace = departurePlace;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
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

    public Object getPoster5() {
        return poster5;
    }

    public void setPoster5(Object poster5) {
        this.poster5 = poster5;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }
}
