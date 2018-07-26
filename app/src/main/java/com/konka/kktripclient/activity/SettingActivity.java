package com.konka.kktripclient.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.konka.account.callback.CallBack;
import com.konka.account.wrapper.UUCWrapper;
import com.konka.kktripclient.R;
import com.konka.kktripclient.layout.view.LoginStatusListener;
import com.konka.kktripclient.pay.UserHelper;
import com.konka.kktripclient.ui.popupwindow.AboutInfoWindow;
import com.konka.kktripclient.ui.popupwindow.AccountWindow;
import com.konka.kktripclient.ui.popupwindow.LoginWindow;
import com.konka.kktripclient.ui.popupwindow.UpgradeWindow;
import com.konka.kktripclient.utils.BigDataHelper;
import com.konka.kktripclient.utils.CommonUtils;
import com.konka.kktripclient.utils.Constant;
import com.konka.kktripclient.utils.LogUtils;
import com.konka.kktripclient.utils.NetworkUtils;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener, LoginStatusListener, View.OnFocusChangeListener{
    private final String TAG = "SettingActivity";

    private RelativeLayout setting_login;
    private RelativeLayout setting_version;
    private RelativeLayout setting_info;

    private AboutInfoWindow mAboutInfoWindow;
    private LoginWindow mLoginWindow;
    private UpgradeWindow mUpgradeWindow;
    private AccountWindow mAccountWindow;
    private Context mContext;

    private TextView  mUserStatus;

    private TextView setting_user_name;
    private TextView setting_user_status;
    private ImageView img_setting_login;

    private TextView tv_setting_version;
    private TextView setting_update_status;
    private ImageView img_setting_version;

    private TextView tv_setting_about;
    private TextView setting_info_hint;
    private ImageView img_setting_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_page_setting);
        mContext = SettingActivity.this;
        initView();
        initData();
        initListener();
    }

    private void initView(){
        setting_login = (RelativeLayout)findViewById(R.id.setting_login);
        setting_version = (RelativeLayout)findViewById(R.id.setting_version);
        setting_info = (RelativeLayout)findViewById(R.id.setting_about);
        mUserStatus = (TextView)findViewById(R.id.setting_user_status);

        setting_user_name = (TextView)findViewById(R.id.setting_user_name);
        setting_user_status = (TextView)findViewById(R.id.setting_user_status);
        img_setting_login = (ImageView)findViewById(R.id.img_setting_login);

        tv_setting_version = (TextView)findViewById(R.id.tv_setting_version);
        setting_update_status = (TextView)findViewById(R.id.setting_update_status);
        img_setting_version = (ImageView)findViewById(R.id.img_setting_version);

        tv_setting_about = (TextView)findViewById(R.id.tv_setting_about);
        setting_info_hint = (TextView)findViewById(R.id.setting_info_hint);
        img_setting_about = (ImageView)findViewById(R.id.img_setting_about);
    }

    private void initListener(){
        setting_login.setOnClickListener(this);
        setting_version.setOnClickListener(this);
        setting_info.setOnClickListener(this);

        setting_login.setOnFocusChangeListener(this);
        setting_version.setOnFocusChangeListener(this);
        setting_info.setOnFocusChangeListener(this);

/*        setting_login_unfocus.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){

                }else{

                }
            }
        });*/
    }

    private void initData(){
        //updateLoginState(Constant.isUserLogin);
        updateLoginState(UserHelper.getInstance(mContext).getUserLogin());
    }

    private void updateLoginState(boolean isUserLogin) {
        if (isUserLogin) {
            //mUserStatus.setText(Constant.sNickName);
            mUserStatus.setText(UserHelper.getInstance(mContext).getNickName());
        } else {
            mUserStatus.setText(R.string.setting_click_login);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.setting_login:
                if (NetworkUtils.isNetworkConnected(this)) {
                    //if (Constant.isUserLogin){
                    if (UserHelper.getInstance(mContext).getUserLogin()){
                        if (mAccountWindow == null) {
                            mAccountWindow = new AccountWindow(this);
                        }
                        mAccountWindow.registerLoginStatusListener(this);
                        mAccountWindow.show();
                        processAccountQRCodeInfo();
                    }else {
                        if (mLoginWindow == null) {
                            LogUtils.d("Setting", "Setting-->mLoginWindow is null");
                            mLoginWindow = new LoginWindow(this);
                        }else{
                            LogUtils.d("Setting", "Setting-->mLoginWindow is not null");
                        }
                        mLoginWindow.registerLoginListener("设置页");
                        mLoginWindow.show();
                        //processLoginQRCodeInfo();
                        getLoginQRCodeUrl();
                    }
                }else {
                    //ToastUtil.show(this, this.getResources().getString(R.string.network_execption_hint));
                    //ToastUtil.getInstance(mContext).setToastContent(this.getResources().getString(R.string.network_execption_hint));
                    Toast.makeText(mContext, this.getResources().getString(R.string.network_exception_hint), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.setting_version:
                if (!checkUpdate()) {
                    if (mUpgradeWindow == null) {
                        mUpgradeWindow = new UpgradeWindow(this);
                    }
                    mUpgradeWindow.show();
                }else {
                    //ToastUtil.show(mContext, this.getResources().getString(R.string.setting_newest_version));
                    //ToastUtil.getInstance(mContext).setToastContent(this.getResources().getString(R.string.setting_newest_version));
                    Toast.makeText(mContext, this.getResources().getString(R.string.setting_newest_version), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.setting_about:
                if (mAboutInfoWindow == null) {
                    mAboutInfoWindow = new AboutInfoWindow(this);
                }
                mAboutInfoWindow.show();
                break;

            default:
                break;
        }
    }

    private void processLoginQRCodeInfo() {
        UUCWrapper.getInstance(this).getLoginQRCodeUrl(new CallBack<String>() {
            @Override
            public void onComplete(String url) {
                //Trace.Debug("getLoginQRCodeUrl onComplete: " + url);
                LogUtils.d(TAG, "getLoginQRCodeUrl onComplete: " + url);
                mLoginWindow.refreshQRCode(url);
            }

            @Override
            public void onError(String s) {
                //Trace.Debug("getLoginQRCodeUrl onError: " + s);
                LogUtils.d(TAG, "getLoginQRCodeUrl onError: " + s);
            }
        });
    }

    //获取登陆二维码
    public void getLoginQRCodeUrl() {
        UUCWrapper.getInstance(mContext).getLoginQRCodeUrl(new CallBack<String>() {
            @Override
            public void onComplete(String url) {
                LogUtils.d(TAG, "getLoginQRCodeUrl onComplete: " + url);
                mLoginWindow.refreshQRCode(url);
            }

            @Override
            public void onError(String s) {

            }
        });
    }

    private boolean checkUpdate(){

        return false;
    }

    @Override
    public void updateUserStatus(boolean isLogin, String username, String openId, String nickname) {
        LogUtils.d(TAG, "updateUserStatus isLogin = "+isLogin);

        UserHelper.getInstance(mContext).setUserLogin(isLogin);
        if(UserHelper.getInstance(mContext).getUserLogin()){
            mUserStatus.setText(nickname);
            UserHelper.getInstance(mContext).setOpenId(openId);
            UserHelper.getInstance(mContext).setUserName(username);
            UserHelper.getInstance(mContext).setNickName(nickname);

        }else {
            mUserStatus.setText(R.string.setting_click_login);
            UserHelper.getInstance(mContext).setOpenId(null);
            UserHelper.getInstance(mContext).setUserName(null);
            UserHelper.getInstance(mContext).setNickName(null);
        }
    }

    //获得并刷新修改用户弹窗二维码
    private void processAccountQRCodeInfo() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoginWindow != null) {
            mLoginWindow.unregisterListener();
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()){
            case R.id.setting_login:
                if (b){
                    img_setting_login.setImageResource(R.drawable.setting_login_focus);
                    setting_user_name.setTextColor(getResources().getColor(R.color.white));
                    setting_user_status.setTextColor(getResources().getColor(R.color.white));
                }else{
                    img_setting_login.setImageResource(R.drawable.setting_login_unfocus);
                    setting_user_name.setTextColor(getResources().getColor(R.color.other));
                    setting_user_status.setTextColor(getResources().getColor(R.color.other));
                }
                break;
            case R.id.setting_version:
                if (b){
                    img_setting_version.setImageResource(R.drawable.setting_version_focus);
                    tv_setting_version.setTextColor(getResources().getColor(R.color.white));
                    setting_update_status.setTextColor(getResources().getColor(R.color.white));
                }else{
                    img_setting_version.setImageResource(R.drawable.setting_version_unfocus);
                    tv_setting_version.setTextColor(getResources().getColor(R.color.other));
                    setting_update_status.setTextColor(getResources().getColor(R.color.other));
                }
                break;

            case R.id.setting_about:
                if (b){
                    img_setting_about.setImageResource(R.drawable.setting_about_focus);
                    tv_setting_about.setTextColor(getResources().getColor(R.color.white));
                    setting_info_hint.setTextColor(getResources().getColor(R.color.white));
                }else{
                    img_setting_about.setImageResource(R.drawable.setting_about_unfocus);
                    tv_setting_about.setTextColor(getResources().getColor(R.color.other));
                    setting_info_hint.setTextColor(getResources().getColor(R.color.other));
                }
                break;
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
