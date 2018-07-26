package com.konka.kktripclient.detail;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.konka.kktripclient.R;
import com.konka.kktripclient.detail.bean.OrderInfo;
import com.konka.kktripclient.detail.bean.RouteInfo;
import com.konka.kktripclient.detail.customview.CycleWheelView;
import com.konka.kktripclient.layout.util.LaunchHelper;
import com.konka.kktripclient.ui.ToastView;
import com.konka.kktripclient.utils.BigDataHelper;
import com.konka.kktripclient.utils.CommonUtils;
import com.konka.kktripclient.utils.Constant;
import com.konka.kktripclient.utils.LogUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.konka.kktripclient.R.id.write_checkbox_protocol;
import static com.konka.kktripclient.R.id.write_txt_protocol;
import static com.konka.kktripclient.R.id.write_txt_username_tip;
import static com.konka.kktripclient.R.id.write_txt_userphone_tip;


/**
 * Created by Zhou Weilin on 2017-6-10.
 */

public class WriteActivity extends Activity {
    private final String TAG = WriteActivity.class.getSimpleName();
    private Context mContext;
    private RouteInfo mRouteInfo;
    private LinearLayout mLinearInfo;
    private TextView mTxtGoodName;
    private TextView mTxtGoodPrice;
    private TextView mTxtGoodNum;
    private TextView mTxtAddress;
    private EditText mEditUserName;
    private EditText mEditUserPhone;
    private TextView mTxtGoodTotal;
    private LinearLayout mLinearTips;
    private TextView mTxtTips;
    private LinearLayout mLinearSelectNum;
    private CycleWheelView mCycleWheelView;
    private Button mBtnSure;
    private Button mBtnCancel;

    private int mGoodId;
    private int mGoodType;
    private String mStrGoodName;
    private double mGoodPrice;
    private List<String> mPlaces;
    private List<String> mCounts = new ArrayList<>();

    private final int MAX_COUNT = 50;
    private int mNumPosition = 0;//代表wheelview里选择的位置

    private Intent intent;

    private ScrollView mScrollView;
    private TextView mTxtNameTip;
    private TextView mTxtPhoneTip;
    private CheckBox mCheckBox;
    private TextView mTxtProtocol;

    private LinearLayout mLinearSelectCity;
    private CityHolder mCityHolder;
    private int mCityPosition = 0;//代表已选城市的位置

    private RelativeLayout mRelativeErrorPage;

