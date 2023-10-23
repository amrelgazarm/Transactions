package com.qceccenter.qcec.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Visit {

    @SerializedName("id")
    @Expose
    int visitID;

    @SerializedName("works_description")
    @Expose
    String workDescription;

    @SerializedName("phone")
    @Expose
    String phone;

    @SerializedName("notes")
    @Expose
    String notes;

    @SerializedName("longitude")
    @Expose
    String longitude;

    @SerializedName("latitude")
    @Expose
    String latitude;

    @SerializedName("visit_date")
    @Expose
    String visitDate;

    @SerializedName("count_images_count")
    @Expose
    int attachedImagesCount;

    @SerializedName("count_images")
    @Expose
    List<Image> attachedImagesURLsList = new ArrayList();

    public int getVisitID() {
        return visitID;
    }

    public void setVisitID(int visitID) {
        this.visitID = visitID;
    }

    public String getWorkDescription() {
        return workDescription;
    }

    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public int getAttachedImagesCount() {
        return attachedImagesCount;
    }

    public void setAttachedImagesCount(int attachedImagesCount) {
        this.attachedImagesCount = attachedImagesCount;
    }

    public List<Image> getAttachedImagesURLsList() {
        return attachedImagesURLsList;
    }

    public void setAttachedImagesURLsList(List<Image> attachedImagesURLsList) {
        this.attachedImagesURLsList = attachedImagesURLsList;
    }
}
