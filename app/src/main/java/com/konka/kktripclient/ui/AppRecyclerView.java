package com.konka.kktripclient.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;

import com.konka.kktripclient.R;


/**
 * 主要提供如下几个功能, 均基于水平方向，如有涉及垂直方向的问题，再做修改：<br/>
 * 1.多种滚动效果，具体参考 setChangeType(int)<br/>
 * 2.item的绘制层次问题，当前焦点item将会顶层绘制<br/>
 * 3.解决边缘按键不切换或者焦点乱飞的问题，具体参考setEdgeOffset(int)
 * @author YangDeZun
 *
 */
public class AppRecyclerView extends RecyclerView {
	private int mTopPosition = -1;
	public static final int CHANGE_TYPE_PAGE = 0;
	public static final int CHANGE_TYPE_CENTER = 1;
	public static final int CHANGE_TYPE_NONE = 3;
	private int mChangeType = CHANGE_TYPE_NONE;
	private static final int DEFAULT_EDGE_OFFSET = 60;
	private int mEdgeOffset = DEFAULT_EDGE_OFFSET;

	public AppRecyclerView(Context arg0) {
		super(arg0);
		setChildrenDrawingOrderEnabled(true);
	}

	public AppRecyclerView(Context arg0, AttributeSet arg1) {
		super(arg0, arg1);
		setChildrenDrawingOrderEnabled(true);
		TypedArray a= getContext().obtainStyledAttributes(arg1, R.styleable.appRecyclerView);
		mChangeType=a.getInt(R.styleable.appRecyclerView_changeType,CHANGE_TYPE_NONE);
		Log.i("mChangeType",mChangeType+"");
	}

	public AppRecyclerView(Context arg0, AttributeSet arg1, int arg2) {
		super(arg0, arg1, arg2);
		setChildrenDrawingOrderEnabled(true);
	}

	/**
	 * 设置切换效果，默认为切页。<br/>
	 * 切页：CHANGE_TYPE_PAGE<br/>
	 * 居中：CHANGE_TYPE_CENTER<br/>
	 * 原生：CHANGE_TYPE_NONE
	 * 
	 * @param tType
	 */
	public void setChangeType(int tType) {
		mChangeType = tType;
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

	@Override
	public boolean requestChildRectangleOnScreen(View child, Rect rect, boolean immediate) {
		switch (mChangeType) {
		case CHANGE_TYPE_PAGE:
			return requestVerticalPageChange(child, rect);
		case CHANGE_TYPE_CENTER:
			return requestHorizontalCenterChange(child, rect);
		case CHANGE_TYPE_NONE:
			return requestVerticalCenterChange(child, rect);
		default:
			return super.requestChildRectangleOnScreen(child, rect, immediate);
		}

	}

	/**
	 * 设置边缘的差分滚动距离，解决在边缘处按键无法调到下一个位置导致的焦点不可控问题;<br/>
	 * 建议设置略大于item之间的间隔值, 默认值为60px<br/>
	 * @param tOffset
	 */
	public void setEdgeOffset(int tOffset){
		mEdgeOffset = tOffset;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
//			if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN){
//				return true;
//			}
			View tView = getFocusedChild();
			if (tView != null) {
				switch (event.getKeyCode()) {
					case KeyEvent.KEYCODE_DPAD_UP:
						if (FocusFinder.getInstance().findNextFocus(this, tView, View.FOCUS_UP) == null) {
							int tPosition = getChildAdapterPosition(tView);
							if (tPosition != 0) {
								scrollBy(0,-mEdgeOffset);
								if (event.getRepeatCount() > 1){
									return true;
								}
							}
						}
						break;
					case KeyEvent.KEYCODE_DPAD_DOWN:
						if (FocusFinder.getInstance().findNextFocus(this, tView, View.FOCUS_DOWN) == null) {
							int tPosition = getChildAdapterPosition(tView);
							if (tPosition != getAdapter().getItemCount() - 1) {
								scrollBy(0,mEdgeOffset);
								if (event.getRepeatCount() > 1){
									return true;
								}
							}
						}
						break;
					default:
						break;
				}

			}
		}

		return super.dispatchKeyEvent(event);
	}

