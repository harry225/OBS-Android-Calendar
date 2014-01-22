package com.obs.mobile_tablet.datamodel.reporting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by sheenobu on 1/5/14.
 */
public class GetAccountTotalsResponse {

    private ArrayList<AccountTotalObj> totals;

    public ArrayList<AccountTotalObj> getTotals() {
        return totals;
    }

    public GetAccountTotalsResponse() {
    }

    public void setTotals(ArrayList<AccountTotalObj> totals) {
        this.totals = totals;
    }
}
