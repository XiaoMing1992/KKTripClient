package com.konka.kktripclient.pay;

import android.content.Context;

import com.konka.account.callback.CallBack;
import com.konka.account.data.UserInfo;
import com.konka.account.wrapper.UUCWrapper;
import com.konka.kktripclient.utils.LogUtils;

/**
 * Created by smith on 2017/05/15.
 */
public class UserHelper {
    private static String TAG = "UserHelper";
    Context mContext;
    private UserHelper(Context context) {
        mContext = context;
    }


    private static volatile UserHelper instance;

    public static UserHelper getInstance(Context context) {

        if(instance == null) {
            synchronized (UserHelper.class) {
                if(instance == null) {
                    instance = new UserHelper(context);
                }
            }
        }
        return instance;
    }


    //是否登陆
    public void isUserLogin() {

        UUCWrapper.getInstance(mContext).isUserLogin(new CallBack<Boolean>() {
            @Override
            public void onComplete(Boolean aBoolean) {
                //Constant.isUserLogin = aBoolean;
                UserHelper.getInstance(mContext).setUserLogin(aBoolean);
                if(aBoolean) {
                    getUserInfo();
                }
            }

            @Override
            public void onError(String s) {

            }
        });
    }

    //获取用户基本信息
    public void getUserInfo() {
        UUCWrapper.getInstance(mContext).getUserInfo(new CallBack<UserInfo>() {
            @Override
            public void onComplete(UserInfo userInfo) {
                if (userInfo != null) {
/*                    Constant.sOpenId = userInfo.getOpenId();
                    Constant.sUserName = userInfo.getUserName();
                    Constant.sNickName = userInfo.getNickname();*/

                    UserHelper.getInstance(mContext).setOpenId(userInfo.getOpenId());
                    UserHelper.getInstance(mContext).setUserName(userInfo.getUserName());
                    UserHelper.getInstance(mContext).setNickName(userInfo.getNickname());

                    //PayInfoHelper.getInstance().getPayInfo(getApplicationContext(), Constant.sOpenId,true);
                }
            }

            @Override
            public void onError(String s) {
                LogUtils.d(TAG,"fail "+s);
            }
        });
    }



    //监听扫码状态
    public void setUserLoginListener() {
        UUCWrapper.getInstance(mContext).setUserLoginListener(new CallBack<String>() {
            @Override
            public void onComplete(String s) {
                LogUtils.d(TAG,"SCAN = "+s);
            }

            @Override
            public void onError(String s) {

            }
        });
    }

    //退出登陆
    public void logOut() {
        UUCWrapper.getInstance(mContext).logout(new CallBack<String>() {
            @Override
            public void onComplete(String s) {
/*                Constant.isUserLogin = false;
                Constant.sOpenId = null;
                Constant.sUserName = null;
                Constant.sNickName = null;*/

                UserHelper.getInstance(mContext).setOpenId(null);
                UserHelper.getInstance(mContext).setUserName(null);
                UserHelper.getInstance(mContext).setNickName(null);
                UserHelper.getInstance(mContext).setUserLogin(false);
                LogUtils.d(TAG,"logout");
            }

            @Override
            public void onError(String s) {

            }
        });
    }

    private String sOpenId = null;
    private String sUserName = null;
    private String sNickName = null;
    private int sUserOrgin = 0;
    //当前用户登录状态标记
    public boolean isUserLogin = false;

    public void setOpenId(String sOpenId) {
        this.sOpenId = sOpenId;
    }
    public String getOpenId() {
        return sOpenId;
    }

    public void setNickName(String sNickName) {
        this.sNickName = sNickName;
    }

    public String getNickName() {
        return sNickName;
    }

    public void setUserName(String sUserName) {
        this.sUserName = sUserName;
    }

    public String getUserName() {
        return sUserName;
    }

    public void setUserLogin(boolean userLogin) {
        isUserLogin = userLogin;
    }

    public boolean getUserLogin(){
        return isUserLogin;
    }

    public void setsUserOrgin(int sUserOrgin) {
        this.sUserOrgin = sUserOrgin;
    }

    public int getsUserOrgin() {
        return sUserOrgin;
    }
}
