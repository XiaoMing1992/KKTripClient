package com.konka.kktripclient.detail.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.konka.kktripclient.R;
import com.konka.kktripclient.utils.CommonUtils;
import com.konka.kktripclient.utils.NetworkUtils;

import java.util.List;

/**
 * Created by Zhou Weilin on 2017-6-2.
 */

public class PhotoThumbView extends RelativeLayout {
    private final String TAG = PhotoThumbView.class.getSimpleName();
    private Context mContext;
    private RelativeHolder mRelativeHolder;
    private ImageViewHolder mImageViewHolder;
    private BgViewHolder mBgViewHolder;
    private ImageView mImgVideoLabel;
    private List<String> mImgUrls;
    private ItemListener mItemListener;
    private boolean isContainVideo = false;

    public PhotoThumbView(Context context) {
        super(context.getApplicationContext());
        LayoutInflater.from(context).inflate(R.layout.view_detail_thumb, this);
        init(context);
    }

    public PhotoThumbView(Context context, AttributeSet attrs) {
        super(context.getApplicationContext(), attrs);
        LayoutInflater.from(context).inflate(R.layout.view_detail_thumb, this);
        init(context);
    }

    public PhotoThumbView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context.getApplicationContext(), attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_detail_thumb, this);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PhotoThumbView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context.getApplicationContext(), attrs, defStyleAttr, defStyleRes);
        LayoutInflater.from(context).inflate(R.layout.view_detail_thumb, this);
        init(context);
    }


    private void init(Context context) {
        mContext = context.getApplicationContext();
        mRelativeHolder = new RelativeHolder();
        mImageViewHolder = new ImageViewHolder();
        mBgViewHolder = new BgViewHolder();
        mImgVideoLabel = (ImageView)findViewById(R.id.detail_video_labe0);
        for(int i=0;i<mRelativeHolder.layouts.length;i++){
            mRelativeHolder.get(i).setVisibility(GONE);
            mRelativeHolder.get(i).setOnClickListener(new MyOnClick(i));
            mRelativeHolder.get(i).setOnFocusChangeListener(new MyFocusChange(i));
        }
        //设置以下两个往上焦点是到小窗口
        mRelativeHolder.get(1).setNextFocusUpId(R.id.detail_relative_window);
        mRelativeHolder.get(4).setNextFocusUpId(R.id.detail_relative_window);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mRelativeHolder = null;
        mImageViewHolder = null;
    }

    public void updateDate(List<String> urls){
        updateDate(urls,false);
    }


    public void updateDate(List<String> urls, boolean isVideo){
        mImgUrls = urls;
        isContainVideo = isVideo;
        initView();

    }

    public void setOnItemListener(ItemListener listener){
        mItemListener = listener;
    }

    private void initView() {
        if(isContainVideo){
            mImgVideoLabel.setVisibility(VISIBLE);
        }else {
            mImgVideoLabel.setVisibility(INVISIBLE);
        }
        int count = getItemCount();
        for(int i=0;i<count;i++){
            mRelativeHolder.get(i).setVisibility(VISIBLE);
            loadImage(mImageViewHolder.get(i),i);
        }
    }

    private void loadImage(final ImageView imageView, final int pos) {
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        try{
            Glide.with(mContext)
                    .load(NetworkUtils.toURL(mImgUrls.get(pos)))
                    .fitCenter()
                    .dontAnimate()
                    .override(CommonUtils.dip2px(mContext,132), CommonUtils.dip2px(mContext,82))
                    /*.listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target,
                                                   boolean isFirstResource) {
                            LogUtils.e(DetailConstant.TAG_DETAIL,TAG+"-->loadImage-->onException-->pos:"+pos);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model,
                                                       Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })*///貌似会导致DetailActivity内存泄漏
                    .error(R.drawable.default_image)
                    .into(imageView);

        } catch (OutOfMemoryError error) {
            imageView.setImageResource(R.drawable.default_image);
        }
    }

    private int getItemCount() {
        if (null==mImgUrls){
            return 0;
        }else if(mImgUrls.size()>5){//目前限制最多显示五张
            return 5;
        }
        return mImgUrls.size();
    }


    private class MyOnClick implements OnClickListener {
        private int onClickIndex;

        public MyOnClick(int i){
            onClickIndex = i;
        }
        @Override
        public void onClick(View v) {
            mItemListener.clickItem(onClickIndex,v.getId());
        }
    }

    private class MyFocusChange implements OnFocusChangeListener {
        int mFocusIndex;

        public MyFocusChange(int i) {
            mFocusIndex = i;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus){
                if(isContainVideo&&mFocusIndex==0){
                    mImgVideoLabel.setSelected(true);
                }else if(mFocusIndex==2){
                    bringToSurface(mFocusIndex);
                }else if(mFocusIndex==3){
                    bringToSurface(mFocusIndex);
                }
                mBgViewHolder.get(mFocusIndex).setSelected(true);
            }else {
                if(isContainVideo&&mFocusIndex==0){
                    mImgVideoLabel.setSelected(false);
                }else if(mFocusIndex==2){
                    bringToSurface(mFocusIndex-1);
                }else if(mFocusIndex==3){
                    bringToSurface(mFocusIndex+1);

                }
                mBgViewHolder.get(mFocusIndex).setSelected(false);
            }

        }

        private void bringToSurface(int index){
            mRelativeHolder.get(index).bringToFront();
            mRelativeHolder.get(index).requestLayout();//android4.4之前的版本需要让view的父控件调用这两个方法使其重绘。
            mRelativeHolder.get(index).invalidate();
        }
    }

    private class RelativeHolder {
        RelativeLayout relativeThumb0;
        RelativeLayout relativeThumb1;
        RelativeLayout relativeThumb2;
        RelativeLayout relativeThumb3;
        RelativeLayout relativeThumb4;

        RelativeLayout[] layouts = {
                relativeThumb0 =(RelativeLayout)findViewById(R.id.detail_relative_thumb0),
                relativeThumb1 =(RelativeLayout)findViewById(R.id.detail_relative_thumb1),
                relativeThumb2 =(RelativeLayout)findViewById(R.id.detail_relative_thumb2),
                relativeThumb3 =(RelativeLayout)findViewById(R.id.detail_relative_thumb3),
                relativeThumb4 =(RelativeLayout)findViewById(R.id.detail_relative_thumb4),
        };

        public RelativeLayout get(int index) {
            return layouts[index];
        }
    }

    private class ImageViewHolder {
        ImageView imgThumb0;
        ImageView imgThumb1;
        ImageView imgThumb2;
        ImageView imgThumb3;
        ImageView imgThumb4;

        ImageView[] imgs = {
                imgThumb0 =(ImageView)findViewById(R.id.detail_thumb0),
                imgThumb1 =(ImageView)findViewById(R.id.detail_thumb1),
                imgThumb2 =(ImageView)findViewById(R.id.detail_thumb2),
                imgThumb3 =(ImageView)findViewById(R.id.detail_thumb3),
                imgThumb4 =(ImageView)findViewById(R.id.detail_thumb4),
        };

        public ImageView get(int index) {
            return imgs[index];
        }
    }

    private class BgViewHolder {
        View bgThumb0;
        View bgThumb1;
        View bgThumb2;
        View bgThumb3;
        View bgThumb4;

        View[] imgs = {
                bgThumb0 =findViewById(R.id.detail_thumb0_bg),
                bgThumb1 =findViewById(R.id.detail_thumb1_bg),
                bgThumb2 =findViewById(R.id.detail_thumb2_bg),
                bgThumb3 =findViewById(R.id.detail_thumb3_bg),
                bgThumb4 =findViewById(R.id.detail_thumb4_bg),
        };

        public View get(int index) {
            return imgs[index];
        }
    }

    public interface ItemListener{
        void clickItem(int pos,int viewId);
    }




}
