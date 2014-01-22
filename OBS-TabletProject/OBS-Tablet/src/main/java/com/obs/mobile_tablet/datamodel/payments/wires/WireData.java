package com.obs.mobile_tablet.datamodel.payments.wires;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Created by david on 1/17/14.
 */
public class WireData {
    /**
     * This object is only used to send user created payment information and send it to the server
     */

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="MM/dd/yyyy", timezone="GMT")
    private Date paymentDate;
    private String debitAccountId;
    private String referenceToBeneficiary;
    private String beneficiaryInformation;
    private String amount;
}
