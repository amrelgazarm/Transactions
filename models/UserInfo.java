package com.qceccenter.qcec.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfo implements Parcelable {
    @SerializedName("user_id")
    @Expose
    int userID;

    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("phone")
    @Expose
    String phone;

    @SerializedName("national_id")
    @Expose
    String nationalID;

    @SerializedName("user_type_id")
    @Expose
    String userTypeID;

    @SerializedName("user_type_name")
    @Expose
    String userTypeName;

    @SerializedName("is_eng")
    @Expose
    boolean isEng;

    @SerializedName("transactions_count")
    @Expose
    int transactionsCount;

    @SerializedName("visits_count")
    @Expose
    int visitsCount;

    @SerializedName("image")
    @Expose
    String profileImage;

    public UserInfo(){}

    protected UserInfo(Parcel in) {
        userID = in.readInt();
        name = in.readString();
        phone = in.readString();
        nationalID = in.readString();
        userTypeID = in.readString();
        userTypeName = in.readString();
        isEng = in.readByte() != 0;
        transactionsCount = in.readInt();
        visitsCount = in.readInt();
        profileImage = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public boolean isEng() {
        return isEng;
    }

    public void setEng(boolean eng) {
        isEng = eng;
    }

    public int getTransactionsCount() {
        return transactionsCount;
    }

    public void setTransactionsCount(int transactionsCount) {
        this.transactionsCount = transactionsCount;
    }

    public int getVisitsCount() {
        return visitsCount;
    }

    public void setVisitsCount(int visitsCount) {
        this.visitsCount = visitsCount;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }

    public String getUserTypeID() {
        return userTypeID;
    }

    public void setUserTypeID(String userTypeID) {
        this.userTypeID = userTypeID;
    }

    public String getUserTypeName() {
        return userTypeName;
    }

    public void setUserTypeName(String userTypeName) {
        this.userTypeName = userTypeName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userID);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(nationalID);
        dest.writeString(userTypeID);
        dest.writeString(userTypeName);
        dest.writeByte((byte) (isEng ? 1 : 0));
        dest.writeInt(transactionsCount);
        dest.writeInt(visitsCount);
        dest.writeString(profileImage);
    }
}
