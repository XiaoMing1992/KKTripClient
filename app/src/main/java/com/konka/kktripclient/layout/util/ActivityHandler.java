package com.konka.kktripclient.layout.util;

import android.graphics.Rect;
import android.os.Handler;

import com.konka.kktripclient.MainActivity;
import com.konka.kktripclient.R;
import com.konka.kktripclient.layout.data.KKServerDataManager;
import com.konka.kktripclient.layout.tab.RecommendFactory;
import com.konka.kktripclient.layout.track.TrackBaseActivity;

public class ActivityHandler {

    private static MainActivity mInstance;
    private static Handler mUIHandler;

    public static void setInstance(MainActivity activity) {
        mInstance = activity;
    }

    public static MainActivity getInstance() {
        return mInstance;
    }

    public static void setUIHandler(Handler handler) {
        mUIHandler = handler;
    }

    public static Handler getUIHandler() {
        if (mUIHandler == null) {
            mUIHandler = new Handler(getInstance().getMainLooper());
        }
        return mUIHandler;
    }

    public static void initAfter(MainActivity activity) {
        try {
            ActivityHandler.setInstance(activity);
            ActivityHandler.setUIHandler(new Handler(activity.getMainLooper()));

            // 注册焦点框图片
            Rect rect = new Rect(-10, 0, -10, 30);
            activity.registerTrackDrawable(LayoutConstant.TRACK_TYPE_TAB_BUTTON, activity.getResources().getDrawable(R.drawable.recommend_tab_button_track), rect);

            rect = new Rect(3, 3, 3, 3);
            activity.registerTrackDrawable(TrackBaseActivity.DEFAULT_TRACK_TYPE, activity.getResources().getDrawable(R.drawable.main_track), rect);

//			ac.setTrackVisibility(false);

            RecommendFactory.getInstance().addByNetwork();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过ActivityHandler调用RecommendFactory小窗口onResume
     */
    public static void onResumeVideoWidget() {
        RecommendFactory.getInstance().onResumeVideoWidget();
    }

    /**
     * 通过ActivityHandler调用RecommendFactory小窗口onPause
     */
    public static void onPauseVideoWidget() {
        RecommendFactory.getInstance().onPauseVideoWidget();
    }

    public static void destroy() {
        CommonFunction.close();
        KKServerDataManager.getInstance(getInstance()).close();
        mInstance = null;
        mUIHandler = null;
    }

}
