package com.konka.kktripclient.net.info;

/**
 * Created by smith on 2017/06/13.
 */
public class PayRequestInfo {

    /**
     * ret : {"ret_code":"0","ret_msg":"success"}
     * data : {"ciphertext":"Gl8DDUHgBQeztILaPFUcoXg/oaXVMoNGvvZVE9CD1Y7cbP/ogyHJxZl9MbNbKEtfnA5Hs94P2Tw1KtKtU4WTyDgvQWSsyyK2fKctud0Y8SMNYXDrVcyIrNdDvdu5xT6cbr5lRReLpdRcrgUgNKYUdheov+QGZuxsaYd7iyIZkwgKkEipuSR6L4JMhqhV0rxnEBC/BpFIX0udTDuf50mroJA9Z+kYncBvl5fxkRtLa1UfMGnVAI5IOM9GsL42QjXV337SyCAlKC0/vQAT6WOOlbDx4IBKSByKmdYFsJYVpZog9FinMAbupti2A9xFX1c/YpiyakPyGTutWb/g/dbYsBnl2zeg/A4L6VOnTYgB9Hpfodg3ClHUFCGgs2nx9nmvWmMK9ECU2ixBOibkM6SPbdOIsI+WM8NfedUrkBT6uxycCr8YCSUSmvF4VFfc35FJBSVAz7psTgI/iYZl5qNPThilkkh+OlGfpHCsv+yJetKLa3e2FxK1Qej6hu+yp6z5","sign":"LSCpcOnYSNpRfhtCTrmSUmEA47Kez+Q9fQHgdwTApd8wlNlUv317FtEPZ0qa/774AMtoTAny6O07+3qQBVf3Nqtq1hqUGmvOGDa7WKFZ59lNR5VpMuToyqXbtqNwM9wpLpgZNEYXIofwUVxTqiG7odWfLq8cBuwchQPw7C1sFuU="}
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
         * ciphertext : Gl8DDUHgBQeztILaPFUcoXg/oaXVMoNGvvZVE9CD1Y7cbP/ogyHJxZl9MbNbKEtfnA5Hs94P2Tw1KtKtU4WTyDgvQWSsyyK2fKctud0Y8SMNYXDrVcyIrNdDvdu5xT6cbr5lRReLpdRcrgUgNKYUdheov+QGZuxsaYd7iyIZkwgKkEipuSR6L4JMhqhV0rxnEBC/BpFIX0udTDuf50mroJA9Z+kYncBvl5fxkRtLa1UfMGnVAI5IOM9GsL42QjXV337SyCAlKC0/vQAT6WOOlbDx4IBKSByKmdYFsJYVpZog9FinMAbupti2A9xFX1c/YpiyakPyGTutWb/g/dbYsBnl2zeg/A4L6VOnTYgB9Hpfodg3ClHUFCGgs2nx9nmvWmMK9ECU2ixBOibkM6SPbdOIsI+WM8NfedUrkBT6uxycCr8YCSUSmvF4VFfc35FJBSVAz7psTgI/iYZl5qNPThilkkh+OlGfpHCsv+yJetKLa3e2FxK1Qej6hu+yp6z5
         * sign : LSCpcOnYSNpRfhtCTrmSUmEA47Kez+Q9fQHgdwTApd8wlNlUv317FtEPZ0qa/774AMtoTAny6O07+3qQBVf3Nqtq1hqUGmvOGDa7WKFZ59lNR5VpMuToyqXbtqNwM9wpLpgZNEYXIofwUVxTqiG7odWfLq8cBuwchQPw7C1sFuU=
         */

        private String ciphertext;
        private String sign;

        public String getCiphertext() {
            return ciphertext;
        }

        public void setCiphertext(String ciphertext) {
            this.ciphertext = ciphertext;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
