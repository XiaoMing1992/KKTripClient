package com.konka.kktripclient.layout.tab;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.konka.kktripclient.R;
import com.konka.kktripclient.detail.DetailConstant;
import com.konka.kktripclient.detail.customview.TripVideoView;
import com.konka.kktripclient.detail.interfaces.ISmallWindow;
import com.konka.kktripclient.detail.presenter.WindowPlayer;
import com.konka.kktripclient.layout.data.KKTabButtonDataInfo;
import com.konka.kktripclient.layout.data.KKTabItemDataInfo;
import com.konka.kktripclient.layout.tab.base.TabButton;
import com.konka.kktripclient.layout.tab.base.TabContent;
import com.konka.kktripclient.layout.tab.base.TabStateCallback;
import com.konka.kktripclient.layout.util.ActivityHandler;
import com.konka.kktripclient.layout.util.CommonFunction;
import com.konka.kktripclient.layout.util.LaunchHelper;
import com.konka.kktripclient.layout.view.CustomViewPager;
import com.konka.kktripclient.layout.view.HorizontalPageScrollView;
import com.konka.kktripclient.net.HttpHelper;
import com.konka.kktripclient.utils.Constant;
import com.konka.kktripclient.utils.LogUtils;

import java.util.ArrayList;

/**
 * Created by YangDeZun on 2016/5/3.
 * <p>
 * 独立的Tab逻辑，实质为LinearLayout，上半部为tab内容，下半部为tab按钮，
 * 使用时只需要经过addTab将继承自相关基类的内容注册进来即可，
 * 整个操作过程以注册时提供的tab的名称作为参考标准，TabContent和TabButton是每个tab两个组成
 * 成份的抽象基类，使用时只需要在getView方法中返回具体的内容View即可，这样每个tab的具体处理
 * 逻辑都将更加独立。TabStateCallback会提供相应的状态回调，具体使用参考方法的注释说明
 */
public final class TabGroup extends LinearLayout {
    private final String TAG = this.getClass().getSimpleName();

    private ArrayList<Tab> mTabList = new ArrayList<>();
    private KKTabItemDataInfo mKKTabItemDataInfo;
    private HorizontalScrollView mHorizontalScrollView;
    private LinearLayout mButtonLayout;
    private CustomViewPager mContentViewPager;
    private FrameLayout mTripVideoViewLayout;
    private ProgressBar mLoadingBar;
    private TripVideoView mTripVideoView;
    private ContentAdapter mAdapter;
    private Tab mCurrentTab;
    private Tab mLastTab;
    public static TabGroup sInstance;
    private boolean mIsEdgeChange = true;
    private int mLastPagePosition = 0;
    private Context mContext;

    private boolean isFirstPage = true;
    private boolean isScrollStart = true;
    private boolean isScrollComplete = true;
    private boolean showVideo = true;

    private SmallWindowListener windowListener;
    private Runnable mStartVideoRunnable;

    public TabGroup(Context context) {
        super(context);
        mContext = context;
        initView(context);
    }

