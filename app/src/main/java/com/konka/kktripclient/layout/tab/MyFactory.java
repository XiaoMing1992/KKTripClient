package com.konka.kktripclient.layout.tab;

import android.content.Intent;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.konka.account.callback.CallBack;
import com.konka.account.wrapper.UUCWrapper;
import com.konka.kktripclient.R;
import com.konka.kktripclient.activity.CollectActivity;
import com.konka.kktripclient.activity.ManageOrderActivity;
import com.konka.kktripclient.activity.SettingActivity;
import com.konka.kktripclient.layout.util.CommonFunction;
import com.konka.kktripclient.layout.view.AnimateFocusChangeListener;
import com.konka.kktripclient.layout.view.HorizontalPageScrollView;
import com.konka.kktripclient.layout.view.OverViewFrameLayout;
import com.konka.kktripclient.layout.tab.base.TabContent;
import com.konka.kktripclient.layout.util.ActivityHandler;
import com.konka.kktripclient.pay.UserHelper;
import com.konka.kktripclient.ui.popupwindow.LoginWindow;
import com.konka.kktripclient.utils.LogUtils;

/**
 * Created by GXY on 2016-11-14.
 * <p>
 * 我的页面
 */

public class MyFactory {
    private static MyFactory mMyFactory;
    private String mTabName;
    private RecommendTabButton mMyTabButton;
    private MyPage mMyPage;

    private LoginWindow mLoginWindow;

    public static MyFactory getInstance() {
        if (mMyFactory == null) mMyFactory = new MyFactory();
        return mMyFactory;
    }

    public void initView() {
        try {
            mTabName = ActivityHandler.getInstance().getString(R.string.my_name);
            mMyTabButton = new RecommendTabButton(mTabName, "", false, true);
            mMyPage = new MyPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RecommendTabButton getMyTabButton() {
        return mMyTabButton;
    }

    public void addTab() {
        try {
            TabGroup.sInstance.addTab(mTabName, mMyPage, mMyTabButton);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 页面内容
     */
    private class MyPage extends TabContent {
        private static final int MAX_HEIGHT = 660;

        private HorizontalPageScrollView mHorizontalScrollView;
        private OverViewFrameLayout mFrameLayout;

        private class FocusChange extends AnimateFocusChangeListener {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                super.onFocusChange(v, hasFocus);
            }
        }

        private class KeyListener implements View.OnKeyListener {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        return true;
                }
                return false;
            }
        }

        public MyPage() {
            initView();
        }

        public synchronized void initView() {

            mHorizontalScrollView = (HorizontalPageScrollView) LayoutInflater.from(ActivityHandler.getInstance()).inflate(R.layout.my_page, null);
            mFrameLayout = (OverViewFrameLayout) mHorizontalScrollView.findViewById(R.id.my_over_view_frame_layout);
            mFrameLayout.setClipChildren(false);
            mFrameLayout.setMinimumHeight(MAX_HEIGHT * CommonFunction.getScreenSize().y / 1080);
            {
                RelativeLayout button = (RelativeLayout) mHorizontalScrollView.findViewById(R.id.my_collect_button);
                button.setOnFocusChangeListener(new FocusChange());
                button.setTag(R.id.track_view_scale_x, 1.08f);
                button.setTag(R.id.track_view_scale_y, 1.08f);
                button.setOnKeyListener(new KeyListener());
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ActivityHandler.getInstance() != null) {
                            if (UserHelper.getInstance(ActivityHandler.getInstance()).getUserLogin()) {
                                Intent intent = new Intent();
                                intent.setClass(ActivityHandler.getInstance(), CollectActivity.class);
                                ActivityHandler.getInstance().startActivity(intent);
                            } else {
                                startLogin("收藏页");
                            }
                        } else {
                            LogUtils.d("MyFactory", "collect = null");
                        }
                    }
                });
            }

            {
                RelativeLayout button = (RelativeLayout) mHorizontalScrollView.findViewById(R.id.my_order_button);
                button.setOnFocusChangeListener(new FocusChange());
                button.setTag(R.id.track_view_scale_x, 1.08f);
                button.setTag(R.id.track_view_scale_y, 1.08f);
                button.setOnKeyListener(new KeyListener());
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ActivityHandler.getInstance() != null) {
                            if (UserHelper.getInstance(ActivityHandler.getInstance()).getUserLogin()) {
                                Intent intent = new Intent();
                                intent.setClass(ActivityHandler.getInstance(), ManageOrderActivity.class); //获取订单信息
                                ActivityHandler.getInstance().startActivity(intent);
                            } else {
                                startLogin("订单页");
                            }
                        } else {
                            LogUtils.d("MyFactory", "order = null");
                        }
                    }
                });
            }

            {
                RelativeLayout button = (RelativeLayout) mHorizontalScrollView.findViewById(R.id.my_settings_button);
                button.setOnFocusChangeListener(new FocusChange());
                button.setTag(R.id.track_view_scale_x, 1.08f);
                button.setTag(R.id.track_view_scale_y, 1.08f);
                button.setOnKeyListener(new KeyListener());
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClass(ActivityHandler.getInstance(), SettingActivity.class);
                        ActivityHandler.getInstance().startActivity(intent);
                    }
                });
            }
        }


        @Override
        public View getView() {
            return mHorizontalScrollView;
        }

        @Override
        public void tabAdded() {

        }

        @Override
        public void tabRemoved() {

        }

        @Override
        public void tabSelected(boolean tFlag) {

        }

        @Override
        public boolean tabEdgeChange(boolean tIsIn, int tDirection, Rect tRect) {
            return false;
        }
    }


    public void startLogin(final String source) {
        if (mLoginWindow == null) {
            LogUtils.d("MyFactory", "startLogin-->mLoginWindow is null");
            mLoginWindow = new LoginWindow(ActivityHandler.getInstance());
        }else{
            //mLoginWindow = null;
            LogUtils.d("MyFactory", "startLogin-->mLoginWindow is not null");
        }

        mLoginWindow.registerLoginListener(source);
        mLoginWindow.show();
        getLoginQRCodeUrl();
    }

    //获取登陆二维码
    private void getLoginQRCodeUrl() {
        UUCWrapper.getInstance(ActivityHandler.getInstance()).getLoginQRCodeUrl(new CallBack<String>() {
            @Override
            public void onComplete(String url) {
                LogUtils.d("MyFactory", "getLoginQRCodeUrl onComplete: " + url);
                mLoginWindow.refreshQRCode(url);
            }

            @Override
            public void onError(String s) {

            }
        });
    }
}
