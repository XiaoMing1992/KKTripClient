package com.konka.kktripclient.net.info;

import com.google.gson.annotations.JsonAdapter;
import com.konka.kktripclient.net.json.ListTypeAdapterFactory;

import java.io.Serializable;
import java.util.List;

/**
 * Created by smith on 2017/06/01.
 */
public class RouteDetailsEvent implements IEvent {
    /**
     * ret : {"ret_code":"0","ret_msg":"success"}
     * data : {"tourRoutes":[{"id":10,"name":"澳门四天游","goodsType":0,"description":"澳门四天游","originalPrice":500,"discountType":"大屏专享价","discountPrice":299,"video":{"id":16,"name":"第3讲 水平面上的连接体问题","description":"牛顿运动定律是经典力学的基石，是解动力学问题的基本方法，掌握好牛顿运动定律是同学们学好中学物理的关键，也是应对目前高考最基本的法宝。在在这一专题中，针对牛顿运动定律应用过程中出现的题型、解题方法以及同学们在学习中容易出现的问题，我们设置了五讲内容：\n第1讲 用牛顿第二定律求加速度；\n第2讲 牛顿第二定律的基本应用；\n第3讲 水平面上的连接体问题（接触连接、绳连体、弹簧连体等）；\n第4讲 斜面和竖直面上的连接体问题；\n第7讲 整体中有部分物体在加速的问题；\n其中内容详实，重在解开疑虑，打开思路，传授方法。欢","duration":15,"thumbnail":"http://img.znds.com/uploads/new/160819/9-160QZ945433I.png","playParam":"{\"fid\":\"83b7f7bda812fb4520fd73701cec0da2\",\"mid\":\"24474939\",\"sid\":\"1\",\"mtype\":\"30\"}","weight":1,"goodsType":null,"goods":null,"accessAdver":0,"createDate":"2017-05-22 12:28:18","updateDate":"2017-05-31 17:15:17"},"score":0,"weight":1,"state":1,"thumbnail":"http://localhost:8082/kktripadmin/swagger/index.html#!/tourroutes/createTourRoute","customService":"","departurePlace":"罗湖口岸","departureDate":"2017-05-35","poster1":"http://img.znds.com/uploads/new/141218/9-14121Q43952431.png","poster2":"http://img.znds.com/uploads/new/141218/9-14121Q43952431.png","poster3":"http://img.znds.com/uploads/new/141218/9-14121Q43952431.png","poster4":"http://img.znds.com/uploads/new/141218/9-14121Q43952431.png","poster5":null,"origin":"旅行社","createDate":"2017-05-19 17:48:54","updateDate":"2017-05-31 17:17:54"},{"id":25,"name":"香港一日游","goodsType":0,"description":"香港一日游","originalPrice":500,"discountType":"大屏专享价","discountPrice":299,"video":null,"score":0,"weight":0,"state":1,"thumbnail":"http://localhost:8082/kktripadmin/swagger/index.html#!/tourroutes/createTourRoute","customService":"","departurePlace":"罗湖口岸","departureDate":"2017-05-35","poster1":"http://img.znds.com/uploads/new/141218/9-14121Q43952431.png","poster2":"http://img.znds.com/uploads/new/141218/9-14121Q43952431.png","poster3":"http://img.znds.com/uploads/new/141218/9-14121Q43952431.png","poster4":"http://img.znds.com/uploads/new/141218/9-14121Q43952431.png","poster5":null,"origin":"旅行社","createDate":"2017-05-19 17:48:54","updateDate":"2017-05-31 17:18:00"}],"pagination":{"page":1,"pageSize":3,"count":2}}
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
         * tourRoutes : [{"id":10,"name":"澳门四天游","goodsType":0,"description":"澳门四天游","originalPrice":500,"discountType":"大屏专享价","discountPrice":299,"video":{"id":16,"name":"第3讲 水平面上的连接体问题","description":"牛顿运动定律是经典力学的基石，是解动力学问题的基本方法，掌握好牛顿运动定律是同学们学好中学物理的关键，也是应对目前高考最基本的法宝。在在这一专题中，针对牛顿运动定律应用过程中出现的题型、解题方法以及同学们在学习中容易出现的问题，我们设置了五讲内容：\n第1讲 用牛顿第二定律求加速度；\n第2讲 牛顿第二定律的基本应用；\n第3讲 水平面上的连接体问题（接触连接、绳连体、弹簧连体等）；\n第4讲 斜面和竖直面上的连接体问题；\n第7讲 整体中有部分物体在加速的问题；\n其中内容详实，重在解开疑虑，打开思路，传授方法。欢","duration":15,"thumbnail":"http://img.znds.com/uploads/new/160819/9-160QZ945433I.png","playParam":"{\"fid\":\"83b7f7bda812fb4520fd73701cec0da2\",\"mid\":\"24474939\",\"sid\":\"1\",\"mtype\":\"30\"}","weight":1,"goodsType":null,"goods":null,"accessAdver":0,"createDate":"2017-05-22 12:28:18","updateDate":"2017-05-31 17:15:17"},"score":0,"weight":1,"state":1,"thumbnail":"http://localhost:8082/kktripadmin/swagger/index.html#!/tourroutes/createTourRoute","customService":"","departurePlace":"罗湖口岸","departureDate":"2017-05-35","poster1":"http://img.znds.com/uploads/new/141218/9-14121Q43952431.png","poster2":"http://img.znds.com/uploads/new/141218/9-14121Q43952431.png","poster3":"http://img.znds.com/uploads/new/141218/9-14121Q43952431.png","poster4":"http://img.znds.com/uploads/new/141218/9-14121Q43952431.png","poster5":null,"origin":"旅行社","createDate":"2017-05-19 17:48:54","updateDate":"2017-05-31 17:17:54"},{"id":25,"name":"香港一日游","goodsType":0,"description":"香港一日游","originalPrice":500,"discountType":"大屏专享价","discountPrice":299,"video":null,"score":0,"weight":0,"state":1,"thumbnail":"http://localhost:8082/kktripadmin/swagger/index.html#!/tourroutes/createTourRoute","customService":"","departurePlace":"罗湖口岸","departureDate":"2017-05-35","poster1":"http://img.znds.com/uploads/new/141218/9-14121Q43952431.png","poster2":"http://img.znds.com/uploads/new/141218/9-14121Q43952431.png","poster3":"http://img.znds.com/uploads/new/141218/9-14121Q43952431.png","poster4":"http://img.znds.com/uploads/new/141218/9-14121Q43952431.png","poster5":null,"origin":"旅行社","createDate":"2017-05-19 17:48:54","updateDate":"2017-05-31 17:18:00"}]
         * pagination : {"page":1,"pageSize":3,"count":2}
         */

        private PaginationBean pagination;
        @JsonAdapter(ListTypeAdapterFactory.class)
        private List<TourRoutesBean> tourRoutes;

        public PaginationBean getPagination() {
            return pagination;
        }

        public void setPagination(PaginationBean pagination) {
            this.pagination = pagination;
        }

        public List<TourRoutesBean> getTourRoutes() {
            return tourRoutes;
        }

        public void setTourRoutes(List<TourRoutesBean> tourRoutes) {
            this.tourRoutes = tourRoutes;
        }

    }



/*    RouteDetailsInfo data;

    public RouteDetailsInfo getData() {
        return data;
    }

    public void setData(RouteDetailsInfo data) {
        this.data = data;
    }*/
}
