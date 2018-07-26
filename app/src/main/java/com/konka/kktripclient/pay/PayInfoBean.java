package com.konka.kktripclient.pay;

/**
 * Created by Zhou Weilin on 2017-6-27.
 */


public class PayInfoBean {

    private String userid;
    private String username;
    private int userorigin;
    private int devicebrand;
    private String deviceid;
    private String deviceip;
    private int goodstype;
    private int goodsid;
    private String goodsname;
    private int amount;
    private String price;
    private String name;
    private String tel;
    private String address;

    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getUserid() {
        return userid;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public void setUserorigin(int userorigin) {
        this.userorigin = userorigin;
    }
    public int getUserorigin() {
        return userorigin;
    }

    public void setDevicebrand(int devicebrand) {
        this.devicebrand = devicebrand;
    }
    public int getDevicebrand() {
        return devicebrand;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }
    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceip(String deviceip) {
        this.deviceip = deviceip;
    }
    public String getDeviceip() {
        return deviceip;
    }

    public void setGoodstype(int goodstype) {
        this.goodstype = goodstype;
    }
    public int getGoodstype() {
        return goodstype;
    }

    public void setGoodsid(int goodsid) {
        this.goodsid = goodsid;
    }
    public int getGoodsid() {
        return goodsid;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }
    public String getGoodsname() {
        return goodsname;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    public int getAmount() {
        return amount;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public String getPrice() {
        return price;
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

    @Override
    public String toString() {
        return "PayInfoBean{" +
                "userid='" + userid + '\'' +
                ", username='" + username + '\'' +
                ", userorigin=" + userorigin +
                ", devicebrand=" + devicebrand +
                ", deviceid='" + deviceid + '\'' +
                ", deviceip='" + deviceip + '\'' +
                ", goodstype=" + goodstype +
                ", goodsid=" + goodsid +
                ", goodsname='" + goodsname + '\'' +
                ", amount=" + amount +
                ", price='" + price + '\'' +
                ", name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
