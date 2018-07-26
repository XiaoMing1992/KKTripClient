package com.konka.kktripclient.layout.view;

/**
 * Created by DeppJay on 2016-12-15.
 */

public interface LoginStatusListener {
    void updateUserStatus(boolean isLogin, String username, String openId, String nickname);
}
