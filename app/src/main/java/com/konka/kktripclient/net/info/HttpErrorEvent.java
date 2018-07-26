package com.konka.kktripclient.net.info;

import com.android.volley.VolleyError;

public class HttpErrorEvent extends VolleyError implements IEvent {
    String retCode;
    String retMsg;
    String req_type;

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public String getReq_type() {
        return req_type;
    }

    public void setReq_type(String req_type) {
        this.req_type = req_type;
    }
}
