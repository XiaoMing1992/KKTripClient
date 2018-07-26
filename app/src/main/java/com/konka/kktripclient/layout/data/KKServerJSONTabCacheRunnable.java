package com.konka.kktripclient.layout.data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.konka.kktripclient.layout.util.LayoutConstant;
import com.konka.kktripclient.net.HttpHelper;
import com.konka.kktripclient.utils.Constant;
import com.konka.kktripclient.utils.LogUtils;
import com.konka.kktripclient.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by The_one on 2017-5-15.
 * <p>
 * 获取全局页面动态配置的数据
 */
public class KKServerJSONTabCacheRunnable implements Runnable {
    private final String TAG = this.getClass().getSimpleName();
    private Context mContext;

    private static final String TAB_JSON_FILE_NAME = "/tab.json";

    private static final int DEFAULT_RETRY_COUNT = 2;
    private int mRetryCount = 0;
    private int mCurrentRetryCount = 0;

    private KKServerDataListener<KKBaseDataInfo> mListener;
    private int mXOffset = 0;
    private int mYOffset = 0;

    /**
     * @param listener 回调，不可为null
     */
    public KKServerJSONTabCacheRunnable(Context context, KKServerDataListener<KKBaseDataInfo> listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener can't be null");
        }
        setRetryCount(DEFAULT_RETRY_COUNT);
        mCurrentRetryCount = 0;
        mContext = context;
        mListener = listener;
    }

    public void setRetryCount(int count) {
        mRetryCount = count;
    }

    @Override
    public void run() {
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        Thread.yield();

        mListener.onLoadStart();
        // 从网络获取tabData
        while (mCurrentRetryCount < mRetryCount) {
            URL tURL;
            URLConnection tConnection;
            InputStreamReader tInputStreamReader = null;
            try {
                long beginMillis = System.currentTimeMillis();

                tURL = new URL(HttpHelper.getInstance(mContext).getTripTabsUrl());
                LogUtils.d(TAG, "HttpHelper.getInstance(mContext).getTripTabsUrl() = " + HttpHelper.getInstance(mContext).getTripTabsUrl());
                tConnection = tURL.openConnection();
                tConnection.setConnectTimeout(5000);
                tConnection.setRequestProperty("Accept-Charset", "utf-8");

                char[] tData = new char[1024];
                StringBuffer tBuffer = new StringBuffer();
                int tLength;
                tInputStreamReader = new InputStreamReader(tConnection.getInputStream());
                while ((tLength = tInputStreamReader.read(tData)) != -1) {
                    tBuffer.append(tData, 0, tLength);
                }
                String tDataString = tBuffer.toString();
                if (tBuffer.length() > 0) {
                    tBuffer.delete(0, tBuffer.length() - 1);
                }

                LogUtils.d(TAG, "Http response millis = " + (System.currentTimeMillis() - beginMillis));
                LogUtils.d(TAG, tDataString);

                JSONObject rootObject = new JSONObject(tDataString);
                JSONObject retObject = rootObject.getJSONObject("ret");
                String ret = retObject.getString("ret_code");
                if (ret.equals(Constant.RETURN_SUCCESS)) {
                    JSONObject dataObject = rootObject.getJSONObject("data");
                    // 服务器地址
                    if (dataObject.has("serverAddr")) {
                        String tServerAddr = dataObject.getString("serverAddr");
                        LogUtils.d(TAG, "serverAddr = " + tServerAddr);
                        if (!tServerAddr.equals(getServerAddress())) {
                            setServerAddress(tServerAddr);
                            setUpdateTime(0);
                        }
                        Constant.SERVICE_ADDRESS = tServerAddr;
                    }

                    String url = dataObject.getString("contentUrl");
                    LogUtils.d(TAG, "begin url = " + url);
                    url = NetworkUtils.toURL(url);
                    LogUtils.d(TAG, "after url = " + url);

                    long update_time = dataObject.getLong("updateTime");
                    String jsonStr = getTabJSON(url, update_time);

                    if (jsonStr != null && !jsonStr.equals("")) {
                        ArrayList<KKBaseDataInfo> tList = getTabList(jsonStr);
                        KKBaseDataInfo tTabButton = getTabButton(jsonStr);
                        if (tList != null) {
                            mListener.onLoadSuccess(tList, tTabButton);
                            return;
                        }
                    }
                } else {
                    LogUtils.d(TAG, "http getTabData failure");
                }
            } catch (Exception e) {
                LogUtils.d(TAG, "http getTabData failure");
            } finally {
                if (tInputStreamReader != null) {
                    try {
                        tInputStreamReader.close();
                    } catch (Exception e) {
                        LogUtils.d(TAG, "InputStreamReader.close failure");
                    }
                }
            }
            mCurrentRetryCount++;
        }

        // 从本地获取tabData
        try {
            Constant.SERVICE_ADDRESS = getServerAddress();
            String jsonStr = readJSON();
            ArrayList<KKBaseDataInfo> tList = getTabList(jsonStr);
            KKBaseDataInfo tTabButton = getTabButton(jsonStr);
            if (tList != null) {
                mListener.onLoadSuccess(tList, tTabButton);
                return;
            }
        } catch (Exception e) {
            LogUtils.d(TAG, "load location tabData failure");
        }

        // tabData加载失败
        mListener.onLoadFail();

    }

    private void saveJSON(String content) {
        try {
            String outFile = mContext.getApplicationContext().getFilesDir().getParent() + TAB_JSON_FILE_NAME;
            FileWriter writer = new FileWriter(outFile, false);
            writer.write(content);
            writer.close();
        } catch (Exception e) {
            LogUtils.d(TAG, "saveJSON failure");
        }
    }

    private String readJSON() {
        try {
            String outFile = mContext.getApplicationContext().getFilesDir().getParent() + TAB_JSON_FILE_NAME;
            FileReader reader = new FileReader(outFile);

            char[] tData = new char[1024];
            StringBuffer tBuffer = new StringBuffer();
            int tLength;
            while ((tLength = reader.read(tData)) != -1) {
                tBuffer.append(tData, 0, tLength);
            }
            reader.close();

            return tBuffer.toString();
        } catch (Exception e) {
            LogUtils.d(TAG, "readJSON failure");
        }

        return null;
    }

    private boolean isJSONExist() {
        try {
            String outFile = mContext.getApplicationContext().getFilesDir().getParent() + TAB_JSON_FILE_NAME;
            File file = new File(outFile);
            return file.exists();
        } catch (Exception e) {
            LogUtils.d(TAG, "isJSONExist failure");
        }
        return false;
    }

    private void setServerAddress(String address) {
        try {
            SharedPreferences preferences = mContext.getSharedPreferences(Constant.PREFERENCES_NAME_TAB, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Constant.PREFERENCES_VALUE_TAB_SERVICE_ADDRESS, address);
            editor.apply();
        } catch (Exception e) {
            LogUtils.d(TAG, "setServerAddress failure");
        }
    }

    private String getServerAddress() {
        try {
            SharedPreferences preferences = mContext.getSharedPreferences(Constant.PREFERENCES_NAME_TAB, MODE_PRIVATE);
            return preferences.getString(Constant.PREFERENCES_VALUE_TAB_SERVICE_ADDRESS, "");
        } catch (Exception e) {
            LogUtils.d(TAG, "getServerAddress failure");
        }
        return null;
    }

    private void setUpdateTime(long time) {
        try {
            SharedPreferences preferences = mContext.getSharedPreferences(Constant.PREFERENCES_NAME_TAB, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(Constant.PREFERENCES_VALUE_TAB_UPDATE_TIME, time);
            editor.apply();
        } catch (Exception e) {
            LogUtils.d(TAG, "setUpdateTime failure");
        }
    }

    private long getUpdateTime() {
        try {
            SharedPreferences preferences = mContext.getSharedPreferences(Constant.PREFERENCES_NAME_TAB, MODE_PRIVATE);
            return preferences.getLong(Constant.PREFERENCES_VALUE_TAB_UPDATE_TIME, 0);
        } catch (Exception e) {
            LogUtils.d(TAG, "getUpdateTime failure");
        }
        return 0;
    }

    /**
     * 获取并保存tab布局数据
     *
     * @param url         访问地址
     * @param update_time 更新时间
     * @return 返回tab布局数据
     */
    private String getTabJSON(String url, long update_time) {
        try {
            //判断是否存在缓存JSON文件
            long defaultTime = getUpdateTime();

            //不存在或文件已更新时从网络获取JSON，并存在本地
            if (!isJSONExist() || defaultTime < update_time) {
                URL tURL;
                URLConnection tConnection;
                InputStreamReader tInputStreamReader = null;
                try {
                    tURL = new URL(url);
                    tConnection = tURL.openConnection();
                    tConnection.setRequestProperty("Accept-Charset", "utf-8");

                    char[] tData = new char[1024];
                    StringBuffer tBuffer = new StringBuffer();
                    int tLength;
                    tInputStreamReader = new InputStreamReader(tConnection.getInputStream());
                    while ((tLength = tInputStreamReader.read(tData)) != -1) {
                        tBuffer.append(tData, 0, tLength);
                    }

                    saveJSON(tBuffer.toString());
                    setUpdateTime(update_time);

                    return tBuffer.toString();
                } catch (Exception e) {
                    LogUtils.d(TAG, "getTabJSON download failure");
                    return readJSON();
                } finally {
                    if (tInputStreamReader != null) tInputStreamReader.close();
                }
            } else {
                return readJSON();
            }
        } catch (Exception e) {
            LogUtils.d(TAG, "getTabJSON failure");
        }
        return null;
    }

    /**
     * 获取实际的TabButton数据
     *
     * @param jsonStr 传入tab的JSONObject
     * @return mKKTabButtonDataInfo
     * @throws Exception 抛出异常
     */
    private KKTabButtonDataInfo getTabButton(String jsonStr) throws Exception {
        KKTabButtonDataInfo tabButtonDataInfo = new KKTabButtonDataInfo();
        JSONObject rootObject = new JSONObject(jsonStr);
        if (rootObject.has(LayoutConstant.mainCoordinateX)) {
            mXOffset = rootObject.getInt(LayoutConstant.mainCoordinateX);
        }
        if (rootObject.has(LayoutConstant.mainCoordinateY)) {
            mYOffset = rootObject.getInt(LayoutConstant.mainCoordinateY);
        }
        if (rootObject.has(LayoutConstant.menuStyle)) {
            JSONObject menuStyleObject = rootObject.getJSONObject(LayoutConstant.menuStyle);
            if (menuStyleObject.has(LayoutConstant.fontSize)) {
                tabButtonDataInfo.fontSize = menuStyleObject.getInt(LayoutConstant.fontSize);
            }
            if (menuStyleObject.has(LayoutConstant.menuCoordinateX)) {
                int menuCoordinateX = menuStyleObject.getInt(LayoutConstant.menuCoordinateX);
                tabButtonDataInfo.menuCoordinateX = menuCoordinateX + mXOffset;
            }
            if (menuStyleObject.has(LayoutConstant.menuCoordinateY)) {
                int menuCoordinateY = menuStyleObject.getInt(LayoutConstant.menuCoordinateY);
                tabButtonDataInfo.menuCoordinateY = menuCoordinateY + mYOffset;
            }
            if (menuStyleObject.has(LayoutConstant.menuDirection)) {
                tabButtonDataInfo.menuDirection = menuStyleObject.getInt(LayoutConstant.menuDirection);
            }
            if (menuStyleObject.has(LayoutConstant.menuGap)) {
                tabButtonDataInfo.menuGap = menuStyleObject.getInt(LayoutConstant.menuGap);
            }
            if (menuStyleObject.has(LayoutConstant.menuId)) {
                tabButtonDataInfo.menuId = menuStyleObject.getInt(LayoutConstant.menuId);
            }
        }
        return tabButtonDataInfo;
    }

    /**
     * 获取实际的Tab页数据
     *
     * @param jsonStr 传入tab的JSONObject
     * @return tabList
     * @throws Exception 异常
     */
    private ArrayList<KKBaseDataInfo> getTabList(String jsonStr) throws Exception {
        try {
            // 返回的Tab数据链表
            ArrayList<KKBaseDataInfo> tabList = new ArrayList<>();

            JSONObject rootObject = new JSONObject(jsonStr);

            // 公用数据——动态布局数据
            if (rootObject.has(LayoutConstant.themeId)) {
                String theme_id = rootObject.getString(LayoutConstant.themeId);
            }
            if (rootObject.has(LayoutConstant.mainCoordinateX)) {
                mXOffset = rootObject.getInt(LayoutConstant.mainCoordinateX);
            }
            if (rootObject.has(LayoutConstant.mainCoordinateY)) {
                mYOffset = rootObject.getInt(LayoutConstant.mainCoordinateY);
            }
            if (rootObject.has(LayoutConstant.globalFilletDegree)) {
                int global_fillet_degree = rootObject.getInt(LayoutConstant.globalFilletDegree);
            }
            if (rootObject.has(LayoutConstant.globalHeight)) {
                int global_height = rootObject.getInt(LayoutConstant.globalHeight);
            }
            if (rootObject.has(LayoutConstant.globalWidth)) {
                int global_width = rootObject.getInt(LayoutConstant.globalWidth);
            }
            if (rootObject.has(LayoutConstant.layoutDirection)) {
                int layout_direction = rootObject.getInt(LayoutConstant.layoutDirection);
            }

            // Tab数据，包含Tab自身数据，以及此Tab下所有广告位数据
            JSONArray tabArray = rootObject.getJSONArray(LayoutConstant.tabs);
            for (int i = 0; i < tabArray.length(); i++) {
                JSONObject tabObject = tabArray.getJSONObject(i);
                KKTabDataInfo tabDataInfo = new KKTabDataInfo();

                if (tabObject.has(LayoutConstant.beanid)) {
                    tabDataInfo.beanid = tabObject.getInt(LayoutConstant.beanid);
                }
                if (tabObject.has(LayoutConstant.businessidTab)) {
                    tabDataInfo.businessidTab = tabObject.getString(LayoutConstant.businessidTab);
                }
                if (tabObject.has(LayoutConstant.fixed)) {
                    tabDataInfo.fixed = tabObject.getString(LayoutConstant.fixed);
                }
                if (tabObject.has(LayoutConstant.isLockedHomepage)) {
                    tabDataInfo.isLockedHomepage = tabObject.getInt(LayoutConstant.isLockedHomepage);
                }
                if (tabObject.has(LayoutConstant.menuIcon)) {
                    String menuIcon = tabObject.getString(LayoutConstant.menuIcon);
                    tabDataInfo.menuIcon = NetworkUtils.toURL(menuIcon);
                }
                if (tabObject.has(LayoutConstant.menuItemName)) {
                    tabDataInfo.menuItemName = tabObject.getString(LayoutConstant.menuItemName);
                }
                if (tabObject.has(LayoutConstant.menuItemWeight)) {
                    tabDataInfo.menuItemWeight = tabObject.getInt(LayoutConstant.menuItemWeight);
                }
                if (tabObject.has(LayoutConstant.tabBackground)) {
                    String tabBackground = tabObject.getString(LayoutConstant.tabBackground);
                    tabDataInfo.tabBackground = NetworkUtils.toURL(tabBackground);
                }
                if (tabObject.has(LayoutConstant.tabLayoutId)) {
                    tabDataInfo.tabLayoutId = tabObject.getInt(LayoutConstant.tabLayoutId);
                }
                // 广告位数据
                if (tabObject.has(LayoutConstant.advers)) {
                    JSONArray adverArray = tabObject.getJSONArray(LayoutConstant.advers);
                    ArrayList<KKBaseDataInfo> adverList = new ArrayList<>();
                    for (int j = 0; j < adverArray.length(); j++) {
                        JSONObject adverObject = adverArray.getJSONObject(j);
                        KKTabItemDataInfo adverDataInfo = getAdverData(tabDataInfo.menuItemName, adverObject);
                        adverList.add(adverDataInfo);
                    }
                    tabDataInfo.adverList = adverList;
                }

                tabList.add(tabDataInfo);
            }

            return tabList;
        } catch (Exception e) {
            LogUtils.d(TAG, "getTabList failure");
        }

        return null;
    }

    /**
     * 设置单个adver的布局信息
     *
     * @param name        tabName
     * @param adverObject 传入adverDataInfo的布局信息的JSONObject
     * @return adverDataInfo
     * @throws Exception 异常
     */
    private KKTabItemDataInfo getAdverData(String name, JSONObject adverObject) throws Exception {
        KKTabItemDataInfo adverDataInfo = new KKTabItemDataInfo();
        adverDataInfo.tabName = name;

        if (adverObject.has(LayoutConstant.adverFilletDegree)) {
            adverDataInfo.filletDegree = adverObject.getInt(LayoutConstant.adverFilletDegree);
        }
        if (adverObject.has(LayoutConstant.adverHeight)) {
            adverDataInfo.height = adverObject.getInt(LayoutConstant.adverHeight);
        }
        if (adverObject.has(LayoutConstant.adverLayoutId)) {
            adverDataInfo.layoutId = adverObject.getInt(LayoutConstant.adverLayoutId);
        }
        if (adverObject.has(LayoutConstant.adverWidth)) {
            adverDataInfo.width = adverObject.getInt(LayoutConstant.adverWidth);
        }
        if (adverObject.has(LayoutConstant.coordinateX)) {
            int coordinateX = adverObject.getInt(LayoutConstant.coordinateX);
            adverDataInfo.x = coordinateX + mXOffset;
        }
        if (adverObject.has(LayoutConstant.coordinateY)) {
            int coordinateY = adverObject.getInt(LayoutConstant.coordinateY);
            adverDataInfo.y = coordinateY + mYOffset;
        }

        try {
            // 获取内容数据
            if (adverObject.has(LayoutConstant.content)) {
                JSONArray contentArray = adverObject.getJSONArray(LayoutConstant.content);
                // 获取第几个？
                JSONObject contentObject = contentArray.getJSONObject(0);
                getContentParams(adverDataInfo, contentObject);

                // 获取启动数据
                try {
                    if (contentObject.has(LayoutConstant.openContent)) {
                        String open_contentStr = contentObject.getString(LayoutConstant.openContent);
                        JSONObject openContent = new JSONObject(open_contentStr);

                        if (adverDataInfo.type.equals("00")) {
                            adverDataInfo.startIntent = getIntent(openContent, adverDataInfo);
                        } else {
                            if (openContent.has("id")) {
                                adverDataInfo.goodsID = openContent.getInt("id");
                            }
                            if (openContent.has("name")) {
                                adverDataInfo.goodsName = openContent.getString("name");
                            }
                            if (openContent.has("type_id")) {
                                adverDataInfo.typeID = openContent.getString("type_id");
                            }
                            if (openContent.has("type_name")) {
                                adverDataInfo.typeName = openContent.getString("type_name");
                            }
                        }
                    }
                } catch (Exception e) {
                    LogUtils.d(TAG, "getAdverData openContent failure");
                }
            }
        } catch (JSONException e) {
            LogUtils.d(TAG, "getAdverData content failure");
        }
        return adverDataInfo;
    }

    /**
     * 设置单个adver的数据信息
     *
     * @param adverDataInfo 需要被设置信息的adverDataInfo
     * @param contentObject 传入信息的JSONObject
     * @return adverDataInfo
     */
    private KKTabItemDataInfo getContentParams(KKTabItemDataInfo adverDataInfo, JSONObject contentObject) {
        try {
            if (contentObject.has(LayoutConstant.adverContentId)) {
                adverDataInfo.contentId = contentObject.getInt(LayoutConstant.adverContentId);
            }
            if (contentObject.has(LayoutConstant.align)) {
                adverDataInfo.align = contentObject.getInt(LayoutConstant.align);
            }
            if (contentObject.has(LayoutConstant.enlargeScale)) {
                adverDataInfo.enlargeScale = contentObject.getDouble(LayoutConstant.enlargeScale);
            }
            if (contentObject.has(LayoutConstant.firstTitle)) {
                adverDataInfo.firstTitle = contentObject.getString(LayoutConstant.firstTitle);
            }
            if (contentObject.has(LayoutConstant.isFocus)) {
                adverDataInfo.isFocus = contentObject.getInt(LayoutConstant.isFocus);
            }
            if (contentObject.has(LayoutConstant.isShowTitle)) {
                adverDataInfo.isShowTitle = contentObject.getInt(LayoutConstant.isShowTitle);
            }
            // 打开内容类型
            if (contentObject.has(LayoutConstant.openContentType)) {
                adverDataInfo.type = contentObject.getString(LayoutConstant.openContentType);
            }
            if (contentObject.has(LayoutConstant.posterBottom)) {
                String poster = contentObject.getString(LayoutConstant.posterBottom);
                adverDataInfo.posterBottom = NetworkUtils.toURL(poster);
            }
            if (contentObject.has(LayoutConstant.posterBottomMd5)) {
                adverDataInfo.posterBottomMd5 = contentObject.getString(LayoutConstant.posterBottomMd5);
            }
            if (contentObject.has(LayoutConstant.posterMiddle)) {
                String poster = contentObject.getString(LayoutConstant.posterMiddle);
                adverDataInfo.posterMiddle = NetworkUtils.toURL(poster);
            }
            if (contentObject.has(LayoutConstant.posterMiddleMd5)) {
                adverDataInfo.posterMiddleMd5 = contentObject.getString(LayoutConstant.posterMiddleMd5);
            }
            if (contentObject.has(LayoutConstant.posterTop)) {
                String poster = contentObject.getString(LayoutConstant.posterTop);
                adverDataInfo.posterTop = NetworkUtils.toURL(poster);
            }
            if (contentObject.has(LayoutConstant.posterTopMd5)) {
                adverDataInfo.posterTopMd5 = contentObject.getString(LayoutConstant.posterTopMd5);
            }
            if (contentObject.has(LayoutConstant.secondTitle)) {
                adverDataInfo.secondTitle = contentObject.getString(LayoutConstant.secondTitle);
            }
            if (contentObject.has(LayoutConstant.state)) {
                adverDataInfo.state = contentObject.getInt(LayoutConstant.state);
            }
        } catch (JSONException e) {
            LogUtils.d(TAG, "getContentParams failure");
        }
        return adverDataInfo;
    }

    /**
     * 获取Intent启动信息
     *
     * @param openContent   传入启动方式的JSONObject
     * @param adverDataInfo 传入需要被设置属性的adverDataInfo
     * @return intent
     */
    public Intent getIntent(JSONObject openContent, KKTabItemDataInfo adverDataInfo) {
        try {
            if (openContent == null) return null;

            Intent intent = new Intent();

            if (openContent.has(LayoutConstant.appName)) {
                String name = openContent.getString(LayoutConstant.appName);
            }
            if (openContent.has(LayoutConstant.appId)) {
                String id = openContent.getString(LayoutConstant.appId);
            }
            if (openContent.has(LayoutConstant.startType)) {
                adverDataInfo.startType = openContent.getString(LayoutConstant.startType);
            }
            if (openContent.has(LayoutConstant.startparaType)) {
                String startparaType = openContent.getString(LayoutConstant.startparaType);
            }
            if (openContent.has(LayoutConstant.contentType)) {
                String contentType = openContent.getString(LayoutConstant.contentType);
            }
            if (openContent.has(LayoutConstant.packageName)) {
                String packageName = openContent.getString(LayoutConstant.packageName);
                if (openContent.has(LayoutConstant.className)) {
                    String className = openContent.getString(LayoutConstant.className);
                    if (packageName != null && !"".equals(packageName) && className != null && !"".equals(className)) {
                        intent.setClassName(packageName, className);
                    }
                } else {
                    if (packageName != null && !"".equals(packageName)) {
                        intent.setPackage(packageName);
                    }
                }
            }
            if (openContent.has(LayoutConstant.code)) {
                String code = openContent.getString(LayoutConstant.code);
            }
            if (openContent.has(LayoutConstant.ignoreVersion)) {
                String ignoreVersion = openContent.getString(LayoutConstant.ignoreVersion);
            }
            if (openContent.has(LayoutConstant.action)) {
                String action = openContent.getString(LayoutConstant.action);
                if (action != null && !"".equals(action)) {
                    intent.setAction(action);
                }
            }
            if (openContent.has(LayoutConstant.uri)) {
                String uri = openContent.getString(LayoutConstant.uri);
                if (uri != null && !uri.equals("")) {
                    intent.setData(Uri.parse(uri));
                }
            }
            if (openContent.has(LayoutConstant.flag)) {
                String flag = openContent.getString(LayoutConstant.flag);
                if (flag != null && !"".equals(flag)) {
                    try {
                        intent.addFlags(Integer.parseInt(flag));
                    } catch (Exception e) {
                        LogUtils.d(TAG, "flag parseInt failure");
                    }
                }
            }
            if (openContent.has(LayoutConstant.name)) {
                String name = openContent.getString(LayoutConstant.name);
            }
            if (openContent.has(LayoutConstant.params)) {
                try {
                    JSONArray paramsArray = openContent.getJSONArray(LayoutConstant.params);
                    Bundle bundle = new Bundle();
                    for (int i = 0; i < paramsArray.length(); i++) {
                        JSONObject paramsObject = paramsArray.getJSONObject(i);
                        int type = paramsObject.getInt("type");
                        String key = paramsObject.getString("key");
                        String value = paramsObject.getString("value");
                        switch (type) {
                            case 0:
                                bundle.putString(key, value);
                                break;
                            case 1:
                                bundle.putBoolean(key, Boolean.valueOf(value));
                                break;
                            case 2:
                                bundle.putInt(key, Integer.valueOf(value));
                                break;
                        }
                    }
                    intent.putExtras(bundle);
                } catch (Exception e) {
                    LogUtils.d(TAG, "get params failure");
                }
            }
            return intent;
        } catch (Exception e) {
            LogUtils.d(TAG, "getIntent failure");
        }
        return null;
    }

}
