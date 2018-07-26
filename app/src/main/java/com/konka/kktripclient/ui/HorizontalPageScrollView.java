package com.konka.kktripclient.ui;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

import com.konka.kktripclient.R;

/**
 *
 * 1.设置边缘差分距离，解决边界焦点元素放大后被截掉的问题，根据布局需要自行调整。具体参考{@link #setEdgeOffset(int)}<br/>
 * 2.提供切页效果，具体参考覆盖的函数{@link #computeScrollDeltaToGetChildRectOnScreen(Rect)}
 * @author YangDeZun
 *
 */
public class HorizontalPageScrollView extends HorizontalScrollView {

    private static final int DEFAULT_EDGE_OFFSET = 80;
    private int mEdgeOffset = DEFAULT_EDGE_OFFSET;

    public HorizontalPageScrollView(Context context) {
        super(context);

        mEdgeOffset = context.getResources().getDimensionPixelOffset(R.dimen.HorizontalPageScrollView_DEFAULT_EDGE_OFFSET);
    }

    public HorizontalPageScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mEdgeOffset = context.getResources().getDimensionPixelOffset(R.dimen.HorizontalPageScrollView_DEFAULT_EDGE_OFFSET);
    }


    /**
     * 设置边缘间隙值，默认为80px
     * @param tOffSet
     */
    public void setEdgeOffset(int tOffSet){
        mEdgeOffset = tOffSet;
    }

    /**
     * Compute the amount to scroll in the X direction in order to get
     * a rectangle completely on the screen (or, if taller than the screen,
     * at least the first screen size chunk of it).
     *
     * @param rect The rect.
     * @return The scroll delta.
     */
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        /**
         *
         * 切页处理原理：
         * 和recyclerView不同，scrollview里面所有的元素都是可见的，所以不需要对边界元素焦点控制等做特殊处理。
         * scrollview只会对未完全显示在范围内的子元素做滚动处理，
         * 所以只需要对滚动偏移量scrollXDelta的计算进行处理即可，并没有太复杂的计算处理。
         *
         */

        if (getChildCount() == 0) return 0;

        int width = getWidth();
        int screenLeft = getScrollX();
        int screenRight = screenLeft + width;
        int fadingEdge = getHorizontalFadingEdgeLength();
        if (rect.left > 0) {
            screenLeft += fadingEdge;
        }
        if (rect.right < getChildAt(0).getWidth()) {
            screenRight -= fadingEdge;
        }
        int scrollXDelta = 0;
        if (rect.right > screenRight && rect.left > screenLeft) {
            scrollXDelta += (rect.left - screenLeft) - mEdgeOffset;   //修改
            int right = getChildAt(0).getRight();
            int distanceToRight = right - screenRight;
            scrollXDelta = Math.min(scrollXDelta, distanceToRight);

        } else if (rect.left < screenLeft && rect.right < screenRight) {
            scrollXDelta -= (screenRight - rect.right) - mEdgeOffset;  //修改
            scrollXDelta = Math.max(scrollXDelta, -getScrollX());
        }
        return scrollXDelta;
    }
}
