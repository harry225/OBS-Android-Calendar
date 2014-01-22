package com.obs.mobile_tablet.facade;

import com.obs.mobile_tablet.rest.RestClient;

import javax.inject.Inject;

/**
 * Created by david on 1/9/14.
 */
public abstract class OBSFacade {

    private Boolean FAKE_REQUESTS;

    @Inject
    RestClient restClient;

    public RestClient getRestClient() {
        return restClient;
    }

    public abstract OBSFacade fake();
}
