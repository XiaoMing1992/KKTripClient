package com.konka.kktripclient.pay;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.konka.commons.codec.binary.Base64;
import com.konka.kktripclient.R;
import com.konka.kktripclient.detail.DetailConstant;
import com.konka.kktripclient.net.HttpHelper;
import com.konka.kktripclient.net.info.PayRequestInfo;
import com.konka.kktripclient.net.info.PayReturnInfo;
import com.konka.kktripclient.net.json.StringConverter;
import com.konka.kktripclient.utils.Constant;
import com.konka.kktripclient.utils.LogUtils;
import com.konka.tvpay.KKPayClient;
import com.konka.tvpay.data.builder.KonkaPayOrderBuilder;

import org.json.JSONObject;

/**
 * Created by Zhou Weilin on 2017-6-27.
 */

public class PayHelper {
    private final String TAG = PayHelper.class.getSimpleName();
    private Activity mActivity;
    private Context mContext;
    private KKPayClient mKKPayClient;
    private PayVerifyTask mParseTask;
    private String mOrderId;
    private PayReturnInfo mPayReturnInfo;
    private final String FLAG_PAY_ORDER_INFO = "payOrderInfo";
    private final String FLAG_PAY_ORDER_ID = "payOrderId";
    private final String RET_CODE_SUCCEED = "0";

    public PayHelper(Activity activity){
        mActivity = activity;
        mContext = activity.getApplicationContext();
        mKKPayClient = new KKPayClient(mContext);
    }

    public String getOrderId(){
        return mOrderId;
    }

    public PayReturnInfo getPayReturnInfo(){return mPayReturnInfo;}


    //支付前的加密验证
    public void startKKpay(PayInfoBean payInfo){
        JSONObject js = new JSONObject();
        try {
            js.put("userId", payInfo.getUserid());
            js.put("userName",payInfo.getUsername());
            js.put("userOrigin",payInfo.getUserorigin());
            js.put("deviceBrand",payInfo.getDevicebrand());
            js.put("deviceId", payInfo.getDeviceid());
            js.put("deviceIp",payInfo.getDeviceip());
            js.put("goodsType",payInfo.getGoodstype());
            js.put("goodsId",payInfo.getGoodsid());
            js.put("goodsName",payInfo.getGoodsname());
            js.put("amount",payInfo.getAmount());
            js.put("price",payInfo.getPrice());
            JSONObject cp = new JSONObject();
            cp.put("name",payInfo.getName());
            cp.put("tel",payInfo.getTel());
            cp.put("address",payInfo.getAddress());
            js.put("cpPrivateInfo",cp);
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->startKKpay:"+js.toString());

            byte[] bytes = AndroidClientRSAUtil.encryptByPublicKey(js.toString().getBytes("UTF-8"), Constant.PUBLIC_KEY);
            String keytext = Base64.encodeBase64String(bytes);
            startPayVerify(keytext);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void startPayVerify(String payKey) {
        cancelPayVerify();
        mOrderId = null;
        mPayReturnInfo = null;
        mParseTask = new PayVerifyTask();
        mParseTask.execute(FLAG_PAY_ORDER_INFO,payKey);
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->startPayVerify");
    }

    public void cancelPayVerify() {
        if(null != mParseTask && mParseTask.getStatus() == AsyncTask.Status.RUNNING){
            mParseTask.cancel(true);
        }
    }

    public void startPayByOrderId(String orderId){
        cancelPayVerify();
        mOrderId = orderId;
        mPayReturnInfo = null;
        mParseTask = new PayVerifyTask();
        mParseTask.execute(FLAG_PAY_ORDER_ID,mOrderId);
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->startPayByOrderId-->mOrderId:"+mOrderId);
    }


    private class PayVerifyTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... info) {
            String flag = info[0];
            String data = info[1];
            LogUtils.d(DetailConstant.TAG_DETAIL, TAG+"-->PayVerifyTask-->doInBackground-->flag:"+flag+"");
            String sourceInfo = null;
            if(FLAG_PAY_ORDER_INFO.equals(flag)){
                sourceInfo = HttpHelper.getInstance(mContext).getPayVerifyResult(data);
            }else if(FLAG_PAY_ORDER_ID.equals(flag)){
                sourceInfo = HttpHelper.getInstance(mContext).getPayInfoById(data);
            }

            return sourceInfo;
        }


        protected void onPostExecute(String result){
            LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->PayVerifyTask-->onPostExecute-->result:"+result);
            if(TextUtils.isEmpty(result)){
                onError("PayVerifyTask result null!!!");
            }else {
                startPayActivity(result);
            }
        }
    }

    private void startPayActivity(String reStr){
        try {
            GsonBuilder gb = new GsonBuilder();
            Gson gson = gb.registerTypeAdapter(String.class, new StringConverter())
                    .disableHtmlEscaping()
                    .create();
            PayRequestInfo payRequestInfo = gson.fromJson(reStr, PayRequestInfo.class);
            String retCode = payRequestInfo.getRet().getRet_code();
            LogUtils.d(DetailConstant.TAG_DETAIL, TAG+"-->startPayActivity-->retCode:" + retCode);

            if(RET_CODE_SUCCEED.equals(retCode)){
                if (AndroidClientRSAUtil.verify(Base64.decodeBase64(payRequestInfo.getData().getCiphertext()), Constant.PUBLIC_KEY, payRequestInfo.getData().getSign())) {// 验签
                    String plainTxt = new String(AndroidClientRSAUtil.decryptByPublicKey(Base64.decodeBase64(payRequestInfo.getData().getCiphertext()), Constant.PUBLIC_KEY), "UTF-8");
                    PayReturnInfo payReturnInfo = gson.fromJson(plainTxt, PayReturnInfo.class);
                    //payReturnInfo.getCpPrivateInfo()里的address和name都为空，后台做了置空处理，防止支付系统里的值超过64位

                    LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->startPayActivity-->payReturnInfo = "+payReturnInfo.toString());

                    mOrderId = payReturnInfo.getOrderId();
                    mPayReturnInfo = payReturnInfo;
                    LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->startPayActivity-->mOrderId = "+mOrderId);
                    KonkaPayOrderBuilder orderBuilder = new KonkaPayOrderBuilder()
                            .setCpId(payReturnInfo.getCpId())
                            .setAppId(payReturnInfo.getAppId())
                            .setGoodsId("" + payReturnInfo.getGoodsId())
                            .setGoodsName(payReturnInfo.getGoodsName())
                            .setCpOrderId(payReturnInfo.getOrderId())
                            .setPrice(payReturnInfo.getPrice())
                            .setPayAmount(payReturnInfo.getAmount())
                            .setAppUserId(UserHelper.getInstance(mContext).getOpenId())
                            .setDistributionChannels(Constant.CHANNEL_ID)
                            .setCpPrivateInfo(payReturnInfo.getCpPrivateInfo().toString())
                            .setNotifyUrl(payReturnInfo.getNotifyUrl())
                            .setUseKonkaUserSys("1")
                            .setSign(payReturnInfo.getSign());
                    mKKPayClient.pay(mActivity, orderBuilder);

                }
            }else {
                onError("ret_code not 0");
            }



        } catch (Exception e) {
            e.printStackTrace();
            onError(e.getMessage());
        }
    }

    private void onError(String reason){
        LogUtils.d(DetailConstant.TAG_DETAIL,TAG+"-->onError-->reason:"+reason);
        Toast.makeText(mContext,mContext.getString(R.string.write_pay_failure),Toast.LENGTH_SHORT).show();
    }
}
