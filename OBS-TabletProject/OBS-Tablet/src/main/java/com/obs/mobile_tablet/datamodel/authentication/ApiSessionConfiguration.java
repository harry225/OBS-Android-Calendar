package com.obs.mobile_tablet.datamodel.authentication;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: stevenguitar
 * Date: 9/5/13
 * Time: 2:47 PM
 * To change this template use File | Settings | File Templates.
 */
class ApiSessionConfiguration {
    String usersName;
    boolean hasAccountInformation;
    Map ach;
    Map wires;
    Map books;
    Map checkPositivePay;
    Map stops;
    Map messaging;
    Map payments;
    Map recon;
    Map marketing;
    Map calendar;

    boolean getHasAccountInformation() {
        return hasAccountInformation;
    }

    void setHasAccountInformation(boolean hasAccountInformation) {
        this.hasAccountInformation = hasAccountInformation;
    }
}
