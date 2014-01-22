package com.obs.mobile_tablet.datamodel.securemessages;

import com.google.common.collect.Iterables;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by stevenguitar on 1/7/14.
 */
public class SecureMessage implements Serializable {
    String recipient;
    String subject;
    String message;
    String date;
    String priority;
    String detailsUrl;
    boolean canReplyTo;
    boolean hasNewReply;
    String originatingCompanyCode;
    String originatingUsername;
    boolean hasAttachment;

    private Collection<SecureMessageReplies> replies;
    Collection<SecureMessageAttachment> attachments;

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }

    public boolean isCanReplyTo() {
        return canReplyTo;
    }

    public void setCanReplyTo(boolean canReplyTo) {
        this.canReplyTo = canReplyTo;
    }

    public boolean isHasNewReply() {
        return hasNewReply;
    }

    public void setHasNewReply(boolean hasNewReply) {
        this.hasNewReply = hasNewReply;
    }

    public String getOriginatingCompanyCode() {
        if(this.originatingCompanyCode != null )
            return originatingCompanyCode;     // return local instance
        if(this.replies.size() > 0 )
            return Iterables.get(this.replies, 0).getCompanyCode();    // otherwise, from first reply
        return null;        // otherwise, don't have one...
    }

    public void setOriginatingCompanyCode(String originatingCompanyCode) {
        this.originatingCompanyCode = originatingCompanyCode;
    }

    public String getOriginatingUsername() {
        if(this.originatingUsername != null )
            return originatingUsername;     // return local instance
        if(this.replies.size() > 0 )
            return Iterables.get(this.replies, 0).getUsername();    // otherwise, from first reply
        return null;        // otherwise, don't have one...
    }

    public void setOriginatingUsername(String originatingUsername) {
        this.originatingUsername = originatingUsername;
    }

    public boolean isHasAttachment() {
        return hasAttachment;
    }

    public void setHasAttachment(boolean hasAttachment) {
        this.hasAttachment = hasAttachment;
    }

    public Collection<SecureMessageReplies> getReplies() { return replies; }

    public void setReplies(Collection<SecureMessageReplies> replies) { this.replies = replies; }

    public Collection<SecureMessageAttachment> getAttachments() { return attachments; }

    public void setAttachments(Collection<SecureMessageAttachment> attachments) { this.attachments = attachments; }
}
