package com.obs.mobile_tablet.datamodel.reporting;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by david on 12/27/13.
 */
public class AccountTotalObj {
    public String displayType;
    public int accountCount;
    public ArrayList<GraphObj> graphs;
    public ArrayList<BalanceObj> balanceTotals;

    public AccountTotalObj() {
    }
}
