package com.wikidsystems.android.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by User: els
 * Date: Nov 3, 2006
 * Time: 1:46:52 PM
 */

public class Token implements Serializable {
    public static final int TOKEN_DISABLED =0;
    public static final int TOKEN_ENABLED =1;


    private long deviceID;
    private long domainID;
    private byte[] offKeyPub;
    private int status;
    private int bads;
    private int offs;
    private Date creation;
    private boolean changed=false;

    private boolean forDeletion=false;

    private long id_devicemap;


    protected Token(long deviceID,long domainID,byte[] offKeyPub,int status, int bads, int offs, Date creation, long id_devicemap){
        this.deviceID=deviceID;
        this.domainID=domainID;
        this.offKeyPub=offKeyPub;
        this.status=status;
        this.bads=bads;
        this.offs=offs;
        this.creation=creation;
        this.id_devicemap=id_devicemap;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        if (changed || status!=this.status) changed=true;
        this.status = status;
    }

    public int getBads() {
        return bads;
    }

    public void setBads(int bads) {
        if (changed || bads!=this.bads) changed=true;
        this.bads = bads;
    }

    public int getOffs() {
        return offs;
    }

    public void setOffs(int offs) {
        if (changed || offs!=this.offs) changed=true;
        this.offs = offs;
    }

    public long getDeviceID() {
        return deviceID;
    }

    public long getDomainID() {
        return domainID;
    }

    public byte[] getOffKeyPub() {
        return offKeyPub;
    }

    public Date getCreation() {
        return creation;
    }

    public long getId_devicemap() {
        return id_devicemap;
    }

    public boolean isChanged() {
        return changed;
    }

    public boolean isForDeletion() {
        return forDeletion;
    }

    public void setForDeletion(boolean forDeletion) {
        this.forDeletion = forDeletion;
    }
}
