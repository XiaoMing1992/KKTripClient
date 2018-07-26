package com.konka.kktripclient.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iflytek.xiri.scene.Scene;
import com.konka.account.callback.CallBack;
import com.konka.account.wrapper.UUCWrapper;
import com.konka.kktripclient.R;
import com.konka.kktripclient.activity.CollectActivity;
import com.konka.kktripclient.activity.IUpdateCollect;
import com.konka.kktripclient.database.CollectTableDao;
import com.konka.kktripclient.detail.bean.RouteInfo;
import com.konka.kktripclient.detail.bean.VideoParams;
import com.konka.kktripclient.detail.customview.PhotoThumbView;
import com.konka.kktripclient.detail.customview.RatingBar;
import com.konka.kktripclient.detail.customview.TripPhotoView;
import com.konka.kktripclient.detail.customview.TripVideoView;
import com.konka.kktripclient.detail.interfaces.IDetailView;
import com.konka.kktripclient.detail.interfaces.IProcessData;
import com.konka.kktripclient.detail.presenter.DetailData;
import com.konka.kktripclient.detail.presenter.SpeechControler;
import com.konka.kktripclient.layout.util.LaunchHelper;
import com.konka.kktripclient.net.info.IEvent;
import com.konka.kktripclient.net.info.TicketsBean;
import com.konka.kktripclient.net.info.TourRoutesBean;
import com.konka.kktripclient.pay.UserHelper;
import com.konka.kktripclient.ui.popupwindow.LoginWindow;
import com.konka.kktripclient.utils.BigDataHelper;
import com.konka.kktripclient.utils.CommonUtils;
import com.konka.kktripclient.utils.Constant;
import com.konka.kktripclient.utils.LogUtils;
import com.konka.kktripclient.utils.NetworkUtils;
import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Zhou Weilin on 2017-6-7.
 */

public class DetailActivity extends Activity implements IDetailView{
    private static final String TAG = DetailActivity.class.getSimpleName();
    private Context mContext;
    private IProcessData mIProcessData;
    private RelativeLayout mRelativeWindow;
    private TripPhotoView mPhotoView;
    private PhotoThumbView mPhotoThumb;
//    private TripVideoView mVideoView;
    private TripVideoView mTripVideoView;
    private TextView mTxtTitle;
    private RatingBar mRatingBar;
    private TextView mTxtDiscountType;
    private TextView mTxtDiscountPrice;
    private TextView mTxtOldPrice;
    private TextView mTxtIntroduce;
    private TextView mTxtTimeName;
    private TextView mTxtTime;
    private TextView mTxtPhone;
    private Button mBtnBuy;
    private Button mBtnSave;
    private ScrollView mScrollIntroduce;

    private int mCurThumbPos = 0;
    private int mGoodId = -1;
    private int mGoodType;
    private String mTicketQRUrl;
    private List<String> mImgUrls;

    private RouteInfo mRouteInfo;

    private LoginWindow mLoginWindow;
    private String mVideoThumb;
    private boolean isCotainVideo = false;
    private TourRoutesBean.VideoBean mVideoBean;

    private RelativeLayout mRelativeInfo;
    private RelativeLayout mRelativeLoadPage;
    private RelativeLayout mRelativeErrorPage;
    private RelativeLayout mRelativeVideo;

    private TextView mTxtErrorMsg;

    private boolean isVideoFull = false;
    private int mSmallCurPos=0;//用于详情页小窗口保存当前时长，在91平台跳到另一页再回来会播放异常
    private boolean isAtPhoto = false;

    private UpdateCollect mUpdateCollect;

    private String mFormSource = "";//打开详情页的来源
    private long mStartTime;//页面启动时间
    private String mGoodName="-1";//商品名称,-1是大数据默认无数据值
    private String mGoodPrice;//商品时间

