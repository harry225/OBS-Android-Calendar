package com.obs.mobile_tablet.wikid_utils;

import android.content.SharedPreferences;

import com.obs.mobile_tablet.OBSApplication;
import com.obs.mobile_tablet.utils.ForApplication;

import javax.inject.Inject;

/**
 * Created by IntelliJ IDEA.
 * User: stguitar
 * Date: 4/7/12
 * Time: 11:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class ObsDetails {
    @Inject
    @ForApplication
    OBSApplication application;

    private static String bankId;
    private static String companyCode;
    private static String username;
    private static String tokenName;
    private static String registrationCode;
    private static long deviceId;
    private String cookie;
    private String apiversion;
    private String nonce;
    private String accessToken;
    private int offset;


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getApiversion() {
        return apiversion;
    }

    public void setApiversion(String apiversion) {
        this.apiversion = apiversion;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    private static ObsDetails ref;

    public String getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        ObsDetails.registrationCode = registrationCode;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        ObsDetails.tokenName = tokenName;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        ObsDetails.deviceId = deviceId;
        SharedPreferences preferences = application.getSharedPreferences("OBS_DETAILS",0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("deviceId",deviceId);
        editor.commit();
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;

        SharedPreferences preferences = application.getSharedPreferences("OBS_DETAILS",0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("company",companyCode);
        editor.commit();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;

        SharedPreferences preferences = application.getSharedPreferences("OBS_DETAILS",0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username",username);
        editor.commit();
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    protected ObsDetails() {}

    public static synchronized ObsDetails getInstance(){
        if(ref == null){
            ref = new ObsDetails();
        }
        return ref;
    }
    
    public void load() {
        SharedPreferences preferences = application.getSharedPreferences("OBS_DETAILS",0);
        ref.username = preferences.getString("username","");
        ref.companyCode = preferences.getString("company","");
        ref.deviceId = preferences.getLong("deviceId",0);

    }
}
