package com.qceccenter.qcec.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompanyInfo {

    @SerializedName("data")
    @Expose
    CInfo data;

    public CInfo getData() {
        return data;
    }

    public void setData(CInfo data) {
        this.data = data;
    }
}
