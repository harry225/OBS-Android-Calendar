package com.obs.mobile_tablet.facade;

import com.obs.mobile_tablet.rest.RestClient;
import com.obs.mobile_tablet.rest.Callback;
import com.obs.mobile_tablet.wikid_utils.WikidAsyncHelper;
import com.obs.mobile_tablet.wikid_utils.WikidRegistration;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by david on 12/18/13.
 */
public class AuthenticationFacade extends OBSFacade{

    @Override
    public OBSFacade fake() {
        restClient.setFAKE_API(true);
        return this;
    }

    public void getInfo(final Callback callback){
        restClient.get("info", callback);

    }
}
