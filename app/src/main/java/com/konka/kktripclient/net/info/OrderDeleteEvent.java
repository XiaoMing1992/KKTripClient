package com.konka.kktripclient.net.info;

/**
 * Created by smith on 2017/07/03.
 */
public class OrderDeleteEvent implements IEvent {

    /**
     * data :
     * ret : {"ret_code":"IE457004","ret_msg":"订单未完成，不可删除"}
     */

    private String data;
    private RetBean ret;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public RetBean getRet() {
        return ret;
    }

    public void setRet(RetBean ret) {
        this.ret = ret;
    }

    public class RetBean {
        /**
         * ret_code : IE457004
         * ret_msg : 订单未完成，不可删除
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
