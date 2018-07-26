package com.konka.kktripclient.ui.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.konka.account.callback.CallBack;
import com.konka.account.data.UserInfo;
import com.konka.account.wrapper.UUCWrapper;
import com.konka.kktripclient.R;
import com.konka.kktripclient.activity.IUpdateCollect;
import com.konka.kktripclient.detail.DetailActivity;
import com.konka.kktripclient.layout.view.LoginStatusListener;
import com.konka.kktripclient.pay.UserHelper;
import com.konka.kktripclient.ui.ToastView;
import com.konka.kktripclient.utils.BigDataHelper;
import com.konka.kktripclient.utils.CommonUtils;
import com.konka.kktripclient.utils.Constant;
import com.konka.kktripclient.utils.LogUtils;
import com.konka.kktripclient.utils.QRCodeHelper;

/**
 *
 */

public class LoginWindow extends PopupWindow {
    private final String TAG = LoginWindow.class.getSimpleName();

    private Context mContext;
    private View mView;
    private ImageView mQRCodeView;
    private Bitmap mQRCodeBitmap;
    private LoginStatusListener mListener;
    private LinearLayout mScanLayout;
    private ImageView mLoadingIcon;

    private TextView tip_success;

    public LoginWindow(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        toastView = new ToastView(mContext);
        mView = LayoutInflater.from(mContext).inflate(R.layout.login_window, null);
        setContentView(mView);
        mQRCodeView = (ImageView) mView.findViewById(R.id.login_qrcode);
        mScanLayout = (LinearLayout) mView.findViewById(R.id.scan_layout);
        mLoadingIcon = (ImageView) mView.findViewById(R.id.login_loading_icon);
        tip_success = (TextView) mView.findViewById(R.id.tip_success);

        CommonUtils.computeScreenSize(context);
        LogUtils.d(TAG, "ScreenWidth = "+CommonUtils.getScreenWidth()+" ScreenHeight = "+CommonUtils.getScreenHeight());
        setWidth(CommonUtils.getScreenWidth());
        setHeight(CommonUtils.getScreenHeight());
        setFocusable(true);
        setBackgroundDrawable(mContext.getResources().getDrawable(R.color.transparent));//避免出现悬浮

        //registerLoginListener(null);
        if (mContext instanceof LoginStatusListener) {
            registerLoginStatusListener((LoginStatusListener) mContext);
        }
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                mQRCodeView.setImageBitmap(null);
                free(mQRCodeBitmap);
            }
        });
    }

    private void registerLoginStatusListener(LoginStatusListener listener) {
        mListener = listener;
    }

    public void unregisterListener() {
        UUCWrapper.getInstance(mContext.getApplicationContext()).cancelUserLoginListener();
        mListener = null;
    }

    @Override
    public void dismiss() {
        if (mContext !=null) {
            if (mContext instanceof Activity) {
                if(!((Activity) mContext).isFinishing()){
                    super.dismiss();
                    LogUtils.d(TAG, "---> dismiss()");
                    //unregisterListener();
                    UUCWrapper.getInstance(mContext.getApplicationContext()).cancelUserLoginListener();
                    mScanLayout.setVisibility(View.GONE);
                    tip_success.setVisibility(View.GONE);
                    if (mContext instanceof DetailActivity) {
                        iUpdateCollect.exitWindow();
                    }
                }
            }
        }
    }

    public void registerLoginListener(final String source) {
        UUCWrapper.getInstance(mContext.getApplicationContext()).setUserLoginListener(new CallBack<String>() {
            @Override
            public void onComplete(String s) {
                LogUtils.d(TAG, "setUserLoginListener onComplete: " + s);
                if (s == null){
                    LogUtils.d(TAG, "fail");
                    //刷新界面
                    refreshUI(RefreshType.TIME_OUT);
                }

                if (!TextUtils.isEmpty(s)) {
                    if (s.equals(Constant.SCAN_QRCODE)) {
                        //正在扫码
                        LogUtils.d(TAG, "scan_ok");
                        //刷新扫码界面
                        refreshUI(RefreshType.SCAN);
                    }
                    //登录成功
                    if (s.equals(Constant.LOGIN)) {
                        String login_time =CommonUtils.getCurrentDateString();
                        LogUtils.d(TAG, "login_time is "+login_time);
                        LogUtils.d(TAG, "source is "+source);
                        BigDataHelper.getInstance().sendLoginUserCenter(login_time, source, "成功");

                        LogUtils.d(TAG, "login");
                        UserHelper.getInstance(mContext.getApplicationContext()).setUserLogin(true);
                        getUserInfo();
                        //刷新界面
                        refreshUI(RefreshType.LOGIN);
                    }
                }
            }

            @Override
            public void onError(String s) {
                LogUtils.d(TAG, "setUserLoginListener onError: " + s);
                BigDataHelper.getInstance().sendLoginUserCenter(CommonUtils.getCurrentDateString(), source, "失败");
            }
        });

    }

    private void refreshUI(RefreshType type) {
        switch (type) {
            case SCAN:
                LogUtils.d(TAG, "SCAN");
                tip_success.setVisibility(View.VISIBLE);
                break;

            case LOGIN:
                LogUtils.d(TAG, "LOGIN");
                mScanLayout.setVisibility(View.GONE);
                tip_success.setVisibility(View.GONE);

                if (LoginWindow.this.isShowing()){
                    dismiss();
                    LogUtils.d(TAG, "dismiss");
                }else{
                    LogUtils.d(TAG, "has dismissed");
                    //dismiss();
                }
                break;

            case TIME_OUT:
                mScanLayout.setVisibility(View.GONE);
                tip_success.setVisibility(View.GONE);
                if (LoginWindow.this.isShowing()) {
                    dismiss();
                }
                break;

            default:
                break;
        }
    }

    private IUpdateCollect iUpdateCollect;
    public void setIUpdateCollect(IUpdateCollect iUpdateCollect){
        this.iUpdateCollect = iUpdateCollect;
    }

    private enum RefreshType{
        SCAN,
        LOGIN,
        TIME_OUT
    }

    //获取用户基本信息
    public void getUserInfo() {
        UUCWrapper.getInstance(mContext.getApplicationContext()).getUserInfo(new CallBack<UserInfo>() {
            @Override
            public void onComplete(UserInfo userInfo) {
                if (userInfo != null) {
                    UserHelper.getInstance(mContext.getApplicationContext()).setOpenId(userInfo.getOpenId());
                    UserHelper.getInstance(mContext.getApplicationContext()).setUserName(userInfo.getUserName());
                    UserHelper.getInstance(mContext.getApplicationContext()).setNickName(userInfo.getNickname());

                    LogUtils.d(TAG, "mListener "+mListener);
                    //同步界面
                    if (mListener != null) {
                        mListener.updateUserStatus(true, userInfo.getUserName(), userInfo.getOpenId(), userInfo.getNickname());
                    }
                    if (mContext instanceof DetailActivity) {
                        LogUtils.d(TAG, "update collect state");
                        iUpdateCollect.updateState();
                    }
                }
            }

            @Override
            public void onError(String s) {
                LogUtils.d(TAG,"fail "+s);
            }
        });
    }

    private ToastView toastView;
    public void refreshQRCode(String QRCodeUrl) {
        resetUI();
        hideLoading();
        if (!TextUtils.isEmpty(QRCodeUrl)) {
            mQRCodeBitmap = QRCodeHelper.createQRCode(QRCodeUrl, mQRCodeView.getWidth());
            mQRCodeView.setImageBitmap(mQRCodeBitmap);
        } else {
            if (toastView == null)
                toastView = new ToastView(mContext);
            toastView.setText(R.string.login_url_fail);
            toastView.show();
        }
    }


    private void resetUI() {
        mScanLayout.setVisibility(View.VISIBLE);
        tip_success.setVisibility(View.GONE);
    }

    public void show() {
        try{
            showAtLocation(mView, Gravity.CENTER, 0, 0);
            showLoading();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void showLoading() {
        mLoadingIcon.setVisibility(View.VISIBLE);
        mLoadingIcon.setAnimation(rotateAnimation());
    }

    private void hideLoading() {
        mLoadingIcon.setVisibility(View.GONE);
        mLoadingIcon.clearAnimation();
    }

    private RotateAnimation rotateAnimation() {
        RotateAnimation animation = new RotateAnimation(0f, 359f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator interpolator = new LinearInterpolator();
        animation.setDuration(1500);
        animation.setRepeatCount(-1);
        animation.setInterpolator(interpolator);
        return animation;
    }

    private void free(Bitmap bp) {
        if (bp != null) {
            try {
                if (!bp.isRecycled()) {
                    bp.recycle();
                    bp = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
