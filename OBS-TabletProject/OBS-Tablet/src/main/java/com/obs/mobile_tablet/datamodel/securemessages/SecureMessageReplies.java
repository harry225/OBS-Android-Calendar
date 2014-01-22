package com.obs.mobile_tablet.datamodel.securemessages;

/**
 * Created by hbray on 1/15/14.
 */
public class SecureMessageReplies {
    String companyCode;
    String username;
    String subject;
    String date;
    boolean hasAttachment;


    public String getCompanyCode() { return companyCode; }

    public void setCompanyCode(String companyCode) { this.companyCode = companyCode; }


    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }


    public String getSubject() { return subject; }

    public void setSubject(String subject) { this.subject = subject; }


    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }


    public boolean getHasAttachment() { return hasAttachment; }

    public void setHasAttachment(boolean hasAttachment) { this.hasAttachment = hasAttachment; }

}
