package com.qceccenter.qcec.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VisitSub {
    @SerializedName("status")
    @Expose
    Boolean isSubmittedSuccessfully;

    @SerializedName("visit_id")
    @Expose
    int visitID;

    public Boolean getSubmittedSuccessfully() {
        return isSubmittedSuccessfully;
    }

    public void setSubmittedSuccessfully(Boolean submittedSuccessfully) {
        isSubmittedSuccessfully = submittedSuccessfully;
    }

    public int getVisitID() {
        return visitID;
    }

    public void setVisitID(int visitID) {
        this.visitID = visitID;
    }
}