    private String mFormSource = "";//从何处打开进入详情页进入的来的填写信息页
    private long mStartTime;//页面启动时间
    private String mBtnName;//要上报的按钮名


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_write);
        mContext = this.getApplicationContext();
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mStartTime = System.currentTimeMillis();
        mBtnName = mContext.getString(R.string.write_btn_cancel);
        //友盟统计
        if (!Constant.MONKEY) MobclickAgent.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //友盟统计
        if (!Constant.MONKEY) MobclickAgent.onResume(this);

        long duration = (System.currentTimeMillis() - mStartTime)/1000;

        BigDataHelper.getInstance().sendFillInOrderInfo(CommonUtils.stampToDate(mStartTime),
                ""+duration,mBtnName,mStrGoodName,mTxtGoodTotal.getText().toString(),mTxtAddress.getText().toString());

    }

    private void initView() {
        //信息页
        mLinearInfo = (LinearLayout) findViewById(R.id.write_linear_info);
        mTxtGoodName = (TextView)findViewById(R.id.write_txt_goodname);
        mTxtGoodPrice = (TextView)findViewById(R.id.write_txt_price);
        mTxtGoodNum = (TextView)findViewById(R.id.write_txt_num);
        mTxtAddress = (TextView)findViewById(R.id.write_txt_address);
        mEditUserName = (EditText)findViewById(R.id.write_edit_username);
        mEditUserPhone = (EditText)findViewById(R.id.write_edit_userphone);
        mTxtGoodTotal = (TextView)findViewById(R.id.write_txt_total);
        mLinearTips = (LinearLayout)findViewById(R.id.write_linear_tips);
        mTxtTips = (TextView)findViewById(R.id.write_txt_tips);

        //选集页
        mCycleWheelView = (CycleWheelView)findViewById(R.id.write_select_cyclewheel);
        mLinearSelectNum = (LinearLayout)findViewById(R.id.write_linear_select_num);
        mLinearSelectCity = (LinearLayout)findViewById(R.id.write_linear_select_city);
        mBtnSure = (Button)findViewById(R.id.write_btn_sure);
        mBtnCancel = (Button)findViewById(R.id.write_btn_cancel);
        mScrollView = (ScrollView)findViewById(R.id.write_scroll_info);
        controlKeyboardLayout(mScrollView,this);

        mTxtNameTip = (TextView)findViewById(write_txt_username_tip);
        mTxtPhoneTip = (TextView)findViewById(write_txt_userphone_tip);
        mTxtProtocol = (TextView)findViewById(write_txt_protocol);
        mCheckBox = (CheckBox)findViewById(write_checkbox_protocol);

        mRelativeErrorPage = (RelativeLayout)findViewById(R.id.write_relative_error_page);


        mCheckBox.setChecked(false);
        mEditUserName.setNextFocusDownId(R.id.write_edit_userphone);
        mEditUserPhone.setNextFocusDownId(R.id.write_checkbox_protocol);

        for (int i = 0; i < MAX_COUNT; i++) {
            mCounts.add("" + (i+1));
        }
        initCycleWheel(mCounts);

        mTxtGoodNum.setOnClickListener(mOnClickListener);
        mTxtAddress.setOnClickListener(mOnClickListener);
        mLinearSelectNum.setOnClickListener(mOnClickListener);
        mBtnSure.setOnClickListener(mOnClickListener);
        mBtnCancel.setOnClickListener(mOnClickListener);
        mTxtProtocol.setOnClickListener(mOnClickListener);
        mCheckBox.setOnClickListener(mOnClickListener);
        mCheckBox.setChecked(false);
        mBtnSure.setEnabled(false);
        mBtnSure.setFocusable(false);
        mEditUserName.setOnFocusChangeListener(mOnFocusChangeListener);
        mEditUserPhone.setOnFocusChangeListener(mOnFocusChangeListener);
        mEditUserName.setOnKeyListener(mOnKeyListener);
        mEditUserPhone.setOnKeyListener(mOnKeyListener);

        mRelativeErrorPage.setVisibility(View.INVISIBLE);
        mLinearSelectNum.setVisibility(View.INVISIBLE);
        mLinearSelectCity.setVisibility(View.INVISIBLE);
        mLinearInfo.setVisibility(View.VISIBLE);
        mLinearTips.setVisibility(View.VISIBLE);



    }

    private void initData(){
        intent = getIntent();
        if(intent!=null){
            mRouteInfo = intent.getParcelableExtra(DetailConstant.KEY_ROUTEINFO);
            mFormSource = intent.getStringExtra(Constant.KEY_START_SOURCE);
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->initData-->mFormSource:" + mFormSource);
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"initData-->mRouteInfo:"+mRouteInfo);
            if(mRouteInfo!=null){
                mGoodId = mRouteInfo.getGoodId();
                mStrGoodName = mRouteInfo.getGoodName();
                mGoodPrice = mRouteInfo.getGoodPrice();
                String strAddress = mRouteInfo.getDeparturePlace();
                if(!TextUtils.isEmpty(strAddress)){
                    mPlaces =  java.util.Arrays.asList(strAddress.split(","));
                }

                mTxtGoodName.setText(mStrGoodName);
                mTxtGoodPrice.setText(CommonUtils.formatPrice(""+mGoodPrice));
                mTxtGoodNum.setText(mCounts.get(0));
                mTxtAddress.setText(mPlaces.get(0));

                initCityHolder();
                setTotalPrice();
            }else {
                mRelativeErrorPage.setVisibility(View.VISIBLE);
            }

        }
    }


    //初始化选择数量数据和布局
    private void initCycleWheel(List<String> data){
        mCycleWheelView.setLabels(data);
        try {
            mCycleWheelView.setWheelSize(9);
        } catch (CycleWheelView.CycleWheelViewException e) {
            e.printStackTrace();
        }
        mCycleWheelView.setCycleEnable(true);
        mCycleWheelView.setDivider(Color.TRANSPARENT, 0);
        mCycleWheelView.setSolid(Color.TRANSPARENT,Color.parseColor("#ff7400"));
        mCycleWheelView.setLabelColor(Color.parseColor("#a16736"));
        mCycleWheelView.setLabelSelectColor(Color.WHITE);
        mCycleWheelView.setOnWheelItemSelectedListener(new WheelItemSelectedListener());

    }

    //初始化选择城市数据和布局
    private void initCityHolder(){
        if(mPlaces.size()==1){
            mTxtAddress.setFocusable(false);
            return;
        }

        mCityHolder = new CityHolder();
        for (int i = 0;i<5;i++){
            if(i<mPlaces.size()){
                mCityHolder.get(i).setOnClickListener(new CityClick(i));
                mCityHolder.get(i).setText(mPlaces.get(i));
                mCityHolder.get(i).setVisibility(View.VISIBLE);
            }else {
                mCityHolder.get(i).setVisibility(View.INVISIBLE);
            }
        }

    }


    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.write_txt_num:
                    setCurWheelData(mTxtGoodNum.getText().toString());
                    mLinearInfo.setVisibility(View.INVISIBLE);
                    mLinearTips.setVisibility(View.INVISIBLE);
                    mLinearSelectCity.setVisibility(View.INVISIBLE);
                    mLinearSelectNum.setVisibility(View.VISIBLE);
                    break;

                case R.id.write_txt_address:
                    mLinearInfo.setVisibility(View.INVISIBLE);
                    mLinearTips.setVisibility(View.INVISIBLE);
                    mLinearSelectNum.setVisibility(View.INVISIBLE);
                    mLinearSelectCity.setVisibility(View.VISIBLE);
                    if(mCityHolder!=null && mCityHolder.get(mCityPosition)!=null){
                        mCityHolder.get(mCityPosition).requestFocus();
                    }
                    break;


                case R.id.write_btn_sure:
                    if(TextUtils.isEmpty(mEditUserName.getText())){
                        mTxtNameTip.setVisibility(View.VISIBLE);
                    }else {
                        mTxtNameTip.setVisibility(View.INVISIBLE);
                    }

                    if(TextUtils.isEmpty(mEditUserPhone.getText())||!isPhoneValid(mEditUserPhone.getText().toString())){
                        mTxtPhoneTip.setVisibility(View.VISIBLE);
                    }else {
                        mTxtPhoneTip.setVisibility(View.INVISIBLE);
                    }

                    if(TextUtils.isEmpty(mEditUserName.getText())||
                           TextUtils.isEmpty(mEditUserPhone.getText())||
                            !isPhoneValid(mEditUserPhone.getText().toString())){
                        return;
                    }

                    LaunchHelper.startBuyOrderActivity(WriteActivity.this,getOrderInfo(),mFormSource);
                    mBtnName = mContext.getString(R.string.write_btn_sure);
                    break;

                case R.id.write_btn_cancel:
                    finish();
                    break;

                case R.id.write_checkbox_protocol:
                    LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"write_checkbox_protocol:"+mCheckBox.isChecked());
                    if(mCheckBox.isChecked()){
                        mCheckBox.setChecked(true);
                        mBtnSure.setEnabled(true);
                        mBtnSure.setFocusable(true);
                        mCheckBox.setButtonDrawable(R.drawable.write_checkbox_check_selector);

//                        mEditUserPhone.setNextFocusDownId(R.id.write_btn_sure);
                    }else {
                        mCheckBox.setChecked(false);
                        mBtnSure.setEnabled(false);
                        mBtnSure.setFocusable(false);
                        mCheckBox.setButtonDrawable(R.drawable.write_checkbox_uncheck_selector);
                        if(!mCheckBox.isChecked()){
                            ToastView toastView = new ToastView(WriteActivity.this);
                            toastView.setText(R.string.write_protocol_agreee);
                            toastView.show();
                        }
//                        mEditUserPhone.setNextFocusDownId(R.id.write_checkbox_protocol);
                    }
                    break;

                case R.id.write_txt_protocol:
                    Dialog protoclDialog = new Dialog(WriteActivity.this,R.style.ProtocolDialog);
                    protoclDialog.setContentView(R.layout.dialog_protool);
                    protoclDialog.show();
                    Window win = protoclDialog.getWindow();
                    win.getDecorView().setPadding(0, 0, 0, 0);
                    WindowManager.LayoutParams lp = win.getAttributes();
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                    win.setAttributes(lp);

                    break;
            }
        }
    };

    private View.OnKeyListener mOnKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if(event.getAction()==KeyEvent.ACTION_DOWN){

                if(mEditUserName.hasFocus()){
                    if(keyCode == KeyEvent.KEYCODE_DPAD_UP){
                        mEditUserName.clearFocus();
                        mTxtAddress.requestFocus();
                    }else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
                        mEditUserName.clearFocus();
                        mEditUserPhone.requestFocus();
                    }else if(keyCode == KeyEvent.KEYCODE_BACK){
                        finish();
                    }else {
                        return false;
                    }
                    return true;
                }else if(mEditUserPhone.hasFocus()){
                    if(keyCode == KeyEvent.KEYCODE_DPAD_UP){
                        mEditUserPhone.clearFocus();
                        mEditUserName.requestFocus();
                    }else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
                        mEditUserPhone.clearFocus();
                        mCheckBox.requestFocus();
                    }else if(keyCode == KeyEvent.KEYCODE_BACK){
                        finish();
                    }else {
                        return false;
                    }
                    return true;
                }


            }
            return false;
        }
    };

    private boolean isPhoneValid(String inputText) {
        Pattern p = Pattern.compile("^(1[0-9])\\d{9}$");
        Matcher m = p.matcher(inputText);
        return m.matches();
    }

    private OrderInfo getOrderInfo(){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setGoodId(mRouteInfo.getGoodId());
        orderInfo.setGoodName(mRouteInfo.getGoodName());
        orderInfo.setDepartureTime(mRouteInfo.getDepartureTime());
        orderInfo.setThumbNail(mRouteInfo.getThumbNail());

        orderInfo.setDeparturePlace(mTxtAddress.getText().toString());
        orderInfo.setGoodPrice(mTxtGoodTotal.getText().toString());
        orderInfo.setGoodNum(Integer.valueOf(mTxtGoodNum.getText().toString()));
        orderInfo.setUserName(mEditUserName.getText().toString());
        orderInfo.setUserPhone(mEditUserPhone.getText().toString());

        return orderInfo;
    }

    private void setCurWheelData(String text) {
        try{
            mNumPosition = Integer.valueOf(text)-1;
            mCycleWheelView.setSelection(mNumPosition);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"setCurWheelData-->mNumPosition:"+ mNumPosition);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(mLinearSelectNum.isShown()){
            int curPos = mCycleWheelView.getSelection();
            switch (keyCode){
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    curPos+=1;
                    mCycleWheelView.setSelection(curPos);
                    break;

                case KeyEvent.KEYCODE_DPAD_UP:
                    curPos-=1;
                    mCycleWheelView.setSelection(curPos);
                    break;

                case KeyEvent.KEYCODE_BACK:
                    mTxtGoodNum.requestFocus();

                    mLinearSelectNum.setVisibility(View.INVISIBLE);
                    mLinearInfo.setVisibility(View.VISIBLE);
                    mLinearTips.setVisibility(View.VISIBLE);
                    break;

                case KeyEvent.KEYCODE_ENTER:
                    mTxtGoodNum.setText(""+(mCycleWheelView.getSelection()+1));
                    mTxtGoodNum.requestFocus();

                    setTotalPrice();
                    mLinearSelectNum.setVisibility(View.INVISIBLE);
                    mLinearInfo.setVisibility(View.VISIBLE);
                    mLinearTips.setVisibility(View.VISIBLE);
                    break;

            }
            return true;
        }else if(mLinearSelectCity.isShown()) {
            if(keyCode == KeyEvent.KEYCODE_BACK){
                mTxtAddress.requestFocus();

                mLinearSelectCity.setVisibility(View.INVISIBLE);
                mLinearInfo.setVisibility(View.VISIBLE);
                mLinearTips.setVisibility(View.VISIBLE);
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }




    private View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus){
                if(v.getId()==R.id.write_edit_username){
                    mEditUserName.setSelection(mEditUserName.getText().toString().length());
                }else if(v.getId()==R.id.write_edit_userphone){
                    mEditUserPhone.setSelection(mEditUserPhone.getText().toString().length());
                }
                mCheckBox.setFocusable(false);
                mTxtProtocol.setFocusable(false);
                mBtnCancel.setFocusable(false);
            }else {
                mCheckBox.setFocusable(true);
                mTxtProtocol.setFocusable(true);
                mBtnCancel.setFocusable(true);
            }
        }
    };


    private class WheelItemSelectedListener implements CycleWheelView.WheelItemSelectedListener {
        @Override
        public void onItemSelected(int position, String label) {
            mNumPosition = position;
            LogUtils.d(DetailConstant.TAG_DETAIL, TAG+"onItemSelected  mNumPosition:"+ mNumPosition +"  label:"+label);
        }
    }

    private class CityHolder {
        Button city0;
        Button city1;
        Button city2;
        Button city3;
        Button city4;

        Button[] layouts = {
                city0 =(Button)findViewById(R.id.write_btn_city0),
                city1 =(Button)findViewById(R.id.write_btn_city1),
                city2 =(Button)findViewById(R.id.write_btn_city2),
                city3 =(Button)findViewById(R.id.write_btn_city3),
                city4 =(Button)findViewById(R.id.write_btn_city4),
        };

        public Button get(int index) {
            return layouts[index];
        }
    }

    private class CityClick implements View.OnClickListener {
        private int onClickIndex;

        public CityClick(int i){
            onClickIndex = i;
        }
        @Override
        public void onClick(View v) {
            mTxtAddress.setText(mCityHolder.get(onClickIndex).getText());
            mLinearSelectCity.setVisibility(View.INVISIBLE);
            mLinearInfo.setVisibility(View.VISIBLE);
            mLinearTips.setVisibility(View.VISIBLE);
            mTxtAddress.requestFocus();
            mCityPosition = onClickIndex;
        }
    }

    private void setTotalPrice() {
        int num = Integer.valueOf(mTxtGoodNum.getText().toString());
        double totalPrice = mGoodPrice*num;
        mTxtGoodTotal.setText(CommonUtils.formatPrice(""+totalPrice));
    }


    private void controlKeyboardLayout(final ScrollView root, final Activity context) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect rect = new Rect();
                root.getWindowVisibleDisplayFrame(rect);
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                //若不可视区域高度大于100，则键盘显示
                if (rootInvisibleHeight > 100) {
                    int[] location = new int[2];
                    View focus = context.getCurrentFocus();
                    if (focus != null) {
                        focus.getLocationInWindow(location);
                        int scrollHeight = (location[1] + focus.getHeight()) - rect.bottom;

                        if (rect.bottom < location[1] + focus.getHeight()) {
                            LogUtils.i(DetailConstant.TAG_DETAIL,TAG+"controlKeyboardLayout-->scrollHeight:"+scrollHeight);
                            root.scrollTo(0, scrollHeight);

                        }
                    }
                } else {
                    //键盘隐藏
                    root.scrollTo(0, 0);
                }
            }
        });
    }



}
