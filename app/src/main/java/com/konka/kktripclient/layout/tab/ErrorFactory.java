package com.konka.kktripclient.layout.tab;

import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.konka.kktripclient.R;
import com.konka.kktripclient.layout.tab.base.TabContent;
import com.konka.kktripclient.layout.util.ActivityHandler;

/**
 * Created by GXY on 2016-11-16.
 * <p>
 * 错误页面
 */

public class ErrorFactory {
    private static ErrorFactory mErrorFactory;
    private String mTabName;
    private RecommendTabButton mMyTabButton;
    private ErrorFactory.MyPage mMyPage;

    public static ErrorFactory getInstance() {
        if (mErrorFactory == null) mErrorFactory = new ErrorFactory();
        return mErrorFactory;
    }

    public void initView() {
        try {
            mTabName = "      ";
            mMyTabButton = new RecommendTabButton(mTabName, "", true, false);
            mMyPage = new ErrorFactory.MyPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RecommendTabButton getMyTabButton() {
        return mMyTabButton;
    }

    public String getTabName() {
        return mTabName;
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
        private LinearLayout mLinearLayout;

        public MyPage() {
            initView();
        }

        public synchronized void initView() {
            mLinearLayout = (LinearLayout) LayoutInflater.from(ActivityHandler.getInstance()).inflate(R.layout.error_page, null);
        }

        @Override
        public View getView() {
            return mLinearLayout;
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
}
