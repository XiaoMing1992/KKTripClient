package com.konka.kktripclient.net.info;

import com.google.gson.annotations.JsonAdapter;
import com.konka.kktripclient.net.json.ListTypeAdapterFactory;

import java.io.Serializable;
import java.util.List;

/**
 * Created by smith on 2017/06/19.
 */
public class AllOrderInfoEvent implements IEvent, Serializable {

    /**
     * ret : {"ret_code":"0","ret_msg":"success"}
     * data : {"orders":[{"id":125,"goodsId":40,"goodsType":0,"goodsName":"阿尔山森林草原深处秘境之旅","thumbnail":"/kkupload/45/454000/F454000/1706131755566U53.jpg","unitPrice":"0.01","amount":1,"price":"0.01","state":1,"cpPrivateInfoStr":"{\"address\":\"中国-全部\",\"name\":\"1\",\"tel\":\"13613088581\"}","cpPrivateInfo":{"name":"1","tel":"13613088581","address":"中国-全部"},"createDate":1497853372000,"updateDate":1497853698000},{"id":124,"goodsId":40,"goodsType":0,"goodsName":"阿尔山森林草原深处秘境之旅","thumbnail":"/kkupload/45/454000/F454000/1706131755566U53.jpg","unitPrice":"0.01","amount":1,"price":"0.01","state":1,"cpPrivateInfoStr":"{\"address\":\"中国-全部\",\"name\":\"13613088581\",\"tel\":\"13613088581\"}","cpPrivateInfo":{"name":"13613088581","tel":"13613088581","address":"中国-全部"},"createDate":1497845236000,"updateDate":1497845315000},{"id":123,"goodsId":40,"goodsType":0,"goodsName":"阿尔山森林草原深处秘境之旅","thumbnail":"/kkupload/45/454000/F454000/1706131755566U53.jpg","unitPrice":"0.01","amount":1,"price":"0.01","state":1,"cpPrivateInfoStr":"{\"address\":\"中国-全部\",\"name\":\"13613088581\",\"tel\":\"2\"}","cpPrivateInfo":{"name":"13613088581","tel":"2","address":"中国-全部"},"createDate":1497845100000,"updateDate":1497845169000},{"id":122,"goodsId":40,"goodsType":0,"goodsName":"阿尔山森林草原深处秘境之旅","thumbnail":"/kkupload/45/454000/F454000/1706131755566U53.jpg","unitPrice":"0.01","amount":1,"price":"0.01","state":0,"cpPrivateInfoStr":"{\"address\":\"中国-全部\",\"name\":\"12\",\"tel\":\"2\"}","cpPrivateInfo":{"name":"12","tel":"2","address":"中国-全部"},"createDate":1497844969000,"updateDate":1497844969000},{"id":120,"goodsId":40,"goodsType":0,"goodsName":"阿尔山森林草原深处秘境之旅","thumbnail":"/kkupload/45/454000/F454000/1706131755566U53.jpg","unitPrice":"0.01","amount":1,"price":"0.01","state":0,"cpPrivateInfoStr":"{\"address\":\"中国-全部\",\"name\":\"1\",\"tel\":\"2\"}","cpPrivateInfo":{"name":"1","tel":"2","address":"中国-全部"},"createDate":1497840631000,"updateDate":1497840631000}],"pagination":{"page":1,"start":0,"pageSize":5,"count":44}}
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

    public class RetBean implements Serializable {
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

    public class DataBean implements Serializable {
        /**
         * orders : [{"id":125,"goodsId":40,"goodsType":0,"goodsName":"阿尔山森林草原深处秘境之旅","thumbnail":"/kkupload/45/454000/F454000/1706131755566U53.jpg","unitPrice":"0.01","amount":1,"price":"0.01","state":1,"cpPrivateInfoStr":"{\"address\":\"中国-全部\",\"name\":\"1\",\"tel\":\"13613088581\"}","cpPrivateInfo":{"name":"1","tel":"13613088581","address":"中国-全部"},"createDate":1497853372000,"updateDate":1497853698000},{"id":124,"goodsId":40,"goodsType":0,"goodsName":"阿尔山森林草原深处秘境之旅","thumbnail":"/kkupload/45/454000/F454000/1706131755566U53.jpg","unitPrice":"0.01","amount":1,"price":"0.01","state":1,"cpPrivateInfoStr":"{\"address\":\"中国-全部\",\"name\":\"13613088581\",\"tel\":\"13613088581\"}","cpPrivateInfo":{"name":"13613088581","tel":"13613088581","address":"中国-全部"},"createDate":1497845236000,"updateDate":1497845315000},{"id":123,"goodsId":40,"goodsType":0,"goodsName":"阿尔山森林草原深处秘境之旅","thumbnail":"/kkupload/45/454000/F454000/1706131755566U53.jpg","unitPrice":"0.01","amount":1,"price":"0.01","state":1,"cpPrivateInfoStr":"{\"address\":\"中国-全部\",\"name\":\"13613088581\",\"tel\":\"2\"}","cpPrivateInfo":{"name":"13613088581","tel":"2","address":"中国-全部"},"createDate":1497845100000,"updateDate":1497845169000},{"id":122,"goodsId":40,"goodsType":0,"goodsName":"阿尔山森林草原深处秘境之旅","thumbnail":"/kkupload/45/454000/F454000/1706131755566U53.jpg","unitPrice":"0.01","amount":1,"price":"0.01","state":0,"cpPrivateInfoStr":"{\"address\":\"中国-全部\",\"name\":\"12\",\"tel\":\"2\"}","cpPrivateInfo":{"name":"12","tel":"2","address":"中国-全部"},"createDate":1497844969000,"updateDate":1497844969000},{"id":120,"goodsId":40,"goodsType":0,"goodsName":"阿尔山森林草原深处秘境之旅","thumbnail":"/kkupload/45/454000/F454000/1706131755566U53.jpg","unitPrice":"0.01","amount":1,"price":"0.01","state":0,"cpPrivateInfoStr":"{\"address\":\"中国-全部\",\"name\":\"1\",\"tel\":\"2\"}","cpPrivateInfo":{"name":"1","tel":"2","address":"中国-全部"},"createDate":1497840631000,"updateDate":1497840631000}]
         * pagination : {"page":1,"start":0,"pageSize":5,"count":44}
         */

