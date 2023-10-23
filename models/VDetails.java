package com.qceccenter.qcec.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VDetails {
    @SerializedName("visit_details")
    @Expose
    Visit visitDetails;

    public Visit getVisitDetails() {
        return visitDetails;
    }

    public void setVisitDetails(Visit visitDetails) {
        this.visitDetails = visitDetails;
    }
}
