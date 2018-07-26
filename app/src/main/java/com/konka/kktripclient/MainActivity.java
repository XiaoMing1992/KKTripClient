package com.konka.kktripclient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iflytek.xiri.scene.ISceneListener;
import com.iflytek.xiri.scene.Scene;
import com.konka.kktripclient.activity.CollectActivity;
import com.konka.kktripclient.activity.ManageOrderActivity;
import com.konka.kktripclient.activity.SettingActivity;
import com.konka.kktripclient.detail.DetailConstant;
import com.konka.kktripclient.detail.VideoActivity;
import com.konka.kktripclient.detail.presenter.WindowPlayer;
import com.konka.kktripclient.layout.tab.MyFactory;
import com.konka.kktripclient.layout.track.TrackBaseActivity;
import com.konka.kktripclient.layout.util.ActivityHandler;
import com.konka.kktripclient.net.HttpHelper;
import com.konka.kktripclient.net.info.HttpErrorEvent;
import com.konka.kktripclient.net.info.IEvent;
import com.konka.kktripclient.net.info.ToastAdverEvent;
import com.konka.kktripclient.pay.UserHelper;
import com.konka.kktripclient.utils.AdHelper;
import com.konka.kktripclient.utils.CommonUtils;
import com.konka.kktripclient.utils.Constant;
import com.konka.kktripclient.utils.LogUtils;
import com.konka.kktripclient.utils.NetworkUtils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends TrackBaseActivity {
    private final String TAG = this.getClass().getSimpleName();

    private EventBus eventBus;

    private Button mButton;

    private long timeLong;
    private String timeString;

    private Scene scene;
    private VoiceListener voiceListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtils.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_main);
        ActivityHandler.initAfter(this);
        initView();
        initData();
        if (!NetworkUtils.isNetworkConnected(this)) {
            showErrorView();
        }
    }

    private void initView() {
        mButton = (Button) findViewById(R.id.button);
        mButton.setVisibility(View.GONE);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                intent.putExtra(DetailConstant.KEY_START_TYPE, DetailConstant.START_TYPE_WINDOW);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        // 初始化广告系统
//        AdHelper.getInstance().init(this);
        // 为防止在onDestroy中，程序直接退出，上传不了数据
//        BigDataHelper.getInstance().sendRouteUserInfo(CommonUtils.getCurrentDateString(), CommonUtils.getMacAddress(), "0");
        timeLong = System.currentTimeMillis();
        timeString = CommonUtils.getCurrentDateString();
        if (eventBus == null) {
            eventBus = EventBus.getDefault();
            if (!eventBus.isRegistered(this)) {
                eventBus.register(this);
            }
        }
        if (HttpHelper.getInstance(this).getToastAdverDetails()) {
            LogUtils.d(TAG, "getToastAdverDetails");
        }
        scene = new Scene(this);
        voiceListener = new VoiceListener();
        scene.init(voiceListener);
    }

    private void showErrorView() {
        LogUtils.d(TAG, "showErrorView");
        setContentView(R.layout.error_page);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.error_page);
        TextView textView = (TextView) findViewById(R.id.error_title);
        linearLayout.setBackgroundResource(R.drawable.main_bg);
        textView.setText(getText(R.string.error_no_network));
    }

    private void setUpdateTime(long updateTime) {
        try {
            SharedPreferences preferences = getSharedPreferences(Constant.PREFERENCES_NAME_TOAST_ADVERS, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(Constant.PREFERENCES_VALUE_TOAST_ADVERS_UPDATE_TIME, updateTime);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long getUpdateTime() {
        try {
            SharedPreferences preferences = getSharedPreferences(Constant.PREFERENCES_NAME_TOAST_ADVERS, MODE_PRIVATE);
            return preferences.getLong(Constant.PREFERENCES_VALUE_TOAST_ADVERS_UPDATE_TIME, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    protected void onPause() {
        LogUtils.d(TAG, "onPause");
        super.onPause();
        ActivityHandler.onPauseVideoWidget();
        //友盟统计
        if (!Constant.MONKEY) MobclickAgent.onPause(this);

    }

    @Override
    protected void onResume() {
        LogUtils.d(TAG, "onResume");
        super.onResume();
        ActivityHandler.onResumeVideoWidget();
        //友盟统计
        if (!Constant.MONKEY) MobclickAgent.onResume(this);

    }

    @Override
    public void onBackPressed() {
        LogUtils.d(TAG, "onBackPressed");
        ExitDialog exitDialog = new ExitDialog(this);
        exitDialog.setTime(timeString, timeLong);
        exitDialog.show();
    }

    @Override
    protected void onDestroy() {
        LogUtils.d(TAG, "onDestroy");
        super.onDestroy();
        if (scene != null) {
            scene.release();
        }
        WindowPlayer.getInstance().destroyWindow();
        AdHelper.getInstance().release();
        if (eventBus.isRegistered(this)) {
            eventBus.unregister(this);
        }
        ActivityHandler.destroy();
        System.exit(0);
    }

    //TODO eventBus事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IEvent event) {
        if (event instanceof ToastAdverEvent) {// 路线的所有分类信息
            LogUtils.d(TAG, "onMessageEvent AllRouteSortEvent");
            if (((ToastAdverEvent) event).getRet().getRet_code().equals(Constant.RETURN_SUCCESS)) {
                if (((ToastAdverEvent) event).getData().getUpdateDate() > getUpdateTime()) {
                    setUpdateTime(((ToastAdverEvent) event).getData().getUpdateDate());
                    RecommendDialog recommendDialog = new RecommendDialog(this);
                    recommendDialog.setData(((ToastAdverEvent) event).getData());
                    recommendDialog.show();
                }
            } else {
                LogUtils.d(TAG, "no data");
            }
        } else if (event instanceof HttpErrorEvent) {
            LogUtils.d(TAG, "HttpErrorEvent");
        }

        if (eventBus.isRegistered(this)) {
            eventBus.unregister(this);
        }
    }

    /**
     * 语音控制类
     * <p>
     * 1.需要时，通过下面的方式注册(判断是否存在)
     * Scene scene = new Scene(this);
     * scene.init(new VoiceListener());
     * <p>
     * 2.不需要时，通过下面的方式注销(判断是否为空)
     * scene.release();
     */
    private class VoiceListener implements ISceneListener {
        private static final String COMMAND_KEY_WORD = "open";

        private static final String KEY_WORD_ACTIVITY = "activity";
        private static final String ACTIVITY_COLLECT = "打开收藏页";
        private static final String ACTIVITY_ORDER = "打开订单页";
        private static final String ACTIVITY_SETTING = "打开设置页";

        @Override
        public String onQuery() {
            String scene = "{\"_scene\": \"com.konka.kktripclient.MainActivity\","
                    + "\"_commands\":{"
                    + "\"" + COMMAND_KEY_WORD + "\":[\"$W(" + KEY_WORD_ACTIVITY + ")\"]"
                    + "},"
                    + "\"_fuzzy_words\":{"
                    + "\"" + KEY_WORD_ACTIVITY
                    + "\":[\"" + ACTIVITY_COLLECT + "\",\"" + ACTIVITY_ORDER + "\",\"" + ACTIVITY_SETTING + "\"]"
                    + "}"
                    + "}";
            LogUtils.d(TAG, "onQuery scene=" + scene);
            return scene;
        }

        @Override
        public void onExecute(Intent intent) {
            if (COMMAND_KEY_WORD.equals(intent.getStringExtra("_command"))) {
                String word = intent.getStringExtra(KEY_WORD_ACTIVITY);
                LogUtils.d(TAG, "onExecute##word=" + word);
                switch (word) {
                    case ACTIVITY_COLLECT:
                        if (UserHelper.getInstance(ActivityHandler.getInstance()).getUserLogin()) {
                            Intent i = new Intent();
                            i.setClass(ActivityHandler.getInstance(), CollectActivity.class);
                            ActivityHandler.getInstance().startActivity(i);
                        } else {
                            MyFactory.getInstance().startLogin("收藏页");
                        }
                        break;
                    case ACTIVITY_ORDER:
                        if (UserHelper.getInstance(ActivityHandler.getInstance()).getUserLogin()) {
                            Intent i = new Intent();
                            i.setClass(ActivityHandler.getInstance(), ManageOrderActivity.class); //获取订单信息
                            ActivityHandler.getInstance().startActivity(i);
                        } else {
                            MyFactory.getInstance().startLogin("订单页");
                        }
                        break;
                    case ACTIVITY_SETTING:
                        Intent i = new Intent();
                        i.setClass(ActivityHandler.getInstance(), SettingActivity.class);
                        ActivityHandler.getInstance().startActivity(i);
                        break;
                }
            }
        }
    }
}
