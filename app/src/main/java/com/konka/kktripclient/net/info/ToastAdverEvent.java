package com.konka.kktripclient.net.info;

/**
 * Created by smith on 2017/06/05.
 */
public class ToastAdverEvent implements IEvent {

    /**
     * ret : {"ret_code":"0","ret_msg":"success"}
     * data : {"id":5,"name":"弹窗1","type":1,"poster":"/kkupload/45/453000/F453000/170601202311McyP.png","resourceId":35,"tourRoute":null,"ticket":{"id":35,"name":"锦绣中华","description":"锦绣中华","originalPrice":200.88,"discountType":"大屏专享价","discountPrice":180,"customService":"114","goodsType":1,"businessHours":"每天上午8点到晚上10点","score":5,"weight":12,"qrcode":"http://happyvalleywh.smartoct.com/weixin/?r=ticketwindow/index&id=2&ocid=10001&tcid=2&sn=fee8f0dfe2fb0c30da8ad162bf689f1a&param=access_token&code=061y9nG32bO6VK0MyrH32U4CG32y9nGX","thumbnail":"/kkupload/45/455000/F455000/170531195829LtbP.png","poster1":"/kkupload/45/455000/F455000/170531195829LtbP.png","poster2":"string","poster3":"string","poster4":"string","poster5":"string","createDate":1496629451000,"updateDate":null},"video":null,"createDate":1494588373000,"updateDate":1496630180000}
     */

    private RetBean ret;
    private ToastAdverEventDataBean data;

    public RetBean getRet() {
        return ret;
    }

    public void setRet(RetBean ret) {
        this.ret = ret;
    }

    public ToastAdverEventDataBean getData() {
        return data;
    }

    public void setData(ToastAdverEventDataBean data) {
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

}