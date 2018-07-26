package com.konka.kktripclient.ui;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.konka.kktripclient.R;

/**
 * Created by The_one on 2017-6-27.
 * <p>
 * 控件库Toast
 */

public class ToastView extends Toast {
    private Context mContext;
    private TextView mToastText;

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public ToastView(Context context) {
        super(context);
        mContext = context;
        initView(mContext);
    }

    private void initView(Context context) {
        LayoutInflater LayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View toastLayout = LayoutInflater.inflate(R.layout.toast_layout, null);
        setView(toastLayout);
        setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, (int) context.getResources().getDimension(R.dimen.toast_layout_margin_bottom));
        mToastText = (TextView) toastLayout.findViewById(R.id.toast_text);
    }

    @Override
    public void setText(CharSequence s) {
        mToastText.setText(s);
    }
}
