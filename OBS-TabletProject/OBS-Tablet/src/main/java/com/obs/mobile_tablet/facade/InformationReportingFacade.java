package com.obs.mobile_tablet.facade;

import android.app.ListActivity;

import javax.inject.Inject;

import com.obs.mobile_tablet.rest.Callback;
import com.obs.mobile_tablet.rest.RestClient;

import com.obs.mobile_tablet.datamodel.reporting.*;
import com.thoughtworks.xstream.converters.collections.MapConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by david on 12/27/13.
 */
public class InformationReportingFacade extends OBSFacade{

    @Override
    public InformationReportingFacade fake() {
        restClient.setFAKE_API(true);
        return this;
    }

    /** @param callback returns Collection<AccountObj> */
    public void getAccounts(Callback<GetAccountsResponse> callback) {
        restClient.get("accounts",null,GetAccountsResponse.class,callback);
//        restClient.get("accounts", callback);
    }

    /** @param callback onTaskCompleted returns AccountObj*/
    public void getAccount(AccountObj accountObj, final Callback<AccountObj> callback) {
        restClient.get(accountObj.accountUrl, null, GetAccountResponse.class, new Callback<GetAccountResponse>() {
            @Override
            public void onTaskCompleted(GetAccountResponse result) {
                callback.onTaskCompleted(result.getAccount());
            }

            @Override
            public void onTaskFailure(String Reason) {
                callback.onTaskFailure(Reason);
            }
        });
    }

    public void getTransactions(AccountObj accountObj, TransactionSearch transactionSearch, final Callback<TransactionsObj> callback) {

        restClient.get(String.format("%s/transactions", accountObj.accountUrl), transactionSearch.toMap(), GetTransactionsResponse.class, new Callback<GetTransactionsResponse>() {
            @Override
            public void onTaskCompleted(GetTransactionsResponse result) {
                callback.onTaskCompleted(result.getTransactionDetails());
            }

            @Override
            public void onTaskFailure(String Reason) {
                callback.onTaskFailure(Reason);
            }
        });
    }

    /** @param callback onTaskCompleted returns Collection<TransactionObj>*/
    public void getTransactions(AccountObj accountObj, final Callback<TransactionsObj> callback) {
        restClient.get(String.format("%s/transactions", accountObj.accountUrl), null, GetTransactionsResponse.class, new Callback<GetTransactionsResponse>() {
            @Override
            public void onTaskCompleted(GetTransactionsResponse result) {
                callback.onTaskCompleted(result.getTransactionDetails());
            }

            @Override
            public void onTaskFailure(String Reason) {
                callback.onTaskFailure(Reason);
            }
        });
    }

    /** @param callback onTaskCompleted returns Collection<AccountTotalObj>*/
    public void getAccountTotals(final Callback<GetAccountTotalsResponse> callback) {
        restClient.get("accountTotals", null, GetAccountTotalsResponse.class, new Callback<GetAccountTotalsResponse>() {

            @Override
            public void onTaskCompleted(GetAccountTotalsResponse result) {
                callback.onTaskCompleted(result);
            }

            @Override
            public void onTaskFailure(String Reason) {
                callback.onTaskFailure(Reason);
            }
        });
    }

    /** @param callback onTaskCompleted returns Collection<TransactionGroup>*/
    public void getTransactionGroups(final Callback<Collection<TransactionGroup>> callback) {
        restClient.get("transactionGroup", null, GetTransactionGroupsResponse.class, new Callback<GetTransactionGroupsResponse>() {
            @Override
            public void onTaskCompleted(GetTransactionGroupsResponse result) {
                callback.onTaskCompleted(result.getTransactionGroups());
            }

            @Override
            public void onTaskFailure(String Reason) {
                callback.onTaskFailure(Reason);
            }
        });
    }

    /** Request with application/pdf, loaded into a webview
    *   Not sure if this will stay here. */
    public void getCheckImage(TransactionObj transactionObj, Callback callback) {
        callback.onTaskCompleted("Image or PDF retrieved");
    }
}
