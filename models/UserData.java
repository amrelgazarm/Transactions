package com.qceccenter.qcec.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData {
    @SerializedName("access_token")
    @Expose
    String accessToken;

    @SerializedName("user_data")
    @Expose
    UserInfo UserInfo;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public com.qceccenter.qcec.models.UserInfo getUserInfo() {
        return UserInfo;
    }

    public void setUserInfo(com.qceccenter.qcec.models.UserInfo userInfo) {
        UserInfo = userInfo;
    }
}
