package com.qceccenter.qcec.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionDetails {
    @SerializedName("data")
    @Expose
    TDetails transactionsDetails;

    public TDetails getTransactionsDetails() {
        return transactionsDetails;
    }

    public void setTransactionsDetails(TDetails transactionsDetails) {
        this.transactionsDetails = transactionsDetails;
    }
}
