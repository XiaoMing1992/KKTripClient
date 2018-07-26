package com.konka.kktripclient.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.konka.kktripclient.R;
import com.konka.kktripclient.detail.DetailConstant;
import com.konka.kktripclient.layout.util.LaunchHelper;
import com.konka.kktripclient.net.HttpHelper;
import com.konka.kktripclient.net.info.HttpErrorEvent;
import com.konka.kktripclient.net.info.IEvent;
import com.konka.kktripclient.net.info.OrderInfoEvent;
import com.konka.kktripclient.net.info.PayReturnInfo;
import com.konka.kktripclient.pay.PayHelper;
import com.konka.kktripclient.ui.ToastView;
import com.konka.kktripclient.ui.popupwindow.DeleteOrderWindow;
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

public class ManageOrderItemActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = ManageOrderItemActivity.class.getSimpleName();
    private Context mContext;

    private TextView order_number;
    private ImageView order_make_state;
    private ImageView order_make_line;
    private TextView order_make_text;
    private ImageView order_pay_state;
    private ImageView order_pay_line;
    private TextView order_pay_text;
    private ImageView order_confirm_line;
    private ImageView order_confirm_state;
    private TextView order_confirm_text;
    private ImageView order_done_state;
    private TextView order_done_text;
    private RoundedImageView img;
    private TextView goods_name;
    private TextView detail_money;
    private TextView price;
    private TextView user_name;
    private TextView user_addr;
    private TextView user_phone;
    private LinearLayout layout_menu;
    private RelativeLayout order_layout;
    private Button continue_pay;
    private Button cancel;
    private LinearLayout layout_loading;
    private LinearLayout layout_load_fail;
    private RelativeLayout layout_content;

    private OrderInfoEvent.DataBean orderBean;
    private PayHelper mPayHelper;
    private String mOrderId = null;
    private ToastView toastView;
    private String req_order_id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_order_item_detail);
        mContext = ManageOrderItemActivity.this;
        initView();
        initData();
    }

    private void initView() {
        layout_content = (RelativeLayout) findViewById(R.id.layout_content);
        layout_loading = (LinearLayout) findViewById(R.id.layout_loading);
        layout_load_fail = (LinearLayout) findViewById(R.id.layout_load_fail);

        toastView = new ToastView(ManageOrderItemActivity.this);

        order_number = (TextView) findViewById(R.id.order_number);
        order_make_state = (ImageView) findViewById(R.id.order_make_state);
        order_make_line = (ImageView) findViewById(R.id.order_make_line);
        order_make_text = (TextView) findViewById(R.id.order_make_text);

        order_pay_state = (ImageView) findViewById(R.id.order_pay_state);
        order_pay_line = (ImageView) findViewById(R.id.order_pay_line);
        order_pay_text = (TextView) findViewById(R.id.order_pay_text);

        order_confirm_state = (ImageView) findViewById(R.id.order_confirm_state);
        order_confirm_line = (ImageView) findViewById(R.id.order_confirm_line);
        order_confirm_text = (TextView) findViewById(R.id.order_confirm_text);

        order_done_state = (ImageView) findViewById(R.id.order_done_state);
        order_done_text = (TextView) findViewById(R.id.order_done_text);

        img = (RoundedImageView) findViewById(R.id.img);
        goods_name = (TextView) findViewById(R.id.goods_name);
        detail_money = (TextView) findViewById(R.id.detail_money);
        price = (TextView) findViewById(R.id.price);
        user_name = (TextView) findViewById(R.id.user_name);
        user_addr = (TextView) findViewById(R.id.user_addr);
        user_phone = (TextView) findViewById(R.id.user_phone);
        layout_menu = (LinearLayout) findViewById(R.id.layout_menu);
        order_layout = (RelativeLayout) findViewById(R.id.order_layout);
        continue_pay = (Button) findViewById(R.id.continue_pay);
        cancel = (Button) findViewById(R.id.cancel);
        continue_pay.setOnClickListener(this);
        cancel.setOnClickListener(this);
        order_layout.setOnClickListener(this);


        continue_pay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    continue_pay.setTextColor(getResources().getColor(R.color.order_color2));
                    continue_pay.setAlpha(1.0f);
                    //continue_pay.setBackgroundColor(getResources().getColor(R.color.order_color3));
                } else {
                    continue_pay.setTextColor(getResources().getColor(R.color.order_color6));
                    continue_pay.setAlpha(0.8f);
                    // continue_pay.setBackgroundColor(getResources().getColor(R.color.order_color2));
                }
            }
        });

        cancel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    cancel.setTextColor(getResources().getColor(R.color.order_color2));
                    cancel.setAlpha(1.0f);
                    //cancel.setBackgroundColor(getResources().getColor(R.color.order_color3));
                } else {
                    cancel.setTextColor(getResources().getColor(R.color.order_color6));
                    cancel.setAlpha(0.8f);
                    //cancel.setBackgroundColor(getResources().getColor(R.color.order_color2));
                }
            }
        });

        mPayHelper = new PayHelper(this);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent == null)
            return;

        req_order_id = intent.getStringExtra("item_id");
        LogUtils.d(TAG, "order detail req_order_id = " + req_order_id);

        layout_loading.setVisibility(View.VISIBLE); //显示进度条
        HttpHelper.getInstance(mContext).getOrderInfoByID(req_order_id);
    }

    private void loadData() {
        //mHandler.sendEmptyMessage(LOAD_OK);
        layout_loading.setVisibility(View.GONE);
        if (orderBean != null) {
            int item_status = orderBean.getState();
            setStatus(item_status);
            price.setText("共计" + orderBean.getPrice() + "元");

            CommonUtils.downloadPicture(mContext, NetworkUtils.toURL(orderBean.getThumbnail()), R.drawable.default_image, R.drawable.default_image, img);

            goods_name.setText(orderBean.getGoodsName());
            order_number.setText("" + orderBean.getId());
            detail_money.setText("￥" + orderBean.getUnitPrice() + "x" + orderBean.getAmount());

            user_addr.setText(orderBean.getCpPrivateInfo().getAddress());
            user_name.setText(orderBean.getCpPrivateInfo().getName());
            user_phone.setText(orderBean.getCpPrivateInfo().getTel());

            layout_load_fail.setVisibility(View.GONE);
            layout_content.setVisibility(View.VISIBLE);
        }else{
            layout_load_fail.setVisibility(View.VISIBLE);
        }
    }

    private void setStatus(int status) {
        switch (status) {
            case Constant.ORDER_STATE_MAKE:
                order_make_state.setImageResource(R.drawable.manager_order_done);
                order_make_line.setBackgroundResource(R.drawable.manager_order_done_line);
                order_make_text.setAlpha(1.0f);

                layout_menu.setVisibility(View.VISIBLE);
                continue_pay.requestFocus();
                continue_pay.setTextColor(getResources().getColor(R.color.order_color2));
                continue_pay.setAlpha(1.0f);
                break;

            case Constant.ORDER_STATE_PAY:
                order_make_state.setImageResource(R.drawable.manager_order_done);
                order_make_line.setBackgroundResource(R.drawable.manager_order_done_line);
                order_make_text.setAlpha(1.0f);
                order_pay_state.setImageResource(R.drawable.manager_order_done);
                order_pay_line.setBackgroundResource(R.drawable.manager_order_done_line);
                order_pay_text.setAlpha(1.0f);
                layout_menu.setVisibility(View.GONE);
                break;

            case Constant.ORDER_STATE_CONFIRM:
                order_make_state.setImageResource(R.drawable.manager_order_done);
                order_make_line.setBackgroundResource(R.drawable.manager_order_done_line);
                order_make_text.setAlpha(1.0f);
                order_pay_state.setImageResource(R.drawable.manager_order_done);
                order_pay_line.setBackgroundResource(R.drawable.manager_order_done_line);
                order_pay_text.setAlpha(1.0f);
                order_confirm_state.setImageResource(R.drawable.manager_order_done);
                order_confirm_line.setBackgroundResource(R.drawable.manager_order_done_line);
                order_confirm_text.setAlpha(1.0f);
                layout_menu.setVisibility(View.GONE);
                break;

            case Constant.ORDER_STATE_DONE:
                order_make_state.setImageResource(R.drawable.manager_order_done);
                order_make_line.setBackgroundResource(R.drawable.manager_order_done_line);
                order_make_text.setAlpha(1.0f);
                order_pay_state.setImageResource(R.drawable.manager_order_done);
                order_pay_line.setBackgroundResource(R.drawable.manager_order_done_line);
                order_pay_text.setAlpha(1.0f);
                order_confirm_state.setImageResource(R.drawable.manager_order_done);
                order_confirm_line.setBackgroundResource(R.drawable.manager_order_done_line);
                order_confirm_text.setAlpha(1.0f);
                order_done_state.setImageResource(R.drawable.manager_order_done);
                order_done_text.setAlpha(1.0f);
                layout_menu.setVisibility(View.GONE);
                break;
        }
        if (Constant.MONKEY) {
            TextView textView = (TextView) findViewById(R.id.auto_test);
            textView.setText(String.valueOf(status));
            textView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mPayHelper != null) {
            mOrderId = mPayHelper.getOrderId();
        }
        LogUtils.d(TAG, "onActivityResult-->mOrderId:" + mOrderId);
        new KKPayClient.KKPayResult(requestCode, requestCode, data) {

            @Override
            public void onPaySucceed(String s, String s1) {
                LogUtils.d(TAG, "onActivityResult-->onPaySucceed-->s:" + s + "#s1:" + s1);
                HttpHelper.getInstance(mContext).getOrderInfoByID(mOrderId);
                reportBigData(1);
            }

            @Override
            public void onPayFailure(String s, String s1) {
                LogUtils.d(TAG, "onActivityResult-->onPayFailure-->s:" + s + "#s1:" + s1);
                reportBigData(-1);
            }

            @Override
            public void onPayCancel(String s, String s1) {
                LogUtils.d(TAG, "onActivityResult-->onPayCancel-->s:" + s + "#s1:" + s1);
                reportBigData(0);
            }

            //上报大数据
            private void reportBigData(int payState) {
                if (mPayHelper != null) {
                    PayReturnInfo payReturnInfo = mPayHelper.getPayReturnInfo();
                    if (payReturnInfo != null && orderBean != null && orderBean.getCpPrivateInfo() != null) {
                        LogUtils.d(DetailConstant.TAG_DETAIL, TAG + "-->onActivityResult-->reportBigData-->" + payReturnInfo.toString());
                        JSONObject source = new JSONObject();
                        source.put(Constant.BIG_DATA_VALUE_KEY_SOURCE, Constant.BIG_DATA_VALUE_SOURCE_DD);
                        source.put(Constant.BIG_DATA_VALUE_KEY_NAME, "-1");
                        source.put(Constant.BIG_DATA_VALUE_KEY_LOCATION, "-1");
                        BigDataHelper.getInstance().sendRouteUserOrder(CommonUtils.getCurrentDateString(), payReturnInfo.getOrderId(),
                                String.valueOf(payReturnInfo.getGoodsId()), payReturnInfo.getGoodsName(), String.valueOf(payReturnInfo.getGoodsType()),
                                payReturnInfo.getPrice(), orderBean.getCpPrivateInfo().getAddress(), String.valueOf(payState), source.toJSONString());
                    }
                }
            }
        };
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order_layout:
                JSONObject source = new JSONObject();
                source.put(Constant.BIG_DATA_VALUE_KEY_SOURCE, Constant.BIG_DATA_VALUE_SOURCE_DD);
                source.put(Constant.BIG_DATA_VALUE_KEY_NAME, "-1");
                source.put(Constant.BIG_DATA_VALUE_KEY_LOCATION, "-1");
                if(orderBean != null) {
                    LaunchHelper.startDetailActivity(mContext, orderBean.getGoodsType(), orderBean.getGoodsId(), source.toJSONString());
                    LogUtils.d(TAG, "type = " + orderBean.getGoodsType());
                    LogUtils.d(TAG, "goodsId = " + orderBean.getGoodsId());
                }
                break;

            case R.id.continue_pay:
                if (mPayHelper != null)
                    mPayHelper.startPayByOrderId(req_order_id);
                break;

            case R.id.cancel:
                showDialog();
                break;
        }
    }

    private void showDialog() {
        DeleteOrderWindow window = new DeleteOrderWindow(mContext);
        if (!window.isShowing())
            window.show();
        window.setListener(iDeleteOrder);
    }

    IDeleteOrder iDeleteOrder = new IDeleteOrder() {
        @Override
        public void doDelete() {
            if(orderBean != null) {
                //请求服务器删除订单
                LogUtils.d(TAG, "order_id = " + orderBean.getId());

                OrderConstant.OPERATE_TYPE = OrderConstant.CANCEL_ORDER;
                boolean is_ok = HttpHelper.getInstance(mContext).doDelete("" + orderBean.getId());
                LogUtils.d(TAG, "iDeleteOrder is_ok = " + is_ok);
                if (!is_ok) {
                    if (toastView == null)
                        toastView = new ToastView(ManageOrderItemActivity.this);
                    toastView.setText("取消订单失败");
                    toastView.show();
                } else {
                    finish();
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
            LogUtils.d(TAG, "register eventBus");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPayHelper != null) {
            mPayHelper.cancelPayVerify();
        }

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
            LogUtils.d(TAG, "unregister eventBus");
        }

    }

    private final int PAY_SUCCEESS = 0x00;
    private final int LOAD_OK = 0x01;
    private final int LOAD_FAIL = 0x02;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PAY_SUCCEESS:
                    setStatus(Constant.ORDER_STATE_PAY);//刷新界面
                    OrderConstant.IS_REFRESH = true;
                    break;

                case LOAD_OK:
                    layout_loading.setVisibility(View.GONE);
                    layout_load_fail.setVisibility(View.GONE);
                    break;

                case LOAD_FAIL:
                    layout_loading.setVisibility(View.GONE);
                    layout_load_fail.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(IEvent event) {
        if (event instanceof OrderInfoEvent) {
            LogUtils.d(TAG, "OrderInfoEvent = " + ((OrderInfoEvent) event).getRet().getRet_msg());
            if (((OrderInfoEvent) event).getRet().getRet_code() != null && ((OrderInfoEvent) event).getRet().getRet_code().equals("0")) {
                //OrderInfoEvent.DataBean orderInfo = ((OrderInfoEvent) event).getData();
                orderBean = ((OrderInfoEvent) event).getData();
                if (orderBean != null) {
                    String orderId = orderBean.getId();
                    LogUtils.d(TAG, "orderId = " + orderId);
                    if (mOrderId != null && mOrderId.equals(orderId)) {
                        int orderState = orderBean.getState();
                        LogUtils.d(TAG, "orderState = " + orderState);
                        if (orderState == Constant.ORDER_STATE_PAY) {
                            mHandler.sendEmptyMessage(PAY_SUCCEESS);
                        }
                    } else
                        loadData();
                }
            }
        } else if (event instanceof HttpErrorEvent) {
            String type = ((HttpErrorEvent) event).getReq_type();
            LogUtils.d(TAG, "HttpErrorEvent type = " + type);
            if ("OrderInfoEvent".equals(type)) {
                LogUtils.d(TAG, "HttpErrorEvent = " + ((HttpErrorEvent) event).getRetCode() + " " + ((HttpErrorEvent) event).getRetMsg());
                if (((HttpErrorEvent) event).getRetCode() != null && ((HttpErrorEvent) event).getRetCode().equals("IE457003")) {
                    mHandler.sendEmptyMessage(LOAD_FAIL);
                }
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (orderBean.getState() == Constant.ORDER_STATE_OUT) {
            OrderConstant.IS_REFRESH = true;
            OrderConstant.OEFRESH_TYPE = 0;
            LogUtils.d(TAG, "OrderConstant.OEFRESH_TYPE = " + OrderConstant.OEFRESH_TYPE);
        }
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
