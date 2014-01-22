package com.obs.mobile_tablet.datamodel.reporting;

import java.util.ArrayList;
import java.util.Collection;

import java.util.Arrays;
import java.util.List;

/**
 * Created by david on 12/27/13.
 */
public class TransactionGroup {
    public String id;
    public String txId;
    public String code;
    public String name;
    public String type;
    private List<String> _typeCodes;

    public void setTypeCodes(String typeCodes) {
        _typeCodes = Arrays.asList(typeCodes.split(","));
    }

    public Collection<String> getTypeCodesAsList() {
        return this._typeCodes;
    }
}
