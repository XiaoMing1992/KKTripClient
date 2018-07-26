package com.konka.kktripclient.layout.bean;

/**
 * Created by The_one on 2017-5-15.
 * <p>
 * MenuStyleBean
 */

public class MenuStyleBean {
    private int fontSize;
    private int menuCoordinateX;
    private int menuCoordinateY;
    private int menuDirection;
    private int menuGap;
    private int menuId;

    public MenuStyleBean() {

    }

    public MenuStyleBean(int fontSize, int menuCoordinateX, int menuCoordinateY, int menuDirection, int menuGap, int menuId) {
        this.fontSize = fontSize;
        this.menuCoordinateX = menuCoordinateX;
        this.menuCoordinateY = menuCoordinateY;
        this.menuDirection = menuDirection;
        this.menuGap = menuGap;
        this.menuId = menuId;
    }

    @Override
    public String toString() {
        return "MenuStyleBean{" +
                "fontSize=" + fontSize +
                ", menuCoordinateX=" + menuCoordinateX +
                ", menuCoordinateY=" + menuCoordinateY +
                ", menuDirection=" + menuDirection +
                ", menuGap=" + menuGap +
                ", menuId=" + menuId +
                '}';
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getMenuCoordinateX() {
        return menuCoordinateX;
    }

    public void setMenuCoordinateX(int menuCoordinateX) {
        this.menuCoordinateX = menuCoordinateX;
    }

    public int getMenuCoordinateY() {
        return menuCoordinateY;
    }

    public void setMenuCoordinateY(int menuCoordinateY) {
        this.menuCoordinateY = menuCoordinateY;
    }

    public int getMenuDirection() {
        return menuDirection;
    }

    public void setMenuDirection(int menuDirection) {
        this.menuDirection = menuDirection;
    }

    public int getMenuGap() {
        return menuGap;
    }

    public void setMenuGap(int menuGap) {
        this.menuGap = menuGap;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }
}
