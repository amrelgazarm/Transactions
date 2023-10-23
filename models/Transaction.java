package com.qceccenter.qcec.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Transaction {
    @SerializedName("id")
    @Expose
    int transactionID;

    @SerializedName("project_name")
    @Expose
    String projectName;

    @SerializedName("area_number")
    @Expose
    String areaNumber;

    @SerializedName("owner_name")
    @Expose
    String ownerName;

    @SerializedName("owner_phone")
    @Expose
    String ownerPhone;

    @SerializedName("file_number")
    @Expose
    String fileNumber;

    @SerializedName("engineer_name")
    @Expose
    String engineerName;

    @SerializedName("contractor_id")
    @Expose
    String contractorID;

    @SerializedName("contractor_name")
    @Expose
    String contractorName;

    @SerializedName("contractor_phone")
    @Expose
    String contractorPhone;

    @SerializedName("location")
    @Expose
    String location;

    @SerializedName("transaction_statement")
    @Expose
    String transactionStatement;

    @SerializedName("notes")
    @Expose
    String notes;

    @SerializedName("created_date")
    @Expose
    String creationDate;

    @SerializedName("visits_count")
    @Expose
    int visitsCount;

    @SerializedName("visits_items")
    @Expose
    List<Visit> visitsList = new ArrayList();

    @SerializedName("follow_up_files")
    @Expose
    List<PdfFile> pdfFilesList = new ArrayList();

    public List<PdfFile> getPdfFilesList() {
        return pdfFilesList;
    }

    public void setPdfFilesList(List<PdfFile> pdfFilesList) {
        this.pdfFilesList = pdfFilesList;
    }

    public String getEngineerName() {
        return engineerName;
    }

    public void setEngineerName(String engineerName) {
        this.engineerName = engineerName;
    }

    public List<Visit> getVisitsList() {
        return visitsList;
    }

    public void setVisitsList(List<Visit> visitsList) {
        this.visitsList = visitsList;
    }

    public int getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getAreaNumber() {
        return areaNumber;
    }

    public void setAreaNumber(String areaNumber) {
        this.areaNumber = areaNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getContractorID() {
        return contractorID;
    }

    public void setContractorID(String contractorID) {
        this.contractorID = contractorID;
    }

    public String getContractorName() {
        return contractorName;
    }

    public void setContractorName(String contractorName) {
        this.contractorName = contractorName;
    }

    public String getContractorPhone() {
        return contractorPhone;
    }

    public void setContractorPhone(String contractorPhone) {
        this.contractorPhone = contractorPhone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTransactionStatement() {
        return transactionStatement;
    }

    public void setTransactionStatement(String transactionStatement) {
        this.transactionStatement = transactionStatement;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public int getVisitsCount() {
        return visitsCount;
    }

    public void setVisitsCount(int visitsCount) {
        this.visitsCount = visitsCount;
    }
}
