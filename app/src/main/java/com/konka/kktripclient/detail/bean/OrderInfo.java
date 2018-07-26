package com.konka.kktripclient.detail.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zhou Weilin on 2017-6-13.
 */

public class OrderInfo implements Parcelable {
    private int mGoodId;
    private String mGoodName;
    private String mGoodPrice;
    private int mGoodNum;
    private String mUserName;
    private String mUserPhone;
    private String mDeparturePlace;
    private String mDepartureTime;
    private String mThumbNail;

    public OrderInfo(){}

    public OrderInfo(Parcel in){
        mGoodId=in.readInt();
        mGoodName=in.readString();
        mGoodPrice=in.readString();
        mGoodNum=in.readInt();
        mUserName=in.readString();
        mUserPhone=in.readString();
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

    public String getGoodPrice() {
        return mGoodPrice;
    }

    public void setGoodPrice(String goodPrice) {
        mGoodPrice = goodPrice;
    }

    public int getGoodNum() {
        return mGoodNum;
    }

    public void setGoodNum(int goodNum) {
        mGoodNum = goodNum;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getUserPhone() {
        return mUserPhone;
    }

    public void setUserPhone(String userPhone) {
        mUserPhone = userPhone;
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
        dest.writeString(mGoodPrice);
        dest.writeInt(mGoodNum);
        dest.writeString(mUserName);
        dest.writeString(mUserPhone);
        dest.writeString(mDeparturePlace);
        dest.writeString(mDepartureTime);
        dest.writeString(mThumbNail);
    }

    public static final Creator<OrderInfo> CREATOR = new Creator<OrderInfo>(){
        @Override
        public OrderInfo[] newArray(int size)
        {
            return new OrderInfo[size];
        }

        @Override
        public OrderInfo createFromParcel(Parcel in)
        {
            return new OrderInfo(in);
        }
    };

    @Override
    public String toString() {
        return "OrderInfo{" +
                "mGoodId=" + mGoodId +
                ", mGoodName='" + mGoodName + '\'' +
                ", mGoodPrice='" + mGoodPrice + '\'' +
                ", mGoodNum=" + mGoodNum +
                ", mUserName='" + mUserName + '\'' +
                ", mUserPhone='" + mUserPhone + '\'' +
                ", mDeparturePlace='" + mDeparturePlace + '\'' +
                ", mDepartureTime='" + mDepartureTime + '\'' +
                ", mThumbNail='" + mThumbNail + '\'' +
                '}';
    }
}
