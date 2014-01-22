package com.obs.mobile_tablet.datamodel.setup;

//import org.springframework.web.bind.annotation.RequestParam

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: stevenguitar
 * Date: 4/30/13
 * Time: 2:14 AM
 * To change this template use File | Settings | File Templates.
 */
class ApiWikidCommandObj implements Serializable {
    String registrationCode;
    String wikidDomainDeviceId;
    String tokenName;
    String questionOneKey;
    String answerOne;
    String questionTwoKey;
    String answerTwo;
    String accessToken;
}
