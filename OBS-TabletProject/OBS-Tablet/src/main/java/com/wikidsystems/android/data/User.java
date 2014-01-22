package com.wikidsystems.android.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by User: els
 * Date: Nov 2, 2006
 * Time: 9:07:48 AM
 */

/* This class represents a user in the WiKID system.
*/
public class User implements Serializable {

    public static final int USER_DISABLED =0;
    public static final int USER_ENABLED =1;

    private long id_usermap;
    private String userID;
    private String domainCode;
    private int bads;
    private Date creation;
    private int status;
    private List<Token> tokens;

    protected User(long id_usermap, String userID, String domainCode, int bads, java.util.Date creation, int status, List<Token> tokens){
        this.id_usermap=id_usermap;
        this.userID=userID.trim();
        this.domainCode=domainCode.trim();
        this.bads=bads;
        this.creation=creation;
        this.status=status;
        this.tokens=tokens;
    }

    public long getId_usermap() {
        return id_usermap;
    }

    public String getUserID() {
        return userID;
    }

    public int getBads() {
        return bads;
    }

    public void setBads(int bads) {
        this.bads = bads;
    }

    public Date getCreation() {
        return creation;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDomainCode() {
        return domainCode;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final User user = (User) o;

        return id_usermap == user.id_usermap && userID.equals(user.userID);

    }

    public int hashCode() {
        int result;
        result = (int) (id_usermap ^ (id_usermap >>> 32));
        result = 29 * result + userID.hashCode();
        return result;
    }

    public String toString() {
        return ""+id_usermap+"/"+userID+"/"+creation;
    }

    public List<Token> getTokens() {
        return tokens;
    }
}
