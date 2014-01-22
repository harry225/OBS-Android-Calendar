package com.obs.mobile_tablet.datamodel.securemessages;

/**
 * Created by stevenguitar on 1/7/14.
 */
public class SecureMessageAttachment {
    String uid;
    String filename;
    String url;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
