package com.obs.mobile_tablet.wikid_utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by IntelliJ IDEA.
 * User: stguitar
 * Date: 4/5/12
 * Time: 1:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class DigestUtils {

    public static byte[] generateFingerprint(byte[] data, String algorithm) {
        byte[] fingerprint = null;
        try {
            fingerprint = generateFingerprint(new ByteArrayInputStream(data), algorithm);
        } catch (IOException ex) {
//            LOG.error("Unexpected IO Error generating fingerprint: " + ex.getMessage(), ex);
            fingerprint = new byte[0];
        }
        return fingerprint;
    }

    public static byte[] generateFingerprint(InputStream data, String algorithm) throws IOException {
        byte[] fingerprint = null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            DigestInputStream dis = new DigestInputStream(data, md);
            int read = 0;
            byte[] buf = new byte[1024 * 32];
            while ((read = dis.read(buf)) != -1) {
            }
            fingerprint = md.digest();
        } catch (NoSuchAlgorithmException e) {
//            LOG.error("Could not generate fingerprint, algorithm not found: " + algorithm + ": " + e.getMessage(), e);
        } finally {
            if (data != null) {
                data.close();
            }
        }
        return fingerprint;
    }
}
