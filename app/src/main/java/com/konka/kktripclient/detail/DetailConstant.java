package com.konka.kktripclient.detail;

/**
 * Created by Zhou Weilin on 2017-6-13.
 */

public class DetailConstant {
    public static final int GOOD_ROUTE = 0;//路线标识
    public static final int GOOD_TICKET = 1;//门票标识
    public static final int ILLEGAL_ID = -1;//非法的商品的ID

    public static final int START_TYPE_WINDOW = 1;//小窗口打开播放界面
    public static final int START_TYPE_ROUTE = 2;//路线海报打开播放界面

    public static final String KEY_TICKET_QR_URL = "ticket_qr_url";//传递门票二维码的key
    public static final String KEY_ROUTEINFO = "route_info";//传递路线信息的key
    public static final String KEY_ORDERINFO = "order_info";//传递订单信息的key
    public static final String KEY_START_TYPE = "start_type";//传递订单信息的key

    public static final String TAG_DETAIL = "trip_detail";
    public static final String TAG_IJK = "trip_ijk";

}
