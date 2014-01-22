package com.obs.mobile_tablet.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 1/12/14.
 */
public class BannersReceivedEvent {
    private ArrayList<String> _errorBanners;
    private ArrayList<String> _infoBanners;
    private ArrayList<String> _successBanners;

    public BannersReceivedEvent(ArrayList<String> errorBanners, ArrayList<String> infoBanners, ArrayList<String> successBanners){

        _errorBanners = errorBanners != null ? errorBanners : new ArrayList<String>();
        _infoBanners = infoBanners != null ? infoBanners : new ArrayList<String>();
        _successBanners = successBanners != null ? successBanners : new ArrayList<String>();
    }

    public ArrayList<String> getSuccessBanners() {
        return _successBanners;
    }

    public void setSuccessBanners(ArrayList<String> successBanners) {
        _successBanners = successBanners;
    }

    public ArrayList<String> getInfoBanners() {
        return _infoBanners;
    }

    public void setInfoBanners(ArrayList<String> infoBanners) {
        _infoBanners = infoBanners;
    }

    public ArrayList<String> getErrorBanners() {
        return _errorBanners;
    }

    public void setErrorBanners(ArrayList<String> errorBanners) {
        _errorBanners = errorBanners;
    }
}
