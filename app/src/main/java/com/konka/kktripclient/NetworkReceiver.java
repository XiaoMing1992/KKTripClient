package com.konka.kktripclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.konka.kktripclient.ui.ToastView;
import com.konka.kktripclient.utils.LogUtils;

/**
 * Created by The_one on 2017-7-20.
 * <p>
 * 网络状态监听
 */

public class NetworkReceiver extends BroadcastReceiver {
    private final String TAG = this.getClass().getSimpleName();
    private ToastView mToastView;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mToastView == null) {
            mToastView = new ToastView(context);
        }
        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isAvailable()) {
                // 网络连接
                String name = netInfo.getTypeName();
                if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // WiFi网络
                    LogUtils.i(TAG, "WiFi网络:" + name);
                } else if (netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
                    // 有线网络
                    LogUtils.i(TAG, "有线网络:" + name);
                } else {
                    LogUtils.i(TAG, "网络连接:" + name);
                }
                mToastView.setText("网络已连接");
                mToastView.show();
            } else {
                // 网络断开
                LogUtils.i(TAG, "网络断开");
                mToastView.setText("网络连接已断开");
                mToastView.show();
            }
        }
    }
}
