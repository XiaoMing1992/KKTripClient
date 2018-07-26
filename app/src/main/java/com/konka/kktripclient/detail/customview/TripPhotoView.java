package com.konka.kktripclient.detail.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.konka.kktripclient.R;
import com.konka.kktripclient.utils.CommonUtils;
import com.konka.kktripclient.utils.NetworkUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhou Weilin on 2017-5-26.
 */

public class TripPhotoView extends RelativeLayout {
    private final String TAG = TripPhotoView.class.getSimpleName();
    private Context mContext;
    private ViewPager mViewPager;

    private List<String> mImageUrls;
    private ArrayList<ImageView> mImageViews;
    private int mCurPosition;
    private PhoteListener mPhoteListener;


    public TripPhotoView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_photo, this);
        init(context);
    }

    public TripPhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_photo, this);
        init(context);
    }

    public TripPhotoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_photo, this);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TripPhotoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater.from(context).inflate(R.layout.view_photo, this);
        init(context);
    }

    private void init(Context context){
        mContext = context;
        initView();

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mViewPager.destroyDrawingCache();
        mViewPager.removeAllViews();
        Glide.get(mContext).clearMemory();
    }

    /**
     * 初始化PhotoView控件
     * @param urls  要显示图片的url
     * @param pos   图片在外部被点击的位置
     */
    public void initPhotoView(List<String> urls, int pos){
        mImageUrls = urls;
        mCurPosition = pos;
        initPager();
        mViewPager.setCurrentItem(mCurPosition);
    }

    /**
     * 目前主要用于小窗口根据焦点变化切换图片
     * @param pos
     */
    public void setCurrentPage(int pos){
        if(mViewPager!=null){
            mViewPager.setCurrentItem(pos,false);
        }
    }


    public void setListener(PhoteListener listener){
        mPhoteListener = listener;
    }

    private void initView() {
        mViewPager = ((ViewPager)findViewById(R.id.viewpager_photoview));
    }

    private void initPager(){
        mImageViews = new ArrayList<>(getItemCount());

        for (int i = 0; i < getItemCount(); i++) {
            ImageView pictureImage = new ImageView(mContext.getApplicationContext());
            pictureImage.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            pictureImage.setScaleType(ImageView.ScaleType.FIT_XY);
            try {
                Glide.with(mContext.getApplicationContext())
                        .load(NetworkUtils.toURL(mImageUrls.get(i)))
                        .placeholder(R.drawable.default_image)
                        .error(R.drawable.default_image)
                        .dontAnimate()
                        .fitCenter()
                        .override(CommonUtils.dip2px(mContext,777), CommonUtils.dip2px(mContext,483))
                        /*.listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target,
                                                       boolean isFirstResource) {
                                LogUtils.e(DetailConstant.TAG_DETAIL,TAG+"-->initPager-->Glide(onException)");
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model,
                                                           Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                return false;
                            }
                        })*///貌似会导致DetailActivity内存泄漏

                        .into(pictureImage);
            } catch (OutOfMemoryError error) {
                pictureImage.setImageResource(R.drawable.default_image);
            }


            mImageViews.add(pictureImage);
        }


        try {
            // 反射设置scroller，解决切页时的时间问题
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(mViewPager.getContext(), new AccelerateInterpolator());
            field.set(mViewPager, scroller);
            scroller.setmDuration(400);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        mViewPager.setAdapter(new ImagePagerAdapter());
        mViewPager.addOnPageChangeListener(new ImagePageChangeListener());
        mViewPager.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(mPhoteListener !=null){//全屏的时候才用到，所以不一定有初始化
                    mPhoteListener.onViewPagerKey(v,keyCode,event);
                }
                return false;
            }
        });

    }


    private int getItemCount() {
        if (null==mImageUrls){
            return 0;
        }else if(mImageUrls.size()>5){//目前限制最多显示五张
            return 5;
        }
        return mImageUrls.size();
    }

    private class ImagePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return getItemCount();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView imageView = mImageViews.get(position%mImageViews.size());
            if (container==imageView.getParent()){
                container.removeView(imageView);
            }
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    }


    private class ImagePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            Log.d("kkzwl",TAG+"-->onPageScrolled:"+position+"===positionOffset"+positionOffset+"===positionOffsetPixels"+positionOffsetPixels);
            if(mPhoteListener !=null){//全屏的时候才用到，所以不一定有初始化
                mPhoteListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
            }

        }

        @Override
        public void onPageSelected(int position) {
            if(mPhoteListener !=null){
                mPhoteListener.onPageSelected(position);
            }


        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if(mPhoteListener !=null){
                mPhoteListener.onPageScrollChanged(state);
            }
        }
    }


    public interface PhoteListener {
        boolean onViewPagerKey(View v, int keyCode, KeyEvent event);
        void onPageScrolled(int position, float offset, int pixels);
        void onPageSelected(int pos);
        void onPageScrollChanged(int state);
    }
}
