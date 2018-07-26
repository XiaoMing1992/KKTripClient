package com.konka.kktripclient.net.info;

import com.google.gson.annotations.JsonAdapter;
import com.konka.kktripclient.net.json.ListTypeAdapterFactory;

import java.util.List;

/**
 * Created by smith on 2017/06/01.
 */
public class AllTicketsSortEvent implements IEvent {

    /**
     * ret : {"ret_code":"0","ret_msg":"success"}
     * data : [{"id":1,"name":"主题乐园","description":"主题乐园门票","createDate":"2017-05-08 10:13:39","updateDate":"2017-05-08 10:13:39"},{"id":2,"name":"城市观光","description":"城市观光门票","createDate":"2017-05-08 10:14:04","updateDate":"2017-05-08 10:14:04"},{"id":3,"name":"名胜古迹","description":"名胜古迹门票","createDate":"2017-05-08 10:14:23","updateDate":"2017-05-08 10:14:23"},{"id":4,"name":"展览演出","description":"展览演出门票","createDate":"2017-05-08 10:14:59","updateDate":"2017-05-08 10:14:59"}]
     */

    private RetBean ret;
    @JsonAdapter(ListTypeAdapterFactory.class)
    private List<DataBean> data;

    public RetBean getRet() {
        return ret;
    }

    public void setRet(RetBean ret) {
        this.ret = ret;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
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
         * id : 1
         * name : 主题乐园
         * description : 主题乐园门票
         * createDate : 2017-05-08 10:13:39
         * updateDate : 2017-05-08 10:13:39
         */

        private int id;
        private String name;
        private String description;
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
    }
}
