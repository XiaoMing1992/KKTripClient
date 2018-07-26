package com.konka.kktripclient.layout.tab.base;

import android.graphics.Rect;

/**
 * Created by YangDeZun on 2016/5/3.
 * <p>
 * tab的状态回调
 */
public interface TabStateCallback {
    /**
     * 由右向左进入
     */
    int PAGE_DIRECTION_RTL_IN = 1;
    /**
     * 由左向右进入
     */
    int PAGE_DIRECTION_LTR_IN = 2;
    /**
     * 由右向左离开
     */
    int PAGE_DIRECTION_RTL_OUT = 1;
    /**
     * 由左向右离开
     */
    int PAGE_DIRECTION_LTR_OUT = 2;

    /**
     * 添加tab时的回调
     */
    void tabAdded();

    /**
     * 移除tab时的回调
     */
    void tabRemoved();

    /**
     * 选中状态时的回调
     *
     * @param tFlag 是否被选中，true表示选中，false未选中，未选中的回调只会在"选中->未选中"切换的情况下才会触发
     */
    void tabSelected(boolean tFlag);

    /**
     * 边缘切页的回调
     *
     * @param tIsIn      是否是进入，true为进入，false为离开
     * @param tDirection 进入时方向(PAGE_DIRECTION_RTL_IN、PAGE_DIRECTION_LTR_IN)
     *                   离开时方向(PAGE_DIRECTION_RTL_OUT、PAGE_DIRECTION_LTR_OUT)
     * @param tRect      前一个边缘焦点的全局位置，可以使用isEmpty来判断是否有值,当tIsIn为false时，该值为null
     * @return 处理了返回true，否则false;(理论上Tab会提供一个默认的处理方法，但鉴于页面构成差异比较大，暂不提供)
     */
    boolean tabEdgeChange(boolean tIsIn, int tDirection, Rect tRect);
}
