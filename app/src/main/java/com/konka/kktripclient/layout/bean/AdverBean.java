package com.konka.kktripclient.layout.bean;

import java.util.List;

/**
 * Created by The_one on 2017-5-15.
 * <p>
 * AdverBean
 */

public class AdverBean {
    private int adverFilletDegree;
    private int adverHeight;
    private int adverLayoutId;
    private int adverWidth;
    private List<ContentBean> content;
    private int coordinateX;
    private int coordinateY;

    public AdverBean() {

    }

    public AdverBean(int adverFilletDegree, int adverHeight, int adverLayoutId, int adverWidth, List<ContentBean> content, int coordinateX, int coordinateY) {
        this.adverFilletDegree = adverFilletDegree;
        this.adverHeight = adverHeight;
        this.adverLayoutId = adverLayoutId;
        this.adverWidth = adverWidth;
        this.content = content;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    @Override
    public String toString() {
        return "AdverBean{" +
                "adverFilletDegree=" + adverFilletDegree +
                ", adverHeight=" + adverHeight +
                ", adverLayoutId=" + adverLayoutId +
                ", adverWidth=" + adverWidth +
                ", content=" + content +
                ", coordinateX=" + coordinateX +
                ", coordinateY=" + coordinateY +
                '}';
    }

    public int getAdverFilletDegree() {
        return adverFilletDegree;
    }

    public void setAdverFilletDegree(int adverFilletDegree) {
        this.adverFilletDegree = adverFilletDegree;
    }

    public int getAdverHeight() {
        return adverHeight;
    }

    public void setAdverHeight(int adverHeight) {
        this.adverHeight = adverHeight;
    }

    public int getAdverLayoutId() {
        return adverLayoutId;
    }

    public void setAdverLayoutId(int adverLayoutId) {
        this.adverLayoutId = adverLayoutId;
    }

    public int getAdverWidth() {
        return adverWidth;
    }

    public void setAdverWidth(int adverWidth) {
        this.adverWidth = adverWidth;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
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
}
