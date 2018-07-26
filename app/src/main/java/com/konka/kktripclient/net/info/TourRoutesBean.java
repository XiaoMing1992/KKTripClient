package com.konka.kktripclient.net.info;

/**
 * Created by smith on 2017/06/02.
 */
public class TourRoutesBean implements IEvent {
    /**
     * id : 10
     * name : 澳门四天游
     * goodsType : 0
     * description : 澳门四天游
     * originalPrice : 500.0
     * discountType : 大屏专享价
     * discountPrice : 299.0
     * video : {"id":16,"name":"第3讲 水平面上的连接体问题","description":"牛顿运动定律是经典力学的基石，是解动力学问题的基本方法，掌握好牛顿运动定律是同学们学好中学物理的关键，也是应对目前高考最基本的法宝。在在这一专题中，针对牛顿运动定律应用过程中出现的题型、解题方法以及同学们在学习中容易出现的问题，我们设置了五讲内容：\n第1讲 用牛顿第二定律求加速度；\n第2讲 牛顿第二定律的基本应用；\n第3讲 水平面上的连接体问题（接触连接、绳连体、弹簧连体等）；\n第4讲 斜面和竖直面上的连接体问题；\n第7讲 整体中有部分物体在加速的问题；\n其中内容详实，重在解开疑虑，打开思路，传授方法。欢","duration":15,"thumbnail":"http://img.znds.com/uploads/new/160819/9-160QZ945433I.png","playParam":"{\"fid\":\"83b7f7bda812fb4520fd73701cec0da2\",\"mid\":\"24474939\",\"sid\":\"1\",\"mtype\":\"30\"}","weight":1,"goodsType":null,"goods":null,"accessAdver":0,"createDate":"2017-05-22 12:28:18","updateDate":"2017-05-31 17:15:17"}
     * score : 0.0
     * weight : 1
     * state : 1
     * thumbnail : http://localhost:8082/kktripadmin/swagger/index.html#!/tourroutes/createTourRoute
     * customService :
     * departurePlace : 罗湖口岸
     * departureDate : 2017-05-35
     * poster1 : http://img.znds.com/uploads/new/141218/9-14121Q43952431.png
     * poster2 : http://img.znds.com/uploads/new/141218/9-14121Q43952431.png
     * poster3 : http://img.znds.com/uploads/new/141218/9-14121Q43952431.png
     * poster4 : http://img.znds.com/uploads/new/141218/9-14121Q43952431.png
     * poster5 : null
     * origin : 旅行社
     * createDate : 2017-05-19 17:48:54
     * updateDate : 2017-05-31 17:17:54
     */

    private int id;
    private String name;
    private int goodsType;
    private String description;
    private double originalPrice;
    private String discountType;
    private double discountPrice;
    private VideoBean video;
    private double score;
    private int weight;
    private int state;
    private String thumbnail;
    private String customService;
    private String departurePlace;
    private String departureDate;
    private String poster1;
    private String poster2;
    private String poster3;
    private String poster4;
    private String poster5;
    private String origin;
    private String createDate;
    private String updateDate;

    @Override
    public String toString() {
        String videoStr = null;
        if (video != null) {
            videoStr = video.toString();
        }
        return "TourRoutesBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", goodsType=" + goodsType +
                ", description='" + description + '\'' +
                ", originalPrice=" + originalPrice +
                ", discountType='" + discountType + '\'' +
                ", discountPrice=" + discountPrice +
                ", video=" + videoStr +
                ", score=" + score +
                ", weight=" + weight +
                ", state=" + state +
                ", thumbnail='" + thumbnail + '\'' +
                ", customService='" + customService + '\'' +
                ", departurePlace='" + departurePlace + '\'' +
                ", departureDate='" + departureDate + '\'' +
                ", poster1='" + poster1 + '\'' +
                ", poster2='" + poster2 + '\'' +
                ", poster3='" + poster3 + '\'' +
                ", poster4='" + poster4 + '\'' +
                ", poster5=" + poster5 +
                ", origin='" + origin + '\'' +
                ", createDate='" + createDate + '\'' +
                ", updateDate='" + updateDate + '\'' +
                '}';
    }

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

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public VideoBean getVideo() {
        return video;
    }

    public void setVideo(VideoBean video) {
        this.video = video;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCustomService() {
        return customService;
    }

    public void setCustomService(String customService) {
        this.customService = customService;
    }

    public String getDeparturePlace() {
        return departurePlace;
    }

    public void setDeparturePlace(String departurePlace) {
        this.departurePlace = departurePlace;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getPoster1() {
        return poster1;
    }

    public void setPoster1(String poster1) {
        this.poster1 = poster1;
    }

    public String getPoster2() {
        return poster2;
    }

    public void setPoster2(String poster2) {
        this.poster2 = poster2;
    }

    public String getPoster3() {
        return poster3;
    }

    public void setPoster3(String poster3) {
        this.poster3 = poster3;
    }

    public String getPoster4() {
        return poster4;
    }

    public void setPoster4(String poster4) {
        this.poster4 = poster4;
    }

    public String getPoster5() {
        return poster5;
    }

    public void setPoster5(String poster5) {
        this.poster5 = poster5;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public class VideoBean {
        /**
         * id : 16
         * name : 第3讲 水平面上的连接体问题
         * description : 牛顿运动定律是经典力学的基石，是解动力学问题的基本方法，掌握好牛顿运动定律是同学们学好中学物理的关键，也是应对目前高考最基本的法宝。在在这一专题中，针对牛顿运动定律应用过程中出现的题型、解题方法以及同学们在学习中容易出现的问题，我们设置了五讲内容：
         * 第1讲 用牛顿第二定律求加速度；
         * 第2讲 牛顿第二定律的基本应用；
         * 第3讲 水平面上的连接体问题（接触连接、绳连体、弹簧连体等）；
         * 第4讲 斜面和竖直面上的连接体问题；
         * 第7讲 整体中有部分物体在加速的问题；
         * 其中内容详实，重在解开疑虑，打开思路，传授方法。欢
         * duration : 15
         * thumbnail : http://img.znds.com/uploads/new/160819/9-160QZ945433I.png
         * playParam : {"fid":"83b7f7bda812fb4520fd73701cec0da2","mid":"24474939","sid":"1","mtype":"30"}
         * weight : 1
         * goodsType : null
         * goods : null
         * accessAdver : 0
         * createDate : 2017-05-22 12:28:18
         * updateDate : 2017-05-31 17:15:17
         */

        private int id;
        private String name;
        private String description;
        private int duration;
        private String thumbnail;
        private String playParam;
        private int weight;
        private Object goodsType;
        private Object goods;
        private int accessAdver;
        private String createDate;
        private String updateDate;

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

        public Object getGoodsType() {
            return goodsType;
        }

        public void setGoodsType(Object goodsType) {
            this.goodsType = goodsType;
        }

        public Object getGoods() {
            return goods;
        }

        public void setGoods(Object goods) {
            this.goods = goods;
        }

        public int getAccessAdver() {
            return accessAdver;
        }

        public void setAccessAdver(int accessAdver) {
            this.accessAdver = accessAdver;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }

        @Override
        public String toString() {
            return "VideoBean{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", duration=" + duration +
                    ", thumbnail='" + thumbnail + '\'' +
                    ", playParam='" + playParam + '\'' +
                    ", weight=" + weight +
                    ", goodsType=" + goodsType +
                    ", goods=" + goods +
                    ", accessAdver=" + accessAdver +
                    ", createDate='" + createDate + '\'' +
                    ", updateDate='" + updateDate + '\'' +
                    '}';
        }
    }
}
