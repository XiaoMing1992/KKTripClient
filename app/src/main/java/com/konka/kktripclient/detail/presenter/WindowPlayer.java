package com.konka.kktripclient.detail.presenter;

import com.konka.kktripclient.R;
import com.konka.kktripclient.detail.DetailConstant;
import com.konka.kktripclient.detail.bean.VideoParams;
import com.konka.kktripclient.detail.customview.TripVideoView;
import com.konka.kktripclient.detail.interfaces.ISmallWindow;
import com.konka.kktripclient.layout.util.ActivityHandler;
import com.konka.kktripclient.net.info.AllVideosEvent;
import com.konka.kktripclient.net.info.AllVideosEvent.DataBean.VideosBean;
import com.konka.kktripclient.net.info.HttpErrorEvent;
import com.konka.kktripclient.net.info.IEvent;
import com.konka.kktripclient.utils.Constant;
import com.konka.kktripclient.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by Zhou Weilin on 2017-6-30.
 */

public class WindowPlayer {
    private final String TAG = WindowPlayer.class.getSimpleName();
    private static volatile WindowPlayer sWindowPlayer;
    private TripVideoView mTripVideoView;
    private List<VideosBean> mVideosBeanList = new ArrayList<>();
    private int mCurPlayPos;//当前播放的是第几个视频
    private int mSeekTime;//需要续播的时间
    private boolean isFullScreen = false;
    private ISmallWindow mISmallWindow;
    private int mErrorCount = 0;//切换下一集的次数，超过五次就不切换

