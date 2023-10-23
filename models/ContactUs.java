package com.qceccenter.qcec.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactUs {
    @SerializedName("data")
    @Expose
    CUs contacts;

    public CUs getContacts() {
        return contacts;
    }

    public void setContacts(CUs contacts) {
        this.contacts = contacts;
    }
}
