package com.konka.kktripclient.net.info;

import com.google.gson.annotations.JsonAdapter;
import com.konka.kktripclient.net.json.ListTypeAdapterFactory;

import java.util.List;

/**
 * Created by smith on 2017/06/28.
 */
public class AllVideosEvent implements IEvent {

    /**
     * ret : {"ret_code":"0","ret_msg":"success"}
     * data : {"videos":[{"id":7,"name":"第1讲 看云识天气  朱泳燚","description":"本项目是一项覆盖初一初二初三全程，横跨多个版本的系列微名师课程。本课程以服务师生促进教学为宗旨，以精要拓展有效提高为原则，从初一初二初三第一学段同时开始、同步跟进。以人教版序列为主干，同步横跨苏教粤教鲁教语文等主流版本，全系列覆盖，多版本适用。以教材目录为序，突出主干篇目，每篇目１-２课时，每课时15分钟左右，不强调面的展开，只突出点的深入，是广大师生日常教学的有益补充及提分利器。\n初中语文同步微名师课程以课程标准的\u201c能力\u201d要求为目标，以教材为例子，以课文为具体的训练材料，既相互独立，各有侧重，17定的顺","duration":14,"thumbnail":"http://img.znds.com/uploads/new/160819/9-160QZ945433I.png","playParam":"{\"fid\":\"d9761c0daac50ce4b7262055ea54a744\",\"mid\":\"14251046\",\"sid\":\"1\",\"mtype\":\"30\"}","weight":6,"state":1,"goods":{"id":44,"name":"这个也是测试的","goodsType":0,"description":"这个也是测试的","originalPrice":100,"discountType":"大屏惊爆价","discountPrice":50,"video":null,"score":0,"weight":19,"state":1,"thumbnail":"/kkupload/45/454000/F454000/170619153415Taqw.jpg","customService":"12345678","departurePlace":"海南省-三亚市,辽宁省-鞍山市,河北省-秦皇岛市,河北省-石家庄市","departureDate":"周末","poster1":"/kkupload/45/454000/F454000/1706191534172T0S.jpg","poster2":"/kkupload/45/454000/F454000/170619153422onkW.jpg","poster3":"/kkupload/45/454000/F454000/170619153428BYK3.jpg","poster4":"/kkupload/45/454000/F454000/170619153432QQJG.jpg","poster5":null,"origin":"测试","createDate":1497857669000,"updateDate":1498556490000},"accessAdver":0,"createDate":1495426986000,"updateDate":1496378189000},{"id":5,"name":"修改名字测试","description":"本专题由各位一线权威名师从高考真题出发，锁定备考靶心，总结出高考必考题，每种题型以经典高考题作为母题，对题型进行深入透析，让考生熟悉并掌握考点之间如何交汇命题、常见的命题特点和规律，引导学生跳出题海，传播解答此类问题的破题技巧和通性通法以及归纳出此类问题重点难点高频考点以及解题题眼；在解答此类问题时，帮助学生重点突破答题瓶颈、常犯的解题错误，扫除解题中的拦路虎，最终达到，\u201c掌握一类题，突破一大片的目的\u201d。\n因为二轮课程对考生的综合能力要求较高，所以建议考生，在学习本专题之前，建议考生先对自己要有清楚的认识","duration":28,"thumbnail":"http://img.znds.com/uploads/new/160819/9-160QZ945433I.png","playParam":"{\"fid\":\"11c96e4dc3ddbf2907148bf765dea2fc\",\"mid\":\"7194620\",\"sid\":\"1\",\"mtype\":\"30\"}","weight":5,"state":1,"goods":null,"accessAdver":0,"createDate":1495426803000,"updateDate":1496222085000},{"id":16,"name":"第3讲 水平面上的连接体问题","description":"牛顿运动定律是经典力学的基石，是解动力学问题的基本方法，掌握好牛顿运动定律是同学们学好中学物理的关键，也是应对目前高考最基本的法宝。在在这一专题中，针对牛顿运动定律应用过程中出现的题型、解题方法以及同学们在学习中容易出现的问题，我们设置了五讲内容：\n第1讲 用牛顿第二定律求加速度；\n第2讲 牛顿第二定律的基本应用；\n第3讲 水平面上的连接体问题（接触连接、绳连体、弹簧连体等）；\n第4讲 斜面和竖直面上的连接体问题；\n第7讲 整体中有部分物体在加速的问题；\n其中内容详实，重在解开疑虑，打开思路，传授方法。欢","duration":15,"thumbnail":"http://img.znds.com/uploads/new/160819/9-160QZ945433I.png","playParam":"{\"fid\":\"83b7f7bda812fb4520fd73701cec0da2\",\"mid\":\"24474939\",\"sid\":\"1\",\"mtype\":\"30\"}","weight":4,"state":1,"goods":{"id":44,"name":"这个也是测试的","goodsType":0,"description":"这个也是测试的","originalPrice":100,"discountType":"大屏惊爆价","discountPrice":50,"video":null,"score":0,"weight":19,"state":1,"thumbnail":"/kkupload/45/454000/F454000/170619153415Taqw.jpg","customService":"12345678","departurePlace":"海南省-三亚市,辽宁省-鞍山市,河北省-秦皇岛市,河北省-石家庄市","departureDate":"周末","poster1":"/kkupload/45/454000/F454000/1706191534172T0S.jpg","poster2":"/kkupload/45/454000/F454000/170619153422onkW.jpg","poster3":"/kkupload/45/454000/F454000/170619153428BYK3.jpg","poster4":"/kkupload/45/454000/F454000/170619153432QQJG.jpg","poster5":null,"origin":"测试","createDate":1497857669000,"updateDate":1498556490000},"accessAdver":0,"createDate":1495427298000,"updateDate":1496378187000}],"pagination":{"page":1,"start":0,"pageSize":3,"count":6}}
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
         * videos : [{"id":7,"name":"第1讲 看云识天气  朱泳燚","description":"本项目是一项覆盖初一初二初三全程，横跨多个版本的系列微名师课程。本课程以服务师生促进教学为宗旨，以精要拓展有效提高为原则，从初一初二初三第一学段同时开始、同步跟进。以人教版序列为主干，同步横跨苏教粤教鲁教语文等主流版本，全系列覆盖，多版本适用。以教材目录为序，突出主干篇目，每篇目１-２课时，每课时15分钟左右，不强调面的展开，只突出点的深入，是广大师生日常教学的有益补充及提分利器。\n初中语文同步微名师课程以课程标准的\u201c能力\u201d要求为目标，以教材为例子，以课文为具体的训练材料，既相互独立，各有侧重，17定的顺","duration":14,"thumbnail":"http://img.znds.com/uploads/new/160819/9-160QZ945433I.png","playParam":"{\"fid\":\"d9761c0daac50ce4b7262055ea54a744\",\"mid\":\"14251046\",\"sid\":\"1\",\"mtype\":\"30\"}","weight":6,"state":1,"goods":{"id":44,"name":"这个也是测试的","goodsType":0,"description":"这个也是测试的","originalPrice":100,"discountType":"大屏惊爆价","discountPrice":50,"video":null,"score":0,"weight":19,"state":1,"thumbnail":"/kkupload/45/454000/F454000/170619153415Taqw.jpg","customService":"12345678","departurePlace":"海南省-三亚市,辽宁省-鞍山市,河北省-秦皇岛市,河北省-石家庄市","departureDate":"周末","poster1":"/kkupload/45/454000/F454000/1706191534172T0S.jpg","poster2":"/kkupload/45/454000/F454000/170619153422onkW.jpg","poster3":"/kkupload/45/454000/F454000/170619153428BYK3.jpg","poster4":"/kkupload/45/454000/F454000/170619153432QQJG.jpg","poster5":null,"origin":"测试","createDate":1497857669000,"updateDate":1498556490000},"accessAdver":0,"createDate":1495426986000,"updateDate":1496378189000},{"id":5,"name":"修改名字测试","description":"本专题由各位一线权威名师从高考真题出发，锁定备考靶心，总结出高考必考题，每种题型以经典高考题作为母题，对题型进行深入透析，让考生熟悉并掌握考点之间如何交汇命题、常见的命题特点和规律，引导学生跳出题海，传播解答此类问题的破题技巧和通性通法以及归纳出此类问题重点难点高频考点以及解题题眼；在解答此类问题时，帮助学生重点突破答题瓶颈、常犯的解题错误，扫除解题中的拦路虎，最终达到，\u201c掌握一类题，突破一大片的目的\u201d。\n因为二轮课程对考生的综合能力要求较高，所以建议考生，在学习本专题之前，建议考生先对自己要有清楚的认识","duration":28,"thumbnail":"http://img.znds.com/uploads/new/160819/9-160QZ945433I.png","playParam":"{\"fid\":\"11c96e4dc3ddbf2907148bf765dea2fc\",\"mid\":\"7194620\",\"sid\":\"1\",\"mtype\":\"30\"}","weight":5,"state":1,"goods":null,"accessAdver":0,"createDate":1495426803000,"updateDate":1496222085000},{"id":16,"name":"第3讲 水平面上的连接体问题","description":"牛顿运动定律是经典力学的基石，是解动力学问题的基本方法，掌握好牛顿运动定律是同学们学好中学物理的关键，也是应对目前高考最基本的法宝。在在这一专题中，针对牛顿运动定律应用过程中出现的题型、解题方法以及同学们在学习中容易出现的问题，我们设置了五讲内容：\n第1讲 用牛顿第二定律求加速度；\n第2讲 牛顿第二定律的基本应用；\n第3讲 水平面上的连接体问题（接触连接、绳连体、弹簧连体等）；\n第4讲 斜面和竖直面上的连接体问题；\n第7讲 整体中有部分物体在加速的问题；\n其中内容详实，重在解开疑虑，打开思路，传授方法。欢","duration":15,"thumbnail":"http://img.znds.com/uploads/new/160819/9-160QZ945433I.png","playParam":"{\"fid\":\"83b7f7bda812fb4520fd73701cec0da2\",\"mid\":\"24474939\",\"sid\":\"1\",\"mtype\":\"30\"}","weight":4,"state":1,"goods":{"id":44,"name":"这个也是测试的","goodsType":0,"description":"这个也是测试的","originalPrice":100,"discountType":"大屏惊爆价","discountPrice":50,"video":null,"score":0,"weight":19,"state":1,"thumbnail":"/kkupload/45/454000/F454000/170619153415Taqw.jpg","customService":"12345678","departurePlace":"海南省-三亚市,辽宁省-鞍山市,河北省-秦皇岛市,河北省-石家庄市","departureDate":"周末","poster1":"/kkupload/45/454000/F454000/1706191534172T0S.jpg","poster2":"/kkupload/45/454000/F454000/170619153422onkW.jpg","poster3":"/kkupload/45/454000/F454000/170619153428BYK3.jpg","poster4":"/kkupload/45/454000/F454000/170619153432QQJG.jpg","poster5":null,"origin":"测试","createDate":1497857669000,"updateDate":1498556490000},"accessAdver":0,"createDate":1495427298000,"updateDate":1496378187000}]
         * pagination : {"page":1,"start":0,"pageSize":3,"count":6}
         */

