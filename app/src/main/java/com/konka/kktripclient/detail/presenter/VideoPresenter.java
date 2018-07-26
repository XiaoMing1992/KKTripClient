package com.konka.kktripclient.detail.presenter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.konka.kktripclient.R;
import com.konka.kktripclient.detail.DetailConstant;
import com.konka.kktripclient.detail.IJKPlayer.IJKPlayer;
import com.konka.kktripclient.detail.IJKPlayer.IRenderView;
import com.konka.kktripclient.detail.bean.VideoParams;
import com.konka.kktripclient.detail.customview.TripVideoView;
import com.konka.kktripclient.detail.interfaces.IProcessVideo;
import com.konka.kktripclient.detail.interfaces.IVideoView;
import com.konka.kktripclient.utils.LogUtils;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by Zhou Weilin on 2017-8-2.
 */

public class VideoPresenter implements IProcessVideo,IMediaPlayer.OnPreparedListener, IMediaPlayer.OnCompletionListener,
        IMediaPlayer.OnErrorListener, IMediaPlayer.OnInfoListener,IMediaPlayer.OnSeekCompleteListener{
    private final String TAG = VideoPresenter.class.getSimpleName();
    private boolean isPlaying = false;
    private boolean isStoping = false;
    private boolean isPrepared = false;
    private Context mContext;
    private IVideoView mIVideoView;
    private IJKPlayer ijkPlayer;
    private IRenderView videoRender;

    private TripVideoView mTripVideoView;


    public VideoPresenter(TripVideoView videoView){
        mTripVideoView = videoView;
    }

    public VideoPresenter(Context context,IVideoView videoView, IRenderView iRenderView){
        mContext = context;
        mIVideoView = videoView;
        videoRender = iRenderView;
        initPlayer();
    }


    private void initPlayer() {
//        if(MediaPlayerManager.getInstance().getmMediaPlayer() == null){
            Log.d(TAG ,"create new player");
            ijkPlayer = new IJKPlayer(mContext);
//            MediaPlayerManager.getInstance().setmMediaPlayer(ijkPlayer);
//        }else{
//            Log.d(TAG ,"use old player");
//            ijkPlayer = MediaPlayerManager.getInstance().getmMediaPlayer();
//        }

        ijkPlayer.setRenderView(videoRender);
        //设置media监听
        setMediaListener();
    }


    public void setMediaListener() {
        ijkPlayer.setOnPreparedListener(this);
        ijkPlayer.setOnCompletionListener(this);
        ijkPlayer.setOnErrorListener(this);
        ijkPlayer.setOnInfoListener(this);
        ijkPlayer.setOnSeekCompleteListener(this);
    }
    @Override
    public void preparePlayer(VideoParams params) {
        isPrepared = false;
        String url = params.getUrl();
        LogUtils.d(DetailConstant.TAG_IJK,TAG+"-->preparePlayer-->url:"+url);
        if(ijkPlayer!=null){
            ijkPlayer.setVideoURI(Uri.parse(url), 0);
        }

    }

    @Override
    public void releasePlayer() {
        LogUtils.d(DetailConstant.TAG_IJK,TAG+"-->releasePlayer-->ijkPlayer:"+ijkPlayer);
        if(ijkPlayer!=null){
            ijkPlayer.releaseWithoutStop();
            ijkPlayer.stopPlayback();
            ijkPlayer = null;
        }
    }

    @Override
    public void startPlayer() {
        if(ijkPlayer!=null){
            ijkPlayer.start();
        }
    }

    @Override
    public void resetPlayer() {
        LogUtils.d(DetailConstant.TAG_IJK,TAG+"-->resetPlayer-->ijkPlayer:"+ijkPlayer);

    }

    @Override
    public int getCurrentPosition() {
        int time;
        try {
            time = ijkPlayer.getCurrentPosition();
        }catch (NullPointerException e){
            e.printStackTrace();
            time = 0;
        }
        return time;
    }

    @Override
    public int getDuration() {
        int time;
        try {
            time = ijkPlayer.getDuration();
        }catch (NullPointerException e){
            e.printStackTrace();
            time = 0;
        }

        return time;
    }

    @Override
    public boolean pausePlayer() {
        LogUtils.d(DetailConstant.TAG_IJK,TAG+"-->pausePlayer-->ijkPlayer:"+ijkPlayer);
        if(ijkPlayer != null){
            ijkPlayer.pause();
            isPlaying = false;
            mIVideoView.onPausePlay();
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean resumePlayer() {
        LogUtils.d(DetailConstant.TAG_IJK,TAG+"-->resumePlayer-->ijkPlayer:"+ijkPlayer);
        if (ijkPlayer != null) {
            ijkPlayer.start();
            isPlaying = true;
            mIVideoView.onResumePlay();
            return true;
        }
        return false;
    }

    @Override
    public void stopPlayer() {
        LogUtils.d(DetailConstant.TAG_IJK,TAG+"-->stopPlayer-->ijkPlayer:"+ijkPlayer);
        isPlaying = false;
        isPrepared = false;
    }

    @Override
    public boolean isPlaying() {
        /*if (ijkPlayer != null) {
            return ijkPlayer.isPlaying();
        }*/
        return isPlaying;
    }


    @Override
    public void seekTo(int time) {
        if(ijkPlayer!=null){
            if(time <= 0){
                ijkPlayer.seekTo(0);
            }else if(time >= getDuration()){
                ijkPlayer.seekTo(getDuration());
            }else{
                ijkPlayer.seekTo(time);
            }
        }
    }

    @Override
    public void setStoping(boolean isStop) {
        isStoping = isStop;
    }

    @Override
    public boolean isPrepared() {
        return isPrepared;
    }

    @Override
    public boolean canSeek() {
        return ijkPlayer.canSeekBackward() && ijkPlayer.canSeekForward();
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        LogUtils.d(DetailConstant.TAG_IJK,TAG+"-->onPrepared-->isStop:"+isStoping);
        if(isStoping)return;
        startPlayer();
        isPrepared = true;
        isPlaying = true;
        mIVideoView.onPreparePlay();

    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        LogUtils.d(DetailConstant.TAG_IJK,TAG+"-->onCompletion");
        isPlaying = false;
//        resetPlayer();
        mIVideoView.onCompleted();
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        LogUtils.e(DetailConstant.TAG_IJK,TAG+"-->onError-->i:"+i+"  i1:"+i1);
        isPlaying = false;
        stopPlayer();
        releasePlayer();
        mIVideoView.onError(mContext.getString(R.string.error_title_video));
        return false;
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
        switch (i) {
            case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                mIVideoView.onMovieStart();//解决某些情况不会隐藏缓冲条的问题

                break;

            case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                mIVideoView.onBufferingStart();
                break;

            case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                mIVideoView.onBufferingEnd();
                break;
        }
        return false;
    }


    @Override
    public void onSeekComplete(IMediaPlayer iMediaPlayer) {
        LogUtils.d(DetailConstant.TAG_IJK,TAG+"-->onSeekComplete");
        mIVideoView.onSeekCompleted();
    }

}
