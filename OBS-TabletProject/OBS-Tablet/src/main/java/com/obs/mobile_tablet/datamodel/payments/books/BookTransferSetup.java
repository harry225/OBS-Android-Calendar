package com.obs.mobile_tablet.datamodel.payments.books;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by david on 1/13/14.
 */
public class BookTransferSetup {

    private ArrayList<TransferableAccount> _toAccounts;
    private ArrayList<TransferableAccount> _fromAccounts;
    private Date _minPaymentDate;
    private Date _maxPaymentDate;

    private static final SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

    public BookTransferSetup() {
    }

    public BookTransferSetup(Map<String, Map<String, String>> map) {

        Map<String, String> fromAccountsMap = map.get("fromAccounts");
        Map<String, String> toAccountsMap = map.get("toAccounts");
        Map<String, String> balancesMap = map.get("balances");
        Map<String, String> currenciesMap = map.get("currencies");

        _fromAccounts = new ArrayList<TransferableAccount>(fromAccountsMap.keySet().size());
        _toAccounts = new ArrayList<TransferableAccount>(toAccountsMap.keySet().size());

        for (String key : fromAccountsMap.keySet()) {
            TransferableAccount ta = new TransferableAccount(key,
                                        fromAccountsMap.get(key),
                                            balancesMap.get(key),
                                          currenciesMap.get(key));
            _fromAccounts.add(ta);
        }

        for (String key : toAccountsMap.keySet()) {
            TransferableAccount ta = new TransferableAccount(key,
                    toAccountsMap.get(key),
                    balancesMap.get(key),
                    currenciesMap.get(key));
            _toAccounts.add(ta);
        }

        try {
            _minPaymentDate = df.parse(String.valueOf(map.get("minPaymentDate")));
            _maxPaymentDate = df.parse(String.valueOf(map.get("maxPaymentDate")));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<TransferableAccount> getToAccounts() {
        return _toAccounts;
    }

    public ArrayList<TransferableAccount> getFromAccounts() {
        return _fromAccounts;
    }

    public Date getMinPaymentDate() {
        return _minPaymentDate;
    }

    public Date getMaxPaymentDate() {
        return _maxPaymentDate;
    }
}
