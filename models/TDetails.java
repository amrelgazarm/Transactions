package com.qceccenter.qcec.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TDetails {
    @SerializedName("transaction_details")
    @Expose
    Transaction transactionDetails;

    public Transaction getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(Transaction transactionDetails) {
        this.transactionDetails = transactionDetails;
    }
}
