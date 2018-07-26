package com.konka.kktripclient.layout.tab;

import android.graphics.Rect;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.konka.kktripclient.R;
import com.konka.kktripclient.layout.data.KKTabDataInfo;
import com.konka.kktripclient.layout.data.KKTabItemDataInfo;
import com.konka.kktripclient.layout.tab.base.TabContent;
import com.konka.kktripclient.layout.tab.base.TabStateCallback;
import com.konka.kktripclient.layout.util.ActivityHandler;
import com.konka.kktripclient.layout.util.CommonFunction;
import com.konka.kktripclient.layout.view.HorizontalPageScrollView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RecommendPage extends TabContent {
    /**
     * 页面布局
     */
    private FrameLayout mFrameLayout;
    private HorizontalPageScrollView mHorizontalScrollView;
    /**
     * 页面内Icons
     */
    private List<RecommendIcon> mRecommendIconList = new ArrayList<>();
    private List<RecommendIcon> mIconLeftEdgeList = new ArrayList<>();
    private List<RecommendIcon> mIconRightEdgeList = new ArrayList<>();
    /**
     * Tab数据
     */
    private KKTabDataInfo mTabDataInfo;
    /**
     * Tab页面下的数据
     */
    private List<KKTabItemDataInfo> mTabItemDataInfoList = new ArrayList<>();

    private boolean isDraw = false;

    public RecommendPage(final KKTabDataInfo dataInfo) {
        mTabDataInfo = dataInfo;
        ActivityHandler.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initLayout();
                initView();
            }
        });
    }

    public synchronized void initLayout() {
        if (mHorizontalScrollView == null)
            mHorizontalScrollView = (HorizontalPageScrollView) LayoutInflater.from(ActivityHandler.getInstance())
                    .inflate(R.layout.recommend_page, null);
    }

    public synchronized void initView() {
        mFrameLayout = (FrameLayout) mHorizontalScrollView.findViewById(R.id.recommend_page_layout);
        mFrameLayout.setClipChildren(false);
    }

    public void resetLayout(KKTabDataInfo dataInfo) {
        isDraw = false;
        mRecommendIconList.clear();
        mTabDataInfo = dataInfo;
        ActivityHandler.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mFrameLayout.removeAllViews();
            }
        });
    }

    public boolean isDraw() {
        return isDraw;
    }

    public void setTabItemDataInfoList(List<KKTabItemDataInfo> dataInfo) {
        mTabItemDataInfoList = dataInfo;
//		computeLocation();
        if (dataInfo.size() == 0)
            drawFail();
        else
            draw();
    }

    private void draw() {
        isDraw = true;
        ActivityHandler.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mFrameLayout.removeAllViews();
                mRecommendIconList.clear();
                for (KKTabItemDataInfo dataInfo : mTabItemDataInfoList) {
                    RecommendIcon recommendIcon = new RecommendIcon(dataInfo);
                    recommendIcon.Draw(mFrameLayout);
                    mRecommendIconList.add(recommendIcon);
                }
                computeEdgeIcon();
            }
        });
    }

    public void drawFail() {
        isDraw = true;
        ActivityHandler.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mFrameLayout.removeAllViews();
                LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(ActivityHandler.getInstance()).inflate(R.layout.error_page, null);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                params.gravity = Gravity.CENTER;
                params.leftMargin = 520 * CommonFunction.getScreenSize().x / 1920;
                mFrameLayout.addView(linearLayout, params);
            }
        });
    }

    private int getMostLeft() {
        int x = 0;
        for (RecommendIcon data : mRecommendIconList) {
            if (x == 0) x = data.getItemData().x;
            else if (x > data.getItemData().x) x = data.getItemData().x;
        }
        return x;
    }

    private int getMostRight() {
        int x = 0;
        for (RecommendIcon data : mRecommendIconList) {
            if (x == 0) x = data.getItemData().x;
            else if (x < data.getItemData().x) x = data.getItemData().x;
        }
        return x;
    }

    public static final Comparator<RecommendIcon> TAB_POSITION = new Comparator<RecommendIcon>() {
        @Override
        public int compare(RecommendIcon a, RecommendIcon b) {
            if (a.getItemData().y > b.getItemData().y)
                return 1;
            if (a.getItemData().y < b.getItemData().y)
                return -1;
            return 0;
        }
    };

    public void computeEdgeIcon() {
        mIconLeftEdgeList.clear();
        mIconRightEdgeList.clear();

        int mostLeft = getMostLeft();
        int mostRight = getMostRight();
        for (RecommendIcon data : mRecommendIconList) {
            if (mostLeft >= data.getItemData().x) {
                mIconLeftEdgeList.add(data);
            }
            if (mostRight <= data.getItemData().x) {
                mIconRightEdgeList.add(data);
            }
        }
        Collections.sort(mIconLeftEdgeList, TAB_POSITION);
        Collections.sort(mIconRightEdgeList, TAB_POSITION);
    }

    //计算矩形中点坐标
//	public void computeLocation() {
//		locations.clear();
//		for (KKTabItemDataInfo data : mTabItemDataInfoList) {
//			Location location = new Location(data.x, data.x + data.width, data.y + 183,
//					data.y + 183 + data.height);
//			locations.add(location);
//		}
//	}

//	public int computeDistance(Location location) {
//		int distance = Integer.MAX_VALUE;
//		int order = 0;
//		for (int i = 0; i < locations.size(); i++) {
//			int result = (locations.get(i).x - location.x) * (locations.get(i).x - location.x)
//					+ (locations.get(i).y - location.y) * (locations.get(i).y - location.y);
//			if (result < distance) {
//				distance = result;
//				order = i;
//			}
//		}
//		return order;
//	}

    public String getTitle() {
        return mTabDataInfo.menuItemName;
    }

    @Override
    public View getView() {
        if (mHorizontalScrollView == null) {
            ActivityHandler.getInstance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initLayout();
                    initView();
                }
            });
        }
        return mHorizontalScrollView;
    }

    private RecommendIcon getEdgeChangeFocusIcon(List<RecommendIcon> list, Rect rect) {
        RecommendIcon focus = null;
        if (list.size() == 1) {
            focus = list.get(0);
        } else {
            for (RecommendIcon data : list) {
                int height = data.getItemData().y + data.getItemData().height;
                if (rect.top <= height) {
                    focus = data;
                    break;
                }
            }
            if (focus == null) focus = list.get(0);
        }
        return focus;
    }

    @Override
    public boolean tabEdgeChange(boolean tIsIn, int tDirection, final Rect tRect) {
        if (tRect == null)
            return false;
        if (mRecommendIconList.size() == 0)
            return false;
        if (TabStateCallback.PAGE_DIRECTION_LTR_IN == tDirection) {
            RecommendIcon focus = getEdgeChangeFocusIcon(mIconLeftEdgeList, tRect);
            if (focus != null) {
                focus.requestFocus();
            }
            return true;
        }
        if (TabStateCallback.PAGE_DIRECTION_RTL_IN == tDirection) {
            RecommendIcon focus = getEdgeChangeFocusIcon(mIconRightEdgeList, tRect);
            if (focus != null) {
                focus.requestFocus();
            }
            return true;
        }
        return false;
    }

    @Override
    public void tabSelected(boolean tFlag) {
        if (tFlag) {

        } else {

        }
    }

    @Override
    public void tabAdded() {

    }

    @Override
    public void tabRemoved() {

    }

//	private class Location {
//		public Location(int left, int right, int top, int bottom) {
//			this.x = (left + right) / 2;
//			this.y = (top + bottom) / 2;
//		}
//
//		public int x;
//		public int y;
//	}
}