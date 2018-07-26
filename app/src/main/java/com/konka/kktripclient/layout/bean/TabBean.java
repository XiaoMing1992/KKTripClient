package com.konka.kktripclient.layout.bean;

import java.util.List;

/**
 * Created by The_one on 2017-5-15.
 * <p>
 * TabBean
 */

public class TabBean {
    private List<AdverBean> advers;
    private int beanid;
    private String businessidTab;
    private String fixed;
    private int isLockedHomepage;
    private String menuIcon;
    private String menuItemName;
    private int menuItemWeight;
    private String tabBackground;
    private int tabLayoutId;

    public TabBean() {

    }

    public TabBean(List<AdverBean> advers, int beanid, String businessidTab, String fixed, int isLockedHomepage, String menuIcon, String menuItemName, int menuItemWeight, String tabBackground, int tabLayoutId) {
        this.advers = advers;
        this.beanid = beanid;
        this.businessidTab = businessidTab;
        this.fixed = fixed;
        this.isLockedHomepage = isLockedHomepage;
        this.menuIcon = menuIcon;
        this.menuItemName = menuItemName;
        this.menuItemWeight = menuItemWeight;
        this.tabBackground = tabBackground;
        this.tabLayoutId = tabLayoutId;
    }

    @Override
    public String toString() {
        return "TabBean{" +
                "advers=" + advers +
                ", beanid=" + beanid +
                ", businessidTab='" + businessidTab + '\'' +
                ", fixed='" + fixed + '\'' +
                ", isLockedHomepage=" + isLockedHomepage +
                ", menuIcon='" + menuIcon + '\'' +
                ", menuItemName='" + menuItemName + '\'' +
                ", menuItemWeight=" + menuItemWeight +
                ", tabBackground='" + tabBackground + '\'' +
                ", tabLayoutId=" + tabLayoutId +
                '}';
    }

    public List<AdverBean> getAdvers() {
        return advers;
    }

    public void setAdvers(List<AdverBean> advers) {
        this.advers = advers;
    }

    public int getBeanid() {
        return beanid;
    }

    public void setBeanid(int beanid) {
        this.beanid = beanid;
    }

    public String getBusinessidTab() {
        return businessidTab;
    }

    public void setBusinessidTab(String businessidTab) {
        this.businessidTab = businessidTab;
    }

    public String getFixed() {
        return fixed;
    }

    public void setFixed(String fixed) {
        this.fixed = fixed;
    }

    public int getIsLockedHomepage() {
        return isLockedHomepage;
    }

    public void setIsLockedHomepage(int isLockedHomepage) {
        this.isLockedHomepage = isLockedHomepage;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public int getMenuItemWeight() {
        return menuItemWeight;
    }

    public void setMenuItemWeight(int menuItemWeight) {
        this.menuItemWeight = menuItemWeight;
    }

    public String getTabBackground() {
        return tabBackground;
    }

    public void setTabBackground(String tabBackground) {
        this.tabBackground = tabBackground;
    }

    public int getTabLayoutId() {
        return tabLayoutId;
    }

    public void setTabLayoutId(int tabLayoutId) {
        this.tabLayoutId = tabLayoutId;
    }
}
