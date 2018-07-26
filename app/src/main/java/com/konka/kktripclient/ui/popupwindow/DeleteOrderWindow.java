package com.konka.kktripclient.ui.popupwindow;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.konka.kktripclient.R;
import com.konka.kktripclient.activity.IDeleteOrder;
import com.konka.kktripclient.utils.CommonUtils;
import com.konka.kktripclient.utils.LogUtils;

/**
 * Created by HP on 2017-7-3.
 */

public class DeleteOrderWindow extends PopupWindow  implements View.OnClickListener{
    private final String TAG = "DeleteOrderWindow";
    private Context mContext;
    private View mView;
    private Button sure;
    private Button cancel;
    private IDeleteOrder listener = null;

    public DeleteOrderWindow(Context context){
        super(context);
        initView(context);
    }

    private void initView(Context context){
        mContext = context;
        mView = LayoutInflater.from(context).inflate(R.layout.delete_order_window, null);
        setContentView(mView);
        sure = (Button)mView.findViewById(R.id.sure);
        cancel = (Button)mView.findViewById(R.id.cancel);

        CommonUtils.computeScreenSize(context);
        LogUtils.d(TAG, "ScreenWidth = "+CommonUtils.getScreenWidth()+" ScreenHeight = "+CommonUtils.getScreenHeight());
        setWidth(CommonUtils.getScreenWidth());
        setHeight(CommonUtils.getScreenHeight());
        setFocusable(true);
        setBackgroundDrawable(mContext.getResources().getDrawable(R.color.transparent));
        sure.setOnClickListener(this);
        cancel.setOnClickListener(this);
        focusListener();
    }

    private void focusListener(){
        sure.requestFocus();
        sure.setTextColor(mContext.getResources().getColor(R.color.order_color2));
        sure.setAlpha(1.0f);
        sure.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                 if (b){
                     sure.setTextColor(mContext.getResources().getColor(R.color.order_color2));
                     sure.setAlpha(1.0f);
                 }else {
                     sure.setTextColor(mContext.getResources().getColor(R.color.order_color8));
                     sure.setAlpha(0.7f);
                 }
            }
        });

        cancel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    cancel.setTextColor(mContext.getResources().getColor(R.color.order_color2));
                    cancel.setAlpha(1.0f);
                }else {
                    cancel.setTextColor(mContext.getResources().getColor(R.color.order_color8));
                    cancel.setAlpha(0.7f);
                }
            }
        });
    }

    public void show(){
        showAtLocation(mView, Gravity.CENTER, 0, 0);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sure:
                listener.doDelete();
                dismiss();
                break;

            case R.id.cancel:
                dismiss();
                break;
        }
    }

    /**********回调函数*********/
    public void setListener(IDeleteOrder listener){
        this.listener=listener;
    }

}
