package com.konka.kktripclient.detail;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.konka.kktripclient.R;
import com.konka.kktripclient.detail.customview.TripPhotoView;
import com.konka.kktripclient.utils.LogUtils;

import java.util.List;

/**
 * Created by Zhou Weilin on 2017-5-26.
 */

public class PhotoFullDialog extends Dialog {
    private final String TAG = PhotoFullDialog.class.getSimpleName();
    private Context mContext;
    private TripPhotoView mPhotoView;
    private int mCurPos;
    private List<String> mImgUrls;
    private ImageView mImgLeft;
    private ImageView mImgRight;

    public PhotoFullDialog(@NonNull Context context) {
        super(context);
    }

    public PhotoFullDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected PhotoFullDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public PhotoFullDialog(Context context, List<String> imageUrls, int position){
        super(context, R.style.PhotoDialog);
        mContext = context;
        mImgUrls = imageUrls;
        mCurPos = position;
        LogUtils.d(DetailConstant.TAG_DETAIL,"PhotoFullDialog-->mImgUrls:"+mImgUrls+"===mCurPos:"+mCurPos);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_photofull);

        //添加以下方法才能使dialog里的图片全屏
        Window win = getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);

        initView();

    }

    private void initView() {
        mPhotoView = (TripPhotoView)findViewById(R.id.photoview_full);
        mPhotoView.initPhotoView(mImgUrls,mCurPos);
        mPhotoView.setListener(mPageSelectedListener);

        mImgLeft = (ImageView)findViewById(R.id.photofull_img_left);
        mImgRight = (ImageView)findViewById(R.id.photofull_img_right);
        changeNavigationImg(mCurPos);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        getWindow().getDecorView().setDrawingCacheEnabled(false);
        mContext=null;
    }

    private TripPhotoView.PhoteListener mPageSelectedListener = new TripPhotoView.PhoteListener() {
        @Override
        public boolean onViewPagerKey(View v, int keyCode, KeyEvent event) {
//            Log.d("kkzwl",TAG+"-->onViewPagerKey-->keycode:"+keyCode);
            if(event.getAction()== KeyEvent.ACTION_DOWN){
                if(keyCode==KeyEvent.KEYCODE_DPAD_LEFT){
                    mImgLeft.setBackgroundResource(R.drawable.photofull_left_focus);
                }else if(keyCode==KeyEvent.KEYCODE_DPAD_RIGHT){
                    mImgRight.setBackgroundResource(R.drawable.photofull_right_focus);
                }
            }
            return false;
        }

        @Override
        public void onPageScrolled(int position, float offset, int pixels) {

        }

        @Override
        public void onPageSelected(int pos) {
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onPageSelected:"+pos);
            mCurPos = pos;
        }

        @Override
        public void onPageScrollChanged(int state) {
            Log.d("kkzwl",TAG+"-->onPageScrollStateChanged:"+state);
            if (state == 2) {

            } else {
                changeNavigationImg(mCurPos);
            }

        }
    };

    private void changeNavigationImg(int curPospos){
        LogUtils.d(DetailConstant.TAG_DETAIL,"PhotoFullDialog-->changeNavigationImg-->curPospos:"+curPospos+"  "+mImgUrls.get(curPospos));

        if(curPospos==0){
            mImgLeft.setBackgroundResource(R.drawable.photofull_left_no_focus);
            mImgLeft.setVisibility(View.INVISIBLE);
        }else {
            mImgLeft.setBackgroundResource(R.drawable.photofull_left_no_focus);
            mImgLeft.setVisibility(View.VISIBLE);
        }
        if(curPospos==mImgUrls.size()-1){
            mImgRight.setBackgroundResource(R.drawable.photofull_right_no_focus);
            mImgRight.setVisibility(View.INVISIBLE);
        }else {
            mImgRight.setBackgroundResource(R.drawable.photofull_right_no_focus);
            mImgRight.setVisibility(View.VISIBLE);

        }
    }

}
