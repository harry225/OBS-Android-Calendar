package com.obs.mobile_tablet.datamodel;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;


public class ResponseWrapper<T> {
    public String reason;
    public List<String> nonce;
    public String apiversion;

    @JsonIgnore
    public boolean isError;

    public T data;

    public ArrayList<String> errorBanners;
    public ArrayList<String> infoBanners;
    public ArrayList<String> successBanners;

    private Boolean timeout;


    public ResponseWrapper(){
        errorBanners = new ArrayList<String>();
        infoBanners = new ArrayList<String>();
        successBanners = new ArrayList<String>();
    }

    public Boolean getTimeout() {
        return timeout;
    }


    public ArrayList<String> getErrorBanners() {
        return errorBanners;
    }

    public ArrayList<String> getInfoBanners() {
        return infoBanners;
    }

    public ArrayList<String> getSuccessBanners() {
        return successBanners;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean isError) {
        this.isError = isError;
    }
}