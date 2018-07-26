package com.konka.kktripclient.ui.popupwindow;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.konka.kktripclient.R;
import com.konka.kktripclient.utils.CommonUtils;
import com.konka.kktripclient.utils.LogUtils;

/**
 *
 */

public class AboutInfoWindow extends PopupWindow{
    private final String TAG = "AboutInfoWindow";
    private Context mContext;
    private View mView;

    public AboutInfoWindow(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        mView = LayoutInflater.from(mContext).inflate(R.layout.about_window, null);
        setContentView(mView);

        CommonUtils.computeScreenSize(context);
        LogUtils.d(TAG, "ScreenWidth = "+CommonUtils.getScreenWidth()+" ScreenHeight = "+CommonUtils.getScreenHeight());
        setWidth(CommonUtils.getScreenWidth());
        setHeight(CommonUtils.getScreenHeight());
        setFocusable(true);
        setBackgroundDrawable(mContext.getResources().getDrawable(R.color.transparent));
    }

    public void show() {
        showAtLocation(mView, Gravity.CENTER, 0, 0);
    }
}
