package com.konka.kktripclient.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.konka.kktripclient.MainActivity;
import com.konka.kktripclient.R;
import com.konka.kktripclient.activity.ManageOrderActivity;
import com.konka.kktripclient.detail.bean.OrderInfo;
import com.konka.kktripclient.detail.customview.StrokeTextView;
import com.konka.kktripclient.net.HttpHelper;
import com.konka.kktripclient.net.info.HttpErrorEvent;
import com.konka.kktripclient.net.info.IEvent;
import com.konka.kktripclient.net.info.OrderInfoEvent;
import com.konka.kktripclient.net.info.PayReturnInfo;
import com.konka.kktripclient.pay.PayHelper;
import com.konka.kktripclient.pay.PayInfoBean;
import com.konka.kktripclient.pay.UserHelper;
import com.konka.kktripclient.utils.BigDataHelper;
import com.konka.kktripclient.utils.CommonUtils;
import com.konka.kktripclient.utils.Constant;
import com.konka.kktripclient.utils.LogUtils;
import com.konka.kktripclient.utils.NetworkUtils;
import com.konka.tvpay.KKPayClient;
import com.makeramen.roundedimageview.RoundedImageView;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Zhou Weilin on 2017-6-29.
 */

public class OrderActivity extends Activity {
    private final String TAG = OrderActivity.class.getSimpleName();
    private Context mContext;
    private Intent intent;
    private PayHelper mPayHelper;
    private String mOrderId;

    private LinearLayout mLinearConfirm;
    private RoundedImageView mThumbNail;
    private TextView mGoodName;
    private TextView mGoodNum;
    private TextView mGoodPrice;
    private TextView mUserName;
    private TextView mUserPhone;
    private TextView mAddress;
    private TextView mTime;
    private TextView mState;
    private Button mBtnConfirmSure;
    private Button mBtnConfirmCancel;

    private RelativeLayout mRelativeOrder;
    private StrokeTextView mTxtOrderTitle;
    private TextView mTxtOrderId;
    private RoundedImageView mImgThumb;
    private TextView mTxtOrderName;
    private TextView mTxtOrderCount;
    private TextView mTxtOrderPrice;
    private TextView mTxtOrderState;
    private Button mBtnOrderSure;
    private Button mBtnOrderCancel;

    private RelativeLayout mRelativeLoading;
    private RelativeLayout mRelativeError;

