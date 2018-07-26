package com.konka.kktripclient.net.info;

import com.google.gson.annotations.JsonAdapter;
import com.konka.kktripclient.net.json.ListTypeAdapterFactory;

import java.util.List;

/**
 * Created by smith on 2017/06/15.
 */
public class QueryRouteAndTicketEvent implements IEvent {

    /**
     * ret : {"ret_code":"0","ret_msg":"success"}
     * data : {"tourRoutes":[{"id":29,"name":"香港一日游","goodsType":0,"description":"香港一日游","originalPrice":500,"discountType":"大屏专享价","discountPrice":299,"video":null,"score":5,"weight":2,"state":1,"thumbnail":"/kkupload/45/454000/F454000/170608165507isrI.png","customService":"114","departurePlace":"广东省-深圳市","departureDate":"2017-05-35","poster1":"/kkupload/45/454000/F454000/1706081655164fJ0.png","poster2":"http://img.znds.com/uploads/new/141218/9-14121Q43952431.png","poster3":"http://img.znds.com/uploads/new/141218/9-14121Q43952431.png","poster4":"http://img.znds.com/uploads/new/141218/9-14121Q43952431.png","poster5":null,"origin":"旅行社","createDate":1495187334000,"updateDate":1496912131000},{"id":40,"name":"阿尔山森林草原深处秘境之旅","goodsType":0,"description":"阿尔山森林草原深处秘境之旅｜奥伦布坎+火山遗址+原始部落；在最完美的自然教育营地，草原定向徒步穿越、白桦林探秘、原始部落探秘、火山遗址探秘、探访牧户、草原骑马。火山、草原、温泉、森林玩个遍","originalPrice":5580,"discountType":"大屏专享价","discountPrice":5580,"video":{"id":11,"name":"第12讲 基础知识积累与运用\u2014\u2014广告、标语的拟写","description":"积累和运用，是语文学科中基础的基础，也是中考试卷中必不可少的有机组成部分。从2011年全国各地40套中考试卷抽样调查来看，\u201c积累和运用\u201d的分值最低为10分，占全卷的10％；最多为40分，占全卷的26.7％。分值在25-40分的卷子就有22套，占51套卷子地位。做好了这部分内容可以让你一马当先，本专题针对中考一轮训练的重点，从实际应对的角度，达到熟练掌握基础知识考点、自信应试的复习目的。","duration":16,"thumbnail":"/kkupload/45/456000/F456000/170614164616sZeW.jpg","playParam":"{\"fid\":\"eb8a235ab2b49f6fb3476cf5b4371dc5\",\"mid\":\"4808923\",\"sid\":\"1\",\"mtype\":\"30\"}","weight":1,"goodsType":null,"goods":null,"accessAdver":0,"createDate":1495427220000,"updateDate":1497430167000},"score":0,"weight":15,"state":1,"thumbnail":"/kkupload/45/454000/F454000/1706131755566U53.jpg","customService":"17701791751","departurePlace":"中国-全部","departureDate":"一期：7月03日-7月09日","poster1":"/kkupload/45/454000/F454000/170613175621VLgI.jpg","poster2":"/kkupload/45/454000/F454000/170613175632IE7R.jpg","poster3":null,"poster4":null,"poster5":null,"origin":"童游","createDate":1497347905000,"updateDate":1497431271000}],"tickets":[{"id":36,"name":"北京天津欢乐谷套票","description":"含北京，天津欢乐谷日场票各一张，仅限本人分别使用一次。","originalPrice":440,"discountType":"260","discountPrice":200,"customService":"12345678","goodsType":1,"businessHours":"8：30~22:00","score":0,"weight":4,"qrcode":"http://mall.smartoct.com/weixin/index.php?r=ticketwindow/index&id=142&ocid=10022&tcid=144&sn=fe85375ad679f4300a57ac8aa5e73f2d","thumbnail":"/kkupload/45/455000/F455000/170614164200X6sf.jpg","poster1":"/kkupload/45/455000/F455000/170614164217icfz.png","poster2":"/kkupload/45/455000/F455000/170614164223aciT.png","poster3":"/kkupload/45/455000/F455000/170614164228rQO1.png","poster4":"","poster5":"","createDate":1497429904000,"updateDate":1497430053000}]}
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
        @JsonAdapter(ListTypeAdapterFactory.class)
        private List<TourRoutesBean> tourRoutes;
        @JsonAdapter(ListTypeAdapterFactory.class)
        private List<TicketsBean> tickets;

        public List<TourRoutesBean> getTourRoutes() {
            return tourRoutes;
        }

        public void setTourRoutes(List<TourRoutesBean> tourRoutes) {
            this.tourRoutes = tourRoutes;
        }

        public List<TicketsBean> getTickets() {
            return tickets;
        }

        public void setTickets(List<TicketsBean> tickets) {
            this.tickets = tickets;
        }

    }
}
