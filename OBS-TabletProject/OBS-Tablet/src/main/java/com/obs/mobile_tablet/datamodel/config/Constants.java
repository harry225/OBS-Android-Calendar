
package com.obs.mobile_tablet.datamodel.config;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
//@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "primaryInterfaceColor",
    "secondaryInterfaceColor",
    "tertiaryInterfaceColor",
    "accentInterfaceColor",
    "lightGrayInterfaceColor",
    "darkGrayInterfaceColor",
    "progressBarInterfaceColor",
    "bankLogo",
    "bankBanner",
    "loginBackgroundImage",
    "loginHasBackgroundTint",
    "loginBackgroundTint",
    "hostIp",
    "apiVersion",
    "serverCode",
    "bank",
    "instance",
    "questionOneKey",
    "questionTwoKey",
    "contactUsUrl",
    "timeout",
    "loginViewCoord",
    "tokenPassphrase"
})
public class Constants {
    @JsonProperty("primaryInterfaceColor")
    private String primaryInterfaceColor;
    @JsonProperty("secondaryInterfaceColor")
    private String secondaryInterfaceColor;
    @JsonProperty("tertiaryInterfaceColor")
    private String tertiaryInterfaceColor;
    @JsonProperty("accentInterfaceColor")
    private String accentInterfaceColor;
    @JsonProperty("lightGrayInterfaceColor")
    private String lightGrayInterfaceColor;
    @JsonProperty("darkGrayInterfaceColor")
    private String darkGrayInterfaceColor;
    @JsonProperty("progressBarInterfaceColor")
    private String progressBarInterfaceColor;
    @JsonProperty("bankLogo")
    private String bankLogo;
    @JsonProperty("bankBanner")
    private String bankBanner;
    @JsonProperty("loginBackgroundImage")
    private String loginBackgroundImage;
    @JsonProperty("loginHasBackgroundTint")
    private String loginHasBackgroundTint;
    @JsonProperty("loginBackgroundTint")
    private String loginBackgroundTint;
    @JsonProperty("hostIp")
    private String hostIp;
    @JsonProperty("apiVersion")
    private String apiVersion;
    @JsonProperty("serverCode")
    private String serverCode;
    @JsonProperty("bank")
    private String bank;
    @JsonProperty("instance")
    private String instance;
    @JsonProperty("questionOneKey")
    private String questionOneKey;
    @JsonProperty("questionTwoKey")
    private String questionTwoKey;
    @JsonProperty("contactUsUrl")
    private String contactUsUrl;
    @JsonProperty("timeout")
    private Integer timeout;
    @JsonProperty("loginViewCoord")
    private String loginViewCoord;
    @JsonProperty("tokenPassphrase")
    private String tokenPassphrase;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("primaryInterfaceColor")
    public String getPrimaryInterfaceColor() {
        return primaryInterfaceColor;
    }

    @JsonProperty("primaryInterfaceColor")
    public void setPrimaryInterfaceColor(String primaryInterfaceColor) {
        this.primaryInterfaceColor = primaryInterfaceColor;
    }

    @JsonProperty("secondaryInterfaceColor")
    public String getSecondaryInterfaceColor() {
        return secondaryInterfaceColor;
    }

    @JsonProperty("secondaryInterfaceColor")
    public void setSecondaryInterfaceColor(String secondaryInterfaceColor) {
        this.secondaryInterfaceColor = secondaryInterfaceColor;
    }

    @JsonProperty("tertiaryInterfaceColor")
    public String getTertiaryInterfaceColor() {
        return tertiaryInterfaceColor;
    }

    @JsonProperty("tertiaryInterfaceColor")
    public void setTertiaryInterfaceColor(String tertiaryInterfaceColor) {
        this.tertiaryInterfaceColor = tertiaryInterfaceColor;
    }

    @JsonProperty("accentInterfaceColor")
    public String getAccentInterfaceColor() {
        return accentInterfaceColor;
    }

    @JsonProperty("accentInterfaceColor")
    public void setAccentInterfaceColor(String accentInterfaceColor) {
        this.accentInterfaceColor = accentInterfaceColor;
    }

    @JsonProperty("lightGrayInterfaceColor")
    public String getLightGrayInterfaceColor() {
        return lightGrayInterfaceColor;
    }

    @JsonProperty("lightGrayInterfaceColor")
    public void setLightGrayInterfaceColor(String lightGrayInterfaceColor) {
        this.lightGrayInterfaceColor = lightGrayInterfaceColor;
    }

