package com.konka.kktripclient;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.haizhi.SDK.SDKClient;
import com.konka.account.callback.CallBack;
import com.konka.account.wrapper.UUCWrapper;
import com.konka.kktripclient.detail.IJKPlayer.IJKPlayer;
import com.konka.kktripclient.net.HttpHelper;
import com.konka.kktripclient.pay.UserHelper;
import com.konka.kktripclient.utils.CommonUtils;
import com.konka.kktripclient.utils.Constant;
import com.konka.kktripclient.utils.LogUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by smith on 2017/05/11.
 * <p>
 * TripApplication
 */
public class TripApplication extends Application {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
        initUUCWrapper();
        initBigData();
        initIJKPlayer();
        initConstant();
//        AdvertSDKManager.init(this);
        //友盟统计
        if (!Constant.MONKEY) MobclickAgent.setCatchUncaughtExceptions(true);
    }

    @Override
    public void onTerminate() {
        Log.d(TAG, "onTerminate");
        UUCWrapper.getInstance(this).clean(new CallBack<Boolean>() {
            @Override
            public void onComplete(Boolean result) {
                //清理成功与否
            }

            @Override
            public void onError(String s) {
                //返回错误信息
            }
        });
        super.onTerminate();
        IjkMediaPlayer.native_profileEnd();
    }

    @Override
    public void onLowMemory() {
        Log.d(TAG, "onLowMemory");
        super.onLowMemory();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initUUCWrapper() {
        UUCWrapper.getInstance(this).init(Constant.APP_ID, Constant.APP_KEY, new CallBack<Boolean>() {
            @Override
            public void onComplete(Boolean result) {
                //用于ID为空时，就尝试获取一次用户信息及订单信息
                if (result) {
                    //若用户已登录获取订单信息
                    //getPayInfo(true);
                    LogUtils.d(TAG, "init success");
                    UserHelper.getInstance(getApplicationContext()).isUserLogin();
                }

            }

            @Override
            public void onError(String msg) {
                //TODO处理错误信息
                LogUtils.d(TAG, "init fail");
            }
        });
    }

    //初始化大数据
    private void initBigData() {
        SDKClient.initInsClient(this, this.getContentResolver(), Constant.BIG_DATA_TOKEN, Constant.MONKEY);
    }

    //初始化播放器
    private void initIJKPlayer() {
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

        Log.d(TAG,"platform:"+CommonUtils.getModel());
        //配置对应平台的播放器属性,默认采用系统播放器,硬解,SurfaceView
        if (CommonUtils.getModel().contains("konka")) {
            if(CommonUtils.getModel().contains("2991")||CommonUtils.getModel().contains("2998")){
                Constant.IJK_MEDIA_PLAYER = Constant.PV_PLAYER_IjkMediaPlayer;
            }else if(CommonUtils.getModel().contains("2861")){
//                Constant.IJK_MEDIA_PLAYER = Constant.PV_PLAYER_IjkMediaPlayer;
                Constant.IJK_RENDER_VIEW = IJKPlayer.RENDER_TEXTURE_VIEW;
            }

        }
    }

    // 初始化常量
    private void initConstant() {
        try {
            File file = new File(Constant.TEST_SERVER_FILE);
            if (file.exists()) {
                Constant.BASE_URL = Constant.TEST_URL;
            }
            SharedPreferences preferences = getSharedPreferences(Constant.PREFERENCES_NAME_TAB, MODE_PRIVATE);
            Constant.SERVICE_ADDRESS = preferences.getString(Constant.PREFERENCES_VALUE_TAB_SERVICE_ADDRESS, "");
            HttpHelper.getInstance(getApplicationContext()).sn = CommonUtils.getSerialNumber(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