    private Scene mScene;
    private SpeechControler mSpeechControler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);//解决起播放器后闪屏问题
        mContext = this.getApplicationContext();
        if(isNetException())return;
        initView();
        getDataByIntent();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        LogUtils.d(DetailConstant.TAG_DETAIL, TAG+"-->onResume");
        super.onResume();
        onResumeVideo();
        mStartTime = System.currentTimeMillis();
        //友盟统计
        if (!Constant.MONKEY) MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //友盟统计
        if (!Constant.MONKEY) MobclickAgent.onPause(this);

        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onPause");
        onReleaseVideo();

        String startTime = CommonUtils.stampToDate(mStartTime);
        long duration = (System.currentTimeMillis() - mStartTime)/1000;
        BigDataHelper.getInstance().sendEnterGoodsDetail(startTime,mGoodName,""+mGoodType,""+duration,mGoodPrice,mFormSource);
        if (mFormSource.contains(Constant.BIG_DATA_VALUE_SOURCE_TJ) || mFormSource.contains(Constant.BIG_DATA_VALUE_SOURCE_TC)) {
            BigDataHelper.getInstance().sendRecommendClick(startTime, String.valueOf(duration), mFormSource, mGoodName);
        }
    }

    @Override
    protected void onStop() {
        LogUtils.i(DetailConstant.TAG_DETAIL,TAG+"-->onStop");
        super.onStop();

    }



    @Override
    protected void onDestroy() {
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onDestroy");
        super.onDestroy();
        if(mIProcessData!=null){//避免无网时未初始化
            mIProcessData.detachView();
            mIProcessData = null;
        }
        if (mLoginWindow != null) {
            mLoginWindow.unregisterListener();
        }
        getWindow().getDecorView().destroyDrawingCache();
        mPhotoView = null;
        mPhotoThumb = null;
        Glide.get(mContext).clearMemory();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onBackPressed");
        mSmallCurPos = 0;
        mHandler.removeMessages(0);
        if(mTripVideoView!=null){
            mTripVideoView.releasePlayer();
        }
    }

    private void initView() {
        mRelativeWindow = (RelativeLayout)findViewById(R.id.detail_relative_window);
        mPhotoView = (TripPhotoView)findViewById(R.id.detail_photo);
        mPhotoThumb = (PhotoThumbView)findViewById(R.id.detail_thumbs);

        mTxtTitle = (TextView)findViewById(R.id.detail_txt_title);
        mRatingBar = (RatingBar)findViewById(R.id.detail_ratingbar);
        mTxtDiscountType = (TextView)findViewById(R.id.detail_txt_discounttype);
        mTxtDiscountPrice = (TextView)findViewById(R.id.detail_txt_discountprice);
        mTxtOldPrice = (TextView)findViewById(R.id.detail_txt_oldprice);
        mTxtIntroduce = (TextView)findViewById(R.id.detail_txt_introduce);
        mTxtTimeName = (TextView)findViewById(R.id.detail_txt_time_name);
        mTxtTime = (TextView)findViewById(R.id.detail_txt_time);
        mTxtPhone = (TextView)findViewById(R.id.detail_txt_phone);
        mBtnBuy = (Button) findViewById(R.id.detail_btn_buy);
        mBtnSave = (Button) findViewById(R.id.detail_btn_save);
        mScrollIntroduce = (ScrollView) findViewById(R.id.detail_scroll_introduce);

        mRelativeInfo = (RelativeLayout)findViewById(R.id.detail_relative_info);
        mRelativeLoadPage = (RelativeLayout)findViewById(R.id.detail_load_page);
        mRelativeErrorPage = (RelativeLayout)findViewById(R.id.detail_error_page);
        mRelativeVideo = (RelativeLayout)findViewById(R.id.detail_relative_video);

        mTxtErrorMsg = (TextView)findViewById(R.id.error_title);

        mRelativeInfo.setVisibility(View.INVISIBLE);
        mRelativeWindow.setVisibility(View.INVISIBLE);
        mRelativeErrorPage.setVisibility(View.INVISIBLE);
        mRelativeLoadPage.setVisibility(View.VISIBLE);
        mRelativeVideo.setVisibility(View.INVISIBLE);

        mRelativeWindow.setOnClickListener(mOnClickListener);
        mPhotoThumb.setOnItemListener(mThumbListener);
        mTxtOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//加中间横线
        mTxtOldPrice.getPaint().setAntiAlias(true);// 抗锯齿
        mBtnBuy.setOnClickListener(mOnClickListener);
        mBtnSave.setOnClickListener(mOnClickListener);

        mScrollIntroduce.setVerticalScrollBarEnabled(false);
        mScrollIntroduce.setOnFocusChangeListener(mFocusChangeListener);

        //解决部分焦点问题
        mRelativeWindow.setNextFocusRightId(R.id.detail_btn_buy);
        mRelativeWindow.setNextFocusDownId(R.id.detail_relative_thumb0);
        mRelativeWindow.setNextFocusLeftId(R.id.detail_relative_window);
        mScrollIntroduce.setNextFocusDownId(R.id.detail_btn_buy);
        mBtnBuy.requestFocus();

        mIProcessData = new DetailData(DetailActivity.this,this);//这个获取数据的类应放在前面view的初始化之后

        isVideoFull = false;
    }



    //直接从intent中获取数据
    private void getDataByIntent(){
        Intent intent = getIntent();
        if (intent != null) {
            int goodType = intent.getIntExtra(Constant.KEY_GOOD_TYPE, DetailConstant.ILLEGAL_ID);
            int goodId = intent.getIntExtra(Constant.KEY_GOOD_ID, DetailConstant.ILLEGAL_ID);
            mFormSource = intent.getStringExtra(Constant.KEY_START_SOURCE);
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->getDataByIntent-->mFormSource:" + mFormSource);
            if (goodType != DetailConstant.ILLEGAL_ID && goodId != DetailConstant.ILLEGAL_ID) {
                mIProcessData.getDataById(goodType, goodId);
                LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->getDataByIntent-->goodType:" + goodType + "===goodId:" + goodId);
            }
        }

    }


    private boolean isNetException(){
        if(!NetworkUtils.isNetworkConnected(mContext)){
            mRelativeErrorPage = (RelativeLayout)findViewById(R.id.detail_error_page);
            mTxtErrorMsg = (TextView)findViewById(R.id.error_title);
            mTxtErrorMsg.setText(getString(R.string.error_title));
            mRelativeErrorPage.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }


    @Override
    public void onUpdateRoute(IEvent event) {
        if (event instanceof TourRoutesBean) {
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onUpdateRoute-->RouteDetailsByID:"+((TourRoutesBean) event).getName());
            TourRoutesBean tourRoutes = (TourRoutesBean) event;
            mGoodId = tourRoutes.getId();
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onUpdateRoute-->RouteDetailsByID:"+mGoodId);
            mGoodType = tourRoutes.getGoodsType();
            mVideoBean = null;
            mVideoBean = tourRoutes.getVideo();

            if(mVideoBean!=null){
                buildVideoView();

                isCotainVideo = true;
                mPhotoView.setVisibility(View.INVISIBLE);
                mVideoThumb = NetworkUtils.toURL(mVideoBean.getThumbnail());
                mHandler.removeMessages(0);
                mHandler.sendEmptyMessageDelayed(0,500);
            }

            initImgUrls(tourRoutes);
            setTxtInfo(tourRoutes.getName(),tourRoutes.getDescription(),
                    getString(R.string.detail_start_time),tourRoutes.getDepartureDate(),tourRoutes.getCustomService());
            setScore((float)tourRoutes.getScore());
            setPrice(tourRoutes.getDiscountType(),tourRoutes.getDiscountPrice(),tourRoutes.getOriginalPrice());
            setPhotoView(mImgUrls);
            setPhotoThumb(mImgUrls);
            updateCollectState();
            initRouteInfo(tourRoutes);

        }
        mRelativeLoadPage.setVisibility(View.INVISIBLE);
        mRelativeWindow.setVisibility(View.VISIBLE);
        mRelativeInfo.setVisibility(View.VISIBLE);

    }


    @Override
    public void onUpdateTicket(IEvent event) {
        if (event instanceof TicketsBean) {
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onUpdateTicket-->RouteDetailsByID:"+((TicketsBean) event).getName());
            TicketsBean ticket = (TicketsBean) event;
            mGoodId = ticket.getId();
            mGoodType = ticket.getGoodsType();
            mTicketQRUrl = ticket.getQrcode();
            initImgUrls(ticket);
            setTxtInfo(ticket.getName(),ticket.getDescription(),
                    getString(R.string.detail_business_time),ticket.getBusinessHours(),ticket.getCustomService());
            setScore((float)ticket.getScore());
            setPrice(ticket.getDiscountType(),ticket.getDiscountPrice(),ticket.getOriginalPrice());
            setPhotoView(mImgUrls);
            setPhotoThumb(mImgUrls);
            updateCollectState();

        }
        mRelativeLoadPage.setVisibility(View.INVISIBLE);
        mRelativeWindow.setVisibility(View.VISIBLE);
        mRelativeInfo.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError(String msg) {//改成gone,因invisible还能接收到焦点
        mRelativeWindow.setVisibility(View.GONE);
        if(mRelativeVideo!=null){
            mRelativeVideo.setVisibility(View.GONE);
        }
        mRelativeInfo.setVisibility(View.GONE);
        mPhotoThumb.setVisibility(View.GONE);
        mRelativeLoadPage.setVisibility(View.GONE);
        mRelativeErrorPage.setVisibility(View.VISIBLE);
        mTxtErrorMsg.setText(msg);

    }

    private void buildVideoView(){
        mTripVideoView = new TripVideoView(mContext);
        mTripVideoView.initShow(isVideoFull);
        mTripVideoView.setVideoViewListener(new OnVideoListener());
        RelativeLayout.LayoutParams videoParams = new RelativeLayout.LayoutParams(CommonUtils.dip2px(mContext,666),
                CommonUtils.dip2px(mContext,370));
        videoParams.leftMargin = CommonUtils.dip2px(mContext,72);
        videoParams.topMargin = CommonUtils.dip2px(mContext,96);
        mTripVideoView.setLayoutParams(videoParams);
        mRelativeVideo.removeAllViews();
        mRelativeVideo.addView(mTripVideoView);
        mRelativeVideo.setVisibility(View.VISIBLE);
    }


    private void initImgUrls(IEvent event) {
        mImgUrls = new ArrayList<>();
        String poster1 = null;
        String poster2 = null;
        String poster3 = null;
        String poster4 = null;
        String poster5 = null;
        if (event instanceof TourRoutesBean) {
            TourRoutesBean route = (TourRoutesBean) event;
            poster1 = route.getPoster1();
            poster2 = route.getPoster2();
            poster3 = route.getPoster3();
            poster4 = route.getPoster4();
            poster5 = route.getPoster5();
        }else if (event instanceof TicketsBean) {
            TicketsBean ticket = (TicketsBean)event;
            poster1 = ticket.getPoster1();
            poster2 = ticket.getPoster2();
            poster3 = ticket.getPoster3();
            poster4 = ticket.getPoster4();
            poster5 = ticket.getPoster5();
        }

        if (!TextUtils.isEmpty(poster1)) {
            mImgUrls.add(NetworkUtils.toURL(poster1));
        }
        if (!TextUtils.isEmpty(poster2)) {
            mImgUrls.add(NetworkUtils.toURL(poster2));
        }
        if (!TextUtils.isEmpty(poster3)) {
            mImgUrls.add(NetworkUtils.toURL(poster3));
        }
        if (!TextUtils.isEmpty(poster4)) {
            mImgUrls.add(NetworkUtils.toURL(poster4));
        }
        if (!TextUtils.isEmpty(poster5)) {
            mImgUrls.add(NetworkUtils.toURL(poster5));
        }

    }

    //初始化发给填写信息activity的信息类
    private void initRouteInfo(TourRoutesBean tour){
        mRouteInfo = new RouteInfo();
        mRouteInfo.setGoodId(tour.getId());
        mRouteInfo.setGoodName(tour.getName());
        mRouteInfo.setGoodPrice(tour.getDiscountPrice());
        mRouteInfo.setDeparturePlace(tour.getDeparturePlace());
        mRouteInfo.setDepartureTime(tour.getDepartureDate());
        mRouteInfo.setThumbNail(NetworkUtils.toURL(tour.getThumbnail()));
    }

    private void setTxtInfo(String title,String introduce,String timeName,String time,String phone){
        mTxtTitle.setText(title);
        mTxtIntroduce.setText(introduce);
        if(!mScrollIntroduce.canScrollVertically(1)){//当判断不能滑动时不获取焦点
            mScrollIntroduce.setFocusable(false);
        }
        mTxtTimeName.setText(timeName);
        mTxtTime.setText(time);
        mTxtPhone.setText(phone);

        mGoodName = title;
    }

    //设置评分
    private void setScore(float score){
        mRatingBar.setStar(score);
    }


    //设置价格信息
    private void setPrice(String discountType,double newPrice,double oldPrice){
        mTxtDiscountType.setText(discountType);
        mTxtDiscountPrice.setText(CommonUtils.formatPrice(""+newPrice));
        String priceFormat = getResources().getString(R.string.detail_price_oldprice);
        mTxtOldPrice.setText(String.format(priceFormat, CommonUtils.formatPrice(""+oldPrice)));

        mGoodPrice = ""+newPrice;
    }

    //设置图片链接
    private void setPhotoView(List<String> urls){
        if(urls==null||urls.size()==0)return;
        mPhotoView.initPhotoView(urls,mCurThumbPos);
    }

    //设置缩略图链接
    private void setPhotoThumb(List<String> urls){
        if(urls==null||urls.size()==0)return;

        if(isCotainVideo){
            List<String> allUrls = new ArrayList<>();
            allUrls.add(0,mVideoThumb);
            for (String url:urls){//不直接传urls，不然影响urls的值
                allUrls.add(url);
            }
            LogUtils.i(DetailConstant.TAG_DETAIL,TAG+"-->setPhotoThumb-->allUrls"+allUrls);
            mPhotoThumb.updateDate(allUrls,true);
        }else {
            LogUtils.i(DetailConstant.TAG_DETAIL,TAG+"-->setPhotoThumb-->urls"+urls);
            mPhotoThumb.updateDate(urls);
        }

    }

    private void initScene(){
        if(mScene == null){
            mScene = new Scene(mContext);
        }
        mSpeechControler = null;
        mSpeechControler = new SpeechControler(mTripVideoView);

        mScene.init(mSpeechControler);
        LogUtils.i(DetailConstant.TAG_DETAIL,TAG+"-->initScene");
    }

    private void releaseScene(){
        if(mScene!=null){
            mScene.release();
            LogUtils.i(DetailConstant.TAG_DETAIL,TAG+"-->releaseScene");
        }
    }


    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id){
                case R.id.detail_relative_window:
                    if(isCotainVideo){
                        if(mTripVideoView!=null&&mTripVideoView.isShown()){
                            isVideoFull = true;
                            mTripVideoView.setFullScreen();
                            mTripVideoView.setFocusable(true);
                            mTripVideoView.requestFocus();

                            initScene();
                        }else {
                            if(mCurThumbPos>0){//解决零延时跑monkey导致的越界问题
                                new PhotoFullDialog(DetailActivity.this, mImgUrls,mCurThumbPos-1).show();
                            }

                        }
                    }else {
                        new PhotoFullDialog(DetailActivity.this, mImgUrls,mCurThumbPos).show();
                    }

                    break;

                case R.id.detail_btn_buy:
                    if(mGoodType==DetailConstant.GOOD_ROUTE){
                        boolean isLogin = UserHelper.getInstance(mContext).getUserLogin();
                        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->detail_btn_buy-->isLogin:" + isLogin);
                        if(isLogin){
                            LaunchHelper.startWriteActivity(DetailActivity.this,mRouteInfo,mFormSource);
                        }else {
                            startLogin(Constant.BIG_DATA_VALUE_SOURCE_DM);
                        }

                    }else if(mGoodType==DetailConstant.GOOD_TICKET){
                        LaunchHelper.startBookActivity(DetailActivity.this,mGoodId,mTicketQRUrl,mFormSource);
                    }

                    BigDataHelper.getInstance().sendDetailButtonClick(CommonUtils.getCurrentDateString(),mGoodName,String.valueOf(mGoodType),mContext.getString(R.string.detail_btn_buy));

                    break;

                case R.id.detail_btn_save:
                    boolean isLogin = UserHelper.getInstance(mContext).getUserLogin();
                    LogUtils.d(DetailConstant.TAG_DETAIL, TAG+"-->detail_btn_save-->isLogin:" + isLogin);
                    if(isLogin){
                        boolean isSucceed = false;
                        if(isIdCollected()){
                            isSucceed = CollectTableDao.getInstance(mContext).deleteByGoodId(UserHelper.getInstance(mContext).getOpenId(),mGoodType,mGoodId);
                            if (isSucceed)
                                CollectActivity.fresh = true;//按返回键回到收藏页面的时候，需要刷新收藏页面
                            LogUtils.i(DetailConstant.TAG_DETAIL,TAG+"-->detail_btn_save-->cancel-->isSucceed:"+isSucceed);
                        }else {
                            isSucceed = CollectTableDao.getInstance(mContext).insert(UserHelper.getInstance(mContext).getOpenId(),mGoodType,mGoodId);
                            if (isSucceed)
                                CollectActivity.fresh = true;//按返回键回到收藏页面的时候，需要刷新收藏页面
                            LogUtils.i(DetailConstant.TAG_DETAIL,TAG+"-->detail_btn_save-->isIdCollected-->openId:"+UserHelper.getInstance(mContext).getOpenId()+" mGoodType:"+mGoodType+" mGoodId"+mGoodId);
                            LogUtils.i(DetailConstant.TAG_DETAIL,TAG+"-->detail_btn_save-->save-->isSucceed:"+isSucceed);

                            String collect_time = CommonUtils.getCurrentDateString();
                            LogUtils.d(DetailConstant.TAG_DETAIL, "collect_time is "+collect_time);
                            LogUtils.d(DetailConstant.TAG_DETAIL, "goods_name is "+mTxtTitle.getText().toString().trim());
                            LogUtils.d(DetailConstant.TAG_DETAIL, "price is "+mTxtDiscountPrice.getText().toString().trim());
                            BigDataHelper.getInstance().sendCollectGoods(collect_time, mTxtTitle.getText().toString().trim(), mTxtDiscountPrice.getText().toString().trim(), String.valueOf(mGoodType));
                        }

                        updateCollectState();

                    }else {
                        startLogin(Constant.BIG_DATA_VALUE_SOURCE_DS);
                    }
                    break;
            }
        }
    };

    private PhotoThumbView.ItemListener mThumbListener = new PhotoThumbView.ItemListener() {
        @Override
        public void clickItem(int pos,int viewId) {
            LogUtils.i(DetailConstant.TAG_DETAIL,TAG+"-->PhotoThumbView.ItemListener-->pos:"+pos);
            if(isCotainVideo){
                if(pos==0){
                    isAtPhoto = false;
                    if(mTripVideoView == null){
                        onResumeVideo();
                        mPhotoView.setVisibility(View.INVISIBLE);
                    }else {
                        if(mRelativeVideo.isShown()){

                        }else {
                            mPhotoView.setVisibility(View.INVISIBLE);
                            mRelativeVideo.setVisibility(View.VISIBLE);
                            if(mTripVideoView.getVideoPresenter()!=null && !mTripVideoView.getVideoPresenter().isPlaying()){
                                mTripVideoView.getVideoPresenter().resumePlayer();
                            }
                        }
                    }

                }else {
                    mRelativeVideo.setVisibility(View.INVISIBLE);
                    mPhotoView.setVisibility(View.VISIBLE);
                    if(mTripVideoView != null && mTripVideoView.getVideoPresenter()!=null){
                        if(!mTripVideoView.getVideoPresenter().isPrepared()){
                            LogUtils.i(DetailConstant.TAG_DETAIL,TAG+"-->PhotoThumbView.ItemListener-->releaseVideo");
                            onReleaseVideo();
                        }else if(mTripVideoView.getVideoPresenter().isPlaying()){
                            mTripVideoView.getVideoPresenter().pausePlayer();
                        }
                    }

                    mPhotoView.setCurrentPage(pos-1);
                    isAtPhoto = true;
                }
                mCurThumbPos = pos;
                mRelativeWindow.setNextFocusDownId(viewId);//设置小窗口向下按键要到达的焦点位置
            }else {
                mCurThumbPos = pos;
                mPhotoView.setCurrentPage(pos);
                mRelativeWindow.setNextFocusDownId(viewId);//设置小窗口向下按键要到达的焦点位置
            }

        }
    };

    public static class ViewWrapper{
        private View mTarget;

        public ViewWrapper(View target){
            mTarget = target;
        }

        public int getWidth(){
            return mTarget.getLayoutParams().width;
        }

        public void setWidth(int width){
            mTarget.getLayoutParams().width = width;
            mTarget.requestLayout();

        }

        public int getHeight(){
            return mTarget.getLayoutParams().height;
        }

        public void setHeight(int height){
            mTarget.getLayoutParams().height = height;
            mTarget.requestLayout();

        }
    }

    private void updateCollectState(){
        if(isIdCollected()){
            mBtnSave.setText(getString(R.string.detail_btn_saved));
        }else {
            mBtnSave.setText(getString(R.string.detail_btn_save));

        }
    }

    private boolean isIdCollected(){
        LogUtils.i(DetailConstant.TAG_DETAIL,TAG+"-->isIdCollected-->mGoodType:"+mGoodType+" mGoodId:"+mGoodId);
        boolean isCollected = CollectTableDao.getInstance(mContext).queryByGoodId(UserHelper.getInstance(mContext).getOpenId(),mGoodType,mGoodId);
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->isIdCollected-->isCollected:"+isCollected);
        return isCollected;
    }


    private void startLogin(final String source){
        onReleaseVideo();
        WeakReference<DetailActivity> mActivityReference = new WeakReference<>(this);
        DetailActivity detailActivity = mActivityReference.get();
        if(detailActivity!=null){
            if (mLoginWindow == null) {
                mLoginWindow = new LoginWindow(detailActivity);
            }
            if(mUpdateCollect == null){
                mUpdateCollect = new UpdateCollect(detailActivity);
            }
            mLoginWindow.registerLoginListener(source);
            mLoginWindow.setIUpdateCollect(mUpdateCollect);
            mLoginWindow.show();
            getLoginQRCodeUrl();
        }

    }


    private static class UpdateCollect implements IUpdateCollect{
        private final DetailActivity mActivity;

        public UpdateCollect(DetailActivity activity) {
            mActivity = activity;
        }

        @Override
        public void updateState() {
            if(mActivity!=null){
                LogUtils.i(DetailConstant.TAG_DETAIL,TAG+"-->updateState-->isLogin:" + UserHelper.getInstance(mActivity).getUserLogin()
                            +"-->updateState-->openId:"+UserHelper.getInstance(mActivity).getOpenId());
                mActivity.updateCollectState();
            }

        }

        @Override
        public void exitWindow() {
            LogUtils.i(DetailConstant.TAG_DETAIL,TAG+"-->exitWindow");
            mActivity.onResumeVideo();

        }
    }


    //获取登陆二维码
    public void getLoginQRCodeUrl() {
        UUCWrapper.getInstance(mContext).getLoginQRCodeUrl(new CallBack<String>() {
            @Override
            public void onComplete(String url) {
                LogUtils.d(DetailConstant.TAG_DETAIL, TAG+"-->getLoginQRCodeUrl onComplete: " + url);
                mLoginWindow.refreshQRCode(url);
            }

            @Override
            public void onError(String s) {

            }
        });
    }

    private void onReleaseVideo(){
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onReleaseVideo-->isCotainVideo:"+isCotainVideo);
        if(isCotainVideo){
            mHandler.removeMessages(0);
            if(mTripVideoView!=null&&mTripVideoView.getVideoPresenter()!=null){
                LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onReleaseVideo-->isCotainVideo-->release");
                mSmallCurPos = mTripVideoView.getVideoPresenter().getCurrentPosition();
                if(mTripVideoView.getVideoPresenter()!=null){
                    mTripVideoView.getVideoPresenter().setStoping(true);
                }
                mRelativeVideo.setVisibility(View.INVISIBLE);//解决起二维码界面回来后上一次画面停留问题
                mTripVideoView.releasePlayer();
                mTripVideoView = null;
                isVideoFull = false;
            }

        }
    }

    private void onResumeVideo(){
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onResumeVideo-->isCotainVideo:"+isCotainVideo+"---isAtPhoto:"+isAtPhoto);
        if(!isAtPhoto && isCotainVideo){
            if(mTripVideoView!=null){
                onReleaseVideo();
            }
            buildVideoView();
            if(mTripVideoView.getVideoPresenter()!=null){
                mTripVideoView.getVideoPresenter().setStoping(false);
            }
            mHandler.removeMessages(0);
            mHandler.sendEmptyMessageDelayed(0,500);
        }
    }


    public Handler mHandler = new Handler() {
        //Bundle bundle = null;
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if(mVideoBean!=null&&mTripVideoView!=null){
                        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->mHandler-->isVideoFull:"+isVideoFull);
                        String playParam = mVideoBean.getPlayParam();
                        VideoParams videoParams = new VideoParams();
                        videoParams.setTitle(mVideoBean.getName());
                        videoParams.setUrl(playParam);
                        videoParams.setRouteName(mGoodName);
                        mTripVideoView.initData(videoParams,isVideoFull);
                    }

                    break;


                default:
                    break;
            }
        }
    };

    private class OnVideoListener implements TripVideoView.OnVideoViewListener {
        @Override
        public void onkeyBack() {
            isVideoFull = false;
            mTripVideoView.setOriginalScreen();
            mTripVideoView.setFocusable(false);
            if(mTripVideoView.getVideoPresenter()!=null && !mTripVideoView.getVideoPresenter().isPlaying()){
                mTripVideoView.getVideoPresenter().resumePlayer();
            }
            releaseScene();
        }

        @Override
        public void onPlayPrepared() {
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onPreparePlay-->mSmallCurPos:"+mSmallCurPos);

            if(mTripVideoView!=null&&mTripVideoView.isShown()){
                LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->OnVideoListener-->onPlayPrepared-->isShown");
                if(mSmallCurPos!=0){
                    mTripVideoView.getVideoPresenter().seekTo(mSmallCurPos);
                    mSmallCurPos = 0;
                }
            }else {
                LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->OnVideoListener-->onPlayPrepared-->notShown");
                onReleaseVideo();//解决其他页面回来后切到图片页面会播放声音的问题
            }
        }

        @Override
        public void onPlayCompleted() {
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->OnVideoListener-->onPlayCompleted-->mVideoBean:"+mVideoBean);
            if(mVideoBean!=null&&mHandler!=null){//解决单视频不会循环播放问题
                mHandler.removeMessages(0);
                mHandler.sendEmptyMessageDelayed(0,1000);
            }
        }

        @Override
        public void updateDate() {

        }
    }

    private View.OnFocusChangeListener mFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(v.getId() == R.id.detail_scroll_introduce){
                if(hasFocus){
                    mScrollIntroduce.setVerticalScrollBarEnabled(true);
                }else {
                    mScrollIntroduce.setVerticalScrollBarEnabled(false);

                }
            }

        }
    };
}
