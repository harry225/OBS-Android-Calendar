package com.obs.mobile_tablet.wikid_utils;

import android.content.Context;
import android.util.Log;

import com.obs.mobile_tablet.datamodel.config.Constants;
import com.obs.mobile_tablet.utils.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
//import org.spongycastle.util.encoders.Hex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;

/**
 * Created by IntelliJ IDEA.
 * User: stguitar
 * Date: 4/7/12
 * Time: 10:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class WikidRegistration {

    @Inject
    Context context;

    public enum OBSRequest{
        INFO,
        SESSIONSETUP,
        SESSIONDELETE,
        TOKENSETUP,
        TOKENDELETE,
        VERIFYID;

        String path(){
            switch(this) {
                case SESSIONSETUP: return "setup/session";
                case SESSIONDELETE: return "setup/session";
                case TOKENSETUP: return "setup/tokenregistration";
                case TOKENDELETE: return "setup/tokenregistration";
                case VERIFYID: return "setup/verifyid";
                default: return "info";
            }
        }
        String uri(){
            switch(this) {
                case SESSIONSETUP: return "session";
                case SESSIONDELETE: return "session";
                case TOKENSETUP: return "tokenregistration";
                case TOKENDELETE: return "tokenregistration";
                case VERIFYID: return "verifyid";
                default: return "info";
            }
        }
    }

    private String _url;

    public OBSRequest getObsRequest() {
        return _obsRequest;
    }

    private OBSRequest _obsRequest;


    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    private void setAuthHeader(HttpGet get) {
        get.setHeader("Content-Type", "application/json");
        String httpMethod = get.getMethod();
        String httpPath = _obsRequest.uri();

    }

    private void setAuthHeader(HttpPost post) throws UnsupportedEncodingException {
        ObsDetails obsDetails = ObsDetails.getInstance();

        if (obsDetails.getCookie() != null){
            post.setHeader("Cookie", obsDetails.getCookie());
        }
        post.setHeader("Content-Type", "application/json");
        post.setHeader("Accept", "application/json");
//        post.setHeader("Accept", "text/plain");


        String httpMethod = post.getMethod();
        String uri = _obsRequest.uri();
        String nonce = obsDetails.getNonce();

        String hmacKey;
        if (_obsRequest == OBSRequest.SESSIONSETUP ||
                _obsRequest == OBSRequest.TOKENDELETE) {
            //TODO: Replace with key from file
            hmacKey = "oogabooga";
        }else {
            String udid = Device.deviceUuid();
            int offset = obsDetails.getOffset();
            hmacKey = udid.substring(offset,offset+16);
            Logger.debug("hmacKey" + hmacKey + " \nlenth:" + hmacKey.length());
        }

        byte[] keyToUse = hmacKey.getBytes("UTF-8");
        String authHeader = "uri="+uri+"&method="+httpMethod+"&nonce="+nonce;

        String urlEncodedQueryString = URLEncoder.encode(authHeader, "UTF-8");

        String signature = hmacSha1(keyToUse, urlEncodedQueryString);

        post.setHeader("Content-MD5", signature);
        post.setHeader("Authorization", nonce);


    }

    private String hmacSha1(byte[] key, String value) {
//        try {
            if (key.length < 1){
                return null;
            } else {
                return "blahblah";
            }


            /*
            // Get an hmac_sha1 key from the raw key bytes
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");

            // Get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);

            // Compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(value.getBytes());

            // Convert raw bytes to Hex

            byte[] hexBytes = new Hex().encode(rawHmac);

            //  Covert array of Hex bytes to a String
            return new String(hexBytes, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        */
    }