    private OrderInfo mOrderInfo;
    private String mFormSource = "";//从何处打开进入详情页进入的来的购买订单页


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order);
        mContext = this.getApplicationContext();
        initView();
        initData();
    }

    private void initData(){
        intent = getIntent();
        if(intent!=null) {
            mOrderInfo = intent.getParcelableExtra(DetailConstant.KEY_ORDERINFO);
            mFormSource = intent.getStringExtra(Constant.KEY_START_SOURCE);
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->initData-->mFormSource:" + mFormSource+"-->mOrderInfo:" + mOrderInfo);
            loadImage(mThumbNail,NetworkUtils.toURL(mOrderInfo.getThumbNail()));
            mGoodName.setText(mOrderInfo.getGoodName());
            mGoodNum.setText(""+mOrderInfo.getGoodNum());
            mGoodPrice.setText(mOrderInfo.getGoodPrice()+getString(R.string.write_good_price_yuan));
            mUserName.setText(mOrderInfo.getUserName());
            mUserPhone.setText(mOrderInfo.getUserPhone());
            mAddress.setText(mOrderInfo.getDeparturePlace());
            mTime.setText(mOrderInfo.getDepartureTime());
            mState.setText(getString(R.string.order_confirm_state_no_pay));
        }

    }

    private void initView() {
        //订单确定页
        mLinearConfirm = (LinearLayout)findViewById(R.id.order_confirm_linear_info);
        mThumbNail = (RoundedImageView)findViewById(R.id.order_confirm_img_thumb);
        mGoodName = (TextView)findViewById(R.id.order_confirm_txt_good_name);
        mGoodNum = (TextView)findViewById(R.id.order_confirm_txt_good_num);
        mGoodPrice = (TextView)findViewById(R.id.order_confirm_txt_good_price);
        mUserName = (TextView)findViewById(R.id.order_confirm_txt_user_name);
        mUserPhone = (TextView)findViewById(R.id.order_confirm_txt_user_phone);
        mAddress = (TextView)findViewById(R.id.order_confirm_txt_address);
        mTime = (TextView)findViewById(R.id.order_confirm_txt_time);
        mState = (TextView)findViewById(R.id.order_confirm_txt_order_state);
        mBtnConfirmSure = (Button)findViewById(R.id.order_confirm_btn_sure);
        mBtnConfirmCancel = (Button)findViewById(R.id.order_confirm_btn_cancel);

        //订单结果页
        mRelativeOrder = (RelativeLayout)findViewById(R.id.order_confirm_relative_pay_result);
        mTxtOrderTitle = (StrokeTextView)findViewById(R.id.write_txt_order_title);
        mTxtOrderId = (TextView)findViewById(R.id.write_order_txt_id);
        mImgThumb = (RoundedImageView) findViewById(R.id.write_order_img_thumb);
        mTxtOrderName = (TextView)findViewById(R.id.write_order_txt_name);
        mTxtOrderCount = (TextView)findViewById(R.id.write_order_txt_count);
        mTxtOrderPrice = (TextView)findViewById(R.id.write_order_txt_price);
        mTxtOrderState = (TextView)findViewById(R.id.write_order_txt_state);
        mBtnOrderSure = (Button)findViewById(R.id.write_order_btn_sure);
        mBtnOrderCancel = (Button)findViewById(R.id.write_order_btn_cancel);

        mRelativeLoading  = (RelativeLayout)findViewById(R.id.order_confirm_relative_loading);
        mRelativeError  = (RelativeLayout)findViewById(R.id.order_confirm_relative_error);


        mBtnConfirmSure.setOnClickListener(mOnClickListener);
        mBtnConfirmCancel.setOnClickListener(mOnClickListener);
        mBtnOrderSure.setOnClickListener(mOnClickListener);
        mBtnOrderCancel.setOnClickListener(mOnClickListener);

        mRelativeLoading.setVisibility(View.INVISIBLE);
        mRelativeError.setVisibility(View.INVISIBLE);
        mRelativeOrder.setVisibility(View.INVISIBLE);
        mLinearConfirm.setVisibility(View.VISIBLE);

        mPayHelper = new PayHelper(OrderActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPayHelper!=null){
            mPayHelper.cancelPayVerify();
        }

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        Glide.get(mContext).clearMemory();
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onDestroy");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(mPayHelper!=null){
            mOrderId = mPayHelper.getOrderId();
        }
        LogUtils.d(DetailConstant.TAG_DETAIL, TAG+"-->onActivityResult-->mOrderId:"+mOrderId);
        new KKPayClient.KKPayResult(requestCode,requestCode,data){

            @Override
            public void onPaySucceed(String s, String s1) {
                LogUtils.d(DetailConstant.TAG_DETAIL, TAG+"-->onActivityResult-->onPaySucceed-->s:"+s+"#s1:"+s1);
                mLinearConfirm.setVisibility(View.INVISIBLE);
                mRelativeOrder.setVisibility(View.INVISIBLE);
                mRelativeLoading.setVisibility(View.VISIBLE);
                mHandler.sendEmptyMessageAtTime(0,1000);
                reportBigData(1);
            }

            @Override
            public void onPayFailure(String s, String s1) {
                LogUtils.d(DetailConstant.TAG_DETAIL, TAG+"-->onActivityResult-->onPayFailure-->s:"+s+"#s1:"+s1);
                reportBigData(-1);
            }

            @Override
            public void onPayCancel(String s, String s1) {
                LogUtils.d(DetailConstant.TAG_DETAIL, TAG+"-->onActivityResult-->onPayCancel-->s:"+s+"#s1:"+s1);
                reportBigData(0);
            }


            //上报大数据
            private void reportBigData(int payState){
                if(mPayHelper!=null){
                    PayReturnInfo payReturnInfo = mPayHelper.getPayReturnInfo();
                    if(payReturnInfo!=null&&mOrderInfo!=null){
                        LogUtils.d(DetailConstant.TAG_DETAIL, TAG+"-->onActivityResult-->reportBigData-->"+payReturnInfo.toString());
                        BigDataHelper.getInstance().sendRouteUserOrder(CommonUtils.getCurrentDateString(),payReturnInfo.getOrderId(),
                                ""+payReturnInfo.getGoodsId(),payReturnInfo.getGoodsName(),""+payReturnInfo.getGoodsType(),
                                payReturnInfo.getPrice(),mOrderInfo.getDeparturePlace(),""+payState,mFormSource);
                    }

                }

            }
        };
        super.onActivityResult(requestCode, resultCode, data);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.order_confirm_btn_sure:
                    startPay();
                    break;

                case R.id.order_confirm_btn_cancel:
                    finish();
                    break;

                case R.id.write_order_btn_sure:
                    if(UserHelper.getInstance(mContext).getUserLogin()){
                        Intent intent = new Intent(OrderActivity.this, ManageOrderActivity.class);
                        startActivity(intent);
                    }else {
                        LogUtils.e(DetailConstant.TAG_DETAIL,TAG+"-->mOnClickListener nologin!!!");
                    }

                    break;

                case R.id.write_order_btn_cancel:
                    Intent intent1= new Intent(OrderActivity.this, MainActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//这样就会销毁OrderActivity和WriteActivity直接到MainActivity
                    startActivity(intent1);
                    break;

            }
        }
    };

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    HttpHelper.getInstance(mContext).getOrderInfoByID(mOrderId);

                    break;


                default:
                    break;
            }
        }
    };

    //调用康佳支付
    private void startPay(){
        PayInfoBean payInfoBean = new PayInfoBean();
        payInfoBean.setUserid(UserHelper.getInstance(OrderActivity.this).getOpenId());
        payInfoBean.setUsername(UserHelper.getInstance(OrderActivity.this).getUserName());
        payInfoBean.setUserorigin(0);
        payInfoBean.setDevicebrand(0);
        payInfoBean.setDeviceid(CommonUtils.getSerialNumber(OrderActivity.this));
        payInfoBean.setDeviceip("");
        payInfoBean.setGoodstype(0);//目前都是路线才会回调这个方法
        payInfoBean.setGoodsid(mOrderInfo.getGoodId());
        payInfoBean.setGoodsname(mOrderInfo.getGoodName());
        payInfoBean.setAmount(Integer.valueOf(mOrderInfo.getGoodNum()));
        payInfoBean.setPrice(mOrderInfo.getGoodPrice());
        payInfoBean.setName(mOrderInfo.getUserName());
        payInfoBean.setTel(mOrderInfo.getUserPhone());
        payInfoBean.setAddress(mOrderInfo.getDeparturePlace());

        mPayHelper.startKKpay(payInfoBean);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IEvent event) {
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onMessageEvent-->event:"+event.getClass().getSimpleName());

        if (event instanceof HttpErrorEvent) {
            String type = ((HttpErrorEvent) event).getReq_type();
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onMessageEvent-->HttpErrorEvent-->type:"+type);
            if("OrderInfoEvent".equals(type)){
                onError();
            }

        }
        if (event instanceof OrderInfoEvent) {
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onMessageEvent-->VideoDetailsEvent:"+((OrderInfoEvent) event).toString());
            OrderInfoEvent.DataBean orderInfo = ((OrderInfoEvent) event).getData();
            String orderId = orderInfo.getId();
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onMessageEvent-->mOrderId:"+mOrderId+"===orderId:"+orderId);
            if(mOrderId.equals(orderId)){
                String titleFormat = getResources().getString(R.string.write_order_title);
                mTxtOrderTitle.setText(String.format(titleFormat, mOrderId));
                mTxtOrderId.setText(mOrderId);
                loadImage(mImgThumb,NetworkUtils.toURL(orderInfo.getThumbnail()));
                mTxtOrderName.setText(orderInfo.getGoodsName());
                mTxtOrderCount.setText(""+orderInfo.getAmount());//要转换为字符串才行，不然会卡住
                mTxtOrderPrice.setText(CommonUtils.formatPrice(orderInfo.getPrice())+getString(R.string.write_good_price_yuan));

                int orderState = orderInfo.getState();
                if(orderState== Constant.ORDER_STATE_MAKE){
                    mTxtOrderState.setText(R.string.order_state_make);
                }else if(orderState==Constant.ORDER_STATE_PAY){
                    mTxtOrderState.setText(R.string.order_state_pay);
                }else if(orderState==Constant.ORDER_STATE_CONFIRM){
                    mTxtOrderState.setText(R.string.order_state_confirm);
                }else if(orderState==Constant.ORDER_STATE_DONE){
                    mTxtOrderState.setText(R.string.order_state_done);
                }
                mRelativeLoading.setVisibility(View.INVISIBLE);
                mRelativeError.setVisibility(View.INVISIBLE);
                mRelativeOrder.setVisibility(View.VISIBLE);
                mBtnOrderCancel.requestFocus();
            }else {
                LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onMessageEvent-->VideoDetailsEvent-->orderId no fit!!!");
                onError();
            }


        }

    }


    private void onError(){
        mLinearConfirm.setVisibility(View.GONE);
        mRelativeLoading.setVisibility(View.GONE);
        mRelativeOrder.setVisibility(View.GONE);
        mRelativeError.setVisibility(View.VISIBLE);
    }

    private void loadImage(RoundedImageView imageView, String url) {
        Glide.with(mContext)
                .load(NetworkUtils.toURL(url))
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .override(CommonUtils.dip2px(mContext, 128), CommonUtils.dip2px(mContext, 72))
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
