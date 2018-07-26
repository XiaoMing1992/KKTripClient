package com.konka.kktripclient.detail.customview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.konka.kktripclient.R;
import com.konka.kktripclient.detail.DetailActivity;
import com.konka.kktripclient.detail.DetailConstant;
import com.konka.kktripclient.detail.IJKPlayer.IJKPlayer;
import com.konka.kktripclient.detail.IJKPlayer.IRenderView;
import com.konka.kktripclient.detail.bean.VideoParams;
import com.konka.kktripclient.detail.interfaces.IProcessVideo;
import com.konka.kktripclient.detail.interfaces.IVideoView;
import com.konka.kktripclient.detail.presenter.VideoPresenter;
import com.konka.kktripclient.detail.presenter.WindowPlayer;
import com.konka.kktripclient.utils.BigDataHelper;
import com.konka.kktripclient.utils.CommonUtils;
import com.konka.kktripclient.utils.Constant;
import com.konka.kktripclient.utils.LogUtils;
import com.konka.kktripclient.utils.NetworkUtils;

import java.util.Formatter;
import java.util.Locale;


/**
 * Created by Zhou Weilin on 2017-6-6.
 */

public class TripVideoView extends RelativeLayout implements IVideoView {
    private final String TAG = TripVideoView.class.getSimpleName();
    private Context mContext;
    private VideoPresenter mIVideoPresenter;
    private OnVideoViewListener mViewListener;
    private boolean isFullScreen;

    private RelativeLayout mRelativeBuffer;
    private ProgressBar mProgressBig;
    private ProgressBar mProgressSmall;

//    private NetWorkSpeedText mNetSpeedText;

    private ViewStub mStubController;
    private ImageView mImgPause;
    private RelativeLayout mRelativeFast;
    private TextView mTxtFastTime;
    private RelativeLayout mRelativePlaybar;
    private ImageView mImgForward;
    private ImageView mImgBackward;
    private ProgressBar mProgressBar;
    private TextView mTxtTitle;
    private TextView mTxtCurTime;
    private TextView mTxtDuration;
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    private RelativeLayout mRelativeError;
    private TextView mTxtErrorTip;

    private RelativeLayout mRelativeLoad;//小窗口的加载背景

    private boolean isLoading = false;
    private boolean isSeeking = false;
    private int mSeekTime;

    private final int SEEK_GAP = 15000;//快进快退的间隔

    private int VIEW_WIDTH = 666;
    private int VIEW_HEIGHT = 370;
    private int VIEW_MARGINLEFT = 72;
    private int VIEW_MARGINTOP = 96;

    private String mVideoTitle;
	private int mCurPositon=0;//用于保存当前时间，解决缓冲条不会消失的问题
    private LinearLayout mLinearLoading;//全屏的加载背景
    private TextView mTxtLoadTitle;

    // 圆角矩形属性，默认无圆角
    private float mRoundLayoutRadius = 0f;
    private Path mRoundPath;
    private RectF mRectF;

    private IRenderView videoRender;
    private ViewStub surfaceRender;
    private ViewStub textureRender;

    private long mStartTime;//大数据统计的开始时间
    private String mRouteName="-1";


    public TripVideoView(Context context) {
        super(context);
        init(context);
    }

