package com.wikidsystems.android.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by User: els
 * Date: Nov 4, 2006
 * Time: 10:03:49 AM
 */
public class WiKIDEvent implements Serializable {

    public static final int CALLBACK_DEREGISTERED = -1;
    public static final int CALLBACK_REGISTERED = 0;
    public static final int USER_DISABLED = 1;
    public static final int DEVICE_DISABLED = 2;
    public static final int USER_DELETED = 3;
    public static final int DEVICE_DELETED = 4;
    public static final int USER_ENABLED = 5;
    public static final int DEVICE_ENABLED = 6;
    public static final int USER_REGISTERED = 7;
    public static final int USER_AUTHENTICATED = 8;

    public static final int DOMAIN_LIST = 100;


    private int eventType;
    private User user;
    private Date eventTime;
    private long deviceId;


    public WiKIDEvent(int eventType, User user, Date eventTime){
        this.eventType=eventType;
        this.user=user;
        this.eventTime=eventTime;
    }

    public int getEventType() {
        return eventType;
    }

    protected void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public User getUser() {
        return user;
    }

    protected void setUser(User user) {
        this.user = user;
    }

    public Date getEventTime() {
        return eventTime;
    }

    protected void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("Event: "+eventType);
        sb.append(" / ").append(user==null ? "No User" : user.getUserID());
        sb.append(" / ").append(eventTime==null ? "No Event Time" : eventTime.toString());
        return sb.toString();
    }
}
