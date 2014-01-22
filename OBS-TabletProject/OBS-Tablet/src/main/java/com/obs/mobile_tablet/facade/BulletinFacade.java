package com.obs.mobile_tablet.facade;

import android.app.ListActivity;
import android.util.Log;

import javax.inject.Inject;

import com.obs.mobile_tablet.datamodel.bulletins.GetBulletinsResponse;
import com.obs.mobile_tablet.datamodel.bulletins.BulletinAttachment;
import com.obs.mobile_tablet.datamodel.bulletins.Bulletin;

import com.obs.mobile_tablet.rest.Callback;
import com.obs.mobile_tablet.rest.RestClient;

import com.obs.mobile_tablet.datamodel.reporting.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by hbray on 1/13/14.
 */
public class BulletinFacade extends OBSFacade{

    @Override
    public OBSFacade fake() {
        restClient.setFAKE_API(true);
        return this;
    }

    @Inject
    RestClient restClient;

    public void getBulletins(Callback<GetBulletinsResponse> callback) {

        Log.d("HDB: BulletinFacade.getBulletins", "restClient(Bulletins)");
        restClient.get("bulletins", null, GetBulletinsResponse.class, callback);
    }

}
