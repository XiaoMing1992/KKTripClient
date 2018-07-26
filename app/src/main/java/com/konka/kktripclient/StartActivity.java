package com.konka.kktripclient;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.konka.kktripclient.net.HttpHelper;
import com.konka.kktripclient.net.info.HttpErrorEvent;
import com.konka.kktripclient.net.info.IEvent;
import com.konka.kktripclient.net.info.StartADEvent;
import com.konka.kktripclient.utils.Constant;
import com.konka.kktripclient.utils.LogUtils;
import com.konka.kktripclient.utils.NetworkUtils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class StartActivity extends Activity {
    private final String TAG = this.getClass().getSimpleName();

    private ImageView adImage;
    private TextView adText;
    private EventBus eventBus;

    private static final int MSG_START_ACTIVITY = 1000;
    private static final int MSG_COUNTDOWN = 1001;
    private static final int MSG_SHOW_AD = 1002;
    private int time = 3;
    private String fileName = "ad_picture.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtils.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ad);
        initView();
        initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        LogUtils.d(TAG, "onNewIntent");
        super.onNewIntent(intent);
    }

    @Override
    public void onBackPressed() {
        LogUtils.d(TAG, "onBackPressed");
        if (eventBus.isRegistered(this)) {
            eventBus.unregister(this);
        }
        handler.removeCallbacksAndMessages(null);
        finish();
    }

    @Override
    protected void onDestroy() {
        LogUtils.d(TAG, "onDestroy");
        super.onDestroy();
        if (eventBus.isRegistered(this)) {
            eventBus.unregister(this);
        }
        Glide.get(this).clearMemory();
    }

    private void initView() {
        adImage = (ImageView) findViewById(R.id.ad_image);
        adText = (TextView) findViewById(R.id.ad_text);
    }

    private void initData() {
        fileName = getFilesDir().getPath() + "/ad_picture.jpg";
        if (eventBus == null) {
            eventBus = EventBus.getDefault();
            if (!eventBus.isRegistered(this)) {
                eventBus.register(this);
            }
        }
        if (!Constant.MONKEY) {
            if (HttpHelper.getInstance(this).getStartAd()) {
                LogUtils.d(TAG, "getStartAd");
            }
            handler.sendEmptyMessage(MSG_SHOW_AD);
        } else {
            handler.sendEmptyMessage(MSG_START_ACTIVITY);
        }
    }

    private void showAdView(String name) {
        if (getReturnCode().equals(Constant.RETURN_SUCCESS)) {
            try {
                Glide.with(this)
                        .load(new File(name))
                        .listener(new RequestListener<File, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, File model, Target<GlideDrawable> target, boolean isFirstResource) {
                                LogUtils.d(TAG, "Glide onException:" + e.getMessage());
                                handler.sendEmptyMessage(MSG_START_ACTIVITY);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, File model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                LogUtils.d(TAG, "Glide onResourceReady");
                                handler.sendEmptyMessage(MSG_COUNTDOWN);
                                return false;
                            }
                        })
                        .into(adImage);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                handler.sendEmptyMessage(MSG_START_ACTIVITY);
            }
        } else {
            handler.sendEmptyMessage(MSG_START_ACTIVITY);
        }
    }

    private void downloadPicture(final String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;
                HttpURLConnection connection = null;
                try {
                    url = new URL(path);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(5000);// 超时设置
                    connection.setDoInput(true);
                    connection.setUseCaches(false);// 设置不使用缓存
                    InputStream inputStream = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();

                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                        fileOutputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    LogUtils.d(TAG, "downloadPicture OK");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * 设置广告返回码
     *
     * @param returnCode 广告返回码
     */
    private void setReturnCode(String returnCode) {
        try {
            SharedPreferences preferences = getSharedPreferences(Constant.PREFERENCES_NAME_AD, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constant.PREFERENCES_VALUE_AD_RETURN_CODE, returnCode);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取广告返回码
     */
    private String getReturnCode() {
        try {
            SharedPreferences preferences = getSharedPreferences(Constant.PREFERENCES_NAME_AD, MODE_PRIVATE);
            return preferences.getString(Constant.PREFERENCES_VALUE_AD_RETURN_CODE, "-1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 设置广告文件名
     *
     * @param fileName 广告文件名
     */
    private void setFileName(String fileName) {
        try {
            SharedPreferences preferences = getSharedPreferences(Constant.PREFERENCES_NAME_AD, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constant.PREFERENCES_VALUE_AD_FILE_NAME, fileName);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取广告文件名
     */
    private String getFileName() {
        try {
            SharedPreferences preferences = getSharedPreferences(Constant.PREFERENCES_NAME_AD, MODE_PRIVATE);
            return preferences.getString(Constant.PREFERENCES_VALUE_AD_FILE_NAME, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 设置广告更新时间
     *
     * @param updateTime 广告更新时间
     */
    private void setUpdateTime(long updateTime) {
        try {
            SharedPreferences preferences = getSharedPreferences(Constant.PREFERENCES_NAME_AD, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(Constant.PREFERENCES_VALUE_AD_UPDATE_TIME, updateTime);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取广告更新时间
     */
    private long getUpdateTime() {
        try {
            SharedPreferences preferences = getSharedPreferences(Constant.PREFERENCES_NAME_AD, MODE_PRIVATE);
            return preferences.getLong(Constant.PREFERENCES_VALUE_AD_UPDATE_TIME, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private final TimeHandler handler = new TimeHandler(this);

    private static class TimeHandler extends Handler {
        private final WeakReference<StartActivity> weakReference;

        public TimeHandler(StartActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            StartActivity activity = weakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case MSG_START_ACTIVITY:
                        activity.handler.removeCallbacksAndMessages(null);
                        Intent intent = new Intent(activity, MainActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                        break;
                    case MSG_COUNTDOWN:
                        activity.adText.setVisibility(View.VISIBLE);
                        activity.adText.setText("" + activity.time + activity.getString(R.string.ad_time));
                        LogUtils.d(activity.TAG, "advertisement:" + activity.time);
                        if (activity.time <= 0) {
                            activity.handler.sendEmptyMessage(MSG_START_ACTIVITY);
                        } else {
                            activity.handler.sendEmptyMessageDelayed(MSG_COUNTDOWN, 1000);
                            activity.time--;
                        }
                        break;
                    case MSG_SHOW_AD:
                        activity.showAdView(activity.getFileName());
                        break;
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IEvent event) {
        LogUtils.d(TAG, "onEvent");
        if (eventBus.isRegistered(this)) {
            eventBus.unregister(this);
        }
        if (event instanceof StartADEvent) {
            setReturnCode(((StartADEvent) event).getRet().getRet_code());
            if (((StartADEvent) event).getRet().getRet_code().equals(Constant.RETURN_SUCCESS)) {
                if (((StartADEvent) event).getData().getUpdateDate() > getUpdateTime() || !new File(fileName).exists()) {
                    downloadPicture(NetworkUtils.toURL(((StartADEvent) event).getData().getThumbnail()));
                    setFileName(fileName);
                    setUpdateTime(((StartADEvent) event).getData().getUpdateDate());
                }
            } else {
                LogUtils.d(TAG, "no data");
            }
        } else if (event instanceof HttpErrorEvent) {
            LogUtils.d(TAG, "HttpErrorEvent");
            setReturnCode(((HttpErrorEvent) event).getRetCode());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //友盟统计
        if (!Constant.MONKEY) MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //友盟统计
        if (!Constant.MONKEY) MobclickAgent.onPause(this);
    }
}
