package com.obs.mobile_tablet.facade;

import com.obs.mobile_tablet.rest.Callback;
import com.obs.mobile_tablet.rest.RestClient;
import com.obs.mobile_tablet.datamodel.authentication.AuthObj;
import javax.inject.Inject;

/**
 * Created by david on 12/18/13.
 */
public class APIWikidCommandObjFacade {


    @Inject
    RestClient restClient;

    public Boolean isSetup () {
        return true;
    }

    public void reset () {

    }

    public void getInfo(final Callback callback){
        restClient.get("info", callback);
    }

    public void authenticate(AuthObj authObj, Callback callback){

    }
/*
    public void createRegistration (String UserName, String companyCode, String productKey, success:(OBSWikidSuccess)success failure:(OBSWikidFailure)failure;

    public void registerPin:(NSString *)pin tokenName:(NSString *)tokenName success:(OBSWikidSuccess)success failure:(OBSWikidFailure)failure;

    public void verifyIdentityWithAnswerOne:(NSString *)answerOne answerTwo:(NSString *)answerTwo success:(OBSWikidSuccess)success failure:(OBSWikidFailure)failure;

    public void deleteRegistrationWithSuccess:(OBSWikidSuccess)success failure:(OBSWikidFailure)failure;

    public void getPasscodeForPin:(NSString *)pin success:(OBSWikidPasscode)success failure:(OBSWikidFailure)failure;

    public void getSecondaryAuthPasscodeForPin:(NSString *)pin success:(WikidHTTPGetPasscodeSuccess)success failure:(OBSWikidFailure)failure;
    */

}
