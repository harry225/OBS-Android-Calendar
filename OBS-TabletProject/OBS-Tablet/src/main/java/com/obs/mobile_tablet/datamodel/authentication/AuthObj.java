package com.obs.mobile_tablet.datamodel.authentication;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created with IntelliJ IDEA.
 * User: stevenguitar
 * Date: 4/30/13
 * Time: 12:36 AM
 * To change this template use File | Settings | File Templates.
 */


public class AuthObj implements Serializable {
    String company;
    String username;
    String authProductKey;
    String pin;
    String os;
    String osVersion;
    String appVersion;
    String deviceType;
    String deviceUuid;

    public AuthObj() {

    }

    public AuthObj(String company, String username, String authProductKey, String os, String osVersion, String appVersion, String deviceType, String deviceUuid) {
        this.company = company;
        this.username = username;
        this.authProductKey = authProductKey;
        this.os = os;
        this.osVersion = osVersion;
        this.appVersion = appVersion;
        this.deviceType = deviceType;
        this.deviceUuid = deviceUuid;

    }
    @JsonIgnore
    private boolean isValidForSetup() {
        return ((company != null) &&
                (username != null) &&
                (authProductKey != null) &&
                (os != null) &&
                (osVersion != null) &&
                (appVersion != null) &&
                (deviceType != null) &&
                (deviceUuid != null));
    }

    @JsonIgnore
    private boolean isValid() {
        return ((company != null) && (username != null) && (pin != null) && (deviceUuid != null));
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthProductKey() {
        return authProductKey;
    }

    public void setAuthProductKey(String authProductKey) {
        this.authProductKey = authProductKey;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceUuid() {
        return deviceUuid;
    }

    public void setDeviceUuid(String deviceUuid) {
        this.deviceUuid = deviceUuid;
    }
}
