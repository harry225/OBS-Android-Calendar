package com.obs.mobile_tablet.wikid_utils;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

//import com.obs.android.R;
import com.obs.mobile_tablet.OBSApplication;
import com.obs.mobile_tablet.rest.Callback;
import com.wikidsystems.android.client.TokenConfiguration;
import com.wikidsystems.android.client.WiKIDDomain;
import com.wikidsystems.android.client.wComms;

import java.util.Map;

import javax.inject.Inject;

/**
 * Created by IntelliJ IDEA.
 * User: stguitar
 * Date: 4/18/12
 * Time: 3:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class WikidAsyncHelper extends AsyncTask<WikidRegistration, Long, String> {

    @Inject
    public OBSApplication application;
    Callback<String> _callback;
    TokenConfiguration _tokenConfiguration;

    ProgressDialog dialog;
    WikidServiceHandler handler;
    Activity activity;
    String statusMessage;

    public WikidAsyncHelper(Activity activity, String statusMessage){
        this.handler = (WikidServiceHandler)activity;
        this.activity = activity;
        this.statusMessage = statusMessage;
    }

    public WikidAsyncHelper(TokenConfiguration tokenConfiguration, Callback<String> callback) {

        _callback = callback;
        _tokenConfiguration = tokenConfiguration;
    }


    @Override
    protected void onPreExecute() {
        Log.i("WikidAsyncHelper","PreExecute Message");
        if (dialog != null){
            dialog = ProgressDialog.show((Context)activity, "", statusMessage + "...", true);
        }
    }

    @Override
    protected String doInBackground(WikidRegistration... wikidRegistrations) {
        WikidRegistration wr = wikidRegistrations[0];
        Map<String, String> params = wr.getParams();
        try{
            if("initialize".equalsIgnoreCase(params.get("action").toString())){
                String serverCode = "172021010024";
//                String serverCode = (String) params.get("serverCode");
                Log.i("WikidAsyncHelper","ServerCode: "+serverCode);
                wComms wcomms = new wComms();

                WiKIDDomain domain = wcomms.pullConfig(serverCode, _tokenConfiguration);
                _tokenConfiguration.deleteAllDomains();
                _tokenConfiguration.addDomain(domain);
                Log.i("WikidAsyncHelper","domain: "+domain);
                return domain.getServerCode();
            } else if("register".equalsIgnoreCase(params.get("action").toString())){
                WiKIDDomain domain = (WiKIDDomain)_tokenConfiguration.getDomains().get(0);
                String pin = params.get("pin");
                return new wComms().setPIN(domain, pin, _tokenConfiguration);
            } else if("getOneTimePasscode".equalsIgnoreCase(params.get("action").toString())){
                String pin = params.get("pin");
                return new wComms().proc(pin.toCharArray(), _tokenConfiguration, (WiKIDDomain) _tokenConfiguration.getDomains().get(0));
            } else {
                return null;
            }
        } catch (Exception e){
            return null;
        }
//        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if(s == null){
            _callback.onTaskFailure("Wikid Failure");
        }else {
            _callback.onTaskCompleted(s);
        }
    }
}