        private PaginationBean pagination;
        @JsonAdapter(ListTypeAdapterFactory.class)
        private List<OrdersBean> orders;

        public PaginationBean getPagination() {
            return pagination;
        }

        public void setPagination(PaginationBean pagination) {
            this.pagination = pagination;
        }

        public List<OrdersBean> getOrders() {
            return orders;
        }

        public void setOrders(List<OrdersBean> orders) {
            this.orders = orders;
        }

        public class PaginationBean implements Serializable {
            /**
             * page : 1
             * start : 0
             * pageSize : 5
             * count : 44
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

        public class OrdersBean implements Serializable {
            /**
             * id : 125
             * goodsId : 40
             * goodsType : 0
             * goodsName : 阿尔山森林草原深处秘境之旅
             * thumbnail : /kkupload/45/454000/F454000/1706131755566U53.jpg
             * unitPrice : 0.01
             * amount : 1
             * price : 0.01
             * state : 1
             * cpPrivateInfoStr : {"address":"中国-全部","name":"1","tel":"13613088581"}
             * cpPrivateInfo : {"name":"1","tel":"13613088581","address":"中国-全部"}
             * createDate : 1497853372000
             * updateDate : 1497853698000
             */

            private String id;
            private int goodsId;
            private int goodsType;
            private String goodsName;
            private String thumbnail;
            private String unitPrice;
            private int amount;
            private String price;
            private int state;
            private String cpPrivateInfoStr;
            private CpPrivateInfoBean cpPrivateInfo;
            private long createDate;
            private long updateDate;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(int goodsId) {
                this.goodsId = goodsId;
            }

            public int getGoodsType() {
                return goodsType;
            }

            public void setGoodsType(int goodsType) {
                this.goodsType = goodsType;
            }

            public String getGoodsName() {
                return goodsName;
            }

            public void setGoodsName(String goodsName) {
                this.goodsName = goodsName;
            }

            public String getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(String thumbnail) {
                this.thumbnail = thumbnail;
            }

            public String getUnitPrice() {
                return unitPrice;
            }

            public void setUnitPrice(String unitPrice) {
                this.unitPrice = unitPrice;
            }

            public int getAmount() {
                return amount;
            }

            public void setAmount(int amount) {
                this.amount = amount;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public String getCpPrivateInfoStr() {
                return cpPrivateInfoStr;
            }

            public void setCpPrivateInfoStr(String cpPrivateInfoStr) {
                this.cpPrivateInfoStr = cpPrivateInfoStr;
            }

            public CpPrivateInfoBean getCpPrivateInfo() {
                return cpPrivateInfo;
            }

            public void setCpPrivateInfo(CpPrivateInfoBean cpPrivateInfo) {
                this.cpPrivateInfo = cpPrivateInfo;
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

            public class CpPrivateInfoBean implements Serializable {
                /**
                 * name : 1
                 * tel : 13613088581
                 * address : 中国-全部
                 */

                private String name;
                private String tel;
                private String address;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getTel() {
                    return tel;
                }

                public void setTel(String tel) {
                    this.tel = tel;
                }

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }
            }
        }
    }
}
