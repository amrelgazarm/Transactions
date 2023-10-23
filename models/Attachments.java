package com.qceccenter.qcec.models;

import java.io.Serializable;
import java.util.List;

import okhttp3.MultipartBody;

public class Attachments implements Serializable {
    List<MultipartBody.Part> attachmentList;

    public Attachments(List<MultipartBody.Part> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public List<MultipartBody.Part> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<MultipartBody.Part> attachmentList) {
        this.attachmentList = attachmentList;
    }

}
