package com.konka.kktripclient.layout.view;

import java.lang.reflect.Field;

import com.konka.kktripclient.layout.util.ActivityHandler;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

/**
 * 1.通过反射调整scroller的动画时间，使动画更平滑，具体参考{@link CustomViewPager#init()}<br/>
 * 2.切页时记录边缘控件的位置信息，用于边缘切页时的焦点控制，具体参考{@link CustomViewPager#dispatchKeyEvent(KeyEvent)} <br/>
 * 3.提供边缘回弹效果，具体参考{@link CustomViewPager#setFlyable(boolean, boolean)}、
 * {@link CustomViewPager#setFlyDistance(float)}、{@link CustomViewPager#setFlyDuration(long)}
 *
 * @author YangDeZun
 */
public class CustomViewPager extends ViewPager {

    private static final float DEFAULT_FLY_DISTANCE = 80;
    private static final long DEFAULT_FLY_DURATION = 200;
    private static final int FLY_DIRECTION_LEFT = 1;
    private static final int FLY_DIRECTION_RIGHT = 2;
    private View mTempFocusView;
    private Rect mLastFocusRect = new Rect();
    /**
     * 设置左滑是否可以回弹
     */
    private boolean mFlyableLeft = true;

    /**
     * 设置右滑是否可以回弹
     */
    private boolean mFlyableRight = true;

    /**
     * fly的距离
     */
    private float mFlyDistance = DEFAULT_FLY_DISTANCE;
    /**
     * fly的时间
     */
    private long mFlyDuration = DEFAULT_FLY_DURATION;

    private long mLastFlyTime = 0;
    private long mCurrentFlyTime = 0;

    public CustomViewPager(Context context) {
        super(context);
        init();
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        try {
            // 反射设置scroller，解决切页的时间问题
            Field tField = this.getClass().getSuperclass().getDeclaredField("mScroller");
            tField.setAccessible(true);
            tField.set(this, new Scroller(getContext(), new DecelerateInterpolator()) {
                @Override
                public void startScroll(int startX, int startY, int dx, int dy, int duration) {
                    super.startScroll(startX, startY, dx, dy, 400);
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置是否可以回弹,默认打开
     *
     * @param tFlyableLeft
     * @param tFlyableRight
     */
    public void setFlyable(boolean tFlyableLeft, boolean tFlyableRight) {
        mFlyableLeft = tFlyableLeft;
        mFlyableRight = tFlyableRight;
    }

    /**
     * 设置fly的距离，会自行取正整数。默认值为80px
     *
     * @param tDistance
     */
    public void setFlyDistance(float tDistance) {
        mFlyDistance = Math.abs(tDistance);
    }

    /**
     * 设置fly的动画时间，取值范围为>0,若小于等于零，则置为默认值。默认值为200ms
     *
     * @param tDuration
     */
    public void setFlyDuration(long tDuration) {
        mFlyDuration = tDuration <= 0 ? DEFAULT_FLY_DURATION : tDuration;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN && (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT
                || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT)) {
            mTempFocusView = ((Activity) getContext()).getCurrentFocus();
            mLastFocusRect.setEmpty();
            if (mTempFocusView != null) {
//				mTempFocusView.getGlobalVisibleRect(mLastFocusRect);

                mLastFocusRect.top = mTempFocusView.getTop();
                mLastFocusRect.left = mTempFocusView.getLeft();
                mLastFocusRect.bottom = mTempFocusView.getBottom();
                mLastFocusRect.right = mTempFocusView.getRight();
            }

            /**
             * 回弹效果思路：
             * 回弹的触发条件为：
             * 1.第0页的left方向无nextfocus
             * 2.第n-1页的right方向无nextfocus
             *
             */
            if (handleFlyEvent(event)) {
                mTempFocusView = null;
                return true;
            } else {
                mTempFocusView = null;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private boolean handleFlyEvent(KeyEvent event) {
        try {
            if (mFlyableLeft && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT && getCurrentItem() == 0) {
                if (this.focusSearch(mTempFocusView, View.FOCUS_LEFT) == null) {
                    startFlying(FLY_DIRECTION_LEFT);
                    return true;
                }
            }

            if (mFlyableRight && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT
                    && getCurrentItem() == getAdapter().getCount() - 1) {
                if (this.focusSearch(mTempFocusView, View.FOCUS_RIGHT) == null) {
                    startFlying(FLY_DIRECTION_RIGHT);
                    return true;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return false;
    }

    private void startFlying(int tDirection) {
        mCurrentFlyTime = System.currentTimeMillis();
        if (mCurrentFlyTime - mLastFlyTime <= mFlyDuration + 100) {
            return;
        }
        mLastFlyTime = mCurrentFlyTime;

        AnimatorSet tAnimatorSet = new AnimatorSet();
        ObjectAnimator tAnimator1 = ObjectAnimator.ofFloat(this, "translationX",
                tDirection == FLY_DIRECTION_LEFT ? mFlyDistance : -mFlyDistance);
        tAnimator1.setDuration(mFlyDuration / 2);
        ObjectAnimator tAnimator2 = ObjectAnimator.ofFloat(this, "translationX", 0);
        tAnimator1.setDuration(mFlyDuration / 2);
        tAnimatorSet.play(tAnimator1).before(tAnimator2);
        tAnimatorSet.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                ActivityHandler.getInstance().refreshTrack();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                ActivityHandler.getInstance().refreshTrack();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ActivityHandler.getInstance().refreshTrack();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                ActivityHandler.getInstance().refreshTrack();
            }
        });
        tAnimatorSet.setInterpolator(new DecelerateInterpolator());
        tAnimatorSet.start();
    }

    public Rect getLastFocusRect() {
        return mLastFocusRect;
    }
}
