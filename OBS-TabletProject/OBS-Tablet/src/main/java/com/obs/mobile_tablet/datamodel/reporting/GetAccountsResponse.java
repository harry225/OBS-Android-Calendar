package com.obs.mobile_tablet.datamodel.reporting;

import java.util.ArrayList;

public class GetAccountsResponse {

    private ArrayList<AccountObj> accounts;
    private ArrayList<AccountTypeObj> accountTypes;

    public ArrayList<AccountObj> getAccounts() {
        return accounts;
    }

    public ArrayList<AccountTypeObj> getAccountTypes() {
        return accountTypes;
    }

    public void setAccounts(ArrayList<AccountObj> accounts) {
        this.accounts = accounts;
    }

    public void setAccountTypes(ArrayList<AccountTypeObj> accountTypes) {
        this.accountTypes = accountTypes;
    }
}
