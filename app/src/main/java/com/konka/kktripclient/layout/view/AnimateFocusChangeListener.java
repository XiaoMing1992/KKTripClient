package com.konka.kktripclient.layout.view;

import android.view.View;

import com.konka.kktripclient.R;

/**
 * 属性动画的焦点缩放控制
 *
 * @author YangDeZun
 */
public class AnimateFocusChangeListener implements View.OnFocusChangeListener {

    private static float DEFAULT_SCALE_X = 1.1f;
    private static float DEFAULT_SCALE_Y = 1.1f;
    private float mFocusScaleX = 0;
    private float mFocusScaleY = 0;
    private FocusScaleListenerCallback mFocusCallback;

    public AnimateFocusChangeListener() {
        this(null);
    }

    public AnimateFocusChangeListener(FocusScaleListenerCallback tCallback) {
        mFocusCallback = tCallback;
    }

    @Override
    public void onFocusChange(final View v, boolean hasFocus) {
        if (hasFocus) {
            if (mFocusScaleX == 0) {
                Float tXObject = (Float) v.getTag(R.id.track_view_scale_x);
                mFocusScaleX = (tXObject == null ? DEFAULT_SCALE_X : tXObject);
            }

            if (mFocusScaleY == 0) {
                Float tYObject = (Float) v.getTag(R.id.track_view_scale_y);
                mFocusScaleY = (tYObject == null ? DEFAULT_SCALE_Y : tYObject);
            }
            v.animate().scaleX(mFocusScaleX).scaleY(mFocusScaleY).start();
            v.requestLayout();
        } else {
            v.animate().scaleX(1.0f).scaleY(1.0f).start();
        }

        if (mFocusCallback != null) {
            mFocusCallback.focusScaleCallback(v, hasFocus);
        }
    }

}