    public TripVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TripVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TripVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LogUtils.i(DetailConstant.TAG_DETAIL,TAG+"-->onAttachedToWindow");

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LogUtils.i(DetailConstant.TAG_DETAIL,TAG+"-->onDetachedFromWindow");
        clearAnimation();
        removeMessage();

//        if(mNetSpeedText != null){
//            mNetSpeedText.quit();
//        }
    }

    private void init(Context context) {
        mContext = context;
        mRoundPath = new Path();
        mRectF = new RectF();
        setWillNotDraw(false);// 如果你继承的是ViewGroup,注意此行,否则draw方法是不会回调的;
        LayoutInflater.from(context).inflate(R.layout.view_trip_video, this);
        initView();
    }

    private void initView() {

        surfaceRender = (ViewStub) findViewById(R.id.surface_view);
        textureRender = (ViewStub) findViewById(R.id.texture_view);


        mRelativeBuffer = (RelativeLayout)findViewById(R.id.video_relative_buffer);
        mProgressBig = (ProgressBar) findViewById(R.id.video_progress_loading_big);
        mProgressSmall = (ProgressBar)findViewById(R.id.video_progress_loading_small);
//        mNetSpeedText = (NetWorkSpeedText)findViewById(R.id.video_txt_loadingspeed);

        mStubController = (ViewStub)findViewById(R.id.viewstub_video_controller);

        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

        mRelativeError = (RelativeLayout)findViewById(R.id.video_view_relative_error_page);
        mTxtErrorTip = (TextView)findViewById(R.id.error_video_txt_tip);
        mRelativeError.setVisibility(INVISIBLE);

        mRelativeLoad = (RelativeLayout)findViewById(R.id.video_relative_load);
        mLinearLoading = (LinearLayout)findViewById(R.id.video_activity_linear_load_page);
        mTxtLoadTitle = (TextView)findViewById(R.id.video_activity_load_title);

    }

    /**
     * 初始化缓冲加载页，解决网络差时暂时加载不出事黑屏问题
     * @param isFullScreen
     */
    public void initShow(boolean isFullScreen){
//        mRelativeBuffer.setVisibility(INVISIBLE);
        if(isFullScreen){
            if(!mLinearLoading.isShown()){
                mRelativeLoad.setVisibility(INVISIBLE);
                mLinearLoading.setVisibility(VISIBLE);
            }
        }else {
            if(!mRelativeLoad.isShown()){
                mLinearLoading.setVisibility(INVISIBLE);
                mRelativeLoad.setVisibility(VISIBLE);
                showLoading();
            }

        }
    }

    /**
     * 给播放器传参数
     * @param videoParams
     */
    public void initData(VideoParams videoParams){
        initData(videoParams,false);
    }

    /**
     * 给播放器传参数
     * @param videoParams
     * @param isFullScreen 初始化时直接设置为true，则适用于全屏播放
     */
    public void initData(VideoParams videoParams,boolean isFullScreen){
        if(mIVideoPresenter == null){//解决下面方法执行后首页小窗口先黑屏再出加载界面问题
            if(Constant.IJK_RENDER_VIEW == IJKPlayer.RENDER_SURFACE_VIEW){
                if(surfaceRender.getParent() !=null){
                    surfaceRender.inflate();
                }
                videoRender = (IRenderView) findViewById(R.id.surface_render);
            }else{
                if(textureRender.getParent() !=null){
                    textureRender.inflate();
                }
                videoRender = (IRenderView) findViewById(R.id.texture_render);
            }
            mIVideoPresenter = new VideoPresenter(mContext,this,videoRender);
        }


        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->initData-->isVideoActivity:"+videoParams.isVideoActivity());
        mVideoTitle = videoParams.getTitle();
        mRouteName = videoParams.getRouteName();

        if(videoParams.isVideoActivity()){
            mTxtLoadTitle.setText(String.format(mContext.getString(R.string.video_up_coming),mVideoTitle));
        }
        initShow(videoParams.isVideoActivity());//解决切集或循环播放时切下一个无加载页问题


        if (Constant.MONKEY) {//解决monkey版频繁切换导致的IllegalStateException异常
            mHandler.removeMessages(HandlerMsg.HM_PREPARE_PLAY.ordinal());
            Message videoMsg = new Message();
            videoMsg.what = HandlerMsg.HM_PREPARE_PLAY.ordinal();
            videoMsg.obj = videoParams;
            mHandler.sendMessageDelayed(videoMsg, 3000);
        } else {
            mIVideoPresenter.preparePlayer(videoParams);
        }


        setScreenState(isFullScreen);
        //放在initData方法会在启动页面时有个缓冲条，所以放在这里
        showPlaybar();
        if(mImgPause!=null){//清除上次播放遗留的状态界面
            mImgPause.setVisibility(INVISIBLE);
            CommonUtils.refreshView(mImgPause);
        }
        mHandler.removeMessages(HandlerMsg.HM_HIDE_PLAYBAR.ordinal());//解决一个视频起播后切另一视频还在缓冲就退出播放条界面问题

        mStartTime = 0;

        if(mViewListener!=null){
            mViewListener.updateDate();
        }



    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mRectF.set(0f, 0f, getMeasuredWidth(), getMeasuredHeight());
        setRoundPath();
    }

    @Override
    public void draw(Canvas canvas) {
        if (mRoundLayoutRadius > 0f) {
            canvas.clipPath(mRoundPath);
        }
        super.draw(canvas);
    }

    /**
     * 添加一个圆角矩形到path中, 如果要实现任意形状的View, 只需要手动添加path就行
     */
    private void setRoundPath() {
        mRoundPath.addRoundRect(mRectF, mRoundLayoutRadius, mRoundLayoutRadius, Path.Direction.CW);
    }

    /**
     * 设置圆角大小
     * @param roundLayoutRadius 圆角大小
     */
    public void setRoundLayoutRadius(float roundLayoutRadius) {
        mRoundLayoutRadius = roundLayoutRadius;
        setRoundPath();
        postInvalidate();
    }


    public IProcessVideo getVideoPresenter(){
        return mIVideoPresenter;
    }


    //全屏
    public void setFullScreen(){
//        scaleAnimation(CommonUtils.dip2px(mContext,VIEW_MARGINLEFT),0,1080,1920);//设置为全屏播放
        changeWindowSize(true);
        setScreenState(true);
        showPlaybar();
        timerHidePlaybar();

    }

    //原始大小
    public void setOriginalScreen(){
//        scaleAnimation(0, CommonUtils.dip2px(mContext,VIEW_MARGINLEFT), CommonUtils.dip2px(mContext,VIEW_HEIGHT), CommonUtils.dip2px(mContext,VIEW_WIDTH));//设置窗口的大小
        changeWindowSize(false);
        setScreenState(false);
        hidePlaybar();
        mHandler.removeMessages(HandlerMsg.HM_TIMER_UPDATE_SEEKBAR.ordinal());
        mStubController.setVisibility(INVISIBLE);

    }

    public void stopPlayer(){
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->stopPlayer");
        removeMessage();
        if(mIVideoPresenter!=null){
            mIVideoPresenter.stopPlayer();
        }
        mIVideoPresenter = null;
    }

    public void releasePlayer(){
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->releasePlayer");
        removeMessage();
        if(mIVideoPresenter!=null){
            mIVideoPresenter.stopPlayer();
            mIVideoPresenter.releasePlayer();
        }
        mIVideoPresenter = null;
        reportBigData();
    }

    private void setScreenState(boolean isFull){
        if(isFull){
            isFullScreen = true;
            inflateController();
            mStubController.setVisibility(VISIBLE);
            updatePlaybar();
        }else {
            isFullScreen = false;
        }
        updateImgLoading();
    }

    private void inflateController(){
        if(controllerNotFlated()) {
            mStubController.inflate();

            mImgPause = (ImageView)findViewById(R.id.video_img_pause);
            mRelativeFast = (RelativeLayout) findViewById(R.id.video_relative_fast);
            mImgForward = (ImageView)findViewById(R.id.video_img_forward);
            mImgBackward = (ImageView)findViewById(R.id.video_img_backward);
            mTxtFastTime = (TextView)findViewById(R.id.video_txt_fasttime);
            mRelativePlaybar = (RelativeLayout)findViewById(R.id.video_relative_playbar);
            mProgressBar = (ProgressBar)findViewById(R.id.video_progress);
            mTxtTitle = (TextView)findViewById(R.id.video_txt_title);
            mTxtCurTime = (TextView)findViewById(R.id.video_txt_curtiem);
            mTxtDuration = (TextView)findViewById(R.id.video_txt_duration);
            LogUtils.i(DetailConstant.TAG_DETAIL,TAG+"-->inflateController");
        }

    }


    private boolean controllerNotFlated(){
        if(mStubController.getParent()!=null){
            return true;
        }else {
            return false;
        }
    }


    private void scaleAnimation(int marginForm, int marginTo, int heightY, int widthX){
        DetailActivity.ViewWrapper wrapper = new DetailActivity.ViewWrapper(this);
        ValueAnimator animator = ValueAnimator.ofInt(marginForm,marginTo);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                MarginLayoutParams layoutParams = (MarginLayoutParams) getLayoutParams();
                layoutParams.topMargin = (int) animation.getAnimatedValue()*4/3;
                layoutParams.leftMargin = (int) animation.getAnimatedValue();
                setLayoutParams(layoutParams);

            }
        });
        animator.setTarget(this);
        ObjectAnimator width = ObjectAnimator.ofInt(wrapper, "width", widthX);
        ObjectAnimator height = ObjectAnimator.ofInt(wrapper, "height", heightY);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(animator).with(width).with(height);
        animSet.setDuration(100);
        animSet.start();
    }

    private void changeWindowSize(boolean isFull){
        if(isFull){
            LayoutParams layoutParams=
                    new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            setLayoutParams(layoutParams);
        }else {
            LayoutParams lp=new  LayoutParams(CommonUtils.dip2px(mContext,VIEW_WIDTH),CommonUtils.dip2px(mContext,VIEW_HEIGHT));
            lp.setMargins(CommonUtils.dip2px(mContext,VIEW_MARGINLEFT),CommonUtils.dip2px(mContext,VIEW_MARGINTOP),0,0);
            setLayoutParams(lp);
        }
        if(CommonUtils.getModel().contains("konka") && CommonUtils.getModel().contains("2991")){
            requestLayout();
            invalidate();
        }
    }

    private void updatePlaybar(){
        if(controllerNotFlated() || mIVideoPresenter == null)return;
        mTxtTitle.setText(mVideoTitle);

        int curtime = mIVideoPresenter.getCurrentPosition();
        int duration = mIVideoPresenter.getDuration();
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->updatePlaybar-->curtime:"+curtime+"  duration:"+duration);

        mProgressBar.setMax(duration);
        mProgressBar.setProgress(curtime);

        String current = formatTime(curtime);
        mTxtCurTime.setText(current);

        String total = formatTime(duration);
        mTxtDuration.setText(total);

        mHandler.removeMessages(HandlerMsg.HM_TIMER_UPDATE_SEEKBAR.ordinal());
        mHandler.sendEmptyMessageDelayed(HandlerMsg.HM_TIMER_UPDATE_SEEKBAR.ordinal(), TIMER_INTERVAL_UPDATE_SEEKBAR);
    }



    private String formatTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours   = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }



    public void seekTo(boolean isFast){
        if(mIVideoPresenter==null || !mIVideoPresenter.canSeek())return;
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->seekTo-->isCanSeek:"+mIVideoPresenter.canSeek());
        if(!isSeeking){
            mSeekTime = mIVideoPresenter.getCurrentPosition();
            isSeeking = true;
        }

        if(isFast){
            mSeekTime+=SEEK_GAP;
        }else {
            mSeekTime-=SEEK_GAP;
        }

        seekToPos(isFast,mSeekTime);

    }

    public void seekToPos(boolean isFast,int positon){
        mSeekTime = positon;
        if(isFast){
            if(mSeekTime>mIVideoPresenter.getDuration()){
                mSeekTime = mIVideoPresenter.getDuration();
            }
            mImgForward.setVisibility(VISIBLE);
            mImgBackward.setVisibility(INVISIBLE);
        }else {
            if(mSeekTime<0){
                mSeekTime = 0;
            }
            mImgForward.setVisibility(INVISIBLE);//用INVISIBLE，因快退时间布局与mImgForward有关
            mImgBackward.setVisibility(VISIBLE);
        }

        mHandler.removeMessages(HandlerMsg.HM_HIDE_PLAYBAR.ordinal());
        showPlaybar();
        if(!mIVideoPresenter.isPlaying()){
            mImgPause.setVisibility(INVISIBLE);
        }
        mTxtFastTime.setText(formatTime(mSeekTime));
        mProgressBar.setProgress(mSeekTime);
        mRelativeFast.setVisibility(VISIBLE);
        timerSeekTo();
    }


    //一定时间后统一快进或快退
    private void timerSeekTo(){
        mHandler.removeMessages(HandlerMsg.HM_TIMER_UPDATE_SEEKBAR.ordinal());
        mHandler.removeMessages(HandlerMsg.HM_TIMER_SEEKTO.ordinal());
        mHandler.sendEmptyMessageDelayed(HandlerMsg.HM_TIMER_SEEKTO.ordinal(), TIME_START_SEEKBAR);
    }

    // 定时隐藏进度条
    private void timerHidePlaybar() {
        mHandler.removeMessages(HandlerMsg.HM_HIDE_PLAYBAR.ordinal());
        mHandler.sendEmptyMessageDelayed(HandlerMsg.HM_HIDE_PLAYBAR.ordinal(), TIME_HIDE_PLAYBAR);
    }

    //移除消息,为了避免因退出Activity还有消息在发送，造成动画空指针
    private void removeMessage(){
        if(mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    private enum HandlerMsg {
        HM_TIMER_SEEKTO,                // 定时快进或快退到某处
        HM_TIMER_UPDATE_SEEKBAR,		// 定时更新seekbar进度
        HM_HIDE_PLAYBAR,		        // 隐藏播控条
        ON_MOVIE_START,                 // 播放器起播
        CHECK_BUFFER,                   // 检查是否已经播放了缓冲条还没消失
        HM_ERROR_RESTART,               // 小窗口出错后重新播放
        HM_PREPARE_PLAY,
    }

    private static final int TIMER_INTERVAL_UPDATE_SEEKBAR = 1000;	// 进度条定时更新的时间间隔
    private static final int TIME_START_SEEKBAR = 1500;	// 进度条特定时间间隔后快进快退
    private static final int TIME_HIDE_PLAYBAR = 6000;	// 定时隐藏播放条
    private static final int TIME_CHECK_BUFFER = 2000;	// 定时检查缓冲条
    private static final int TIME_ERROR_RESTART = 5000;	// 一定延时后重新初始化播放器

    public Handler mHandler = new Handler() {
        //Bundle bundle = null;
        public void handleMessage(Message msg) {
            switch (HandlerMsg.values()[msg.what]) {
                case HM_TIMER_SEEKTO:
                    mRelativeFast.setVisibility(INVISIBLE);
                    if(!mIVideoPresenter.isPlaying()){
                        mImgPause.setVisibility(VISIBLE);
                    }
                    if(mSeekTime>=mIVideoPresenter.getDuration()){//解决直接快进到最后回滚现象
                        mSeekTime = mIVideoPresenter.getDuration()-1000;
                    }
                    mIVideoPresenter.seekTo(mSeekTime);
                    LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->HM_TIMER_SEEKTO-->seekTo:"+mSeekTime);
                    isSeeking = false;
                    updatePlaybar();
                    break;

                case HM_TIMER_UPDATE_SEEKBAR:
                    updatePlaybar();
                    if (mIVideoPresenter.isPlaying()){
                        mHandler.removeMessages(HandlerMsg.HM_TIMER_UPDATE_SEEKBAR.ordinal());
                        sendEmptyMessageDelayed(HandlerMsg.HM_TIMER_UPDATE_SEEKBAR.ordinal(), TIMER_INTERVAL_UPDATE_SEEKBAR);
                    }
                    break;

                case HM_HIDE_PLAYBAR:
                    hidePlaybar();
                    break;

                case ON_MOVIE_START:
                    if(mImgPause!=null&&mIVideoPresenter!=null){
                        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->ON_MOVIE_START-->mImgPause:"+mImgPause.isShown());
                        if(mImgPause.isShown() == true&&mIVideoPresenter.isPlaying()){
                            mImgPause.setVisibility(INVISIBLE);
                            CommonUtils.refreshView(mImgPause);
                        }
                    }
                    if(mRelativeLoad!=null&&mRelativeLoad.isShown()){
                        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->ON_MOVIE_START-->isShown():"+mRelativeLoad.isShown());
                        mRelativeLoad.setVisibility(INVISIBLE);
                    }else if(mLinearLoading!=null&&mLinearLoading.isShown()){
                        mLinearLoading.setVisibility(INVISIBLE);
                    }
                    break;

                case CHECK_BUFFER:
                    LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->CHECK_BUFFER-->mCurPositon:"+mCurPositon+"  "+mIVideoPresenter.getCurrentPosition());
                    int positionGap = Math.abs(mIVideoPresenter.getCurrentPosition()-mCurPositon);
                    LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->CHECK_BUFFER-->positionGap:"+positionGap);
                    if(positionGap>1000){
                        mHandler.removeMessages(HandlerMsg.CHECK_BUFFER.ordinal());
                        if(mRelativeBuffer.isShown()){
                            hideLoading();
                        }
                        mCurPositon = 0;
                    }else {
                        sendEmptyMessageDelayed(HandlerMsg.CHECK_BUFFER.ordinal(), TIME_CHECK_BUFFER);
                    }
                    break;

                case HM_ERROR_RESTART:
                    LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->HM_ERROR_RESTART");
                    mIVideoPresenter = null;
                    WindowPlayer.getInstance().onPlayerError();

                    break;

                case HM_PREPARE_PLAY:
                    LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->HM_PREPARE_PLAY");
                    try {
                        mIVideoPresenter.preparePlayer((VideoParams) msg.obj);
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                        onError("");
                    }
                    break;


                default:
                    break;
            }
        }
    };

    //显示播放条
    private void showPlaybar(){
        if(controllerNotFlated())return;
        if(!mRelativePlaybar.isShown()){
            mRelativePlaybar.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.video_bottom_show));
            mRelativePlaybar.setVisibility(View.VISIBLE);
        }

    }

    //隐藏播放条
    private void hidePlaybar(){
        if(controllerNotFlated())return;
        if(mRelativePlaybar.isShown()){
            mRelativePlaybar.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.video_bottom_hide));
            mRelativePlaybar.setVisibility(View.INVISIBLE);
        }

    }

    private void updateImgLoading(){
        if(mRelativeBuffer.isShown()){
            LogUtils.i(DetailConstant.TAG_DETAIL,TAG+"-->updateImgLoading");
            showLoading();
        }else {
            hideLoading();
        }

    }

    public void showLoading(){
        mRelativeBuffer.setVisibility(VISIBLE);
//        mNetSpeedText.setVisibility(View.VISIBLE);
        if(isFullScreen){
            mProgressSmall.setVisibility(INVISIBLE);
            mProgressBig.setVisibility(VISIBLE);
        }else {
            mProgressBig.setVisibility(INVISIBLE);
            mProgressSmall.setVisibility(VISIBLE);
        }
    }

    public void hideLoading(){
        mRelativeBuffer.setVisibility(INVISIBLE);
//        mNetSpeedText.setVisibility(View.INVISIBLE);
    }


    public interface OnVideoViewListener {
        void onkeyBack();
        void onPlayPrepared();
        void onPlayCompleted();
        void updateDate();
    }

    public void setVideoViewListener(OnVideoViewListener keyListener) {
        mViewListener = keyListener;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(!isFullScreen||controllerNotFlated()){
            return super.onKeyDown(keyCode, event);
        }
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                if(mViewListener != null){
                    mViewListener.onkeyBack();
                }
                return true;
            case KeyEvent.KEYCODE_ENTER:
                boolean isPrepared = mIVideoPresenter.isPrepared();
                boolean isPlaying = mIVideoPresenter.isPlaying();
                LogUtils.i(DetailConstant.TAG_DETAIL,TAG+"-->onKeyDown-->KEYCODE_ENTER-->isPrepared:"+isPrepared+"===isPlaying:"+isPlaying);
                if(isPrepared){//准备阶段不给响应确认按钮，不然界面会获取时间有误
                    if(isPlaying){
                        mIVideoPresenter.pausePlayer();
                    }else {
                        mIVideoPresenter.resumePlayer();
                    }
                }

                return true;

            case KeyEvent.KEYCODE_MENU:
                return true;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                seekTo(false);
                return true;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                seekTo(true);
                return true;

            default:
                super.onKeyDown(keyCode, event);
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBufferingStart() {
        if(mIVideoPresenter==null)return;
        showLoading();

        //屏蔽检查，不然会出现进度已到点但缓冲还没结束导致缓冲条提前消失
/*        if(!CommonUtils.getModel().contains("2861")){//2861不要做检查判断，
            mCurPositon = mIVideoPresenter.getCurrentPosition();
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onBufferingStart-->mCurPositon:"+mCurPositon);
            mHandler.removeMessages(HandlerMsg.CHECK_BUFFER.ordinal());
            mHandler.sendEmptyMessageDelayed(HandlerMsg.CHECK_BUFFER.ordinal(),2000);
        }*/

    }

    @Override
    public void onBufferingEnd() {
        hideLoading();

/*        if(!CommonUtils.getModel().contains("2861")){
            mHandler.removeMessages(HandlerMsg.CHECK_BUFFER.ordinal());
            mCurPositon = 0;
        }*/

    }

    @Override
    public void onPreparePlay() {
        if(mRelativeError.isShown()){
            mRelativeError.setVisibility(INVISIBLE);
        }

        timerHidePlaybar();

        WindowPlayer.getInstance().preparePlayer();
        if(mViewListener!=null){
            mViewListener.onPlayPrepared();
        }

        mHandler.sendEmptyMessage(HandlerMsg.ON_MOVIE_START.ordinal());
        onBufferingEnd();

        mStartTime = System.currentTimeMillis();
    }

    @Override
    public void onPausePlay() {
        if(controllerNotFlated())return;
        requestLayout();
        invalidate();
        mImgPause.setVisibility(View.VISIBLE);
        mHandler.removeMessages(HandlerMsg.HM_TIMER_UPDATE_SEEKBAR.ordinal());

        mHandler.removeMessages(HandlerMsg.HM_HIDE_PLAYBAR.ordinal());
        showPlaybar();

        //2991平台暂停按钮显示不出来，要加这些方法才行
        CommonUtils.refreshView(mImgPause);

    }

    @Override
    public void onResumePlay() {
        if(controllerNotFlated())return;
        mImgPause.setVisibility(View.INVISIBLE);
        mHandler.removeMessages(HandlerMsg.HM_TIMER_UPDATE_SEEKBAR.ordinal());   //必须先清了再加入消息，因为调onResumeUpdateUI之前并非一定调了onPauseUpdateUI
        mHandler.sendEmptyMessage(HandlerMsg.HM_TIMER_UPDATE_SEEKBAR.ordinal());
        timerHidePlaybar();
    }

    @Override
    public void onSeekCompleted() {
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onSeekCompleted");

        if(mImgPause!=null&&!mImgPause.isShown()) {//暂停时seek后也不隐藏播放条
            timerHidePlaybar();
        }

    }


    @Override
    public void onError(String msg) {
        mRelativeBuffer.setVisibility(GONE);
        if(mRelativeLoad!=null){
            mRelativeLoad.setVisibility(GONE);
        }
        if(mLinearLoading!=null){
            mLinearLoading.setVisibility(GONE);
        }
        if(mRelativePlaybar!=null){
            mRelativePlaybar.setVisibility(GONE);
        }
        if(mRelativeFast!=null){
            mRelativeFast.setVisibility(GONE);
        }

        removeMessage();

        boolean isNetAvailable = NetworkUtils.isNetworkConnected(mContext);
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onError-->isNetAvailable:"+isNetAvailable);
        if(isNetAvailable){
            if(TextUtils.isEmpty(msg)){
                msg = mContext.getString(R.string.error_title_video);
            }
        }else {
            msg = mContext.getString(R.string.error_no_network);
        }
        mTxtErrorTip.setText(msg);
        mHandler.sendEmptyMessageDelayed(HandlerMsg.HM_ERROR_RESTART.ordinal(), TIME_ERROR_RESTART);//小窗口播放出错后续播下一集
        mRelativeError.setVisibility(VISIBLE);

    }

    @Override
    public void onCompleted() {
        WindowPlayer.getInstance().onPlayerComplete();//这个要放在前面，因这个会更新小窗口想关数据后，下面方法执行才不会有误
        if(mViewListener!=null){
            mViewListener.onPlayCompleted();
        }
        reportBigData();

    }

    @Override
    public void onMovieStart() {
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onMovieStart");
        //有风险，可能存在无此回调的问题
//        mHandler.sendEmptyMessage(HandlerMsg.ON_MOVIE_START.ordinal());
//        onBufferingEnd();

    }

    private void reportBigData(){
        if(mStartTime!=0){
            String startTime = CommonUtils.stampToDate(mStartTime);
            long duration = (System.currentTimeMillis() - mStartTime)/1000;
            BigDataHelper.getInstance().sendPlayVideo(startTime,""+duration,mVideoTitle,mRouteName);
        }


    }


}
