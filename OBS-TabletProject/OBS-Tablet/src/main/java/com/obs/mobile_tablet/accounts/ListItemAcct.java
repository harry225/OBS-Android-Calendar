package com.obs.mobile_tablet.accounts;


import com.obs.mobile_tablet.datamodel.reporting.AccountObj;
import com.obs.mobile_tablet.datamodel.reporting.BalanceObj;

import java.util.ArrayList;
import java.util.Collection;

public class ListItemAcct {

    public AccountObj accountObj;
    public String name;
    public ArrayList<String> balances = new ArrayList<String>();
    public Boolean dataReported = false;

    public ListItemAcct(AccountObj acct) {
        accountObj = acct;
        name = acct.displayName;
        Collection<BalanceObj> acctBalances = acct.balances;

        //fill the balances with spaces:
        for (int i=0; i<5; i++) {
            balances.add(" ");
        }

        if (acctBalances != null) {
            //fill in balances with values:
            int currentIndex = 5 - acctBalances.size();
            for (BalanceObj balanceObj : acctBalances) {
                //Logger.debug(balanceObj.type+", "+balanceObj.amount+", "+balanceObj.displayText);
                balances.set(currentIndex, balanceObj.amount);
                currentIndex++;
            }
            dataReported = true;
        }
    }
}
