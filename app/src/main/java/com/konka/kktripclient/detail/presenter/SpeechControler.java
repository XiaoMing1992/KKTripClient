package com.konka.kktripclient.detail.presenter;

import android.content.Intent;

import com.iflytek.xiri.scene.ISceneListener;
import com.konka.kktripclient.activity.CollectActivity;
import com.konka.kktripclient.activity.ManageOrderActivity;
import com.konka.kktripclient.activity.SettingActivity;
import com.konka.kktripclient.detail.DetailConstant;
import com.konka.kktripclient.detail.customview.TripVideoView;
import com.konka.kktripclient.layout.tab.MyFactory;
import com.konka.kktripclient.layout.util.ActivityHandler;
import com.konka.kktripclient.pay.UserHelper;
import com.konka.kktripclient.utils.LogUtils;

/**
 * Created by Zhou Weilin on 2017-12-13.
 */

public class SpeechControler implements ISceneListener {
    private final String TAG = SpeechControler.class.getSimpleName();
    private static final String COMMAND_KEY_PLAY = "player_play";
    private static final String COMMAND_KEY_EPISODE = "player_episode";
    private static final String COMMAND_KEY_WORD = "player_word";
    private static final String PLAYER_PLAY = "PLAY";
    private static final String PLAYER_PAUSE = "PAUSE";
    private static final String PLAYER_SEEK = "SEEK";
    private static final String PLAYER_FORWARD_TIME = "FORWARD";
    private static final String PLAYER_BACKWARD_TIME = "BACKWARD";
    private static final String PLAYER_EXIT = "EXIT";
    private static final String PLAYER_NEXT = "NEXT";
    private static final String PLAYER_PREV = "PREV";
    private static final String KEY_PLAYER_WORD = "player_word";
    private static final String PLAYER_FORWARD = "快进";
    private static final String PLAYER_BACKWARD= "快退";
    private static final String ACTIVITY_COLLECT = "打开收藏页";
    private static final String ACTIVITY_ORDER = "打开订单页";
    private static final String ACTIVITY_SETTING = "打开设置页";

    private TripVideoView mTripVideoView;

    public SpeechControler(TripVideoView tripVideoView){
        mTripVideoView = tripVideoView;
    }

    @Override
    public String onQuery() {
        String scene = "{\"_scene\": \"com.konka.kktripclient.Video\","
                + "\"_commands\":{"
                + "\"" + COMMAND_KEY_PLAY + "\":[\"$P(_PLAY)\"],"
                + "\"" + COMMAND_KEY_EPISODE + "\":[\"$P(_EPISODE)\"],"
                + "\"" + COMMAND_KEY_WORD + "\":[\"$W(" + KEY_PLAYER_WORD + ")\"]"
                + "},"
                + "\"_fuzzy_words\":{"
                + "\"" + KEY_PLAYER_WORD
                + "\":[\"" + PLAYER_FORWARD + "\",\"" + PLAYER_BACKWARD
                + "\",\"" + ACTIVITY_COLLECT + "\",\"" + ACTIVITY_ORDER + "\",\"" + ACTIVITY_SETTING + "\"]"
                + "}"
                + "}";
        LogUtils.d(DetailConstant.TAG_DETAIL, TAG +"-->onQuery scene=" + scene);
        return scene;
    }