	/**
	 * 水平切页效果
	 * 
	 * @param child
	 * @param rect
	 * @return
	 */
	private boolean requestHorizontalPageChange(View child, Rect rect) {
		final int parentLeft = getPaddingLeft();
		final int parentRight = getWidth() - getPaddingRight();
		final int childLeft = child.getLeft() + rect.left - child.getScrollX();
		final int childRight = childLeft + rect.width();

		final int offScreenLeft = Math.min(0, childLeft - parentLeft);
		final int offScreenRight = Math.max(0, childRight - parentRight);

		// Favor the "start" layout direction over the end when bringing one
		// side or the other
		// of a large rect into view. If we decide to bring in end because start
		// is already
		// visible, limit the scroll such that start won't go out of bounds.
		int dx;
		if (getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {// 右到左的局部
			dx = offScreenRight != 0 ? offScreenRight : Math.max(offScreenLeft, childRight - parentRight);
		} else {
			dx = offScreenLeft != 0 ? offScreenLeft : Math.min(childLeft - parentLeft, offScreenRight);
		}

		if (dx != 0) {
			if (dx > 0) {
				dx = dx + getWidth() - child.getWidth() - child.getWidth() / 2;
			} else {
				dx = dx - getWidth() + child.getWidth() + child.getWidth() / 2;
			}

			this.smoothScrollBy(dx, 0);

			return true;
		}
		return false;
	}


	private boolean requestVerticalPageChange(View child, Rect rect) {
		final int parentTop = getPaddingTop();
		final int parentBottom = getHeight() - getPaddingBottom();
		final int childTop = child.getTop() + rect.top - child.getScrollY();
		final int childBottom = childTop + rect.height();

		final int offScreenTop = Math.min(0, childTop - parentTop);
		final int offScreenBottom = Math.max(0, childBottom - parentBottom);

		// Favor the "start" layout direction over the end when bringing one
		// side or the other
		// of a large rect into view. If we decide to bring in end because start
		// is already
		// visible, limit the scroll such that start won't go out of bounds.
		int dy;
		if (getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {// 右到左的局部
			dy = offScreenBottom != 0 ? offScreenBottom : Math.max(offScreenTop, childBottom - parentBottom);
		} else {
			dy = offScreenTop != 0 ? offScreenTop : Math.min(childTop - parentTop, offScreenBottom);
		}

		if (dy != 0) {
			if (dy > 0) {
				dy = dy + getHeight() - child.getHeight() - child.getHeight() / 2;
			} else {
				dy = dy - getHeight() + child.getHeight() + child.getHeight() / 2;
			}

			this.smoothScrollBy(0, dy);

			return true;
		}
		return false;
	}


	/**
	 * 竖直居中效果
	 * 
	 * @param child
	 * @param rect
	 * @return
	 */
	private boolean requestVerticalCenterChange(View child, Rect rect) {
		final int parentTop = getPaddingTop();
		final int parentBottom = getHeight() - getPaddingBottom();
		final int childTop = child.getTop() + rect.top - child.getScrollY();
		final int childBottom = childTop + rect.height();

		int dy = 0;
		final int offScreenTop = childTop - (parentTop + getHeight() / 2 - child.getHeight() / 2);
		final int offScreenBottom = childBottom - (parentBottom + getHeight() / 2 - child.getHeight() / 2);

		if (getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) { // 右到左的局部
			dy = offScreenBottom;
		} else {
			dy = offScreenTop;
		}

		if (dy != 0) {
			this.smoothScrollBy(0, dy);
			return true;
		}
		return false;
	}



	/**
	 * 水平居中效果
	 *
	 * @param child
	 * @param rect
	 * @return
	 */
	private boolean requestHorizontalCenterChange(View child, Rect rect) {
		final int parentLeft = getPaddingLeft();
		final int parentRight = getWidth() - getPaddingRight();
		final int childLeft = child.getLeft() + rect.left - child.getScrollX();
		final int childRight = childLeft + rect.width();

		int dx = 0;
		final int offScreenLeft = childLeft - (parentLeft + getWidth() / 2 - child.getWidth() / 2);
		final int offScreenRight = childRight - (parentRight + getWidth() / 2 - child.getWidth() / 2);

		if (getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) { // 右到左的局部
			dx = offScreenRight;
		} else {
			dx = offScreenLeft;
		}

		if (dx != 0) {
			this.smoothScrollBy(dx, 0);
			return true;
		}
		return false;
	}
}
