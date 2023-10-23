package com.qceccenter.qcec.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchResult {
    @SerializedName("data")
    @Expose
    SResult data;

    public SResult getData() {
        return data;
    }

    public void setData(SResult data) {
        this.data = data;
    }
}
