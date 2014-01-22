package com.obs.mobile_tablet.datamodel.reporting;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by david on 12/27/13.
 */
public class TransactionsObj {
    private ArrayList<TransactionObj> transactions;
    private ArrayList<GraphObj> graphs;

    public ArrayList<TransactionObj> getTransactions() {
        return transactions;
    }

    public ArrayList<GraphObj> getGraphs() {
        return graphs;
    }
}