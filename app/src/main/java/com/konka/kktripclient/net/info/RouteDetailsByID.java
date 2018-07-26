package com.konka.kktripclient.net.info;

/**
 * Created by smith on 2017/06/05.
 */
public class RouteDetailsByID implements IEvent {


    /**
     * ret : {"ret_code":"0","ret_msg":"success"}
     * data : {"id":10,"name":"澳门四天游","goodsType":0,"description":"澳门四天游","originalPrice":500,"discountType":"大屏专享价","discountPrice":299,"video":{"id":20,"name":"《长河落日扁》","description":"长春版初三语文《长河落日扁》","duration":36,"thumbnail":"/kkupload/45/456000/F456000/170605102337eWi2.png","playParam":"{\"fid\":\"481e69159787f48b678e212e5aab2ad9\",\"mid\":\"38828186\",\"sid\":\"1\",\"mtype\":\"30\"}","weight":9,"goodsType":null,"goods":null,"accessAdver":0,"createDate":"2017-06-05 10:31:43","updateDate":"2017-06-05 10:31:43"},"score":0,"weight":4,"state":1,"thumbnail":"http://localhost:8082/kktripadmin/swagger/index.html#!/tourroutes/createTourRoute","customService":"","departurePlace":"罗湖口岸","departureDate":"2017-05-35","poster1":"http://img.znds.com/uploads/new/141218/9-14121Q43952431.png","poster2":"http://img.znds.com/uploads/new/141218/9-14121Q43952431.png","poster3":"http://img.znds.com/uploads/new/141218/9-14121Q43952431.png","poster4":"http://img.znds.com/uploads/new/141218/9-14121Q43952431.png","poster5":null,"origin":"旅行社","createDate":"2017-05-19 17:48:54","updateDate":"2017-06-05 09:40:20"}
     */

    private RetBean ret;
    private TourRoutesBean data;

    public RetBean getRet() {
        return ret;
    }

    public void setRet(RetBean ret) {
        this.ret = ret;
    }

    public TourRoutesBean getData() {
        return data;
    }

    public void setData(TourRoutesBean data) {
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

        @Override
        public String toString() {
            return "RetBean{" +
                    "ret_code='" + ret_code + '\'' +
                    ", ret_msg='" + ret_msg + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "RouteDetailsByID{" +
                "ret=" + ret.toString() +
                ", data=" + data.toString() +
                '}';
    }
}
