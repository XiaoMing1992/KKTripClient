package com.konka.kktripclient.layout.data;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 后台数据管理
 */
public class KKServerDataManager {
    /**
     * 线程池
     */
    private ExecutorService mExecutorService;
    private Context mContext;
    private static KKServerDataManager sInstance;

    public static KKServerDataManager getInstance(Context tContext) {
        if (sInstance == null) {
            synchronized (KKServerDataManager.class) {
                if (sInstance == null) {
                    sInstance = new KKServerDataManager(tContext);
                }
            }
        }
        return sInstance;
    }

    private KKServerDataManager(Context tContext) {
        mContext = tContext;
        mExecutorService = Executors.newFixedThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "KK-TripServer");
            }
        });
    }

    public void getTabListDataCache(KKServerDataListener<KKBaseDataInfo> tListener) {
        if (tListener == null) {
            throw new IllegalArgumentException("Listener can't be null");
        }
        mExecutorService.execute(new KKServerJSONTabCacheRunnable(mContext, tListener));
    }

    public void close() {
        try {
            mExecutorService.shutdownNow();
            sInstance = null;
            mContext = null;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
