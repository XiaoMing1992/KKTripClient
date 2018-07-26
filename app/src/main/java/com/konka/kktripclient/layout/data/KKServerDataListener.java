package com.konka.kktripclient.layout.data;

import java.util.ArrayList;

/***
 * 加载任务回调接口
 *
 * @param <T> 返回的数据类型
 */
public interface KKServerDataListener<T> {
    /**
     * 数据没有改变时的回调
     */
    public void onDataNotChange();

    /**
     * 开始加载的回调
     */
    public void onLoadStart();

    /**
     * 数据加载结束的回调
     *
     * @param data
     * @param t
     */
    public void onLoadSuccess(ArrayList<T> data, T t);

    /**
     * 加载失败的回调
     */
    public void onLoadFail();

}