    public TabGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView(context);
    }

    public TabGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView(context);
    }

    public void hideTabButtonLayout() {
        if (mButtonLayout != null) {
            mButtonLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void showTabButtonLayout() {
        if (mButtonLayout != null) {
            mButtonLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取当前的tab列表
     *
     * @return
     */
    public ArrayList<Tab> getCurrentTabList() {
        return mTabList;
    }

    /**
     * 获取当前的tab索引
     *
     * @return
     */
    public int getCurrentTabIndex() {
        if (mContentViewPager == null) {
            return -1;
        }
        return mContentViewPager.getCurrentItem();
    }

    /**
     * 初始化view，主要是绑定viewpager和buttonLayout组件，并关联对应的data
     *
     * @param tContext
     */
    private void initView(Context tContext) {
        sInstance = this;
        this.setOrientation(LinearLayout.VERTICAL);

        LayoutInflater.from(tContext).inflate(R.layout.tab_group, this, true);
        mContentViewPager = (CustomViewPager) findViewById(R.id.tab_viewpager);
        mContentViewPager.setFlyable(false, false);
        mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.tab_button_scrollview);
        mButtonLayout = (LinearLayout) findViewById(R.id.tab_button_content);

        mAdapter = new ContentAdapter(mTabList);
        mContentViewPager.setAdapter(mAdapter);

        mContentViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                LogUtils.d(TAG, "onPageSelected: position:" + position);
                if (getTabContent(mTabList.get(position).getTabName()).getView() instanceof HorizontalPageScrollView) {
                    ((HorizontalPageScrollView) getTabContent(mTabList.get(position).getTabName()).getView()).setScrollViewListener(mScrollViewListener);
                    if (position == 0) {
                        isFirstPage = true;
                        if (getTabContent(mTabList.get(position).getTabName()).getView().getScrollX() == 0) {
                            isScrollStart = true;
                        } else {
                            isScrollStart = false;
                        }
                    } else {
                        isFirstPage = false;
                    }
                }
                ShowVideoWidget(isFirstPage, isScrollStart, isScrollComplete, showVideo);

                mLastTab = mCurrentTab;
                mCurrentTab = mTabList.get(position);
                mCurrentTab.tabSelected(true);
                if (mLastTab != null && mLastTab != mCurrentTab) {
                    mLastTab.tabSelected(false);
                }
                if (mIsEdgeChange && mLastPagePosition != position) {
                    mCurrentTab.tabEdgeChange(true, position > mLastPagePosition ? TabStateCallback.PAGE_DIRECTION_LTR_IN : TabStateCallback.PAGE_DIRECTION_RTL_IN, mContentViewPager.getLastFocusRect());
                    if (mLastTab != null) {
                        mLastTab.tabEdgeChange(false, position > mLastPagePosition ? TabStateCallback.PAGE_DIRECTION_LTR_OUT : TabStateCallback.PAGE_DIRECTION_RTL_OUT, null);
                    }
                }
                mLastPagePosition = position;
                mIsEdgeChange = true;
                mLastTab = null;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                LogUtils.d(TAG, "onPageScrollStateChanged: arg0:" + arg0);
                if (arg0 == ViewPager.SCROLL_STATE_IDLE) {
                    isScrollComplete = true;
                    Glide.with(mContext).resumeRequests();
                } else {
                    isScrollComplete = false;
                    Glide.with(mContext).pauseRequests();
                }
                ShowVideoWidget(isFirstPage, isScrollStart, isScrollComplete, showVideo);
            }
        });
    }

    /**
     * 动态设置菜单栏
     */
    public void DrawTabButton(KKTabButtonDataInfo mKKTabButtonDataInfo) {
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.setIntrinsicWidth(mKKTabButtonDataInfo.menuGap);
        shapeDrawable.setIntrinsicHeight(mKKTabButtonDataInfo.fontSize);
        shapeDrawable.setAlpha(0);
        mButtonLayout.setDividerDrawable(shapeDrawable);
        HorizontalScrollView.LayoutParams layoutParams = new HorizontalScrollView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.topMargin = mKKTabButtonDataInfo.menuCoordinateY * CommonFunction.getScreenSize().y / 1080;
        mHorizontalScrollView.setPadding(mKKTabButtonDataInfo.menuCoordinateX * CommonFunction.getScreenSize().x / 1920, 0, ActivityHandler.getInstance().getResources().getDimensionPixelOffset(R.dimen.tab_button_scrollview_padding) * CommonFunction.getScreenSize().x / 1920, 0);
        mHorizontalScrollView.setLayoutParams(layoutParams);
    }

    /**
     * 动态设置小窗口ing
     */
    public void DrawVideoWidget(final KKTabItemDataInfo tabItemDataInfo) {
        if (tabItemDataInfo == null || !Constant.LAUNCH_VIDEO_WIDGET.equals(tabItemDataInfo.type)) {
            return;
        }

        mKKTabItemDataInfo = tabItemDataInfo;
        mTripVideoView = new TripVideoView(mContext.getApplicationContext());
        mTripVideoView.initShow(false);
        mTripVideoView.setRoundLayoutRadius(6f);
        RelativeLayout.LayoutParams videoParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mTripVideoView.setLayoutParams(videoParams);

        mTripVideoViewLayout = (FrameLayout) findViewById(R.id.tab_video_widget_bg);


        int step = ActivityHandler.getInstance().getResources().getDimensionPixelOffset(R.dimen.recommend_icon_step);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.leftMargin = (tabItemDataInfo.x - step) * CommonFunction.getScreenSize().x / 1920;
        layoutParams.rightMargin = ActivityHandler.getInstance().getResources().getDimensionPixelOffset(R.dimen.tab_button_scrollview_padding) * CommonFunction.getScreenSize().x / 1920;
        layoutParams.topMargin = (tabItemDataInfo.y - step) * CommonFunction.getScreenSize().y / 1080;
        layoutParams.width = (tabItemDataInfo.width + step * 2) * CommonFunction.getScreenSize().x / 1920;
        layoutParams.height = (tabItemDataInfo.height + step * 2) * CommonFunction.getScreenSize().y / 1080;

        mTripVideoViewLayout.setLayoutParams(layoutParams);
        mTripVideoViewLayout.setTag(R.id.track_view_scale_x, 1.0f);
        mTripVideoViewLayout.setTag(R.id.track_view_scale_y, 1.0f);
        mTripVideoViewLayout.setFocusable(true);
        mTripVideoViewLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject source = new JSONObject();
                source.put(Constant.BIG_DATA_VALUE_KEY_SOURCE, Constant.BIG_DATA_VALUE_SOURCE_TJ);
                source.put(Constant.BIG_DATA_VALUE_KEY_NAME, tabItemDataInfo.tabName);
                source.put(Constant.BIG_DATA_VALUE_KEY_LOCATION, String.valueOf(tabItemDataInfo.layoutId));
                LaunchHelper.startVideoActivity(ActivityHandler.getInstance(), DetailConstant.START_TYPE_WINDOW, tabItemDataInfo.goodsID, source.toJSONString());
            }
        });
        mTripVideoViewLayout.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        return true;
                }
                return false;
            }
        });

        mTripVideoViewLayout.removeAllViews();
        mTripVideoViewLayout.addView(mTripVideoView);

        if (windowListener == null) {
            windowListener = new SmallWindowListener();
            WindowPlayer.getInstance().setWindowListener(windowListener);
        }
        if (mStartVideoRunnable == null) {
            mStartVideoRunnable = new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "trip-->TabGroup-->ShowVideoWidget-->mStartVideoRunnable");
                    WindowPlayer.getInstance().startVideoView();
                }
            };
        }
        WindowPlayer.getInstance().setTripVideoView(mTripVideoView);
        WindowPlayer.getInstance().setIsFullScreen(false);
        if (WindowPlayer.getInstance().getVideosBeanList() == null || WindowPlayer.getInstance().getVideosBeanList().size() == 0) {
            LogUtils.d(TAG, "trip-->TabGroup-->DrawVideoWidget-->getAllVideos 100");
            HttpHelper.getInstance(mContext).getAllVideos(1, 100);
        }
        ShowVideoWidget(isFirstPage, isScrollStart, isScrollComplete, showVideo);
    }

    /**
     * 显示小窗口
     */
    public void ShowVideoWidget(boolean isFirstPage, boolean isScrollStart, boolean isScrollComplete, boolean showVideo) {
        Log.d(TAG, "trip-->ShowVideoWidget-->boolean:" + isFirstPage + " " + isScrollStart + " " + isScrollComplete + " " + showVideo);
        if (mTripVideoViewLayout != null) {
            if (isFirstPage && isScrollStart && isScrollComplete && showVideo) {
                mTripVideoViewLayout.setVisibility(VISIBLE);
                WindowPlayer.getInstance().setStop(false);
                Log.d(TAG, "trip-->TabGroup-->ShowVideoWidget-->prepared:" + WindowPlayer.getInstance().isPrepared());
                if (!WindowPlayer.getInstance().isPrepared()) {
                    if (WindowPlayer.getInstance().getVideosBeanList() != null
                            && WindowPlayer.getInstance().getVideosBeanList().size() != 0) {
                        ActivityHandler.getUIHandler().postDelayed(mStartVideoRunnable, 1000);
                    }
                } else if (!WindowPlayer.getInstance().isPlaying()) {
                    WindowPlayer.getInstance().resumePlayer();
                }
            } else {
                mTripVideoViewLayout.setVisibility(INVISIBLE);
                ActivityHandler.getUIHandler().removeCallbacks(mStartVideoRunnable);
                Log.d(TAG, "trip-->TabGroup-->ShowVideoWidget-->isPlaying:" + WindowPlayer.getInstance().isPlaying());
                if (WindowPlayer.getInstance().isPlaying()) {
                    WindowPlayer.getInstance().pausePlayer();
                } else {
                    WindowPlayer.getInstance().setStop(true);
                }
            }
        }
    }

    /**
     * 调用小窗口onResume
     */
    public void onResumeVideoWidget() {
        DrawVideoWidget(mKKTabItemDataInfo);
    }

    /**
     * 调用小窗口onPause
     */
    public void onPauseVideoWidget() {
        Log.d(TAG, "detail-->TabGroup-->onPauseVideoWidget");
        ActivityHandler.getUIHandler().removeCallbacks(mStartVideoRunnable);
        WindowPlayer.getInstance().releasePlayer();
        mTripVideoView = null;
    }

    /**
     * 在当前tab列表的末尾添加tab
     *
     * @param tName    tab的名称，为tab的唯一标识
     * @param tContent tab的内容
     * @param tButton  tab的button
     */
    public void addTab(String tName, TabContent tContent, TabButton tButton) {
        addTab(tName, null, tContent, tButton, true);
    }

    /**
     * 在某个tab之后添加tab
     *
     * @param tName    tab的名称，为tab的唯一标识
     * @param tFlag    定位的tab标识，若该tab不存在则直接将tab添加到列表末尾
     * @param tContent tab的内容
     * @param tButton  tab的button
     * @param tAfter   是否在标识之后，true为后，false为前
     */
    public void addTab(String tName, String tFlag, final TabContent tContent, TabButton tButton, boolean tAfter) {
        if (tName == null || tName.length() == 0) {
            throw new IllegalArgumentException("Tab's name can't be null or empty");
        }

        if (tContent == null) {
            throw new IllegalArgumentException("Tab's content can't be null");
        }

        if (tButton == null) {
            throw new IllegalArgumentException("Tab's button can't be null");
        }

        // 检查是否重复添加
        if (isContainTab(tName)) {
            Log.d("TabGroup", "Already exits tab \'" + tName + "\',please remove it first");
            return;
        }

        Tab tTab = new Tab();
        tTab.setTabName(tName);
        tTab.setTabContent(tContent);
        tTab.setTabButton(tButton);

        int tIndex = mTabList.size();

        // 根据定位寻找索引
        if (tFlag != null) {
            for (int i = 0; i < mTabList.size(); i++) {
                if (mTabList.get(i).getTabName().equals(tFlag)) {
                    if (tAfter) {
                        tIndex = i + 1;
                    } else {
                        tIndex = i;
                    }
                    break;
                }
            }
        }

        mTabList.add(tIndex, tTab);

        mButtonLayout.addView(tButton.getView(), tIndex);

        // 添加标记，方便做焦点控制
        tButton.getView().setTag(R.id.is_tab_button, true);

        mAdapter.notifyDataSetChanged();
        tTab.tabAdded();
    }

    /**
     * 删除指定的tab
     *
     * @param tName tab的名字标识
     */
    public void removeTab(String tName) {
        if (tName == null || tName.length() == 0) {
            return;
        }

        Tab tTab;
        for (int i = 0; i < mTabList.size(); i++) {
            tTab = mTabList.get(i);
            if (tTab.getTabName().equals(tName)) {
                mButtonLayout.removeView(tTab.getTabButton().getView());
                mTabList.remove(i);
                mAdapter.notifyDataSetChanged();
                tTab.tabRemoved();
                return;
            }
        }
    }

    /**
     * 移除所有的tab
     */
    public void removeAllTab() {
        Tab tTab;
        for (int i = 0; i < mTabList.size(); ) {
            tTab = mTabList.remove(i);
            mButtonLayout.removeView(tTab.getTabButton().getView());
            tTab.tabRemoved();
        }
        mCurrentTab = null;
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 设置当前选中页
     *
     * @param tName          tab的名字标识
     * @param tNeedAnimation 是否需要切页动画, true需要，false无动画
     */
    public void setCurrentTab(String tName, boolean tNeedAnimation) {
        mIsEdgeChange = false;

        Tab tTab;
        for (int i = 0; i < mTabList.size(); i++) {
            tTab = mTabList.get(i);
            if (tTab.getTabName().equals(tName)) {
                if (mCurrentTab == null) {
                    mCurrentTab = tTab;
                    mCurrentTab.tabSelected(true);
                }
                if (getTabContent(mTabList.get(i).getTabName()).getView() instanceof HorizontalPageScrollView) {
                    ((HorizontalPageScrollView) getTabContent(mTabList.get(i).getTabName()).getView()).setScrollViewListener(mScrollViewListener);
                }
                mContentViewPager.setCurrentItem(i, tNeedAnimation);
                return;
            }
        }
    }

    public View getCurrentTabButtonView() {
        if (mCurrentTab == null && mTabList.size() > 0) {
            mCurrentTab = mTabList.get(0);
        }

        if (mCurrentTab != null) {
            return mCurrentTab.getTabButton().getView();
        }

        return null;
    }

    /**
     * 是否已存在指定的tab
     *
     * @param tName tab名字标识
     * @return true已经存在，false不存在
     */
    public boolean isContainTab(String tName) {
        if (tName == null || tName.length() == 0) {
            return false;
        }

        for (int i = 0; i < mTabList.size(); i++) {
            if (mTabList.get(i).getTabName().equals(tName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 获取指定tab的内容
     *
     * @param tName
     * @return
     */
    public TabContent getTabContent(String tName) {
        if (tName == null || tName.length() == 0) {
            return null;
        }

        for (int i = 0; i < mTabList.size(); i++) {
            if (mTabList.get(i).getTabName().equals(tName)) {
                return mTabList.get(i).getTabContent();
            }
        }

        return null;
    }

    /**
     * 获取指定tab的button
     *
     * @param tName
     * @return
     */
    public TabButton getTabButton(String tName) {
        if (tName == null || tName.length() == 0) {
            return null;
        }

        for (int i = 0; i < mTabList.size(); i++) {
            if (mTabList.get(i).getTabName().equals(tName)) {
                return mTabList.get(i).getTabButton();
            }
        }

        return null;
    }

    public ViewGroup getTabIconGroupView() {
        return mButtonLayout;
    }

    /**
     * 刷新tab的内容，实际上只是调用viewpager-notifyDataSetChange方法，不到非不得已不建议主动调用该方法
     */
    public void updateTabContentView() {
        mAdapter.notifyDataSetChanged();
    }

    /**
     * ViewPager的适配器
     */
    private class ContentAdapter extends PagerAdapter {

        private ArrayList<Tab> mList;

        public ContentAdapter(ArrayList<Tab> tList) {
            mList = tList;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View tView = mList.get(position).getTabContent().getView();
            container.addView(tView);
            return tView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    /**
     * tab逻辑，用于实现tabContent和tabButton的对应关联
     */
    public class Tab implements TabStateCallback {

        private String mTabName;
        private TabButton mTabButton;
        private TabContent mTabContent;

        public String getTabName() {
            return mTabName;
        }

        public void setTabName(String mTabName) {
            this.mTabName = mTabName;
        }

        public TabButton getTabButton() {
            return mTabButton;
        }

        public void setTabButton(TabButton mTabButton) {
            this.mTabButton = mTabButton;
        }

        public TabContent getTabContent() {
            return mTabContent;
        }

        public void setTabContent(TabContent mTabContent) {
            this.mTabContent = mTabContent;
        }

        @Override
        public void tabAdded() {
            mTabContent.tabAdded();
            mTabButton.tabAdded();
        }

        @Override
        public void tabRemoved() {
            mTabContent.tabRemoved();
            mTabButton.tabRemoved();
        }

        @Override
        public void tabSelected(boolean tFlag) {
            mTabContent.tabSelected(tFlag);
            mTabButton.tabSelected(tFlag);
        }

        @Override
        public boolean tabEdgeChange(boolean tIsIn, int tDirection, Rect tRect) {
            mTabContent.tabEdgeChange(tIsIn, tDirection, tRect);
            mTabButton.tabEdgeChange(tIsIn, tDirection, tRect);
            return true;
        }
    }

    public boolean isChildFocused() {
        for (int i = 0; i < mButtonLayout.getChildCount(); i++) {
            if (mButtonLayout.getChildAt(i).isFocused())
                return true;
        }
        return false;
    }

    private HorizontalPageScrollView.ScrollViewListener mScrollViewListener = new HorizontalPageScrollView.ScrollViewListener() {
        @Override
        public void onScrollChanged(HorizontalPageScrollView scrollView, int x, int y, int oldX, int oldY) {
            LogUtils.d(TAG, "onScrollChanged: scrollView:" + scrollView.getScrollX());
            if (scrollView.getScrollX() == 0) {
                isScrollStart = true;
            } else {
                isScrollStart = false;
            }
            ShowVideoWidget(isFirstPage, isScrollStart, true, showVideo);
//            ActivityHandler.getInstance().refreshTrack();
        }
    };

    private class SmallWindowListener implements ISmallWindow {
        @Override
        public void onDataSucceed() {
            LogUtils.d(TAG, "detail-->TabGroup-->onDataSucceed");
            ActivityHandler.getUIHandler().postDelayed(mStartVideoRunnable, 1000);
        }
    }


}
