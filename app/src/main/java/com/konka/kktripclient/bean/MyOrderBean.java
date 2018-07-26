package com.konka.kktripclient.bean;

/**
 * Created by HP on 2017-5-23.
 */

public class MyOrderBean {
    /**
     * 订单id
     */
    private int id;

    /**
     * 用户电视串号
     */
    private String sn;

    /**
     * 时间戳
     */
    private String timestamp;

    /**
     * 缩略图的路径
     */
    private String thumbnail;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 登录应用的用户名称
     */
    private String userName;

    /**
     * 用户来源
     */
    private Integer userOrigin;

    /**
     * 商品类型
     */
    private Integer goodsType;

    /**
     * 商品id
     */
    private Integer goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 订单金额
     */
    private String price;

    /**
     * 商品单价
     */
    private String unitPrice;

    /**
     * 购买数量
     */
    private Integer amount;

    /**
     * 订单有四种状态（0:订单生成、1:用户已支付、2:商家确认、3:出行完成）
     */
    private Integer state;

    /**
     * 商户的私有信息的姓名
     */
    private String name;

    /**
     * 商户的私有信息的电话
     */
    private String tel;

    /**
     * 商户的私有信息的地址
     */
    private String address;

    /**
     * 商户订单私有信息
     */
    private String cpPrivateInfo;

    /**
     * 订单生成时间
     */
    private String createDate;

    /**
     * 订单更新时间
     */
    private String updateDate;


    public MyOrderBean(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setUserOrigin(Integer userOrigin) {
        this.userOrigin = userOrigin;
    }

    public Integer getUserOrigin() {
        return userOrigin;
    }

    public void setCpPrivateInfo(String cpPrivateInfo) {
        this.cpPrivateInfo = cpPrivateInfo;
    }

    public String getCpPrivateInfo() {
        return cpPrivateInfo;
    }

    public void setGoodsType(Integer goodsType) {
        this.goodsType = goodsType;
    }

    public Integer getGoodsType() {
        return goodsType;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getSn() {
        return sn;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getState() {
        return state;
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

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTel() {
        return tel;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
