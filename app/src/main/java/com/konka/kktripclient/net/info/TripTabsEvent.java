package com.konka.kktripclient.net.info;

/**
 * Created by smith on 2017/06/15.
 */
public class TripTabsEvent implements IEvent {

    /**
     * ret : {"ret_code":"0","ret_msg":"success"}
     * data : {"contentUrl":"/project/jkktripadmin/json/1497430201890.json","serverAddr":"http://test.kkapp.com","updateTime":1497430180000}
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
         * contentUrl : /project/jkktripadmin/json/1497430201890.json
         * serverAddr : http://test.kkapp.com
         * updateTime : 1497430180000
         */

        private String contentUrl;
        private String serverAddr;
        private long updateTime;

        public String getContentUrl() {
            return contentUrl;
        }

        public void setContentUrl(String contentUrl) {
            this.contentUrl = contentUrl;
        }

        public String getServerAddr() {
            return serverAddr;
        }

        public void setServerAddr(String serverAddr) {
            this.serverAddr = serverAddr;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }
    }
}
