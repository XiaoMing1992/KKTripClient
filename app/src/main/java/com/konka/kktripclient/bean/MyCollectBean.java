package com.konka.kktripclient.bean;

/**
 * Created by HP on 2017-5-17.
 */

public class MyCollectBean {
    /**
     * item的id
     */
    private int id;

    /**
     * item的id
     */
    private String user_id;

    /**
     * 缩略图的路径
     */
    private String thumbnail;

    /**
     * 商品id
     */
    private String goods_id;

    /**
     * 商品类型
     */
    private Integer goodsType;

    /**
     * 商品名称
     */
    private String goods_name;

    /**
     * 原价
     */
    private String original_price;

    /**
     * 售价
     */
    private String discount_price;

    /**
     * 订单生成时间
     */
    private String createDate;

    /**
     * 订单更新时间
     */
    private String updateDate;

    /**
     * 收藏的路线的状态
     */
    private Integer state;

    /**
     * 收藏的路线id
     */
    private Integer tourRouteId;

    /**
     * 收藏的门票id
     */
    private Integer ticketId;

    public MyCollectBean(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(String original_price) {
        this.original_price = original_price;
    }

    public void setDiscount_price(String discount_price) {
        this.discount_price = discount_price;
    }

    public String getDiscount_price() {
        return discount_price;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getState() {
        return state;
    }

    public void setGoodsType(Integer goodsType) {
        this.goodsType = goodsType;
    }

    public Integer getGoodsType() {
        return goodsType;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTourRouteId(Integer tourRouteId) {
        this.tourRouteId = tourRouteId;
    }

    public Integer getTourRouteId() {
        return tourRouteId;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
