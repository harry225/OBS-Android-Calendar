package com.obs.mobile_tablet.facade;

import android.content.Context;

import com.obs.mobile_tablet.OBSApplication;
import com.obs.mobile_tablet.datamodel.authentication.AuthObj;
import com.obs.mobile_tablet.datamodel.setup.ApiSession;
import com.obs.mobile_tablet.rest.Callback;
import com.obs.mobile_tablet.rest.RestClient;
import com.obs.mobile_tablet.utils.ForApplication;
import com.obs.mobile_tablet.utils.Logger;
import com.obs.mobile_tablet.wikid_utils.ObsDetails;
import com.obs.mobile_tablet.wikid_utils.WikidAsyncHelper;
import com.obs.mobile_tablet.wikid_utils.WikidRegistration;
import com.wikidsystems.android.client.TokenConfiguration;
import com.wikidsystems.android.client.WiKIDDomain;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by david on 12/18/13.
 */
public class SetupFacade extends OBSFacade{


    @Inject
    @ForApplication
    Context context;

    @Inject
    @ForApplication
    OBSApplication application;

    public TokenConfiguration tokenConfiguration;
    public static final String TOKEN_FILE_NAME = "WiKIDToken.wkd";
    private Boolean isSetup;
    private String pinRegex;
    private String pinDescription;


    @Override
    public OBSFacade fake() {
        restClient.setFAKE_API(true);
        return this;
    }

    public SetupFacade() {
        Logger.debug("creating setup facade.");
    }

    public void init() {
        isSetup = false;
        if (tokenConfiguration == null) {
            // see if a configuration file exists. if so upgrade it if necessary otherwise create one.
            String[] fList = context.fileList();
            boolean configExists = false;

            for (String fname : fList) {
                configExists = configExists || TOKEN_FILE_NAME.equals(fname);
            }

            try {
                tokenConfiguration = (new TokenConfiguration()).load(context.getFileStreamPath(TOKEN_FILE_NAME), "lucky13".toCharArray());
                if (!configExists || tokenConfiguration.getDomains().size() == 0) {
                    TokenConfiguration.save(tokenConfiguration, context.getFileStreamPath(TOKEN_FILE_NAME), "lucky13".toCharArray());

                } else {
                    isSetup = true;
                    Logger.debug("Unable to create token Configuration");
                }
            } catch (Exception e) {
                Logger.debug("HomeMenu" + "Exception thrown: " + e.getMessage());
            }

        } else {
            Logger.debug("Token already exists");
        }
    }

    public void init(Context testContext) {
        context = testContext;
        init();
    }

    public void init(TokenConfiguration testTokenConfiguration) {
        tokenConfiguration = testTokenConfiguration;
    }

    public boolean getIsSetup() {
        return isSetup;
    }

    public String getPinRegex() {
        return pinRegex;
    }

    public String getPinDescription() {
        return pinDescription;
    }


    public void getInfo(final Callback<String> callback){
        restClient.get("info", callback);

    }

    public void authenticate(final String company, final String name, final String authProductKey, final Callback<String> callback) {
        final AuthObj authObj = new AuthObj(company, name, authProductKey, "android", "4.0", "1.0", "tablet", "SOMESTATICUUID");

        restClient.get("info", new Callback<String>() {
            @Override
            public void onTaskCompleted(String result) {
                restClient.postLogin("setup/session", authObj, new Callback<ApiSession>() {
                    @Override
                    public void onTaskCompleted(ApiSession data) {
                        try {
                            pinRegex = data.getPinFormat();
                            pinDescription = data.getPinDescription();
//                            if () {
                            WikidRegistration wr = new WikidRegistration();
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("action", "initialize");
                            params.put("serverCode", "172021010024");
                            wr.setParams(params);

                            ObsDetails obsDetails = ObsDetails.getInstance();
                            obsDetails.setUsername(name);
                            obsDetails.setCompanyCode(company);
                            obsDetails.setAccessToken(data.getAccessToken());

                            WikidAsyncHelper wah = new WikidAsyncHelper(tokenConfiguration, callback);
                            wah.application = application;
                            wah.execute(wr);


//                            } else if ("DELETE_REGISTRATION".equalsIgnoreCase(data.get("type").toString())) {
                            //TODO: Update this to use the new delete token api that does not require a password

//                issueAuthenticationCheck();
//                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.onTaskFailure(e.getLocalizedMessage());
                        }
                    }

                    @Override
                    public void onTaskFailure(String Reason) {
                        callback.onTaskFailure(Reason != null ? Reason : "Unknown Error");
                    }
                });

            }

            @Override
            public void onTaskFailure(String Reason) {
                callback.onTaskFailure(Reason != null ? Reason : "Unknown Error");
            }
        });

    }

    /**
     * Pretty Much Never Used
     */
    public void logout(final Callback<String> callback) {

        callback.onTaskCompleted("Success");
    }

