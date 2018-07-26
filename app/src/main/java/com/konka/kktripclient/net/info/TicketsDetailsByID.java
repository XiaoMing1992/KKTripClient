package com.konka.kktripclient.net.info;

/**
 * Created by smith on 2017/06/05.
 */
public class TicketsDetailsByID implements IEvent {

    /**
     * ret : {"ret_code":"0","ret_msg":"success"}
     * data : {"id":23,"name":"香港迪士尼","description":"香港迪士尼","originalPrice":1,"discountType":"大屏专享价","discountPrice":1,"customService":"114","goodsType":1,"businessHours":"每天上午8点到晚上10点","score":0,"weight":11,"qrcode":"http://happyvalleywh.smartoct.com/weixin/?r=ticketwindow/index&id=2&ocid=10001&tcid=2&sn=fee8f0dfe2fb0c30da8ad162bf689f1a&param=access_token&code=061y9nG32bO6VK0MyrH32U4CG32y9nGX","thumbnail":"/kkupload/45/455000/F455000/170601102930TRAz.png","poster1":"/kkupload/45/455000/F455000/170601102933epFd.png","poster2":"","poster3":"","poster4":"","poster5":"","createDate":"2017-06-01 10:29:59","updateDate":"2017-06-01 17:08:53"}
     */

    private RetBean ret;
    private TicketsBean data;

    public RetBean getRet() {
        return ret;
    }

    public void setRet(RetBean ret) {
        this.ret = ret;
    }

    public TicketsBean getData() {
        return data;
    }


    public void setData(TicketsBean data) {
        this.data = data;
    }

    public class RetBean {
        /**
         * ret_code : 0
         * ret_msg : success
         */

        private String ret_code;
        private String ret_msg;

        public String getRet_code() {
            return ret_code;
        }

        public void setRet_code(String ret_code) {
            this.ret_code = ret_code;
        }

        public String getRet_msg() {
            return ret_msg;
        }

        public void setRet_msg(String ret_msg) {
            this.ret_msg = ret_msg;
        }
    }

    @Override
    public String toString() {
        return "TicketsDetailsByID{" +
                "ret=" + ret +
                ", data=" + data.toString() +
                '}';
    }
}