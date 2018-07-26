package com.konka.kktripclient.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.konka.advert.utils.DeviceInfoManager;
import com.konka.android.tv.KKFactoryManager;

import java.io.File;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by The_one on 2017-5-9.
 * <p>
 * 常用工具类
 */

public class CommonUtils {

    public static void downloadPicture(Context context, String url, int loadingRes, int errorRes, ImageView imageView) {
        LogUtils.d("Glide", "--- Glide ---");
        Glide.with(context).load(url)
                .fitCenter()
                .placeholder(loadingRes)
                .error(errorRes)
                .into(imageView);
    }

    public static String getVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        String versionName;
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            //PreferencesHelper.saveString(PreferencesHelper.VERSION_NAME, versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionName = "1.0.0";
            //versionName = PreferencesHelper.getString(PreferencesHelper.VERSION_NAME);
        }
        return versionName;
    }

    public static int getVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        int versionCode;
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionCode = 1;
        }
        return versionCode;
    }


    /**
     * 价格格式化
     *
     * @param price 传入价格数值
     * @return 转成去掉小数点后面多余的0
     */
    public static String formatPrice(String price) {
        if (price.indexOf(".") > 0) {
            price = price.replaceAll("0+?$", "");// 去掉多余的0
            price = price.replaceAll("[.]$", "");// 如最后一位是.则去掉
        }
        return price;
    }

    private static SimpleDateFormat formatter;

    /**
     * 时间格式化
     *
     * @return 时间格式yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentDateString() {
        Date date = new Date();
        if (formatter == null) {
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        synchronized (formatter) {
            LogUtils.d("getCurrentDateString", "formatter = " + formatter.format(date));
            return formatter.format(date);
        }
    }

    /**
     * 将时间戳转换为时间
     */
    public static String stampToDate(long lt) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static void installApk(Context context, String storePath) {
        LogUtils.d("installApk", "install Apk path : " + storePath);
        File apkFile = new File(storePath);
        if (!apkFile.exists()) {
            return;
        }
        String command = "chmod 777 " + storePath;
        LogUtils.d("installApk", "command: " + command);
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec(command);
        } catch (IOException e) {
            LogUtils.d("installApk", "chmod failed");
            e.printStackTrace();
        }
        LogUtils.d("installApk", "apkFile toString : " + apkFile.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");
        try {
            context.startActivity(intent);
        } catch (Throwable e) {
            LogUtils.d("installApk", "installApk catch throwable " + e.toString());
        }
    }

    public static String getApkDownloadPath(Context context) {
        LogUtils.d("installApk", "getApkDownloadPath : " + context.getFilesDir() + "/" + Constant.APK_NAME);
        return context.getFilesDir() + "/" + Constant.APK_NAME;
    }

    /**
     * @param context 上下文
     * @param dpValue 设备独立像素dip
     * @return 像素值
     * @brief 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        // final float scale = 1f;
        // Trace.Info("缩放比例-->"+scale);
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 获取设备MAC地址
     *
     * @return MAC地址
     */
    public static String getMacAddress() {
        String macAddress;
        StringBuffer buffer = new StringBuffer();
        NetworkInterface networkInterface;
        try {
            networkInterface = NetworkInterface.getByName("eth0");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "00:00:00:00:00:00";
            }
            byte[] address = networkInterface.getHardwareAddress();
            for (byte b : address) {
                buffer.append(String.format("%02X:", b));
            }
            if (buffer.length() > 0) {
                buffer.deleteCharAt(buffer.length() - 1);
            }
            macAddress = buffer.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "00:00:00:00:00:00";
        }
        return macAddress;
    }

    /**
     * 获取电视串号
     *
     * @return
     */
    public static String getSerialNumber(Context context) {
        byte[] serial = KKFactoryManager.getInstance(context.getApplicationContext()).getSerialNumber();
        if (serial == null) {
            LogUtils.i("getSerialNumber##serial", "null");
            return "";
        }
        String serialNum = new String(serial).trim();
        if (serialNum == null || serialNum.equals("")) {
            return "";
        }
        return serialNum.substring(0, 20);
    }

    private static String mPlatformName;

    /**
     * 获取平台信息
     *
     * @param context
     */
    public static String getCurPlatform(Context context) {
        if (!TextUtils.isEmpty(mPlatformName)) {
            return mPlatformName;
        }
        try {
            mPlatformName = DeviceInfoManager.getInstance(context).getPlatform();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(mPlatformName)) {
            mPlatformName = "unknow";
        }
        LogUtils.d("utils", "platform = " + mPlatformName);

        return mPlatformName;
    }

    /**
     * 刷新对应view
     *
     * @param view
     */
    public static void refreshView(View view) {
        if (CommonUtils.getModel().contains("konka") && CommonUtils.getModel().contains("2991")) {
            view.requestLayout();
            view.invalidate();
        }
    }


    public static String getModel() {
        String model = Build.MODEL.toLowerCase();
        return model;
    }

    public static int screenWidth;
    public static int screenHeight;

    public static void computeScreenSize(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
        screenHeight = outMetrics.heightPixels;
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }
}
