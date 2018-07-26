package com.konka.kktripclient;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.konka.kktripclient.detail.DetailConstant;
import com.konka.kktripclient.layout.util.LaunchHelper;
import com.konka.kktripclient.layout.view.GlideRoundTransform;
import com.konka.kktripclient.net.info.ToastAdverEventDataBean;
import com.konka.kktripclient.utils.Constant;
import com.konka.kktripclient.utils.LogUtils;
import com.konka.kktripclient.utils.NetworkUtils;
import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by The_one on 2017-6-6.
 * <p>
 * 弹窗信息
 */

public class RecommendDialog extends Dialog {
    private final String TAG = this.getClass().getSimpleName();

    private Context mContext;
    private Button mButtonOk;
    private Button mButtonCancel;
    private RoundedImageView mImageView;
    private GlideRoundTransform roundTransform;
    private ToastAdverEventDataBean mDataBean = new ToastAdverEventDataBean();

    public RecommendDialog(Context context) {
        super(context, R.style.CustomDialog);
        init(context);
    }

    public RecommendDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    public RecommendDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
    }

    public void setData(ToastAdverEventDataBean dataBean) {
        mDataBean = dataBean;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtils.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommend_dialog);
        mImageView = (RoundedImageView) findViewById(R.id.recommend_dialog_image);
        mButtonOk = (Button) findViewById(R.id.recommend_dialog_ok);
        mButtonCancel = (Button) findViewById(R.id.recommend_dialog_cancel);

        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                JSONObject source = new JSONObject();
                source.put(Constant.BIG_DATA_VALUE_KEY_SOURCE, Constant.BIG_DATA_VALUE_SOURCE_TC);
                source.put(Constant.BIG_DATA_VALUE_KEY_NAME, "-1");
                source.put(Constant.BIG_DATA_VALUE_KEY_LOCATION, "-1");
                if (Constant.RECOMMEND_DIALOG_ROUTE == mDataBean.getType()) {
                    LaunchHelper.startDetailActivity(mContext, DetailConstant.GOOD_ROUTE, mDataBean.getResourceId(), source.toJSONString());
                } else if (Constant.RECOMMEND_DIALOG_TICKET == mDataBean.getType()) {
                    LaunchHelper.startDetailActivity(mContext, DetailConstant.GOOD_TICKET, mDataBean.getResourceId(), source.toJSONString());
                } else if (Constant.RECOMMEND_DIALOG_VIDEO == mDataBean.getType()) {
                    LaunchHelper.startVideoActivity(mContext, DetailConstant.START_TYPE_ROUTE, mDataBean.getResourceId(), source.toJSONString());
                }
            }
        });
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        loadImage(mImageView, NetworkUtils.toURL(mDataBean.getPoster()));
    }

    @Override
    public void show() {
        super.show();
        // 设置宽度全屏，要设置在show的后面
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.TOP;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;

        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }

    @Override
    public void dismiss() {
        LogUtils.d(TAG, "dismiss");
        super.dismiss();
        Glide.get(mContext).clearMemory();
    }

    private void loadImage(RoundedImageView imageView, String url) {
        if (roundTransform == null) {
            roundTransform = new GlideRoundTransform(mContext, 4);
        }
        Glide.with(mContext)
                .load(url)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .override(mContext.getResources().getDimensionPixelOffset(R.dimen.recommend_dialog_image_width), mContext.getResources().getDimensionPixelOffset(R.dimen.recommend_dialog_image_height))
                .transform(roundTransform)
                .into(imageView);
    }
}
