package com.obs.mobile_tablet.rest;

import com.obs.mobile_tablet.datamodel.setup.ApiSession;
import com.obs.mobile_tablet.utils.Logger;
import com.obs.mobile_tablet.wikid_utils.Device;

import org.springframework.http.HttpHeaders;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by david on 1/2/14.
 */
public class AuthHelper {

    private String _cookie;
    private ArrayList<String> _cookies;
    private ArrayList<String> _nonce;
    private String _globalKey = "oogabooga";
    private Integer _offset;
    private String _accessToken;
    private String _authToken;

    public AuthHelper() {
        _nonce = new ArrayList<String>();
        _cookies = new ArrayList<String>();

    }

    public void processApiSession(ApiSession apiSession) {
        _offset = apiSession.getOffset();
        _accessToken = apiSession.getAccessToken();
        _authToken = apiSession.getAuthToken();

    }

    public HttpHeaders getAuthHeader(String path, String httpMethod) throws UnsupportedEncodingException {

        HttpHeaders requestHeaders = new HttpHeaders();

        requestHeaders.set("Content-Type", "application/json");
        requestHeaders.set("Accept", "application/json");

        if (_authToken!=null) {
            requestHeaders.set("X-AUTH-TOKEN",_authToken);
        }

        if (_nonce.isEmpty()){
            return requestHeaders;
        }

        String hmacKey;
        if (_offset == null) {
            hmacKey = _globalKey;
        }else {
            String udid = Device.deviceUuid();

            hmacKey = udid.substring(_offset,_offset+16);
            Logger.debug("hmacKey: " + hmacKey + " \nlenth:" + hmacKey.length());
        }


        String nonce = getOneNonce();

        byte[] keyToUse = hmacKey.getBytes("UTF-8");
        String authHeader = "uri="+path+"&method="+httpMethod+"&nonce="+nonce;

        String urlEncodedQueryString = URLEncoder.encode(authHeader, "UTF-8");

        String signature = hmacSha1(keyToUse, urlEncodedQueryString);

        requestHeaders.set("Content-MD5", signature);
        requestHeaders.set("Authorization", nonce);



        return requestHeaders;
    }

    private String hmacSha1(byte[] key, String value) {
        try {
            if (key.length < 1){
                return null;
            }

            // Get an hmac_sha1 key from the raw key bytes
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");

            // Get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);

            // Compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(value.getBytes());

            // Convert raw bytes to Hex
            /*
            byte[] hexBytes = new Hex().encode(rawHmac);

            //  Covert array of Hex bytes to a String
            return new String(hexBytes, "UTF-8");
            */
            return "derp";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private String getOneNonce(){
        String aNonce = _nonce.get(0);
        _nonce.remove(0);
        return aNonce;
    }




}
