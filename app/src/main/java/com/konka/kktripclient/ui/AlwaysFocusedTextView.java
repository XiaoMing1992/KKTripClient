package com.konka.kktripclient.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by The_one on 2017-5-9.
 * <p>
 * 永远获取到焦点的TextView
 */

public class AlwaysFocusedTextView extends TextView {
    public AlwaysFocusedTextView(Context context) {
        super(context);
    }

    public AlwaysFocusedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlwaysFocusedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

}
