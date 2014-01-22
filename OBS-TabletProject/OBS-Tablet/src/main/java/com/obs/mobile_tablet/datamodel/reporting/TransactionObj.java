package com.obs.mobile_tablet.datamodel.reporting;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * Created by david on 12/27/13.
 */
public class TransactionObj {

//    @JsonProperty("asOfDate")
//    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="MM/dd/yyyy", timezone="GMT")

    @JsonIgnore
    private Date rawDate;

    public String asOfDate;

    public String checkNumber;
    public String displayCheckNumber;
    public String displayText;
    public String creditAmount;
    public String debitAmount;
    public String balanceAmount;
    public String type;
    public String accountNumber;
    public String companyAccountId;
    public String intuitTransactionType;
    private boolean hasCheckImage;
    private boolean isCheck;

    private static final SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

    Collection<String> additionalText;
    String checkImageUrl;

    public TransactionObj() {
    }


    public Date getRawDate() {
        if (rawDate == null) {
            try {
                rawDate = df.parse(asOfDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return rawDate;
    }

    public boolean getHasCheckImage() {
        return hasCheckImage;
    }

    void setHasCheckImage(boolean hasCheckImage) {
        this.hasCheckImage = hasCheckImage;
    }

    boolean getIsCheck() {
        return isCheck;
    }

    void setIsCheck(boolean check) {
        isCheck = check;
    }

    public String getCheckImageUrl() {
        return checkImageUrl;
    }

    public void setCheckImageUrl(String checkImageUrl) {
        this.checkImageUrl = checkImageUrl;
    }

    public Collection<String> getAdditionalText() {
        return additionalText;
    }

    public void setAdditionalText(Collection<String> additionalText) {
        this.additionalText = additionalText;
    }
}
