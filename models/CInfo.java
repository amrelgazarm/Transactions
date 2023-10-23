package com.qceccenter.qcec.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CInfo {
    @SerializedName("introduction")
    @Expose
    AboutCompany introduction;

    @SerializedName("services")
    @Expose
    AboutCompany services;

    public AboutCompany getIntroduction() {
        return introduction;
    }

    public void setIntroduction(AboutCompany introduction) {
        this.introduction = introduction;
    }

    public AboutCompany getServices() {
        return services;
    }

    public void setServices(AboutCompany services) {
        this.services = services;
    }
}
