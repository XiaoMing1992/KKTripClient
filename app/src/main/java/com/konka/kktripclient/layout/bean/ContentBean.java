package com.konka.kktripclient.layout.bean;

/**
 * Created by The_one on 2017-5-15.
 * <p>
 * ContentBean
 */

public class ContentBean {
    private int adverContentId;
    private int align;
    private double enlargeScale;
    private String firstTitle;
    private int isFocus;
    private int isShowTitle;
    private String openContent;
    private String openContentType;
    private String posterBottom;
    private String posterBottomMd5;
    private String posterMiddle;
    private String posterMiddleMd5;
    private String posterTop;
    private String posterTopMd5;
    private String secondTitle;
    private int state;

    public ContentBean() {

    }

    public ContentBean(int adverContentId, int align, double enlargeScale, String firstTitle, int isFocus, int isShowTitle, String openContent, String openContentType, String posterBottom, String posterBottomMd5, String posterMiddle, String posterMiddleMd5, String posterTop, String posterTopMd5, String secondTitle, int state) {
        this.adverContentId = adverContentId;
        this.align = align;
        this.enlargeScale = enlargeScale;
        this.firstTitle = firstTitle;
        this.isFocus = isFocus;
        this.isShowTitle = isShowTitle;
        this.openContent = openContent;
        this.openContentType = openContentType;
        this.posterBottom = posterBottom;
        this.posterBottomMd5 = posterBottomMd5;
        this.posterMiddle = posterMiddle;
        this.posterMiddleMd5 = posterMiddleMd5;
        this.posterTop = posterTop;
        this.posterTopMd5 = posterTopMd5;
        this.secondTitle = secondTitle;
        this.state = state;
    }

    @Override
    public String toString() {
        return "ContentBean{" +
                "adverContentId=" + adverContentId +
                ", align=" + align +
                ", enlargeScale=" + enlargeScale +
                ", firstTitle='" + firstTitle + '\'' +
                ", isFocus=" + isFocus +
                ", isShowTitle=" + isShowTitle +
                ", openContent='" + openContent + '\'' +
                ", openContentType='" + openContentType + '\'' +
                ", posterBottom='" + posterBottom + '\'' +
                ", posterBottomMd5='" + posterBottomMd5 + '\'' +
                ", posterMiddle='" + posterMiddle + '\'' +
                ", posterMiddleMd5='" + posterMiddleMd5 + '\'' +
                ", posterTop='" + posterTop + '\'' +
                ", posterTopMd5='" + posterTopMd5 + '\'' +
                ", secondTitle='" + secondTitle + '\'' +
                ", state=" + state +
                '}';
    }

    public int getAdverContentId() {
        return adverContentId;
    }

    public void setAdverContentId(int adverContentId) {
        this.adverContentId = adverContentId;
    }

    public int getAlign() {
        return align;
    }

    public void setAlign(int align) {
        this.align = align;
    }

    public double getEnlargeScale() {
        return enlargeScale;
    }

    public void setEnlargeScale(double enlargeScale) {
        this.enlargeScale = enlargeScale;
    }

    public String getFirstTitle() {
        return firstTitle;
    }

    public void setFirstTitle(String firstTitle) {
        this.firstTitle = firstTitle;
    }

    public int getIsFocus() {
        return isFocus;
    }

    public void setIsFocus(int isFocus) {
        this.isFocus = isFocus;
    }

    public int getIsShowTitle() {
        return isShowTitle;
    }

    public void setIsShowTitle(int isShowTitle) {
        this.isShowTitle = isShowTitle;
    }

    public String getOpenContent() {
        return openContent;
    }

    public void setOpenContent(String openContent) {
        this.openContent = openContent;
    }

    public String getOpenContentType() {
        return openContentType;
    }

    public void setOpenContentType(String openContentType) {
        this.openContentType = openContentType;
    }

    public String getPosterBottom() {
        return posterBottom;
    }

    public void setPosterBottom(String posterBottom) {
        this.posterBottom = posterBottom;
    }

    public String getPosterBottomMd5() {
        return posterBottomMd5;
    }

    public void setPosterBottomMd5(String posterBottomMd5) {
        this.posterBottomMd5 = posterBottomMd5;
    }

    public String getPosterMiddle() {
        return posterMiddle;
    }

    public void setPosterMiddle(String posterMiddle) {
        this.posterMiddle = posterMiddle;
    }

    public String getPosterMiddleMd5() {
        return posterMiddleMd5;
    }

    public void setPosterMiddleMd5(String posterMiddleMd5) {
        this.posterMiddleMd5 = posterMiddleMd5;
    }

    public String getPosterTop() {
        return posterTop;
    }

    public void setPosterTop(String posterTop) {
        this.posterTop = posterTop;
    }

    public String getPosterTopMd5() {
        return posterTopMd5;
    }

    public void setPosterTopMd5(String posterTopMd5) {
        this.posterTopMd5 = posterTopMd5;
    }

    public String getSecondTitle() {
        return secondTitle;
    }

    public void setSecondTitle(String secondTitle) {
        this.secondTitle = secondTitle;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
