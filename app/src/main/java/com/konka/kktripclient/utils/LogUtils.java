package com.konka.kktripclient.utils;

import android.util.Log;

/**
 * Created by smith on 2017/05/12.
 */
public class LogUtils {
    /**
     * 是否开启debug
     */
    public static boolean isDebug=true;

    public static void e(String TAG,String msg){
        if(isDebug){
            Log.e(TAG, msg+"");
        }
    }

    public static void i(String TAG,String msg){
        if(isDebug){
            Log.i(TAG, msg+"");
        }
    }

    public static void d(String TAG,String msg){
        if(isDebug){
            Log.d(TAG, msg+"");
        }
    }

    public static void w(String TAG,String msg){
        if(isDebug){
            Log.w(TAG, msg+"");
        }
    }
}
