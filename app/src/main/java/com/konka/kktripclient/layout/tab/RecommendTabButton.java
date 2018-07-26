package com.konka.kktripclient.layout.tab;

import com.konka.kktripclient.layout.track.TrackBaseActivity;
import com.konka.kktripclient.R;
import com.konka.kktripclient.layout.tab.base.TabButton;
import com.konka.kktripclient.layout.tab.base.TabContent;
import com.konka.kktripclient.layout.util.ActivityHandler;
import com.konka.kktripclient.layout.util.LayoutConstant;

import android.graphics.Rect;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @author wu, The_one
 */
public class RecommendTabButton extends TabButton {
    /**
     * Tab布局
     */
    private FrameLayout mFrameLayout;
    private TextView mTextView;
    /**
     * 唯一标识Tab
     */
    private String mTitle;
    private String mBackgroundURL;
    private boolean isFirst = false;
    private boolean isLast = false;
    private boolean isFocus = false;
    private static View mCurrentSelected = null;
    /**
     * 增加额外的高度，防止文字被切边
     */
    private int extraHeight = 1;

    public RecommendTabButton(String title, String backgroundURL, boolean first, boolean last) {
        mTitle = title;
        mBackgroundURL = backgroundURL;
        isFirst = first;
        isLast = last;

        ActivityHandler.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initLayout();
                initView();
            }
        });
    }

    public synchronized void initLayout() {
        if (mFrameLayout == null)
            mFrameLayout = (FrameLayout) LayoutInflater.from(ActivityHandler.getInstance()).inflate(R.layout.recommend_tab_button, null);
    }

    public synchronized void initView() {
        mTextView = (TextView) mFrameLayout.findViewById(R.id.tab_button_name);
        mTextView.setText(mTitle.trim());
        mTextView.setHeight(ActivityHandler.getInstance().getResources().getDimensionPixelOffset(R.dimen.tab_button_name_size) + extraHeight);

        mFrameLayout.setTag(TrackBaseActivity.TRACK_TYPE_KEY, LayoutConstant.TRACK_TYPE_TAB_BUTTON);

        mFrameLayout.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean focus) {
                if (mCurrentSelected != null && mCurrentSelected != v) {
                    mCurrentSelected.requestFocus();
                    mCurrentSelected = null;
                    return;
                }

                if (focus) {
                    isFocus = true;
                    TabGroup.sInstance.setCurrentTab(mTitle, true);
                    mTextView.setTextColor(ActivityHandler.getInstance().getResources().getColor(R.color.main_accent));
                    mTextView.setSelected(true);

                    scrollToBegin();
                } else {
                    isFocus = false;
                    mTextView.setTextColor(ActivityHandler.getInstance().getResources().getColor(R.color.main_primary));
                    mTextView.setSelected(false);
                }

                isLostFocus();
            }
        });
        mFrameLayout.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (isFirst) {
                    if (i == KeyEvent.KEYCODE_DPAD_LEFT) return true;
                }
                if (isLast) {
                    if (i == KeyEvent.KEYCODE_DPAD_RIGHT) return true;
                }
                return false;
            }
        });
    }

    private void isLostFocus() {
        try {
            boolean focus = false;

            ArrayList<TabGroup.Tab> tabList = TabGroup.sInstance.getCurrentTabList();
            for (TabGroup.Tab tab : tabList) {
                TabButton tabButton = tab.getTabButton();
                if (tabButton.getView().hasFocus()) {
                    focus = true;
                    break;
                }
            }
            if (!focus) {
                mCurrentSelected = mFrameLayout;
            } else {
                mCurrentSelected = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void scrollToBegin() {
        try {
            TabContent content = TabGroup.sInstance.getTabContent(mTitle);
            View view = content.getView();
            if (view instanceof HorizontalScrollView) {
                HorizontalScrollView scrollView = (HorizontalScrollView) view;
                if (scrollView.getScrollX() > 0) {
                    scrollView.scrollTo(0, 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return mTitle;
    }

    @Override
    public void tabSelected(final boolean tFlag) {
        ActivityHandler.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tFlag) {
                    mTextView.setAlpha(1);
                    mTextView.setHeight(ActivityHandler.getInstance().getResources().getDimensionPixelOffset(R.dimen.tab_button_name_size_big) + extraHeight);
                    mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ActivityHandler.getInstance().getResources().getDimensionPixelOffset(R.dimen.tab_button_name_size_big));
                    mTextView.getPaint().setFakeBoldText(true);
                    if (isFocus) {
                        mTextView.setTextColor(ActivityHandler.getInstance().getResources().getColor(R.color.main_accent));
                    } else {
                        mTextView.setTextColor(ActivityHandler.getInstance().getResources().getColor(R.color.main_primary));
                    }
                    isLostFocus();
                } else {
                    mTextView.setAlpha(0.8f);
                    mTextView.setHeight(ActivityHandler.getInstance().getResources().getDimensionPixelOffset(R.dimen.tab_button_name_size) + extraHeight);
                    mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ActivityHandler.getInstance().getResources().getDimensionPixelOffset(R.dimen.tab_button_name_size));
                    mTextView.getPaint().setFakeBoldText(false);
                    mTextView.setTextColor(ActivityHandler.getInstance().getResources().getColor(R.color.main_primary));
                }
            }
        });

    }

    @Override
    public View getView() {
        if (mFrameLayout == null)
            ActivityHandler.getInstance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initLayout();
                    initView();
                }
            });

        return mFrameLayout;
    }

    @Override
    public boolean tabEdgeChange(boolean tIsIn, int tDirection, Rect tRect) {
        return false;
    }

    @Override
    public void tabAdded() {

    }

    @Override
    public void tabRemoved() {

    }
}
