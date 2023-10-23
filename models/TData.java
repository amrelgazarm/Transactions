package com.qceccenter.qcec.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TData implements Parcelable {

    @SerializedName("data")
    @Expose
    List<Transaction> transactionsList = new ArrayList();

    public TData(){}

    protected TData(Parcel in) {
    }

    public static final Creator<TData> CREATOR = new Creator<TData>() {
        @Override
        public TData createFromParcel(Parcel in) {
            return new TData(in);
        }

        @Override
        public TData[] newArray(int size) {
            return new TData[size];
        }
    };

    public List<Transaction> getTransactionsList() {
        return transactionsList;
    }

    public void setTransactionsList(List<Transaction> transactionsList) {
        this.transactionsList = transactionsList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(transactionsList);
    }
}
