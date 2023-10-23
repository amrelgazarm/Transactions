package com.qceccenter.qcec.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VisitSubmission {
    @SerializedName("data")
    @Expose
    VisitSub visitSubmission;

    public VisitSub getVisitSubmission() {
        return visitSubmission;
    }

    public void setVisitSubmission(VisitSub visitSubmission) {
        this.visitSubmission = visitSubmission;
    }
}
