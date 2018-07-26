package com.konka.kktripclient.net.info;

/**
 * Created by The_one on 2017-10-23.
 */

public class ToastAdverEventDataBean {
    /**
     * id : 5
     * name : 弹窗1
     * type : 1
     * poster : /kkupload/45/453000/F453000/170601202311McyP.png
     * resourceId : 35
     * tourRoute : null
     * ticket : {"id":35,"name":"锦绣中华","description":"锦绣中华","originalPrice":200.88,"discountType":"大屏专享价","discountPrice":180,"customService":"114","goodsType":1,"businessHours":"每天上午8点到晚上10点","score":5,"weight":12,"qrcode":"http://happyvalleywh.smartoct.com/weixin/?r=ticketwindow/index&id=2&ocid=10001&tcid=2&sn=fee8f0dfe2fb0c30da8ad162bf689f1a&param=access_token&code=061y9nG32bO6VK0MyrH32U4CG32y9nGX","thumbnail":"/kkupload/45/455000/F455000/170531195829LtbP.png","poster1":"/kkupload/45/455000/F455000/170531195829LtbP.png","poster2":"string","poster3":"string","poster4":"string","poster5":"string","createDate":1496629451000,"updateDate":null}
     * video : null
     * createDate : 1494588373000
     * updateDate : 1496630180000
     */

    private int id;
    private String name;
    private int type;
    private String poster;
    private int resourceId;
    private TourRoutesBean tourRoute;
    private TicketsBean ticket;
    private Object video;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public Object getVideo() {
        return video;
    }

    public void setVideo(Object video) {
        this.video = video;
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

    public TourRoutesBean getTourRoute() {
        return tourRoute;
    }

    public TicketsBean getTicket() {
        return ticket;
    }

    public void setTourRoute(TourRoutesBean tourRoute) {
        this.tourRoute = tourRoute;
    }

    public void setTicket(TicketsBean ticket) {
        this.ticket = ticket;
    }
}
