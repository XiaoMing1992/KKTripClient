package com.konka.kktripclient.detail.interfaces;

/**
 * Created by Zhou Weilin on 2017-6-7.
 */

public interface IVideoView extends IBaseView{
    void onBufferingStart();
    void onBufferingEnd();
    void onPreparePlay();
    void onPausePlay();
    void onResumePlay();
    void onSeekCompleted();
    void onError(String msg);
    void onCompleted();
    void onMovieStart();
}
