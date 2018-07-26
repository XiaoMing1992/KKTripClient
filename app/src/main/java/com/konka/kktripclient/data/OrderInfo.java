package com.konka.kktripclient.data;

/**
 * 类描述：
 * 创建人：lihuiqi
 * 创建时间：2016-12-25 16:35
 */
public class OrderInfo {
    /**
     * sign : A31C80C4F5FE759A0D581C5DCCC5DBCB
     * cp_id : 1000000000000239
     * pay_amount : 1
     * price : 0.10
     * comboname : 黄冈教育半年套餐
     * buy_order_id : 29
     * cp_private_info : wu
     * notify_url : http://test.kkapp.com/kknyxserver/NotifyInfo/notifyPayResult.do
     * app_id : 1000000000001182
     * comboid : 5
     */
    private String sign;
    private String cp_id;
    private String pay_amount;
    private String price;
    private String comboname;
    private String buy_order_id;
    private String cp_private_info;
    private String notify_url;
    private String app_id;
    private String comboid;

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setCp_id(String cp_id) {
        this.cp_id = cp_id;
    }

    public void setPay_amount(String pay_amount) {
        this.pay_amount = pay_amount;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setComboname(String comboname) {
        this.comboname = comboname;
    }

    public void setBuy_order_id(String buy_order_id) {
        this.buy_order_id = buy_order_id;
    }

    public void setCp_private_info(String cp_private_info) {
        this.cp_private_info = cp_private_info;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public void setComboid(String comboid) {
        this.comboid = comboid;
    }

    public String getSign() {
        return sign;
    }

    public String getCp_id() {
        return cp_id;
    }

    public String getPay_amount() {
        return pay_amount;
    }

    public String getPrice() {
        return price;
    }

    public String getComboname() {
        return comboname;
    }

    public String getBuy_order_id() {
        return buy_order_id;
    }

    public String getCp_private_info() {
        return cp_private_info;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public String getApp_id() {
        return app_id;
    }

    public String getComboid() {
        return comboid;
    }
    /*  结构:
 {"app_id":"1000000000001182","buy_order_id":"29","comboid":"5","comboname":"黄冈教育半年套餐","cp_id":"1000000000000239","cp_private_info":"wu","notify_url":"http://test.kkapp.com/kknyxserver/NotifyInfo/notifyPayResult.do","pay_amount":"1","price":"0.10","sign":"A31C80C4F5FE759A0D581C5DCCC5DBCB"
     */


}
