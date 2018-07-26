package com.konka.kktripclient.net.info;

import com.google.gson.annotations.JsonAdapter;
import com.konka.kktripclient.net.json.ListTypeAdapterFactory;

import java.util.List;

/**
 * Created by smith on 2017/06/01.
 */
public class TicketsDetailsEvent implements IEvent {

    /**
     * ret : {"ret_code":"0","ret_msg":"success"}
     * data : {"tickets":[{"id":22,"name":"香港迪士尼","description":"香港迪士尼","originalPrice":10001,"discountType":"大屏专享价","discountPrice":8001,"customService":"114","goodsType":1,"businessHours":"每天上午8点到晚上10点","score":0,"weight":0,"qrcode":"http://happyvalleywh.smartoct.com/weixin/?r=ticketwindow/index&id=2&ocid=10001&tcid=2&sn=fee8f0dfe2fb0c30da8ad162bf689f1a&param=access_token&code=061y9nG32bO6VK0MyrH32U4CG32y9nGX","thumbnail":"/kkupload/45/455000/F455000/170601110511QqwI.png","poster1":"/kkupload/45/455000/F455000/1706011104164qEd.png","poster2":"/kkupload/45/455000/F455000/170601110418UZs9.png","poster3":"/kkupload/45/455000/F455000/170601110421ORhx.png","poster4":"/kkupload/45/455000/F455000/170601110423Wlfm.png","poster5":"/kkupload/45/455000/F455000/170601110425EXkQ.png","createDate":"2017-05-31 20:51:45","updateDate":"2017-06-01 17:07:55"}],"pagination":{"page":1,"pageSize":1,"count":1}}
     */

    private RetBean ret;
    private DataBean data;

    public RetBean getRet() {
        return ret;
    }

    public void setRet(RetBean ret) {
        this.ret = ret;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
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

    public class DataBean {
        /**
         * tickets : [{"id":22,"name":"香港迪士尼","description":"香港迪士尼","originalPrice":10001,"discountType":"大屏专享价","discountPrice":8001,"customService":"114","goodsType":1,"businessHours":"每天上午8点到晚上10点","score":0,"weight":0,"qrcode":"http://happyvalleywh.smartoct.com/weixin/?r=ticketwindow/index&id=2&ocid=10001&tcid=2&sn=fee8f0dfe2fb0c30da8ad162bf689f1a&param=access_token&code=061y9nG32bO6VK0MyrH32U4CG32y9nGX","thumbnail":"/kkupload/45/455000/F455000/170601110511QqwI.png","poster1":"/kkupload/45/455000/F455000/1706011104164qEd.png","poster2":"/kkupload/45/455000/F455000/170601110418UZs9.png","poster3":"/kkupload/45/455000/F455000/170601110421ORhx.png","poster4":"/kkupload/45/455000/F455000/170601110423Wlfm.png","poster5":"/kkupload/45/455000/F455000/170601110425EXkQ.png","createDate":"2017-05-31 20:51:45","updateDate":"2017-06-01 17:07:55"}]
         * pagination : {"page":1,"pageSize":1,"count":1}
         */

        private PaginationBean pagination;
        @JsonAdapter(ListTypeAdapterFactory.class)
        private List<TicketsBean> tickets;

        public PaginationBean getPagination() {
            return pagination;
        }

        public void setPagination(PaginationBean pagination) {
            this.pagination = pagination;
        }

        public List<TicketsBean> getTickets() {
            return tickets;
        }

        public void setTickets(List<TicketsBean> tickets) {
            this.tickets = tickets;
        }

    }
}