    @Override
    public void onExecute(Intent intent) {
        if(mTripVideoView == null || mTripVideoView.getVideoPresenter()==null) {
            LogUtils.e(DetailConstant.TAG_DETAIL, TAG +"-->onExecute-->return");
            return;
        }
        switch (intent.getStringExtra("_command")) {
            case COMMAND_KEY_PLAY:
                String playAction = intent.getStringExtra("_action");
                LogUtils.d(DetailConstant.TAG_DETAIL, TAG +"-->onExecute-->playAction:" + playAction);
                switch (playAction) {
                    case PLAYER_PLAY: // 播放
                        LogUtils.d(DetailConstant.TAG_DETAIL, TAG +"-->onExecute-->play");
                        if(!mTripVideoView.getVideoPresenter().isPlaying()){
                            mTripVideoView.getVideoPresenter().resumePlayer();
                        }
                        break;

                    case PLAYER_PAUSE: // 暂停
                        LogUtils.d(DetailConstant.TAG_DETAIL, TAG +"-->onExecute-->pause");
                        if(mTripVideoView.getVideoPresenter().isPlaying()){
                            mTripVideoView.getVideoPresenter().pausePlayer();
                        }
                        break;

                    case PLAYER_FORWARD_TIME: // 快进xx秒
                        int fastOffset = intent.getIntExtra("offset", 0);
                        int fastPos = mTripVideoView.getVideoPresenter().getCurrentPosition()+fastOffset*1000;
                        LogUtils.d(DetailConstant.TAG_DETAIL, TAG +"-->onExecute-->fastOffset:"+fastOffset+"s,fastPos:"+fastPos);
                        mTripVideoView.seekToPos(true,fastPos);
                        break;

                    case PLAYER_BACKWARD_TIME: // 快退xx秒
                        int backOffset = intent.getIntExtra("offset", 0);
                        int backPos = mTripVideoView.getVideoPresenter().getCurrentPosition()-backOffset*1000;
                        LogUtils.d(DetailConstant.TAG_DETAIL, TAG +"-->onExecute-->backOffset:"+backOffset+"s,backPos:"+backPos);
                        mTripVideoView.seekToPos(false,backPos);
                        break;

                    case PLAYER_SEEK: // 跳到第几秒开始播
                        int jumpOffset = intent.getIntExtra("position", 0);
                        int jumpPos = jumpOffset*1000;
                        if(jumpPos>mTripVideoView.getVideoPresenter().getCurrentPosition()){
                            mTripVideoView.seekToPos(true,jumpPos);
                        }else {
                            mTripVideoView.seekToPos(false,jumpPos);
                        }

                        LogUtils.d(DetailConstant.TAG_DETAIL, TAG +"-->onExecute-->jumpOffset:"+jumpOffset+"s,jumpPos:"+jumpPos);
                        break;

                }
                break;

            case COMMAND_KEY_EPISODE:
                String episodeAction = intent.getStringExtra("_action");
                LogUtils.d(DetailConstant.TAG_DETAIL, TAG +"-->onExecute-->episodeAction:" + episodeAction);
                if (episodeAction.equals(PLAYER_NEXT)) {// 播放器播放下一集
                    WindowPlayer.getInstance().voicePlayNext();
                } else if (episodeAction.equals(PLAYER_PREV)) {// 播放器播放上一集
                    WindowPlayer.getInstance().voicePlayPrevious();
                }
                break;

            case COMMAND_KEY_WORD:
                String word = intent.getStringExtra(KEY_PLAYER_WORD);
                LogUtils.d(DetailConstant.TAG_DETAIL, TAG +"-->onExecute-->word:" + word);
                if (word.equals(PLAYER_FORWARD)) {// 快进
                    mTripVideoView.seekTo(true);
                } else if (word.equals(PLAYER_BACKWARD)) {// 快退
                    mTripVideoView.seekTo(false);
                } else if (word.equals(ACTIVITY_COLLECT)) {
                    if (UserHelper.getInstance(ActivityHandler.getInstance()).getUserLogin()) {
                        Intent i = new Intent();
                        i.setClass(ActivityHandler.getInstance(), CollectActivity.class);
                        ActivityHandler.getInstance().startActivity(i);
                    } else {
                        MyFactory.getInstance().startLogin("收藏页");
                    }
                } else if (word.equals(ACTIVITY_ORDER)) {
                    if (UserHelper.getInstance(ActivityHandler.getInstance()).getUserLogin()) {
                        Intent i = new Intent();
                        i.setClass(ActivityHandler.getInstance(), ManageOrderActivity.class); //获取订单信息
                        ActivityHandler.getInstance().startActivity(i);
                    } else {
                        MyFactory.getInstance().startLogin("订单页");
                    }
                } else if (word.equals(ACTIVITY_SETTING)) {
                    Intent i = new Intent();
                    i.setClass(ActivityHandler.getInstance(), SettingActivity.class);
                    ActivityHandler.getInstance().startActivity(i);
                }
                break;
        }
    }

}
