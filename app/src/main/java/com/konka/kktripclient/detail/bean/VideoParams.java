package com.konka.kktripclient.detail.bean;


public class VideoParams {
    private String mTitle;
    private boolean isVideoActivity = false;
    private String mUrl;
    private String mRouteName = "-1";

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public boolean isVideoActivity() {
        return isVideoActivity;
    }

    public void setVideoActivity(boolean videoActivity) {
        isVideoActivity = videoActivity;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getRouteName() {
        return mRouteName;
    }

    public void setRouteName(String routeName) {
        this.mRouteName = routeName;
    }
}
