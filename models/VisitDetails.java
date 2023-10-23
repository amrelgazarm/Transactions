package com.qceccenter.qcec.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VisitDetails {
    @SerializedName("data")
    @Expose
    VDetails visitDetails;

    public VDetails getVisitDetails() {
        return visitDetails;
    }

    public void setVisitDetails(VDetails visitDetails) {
        this.visitDetails = visitDetails;
    }
}
