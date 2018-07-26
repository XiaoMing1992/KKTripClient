package com.konka.kktripclient.net.info;

/**
 * Created by smith on 2017/06/21.
 */
public class OrderInfoEvent implements IEvent {

    /**
     * ret : {"ret_code":"0","ret_msg":"success"}
     * data : {"id":151,"goodsId":40,"goodsType":0,"goodsName":"阿尔山森林草原深处秘境之旅","thumbnail":"/kkupload/45/454000/F454000/1706131755566U53.jpg","unitPrice":"0.01","amount":1,"price":"0.01","state":0,"cpPrivateInfoStr":"{\"address\":\"中国-全部\",\"name\":\"\",\"tel\":\"\"}","cpPrivateInfo":{"name":"","tel":"","address":"中国-全部"},"createDate":1498035491000,"updateDate":1498035491000}
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
         * id : 151
         * goodsId : 40
         * goodsType : 0
         * goodsName : 阿尔山森林草原深处秘境之旅
         * thumbnail : /kkupload/45/454000/F454000/1706131755566U53.jpg
         * unitPrice : 0.01
         * amount : 1
         * price : 0.01
         * state : 0
         * cpPrivateInfoStr : {"address":"中国-全部","name":"","tel":""}
         * cpPrivateInfo : {"name":"","tel":"","address":"中国-全部"}
         * createDate : 1498035491000
         * updateDate : 1498035491000
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

        public class CpPrivateInfoBean {
            /**
             * name :
             * tel :
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
