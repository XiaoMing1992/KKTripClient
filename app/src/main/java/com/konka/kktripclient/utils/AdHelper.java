package com.konka.kktripclient.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

public class AdHelper {

    private static final String TAG = "AdHelper";
    private static AdHelper mInstance = null;

    private Context mContext;

    private LoadADSucessReceiver mReceiver = null;

    private AdHelper(){


    }

    public static AdHelper getInstance(){
        if(mInstance == null){
            mInstance = new AdHelper();
        }
        return mInstance;
    }



    public void init(Context context){
        mContext = context;
        if(mReceiver == null){
            mReceiver = new LoadADSucessReceiver();
        }
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, new IntentFilter("com.konka.android.action.AD_LOAD_SUCCESS"));
    }

    public void release(){

        if(mReceiver != null){
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }



    class LoadADSucessReceiver extends BroadcastReceiver {



        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equalsIgnoreCase("com.konka.android.action.AD_LOAD_SUCCESS ")){
                int id = intent.getIntExtra("ad_load_posid",-1);
                String path = intent.getStringExtra("ad_save_path");
                LogUtils.d(TAG,path);
            }
        }
    }





}
