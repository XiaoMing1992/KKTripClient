package com.konka.kktripclient.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.konka.kktripclient.R;
import com.konka.kktripclient.pay.UserHelper;
import com.konka.kktripclient.utils.CommonUtils;
import com.konka.kktripclient.utils.Constant;
import com.konka.kktripclient.utils.LogUtils;
import com.konka.kktripclient.utils.NetworkUtils;
import com.konka.kktripclient.utils.QRCodeHelper;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Zhou Weilin on 2017-6-12.
 */

public class BookActivity extends Activity {
    private final String TAG = BookActivity.class.getSimpleName();
    private Context mContext;
    private ImageView mImgQRCode;
    private Button mBtnReturn;
    private Bitmap mQRCodeBitmap;
    private final int QR_WIDTH = 470;
    private final int DEFAULT_USER = 0;
    private final int KONKA = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_book);
        mContext = this.getApplicationContext();
        initView();
        initData();
        showQrCode();
    }

    private void initData() {
        Intent intent = getIntent();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImgQRCode.setImageBitmap(null);
        try {
            if (!mQRCodeBitmap.isRecycled()) {
                mQRCodeBitmap.recycle();
                mQRCodeBitmap = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initView() {
        mImgQRCode = (ImageView)findViewById(R.id.book_img_qrcode);
        mBtnReturn = (Button)findViewById(R.id.book_btn_return);
        mBtnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //缺少出错界面处理
    private void showQrCode(){
        String url = getQRCodeUrl();
        mQRCodeBitmap = QRCodeHelper.createQRCode(url, QR_WIDTH);
        mImgQRCode.setImageBitmap(mQRCodeBitmap);
    }

    private String getQRCodeUrl(){
        String url = "http://api.kkapp.com/kktripserver/tickets/scan_qrcode";
        String userId = UserHelper.getInstance(mContext).getOpenId();
        if(TextUtils.isEmpty(userId)){
            userId = "?userId="+ DEFAULT_USER;
        }else {
            userId = "?userId="+ userId;
        }

        String userOrgin = "&userOrigin="+KONKA;
        String deviceId = "&deviceId="+ CommonUtils.getSerialNumber(mContext);
        String deviceBrand = "&deviceBrand="+KONKA;

        Intent intent = getIntent();
        String ticketId = "";
        String ticketUrl = "";
        if(intent!=null){
            ticketId = "&ticketId="+intent.getIntExtra(Constant.KEY_GOOD_ID,0);
            String qrUrl = intent.getStringExtra("ticket_qr_url");
            ticketUrl = "&ticketUri="+ NetworkUtils.urlEncode(qrUrl);

        }
        url+= userId+userOrgin+deviceId+deviceBrand+ticketId+ticketUrl;
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->getQRCodeUrl:"+url);
        return url;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //友盟统计
        if (!Constant.MONKEY) MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //友盟统计
        if (!Constant.MONKEY) MobclickAgent.onPause(this);
    }
}
