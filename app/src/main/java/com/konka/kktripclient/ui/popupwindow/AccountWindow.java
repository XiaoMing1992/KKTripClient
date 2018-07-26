package com.konka.kktripclient.ui.popupwindow;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.konka.account.callback.CallBack;
import com.konka.account.wrapper.UUCWrapper;
import com.konka.kktripclient.R;
import com.konka.kktripclient.layout.view.LoginStatusListener;
import com.konka.kktripclient.pay.UserHelper;
import com.konka.kktripclient.utils.CommonUtils;
import com.konka.kktripclient.utils.Constant;
import com.konka.kktripclient.utils.LogUtils;

/**
 *
 */

public class AccountWindow extends PopupWindow implements View.OnClickListener {
    private final String TAG = "AccountWindow";
    private Context mContext;
    private View mView;
    private Button mLogoutButton;
    private TextView account_desc;
    private LoginStatusListener mListener;

    public AccountWindow(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        mView = LayoutInflater.from(mContext).inflate(R.layout.account_window, null);
        setContentView(mView);
        mLogoutButton = (Button) mView.findViewById(R.id.account_logout);
        account_desc = (TextView) mView.findViewById(R.id.account_desc);
        mLogoutButton.setOnClickListener(this);

        CommonUtils.computeScreenSize(context);
        LogUtils.d(TAG, "ScreenWidth = " + CommonUtils.getScreenWidth() + " ScreenHeight = " + CommonUtils.getScreenHeight());
        setWidth(CommonUtils.getScreenWidth());
        setHeight(CommonUtils.getScreenHeight());
        setFocusable(true);
        setBackgroundDrawable(mContext.getResources().getDrawable(R.color.transparent));
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                cancelLoginStatusListener();
            }
        });
        initData();
    }

    private void initData() {
        account_desc.setText("当前账号为" + UserHelper.getInstance(mContext).getNickName() + ",");
    }

    public void show() {
        showAtLocation(mView, Gravity.CENTER, 0, 0);
    }

    public void registerLoginStatusListener(LoginStatusListener listener) {
        mListener = listener;
    }

    private void cancelLoginStatusListener() {
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_logout:
                if (!Constant.MONKEY) {
                    logOut();
                }
                break;
            default:
                break;
        }
    }

    //退出登陆
    public void logOut() {
        UUCWrapper.getInstance(mContext).logout(new CallBack<String>() {
            @Override
            public void onComplete(String s) {
                UserHelper.getInstance(mContext).setOpenId(null);
                UserHelper.getInstance(mContext).setUserName(null);
                UserHelper.getInstance(mContext).setNickName(null);
                UserHelper.getInstance(mContext).setUserLogin(false);

                LogUtils.d(TAG, "logout");
                if (mListener != null) {
                    mListener.updateUserStatus(false, null, null, null);
                }
                dismiss();
            }

            @Override
            public void onError(String s) {
                LogUtils.d(TAG, "User Logout Failed");
                Toast.makeText(mContext, R.string.account_logout_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
