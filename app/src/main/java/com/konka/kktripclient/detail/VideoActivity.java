package com.konka.kktripclient.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.iflytek.xiri.scene.Scene;
import com.konka.kktripclient.R;
import com.konka.kktripclient.detail.adapter.VideoEpisodeAdapter;
import com.konka.kktripclient.detail.bean.VideoParams;
import com.konka.kktripclient.detail.customview.RatingBar;
import com.konka.kktripclient.detail.customview.TripVideoView;
import com.konka.kktripclient.detail.presenter.SpeechControler;
import com.konka.kktripclient.detail.presenter.WindowPlayer;
import com.konka.kktripclient.layout.util.LaunchHelper;
import com.konka.kktripclient.layout.view.GlideRoundTransform;
import com.konka.kktripclient.net.HttpHelper;
import com.konka.kktripclient.net.info.AllVideosEvent.DataBean.VideosBean;
import com.konka.kktripclient.net.info.GoodsBean;
import com.konka.kktripclient.net.info.HttpErrorEvent;
import com.konka.kktripclient.net.info.IEvent;
import com.konka.kktripclient.net.info.VideoDetailsEvent;
import com.konka.kktripclient.ui.AppRecyclerView;
import com.konka.kktripclient.ui.RecycleViewDivider;
import com.konka.kktripclient.utils.BigDataHelper;
import com.konka.kktripclient.utils.CommonUtils;
import com.konka.kktripclient.utils.Constant;
import com.konka.kktripclient.utils.LogUtils;
import com.konka.kktripclient.utils.NetworkUtils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by Zhou Weilin on 2017-6-11.
 */

public class VideoActivity extends Activity {
    private final String TAG = VideoActivity.class.getSimpleName();
    private Context mContext;
    private TripVideoView mTripVideoView;

    private LinearLayout mLinearMenuTip;
    private LinearLayout mLinearShade;

    private TextView mTxtTitle;
    private TextView mTxtIntroduce;
    private TextView mTxtTime;
    private TextView mTxtPhone;
    private RatingBar mRatingBar;
    private TextView mTxtDiscountType;
    private TextView mTxtNewPrice;
    private TextView mTxtOldPrice;
    private ImageView mImgThumb;
    private Button mBtnCheck;

    private RelativeLayout mRelativeEpisode;
    private AppRecyclerView mRecyclerEpisode;
    private LinearLayoutManager mLayoutManager;
    private VideoEpisodeAdapter mEpisodeAdapter;

    private int mGoodId=-1;
    private int mGoodType=-1;
    private String mVideoNailUrl;

    private RelativeLayout mRelativeErrorPage;

    private VideoDetailsEvent.DataBean mVideoData;

    private int mStartType = -1;
    private GlideRoundTransform mRadiusTransform;

    private String mFormSource = "";//打开视频页的来源
    private String mVideoTitle;//当前播放视频名

