package com.konka.kktripclient.data;

import android.content.Context;

/**
 * Created by The_one on 2017-5-9.
 * <p>
 * 数据获取
 */

public class DataHelper {
    private static DataHelper instance;
    private Context mContext;

    private DataHelper(Context context) {
        mContext = context;
    }

    public static DataHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DataHelper(context);
        }
        return instance;
    }
}
