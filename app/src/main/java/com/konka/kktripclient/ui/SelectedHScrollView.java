package com.konka.kktripclient.ui;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

public class SelectedHScrollView extends HorizontalScrollView {
	public SelectedHScrollView(Context context) {
		this(context, null);
	}

	public SelectedHScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SelectedHScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void autoScrollWhenChildrenSelected(View children) {
		if (children == null || children.hasFocus()) {
			return;
		}

		View tFirstChildren = this.getChildAt(0);
		if (tFirstChildren == null) {
			return;
		}
		Rect tRect = new Rect();
		children.getFocusedRect(tRect);
		try {
			this.offsetDescendantRectToMyCoords(children, tRect);
		} catch (Exception e) {
			return;
		}

		int tDistance = computeScrollDeltaToGetChildRectOnScreen(tRect);

		if (tDistance != 0) {
			if (this.isSmoothScrollingEnabled()) {
				smoothScrollBy(tDistance, 0);
			} else {
				scrollBy(tDistance, 0);
			}
		}
	}
}