//    private void setAuth


    private Map<String, String> params;

    public WikidRegistration(Map<String, String> params){
        this.params = params;
    }

    public WikidRegistration(OBSRequest request) {
        _obsRequest = request;
        if (request == OBSRequest.INFO){
            this.setUrlForInfoRequest();
        }else {
            this.setUrlWithPath(request.path());
        }

    }

    public WikidRegistration(){

    }

    public String getUrl() {
        if (_url == null) {

            String hostIP = "https://mfcqa.onlinebankingsolutions.com";
            String apiVersion = "v1";
            setUrlWithPath(_obsRequest.path());
            Logger.debug("getUrl returning " + _url);

            return _url;
        }else {
            Logger.debug("getUrl returning " + _url);

            return _url;
        }
    }

    private void setUrlForInfoRequest(){
        String hostIP = "https://mfcqa.onlinebankingsolutions.com";
        setUrl(hostIP+"/api/info");

    }

    private void setUrlWithPath(String path) {
        String hostIP = "https://mfcqa.onlinebankingsolutions.com";
        String apiVersion = "v1";

        setUrl(hostIP+"/api/"+apiVersion+"/"+path);
    }

    private void setUrl(String url) {
        _url = url;
    }


    public JSONObject postData(){
        JSONObject holder = new JSONObject();
        JSONObject result = new JSONObject();
        StringEntity se = null;
        ObsHttpClient obsHttpClient = ObsHttpClient.getInstance();

//        HttpClient httpclient = new DefaultHttpClient();

        // Prepare a request object
        String HostIP = this.getUrl();
        Log.w("WikidRegistration","HostIP: "+HostIP);


        HttpPost httppost = new HttpPost(HostIP);
        try {
            setAuthHeader(httppost);
        } catch (UnsupportedEncodingException e) {
            Log.e("WikidRegistration", "Signing threw exception: " + e);
            e.printStackTrace();
        }

        Log.i("WikidRegistration","URL: "+httppost.getURI());
        Log.i("WikidRegistration","Params: "+httppost.getParams().toString());
        Log.i("WikidRegistration","Headers: "+httppost.getAllHeaders().toString());

//        HttpPost httppost = new HttpPost("https://mfc.onlinebankingsolutions.com/jsonapi");
/*
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        Iterator it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            if (pairs != null){
                nameValuePairs.add(new BasicNameValuePair(pairs.getKey().toString(), pairs.getValue().toString()));
            }
        }
        try{
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException uee){
            Log.e("WikidRegistration", "uee thrown! " + uee.getMessage());
        }
*/

        //Set JSON Entity

        try {
            holder = new JSONObject(getParams());
            se = new StringEntity(holder.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (se != null){
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_ENCODING, "application/json"));
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httppost.setEntity(se);
        }

//        HttpParams postParams = new HttpP
//        httppost.setParams();
        // Execute the request

        HttpResponse response;
        try {

            response = obsHttpClient.execute(httppost,obsHttpClient.getmHttpContext());
            Log.i("WikidResistration","Response was"+response.toString());

            // Get hold of the response entity
            HttpEntity entity = response.getEntity();
            // If the response does not enclose an entity, there is no need
            // to worry about connection release
            if (entity != null) {
                // A Simple JSON Response Read
                InputStream instream = entity.getContent();
                result = new JSONObject(convertStreamToString(instream));
                instream.close();
            }
        } catch (Exception e){
            Log.e("WikidRegistration", "POST Request Threw Exception: " + e);
        }
        return result;
    }

    public JSONObject getData(){
        JSONObject result = new JSONObject();
        ObsHttpClient obsHttpClient = ObsHttpClient.getInstance();

        // Prepare a request object
        String HostIP = this.getUrl();
        Log.w("WikidRegistration","HostIP: "+HostIP);


//        HttpPost httppost = new HttpPost(HostIP);
        HttpGet httpGet = new HttpGet(HostIP);

        this.setAuthHeader(httpGet);
        Log.i("WikidRegistration","URL: "+httpGet.getURI());
        Log.i("WikidRegistration","Params: "+httpGet.getParams().toString());
        Log.i("WikidRegistration","Headers: "+httpGet.getAllHeaders().toString());

//        HttpPost httppost = new HttpPost("https://mfc.onlinebankingsolutions.com/jsonapi");
//        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//        Iterator it = params.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry pairs = (Map.Entry)it.next();
//            nameValuePairs.add(new BasicNameValuePair(pairs.getKey().toString(), pairs.getValue().toString()));
//        }
//        try{
//            httpGet.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//        } catch (UnsupportedEncodingException uee){
//            Log.e("WikidRegistration", "uee thrown! " + uee.getMessage());
//        }

        // Execute the request
        HttpResponse response;
        try {

//            response = obsHttpClient.execute(httpGet);
            response = obsHttpClient.execute(httpGet,obsHttpClient.getmHttpContext());

//            ((DefaultHttpClient) obsHttpClient).getCookieStore().getCookies();

            Log.i("WikidResistration","Response was"+response.toString());

            // Get hold of the response entity
            HttpEntity entity = response.getEntity();
            // If the response does not enclose an entity, there is no need
            // to worry about connection release
            if (entity != null) {
                // A Simple JSON Response Read
                InputStream instream = entity.getContent();
                result = new JSONObject(convertStreamToString(instream));
                instream.close();
            }

        } catch (Exception e){
            Log.e("WikidRegistration", "POST Request Threw Exception: " + e);
        }
        return result;
    }



        private String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
        }

    private static JSONObject getJsonObjectFromMap(Map params) throws JSONException {

        //all the passed parameters from the post request
        //iterator used to loop through all the parameters
        //passed in the post request
        Iterator iter = params.entrySet().iterator();

        //Stores JSON
        JSONObject holder = new JSONObject();

        //using the earlier example your first entry would get email
        //and the inner while would get the value which would be 'foo@bar.com'
        //{ fan: { email : 'foo@bar.com' } }

        //While there is another entry
        while (iter.hasNext())
        {
            //gets an entry in the params
            Map.Entry pairs = (Map.Entry)iter.next();

            //creates a key for Map
            String key = (String)pairs.getKey();

            //Create a new map
            Map m = (Map)pairs.getValue();

            //object for storing Json
            JSONObject data = new JSONObject();

            //gets the value
            Iterator iter2 = m.entrySet().iterator();
            while (iter2.hasNext())
            {
                Map.Entry pairs2 = (Map.Entry)iter2.next();
                data.put((String)pairs2.getKey(), (String)pairs2.getValue());
            }

            //puts email and 'foo@bar.com'  together in map
            holder.put(key, data);
        }
        return holder;
    }
}
class ObsHttpClient extends DefaultHttpClient {


    private static ObsHttpClient ref;

    public BasicHttpContext getmHttpContext() {
        return _mHttpContext;
    }

    private BasicHttpContext _mHttpContext;

    public ObsHttpClient(ClientConnectionManager conman, HttpParams params) {
        super(conman, params);
    }

    public ObsHttpClient() {
        super();
        DefaultHttpClient mHttpClient = new DefaultHttpClient();
        BasicHttpContext mHttpContext = new BasicHttpContext();
        CookieStore mCookieStore = new BasicCookieStore();
        mHttpContext.setAttribute(ClientContext.COOKIE_STORE, mCookieStore);
        _mHttpContext = mHttpContext;
        this.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler
                (0, false));
    }

    public static synchronized ObsHttpClient getInstance(){
        if(ref == null){
            ref = new ObsHttpClient();
        }
        return ref;
    }
}
