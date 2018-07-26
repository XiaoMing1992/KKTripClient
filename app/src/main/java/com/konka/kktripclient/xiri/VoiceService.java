package com.konka.kktripclient.xiri;

import android.content.Intent;
import android.util.Log;

import com.iflytek.xiri.AppService;

public class VoiceService extends AppService {
    // 目前暂不知是哪个Listener
    private ILocalAppListener listener = new ILocalAppListener() {
        @Override
        public void onExecute(Intent intent) {
            Log.d(TAG, "onExecute");
            // TODO
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        setLocalAppListener(listener);
    }

    @Override
    protected void onInit() {
        Log.d(TAG, "onInit");
    }

}
