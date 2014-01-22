package com.obs.mobile_tablet.facade;

import android.app.ListActivity;
import android.util.Log;

import javax.inject.Inject;

import com.obs.mobile_tablet.datamodel.securemessages.GetSecureMessagesResponse;
import com.obs.mobile_tablet.datamodel.securemessages.SecureMessage;
import com.obs.mobile_tablet.datamodel.securemessages.SecureMessageAttachment;
import com.obs.mobile_tablet.datamodel.securemessages.SecureMessageReply;

import com.obs.mobile_tablet.rest.Callback;
import com.obs.mobile_tablet.rest.RestClient;

import com.obs.mobile_tablet.datamodel.reporting.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by hbray on 1/13/14.
 */
public class SecureMessageFacade extends OBSFacade{

    @Override
    public OBSFacade fake() {
        restClient.setFAKE_API(true);
        return this;
    }

    @Inject
    RestClient restClient;

    public void getMessages(Callback<GetSecureMessagesResponse> callback) {

        Log.d("HDB: SecureMessageFacade.getMessages", "restClient(messages)");
        restClient.get("messages", null, GetSecureMessagesResponse.class, callback);
    }

}