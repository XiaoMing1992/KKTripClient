package com.konka.kktripclient.layout.data;

import java.util.ArrayList;

/**
 * tab列表条目的数据类型
 *
 * @author The_one
 */
public class KKTabDataInfo extends KKBaseDataInfo {
    /**
     * 默认序号
     */
    public int orderID;

    /**
     * theme_menu_dimension_business主键
     */
    public int beanid;
    /**
     * tab业务id,标志tab对应关系:我的->T431001 应用->T431002,默认是0,没有特殊指定
     */
    public String businessidTab;
    /**
     * tab是否顺序固定 0:不固定 1:固定
     */
    public String fixed;
    /**
     * 是否锁定在首页 0：锁定 1：不锁定 默认 1
     */
    public int isLockedHomepage;
    /**
     * tab菜单文字背景图
     */
    public String menuIcon;
    /**
     * tab菜单栏目名称,用于显示
     */
    public String menuItemName;
    /**
     * tab权重 值越大,越靠前
     */
    public int menuItemWeight;
    /**
     * tab背景图
     */
    public String tabBackground;
    /**
     * theme_tab_layout主键
     */
    public int tabLayoutId;
    /**
     * tab下广告位布局和广告位数据
     */
    public ArrayList<KKBaseDataInfo> adverList;
}