    private WindowPlayer(){
        mCurPlayPos = 0;
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    //定义一个共有的静态方法，返回该类型实例
    public static WindowPlayer getInstance() {
        if (sWindowPlayer == null) {
            synchronized (WindowPlayer.class) {
                if (sWindowPlayer == null) {
                    sWindowPlayer = new WindowPlayer();
                }
            }
        }
        return sWindowPlayer;
    }

    public void destroyWindow(){
        if(mTripVideoView!=null){
            mTripVideoView.releasePlayer();
            mTripVideoView = null;
        }
        mVideosBeanList.clear();
        mVideosBeanList = null;
        sWindowPlayer = null;
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->destroyWindow");
    }

    public void setWindowListener(ISmallWindow onWindowListener){
        mISmallWindow = onWindowListener;
    }

    public void setTripVideoView(TripVideoView videoView){
        mTripVideoView = null;
        mTripVideoView = videoView;

    }


    public void setVideosBeanList(List<VideosBean> videosEvent){
        for(VideosBean videosBean:videosEvent){
            mVideosBeanList.add(videosBean);//单复制数值，不复制对象
        }
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->setVideosBeanList-->mVideosBeanList:"+mVideosBeanList);
        if(mVideosBeanList==null||mVideosBeanList.size()==0){
            String errorMsg = "";
            if(ActivityHandler.getInstance()!=null){
                errorMsg = ActivityHandler.getInstance().getString(R.string.video_no_data);
            }
            mTripVideoView.onError(errorMsg);
        }
    }

    public List<VideosBean> getVideosBeanList(){
        return mVideosBeanList;
    }

    //播放后如果是小窗口且有记录则续播
    public void preparePlayer(){
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->preparePlayer");
        if(mTripVideoView!=null && mTripVideoView.getVideoPresenter()!=null && mTripVideoView.isShown()){
            if(mSeekTime>0){
                LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onPreparePlayer-->mSeekTime:"+mSeekTime);
                mTripVideoView.getVideoPresenter().seekTo(mSeekTime);
                mSeekTime = 0;
            }
            mErrorCount = 0;
        }

    }

    public boolean isPlaying(){
        if(mTripVideoView!=null&&mTripVideoView.getVideoPresenter()!=null){
            return mTripVideoView.getVideoPresenter().isPlaying();
        }else {
            return false;
        }

    }

    public boolean isPrepared(){
        if(mTripVideoView!=null && mTripVideoView.getVideoPresenter()!=null){
            return mTripVideoView.getVideoPresenter().isPrepared();
        }else {
            return false;
        }
    }

    public void setStop(boolean isStop){
        if(mTripVideoView!=null && mTripVideoView.getVideoPresenter()!=null){
            mTripVideoView.getVideoPresenter().setStoping(isStop);
        }

    }


    public void releasePlayer(){
        if(mTripVideoView ==null || mTripVideoView.getVideoPresenter()==null)return;
        if(isPlaying()){//避免还在加载中取消时导致获取时间为0
            mSeekTime = mTripVideoView.getVideoPresenter().getCurrentPosition();
        }
        mTripVideoView.releasePlayer();
        mTripVideoView = null;
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->releasePlayer-->mSeekTime:"+mSeekTime);
    }

    public void pausePlayer(){
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->pausePlayer->mTripVideoView:"+mTripVideoView);
        if(mTripVideoView ==null || mTripVideoView.getVideoPresenter()==null)return;
        if(isPlaying()){
            mTripVideoView.getVideoPresenter().pausePlayer();
        }

    }


    public void resumePlayer(){
        if(mTripVideoView ==null || mTripVideoView.getVideoPresenter()==null)return;
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->resumePlayer");
        if(!isPlaying()){
            mTripVideoView.getVideoPresenter().resumePlayer();
        }

    }


    public void setCurPlayPos(int curPlayPos) {
        mCurPlayPos = curPlayPos;
    }

    public int getCurPlayPos() {
        return mCurPlayPos;
    }

    public void setIsFullScreen(boolean isFull){
        isFullScreen = isFull;
    }

    public void saveSeekTime(){
        if(mTripVideoView!=null && mTripVideoView.getVideoPresenter()!=null){
            mSeekTime = mTripVideoView.getVideoPresenter().getCurrentPosition();
        }

    }

    public void onPlayerComplete(){
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onPlayerComplete");
        if(mTripVideoView!=null && mTripVideoView.getVideoPresenter()!=null&&mTripVideoView.isShown()){
            setNextPosition();
            startVideoView();
        }

    }


    public void onPlayerError(){
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onPlayerError");
        if(mTripVideoView!=null && mTripVideoView.isShown()){
            mErrorCount++;
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onPlayerError--mErrorCount:"+mErrorCount);
            if(mErrorCount>=5){
                mErrorCount = 0;
            }else {
                setNextPosition();
                startVideoView();
            }

        }
    }


    public void startVideoView(){
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->startVideoView");
        if(mVideosBeanList==null||mVideosBeanList.size()==0) {
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->startVideoView-->mVideosBeanList==null");
            return;
        }else if(mTripVideoView==null){
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->startVideoView-->mTripVideoView==null");
            return;
        }
        VideoParams videoParams = new VideoParams();
        videoParams.setVideoActivity(isFullScreen);
        videoParams.setTitle(mVideosBeanList.get(mCurPlayPos).getName());
        videoParams.setUrl(mVideosBeanList.get(mCurPlayPos).getPlayParam());
        String routeName = "-1";
        if(mVideosBeanList.get(mCurPlayPos).getGoods()!=null){
            routeName = mVideosBeanList.get(mCurPlayPos).getGoods().getName();
        }
        videoParams.setRouteName(routeName);
//        videoParams.setUrl("http://pcvideogs.titan.mgtv.com/c1/2017/04/28_0/C41AD2CA880ACD6837793D7BAC070BB7_20170428_1_1_670_mp4/E5B0D120A892182F41E12E8C79CF7C46.m3u8?arange=0&pm=_gfEAo_SH3C~7_WsJVcX8Fp73RZi2npLPNP5uMq3zbCfW1WtHz05OASjeKCdLwLgh0FmBqtFjC8c7xc5NNKt8myLXh57lbhYInqSv~ndky2X6Kjqe0rEud0gK5wGD0DFG888zcBF0AFCQBnAQYLS8EceIYP4Fxw9gHPzkUl6Y_wYcLb5Dbzaxo1tjH0bvWVExnXaGvi3EMDR~_2UTbcqySGA5dd1tych1CZSPVT_99SsmanJaLYJv_hWVyi4_Wzsp~bX128xzABVfaXus3ikkh8f3ikCWbou0pP4YF3aICkDRxjKvHJELUa4bjgHLZAOQLaNOi2yh5FnhZ2owZVt8NQT3Tn_6sTpbYySQxXfmQhVBdh8bd1QUKMLxYBzB16mP2VhdBXPlW8FC~a6w3FsvA--");
        mTripVideoView.initData(videoParams,isFullScreen);

    }

    public void voicePlayNext(){
        if(mTripVideoView!=null && mTripVideoView.getVideoPresenter()!=null&&mTripVideoView.isShown()){
            setNextPosition();
            startVideoView();
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->voicePlayNext");
        }

    }

    public void voicePlayPrevious(){
        if(mTripVideoView!=null && mTripVideoView.getVideoPresenter()!=null&&mTripVideoView.isShown()){
            setPreviousPosition();
            startVideoView();
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->voicePlayPrevious");
        }

    }

    private void setNextPosition() {
        if(mTripVideoView==null)return;
        mCurPlayPos = mCurPlayPos + 1;
        if (mCurPlayPos >= mVideosBeanList.size()) {
            mCurPlayPos = 0;
        }
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->setNextPosition-->mCurPlayPos: " + mCurPlayPos + "===size: " + mVideosBeanList.size());
    }

    private void setPreviousPosition() {
        if(mTripVideoView==null)return;
        mCurPlayPos = mCurPlayPos - 1;
        if (mCurPlayPos < 0) {
            mCurPlayPos = mVideosBeanList.size()-1;
        }
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->setPreviousPosition-->mCurPlayPos: " + mCurPlayPos + "===size: " + mVideosBeanList.size());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IEvent event) {
//        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onMessageEvent-->event:"+event.getClass().getSimpleName());
        if (event instanceof AllVideosEvent) {
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onMessageEvent-->AllVideosEvent");
            AllVideosEvent allVideosEvent = (AllVideosEvent) event;
            if (allVideosEvent.getRet().getRet_code().equals(Constant.RETURN_SUCCESS)) {
                List<VideosBean> videosList = allVideosEvent.getData().getVideos();
                setVideosBeanList(videosList);
                mISmallWindow.onDataSucceed();
            }
        } else if (event instanceof HttpErrorEvent) {
            String type = ((HttpErrorEvent) event).getReq_type();
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onMessageEvent-->HttpErrorEvent-->type:"+type);
            if("AllVideosEvent".equals(type)){
                if(mTripVideoView!=null){
                    mTripVideoView.onError("");
                }
            }

        }

    }

}
