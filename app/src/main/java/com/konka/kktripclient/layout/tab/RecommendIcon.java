package com.konka.kktripclient.layout.tab;

import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.konka.kktripclient.R;
import com.konka.kktripclient.detail.DetailConstant;
import com.konka.kktripclient.layout.data.KKTabItemDataInfo;
import com.konka.kktripclient.layout.util.ActivityHandler;
import com.konka.kktripclient.layout.util.CommonFunction;
import com.konka.kktripclient.layout.util.LaunchHelper;
import com.konka.kktripclient.layout.view.AnimateFocusChangeListener;
import com.konka.kktripclient.layout.view.GlideRoundTransform;
import com.konka.kktripclient.utils.Constant;
import com.konka.kktripclient.utils.LogUtils;
import com.makeramen.roundedimageview.RoundedImageView;

public class RecommendIcon {
    private final String TAG = this.getClass().getSimpleName();
    /**
     * Icon布局
     */
    private ViewGroup mParentView;
    private FrameLayout content;
    private RoundedImageView mImageView;
    private View mTitleView;
    private GlideRoundTransform roundTransform;
    /**
     * 要传入的数据
     */
    private KKTabItemDataInfo mKKTabItemDataInfo;
    /**
     * 布局属性
     */
    private int rightMargin = 60;
    private int titleHeight = 60;
    private int titlePaddingHorizontal = 24;
    private int titlePaddingVertical = 16;
    private int titleTextSize = 18;
    private int secondTitleHeight = 100;
    private int secondTitleTextSize = 15;
    private int step = 18;

    public RecommendIcon(KKTabItemDataInfo kkTabItemDataInfo) {
        mKKTabItemDataInfo = kkTabItemDataInfo;

        content = (FrameLayout) LayoutInflater.from(ActivityHandler.getInstance()).inflate(R.layout.recommend_icon, null);

        rightMargin = ActivityHandler.getInstance().getResources().getDimensionPixelOffset(R.dimen.recommend_icon_margin);
        titleHeight = ActivityHandler.getInstance().getResources().getDimensionPixelOffset(R.dimen.recommend_icon_title_height);
        titlePaddingHorizontal = ActivityHandler.getInstance().getResources().getDimensionPixelOffset(R.dimen.recommend_icon_title_padding_horizontal);
        titlePaddingVertical = ActivityHandler.getInstance().getResources().getDimensionPixelOffset(R.dimen.recommend_icon_title_padding_vertical);
        titleTextSize = ActivityHandler.getInstance().getResources().getDimensionPixelOffset(R.dimen.recommend_icon_title_size);
        secondTitleHeight = ActivityHandler.getInstance().getResources().getDimensionPixelOffset(R.dimen.recommend_icon_second_title_height);
        secondTitleTextSize = ActivityHandler.getInstance().getResources().getDimensionPixelOffset(R.dimen.recommend_icon_second_title_size);
        step = ActivityHandler.getInstance().getResources().getDimensionPixelOffset(R.dimen.recommend_icon_step);
    }

    public KKTabItemDataInfo getItemData() {
        return mKKTabItemDataInfo;
    }

