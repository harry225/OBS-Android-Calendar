package com.obs.mobile_tablet.accounts;

import com.obs.mobile_tablet.datamodel.reporting.BalanceObj;
import com.obs.mobile_tablet.utils.Logger;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by jaimiespetseris on 1/8/14.
 */
public class ListItemHeader {

    public String name;
    public ArrayList<String> balanceTypes = new ArrayList<String>();
    public Boolean hasColumnHeadings = false;

    public ListItemHeader(String newName) {
        name = newName;
        //fill the balanceTypes with spaces:
        for (int i=0; i<5; i++) {
            balanceTypes.add("");
        }
    }

    public void setBalanceTypes(Collection<BalanceObj> acctBalances) {
        if (acctBalances != null) {
             //fill in balanceTypes with values:
            int currentIndex = 5 - acctBalances.size();
            for (BalanceObj balanceObj : acctBalances) {
                balanceTypes.set(currentIndex, balanceObj.displayText);
                currentIndex++;
            }
            hasColumnHeadings = true;
        }
    }
}