    @JsonProperty("darkGrayInterfaceColor")
    public String getDarkGrayInterfaceColor() {
        return darkGrayInterfaceColor;
    }

    @JsonProperty("darkGrayInterfaceColor")
    public void setDarkGrayInterfaceColor(String darkGrayInterfaceColor) {
        this.darkGrayInterfaceColor = darkGrayInterfaceColor;
    }

    @JsonProperty("progressBarInterfaceColor")
    public String getProgressBarInterfaceColor() {
        return progressBarInterfaceColor;
    }

    @JsonProperty("progressBarInterfaceColor")
    public void setProgressBarInterfaceColor(String progressBarInterfaceColor) {
        this.progressBarInterfaceColor = progressBarInterfaceColor;
    }

    @JsonProperty("bankLogo")
    public String getBankLogo() {
        return bankLogo;
    }

    @JsonProperty("bankLogo")
    public void setBankLogo(String bankLogo) {
        this.bankLogo = bankLogo;
    }

    @JsonProperty("bankBanner")
    public String getBankBanner() {
        return bankBanner;
    }

    @JsonProperty("bankBanner")
    public void setBankBanner(String bankBanner) {
        this.bankBanner = bankBanner;
    }

    @JsonProperty("loginBackgroundImage")
    public String getLoginBackgroundImage() {
        return loginBackgroundImage;
    }

    @JsonProperty("loginBackgroundImage")
    public void setLoginBackgroundImage(String loginBackgroundImage) {
        this.loginBackgroundImage = loginBackgroundImage;
    }

    @JsonProperty("loginHasBackgroundTint")
    public String getLoginHasBackgroundTint() {
        return loginHasBackgroundTint;
    }

    @JsonProperty("loginHasBackgroundTint")
    public void setLoginHasBackgroundTint(String loginHasBackgroundTint) {
        this.loginHasBackgroundTint = loginHasBackgroundTint;
    }

    @JsonProperty("loginBackgroundTint")
    public String getLoginBackgroundTint() {
        return loginBackgroundTint;
    }

    @JsonProperty("loginBackgroundTint")
    public void setLoginBackgroundTint(String loginBackgroundTint) {
        this.loginBackgroundTint = loginBackgroundTint;
    }

    @JsonProperty("hostIp")
    public String getHostIp() {
        return hostIp;
    }

    @JsonProperty("hostIp")
    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    @JsonProperty("apiVersion")
    public String getApiVersion() {
        return apiVersion;
    }

    @JsonProperty("apiVersion")
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    @JsonProperty("serverCode")
    public String getServerCode() {
        return serverCode;
    }

    @JsonProperty("serverCode")
    public void setServerCode(String serverCode) {
        this.serverCode = serverCode;
    }

    @JsonProperty("bank")
    public String getBank() {
        return bank;
    }

    @JsonProperty("bank")
    public void setBank(String bank) {
        this.bank = bank;
    }

    @JsonProperty("instance")
    public String getInstance() {
        return instance;
    }

    @JsonProperty("instance")
    public void setInstance(String instance) {
        this.instance = instance;
    }

    @JsonProperty("questionOneKey")
    public String getQuestionOneKey() {
        return questionOneKey;
    }

    @JsonProperty("questionOneKey")
    public void setQuestionOneKey(String questionOneKey) {
        this.questionOneKey = questionOneKey;
    }

    @JsonProperty("questionTwoKey")
    public String getQuestionTwoKey() {
        return questionTwoKey;
    }

    @JsonProperty("questionTwoKey")
    public void setQuestionTwoKey(String questionTwoKey) {
        this.questionTwoKey = questionTwoKey;
    }

    @JsonProperty("contactUsUrl")
    public String getContactUsUrl() {
        return contactUsUrl;
    }

    @JsonProperty("contactUsUrl")
    public void setContactUsUrl(String contactUsUrl) {
        this.contactUsUrl = contactUsUrl;
    }

    @JsonProperty("timeout")
    public Integer getTimeout() {
        return timeout;
    }

    @JsonProperty("timeout")
    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    @JsonProperty("loginViewCoord")
    public String getLoginViewCoord() {
        return loginViewCoord;
    }

    @JsonProperty("loginViewCoord")
    public void setLoginViewCoord(String loginViewCoord) {
        this.loginViewCoord = loginViewCoord;
    }

    @JsonProperty("tokenPassphrase")
    public String getTokenPassphrase() {
        return tokenPassphrase;
    }

    @JsonProperty("tokenPassphrase")
    public void setTokenPassphrase(String tokenPassphrase) {
        this.tokenPassphrase = tokenPassphrase;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
