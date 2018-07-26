package com.konka.kktripclient.detail.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zhou Weilin on 2017-6-13.
 */

public class RouteInfo implements Parcelable {

    private int mGoodId;
    private String mGoodName;
    private double mGoodPrice;
    private String mDeparturePlace;
    private String mDepartureTime;
    private String mThumbNail;

    public RouteInfo(){}

    public RouteInfo(Parcel in){
        mGoodId=in.readInt();
        mGoodName=in.readString();
        mGoodPrice=in.readDouble();
        mDeparturePlace=in.readString();
        mDepartureTime=in.readString();
        mThumbNail=in.readString();

    }

    public int getGoodId() {
        return mGoodId;
    }

    public void setGoodId(int goodId) {
        mGoodId = goodId;
    }

    public String getGoodName() {
        return mGoodName;
    }

    public void setGoodName(String goodName) {
        mGoodName = goodName;
    }

    public double getGoodPrice() {
        return mGoodPrice;
    }

    public void setGoodPrice(double goodPrice) {
        mGoodPrice = goodPrice;
    }

    public String getDeparturePlace() {
        return mDeparturePlace;
    }

    public void setDeparturePlace(String departurePlace) {
        mDeparturePlace = departurePlace;
    }

    public String getDepartureTime() {
        return mDepartureTime;
    }

    public void setDepartureTime(String departureTime) {
        mDepartureTime = departureTime;
    }

    public String getThumbNail() {
        return mThumbNail;
    }

    public void setThumbNail(String thumbNail) {
        mThumbNail = thumbNail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mGoodId);
        dest.writeString(mGoodName);
        dest.writeDouble(mGoodPrice);
        dest.writeString(mDeparturePlace);
        dest.writeString(mDepartureTime);
        dest.writeString(mThumbNail);
    }

    public static final Parcelable.Creator<RouteInfo> CREATOR = new Creator<RouteInfo>(){
        @Override
        public RouteInfo[] newArray(int size)
        {
            return new RouteInfo[size];
        }

        @Override
        public RouteInfo createFromParcel(Parcel in)
        {
            return new RouteInfo(in);
        }
    };

    @Override
    public String toString() {
        return "RouteInfo{" +
                "mGoodId=" + mGoodId +
                ", mGoodName='" + mGoodName + '\'' +
                ", mGoodPrice=" + mGoodPrice +
                ", mDeparturePlace='" + mDeparturePlace + '\'' +
                ", mDepartureTime='" + mDepartureTime + '\'' +
                ", mThumbNail='" + mThumbNail + '\'' +
                '}';
    }
}
