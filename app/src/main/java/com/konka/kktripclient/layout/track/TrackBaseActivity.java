package com.konka.kktripclient.layout.track;

import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.konka.kktripclient.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by The_one on 2017-5-16.
 * <p>
 * TrackBaseActivity
 * <p>
 * 0. 轨迹动画的基类Activity，继承该类即可实现轨迹动画效果。默认为no_title，full_screen<br/>
 * 1. 整个轨迹动画工程主要包含两个类：{@link TrackDrawableCache}、{@link TrackView}。均已隐藏<br/>
 * 2. 在{@link #setContentView}的时候会自动添加TrackView，默认为矩形轨迹<br/>
 * 3. {@link #setAutoBindFocus(boolean)}方法用于设置是否自动绑定focus控件，默认为true<br/>
 * 4. 通过{@link #registerTrackDrawable(String, Drawable)}或{@link #registerTrackDrawable(String, Drawable, Rect)}<br/>
 * 来注册轨迹类型及轨迹图片；当自动计算时，.9图的非拉伸区域由“右下”黑边决定<br/>
 * 5. 通过{@link View#setTag(int, Object)}来指定焦点控件的轨迹动画类型，给焦点控件设置放大倍数<br/>
 * View.setTag(R.id.track_view_type, string)来指定焦点控件的轨迹动画类型<br/>
 * View.setTag(R.id.track_view_scale_x, int)/View.setTag(R.id.track_view_scale_y, int)来给焦点控件设置放大倍数，默认为1.1倍<br/>
 * 6. {@link #setTrackVisibility(boolean)}可显示和隐藏轨迹View<br/>
 * 7. {@link #setTrackDuration(double)}可控制轨迹动画时间<br/>
 * 8. {@link #requestTrackQuicklyNextFocus()}或者{@link #requestTrackQuickly()}可实现快速定位<br/>
 * 9. {@link #bindFocusView(View)}方法可以主动绑定需要定位的控件<br/>
 *
 * @author ZhangeFei
 * @author YangDeZun
 */

public class TrackBaseActivity extends Activity {
    /**
     * 默认的轨迹动画类型
     */
    public static final String DEFAULT_TRACK_TYPE = "RoundConnerRectangle";
    /**
     * 默认的轨迹动画图片
     */
    public static final int DEFAULT_TRACK_DRAWABLE = R.drawable.default_rectangle;
    /**
     * 轨迹类型对应的key
     **/
    public static final int TRACK_TYPE_KEY = R.id.track_view_type;

    private TrackView mTrackView;
    /**
     * 是否自动在焦点发生变化的时候绑定控件
     */
    private boolean mIsAutoBindFocus = true;
    /**
     * 管理轨迹缓存
     */
    private TrackDrawableCache mTrackDrawableCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 设置全屏
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initTrackView();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initTrackView();
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        initTrackView();
    }

    private void initTrackView() {
        if (mTrackDrawableCache == null) {
            mTrackDrawableCache = new TrackDrawableCache();
            registerTrackDrawable(DEFAULT_TRACK_TYPE, this.getResources().getDrawable(DEFAULT_TRACK_DRAWABLE));

            mTrackView = new TrackView(this);
            ViewGroup contentView = (ViewGroup) findViewById(android.R.id.content);
            LayoutParams tParams = new LayoutParams(1, 1);
            contentView.addView(mTrackView, tParams);
            contentView.getViewTreeObserver().addOnGlobalFocusChangeListener(new OnGlobalFocusChangeListener() {
                @Override
                public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                    if (newFocus != null && mIsAutoBindFocus) {
                        mTrackView.bindFocusView(newFocus);
                    }
                }
            });
        }
    }

    /**
     * 设置在focus发生变化时是否主动绑定轨迹
     *
     * @param tFlag
     */
    public void setAutoBindFocus(boolean tFlag) {
        mIsAutoBindFocus = tFlag;
    }

    /**
     * 注册轨迹类型及其图片, 会自动计算.9图的非拉伸区域
     *
     * @param type     轨迹类型
     * @param drawable 对应的图片
     * @return
     */
    public void registerTrackDrawable(String type, Drawable drawable) {
        if (mTrackDrawableCache != null) {
            mTrackDrawableCache.registerTrackDrawable(type, drawable);
        }
    }

    /**
     * 注册轨迹类型机器图片，并显式指定非拉伸区域
     *
     * @param type           轨迹类型
     * @param drawable       轨迹图片
     * @param notScaleRegion 图片的非拉伸区域，为null时会自动计算.9图的非拉伸区域
     * @return
     */
    public void registerTrackDrawable(String type, Drawable drawable, Rect notScaleRegion) {
        if (mTrackDrawableCache != null) {
            mTrackDrawableCache.registerTrackDrawable(type, drawable, notScaleRegion);
        }
    }

    /**
     * 通过类型名字获取轨迹图片
     *
     * @param type 轨迹类型
     * @return
     */
    public Drawable getTrackDrawable(String type) {
        if (mTrackDrawableCache == null) {
            return null;
        }
        return mTrackDrawableCache.getTrackDrawable(type);
    }

    /**
     * 获取轨迹图片的非拉伸区域
     *
     * @param type
     * @return
     */
    public Rect getTrackDrawableNotScaleRegion(String type) {
        if (mTrackDrawableCache == null) {
            return null;
        }
        return mTrackDrawableCache.getTrackDrawableNotScaleRegion(type);
    }

    /**
     * 设置轨迹动画时间
     *
     * @param mills
     */
    public void setTrackDuration(double mills) {
        if (mTrackView != null) {
            mTrackView.setTrackDuration(mills);
        }
    }

    /**
     * 获取轨迹动画时间
     *
     * @return
     */
    public double getTrackDuration() {
        if (mTrackView == null) {
            return 0;
        }
        return mTrackView.getTrackDuration();
    }

    /**
     * 绑定焦点控件
     *
     * @param tView
     */
    public void bindFocusView(View tView) {
        if (mTrackView != null) {
            mTrackView.bindFocusView(tView);
        }
    }

    /**
     * 刷新Track
     */
    public synchronized void refreshTrack() {
        if (mTrackView != null) {
            mTrackView.refreshPosition();
        }
    }

    /**
     * 设置TrackView的可视与否
     *
     * @param tShow true--显示， false--隐藏
     */
    public void setTrackVisibility(boolean tShow) {
        if (mTrackView != null) {
            mTrackView.hideTrackView(!tShow);
        }
    }

    /**
     * 快速定位，下一次 {@link TrackView#onDraw(Canvas)} 调用时将会直接更新参数而无动画效果.<br/>
     * <p/>
     * 2016-07-06 ydz注：此方法的本意是让下一个焦点控件快速定位，但可能存在时序上的bug，并不能保证效果。<br/>
     * 建议使用 {@link #requestTrackQuicklyNextFocus()}
     */
    @Deprecated
    public void requestTrackQuickly() {
        if (mTrackView != null) {
            mTrackView.requestTrackQuickly();
        }
    }

    /**
     * 下一个焦点控件采用快速定位
     */
    public void requestTrackQuicklyNextFocus() {
        if (mTrackView != null) {
            mTrackView.requestTrackQuicklyNextFocus();
        }
    }

    /**
     * 轨迹动画控件，动画效果在{@link #onDraw(Canvas)}中实现
     *
     * @author ZhangFei
     * @author YangDeZun
     */
    private class TrackView extends FrameLayout {
        /**
         * 默认的检测刷新次数，避免放大动画或者滚动动画之类的影响
         */
        private final static int DEFAULT_REPOSITION_COUNT = 5;
        /**
         * 检测刷新时间间隔
         */
        private final static int DEFAULT_INVALIDATE_DURATION = 10;
        /**
         * 计算的补充值
         */
        private final static int DEFAULT_COMPUTER_FIX_PX = 5;
        /**
         * 默认的放大倍数
         */
        private final float DEFAULT_SCALE_X = 1.1f, DEFAULT_SCALE_Y = 1.1f;
        /**
         * 当前的焦点类型
         */
        private String mCurrentTrackType;
        /**
         * 目标焦点类型
         */
        protected String mAimTrackType;
        /**
         * 默认的轨迹时间,毫秒
         */
        private double mPositionDuration = 200;
        /**
         * 默认的缩放动画时间
         */
        private double mSizeDuration = (7 * mPositionDuration) / 8;
        /**
         * 轨迹图片
         */
        private Drawable mCurrentTrackDrawable;
        /**
         * 轨迹图片的非拉伸区域
         */
        private Rect mDrawableNotScaleRegion;
        /**
         * 当前焦点控件
         */
        protected View mCurrentFocusedView;
        /**
         * 放大倍数
         */
        private float mScaleX = DEFAULT_SCALE_X, mScaleY = DEFAULT_SCALE_Y;
        /**
         * 动画加速器
         */
        private TimeInterpolator mTimeInterpolator;
        /**
         * 快速定位
         **/
        private volatile boolean mQuickPositioning = false;
        /**
         * 下一个focus快速定位
         */
        private volatile boolean mQuickPositioningNextFocus = false;
        /**
         * 重画计数器，避免scrollview等滚动过程中轨迹view没有及时跟随的问题
         */
        private int mRePostInvalidateCount = 0;

        /**
         * 各种计算中间量
         */
        private int[] mToPosition = new int[2];
        protected int[] mFromPosition = new int[2];
        private int[] mCurrentPosition = new int[2];
        private int[] mToSize = new int[2];
        private int[] mCurrentSize = new int[2];
        private int[] mFromSize = new int[2];
        private float mPositionTempProportion;
        private float mSizeTempProportion;
        protected float mOmitWidth, mOmitHeight; // 非拉伸区域的微调值
        private double mCurrentTime, mDeltaTime;
        private long mTempTime;

        private View mDrawableView;
        private int mDrawablePadding = 100;

        private OnLayoutChangeListener mLayoutChangeListener = new OnLayoutChangeListener() {

            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
                                       int oldRight, int oldBottom) {
                if (v == mCurrentFocusedView) {
                    invalidate();
                }
            }
        };

        public TrackView(Context context) {
            this(context, null);
        }

        public TrackView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public TrackView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);

            mDrawablePadding = (int) (100 * context.getResources().getDisplayMetrics().density);

            mDrawableView = new View(context);
            this.setPadding(mDrawablePadding, mDrawablePadding, mDrawablePadding, mDrawablePadding);
            this.addView(mDrawableView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT));
            this.setWillNotDraw(false);
            this.setBackgroundColor(0x01000000);

            mTimeInterpolator = new DecelerateInterpolator();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (calculateChange()) {
                requestLayout();
                invalidate();
            } else {
                if (mRePostInvalidateCount < DEFAULT_REPOSITION_COUNT) {
                    mRePostInvalidateCount++;
                    postInvalidateDelayed(DEFAULT_INVALIDATE_DURATION);
                }
            }

        }

        /**
         * @return 有变化返回true，否则false
         */
        private boolean calculateChange() {
            if (mCurrentFocusedView == null) {
                return false;
            }

            if (mCurrentTime == 0) {
                mCurrentTime = System.currentTimeMillis();
                mDeltaTime = 0;
            } else {
                mTempTime = System.currentTimeMillis();
                mDeltaTime += mTempTime - mCurrentTime;
                mCurrentTime = mTempTime;
            }

            this.getLocationInWindow(mCurrentPosition);
            mCurrentSize[0] = this.getWidth();
            mCurrentSize[1] = this.getHeight();
            mCurrentFocusedView.getLocationInWindow(mToPosition);
            mToSize[0] = (int) (mCurrentFocusedView.getWidth() * mScaleX + mOmitWidth);
            mToSize[1] = (int) (mCurrentFocusedView.getHeight() * mScaleY + mOmitHeight);
            // 先取中心点，再算偏移位置，属性动画和view动画的差异表现在getScale上
            mToPosition[0] = (int) (mToPosition[0]
                    + mCurrentFocusedView.getWidth() * mCurrentFocusedView.getScaleX() / 2 - mToSize[0] / 2);
            mToPosition[1] = (int) (mToPosition[1]
                    + mCurrentFocusedView.getHeight() * mCurrentFocusedView.getScaleY() / 2 - mToSize[1] / 2);

            if (!isApproximate(mCurrentPosition[0], mToPosition[0])
                    || !isApproximate(mCurrentPosition[1], mToPosition[1])
                    || !isApproximate(mCurrentSize[0], mToSize[0]) || !isApproximate(mCurrentSize[1], mToSize[1])) {
                mRePostInvalidateCount = 0;
                if (mQuickPositioning) {
                    mQuickPositioning = false;
                    setX(mToPosition[0]);
                    setY(mToPosition[1]);
                    getLayoutParams().width = mToSize[0];
                    getLayoutParams().height = mToSize[1];
                } else {
                    mPositionTempProportion = mTimeInterpolator
                            .getInterpolation(clamp((float) (mDeltaTime / mPositionDuration), 0, 1.0f));
                    mSizeTempProportion = mTimeInterpolator
                            .getInterpolation(clamp((float) (mDeltaTime / mSizeDuration), 0, 1.0f));
                    setX(getFixValue(mFromPosition[0], mToPosition[0], mPositionTempProportion));
                    setY(getFixValue(mFromPosition[1], mToPosition[1], mPositionTempProportion));
                    getLayoutParams().width = (int) getFixValue(mFromSize[0], mToSize[0], mSizeTempProportion);
                    getLayoutParams().height = (int) getFixValue(mFromSize[1], mToSize[1], mSizeTempProportion);
                }
                return true;
            } else {
                mFromPosition[0] = mToPosition[0];
                mFromPosition[1] = mToPosition[1];
                mFromSize[0] = mToSize[0];
                mFromSize[1] = mFromSize[1];
                return false;
            }
        }

        /**
         * 设置轨迹动画时间
         *
         * @param mills
         */
        public void setTrackDuration(Double mills) {
            mPositionDuration = mills;
            mSizeDuration = (7 * mPositionDuration) / 8;
        }

        /**
         * 获取轨迹时间
         *
         * @return
         */
        public double getTrackDuration() {
            return mPositionDuration;
        }

        /**
         * 绑定焦点控件
         *
         * @param view
         */
        public void bindFocusView(View view) {
            if (view == null) {
                return;
            }
            if (mCurrentFocusedView != null) {
                mCurrentFocusedView.removeOnLayoutChangeListener(mLayoutChangeListener);
            }
            if (mCurrentFocusedView != view) {
                try {
                    mAimTrackType = (String) view.getTag(TRACK_TYPE_KEY);
                } catch (Throwable e) {
                    mAimTrackType = DEFAULT_TRACK_TYPE;
                }
                mCurrentFocusedView = view;
                mCurrentFocusedView.addOnLayoutChangeListener(mLayoutChangeListener);
                resetValue();
                if (mQuickPositioningNextFocus) {
                    mQuickPositioningNextFocus = false;
                    mQuickPositioning = true;
                }
                postInvalidate();
            }
        }

        /**
         * 刷新显示，用于实现跟随动画等
         */
        public void refreshPosition() {
            resetValue();
            postInvalidate();
        }

        /**
         * 隐藏或显示轨迹view
         *
         * @param isHide true 隐藏；false 显示
         */
        public void hideTrackView(boolean isHide) {
            if (isHide) {
                setVisibility(View.GONE);
            } else {
                calculateChange(); // 提前计算，解决闪抖的问题
                setVisibility(View.VISIBLE);
            }
        }

        /**
         * 快速定位，下一次 {@link #onDraw(Canvas)} 调用时将会直接更新参数而无动画效果.<br/>
         * <p/>
         * 2016-07-06 ydz注：此方法的本意是让下一个焦点控件快速定位，但可能存在时序上的bug，并不能保证效果。<br/>
         * 建议使用 {@link #requestTrackQuicklyNextFocus()}
         */
        @Deprecated
        public void requestTrackQuickly() {
            mQuickPositioning = true;
        }

        /**
         * 下一个焦点控件采用快速定位
         */
        public void requestTrackQuicklyNextFocus() {
            mQuickPositioningNextFocus = true;
        }

        /**
         * 判断float类型是否相等
         *
         * @param a
         * @param b
         * @return
         */
        private boolean isApproximate(int a, int b) {
            if (Math.abs(a - b) <= 1) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * 将value取值限定在min和max之间
         *
         * @param value
         * @param min
         * @param max
         * @return
         */
        private float clamp(float value, float min, float max) {
            if (value < min)
                return min;
            if (value > max)
                return max;
            return value;
        }

        private float getFixValue(float tSource, float tAim, float tTimeProportion) {
            float tTemp = 0;
            if (tAim >= tSource) {
                tTemp = tSource + (tAim - tSource) * tTimeProportion + DEFAULT_COMPUTER_FIX_PX;
                tTemp = (tTemp - tAim >= 1) ? tAim : tTemp;
            } else {
                tTemp = tSource + (tAim - tSource) * tTimeProportion - DEFAULT_COMPUTER_FIX_PX;
                tTemp = (tAim - tTemp >= 1) ? tAim : tTemp;
            }
            return tTemp;
        }

        private void resetValue() {
            // 焦点控件发生变化时，记录起始大小、位置
            this.getLocationInWindow(mFromPosition);
            mFromSize[0] = this.getWidth();
            mFromSize[1] = this.getHeight();

            // 复位
            mCurrentPosition[0] = 0;
            mCurrentPosition[1] = 0;
            mToPosition[0] = 0;
            mToPosition[1] = 0;
            mCurrentSize[0] = 0;
            mCurrentSize[1] = 0;
            mToSize[0] = 0;
            mToSize[1] = 0;
            mDeltaTime = 0;
            mCurrentTime = 0;
            mRePostInvalidateCount = 0;

            if (mCurrentTrackType == null || !mCurrentTrackType.equals(mAimTrackType)) {
                mCurrentTrackDrawable = mTrackDrawableCache.getTrackDrawable(mAimTrackType);
                mDrawableNotScaleRegion = mTrackDrawableCache.getTrackDrawableNotScaleRegion(mAimTrackType);
                if (mCurrentTrackDrawable == null) {
                    mCurrentTrackDrawable = mTrackDrawableCache.getTrackDrawable(DEFAULT_TRACK_TYPE);
                    mDrawableNotScaleRegion = mTrackDrawableCache.getTrackDrawableNotScaleRegion(DEFAULT_TRACK_TYPE);
                }
                mCurrentTrackType = mAimTrackType;
                mDrawableView.setBackground(mCurrentTrackDrawable);
                // 根据非拉伸区域计算微调值
                if (mDrawableNotScaleRegion != null) {
                    mOmitWidth = mDrawableNotScaleRegion.left + mDrawableNotScaleRegion.right + mDrawablePadding * 2;
                    mOmitHeight = mDrawableNotScaleRegion.top + mDrawableNotScaleRegion.bottom + mDrawablePadding * 2;
                } else {
                    mOmitWidth = mOmitHeight = mDrawablePadding * 2;
                }
            }

            if (mCurrentFocusedView != null) {
                Float tXObject = (Float) mCurrentFocusedView.getTag(R.id.track_view_scale_x);
                Float tYObject = (Float) mCurrentFocusedView.getTag(R.id.track_view_scale_y);
                mScaleX = (tXObject == null ? DEFAULT_SCALE_X : tXObject);
                mScaleY = (tYObject == null ? DEFAULT_SCALE_Y : tYObject);
            } else {
                mScaleX = DEFAULT_SCALE_X;
                mScaleY = DEFAULT_SCALE_Y;
            }

        }

    }

    /**
     * 不同类型轨迹图片的管理类
     *
     * @author ZhangeFei
     * @author YangDeZun
     */
    private class TrackDrawableCache {
        private Map<String, DrawableInfo> mTrackDrawableMap;

        public TrackDrawableCache() {
            mTrackDrawableMap = new HashMap<String, DrawableInfo>();
        }

        /**
         * 通过类型名字获取轨迹图片
         *
         * @param type 轨迹类型
         * @return
         */
        public Drawable getTrackDrawable(String type) {
            if (TextUtils.isEmpty(type))
                return null;
            return mTrackDrawableMap.get(type).getDrawable();
        }

        /**
         * 获取轨迹图片的非拉伸区域
         *
         * @param type
         * @return
         */
        public Rect getTrackDrawableNotScaleRegion(String type) {
            if (TextUtils.isEmpty(type))
                return null;
            return mTrackDrawableMap.get(type).getNotScaleRegion();
        }

        /**
         * 注册轨迹类型及其图片, 会自动计算.9图的非拉伸区域
         *
         * @param type     轨迹类型
         * @param drawable 对应的图片
         * @return
         */
        public void registerTrackDrawable(String type, Drawable drawable) {
            if (TextUtils.isEmpty(type)) {
                throw new IllegalArgumentException("The drawable can't be empty!");
            }
            if (drawable == null) {
                throw new IllegalArgumentException("The drawable can't be null!");
            }
            mTrackDrawableMap.put(type, new DrawableInfo(drawable));
        }

        /**
         * 注册轨迹类型机器图片，并显式指定非拉伸区域
         *
         * @param type           轨迹类型
         * @param drawable       轨迹图片
         * @param notScaleRegion 图片的非拉伸区域，为null时会自动计算.9图的非拉伸区域
         * @return
         */
        public void registerTrackDrawable(String type, Drawable drawable, Rect notScaleRegion) {
            if (TextUtils.isEmpty(type)) {
                throw new IllegalArgumentException("The drawable can't be empty!");
            }
            if (drawable == null) {
                throw new IllegalArgumentException("The drawable can't be null!");
            }

            mTrackDrawableMap.put(type, new DrawableInfo(drawable, notScaleRegion));
        }

        /**
         * 图片信息<br/>
         * 包含drawable、非拉伸区域
         *
         * @author YangDeZun
         */
        private class DrawableInfo {
            /**
             * 轨迹图片
             **/
            private Drawable mDrawable;
            /**
             * 非拉伸区域
             **/
            private Rect mNotScaleRegion = new Rect();

            public DrawableInfo(Drawable drawable) {
                this(drawable, null);
            }

            /**
             * 显式指定非拉伸区域
             *
             * @param drawable
             * @param notScaleRegion
             */
            public DrawableInfo(Drawable drawable, Rect notScaleRegion) {
                mDrawable = drawable;
                if (notScaleRegion == null) {
                    if (drawable instanceof NinePatchDrawable) {
                        drawable.getPadding(mNotScaleRegion);
                    }
                } else {
                    mNotScaleRegion = notScaleRegion;
                }
            }

            /**
             * 获取drawable
             *
             * @return
             */
            public Drawable getDrawable() {
                return mDrawable;
            }

            /**
             * 获取非拉伸区域
             */
            public Rect getNotScaleRegion() {
                return mNotScaleRegion;
            }
        }
    }
}
