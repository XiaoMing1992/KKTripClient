package com.konka.kktripclient.layout.util;

import android.content.Context;
import android.content.Intent;

import com.konka.account.callback.CallBack;
import com.konka.account.wrapper.UUCWrapper;
import com.konka.kktripclient.activity.CategoryActivity;
import com.konka.kktripclient.activity.CollectActivity;
import com.konka.kktripclient.detail.BookActivity;
import com.konka.kktripclient.detail.DetailActivity;
import com.konka.kktripclient.detail.DetailConstant;
import com.konka.kktripclient.detail.OrderActivity;
import com.konka.kktripclient.detail.VideoActivity;
import com.konka.kktripclient.detail.WriteActivity;
import com.konka.kktripclient.detail.bean.OrderInfo;
import com.konka.kktripclient.detail.bean.RouteInfo;
import com.konka.kktripclient.pay.UserHelper;
import com.konka.kktripclient.ui.popupwindow.LoginWindow;
import com.konka.kktripclient.utils.Constant;

/**
 * Created by The_one on 2017-6-12.
 * <p>
 * 用于启动页面
 */

public class LaunchHelper {
    /**
     * 启动分类页
     *
     * @param context Activity
     * @param type    type
     */
    public static void startCategoryActivity(Context context, String type) {
        Intent intent = new Intent(context, CategoryActivity.class);
        intent.putExtra(Constant.KEY_TYPE, type);
        context.startActivity(intent);
    }

    /**
     * 启动详情页
     *
     * @param context Activity
     * @param type    type
     * @param id      id
     * @param source  source
     */
    public static void startDetailActivity(Context context, int type, int id, String source) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(Constant.KEY_GOOD_TYPE, type);
        intent.putExtra(Constant.KEY_GOOD_ID, id);
        intent.putExtra(Constant.KEY_START_SOURCE, source);
        context.startActivity(intent);
    }


    /**
     * 启动填写信息页面
     *
     * @param context
     * @param routeInfo
     * @param source
     */
    public static void startWriteActivity(Context context, RouteInfo routeInfo, String source){
        Intent intent = new Intent(context,WriteActivity.class);
        intent.putExtra(DetailConstant.KEY_ROUTEINFO,routeInfo);
        intent.putExtra(Constant.KEY_START_SOURCE, source);
        context.startActivity(intent);
    }


    /**
     * 启动门票的二维码扫描页面
     *
     * @param context
     * @param goodId
     * @param qrUrl
     * @param source
     */
    public static void startBookActivity(Context context, int goodId, String qrUrl, String source){
        Intent intent = new Intent(context,BookActivity.class);
        intent.putExtra(Constant.KEY_GOOD_ID,goodId);
        intent.putExtra(DetailConstant.KEY_TICKET_QR_URL,qrUrl);
        intent.putExtra(Constant.KEY_START_SOURCE, source);
        context.startActivity(intent);
    }


    /**
     * 启动购买订单页
     *
     * @param context
     * @param orderInfo
     * @param source
     */
    public static void startBuyOrderActivity(Context context, OrderInfo orderInfo, String source){
        Intent intent = new Intent(context,OrderActivity.class);
        intent.putExtra(DetailConstant.KEY_ORDERINFO,orderInfo);
        intent.putExtra(Constant.KEY_START_SOURCE, source);
        context.startActivity(intent);
    }

    /**
     * 启动视频页
     *
     * @param context Activity
     * @param type    type
     * @param id      id
     * @param source  source
     */
    public static void startVideoActivity(Context context, int type, int id, String source) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra(DetailConstant.KEY_START_TYPE, type);
        intent.putExtra(Constant.KEY_GOOD_ID, id);
        intent.putExtra(Constant.KEY_START_SOURCE, source);
        context.startActivity(intent);
    }

    /**
     * 启动收藏页
     *
     * @param context Activity
     * @param source  source
     */
    public static void startCollectActivity(Context context, String source) {
        if (UserHelper.getInstance(context).getUserLogin()) {
            Intent intent = new Intent(context, CollectActivity.class);
            context.startActivity(intent);
        } else {
            final LoginWindow loginWindow = new LoginWindow(context);
            loginWindow.registerLoginListener(source);
            loginWindow.show();
            UUCWrapper.getInstance(context).getLoginQRCodeUrl(new CallBack<String>() {
                @Override
                public void onComplete(String url) {
                    loginWindow.refreshQRCode(url);
                }

                @Override
                public void onError(String s) {

                }
            });
        }
    }

}
