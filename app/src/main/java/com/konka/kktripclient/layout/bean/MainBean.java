package com.konka.kktripclient.layout.bean;

import java.util.List;

/**
 * Created by The_one on 2017-5-15.
 * <p>
 * MainBean
 */

public class MainBean {
    private int coordinateX;
    private int coordinateY;
    private int globalFilletDegree;
    private int globalHeight;
    private int globalWidth;
    private int layoutDirection;
    private MenuStyleBean menuStyle;
    private List<TabBean> tabs;
    private int themeId;

    public MainBean() {

    }

    public MainBean(int coordinateX, int coordinateY, int globalFilletDegree, int globalHeight, int globalWidth, int layoutDirection, MenuStyleBean menuStyle, List<TabBean> tabs, int themeId) {
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.globalFilletDegree = globalFilletDegree;
        this.globalHeight = globalHeight;
        this.globalWidth = globalWidth;
        this.layoutDirection = layoutDirection;
        this.menuStyle = menuStyle;
        this.tabs = tabs;
        this.themeId = themeId;
    }

    @Override
    public String toString() {
        return "MainBean{" +
                "coordinateX=" + coordinateX +
                ", coordinateY=" + coordinateY +
                ", globalFilletDegree=" + globalFilletDegree +
                ", globalHeight=" + globalHeight +
                ", globalWidth=" + globalWidth +
                ", layoutDirection=" + layoutDirection +
                ", menuStyle=" + menuStyle +
                ", tabs=" + tabs +
                ", themeId=" + themeId +
                '}';
    }

    public int getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(int coordinateX) {
        this.coordinateX = coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(int coordinateY) {
        this.coordinateY = coordinateY;
    }

    public int getGlobalFilletDegree() {
        return globalFilletDegree;
    }

    public void setGlobalFilletDegree(int globalFilletDegree) {
        this.globalFilletDegree = globalFilletDegree;
    }

    public int getGlobalHeight() {
        return globalHeight;
    }

    public void setGlobalHeight(int globalHeight) {
        this.globalHeight = globalHeight;
    }

    public int getGlobalWidth() {
        return globalWidth;
    }

    public void setGlobalWidth(int globalWidth) {
        this.globalWidth = globalWidth;
    }

    public int getLayoutDirection() {
        return layoutDirection;
    }

    public void setLayoutDirection(int layoutDirection) {
        this.layoutDirection = layoutDirection;
    }

    public MenuStyleBean getMenuStyle() {
        return menuStyle;
    }

    public void setMenuStyle(MenuStyleBean menuStyle) {
        this.menuStyle = menuStyle;
    }

    public List<TabBean> getTabs() {
        return tabs;
    }

    public void setTabs(List<TabBean> tabs) {
        this.tabs = tabs;
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

}
