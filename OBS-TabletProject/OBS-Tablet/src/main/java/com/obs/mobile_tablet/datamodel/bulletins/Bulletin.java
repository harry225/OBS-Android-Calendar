package com.obs.mobile_tablet.datamodel.bulletins;

import java.util.Collection;

/**
 * Created by stevenguitar on 1/7/14.
 */
public class Bulletin {
    String subject;
    String text;
    String date;

    Collection<BulletinAttachment> attachments;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) { this.date = date; }

    public Collection<BulletinAttachment> getAttachments() { return attachments; }

    public void setAttachments(Collection<BulletinAttachment> attachments) { this.attachments = attachments; }
}
