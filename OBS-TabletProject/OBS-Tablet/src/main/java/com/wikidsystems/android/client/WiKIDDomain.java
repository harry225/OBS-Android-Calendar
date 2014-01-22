package com.wikidsystems.android.client;


import com.obs.mobile_tablet.wikid_utils.ObsDetails;
import com.wikidsystems.android.crypto.wCryptoException;
import com.wikidsystems.android.crypto.wEncKeys;
import com.wikidsystems.android.crypto.wEncKeysFactory;
import com.wikidsystems.android.crypto.wJceEncKeysFactory;
import com.wikidsystems.android.util.B64;

import java.security.PublicKey;

/**
 * Created by Eric Shoemaker
 * Date: Dec 25, 2005
 * Time: 6:34:27 PM
 */
public class WiKIDDomain implements Comparable{

    private String serverCode;
    private PublicKey pubKey;
    private String name;
    private int minPIN;
    private int PINLifetime;
    private long deviceID;
    private byte[] offlineKey;
    private String registeredURL;

    public String getServerCode() {
        return serverCode;
    }

    public void setServerCode(String serverCode) {
        this.serverCode = serverCode;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("serverCode:").append(serverCode)
                .append("\nname:").append(name)
                .append("\nminPIN:").append(minPIN)
                .append("\nPINLifetime:").append(PINLifetime)
                .append("\ndeviceID:").append(deviceID)
                .append("\nregisteredURL:").append(registeredURL);
        if (pubKey != null) sb.append("\npubKey:").append(B64.encodeBytes(pubKey.getEncoded()));
        if (offlineKey != null) sb.append("\nofflineKey:").append(B64.encodeBytes(offlineKey));
        return sb.toString();
    }

    public wEncKeys getEncKeys(int keySize) {
//        Utility method to create a keypair with a null private key
//  TODO:      The keysize should be dertemined by attributes of the key in the config

        wEncKeysFactory keyFact = new wJceEncKeysFactory("RSA", "RSA/ECB/PKCS1PADDING", keySize);
        try {
            return keyFact.create(getPubKey().getEncoded(), null);
        } catch (wCryptoException e) {
            throw new RuntimeException(e);
        }
    }

    public PublicKey getPubKey() {
        return pubKey;
    }

    public void setPubKey(PublicKey pubKey) {
        this.pubKey = pubKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinPIN() {
        return minPIN;
    }

    public void setMinPIN(int minPIN) {
        this.minPIN = minPIN;
    }

    public int getPINLifetime() {
        return PINLifetime;
    }

    public void setPINLifetime(int PINLifetime) {
        this.PINLifetime = PINLifetime;
    }

    public long getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(long deviceID) {
        this.deviceID = deviceID;
        ObsDetails.getInstance().setDeviceId(deviceID);
    }

    public byte[] getOfflineKey() {
        return offlineKey;
    }

    public void setOfflineKey(byte[] offlineKey) {
        this.offlineKey = offlineKey;
    }

    public String getRegisteredURL() {
        return registeredURL;
    }

    public void setRegisteredURL(String registeredURL) {
        this.registeredURL = registeredURL;
    }

    public int compareTo(Object o) {
        if (o==null) return -1;
        return this.name.compareTo(((WiKIDDomain)o).getName());
    }
}
