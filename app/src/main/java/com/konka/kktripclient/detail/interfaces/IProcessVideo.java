package com.konka.kktripclient.detail.interfaces;


import com.konka.kktripclient.detail.bean.VideoParams;

/**
 * Created by Zhou Weilin on 2017-5-25.
 */

public interface IProcessVideo extends IBasePresenter{

    void preparePlayer(VideoParams params);

    void releasePlayer();

    void startPlayer();

    void resetPlayer();

    int getCurrentPosition();

    int getDuration();

    boolean pausePlayer();
    //暂停恢复接口
    boolean resumePlayer();

    void stopPlayer();

    boolean isPlaying();

    boolean canSeek();

    void seekTo(int time);

    void setStoping(boolean isStop);

    boolean isPrepared();
}