        private PaginationBean pagination;
        @JsonAdapter(ListTypeAdapterFactory.class)
        private List<VideosBean> videos;

        public PaginationBean getPagination() {
            return pagination;
        }

        public void setPagination(PaginationBean pagination) {
            this.pagination = pagination;
        }

        public List<VideosBean> getVideos() {
            return videos;
        }

        public void setVideos(List<VideosBean> videos) {
            this.videos = videos;
        }

        public class PaginationBean {
            /**
             * page : 1
             * start : 0
             * pageSize : 3
             * count : 6
             */

            private int page;
            private int start;
            private int pageSize;
            private int count;

            public int getPage() {
                return page;
            }

            public void setPage(int page) {
                this.page = page;
            }

            public int getStart() {
                return start;
            }

            public void setStart(int start) {
                this.start = start;
            }

            public int getPageSize() {
                return pageSize;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }
        }

        public class VideosBean {
            /**
             * id : 7
             * name : 第1讲 看云识天气  朱泳燚
             * description : 本项目是一项覆盖初一初二初三全程，横跨多个版本的系列微名师课程。本课程以服务师生促进教学为宗旨，以精要拓展有效提高为原则，从初一初二初三第一学段同时开始、同步跟进。以人教版序列为主干，同步横跨苏教粤教鲁教语文等主流版本，全系列覆盖，多版本适用。以教材目录为序，突出主干篇目，每篇目１-２课时，每课时15分钟左右，不强调面的展开，只突出点的深入，是广大师生日常教学的有益补充及提分利器。
             * 初中语文同步微名师课程以课程标准的“能力”要求为目标，以教材为例子，以课文为具体的训练材料，既相互独立，各有侧重，17定的顺
             * duration : 14
             * thumbnail : http://img.znds.com/uploads/new/160819/9-160QZ945433I.png
             * playParam : {"fid":"d9761c0daac50ce4b7262055ea54a744","mid":"14251046","sid":"1","mtype":"30"}
             * weight : 6
             * state : 1
             * goods : {"id":44,"name":"这个也是测试的","goodsType":0,"description":"这个也是测试的","originalPrice":100,"discountType":"大屏惊爆价","discountPrice":50,"video":null,"score":0,"weight":19,"state":1,"thumbnail":"/kkupload/45/454000/F454000/170619153415Taqw.jpg","customService":"12345678","departurePlace":"海南省-三亚市,辽宁省-鞍山市,河北省-秦皇岛市,河北省-石家庄市","departureDate":"周末","poster1":"/kkupload/45/454000/F454000/1706191534172T0S.jpg","poster2":"/kkupload/45/454000/F454000/170619153422onkW.jpg","poster3":"/kkupload/45/454000/F454000/170619153428BYK3.jpg","poster4":"/kkupload/45/454000/F454000/170619153432QQJG.jpg","poster5":null,"origin":"测试","createDate":1497857669000,"updateDate":1498556490000}
             * accessAdver : 0
             * createDate : 1495426986000
             * updateDate : 1496378189000
             */

