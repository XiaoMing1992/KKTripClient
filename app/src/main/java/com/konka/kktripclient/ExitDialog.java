package com.konka.kktripclient;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.konka.kktripclient.layout.util.ActivityHandler;
import com.konka.kktripclient.utils.BigDataHelper;
import com.konka.kktripclient.utils.CommonUtils;
import com.konka.kktripclient.utils.Constant;

/**
 * Created by The_one on 2017-6-20.
 * <p>
 * 退出弹窗
 */

public class ExitDialog extends Dialog {
    private Button mButtonOk;
    private Button mButtonCancel;
    private long timeLong;
    private String timeString;

    public ExitDialog(Context context) {
        super(context, R.style.CustomDialog);
    }

    public ExitDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ExitDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exit_dialog);
        mButtonOk = (Button) findViewById(R.id.exit_dialog_ok);
        mButtonCancel = (Button) findViewById(R.id.exit_dialog_cancel);
        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (!Constant.MONKEY) {
                    BigDataHelper.getInstance().sendRouteUserInfo(timeString, CommonUtils.getMacAddress(), String.valueOf((System.currentTimeMillis() - timeLong) / 1000));
                    ActivityHandler.getInstance().finish();
                }
            }
        });
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;

        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }

    public void setTime(String timeString, long timeLong) {
        this.timeString = timeString;
        this.timeLong = timeLong;
    }
}