    private Scene mScene;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);//解决起播放器后闪屏问题
        mContext = this.getApplicationContext();
        if(isNetException())return;
        initView();
        getDataByIntent();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        if(mScene == null){
            mScene = new Scene(mContext);
        }
        mScene.init(new SpeechControler(mTripVideoView));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mHandler!=null){
            mHandler.removeMessages(INIT_VIDEO);
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onDestroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onStop");
        if(mScene!=null){
            mScene.release();
        }
    }

    @Override
    public void onBackPressed() {
        onBack();
        super.onBackPressed();
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onBackPressed");
    }

    private void initView() {
        mTripVideoView = (TripVideoView)findViewById(R.id.video_activity_tripvideo);
        mTripVideoView.setFocusable(true) ;
        mTripVideoView.requestFocus();
        mTripVideoView.initShow(true);
        mTripVideoView.setVideoViewListener(new MyOnVideoKeyListener());

        mLinearMenuTip = (LinearLayout)findViewById(R.id.video_linear_menu_tip);
        mLinearShade = (LinearLayout)findViewById(R.id.video_linear_shade);

        mTxtTitle = (TextView)findViewById(R.id.video_shade_txt_title);
        mTxtIntroduce = (TextView)findViewById(R.id.video_shade_txt_introduce);
        mTxtTime = (TextView)findViewById(R.id.video_shade_txt_time);
        mTxtPhone = (TextView)findViewById(R.id.video_shade_txt_phone);
        mRatingBar = (RatingBar)findViewById(R.id.video_shade_ratingbar);
        mTxtDiscountType = (TextView)findViewById(R.id.video_shade_txt_discount_type);
        mTxtNewPrice = (TextView)findViewById(R.id.video_shade_txt_newprice);
        mTxtOldPrice = (TextView)findViewById(R.id.video_shade_txt_oldprice);
        mImgThumb = (ImageView)findViewById(R.id.video_shade_img_thumb);
        mBtnCheck = (Button)findViewById(R.id.video_shade_btn_check);

        mRelativeEpisode = (RelativeLayout)findViewById(R.id.video_relative_episode);
        mRecyclerEpisode = (AppRecyclerView) findViewById(R.id.video_recycler_episode);

        mTxtOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//加中间横线
        mTxtOldPrice.getPaint().setAntiAlias(true);// 抗锯齿
        mLinearMenuTip.setVisibility(View.INVISIBLE);
        mLinearShade.setVisibility(View.INVISIBLE);
        mRelativeEpisode.setVisibility(View.INVISIBLE);
        mBtnCheck.setOnClickListener(mOnClickListener);
    }


    //直接从intent中获取数据
    private void getDataByIntent(){
        Intent intent = getIntent();
        if (intent != null) {
            mStartType = intent.getIntExtra(DetailConstant.KEY_START_TYPE, DetailConstant.ILLEGAL_ID);
            mFormSource = intent.getStringExtra(Constant.KEY_START_SOURCE);
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->getDataByIntent-->mFormSource:" + mFormSource+"  startType:" + mStartType);
            if(mStartType == DetailConstant.START_TYPE_ROUTE){
                int goodId = intent.getIntExtra(Constant.KEY_GOOD_ID, DetailConstant.ILLEGAL_ID);
                LogUtils.d(DetailConstant.TAG_DETAIL, TAG+"-->getDataByIntent-->goodId:"  + goodId);
                if (goodId != DetailConstant.ILLEGAL_ID) {
                    HttpHelper.getInstance(mContext).getVideosDetailsByID(goodId);
                }

            }else if(mStartType == DetailConstant.START_TYPE_WINDOW){
                initAllVideo(WindowPlayer.getInstance().getVideosBeanList());

            }

        }

    }


    private boolean isNetException(){
        if(!NetworkUtils.isNetworkConnected(mContext)){
            mRelativeErrorPage = (RelativeLayout)findViewById(R.id.video_activity_relative_error_page);
            mRelativeErrorPage.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onMessageEvent(IEvent event) {
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onMessageEvent-->isViewAttached():"+"===event:"+event.getClass().getSimpleName());

        if (event instanceof HttpErrorEvent) {
            String type = ((HttpErrorEvent) event).getReq_type();
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onMessageEvent-->HttpErrorEvent:"+((HttpErrorEvent) event).getRetCode()+"  "+type);
            if("VideoDetailsEvent".equals(type)){
                mTripVideoView.onError(mContext.getString(R.string.error_title_video));
            }

        }
        if (event instanceof VideoDetailsEvent) {
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onMessageEvent-->VideoDetailsEvent:"+((VideoDetailsEvent) event).toString());
            mVideoData = null;
            mVideoData = ((VideoDetailsEvent) event).getData();
            if(mVideoData!=null){
                Message msg = new Message();
                msg.what = INIT_VIDEO;
                mHandler.removeMessages(INIT_VIDEO);
                mHandler.sendMessageDelayed(msg,500);

                updateShadeShow(mVideoData.getGoods());
                mVideoTitle = mVideoData.getName();

            }

        }

    }

    private void updateShadeShow(GoodsBean goodsBean){
        if(goodsBean!=null){
            mLinearMenuTip.setVisibility(View.VISIBLE);
            initShadeData(goodsBean);
        }else {
            if(mLinearShade!=null&&mLinearShade.isShown()){
                mLinearShade.setVisibility(View.INVISIBLE);
            }
            mLinearMenuTip.setVisibility(View.INVISIBLE);
        }
    }

    private void initShadeData(GoodsBean goodsBean) {
        mGoodId = goodsBean.getId();
        mGoodType = goodsBean.getGoodsType();
        mTxtTitle.setText(goodsBean.getName());
        mTxtIntroduce.setText(goodsBean.getDescription());
        mTxtTime.setText(goodsBean.getDepartureDate());
        mTxtPhone.setText(goodsBean.getCustomService());
        mRatingBar.setStar((float) goodsBean.getScore());
        mTxtDiscountType.setText(goodsBean.getDiscountType());
        mTxtNewPrice.setText(CommonUtils.formatPrice(""+goodsBean.getDiscountPrice()));
        String priceFormat = getResources().getString(R.string.detail_price_oldprice);
        mTxtOldPrice.setText(String.format(priceFormat, CommonUtils.formatPrice(""+goodsBean.getOriginalPrice())));
        mVideoNailUrl = NetworkUtils.toURL(goodsBean.getThumbnail());

    }

    private void initAllVideo(List<VideosBean> videosBeanLIst) {
//        List<VideosBean> videosBean = WindowPlayer.getInstance().getVideosBeanList();
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->initAllVideo");
        if(videosBeanLIst!=null&&videosBeanLIst.size()>0){
            mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            mRecyclerEpisode.setLayoutManager(mLayoutManager);
            mRecyclerEpisode.addItemDecoration(new RecycleViewDivider(
                    this, LinearLayoutManager.HORIZONTAL, CommonUtils.dip2px(mContext,16), getResources().getColor(R.color.transparent)));
            mRecyclerEpisode.setChangeType(AppRecyclerView.CHANGE_TYPE_CENTER);
            mRecyclerEpisode.addOnScrollListener(new MyOnScrollListener());
            mEpisodeAdapter = new VideoEpisodeAdapter(mContext);
            mEpisodeAdapter.setVideoItemInfos(videosBeanLIst);
            mEpisodeAdapter.setItemListener(new AdapterItemListener());
            mRecyclerEpisode.setAdapter(mEpisodeAdapter);

            VideosBean curVideosBean = videosBeanLIst.get(WindowPlayer.getInstance().getCurPlayPos());
            mVideoTitle = curVideosBean.getName();

            GoodsBean goodsBean = curVideosBean.getGoods();
            updateShadeShow(goodsBean);

            WindowPlayer.getInstance().setTripVideoView(mTripVideoView);
            WindowPlayer.getInstance().setIsFullScreen(true);
            WindowPlayer.getInstance().startVideoView();
        }else {
            if(mTripVideoView!=null){
                mTripVideoView.onError(mContext.getString(R.string.video_no_data));
            }
        }

    }

    private final int INIT_VIDEO = 0;
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case INIT_VIDEO:
                    String routeName = "-1";//大数据默认值
                    if(mVideoData.getGoods()!=null){
                        routeName = mVideoData.getGoods().getName();
                    }
                    String playParam = mVideoData.getPlayParam();
                    VideoParams videoParams = new VideoParams();
                    videoParams.setVideoActivity(true);
                    videoParams.setTitle(mVideoData.getName());
                    videoParams.setUrl(playParam);
                    videoParams.setRouteName(routeName);
                    mTripVideoView.initData(videoParams,true);
                    break;

                default:
                    break;
            }
        }
    };


    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id){
                case R.id.video_shade_btn_check:
                    JSONObject source = new JSONObject();
                    source.put(Constant.BIG_DATA_VALUE_KEY_SOURCE, Constant.BIG_DATA_VALUE_SOURCE_ZZ);
                    source.put(Constant.BIG_DATA_VALUE_KEY_NAME, "-1");
                    source.put(Constant.BIG_DATA_VALUE_KEY_LOCATION, "-1");
                    LaunchHelper.startDetailActivity(VideoActivity.this,mGoodType,mGoodId, source.toJSONString());
//                    onBack();
                    BigDataHelper.getInstance().sendVideoMenu(CommonUtils.getCurrentDateString(),mVideoTitle,mFormSource);
                    break;

            }
        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_MENU){
            if(mLinearMenuTip.isShown()&&!mLinearShade.isShown()){//有显示说明有详情页
                if(mRelativeEpisode.isShown()){
                    hideSelectWorks();
                }
                showShade();
                mBtnCheck.requestFocus();
            }

        }else if(keyCode==KeyEvent.KEYCODE_BACK){
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->KEYCODE_BACK");
            if(mLinearShade!=null&&mLinearShade.isShown()){
                hideShade();
            }else if(mRelativeEpisode!=null&&mRelativeEpisode.isShown()){
                hideSelectWorks();
            }else {
                return super.onKeyUp(keyCode,event);//避免无网络出错或获取视频列表出错时按返回无响应
            }
        }else if(keyCode==KeyEvent.KEYCODE_DPAD_DOWN){
            if(mEpisodeAdapter!=null&&mEpisodeAdapter.getItemCount()>1){
                if(mRelativeEpisode.isShown()){
                    hideSelectWorks();

                }else {
                    if(mLinearShade.isShown()){
                        hideShade();
                    }
                    showSelectWorks();
                }
            }
        }
        return true;
    }


    //显示选集的动画
    private void showSelectWorks(){
        mEpisodeAdapter.notifyDataSetChanged();
        final int pos = WindowPlayer.getInstance().getCurPlayPos();
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.video_bottom_show);
        animation.setAnimationListener(new Animation.AnimationListener(){
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
                if(mLayoutManager.getFocusedChild() != null){
                    mLayoutManager.getFocusedChild().clearFocus();
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                for(int i = 0; i<mLayoutManager.getChildCount(); i++){
                    if(mLayoutManager.getChildAt(i)!=null){		//避免无列表时造成空指针
                        if(mLayoutManager.getChildAt(i).getTag().equals(pos)){//解决滑动后关闭再打开时焦点对应偏移出错问题
                            Log.d("kkzwl",TAG+"-->showSelectWorks-->pos:"+pos+"==i:"+i);
                            mEpisodeAdapter.viewRequestFocus(mLayoutManager.getChildAt(i));

                            break;
                        }
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }
        });
        mLayoutManager.scrollToPosition(pos);
        mRelativeEpisode.startAnimation(animation);
        mRelativeEpisode.setVisibility(View.VISIBLE);

        CommonUtils.refreshView(mRelativeEpisode);
    }

    //隐藏选集的动画
    private void hideSelectWorks(){
        mRelativeEpisode.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.video_bottom_hide));
        mRelativeEpisode.setVisibility(View.GONE);
    }

    //显示遮罩
    private void showShade(){
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.video_shade_show);
        animation.setAnimationListener(new Animation.AnimationListener(){
            public void onAnimationStart(Animation animation) {
                CommonUtils.refreshView(mLinearShade);//解决偶现遮罩上小半部分没了的情况
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                CommonUtils.refreshView(mLinearShade);
                loadImage(mImgThumb,mVideoNailUrl);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        mLinearShade.startAnimation(animation);
        mLinearShade.setVisibility(View.VISIBLE);
    }


    //隐藏遮罩
    private void hideShade(){
        mLinearShade.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.video_shade_hide));
        mLinearShade.setVisibility(View.INVISIBLE);
    }

    private void onBack() {
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onBack-->mStartType:"+mStartType);
        if(mStartType==DetailConstant.START_TYPE_WINDOW){
            WindowPlayer.getInstance().releasePlayer();
        }else if(mStartType == DetailConstant.START_TYPE_ROUTE){
            if(mTripVideoView!=null){
                mTripVideoView.releasePlayer();
            }
        }

        finish();
    }

    //实现长按滑动时不加载图片，解决卡顿和焦点消失问题
    private class MyOnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == 2) {
                Glide.with(mContext).pauseRequests();
            }
            if (newState == 0) {
                Glide.with(mContext).resumeRequests();
            }
        }
    }

    private class AdapterItemListener implements VideoEpisodeAdapter.ItemListener {
        @Override
        public void itemClick(VideosBean info, int pos) {
            if(pos==WindowPlayer.getInstance().getCurPlayPos()){
                LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->AdapterItemListener-->itemClick-->The same video,don't change!");
                return;
            }
            hideSelectWorks();
            mEpisodeAdapter.notifyDataSetChanged();

            updateShadeShow(info.getGoods());

            WindowPlayer.getInstance().setCurPlayPos(pos);
            if(!WindowPlayer.getInstance().isPlaying()){//暂停状态切集不调以下方法播放器不会回调onPrepare
                WindowPlayer.getInstance().resumePlayer();
            }
            WindowPlayer.getInstance().setIsFullScreen(true);
            WindowPlayer.getInstance().startVideoView();
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->AdapterItemListener-->itemClick-->pos:"+pos);
        }

    }

    private class MyOnVideoKeyListener implements TripVideoView.OnVideoViewListener {
        @Override
        public void onkeyBack() {
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->MyOnVideoKeyListener-->onkeyBack");
            if(mStartType == DetailConstant.START_TYPE_WINDOW){//焦点在tripview上，但选集框还在显示的情况
                if(mRelativeEpisode!=null&&mRelativeEpisode.isShown()){
                    hideSelectWorks();
                    return;
                }

            }
            onBack();
        }

        @Override
        public void onPlayPrepared() {
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->MyOnVideoKeyListener-->onPlayPrepared");
        }

        @Override
        public void onPlayCompleted() {
            if(mStartType == DetailConstant.START_TYPE_WINDOW){
                if (WindowPlayer.getInstance().getVideosBeanList() != null
                        && WindowPlayer.getInstance().getVideosBeanList().size() != 0) {
                    int pos = WindowPlayer.getInstance().getCurPlayPos();
                    LogUtils.d(DetailConstant.TAG_DETAIL, TAG+"-->MyOnVideoKeyListener-->onPlayCompleted-->pos:"+pos);
                    GoodsBean goodsBean = WindowPlayer.getInstance().getVideosBeanList().get(pos).getGoods();
                    updateShadeShow(goodsBean);
                }

            }else if(mStartType == DetailConstant.START_TYPE_ROUTE){
                LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->MyOnVideoKeyListener-->onPlayCompleted-->mVideoData:"+mVideoData);
                if(mVideoData!=null&&mHandler!=null){//解决单视频不会循环播放问题
                    mHandler.removeMessages(INIT_VIDEO);
                    mHandler.sendEmptyMessageDelayed(INIT_VIDEO,500);
                }

            }

        }

        @Override
        public void updateDate() {
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->MyOnVideoKeyListener-->updateDate");
            if(mRelativeEpisode!=null&&mRelativeEpisode.isShown()){
                hideSelectWorks();
            }
        }

    }

    private void loadImage(ImageView imageView, String url) {
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        if(mRadiusTransform==null){
            mRadiusTransform = new GlideRoundTransform(mContext, CommonUtils.dip2px(mContext,4));
        }
        Glide.with(mContext)
                .load(NetworkUtils.toURL(url))
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .transform(mRadiusTransform)
                .override(CommonUtils.dip2px(mContext,260), CommonUtils.dip2px(mContext,146))
                .into(imageView);
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
