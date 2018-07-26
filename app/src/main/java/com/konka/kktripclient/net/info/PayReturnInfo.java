package com.konka.kktripclient.net.info;

import com.alibaba.fastjson.JSON;

/**
 * Created by smith on 2017/06/19.
 */
public class PayReturnInfo {

    /**
     * amount : 1
     * appId : 1000000000000023
     * cpId : 1000000000000001
     * cpPrivateInfo : {"address":"中国-全部","name":"1","tel":"3"}
     * goodsId : 40
     * goodsName : 阿尔山森林草原深处秘境之旅
     * goodsType : 0
     * notifyUrl : http://test.kkapp.com/kktripserver/pay/notify
     * orderId : 114
     * price : 0.01
     * sign : D4FDE911B86924DAC3B9D021D1629422
     * userId : 69190
     * userName : 136****8581
     * userOrigin : 0
     */

    private int amount;
    private String appId;
    private String cpId;
    private CpPrivateInfoBean cpPrivateInfo;
    private int goodsId;
    private String goodsName;
    private int goodsType;
    private String notifyUrl;
    private String orderId;
    private String price;
    private String sign;
    private String userId;
    private String userName;
    private int userOrigin;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getCpId() {
        return cpId;
    }

    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    public CpPrivateInfoBean getCpPrivateInfo() {
        return cpPrivateInfo;
    }

    public void setCpPrivateInfo(CpPrivateInfoBean cpPrivateInfo) {
        this.cpPrivateInfo = cpPrivateInfo;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public int getUserOrigin() {
        return userOrigin;
    }

    public void setUserOrigin(int userOrigin) {
        this.userOrigin = userOrigin;
    }

    public class CpPrivateInfoBean {
        /**
         * address : 中国-全部
         * name : 1
         * tel : 3
         */

        private String address;
        private String name;
        private String tel;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }
    }

    @Override
    public String toString() {
        return "PayReturnInfo{" +
                "amount=" + amount +
                ", appId='" + appId + '\'' +
                ", cpId='" + cpId + '\'' +
                ", cpPrivateInfo=" + cpPrivateInfo.toString() +
                ", goodsId=" + goodsId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsType=" + goodsType +
                ", notifyUrl='" + notifyUrl + '\'' +
                ", orderId='" + orderId + '\'' +
                ", price='" + price + '\'' +
                ", sign='" + sign + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userOrigin=" + userOrigin +
                '}';
    }
}
