package com.qceccenter.qcec.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionsData {
    @SerializedName("user_data")
    @Expose
    UserInfo userInfo;

    @SerializedName("transactions")
    @Expose
    TData transactions;

    @SerializedName("visits_count")
    @Expose
    int visitsCount;


    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public TData getTransactions() {
        return transactions;
    }

    public void setTransactions(TData transactions) {
        this.transactions = transactions;
    }

    public int getVisitsCount() {
        return visitsCount;
    }

    public void setVisitsCount(int visitsCount) {
        this.visitsCount = visitsCount;
    }
}