    /**
     * 绘制显示区域
     *
     * @param parent 布局
     */
    public void Draw(FrameLayout parent) {
        LogUtils.d(TAG, "mKKTabItemDataInfo.type:" + mKKTabItemDataInfo.type);
        if (Constant.LAUNCH_VIDEO_WIDGET.equals(mKKTabItemDataInfo.type)) {
            return;
        }
        mParentView = parent;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.leftMargin = (mKKTabItemDataInfo.x - step) * CommonFunction.getScreenSize().x / 1920;
        layoutParams.rightMargin = rightMargin * CommonFunction.getScreenSize().x / 1920;
        layoutParams.topMargin = (mKKTabItemDataInfo.y - step) * CommonFunction.getScreenSize().y / 1080;

        layoutParams.width = (mKKTabItemDataInfo.width + step * 2) * CommonFunction.getScreenSize().x / 1920;
        layoutParams.height = (mKKTabItemDataInfo.height + step * 2) * CommonFunction.getScreenSize().y / 1080;

        content.setLayoutParams(layoutParams);
        content.setFocusable(true);

        roundTransform = new GlideRoundTransform(ActivityHandler.getInstance(), 6);
        mImageView = new RoundedImageView(ActivityHandler.getInstance());
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(ActivityHandler.getInstance())
                .load(mKKTabItemDataInfo.posterBottom)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .override(mKKTabItemDataInfo.width, mKKTabItemDataInfo.height)
                .transform(roundTransform)
                .into(mImageView);
        layoutParams = new FrameLayout.LayoutParams(mKKTabItemDataInfo.width * CommonFunction.getScreenSize().x / 1920, mKKTabItemDataInfo.height * CommonFunction.getScreenSize().y / 1080);
        content.addView(mImageView, layoutParams);

        if (mKKTabItemDataInfo.isShowTitle == 0) {
            String title = mKKTabItemDataInfo.firstTitle;
            String secondTitle = mKKTabItemDataInfo.secondTitle;

            if (secondTitle != null && !"".equals(secondTitle.trim())) {
                layoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, secondTitleHeight * CommonFunction.getScreenSize().y / 1080);
                layoutParams.gravity = Gravity.BOTTOM;

                mTitleView = getAdverTitleView(title, secondTitle);
                content.addView(mTitleView, layoutParams);
            } else if (title != null && !"".equals(title.trim())) {
                layoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, titleHeight * CommonFunction.getScreenSize().y / 1080);
                layoutParams.gravity = Gravity.BOTTOM;

                mTitleView = getAdverTitleView(title);
                content.addView(mTitleView, layoutParams);
            }
        }

        mParentView.addView(content);

        content.setTag(R.id.track_view_scale_x, 1.08f);
        content.setTag(R.id.track_view_scale_y, 1.08f);
        content.setOnClickListener(new ClickListener());
        content.setOnFocusChangeListener(new FocusChange());
        content.setOnKeyListener(new KeyListener());
    }

    /**
     * 绘制标题title
     *
     * @param title 标题
     * @return textView
     */
    private View getAdverTitleView(String title) {
        try {
            TextView textView = new TextView(ActivityHandler.getInstance());

            textView.setText(title);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
            textView.setTextColor(Color.WHITE);
            textView.setEllipsize(TruncateAt.MARQUEE);
            textView.setSingleLine(true);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setBackgroundResource(R.drawable.recommend_icon_title_bg_selector);
            textView.setPadding(titlePaddingHorizontal * CommonFunction.getScreenSize().x / 1920, 0, titlePaddingHorizontal * CommonFunction.getScreenSize().x / 1920, 0);

            return textView;
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 绘制标题title
     *
     * @param title       一级标题
     * @param secondTitle 二级标题
     * @return parent
     */
    private View getAdverTitleView(String title, String secondTitle) {
        try {
            LinearLayout parent = new LinearLayout(ActivityHandler.getInstance());
            parent.setOrientation(LinearLayout.VERTICAL);
            parent.setBackgroundResource(R.drawable.recommend_icon_title_bg_selector);

            {
                TextView textView = new TextView(ActivityHandler.getInstance());

                textView.setText(title);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
                textView.setTextColor(Color.WHITE);
                textView.setEllipsize(TruncateAt.MARQUEE);
                textView.setSingleLine(true);
                textView.setGravity(Gravity.CENTER_VERTICAL);
                textView.setPadding(titlePaddingHorizontal * CommonFunction.getScreenSize().x / 1920, titlePaddingVertical * CommonFunction.getScreenSize().y / 1080, titlePaddingHorizontal * CommonFunction.getScreenSize().x / 1920, 0);

                parent.addView(textView);
            }
            {
                TextView textView = new TextView(ActivityHandler.getInstance());

                textView.setText(secondTitle);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, secondTitleTextSize);
                textView.setTextColor(Color.WHITE);
                textView.setEllipsize(TruncateAt.MARQUEE);
                textView.setSingleLine(true);
                textView.setGravity(Gravity.CENTER_VERTICAL);
                textView.setPadding(titlePaddingHorizontal * CommonFunction.getScreenSize().x / 1920, 0, titlePaddingHorizontal * CommonFunction.getScreenSize().x / 1920, titlePaddingVertical * CommonFunction.getScreenSize().y / 1080);

                parent.addView(textView);
            }

            return parent;
        } catch (Exception e) {

        }
        return null;
    }

    private class ClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            String type = mKKTabItemDataInfo.type;
            int id = mKKTabItemDataInfo.goodsID;
            String name = mKKTabItemDataInfo.goodsName;
            String typeID = mKKTabItemDataInfo.typeID;
            String typeName = mKKTabItemDataInfo.typeName;

            JSONObject source = new JSONObject();
            source.put(Constant.BIG_DATA_VALUE_KEY_SOURCE, Constant.BIG_DATA_VALUE_SOURCE_TJ);
            source.put(Constant.BIG_DATA_VALUE_KEY_NAME, mKKTabItemDataInfo.tabName);
            source.put(Constant.BIG_DATA_VALUE_KEY_LOCATION, String.valueOf(mKKTabItemDataInfo.layoutId));

            try {
                if (type.equals(Constant.LAUNCH_COLLECT)) {
                    // 41：收藏
                    LaunchHelper.startCollectActivity(ActivityHandler.getInstance(), source.toJSONString());
                } else if (type.equals(Constant.LAUNCH_ROUTE_LIST)) {
                    // 42：路线列表
                    LaunchHelper.startCategoryActivity(ActivityHandler.getInstance(), type);
                } else if (type.equals(Constant.LAUNCH_ROUTE_DETAIL)) {
                    // 43：路线详情
                    LaunchHelper.startDetailActivity(ActivityHandler.getInstance(), DetailConstant.GOOD_ROUTE, id, source.toJSONString());
                } else if (type.equals(Constant.LAUNCH_TICKET_LIST)) {
                    // 44：门票列表
                    LaunchHelper.startCategoryActivity(ActivityHandler.getInstance(), type);
                } else if (type.equals(Constant.LAUNCH_TICKET_DETAIL)) {
                    // 45：门票详情
                    LaunchHelper.startDetailActivity(ActivityHandler.getInstance(), DetailConstant.GOOD_TICKET, id, source.toJSONString());
                } else if (type.equals(Constant.LAUNCH_VIDEO)) {
                    // 46：视频
                    LaunchHelper.startVideoActivity(ActivityHandler.getInstance(), DetailConstant.START_TYPE_ROUTE, id, source.toJSONString());
                } else if (type.equals(Constant.LAUNCH_VIDEO_WIDGET)) {
                    // 47：视频小窗口
                    source.remove(Constant.BIG_DATA_VALUE_KEY_SOURCE);
                    source.put(Constant.BIG_DATA_VALUE_KEY_SOURCE, Constant.BIG_DATA_VALUE_SOURCE_SW);
                    LaunchHelper.startVideoActivity(ActivityHandler.getInstance(), DetailConstant.START_TYPE_WINDOW, id, source.toJSONString());
                } else if (type.equals(Constant.LAUNCH_APK)) {
                    // 00：APK拉起
                    ActivityHandler.getInstance().startActivity(mKKTabItemDataInfo.startIntent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private class FocusChange extends AnimateFocusChangeListener {
        @Override
        public void onFocusChange(final View v, boolean hasFocus) {
            super.onFocusChange(v, hasFocus);
            if (mTitleView != null)
                if (hasFocus) {
                    mTitleView.animate().setDuration(300).start();
                    mTitleView.setSelected(true);
                } else {
                    mTitleView.animate().setDuration(300).start();
                    mTitleView.setSelected(false);
                }
        }
    }

    private class KeyListener implements View.OnKeyListener {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    return true;
            }
            return false;
        }
    }

    public void requestFocus() {
        ActivityHandler.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                content.requestFocus();
            }
        });
    }

}