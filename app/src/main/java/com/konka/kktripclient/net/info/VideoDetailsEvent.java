package com.konka.kktripclient.net.info;

/**
 * Created by smith on 2017/06/20.
 */
public class VideoDetailsEvent implements IEvent {

    /**
     * ret : {"ret_code":"0","ret_msg":"success"}
     * data : {"id":16,"name":"第3讲 水平面上的连接体问题","description":"牛顿运动定律是经典力学的基石，是解动力学问题的基本方法，掌握好牛顿运动定律是同学们学好中学物理的关键，也是应对目前高考最基本的法宝。在在这一专题中，针对牛顿运动定律应用过程中出现的题型、解题方法以及同学们在学习中容易出现的问题，我们设置了五讲内容：\n第1讲 用牛顿第二定律求加速度；\n第2讲 牛顿第二定律的基本应用；\n第3讲 水平面上的连接体问题（接触连接、绳连体、弹簧连体等）；\n第4讲 斜面和竖直面上的连接体问题；\n第7讲 整体中有部分物体在加速的问题；\n其中内容详实，重在解开疑虑，打开思路，传授方法。欢","duration":15,"thumbnail":"http://img.znds.com/uploads/new/160819/9-160QZ945433I.png","playParam":"{\"fid\":\"83b7f7bda812fb4520fd73701cec0da2\",\"mid\":\"24474939\",\"sid\":\"1\",\"mtype\":\"30\"}","weight":4,"state":1,"goods":{"id":44,"name":"这个也是测试的","goodsType":0,"description":"这个也是测试的","originalPrice":100,"discountType":"大屏惊爆价","discountPrice":50,"video":null,"score":0,"weight":19,"state":1,"thumbnail":"/kkupload/45/454000/F454000/170619153415Taqw.jpg","customService":"12345678","departurePlace":"出发地，待定（修改）","departureDate":"周末","poster1":"/kkupload/45/454000/F454000/1706191534172T0S.jpg","poster2":"/kkupload/45/454000/F454000/170619153422onkW.jpg","poster3":"/kkupload/45/454000/F454000/170619153428BYK3.jpg","poster4":"/kkupload/45/454000/F454000/170619153432QQJG.jpg","poster5":null,"origin":"测试","createDate":1497857669000,"updateDate":1497857706000},"accessAdver":0,"createDate":1495427298000,"updateDate":1496378187000}
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
         * id : 16
         * name : 第3讲 水平面上的连接体问题
         * description : 牛顿运动定律是经典力学的基石，是解动力学问题的基本方法，掌握好牛顿运动定律是同学们学好中学物理的关键，也是应对目前高考最基本的法宝。在在这一专题中，针对牛顿运动定律应用过程中出现的题型、解题方法以及同学们在学习中容易出现的问题，我们设置了五讲内容：
         第1讲 用牛顿第二定律求加速度；
         第2讲 牛顿第二定律的基本应用；
         第3讲 水平面上的连接体问题（接触连接、绳连体、弹簧连体等）；
         第4讲 斜面和竖直面上的连接体问题；
         第7讲 整体中有部分物体在加速的问题；
         其中内容详实，重在解开疑虑，打开思路，传授方法。欢
         * duration : 15
         * thumbnail : http://img.znds.com/uploads/new/160819/9-160QZ945433I.png
         * playParam : {"fid":"83b7f7bda812fb4520fd73701cec0da2","mid":"24474939","sid":"1","mtype":"30"}
         * weight : 4
         * state : 1
         * goods : {"id":44,"name":"这个也是测试的","goodsType":0,"description":"这个也是测试的","originalPrice":100,"discountType":"大屏惊爆价","discountPrice":50,"video":null,"score":0,"weight":19,"state":1,"thumbnail":"/kkupload/45/454000/F454000/170619153415Taqw.jpg","customService":"12345678","departurePlace":"出发地，待定（修改）","departureDate":"周末","poster1":"/kkupload/45/454000/F454000/1706191534172T0S.jpg","poster2":"/kkupload/45/454000/F454000/170619153422onkW.jpg","poster3":"/kkupload/45/454000/F454000/170619153428BYK3.jpg","poster4":"/kkupload/45/454000/F454000/170619153432QQJG.jpg","poster5":null,"origin":"测试","createDate":1497857669000,"updateDate":1497857706000}
         * accessAdver : 0
         * createDate : 1495427298000
         * updateDate : 1496378187000
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
