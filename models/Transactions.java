package com.qceccenter.qcec.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Transactions {
    @SerializedName("data")
    @Expose
    TransactionsData transactionsData;

    public TransactionsData getTransactionsData() {
        return transactionsData;
    }

    public void setTransactionsData(TransactionsData transactionsData) {
        this.transactionsData = transactionsData;
    }
}