            private int id;
            private String name;
            private String description;
            private int duration;
            private String thumbnail;
            private String playParam;
            private int weight;
            private int state;
            private GoodsBean goods;
            private int accessAdver;
            private long createDate;
            private long updateDate;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public int getDuration() {
                return duration;
            }

            public void setDuration(int duration) {
                this.duration = duration;
            }

            public String getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(String thumbnail) {
                this.thumbnail = thumbnail;
            }

            public String getPlayParam() {
                return playParam;
            }

            public void setPlayParam(String playParam) {
                this.playParam = playParam;
            }

            public int getWeight() {
                return weight;
            }

            public void setWeight(int weight) {
                this.weight = weight;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public GoodsBean getGoods() {
                return goods;
            }

            public void setGoods(GoodsBean goods) {
                this.goods = goods;
            }

            public int getAccessAdver() {
                return accessAdver;
            }

            public void setAccessAdver(int accessAdver) {
                this.accessAdver = accessAdver;
            }

            public long getCreateDate() {
                return createDate;
            }

            public void setCreateDate(long createDate) {
                this.createDate = createDate;
            }

            public long getUpdateDate() {
                return updateDate;
            }

            public void setUpdateDate(long updateDate) {
                this.updateDate = updateDate;
            }

        }
    }
}