    /**
     * Registers the pin with the token, and the token with the registration
     */
    public void createRegistration(final String pin, final Callback<String> callback) {
        WiKIDDomain domain = (WiKIDDomain) tokenConfiguration.getDomains().get(0);
        ObsDetails details = ObsDetails.getInstance();
        details.setDeviceId(domain.getDeviceID());
        details.setTokenName("Android Test 1");
        try {
            WikidRegistration wr = new WikidRegistration();
            Map<String, String> params = new HashMap<String, String>();
            params.put("action", "register");
            params.put("pin", pin);
            wr.setParams(params);
            WikidAsyncHelper wah = new WikidAsyncHelper(tokenConfiguration, new Callback<String>() {

                @Override
                public void onTaskCompleted(String response) {
                    tokenConfiguration.save();

                    ObsDetails obsDetails = ObsDetails.getInstance();
                    obsDetails.setRegistrationCode(response);

                    Map<String, String> params = new HashMap<String, String>();

                    params.put("registrationCode", obsDetails.getRegistrationCode());
                    params.put("wikidDomainDeviceId", String.valueOf(obsDetails.getDeviceId()));
                    params.put("tokenName", obsDetails.getTokenName());
                    params.put("accessToken", obsDetails.getAccessToken());

//                    WikidRegistration wr = new WikidRegistration(WikidRegistration.OBSRequest.TOKENSETUP);
//                    wr.setParams(params);
                    restClient.post("setup/tokenregistration", params, LinkedHashMap.class, new Callback() {
                        @Override
                        public void onTaskCompleted(Object result) {
                            callback.onTaskCompleted("Success");
                        }

                        @Override
                        public void onTaskFailure(String Reason) {
                            callback.onTaskFailure(Reason);
                        }
                    });

                }

                @Override
                public void onTaskFailure(String Reason) {
                    callback.onTaskFailure(Reason);
                }
            });
            wah.execute(wr);
        } catch (Exception e) {
            e.printStackTrace();
            callback.onTaskFailure(e.getLocalizedMessage());

        }
    }

    /**
     * This is used to delete a token on the server. Used for forgotten pins, and restarted registrations.
     * TODO: Add in call to delete token from server
     */
    public void deleteRegistration(final Callback<String> callback) {
        getInfo(new Callback<String>() {
            @Override
            public void onTaskCompleted(String result) {
                ObsDetails obsDetails = ObsDetails.getInstance();
                restClient.delete("setup/tokenregistration"+"/company/"+obsDetails.getCompanyCode()+"/user/"+obsDetails.getUsername()+"/"+obsDetails.getDeviceId(), null, null, new Callback<String>() {
                    @Override
                    public void onTaskCompleted(String result) {
                        tokenConfiguration.deleteAllDomains();
                        callback.onTaskCompleted(result != null ? result : "Success, no reason given");
                    }

                    @Override
                    public void onTaskFailure(String Reason) {
                        tokenConfiguration.deleteAllDomains();
                        callback.onTaskFailure(Reason != null ? Reason : "Failure, no reason given");
                    }
                });
            }

            @Override
            public void onTaskFailure(String Reason) {
                callback.onTaskFailure(Reason != null ? Reason : "Failure, no reason given");
            }
        });

    }

    /**
     * This is where the users ID is verified via security questions
     */
    public void registerTokenAndObs(String questionOne, String questionTwo, final Callback<String> callback) {


        //action=registerTokenAndObs&bankId=%@&companyCode=%@&username=%@&registrationCode=%@&wikidDomainDeviceId=%@&tokenName=%@&questionOneKey=%@&answerOne=%@&questionTwoKey=%@&answerTwo=%@
        Map<String, String> params = new HashMap<String, String>();
        ObsDetails obsDetails = ObsDetails.getInstance();
        params.put("registrationCode", obsDetails.getRegistrationCode());
        params.put("wikidDomainDeviceId", String.valueOf(obsDetails.getDeviceId()));
        params.put("questionOneKey", "mothersFirstName"); //TODO: make configurable, possibly from server?
        params.put("questionTwoKey", "townOfBirth"); //TODO: make configurable
        params.put("answerOne", questionOne);
        params.put("answerTwo", questionTwo);
        params.put("tokenName", obsDetails.getTokenName());

        restClient.post("setup/verifyid", params, LinkedHashMap.class, new Callback<String>() {
            @Override
            public void onTaskCompleted(String result) {
                tokenConfiguration.save();
                callback.onTaskCompleted(result != null ? result : "Success, no reason given");
            }

            @Override
            public void onTaskFailure(String Reason) {
                callback.onTaskFailure(Reason != null ? Reason : "Failure, no reason given");

            }
        });
//            ObsAsyncHelper oah = new ObsAsyncHelper(IdentityVerification.this, "Verifying Identity");
//            oah.execute(wr);


    }

    public void login(final String pin, final Callback callback) {
        restClient.get("info", new Callback() {
            @Override
            public void onTaskCompleted(Object result) {
                WikidRegistration wr = new WikidRegistration();
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "getOneTimePasscode");
                params.put("pin", pin);
                wr.setParams(params);
                WikidAsyncHelper wah = new WikidAsyncHelper(tokenConfiguration, new Callback<String>() {
                    @Override
                    public void onTaskCompleted(String result) {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("pin", result);
                        params.put("username", ObsDetails.getInstance().getUsername());
                        params.put("company", ObsDetails.getInstance().getCompanyCode());
                        params.put("deviceUuid", "SOMESTATICUUID");

                        restClient.postLogin("session", params, new Callback<ApiSession>() {
                            @Override
                            public void onTaskCompleted(ApiSession apiSession) {
                                ObsDetails obsDetails = ObsDetails.getInstance();

                                obsDetails.setAccessToken(apiSession.getAccessToken());
                                callback.onTaskCompleted("Login Successful");
                                Logger.debug("Login Successful");
                            }

                            @Override
                            public void onTaskFailure(String Reason) {
                                callback.onTaskFailure(Reason != null ? Reason : "Unknown Error");

                            }
                        });

                    }

                    @Override
                    public void onTaskFailure(String Reason) {
                        callback.onTaskFailure(Reason != null ? Reason : "Unknown Error");

                    }
                });
                wah.execute(wr);
            }

            @Override
            public void onTaskFailure(String Reason) {
                callback.onTaskFailure(Reason != null ? Reason : "Unknown Error");
            }
        });

    }

    public void testAlwaysFailsRequest(final Callback callback) {
        callback.onTaskFailure("Failure");

    }


}
