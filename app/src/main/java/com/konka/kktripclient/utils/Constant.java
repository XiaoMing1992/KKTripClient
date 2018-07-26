package com.konka.kktripclient.utils;

import com.konka.kktripclient.detail.IJKPlayer.IJKPlayer;

/**
 * Created by 米娜桑 on 2017-6-12.
 * <p>
 * 全局常量
 */
public class Constant {

    //RSA加密算法使用的PUBLIC_KEY
    public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCsrct3lTjIKdHIEoW9VI4z9VC0kwmqPVrtVqQBzV41rw6xbFmweQEDd2mQzftgR3PFoRUcuWsiyDKhEbdVCgePvxqwpdTwQb3jwXfEtLh+fm5KDaClka+R01ubcGo7PBcbA1t1U7FgQCS6jCvvbYYpAG5s7BHQWgRPg9NhhXCqRwIDAQAB";

    //静态渠道号,默认为1代表康佳平台
    public static String CHANNEL_ID = "1";

    // 服务器域名:默认正式服务器域名
    public static String BASE_URL = "http://api.kkapp.com/";
    // 测试服务器域名
    public static final String TEST_URL = "http://test.kkapp.com/";
    // 判断是否是测试服务器的文件名
    public static final String TEST_SERVER_FILE = "/data/misc/konka/DefaultLoginServer.txt";
    // 获取到的ServiceAddr地址
    public static String SERVICE_ADDRESS = "http://api.kkapp.com/";

    public static String serialNumber = "unknow";
    public static final String SCAN_QRCODE = "SCAN_QRCODE";
    public static final String LOGIN = "LOGIN";

    public static final String APP_KEY = "59HDno0iuSE";
    public static final String APP_ID = "8A529KWYbAf";

    // 跑monkey使用需要使用
    public static final boolean MONKEY = false;

    public static int width = 1920;
    public static int height = 1080;

    //订单状态，订单有四种状态（0:订单生成、1:用户已支付、2:商家确认、3:出行完成）
    public static final int ORDER_STATE_MAKE = 0;
    public static final int ORDER_STATE_PAY = 1;
    public static final int ORDER_STATE_CONFIRM = 2;
    public static final int ORDER_STATE_DONE = 3;
    public static final int ORDER_STATE_OUT = 4; //订单已过期

    public static final String APK_NAME = "temp.apk";
    /**
     * 各类的键值
     * 发送intent的商品的id的key
     */
    public static final String KEY_TYPE = "type";
    public static final String KEY_GOOD_ID = "good_id";
    public static final String KEY_GOOD_TYPE = "good_type";
    public static final String KEY_START_SOURCE = "start_source";

    /**
     * 返回值类型
     */
    public static final String RETURN_SUCCESS = "0";

    /**
     * 弹窗广告的类型
     */
    public static final int RECOMMEND_DIALOG_ROUTE = 0;
    public static final int RECOMMEND_DIALOG_TICKET = 1;
    public static final int RECOMMEND_DIALOG_VIDEO = 2;

    /**
     * 分类的类型
     */
    public static final int CATEGORY_DEFAULT = 0;
    public static final int CATEGORY_ROUTE = 1;
    public static final int CATEGORY_TICKET = 2;

    /**
     * SharedPreferences中的值
     */
    public static final String PREFERENCES_NAME_TAB = "data_tab";
    public static final String PREFERENCES_NAME_AD = "data_ad";
    public static final String PREFERENCES_NAME_TOAST_ADVERS = "data_toast_advers";
    public static final String PREFERENCES_VALUE_TAB_SERVICE_ADDRESS = "service_address";
    public static final String PREFERENCES_VALUE_TAB_UPDATE_TIME = "update_time";
    public static final String PREFERENCES_VALUE_AD_RETURN_CODE = "return_code";
    public static final String PREFERENCES_VALUE_AD_FILE_NAME = "file_name";
    public static final String PREFERENCES_VALUE_AD_UPDATE_TIME = "update_time";
    public static final String PREFERENCES_VALUE_TOAST_ADVERS_UPDATE_TIME = "update_time";

    /**
     * 启动的类型
     */
    public static final String LAUNCH_APK = "00";
    public static final String LAUNCH_COLLECT = "41";
    public static final String LAUNCH_ROUTE_LIST = "42";
    public static final String LAUNCH_ROUTE_DETAIL = "43";
    public static final String LAUNCH_TICKET_LIST = "44";
    public static final String LAUNCH_TICKET_DETAIL = "45";
    public static final String LAUNCH_VIDEO = "46";
    public static final String LAUNCH_VIDEO_WIDGET = "47";


    //-start-ijkPlayer播放器状态相关--//
    public static final int PV_PLAYER_AndroidMediaPlayer = 1;
    public static final int PV_PLAYER_IjkMediaPlayer = 2;
    public static int IJK_MEDIA_PLAYER = PV_PLAYER_AndroidMediaPlayer;
    public static int IJK_RENDER_VIEW = IJKPlayer.RENDER_SURFACE_VIEW;
    public static boolean IJK_MEDIA_CODEC = true;//true是硬解，false是软解
    public static boolean IJK_OPENSLES = false;
    //-end-ijkPlayer播放器状态相关--//

    /**
     * 大数据上报TOKEN
     */
    public static final String BIG_DATA_TOKEN = "HNrZmWBZ";
    /**
     * 大数据上报项
     */
    public static final String BIG_DATA_ROUTE_USER_ORDER = "yql_route_user_order";
    public static final String BIG_DATA_RECOMMEND_CLICK = "yql_recommend_click";
    public static final String BIG_DATA_ROUTE_USER_INFO = "yql_route_user_info";
    public static final String BIG_DATA_ENTER_GOODS_DETAIL = "yql_enter_goods_detail";
    public static final String BIG_DATA_PLAY_VIDEO = "yql_play_video";
    public static final String BIG_DATA_VIDEO_MENU = "yql_video_menu";
    public static final String BIG_DATA_DETAIL_BUTTON_CLICK = "yql_detail_button_click";
    public static final String BIG_DATA_FILL_IN_ORDER_INFO = "yql_fill_in_order_info";
    public static final String BIG_DATA_LOGIN_USER_CENTER = "yql_login_user_center";
    public static final String BIG_DATA_ERROR_EVENT = "yql_error_event";
    public static final String BIG_DATA_COLLECT_GOODS = "yql_collect_goods";
    /**
     * 大数据上报的值(详情页启动来源)
     */
    public static final String BIG_DATA_VALUE_KEY_SOURCE = "source";
    public static final String BIG_DATA_VALUE_KEY_NAME = "name";
    public static final String BIG_DATA_VALUE_KEY_LOCATION = "location";
    public static final String BIG_DATA_VALUE_SOURCE_TJ = "推荐页";
    public static final String BIG_DATA_VALUE_SOURCE_SW = "小窗口";
    public static final String BIG_DATA_VALUE_SOURCE_TC = "弹窗页";
    public static final String BIG_DATA_VALUE_SOURCE_LB = "列表页";
    public static final String BIG_DATA_VALUE_SOURCE_DD = "订单页";
    public static final String BIG_DATA_VALUE_SOURCE_SC = "收藏页";
    public static final String BIG_DATA_VALUE_SOURCE_ZZ = "遮罩页";
    //大数据登陆来源
    public static final String BIG_DATA_VALUE_SOURCE_DM = "详情页-马上浪";
    public static final String BIG_DATA_VALUE_SOURCE_DS = "详情页-收藏";

}
