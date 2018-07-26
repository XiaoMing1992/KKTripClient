package com.konka.kktripclient.layout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 焦点控件顶层绘制
 *
 * @author
 */
public class OverViewFrameLayout extends FrameLayout {
    private int mTopPosition = -1;

    public OverViewFrameLayout(Context context) {
        super(context);
        setChildrenDrawingOrderEnabled(true);
    }

    public OverViewFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setChildrenDrawingOrderEnabled(true);
    }

    public OverViewFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setChildrenDrawingOrderEnabled(true);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (i == 0) {
            mTopPosition = -1; // 重绘时使标记位失效
        }

        View tView = getChildAt(i);
        if (tView != null) {
            if (tView.isFocused()) { // 焦点位置最后绘制，保持在顶层
                mTopPosition = i;
                return childCount - 1;
            } else {
                if (i == childCount - 1 && mTopPosition != -1) { // 最后一个位置与焦点位置交替
                    return mTopPosition;
                }
            }
        }

        return i;
    }
}
